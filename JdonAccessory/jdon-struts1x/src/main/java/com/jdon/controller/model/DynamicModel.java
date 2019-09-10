/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.controller.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Dynamic Model this class can be used for DTO
 * 
 * 
 * @author banq
 */
public class DynamicModel extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5782239189450593547L;
	private Map map = new HashMap();

	public Object getValue(String key) {
		return map.get(key);
	}

	public String getStringValue(String key) {
		String value = (String) map.get(key);
		if (value == null)
			return "";
		else
			return value;
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public String toString() {
		StringBuilder bf = new StringBuilder();
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			bf.append(o.toString());
		}
		return bf.toString();
	}

}
