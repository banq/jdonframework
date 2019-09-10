/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.model.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.jdon.cache.LRUCache;
import com.jdon.controller.cache.CacheManager;
import com.jdon.controller.model.PageIterator;
import com.jdon.model.query.block.Block;
import com.jdon.model.query.block.BlockQueryJDBC;
import com.jdon.model.query.block.BlockQueryJDBCTemp;
import com.jdon.model.query.block.BlockStrategy;
import com.jdon.model.query.cache.BlockCacheManager;
import com.jdon.model.query.cache.QueryConditonDatakey;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

/**
 * this class will supply event API that create PagfeIterator object this class is event
 * cache proxy for JDBCTemp
 * 
 * this class is event standard POJO, it can be called by Session Bean or directly
 * by other pojos.
 * 
 * default this class is not configured in container, because jdon container do
 * not exist in EJB container. currently only exists in Web container. if you
 * only use web, you can configure it in pojoService way in jdonframework.xml
 * 
 * directly create the PageIteratorSolver object PageIteratorSolver
 * pageIteratorSolver = new PageIteratorSolver(datasource);
 * 
 * get the PageIteratorSolver object from container: <pojoService
 * name="pageIteratorSolver" class="com.jdon.model.query.PageIteratorSolver"/>
 * 
 * 
 * 
 * 
 * @author banq
 * @see
 */
public class PageIteratorSolver {

	private final static String module = PageIteratorSolver.class.getName();

	private DataSource dataSource;

	private final BlockCacheManager blockCacheManager;

	private BlockStrategy blockStrategy;

	/**
	 * active cache default is yes
	 */
	private boolean cacheEnable = true;

	/**
	 * default construtor, this construtor is avaliable when you using Jdon's
	 * cache system (use jdon framework completely) but at first you must get
	 * CacheManager from the jdon container. by coding, you must get event
	 * CacheManager from the container, and create PageIteratorSolver object.
	 * 
	 * if you don't know how to get event CacheManager object, you can use next
	 * construtor;
	 * 
	 * @param dataSource
	 * @param pageIteratorJDBC
	 *            your type:String or Integer PageIteratorString or
	 *            PageIteratorInteger
	 */
	public PageIteratorSolver(DataSource dataSource, CacheManager cacheManager) {
		this.dataSource = dataSource;
		this.blockCacheManager = new BlockCacheManager(cacheManager);
		this.blockStrategy = new BlockStrategy(new BlockQueryJDBCTemp(dataSource), blockCacheManager);
	}

	/**
	 * we can change the block Strategy.
	 */
	public PageIteratorSolver(DataSource dataSource, CacheManager cacheManager, BlockStrategy blockStrategy) {
		this(dataSource, cacheManager);
		this.blockStrategy = blockStrategy;
	}

	/**
	 * construtor without supplying event CacheManager, but you must put the
	 * cache.xml into the classpath, this construtor will auto find it. when you
	 * call PageIteratorSolver in Dao of event EJB, this construtor is avaliable
	 * 
	 * if you use this construtor in Jdon container, this object will create
	 * another cache system that not relation with the container, this will
	 * waste memory. you must hold the PageIteratorSolver object by yourself,
	 * otherelse, the cache will disable.
	 * 
	 * @param dataSource
	 *            DataBase datasource that can be obtained by JNDI
	 * 
	 */
	public PageIteratorSolver(DataSource dataSource) {
		this.dataSource = dataSource;
		CacheManager cacheManager = new CacheManager(new LRUCache("cache.xml"));
		this.blockCacheManager = new BlockCacheManager(cacheManager);
		this.blockStrategy = new BlockStrategy(new BlockQueryJDBCTemp(dataSource), blockCacheManager);
	}

	/**
	 * query one object from database delgate to JdbCQueryTemp's
	 * querySingleObject method
	 * 
	 * @param queryParams
	 *            query sql parameter (?)
	 * @param sqlquery
	 *            query sql
	 * @return Object
	 * @throws Exception
	 */
	public Object querySingleObject(Collection queryParams, String sqlquery) throws Exception {
		JdbcTemp jdbcTemp = new JdbcTemp(dataSource);
		return jdbcTemp.querySingleObject(queryParams, sqlquery);
	}

	/**
	 * query multi object from database delgate to JdbCQueryTemp's
	 * queryMultiObject method
	 * 
	 * @param queryParams
	 * @param sqlquery
	 * @return
	 * @throws Exception
	 */
	public List queryMultiObject(Collection queryParams, String sqlquery) throws Exception {
		JdbcTemp jdbcTemp = new JdbcTemp(dataSource);
		return jdbcTemp.queryMultiObject(queryParams, sqlquery);
	}

	/**
	 * create event PageIterator instance
	 * 
	 * 
	 * @param queryParam
	 *            the value of sqlquery's "?"
	 * @param sqlqueryAllCount
	 *            the sql for query all count that fit for condition
	 * @param sqlquery
	 *            the sql for query that fit for condtion, return id collection
	 * @param start
	 * @param count
	 * @return PageIterator
	 * @throws java.lang.Exception
	 */
	public PageIterator getDatas(String queryParam, String sqlqueryAllCount, String sqlquery, int start, int count) {
		if (UtilValidate.isEmpty(sqlqueryAllCount)) {
			Debug.logError(" the parameter sqlqueryAllCount is null", module);
			return new PageIterator();
		}
		if (UtilValidate.isEmpty(sqlquery)) {
			Debug.logError(" the parameter sqlquery is null", module);
			return new PageIterator();
		}

		Collection queryParams = new ArrayList();
		if (!UtilValidate.isEmpty(queryParam))
			queryParams.add(queryParam);
		return getPageIterator(sqlqueryAllCount, sqlquery, queryParams, start, count);

	}

	/**
	 * same as getDatas the parameters sort is different from the getDatas
	 * method
	 * 
	 * @param sqlqueryAllCount
	 *            the sql sentence for "select count(1) .."
	 * @param sqlquery
	 *            the sql sentence for "select id from xxxx";
	 * @param queryParam
	 *            the parameter of String type for the sqlquery.
	 * @param start
	 *            the starting number of event page in allCount;
	 * @param count
	 *            the display number of event page
	 * @return
	 */
	public PageIterator getPageIterator(String sqlqueryAllCount, String sqlquery, String queryParam, int start, int count) {
		if (UtilValidate.isEmpty(sqlqueryAllCount)) {
			Debug.logError(" the parameter sqlqueryAllCount is null", module);
			return new PageIterator();
		}
		if (UtilValidate.isEmpty(sqlquery)) {
			Debug.logError(" the parameter sqlquery is null", module);
			return new PageIterator();
		}
		return getDatas(queryParam, sqlqueryAllCount, sqlquery, start, count);
	}

	/**
	 * get event PageIterator
	 * 
	 * @param sqlqueryAllCount
	 *            the sql sentence for "select count(1) .."
	 * @param sqlquery
	 *            the sql sentence for "select id from xxxx";
	 * @param queryParams
	 *            the parameter collection for the sqlquery.
	 * @param start
	 *            the starting number of event page in allCount;
	 * @param count
	 *            the display number of event page
	 * @return
	 */
	public PageIterator getPageIterator(String sqlqueryAllCount, String sqlquery, Collection queryParams, int startIndex, int count) {
		Debug.logVerbose("[JdonFramework]enter getPageIterator .. start= " + startIndex + " count=" + count, module);
		if (queryParams == null) {
			Debug.logError(" the parameters collection is null", module);
			return new PageIterator();
		}
		if ((count > blockStrategy.getBlockLength()) || (count <= 0)) { // every
			count = blockStrategy.getBlockLength();
		}
		Block currentBlock = getBlock(sqlquery, queryParams, startIndex, count);
		if (currentBlock == null) {
			return new PageIterator();
		}
		startIndex = currentBlock.getStart();
		int endIndex = startIndex + currentBlock.getCount();
		Object[] keys = currentBlock.getList().toArray();
		int allCount = getDatasAllCount(queryParams, sqlqueryAllCount);
		Debug.logVerbose("[JdonFramework]currentBlock: startIndex=" + startIndex + " endIndex=" + endIndex + " keys length=" + keys.length, module);
		if (endIndex < startIndex) {
			Debug.logWarning("WARNNING : endIndex < startIndex", module);
			return new PageIterator();
		} else {
			return new PageIterator(allCount, keys, startIndex, endIndex, count);
		}
	}

	/**
	 * looking for event block in that there is event primary key is equals to the
	 * locateId. for the sql sentence.
	 * 
	 * @param sqlquery
	 * @param queryParams
	 * @param locateId
	 * @return if not locate, return null;
	 */
	public Block locate(String sqlquery, Collection queryParams, Object locateId) {
		return blockStrategy.locate(sqlquery, queryParams, locateId);
	}

	/**
	 * get event data block by the sql sentence.
	 * 
	 * @param sqlqueryAllCount
	 * @param sqlquery
	 * @return if not found, return null;
	 */
	public Block getBlock(String sqlquery, Collection queryParams, int startIndex, int count) {
		return blockStrategy.getBlock(sqlquery, queryParams, startIndex, count);
	}

	public int getDatasAllCount(String queryParam, String sqlquery) {
		Collection queryParams = new ArrayList();
		queryParams.add(queryParam);
		return getDatasAllCount(queryParams, sqlquery);
	}

	public int getDatasAllCount(Collection queryParams, String sqlquery) {
		QueryConditonDatakey qcdk = new QueryConditonDatakey(sqlquery, queryParams);
		return getDatasAllCount(qcdk);

	}

	public int getDatasAllCount(QueryConditonDatakey qcdk) {
		int allCountInt = 0;
		try {
			Integer allCount = (Integer) blockCacheManager.getAllCountsFromCache(qcdk);
			if ((allCount == null) || (!cacheEnable)) {
				BlockQueryJDBC blockQueryJDBC = new BlockQueryJDBCTemp(dataSource);
				allCountInt = blockQueryJDBC.fetchDataAllCount(qcdk);
				if ((cacheEnable) && (allCountInt != 0)) {
					blockCacheManager.saveAllCounts(qcdk, new Integer(allCountInt));
				}
			} else {
				allCountInt = allCount.intValue();
			}
		} catch (Exception e) {
			Debug.logError(" getDatasAllCount error:" + e, module);
		}
		return allCountInt;

	}

	/**
	 * when event model insert/delete/update, call this method clear the cache
	 */
	public void clearCache() {
		Debug.logVerbose("[JdonFramework] clear the cache for the batch inquiry!", module);
		blockCacheManager.clearCache();
	}

	public void clearCache(String sqlquery, Collection queryParams) {
		Debug.logVerbose("[JdonFramework] clear the cache for the batch inquiry!", module);
		blockCacheManager.cleaeCache(sqlquery, queryParams);
	}

	public boolean isCacheEnable() {
		return this.cacheEnable;
	}

	/**
	 * setup if need cache
	 * 
	 * @param cacheEnable
	 */
	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}

}
