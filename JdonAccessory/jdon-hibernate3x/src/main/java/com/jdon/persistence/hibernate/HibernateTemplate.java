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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;

import com.jdon.persistence.hibernate.util.HibernateCallback;
import com.jdon.persistence.hibernate.util.SessionFactoryHolder;

public class HibernateTemplate {
	private final static Logger logger = LogManager.getLogger(HibernateTemplate.class);

	private boolean cacheQueries = false;

	private String queryCacheRegion;

	private int fetchSize = 0;

	private int firstResult = 0;

	private int maxResults = 0;

	private SessionProvider sessionProvider;

	/**
	 * Create event new HibernateTemplate instance.
	 * 
	 * @param sessionFactory
	 *            SessionFactory to create Sessions
	 */
	public HibernateTemplate(SessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	/**
	 * Execute the action specified by the given action object within event Session.
	 * 
	 * @param action
	 *            callback object that specifies the Hibernate action
	 * @param exposeNativeSession
	 *            whether to expose the native Hibernate Session to callback
	 *            code
	 * @return event result object returned by the action, or <code>null</code>
	 * @throws org.springframework.dao.Exception
	 *             in case of Hibernate errors
	 */
	public Object doHibernate(HibernateCallback action) throws Exception {

		Session session = sessionProvider.getSession();
		try {
			Object result = action.execute(session);
			return result;
		} catch (Exception ex) {
			logger.error("exception while execute", ex);
			try {
				sessionProvider.rollback();
			} catch (HibernateException ee) {
				logger.error("error while session rollback", ee);
			} finally {
				sessionProvider.resetSession();
			}
			throw new Exception(ex);

		} finally {
			// sessionProvider.returnCloseSession(session);
		}
	}

	// -------------------------------------------------------------------------
	// Convenience methods for loading individual objects
	// -------------------------------------------------------------------------

	public Object get(Class entityClass, Serializable id) throws Exception {
		return get(entityClass, id, null);
	}

	public Object get(final Class entityClass, final Serializable id, final LockMode lockMode) throws Exception {

		return doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.get(entityClass, id, lockMode);
				} else {
					return session.get(entityClass, id);
				}
			}
		});
	}

	public Object get(String entityName, Serializable id) throws Exception {
		return get(entityName, id, null);
	}

	public Object get(final String entityName, final Serializable id, final LockMode lockMode) throws Exception {

		return doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.get(entityName, id, lockMode);
				} else {
					return session.get(entityName, id);
				}
			}
		});
	}

	public Object load(Class entityClass, Serializable id) throws Exception {
		return load(entityClass, id, null);
	}

	public Object load(final Class entityClass, final Serializable id, final LockMode lockMode) throws Exception {

		return doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.load(entityClass, id, lockMode);
				} else {
					return session.load(entityClass, id);
				}
			}
		});
	}

	public Object load(String entityName, Serializable id) throws Exception {
		return load(entityName, id, null);
	}

	public Object load(final String entityName, final Serializable id, final LockMode lockMode) throws Exception {

		return doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.load(entityName, id, lockMode);
				} else {
					return session.load(entityName, id);
				}
			}
		});
	}

	public List loadAll(final Class entityClass) throws Exception {
		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Criteria criteria = session.createCriteria(entityClass);
				prepareCriteria(criteria);
				return criteria.list();
			}
		});
	}

	public void load(final Object entity, final Serializable id) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.load(entity, id);
				return null;
			}
		});
	}

	public void refresh(final Object entity) throws Exception {
		refresh(entity, null);
	}

	public void refresh(final Object entity, final LockMode lockMode) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					session.refresh(entity, lockMode);
				} else {
					session.refresh(entity);
				}
				return null;
			}
		});
	}

	public boolean contains(final Object entity) throws Exception {
		Boolean result = (Boolean) doHibernate(new HibernateCallback() {
			public Object execute(Session session) {
				return (session.contains(entity) ? Boolean.TRUE : Boolean.FALSE);
			}
		});
		return result.booleanValue();
	}

	public void evict(final Object entity) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.evict(entity);
				return null;
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	// -------------------------------------------------------------------------

	public void lock(final Object entity, final LockMode lockMode) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.lock(entity, lockMode);
				return null;
			}
		});
	}

	public void lock(final String entityName, final Object entity, final LockMode lockMode) throws Exception {

		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.lock(entityName, entity, lockMode);
				return null;
			}
		});
	}

	public Serializable save(final Object entity) throws Exception {
		return (Serializable) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				return session.save(entity);
			}
		});
	}

	public Serializable save(final String entityName, final Object entity) throws Exception {
		return (Serializable) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				return session.save(entityName, entity);
			}
		});
	}

	public void update(Object entity) throws Exception {
		update(entity, null);
	}

	public void update(final Object entity, final LockMode lockMode) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.update(entity);
				if (lockMode != null) {
					session.lock(entity, lockMode);
				}
				return null;
			}
		});
	}

	public void update(String entityName, Object entity) throws Exception {
		update(entityName, entity, null);
	}

	public void update(final String entityName, final Object entity, final LockMode lockMode) throws Exception {

		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.update(entityName, entity);
				if (lockMode != null) {
					session.lock(entity, lockMode);
				}
				return null;
			}
		});
	}

	public void saveOrUpdate(final Object entity) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.saveOrUpdate(entity);
				return null;
			}
		});
	}

	public void saveOrUpdate(final String entityName, final Object entity) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.saveOrUpdate(entityName, entity);
				return null;
			}
		});
	}

	public void saveOrUpdateAll(final Collection entities) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				for (Iterator it = entities.iterator(); it.hasNext();) {
					session.saveOrUpdate(it.next());
				}
				return null;
			}
		});
	}

	public void replicate(final Object entity, final ReplicationMode replicationMode) throws Exception {

		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.replicate(entity, replicationMode);
				return null;
			}
		});
	}

	public void replicate(final String entityName, final Object entity, final ReplicationMode replicationMode) throws Exception {

		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.replicate(entityName, entity, replicationMode);
				return null;
			}
		});
	}

	public void persist(final Object entity) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.persist(entity);
				return null;
			}
		});
	}

	public void persist(final String entityName, final Object entity) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.persist(entityName, entity);
				return null;
			}
		});
	}

	public Object merge(final Object entity) throws Exception {
		return doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				return session.merge(entity);
			}
		});
	}

	public Object merge(final String entityName, final Object entity) throws Exception {
		return doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				return session.merge(entityName, entity);
			}
		});
	}

	public void delete(Object entity) throws Exception {
		delete(entity, null);
	}

	public void delete(final Object entity, final LockMode lockMode) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					session.lock(entity, lockMode);
				}
				session.delete(entity);
				return null;
			}
		});
	}

	public void deleteAll(final Collection entities) throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				for (Iterator it = entities.iterator(); it.hasNext();) {
					session.delete(it.next());
				}
				return null;
			}
		});
	}

	public void flush() throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				session.flush();
				return null;
			}
		});
	}

	public void clearSession() throws Exception {
		doHibernate(new HibernateCallback() {
			public Object execute(Session session) {
				session.clear();
				return null;
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for HQL strings
	// -------------------------------------------------------------------------

	public List find(String queryString) throws Exception {
		return find(queryString, (Object[]) null);
	}

	public List find(String queryString, Object value) throws Exception {
		return find(queryString, new Object[] { value });
	}

	public List find(final String queryString, final Object[] values) throws Exception {
		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	public List findByNamedParam(String queryString, String paramName, Object value) throws Exception {

		return findByNamedParam(queryString, new String[] { paramName }, new Object[] { value });
	}

	public List findByNamedParam(final String queryString, final String[] paramNames, final Object[] values) throws Exception {

		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	public List findByValueBean(final String queryString, final Object valueBean) throws Exception {

		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				queryObject.setProperties(valueBean);
				return queryObject.list();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for named queries
	// -------------------------------------------------------------------------

	public List findByNamedQuery(String queryName) throws Exception {
		return findByNamedQuery(queryName, (Object[]) null);
	}

	public List findByNamedQuery(String queryName, Object value) throws Exception {
		return findByNamedQuery(queryName, new Object[] { value });
	}

	public List findByNamedQuery(final String queryName, final Object[] values) throws Exception {
		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	public List findByNamedQueryAndNamedParam(String queryName, String paramName, Object value) throws Exception {

		return findByNamedQueryAndNamedParam(queryName, new String[] { paramName }, new Object[] { value });
	}

	public List findByNamedQueryAndNamedParam(final String queryName, final String[] paramNames, final Object[] values) throws Exception {

		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	public List findByNamedQueryAndValueBean(final String queryName, final Object valueBean) throws Exception {

		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				prepareQuery(queryObject);
				queryObject.setProperties(valueBean);
				return queryObject.list();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	// -------------------------------------------------------------------------

	public List findByCriteria(DetachedCriteria criteria) throws Exception {
		return findByCriteria(criteria, -1, -1);
	}

	public List findByCriteria(final DetachedCriteria criteria, final int firstResult, final int maxResults) throws Exception {

		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				prepareCriteria(executableCriteria);
				if (firstResult >= 0) {
					executableCriteria.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}

	public List findByExample(Object exampleEntity) throws Exception {
		return findByExample(exampleEntity, -1, -1);
	}

	public List findByExample(final Object exampleEntity, final int firstResult, final int maxResults) throws Exception {

		return (List) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Criteria executableCriteria = session.createCriteria(exampleEntity.getClass());
				executableCriteria.add(Example.create(exampleEntity));
				prepareCriteria(executableCriteria);
				if (firstResult >= 0) {
					executableCriteria.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	// -------------------------------------------------------------------------

	public Iterator iterate(String queryString) throws Exception {
		return iterate(queryString, (Object[]) null);
	}

	public Iterator iterate(String queryString, Object value) throws Exception {
		return iterate(queryString, new Object[] { value });
	}

	public Iterator iterate(final String queryString, final Object[] values) throws Exception {
		return (Iterator) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.iterate();
			}
		});
	}

	public int bulkUpdate(String queryString) throws Exception {
		return bulkUpdate(queryString, (Object[]) null);
	}

	public int bulkUpdate(String queryString, Object value) throws Exception {
		return bulkUpdate(queryString, new Object[] { value });
	}

	public int bulkUpdate(final String queryString, final Object[] values) throws Exception {
		Integer updateCount = (Integer) doHibernate(new HibernateCallback() {
			public Object execute(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return new Integer(queryObject.executeUpdate());
			}
		});
		return updateCount.intValue();
	}

	// -------------------------------------------------------------------------
	// Helper methods used by the operations above
	// -------------------------------------------------------------------------

	/**
	 * Prepare the given Query object, applying cache settings and/or event
	 * transaction timeout.
	 * 
	 * @param queryObject
	 *            the Query object to prepare
	 * @see #setCacheQueries
	 * @see #setQueryCacheRegion
	 * @see SessionProviderHolder#applyTransactionTimeout
	 */
	protected void prepareQuery(Query queryObject) {
		if (isCacheQueries()) {
			queryObject.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				queryObject.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			queryObject.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			queryObject.setMaxResults(getMaxResults());
		}

		if (getFirstResult() > 0) {
			queryObject.setFirstResult(getFirstResult());
		}

	}

	/**
	 * Prepare the given Criteria object, applying cache settings and/or event
	 * transaction timeout.
	 * 
	 * @param criteria
	 *            the Criteria object to prepare
	 * @see #setCacheQueries
	 * @see #setQueryCacheRegion
	 * @see SessionProviderHolder#applyTransactionTimeout
	 */
	protected void prepareCriteria(Criteria criteria) {
		if (isCacheQueries()) {
			criteria.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				criteria.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			criteria.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			criteria.setMaxResults(getMaxResults());
		}
	}

	/**
	 * Apply the given name parameter to the given Query object.
	 * 
	 * @param queryObject
	 *            the Query object
	 * @param paramName
	 *            the name of the parameter
	 * @param value
	 *            the value of the parameter
	 * @throws HibernateException
	 *             if thrown by the Query object
	 */
	protected void applyNamedParameterToQuery(Query queryObject, String paramName, Object value) throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	/**
	 * Set whether to cache all queries executed by this template. If this is
	 * true, all Query and Criteria objects created by this template will be
	 * marked as cacheable (including all queries through find methods).
	 * <p>
	 * To specify the query region to be used for queries cached by this
	 * template, set the "queryCacheRegion" property.
	 * 
	 * @see #setQueryCacheRegion
	 * @see org.hibernate.Query#setCacheable
	 * @see org.hibernate.Criteria#setCacheable
	 */
	public void setCacheQueries(boolean cacheQueries) {
		this.cacheQueries = cacheQueries;
	}

	/**
	 * Return whether to cache all queries executed by this template.
	 */
	public boolean isCacheQueries() {
		return cacheQueries;
	}

	/**
	 * Set the name of the cache region for queries executed by this template.
	 * If this is specified, it will be applied to all Query and Criteria
	 * objects created by this template (including all queries through find
	 * methods).
	 * <p>
	 * The cache region will not take effect unless queries created by this
	 * template are configured to be cached via the "cacheQueries" property.
	 * 
	 * @see #setCacheQueries
	 * @see org.hibernate.Query#setCacheRegion
	 * @see org.hibernate.Criteria#setCacheRegion
	 */
	public void setQueryCacheRegion(String queryCacheRegion) {
		this.queryCacheRegion = queryCacheRegion;
	}

	/**
	 * Return the name of the cache region for queries executed by this
	 * template.
	 */
	public String getQueryCacheRegion() {
		return queryCacheRegion;
	}

	/**
	 * Set the fetch size for this HibernateTemplate. This is important for
	 * processing large result sets: Setting this higher than the default value
	 * will increase processing speed at the cost of memory consumption; setting
	 * this lower can avoid transferring row data that will never be read by the
	 * application.
	 * <p>
	 * Default is 0, indicating to use the JDBC driver's default.
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/**
	 * Return the fetch size specified for this HibernateTemplate.
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * Set the maximum number of rows for this HibernateTemplate. This is
	 * important for processing subsets of large result sets, avoiding to read
	 * and hold the entire result set in the database or in the JDBC driver if
	 * we're never interested in the entire result in the first place (for
	 * example, when performing searches that might return event large number of
	 * matches).
	 * <p>
	 * Default is 0, indicating to use the JDBC driver's default.
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * Return the maximum number of rows specified for this HibernateTemplate.
	 */
	public int getMaxResults() {
		return maxResults;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

	public void closeSession() {
		if (SessionFactoryHolder.isSessionIsActive()) {
			sessionProvider.closeSession();
		}
	}

	public void clearParams() {
		this.cacheQueries = false;
		this.fetchSize = 0;
		this.firstResult = 0;
		this.maxResults = 0;
		this.queryCacheRegion = null;

	}

}
