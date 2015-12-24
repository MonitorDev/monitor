package com.rongji.websiteMonitor.common;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用单线程顺序执行某些Runnable对象
 * 
 * @author I-TASK TEAM
 */
public class SingleThreadRunner {
	private ConcurrentLinkedQueue<Runnable> plans;

	private boolean working;
	public SingleThreadRunner(){
		plans = new ConcurrentLinkedQueue<Runnable>();
	    working = false;
	}
	public boolean isWorking() {
		return working;
	}

	/**
	 * 添加一个执行对象
	 * 
	 * @param item
	 */
	public void add(Runnable item) {
		if (working) {
			plans.add(item);
		} else {
			working = true;
			plans.add(item);
			Thread t = new Thread() {
				@Override
				public void run() {
					while (!plans.isEmpty()) {
						try {
							plans.poll().run();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					working = false;
				}
			};
			int priority = Thread.currentThread().getPriority() - 1;
			if (priority < Thread.MIN_PRIORITY) {
				priority = Thread.MIN_PRIORITY;
			}
			t.setPriority(priority); // 用低一些的线程优先级完成该任务.
			t.start();
		}
	}
}
