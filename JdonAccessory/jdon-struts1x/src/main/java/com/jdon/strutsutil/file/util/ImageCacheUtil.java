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

package com.jdon.strutsutil.file.util;



public class ImageCacheUtil {
    /**
  public ImageCacheUtil(CacheProcessorRegistry CacheFactoryRegistry) {
  }


  public Object getImageFromCache(String Id) throws Exception {
    CacheKey cacheKey = cacheKeyFactory.createCacheKey(Id, modelClassName);
    return cacheManager.getObect(cacheKey);
  }

  public void saveImageToCache(String Id, Object o) throws Exception {
    CacheKey cacheKey = cacheKeyFactory.createCacheKey(Id, modelClassName);
    cacheManager.putObect(cacheKey, o);
  }

  public void removeImageFromCache(String Id) throws Exception {
    CacheKey cacheKey = cacheKeyFactory.createCacheKey(Id, modelClassName);
    cacheManager.removeObect(cacheKey);
  }

  public void getCacheKeyFactory(HttpServletRequest request){
    CacheFactory cacheManager = null;

    try {
    cacheManager = (CacheFactory)cfR.lookup();
    cacheKeyFactory = new ModelCacheKeyFactory(cacheManager);
  } catch (Exception ex) {
    Debug.logError("[JdonFramework] ImageCacheFilter init error: " + ex, module);

  }

  }
      }
     **/
}
