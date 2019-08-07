package com.geekfoxer.gateway.ops.service;

import com.geekfoxer.gateway.ops.domain.PageDO;
import com.geekfoxer.gateway.ops.req.PageQuery;
import com.geekfoxer.gateway.ops.resp.PageResult;
import com.geekfoxer.gateway.ops.resp.RespBean;
import com.geekfoxer.gateway.ops.utils.Query;
import com.geekfoxer.gateway.ops.vo.ApiVo;

/**
 * @author pizhihui
 * @date 2019-08-07
 */
public interface ApiService {

    PageResult queryList(Query query);

    int save(ApiVo vo);

    int remove(Long id);

}
