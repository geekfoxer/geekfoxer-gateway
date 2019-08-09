package com.geekfoxer.sample.dubbo;

import com.alibaba.dubbo.container.Main;

/**
 * @author pizhihui
 * @date 2019-08-08
 */
public class DubboMain {

    public static void main(String[] args) {
        // 使用 dubbo 的 main 方法启动.就是启动 spring 的一个容器
        com.alibaba.dubbo.container.Main.main(args);
    }

}
