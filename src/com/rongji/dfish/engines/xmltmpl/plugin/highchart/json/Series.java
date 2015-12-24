package com.rongji.dfish.engines.xmltmpl.plugin.highchart.json;

public class Series {
	private String type;
	private String name;
	private Object[] data;

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
