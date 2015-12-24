package com.rongji.websiteMonitor.service;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public interface MonitorDetialService {

	/**
	 * 根据mtAlias获取某个类别下的监控项目的详细信息
	 * @param mtAlias
	 * @return
	 */
	public List<Object[]> getMonitorDetialByCondition(Page page,QueryCondition condition);

	/**
	 * 获取可用率,每天的可用率
	 * @param condition
	 * @return
	 */
	public List<Object[]> getAvailableRateByCondition(QueryCondition condition);

	/**
	 * 获取各个监控点的可用率
	 * @param condition
	 * @return
	 */
	public List<Object[]> getMPAvailableRateByCondition(QueryCondition condition);

	/**
	 * 获取响应时间，每天的响应时间，平均响应时间、最小、最大响应时间
	 * @param condition
	 * @param isGetMonitorPoint 
	 * @return
	 */
	public List<Object[]> getResponseTimeByCondition(QueryCondition condition, boolean isGetMonitorPoint);

	/**
	 * 获取响应的各个连接时间（域名解析时间，下载时间等）
	 * @param condition
	 * @param b
	 * @return
	 */
	public List<Object[]> getResponseDetialTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint);

	/**
	 * 根据taskId获取监控统计
	 * @param taskId
	 * @return
	 */
	public Object[] getMonitorDetialByTaskId(QueryCondition condition);
	
	/**
	 * 根据检测点获取各个检查点当前返回的检测状态和响应时间
	 * @param taskId  监测点ID
	 * @return 0:createTime, 1:Id,  2:taskId, 3:pointId, 4:pointName, 5:responseTime, 6:status, 7:available
	 */
	public List<Object[]> getCurrentMonitorStatus(String taskId);
	
	/**
	 * 根据条件获取条件范围内的最大响应时间，最小响应时间和平均响应时间
	 * @param condition
	 * @return 0:最小响应时间，1:最大响应时间，2:平均响应时间
	 */
	public Object[] getOnlyResponseTimeByCondition(QueryCondition condition);

	
	/**
	 * 根据条件获取每天内各个时间点的最大响应时间，最小响应时间和平均响应时间
	 * @param condition
	 * @return
	 */
	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition);
	
	/**
	 * 根据条件获取每天内各个时间点的下载速度、链接速度、NDS解析速度、服务器计算速度
	 * @param condition
	 * @return
	 */
	public List<Object[]> getHourResponseDetailByCondition(
			QueryCondition condition);
	

	/**
	 * 根据条件获取每天的可使用统计和不可使用统计
	 * @param condition
	 * @return
	 */
	public List<Object[]> getHourAvailableByCondition(
			QueryCondition condition);
	
	/**
	 * 根据条件获取检测频率点的可使用统计和不可使用统计
	 * @param condition
	 * @return
	 */
	public List<Object[]> getMinuteAvailableByCondition(
			QueryCondition condition);
	
	/**
	 * 根据条件获取每个检测频率内各个时间点的最大响应时间，最小响应时间和平均响应时间
	 * @param condition
	 * @return
	 */
	public List<Object[]> getMinuteResponseTimeByCondition(
			QueryCondition condition);
	
	/**
	 * 根据条件获取检测频率内各个时间点的下载速度、链接速度、NDS解析速度、服务器计算速度
	 * @param condition
	 * @return
	 */
	public List<Object[]> getMinuteResponseDetailByCondition(
			QueryCondition condition);
	
	/**
	 * 根据项目Id获取其最新的监控结果
	 * @param projectId 项目ID
	 * @return
	 */
	public List<Object[]> getLatestStatusByProjectId(String projectId);
}
