package com.geekfoxer.gateway.server.netty.filter;

import com.geekfoxer.gateway.common.ClassUtils;
import com.geekfoxer.gateway.filter.servlet.NettyHttpServletRequest;
import com.geekfoxer.gateway.server.netty.filter.response.HttpResponseFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.http.HttpResponse;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class HttpResponseFilterChain {
    public static final Map<String, HttpResponseFilter> filters = Maps.newTreeMap();
    private static HttpResponseFilterChain filterChain = new HttpResponseFilterChain();
    // TODO 硬编码
    private static final String RESPONSE_FILTER_PACKAGENAME =
            "com.geekfoxer.gateway.server.netty.filter.response";

    static {
        Set<Class<?>> requestFilterClazzs = ClassUtils.findAllClasses(RESPONSE_FILTER_PACKAGENAME);
        for (Class<?> clazz : requestFilterClazzs) {
            if (HttpResponseFilter.class.isAssignableFrom(clazz)
                    && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                try {
                    HttpResponseFilter filter = (HttpResponseFilter) clazz.newInstance();
                    filters.put(filter.filterName(), filter);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static HttpResponseFilterChain responseFilterChain() {
        return filterChain;
    }


    public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
        List<Entry<String, HttpResponseFilter>> list = Lists.newArrayList(filters.entrySet());

        list.sort(Comparator.comparingInt(o -> o.getValue().filterType().order()));

        for (Entry<String, HttpResponseFilter> stringHttpResponseFilterEntry : list) {
            HttpResponseFilter filter = stringHttpResponseFilterEntry.getValue();
            HttpResponse response = filter.doFilter(servletRequest, httpResponse);
            if (response != null) {
                return response;
            }
        }
        return null;
    }
}
