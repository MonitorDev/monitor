package com.rongji.websiteMonitor.service;

import java.util.Date;
import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public interface FaultHistoryService {

	/**
	 * 保存故障历史
	 * @param faultHistory 
	 * @param string
	 * @param time
	 */
	public void saveFaultHistory(FaultHistory faultHistory);

	/**
	 * 根据taskID获取故障记录
	 * @param taskId
	 * @param isIgnoreEnfTime （是否忽略还未结束的故障 true：是 false：不是）
	 * @return
	 */
	public FaultHistory getFaultHistoryByTaskId(String taskId, boolean isIgnoreEnfTime);

	/**
	 * 更新故障历史
	 * @param faultHistory
	 */
	public void updateFaultHistory(FaultHistory faultHistory);

	/**
	 * 获取可用率
	 * @param condition
	 * @return
	 */
	public Object getAvailableRate(QueryCondition condition);

	/**
	 * 获取可用率组
	 * @param condition
	 * @return
	 */
	public List<Object[]> getAvailableRatesByCondition(QueryCondition condition);

	/**
	 * 通过条件获取故障历史
	 * @param condition
	 * @param page 
	 * @return
	 */
	public List<FaultHistory> getFaultHistoryByCondition(
			QueryCondition condition, Page page);

	/**
	 * 根据taskID、mpid获取故障记录
	 * @param taskId
	 * @param mpId
	 * @param isIgnoreEnfTime
	 * @return
	 */
	public FaultHistory getFaultHistoryByParam(String taskId, String type,  String mpId, boolean isIgnoreEnfTime);

	/**
	 * 根据项目ID和相应时间获取故障信息
	 * @param projectId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<FaultHistory> getFaultHistoryInfoByProjectId(String projectId);
}
