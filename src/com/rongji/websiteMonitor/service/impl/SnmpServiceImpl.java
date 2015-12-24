package com.rongji.websiteMonitor.service.impl;


import java.util.Date;
import java.util.List;

import com.rongji.websiteMonitor.dao.SnmpDao;
import com.rongji.websiteMonitor.persistence.SnmpModel;
import com.rongji.websiteMonitor.service.SnmpService;

public class SnmpServiceImpl implements SnmpService {
	
	private SnmpDao snmpDao;
	
	public void setSnmpDao(SnmpDao snmpDao) {
		this.snmpDao = snmpDao;
	}

	@Override
	public void insertSnmp(SnmpModel snmpModel) {
		snmpDao.insertSnmp(snmpModel);
	}

	@Override
	public void updateSnmp(SnmpModel snmpModel) {
		
	}

	@Override
	public SnmpModel getSnmpById(String id) {
		return null;
	}

	@Override
	public List<Object[]> getHourStatistics(String taskId, Date startDate,
			Date endDate) {
		return snmpDao.getHourStatistics(taskId, startDate, endDate);
	}

	@Override
	public List<Object[]> getDayStatistics(String taskId, Date startDate,
			Date endDate) {
		return snmpDao.getDayStatistics(taskId, startDate, endDate);
	}

	@Override
	public SnmpModel getLatestSnmp(String taskId) {
		return snmpDao.getLatestSnmp(taskId);
	}

	@Override
	public List<Object[]> getMimuteStatistics(String taskId, Date startDate,
			Date endDate) {
		return snmpDao.getMimuteStatistics(taskId, startDate, endDate);
	}

}
