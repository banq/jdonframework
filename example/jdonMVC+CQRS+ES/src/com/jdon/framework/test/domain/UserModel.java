/**
 * Copyright 2005 Jdon.com
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

package com.jdon.framework.test.domain;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.framework.test.domain.event.UploadSavedEvent;
import com.jdon.framework.test.domain.event.UserUpdatedEvent;
import com.jdon.framework.test.event.domain.publisher.EventSourcing;
import com.jdon.framework.test.event.domain.publisher.LazyLoaderRole;

/**
 * Aggregate root
 * 
 * @author banq
 * 
 */
@Model
public class UserModel {

	private String userId;
	private String name;
	private String email;

	private String password;

	private String verifypassword;

	private Attachment attachment;

	@Inject
	public EventSourcing es;

	@Inject
	public LazyLoaderRole lazyLoaderRole;

	private int count = -1;

	public void update(UserUpdatedEvent userUpdatedEvent) {
		if (!userUpdatedEvent.getNewUserDTO().getUserId().equals(this.userId)) {
			System.err.print("update not this user");
			return;
		}
		this.name = userUpdatedEvent.getNewUserDTO().getName();
		this.email = userUpdatedEvent.getNewUserDTO().getEmail();
		this.es.updated(userUpdatedEvent);
	}

	public String getName() {
		return name;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifypassword() {
		return verifypassword;
	}

	public void setVerifypassword(String verifypassword) {
		this.verifypassword = verifypassword;
	}

	public Attachment getAttachment() {
		if (attachment == null)
			attachment = new Attachment(this.getUserId(), this.lazyLoaderRole);
		return attachment;
	}

	public UploadFile getUploadFile() {
		return getAttachment().getUploadFile();
	}

	public void setUploadFile(UploadSavedEvent event) {
		es.saveUpload(event);
		UploadFile uploadFile = new UploadFile();
		uploadFile.setData(event.getFilesData());
		uploadFile.setContentType(event.getContextType());
		uploadFile.setName(event.getFilename());
		getAttachment().setUploadFile(uploadFile);

	}
}
