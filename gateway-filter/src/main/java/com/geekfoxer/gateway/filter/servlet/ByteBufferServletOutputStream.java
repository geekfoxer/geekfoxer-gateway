package com.geekfoxer.gateway.filter.servlet;

import io.netty.buffer.ByteBuf;

import javax.servlet.ServletOutputStream;
import java.io.IOException;


public class ByteBufferServletOutputStream extends ServletOutputStream {

  private final ByteBuf byteBuf;

  public ByteBufferServletOutputStream(ByteBuf byteBuf) {
    if (byteBuf == null) {
      throw new NullPointerException("buffer");
    }
    this.byteBuf = byteBuf;
  }

  @Override
  public void write(int b) throws IOException {
    byteBuf.writeByte((byte) b);
  }


}
