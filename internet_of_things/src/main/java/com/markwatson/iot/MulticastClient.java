package com.markwatson.iot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient {

  final static String INET_ADDR = "224.0.0.5";
  final static int PORT = 9002;

  public static void main(String[] args) throws IOException {
    InetAddress address = InetAddress.getByName(INET_ADDR);
    byte[] buf = new byte[512]; // more than enough for any UDP style packet
    MulticastSocket clientSocket = new MulticastSocket(PORT);
    // listen for broadcast messages:
    clientSocket.joinGroup(address);

    while (true) {
      DatagramPacket messagePacket = new DatagramPacket(buf, buf.length);
      clientSocket.receive(messagePacket);

      String message = new String(buf, 0, buf.length);
      System.out.println("received broadcast message: " + message);
    }
  }
}


