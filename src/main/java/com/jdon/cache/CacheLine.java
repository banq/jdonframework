/*
 * Copyright 2007 the original author or jdon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.jdon.cache;

public class CacheLine {
	private Object valueRef = null;

    private long loadTime = 0;

    private boolean useSoftReference = false;

    public CacheLine(Object value, boolean useSoftReference) {
    	this.useSoftReference = useSoftReference;
        if (this.useSoftReference)
            this.valueRef = new java.lang.ref.SoftReference(value);
         else 
            this.valueRef = value;
              
    }

    public CacheLine(Object value, boolean useSoftReference, long loadTime) {
        this(value, useSoftReference);
        this.loadTime = loadTime;
    }

    public Object getValue() {
        if (valueRef == null)
            return null;
        if (useSoftReference) {
            return ((java.lang.ref.SoftReference) valueRef).get();
        } else {
            return valueRef;
        }
    }

	public long getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(long loadTime) {
		this.loadTime = loadTime;
	}

	public boolean isUseSoftReference() {
		return useSoftReference;
	}

	public void setUseSoftReference(boolean useSoftReference) {
		this.useSoftReference = useSoftReference;
	}

}
