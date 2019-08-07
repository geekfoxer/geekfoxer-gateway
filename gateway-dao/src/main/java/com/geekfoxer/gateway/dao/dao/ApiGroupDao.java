
package com.geekfoxer.gateway.dao.dao;


import com.geekfoxer.gateway.dao.domain.ApiGroupDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liushiming
 * @version ApiGroupDao.java, v 0.0.1 2018年4月11日 下午5:49:59 liushiming
 */
@Mapper
public interface ApiGroupDao {
  ApiGroupDO get(Long id);

//  ApiGroupDO load(String key);

  List<ApiGroupDO> list(Map<String, Object> map);

  int count(Map<String, Object> map);

  int save(ApiGroupDO apiGroupDO);

  int update(ApiGroupDO apiGroupDO);

  int remove(Long id);

  int batchRemove(Long[] ids);

}
