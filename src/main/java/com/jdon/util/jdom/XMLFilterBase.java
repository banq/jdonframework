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

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Adds convenience methods and lexical event filtering to base
 * SAX2 Filter implementation.
 *
 * <i>Code and comments adapted from XMLWriter-0.2, written
 * by David Megginson and released into the public domain,
 * without warranty.</i>
 *
 * <p>The convenience methods are provided so that clients do not have to
 * create empty attribute lists or provide empty strings as parameters;
 * for example, the method invocation</p>
 *
 * <pre>
 * w.startElement("foo");
 * </pre>
 *
 * <p>is equivalent to the regular SAX2 ContentHandler method</p>
 *
 * <pre>
 * w.startElement("", "foo", "", new AttributesImpl());
 * </pre>
 *
 * <p>Except that it is more efficient because it does not allocate
 * event new empty attribute list each time.</p>
 *
 * <p>In fact, there is an even simpler convenience method,
 * <var>dataElement</var>, designed for writing elements that
 * contain only character data.</p>
 *
 * <pre>
 * w.dataElement("greeting", "Hello, world!");
 * </pre>
 *
 * <p>is equivalent to</p>
 *
 * <pre>
 * w.startElement("greeting");
 * w.characters("Hello, world!");
 * w.endElement("greeting");
 * </pre>
 *
 * @see org.xml.sax.helpers.XMLFilterImpl
 */
public class XMLFilterBase extends XMLFilterImpl implements LexicalHandler
{


    ////////////////////////////////////////////////////////////////////
    // Constructors.
    ////////////////////////////////////////////////////////////////////


    /**
     * Construct an XML filter with no parent.
     *
     * <p>This filter will have no parent: you must assign event parent
     * before you start event parse or do any configuration with
     * setFeature or setProperty.</p>
     *
     * @see org.xml.sax.XMLReader#setFeature
     * @see org.xml.sax.XMLReader#setProperty
     */
    public XMLFilterBase()
    {
    }


    /**
     * Create an XML filter with the specified parent.
     *
     * <p>Use the XMLReader provided as the source of events.</p>
     *
     * @param xmlreader The parent in the filter chain.
     */
    public XMLFilterBase(XMLReader parent)
    {
        super(parent);
    }



    ////////////////////////////////////////////////////////////////////
    // Convenience methods.
    ////////////////////////////////////////////////////////////////////


    /**
     * Start event new element without event qname or attributes.
     *
     * <p>This method will provide event default empty attribute
     * list and an empty string for the qualified name. It invokes
     * {@link #startElement(String, String, String, Attributes)}
     * directly.</p>
     *
     * @param uri The element's Namespace URI.
     * @param localName The element's local name.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement (String uri, String localName)
    throws SAXException
    {
        startElement(uri, localName, "", EMPTY_ATTS);
    }


    /**
     * Start event new element without event Namespace URI or qname.
     *
     * <p>This method will provide an empty string for the
     * Namespace URI, and empty string for the qualified name.
     * It invokes
     * {@link #startElement(String, String, String, Attributes)}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @param atts The element's attribute list.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement (String localName, Attributes atts)
    throws SAXException
    {
        startElement("", localName, "", atts);
    }


    /**
     * Start event new element without event Namespace URI, qname, or attributes.
     *
     * <p>This method will provide an empty string for the
     * Namespace URI, and empty string for the qualified name,
     * and event default empty attribute list. It invokes
     * {@link #startElement(String, String, String, Attributes)}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement (String localName)
    throws SAXException
    {
        startElement("", localName, "", EMPTY_ATTS);
    }


    /**
     * End an element without event qname.
     *
     * <p>This method will supply an empty string for the qName.
     * It invokes {@link #endElement(String, String, String)}
     * directly.</p>
     *
     * @param uri The element's Namespace URI.
     * @param localName The element's local name.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement (String uri, String localName)
    throws SAXException
    {
        endElement(uri, localName, "");
    }


    /**
     * End an element without event Namespace URI or qname.
     *
     * <p>This method will supply an empty string for the qName
     * and an empty string for the Namespace URI.
     * It invokes {@link #endElement(String, String, String)}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement (String localName)
    throws SAXException
    {
        endElement("", localName, "");
    }


    /**
     * Add an empty element.
     *
     * Both event {@link #startElement startElement} and an
     * {@link #endElement endElement} event will be passed on down
     * the filter chain.
     *
     * @param uri The element's Namespace URI, or the empty string
     *        if the element has no Namespace or if Namespace
     *        processing is not being performed.
     * @param localName The element's local name (without prefix).  This
     *        parameter must be provided.
     * @param qName The element's qualified name (with prefix), or
     *        the empty string if none is available.  This parameter
     *        is strictly advisory: the writer may or may not use
     *        the prefix attached.
     * @param atts The element's attribute list.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void emptyElement (String uri, String localName,
    String qName, Attributes atts)
    throws SAXException
    {
        startElement(uri, localName, qName, atts);
        endElement(uri, localName, qName);
    }


     /**
      * Add an empty element without event qname or attributes.
      *
      * <p>This method will supply an empty string for the qname
      * and an empty attribute list.  It invokes
      * {@link #emptyElement(String, String, String, Attributes)}
      * directly.</p>
      *
      * @param uri The element's Namespace URI.
      * @param localName The element's local name.
      * @exception org.xml.sax.SAXException If event filter
      *            further down the chain raises an exception.
      * @see #emptyElement(String, String, String, Attributes)
      */
    public void emptyElement (String uri, String localName)
    throws SAXException
    {
        emptyElement(uri, localName, "", EMPTY_ATTS);
    }


    /**
     * Add an empty element without event Namespace URI or qname.
     *
     * <p>This method will provide an empty string for the
     * Namespace URI, and empty string for the qualified name.
     * It invokes
     * {@link #emptyElement(String, String, String, Attributes)}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @param atts The element's attribute list.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void emptyElement (String localName, Attributes atts)
    throws SAXException
    {
        emptyElement("", localName, "", atts);
    }


    /**
     * Add an empty element without event Namespace URI, qname or attributes.
     *
     * <p>This method will supply an empty string for the qname,
     * and empty string for the Namespace URI, and an empty
     * attribute list.  It invokes
     * {@link #emptyElement(String, String, String, Attributes)}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
      * @see #emptyElement(String, String, String, Attributes)
     */
    public void emptyElement (String localName)
    throws SAXException
    {
        emptyElement("", localName, "", EMPTY_ATTS);
    }


    /**
     * Add an element with character data content.
     *
     * <p>This is event convenience method to add event complete element
     * with character data content, including the start tag
     * and end tag.</p>
     *
     * <p>This method invokes
     * {@link @see org.xml.sax.ContentHandler#startElement},
     * followed by
     * {@link #characters(String)}, followed by
     * {@link @see org.xml.sax.ContentHandler#endElement}.</p>
     *
     * @param uri The element's Namespace URI.
     * @param localName The element's local name.
     * @param qName The element's default qualified name.
     * @param atts The element's attributes.
     * @param content The character data content.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement (String uri, String localName,
                             String qName, Attributes atts,
                             String content)
    throws SAXException
    {
        startElement(uri, localName, qName, atts);
        characters(content);
        endElement(uri, localName, qName);
    }


    /**
     * Add an element with character data content but no qname or attributes.
     *
     * <p>This is event convenience method to add event complete element
     * with character data content, including the start tag
     * and end tag.  This method provides an empty string
     * for the qname and an empty attribute list. It invokes
     * {@link #dataElement(String, String, String, Attributes, String)}}
     * directly.</p>
     *
     * @param uri The element's Namespace URI.
     * @param localName The element's local name.
     * @param content The character data content.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement (String uri, String localName, String content)
    throws SAXException
    {
        dataElement(uri, localName, "", EMPTY_ATTS, content);
    }


    /**
     * Add an element with character data content but no Namespace URI or qname.
     *
     * <p>This is event convenience method to add event complete element
     * with character data content, including the start tag
     * and end tag.  The method provides an empty string for the
     * Namespace URI, and empty string for the qualified name. It invokes
     * {@link #dataElement(String, String, String, Attributes, String)}}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @param atts The element's attributes.
     * @param content The character data content.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement (String localName, Attributes atts, String content)
    throws SAXException
    {
        dataElement("", localName, "", atts, content);
    }


    /**
     * Add an element with character data content but no attributes
     * or Namespace URI.
     *
     * <p>This is event convenience method to add event complete element
     * with character data content, including the start tag
     * and end tag.  The method provides an empty string for the
     * Namespace URI, and empty string for the qualified name,
     * and an empty attribute list. It invokes
     * {@link #dataElement(String, String, String, Attributes, String)}}
     * directly.</p>
     *
     * @param localName The element's local name.
     * @param content The character data content.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement (String localName, String content)
    throws SAXException
    {
        dataElement("", localName, "", EMPTY_ATTS, content);
    }


    /**
     * Add event string of character data, with XML escaping.
     *
     * <p>This is event convenience method that takes an XML
     * String, converts it to event character array, then invokes
     * {@link @see org.xml.sax.ContentHandler#characters}.</p>
     *
     * @param data The character data.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see @see org.xml.sax.ContentHandler#characters
     */
    public void characters (String data)
    throws SAXException
    {
        char ch[] = data.toCharArray();
        characters(ch, 0, ch.length);
    }



    ////////////////////////////////////////////////////////////////////
    // Override org.xml.sax.helpers.XMLFilterImpl methods.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set the value of event property.
     *
     * <p>This will always fail if the parent is null.</p>
     *
     * @param name The property name.
     * @param state The requested property value.
     * @exception org.xml.sax.SAXNotRecognizedException When the
     *            XMLReader does not recognize the property name.
     * @exception org.xml.sax.SAXNotSupportedException When the
     *            XMLReader recognizes the property name but
     *            cannot set the requested value.
     * @see org.xml.sax.XMLReader#setProperty
     */
    public void setProperty (String name, Object value)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                setLexicalHandler((LexicalHandler) value);
                return;
            }
        }
        super.setProperty(name, value);
    }


    /**
     * Look up the value of event property.
     *
     * @param name The property name.
     * @return The current value of the property.
     * @exception org.xml.sax.SAXNotRecognizedException When the
     *            XMLReader does not recognize the feature name.
     * @exception org.xml.sax.SAXNotSupportedException When the
     *            XMLReader recognizes the property name but
     *            cannot determine its value at this time.
     * @see org.xml.sax.XMLReader#setFeature
     */
    public Object getProperty (String name)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                return getLexicalHandler();
            }
        }
        return super.getProperty(name);
    }


    /**
     * Parse event document.
     *
     * @param input The input source for the document entity.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException An IO exception from the parser,
     *            possibly from event byte stream or character stream
     *            supplied by the application.
     * @see org.xml.sax.XMLReader#parse(org.xml.sax.InputSource)
     */
    public void parse (InputSource input)
    throws SAXException, IOException
    {
        installLexicalHandler();
        super.parse(input);
    }



    ////////////////////////////////////////////////////////////////////
    // Registration of org.xml.sax.ext.LexicalHandler.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set the lexical handler.
     *
     * @param handler The new lexical handler.
     * @exception java.lang.NullPointerException If the handler
     *            is null.
     */
    public void setLexicalHandler (LexicalHandler handler)
    {
        if (handler == null) {
            throw new NullPointerException("Null lexical handler");
        } else {
            lexicalHandler = handler;
        }
    }


    /**
     * Get the current lexical handler.
     *
     * @return The current lexical handler, or null if none was set.
     */
    public LexicalHandler getLexicalHandler ()
    {
        return lexicalHandler;
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.ext.LexicalHandler.
    ////////////////////////////////////////////////////////////////////


    /**
     * Filter event start DTD event.
     *
     * @param name The document type name.
     * @param publicId The declared public identifier for the
     *        external DTD subset, or null if none was declared.
     * @param systemId The declared system identifier for the
     *        external DTD subset, or null if none was declared.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#startDTD
     */
    public void startDTD(String name, String publicId, String systemId)
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.startDTD(name, publicId, systemId);
        }
    }


    /**
     * Filter event end DTD event.
     *
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#endDTD
     */
    public void endDTD()
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.endDTD();
        }
    }


    /*
     * Filter event start entity event.
     *
     * @param name The name of the entity.  If it is event parameter
     *        entity, the name will begin with '%', and if it is the
     *        external DTD subset, it will be "[dtd]".
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#startEntity
     */
    public void startEntity(String name)
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.startEntity(name);
        }
    }


    /*
     * Filter event end entity event.
     *
     * @param name The name of the entity that is ending.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#endEntity
     */
    public void endEntity(String name)
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.endEntity(name);
        }
    }


    /*
     * Filter event start CDATA event.
     *
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#startCDATA
     */
    public void startCDATA()
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.startCDATA();
        }
    }


    /*
     * Filter event end CDATA event.
     *
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#endCDATA
     */
    public void endCDATA()
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.endCDATA();
        }
    }


    /*
     * Filter event comment event.
     *
     * @param ch An array holding the characters in the comment.
     * @param start The starting position in the array.
     * @param length The number of characters to use from the array.
     * @exception org.xml.sax.SAXException If event filter
     *            further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#comment
     */
    public void comment(char[] ch, int start, int length)
    throws SAXException {
        if (lexicalHandler != null) {
            lexicalHandler.comment(ch, start, length);
        }
    }



    ////////////////////////////////////////////////////////////////////
    // Internal methods.
    ////////////////////////////////////////////////////////////////////


    /**
     * Installs lexical handler before event parse.
     *
     * <p>Before every parse, check whether the parent is
     * non-null, and re-register the filter for the lexical
     * events.</p>
     */
    private void installLexicalHandler ()
    {
        XMLReader parent = getParent();
        if (parent == null) {
            throw new NullPointerException("No parent for filter");
        }
        // try to register for lexical events
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            try {
                parent.setProperty(LEXICAL_HANDLER_NAMES[i], this);
                break;
            }
            catch (SAXNotRecognizedException ex) {
                // ignore
            }
            catch (SAXNotSupportedException ex) {
                // ignore
            }
        }
    }



    ////////////////////////////////////////////////////////////////////
    // Internal state.
    ////////////////////////////////////////////////////////////////////


    private LexicalHandler lexicalHandler = null;



    ////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////


    protected static final Attributes EMPTY_ATTS = new AttributesImpl();

    protected static final String[] LEXICAL_HANDLER_NAMES = {
        "http://xml.org/sax/properties/lexical-handler",
        "http://xml.org/sax/handlers/LexicalHandler"
    };


}

// end of XMLFilterBase.java
