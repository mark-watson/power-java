package com.markwatson.linkeddata;

import  com.markwatson.linkeddata.DBpediaLookupClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Unit test for NLP.
 */
public class DBPediaTest extends TestCase {

  public DBPediaTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(DBPediaTest.class);
  }

  public void testSentenceSplitter() {
    assertTrue(true);
    try {
      //DBpediaLookupClient client = new DBpediaLookupClient("Bill Clinton");
      DBpediaLookupClient client = new DBpediaLookupClient("London UK");
      List<Map<String, String>> results = client.variableBindings();
      System.out.println("# query results: " + results.size());
      client.printResults(results);
      // JSON response:
      String json_string = DBpediaLookupClientJson.lookup("Bill Clinton");
      System.out.println("\n\nJSON string:\n" + json_string);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

