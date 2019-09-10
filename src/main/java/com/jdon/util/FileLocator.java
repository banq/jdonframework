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
package com.jdon.util;

import java.io.InputStream;
import java.net.URL;
import java.io.File;

/**
 * the locator for configure file
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class FileLocator {

 /**
  * com.jdon.sample.xxx.xml ==>
  * com/jdon/sample/xxx.xml
  * @param filePathName
  * @return filename's string
  */
  public String getConfPathXmlFile(String filePathName){
     int i = filePathName.lastIndexOf(".xml");
     String name = filePathName.substring(0, i);
     name = name.replace('.', '/');
     name += ".xml";
     return getConfFile(name);
  }

  /**
   * same as getConfPathXmlFile
   * @param filePathName
   * @return the InputStream intance
   */
  public InputStream getConfPathXmlStream(String filePathName){
     int i = filePathName.lastIndexOf(".xml");
     String name = filePathName.substring(0, i);
     name = name.replace('.', '/');
     name += ".xml";
     return getConfStream(name);
  }


  public String getConfFile(String fileName) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      classLoader = getClass().getClassLoader();
    }
    URL confURL = classLoader.getResource(fileName);
    if (confURL == null)
      confURL = classLoader.getResource("META-INF/" + fileName);
    if (confURL == null) {
      return null;
    } else {
      File file1 = new File(confURL.getFile());
      if (file1.isFile()) {
        System.out.println(" locate file: " + confURL.getFile());
        return confURL.getFile();
      } else {
        System.err.println(" it is not event file: " + confURL.getFile());
        return null;
      }
    }
  }

  public InputStream getConfStream(String fileName) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      classLoader = this.getClass().getClassLoader();
    }
    InputStream stream = classLoader.getResourceAsStream(fileName);
    if (stream == null)
      stream = classLoader.getResourceAsStream("META-INF/" + fileName);
   
    return stream;
  }

}
