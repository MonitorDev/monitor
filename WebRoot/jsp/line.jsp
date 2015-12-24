<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 



<html>
 <head>
    <base href="<%=basePath%>">
 
  <script src="js/pl/amcharts/jquery-1.8.0.min.js" type="text/javascript"></script>        
	<script src="js/pl/amcharts/amcharts.js" type="text/javascript" ></script>
   <script src="js/pl/amcharts/exporting/amexport.js" type="text/javascript"></script>
   <script src="js/pl/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
   <script src="js/pl/amcharts/exporting/canvg.js" type="text/javascript"></script>
   <script src="js/pl/amcharts/exporting/jspdf.js" type="text/javascript"></script>
   <script src="js/pl/amcharts/exporting/filesaver.js" type="text/javascript"></script>
   <script src="js/pl/amcharts/exporting/jspdf.plugin.addimage.js" type="text/javascript"></script>
  <script src="js/pl/amcharts/serial.js" type="text/javascript"></script>
        <script type="text/javascript" defer="true">
            var chart;
            var chartData = [
                {
                    "date": "2012-03-01",
                    "price": 20
                },
                {
                    "date": "2012-03-02",
                    "price": 75
                },
                {
                    "date": "2012-03-03",
                    "price": 15
                },
                {
                    "date": "2012-03-04",
                    "price": 75
                },
                {
                    "date": "2012-03-05",
                    "price": 158
                },
                {
                    "date": "2012-03-06",
                    "price": 57
                },
                {
                    "date": "2012-03-07",
                    "price": 107
                },
                {
                    "date": "2012-03-08",
                    "price": 89
                },
                {
                    "date": "2012-03-09",
                    "price": 75
                },
                {
                    "date": "2012-03-10",
                    "price": 132
                },
                {
                    "date": "2012-03-11",
                    "price": 158
                },
                {
                    "date": "2012-03-12",
                    "price": 56
                },
                {
                    "date": "2012-03-13",
                    "price": 169
                },
                {
                    "date": "2012-03-14",
                    "price": 24
                },
                {
                    "date": "2012-03-15",
                    "price": 147
                }
            ];

            var average = 90.4;

            AmCharts.ready(function () {

                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.pathToImages = "../amcharts/images/";
                chart.dataProvider = chartData;
                chart.categoryField = "date";
                chart.dataDateFormat = "YYYY-MM-DD";

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
                categoryAxis.minPeriod = "DD"; // our data is daily, so we set minPeriod to DD
                categoryAxis.dashLength = 1;
                categoryAxis.gridAlpha = 0.15;
                categoryAxis.axisColor = "#DADADA";

                // value
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.axisColor = "#DADADA";
                valueAxis.dashLength = 1;
                valueAxis.logarithmic = true; // this line makes axis logarithmic
                chart.addValueAxis(valueAxis);

                // GUIDE for average
                var guide = new AmCharts.Guide();
                guide.value = average;
                guide.lineColor = "#CC0000";
                guide.dashLength = 4;
                guide.label = "average";
                guide.inside = true;
                guide.lineAlpha = 1;
                valueAxis.addGuide(guide);


                // GRAPH
                var graph = new AmCharts.AmGraph();
                graph.type = "smoothedLine";
                graph.bullet = "round";
                graph.bulletColor = "#FFFFFF";
                graph.useLineColorForBulletBorder = true;
                graph.bulletBorderAlpha = 1;
                graph.bulletBorderThickness = 2;
                graph.bulletSize = 7;
                graph.title = "Price";
                graph.valueField = "price";
                graph.lineThickness = 2;
                graph.lineColor = "#00BBCC";
                chart.addGraph(graph);

                // CURSOR
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.cursorPosition = "mouse";
                chart.addChartCursor(chartCursor);

                // SCROLLBAR
                var chartScrollbar = new AmCharts.ChartScrollbar();
                chartScrollbar.graph = graph;
                chartScrollbar.scrollbarHeight = 30;
                chart.addChartScrollbar(chartScrollbar);

                chart.creditsPosition = "bottom-right";

                // WRITE
                chart.write("chartdiv");
            });
    
        </script>

  </head>
  
  <body>
      <div id="chartdiv" style="width:100%; height:500px;"></div>
  </body>
</html>
