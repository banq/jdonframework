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

import java.util.List;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class Block {

	private int start;
	private int count;

	private List list;

	/**
	 * @param start
	 * @param count
	 */
	public Block(int start, int count) {
		super();
		this.start = start;
		this.count = count;
	}

	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
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

	/**
	 * @return Returns the list.
	 */
	public List getList() {
		return list;
	}

	/**
	 * @param list
	 *            The list to set.
	 */
	public void setList(List list) {
		this.list = list;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("block start=");
		sb.append(start);
		sb.append(" and count=");
		sb.append(count);
		return sb.toString();
	}

}
