
package com.geekfoxer.gateway.dao.dao;

import com.geekfoxer.gateway.dao.domain.ApiRpcDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liushiming
 * @version GrpcDao.java, v 0.0.1 2018年1月4日 上午10:48:12 liushiming
 */
@Mapper
public interface ApiRpcDao {
  ApiRpcDO get(@Param("apiId") Long apiId);

  ApiRpcDO loadByService(@Param("serviceName") String serviceName,
                         @Param("methodName") String methodName, @Param("group") String group,
                         @Param("version") String version);

  List<ApiRpcDO> list(Map<String, Object> map);

  int save(ApiRpcDO rpc);

  int update(ApiRpcDO rpc);

  int remove(@Param("apiId") Long apiId);

  int batchRemove(Long[] apiIds);

}
