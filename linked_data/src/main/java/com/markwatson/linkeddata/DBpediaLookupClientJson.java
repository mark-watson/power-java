package com.markwatson.linkeddata;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Copyright 2015 Mark Watson.  Apache 2 license.
 */
public class DBpediaLookupClientJson {
  public static String lookup(String query) {
    StringBuffer sb = new StringBuffer();
    try {
      HttpClient httpClient = new DefaultHttpClient();
      String query2 = query.replaceAll(" ", "+");
      HttpGet getRequest =
          new HttpGet("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryString="
              + query2);
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
