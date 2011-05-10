/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package com.jdon.strutsutil.util;

import javax.servlet.http.HttpServletRequest;

import com.jdon.model.ModelHandler;
import com.jdon.model.ModelManager;
import com.jdon.util.Debug;

/**
 * @author <a href="mailto:banqiao@jdon.com">banq</a>
 *
 */
public class ViewPageUtil extends EditeViewPageUtil{
    private final static String module = ViewPageUtil.class.getName();

    public ViewPageUtil(ModelManager modelManager) {
        super(modelManager);
    }

    protected Object fetchModel(HttpServletRequest request, String formName, ModelHandler modelHandler) throws Exception {
    	Object model = null;
        try {
            Object keyValue = getParamKeyValue(request, modelHandler);
            model = modelHandler.findModelIF(keyValue, request);    
        } catch (Exception ex) {
            Debug.logError("[JdonFramework] the method 'findModelByKey' of your handler or 'getMethod' of service happened error: " + ex, module);
            throw new Exception(ex);
        }
        return model;
    }

}
