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

import org.hibernate.HibernateException;
import org.hibernate.Session;

public interface SessionProvider {

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#getSession()
	 */
	public abstract Session getSession() throws HibernateException;

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#rollback()
	 */
	public abstract void rollback() throws HibernateException;

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#resetSession()
	 */
	public abstract void resetSession();

	/* (non-Javadoc)
	 * @see com.jdon.model.crud.hibernate.SessionProvider#returnCloseSession(org.hibernate.Session)
	 */
	public abstract void closeSession() throws HibernateException;

}