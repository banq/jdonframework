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
package com.jdon.persistence.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jdon.persistence.hibernate.SessionProvider;

public class ThreadLocalSessionProvider implements SessionProvider {

	protected SessionFactory _factory;
	
	/**
	 * Constructor for the ThreadLocalSessionProvider object
	 *
	 * @param sfp  Description of Parameter
	 */
	public ThreadLocalSessionProvider(Configuration cfg) {
		_factory = cfg.buildSessionFactory();
	}
	
	public ThreadLocalSessionProvider(SessionFactory _factory) {
		this._factory = _factory;
	}
	



	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#getSession()
	 */
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#getSession()
	 */
	public Session getSession() throws HibernateException {
		Session sess = (Session)SessionFactoryHolder.getSession();
		if (sess == null) {
			sess = getFactory().openSession();
			Transaction tr = sess.beginTransaction();
			SessionFactoryHolder.setSession(sess);
			SessionFactoryHolder.setTransaction(tr);
		}
		return sess;
	}

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#rollback()
	 */
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#rollback()
	 */
	public void rollback() throws HibernateException {
		Transaction tr = (Transaction) SessionFactoryHolder.getTransaction();
		if (tr != null) {
			tr.rollback();
		}
		SessionFactoryHolder.setTransaction(null);
	}

	
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#resetSession()
	 */
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#resetSession()
	 */
	public void resetSession() {
		SessionFactoryHolder.setTransaction(null);
		SessionFactoryHolder.setSession(null);

	}

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#returnCloseSession(org.hibernate.Session)
	 */
	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#closeSession()
	 */
	public void closeSession() throws HibernateException {
		Transaction tr = (Transaction) SessionFactoryHolder.getTransaction();
		try {
			if (tr != null && !tr.wasCommitted() && !tr.wasRolledBack()) {
				tr.commit();
			}
		} catch (Exception e) {
		}finally{
			SessionFactoryHolder.setTransaction(null);
			SessionFactoryHolder.getSession().close();
			SessionFactoryHolder.setSession(null);			
		}
	}

	/**
	 * Gets the Factory attribute of the BaseSessionProvider object
	 *
	 * @return   The Factory value
	 */
	SessionFactory getFactory() {
		return _factory;
	}

}
