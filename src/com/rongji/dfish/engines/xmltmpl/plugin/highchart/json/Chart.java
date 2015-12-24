package com.rongji.dfish.engines.xmltmpl.plugin.highchart.json;

public class Chart {
	public static final String TYPE_AREA="area";
	public static final String TYPE_AREARANGE="arearange";
	public static final String TYPE_AREASPLINE="areaspline";
	public static final String TYPE_AREASPLINERANGE="areasplinerange";
	public static final String TYPE_BAR="bar";
	public static final String TYPE_BOXPLOT="boxplot";
	public static final String TYPE_BUBBLE="bubble";
	public static final String TYPE_COLUMN="column";
	public static final String TYPE_COLUMNRANGE="columnrange";
	public static final String TYPE_ERRORBAR="errorbar";
	public static final String TYPE_FUNNEL="funnel";
	public static final String TYPE_GAUGE="gauge";
	public static final String TYPE_HEATMAP="heatmap";

	public static final String TYPE_LINE="line";
	public static final String TYPE_PIE="pie";
	public static final String TYPE_PYRAMID="pyramid";
	public static final String TYPE_SCATTER="scatter";
	public static final String TYPE_SERIES="series";
	public static final String TYPE_SOLIDGAUGE="solidgauge";
	public static final String TYPE_SPLINE="spline";
	public static final String TYPE_WATERFALL="waterfall";

	private Boolean alignTicks;
	private Boolean animation;
	private String backgroundColor;
	private String borderColor;
	private Integer borderRadius;
	private Integer borderWidth;
	private String className;
	private String defaultSeriesType;//line
	private Events events;
	private Integer height;
	private Boolean ignoreHiddenSeries;
	private Boolean inverted;
	private Integer[] margin;
	private Integer marginBottom;
	private Integer marginLeft;
	private Integer marginRight;
	private Integer marginTop;
	private Options3d options3d;
	private String pinchType;
	private String plotBackgroundColor;
	private String plotBackgroundImage;
	private String plotBorderColor;
	private Integer plotBorderWidth;
	private Boolean plotShadow;
	private Boolean polar;
	private Boolean reflow;
	private String renderTo;
	private ResetZoomButton resetZoomButton;
	private String selectionMarkerFill;
	private Boolean shadow;
	private Boolean showAxes;
	private Integer[] spacing;
	private Integer spacingBottom;
	private Integer spacingLeft;
	private Integer spacingRight;
	private Integer spacingTop;
	private CssObject style;
	private String type;//line
	private Integer width;
	private String zoomType;
	public Boolean getAlignTicks() {
		return alignTicks;
	}
	public void setAlignTicks(Boolean alignTicks) {
		this.alignTicks = alignTicks;
	}
	public Boolean getAnimation() {
		return animation;
	}
	public void setAnimation(Boolean animation) {
		this.animation = animation;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public Integer getBorderRadius() {
		return borderRadius;
	}
	public void setBorderRadius(Integer borderRadius) {
		this.borderRadius = borderRadius;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDefaultSeriesType() {
		return defaultSeriesType;
	}
	public void setDefaultSeriesType(String defaultSeriesType) {
		this.defaultSeriesType = defaultSeriesType;
	}
	public Events getEvents() {
		return events;
	}
	public void setEvents(Events events) {
		this.events = events;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Boolean getIgnoreHiddenSeries() {
		return ignoreHiddenSeries;
	}
	public void setIgnoreHiddenSeries(Boolean ignoreHiddenSeries) {
		this.ignoreHiddenSeries = ignoreHiddenSeries;
	}
	public Boolean getInverted() {
		return inverted;
	}
	public void setInverted(Boolean inverted) {
		this.inverted = inverted;
	}
	public Integer[] getMargin() {
		return margin;
	}
	public void setMargin(Integer[] margin) {
		this.margin = margin;
	}
	public Integer getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(Integer marginBottom) {
		this.marginBottom = marginBottom;
	}
	public Integer getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(Integer marginLeft) {
		this.marginLeft = marginLeft;
	}
	public Integer getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(Integer marginRight) {
		this.marginRight = marginRight;
	}
	public Integer getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(Integer marginTop) {
		this.marginTop = marginTop;
	}
	public Options3d getOptions3d() {
		return options3d;
	}
	public void setOptions3d(Options3d options3d) {
		this.options3d = options3d;
	}
	public String getPinchType() {
		return pinchType;
	}
	public void setPinchType(String pinchType) {
		this.pinchType = pinchType;
	}
	public String getPlotBackgroundColor() {
		return plotBackgroundColor;
	}
	public void setPlotBackgroundColor(String plotBackgroundColor) {
		this.plotBackgroundColor = plotBackgroundColor;
	}
	public String getPlotBackgroundImage() {
		return plotBackgroundImage;
	}
	public void setPlotBackgroundImage(String plotBackgroundImage) {
		this.plotBackgroundImage = plotBackgroundImage;
	}
	public String getPlotBorderColor() {
		return plotBorderColor;
	}
	public void setPlotBorderColor(String plotBorderColor) {
		this.plotBorderColor = plotBorderColor;
	}
	public Integer getPlotBorderWidth() {
		return plotBorderWidth;
	}
	public void setPlotBorderWidth(Integer plotBorderWidth) {
		this.plotBorderWidth = plotBorderWidth;
	}
	public Boolean getPlotShadow() {
		return plotShadow;
	}
	public void setPlotShadow(Boolean plotShadow) {
		this.plotShadow = plotShadow;
	}
	public Boolean getPolar() {
		return polar;
	}
	public void setPolar(Boolean polar) {
		this.polar = polar;
	}
	public Boolean getReflow() {
		return reflow;
	}
	public void setReflow(Boolean reflow) {
		this.reflow = reflow;
	}
	public String getRenderTo() {
		return renderTo;
	}
	public void setRenderTo(String renderTo) {
		this.renderTo = renderTo;
	}
	public ResetZoomButton getResetZoomButton() {
		return resetZoomButton;
	}
	public void setResetZoomButton(ResetZoomButton resetZoomButton) {
		this.resetZoomButton = resetZoomButton;
	}
	public String getSelectionMarkerFill() {
		return selectionMarkerFill;
	}
	public void setSelectionMarkerFill(String selectionMarkerFill) {
		this.selectionMarkerFill = selectionMarkerFill;
	}
	public Boolean getShadow() {
		return shadow;
	}
	public void setShadow(Boolean shadow) {
		this.shadow = shadow;
	}
	public Boolean getShowAxes() {
		return showAxes;
	}
	public void setShowAxes(Boolean showAxes) {
		this.showAxes = showAxes;
	}
	public Integer[] getSpacing() {
		return spacing;
	}
	public void setSpacing(Integer[] spacing) {
		this.spacing = spacing;
	}
	public Integer getSpacingBottom() {
		return spacingBottom;
	}
	public void setSpacingBottom(Integer spacingBottom) {
		this.spacingBottom = spacingBottom;
	}
	public Integer getSpacingLeft() {
		return spacingLeft;
	}
	public void setSpacingLeft(Integer spacingLeft) {
		this.spacingLeft = spacingLeft;
	}
	public Integer getSpacingRight() {
		return spacingRight;
	}
	public void setSpacingRight(Integer spacingRight) {
		this.spacingRight = spacingRight;
	}
	public Integer getSpacingTop() {
		return spacingTop;
	}
	public void setSpacingTop(Integer spacingTop) {
		this.spacingTop = spacingTop;
	}
	public CssObject getStyle() {
		return style;
	}
	public void setStyle(CssObject style) {
		this.style = style;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getZoomType() {
		return zoomType;
	}
	public void setZoomType(String zoomType) {
		this.zoomType = zoomType;
	}

	
	

}
