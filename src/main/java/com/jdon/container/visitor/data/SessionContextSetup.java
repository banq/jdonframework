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
package com.jdon.container.visitor.data;

import com.jdon.controller.context.RequestWrapper;


/**
 * load the informations about the login user, and 
 * save into sessioncontext that can be accessed in container.
 *
 * the default implementions is the HttpRequestUserSetup.
 * HttpRequestUserSetup must be configured in container.xml
 * so ,you can replace it.
 * 
 * @author <event href="mailto:banqJdon<AT>jdon.com">banq</event>
 *
 */

public interface SessionContextSetup {
    
    String PRINCIPAL_NAME = "PRINCIPAL_NAME";
    String REMOTE_ADDRESS = "REMOTE_ADDRESS";
    
    void setup(SessionContext sessionContext, RequestWrapper request);

    void saveSessionContext(String arrtibuteName, String arrtibuteValue, SessionContext sessionContext);

    Object getArrtibute(String arrtibuteName, SessionContext sessionContext);

}
