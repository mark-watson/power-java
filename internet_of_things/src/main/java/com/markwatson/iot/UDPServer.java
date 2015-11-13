package com.markwatson.iot;

import java.io.IOException;
import java.net.*;

public class UDPServer {
  final static int PACKETSIZE = 256;
  final static int PORT = 9005;

  public static void main(String args[]) throws IOException {
    DatagramSocket socket = new DatagramSocket(PORT);
    while (true) {
      DatagramPacket packet =
          new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
      socket.receive(packet);
      System.out.println("message from client: address: " +
          packet.getAddress() + " port: " + packet.getPort() +
          " data: " + new String(packet.getData()));
      // change message content before sending it back to the client:
      packet.setData(("from server - " +
          new String(packet.getData())).getBytes());
      socket.send(packet);
    }
  }
}
