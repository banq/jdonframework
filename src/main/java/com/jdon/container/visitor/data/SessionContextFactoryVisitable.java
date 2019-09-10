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
package com.jdon.container.visitor.data;

import com.jdon.container.visitor.Visitable;

/**
 * 
 *
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * @see ComponentKeys.SESSIONCONTEXT_FACTORY
 */

public class SessionContextFactoryVisitable implements Visitable {
	private final int maxSize;

	public SessionContextFactoryVisitable(String maxSize) {
		super();
		this.maxSize = Integer.parseInt(maxSize);
	}

	public Object accept() {
		return new SessionContext(maxSize);
	}
}
