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

        String monitorDBPath = "/pre/fql/db/MonitorDB";
        String monitorDBData = "{\"MonitorDB.master.ip\":\"127.0.0.1\",\"MonitorDB.master.port\":\"3306\",\"MonitorDB.master.username\":\"root\",\"MonitorDB.master.password\":\"root\",\"MonitorDB.slave.ip\":\"127.0.0.1\",\"MonitorDB.slave.port\":\"3306\",\"MonitorDB.slave.username\":\"root\",\"MonitorDB.slave.password\":\"root\"}\n";

        String ldbankDBPath = "/pre/fql/db/LdbankDB";
        String ldbankDBData = "{\"LdbankDB.master.ip\":\"127.0.0.1\",\"LdbankDB.master.port\":\"3306\",\"LdbankDB.master.username\":\"root\",\"LdbankDB.master.password\":\"root\",\"LdbankDB.slave.ip\":\"127.0.0.1\",\"LdbankDB.slave.port\":\"3306\",\"LdbankDB.slave.username\":\"root\",\"LdbankDB.slave.password\":\"root\"}";

        String redisPath = "/pre/fql/redis/Data_Fintech_Cache";
        String redisData = "{\"Data_Fintech_Cache.master.ip\":\"127.0.0.1\",\"Data_Fintech_Cache.master.port\":\"6379\",\"Data_Fintech_Cache.master.password\":\"\",\"Data_Fintech_Cache.slave.ip\":\"127.0.0.1\",\"Data_Fintech_Cache.slave.port\":\"6379\",\"Data_Fintech_Cache.slave.password\":\"\"}";

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
