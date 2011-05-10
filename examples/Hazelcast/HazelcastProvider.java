package com.jdon.components.hazelcast;

import java.util.Collection;

import com.hazelcast.core.Hazelcast;
import com.jdon.components.encache.EhcacheConf;
import com.jdon.components.encache.EncacheProvider;
import com.jdon.util.Debug;

import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
/**
 * 
 * @author flyzb
 *
 */
public class HazelcastProvider implements com.jdon.controller.cache.Cache {
	private final static String module = HazelcastProvider.class.getName();
	private String defaultMap="default";
	
	public HazelcastProvider() {
		Debug.logVerbose("HazelcastProvider construting", module);		
	}
	
	public Object get(Object key) {
		Debug.logVerbose("hazelcast get key", module);
		Map map     = Hazelcast.getMap  (defaultMap);
		Element e = (Element) map.get(key);
		if (e == null)
			return null;
		return e.getObjectValue();		
	}


	public void put(Object key, Object value) {
		Debug.logVerbose("hazelcast put key value", module);
		Element element = new Element(key, value);
		Map map     = Hazelcast.getMap  (defaultMap);
		map.put(key, element);
	}


	public void remove(Object key) {
		Debug.logVerbose("hazelcast remove", module);
		Map map     = Hazelcast.getMap  (defaultMap);
		map.remove(key);
	}


	public long size() {
		Map map     = Hazelcast.getMap  (defaultMap);
		return map.size();
	}


	public void clear() {
		Map map     = Hazelcast.getMap  (defaultMap);
		map.clear();		
	}


	public boolean contain(Object key) {
		Map map     = Hazelcast.getMap  (defaultMap);
		return map.containsKey(key);
	}


	public Collection keySet() {
		Map map     = Hazelcast.getMap  (defaultMap);
		return map.keySet();
	}

}
