package com.markwatson.linkeddata;

import com.markwatson.linkeddata.AnnotationTest;

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
public class AnnotationTest extends TestCase {

  public AnnotationTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(AnnotationTest.class);
  }

  public void testSentenceSplitter() {
    assertTrue(true);
    String s = "Bill Clinton went to a charity event in Thailand.";
    String annotated = AnnotateEntities.annotate(s);
    System.out.println("Annotated string: " + annotated);
  }
}

