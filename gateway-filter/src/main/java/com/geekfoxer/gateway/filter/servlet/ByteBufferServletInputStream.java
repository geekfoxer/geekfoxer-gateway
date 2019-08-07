package com.geekfoxer.gateway.filter.servlet;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import javax.servlet.ServletInputStream;
import java.io.IOException;


public class ByteBufferServletInputStream extends ServletInputStream {

  private final ByteBuf byteBuf;

  public ByteBufferServletInputStream(ByteBuf byteBuf) {
    this.byteBuf = byteBuf;
  }

  @Override
  public int read() throws IOException {
    ByteBufInputStream byteBufInputStream = new ByteBufInputStream(byteBuf);
    try {
      return byteBufInputStream.read();
    } finally {
      byteBufInputStream.close();
    }

  }

}
