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
package com.geekfoxer.gateway.ops.vo;

import com.geekfoxer.gateway.common.RequestFilterTypeEnum;
import com.geekfoxer.gateway.common.ResponseFilterTypeEnum;
import com.geekfoxer.gateway.common.RouteType;
import com.geekfoxer.gateway.dao.domain.ApiDO;
import com.geekfoxer.gateway.dao.domain.ApiGroupDO;
import com.geekfoxer.gateway.dao.domain.ApiRpcDO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author liushiming
 * @version ApiVo.java, v 0.0.1 2018年4月17日 下午2:17:58 liushiming
 */
@Data
public class ApiVo implements Serializable {

  private static final long serialVersionUID = 8303012923548625829L;

  private Long id;

  private String name;

  private String describe;

  private String url;

  private String httpMethod;

  private String path;

  private Integer routeType;

  private Long groupId;

  private String groupName;

  private Timestamp gmtCreate;

  private Timestamp gmtModified;

  // RPC
  private String serviceName;

  private String methodName;

  private String serviceGroup;

  private String serviceVersion;

  private byte[] protoContext;

  private String dubboParamTemplate;

  // Spring Cloud
  private String instanceId;

  // Filter
  private List<RequestFilterTypeEnum> requestFilterType;

  private List<ResponseFilterTypeEnum> responseFilterType;


    public RouteType getRouteType() {
        return RouteType.fromType(Integer.valueOf(routeType));
    }

    public String getRouteTypeName() {
        return this.getRouteType().typeName();
    }


  public ApiDO buildApiDO() {
    ApiDO apiDO = new ApiDO();
    apiDO.setId(this.id);
    apiDO.setName(this.name);
    apiDO.setDescribe(this.describe);
    apiDO.setUrl(this.url);
    apiDO.setPath(this.path);
    apiDO.setRoutes(this.routeType);
    apiDO.setHttpMethod(this.httpMethod);
    ApiGroupDO apiGroup = new ApiGroupDO();
    apiGroup.setId(groupId);
    apiDO.setApiGroup(apiGroup);
    return apiDO;
  }

  public ApiRpcDO buildApiRpcDO() {
    if (this.routeType != null
        && (this.getRouteType() == RouteType.DUBBO || this.getRouteType() == RouteType.GRPC)) {
      ApiRpcDO rpcDO = new ApiRpcDO();
      rpcDO.setServiceName(this.serviceName);
      rpcDO.setMethodName(this.methodName);
      rpcDO.setServiceGroup(this.serviceGroup);
      rpcDO.setServiceVersion(this.serviceVersion);
      rpcDO.setProtoContext(this.protoContext);
      rpcDO.setDubboParamTemplate(this.dubboParamTemplate);
      return rpcDO;
    } else {
      return null;
    }
  }

//  public ApiSpringCloudDO buildApiSpringCloudDO() {
//    if (this.routeType != null && this.getRouteType() == RouteType.SpringCloud) {
//      ApiSpringCloudDO springCloudDO = new ApiSpringCloudDO();
//      springCloudDO.setInstanceId(this.instanceId);
//      return springCloudDO;
//    } else {
//      return null;
//    }
//  }


  public static ApiVo buildApiVO(ApiDO apiDO, ApiRpcDO rpcDO) {
    if (apiDO != null) {
      ApiVo apiVO = new ApiVo();
      apiVO.setId(apiDO.getId());
      apiVO.setName(apiDO.getName());
      apiVO.setDescribe(apiDO.getDescribe());
      apiVO.setUrl(apiDO.getUrl());
      apiVO.setHttpMethod(apiDO.getHttpMethod());
      apiVO.setPath(apiDO.getPath());
      apiVO.setGmtCreate(apiDO.getGmtCreate());
      apiVO.setGmtModified(apiDO.getGmtModified());
//      apiVO.setGroupId(apiDO.getApiGroup().getId());
//      apiVO.setGroupName(apiDO.getApiGroup().getName());
      apiVO.setRouteType(apiDO.getRoutes());
      // RPC
      if (apiDO.isRpc() && rpcDO != null) {
        apiVO.setServiceName(rpcDO.getServiceName());
        apiVO.setMethodName(rpcDO.getMethodName());
        apiVO.setServiceGroup(rpcDO.getServiceGroup());
        apiVO.setServiceVersion(rpcDO.getServiceVersion());
        apiVO.setProtoContext(rpcDO.getProtoContext());
        apiVO.setDubboParamTemplate(rpcDO.getDubboParamTemplate());
      }
//      if (apiDO.isSpringCloud() && scDO != null) {
//        // Spring Cloud
//        apiVO.setInstanceId(scDO.getInstanceId());
//      }
      return apiVO;
    }
    return null;
  }


}
