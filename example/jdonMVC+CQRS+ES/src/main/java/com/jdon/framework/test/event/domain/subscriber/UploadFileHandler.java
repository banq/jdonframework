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
package com.jdon.framework.test.event.domain.subscriber;

import com.jdon.annotation.Component;
import com.jdon.annotation.model.OnEvent;
import com.jdon.framework.test.domain.UploadFile;
import com.jdon.framework.test.repository.UploadRepository;

@Component
public class UploadFileHandler {
	private final UploadRepository uploadRepository;

	public UploadFileHandler(UploadRepository uploadRepository) {
		super();
		this.uploadRepository = uploadRepository;
	}

	@OnEvent("saveUpload")
	public void save(UploadFile uploadFile) {
		uploadRepository.deleteUploadFile(uploadFile.getParentId());
		uploadRepository.createUploadFile(uploadFile);
	}

	@OnEvent("loadUploadFile")
	public UploadFile getUploadFile(String parentId) {
		return uploadRepository.getUploadFile(parentId);
	}

}
