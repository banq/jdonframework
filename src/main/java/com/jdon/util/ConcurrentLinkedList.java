package com.jdon.util;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLinkedList {
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(
			true);

	private final Lock readLock = readWriteLock.readLock();

	private final Lock writeLock = readWriteLock.writeLock();

	/** A list of the elements order by Least Recent Use */
	public final LinkedList keyLRUList = new LinkedList();

	public void add(Object o) {
		writeLock.lock();
		try {
			keyLRUList.add(o);
		} finally {
			writeLock.unlock();
		}
	}

	public void addFirst(Object key) {
		writeLock.lock();
		try {
			keyLRUList.addFirst(key);
		} finally {
			writeLock.unlock();
		}
	}

	public void moveFirst(Object key) {
		writeLock.lock();
		try {
			keyLRUList.remove(key);
			keyLRUList.addFirst(key);
		} catch (Exception e) {
		} finally {
			writeLock.unlock();
		}
	}

	public Object getLast() {
		readLock.lock();
		try {
			return keyLRUList.getLast();
		} finally {
			readLock.unlock();
		}
	}

	public int size() {
		readLock.lock();
		try {
			return keyLRUList.size();
		} finally {
			readLock.unlock();
		}
	}

	public void remove(Object key) {
		readLock.lock();
		try {
			keyLRUList.remove(key);
		} finally {
			readLock.unlock();
		}
	}

	public void clear() {
		writeLock.lock();
		try {
			keyLRUList.clear();
		} finally {
			writeLock.unlock();
		}
	}

}
