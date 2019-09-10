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

package com.jdon.strutsutil.treeview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jdon.util.Debug;

import java.util.*;

/**
 * 树形结构初始设置程序
 * 1. 生成根节点
 * 2. 从数据库获取根节点下子节点集合
 * 3. 创建TreeControl保存在HttpSession中
 *    getRootChildernNode方法返回的Collection是ViewNode的集合
 *
 * 和TreeControl和TreeControlTag 以及ViewNode组成树形视图显示。
 *
 *
 *
 */

public abstract class TreeAction extends Action {

  private final static String module = TreeAction.class.getName();

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws
      Exception {

    TreeControl control = getTreeControl(request);
    if (control.getRoot() == null) {
       if (request.getParameterMap().containsKey("init")) { //初始化树
         initRoot(request);
       }else{
         Debug.logError("[JdonFramework]please init the tree at first !");
       }
    } else {
      refreshTree(request, control);
    }
    return (mapping.findForward(TreeControlTag.TREE_NAME));
  }


  /**
   * 当调用无参数，看成是初始化，重新再从后台获得root
   *
     //root node 双向关联　control
     //每一个Root Node 对应一个control

   * @param request HttpServletRequest
   * @throws Exception
   * @return TreeControl
   */
  private TreeControl getTreeControl(HttpServletRequest request) throws
      Exception {
    HttpSession session = request.getSession();
    TreeControl control = (TreeControl) session.getAttribute(TreeControlTag.
        TREE_NAME);
    try{
      //第一次调用
      if ( (control == null) || (request.getParameterMap().isEmpty())) {
        Debug.logVerbose("[JdonFramework] get tree from db..", module);
        ViewNode root =  getRootViewNode(request);
        control = new TreeControl(root);
        fetchRootChildern(request, root);
        session.setAttribute(TreeControlTag.TREE_NAME, control);
      }
    }catch(Exception ex){
      Debug.logError("[JdonFramework] getTreeControl error: " + ex, module);
    }
    return  control;
  }

  /**
   * 根据后台Root得到显示的根节点
   * @param request HttpServletRequest
   * @throws Exception
   * @return ViewNode
   */
  private ViewNode getRootViewNode(HttpServletRequest request) throws Exception {
    String rooNodeID = getRootNodeID(request);
    if (rooNodeID == null){
      Debug.logError("[JdonFramework] root Id is null ! " , module);
      return null;
    }
    Debug.logVerbose("[JdonFramework] get the tree's root Node: " + rooNodeID, module);
    ViewNode rNode = new ViewNode(rooNodeID, rooNodeID, true);
    rNode.setRoot(true);
    return rNode;
  }

  public void fetchRootChildern(HttpServletRequest request, ViewNode root)
      throws   Exception {
    Collection childernNodeOfRoot = null;
    try {
      childernNodeOfRoot = getRootChildern(request);

      Iterator iter = childernNodeOfRoot.iterator();
      while (iter.hasNext()) {
        ViewNode nodeChild = (ViewNode) iter.next();
        root.addChild(nodeChild);
      }
    } catch (Exception ex) {
      Debug.logError(ex, module);
      throw new Exception(" tree not init ");
    }
  }

  /**
   * 刷新树
   * @param request HttpServletRequest
   * @param control TreeControl
   * @throws Exception
   */
  private void refreshTree(HttpServletRequest request, TreeControl control) throws Exception {
    String key = request.getParameter("key");
    if (key != null) {
      ViewNode node = control.findNode(key);
      if (node != null) {
        Debug.logVerbose("[JdonFramework]Found Node: " + key, module);
        if (!node.isExpanded()) {
          //原来是叠合的，现在需要展开
          //需要查询数据库，将该节点下的子节点查询获取
          node.clearChildren();
          fetchChildern(request, node);
        }
        node.setExpanded(!node.isExpanded());
      } else {
        Debug.logVerbose("[JdonFramework]Node is null : " + key);
      }
    }
    // Handle event select item event
    String select = request.getParameter("select");
    if (select != null) {
        control.selectNode(select);
    }

  }


  /**
   * 需要查询数据库，将该节点下的子节点查询获取
   * @param request
   * @param node
   */
  public void fetchChildern(HttpServletRequest request, ViewNode node) {
    Collection childernNode = null;
    try {
      childernNode = getChildern(request, node);

      Iterator iter = childernNode.iterator();
      while (iter.hasNext()) {
        ViewNode nodeChild = (ViewNode) iter.next();
        node.addChild(nodeChild);
      }
    } catch (Exception ex) {
      Debug.logError(ex, module);
    }
  }


  public abstract Collection getRootChildern(HttpServletRequest request) throws
      Exception;

  public abstract String getRootNodeID(HttpServletRequest request) throws Exception;

  public abstract void initRoot(HttpServletRequest request) throws Exception;

  public abstract Collection getChildern(HttpServletRequest request,
                                         ViewNode node) throws Exception;

}
