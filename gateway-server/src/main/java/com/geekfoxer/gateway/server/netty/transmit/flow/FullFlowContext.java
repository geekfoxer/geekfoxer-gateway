package com.geekfoxer.gateway.server.netty.transmit.flow;


import com.geekfoxer.gateway.server.netty.transmit.connection.ClientToProxyConnection;
import com.geekfoxer.gateway.server.netty.transmit.connection.ProxyToServerConnection;

public class FullFlowContext extends FlowContext {
  private final String serverHostAndPort;

  public FullFlowContext(ClientToProxyConnection clientConnection,
                         ProxyToServerConnection serverConnection) {
    super(clientConnection);
    this.serverHostAndPort = serverConnection.getServerHostAndPort();
  }

  public String getServerHostAndPort() {
    return serverHostAndPort;
  }

}
