package com.jdon.util;

import java.util.*;
import java.io.File;

import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;


import com.jdon.util.Debug;



public class XmlUtil {

  public final static String module = XmlUtil.class.getName();
  

  public static  Map loadMapping(String fileName, String nodeName, String keyName,
                          String valueName) {
    Map map = new HashMap();
    FileLocator fileLocator = new FileLocator();
    try {
      String xmlFile = fileLocator.getConfFile(fileName);

      Debug.logVerbose("[JdonFramework] mapping file:" + xmlFile, module);

      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(new File(xmlFile));

      Debug.logVerbose("[JdonFramework] got mapping file ", module);

      // Get the root element
      Element root = doc.getRootElement();

      List mappings = root.getChildren(nodeName);
      Iterator i = mappings.iterator();
      while (i.hasNext()) {
        Element mapping = (Element) i.next();
        String key = mapping.getChild(keyName).getTextTrim();
        String value = mapping.getChild(valueName).getTextTrim();
         Debug.logVerbose("[JdonFramework] get the " + key + "=" + value, module);
        map.put(key, value);

      }
      Debug.logVerbose("[JdonFramework] read finished", module);

    } catch (Exception ex) {
      Debug.logError("[JdonFramework] error: " + ex, module);
    }

    return map;

  }

}
