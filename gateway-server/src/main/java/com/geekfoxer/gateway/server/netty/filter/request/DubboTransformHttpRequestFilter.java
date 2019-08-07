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
package com.geekfoxer.gateway.server.netty.filter.request;

import com.geekfoxer.gateway.common.RequestFilterTypeEnum;
import com.geekfoxer.gateway.dao.domain.ApiRpcDO;
import com.geekfoxer.gateway.server.cache.ApiAndFilterCacheComponent;
import com.geekfoxer.gateway.server.config.SpringContextHolder;
import com.geekfoxer.gateway.filter.servlet.NettyHttpServletRequest;
import com.geekfoxer.gateway.server.protocol.dubbo.DynamicDubboClient;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * dubbo协议转换
 */
public class DubboTransformHttpRequestFilter extends HttpRequestFilter {

  private final DynamicDubboClient dubboClient =
      SpringContextHolder.getBean(DynamicDubboClient.class);

  private final ApiAndFilterCacheComponent routeRuleCache =
      SpringContextHolder.getBean(ApiAndFilterCacheComponent.class);

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    ApiRpcDO rpc = routeRuleCache.getRpcRoute(uri);
    if (rpc != null && rpc.getDubboParamTemplate() != null && dubboClient != null) {
      String jsonOutput = dubboClient.doRpcRemoteCall(rpc, servletRequest);
      HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
          HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonOutput.getBytes(CharsetUtil.UTF_8)));
      HttpUtil.setKeepAlive(response, false);
      return response;
    } else {
      // 如果从缓存没有查到dubbo的映射信息，说明不是泛化调用，返回空，继续走下一个filter或者去走rest服务发现等
      return null;
    }
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.DUBBO;
  }

}
