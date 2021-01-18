package com.geekfoxer.gateway.ops.curator;

import com.google.common.collect.Maps;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pizhihui
 * @date 2019-08-14
 */
public class CuratorDemo {



    public static void main(String[] args) throws Exception {

        String monitorDBPath = "";
        String monitorDBData = "";

        String ldbankDBPath = "";
        String ldbankDBData = "";

        String redisPath = "";
        String redisData = "";

        CuratorFramework client = new CuratorConnect().client;

        Map<String, String> zkPathData = Maps.newHashMap();
        zkPathData.put(monitorDBPath, monitorDBData);
        zkPathData.put(ldbankDBPath, ldbankDBData);
        zkPathData.put(redisPath, redisData);


        for (Map.Entry entry : zkPathData.entrySet()) {
            String key = (String)entry.getKey();
            String val = (String) entry.getValue();
            if (client.checkExists().forPath(key) != null) {
                client.delete().forPath(key);
                System.out.println(key + "已经存在, 先删除, 在创建");
            }

            client.create()
                    .creatingParentsIfNeeded()
                    .forPath(key, val.getBytes(Charset.forName("UTF-8")));
        }






    }


}
