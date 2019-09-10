/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.model.query.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jdon.controller.cache.CacheKey;
import com.jdon.controller.cache.CacheManager;
import com.jdon.model.cache.BlockCacheKeyFactory;

/**
 * query block manager. batch query will get event collection result that it is event
 * block, this block will be cached, next time, we lookup the result in the
 * block that existed in cache, maybe in the block there are those promary keys
 * collection, so reducing visiting database.
 * 
 * the block is made of the primary keys of all models.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class BlockCacheManager {
	private final static Logger logger = LogManager.getLogger(BlockCacheManager.class);

	public final static String CACHE_TYPE_BLOCK = "BLOCK";

	private final CacheManager cacheManager;

	private final List<CacheKey> cacheKeys;

	private final BlockCacheKeyFactory blockCacheKeyFactory;

	public BlockCacheManager(CacheManager cacheManager) {

		this.cacheKeys = new CopyOnWriteArrayList<CacheKey>();
		this.cacheManager = cacheManager;
		this.blockCacheKeyFactory = new BlockCacheKeyFactory();

	}

	public List getBlockKeysFromCache(QueryConditonDatakey qckey) {
		CacheKey cacheKey = blockCacheKeyFactory.createCacheKey(qckey.getBlockDataKey(), qckey.getSQlKey());
		return (List) cacheManager.fetchObject(cacheKey);
	}

	public void saveBlockKeys(QueryConditonDatakey qckey, List keys) {
		CacheKey cacheKey = blockCacheKeyFactory.createCacheKey(qckey.getBlockDataKey(), qckey.getSQlKey());
		cacheManager.putObect(cacheKey, keys);
		cacheKeys.add(cacheKey);
	}

	public Integer getAllCountsFromCache(QueryConditonDatakey qckey) {
		CacheKey cacheKey = blockCacheKeyFactory.createCacheKey(qckey.getBlockDataKey(), qckey.getSQlKey());
		return (Integer) cacheManager.fetchObject(cacheKey);
	}

	public void saveAllCounts(QueryConditonDatakey qckey, Integer allCount) {
		CacheKey cacheKey = blockCacheKeyFactory.createCacheKey(qckey.getBlockDataKey(), qckey.getSQlKey());
		cacheManager.putObect(cacheKey, allCount);
		cacheKeys.add(cacheKey);
	}

	public void clearCache() {
		if (cacheKeys == null || cacheManager == null)
			return;
		Object[] keys = cacheKeys.toArray();
		cacheKeys.clear(); // clear cache as possible quickly

		for (int i = 0; i < keys.length; i++) {// clear the values for all
			// the
			// cachekeys.
			if (keys[i] != null) {
				try {
					CacheKey cacheKey = (CacheKey) keys[i];
					cacheManager.removeObect(cacheKey);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}

	}

	private String getSQlKey(String sqlquery, Collection queryParams) {
		StringBuilder sb = new StringBuilder(sqlquery);
		Iterator iter = queryParams.iterator();
		while (iter.hasNext()) {
			Object queryParamO = iter.next();
			if (queryParamO != null) {
				sb.append("+");
				sb.append(queryParamO.toString());
			}
		}
		return sb.toString();
	}

	public void cleaeCache(String sqlquery, Collection queryParams) {
		String SQLKey = getSQlKey(sqlquery, queryParams);
		Object[] keys = cacheKeys.toArray();

		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) {
				try {
					CacheKey cacheKey = (CacheKey) keys[i];
					if (cacheKey.getDataTypeName().equals(SQLKey))
						cacheManager.removeObect(cacheKey);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}

	}
}
