<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String sAccount = request.getParameter("sAccount");
String sAct = request.getParameter("sAct");
String sSt = request.getParameter("sSt");
String sEnd = request.getParameter("sEnd");
String url = "perform.sp?act=pieDataSource&type=time";
if(sAccount!=null&&!"".equals(sAccount)){
	url +="&sAccount="+sAccount;
}
if(sAct!=null&&!"".equals(sAct)){
	url +="&sAct="+sAct;
}
if(sSt !=null&&!"".equals(sSt)){
	url +="&sSt="+sSt;
}
if(sEnd!=null&&!"".equals(sEnd)){
	url +="&sEnd="+sEnd;
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 



<html>
 <head>
    <base href="<%=basePath%>">
 <script src="js/pl/amcharts/jquery-1.8.0.min.js" type="text/javascript"></script>        
 <script src="js/pl/amcharts/amcharts.js" type="text/javascript" ></script>
 <script src="js/pl/amcharts/serial.js" type="text/javascript" ></script>
        <script type="text/javascript" defer="true">
       
            var chart;
			var chartData;
			var legend;
			$.getJSON('<%=url%>',	function(d) {
				chart = new AmCharts.AmSerialChart();
                chart.dataProvider = d;
   				chart.rotate = true;
   				
                chart.startDuration = 0.2;
                chart.addTitle('接口平均耗时分析统计', 16);
   				chart.categoryField = "name";
				chart.depth3D = 20;
                chart.angle = 30;
                chart.fontFamily = "Microsoft yahei";
            

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.labelRotation = 90;
                categoryAxis.dashLength = 5;
                categoryAxis.gridPosition = "start";

                // value
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.title = "平均时间（ms）";
                valueAxis.dashLength = 5;
                chart.addValueAxis(valueAxis);

                // GRAPH
                var graph = new AmCharts.AmGraph();
                graph.valueField = "number";
                graph.colorField = "color";
                graph.balloonText = "<span style='font-size:14px'>[[category]]: <b>[[value]]ms</b></span>";
                graph.type = "column";
                graph.lineAlpha = 0;
                graph.fillAlphas = 1;
                chart.addGraph(graph);

                // CURSOR
                var chartCursor = new AmCharts.ChartCursor();
                chartCursor.cursorAlpha = 0;
				chart.addChartCursor(chartCursor);
				chart.creditsPosition = "top-right";

                // WRITE
                chart.write("chartdiv");
			
				
			});
            
    
        </script>

  </head>
  
  <body>
      <div id="chartdiv" style="width:100%; height:500px;"></div>
  </body>
</html>
