package com.jdon.async.task;

import java.util.ArrayList;
import java.util.List;

import com.jdon.async.EventProcessor;

public class ObservableAdapter {

	private List<Task> observers = new ArrayList();
	private EventProcessor eventProcessor;

	public ObservableAdapter(EventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	public ObservableAdapter(int taskExecCount, int MaxconcurrentTaskCount) {
		eventProcessor = new EventProcessor(Integer.toString(MaxconcurrentTaskCount), Integer.toString(taskExecCount));
	}

	public void addObserver(Task taskObserver) {
		observers.add(taskObserver);
	}

	public void notifyObservers(Object[] args) {
		for (Task task : observers) {
			// Task has its State, execute task don't effect its
			// state
			Task prototypTask = (Task) task.clone();
			prototypTask.setArgs(args);
			eventProcessor.addTask(prototypTask);
		}
	}

	public void clear() {
		observers.clear();
	}

	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	public void setEventProcessor(EventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

}
