package com.geekfoxer.gateway.server;

import com.geekfoxer.gateway.server.netty.HttpFiltersSourceAdapter;
import com.geekfoxer.gateway.server.netty.HttpProxyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
@SpringBootApplication(
        exclude = {ValidationAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ImportResource({"classpath:spring.xml"})
public class ServerGatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
//        SpringApplication.run(ServerGatewayApplication.class, args);
        // 手动指定非 web 方式启动
        new SpringApplicationBuilder(ServerGatewayApplication.class)
                .web(false)
                .run(args);
    }

    @Value("${server.port}")
    private int httpPort;

    @Override
    public void run(String... args) throws Exception {
         runNettyServer();
    }

    private void runNettyServer() {
        HttpProxyServer.bootstrap()
                .withPort(httpPort)
                .withFiltersSource(new HttpFiltersSourceAdapter())
                .withAllowRequestToOriginServer(true)
                .withAllowLocalOnly(false)
                .start();
    }


}
