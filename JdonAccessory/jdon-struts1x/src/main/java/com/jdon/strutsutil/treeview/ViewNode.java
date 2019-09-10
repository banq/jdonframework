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
 * 节点
 * 
 * 包含父节点和子节点的上下三级。
 * 
 * 父节点<-- 当前节点 -->子节点
 * 
 * 采取类似游标的形式，整个树就是由这样的节点上下衔接在一起的。
 * 
 * 
 * 一个节点包含三个部分的信息 1.节点的相关树信息 2.节点的相关显示信息 3.节点的属性信息
 * 
 * 节点和TreeControl属于n:1的关联关系
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

public class ViewNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3056749981121534528L;
	// 是否是兄弟姐妹中最后一个
	private boolean last = false;
	// 是否是叶（无子节点）
	private boolean leaf = false;

	// 本Node是否被选择
	private boolean selected = false;
	// 是否展开显示其所有的子节点
	private boolean expanded = false;

	// 该节点的父节点
	private ViewNode parent = null;
	// 该节点的深度
	private int depth = 0;

	// 树形结构对象
	private TreeControl tree = null;
	// 该节点的所有子节点
	private List children = new ArrayList();

	// 节点的关键字
	protected String key = null;
	// 节点的label标识名称
	protected String label = null;

	// 后台是否更新
	protected boolean root = false;

	// ---------------------------------节点在树结构中的属性

	public boolean isLast() {
		return (this.last);
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isLeaf() {
		return (this.leaf);
	}

	public void setTree(TreeControl tree) {
		this.tree = tree;
	}

	public void clearChildren() {
		children.clear();
	}

	/**
	 * Return the set of child nodes for this node.
	 */
	public ViewNode[] findChildren() {
		synchronized (children) {
			ViewNode results[] = new ViewNode[children.size()];
			return ((ViewNode[]) children.toArray(results));
		}
	}

	/**
	 * 增加子节点
	 * 
	 * @param child
	 * @throws java.lang.IllegalArgumentException
	 */
	public void addChild(ViewNode child) throws IllegalArgumentException {
		tree.addNode(child);
		child.setParent(this);
		this.leaf = false; // 一旦增加子节点，该节点就不是叶了。
		int n = children.size();
		if (n > 0) {
			ViewNode node = (ViewNode) children.get(n - 1);
			node.setLast(false);
		}
		child.setLast(true);
		children.add(child);
	}

	public ViewNode getParent() {
		return (this.parent);
	}

	public void setParent(ViewNode parent) {
		this.parent = parent;
		if (parent == null)
			depth = 1;
		else
			depth = parent.getDepth() + 1;
	}

	public int getDepth() {
		return depth;
	}

	// --------------------------------- 节点在树结构中的属性　结束

	// --------------------------------- 与动态显示相关

	public boolean isSelected() {
		return (this.selected);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isExpanded() {
		return (this.expanded);
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	// --------------------------------- 与动态显示相关　结束

	// --------------------------------- 节点的通用属性

	public String getKey() {
		return (this.key);
	}

	public String getLabel() {
		return (this.label);
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	// --------------------------------- 节点的通用属性　结束

	public ViewNode(String key, String label, boolean leaf) {
		super();
		this.key = key;
		this.label = label;
		this.leaf = leaf;
	}

}
