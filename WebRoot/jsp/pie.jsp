<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String sAccount = request.getParameter("sAccount");
String sAct = request.getParameter("sAct");
String sSt = request.getParameter("sSt");
String sEnd = request.getParameter("sEnd");
String type = request.getParameter("type");
String title = "";
String url = "perform.sp?act=pieDataSource&type="+type;
if(sAccount!=null&&!"".equals(sAccount)){
	url+="&sAccount="+sAccount;
}
if(sAct!=null&&!"".equals(sAct)){
	url+="&sAct="+sAct;
} 
if(sSt!=null && !"".equals(sSt)){
	url+="&sSt="+sSt;
}
if(sEnd!=null&&!"".equals(sEnd)){
	url+="&sEnd="+sEnd;
}
if(type!=null && !"".equals(type)){
	if(sAccount!=null && !"".equals(sAccount)){
		if("visit".equals(type)){
			title = sAccount+"账号接口访问量分析统计饼图";
		}else if("fail".equals(type)){
			title = sAccount+"账号接口失败率分析统计饼图";
		}else if("time".equals(type)){
			title = sAccount+"账号接口耗时分析统计饼图"; 
		}
	}else{
		if("visit".equals(type)){
			title = "接口访问量分析统计饼图";
		}else if("fail".equals(type)){
			title = "接口失败率分析统计饼图";
		}else if("time".equals(type)){
			title = "接口平均耗时分析统计饼图 "; 
		}
	}
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 



<html>
 <head>
    <base href="<%=basePath%>">
  <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
 <script src="js/pl/amcharts/jquery-1.8.0.min.js" type="text/javascript"></script>        
 <script src="js/pl/amcharts/amcharts.js" type="text/javascript" ></script>
 <script src="js/pl/amcharts/pie.js" type="text/javascript" ></script>
        <script type="text/javascript" defer="true">
         var chart = new AmCharts.AmPieChart();;
		 var chartData;
		 var legend;
         var system ={ win : false, mac : false, xll : false};  
		 var p = navigator.platform;  
		 system.win = p.indexOf("Win") == 0;  
		 system.mac = p.indexOf("Mac") == 0;  
		 system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);  
		 
		 if(system.win == false && system.mac==false &&system.xll==false){
			chart.labelsEnabled = false;
    	 } else if(findDimensions()<700){
    		 chart.labelsEnabled = false;
    	 } 
		 $.getJSON("<%=url%>",	function(d) {
				chartData = d;
				chart.addTitle("<%=title%>", 16);
                chart.dataProvider = chartData;
              	chart.titleField = "name";
                chart.valueField = "number";
                chart.sequencedAnimation = true;
               	chart.startEffect = ">";
                chart.innerRadius = "30%";
                chart.startDuration = 0.3;
				chart.outlineAlpha = 0.5;
                chart.outlineThickness = 1.2;
            	chart.marginTop = 20;
                chart.radius = "40%";
              	chart.fontFamily = "Microsoft yahei";

                //设置legend
                legend = new AmCharts.AmLegend();
                legend.align = "center";
                legend.markerType = "circle";
                legend.switchType = "v";
                chart.addLegend(legend);
				//3d设置
               	chart.depth3D = 20;
                chart.angle = 30;
				chart.write("chartdiv");
		});
         
		function findDimensions(){
			var winWidth = 0; 
			var winHeight = 0;
			 
			if (window.innerWidth) 
			winWidth = window.innerWidth; 
			else if ((document.body) && (document.body.clientWidth)) 
			winWidth = document.body.clientWidth; 
			 
			if (window.innerHeight) 
			winHeight = window.innerHeight; 
			else if ((document.body) && (document.body.clientHeight)) 
			winHeight = document.body.clientHeight; 
			
			if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth){ 
				winHeight = document.documentElement.clientHeight; 
				winWidth = document.documentElement.clientWidth; 
			}
			return winWidth;
		} 
		function setChartStyle(){
			if(findDimensions()<700){
				 chart.labelsEnabled = false;
			}else{
				chart.labelsEnabled = true;
			}
		}
		window.onresize= setChartStyle;
          
    </script>

  </head>
  
  <body>
      <div id="chartdiv" style="width:100%; height:500px;"></div>
  </body>
</html>
