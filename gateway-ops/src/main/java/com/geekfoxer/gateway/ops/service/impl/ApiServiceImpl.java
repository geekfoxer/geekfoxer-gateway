package com.geekfoxer.gateway.ops.service.impl;

import com.geekfoxer.gateway.dao.dao.ApiDao;
import com.geekfoxer.gateway.dao.dao.ApiGroupDao;
import com.geekfoxer.gateway.dao.dao.ApiRpcDao;
import com.geekfoxer.gateway.dao.domain.ApiDO;
import com.geekfoxer.gateway.dao.domain.ApiGroupDO;
import com.geekfoxer.gateway.dao.domain.ApiRpcDO;
import com.geekfoxer.gateway.ops.domain.PageDO;
import com.geekfoxer.gateway.ops.req.PageQuery;
import com.geekfoxer.gateway.ops.resp.PageResult;
import com.geekfoxer.gateway.ops.resp.RespBean;
import com.geekfoxer.gateway.ops.service.ApiService;
import com.geekfoxer.gateway.ops.utils.Query;
import com.geekfoxer.gateway.ops.vo.ApiGroupVo;
import com.geekfoxer.gateway.ops.vo.ApiVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author pizhihui
 * @date 2019-08-07
 */
@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ApiGroupDao apiGroupDao;
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ApiRpcDao rpcDao;

    @Override
    public PageResult queryList(Query query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
//        int total = apiDao.count(query);
        List<ApiDO> apiDOs = apiDao.list(query);

        List<ApiVo> apiVOs = Lists.newArrayListWithCapacity(apiDOs.size());
        for (ApiDO apiDO : apiDOs) {
            Long apiId = apiDO.getId();
            Boolean isRpc = apiDO.isRpc();
            Boolean isSpringCloud = apiDO.isSpringCloud();
            ApiRpcDO rpcDO = null;
            // ApiSpringCloudDO springCloudDO = null;
            if (isRpc) {
                rpcDO = rpcDao.get(apiId);
            }
//            if (isSpringCloud) {
//                springCloudDO = springCloudDao.get(apiId);
//            }
            ApiVo apiVO = ApiVo.buildApiVO(apiDO, rpcDO);
            apiVOs.add(apiVO);
        }

        return new PageResult<>(new PageInfo<>(apiDOs).getTotal(), apiVOs);
    }

    @Override
    public int save(ApiVo vo) {
        ApiDO apiDO = vo.buildApiDO();
        apiDao.save(apiDO);
        ApiRpcDO rpcDO = vo.buildApiRpcDO();
        // ApiSpringCloudDO springCloudDO = vo.buildApiSpringCloudDO();
        if (rpcDO != null) {
            rpcDO.setApi(apiDO);
            rpcDao.save(rpcDO);
        }
//        if (springCloudDO != null) {
//            springCloudDO.setApi(apiDO);
//            springCloudDao.save(springCloudDO);
//        }
        return 0;
    }

    @Override
    public int remove(Long id) {
        // filterRuleService.removeByApiId(id);
        rpcDao.remove(id);
        // springCloudDao.remove(id);
        apiDao.remove(id);
        return 1;
    }

}
