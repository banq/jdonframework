package com.jdon.container.access;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.pico.Startable;

/**
 * 以service name为key, Service TargetMetaDef为value
 * 
 * this class is registered in contain.xml every name has one TargetMetaDef
 * instance, if multi thread access same TargetMetaDef instance, they will
 * crash.
 * 
 * @author banq
 * 
 */
public class TargetMetaDefHolder implements Startable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9184139922656648301L;
	/**
	 * 以service name为key, Service TargetMetaDef为value
	 */
	private final Map<String, TargetMetaDef> metaDefs;

	public TargetMetaDefHolder() {
		this.metaDefs = new HashMap<String, TargetMetaDef>();
	}

	public void add(Map<String, TargetMetaDef> maps) {
		metaDefs.putAll(maps);
	}

	public TargetMetaDef getTargetMetaDef(String name) {
		return metaDefs.get(name);
	}

	public void start() {

	}

	public void stop() {
		metaDefs.clear();
	}

	public void add(String name, TargetMetaDef targetMetaDef) {
		metaDefs.put(name, targetMetaDef);
	}

	// for reister in container of DefaultContainerBuilder.registerUserService
	public Map<String, TargetMetaDef> loadMetaDefs() {
		return metaDefs;
	}

	public String lookupForName(String ClassName) {
		for (String name : metaDefs.keySet()) {
			TargetMetaDef tm = metaDefs.get(name);
			if (tm.getClassName().equals(ClassName)) {
				return name;
			}
		}
		return null;

	}

}
