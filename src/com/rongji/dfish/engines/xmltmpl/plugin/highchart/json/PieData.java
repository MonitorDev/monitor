package com.rongji.dfish.engines.xmltmpl.plugin.highchart.json;

public class PieData {
	public PieData(){
		
	}
	public PieData(String name,Double y){
		this.name=name;
		this.y=y;
	}
	private String name; 
	private Double y;
	private Boolean sliced;
	private Boolean selected;
	public String getName() {
		return name;
	}
	public PieData setName(String name) {
		this.name = name;
		return this;
	}
	public Double getY() {
		return y;
	}
	public PieData setY(Double y) {
		this.y = y;
		return this;
	}
	public Boolean getSliced() {
		return sliced;
	}
	public PieData setSliced(Boolean sliced) {
		this.sliced = sliced;
		return this;
	}
	public Boolean getSelected() {
		return selected;
	}
	public PieData setSelected(Boolean selected) {
		this.selected = selected;
		return this;
	}
}
