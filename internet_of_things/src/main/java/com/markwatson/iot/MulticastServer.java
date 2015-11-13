package com.markwatson.iot;

import java.io.IOException;
import java.net.*;

public class MulticastServer {

  final static String INET_ADDR = "224.0.0.5";
  final static int PORT = 9002;

  public static void main(String[] args)
      throws IOException, InterruptedException {
    InetAddress addr = InetAddress.getByName(INET_ADDR);
    DatagramSocket serverSocket = new DatagramSocket();
    for (int i = 0; i < 10; i++) {
      String message = "message " + i;

      DatagramPacket messagePacket =
          new DatagramPacket(message.getBytes(),
              message.getBytes().length, addr, PORT);
      serverSocket.send(messagePacket);

      System.out.println("broadcast message sent: " + message);
      Thread.sleep(500);
    }
  }
}
