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

package com.jdon.model.query.block;

import java.util.List;

import com.jdon.model.query.JdbcUtil;
import com.jdon.model.query.cache.QueryConditonDatakey;

/**
 * Batch Query JDBC Template
 * pack the sql execution.
 * 
 * @author banq
 */
public interface BlockQueryJDBC {

    /**
     * execute sql that fetch all count 
     * @param queryParam
     * @param sqlquery
     * @return int the count
     * @throws Exception
     */

    public abstract int fetchDataAllCount(QueryConditonDatakey qcd);

    /**
     * execute sql that fetch all ID's collection
     * @param queryParam
     * @param sqlquery
     * @param start
     * @param count
     * @return PageIterator  create event PageIterator instance
     * @throws Exception
     */
    public abstract List fetchDatas(QueryConditonDatakey qcdk) ;
    
    
    public JdbcUtil getJdbcUtil() ;

    //replace JdbcUtil
	public void setJdbcUtil(JdbcUtil jdbcUtil); 

}
