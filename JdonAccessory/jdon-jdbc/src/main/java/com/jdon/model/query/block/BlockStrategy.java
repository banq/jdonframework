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
package com.jdon.model.query.block;

import java.util.Collection;
import java.util.List;

import com.jdon.model.query.cache.BlockCacheManager;
import com.jdon.model.query.cache.QueryConditonDatakey;
import com.jdon.util.Debug;

/**
 * Block Strategy to increase the performance for event lot of datas fetching from
 * the database ervery time read the database, we fetch event block that maye
 * include 200 datas. note: these datas are the primary keys of the models that
 * save in database.
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class BlockStrategy {
	private final static String module = BlockStrategy.class.getName();

	private final BlockQueryJDBC blockQueryJDBC;

	private final BlockCacheManager blockCacheManager;

	private int blockLength = 200;

	/**
	 * @param blockQueryJDBC
	 * @param jdbcTemp
	 * @param blockCacheManager
	 */
	public BlockStrategy(BlockQueryJDBC blockQueryJDBC, BlockCacheManager blockCacheManager) {
		super();
		this.blockQueryJDBC = blockQueryJDBC;
		this.blockCacheManager = blockCacheManager;
	}

	/**
	 * looking for the primary be equals to locateId in the result for the sql
	 * sentence.
	 * 
	 * @param sqlquery
	 * @param queryParams
	 * @param locateId
	 * @return if not locate, return null;
	 */
	public Block locate(String sqlquery, Collection queryParams, Object locateId) {
		int blockSize = getBlockLength();
		Block block = null;
		int index = -1;
		int prevBlockStart = Integer.MIN_VALUE;
		int nextBlockStart = Integer.MIN_VALUE;
		int start = 0;

		Debug.logVerbose("[JdonFramework]try to locate event block locateId= " + locateId + " blockSize=" + blockSize, module);

		try {
			while (index == -1) {
				block = getBlock(sqlquery, queryParams, start, blockSize);
				if (block == null)
					break;

				List list = block.getList();
				index = list.indexOf(locateId);
				if ((index >= 0) && (index < list.size())) {
					Debug.logVerbose("[JdonFramework]found the locateId, index= " + index, module);
					if ((index == 0) && (block.getStart() >= blockSize))// if is
						// the
						// first
						// in
						// this
						// block
						prevBlockStart = start - blockSize;
					else if (index == blockSize - 1) // // if is the last in
						// this block
						nextBlockStart = start + blockSize;
					break;
				} else {
					if (block.getCount() >= blockSize)// there are more block.
						start = start + blockSize;
					else
						// there are no more block;
						break;
				}
			}

			if (index == -1) {
				Debug.logVerbose("[JdonFramework] not locate the block that have the locateId= " + locateId, module);
				return null; // not found return null
			}

			if (prevBlockStart != Integer.MIN_VALUE) {
				Block prevBlock = getBlock(sqlquery, queryParams, prevBlockStart, blockSize);
				prevBlock.getList().addAll(block.getList());
				prevBlock.setStart(prevBlock.getStart() + prevBlock.getCount());
				prevBlock.setCount(prevBlock.getCount() + block.getCount());
				return prevBlock;
			} else if (nextBlockStart != Integer.MIN_VALUE) { // if
				// nextBlockStart
				// has new
				// value, so we
				// need next
				// block, fetch
				// it.
				Block nextBlock = getBlock(sqlquery, queryParams, nextBlockStart, blockSize);
				if (nextBlock != null) {
					block.getList().addAll(nextBlock.getList());
					block.setCount(block.getCount() + nextBlock.getCount());
				}
				return block;
			} else
				return block;
		} catch (Exception e) {
			Debug.logError(" locate Block error" + e, module);
		}
		return block;
	}

	/**
	 * get event data block by the sql sentence.
	 * 
	 * @param sqlqueryAllCount
	 * @param sqlquery
	 * @return if not found, return null;
	 */
	public Block getBlock(String sqlquery, Collection queryParams, int startIndex, int count) {
		Debug.logVerbose("[JdonFramework]enter getBlock .. ", module);
		if ((count > this.blockLength) || (count <= 0)) { // every query max
			// length must be
			// little than
			// blockLength
			count = this.blockLength;
		}
		QueryConditonDatakey qcdk = new QueryConditonDatakey(sqlquery, queryParams, startIndex, count, this.blockLength);
		Block block = getBlock(qcdk);
		if (block.getCount() > 0) {
			Debug.logVerbose("[JdonFramework]got event Block" + block.getCount(), module);
			return block;
		} else {
			Debug.logVerbose("[JdonFramework]not found the block!", module);
			return null;
		}
	}

	/**
	 * get the current block being avaliable to the query condition
	 * 
	 * @param qcdk
	 * @return
	 */
	private Block getBlock(QueryConditonDatakey qcdk) {
		Block clientBlock = new Block(qcdk.getStart(), qcdk.getCount());
		if (clientBlock.getCount() > this.blockLength)
			clientBlock.setCount(this.blockLength);

		// create dataBlock get DataBase or cache;
		List list = getBlockKeys(qcdk);
		Block dataBlock = new Block(qcdk.getBlockStart(), list.size());

		int currentStart = clientBlock.getStart() - dataBlock.getStart();
		Block currentBlock = new Block(currentStart, clientBlock.getCount());
		currentBlock.setList(list);

		try {
			// because clientBlock's length maybe not be equals to the
			// dataBlock's length
			// we need the last length:
			// 1.the last block length is great than the clientBlock's
			// length，the current block's length is the clientBlock's length
			// 2.the last block length is less than the clientBlock's
			// length，under this condition, there are two choice.
			int lastCount = dataBlock.getCount() + dataBlock.getStart() - clientBlock.getStart();
			Debug.logVerbose("[JdonFramework] lastCount=" + lastCount, module);
			// 2 happened
			if (lastCount < clientBlock.getCount()) {
				// if 2 , two case：
				// 1. if the dataBlock's length is this.blockLength(200)，should
				// have more dataBlocks.
				if (dataBlock.getCount() == this.blockLength) {
					// new datablock, we must support new start and new count
					// the new start = old datablock's length + old datablock's
					// start.
					int newStartIndex = dataBlock.getStart() + dataBlock.getCount();
					int newCount = clientBlock.getCount() - lastCount;
					qcdk.setStart(newStartIndex);
					qcdk.setCount(newCount);
					Debug.logVerbose("[JdonFramework]  newStartIndex=" + newStartIndex + " newCount=" + newCount, module);
					Block nextBlock = getBlock(qcdk);
					Debug.logVerbose("[JdonFramework]  nextBlock.getCount()=" + nextBlock.getCount(), module);
					currentBlock.setCount(currentBlock.getCount() + nextBlock.getCount());
				} else {
					// 2. if not, all datas just be here, clientBlock's count
					// value maybe not correct.
					currentBlock.setCount(lastCount);
				}
			}
		} catch (Exception e) {
			Debug.logError(" getBlock error" + e, module);
		}
		return currentBlock;
	}

	/**
	 * get event Block that begin at the start
	 * 
	 * @param qcdk
	 *            QueryConditonDatakey
	 * @return return the collection fit for start value and count value.
	 */
	private List getBlockKeys(QueryConditonDatakey qcdk) {
		List keys = blockCacheManager.getBlockKeysFromCache(qcdk);
		if ((keys == null)) {
			keys = blockQueryJDBC.fetchDatas(qcdk);
			if (keys != null && keys.size() != 0)
				blockCacheManager.saveBlockKeys(qcdk, keys);
		}
		Debug.logVerbose("[JdonFramework] getBlockKeys, size=" + keys.size(), module);
		return keys;
	}

	/**
	 * @return Returns the blockLength.
	 */
	public int getBlockLength() {
		return blockLength;
	}

	/**
	 * @param blockLength
	 *            The blockLength to set.
	 */
	public void setBlockLength(int blockLength) {
		this.blockLength = blockLength;
	}

}
