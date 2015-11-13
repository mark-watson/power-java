package com.markwatson.iot;

import javax.jmdns.*;
import java.io.IOException;

// reference: https://github.com/openhab/jmdns

public class LookForTestService {

  static public void main(String[] args) throws IOException {
    System.out.println("APPLICATION entered LookForTestService");
    JmDNS jmdns = JmDNS.create();
    // look for all local services:
    ServiceInfo[] local_services = jmdns.list("_http._tcp.local.");
    for (ServiceInfo si2 : local_services) {
      System.out.println("* APPLICATION si: " + si2);
      System.out.println("* APPLICATION APP: " + si2.getApplication());
      System.out.println("* APPLICATION DOMAIN: " + si2.getDomain());
      System.out.println("* APPLICATION NAME: " + si2.getName());
      System.out.println("* APPLICATION PORT: " + si2.getPort());
      System.out.println("* APPLICATION getQualifiedName: " + si2.getQualifiedName());
      System.out.println("* APPLICATION getNiceTextString: " + si2.getNiceTextString());
    }

  }
}
