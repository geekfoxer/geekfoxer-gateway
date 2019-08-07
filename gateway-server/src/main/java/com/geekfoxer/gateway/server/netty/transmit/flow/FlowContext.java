package com.geekfoxer.gateway.server.netty.transmit.flow;


import com.geekfoxer.gateway.server.netty.transmit.connection.ClientToProxyConnection;

import java.net.InetSocketAddress;


public class FlowContext {
  private final InetSocketAddress clientAddress;

  public FlowContext(ClientToProxyConnection clientConnection) {
    super();
    this.clientAddress = clientConnection.getClientAddress();
  }

  public InetSocketAddress getClientAddress() {
    return clientAddress;
  }

}
