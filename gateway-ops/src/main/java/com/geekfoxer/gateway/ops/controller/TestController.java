package com.geekfoxer.gateway.ops.controller;

import com.geekfoxer.gateway.ops.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private HelloService helloService;

    @GetMapping
    public String hello() {
        String res = helloService.sayHello("tom");
        return res;
    }

}
