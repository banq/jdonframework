package com.jdon.servicelocator.web;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import com.jdon.container.pico.Startable;
import com.jdon.servicelocator.ServiceLocatorException;
import com.jdon.util.Debug;


/**
 * This class is an implementation of the Service Locator pattern. It is
 * used to looukup resources such as EJBHomes, JMS Destinations, etc.
 * This implementation uses the "singleton" strategy and also the "caching"
 * strategy.
 * This implementation is intended to be used on the web tier and
 * not on the ejb tier.
 * 
 * registering with container.xml 
 * 
 */
public class ServiceLocator implements Startable{
  private final static String module = ServiceLocator.class.getName();

  private InitialContext ic;
  private Map cache; //used to hold references to EJBHomes/JMS Resources for re-use
  
  public ServiceLocator() {
    try {
      ic = new InitialContext();
      cache = new ConcurrentHashMap();
    } catch (NamingException ne) {
      System.err.print("NamingException error: " + ne);
    } catch (Exception e) {
      System.err.print(e);
    }
  }

  public void start() {
     Debug.logVerbose("[JdonFramework]ServiceLocator start .....", module);
     cache.clear();
   }

   public void stop() {
     cache.clear();
   }


  /**
   * will get the ejb Local home factory. If this ejb home factory has already been
   * clients need to cast to the type of EJBHome they desire
   *
   * @return the EJB Home corresponding to the homeName
   */
  public EJBLocalHome getLocalHome(String jndiHomeName) throws
      ServiceLocatorException {
    Debug.logVerbose("[JdonFramework] -- > getLocalHome.... ", module);
    EJBLocalHome home = null;
    try {
      if (cache.containsKey(jndiHomeName)) {
        home = (EJBLocalHome) cache.get(jndiHomeName);
      } else {
        Debug.logVerbose("[JdonFramework]  lookUp LocalHome.... ", module);
        home = (EJBLocalHome) ic.lookup(jndiHomeName);
        cache.put(jndiHomeName, home);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return home;
  }

  /**
   * will get the ejb Remote home factory. If this ejb home factory has already been
   * clients need to cast to the type of EJBHome they desire
   *
   * @return the EJB Home corresponding to the homeName
   */
  public EJBHome getRemoteHome(String jndiHomeName, Class className) throws
      ServiceLocatorException {
    EJBHome home = null;
    try {
      if (cache.containsKey(jndiHomeName)) {
        home = (EJBHome) cache.get(jndiHomeName);
      } else {
        Object objref = ic.lookup(jndiHomeName);
        Object obj = PortableRemoteObject.narrow(objref, className);
        home = (EJBHome) obj;
        cache.put(jndiHomeName, home);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }

    return home;
  }

  /**
   * @return the factory for the factory to get queue connections from
   */
  public QueueConnectionFactory getQueueConnectionFactory(String
      qConnFactoryName) throws ServiceLocatorException {
    QueueConnectionFactory factory = null;
    try {
      if (cache.containsKey(qConnFactoryName)) {
        factory = (QueueConnectionFactory) cache.get(qConnFactoryName);
      } else {
        factory = (QueueConnectionFactory) ic.lookup(qConnFactoryName);
        cache.put(qConnFactoryName, factory);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return factory;
  }

  /**
   * @return the Queue Destination to send messages to
   */
  public Queue getQueue(String queueName) throws ServiceLocatorException {
    Queue queue = null;
    try {
      if (cache.containsKey(queueName)) {
        queue = (Queue) cache.get(queueName);
      } else {
        queue = (Queue) ic.lookup(queueName);
        cache.put(queueName, queue);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }

    return queue;
  }

  /**
   * This method helps in obtaining the topic factory
   * @return the factory for the factory to get topic connections from
   */
  public TopicConnectionFactory getTopicConnectionFactory(String
      topicConnFactoryName) throws ServiceLocatorException {
    TopicConnectionFactory factory = null;
    try {
      if (cache.containsKey(topicConnFactoryName)) {
        factory = (TopicConnectionFactory) cache.get(topicConnFactoryName);
      } else {
        factory = (TopicConnectionFactory) ic.lookup(topicConnFactoryName);
        cache.put(topicConnFactoryName, factory);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return factory;
  }

  /**
   * This method obtains the topc itself for event caller
   * @return the Topic Destination to send messages to
   */
  public Topic getTopic(String topicName) throws ServiceLocatorException {
    Topic topic = null;
    try {
      if (cache.containsKey(topicName)) {
        topic = (Topic) cache.get(topicName);
      } else {
        topic = (Topic) ic.lookup(topicName);
        cache.put(topicName, topic);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return topic;
  }

  /**
   * This method obtains the datasource itself for event caller
   * @return the DataSource corresponding to the name parameter
   */
  public DataSource getDataSource(String dataSourceName) throws
      ServiceLocatorException {
    DataSource dataSource = null;
    try {
      if (cache.containsKey(dataSourceName)) {
        dataSource = (DataSource) cache.get(dataSourceName);
      } else {
        dataSource = (DataSource) ic.lookup(dataSourceName);
        cache.put(dataSourceName, dataSource);
      }
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return dataSource;
  }

  /**
   * @return the URL value corresponding
   * to the env entry name.
   */
  public URL getUrl(String envName) throws ServiceLocatorException {
    URL url = null;
    try {
      url = (URL) ic.lookup(envName);
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }

    return url;
  }

  /**
   * @return the boolean value corresponding
   * to the env entry such as SEND_CONFIRMATION_MAIL property.
   */
  public boolean getBoolean(String envName) throws ServiceLocatorException {
    Boolean bool = null;
    try {
      bool = (Boolean) ic.lookup(envName);
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return bool.booleanValue();
  }

  /**
   * @return the String value corresponding
   * to the env entry name.
   */
  public String getString(String envName) throws ServiceLocatorException {
    String envEntry = null;
    try {
      envEntry = (String) ic.lookup(envName);
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception e) {
      throw new ServiceLocatorException(e);
    }
    return envEntry;
  }

  public Object getDAO(String jndiDAOName) throws ServiceLocatorException {

    Object object = null;
    try {
      String className = (String) ic.lookup(jndiDAOName);
      object = Class.forName(className).newInstance();
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    } catch (Exception se) {
      throw new ServiceLocatorException(se);
    }
    return object;
  }

}
