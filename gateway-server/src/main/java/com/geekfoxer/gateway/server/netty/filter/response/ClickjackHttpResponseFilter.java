package com.geekfoxer.gateway.server.netty.filter.response;

import com.geekfoxer.gateway.common.ResponseFilterTypeEnum;
import com.geekfoxer.gateway.filter.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpResponse;


public class ClickjackHttpResponseFilter extends HttpResponseFilter {
  private static final X_Frame_Options X_Frame_Option = X_Frame_Options.SAMEORIGIN;

  private static enum X_Frame_Options {
    DENY, SAMEORIGIN
  }

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    httpResponse.headers().add("X-FRAME-OPTIONS", X_Frame_Option);
    return httpResponse;
  }

  @Override
  public ResponseFilterTypeEnum filterType() {
    return ResponseFilterTypeEnum.ClickjackHttpResponseFilter;
  }
}
