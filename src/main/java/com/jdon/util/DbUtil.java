package com.jdon.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class DbUtil {

  public final static String ORACLE = "oracle";
  public final static String MYSQL =  "mysql";
  public final static String SQLSERVER = "sql server";
  public final static String INTERBASE = "interbase";
  public final static String POSTGRES = "postgres";


  private static boolean init = false;
  // True if the database requires large text fields to be streamed.
  public static boolean streamLargeText;

  // True if the database supports the Statement.setMaxRows() method.
  public static boolean supportsMaxRows;
  // True if the database supports the Statement.setFetchSize() method.
  public static boolean supportsFetchSize;
  // True if the database supports correlated subqueries.

  public static void testConnection(Connection con) {
    if (init)
      return;

    init = true;
    // Set defaults for other meta properties
    streamLargeText = false;
    supportsMaxRows = true;
    supportsFetchSize = true;

    try {
      DatabaseMetaData metaData = con.getMetaData();
      // Supports transactions?

      // Get the database name so that we can perform meta data settings.
      String dbName = metaData.getDatabaseProductName().toLowerCase();
      String driverName = metaData.getDriverName().toLowerCase();

      // Oracle properties.
      if (dbName.indexOf(ORACLE) != -1) {

        streamLargeText = true;
        // The i-net AUGURO JDBC driver
        if (driverName.indexOf("auguro") != -1) {
          streamLargeText = false;
          supportsFetchSize = true;
          supportsMaxRows = false;
        }
      }
      // Postgres properties
      else if (dbName.indexOf(POSTGRES) != -1) {
        supportsFetchSize = false;
      }
      // Interbase properties
      else if (dbName.indexOf(INTERBASE) != -1) {
        supportsFetchSize = false;
        supportsMaxRows = false;
      }
      // SQLServer, JDBC driver i-net UNA properties
      else if (dbName.indexOf(SQLSERVER) != -1 &&
               driverName.indexOf("una") != -1) {
        supportsFetchSize = true;
        supportsMaxRows = false;
      }
      // MySQL properties
      else if (dbName.indexOf(MYSQL) != -1) {

      }

    } catch (Exception ex) {

    }

  }


 public static int getProductAllCount(DataSource ds, String key, String sql_allcount) throws Exception {
   Connection c = null;
   PreparedStatement ps = null;
   ResultSet rs = null;
   int ret = 0;
   try {
     c = ds.getConnection();
     ps = c.prepareStatement(sql_allcount,
                             ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY);
     ps.setString(1, key);
     rs = ps.executeQuery();
     if (rs.first()) {
       ret = rs.getInt(1);
     }
   } catch (SQLException se) {
     throw new Exception("SQLException: " + se.getMessage());
   } finally {
     if (rs != null)
       rs.close();
     if (ps != null)
       ps.close();
     if (c != null)
       c.close();
   }
   return ret;

 }

  //该方法不能返回hasNext，待完善！
 public List getDataList(DataSource ds, String key, String sql, int start, int count) throws
     Exception {
   Connection c = null;
   Statement stmt = null;
   ResultSet rs = null;
   List items = new ArrayList(count);
   try {
     c = ds.getConnection();
     stmt = c.createStatement();

     DbUtil.testConnection(c);
     if (DbUtil.supportsMaxRows)
       stmt.setMaxRows(count + start + 1);
     rs = stmt.executeQuery(sql);
     if (DbUtil.supportsFetchSize)
       rs.setFetchSize(count);
     if (start >= 0 && rs.absolute(start + 1)) {

       do {
         items.add(rs.getString(1));
       }
       while ( (rs.next()) && (--count > 0));

     }

   } catch (SQLException se) {
     throw new Exception("SQLException: " + se.getMessage());
   } finally {
     if (rs != null)
       rs.close();
     if (stmt != null)
       stmt.close();
     if (c != null)
       c.close();
   }
    return items;
 }


}