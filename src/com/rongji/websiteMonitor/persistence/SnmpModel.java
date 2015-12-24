package com.rongji.websiteMonitor.persistence;

import java.util.Date;

public class SnmpModel implements java.io.Serializable {

	private static final long serialVersionUID = 827293186218811755L;

	private String id;
	
	private String taskId;
	
	private Date createTime;
	
	private String available;
	
	private String cpuUsedRate;
	
	private String memoryUsedSize;
	
	private String memoryTotalSize;
	
	private String jvmHeadTotalSize;
	
	private String jvmHeapUsedSize;
	
	private String diskIOReadSize;
	
	private String diskIOWrittenSize;
	
	private String systemProcess;
	
	private String ifInSize;
	
	private String ifOutSize;
	
	private String storageSize;
	
	private String storageUsed;
	
	public String getMemoryTotalSize() {
		return memoryTotalSize;
	}

	public void setMemoryTotalSize(String memoryTotalSize) {
		this.memoryTotalSize = memoryTotalSize;
	}

	public String getJvmHeadTotalSize() {
		return jvmHeadTotalSize;
	}

	public void setJvmHeadTotalSize(String jvmHeadTotalSize) {
		this.jvmHeadTotalSize = jvmHeadTotalSize;
	}

	private String jvmTheadSize;
	
	private String ioUsedSize;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getCpuUsedRate() {
		return cpuUsedRate;
	}

	public void setCpuUsedRate(String cpuUsedRate) {
		this.cpuUsedRate = cpuUsedRate;
	}

	public String getMemoryUsedSize() {
		return memoryUsedSize;
	}

	public void setMemoryUsedSize(String memoryUsedSize) {
		this.memoryUsedSize = memoryUsedSize;
	}

	public String getJvmHeapUsedSize() {
		return jvmHeapUsedSize;
	}

	public void setJvmHeapUsedSize(String jvmHeapUsedSize) {
		this.jvmHeapUsedSize = jvmHeapUsedSize;
	}

	public String getJvmTheadSize() {
		return jvmTheadSize;
	}

	public void setJvmTheadSize(String jvmTheadSize) {
		this.jvmTheadSize = jvmTheadSize;
	}

	public String getIoUsedSize() {
		return ioUsedSize;
	}

	public void setIoUsedSize(String ioUsedSize) {
		this.ioUsedSize = ioUsedSize;
	}

	public String getDiskIOReadSize() {
		return diskIOReadSize;
	}

	public void setDiskIOReadSize(String diskIOReadSize) {
		this.diskIOReadSize = diskIOReadSize;
	}

	public String getDiskIOWrittenSize() {
		return diskIOWrittenSize;
	}

	public void setDiskIOWrittenSize(String diskIOWrittenSize) {
		this.diskIOWrittenSize = diskIOWrittenSize;
	}

	public String getSystemProcess() {
		return systemProcess;
	}

	public void setSystemProcess(String systemProcess) {
		this.systemProcess = systemProcess;
	}

	public String getIfInSize() {
		return ifInSize;
	}

	public void setIfInSize(String ifInSize) {
		this.ifInSize = ifInSize;
	}

	public String getIfOutSize() {
		return ifOutSize;
	}

	public void setIfOutSize(String ifOutSize) {
		this.ifOutSize = ifOutSize;
	}

	public String getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(String storageSize) {
		this.storageSize = storageSize;
	}

	public String getStorageUsed() {
		return storageUsed;
	}

	public void setStorageUsed(String storageUsed) {
		this.storageUsed = storageUsed;
	}
	
	
}
