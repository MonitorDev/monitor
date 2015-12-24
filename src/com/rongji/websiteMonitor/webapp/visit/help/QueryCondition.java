package com.rongji.websiteMonitor.webapp.visit.help;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.rongji.websiteMonitor.common.util.Utils;

public class QueryCondition {
	
	private Date startTime;
	private Date endTime;
	private String type;
	private String taskName;
	private String quickPeriod;
	private String mtAlias;
	private String taskId;
	private String summary;
	private String mpId;
	private String fhId;
	
	
	public QueryCondition(){}
	
	public QueryCondition(HttpServletRequest request){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String start = Utils.getParameter(request, "startTime");
		String end = Utils.getParameter(request, "endTime");
		try {
			this.startTime = Utils.notEmpty(start)?sdf.parse(start):null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.endTime = Utils.notEmpty(end)?sdf.parse(end):null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.type = Utils.getParameter(request, "type");
		this.taskName = Utils.getParameter(request, "taskName");
		this.quickPeriod = Utils.getParameter(request, "quickPeriod");
		this.mtAlias = Utils.getParameter(request, "type");
		this.summary = Utils.getParameter(request, "summary");
		this.mpId = Utils.getParameter(request, "mpId");
		this.fhId = Utils.getParameter(request, "fhId");
	}
	
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/***
	 * 根据查询条件构造url
	 * @return
	 */
	public String getUrlSearchConditionParm(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		StringBuilder sb = new StringBuilder();
		if(Utils.notEmpty(taskName)){
			sb.append("&taskName=").append(taskName);
		}
		if(Utils.notEmpty(type)){
			sb.append("&type=").append(type);
		}
		if(Utils.notEmpty(quickPeriod)){
			sb.append("&quickPeriod=").append(quickPeriod);
		}
		if(startTime!=null){
			sb.append("&startTime=").append(sdf.format(startTime));
		}
		if(endTime!=null){
			sb.append("&endTime=").append(sdf.format(endTime));
		}
		if(Utils.notEmpty(mtAlias)){
			sb.append("&mtAlias=").append(mtAlias);
		}
		if(Utils.notEmpty(summary)){
			sb.append("&summary=").append(summary);
		}
		if(Utils.notEmpty(mpId)){
			sb.append("&mpId=").append(mpId);
		}
		if(Utils.notEmpty(fhId)){
			sb.append("&fhId=").append(fhId);
		}
		return sb.toString();
	}

	public String getQuickPeriod() {
		return quickPeriod;
	}

	public void setQuickPeriod(String quickPeriod) {
		this.quickPeriod = quickPeriod;
	}

	public String getMtAlias() {
		return mtAlias;
	}

	public void setMtAlias(String mtAlias) {
		this.mtAlias = mtAlias;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMpId() {
		return mpId;
	}

	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	public String getFhId() {
		return fhId;
	}

	public void setFhId(String fhId) {
		this.fhId = fhId;
	}
	

}
