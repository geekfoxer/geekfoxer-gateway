package com.geekfoxer.gateway.ops;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * WelcomePageHandlerMapping 会自动加载 public/index.html 的文件,映射静态资源文件
 *
 * @author pizhihui
 * @date 2019-08-06
 */
@SpringBootApplication
//@MapperScan(basePackages = {"com.geekfoxer.gateway.dao.dao"}, annotationClass = Mapper.class)
@ImportResource({"classpath:spring.xml"})
public class OpsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsGatewayApplication.class, args);
    }


}
