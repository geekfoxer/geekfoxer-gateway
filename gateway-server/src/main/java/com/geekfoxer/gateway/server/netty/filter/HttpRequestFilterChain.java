package com.geekfoxer.gateway.server.netty.filter;

import com.geekfoxer.gateway.common.ClassUtils;
import com.geekfoxer.gateway.filter.servlet.NettyHttpServletRequest;
import com.geekfoxer.gateway.server.netty.filter.request.HttpRequestFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpRequestFilterChain {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestFilterChain.class);
  public static final Map<String, HttpRequestFilter> filters = Maps.newTreeMap();
  private static final HttpRequestFilterChain filterChain = new HttpRequestFilterChain();

  // TODO 硬编码
  private static final String REQUEST_FILTER_PACKAGENAME =
      "com.geekfoxer.gateway.server.netty.filter.request";

  static {
    Set<Class<?>> requestFilterClazzs = ClassUtils.findAllClasses(REQUEST_FILTER_PACKAGENAME);
    for (Class<?> clazz : requestFilterClazzs) {
      if (HttpRequestFilter.class.isAssignableFrom(clazz)
          && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
        try {
          HttpRequestFilter filter = (HttpRequestFilter) clazz.newInstance();
          filters.put(filter.filterName(), filter);
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static HttpRequestFilterChain requestFilterChain() {
    return filterChain;
  }


  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject,
                               ChannelHandlerContext channelHandlerContext) {
    List<Entry<String, HttpRequestFilter>> list = Lists.newArrayList(filters.entrySet());

    list.sort(Comparator.comparingInt(o -> o.getValue().filterType().order()));

    for (Iterator<Entry<String, HttpRequestFilter>> it = list.iterator(); it.hasNext();) {
      HttpRequestFilter filter = it.next().getValue();
      LOGGER.debug("do filter,the name is:" + filter.filterName());
      HttpResponse response = filter.doFilter(servletRequest, httpObject);
      if (response != null) {
        LOGGER.debug("hit " + filter);
        return response;
      }
    }
    return null;
  }
}
