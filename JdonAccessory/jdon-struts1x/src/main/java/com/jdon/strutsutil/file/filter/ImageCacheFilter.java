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

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.jdon.strutsutil.file.UploadFile;
import com.jdon.util.Debug;

public class ImageCacheFilter implements ImageFilter {
  public final static String module = ImageCacheFilter.class.getName();

  private ImageFilter imageFilter = null;

  public ImageCacheFilter(ImageFilter imageFilter) {
    this.imageFilter = imageFilter;

  }

  /**
   * ID集合暂时不做缓存
   * @param request
   * @return
   */
  public Collection getUploadFileID(HttpServletRequest request) {
    Debug.logVerbose("[JdonFramework]--> enter cache filter ", module);
    Collection uploadIDList = null;
    try {
      uploadIDList = imageFilter.getUploadFileID(request);
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] not found the upload files in session", module);
    }
    return uploadIDList;

  }

  /**
   * 根据UploadFile的id获得UploadFile实例
   * @param request
   * @param id
   * @return
   */
  public UploadFile getUploadFile(HttpServletRequest request, String id) {
    Debug.logVerbose("[JdonFramework]--> enter cache filter ", module);
    UploadFile uploadFile = null;
    try {
//      uploadFile = (UploadFile) getImageFromCache(id);
      if (uploadFile == null) {
        uploadFile = imageFilter.getUploadFile(request, id);
//        if (uploadFile != null)
//          saveImageToCache(id, uploadFile); //空白图片不放入了缓存,
      }
    } catch (Exception ex) {
      Debug.logError("[JdonFramework]getData error:" + ex, module);

    }
    return uploadFile;
  }

  /**
   * 根据UploadFile的id删除UploadFile实例
   * @param request
   * @param id
   */
  public void deleteUploadFile(HttpServletRequest request,
                               UploadFile uploadFile) {
    try {
//      removeImageFromCache(uploadFile.getId());
      imageFilter.deleteUploadFile(request, uploadFile);
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] deleteUploadFile error" + ex, module);
    }

  }

  public void addUploadFile(HttpServletRequest request, UploadFile uploadFile) {

  }

  public Collection loadAllUploadFile(HttpServletRequest request) {
    return null;
  }
}
