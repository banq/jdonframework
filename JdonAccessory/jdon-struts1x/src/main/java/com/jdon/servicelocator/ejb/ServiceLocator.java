package com.jdon.servicelocator.ejb;

import java.net.URL;

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

import com.jdon.servicelocator.ServiceLocatorException;

/**
 * This class is an implementation of the Service Locator pattern. It is used to
 * looukup resources such as EJBHomes, JMS Destinations, etc.
 */
public class ServiceLocator implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9118735048187674303L;
	private InitialContext ic;

	public ServiceLocator() throws ServiceLocatorException {
		try {
			ic = new InitialContext();
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
	}

	/**
	 * will get the ejb Local home factory. clients need to cast to the type of
	 * EJBHome they desire
	 * 
	 * @return the Local EJB Home corresponding to the homeName
	 */
	public EJBLocalHome getLocalHome(String jndiHomeName) throws ServiceLocatorException {
		EJBLocalHome home = null;
		try {
			home = (EJBLocalHome) ic.lookup(jndiHomeName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return home;
	}

	/**
	 * will get the ejb Remote home factory. clients need to cast to the type of
	 * EJBHome they desire
	 * 
	 * @return the EJB Home corresponding to the homeName
	 */
	public EJBHome getRemoteHome(String jndiHomeName, Class className) throws ServiceLocatorException {
		EJBHome home = null;
		try {
			Object objref = ic.lookup(jndiHomeName);
			Object obj = PortableRemoteObject.narrow(objref, className);
			home = (EJBHome) obj;
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
	public QueueConnectionFactory getQueueConnectionFactory(String qConnFactoryName) throws ServiceLocatorException {
		QueueConnectionFactory factory = null;
		try {
			factory = (QueueConnectionFactory) ic.lookup(qConnFactoryName);
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
			queue = (Queue) ic.lookup(queueName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return queue;
	}

	/**
	 * This method helps in obtaining the topic factory
	 * 
	 * @return the factory for the factory to get topic connections from
	 */
	public TopicConnectionFactory getTopicConnectionFactory(String topicConnFactoryName) throws ServiceLocatorException {
		TopicConnectionFactory factory = null;
		try {
			factory = (TopicConnectionFactory) ic.lookup(topicConnFactoryName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return factory;
	}

	/**
	 * This method obtains the topc itself for event caller
	 * 
	 * @return the Topic Destination to send messages to
	 */
	public Topic getTopic(String topicName) throws ServiceLocatorException {
		Topic topic = null;
		try {
			topic = (Topic) ic.lookup(topicName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return topic;
	}

	/**
	 * This method obtains the datasource itself for event caller
	 * 
	 * @return the DataSource corresponding to the name parameter
	 */
	public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
		DataSource dataSource = null;
		try {
			dataSource = (DataSource) ic.lookup(dataSourceName);
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
		return dataSource;
	}

	/**
	 * @return the URL value corresponding to the env entry name.
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
	 * @return the boolean value corresponding to the env entry such as
	 *         SEND_CONFIRMATION_MAIL property.
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
	 * @return the String value corresponding to the env entry name.
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
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			object = classLoader.loadClass(className).newInstance();
		} catch (NamingException ne) {
			throw new ServiceLocatorException(ne);
		} catch (Exception se) {
			throw new ServiceLocatorException(se);
		}
		return object;
	}

}
