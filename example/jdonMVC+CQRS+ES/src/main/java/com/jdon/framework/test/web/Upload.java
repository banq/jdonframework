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
package com.jdon.framework.test.web;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.jdon.framework.test.repository.EntityFactory;
import com.jdon.mvc.annotations.In;
import com.jdon.mvc.http.FormFile;
import com.jdon.mvc.ioc.BeanType;
import com.jdon.mvc.represent.Represent;
import com.jdon.mvc.represent.Text;

/**
 * User: oojdon Date: 11-9-14 Time: 上午12:06
 */
public class Upload {

	private @Context
	HttpServletRequest request;
	private @Context
	HttpSession session;
	private @Context
	HttpServletResponse response;
	private @Context
	ServletContext webApp;

	@In(value = "entityFactory", type = BeanType.COMPONENT)
	private EntityFactory entityFactory;

	@POST
	@Path("/singleupload")
	public Represent upload(FormFile file) {
		request.getSession().setAttribute("formFile", file);
		return new Text("你上传文件名是：" + file.getFileName());
	}

	@POST
	@Path("/multiupload")
	public Represent upload(List<FormFile> files) {
		request.getSession().setAttribute("formFiles", files);
		return new Text("你上传的文件个数是：" + files.size());
	}

}
