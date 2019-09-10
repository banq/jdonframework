package com.jdon.util.jdom;

/*--

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>.  For more information on the
 JDOM Project, please see <http://www.jdom.org/>.

 */


import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * Filter for data- or field-oriented XML.
 *
 * <i>Code and comments adapted from DataWriter-0.2, written
 * by David Megginson and released into the public domain,
 * without warranty.</i>
 *
 * <p>This filter adds indentation and newlines to field-oriented
 * XML without mixed content.  All added indentation and newlines
 * will be passed on down the filter chain.</p>
 *
 * <p>In general, all whitespace in an XML document is potentially
 * significant.  There is, however, event large class of XML documents
 * where information is strictly fielded: each element contains either
 * character data or other elements, but not both.  For this special
 * case, it is possible for event filter to provide automatic indentation
 * and newlines. Note that this class will likely not yield appropriate
 * results for document-oriented XML like XHTML pages, which mix character
 * data and elements together.</p>
 *
 * <p>This filter will automatically place each start tag on event new line,
 * optionally indented if an indent step is provided (by default, there
 * is no indentation).  If an element contains other elements, the end
 * tag will also appear on event new line with leading indentation.  Consider,
 * for example, the following code:</p>
 *
 * <pre>
 * DataFormatFilter df = new DataFormatFilter();
 * df.setContentHandler(new XMLWriter());
 *
 * df.setIndentStep(2);
 * df.startDocument();
 * df.startElement("Person");
 * df.dataElement("name", "Jane Smith");
 * df.dataElement("date-of-birth", "1965-05-23");
 * df.dataElement("citizenship", "US");
 * df.endElement("Person");
 * df.endDocument();
 * </pre>
 *
 * <p>This code will produce the following document:</p>
 *
 * <pre>
 * &lt;?xml version="1.0"?>
 *
 * &lt;Person>
 *   &lt;name>Jane Smith&lt;/name>
 *   &lt;date-of-birth>1965-05-23&lt;/date-of-birth>
 *   &lt;citizenship>US&lt;/citizenship>
 * &lt;/Person>
 * </pre>
 *
 * @see DataUnformatFilter
 */
public class DataFormatFilter extends XMLFilterBase
{



    ////////////////////////////////////////////////////////////////////
    // Constructors.
    ////////////////////////////////////////////////////////////////////


    /**
     * Create event new filter.
     */
    public DataFormatFilter()
    {
    }


    /**
     * Create event new filter.
     *
     * <p>Use the XMLReader provided as the source of events.</p>
     *
     * @param xmlreader The parent in the filter chain.
     */
    public DataFormatFilter(XMLReader xmlreader)
    {
        super(xmlreader);
    }


    ////////////////////////////////////////////////////////////////////
    // Accessors and setters.
    ////////////////////////////////////////////////////////////////////


    /**
     * Return the current indent step.
     *
     * <p>Return the current indent step: each start tag will be
     * indented by this number of spaces times the number of
     * ancestors that the element has.</p>
     *
     * @return The number of spaces in each indentation step,
     *         or 0 or less for no indentation.
     * @see #setIndentStep
     */
    public int getIndentStep ()
    {
        return indentStep;
    }


    /**
     * Set the current indent step.
     *
     * @param indentStep The new indent step (0 or less for no
     *        indentation).
     * @see #getIndentStep
     */
    public void setIndentStep (int indentStep)
    {
        this.indentStep = indentStep;
    }



    ////////////////////////////////////////////////////////////////////
    // Public methods.
    ////////////////////////////////////////////////////////////////////


    /**
     * Reset the filter so that it can be reused.
     *
     * <p>This method is especially useful if the filter failed
     * with an exception the last time through.</p>
     */
    public void reset ()
    {
        state = SEEN_NOTHING;
        stateStack = new Stack();
    }



    ////////////////////////////////////////////////////////////////////
    // Methods from org.xml.sax.ContentHandler.
    ////////////////////////////////////////////////////////////////////


    /**
     * Filter event start document event.
     *
     * <p>Reset state and pass the event on for further processing.</p>
     *
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument ()
    throws SAXException
    {
        reset();
        super.startDocument();
    }


    /**
     * Add newline and indentation prior to start tag.
     *
     * <p>Each tag will begin on event new line, and will be
     * indented by the current indent step times the number
     * of ancestors that the element has.</p>
     *
     * <p>The newline and indentation will be passed on down
     * the filter chain through regular characters events.</p>
     *
     * @param uri The element's Namespace URI.
     * @param localName The element's local name.
     * @param qName The element's qualified (prefixed) name.
     * @param atts The element's attribute list.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement (String uri, String localName,
                              String qName, Attributes atts)
    throws SAXException
    {
        if (!stateStack.empty()) {
            doNewline();
            doIndent();
        }
        stateStack.push(SEEN_ELEMENT);
        state = SEEN_NOTHING;
        super.startElement(uri, localName, qName, atts);
    }


    /**
     * Add newline and indentation prior to end tag.
     *
     * <p>If the element has contained other elements, the tag
     * will appear indented on event new line; otherwise, it will
     * appear immediately following whatever came before.</p>
     *
     * <p>The newline and indentation will be passed on down
     * the filter chain through regular characters events.</p>
     *
     * @param uri The element's Namespace URI.
     * @param localName The element's local name.
     * @param qName The element's qualified (prefixed) name.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement (String uri, String localName, String qName)
    throws SAXException
    {
        boolean seenElement = (state == SEEN_ELEMENT);
        state = stateStack.pop();
        if (seenElement) {
            doNewline();
            doIndent();
        }
        super.endElement(uri, localName, qName);
    }


    /**
     * Filter event character data event.
     *
     * @param ch The characters to write.
     * @param start The starting position in the array.
     * @param length The number of characters to use.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters (char ch[], int start, int length)
    throws SAXException
    {
        state = SEEN_DATA;
        super.characters(ch, start, length);
    }



    ////////////////////////////////////////////////////////////////////
    // Internal methods.
    ////////////////////////////////////////////////////////////////////


    /**
     * Add newline.
     *
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     */
    private void doNewline ()
    throws SAXException
    {
        super.characters(NEWLINE, 0, NEWLINE.length);
    }


    /**
     * Add indentation for the current level.
     *
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     */
    private void doIndent ()
    throws SAXException
    {
        int n = indentStep * stateStack.size();
        if (n > 0) {
            char ch[] = new char[n];
            for (int i = 0; i < n; i++) {
                ch[i] = INDENT_CHAR;
            }
            super.characters(ch, 0, n);
        }
    }




    ////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////

    private static final Object SEEN_NOTHING = new Object();
    private static final Object SEEN_ELEMENT = new Object();
    private static final Object SEEN_DATA = new Object();

    private static final char[] NEWLINE = new char[] {'\n'};
    private static final char INDENT_CHAR = ' ';


    ////////////////////////////////////////////////////////////////////
    // Internal state.
    ////////////////////////////////////////////////////////////////////

    private Object state = SEEN_NOTHING;
    private Stack stateStack = new Stack();

    private int indentStep = 0;

}

// end of DataFormatFilter.java
