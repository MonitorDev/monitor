package com.rongji.websiteMonitor.dao;

import com.rongji.websiteMonitor.persistence.PubScheduler;



public interface SchedulerDao {

	public PubScheduler getSchedulerById(String schlId);
	
	public PubScheduler getSchedulerByTaskId(String taskId);
	
}
