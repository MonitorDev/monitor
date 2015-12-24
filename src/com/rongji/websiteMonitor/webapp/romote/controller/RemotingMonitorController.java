package com.rongji.websiteMonitor.webapp.romote.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.DateUtils;
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.MonitorDetialService;
import com.rongji.websiteMonitor.service.TaskService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class RemotingMonitorController extends BaseActionController {
	
	private MonitorDetialService detialService;
	private TaskService taskService;
	public TaskService getTaskService() {
		return taskService;
	}


	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}


	public MonitorDetialService getDetialService() {
		return detialService;
	}


	public void setDetialService(MonitorDetialService detialService) {
		this.detialService = detialService;
	}
	
	public ModelAndView getMonitorData(HttpServletRequest request, HttpServletResponse response){
		QueryCondition condition = new QueryCondition(request);
		Date now = new Date();
		condition.setStartTime(DateUtils.getMonthBegin(now));
		condition.setEndTime(now);
		String taskId = Utils.getParameter(request, "taskId");
//		taskId = "00000001";
		condition.setTaskId(taskId);
		Object[] data = detialService.getMonitorDetialByTaskId(condition);
		Map<String, String> map = new HashMap<String, String>();
		if(data!=null&&data.length>0){
			map.put("availableRate", (String)data[5]);
			map.put("avgResponse", (String)data[6]);
			
		}
		Gson gson = new Gson();
		FrameworkHelper.outPutTEXT(response, gson.toJson(map));
		return  null;
	}
	
	public ModelAndView buildMonitor(HttpServletRequest request, HttpServletResponse response){
		String url = Utils.getParameter(request, "url");
		String prjId = Utils.getParameter(request, "prjId");
		String phone = Utils.getParameter(request, "phone");
		String email = Utils.getParameter(request, "email");
		String name = Utils.getParameter(request, "name");
		String taskId = Utils.getParameter(request, "taskId");
		String mes = "";
		
		boolean isNew = true;
		Task point = taskService.getTask(taskId);
		if(point!=null){
			isNew = false;
		}else{
			point = new Task();
		}
		
		if(Utils.notEmpty(prjId)){
			point.setPrjId(prjId);
		}
		
		point.setFrequency("2");
		point.setIsUsable(Constants.OPTION_IS_YES);
		point.setRetry(1L);
		
		point.setTaskSummary(url);
		
		point.setTaskType("http");
		List<Map<String, String>> notifyTypeList = new ArrayList<Map<String,String>>();
		Map<String, String> item = new HashMap<String, String>();
		if(Utils.notEmpty(phone)){
			item.put("type", Constants.PHONE_TYPE);
			item.put("content", phone);
			notifyTypeList.add(item);
			
		}
		if(Utils.notEmpty(email)){
			item.put("type", Constants.EMAIL_TYPE);
			item.put("content", email);
			notifyTypeList.add(item);
		}
		if(Utils.notEmpty(name)){
			point.setTaskName(name);
		}
		
//		point.setMonitorPoint(Utils.notEmpty(monitorPoint)?monitorPoint.substring(0, monitorPoint.length()-1):"");
	
		Gson gson = new Gson();
		point.setNotifycationType(gson.toJson(notifyTypeList));
		Map<String , String> map = new HashMap<String, String>();
		try {
			if (isNew) {
				String schlId=FrameworkHelper.getNewId("PubScheduler", "schlId", "00001001");
				point.setSchlId(schlId);
				point = taskService.saveTask(point);
				StringBuilder sb = new StringBuilder();
				sb.append("type:").append(point.getTaskType()).append(";");
//				sb.append("monitorPoint:").append(point.getMonitorPoint().replace(",", "%3B")).append(";");
				sb.append("url:").append(Utils.notEmpty(point.getTaskSummary())?point.getTaskSummary().replace(":", "%3A"):"").append(";");
				sb.append("schlId:").append(schlId).append(";");
				sb.append("retry:").append(1L).append(";");
				sb.append("hasRetry:0;");
				PubScheduler schl = new PubScheduler();
				schl.setClzName("com.rongji.websiteMonitor.common.trigger.MonitorTrigger");
				schl.setConfigXml(sb.toString());
				schl.setCronExpression("0 */"+point.getFrequency()+" * * * ?");
				schl.setSchlId(schlId);
				schl.setSchlName(point.getTaskName());
				//触发方式
				schl.setSchlType("1");
//				schl.setSchlCreator(userId);
				schl.setUsable(Constants.OPTION_IS_YES.equals(point.getIsUsable())?Constants.OPTION_IS_YES:Constants.OPTION_IS_NO);
				PubCommonDAO dao = FrameworkHelper.getDAO();
				dao.saveObject(schl);
				mes = "初始化成功";
				map.put("ret", "0");
			} else {
				point.setModifyTime(new Date());
				point = taskService.updateTask(point);
				PubCommonDAO dao = FrameworkHelper.getDAO();
				List<PubScheduler> list = null;
				if(Utils.notEmpty(point.getSchlId())){
					list = dao.getQueryList("FROM PubScheduler t WHERE t.schlId = ?",point.getSchlId());
				}
				PubScheduler scheduler = null;
				if(Utils.notEmpty(list)){
					scheduler = list.get(0);
					StringBuilder sb = new StringBuilder();
					sb.append("type:"+point.getTaskType()+";");
//					sb.append("monitorPoint:"+point.getMonitorPoint().replace(",", "%3B")+";");
					sb.append("url:").append(Utils.notEmpty(point.getTaskSummary())?point.getTaskSummary().replace(":", "%3A"):"").append(";");
					sb.append("schlId:").append(scheduler.getSchlId()).append(";");
					sb.append("retry:").append(1L).append(";");
					sb.append("hasRetry:0;");
					scheduler.setConfigXml(sb.toString());
					scheduler.setCronExpression("0 */"+point.getFrequency()+" * * * ?");
					scheduler.setSchlName(point.getTaskName());
					//触发方式
					scheduler.setSchlType("1");
//					scheduler.setSchlCreator(userId);
					scheduler.setUsable(Constants.OPTION_IS_YES.equals(point.getIsUsable())?Constants.OPTION_IS_YES:Constants.OPTION_IS_NO);
					dao.updateObject(scheduler);
				}else{
					scheduler = new PubScheduler();
					String schlId=FrameworkHelper.getNewId("PubScheduler", "schlId", "00001001");
					point.setSchlId(schlId);
					dao.updateObject(point);
					scheduler.setSchlId(schlId);
					scheduler.setClzName("com.rongji.websiteMonitor.common.trigger.MonitorTrigger");
					scheduler.setConfigXml("type:"+point.getTaskType()+";");
					scheduler.setCronExpression("0 */"+point.getFrequency()+" * * * ?");
					scheduler.setSchlName(point.getTaskName());
					//触发方式
					scheduler.setSchlType("1");
//					scheduler.setSchlCreator(userId);
					scheduler.setUsable(Constants.OPTION_IS_YES.equals(point.getIsUsable())?Constants.OPTION_IS_YES:Constants.OPTION_IS_NO);
					dao.saveObject(scheduler);
				}
				mes = "修改成功";
				map.put("ret", "0");
			}
			map.put("monitorTaskId", point.getTaskId());
		} catch (Exception e) {
			e.printStackTrace();
			mes = "保存失败";
			map.put("ret", "1");
		}
		
		
		

		
		map.put("mes", mes);
		
		
		FrameworkHelper.outPutTEXT(response, gson.toJson(map));
		return null;
	}

}
