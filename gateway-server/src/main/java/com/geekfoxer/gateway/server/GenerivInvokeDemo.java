package com.geekfoxer.gateway.server;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * dubbo 的泛化调用,可以跨语言使用,以及 http 网关都可以使用
 *
 * @author pizhihui
 * @date 2019-08-06
 */
public class GenerivInvokeDemo {

    public static void main(String[] args) {
        // 普通编码配置方式
        ApplicationConfig application = new ApplicationConfig();
        application.setName("geekfoxer-gateway");
        application.setId("geekfoxer-gateway");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
//        registry.setClient("curator");

        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setId("com.geekfoxer.sample.dubbo.user.UserService");
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setGroup("geekfoxer");
        reference.setGeneric(true);
        reference.setCheck(false);
        reference.setInterface("com.geekfoxer.sample.dubbo.user.UserService");
        reference.setVersion("1.0.0");

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);


        Map<String, String> params = new HashMap<>(4);
        params.put("name", "tomc");
        params.put("mobile", "123456");
        params.put("idNo", "hello");
        params.put("class", "com.geekfoxer.sample.dubbo.pojo.UserRequest");

        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke("sayHello",
                new String[] { "com.geekfoxer.sample.dubbo.pojo.UserRequest" },
                new Object[] { params });
        System.out.println("获取的结果是: " + result);


    }

}
