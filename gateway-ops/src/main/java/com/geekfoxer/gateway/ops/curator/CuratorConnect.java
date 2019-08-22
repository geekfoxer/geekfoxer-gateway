package com.geekfoxer.gateway.ops.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author pizhihui
 * @date 2019-08-14
 */
public class CuratorConnect {

    // Curator客户端
    public CuratorFramework client = null;

    private static final String ZK_CONNECT_ADD = "127.0.0.1:2181";
    private static final int ZK_SESSION_TIMEOUT_MS = 10000; // ms

    public CuratorConnect() {
        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        client = CuratorFrameworkFactory.builder()
                .connectString(ZK_CONNECT_ADD)
                .sessionTimeoutMs(ZK_SESSION_TIMEOUT_MS)
                .retryPolicy(retryPolicy)
                .build();

        client.start();
    }

}
