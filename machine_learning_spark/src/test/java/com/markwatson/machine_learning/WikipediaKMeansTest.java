package com.markwatson.machine_learning;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Set;

public class WikipediaKMeansTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public WikipediaKMeansTest(String testName)
  {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite( WikipediaKMeansTest.class );
  }

  /**
   * Test that is just for side effect printouts:
   */
  public void test1() throws Exception {
    assertTrue(true);
    WikipediaKMeans.main(null);
  }

}

