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

import com.jdon.strutsutil.file.*;
import javax.servlet.http.*;
import java.util.*;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

public class ImageSessionFilter implements ImageFilter {

  public final static String module = ImageSessionFilter.class.getName();

  private final static String PIC_NAME_PACKAGE = "PicturesPakage";

  private ImageFilter imageFilter = null;

  public ImageSessionFilter(ImageFilter imageFilter) {
    this.imageFilter = imageFilter;

  }

  /**
   * 有些功能直接操作ImageSessionFilter()为止
   */
  public ImageSessionFilter() {
  }

  /**
   * 将HttpSession中的ID集合和老的imageFilterID集合合并
   *
   * 保证每个ID只有一个。
   *
   * @param request
   * @return
   */
  public Collection getUploadFileID(HttpServletRequest request) {
    Collection set = null;
    Debug.logVerbose("[JdonFramework]--> enter session filter ", module);
    try {
       //保证一个id只有一个

      //从下一个Filter取出ID集合，没有重复
      Collection list = imageFilter.getUploadFileID(request);
      if (list == null)
        set = new LinkedHashSet();
      else
        set = new LinkedHashSet(list);

      HttpSession session = request.getSession();
      Collection sessionList = (Collection) session.getAttribute(PIC_NAME_PACKAGE);
      if (sessionList == null) return set;

      //确保Session中的ID覆盖下一个Filter中ID
      Iterator  iter = sessionList.iterator();
      while (iter.hasNext()) {
        UploadFile uploadFile = (UploadFile) iter.next();
        if ( (uploadFile.getId() != null) && (!uploadFile.getId().equals(""))) {
          set.add(uploadFile.getId());
        } else { //如果Session有多于下一个Filter的ID，以排列TempId加入
          set.add(uploadFile.getTempId());
        }
      }
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] getUploadFileID error" + ex, module);
    }
    return set;

  }

  /**
   * 根据UploadFile的id获得UploadFile实例
   * @param request
   * @param id
   * @return
   */
  public UploadFile getUploadFile(HttpServletRequest request, String id) {
    UploadFile uploadFile = null;
    Debug.logVerbose("[JdonFramework]--> getUploadFile session filter id=" + id, module);
    try {
      uploadFile = getUploadFileFromSession(request, id);
      if (uploadFile == null) {
        uploadFile = imageFilter.getUploadFile(request, id);
      }
    } catch (Exception ex) {
      Debug.logWarning(" getUploadFile error" + ex, module);

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
      String imageId = null;
      //首先先删除Session
     if (!UtilValidate.isEmpty(uploadFile.getTempId())) {
        imageId = uploadFile.getTempId();
        deleteUploadFileFromSession(request, imageId);
      }else  if (!UtilValidate.isEmpty(uploadFile.getId())) {
       imageId = uploadFile.getId();
       deleteUploadFileFromSession(request, imageId);
       imageFilter.deleteUploadFile(request, uploadFile);
      } else
        throw new Exception("parameter id  and tempId all is null");

    } catch (Exception ex) {
      Debug.logError("[JdonFramework] deleteUploadFile error:" + ex, module);
    }

  }

  /**
   * 逐个UploadFile新增保存到Http Session
   * @param request
   * @param uploadFile
   */
  public void addUploadFile(HttpServletRequest request,
                            UploadFile uploadFile) {
    if (uploadFile == null)
      return;
    try{
      HttpSession session = request.getSession(true);
      Collection uploadList = (Collection)session.getAttribute(PIC_NAME_PACKAGE);
      if (uploadList == null) {
        uploadList = new ArrayList();
        session.setAttribute(PIC_NAME_PACKAGE, uploadList);
      }
      uploadList.add(uploadFile);
      uploadFile.setTempId(Integer.toString(uploadList.size() - 1)); //临时给予id
    }catch(Exception ex){
      Debug.logError("[JdonFramework]addUploadFile error:" + ex, module);
    }

  }

  /**
   * 一次性将之前保存的所有UploadFile集合取出，然后从Session中删除
   *
   * 获得前一个步骤上传的数据
   *
   * 文件上传是在专门页面操作，操作成功后，其它页面通过本方法获得数据。
   *
   * @param request
   * @return UploadFile 集合
   */
  public Collection loadAllUploadFile(HttpServletRequest request) {
    Debug.logVerbose("[JdonFramework] load all upload files from session and remove them", module);
    Collection uploadList = null;
    try {
      HttpSession session = request.getSession();
      if (session != null) {
        uploadList = (Collection) session.getAttribute(PIC_NAME_PACKAGE);
        session.removeAttribute(PIC_NAME_PACKAGE);
      }
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] not found the upload files in session" + ex, module);
    }
    return uploadList;
  }

  /**
   * 取出保存的所有UploadFile集合，但不从Session删除这些集合
   * @param request
   * @return
   */
  public Collection getAllUploadFile(HttpServletRequest request) {

    Collection uploadList = null;
    try {
      HttpSession session = request.getSession();
      if (session != null) {
        uploadList = (Collection) session.getAttribute(PIC_NAME_PACKAGE);
      }
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] not found the upload files in session" + ex, module);
    }
    return uploadList;
  }


  /**
   * 根据UploadFile的id获得UploadFile实例
   * @param request
   * @param id
   * @return
   */
  private UploadFile getUploadFileFromSession(HttpServletRequest request,
                                              String id) {
    UploadFile uploadFile = null;
    boolean found = false;
    try {
      HttpSession session = request.getSession(true);
      Collection uploadList = (Collection) session.getAttribute(PIC_NAME_PACKAGE);
      if (uploadList == null)
        return null;
      Iterator iter = uploadList.iterator();
      while (iter.hasNext()) {
        uploadFile = (UploadFile) iter.next();
        if (uploadFile.getId().equals(id)){
          found = true;
          break;
        }else if (uploadFile.getTempId().equals(id)){
          found = true;
          break;
        }
      }
      if (!found )  uploadFile = null;
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] getUploadFileSession error" + ex, module);

    }
    return uploadFile;

  }

  /**
   * 删除图片
   * @param request
   * @param uploadFile
   */
  private void deleteUploadFileFromSession(HttpServletRequest request,
                                           String id) {
    try {

      HttpSession session = request.getSession(true);
      Collection uploadList = (Collection) session.getAttribute(PIC_NAME_PACKAGE);
      if ( (uploadList == null) || (id == null))
        return;

      UploadFile uploadFile = null;
      boolean found = false;
      Iterator iter = uploadList.iterator();
      while (iter.hasNext()) {
        Object o = iter.next();
        Debug.logError("[JdonFramework] object: " + o.getClass().getName(), module);
        uploadFile = (UploadFile) o;
        if (uploadFile.getId().equals(id)){
          found = true;
          break;
        }else if (uploadFile.getTempId().equals(id)){
          found = true;
          break;
        }
      }
      if (found )
        uploadList.remove(uploadFile);
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] deleteUploadFileFromSession error" + ex, module);

    }

  }

}
