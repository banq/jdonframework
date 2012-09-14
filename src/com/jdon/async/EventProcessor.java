package com.jdon.async;

import com.jdon.async.task.MessageProcessor;
import com.jdon.async.task.QueueMessageProcessor;
import com.jdon.async.task.Task;
import com.jdon.async.task.TaskMonitor;

public class EventProcessor {

	private MessageProcessor messageProcessor;
	private TaskMonitor tm;

	public EventProcessor(String taskExecCount, String MaxconcurrentTaskCount) {
		tm = new TaskMonitor(taskExecCount, MaxconcurrentTaskCount);
		messageProcessor = new QueueMessageProcessor();
		tm.setMessageProcessor(messageProcessor);
		tm.start();
	}

	public void addTask(Task to) {
		messageProcessor.addMessage(to);
	}

	public void stop() {
		tm.shut();
	}

}
