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
import org.hibernate.Transaction;

public class SessionFactoryHolder {

	private static final ThreadLocal _session = new ThreadLocal();

	private static final ThreadLocal _transaction = new ThreadLocal();

	private static final ThreadLocal sessionIsActive = new ThreadLocal();

	public static boolean isSessionIsActive() {
		Boolean result = (Boolean) sessionIsActive.get();
		return ((result != null) && result);
	}

	public static Session getSession() {
		return (Session) _session.get();
	}

	public static void setSession(Session session) {
		if (session == null) {
			sessionIsActive.set(null);
		} else
			sessionIsActive.set(new Boolean(true));
		_session.set(session);
	}

	public static Transaction getTransaction() {
		return (Transaction) _transaction.get();
	}

	public static void setTransaction(Transaction trans) {
		_transaction.set(trans);
	}

	public static void closeSession() throws HibernateException {
		Transaction tr = (Transaction) getTransaction();
		try {
			if (tr != null && !tr.wasCommitted() && !tr.wasRolledBack()) {
				getSession().flush();//flush cache to db;
				getSession().clear();//clear cache;
				tr.commit();
			}
		} catch (Exception e) {
		} finally {
			setTransaction(null);
			getSession().close();
			setSession(null);
		}
	}

}
