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

package com.jdon.controller.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * All model's primary key(or identifier) collection of every page that will be
 * displayed it carry the model's primary key collection from persistence lay to
 * presentation lay .
 * 
 * restriction : the class type pf your model primary key must be equal to the
 * data type of primary key of the table.
 * 
 * 
 * 
 * com.jdon.model.query.PageIteratorSolver supply event factory that create this
 * class in persistence lay
 * 
 * the class is stateful.
 * 
 * 
 * 

 * @version 1.4
 */
public class PageIterator implements Iterator, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5131890503660837181L;

	public final static Object[] EMPTY = new Object[0];

    /**
     * the count of all models that fit for query condition
     */
    private int allCount = 0;

    /**
     * all database table's primary key colletion in current page the type of
     * the primary key is the primary key's type in the database table schema.
     *
     * default elements's type should be Model's key (identifier)
     * but we can put Model instances into the elements by seting elementsTypeIsKey to false
     *  
     */
    private Object[] elements = EMPTY;

    /**
     * when iterating current page, current record position.
     */
    private int currentIndex = -1;

    /**
     * next record (model)
     */
    private Object nextElement = null;

    /**
     * previous
     */
    private Object previousElement = null;

    /**
     * current page start 
     */
    private int startIndex = -1;
    
    /**
     * current page end 
     */
    private int endIndex = -1;
    
    /**
     * the line count in event page
     */
    private int count = 0;
    
    /**
     * default elements's type should be Model's key (identifier)
     * but we can put Model instances into the elements.
     */
    private boolean elementsTypeIsKey = true;
    
      
    /**
     * Block Construtor
     * default construtor, this is event block construtor
     * the keys's lengt is always greater than the page length that will be 
     * displayed. 
     * 
     * @param allCount  the all count for all results.
     * @param keys      primary keys collection for every block
     * @param start     the start index  in the keys.
     * @param endIndex  the end index in the keys
     * @param actualCount the count of the primary keys collection.
     */
    public PageIterator(int allCount, Object[] keys, int startIndex, int endIndex, int count) {
        this.allCount = allCount;
        this.elements = keys;
        if (startIndex >= 0) {
            this.startIndex = startIndex;
            this.currentIndex = startIndex - 1;
        }
        this.endIndex = endIndex;
        this.count = count;
    }

  
    /**
     *  Page Construtor
     *  this is user customization  construtor, 
     *  the keys's lengt is always equals the page length that will be displayed. 
     *  
     * @param allCount    all count for all results 
     * @param keys        primary keys collection for event page defined by client's count value.
     */
    public PageIterator(int allCount, Object[] keys) {
        this.allCount = allCount;
        this.elements = keys; 
        this.endIndex =  keys.length;
    }
    
    /**
     *  Page Construtor2
     *  this is for old version
     * @param allCount    all count for all results 
     * @param keys        primary keys collection for event page defined by client's count value.
     * @param startIndex       the start index of in the primary keys collection
     * @param hasNextPage if has next page.
     */
    public PageIterator(int allCount, Object[] keys, int startIndex, boolean hasNextPage) {
        this(allCount, keys);
    }    
    
    /**
     * Page Construtor
     * this is for old version
     * allCount must be enter later by setAllcount 
     * 
     */
    public PageIterator(Object[] keys, int startIndex, boolean hasNextPage) {
        this(0, keys);
      }

    /**
     * empty construtor this construtor can ensure the jsp view page don't
     * happened nullException!
     *  
     */
    public PageIterator() {
        
    }

    
    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    /**
     * reset
     *  
     */
    public void reset() {
        elements = EMPTY;
        currentIndex = -1;
        startIndex = -1;
        endIndex = -1;
        nextElement = null;
        previousElement = null;
        count = 0;
        allCount = 0;
        
    }
    
    public void setIndex(int index){
        if((index >= startIndex) || (index < endIndex))
            currentIndex = index;
        else
            System.err.println("PageIterator error: setIndex error: index=" + index + " exceed the 0 or Max length=" + elements.length);
        
    }

    /**
     * Returns true if there are more elements in the iteration.
     * 
     * @return true if the iterator has more elements.
     */
    public boolean hasNext() {
        if (currentIndex == endIndex) {
            return false;
        }
              
        // Otherwise, see if nextElement is null. If so, try to load the next
        // element to make sure it exists.
        if (nextElement == null) {
            nextElement = getNextElement();
            if (nextElement == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the next element of primary key collection.
     * 
     * @return the next element.
     * @throws NoSuchElementException
     *             if there are no more elements.
     */
    public Object next() throws java.util.NoSuchElementException {
        Object element = null;
        if (nextElement != null) {
            element = nextElement;
            nextElement = null;
        } else {
            element = getNextElement();
            if (element == null) {
                throw new java.util.NoSuchElementException();
            }
        }
        return element;
    }

    /**
     * Returns true if there are previous elements in the iteration.
     * 
     * @return
     */
    public boolean hasPrevious() {
        // If we are at the start of the list there are no previous elements.
        if (currentIndex == startIndex) {
            return false;
        }
        // Otherwise, see if previous Element is null. If so, try to load the
        // previous element to make sure it exists.
        if (previousElement == null) {
            previousElement = getPreviousElement();
            // If getting the previous element failed, return false.
            if (previousElement == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * * Returns the previous element of primary key collection.
     * 
     * @return
     */
    public Object previous() {
        Object element = null;
        if (previousElement != null) {
            element = previousElement;
            previousElement = null;
        } else {
            element = getPreviousElement();
            if (element == null) {
                throw new java.util.NoSuchElementException();
            }
        }
        return element;
    }

    /**
     * Returns the previous element, or null if there are no more elements to
     * return.
     * 
     * @return the previous element.
     */
    private Object getPreviousElement() {
        Object element = null;
        while (currentIndex >= startIndex && element == null) {
            currentIndex--;
            element = getElement();
        }
        return element;
    }

    /**
     * Not supported for security reasons.
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the next available element of primary key collection, or null if
     * there are no more elements to return.
     * 
     * @return the next available element.
     */
    public Object getNextElement() {
        Object element = null;
        while (currentIndex+1 < endIndex && element == null) {
            currentIndex++;
            element = getElement();
        }
        return element;      
    }
    
    private Object getElement(){
        Object element = null;
        if  ((currentIndex >=0 ) && (currentIndex < elements.length)){
            element = elements[currentIndex];
        }else
            System.err.println("PageIterator error: currentIndex=" + currentIndex + " exceed the 0 or Max length=" + elements.length);
        return element;
    }

    public int getSize() {
        return elements.length;
    }
    
    /**
     * @return Returns the keys.
     */
    public Object[] getKeys() {
        return elements;
    }
        
    /**
     * @param keys The keys to set.
     */
    public void setKeys(Object[] keys) {
        this.elements = keys;
    }
    
    
    /**
     * @return Returns the actualCount.
     */
    public int getCount() {
        return count;
    }

    

	public boolean isElementsTypeIsKey() {
		return elementsTypeIsKey;
	}


	public void setElementsTypeIsKey(boolean elementsTypeIsKey) {
		this.elementsTypeIsKey = elementsTypeIsKey;
	}
    
    
    
}
