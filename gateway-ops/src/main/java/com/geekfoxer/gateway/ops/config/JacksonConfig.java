package com.geekfoxer.gateway.ops.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson的配置
 *
 * @author pizhihui
 * @date 2018-12-02
 */
@Configuration
public class JacksonConfig {


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
            // null字段不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 序列化空对象
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // 禁止失败的字段属性
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //objectMapper.registerModule(newJavaTimeModule);
        // 允许非引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return objectMapper;


    }

}
