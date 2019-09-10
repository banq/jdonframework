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

import org.apache.struts.action.*;
import javax.servlet.http.*;

import java.io.*;
import com.jdon.util.Debug;

import com.jdon.strutsutil.file.UploadFile;
import com.jdon.strutsutil.file.filter.*;

import com.jdon.util.UtilValidate;

/**
 *  显示图片 Action
 *  需要继承实现。
 *
 * 参数id, 每个图片的id
 * 参数为tempId，取出HttpSession中当前图片
 *
 * <action type="com.jdon.estore.web.catalog.ProductImgShowAction" validate="false" path="/imageShowAction" />
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p></p>
 * @author banq
 * @version 1.0
 */
public abstract class ImageShowAction extends Action {
  public final static String module = ImageShowAction.class.getName();

  private static final byte[] BLANK = {
      71, 73, 70, 56, 57, 97, 1, 0, 1, 0, -111, 0, 0, 0, 0, 0, -1, -1, -1, -1,
      -1, -1, 0, 0, 0, 33, -7, 4, 1, 0, 0, 2, 0, 44, 0, 0, 0, 0, 1, 0, 1, 0,
      0, 2, 2, 76, 1, 0, 59};

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse response) throws
      Exception {

    String Id = request.getParameter("id");
    String tempId = request.getParameter("tempId");

    String imageId = null;
    if (!UtilValidate.isEmpty(tempId))
      imageId = tempId;
    else if (!UtilValidate.isEmpty(Id))
      imageId = Id;
    else
      throw new Exception("parameter id  and tempId all is null");

    UploadFile uploadFile = null;
    try {

      ImageFilter imageCacheFilter = new ImageCacheFilter(getImageFilter());
      ImageFilter imageFilter = new ImageSessionFilter(imageCacheFilter);

      uploadFile = imageFilter.getUploadFile(request, imageId);
      if (uploadFile != null)
        outImage(response, uploadFile.getData());
      else
        outImage(response, BLANK);
    } catch (Exception ex) {
      Debug.logError("[JdonFramework]get the image error:" + ex, module);

    }
    return null;
  }

  private void outImage(HttpServletResponse response, byte[] data) throws
      Exception {
    response.setContentType("images/jpeg");
    OutputStream toClient = response.getOutputStream();
    try {

      toClient.write(data);

    } catch (Exception ex) {
      Debug.logError("[JdonFramework]get the image error:" + ex, module);
    } finally {
      toClient.close();
    }

  }

  /**
   * 从持久层获得图片数据。
   * @param request
   * @return byte[]
   */
  public abstract ImageFilter getImageFilter();

}
