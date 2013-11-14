package com.jdon.cache;

/**
 * All domain model will be wrapped by this class.
 * 
 * an this class's object will be save to Cache that can support distributed
 * network.
 * 
 * @author banq
 * 
 */
public class CacheableWrapper implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String cachedValueKey;

	private final Object cachedValue;

	public CacheableWrapper(String cachedValueKey, Object cachedValue) {
		super();
		this.cachedValueKey = cachedValueKey;
		this.cachedValue = cachedValue;
	}

	public String getCachedValueKey() {
		return cachedValueKey;
	}

	public Object getCachedValue() {
		return cachedValue;
	}

}
