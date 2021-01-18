package com.geekfoxer.gateway.ops.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author pizhihui
 * @date 2020-03-31
 */
public class ZKDemo {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {


        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper("10.9.22.26:2182",
                50000, new Watcher() {
            // 监控所有被触发的事件
            @Override
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
        System.out.println(zk);
        List<String> list = zk.getChildren("", false);
        System.out.println(list.toString());
        byte[] redis = zk.getData("", false, new Stat());



        List<String> children = zk.getChildren("", false);
        children.forEach(v -> {
            try {
                System.out.println("xxxxxx: " + URLDecoder.decode(v, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
//        byte[] logDB = zk.getData("", false, new Stat());
//        byte[] datasysDB = zk.getData("", false, new Stat());
//        byte[] monitorDB = zk.getData("", false, new Stat());

//        System.out.println(new String(redis));
//        list.forEach(path -> System.out.println(path));
        // 关闭连接
        zk.close();

    }

}
