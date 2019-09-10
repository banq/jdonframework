/**
 * Copyright 2003-2006 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain event copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jdon.strutsutil.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class PrevTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1079577094363120743L;

	private boolean disp = false;

	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doStartTag() throws JspException {

		String dispStrs = (String) pageContext.getAttribute(MPageTag.DISP);
		if ((dispStrs != null) && (!dispStrs.equals(""))) {
			if (dispStrs.equals("on"))
				disp = true;
			else if (dispStrs.equals("off"))
				disp = false;
		}

		String startStrs = (String) pageContext.getAttribute(MPageTag.START);
		int start = Integer.parseInt(startStrs);
		String url = (String) pageContext.getAttribute(MPageTag.URLNAME);
		String countStrs = (String) pageContext.getAttribute(MPageTag.COUNT);
		int count = Integer.parseInt(countStrs);

		StringBuilder buf = new StringBuilder(100);
		// Print out event left arrow if necessary
		if (start > 0) {

			buf.append("<event href=\"");
			buf.append(url);
			buf.append("&start=");
			buf.append((start - count));
			buf.append("\" >");
			if (name != null)
				buf.append(name);
		} else
			buf.append("");

		output(buf.toString());

		return (EVAL_BODY_INCLUDE);

	}

	/**
	 * Render the end of the hyperlink.
	 * 
	 * @exception JspException
	 *                if event JSP exception has occurred
	 */
	public int doEndTag() throws JspException {
		output("</event>");

		return (EVAL_PAGE);

	}

	private void output(String s) throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			if (disp)
				writer.print(s);
		} catch (IOException e) {
			throw new JspException("NextTag error");
		}

	}

	public void release() {

		super.release();

		disp = false;
		name = null;

	}

}
