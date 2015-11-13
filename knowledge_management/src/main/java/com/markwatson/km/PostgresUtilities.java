package com.markwatson.km;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by Mark on 6/5/2015.
 */
public class PostgresUtilities {
  private static String defaultConnectionURL =
       "jdbc:postgresql://localhost:5432/kmdexample?user=markw&password=";
  private Connection conn = null;

  public PostgresUtilities() throws ClassNotFoundException, SQLException {
    this(defaultConnectionURL);
  }

  public PostgresUtilities(String connectionURL) throws ClassNotFoundException, SQLException {
    Class.forName("org.postgresql.Driver");
    Properties props = new Properties();
    //props.setProperty("user","a username could go here");
    //props.setProperty("password","a passwordc could go here");
    conn = DriverManager.getConnection(connectionURL, props);
  }

  /**
   * doQuery - performs an SQL update
   * @param sql the update string
   * @return List<HashMap<String,Object>> Each row is a map where columns are fetched by (lower case) column name
   * @throws SQLException
   */
  public int doUpdate(String sql) throws SQLException {
    Statement st = conn.createStatement();
    int rows_affected = st.executeUpdate(sql);
    return rows_affected;
  }

  /**
   * doQuery - performs an SQL query
   * @param sql the query string
   * @return List<HashMap<String,Object>> Each row is a map where columns are fetched by (lower case) column name
   * @throws SQLException
   */
  public List<HashMap<String,Object>> doQuery(String sql) throws SQLException {
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(sql);
    List<HashMap<String,Object>> ret = convertResultSetToList(rs);
    rs.close();
    st.close();
    return ret;
  }

  /**
   * textSearch - search for a space separated query string in a specific column in a specific table.
   *
   * Please note that this utility method handles a very general case. I usually start out with
   * a general purpose method like <strong>textSearch</strong> and then as needed add additional
   * methods that are very application specific (e.g., selecting on different columns, etc.)
   *
   * @param tableName
   * @param coloumnToSearch
   * @param query
   * @return List<HashMap<String,Object>> Each row is a map where columns are fetched by (lower case) column name
   * @throws SQLException
   */
  public List<HashMap<String,Object>> textSearch(String tableName, String coloumnToSearch, String query) throws SQLException {
    // we need to separate query words with the & character:
    String modifiedQuery = query.replaceAll(" ", " & ");
    String qString = "select * from " + tableName + " where to_tsvector(" + coloumnToSearch + ") @@ to_tsquery('" + modifiedQuery +  "')";
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery(qString);
    List<HashMap<String,Object>> ret = convertResultSetToList(rs);
    rs.close();
    st.close();
    return ret;
  }

  /**
   * convertResultSetToList
   *
   * The following method is derived from an example on stack overflow.
   * Thanks to stack overflow users RHT and Brad M!
   *
   * Please note that I lower-cased the column names so the column data for each row can
   * uniformly be accessed by using the column name in the query as lower-case.
   *
   * @param rs is a JDBC ResultSet
   * @return List<HashMap<String,Object>> Each row is a map where columns are fetched by (lower case) column name
   * @throws SQLException
   */
  private List<HashMap<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {
    ResultSetMetaData md = rs.getMetaData();
    int columns = md.getColumnCount();
    List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
    while (rs.next()) {
      HashMap<String,Object> row = new HashMap<String, Object>(columns);
      for(int i=1; i<=columns; ++i) {
        row.put(md.getColumnName(i).toLowerCase(),rs.getObject(i));
      }
      list.add(row);
    }
    return list;
  }
}
