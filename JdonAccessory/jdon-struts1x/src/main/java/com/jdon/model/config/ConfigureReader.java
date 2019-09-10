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

package com.jdon.model.config;

import java.util.*;
import com.jdon.util.Debug;

import com.jdon.controller.config.XmlParser;
import com.jdon.controller.config.XmlModelParser;

/**
 * read the configure file of  jdonframework.xml
 *
 * <p>Copyright: Jdon.com Copyright (c) 2005</p>
 * @author banq
 */
public class ConfigureReader {

  private final static String module = ConfigureReader.class.getName();
  private String configFileName;

  public ConfigureReader() {
  }

  public ConfigureReader(String configFileName) {
    this.configFileName = configFileName;
  }

  public Map load() {
    Map modelmps = null;
    try {
      XmlParser XmlParser = new XmlModelParser();
      modelmps = XmlParser.load(this.configFileName); //获取xml
    } catch (Exception ex) {
      Debug.logError("[JdonFramework] ConfigureLoader create error: " + ex, module);
    }
    return modelmps;
  }
}
