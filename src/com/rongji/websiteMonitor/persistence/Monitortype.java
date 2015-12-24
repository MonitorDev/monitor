package com.rongji.websiteMonitor.persistence;

public class Monitortype {
	private String mtId;
	private String mtName;
	private String mtAlias;
	public String getMtAlias() {
		return mtAlias;
	}
	public void setMtAlias(String mtAlias) {
		this.mtAlias = mtAlias;
	}
	public String getMtId() {
		return mtId;
	}
	public void setMtId(String mtId) {
		this.mtId = mtId;
	}
	public String getMtName() {
		return mtName;
	}
	public void setMtName(String mtName) {
		this.mtName = mtName;
	}
	public Monitortype(String mtId, String mtName) {
		super();
		this.mtId = mtId;
		this.mtName = mtName;
	}
	public Monitortype() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
