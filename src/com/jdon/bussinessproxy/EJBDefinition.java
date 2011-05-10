/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.bussinessproxy;

import com.jdon.bussinessproxy.meta.EJBTargetMetaDef;

/**
 * 兼容旧版本
 * 
 * <p>
 * </p>
 * 
 * @author banq
 * @version JdonFramework 2005 v1.0
 */
public class EJBDefinition extends EJBTargetMetaDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3775651877379365441L;

	public EJBDefinition(String p_jndiName, String p_localClassName) {
		super(p_jndiName, p_jndiName, p_localClassName);
	}

}
