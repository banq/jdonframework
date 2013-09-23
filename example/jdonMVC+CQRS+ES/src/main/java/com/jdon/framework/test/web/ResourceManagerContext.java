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
package com.jdon.framework.test.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import com.jdon.domain.dci.RoleAssigner;
import com.jdon.framework.test.domain.UploadFile;
import com.jdon.framework.test.domain.UserModel;
import com.jdon.framework.test.domain.command.UpdateCommand;
import com.jdon.framework.test.domain.event.UserCreatedEvent;
import com.jdon.framework.test.domain.event.UserDeletedEvent;
import com.jdon.framework.test.domain.vo.UploadVO;
import com.jdon.framework.test.query.UserQuery;
import com.jdon.framework.test.repository.EntityFactory;
import com.jdon.mvc.annotations.In;
import com.jdon.mvc.http.FormFile;
import com.jdon.mvc.ioc.BeanType;
import com.jdon.mvc.represent.Html;
import com.jdon.mvc.represent.Image;
import com.jdon.mvc.represent.Represent;
import com.jdon.mvc.represent.State;
import com.jdon.mvc.validation.Validation;
import com.jdon.mvc.validation.infrastructure.NotEmail;
import com.jdon.mvc.validation.infrastructure.NotEqual;
import com.jdon.mvc.validation.infrastructure.NotNull;

public class ResourceManagerContext {
	private final static Logger logger = Logger.getLogger(ResourceManagerContext.class);

	private @Context
	HttpServletRequest request;

	private @Context
	Validation validation;

	@In(value = "entityFactory", type = BeanType.COMPONENT)
	private EntityFactory entityFactory;

	@In(value = "userQuery", type = BeanType.COMPONENT)
	private UserQuery userQuery;

	@In(value = "roleAssigner", type = BeanType.COMPONENT)
	private RoleAssigner roleAssigner;

	@In(value = "commandHandler", type = BeanType.COMPONENT)
	private CommandHandler commandHandler;

	@Path("/")
	public Represent index() {
		logger.debug(" enter index ");
		List userList = userQuery.getUserList();
		return new Html("/index.jsp", "userList", userList);

	}

	@Path("/user/{userId}")
	public Represent get(int userId) {
		UserModel user = getUser(Integer.toString(userId));
		return new Html("/editUser.jsp", "user", user);
	}

	@Path("/users")
	@POST
	public Represent post(UserModel user) {
		if (validate(user))
			return new Html("/newUser.jsp", "user", user);
		String userId = Integer.toString(user.hashCode());
		user.setUserId(userId);
		roleAssigner.assignDomainEvents(user);

		UploadFile uploadFile = new UploadFile();
		FormFile file = (FormFile) request.getSession().getAttribute("formFile");
		if (file != null) {
			uploadFile.setParentId(userId);
			uploadFile.setData(file.getFileData());
			uploadFile.setName(file.getFileName());
			uploadFile.setContentType(file.getContentType());
		}
		user.es.created(new UserCreatedEvent(user, uploadFile));
		return new State("/result.jsp");
	}

	private boolean validate(UserModel user) {
		validation.use(new NotNull(user.getUsername())).message("name", "不能为空");
		validation.use(new NotEmail(user.getEmail())).message("email", "邮件格式不对");
		validation.use(new NotEqual(user.getPassword(), user.getVerifypassword())).message("password", "两次输入不一致");
		return validation.hasErrors() ? true : false;
	}

	public UserModel getUser(String userId) {
		logger.debug(" enter UserModel getUser(String userId) ");
		UserModel userModel = entityFactory.getUser(userId);
		return userModel;
	}

	@Path("/showUpload")
	public Represent showUpload(String pid) {
		UserModel user = entityFactory.getUser(pid);
		Image image = new Image();
		image.setData(user.getUploadFile().getData(), user.getUploadFile().getContentType());
		return image;
	}

	@Path("/user")
	@PUT
	public Represent update(UserModel user) {
		UserModel userold = getUser(user.getUserId());
		if (userold == null)
			return new State("/");

		UserModel oldUser = this.getUser(user.getUserId());
		FormFile file = (FormFile) request.getSession().getAttribute("formFile");
		UploadVO uploadVO = null;
		if (file != null) {
			uploadVO = new UploadVO(file.getFileName(), file.getFileData(), file.getContentType());
		}

		commandHandler.saveUser(oldUser, new UpdateCommand(user, uploadVO));
		return new State("/result.jsp");
	}

	@Path("/user/{user.userId}")
	@DELETE
	public Represent delete(UserModel user) {
		UserModel oldUser = this.getUser(user.getUserId());
		oldUser.es.deleted(new UserDeletedEvent(user.getUserId()));
		return new State("/result.jsp");
	}
}
