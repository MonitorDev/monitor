package com.rongji.websiteMonitor.service.impl;

import com.rongji.websiteMonitor.dao.SchedulerDao;
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.service.SchedulerService;

public class SchedulerServiceImpl implements SchedulerService {

	private SchedulerDao schedulerDao;
	
	public void setSchedulerDao(SchedulerDao schedulerDao) {
		this.schedulerDao = schedulerDao;
	}

	@Override
	public PubScheduler getSchedulerById(String schlId) {
		return schedulerDao.getSchedulerById(schlId);
	}

	@Override
	public PubScheduler getSchedulerByTaskId(String taskId) {
		return schedulerDao.getSchedulerByTaskId(taskId);
	}

}
