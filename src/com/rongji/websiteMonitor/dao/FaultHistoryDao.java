package com.rongji.websiteMonitor.dao;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public interface FaultHistoryDao extends PubCommonDAO {

	public void saveFaultHistory(FaultHistory faultHistory);

	public FaultHistory getFaultHistoryByTaskId(String taskId,
			boolean isIgnoreEnfTime);

	public void updateFaultHistory(FaultHistory faultHistory);

	public Object getAvailableRate(QueryCondition condition);

	public List<Object[]> getAvailableRatesByCondition(QueryCondition condition);

	public List<FaultHistory> getFaultHistoryByCondition(
			QueryCondition condition, Page page);

	public FaultHistory getFaultHistoryByParam(String taskId, String type, String mpId,
			boolean isIgnoreEnfTime);
	
	public List<FaultHistory> getFaultHistoryByProjectId(String projectId);


}
