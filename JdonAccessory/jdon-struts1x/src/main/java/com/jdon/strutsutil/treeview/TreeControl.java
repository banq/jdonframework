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

import java.io.Serializable;
import java.util.*;

/**
 * 树形结构控制类，被类被保存在用户Session中， 用以记载树形结构的一些特点。
 * 
 * 
 * ViewNode是记录节点微观信息，聚焦每个节点 该类则关注根节点和所有节点。
 * 
 * 和节点ViewNode共同使用，可记录树形结构的全部信息
 * 
 */

public class TreeControl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5384472151297891954L;
	public static boolean init = true; // 是否初始化
	// 当前选中的节点
	private ViewNode selected = null;
	private ViewNode root = null;
	private HashMap registry = new HashMap();

	public TreeControl(ViewNode root) {
		this.root = root;
		root.setTree(this);
		root.setLast(true);
	}

	public ViewNode getRoot() {
		return (this.root);
	}

	public ViewNode findNode(String key) {
		return ((ViewNode) registry.get(key));
	}

	public void selectNode(String key) {
		if (selected != null) {
			selected.setSelected(false);
			selected = null;
		}
		selected = findNode(key);
		if (selected != null)
			selected.setSelected(true);

	}

	public void addNode(ViewNode node) throws IllegalArgumentException {
		node.setTree(this);
		registry.put(node.getKey(), node);
	}

	public int getTreeWidth() {
		if (root == null)
			return (0);
		else
			return (getWidth(root));

	}

	/**
	 * 该节点到树底端的距离 与node.getDepth()区别： 后者是该节点到树顶的距离。
	 * 
	 * @param node
	 * @return
	 */
	public int getWidth(ViewNode node) {
		int width = node.getDepth();
		ViewNode children[] = node.findChildren();
		for (int i = 0; i < children.length; i++) {
			int current = getWidth(children[i]);
			if (current > width)
				width = current;
		}
		return (width);
	}

}
