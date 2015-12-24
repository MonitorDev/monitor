package com.rongji.websiteMonitor.persistence;

import java.util.Date;

/**
 * TaskResult entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TaskResult implements java.io.Serializable {

	// Fields

	private String trId;
	private String taskId;
	private String monitoringPoint;
	private String isuseable;
	private String ip;
	private Double trMinRespTime;
	private Double trMaxRespTime;
	private Double trRespTime;
	private Double ext1;
	private Double ext2;
	private Double ext3;
	private Double ext4;
	private String trType;
	private Date staDate;
	private String trOtherResult;
	private String mpId;
	private long times;
	private String ArchiveLevel;
	private String code;
	private Long dataTime;

	// Constructors

	public Long getDataTime() {
		return dataTime;
	}

	public void setDataTime(Long dataTime) {
		this.dataTime = dataTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public String getMpId() {
		return mpId;
	}

	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	/** default constructor */
	public TaskResult() {
	}

	/** minimal constructor */
	public TaskResult(String trId) {
		this.trId = trId;
	}

	/** full constructor */
	public TaskResult(String trId, String taskId, String monitoringPoint,
			String isuseable, String ip, Double trMinRespTime,
			Double trMaxRespTime, Double trRespTime, Double ext1, Double ext2,
			Double ext3, Double ext4, String trType, Date staDate,
			String trOtherResult) {
		this.trId = trId;
		this.taskId = taskId;
		this.monitoringPoint = monitoringPoint;
		this.isuseable = isuseable;
		this.ip = ip;
		this.trMinRespTime = trMinRespTime;
		this.trMaxRespTime = trMaxRespTime;
		this.trRespTime = trRespTime;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
		this.trType = trType;
		this.staDate = staDate;
		this.trOtherResult = trOtherResult;
	}

	// Property accessors

	public String getTrId() {
		return this.trId;
	}

	public void setTrId(String trId) {
		this.trId = trId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMonitoringPoint() {
		return this.monitoringPoint;
	}

	public void setMonitoringPoint(String monitoringPoint) {
		this.monitoringPoint = monitoringPoint;
	}

	public String getIsuseable() {
		return this.isuseable;
	}

	public void setIsuseable(String isuseable) {
		this.isuseable = isuseable;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Double getTrMinRespTime() {
		return this.trMinRespTime;
	}

	public void setTrMinRespTime(Double trMinRespTime) {
		this.trMinRespTime = trMinRespTime;
	}

	public Double getTrMaxRespTime() {
		return this.trMaxRespTime;
	}

	public void setTrMaxRespTime(Double trMaxRespTime) {
		this.trMaxRespTime = trMaxRespTime;
	}

	public Double getTrRespTime() {
		return this.trRespTime;
	}

	public void setTrRespTime(Double trRespTime) {
		this.trRespTime = trRespTime;
	}

	public Double getExt1() {
		return this.ext1;
	}

	public void setExt1(Double ext1) {
		this.ext1 = ext1;
	}

	public Double getExt2() {
		return this.ext2;
	}

	public void setExt2(Double ext2) {
		this.ext2 = ext2;
	}

	public Double getExt3() {
		return this.ext3;
	}

	public void setExt3(Double ext3) {
		this.ext3 = ext3;
	}

	public Double getExt4() {
		return this.ext4;
	}

	public void setExt4(Double ext4) {
		this.ext4 = ext4;
	}

	public String getTrType() {
		return this.trType;
	}

	public void setTrType(String trType) {
		this.trType = trType;
	}

	public Date getStaDate() {
		return this.staDate;
	}

	public void setStaDate(Date staDate) {
		this.staDate = staDate;
	}

	public String getTrOtherResult() {
		return this.trOtherResult;
	}

	public void setTrOtherResult(String trOtherResult) {
		this.trOtherResult = trOtherResult;
	}

	public String getArchiveLevel() {
		return ArchiveLevel;
	}

	public void setArchiveLevel(String archiveLevel) {
		ArchiveLevel = archiveLevel;
	}

}