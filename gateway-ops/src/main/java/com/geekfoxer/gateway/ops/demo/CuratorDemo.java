package com.geekfoxer.gateway.ops.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author pizhihui
 * @date 2020-03-31
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {


        RetryPolicy retryPolicy = new RetryNTimes(10, 60000);
        CuratorFramework client = CuratorFrameworkFactory.newClient("10.1.48.44:2181",
                60000, 15000, retryPolicy);
        client.start();

        byte[] bytes = client.getData().forPath("/dev/fql/db/LogDB");
        System.out.println(new String(bytes));
        System.out.println(new String(client.getData().forPath("/dev/fql/db/MonitorDB")));



    }

}
