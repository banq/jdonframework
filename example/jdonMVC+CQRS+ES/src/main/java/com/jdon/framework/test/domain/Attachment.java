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
package com.jdon.framework.test.domain;

import com.jdon.domain.message.DomainMessage;
import com.jdon.framework.test.domain.event.LazyLoader;
import com.jdon.framework.test.event.domain.publisher.LazyLoaderRole;

public class Attachment extends LazyLoader {

	private final String accountId;

	private final LazyLoaderRole uploadLazyLoader;

	private volatile UploadFile uploadFile;

	private boolean noProfilepic;

	public Attachment(String accountId, LazyLoaderRole uploadLazyLoader) {
		super();
		this.accountId = accountId;
		this.uploadLazyLoader = uploadLazyLoader;
		this.noProfilepic = false;
	}

	/**
	 * twice times called.
	 * 
	 * 
	 * @return
	 */
	public UploadFile getUploadFile() {
		if (uploadFile == null && !noProfilepic) {
			if (this.domainMessage == null && uploadLazyLoader != null) {
				super.preload();
			} else if (this.domainMessage != null) {
				uploadFile = (UploadFile) super.loadResult();
				if (uploadFile == null)
					noProfilepic = true;
			}
		}
		return uploadFile;
	}

	public void updateUploadFile() {
		this.domainMessage = uploadLazyLoader.loadUploadFile(this.accountId);
	}

	@Override
	public DomainMessage getDomainMessage() {
		return uploadLazyLoader.loadUploadFile(this.accountId);
	}

	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

}
