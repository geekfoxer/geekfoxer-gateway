package com.geekfoxer.gateway.ops;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * WelcomePageHandlerMapping 会自动加载 public/index.html 的文件,映射静态资源文件
 *
 * @author pizhihui
 * @date 2019-08-06
 */
@SpringBootApplication
//@MapperScan(basePackages = {"com.geekfoxer.gateway.dao.dao"}, annotationClass = Mapper.class)
@ImportResource({"classpath:spring.xml"})
public class OpsGatewayApplication implements CommandLineRunner {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    public static void main(String[] args) {
        SpringApplication.run(OpsGatewayApplication.class, args);
    }


    @Override
    public void run(String... args) {
        System.out.println("执行sql文件.....");
        Connection connection = sqlSessionFactory.openSession().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM GATEWAY_API ");
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("真正执行sql文件.....");
            ClassPathResource rc = new ClassPathResource("sql/schema.sql");
            EncodedResource er = new EncodedResource(rc, "utf-8");
            ScriptUtils.executeSqlScript(connection, er);
        }

    }
}
