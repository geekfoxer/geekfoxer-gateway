package com.geekfoxer.gateway.server.netty;

import com.geekfoxer.gateway.server.metrics.MetricsExporter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
public class HttpFiltersSourceAdapter {

    public HttpFiltersAdapter filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx,
                                            MetricsExporter metricExporter) {
        return new HttpFiltersAdapter(originalRequest, ctx, metricExporter);
    }

    public int getMaximumRequestBufferSizeInBytes() {
        return 512 * 1024;
    }

    public int getMaximumResponseBufferSizeInBytes() {
        return 512 * 1024;
    }

}
