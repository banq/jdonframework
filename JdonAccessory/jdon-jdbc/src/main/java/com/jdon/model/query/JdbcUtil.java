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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq </event>
 * 
 */
public class JdbcUtil {
	private final static String module = JdbcUtil.class.getName();

	/**
	 * queryParam type only support String Integer Float or Long Double Bye
	 * Short if you need operate other types, you must use JDBC directly!
	 */
	public void setQueryParams(Collection queryParams, PreparedStatement ps) throws Exception {
		if ((queryParams == null) || (queryParams.size() == 0))
			return;
		int i = 1;
		Object key = null;

		Iterator iter = queryParams.iterator();
		while (iter.hasNext()) {
			key = iter.next();
			if (key != null) {
				convertType(i, key, ps);
				Debug.logVerbose("[JdonFramework] parameter " + i + " = " + key.toString(), module);
			} else {
				Debug.logWarning("[JdonFramework] parameter " + i + " is null", module);
				ps.setString(i, "");
			}
			i++;
		}

	}

	protected void convertType(int i, Object key, PreparedStatement ps) {
		try {
			if (key instanceof java.lang.String) {
				String keyStrs = (String) key;
				ps.setString(i, keyStrs);
			} else if (key instanceof Integer) {
				ps.setInt(i, ((Integer) key).intValue());
			} else if (key instanceof Float) {
				ps.setFloat(i, ((Float) key).floatValue());
			} else if (key instanceof Long) {
				ps.setLong(i, ((Long) key).longValue());
			} else if (key instanceof Double) {
				ps.setDouble(i, ((Double) key).doubleValue());
			} else if (key instanceof Byte) {
				ps.setByte(i, ((Byte) key).byteValue());
			} else if (key instanceof Short) {
				ps.setShort(i, ((Short) key).shortValue());
			} else if (key instanceof BigDecimal) {
				ps.setBigDecimal(i, ((BigDecimal) key));
			} else {
				ps.setObject(i, key);
				Debug.logVerbose("[JdonFramework]warning: Type =" + key.getClass().getName() + " isn't be converted!", module);
			}
		} catch (SQLException e) {
			Debug.logError("[JdonFramework]setQueryParams error " + e + "in parameter order=" + i + " its value=" + key, module);
		} catch (Exception e) {
			Debug.logError(e, module);
		}
	}

	/**
	 * return event List in the List, every object is event map, by database column
	 * name, we can get the its result from map
	 * 
	 * 
	 * @param rs
	 *            ResultSet
	 * @return List
	 * @throws Exception
	 */
	public List extract(ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		List ret = new ArrayList();
		while (rs.next()) {
			Map map = new LinkedHashMap(count);
			for (int i = 1; i <= count; i++) {
				map.put(meta.getColumnName(i), rs.getObject(i));
			}
			ret.add(map);
		}
		return ret;
	}

}
