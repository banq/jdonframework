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

import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.jdon.strutsutil.file.filter.*;

/**
 * 无需继承实现 直接配置使用
 * 使用本上传Action时，需要在struts_config.xml配置
 *  <action name="uploadForm" type="com.jdon.strutsutil.file.UploadAction" input="/admin/uploadImg.jsp" scope="request" path="/admin/uploadAction">
 *      <forward name="display" path="/admin/uploadOk.jsp" />
 *  </action>
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p></p>
 * @author banq
 * @version 1.0
 */
public class MultiUploadAction extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

    if (form == null) {
      throw new Exception("please setup UploadImageForm in struts-config.xml");
    }
    MultiImageForm theForm = (MultiImageForm) form;
    FormFile[] files = theForm.getFiles();
    int length = files.length;

    ImageSessionFilter imageFilter = new ImageSessionFilter();

    UploadFile uploadFile = null;
    for (int i = 0; i < length; i++) {
      uploadFile = doFormFile(request, files[i]);
      uploadFile.setName(theForm.getNames()[i]);
      //保存到HttpSession中
      imageFilter.addUploadFile(request, uploadFile);
    }

    //return event forward to display.jsp
    return mapping.findForward("display");

  }

  private UploadFile doFormFile(HttpServletRequest request, FormFile file) throws
      Exception {
    UploadFile uploadFile = new UploadFile();
    try {
      //retrieve the file data
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      InputStream stream = file.getInputStream();

      byte[] buffer = new byte[8192];
      int bytesRead = 0;
      while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
        baos.write(buffer, 0, bytesRead);
      }

      byte[] data = baos.toByteArray();
      baos.close();
      //close the stream
      stream.close();

      if (data != null) {
        uploadFile.setData(data);
        uploadFile.setContentType(file.getContentType());
        uploadFile.setSize(file.getFileSize());

      }
    } catch (Exception ex) {
      throw new Exception(ex);
    } finally {
      //destroy the temporary file created
      file.destroy();
    }

    return uploadFile;
  }

}
