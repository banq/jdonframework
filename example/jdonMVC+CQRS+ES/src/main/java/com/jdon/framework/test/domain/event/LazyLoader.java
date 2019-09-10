/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.framework.test.domain.event;

import com.jdon.domain.message.DomainMessage;

public abstract class LazyLoader {

	protected volatile DomainMessage domainMessage;

	public Object loadBlockedResult() {
		if (domainMessage == null)
			preload();
		Object loadedResult = null;
		try {
			loadedResult = domainMessage.getBlockEventResult();
			if (domainMessage != null)
				domainMessage.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadedResult;
	}

	// messageCount is -1, or Collection is null
	// public int getMessageCount() {
	// if (messageCount == -1) {
	// if (super.domainMessage == null) {
	// super.preload();
	//
	// } else {
	// messageCount = (Integer) super.loadResult();
	// }
	// }
	// return messageCount;
	// }

	/**
	 * 
	 * if return event COllection, should be event empty arrayList.
	 * 
	 * @return
	 */
	public Object loadResult() {
		if (domainMessage == null)
			preload();
		Object loadedResult = null;
		try {
			loadedResult = domainMessage.getEventResult();
			if (loadedResult != null)
				domainMessage.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadedResult;

	}

	public void preload() {
		try {
			if (domainMessage == null)
				synchronized (this) {
					if (domainMessage != null)
						return;
					domainMessage = getDomainMessage();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		if (this.domainMessage != null) {
			domainMessage.clear();
		}
	}

	public void setDomainMessage(DomainMessage domainMessage) {
		this.domainMessage = domainMessage;
	}

	public abstract DomainMessage getDomainMessage();

}
