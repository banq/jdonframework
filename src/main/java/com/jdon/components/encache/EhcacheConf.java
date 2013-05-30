/*
 * Copyright 2007 the original author or jdon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.jdon.components.encache;

public class EhcacheConf {

	private String ehcacheConfFileName = "jdon_ehcache.xml";

	private String predefinedCacheName = "jdonCache";

	public EhcacheConf(String ehcacheConfFileName, String predefinedCacheName) {
		super();
		this.ehcacheConfFileName = ehcacheConfFileName;
		this.predefinedCacheName = predefinedCacheName;
	}

	public String getEhcacheConfFileName() {
		return ehcacheConfFileName;
	}

	public void setEhcacheConfFileName(String ehcacheConfFileName) {
		this.ehcacheConfFileName = ehcacheConfFileName;
	}

	public String getPredefinedCacheName() {
		return predefinedCacheName;
	}

	public void setPredefinedCacheName(String predefinedCacheName) {
		this.predefinedCacheName = predefinedCacheName;
	}

}
