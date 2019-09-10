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

package com.jdon.strutsutil.file.filter;

import javax.servlet.http.*;
import com.jdon.strutsutil.file.UploadFile;
import java.util.*;

public interface ImageFilter {

  /**
   * 获得所有上传文件的ID集合
   * @param request
   * @return
   */
  public Collection getUploadFileID(HttpServletRequest request);

  /**
   * 获得某个id的上传文件对象
   * @param request
   * @param id
   * @return
   */
  public UploadFile getUploadFile(HttpServletRequest request, String id);

  /**
   * 删除某个id的上传文件
   * @param request
   * @param uploadFile
   */
  public void deleteUploadFile(HttpServletRequest request, UploadFile uploadFile);

  /**
   * 加入新的上传文件
   * **/
  public void addUploadFile(HttpServletRequest request, UploadFile uploadFile);

  /**
   * 取走本次的上传图片
   * @param request
   * @return
   */
  public Collection loadAllUploadFile(HttpServletRequest request);

}
