package com.rongji.dfish.engines.xmltmpl.plugin.highchart.json;

public class CssObject {
	private String fontFamily;
	private String fontSize;
	private String color;
	public String getFontFamily() {
		return fontFamily;
	}
	public CssObject setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		return this;
	}
	public String getFontSize() {
		return fontSize;
	}
	public CssObject setFontSize(String fontSize) {
		this.fontSize = fontSize;
		return this;
	}
	public String getColor() {
		return color;
	}
	public CssObject setColor(String color) {
		this.color = color;
		return this;
	}
}
