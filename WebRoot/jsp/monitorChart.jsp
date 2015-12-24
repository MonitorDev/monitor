<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ip = request.getParameter("ip");
String serverName = request.getParameter("serverName");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
 <head>
 
    <base href="<%=basePath%>">
 		<style type="text/css">
			body{ font-family:"Microsoft yahei";font-size:12px;color: #333;}
			.chart{
				width:100%; 
				height:180px;
				margin-left: 5px;
				margin-right: 5px;
				margin-top: 10px;
			}
			.menu{
				margin-left: 10px;
				cursor:pointer;	
			}
			.item{
				margin-top: 10px;
			}

		</style>
  <script src="js/pl/amcharts/jquery-1.8.0.min.js" type="text/javascript"></script>        
<script src="js/pl/highchart/highcharts.js" type="text/javascript" ></script>
 <body>
  <script type="text/javascript">
		$(document).ready(function (){
 		Highcharts.setOptions({
            global: {
                useUTC: false
            },
            lang:{
	              loading:'加载中...',
                  months:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
                  printButtonTitle:'打印图表',
                  resetZoom:"还原图表",
                  resetZoomTitle:'还原图表为1:1大小',
                  weekdays:['Sun', 'Mon', 'Tues', 'Wednes', 'Thurs', 'Fri', 'Satur']
             }
        });

		
		var createChart = function(id,time,data){
		$("#"+id).highcharts({
           chart: {
        	  	type:"spline",
                zoomType: 'x',
                spacingRight: 20,
                style: {
					fontFamily: 'Microsoft yahei',
					fontSize: '12px'
				}
            },
            title: {
                text: null
            },
           
            xAxis: {
               //	type:"datetime",
               	tickLength:1,
                tickInterval: 1,
				gridLineWidth: 1,
				labels: {
                    formatter: function() {
            			return null;
                    }
				},
				categories: time
                
            },
            yAxis: {
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                		if(this.value<0){
                			return null;
                		}
                        return this.value +"%";
                    },
                    style: {
						fontFamily: 'Microsoft yahei',
						fontSize: '12px'
					}
				}
			},
            tooltip: {
                crosshairs: true,
            	shared: false,
            	formatter: function() {
                   return "<div><span><b>时间:</b> "+this.x+"</span><br/><span><b>使用率:</b> "+this.y+"%</span></div>";
			    },
			    style: {
					fontFamily: 'Microsoft yahei',
					fontSize: '12px'
				}
                
            },
            legend: {
                enabled: false
            },

            plotOptions: {
               spline: {
            		lineWidth:1,
                    marker: {
                        enabled: false,
                        symbol: 'circle',
            			radius: 1
                		
                    }
            	},
            	series:{
            			states:{
            				hover:{
            					lineWidth:1
            				}
            			}
				}         
            },

    		credits:{
	       			enabled: false
	        		
            },
            series: [{name: null,data: data}]
            
        	});
            }
        	
		
          	$.getJSON("perform.sp?act=monitorDataSource&ip=<%=ip%>&serverName=<%=serverName%>",function(d) {
	         	if(d.cpu && d.cpu.length==0){
	         		$("body").html("<div style='text-align:center;color:gray;margin-top:20px'>暂无相关记录</div>");
	         	}else{
	         		
	         		$('body').html("<div class='item'>"+
									"<span  class='menu'><img src='img/p/playd.gif'/>&nbsp;CPU使用率监控</span>"+
									"<div id='cpu' class='chart'></div>"+
									"</div>"+
								"<div class='item'>"+
									"<span class='menu'><img src='img/p/playd.gif'/>&nbsp;JVM使用率监控</span>"+
									"<div id='jvm' class='chart'></div>"+
								"</div>"+
								"<div class='item'>"+
									"<span class='menu'><img src='img/p/playd.gif'/>&nbsp;服务器使用内存监控</span>"+
									"<div id='serverUsedMemory' class='chart'></div>"+
								"</div>"+
								"<div class='item'>"+
									"<span class='menu'><img src='img/p/playd.gif'/>&nbsp;服务器实际使用内存监控</span>"+
									"<div id='serverActualUsedMemory' class='chart'></div>"+
								"</div>");
	         		
	         		 $(".menu").click(function(){
		           	  	if(!$(this).next("div").is(":hidden")){
							$(this).find("img").attr("src","img/p/playr.gif");
						}else{
							$(this).find("img").attr("src","img/p/playd.gif");
						}
						$(this).next("div").slideToggle(10);
					});
          			var datas = [d.cpu,d.jvm,d.serverUsedMemory,d.serverActualUsedMemory];
  					var name = ["cpu","jvm","serverUsedMemory","serverActualUsedMemory"];
					for(var i =0;i<name.length;i++){
						createChart(name[i],d.time,datas[i]);
					}	
				}
          		
          	});
      	});
	</script>
     
			
				
	</body>
</html>
