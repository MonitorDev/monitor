package com.rongji.websiteMonitor.persistence;

import java.util.Date;

public class Subproject {

	private String id;
	private String name;
	private String projectId;
	private String url;
	private String type;
	private String cronExpression;
	private Date createTime;
	private String clzName;
	private String isuable;
	private String creater;
	private String configXml;
	private int retry;
	private int frequency;
	private int warningFrequency;
	private String notification;
	private int hasRetry;
	private String remark;
	private String monitorPoint;
	private String isExternal;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getClzName() {
		return clzName;
	}
	public void setClzName(String clzName) {
		this.clzName = clzName;
	}
	public String getIsuable() {
		return isuable;
	}
	public void setIsuable(String isuable) {
		this.isuable = isuable;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getConfigXml() {
		return configXml;
	}
	public void setConfigXml(String configXml) {
		this.configXml = configXml;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public int getWarningFrequency() {
		return warningFrequency;
	}
	public void setWarningFrequency(int warningFrequency) {
		this.warningFrequency = warningFrequency;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public int getHasRetry() {
		return hasRetry;
	}
	public void setHasRetry(int hasRetry) {
		this.hasRetry = hasRetry;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subproject other = (Subproject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMonitorPoint() {
		return monitorPoint;
	}
	public void setMonitorPoint(String monitorPoint) {
		this.monitorPoint = monitorPoint;
	}
	public String getIsExternal() {
		return isExternal;
	}
	public void setIsExternal(String isExternal) {
		this.isExternal = isExternal;
	}
}
