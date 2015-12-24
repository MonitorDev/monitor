package com.rongji.dfish.engines.xmltmpl.plugin.highchart;

import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.*;

public class HighChart {
	public HighChart(){
		credits=new Credits();
		credits.setEnabled(false);
	}
	public static final String ALIGN_CENTER="center";
	private Chart chart;
	private Title title;
	private String[] colors;
	
	private Credits credits;
	private Drilldown drilldown;
	private Exporting exporting;
	private Labels labels;
	private Legend legend;
	private Loading loading;
	private Navigation navigation;
	private Pane pane;
	private PlotOptions plotOptions;
	private Series[] series;
	private Title subtitle;
	private Tooltip tooltip;
	private Axis xAxis;
	private Axis yAxis;
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	public String[] getColors() {
		return colors;
	}
	public void setColors(String[] colors) {
		this.colors = colors;
	}
	public Credits getCredits() {
		return credits;
	}
	public void setCredits(Credits credits) {
		this.credits = credits;
	}
	public Drilldown getDrilldown() {
		return drilldown;
	}
	public void setDrilldown(Drilldown drilldown) {
		this.drilldown = drilldown;
	}
	public Exporting getExporting() {
		return exporting;
	}
	public void setExporting(Exporting exporting) {
		this.exporting = exporting;
	}
	public Labels getLabels() {
		return labels;
	}
	public void setLabels(Labels labels) {
		this.labels = labels;
	}
	public Legend getLegend() {
		return legend;
	}
	public void setLegend(Legend legend) {
		this.legend = legend;
	}
	public Loading getLoading() {
		return loading;
	}
	public void setLoading(Loading loading) {
		this.loading = loading;
	}
	public Navigation getNavigation() {
		return navigation;
	}
	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
	public Pane getPane() {
		return pane;
	}
	public void setPane(Pane pane) {
		this.pane = pane;
	}
	public PlotOptions getPlotOptions() {
		return plotOptions;
	}
	public void setPlotOptions(PlotOptions plotOptions) {
		this.plotOptions = plotOptions;
	}
	public Series[] getSeries() {
		return series;
	}
	public void setSeries(Series[] series) {
		this.series = series;
	}
	public Title getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(Title subtitle) {
		this.subtitle = subtitle;
	}
	public Tooltip getTooltip() {
		return tooltip;
	}
	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}
	public Axis getXAxis() {
		return xAxis;
	}
	public void setXAxis(Axis axis) {
		xAxis = axis;
	}
	public Axis getYAxis() {
		return yAxis;
	}
	public void setYAxis(Axis axis) {
		yAxis = axis;
	}
	public Chart getChart() {
		return chart;
	}
	public void setChart(Chart chart) {
		this.chart = chart;
	}
	
}
