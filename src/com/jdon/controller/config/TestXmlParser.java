package com.jdon.controller.config;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestXmlParser extends TestCase {
  private XmlParser xmlParser1 = null;
  private XmlParser xmlParser2 = null;

  protected void setUp() throws Exception {
    super.setUp();
    xmlParser1 = new XmlModelParser();
    xmlParser2 = new XmlServiceParser();
  }

  protected void tearDown() throws Exception {
    xmlParser1 = null;
    xmlParser2 = null;
    super.tearDown();
  }

  public void testLoad() {
    String configFileName = "jdonframework.xml";
    Map actualReturn = xmlParser1.load(configFileName);
    Assert.assertNotNull(actualReturn);

    actualReturn = xmlParser2.load(configFileName);
    Assert.assertNotNull(actualReturn);

   

    /**@todo fill in the test code*/
  }

}
