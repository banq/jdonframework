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
import com.jdon.framework.test.domain.event.UserCreatedEvent;
import com.jdon.framework.test.domain.event.UserDeletedEvent;
import com.jdon.framework.test.domain.event.UserUpdatedEvent;
import com.jdon.framework.test.query.UserQuery;
import com.jdon.framework.test.repository.UploadRepository;
import com.jdon.framework.test.repository.UserRepository;

@Component
public class UserEventHandler {

	private final UserRepository userRepository;

	private final UploadRepository uploadRepository;

	private final UserQuery userQuery;

	public UserEventHandler(UserRepository userRepository, UserQuery userQuery, UploadRepository uploadRepository) {
		super();
		this.userRepository = userRepository;
		this.userQuery = userQuery;
		this.uploadRepository = uploadRepository;
	}

	@OnEvent("created")
	public void created(UserCreatedEvent event) {
		try {
			userRepository.save(event.getUserDTO());
			UploadFile uploadFile = event.getUploadFile();
			if (uploadFile.getParentId() != null) {
				uploadFile.setParentId(event.getUserDTO().getUserId());
				uploadFile.setId(Integer.toString(uploadFile.hashCode()));
				uploadRepository.createUploadFile(uploadFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnEvent("updated")
	public void updated(UserUpdatedEvent event) {
		try {
			userRepository.update(event.getNewUserDTO());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnEvent("deleted")
	public void deleted(UserDeletedEvent event) {
		try {
			userRepository.delete(event.getUserId());
			uploadRepository.deleteUploadFile(event.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
