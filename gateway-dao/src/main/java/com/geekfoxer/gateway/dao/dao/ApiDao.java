
package com.geekfoxer.gateway.dao.dao;


import com.geekfoxer.gateway.dao.domain.ApiDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liushiming
 * @version RouteDao.java, v 0.0.1 2018年1月4日 上午10:38:23 liushiming
 */
@Mapper
public interface ApiDao {

  ApiDO get(Long id);

  List<ApiDO> list(Map<String, Object> map);

  int count(Map<String, Object> map);

  Long save(ApiDO api);

  int update(ApiDO api);

  int remove(Long id);

  int batchRemove(Long[] ids);

}
