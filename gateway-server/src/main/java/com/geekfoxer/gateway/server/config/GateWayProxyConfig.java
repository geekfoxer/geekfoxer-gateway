/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.geekfoxer.gateway.server.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.geekfoxer.gateway.server.protocol.dubbo.DynamicDubboClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import io.github.tesla.gateway.config.eureka.EurekaClientConfigBean;
//import io.github.tesla.gateway.config.eureka.EurekaInstanceConfigBean;
//import io.github.tesla.gateway.protocol.springcloud.DynamicSpringCloudClient;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
@Configuration
public class GateWayProxyConfig {


//  @Configuration
//  @ConditionalOnProperty(prefix = "saluki.grpc", name = "registryAddress")
//  protected class GrpcConfig {
//
//    @SalukiReference(group = "default", version = "1.0.0")
//    private io.github.saluki.grpc.service.GenericService genricService;
//
//    @Bean
//    protected DynamicGrpcClient dynamicGrpcClient() {
//      return new DynamicGrpcClient(genricService);
//    }
//
//  }
//  @Configuration
//  @ConditionalOnProperty(prefix = "eureka.client.serviceUrl", name = "defaultZone")
//  protected class SpringCloudConfig {
//
//    @Value("${server.port}")
//    private int httpPort;
//
//    @Bean
//    protected DynamicSpringCloudClient dynamicSpringCloudClient1(
//        EurekaInstanceConfigBean instanceConfig, EurekaClientConfigBean eurekaClientConfig) {
//      return new DynamicSpringCloudClient(instanceConfig, eurekaClientConfig, httpPort);
//    }
//
//  }

  @Configuration
  @ConditionalOnProperty(prefix = "dubbo", name = "registryAddress")
  protected class DubboCoonfig {
    @Value("${dubbo.registryAddress}")
    private String registry;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    protected ApplicationConfig dubboApplicationConfig() {
      ApplicationConfig appConfig = new ApplicationConfig();
      appConfig.setName(applicationName);
      return appConfig;
    }

    @Bean
    protected RegistryConfig dubboRegistryConfig() {
      RegistryConfig registryConfig = new RegistryConfig();
      if (StringUtils.startsWith(registry, "zookeeper")) {
        registryConfig.setAddress(registry);
      } else {
        registryConfig.setAddress("zookeeper://" + registry);
      }
//      registryConfig.setClient("curator");
      return registryConfig;
    }

    @Bean
    protected DynamicDubboClient dynamicDubboClient(ApplicationConfig applicationConfig,
                                                    RegistryConfig registryConfig) {
      return new DynamicDubboClient(applicationConfig, registryConfig);
    }

  }


}
