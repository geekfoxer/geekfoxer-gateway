package com.geekfoxer.gateway.server.netty;

import com.geekfoxer.gateway.common.ProxyUtils;
import com.geekfoxer.gateway.server.metrics.MetricsExporter;
import com.geekfoxer.gateway.server.netty.transmit.connection.ClientToProxyConnection;
import com.geekfoxer.gateway.server.netty.transmit.support.HostResolver;
import com.geekfoxer.gateway.server.netty.transmit.support.ServerGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author pizhihui
 * @date 2019-08-06
 */

public class HttpProxyServer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpProxyServer.class);

    private static final long TRAFFIC_SHAPING_CHECK_INTERVAL_MS = 250L;
    private static final String FALLBACK_PROXY_ALIAS = "tesla";

    private final ServerGroup serverGroup;
    private final HttpFiltersSourceAdapter filtersSource;
    private final MetricsExporter metricsExporter;
    private final boolean transparent;
    private final InetSocketAddress requestedAddress;
    private final HostResolver serverResolver;
    private final int maxInitialLineLength;
    private final int maxHeaderSize;
    private final int maxChunkSize;
    private final boolean allowRequestsToOriginServer;
    private final String proxyAlias;
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final Collection<ActivityTracker> activityTrackers =
            new ConcurrentLinkedQueue<ActivityTracker>();
    private final ChannelGroup allChannels =
            new DefaultChannelGroup("HTTP-Proxy-Server", GlobalEventExecutor.INSTANCE);
    private final Thread jvmShutdownHook = new Thread(new Runnable() {
        @Override
        public void run() {
            abort();
        }
    }, "Tesla-JVM-shutdown-hook");

    private volatile InetSocketAddress localAddress;
    private volatile InetSocketAddress boundAddress;
    private volatile int connectTimeout;
    private volatile int idleConnectionTimeout;
    private volatile GlobalTrafficShapingHandler globalTrafficShapingHandler;


    public static HttpProxyServerBootstrap bootstrap() {
        return new HttpProxyServerBootstrap();
    }


    public static HttpProxyServerBootstrap bootstrapFromFile(String path) {
        final File propsFile = new File(path);
        Properties props = new Properties();

        if (propsFile.isFile()) {
            try (InputStream is = new FileInputStream(propsFile)) {
                props.load(is);
            } catch (final IOException e) {
                LOG.warn("Could not load props file?", e);
            }
        }

        return new HttpProxyServerBootstrap(props);
    }


    public HttpProxyServer(ServerGroup serverGroup, InetSocketAddress requestedAddress,
                           HttpFiltersSourceAdapter filtersSource, MetricsExporter metricExporter, boolean transparent,
                           int idleConnectionTimeout, Collection<ActivityTracker> activityTrackers, int connectTimeout,
                           HostResolver serverResolver, long readThrottleBytesPerSecond,
                           long writeThrottleBytesPerSecond, InetSocketAddress localAddress, String proxyAlias,
                           int maxInitialLineLength, int maxHeaderSize, int maxChunkSize,
                           boolean allowRequestsToOriginServer) {
        this.serverGroup = serverGroup;
        this.requestedAddress = requestedAddress;
        this.filtersSource = filtersSource;
        this.metricsExporter = metricExporter;
        this.transparent = transparent;
        this.idleConnectionTimeout = idleConnectionTimeout;
        if (activityTrackers != null) {
            this.activityTrackers.addAll(activityTrackers);
        }
        this.connectTimeout = connectTimeout;
        this.serverResolver = serverResolver;

        if (writeThrottleBytesPerSecond > 0 || readThrottleBytesPerSecond > 0) {
            this.globalTrafficShapingHandler = createGlobalTrafficShapingHandler(
                    readThrottleBytesPerSecond, writeThrottleBytesPerSecond);
        } else {
            this.globalTrafficShapingHandler = null;
        }
        this.localAddress = localAddress;

        if (proxyAlias == null) {
            String hostname = ProxyUtils.getHostName();
            if (hostname == null) {
                hostname = FALLBACK_PROXY_ALIAS;
            }
            this.proxyAlias = hostname;
        } else {
            this.proxyAlias = proxyAlias;
        }
        this.maxInitialLineLength = maxInitialLineLength;
        this.maxHeaderSize = maxHeaderSize;
        this.maxChunkSize = maxChunkSize;
        this.allowRequestsToOriginServer = allowRequestsToOriginServer;
    }


    private GlobalTrafficShapingHandler createGlobalTrafficShapingHandler(
            long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond) {
        EventLoopGroup proxyToServerEventLoop = this.getProxyToServerWorkerFor();
        return new GlobalTrafficShapingHandler(proxyToServerEventLoop, writeThrottleBytesPerSecond,
                readThrottleBytesPerSecond, TRAFFIC_SHAPING_CHECK_INTERVAL_MS, Long.MAX_VALUE);
    }

    public boolean isTransparent() {
        return transparent;
    }


    public int getIdleConnectionTimeout() {
        return idleConnectionTimeout;
    }


    public void setIdleConnectionTimeout(int idleConnectionTimeout) {
        this.idleConnectionTimeout = idleConnectionTimeout;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }


    public void setConnectTimeout(int connectTimeoutMs) {
        this.connectTimeout = connectTimeoutMs;
    }

    public HostResolver getServerResolver() {
        return serverResolver;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }


    public InetSocketAddress getListenAddress() {
        return boundAddress;
    }


    public void setThrottle(long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond) {
        if (globalTrafficShapingHandler != null) {
            globalTrafficShapingHandler.configure(writeThrottleBytesPerSecond,
                    readThrottleBytesPerSecond);
        } else {
            if (readThrottleBytesPerSecond > 0 || writeThrottleBytesPerSecond > 0) {
                globalTrafficShapingHandler = createGlobalTrafficShapingHandler(readThrottleBytesPerSecond,
                        writeThrottleBytesPerSecond);
            }
        }
    }

    public long getReadThrottle() {
        return globalTrafficShapingHandler.getReadLimit();
    }

    public long getWriteThrottle() {
        return globalTrafficShapingHandler.getWriteLimit();
    }

    public int getMaxInitialLineLength() {
        return maxInitialLineLength;
    }

    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }

    public int getMaxChunkSize() {
        return maxChunkSize;
    }

    public boolean isAllowRequestsToOriginServer() {
        return allowRequestsToOriginServer;
    }


    public HttpProxyServerBootstrap clone() {
        return new HttpProxyServerBootstrap(serverGroup,
                new InetSocketAddress(requestedAddress.getAddress(),
                        requestedAddress.getPort() == 0 ? 0 : requestedAddress.getPort() + 1),
                filtersSource, transparent, idleConnectionTimeout, activityTrackers, connectTimeout,
                serverResolver,
                globalTrafficShapingHandler != null ? globalTrafficShapingHandler.getReadLimit() : 0,
                globalTrafficShapingHandler != null ? globalTrafficShapingHandler.getWriteLimit() : 0,
                localAddress, proxyAlias, maxInitialLineLength, maxHeaderSize, maxChunkSize,
                allowRequestsToOriginServer);
    }


    public void stop() {
        doStop(true);
    }


    public void abort() {
        doStop(false);
    }


    protected void doStop(boolean graceful) {
        if (stopped.compareAndSet(false, true)) {
            if (graceful) {
                LOG.info("Shutting down proxy server gracefully");
            } else {
                LOG.info("Shutting down proxy server immediately (non-graceful)");
            }
            closeAllChannels(graceful);
            serverGroup.unregisterProxyServer(this, graceful);
            try {
                Runtime.getRuntime().removeShutdownHook(jvmShutdownHook);
            } catch (IllegalStateException e) {
            }

            LOG.info("Done shutting down proxy server");
        }
    }


    public void registerChannel(Channel channel) {
        allChannels.add(channel);
    }


    protected void closeAllChannels(boolean graceful) {
        LOG.info("Closing all channels " + (graceful ? "(graceful)" : "(non-graceful)"));

        ChannelGroupFuture future = allChannels.close();
        if (graceful) {
            try {
                future.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                LOG.warn("Interrupted while waiting for channels to shut down gracefully.");
            }

            if (!future.isSuccess()) {
                for (ChannelFuture cf : future) {
                    if (!cf.isSuccess()) {
                        LOG.info("Unable to close channel.  Cause of failure for {} is {}", cf.channel(),
                                cf.cause());
                    }
                }
            }
        }
    }

    public HttpProxyServer start() {
        if (!serverGroup.isStopped()) {
            LOG.info("Starting proxy at address: " + this.requestedAddress);

            serverGroup.registerProxyServer(this);

            doStart();
        } else {
            throw new IllegalStateException(
                    "Attempted to start proxy, but proxy's server group is already stopped");
        }

        return this;
    }

    private void doStart() {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(serverGroup.getClientToProxyAcceptorPoolForTransport(), serverGroup.getClientToProxyWorkerPoolForTransport());

        ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                new ClientToProxyConnection(HttpProxyServer.this, ch.pipeline(), globalTrafficShapingHandler);
            };
        };

        serverBootstrap.channel(NioServerSocketChannel.class);

//        serverBootstrap.channelFactory(new ChannelFactory<ServerChannel>() {
//            @Override
//            public ServerChannel newChannel() {
//                return new NioServerSocketChannel();
//            }
//        });

        serverBootstrap.childHandler(initializer);

        ChannelFuture future = serverBootstrap
                .bind(requestedAddress)
                .addListener((ChannelFutureListener) future1 -> {
                    if (future1.isSuccess()) {
                        registerChannel(future1.channel());
                    }
                }).awaitUninterruptibly();

        Throwable cause = future.cause();
        if (cause != null) {
            throw new RuntimeException(cause);
        }

        this.boundAddress = ((InetSocketAddress) future.channel().localAddress());
        LOG.info("Proxy started at address: " + this.boundAddress);

        Runtime.getRuntime().addShutdownHook(jvmShutdownHook);
    }


    public HttpFiltersSourceAdapter getFiltersSource() {
        return filtersSource;
    }

    public MetricsExporter getMetricsExporter() {
        return metricsExporter;
    }


    public Collection<ActivityTracker> getActivityTrackers() {
        return activityTrackers;
    }

    public String getProxyAlias() {
        return proxyAlias;
    }


    public EventLoopGroup getProxyToServerWorkerFor() {
        return serverGroup.getProxyToServerWorkerPoolForTransport();
    }


}
