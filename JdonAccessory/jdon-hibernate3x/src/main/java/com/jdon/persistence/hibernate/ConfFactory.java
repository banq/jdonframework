/*
 * Copyright 2007 the original author or jdon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.jdon.persistence.hibernate;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.DatasourceConnectionProvider;
import org.hibernate.impl.SessionFactoryImpl;

/**
 * 
 * <component name="daoCRUD" class="com.jdon.model.HibernateCRUDTemplate"/>
 * <component name="hibernateConfFactory"
 * class="com.jdon.model.crud.AnnotationConfFactory"> <constructor
 * value="/hibernate.cfg.xml"/> </component>
 * 
 * @author banq
 * 
 */
public class ConfFactory {
	private final static Logger logger = LogManager.getLogger(ConfFactory.class);

	protected SessionFactory sessionFactory;

	protected DataSource dataSource;

	protected String hibernate_cfg_xml;

	public ConfFactory(String hibernate_cfg_xml) {
		this.hibernate_cfg_xml = hibernate_cfg_xml;
	}

	public SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			createSessionFactory();
		return sessionFactory;
	}

	public void createSessionFactory() {
		try {
			Configuration configuration = null;
			if ((hibernate_cfg_xml != null) && (hibernate_cfg_xml.length() != 0)) {
				configuration = new Configuration().configure(hibernate_cfg_xml);
			} else {
				configuration = new Configuration().configure();
			}
			this.sessionFactory = configuration.buildSessionFactory();
		} catch (HibernateException e) {
			logger.error("Hibernate start error: " + e);
		}
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = getDataSource(getSessionFactory());
		}
		return dataSource;
	}

	protected DataSource getDataSource(SessionFactory sessionFactory) {
		if (sessionFactory instanceof SessionFactoryImpl) {
			ConnectionProvider cp = ((SessionFactoryImpl) sessionFactory).getConnectionProvider();
			if (cp instanceof DatasourceConnectionProvider) {
				return ((DatasourceConnectionProvider) cp).getDataSource();
			}
		}
		return null;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
