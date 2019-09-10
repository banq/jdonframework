/**
 * Copyright 2005 Jdon.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jdon.framework.test;

import java.io.Serializable;

import com.jdon.annotation.Component;

@Component
public class Constants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String USER_SAVE_ERROR = "USER.CREATE.ERROR";

	private String jndiname = "java:comp/env/jdbc/TestDS";

	/**
	 * @param jndiname
	 */
	public Constants(String jndiname) {
		this.jndiname = jndiname;
	}

	public Constants() {
	}

	/**
	 * @return Returns the jndiname.
	 */
	public String getJndiname() {
		return jndiname;
	}

	/**
	 * @param jndiname
	 *            The jndiname to set.
	 */
	public void setJndiname(String jndiname) {
		this.jndiname = jndiname;
	}
}
