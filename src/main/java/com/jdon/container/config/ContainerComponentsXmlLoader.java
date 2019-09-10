/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.container.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.jdon.controller.config.XmlParser;
import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class ContainerComponentsXmlLoader extends XmlParser {
    public final static String module = ContainerComponentsXmlLoader.class.getName();

    public void parse(Element root, Map mps) throws Exception {        
        List components = root.getChildren("component");
        Debug.logVerbose("[JdonFramework] found component size:" + components.size(), module);
        Iterator iter = components.iterator();
        
        ComponentMetaDef componentMetaDef;
        
        while (iter.hasNext()) {
          Element component = (Element) iter.next();
          String name = component.getAttributeValue("name");
          String className = component.getAttributeValue("class");   
          List mappings = component.getChildren("constructor");
          String[] constructors = null;
          if ((mappings != null) && (mappings.size() != 0)) {
              constructors = new String[mappings.size()];
              int j = 0;
              Iterator i = mappings.iterator();
              while (i.hasNext()) {
                Element constructor = (Element) i.next();
                String value = constructor.getAttributeValue("value");
                Debug.logVerbose("[JdonFramework] component constructor=" + value, module);
                constructors[j] = value;
                j++;
              }
            }

          if (constructors != null)
              componentMetaDef = new ComponentMetaDef(name, className, constructors);
          else
              componentMetaDef = new ComponentMetaDef(name, className);
          
          mps.put(name, componentMetaDef);
        }
      }
    
  
    


}
