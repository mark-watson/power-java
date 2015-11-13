package com.markwatson.machine_learning;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by markw on 9/27/15.
 */
public class LogisticRegressionTest  extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public LogisticRegressionTest(String testName)
  {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite( LogisticRegressionTest.class );
  }

  /**
   * Test that is just for side effect printouts:
   */
  public void test1() throws Exception {
    LogisticRegression.main(null);
  }

}
