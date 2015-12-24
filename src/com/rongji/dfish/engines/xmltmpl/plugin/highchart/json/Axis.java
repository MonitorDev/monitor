package com.rongji.dfish.engines.xmltmpl.plugin.highchart.json;

public class Axis {
	private Boolean allowDecimals;
	private String alternateGridColor;
	private Object[] categories;
	private Integer ceiling;
	private DateTimeLabelFormats dateTimeLabelFormats;
	private Boolean endOnTick;
	private Events events;
	private Integer floor;
	private String gridLineColor;
	private String gridLineDashStyle;//Solid
	private String gridLineInterpolation;
	private Integer gridLineWidth;
	private Integer gridZIndex;
	private String id;
	private Labels labels;
	private String lineColor;
	private Integer lineWidth;
	private Integer linkedTo;
	private Double max;
	private String maxColor;
	private Double maxPadding;

	private Double min;
	private String minColor;
	private Double minPadding;
	private Integer minRange;
	private Integer minTickInterval;
	private String minorGridLineColor;
	private String minorGridLineDashStyle;//Solid
	private Integer minorGridLineWidth;
	private String minorTickColor;
	private Integer minorTickInterval;
	private Integer minorTickLength;
	private String minorTickPosition;//outside
	private Integer minorTickWidth;
	private Integer offset;

	private Boolean opposite;
	private PlotBands plotBands;
	private PlotLines plotLines;
	private Boolean reversed;
	private Boolean reversedStacks;
	private Boolean showEmpty;
	private Boolean showFirstLabel;
	private Boolean showLastLabel;

	private StackLabels stackLabels;
	
	private Integer startOfWeek;
	private Boolean startOnTick;
	private Object[][] stops;
	
	private String tickColor;
	private Integer tickInterval;
	private Integer tickLength;
	private Integer tickPixelInterval;
	private String tickPosition;//outside
	private String tickPositioner;//FUNCTION
	private Integer[] tickPositions;//outside
	private Integer tickWidth;
	private String tickmarkPlacement;
	private Title title;
	private String type;//Can be one of "linear", "logarithmic", "datetime" or "category". 
	public StackLabels getStackLabels() {
		return stackLabels;
	}
	public void setStackLabels(StackLabels stackLabels) {
		this.stackLabels = stackLabels;
	}
	public Integer getStartOfWeek() {
		return startOfWeek;
	}
	public void setStartOfWeek(Integer startOfWeek) {
		this.startOfWeek = startOfWeek;
	}
	public Boolean getStartOnTick() {
		return startOnTick;
	}
	public void setStartOnTick(Boolean startOnTick) {
		this.startOnTick = startOnTick;
	}
	public Object[][] getStops() {
		return stops;
	}
	public void setStops(Object[][] stops) {
		this.stops = stops;
	}
	public String getTickColor() {
		return tickColor;
	}
	public void setTickColor(String tickColor) {
		this.tickColor = tickColor;
	}
	public Integer getTickInterval() {
		return tickInterval;
	}
	public void setTickInterval(Integer tickInterval) {
		this.tickInterval = tickInterval;
	}
	public Integer getTickLength() {
		return tickLength;
	}
	public void setTickLength(Integer tickLength) {
		this.tickLength = tickLength;
	}
	public Integer getTickPixelInterval() {
		return tickPixelInterval;
	}
	public void setTickPixelInterval(Integer tickPixelInterval) {
		this.tickPixelInterval = tickPixelInterval;
	}
	public String getTickPosition() {
		return tickPosition;
	}
	public void setTickPosition(String tickPosition) {
		this.tickPosition = tickPosition;
	}
	public String getTickPositioner() {
		return tickPositioner;
	}
	public void setTickPositioner(String tickPositioner) {
		this.tickPositioner = tickPositioner;
	}
	public Integer[] getTickPositions() {
		return tickPositions;
	}
	public void setTickPositions(Integer[] tickPositions) {
		this.tickPositions = tickPositions;
	}
	public Integer getTickWidth() {
		return tickWidth;
	}
	public void setTickWidth(Integer tickWidth) {
		this.tickWidth = tickWidth;
	}
	public String getTickmarkPlacement() {
		return tickmarkPlacement;
	}
	public void setTickmarkPlacement(String tickmarkPlacement) {
		this.tickmarkPlacement = tickmarkPlacement;
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getAllowDecimals() {
		return allowDecimals;
	}
	public void setAllowDecimals(Boolean allowDecimals) {
		this.allowDecimals = allowDecimals;
	}
	public String getAlternateGridColor() {
		return alternateGridColor;
	}
	public void setAlternateGridColor(String alternateGridColor) {
		this.alternateGridColor = alternateGridColor;
	}
	public Object[] getCategories() {
		return categories;
	}
	public void setCategories(Object[] categories) {
		this.categories = categories;
	}
	public Integer getCeiling() {
		return ceiling;
	}
	public void setCeiling(Integer ceiling) {
		this.ceiling = ceiling;
	}
	public DateTimeLabelFormats getDateTimeLabelFormats() {
		return dateTimeLabelFormats;
	}
	public void setDateTimeLabelFormats(DateTimeLabelFormats dateTimeLabelFormats) {
		this.dateTimeLabelFormats = dateTimeLabelFormats;
	}
	public Boolean getEndOnTick() {
		return endOnTick;
	}
	public void setEndOnTick(Boolean endOnTick) {
		this.endOnTick = endOnTick;
	}
	public Events getEvents() {
		return events;
	}
	public void setEvents(Events events) {
		this.events = events;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public String getGridLineColor() {
		return gridLineColor;
	}
	public void setGridLineColor(String gridLineColor) {
		this.gridLineColor = gridLineColor;
	}
	public String getGridLineDashStyle() {
		return gridLineDashStyle;
	}
	public void setGridLineDashStyle(String gridLineDashStyle) {
		this.gridLineDashStyle = gridLineDashStyle;
	}
	public String getGridLineInterpolation() {
		return gridLineInterpolation;
	}
	public void setGridLineInterpolation(String gridLineInterpolation) {
		this.gridLineInterpolation = gridLineInterpolation;
	}
	public Integer getGridLineWidth() {
		return gridLineWidth;
	}
	public void setGridLineWidth(Integer gridLineWidth) {
		this.gridLineWidth = gridLineWidth;
	}
	public Integer getGridZIndex() {
		return gridZIndex;
	}
	public void setGridZIndex(Integer gridZIndex) {
		this.gridZIndex = gridZIndex;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Labels getLabels() {
		return labels;
	}
	public void setLabels(Labels labels) {
		this.labels = labels;
	}
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	public Integer getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
	}
	public Integer getLinkedTo() {
		return linkedTo;
	}
	public void setLinkedTo(Integer linkedTo) {
		this.linkedTo = linkedTo;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public String getMaxColor() {
		return maxColor;
	}
	public void setMaxColor(String maxColor) {
		this.maxColor = maxColor;
	}
	public Double getMaxPadding() {
		return maxPadding;
	}
	public void setMaxPadding(Double maxPadding) {
		this.maxPadding = maxPadding;
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}
	public String getMinColor() {
		return minColor;
	}
	public void setMinColor(String minColor) {
		this.minColor = minColor;
	}
	public Double getMinPadding() {
		return minPadding;
	}
	public void setMinPadding(Double minPadding) {
		this.minPadding = minPadding;
	}
	public Integer getMinRange() {
		return minRange;
	}
	public void setMinRange(Integer minRange) {
		this.minRange = minRange;
	}
	public Integer getMinTickInterval() {
		return minTickInterval;
	}
	public void setMinTickInterval(Integer minTickInterval) {
		this.minTickInterval = minTickInterval;
	}
	public String getMinorGridLineColor() {
		return minorGridLineColor;
	}
	public void setMinorGridLineColor(String minorGridLineColor) {
		this.minorGridLineColor = minorGridLineColor;
	}
	public String getMinorGridLineDashStyle() {
		return minorGridLineDashStyle;
	}
	public void setMinorGridLineDashStyle(String minorGridLineDashStyle) {
		this.minorGridLineDashStyle = minorGridLineDashStyle;
	}
	public Integer getMinorGridLineWidth() {
		return minorGridLineWidth;
	}
	public void setMinorGridLineWidth(Integer minorGridLineWidth) {
		this.minorGridLineWidth = minorGridLineWidth;
	}
	public String getMinorTickColor() {
		return minorTickColor;
	}
	public void setMinorTickColor(String minorTickColor) {
		this.minorTickColor = minorTickColor;
	}
	public Integer getMinorTickInterval() {
		return minorTickInterval;
	}
	public void setMinorTickInterval(Integer minorTickInterval) {
		this.minorTickInterval = minorTickInterval;
	}
	public Integer getMinorTickLength() {
		return minorTickLength;
	}
	public void setMinorTickLength(Integer minorTickLength) {
		this.minorTickLength = minorTickLength;
	}
	public String getMinorTickPosition() {
		return minorTickPosition;
	}
	public void setMinorTickPosition(String minorTickPosition) {
		this.minorTickPosition = minorTickPosition;
	}
	public Integer getMinorTickWidth() {
		return minorTickWidth;
	}
	public void setMinorTickWidth(Integer minorTickWidth) {
		this.minorTickWidth = minorTickWidth;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Boolean getOpposite() {
		return opposite;
	}
	public void setOpposite(Boolean opposite) {
		this.opposite = opposite;
	}
	public PlotBands getPlotBands() {
		return plotBands;
	}
	public void setPlotBands(PlotBands plotBands) {
		this.plotBands = plotBands;
	}
	public PlotLines getPlotLines() {
		return plotLines;
	}
	public void setPlotLines(PlotLines plotLines) {
		this.plotLines = plotLines;
	}
	public Boolean getReversed() {
		return reversed;
	}
	public void setReversed(Boolean reversed) {
		this.reversed = reversed;
	}
	public Boolean getReversedStacks() {
		return reversedStacks;
	}
	public void setReversedStacks(Boolean reversedStacks) {
		this.reversedStacks = reversedStacks;
	}
	public Boolean getShowEmpty() {
		return showEmpty;
	}
	public void setShowEmpty(Boolean showEmpty) {
		this.showEmpty = showEmpty;
	}
	public Boolean getShowFirstLabel() {
		return showFirstLabel;
	}
	public void setShowFirstLabel(Boolean showFirstLabel) {
		this.showFirstLabel = showFirstLabel;
	}
	public Boolean getShowLastLabel() {
		return showLastLabel;
	}
	public void setShowLastLabel(Boolean showLastLabel) {
		this.showLastLabel = showLastLabel;
	}

}
