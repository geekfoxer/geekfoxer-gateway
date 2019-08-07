package com.geekfoxer.gateway.common;

import io.netty.channel.Channel;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
public class ChannelThreadLocal {

    public static final ThreadLocal<Channel> CHANNEL_THREAD_LOCAL = new ThreadLocal<Channel>();

    private ChannelThreadLocal() {
        // Utils class
    }

    public static void set(Channel channel) {
        CHANNEL_THREAD_LOCAL.set(channel);
    }

    public static void unset() {
        CHANNEL_THREAD_LOCAL.remove();
    }

    public static Channel get() {
        return CHANNEL_THREAD_LOCAL.get();
    }

}
