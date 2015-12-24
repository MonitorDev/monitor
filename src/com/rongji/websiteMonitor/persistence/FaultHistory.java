package com.rongji.websiteMonitor.persistence;

import java.util.Date;

/**
 * FaultHistory entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FaultHistory implements java.io.Serializable {

	// Fields

	private String fhId;
	private String taskId;
	private Date fhBeginTime;
	private Date fhEndTime;
	private String fhReason;
	private String fhType;
	private String mpId;
	private String isIgnore;
	private int retry;
	private Date alarmTime;

	// Constructors

	public String getIsIgnore() {
		return isIgnore;
	}

	public void setIsIgnore(String isIgnore) {
		this.isIgnore = isIgnore;
	}

	public String getMpId() {
		return mpId;
	}

	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	/** default constructor */
	public FaultHistory() {
	}

	/** minimal constructor */
	public FaultHistory(String fhId) {
		this.fhId = fhId;
	}

	/** full constructor */
	public FaultHistory(String fhId, String taskId, Date fhBeginTime,
			Date fhEndTime, String fhReason, String fhType) {
		this.fhId = fhId;
		this.taskId = taskId;
		this.fhBeginTime = fhBeginTime;
		this.fhEndTime = fhEndTime;
		this.fhReason = fhReason;
		this.fhType = fhType;
	}

	// Property accessors

	public String getFhId() {
		return this.fhId;
	}

	public void setFhId(String fhId) {
		this.fhId = fhId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getFhBeginTime() {
		return this.fhBeginTime;
	}

	public void setFhBeginTime(Date fhBeginTime) {
		this.fhBeginTime = fhBeginTime;
	}

	public Date getFhEndTime() {
		return this.fhEndTime;
	}

	public void setFhEndTime(Date fhEndTime) {
		this.fhEndTime = fhEndTime;
	}

	public String getFhReason() {
		return this.fhReason;
	}

	public void setFhReason(String fhReason) {
		this.fhReason = fhReason;
	}

	public String getFhType() {
		return this.fhType;
	}

	public void setFhType(String fhType) {
		this.fhType = fhType;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

}