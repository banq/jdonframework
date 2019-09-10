package com.jdon.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <event href="SimpleCachePool.java.html"><b><i>View Source</i></b></event>
 *
 * @author  Brian Chan
 * @version $Revision: 1.1.1.1 $
 *
 */
public class SimpleCachePool {

  private static SimpleCachePool _instance;
  private static int _SIZE = 100000;

  private Map _scPool;

   public static Object get(String id) {
      return _getInstance()._get(id);
   }

   public static void put(String id, Object obj) {
      _getInstance()._put(id, obj);
   }

   public static Object remove(String id) {
      return _getInstance()._remove(id);
   }

   private static SimpleCachePool _getInstance() {
      if (_instance == null) {
         synchronized (SimpleCachePool.class) {
            if (_instance == null) {
               _instance = new SimpleCachePool();
            }
         }
      }
      return _instance;
   }

   private SimpleCachePool() {
      _scPool = new ConcurrentHashMap(_SIZE);
   }

   private Object _get(String id) {
      return (Object)_scPool.get(id);
   }

   private void _put(String id, Object ds) {
      _scPool.put(id, ds);
   }

   private Object _remove(String id) {
      return _scPool.remove(id);
   }



}
