package com.jdon.async.task;

public abstract class Task implements Cloneable {

	protected Object[] args;

	public abstract void action();

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
