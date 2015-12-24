package com.rongji.websiteMonitor.persistence;

import java.util.Date;

public class Task {
	private String taskId;
	private String taskName;
	private String taskSummary;
	private String taskPort;
	private String taskUsername;
	private String taskPassword;
	private String taskType;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private Date createTime;
	private String createId;
	private Date modifyTime;
	private String frequency;
	private Long retry;
	private String notifycationType;
	private String isUsable;
	private String isFault;
	private String schlId;
	private String monitorPoint;
	
	private String prjId;


	public String getPrjId() {
		return prjId;
	}


	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}


	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Task(String taskId, String taskName, String taskSummary,
			String taskPort, String taskUsername, String taskPassword,
			String taskType, String ext1, String ext2, String ext3,
			String ext4, Date createTime, String createId, Date modifyTime,
			String frequencyp, Long retry, String notifycationType,
			String isUsable, String isFault) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskSummary = taskSummary;
		this.taskPort = taskPort;
		this.taskUsername = taskUsername;
		this.taskPassword = taskPassword;
		this.taskType = taskType;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
		this.createTime = createTime;
		this.createId = createId;
		this.modifyTime = modifyTime;
		this.frequency = frequencyp;
		this.retry = retry;
		this.notifycationType = notifycationType;
		this.isUsable = isUsable;
		this.isFault = isFault;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getTaskSummary() {
		return taskSummary;
	}


	public void setTaskSummary(String taskSummary) {
		this.taskSummary = taskSummary;
	}


	public String getTaskPort() {
		return taskPort;
	}


	public void setTaskPort(String taskPort) {
		this.taskPort = taskPort;
	}


	public String getTaskUsername() {
		return taskUsername;
	}


	public void setTaskUsername(String taskUsername) {
		this.taskUsername = taskUsername;
	}


	public String getTaskPassword() {
		return taskPassword;
	}


	public void setTaskPassword(String taskPassword) {
		this.taskPassword = taskPassword;
	}


	public String getTaskType() {
		return taskType;
	}


	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}


	public String getExt1() {
		return ext1;
	}


	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}


	public String getExt2() {
		return ext2;
	}


	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}


	public String getExt3() {
		return ext3;
	}


	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}


	public String getExt4() {
		return ext4;
	}


	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getCreateId() {
		return createId;
	}


	public void setCreateId(String createId) {
		this.createId = createId;
	}


	public Date getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}


	public String getFrequency() {
		return frequency;
	}


	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}


	public Long getRetry() {
		return retry;
	}


	public void setRetry(Long retry) {
		this.retry = retry;
	}


	public String getNotifycationType() {
		return notifycationType;
	}


	public void setNotifycationType(String notifycationType) {
		this.notifycationType = notifycationType;
	}


	public String getIsUsable() {
		return isUsable;
	}


	public void setIsUsable(String isUsable) {
		this.isUsable = isUsable;
	}


	public String getIsFault() {
		return isFault;
	}


	public void setIsFault(String isFault) {
		this.isFault = isFault;
	}
	public String getSchlId() {
		return schlId;
	}


	public void setSchlId(String schlId) {
		this.schlId = schlId;
	}


	public String getMonitorPoint() {
		return monitorPoint;
	}


	public void setMonitorPoint(String monitorPoint) {
		this.monitorPoint = monitorPoint;
	}
}
