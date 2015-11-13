package com.markwatson.iot;

import java.io.IOException;
import java.net.*;

public class UDPClient {

  static String INET_ADDR = "localhost";
  static int PORT = 9005;
  static int PACKETSIZE = 256;

  public static void main(String args[]) throws IOException {
    InetAddress host = InetAddress.getByName(INET_ADDR);
    DatagramSocket socket = new DatagramSocket();
    byte[] data = ("Hello Server" + System.currentTimeMillis()).getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length, host, PORT);
    socket.send(packet);
    socket.setSoTimeout(1000); // 1000 milliseconds
    packet.setData(new byte[PACKETSIZE]);
    socket.receive(packet);
    System.out.println(new String(packet.getData()));
    socket.close();
  }
}
