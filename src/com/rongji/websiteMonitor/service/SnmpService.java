package com.rongji.websiteMonitor.service;

import java.util.Date;
import java.util.List;

import com.rongji.websiteMonitor.persistence.SnmpModel;

public interface SnmpService{

	public void insertSnmp(SnmpModel snmpModel);
	
	public void updateSnmp(SnmpModel snmpModel);
	
	public SnmpModel getSnmpById(String id);
	
	public SnmpModel getLatestSnmp(String taskId);
	public List<Object[]> getHourStatistics(String taskId, Date startDate, Date endDate);
	
	public List<Object[]> getDayStatistics(String taskId, Date startDate, Date endDate);
	
	/**
	 * 根据条件获取每分钟的统计
	 * @param taskId  任务Id
	 * @param startDate 开始时间（精确到分钟以上）
	 * @param endDate  结束时间（精确到分钟以上）
	 * @return
	 */
	public List<Object[]> getMimuteStatistics(String taskId, Date startDate, Date endDate);
}
