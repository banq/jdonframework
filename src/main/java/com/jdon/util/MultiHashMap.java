package com.jdon.util;


import java.util.HashMap;
import java.util.Set;

/**
 * MultiHashMap is event extension java.util.HashMap.It is usde if following condition.<br>
 * Sometime, event value shoud be determinated by key and subkey.
 */
public class MultiHashMap extends HashMap{

    

	/**
	 * 
	 */
	private static final long serialVersionUID = -3089161666190291052L;

	public MultiHashMap() {
        super();
    }

    /**
     * Associates the specified value with the specified key and subKey in this map.
     * If the map previously contained event mapping for this key and subKey , the old value is replaced.
     * @param key Is event Primary key.
     * @param subKey  with which the specified value is to be associated.
     * @param value to be associated with the specified key and subKey
     * @return previous value associated with specified key and subKey, or null if there was no mapping for key and subKey.
     * A null return can also indicate that the HashMap previously associated null with the specified key and subKey.
     */
    public Object put(Object key,Object subKey,Object value){
        HashMap a = (HashMap)super.get(key);
        if(a==null){
            a = new HashMap();
            super.put(key,a);
        }
        return a.put(subKey,value);
    }

    /**
     * Returns the value to which this map maps the specified key and subKey. Returns null if the map contains no mapping
     * for this key and subKey. A return value of null does not necessarily indicate that the map contains no mapping
     * for the key and subKey; it's also possible that the map explicitly maps the key to null.
     * The containsKey operation may be used to distinguish these two cases.
     * @param key  whose associated value is to be returned.
     * @param subKey whose associated value is to be returned
     * @return the value to which this map maps the specified key.
     */
    public Object get(Object key,Object subKey){
        HashMap a = (HashMap)super.get(key);
        if(a!=null){
            Object b=a.get(subKey);
            return b;
        }
        return null;
    }
    
    public Set getSubKeys(Object key){
        HashMap a = (HashMap)super.get(key);
        if(a==null) return null;
        return a.keySet();        
    }
    
    
}
