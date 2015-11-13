package com.markwatson.iot;

import javax.jmdns.*;
import java.io.IOException;

public class CreateTestService {
  static public void main(String[] args) throws IOException {
    JmDNS jmdns = JmDNS.create();
    jmdns.registerService(
        ServiceInfo.create("_http._tcp.local.", "FOO123.", 1234,
            0, 0, "any data for FOO123")
    );
    // listener for service FOO123:
    jmdns.addServiceListener("_http._tcp.local.", new Foo123Listener());
  }

  static class Foo123Listener implements ServiceListener {

    public void serviceAdded(ServiceEvent serviceEvent) {
      System.out.println("FOO123 SERVICE ADDED in class CreateTestService");
    }

    public void serviceRemoved(ServiceEvent serviceEvent) {
      System.out.println("FOO123 SERVICE REMOVED in class CreateTestService");
    }

    public void serviceResolved(ServiceEvent serviceEvent) {
      System.out.println("FOO123 SERVICE RESOLVED in class CreateTestService");
    }
  }
}
