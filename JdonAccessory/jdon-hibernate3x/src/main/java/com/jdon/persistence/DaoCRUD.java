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
package com.jdon.persistence;

import java.io.Serializable;

import javax.sql.DataSource;


public interface DaoCRUD extends java.io.Serializable{

	public abstract DataSource getDataSource();

	public abstract void setDataSource(DataSource dataSource);

	
	/* (non-Javadoc)
	 * @see sample.persistence.UserDaoIF#insert(sample.model.User)
	 */
	public abstract void insert(Object o) throws Exception;

	/* (non-Javadoc)
	 * @see sample.persistence.UserDaoIF#update(sample.model.User)
	 */
	public abstract void update(Object o) throws Exception;
	
	public void saveOrUpdate(Object o) throws Exception;
	
	public void merge(Object o) throws Exception;

	public abstract void delete(Object o) throws Exception;

	/* (non-Javadoc)
	 * @see sample.persistence.UserDaoIF#getUser(java.lang.String)
	 */
	public abstract Object loadById(Class entity, Serializable id) throws Exception ;

}