package com.rongji.websiteMonitor.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.Constants.MonitorTYPE;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.TaskResultDao;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.service.TaskResultService;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class TaskResultServiceImpl implements TaskResultService {
	
	private TaskResultDao resultDao;

	public TaskResultDao getResultDao() {
		return resultDao;
	}

	public void setResultDao(TaskResultDao resultDao) {
		this.resultDao = resultDao;
	}

	@Override
	public void saveResultByMap(Map<String, String> tempMap, String type,String taskId,Date time) {
		TaskResult result = null;
		if(tempMap==null){
			result = new TaskResult();
			result.setTaskId(taskId);
			result.setStaDate(time);
			resultDao.saveResult(result);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		switch (MonitorTYPE.toTYPE(type.toUpperCase())) {
		case HTTP:
			if(tempMap!=null){
				result = new TaskResult();
				result.setTaskId(taskId);
				result.setMpId(tempMap.get("mpId"));
				//域名解析时间
				result.setExt1(Utils.notEmpty(tempMap.get("time_namelookup"))?Double.parseDouble(tempMap.get("time_namelookup")):0.0);
				//连接时间
				result.setExt2(Utils.notEmpty(tempMap.get("linkTime"))?Double.parseDouble(tempMap.get("linkTime")):0.0);
				//服务器计算时间
				result.setExt3(Utils.notEmpty(tempMap.get("serverArTime"))?Double.parseDouble(tempMap.get("serverArTime")):0.0);
				//下载时间
				result.setExt4(Utils.notEmpty(tempMap.get("downloadTime"))?Double.parseDouble(tempMap.get("downloadTime")):0.0);
				//检测类型
				result.setTrType(type);
				//响应IP
				result.setIp(tempMap.get("ip"));
				//是否可用
				result.setIsuseable(Utils.notEmpty(tempMap.get("result"))&&"可用".equals(tempMap.get("result"))?Constants.IS_USE:Constants.IS_UNUSE);
				//地址
				result.setMonitoringPoint(tempMap.get("address"));
				//代码code
				result.setCode(tempMap.get("code"));
				result.setStaDate(time);
				result.setTrRespTime(Utils.notEmpty(tempMap.get("totalTime"))?Double.parseDouble(tempMap.get("totalTime")):0.0);
				result.setTrMaxRespTime(Utils.notEmpty(tempMap.get("totalTime"))?Double.parseDouble(tempMap.get("totalTime")):0.0);
				result.setTrMinRespTime(Utils.notEmpty(tempMap.get("totalTime"))?Double.parseDouble(tempMap.get("totalTime")):0.0);
				result.setTimes(1L);
				result.setArchiveLevel(Constants.ARCHIVE_LEVEL_NONR);
				result.setDataTime(Long.parseLong(sdf.format(new Date())));
				Gson gson = new Gson();
				Map<String, String> map = new HashMap<String, String>();
				map.put("size", tempMap.get("size"));
				map.put("status", tempMap.get("status"));
				map.put("result", tempMap.get("result"));
				map.put("code", tempMap.get("code"));
				
				result.setTrOtherResult(gson.toJson(map));
				resultDao.saveResult(result);
			}
			
			break;
		case PING:
			if(tempMap!=null){
				result = new TaskResult();
				result.setTaskId(taskId);
				result.setMpId(tempMap.get("mpId"));
				//是否可用
				result.setIsuseable(Utils.notEmpty(tempMap.get("result"))&&"可用".equals(tempMap.get("result"))?Constants.IS_USE:Constants.IS_UNUSE);
				//响应IP
				result.setIp(tempMap.get("ip"));
				//最小值
				result.setTrMinRespTime(Utils.notEmpty(tempMap.get("min"))?Double.parseDouble(tempMap.get("min")):0.0);
				//最大值
				result.setTrMaxRespTime(Utils.notEmpty(tempMap.get("max"))?Double.parseDouble(tempMap.get("max")):0.0);
				//地址
				result.setMonitoringPoint(tempMap.get("address"));
				//平均值
				result.setTrRespTime(Utils.notEmpty(tempMap.get("avg"))?Double.parseDouble(tempMap.get("avg")):0.0);
				//检测类型
				result.setTrType(type);
				result.setStaDate(time);
				result.setTimes(1L);
				result.setArchiveLevel(Constants.ARCHIVE_LEVEL_NONR);
				Gson gson = new Gson();
				Map<String, String> map = new HashMap<String, String>();
				map.put("result", tempMap.get("result"));
				result.setDataTime(Long.parseLong(sdf.format(new Date())));
				result.setTrOtherResult(gson.toJson(map));
				resultDao.saveResult(result);
			}
			break;

		default:
			break;
		}
		
		
	}

	@Override
	public Object getAvgResponse(QueryCondition condition) {
		return resultDao.getAvgResponse(condition);
	}

	@Override
	public List<Object[]> getMPAvailableRateByCondition(QueryCondition condition) {
		return resultDao.getMPAvailableRateByCondition(condition);
	}

	@Override
	public List<Object[]> getResponseTimeByCondition(QueryCondition condition,boolean isGetMonitorPoint) {
		return resultDao.getResponseTimeByCondition(condition,isGetMonitorPoint);
	}

	@Override
	public List<Object[]> getResponseDetialTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint) {
		return resultDao.getResponseDetialTimeByCondition(condition,isGetMonitorPoint);
	}

	@Override
	public List<Object[]> getStartAndEndTime() {
		
		return resultDao.getStartAndEndTime();
	}

	@Override
	public List<Object[]> getSumResult(Date startTime, Date endTime,
			String statType) {
		return resultDao.getSumResult(startTime,endTime,statType);
	}

	@Override
	public TaskResult getTaskResultById(String trId) {
	
		return resultDao.getTaskResultById(trId);
	}

	@Override
	public void archiveResult(TaskResult taskResult, boolean isExit) {
		if(isExit){
			resultDao.updateObject(taskResult);
		}else{
			resultDao.saveResult(taskResult);
		}
		
	}

	@Override
	public void deleteRedundance(String startDate, String enDate,
			String statType) {
		resultDao.deleteResult(startDate,enDate,statType);
		
	}

	@Override
	public List<Object[]> getHourAvailableRateByCondition(QueryCondition condition) {
		 return resultDao.getHourAvailableRateByCondition(condition);
		
	}

	@Override
	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint) {
		return resultDao.getHourResponseTimeByCondition(condition,isGetMonitorPoint);
	}

	@Override
	public List<TaskResult> getTaskResultByDay(long dateTime, String taskId,
			String mapId, String trType, String isuseable) {
		return resultDao.getTaskResultByDay(dateTime, taskId, mapId, trType, isuseable);
	}

	@Override
	public List<Object[]> getCurrentMonitorStatus(String taskId) {
		return resultDao.getCurrentMonitorStatus(taskId);
	}

	@Override
	public Object[] getOnlyResponseTimeByCondition(QueryCondition condition) {
		return resultDao.getOnlyResponseTimeByCondition(condition);
	}

	@Override
	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition) {
		return resultDao.getHourResponseTimeByCondition(condition);
	}

	@Override
	public List<Object[]> getHourResponseDetailByCondition(
			QueryCondition condition) {
		return resultDao.getHourResponseDetailByCondition(condition);
	}

	@Override
	public List<Object[]> getHourAvailableByCondition(QueryCondition condition) {
		return resultDao.getHourAvailableByCondition(condition);
	}

	@Override
	public List<Object[]> getMinuteAvailableByCondition(QueryCondition condition) {
		return resultDao.getMinuteAvailableByCondition(condition);
	}

	@Override
	public List<Object[]> getMinuteResponseTimeByCondition(
			QueryCondition condition) {
		return resultDao.getMinuteResponseTimeByCondition(condition);
	}

	@Override
	public List<Object[]> getMinuteResponseDetailByCondition(
			QueryCondition condition) {
		return resultDao.getMinuteResponseDetailByCondition(condition);
	}

	@Override
	public List<TaskResult> getLatestByProjectId(String projectId) {
		return resultDao.getLatestByProjectId(projectId);
	}
	
	
}
