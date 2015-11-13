package com.markwatson.linkeddata;

import  com.markwatson.linkeddata.SparqlTest;

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
public class SparqlTest extends TestCase {

    public SparqlTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(SparqlTest.class);
    }

    public void testSparqlClient() {
        assertTrue(true);
        String sparql =
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                        "PREFIX dbpedia2: <http://dbpedia.org/property/>\n" +
                        "PREFIX dbpedia: <http://dbpedia.org/>\n" +
                        "SELECT ?name ?person WHERE {\n" +
                        "     ?person dbpedia2:birthPlace <http://dbpedia.org/resource/Arizona> .\n" +
                        "     ?person foaf:name ?name .\n" +
                        "}\n" +
                        "LIMIT 10\n";
        SparqlClient test = null;
        try {
            test = new SparqlClient("http://dbpedia.org/sparql", sparql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map<String, String> bindings : test.variableBindings()) {
            System.out.print("result:");
            for (String variableName : bindings.keySet()) {
                System.out.print("  " + variableName + ": " + bindings.get(variableName));
            }
            System.out.println();
        }

      // JSON results:
      try {
        String json_string = SparqlClientJson.query("http://dbpedia.org/sparql", sparql);
        System.out.println("\n\nJSON string:\n" + json_string);
      } catch (Exception e) {
        e.printStackTrace();
      }


    }
}

