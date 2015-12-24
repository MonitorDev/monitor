package com.rongji.websiteMonitor.service.impl;

import java.util.Date;
import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.dao.FaultHistoryDao;
import com.rongji.websiteMonitor.dao.SubprojectDao;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.service.FaultHistoryService;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class FaultHistoryServiceImpl implements FaultHistoryService {
	
	private FaultHistoryDao faultHistoryDao;

	public FaultHistoryDao getFaultHistoryDao() {
		return faultHistoryDao;
	}

	public void setFaultHistoryDao(FaultHistoryDao faultHistoryDao) {
		this.faultHistoryDao = faultHistoryDao;
	}

	@Override
	public void saveFaultHistory(FaultHistory faultHistory) {
		faultHistoryDao.saveFaultHistory(faultHistory);
		
	}

	@Override
	public FaultHistory getFaultHistoryByTaskId(String taskId,
			boolean isIgnoreEnfTime) {
		
		return faultHistoryDao.getFaultHistoryByTaskId(taskId,isIgnoreEnfTime);
	}

	@Override
	public void updateFaultHistory(FaultHistory faultHistory) {
		faultHistoryDao.updateFaultHistory(faultHistory);
		
	}

	@Override
	public Object getAvailableRate(QueryCondition condition) {
		return faultHistoryDao.getAvailableRate(condition);
	}

	@Override
	public List<Object[]> getAvailableRatesByCondition(QueryCondition condition) {
		return faultHistoryDao.getAvailableRatesByCondition(condition);
	}

	@Override
	public List<FaultHistory> getFaultHistoryByCondition(
			QueryCondition condition,Page page) {
		return faultHistoryDao.getFaultHistoryByCondition(condition,page);
	}

	@Override
	public FaultHistory getFaultHistoryByParam(String taskId,String type, String mpId,
			boolean isIgnoreEnfTime) {
		return faultHistoryDao.getFaultHistoryByParam(taskId, type, mpId,isIgnoreEnfTime);
	}

	@Override
	public List<FaultHistory> getFaultHistoryInfoByProjectId(String projectId) {
		return faultHistoryDao.getFaultHistoryByProjectId(projectId);
	}


}
