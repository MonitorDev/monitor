package com.rongji.websiteMonitor.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.MonitorDetialService;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class MonitorDetialServiceImpl implements MonitorDetialService {

	@Override
	public List<Object[]> getMonitorDetialByCondition(Page page,
			QueryCondition condition) {
		List<Task> list = ServiceLocator.getTaskService().getTaskByCondition(
				page, condition);
		List<Object[]> dataList = new ArrayList<Object[]>();
		if (Utils.notEmpty(list)) {
			Object[] datas = null;
			for (Task task : list) {
				if(Utils.isEmpty(task.getTaskSummary())&&Utils.isEmpty(task.getMonitorPoint())) {
					continue;
				}
				datas = new Object[8];
				datas[0] = task.getTaskId();
				datas[1] = task.getTaskName();
				datas[2] = task.getTaskSummary();
				datas[3] = task.getTaskType();
				datas[4] = task.getFrequency();
				condition.setTaskId(task.getTaskId());
				// 获取可用率
				Object object = ServiceLocator.getFaultHistoryService()
						.getAvailableRate(condition);
				datas[5] = object != null ? object : "100";
				object = null; 
//					ServiceLocator.getTaskResultService().getAvgResponse(
//						condition);
				if(object==null){
					datas[5] = "0";
				}
				datas[6] = object != null ? object : "0";
				datas[7] = task.getTaskId();
				dataList.add(datas);
			}
			return dataList;
		}
		return null;
	}

	@Override
	public List<Object[]> getAvailableRateByCondition(QueryCondition condition) {	

		return ServiceLocator.getFaultHistoryService()
		.getAvailableRatesByCondition(condition);
	}

	@Override
	public List<Object[]> getMPAvailableRateByCondition(QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getMPAvailableRateByCondition(condition);
	}

	@Override
	public List<Object[]> getResponseTimeByCondition(QueryCondition condition ,boolean isGetMonitorPoint) {
		
		return ServiceLocator.getTaskResultService().getResponseTimeByCondition(condition,isGetMonitorPoint);
	}

	@Override
	public List<Object[]> getResponseDetialTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint) {
		return ServiceLocator.getTaskResultService().getResponseDetialTimeByCondition(condition,isGetMonitorPoint);
	}

	@Override
	public Object[] getMonitorDetialByTaskId(QueryCondition condition) {
		Task task = ServiceLocator.getTaskService().getTask(condition.getTaskId());
//		Object[] dataList = new Object[8];
		
			Object[] datas = null;
			if(task!=null){
				datas = new Object[8];
				datas[0] = task.getTaskId();
				datas[1] = task.getTaskName();
				datas[2] = task.getTaskSummary();
				datas[3] = task.getTaskType();
				datas[4] = task.getFrequency();
				condition.setTaskId(task.getTaskId());
				// 获取可用率
				Object object = ServiceLocator.getFaultHistoryService()
						.getAvailableRate(condition);
				datas[5] = object != null ? object : "100";
				object = ServiceLocator.getTaskResultService().getAvgResponse(
						condition);
				datas[6] = object != null ? object : "0";
				datas[7] = task.getTaskId();
			}
		
			return datas;
		
		
	}

	@Override
	public List<Object[]> getCurrentMonitorStatus(String taskId) {
		return ServiceLocator.getTaskResultService().getCurrentMonitorStatus(taskId);
	}

	@Override
	public Object[] getOnlyResponseTimeByCondition(QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getOnlyResponseTimeByCondition(condition);
	}

	@Override
	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getHourResponseTimeByCondition(condition);
	}

	@Override
	public List<Object[]> getHourResponseDetailByCondition(
			QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getHourResponseDetailByCondition(condition);
	}

	@Override
	public List<Object[]> getHourAvailableByCondition(QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getHourAvailableByCondition(condition);
	}

	@Override
	public List<Object[]> getMinuteAvailableByCondition(QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getMinuteAvailableByCondition(condition);
	}

	@Override
	public List<Object[]> getMinuteResponseTimeByCondition(
			QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getMinuteResponseTimeByCondition(condition);
	}

	@Override
	public List<Object[]> getMinuteResponseDetailByCondition(
			QueryCondition condition) {
		return ServiceLocator.getTaskResultService().getMinuteResponseDetailByCondition(condition);
	}


	@Override
	public List<Object[]> getLatestStatusByProjectId(String projectId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		JsonParser gson = new JsonParser();
		Map<String, Subproject> map = ServiceLocator.getSubprojectService().findSubprojectsByProjectId(projectId);
		List<Object[]> list = new ArrayList<Object[]>();
		Map<String, Object[]> tmpMap = new HashMap<String, Object[]>();
		if(map != null) {
			List<TaskResult> listTaskResult = ServiceLocator.getTaskResultService().getLatestByProjectId(projectId);
			if(Utils.notEmpty(listTaskResult)) {
				for(TaskResult tr : listTaskResult) {
					Subproject sp = map.get(tr.getTaskId());
					Map<String, String> config = Utils.string2Map(sp.getConfigXml());
					double responseTime = 0d;
					if(Utils.notEmpty(config.get("continueGroup"))) {
						Threshold th = ServiceLocator.getThresholdService().getThresholdById(config.get("continueGroup"));
						if(th != null) {
							responseTime = Double.parseDouble(Utils.string2Map(th.getContent()).get("responseTime"));
						}
					}
					JsonElement je = gson.parse(tr.getTrOtherResult());
					Object[] obj = new Object[7];
					obj[0] = tr.getTaskId();
					obj[1] = sp.getName();
					obj[2] = sdf.format(tr.getStaDate());
					obj[3] = sp.getType();
					String status = je.getAsJsonObject().get("result").getAsString();
					if("可用".equals(status)) {
						obj[4] = status;
					}else {
						obj[4] = "<span style='color:red'>" + status + "</span>";
					}
					obj[5] = tr.getTrRespTime();
					if(responseTime > 0) {
						if(tr.getTrRespTime() > responseTime) {
							obj[6] = "<span style='color:red'>" + tr.getTrRespTime()+ "(阀值:"+responseTime+")</span>";
							obj[4] = "<span style='color:red'>响应时间超时</span>";
						}else {
							obj[6] = tr.getTrRespTime();
						}
					}
					
					if(Utils.isEmpty(tmpMap.get(obj[0]))) {
						tmpMap.put(String.valueOf(obj[0]), obj);
					}else if("可用".equals(obj[4])) {
						tmpMap.put(String.valueOf(obj[0]), obj);
					}
				}
				for(Entry<String, Object[]> entry : tmpMap.entrySet()) {
					list.add(entry.getValue());
				}
			}
		}
		return list;
	}

}
