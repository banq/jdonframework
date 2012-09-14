package com.jdon.async;

import java.util.Timer;
import java.util.TimerTask;

import com.jdon.async.task.Task;

public class ScheduledTask extends TimerTask {

	private Task task;
	private Timer taskTimer;
	private EventProcessor ep;

	public ScheduledTask(EventProcessor ep) {
		this.taskTimer = new Timer(true);
		this.ep = ep;
	}

	public ScheduledTask clone() {
		ScheduledTask st = new ScheduledTask(ep);
		return st;
	}

	/**
	 * Schedules a task to periodically run. This is useful for tasks such as
	 * updating search indexes, deleting old data at periodic intervals, etc.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param delay
	 *            delay in milliseconds before task is to be executed.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @return a TimerTask object which can be used to track executions of the
	 *         task and to cancel subsequent executions.
	 */
	public void scheduleTask(Task task, long delay, long period) {
		ScheduledTask st = clone();
		st.setTask(task);
		taskTimer.scheduleAtFixedRate(st, delay, period);
	}

	public void run() {
		// Put the task into the queue to be run as soon as possible by a
		// worker.
		ep.addTask(task);
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Timer getTaskTimer() {
		return taskTimer;
	}

	public void setTaskTimer(Timer taskTimer) {
		this.taskTimer = taskTimer;
	}

	public void stop() {
		this.ep.stop();
		this.taskTimer.purge();
	}

}
