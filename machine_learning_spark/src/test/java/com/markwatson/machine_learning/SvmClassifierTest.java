package com.markwatson.machine_learning;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by markw on 11/29/15.
 */
public class SvmClassifierTest   extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public SvmClassifierTest(String testName)
  {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite( SvmClassifierTest.class );
  }

  /**
   * Test that is just for side effect printouts:
   */
  public void test1() throws Exception {
    SvmClassifier.main(null);
  }

}
