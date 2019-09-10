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

package com.jdon.controller.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.jdon.model.config.ModelMapping;
import com.jdon.model.handler.HandlerMetaDef;
import com.jdon.util.Debug;

public class XmlModelParser extends XmlParser {
    private final static String module = XmlModelParser.class.getName();

    public void parse(Element root, Map mps) throws Exception {
        try {
            List modelList = root.getChildren("models");
            Iterator iter = modelList.iterator();
            while (iter.hasNext()) {
                Element models = (Element) iter.next();
                Iterator i = models.getChildren("model").iterator();
                while (i.hasNext()) {
                    Element model = (Element) i.next();
                    parseModelConfig(model, mps);
                }

            }
        } catch (Exception e) {
            Debug.logError("[JdonFramework]parse models error: " + e, module);
            throw new Exception(e);
        }
    }

    private void parseModelConfig(Element model, Map mps) throws Exception {
        try {
            ModelMapping modelMapping = new ModelMapping();
            modelMapping.setKeyName(model.getAttributeValue("key"));
            modelMapping.setClassName(model.getAttributeValue("class"));
            Debug.logVerbose("[JdonFramework] read Config Model: " + modelMapping.getClassName(), module);
            Debug.logVerbose("[JdonFramework] key: " + modelMapping.getKeyName(), module);
            //keyClassType in XmlHandlerClassFactory;

            Element actionFormE = model.getChild("actionForm");
            modelMapping.setFormName(actionFormE.getAttributeValue("name"));
            Debug.logVerbose("[JdonFramework] actionForm name: " + modelMapping.getFormName(), module);

            Element handlerE = model.getChild("handler");
            if (handlerE != null) {
                String classAttr = handlerE.getAttributeValue("class");
                if (classAttr != null) {
                    modelMapping.setHandler(classAttr);
                    Debug.logVerbose("[JdonFramework] handler class: " + modelMapping.getHandler(), module);
                }
                Debug.logVerbose("[JdonFramework] look up service in handler: ", module);
                HandlerMetaDef sm = new HandlerMetaDef();
                Element serviceE = handlerE.getChild("service");
                if (serviceE != null) {
                    parseHandlerConfig(serviceE, sm);
                    modelMapping.setHandlerMetaDef(sm);
                    sm.setModelMapping(modelMapping);
                }

            }
            mps.put(modelMapping.getFormName(), modelMapping);
        } catch (Exception e) {
            Debug.logError("[JdonFramework]parse Model class:" + model.getAttributeValue("class") + " error: " + e, module);
            throw new Exception(e);
        }
    }
    
    private void parseHandlerConfig(Element serviceE, HandlerMetaDef sm) throws Exception {

        Debug.logVerbose("[JdonFramework] find event Service in handler: ", module);
        try {
            sm.setServiceRef(serviceE.getAttributeValue("ref"));
            Debug.logVerbose("[JdonFramework] service ref: " + sm.getServiceRef(), module);

            Element initMethod = serviceE.getChild("initMethod");
            if (initMethod != null) {
                sm.setInitMethod(initMethod.getAttributeValue("name"));
                Debug.logVerbose("[JdonFramework] initMethod name: " + sm.getInitMethod(), module);
            }

            Element getMethod = serviceE.getChild("getMethod");
            if (getMethod != null) {
                sm.setFindtMethod(getMethod.getAttributeValue("name"));
                Debug.logVerbose("[JdonFramework] getMethod name: " + sm.getFindMethod(), module);
            }

            Element createMethod = serviceE.getChild("createMethod");
            if (createMethod != null) {
                sm.setCreateMethod(createMethod.getAttributeValue("name"));
                Debug.logVerbose("[JdonFramework] createMethod name: " + sm.getCreateMethod(), module);
            }

            Element updateMethod = serviceE.getChild("updateMethod");
            if (updateMethod != null) {
                sm.setUpdateMethod(updateMethod.getAttributeValue("name"));
                Debug.logVerbose("[JdonFramework] updateMethod name: " + sm.getUpdateMethod(), module);
            }

            Element deleteMethod = serviceE.getChild("deleteMethod");
            if (deleteMethod != null) {
                sm.setDeleteMethod(deleteMethod.getAttributeValue("name"));
                Debug.logVerbose("[JdonFramework] deleteMethod name: " + sm.getDeleteMethod(), module);
            }
            
            
        } catch (Exception e) {
            Debug.logError("[JdonFramework]parseHandlerConfig error: " + e, module);
            throw new Exception(e);
        }

    }
    
    
   
}
