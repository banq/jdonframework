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

import java.io.Serializable;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jdon.persistence.DaoCRUD;
import com.jdon.persistence.hibernate.util.ThreadLocalSessionProvider;

/**
 *     <component name="daoCRUD" class="com.jdon.model.HibernateCRUDTemplate"/>    
    <component name="hibernateConfFactory" class="com.jdon.model.crud.AnnotationConfFactory">
        <constructor value="/hibernate.cfg.xml"/>
    </component>

 * @author banq
 *
 */
public class HibernateCRUDTemplate implements DaoCRUD {

	/**
	 * 
	 */
	private static final long serialVersionUID = -935866148100826167L;

	protected DataSource dataSource;
	
	protected SessionProvider sessionProvider;
	
	protected HibernateTemplate hibernateTemplate;

	public HibernateCRUDTemplate(ConfFactory hibernateConfFactory) {
		this.dataSource = hibernateConfFactory.getDataSource();
		SessionFactory sessionFactory = hibernateConfFactory.getSessionFactory();
		this.sessionProvider = new ThreadLocalSessionProvider(sessionFactory);
		this.hibernateTemplate = new HibernateTemplate(sessionProvider);
	}
	
	public HibernateCRUDTemplate(Configuration cfg) {
		this.sessionProvider = new ThreadLocalSessionProvider(cfg);
		this.hibernateTemplate = new HibernateTemplate(sessionProvider);
	}
	
    
	/* (non-Javadoc)
	 * @see sample.persistence.UserDaoIF#insert(sample.model.User)
	 */
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.DaoCRUDTemplate#insert(java.lang.Object)
	 */
	public void insert(Object o) throws Exception {
		getHibernateTemplate().save(o);

	}

	public void update(Object o) throws Exception{
		getHibernateTemplate().update(o);
	}
	
	public void saveOrUpdate(Object o) throws Exception{
		getHibernateTemplate().saveOrUpdate(o);
	}
	
	public void merge(Object o) throws Exception{
		getHibernateTemplate().merge(o);
	}

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.DaoCRUDTemplate#delete(java.lang.Object)
	 */
	public void delete(Object o) throws Exception {
		getHibernateTemplate().delete(o);
	}

	/* (non-Javadoc)
	 * @see sample.persistence.UserDaoIF#getUser(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.DaoCRUDTemplate#loadById(java.lang.Class, java.lang.String)
	 */
	public Object loadById(Class entity, Serializable id) throws Exception  {
		return getHibernateTemplate().load(entity, id);
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
