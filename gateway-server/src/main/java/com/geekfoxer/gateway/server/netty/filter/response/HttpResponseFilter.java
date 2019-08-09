package com.geekfoxer.gateway.server.netty.filter.response;

import com.geekfoxer.gateway.common.ResponseFilterTypeEnum;
import com.geekfoxer.gateway.filter.servlet.NettyHttpServletRequest;
import com.geekfoxer.gateway.server.netty.filter.AbstractCommonFilter;
import io.netty.handler.codec.http.HttpResponse;


public abstract class HttpResponseFilter extends AbstractCommonFilter {

    public abstract HttpResponse doFilter(NettyHttpServletRequest servletRequest,
                                          HttpResponse httpResponse);

    public abstract ResponseFilterTypeEnum filterType();

    @Override
    public String filterName() {
        return filterType().name();
    }
}
