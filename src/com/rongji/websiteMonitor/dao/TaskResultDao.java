package com.rongji.websiteMonitor.dao;

import java.util.Date;
import java.util.List;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public interface TaskResultDao extends PubCommonDAO {

	public void saveResult(TaskResult result);

	public Object getAvgResponse(QueryCondition condition);

	public List<Object[]> getMPAvailableRateByCondition(QueryCondition condition);

	public List<Object[]> getResponseTimeByCondition(QueryCondition condition, boolean isGetMonitorPoint);

	public List<Object[]> getResponseDetialTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint);

	public List<Object[]> getStartAndEndTime();

	public List<Object[]> getSumResult(Date startTime, Date endTime,
			String statType);

	public TaskResult getTaskResultById(String trId);

	public void deleteResult(String startDate, String enDate, String statType);

	public List<Object[]> getHourAvailableRateByCondition(
			QueryCondition condition);

	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint);

	/**
	 * 根据每天归档的条件获取各个归档的所有记录
	 * @param dateTime  日期
	 * @param taskId  检测站点
	 * @param mapId   监控点
	 * @param trType  检测类型
	 * @param isuseable  是否可用 
	 * @return
	 */
	public List<TaskResult> getTaskResultByDay(long dateTime, String taskId, String mapId, String trType, String isuseable);
	
	
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
	 * 根据条件获取每个检测频率内各个时间点的最大响应时间，最小响应时间和平均响应时间
	 * @param condition
	 * @return
	 */
	public List<Object[]> getMinuteResponseTimeByCondition(
			QueryCondition condition);
	
	
	
	/**
	 * 根据条件获取每天内各个时间点的下载速度、链接速度、NDS解析速度、服务器计算速度
	 * @param condition
	 * @return
	 */
	public List<Object[]> getHourResponseDetailByCondition(
			QueryCondition condition);
	
	/**
	 * 根据条件获取检测频率内各个时间点的下载速度、链接速度、NDS解析速度、服务器计算速度
	 * @param condition
	 * @return
	 */
	public List<Object[]> getMinuteResponseDetailByCondition(
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
	 * 根据项目Id获取其最新的监控结果
	 * @param projectId 项目ID
	 * @return
	 */
	public List<TaskResult> getLatestByProjectId(String projectId);
	
}
