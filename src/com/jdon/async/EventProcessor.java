package com.jdon.async;

import com.jdon.async.message.MessageProcessor;
import com.jdon.async.message.QueueMessageProcessor;
import com.jdon.async.task.Task;
import com.jdon.async.task.TaskMonitor;

public class EventProcessor {

	private MessageProcessor messageProcessor;

	public EventProcessor(String taskExecCount, String MaxconcurrentTaskCount) {
		TaskMonitor tm = new TaskMonitor(taskExecCount, MaxconcurrentTaskCount);
		messageProcessor = new QueueMessageProcessor();
		tm.setMessageProcessor(messageProcessor);
		tm.start();
	}

	public void addTask(Task to) {
		messageProcessor.addMessage(to);
	}

}
