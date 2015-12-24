package com.rongji.websiteMonitor.persistence;

import java.util.Date;

public class MonitoringPoint {
	private String mpId;
	private String mpName;
	private String mpPort;
	private String mpIp;
	private String mpRegion;
	private String mpNetType;
	private String mpmonitorType;
	private String creatorId;
	private Date createTime;
	private String isExternal;
	
	
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMpmonitorType() {
		return mpmonitorType;
	}

	public void setMpmonitorType(String mpmonitorType) {
		this.mpmonitorType = mpmonitorType;
	}

	public MonitoringPoint() {
		super();
	}
	
	public MonitoringPoint(String mpId, String mpName, String mpPort,
			String mpIp, String mpRegion, String mpNetType,String mpmonitorType,String creatorId,Date createTime) {
		super();
		this.mpId = mpId;
		this.mpName = mpName;
		this.mpPort = mpPort;
		this.mpIp = mpIp;
		this.mpRegion = mpRegion;
		this.mpNetType = mpNetType;
		this.mpmonitorType = mpmonitorType;
		this.creatorId = creatorId;
		this.createTime = createTime;
	}
	public String getMpId() {
		return mpId;
	}
	public void setMpId(String mpId) {
		this.mpId = mpId;
	}
	public String getMpName() {
		return mpName;
	}
	public void setMpName(String mpName) {
		this.mpName = mpName;
	}
	public String getMpPort() {
		return mpPort;
	}
	public void setMpPort(String mpPort) {
		this.mpPort = mpPort;
	}
	public String getMpIp() {
		return mpIp;
	}
	public void setMpIp(String mpIp) {
		this.mpIp = mpIp;
	}
	public String getMpRegion() {
		return mpRegion;
	}
	public void setMpRegion(String mpRegion) {
		this.mpRegion = mpRegion;
	}
	public String getMpNetType() {
		return mpNetType;
	}
	public void setMpNetType(String mpNetType) {
		this.mpNetType = mpNetType;
	}

	public String getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(String isExternal) {
		this.isExternal = isExternal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mpId == null) ? 0 : mpId.hashCode());
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
		MonitoringPoint other = (MonitoringPoint) obj;
		if (mpId == null) {
			if (other.mpId != null)
				return false;
		} else if (!mpId.equals(other.mpId))
			return false;
		return true;
	}
	
}
