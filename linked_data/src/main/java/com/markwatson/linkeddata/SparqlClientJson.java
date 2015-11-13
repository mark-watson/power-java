package com.markwatson.linkeddata;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * Created by markw on 6/6/15.
 */
public class SparqlClientJson {
  public static String query(String endpoint_URL, String sparql) throws Exception {
    StringBuffer sb = new StringBuffer();
    try {
      org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
      String req = URLEncoder.encode(sparql, "utf-8");
      HttpGet getRequest =
          new HttpGet(endpoint_URL + "?query=" + req);
      getRequest.addHeader("accept", "application/json");
      HttpResponse response = httpClient.execute(getRequest);
      if (response.getStatusLine().getStatusCode() != 200) return "Server error";

      BufferedReader br =
          new BufferedReader(
              new InputStreamReader((response.getEntity().getContent())));
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      httpClient.getConnectionManager().shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
}
