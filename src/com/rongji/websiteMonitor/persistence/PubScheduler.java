package com.rongji.websiteMonitor.persistence;

import java.util.Date;

/**
 * PubScheduler entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class PubScheduler implements java.io.Serializable {

	// Fields

	private String schlId;
	private String schlName;
	private String schlType;
	private String cronExpression;
	private Date startTime;
	private Date nextFireTime;
	private Date endTime;
	private Long repeatInterval;
	private Long repeatCount;
	private Long timesTriggered;
	private String clzName;
	private String configXml;
	private String usable;
	private String schlRemark;
	private String schlCreator;

	// Constructors

	/** default constructor */
	public PubScheduler() {
	}

	/** minimal constructor */
	public PubScheduler(String schlId) {
		this.schlId = schlId;
	}

	/** full constructor */
	public PubScheduler(String schlId, String schlName, String schlType,
			String cronExpression, Date startTime, Date nextFireTime,
			Date endTime, Long repeatInterval, Long repeatCount,
			Long timesTriggered, String clzName, String configXml, String usable) {
		this.schlId = schlId;
		this.schlName = schlName;
		this.schlType = schlType;
		this.cronExpression = cronExpression;
		this.startTime = startTime;
		this.nextFireTime = nextFireTime;
		this.endTime = endTime;
		this.repeatInterval = repeatInterval;
		this.repeatCount = repeatCount;
		this.timesTriggered = timesTriggered;
		this.clzName = clzName;
		this.configXml = configXml;
		this.usable = usable;
	}

	// Property accessors

	public String getSchlId() {
		return this.schlId;
	}

	public void setSchlId(String schlId) {
		this.schlId = schlId;
	}

	public String getSchlName() {
		return this.schlName;
	}

	public void setSchlName(String schlName) {
		this.schlName = schlName;
	}

	public String getSchlType() {
		return this.schlType;
	}

	public void setSchlType(String schlType) {
		this.schlType = schlType;
	}

	public String getCronExpression() {
		return this.cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getNextFireTime() {
		return this.nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getRepeatInterval() {
		return this.repeatInterval;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Long getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(Long repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Long getTimesTriggered() {
		return this.timesTriggered;
	}

	public void setTimesTriggered(Long timesTriggered) {
		this.timesTriggered = timesTriggered;
	}

	public String getClzName() {
		return this.clzName;
	}

	public void setClzName(String clzName) {
		this.clzName = clzName;
	}

	public String getConfigXml() {
		return this.configXml;
	}

	public void setConfigXml(String configXml) {
		this.configXml = configXml;
	}

	public String getUsable() {
		return this.usable;
	}

	public void setUsable(String usable) {
		this.usable = usable;
	}

	public String getSchlRemark() {
		return schlRemark;
	}

	public void setSchlRemark(String schlRemark) {
		this.schlRemark = schlRemark;
	}

	public String getSchlCreator() {
		return schlCreator;
	}

	public void setSchlCreator(String schlCreator) {
		this.schlCreator = schlCreator;
	}

}