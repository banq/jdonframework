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
package com.jdon.model.query.block;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jdon.model.query.JdbcUtil;
import com.jdon.model.query.cache.QueryConditonDatakey;
import com.jdon.util.DbUtil;
import com.jdon.util.Debug;

/**
 * Batch query JDBC Template
 * 
 * this class can be directly by application system.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq </event>
 * 
 */
public class BlockQueryJDBCTemp implements BlockQueryJDBC {
	private final static String module = BlockQueryJDBCTemp.class.getName();

	private final DataSource dataSource;

	private JdbcUtil jdbcUtil;

	public BlockQueryJDBCTemp(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcUtil = new JdbcUtil();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jdon.model.query.PageIteratorJDBC#fetchDataAllCount(java.lang.Object,
	 * java.lang.String)
	 */
	public int fetchDataAllCount(QueryConditonDatakey qcd) {
		Debug.logVerbose("[JdonFramework]--> execute fetch all count for sql sentence: " + qcd.getSqlquery(), module);
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ret = 0;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(qcd.getSqlquery(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			jdbcUtil.setQueryParams(qcd.getQueryParams(), ps);

			rs = ps.executeQuery();
			if (rs.first()) {
				ret = rs.getInt(1);
			}
		} catch (SQLException se) {
			Debug.logError(se, module);
		} catch (Exception ex) {
			Debug.logError(ex, module);
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
		Debug.logVerbose("[JdonFramework]--> fetchDataAllCount is" + ret, module);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jdon.model.query.PageIteratorJDBC#fetchDatas(java.util.Collection,
	 * java.lang.String, int, int)
	 */
	public List fetchDatas(QueryConditonDatakey qcdk) {
		Debug.logVerbose("[JdonFramework]--> fetch the primary key collection, sql sentence: " + qcdk.getSQlKey(), module);
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int blockStart = qcdk.getBlockStart();
		int blockSize = qcdk.getBlockSize();
		Debug.logVerbose("[JdonFramework]--> blockStart=" + blockStart + " blockSize=" + blockSize, module);
		List items = new ArrayList(blockSize);
		try {
			c = dataSource.getConnection();

			DbUtil.testConnection(c);
			ps = c.prepareStatement(qcdk.getSqlquery(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			jdbcUtil.setQueryParams(qcdk.getQueryParams(), ps);

			rs = ps.executeQuery();
			if (DbUtil.supportsFetchSize)
				rs.setFetchSize(blockSize);
			// Many JDBC drivers don't implement scrollable cursors the real
			// way, but instead load all results into memory. Looping through
			// the results ourselves is more efficient.
			for (int i = 0; i < blockStart; i++) {
				if (!rs.next())
					break;
			}
			blockSize++;
			while (rs.next() && (--blockSize > 0)) {
				Object result = rs.getObject(1);
				Debug.logVerbose("[JdonFramework]--> found event primary key = " + result + ",  type:" + result.getClass().getName(), module);
				items.add(result);
			}

			Debug.logVerbose("[JdonFramework]--> get event result succefully ..", module);
		} catch (SQLException se) {
			Debug.logError(se, module);

		} catch (Exception ex) {
			Debug.logError(ex, module);

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

	public JdbcUtil getJdbcUtil() {
		return jdbcUtil;
	}

	public void setJdbcUtil(JdbcUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
	}

}
