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
import javax.servlet.jsp.tagext.TagSupport;

public class IndexTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1301376274327149071L;

	private boolean disp = false;

	private String displayCount;

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

		String allCountStrs = (String) pageContext.getAttribute(MPageTag.ALLCOUNT);
		int allCount = Integer.parseInt(allCountStrs);

		StringBuilder buf = new StringBuilder(100);

		int numPages = 0;
		if (allCount != count) {
			numPages = (int) Math.ceil((double) allCount / (double) count);
		} else {
			numPages = 1;
		}

		// Calculate the starting point & end points (the count of pages to
		// display)
		// 5表示，前后显示数字5个
		int currentPage = 1;
		if (count > 0) {
			currentPage = (start / count) + 1;
		}

		if ((displayCount == null) || (displayCount.length() == 0))
			this.displayCount = "5"; // default 5
		int dispCount = Integer.parseInt(displayCount);
		int lo = currentPage - dispCount;
		if (lo <= 0) {
			lo = 1;
		}
		int hi = currentPage + dispCount;

		// print out event link to the first page if we're beyond that page
		if (lo > 2) {
			buf.append("<event href=\"").append(url);
			buf.append("\" class=\"paginator_href\" title=\"Go to the first page\">1</event> ... ");
		}

		// Print the page numbers before the current page
		while (lo < currentPage) {
			buf.append("<event href=\"").append(url);
			buf.append("&start=");
			buf.append(((lo - 1) * count));
			buf.append("\" class=\"paginator_href\">");
			buf.append("<b>");
			buf.append(lo);
			buf.append("</b></event>&nbsp;");
			lo++;
		}

		// Print the current page
		buf.append("<b><span class=\"paginator_currentPage\">");
		buf.append(currentPage);
		buf.append("</span></b>");

		currentPage++;

		// Print page numbers after the current page
		while ((currentPage <= hi) && (currentPage <= numPages)) {
			buf.append("&nbsp;");
			buf.append("<event href=\"").append(url);
			buf.append("&start=");
			buf.append(((currentPage - 1) * count));
			buf.append("\" class=\"paginator_href\">");
			buf.append("<b>");
			buf.append(currentPage);
			buf.append("</b></event>");
			currentPage++;
		}

		if (currentPage <= numPages) {
			buf.append(" ... ");
			buf.append("<event href=\"").append(url);
			buf.append("&start=");
			buf.append(((numPages - 1) * count));
			buf.append("\" class=\"paginator_href\" title=\"Go to the last page\">");
			buf.append(numPages);
		}

		JspWriter writer = pageContext.getOut();
		try {
			if (disp)
				writer.print(buf.toString());
		} catch (IOException e) {
			throw new JspException("PrevTag error");
		}

		return (EVAL_BODY_INCLUDE);

	}

	/**
	 * Render the end of the hyperlink.
	 * 
	 * @exception JspException
	 *                if event JSP exception has occurred
	 */
	public int doEndTag() throws JspException {

		return (EVAL_PAGE);

	}

	/**
	 * @return Returns the displayCount.
	 */
	public String getDisplayCount() {
		return displayCount;
	}

	/**
	 * @param displayCount
	 *            The displayCount to set.
	 */
	public void setDisplayCount(String displayCount) {
		this.displayCount = displayCount;
	}
}
