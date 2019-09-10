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

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 树形结构显示 类似javascript实现功能，主要是输出Html语句，用于显示树形结构。
 * 
 * 
 * ViewNode :表示三层关系节点 TreeControl：整个树形结构节点的保存器（HashMap）
 * 本类根据以上两个基类，以level和width两个变量来绘制table表格。
 * 
 * 
 * <TreeView:tree treeAction="updateTreeAction.do"
 * nodeAction="categoryAction.do?action=edit&catId=${key}" target="content" />
 * 
 * 
 */
public class TreeControlTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2869394702697666412L;

	/**
	 * The default directory name for icon images.
	 */
	private static final String DEFAULT_IMAGES = "images";

	public static final String TREE_NAME = "tree-control";
	public static final String TREE_NAME_INIT = "tree-control-init";

	private static final String CSS_STYLE = "tree-control";
	private static final String CSS_STYLE_SELECTED = "tree-control-selected";
	private static final String CSS_STYLE_UN_SELECTED = "tree-control-unselected";

	/**
	 * The names of tree state images that we need.
	 */
	private static final String IMAGE_HANDLE_DOWN_LAST = "handledownlast.gif";
	private static final String IMAGE_HANDLE_DOWN_MIDDLE = "handledownmiddle.gif";
	private static final String IMAGE_HANDLE_RIGHT_LAST = "handlerightlast.gif";
	private static final String IMAGE_HANDLE_RIGHT_MIDDLE = "handlerightmiddle.gif";
	private static final String IMAGE_LINE_LAST = "linelastnode.gif";
	private static final String IMAGE_LINE_MIDDLE = "linemiddlenode.gif";
	private static final String IMAGE_LINE_VERTICAL = "linevertical.gif";

	// ------------------------------------------------------------- Properties

	protected String treeAction = null;

	public String getTreeAction() {
		return (this.treeAction);
	}

	public void setTreeAction(String treeAction) {
		this.treeAction = treeAction;
	}

	protected String nodeAction = null;

	public String getNodeAction() {
		return (this.nodeAction);
	}

	public void setNodeAction(String nodeAction) {
		this.nodeAction = nodeAction;
	}

	protected String target = null;

	public String getTarget() {
		return (this.target);
	}

	public void setTarget(String target) {
		this.target = target;
	}

	protected String images = DEFAULT_IMAGES;

	public String getImages() {
		return (this.images);
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int doEndTag() throws JspException {

		TreeControl treeControl = (TreeControl) pageContext.findAttribute(TREE_NAME);
		JspWriter out = pageContext.getOut();

		try {
			if (treeControl == null) {
				out.print("Please login again, or refresh again");
				return (EVAL_PAGE);
			}

			out.print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"");
			out.print(" class=\"");
			out.print(CSS_STYLE);
			out.print("\"");
			out.println(">");
			int level = 0;
			ViewNode rootNode = treeControl.getRoot();
			render(out, rootNode, level, treeControl.getTreeWidth(), true);
			out.println("</table>");
		} catch (IOException e) {
			throw new JspException(e);
		} catch (Exception e) {
			throw new JspException("Please refresh this page!");
		}

		return (EVAL_PAGE);

	}

	// level是当前层
	protected void render(JspWriter out, ViewNode node, int level, int width, boolean last) throws IOException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String url_Path = request.getContextPath();

		// Debug.logVerbose("[JdonFramework] node key=" + node.getKey() +
		// " level=" + level +
		// " width=" + width + " last=" + last, module);

		// 如果是根节点或无标签，不显示该节点
		if (node.isRoot()) {
			// Render the children of this node
			ViewNode children[] = node.findChildren();
			// Debug.logVerbose("[JdonFramework] children count =" +
			// children.length);
			int lastIndex = children.length - 1;
			int newLevel = level + 1;
			for (int i = 0; i < children.length; i++) {
				render(out, children[i], newLevel, width, i == lastIndex);
			}
			return;
		}

		// Render the beginning of this node
		out.println("  <tr valign=\"middle\">");

		for (int i = 0; i < level; i++) {
			int levels = level - i;
			ViewNode parent = node;
			for (int j = 1; j <= levels; j++)
				parent = parent.getParent();
			if (parent.isLast())
				out.print("    <td></td>");
			else { // 显示竖线
				out.print("    <td><img src=\"");
				out.print(images);
				out.print("/");
				out.print(IMAGE_LINE_VERTICAL);
				out.print("\" alt=\"\" border=\"0\"></td>");
			}
			out.println();
		}

		displayNodePic(out, node, url_Path);
		displayNode(out, node, url_Path, level, width);

		// Render the end of this node
		out.println("  </tr>");

		// Render the children of this node
		if (node.isExpanded()) {
			ViewNode children[] = node.findChildren();
			int lastIndex = children.length - 1;
			int newLevel = level + 1;
			for (int i = 0; i < children.length; i++) {
				render(out, children[i], newLevel, width, i == lastIndex);
			}
		}

	}

	/**
	 * 
	 * 显示节点前图形符号　　开始
	 * 
	 * 如果该节点有子节点，符号显示IMAGE_HANDLE图形 注：点按IMAGE_HANDLE，可以展开所有的子节点
	 * 如果无子节点，显示IMAGE_LINE
	 */
	private void displayNodePic(JspWriter out, ViewNode node, String url_Path) throws IOException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		StringBuilder url = new StringBuilder(url_Path);
		url.append(treeAction);
		if (treeAction.indexOf("?") < 0)
			url.append("?");
		else
			url.append("&");
		url.append("key=").append(node.getKey());

		out.print("    <td>");
		if ((nodeAction != null) && !node.isLeaf()) {
			out.print("<event href=\"");
			out.print(response.encodeURL(url.toString()));
			out.print("\">");
		}
		out.print("<img src=\"");
		out.print(images);
		out.print("/");
		if (node.isLeaf()) {
			if (node.isLast())
				out.print(IMAGE_LINE_LAST);
			else
				out.print(IMAGE_LINE_MIDDLE);
			out.print("\" alt=\"");
		} else if (node.isExpanded()) {
			if (node.isLast())
				out.print(IMAGE_HANDLE_DOWN_LAST);
			else
				out.print(IMAGE_HANDLE_DOWN_MIDDLE);
			out.print("\" alt=\"close node");
		} else {
			if (node.isLast())
				out.print(IMAGE_HANDLE_RIGHT_LAST);
			else
				out.print(IMAGE_HANDLE_RIGHT_MIDDLE);
			out.print("\" alt=\"expand node");
		}
		out.print("\" border=\"0\">");
		if ((nodeAction != null) && !node.isLeaf())
			out.print("</event>");
		out.println("</td>");
	}

	/**
	 * 显示节点本身信息，如节点名称等
	 * 
	 * @throws IOException
	 */
	private void displayNode(JspWriter out, ViewNode node, String url_Path, int level, int width) throws IOException {

		// ----------------------------------------显示节点本身信息，如节点名称等开始
		String encodedNodeKey = URLEncoder.encode(node.getKey(), "UTF-8");
		String action = replace(nodeAction, "${key}", encodedNodeKey);
		StringBuilder url = new StringBuilder(url_Path);
		url.append(action);
		if (nodeAction.indexOf("?") < 0)
			url.append("?");
		else
			url.append("&");
		url.append("key=").append(node.getKey());
		String treeNodeAction = url.toString();

		url = new StringBuilder(url_Path);
		url.append(treeAction);
		if (treeAction.indexOf("?") < 0)
			url.append("?");
		else
			url.append("&");
		url.append("select=").append(node.getKey());
		String updateNodeAction = url.toString();

		// Render the label for this node (if any)
		out.print("    <td colspan=\"");
		out.print(width - level + 1);
		out.print("\">");

		// 这里省略了icon

		if (node.getLabel() != null) {
			String labelStyle = null;
			if (node.isSelected())
				labelStyle = CSS_STYLE_SELECTED;
			else if (!node.isSelected())
				labelStyle = CSS_STYLE_UN_SELECTED;
			if (treeNodeAction != null) {
				// Note the leading space so that the text has some space
				// between it and any preceding images
				out.print(" <event href=\"");
				out.print(treeNodeAction);
				out.print("\"");
				if (target != null) {
					out.print(" target=\"");
					out.print(target);
					out.print("\"");
				}
				if (labelStyle != null) {
					out.print(" class=\"");
					out.print(labelStyle);
					out.print("\"");
				}
				// to refresh the tree in the same 'self' frame
				out.print(" onclick=\"");
				out.print("self.location.href='" + updateNodeAction + "'");
				out.print("\"");
				out.print(">");
			} else if (labelStyle != null) {
				out.print("<span class=\"");
				out.print(labelStyle);
				out.print("\">");
			}
			out.print(node.getLabel());
			if (treeNodeAction != null)
				out.print("</event>");
			else if (labelStyle != null)
				out.print("</span>");
		}

		out.println("</td>");

		// ----------------------------------------显示节点本身信息，如节点名称等结束

	}

	/**
	 * Replace any occurrence of the specified placeholder in the specified
	 * template string with the specified replacement value.
	 * 
	 * @param template
	 *            Pattern string possibly containing the placeholder
	 * @param placeholder
	 *            Placeholder expression to be replaced
	 * @param value
	 *            Replacement value for the placeholder
	 */
	protected String replace(String template, String placeholder, String value) {

		if (template == null)
			return (null);
		if ((placeholder == null) || (value == null))
			return (template);
		while (true) {
			int index = template.indexOf(placeholder);
			if (index < 0)
				break;
			StringBuilder temp = new StringBuilder(template.substring(0, index));
			temp.append(value);
			temp.append(template.substring(index + placeholder.length()));
			template = temp.toString();
		}
		return (template);

	}

}
