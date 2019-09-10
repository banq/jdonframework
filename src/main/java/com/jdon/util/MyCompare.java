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

package com.jdon.util;

import java.io.Serializable;
import java.util.*;

public class MyCompare implements Comparator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8599878655714209261L;

	public int compare(Object x, Object y) {

		Integer a = (Integer) x, b = (Integer) y;
		if (a.intValue() > b.intValue())
			return 1;
		else if (a.intValue() == b.intValue())
			return 0;
		else
			return -1;
	}

}
