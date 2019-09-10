/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.strutsutil.file;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import java.util.*;

/**
 * 一次性上传多个图片Form
 * 
 * 可以Override本ActionForm,设计自己的UploadImageForm
 * 
 * 1. 配置如下 <form-bean name="uploadForm"
 * type="com.jdon.strutsutil.file.UploadImageForm" /> 2
 * .注意，需要配置上传文件大小限制，在struts-config.xml有： <controller maxFileSize="100K" /> 3.
 * 配置ApplicationResoureces maxLengthExceeded=The maximum upload length has been
 * exceeded by the client. notImage=this is not Image.
 * 
 * <p>
 * Copyright: Jdon.com Copyright (c) 2003
 * </p>
 * <p>
 * </p>
 * 
 * @author banq
 * @version 1.0
 */
public class MultiImageForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1485700498155979477L;

	public final static String module = MultiImageForm.class.getName();

	/** 每张图片大小，由 struts-config.xml maxFileSize 设置 */
	private static String ERROR_PROPERTY_MAX_LENGTH_EXCEEDED = "com.jdon.strutsutil.file.MaxLengthExceeded";

	/** 最多图片数 是2 */
	public static int MAX_IMAGES_COUNT = 2;

	private Map fileMap = new HashMap();
	private Map nameMap = new HashMap();

	public FormFile getFile(int index) {
		return (FormFile) fileMap.get(new Integer(index));
	}

	public void setFile(int index, FormFile file) {
		fileMap.put(new Integer(index), file);
	}

	public FormFile[] getFiles() {
		return (FormFile[]) fileMap.values().toArray(new FormFile[fileMap.size()]);
	}

	public String getName(int index) {
		return (String) nameMap.get(new Integer(index));
	}

	public void setName(int index, String name) {
		nameMap.put(new Integer(index), name);
	}

	public String[] getNames() {
		return (String[]) nameMap.values().toArray(new String[0]);
	}

	/**
	 * Check to make sure the client hasn't exceeded the maximum allowed upload
	 * size inside of this validate method.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = null;
		// has the maximum length been exceeded?
		Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
		if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
			errors = new ActionErrors();
			errors.add(ERROR_PROPERTY_MAX_LENGTH_EXCEEDED, new ActionMessage("maxLengthExceeded"));
		} else if (fileMap.size() > MAX_IMAGES_COUNT) {
			errors = new ActionErrors();
			errors.add(ERROR_PROPERTY_MAX_LENGTH_EXCEEDED, new ActionMessage("maxLengthExceeded"));
		} else {
			// retrieve the file name
			Iterator iter = fileMap.values().iterator();
			while (iter.hasNext()) {
				FormFile file = (FormFile) iter.next();
				String fileName = file.getFileName();
				if ((!fileName.toLowerCase().endsWith(".gif")) && !(fileName.toLowerCase().endsWith(".jpg"))
						&& !(fileName.toLowerCase().endsWith(".png"))) {
					errors = new ActionErrors();
					errors.add("notImage", new ActionMessage("notImage"));
				}
			}
		}
		return errors;
	}

}
