/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package com.jdon.framework.test.event.domain.publisher;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.framework.test.domain.event.UserCreatedEvent;
import com.jdon.framework.test.domain.event.UserDeletedEvent;
import com.jdon.framework.test.domain.event.UserUpdatedEvent;
import com.jdon.framework.test.domain.vo.UploadVO;

@Introduce("message")
public class EventSourcing {

	@Send("created")
	public DomainMessage created(UserCreatedEvent event) {
		return new DomainMessage(event);
	}

	@Send("updated")
	public DomainMessage updated(UserUpdatedEvent event) {
		return new DomainMessage(event);
	}

	@Send("deleted")
	public DomainMessage deleted(UserDeletedEvent event) {
		return new DomainMessage(event);
	}

	@Send("saveUpload")
	public DomainMessage saveUpload(UploadVO event) {
		return new DomainMessage(event);
	}
}
