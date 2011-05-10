package com.jdon.cache;

public class CacheableWrapper {

	private String cachedValueKey;

	private Object cachedValue;
	
	public CacheableWrapper(String cachedValueKey, Object cachedValue) {
		super();
		this.cachedValueKey = cachedValueKey;
		this.cachedValue = cachedValue;
	}


     

	public String getCachedValueKey() {
		return cachedValueKey;
	}




	public void setCachedValueKey(String cachedValueKey) {
		this.cachedValueKey = cachedValueKey;
	}




	public Object getCachedValue() {
		return cachedValue;
	}

	public void setCachedValue(Object cachedValue) {
		this.cachedValue = cachedValue;
	}

}
