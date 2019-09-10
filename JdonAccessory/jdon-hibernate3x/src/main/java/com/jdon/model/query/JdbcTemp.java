/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.model.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.jdon.util.DbUtil;
import com.jdon.util.Debug;

/**
 * JDBC Template using this class, don't need write jdbc operations.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq </event>
 * 
 */
public class JdbcTemp {
	private final static String module = JdbcTemp.class.getName();

	private final DataSource dataSource;

	private JdbcUtil jdbcUtil;

	public JdbcTemp(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcUtil = new JdbcUtil();
	}

	/**
	 * get event single object from database
	 * 
	 * fit for this sql: select name from user where id=?
	 * 
	 * the "name" is single result
	 * 
	 * @param queryParams
	 * @param sqlquery
	 * @return
	 * @throws Exception
	 */
	public Object querySingleObject(Collection queryParams, String sqlquery) throws Exception {
		Debug.logVerbose("[JdonFramework]--> enter getSingleObject ", module);
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Object o = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sqlquery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Debug.logVerbose(sqlquery, module);
			jdbcUtil.setQueryParams(queryParams, ps);

			rs = ps.executeQuery();
			if (rs.first()) {
				o = rs.getObject(1);
				Debug.logVerbose("[JdonFramework]-->in db found it:" + o.getClass().getName(), module);
			}
		} catch (SQLException se) {
			throw new SQLException("SQLException: " + se.getMessage());
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException quiet) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException quiet) {
				}
			if (c != null)
				try {
					c.close();
				} catch (SQLException quiet) {
				}
		}

		return o;
	}

	/**
	 * this method is fit for this sql:
	 * 
	 * select id, name, password from user where id = ?
	 * 
	 * the " id, name, password " and its values are packed in event map of returen
	 * list.
	 * 
	 * 
	 * @param queryParams
	 * @param sqlquery
	 * @return
	 * @throws Exception
	 */
	public List queryMultiObject(Collection queryParams, String sqlquery) throws Exception {
		Debug.logVerbose("[JdonFramework]--> enter queryMultiObject ", module);
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sqlquery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Debug.logVerbose("[JdonFramework]" + sqlquery, module);
			jdbcUtil.setQueryParams(queryParams, ps);

			rs = ps.executeQuery();
			list = jdbcUtil.extract(rs);
		} catch (SQLException se) {
			throw new SQLException("SQLException: " + se.getMessage());
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException quiet) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException quiet) {
				}
			if (c != null)
				try {
					c.close();
				} catch (SQLException quiet) {
				}
		}
		return list;
	}

	/**
	 * same as queryMultiObject(Collection queryParams, String sqlquery) but the
	 * result is event block result, the result'size is the value of count, and the
	 * result's start poiny is the value of start.
	 * 
	 * @param queryParams
	 * @param sqlquery
	 * @param start
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public List queryMultiObject(Collection queryParams, String sqlquery, int start, int count) throws Exception {
		Debug.logVerbose("[JdonFramework]--> enter queryMultiObject from:" + start + " size:" + count, module);
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List items = new ArrayList(count);
		try {
			c = dataSource.getConnection();
			DbUtil.testConnection(c);
			ps = c.prepareStatement(sqlquery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Debug.logVerbose(sqlquery, module);
			jdbcUtil.setQueryParams(queryParams, ps);
			rs = ps.executeQuery();
			if (DbUtil.supportsFetchSize)
				rs.setFetchSize(count);
			if (start >= 0 && rs.absolute(start + 1)) {
				do {
					items = jdbcUtil.extract(rs);
				} while ((rs.next()) && (--count > 0));
			}
		} catch (SQLException se) {
			throw new SQLException("SQLException: " + se.getMessage());
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException quiet) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException quiet) {
				}
			if (c != null)
				try {
					c.close();
				} catch (SQLException quiet) {
				}
		}
		return items;
	}

	/**
	 * this method can used for insert/update/delete for database
	 * 
	 * In insertParams the parameter type only supports: String Integer Float or
	 * Long Double Bye Short if you need operate other types, you must use JDBC
	 * directly replacing this method.
	 * 
	 * @param insertParams
	 *            the parameter that will be insert into sql.
	 * @param sql
	 *            the standard sql sentence.
	 * @throws Exception
	 */
	public void operate(Collection insertParams, String sql) throws Exception {
		Debug.logVerbose("[JdonFramework]--> enter getSingleObject ", module);
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(sql);
			Debug.logVerbose(sql, module);
			jdbcUtil.setQueryParams(insertParams, ps);

			ps.executeUpdate();
		} catch (SQLException se) {
			throw new SQLException("SQLException: " + se.getMessage());
		} catch (Exception ex) {
			Debug.logError(ex, module);
			throw new Exception(ex);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException quiet) {
				}
			if (c != null)
				try {
					c.close();
				} catch (SQLException quiet) {
				}
		}
	}

	public JdbcUtil getJdbcUtil() {
		return jdbcUtil;
	}

	public void setJdbcUtil(JdbcUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
	}

}
