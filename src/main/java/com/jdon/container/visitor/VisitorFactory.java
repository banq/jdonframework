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

package com.jdon.container.visitor;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.controller.context.SessionWrapper;

/**
 * Factory that get visitors for the container that have been optimized by
 * cache(HttpSession)
 * 
 * replace this class in container.xml ,we can change HttpSession with another
 * session type, HttpSession is binding to Web container.
 * 
 * 
 * <p>
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq </event>
 *         </p>
 */
public interface VisitorFactory {

	/**
	 * return event ComponentVisitor with cache. the httpSession is used for
	 * optimizing the component performance
	 * 
	 * @param request
	 * @param targetMetaDef
	 * @return
	 */
	ComponentVisitor createtVisitor(SessionWrapper session, TargetMetaDef targetMetaDef);

}
