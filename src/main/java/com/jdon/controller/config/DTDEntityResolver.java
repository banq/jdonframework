package com.jdon.controller.config;

import java.net.URL;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import java.io.InputStream;
import java.io.IOException;
import org.xml.sax.InputSource;

public class DTDEntityResolver implements EntityResolver {
  //~ Methods ////////////////////////////////////////////////////////////////

  public InputSource resolveEntity(String publicId, String systemId) throws
      SAXException, IOException {
    if (systemId == null) {
      return null;
    }

    URL url = new URL(systemId);
    String file = url.getFile();

    if ( (file != null) && (file.indexOf('/') > -1)) {
      file = file.substring(file.lastIndexOf('/') + 1);
    }

    if ("www.jdon.com".equals(url.getHost())) {
      InputStream is = getClass().getResourceAsStream("/META-INF/" + file);

      if (is == null) {
        is = getClass().getResourceAsStream("/" + file);
      }

      if (is != null) {
        return new InputSource(is);
      }
    }

    return null;
  }
}
