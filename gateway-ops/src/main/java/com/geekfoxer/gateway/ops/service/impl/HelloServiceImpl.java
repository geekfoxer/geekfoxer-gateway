package com.geekfoxer.gateway.ops.service.impl;

import com.geekfoxer.gateway.dao.dao.ApiDao;
import com.geekfoxer.gateway.dao.domain.ApiDO;
import com.geekfoxer.gateway.ops.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private ApiDao apiDao;

    @Override
    public String sayHello(String name) {
        ApiDO apiDO = apiDao.get(1L);
        return "hello " + name + apiDO.toString();
    }
}
