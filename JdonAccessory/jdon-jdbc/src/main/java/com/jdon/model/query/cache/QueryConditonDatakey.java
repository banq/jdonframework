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
package com.jdon.model.query.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class QueryConditonDatakey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5226727949813500273L;
	private String sqlquery;
	private Collection queryParams;
	private int start;
	private int count = 200;
	private int blockSize;

	/**
	 * @param sqlquery
	 * @param queryParams
	 */
	public QueryConditonDatakey(String sqlquery, Collection queryParams) {
		super();
		this.sqlquery = sqlquery;
		this.queryParams = queryParams;
	}

	/**
	 * @param sqlquery
	 * @param queryParams
	 */
	public QueryConditonDatakey(String sqlquery, Collection queryParams, int start, int count, int blockSize) {
		this.sqlquery = sqlquery;
		this.queryParams = queryParams;
		this.start = start;
		this.count = count;
		this.blockSize = blockSize;
	}

	/**
	 * @return Returns the queryParams.
	 */
	public Collection getQueryParams() {
		return queryParams;
	}

	/**
	 * @param queryParams
	 *            The queryParams to set.
	 */
	public void setQueryParams(Collection queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * @return Returns the sqlquery.
	 */
	public String getSqlquery() {
		return sqlquery;
	}

	/**
	 * @param sqlquery
	 *            The sqlquery to set.
	 */
	public void setSqlquery(String sqlquery) {
		this.sqlquery = sqlquery;
	}

	/**
	 * @return Returns the start.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            The start to set.
	 */
	public void setStart(int start) {
		this.start = start;
	}

	public String getSQlKey() {
		StringBuilder sb = new StringBuilder(sqlquery);
		Iterator iter = queryParams.iterator();
		while (iter.hasNext()) {
			Object queryParamO = iter.next();
			if (queryParamO != null) {
				sb.append("+");
				sb.append(queryParamO.toString());
			}
		}
		return sb.toString();
	}

	public String toString() {
		return getSQlKey();
	}

	public int getBlockStart() {
		int blockID = start / count;
		int blockStart = blockID * count;
		return blockStart;
	}

	public String getBlockDataKey() {
		return Integer.toString(getBlockStart());
	}

	/**
	 * @return Returns the blockSize.
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @param blockSize
	 *            The blockSize to set.
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * @return Returns the blockSize.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param blockSize
	 *            The blockSize to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}
}
