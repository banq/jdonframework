package sample.repository;

import sample.domain.Robot;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;

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

@Introduce("message")
public class PublisherRoleImp implements PublisherRole {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sample.repository.RepositoryPublisherRole#remember(sample.domain.Robot)
	 */

	@Send("saveme")
	public DomainMessage remember(Robot robot) {
		return new DomainMessage(robot);
	}

}
