package com.rongji.websiteMonitor.service;

import com.rongji.websiteMonitor.persistence.PubScheduler;

public interface SchedulerService {

	public PubScheduler getSchedulerById(String schlId);
	
	public PubScheduler getSchedulerByTaskId(String taskId);
}
