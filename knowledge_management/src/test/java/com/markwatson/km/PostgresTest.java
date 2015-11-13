package com.markwatson.km;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.markwatson.km.PostgresUtilities;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Unit test for NLP.
 */
public class PostgresTest extends TestCase {

  public PostgresTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(PostgresTest.class);
  }

  public void testDatabase() throws SQLException, ClassNotFoundException {
    PostgresUtilities pcon = new PostgresUtilities();
    int num_rows = pcon.doUpdate(
        "create table test (id int, name varchar(40), description varchar(100))");
    System.out.println("num_rows = " + num_rows);
    num_rows = pcon.doUpdate(
        "CREATE INDEX description_search_ix ON test USING gin(to_tsvector('english', description))");
    System.out.println("num_rows = " + num_rows);
    num_rows = pcon.doUpdate("insert into test values (1, 'Ron', 'brother who lives in San Diego')");
    System.out.println("num_rows = " + num_rows);
    num_rows = pcon.doUpdate("insert into test values (1, 'Anita', 'sister inlaw who lives in San Diego')");
    System.out.println("num_rows = " + num_rows);
    List<HashMap<String,Object>> results = pcon.doQuery("select * from test");
    System.out.println("results = " + results);
    String name = "Ron";
    String search_string = "sister";
    results = pcon.doQuery(
        "select * from test where name = '"  +name +
        "' and to_tsvector(description) @@ to_tsquery('" + search_string + "')");
    System.out.println("results = " + results);
    results = pcon.doQuery(
        "select * from test where to_tsvector(description) @@ to_tsquery('" + search_string + "')");
    System.out.println("results = " + results);
    num_rows = pcon.doUpdate("drop table test");
    System.out.println("num_rows = " + num_rows);

  }
}

