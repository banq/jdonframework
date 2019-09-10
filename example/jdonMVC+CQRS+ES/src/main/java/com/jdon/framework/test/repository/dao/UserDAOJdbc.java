/**
 * Copyright 2005 Jdon.com Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jdon.framework.test.repository.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.Around;
import com.jdon.framework.test.domain.UserModel;
import com.jdon.framework.test.repository.ModelCacheManager;
import com.jdon.framework.test.repository.UserRepository;
import com.jdon.model.query.JdbcTemp;

/**
 * before using this class , you must active database
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
@Component("userRepository")
@Introduce("modelCache")
public class UserDAOJdbc implements UserRepository {

	private final static Logger logger = Logger.getLogger(UserDAOJdbc.class);

	private final JdbcTemp jdbcTemp;

	private final ModelCacheManager modelCacheManager;

	/**
	 * Constants must be configured in jdonframework.xml CacheManager has been
	 * in jdon container, do not need configured it again. in the jdon
	 * framework's container.xml : <component name="cacheManager"
	 * class="com.jdon.controller.cache.CacheManager" />
	 * 
	 * @param constants
	 * @param cacheManager
	 */
	public UserDAOJdbc(ModelCacheManager modelCacheManager) throws NamingException {
		this.jdbcTemp = new JdbcTemp(modelCacheManager.getDataSource());
		this.modelCacheManager = modelCacheManager;
	}

	public void save(UserModel userTest) throws Exception {
		logger.debug(" enter insert ");
		String sql = "INSERT INTO testuser (userId , name) " + "VALUES (?, ?)";

		List queryParams = new ArrayList();
		queryParams.add(userTest.getUserId());
		queryParams.add(userTest.getUsername());
		jdbcTemp.operate(queryParams, sql);

		modelCacheManager.clearModelList();
	}

	public void update(UserModel userTest) throws Exception {
		String sql = "update testuser set name=? where userId=?";
		List queryParams = new ArrayList();
		queryParams.add(userTest.getUsername());
		queryParams.add(userTest.getUserId());
		jdbcTemp.operate(queryParams, sql);

		modelCacheManager.removeModelFromCache(userTest.getUserId());
	}

	public void delete(String userId) throws Exception {
		String sql = "delete from testuser where userId=?";
		List queryParams = new ArrayList();
		queryParams.add(userId);
		jdbcTemp.operate(queryParams, sql);

		modelCacheManager.removeModelFromCache(userId);
		modelCacheManager.clearModelList();
	}

	@Around
	public UserModel getUser(String Id) {
		logger.debug(" enter getUser:" + Id);

		String GET_FIELD = "select  * from testuser where userId = ?";
		List queryParams = new ArrayList();
		queryParams.add(Id);

		UserModel ret = null;

		try {
			List list = jdbcTemp.queryMultiObject(queryParams, GET_FIELD);
			Iterator iter = list.iterator();
			if (iter.hasNext()) {
				Map map = (Map) iter.next();
				ret = new UserModel();
				ret.setUsername((String) map.get("name"));
				ret.setUserId((String) map.get("userId"));
			}
		} catch (Exception e) {
			logger.error("getUser" + e);
		}
		return ret;
	}

}
