<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String Title = request.getParameter("Title");
String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");
String type = request.getParameter("type");


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="js/pl/amcharts/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/pl/highchart/highstock.js"></script>

</head>
<body>
	<div id="container" ></div>
	<script type="text/javascript">
	var seriesOptions = [];

$(function(){
		$.getJSON('perform.sp?act=lineChartDataSource&Title=<%=Title%>&startTime=<%=startTime%>&endTime=<%=endTime%>&type=<%=type%>',function(d) {	
			seriesOptions[0] = {name:d.cpuname,data: d.cpudata};
			seriesOptions[1] = {name:d.jvmname,data: d.jvmdata};
			seriesOptions[2] = {name:d.severname,data: d.severdata};
			seriesOptions[3] = {name:d.severactname,data: d.severactdata};		
			createChart();
			
		});

});
	Highcharts.setOptions({
		 lang:{
              rangeSelectorZoom:'时间',
              decimalPoint:'.',
              downloadPNG:'下载PNG文件',
              downloadJPEG:'下载JPG文件',
              downloadPDF:'下载PDF文件',
              downloadSVG:'下载SVG文件',
              exportButtonTitle:'导出图表',
              loading:'加载中...',
              months:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
              printButtonTitle:'打印图表',
              resetZoom:'还原图表',
              resetZoomTitle:'还原图表为1:1大小',
              thousandsSep:','
              
        }
	});
	function createChart(){
		
		 var chart = new Highcharts.StockChart({
        
                  
                    chart: {
                   		type:"spline",
                        renderTo: 'container',
                        //zoomType: 'x',
                        height:500
                    },
                    
                    colors:[
                    '#FF6600',
                    '#55bf3b',
                    '#0d8ecf',
                    '#FF0000'
                  ],
                    
                  	
                   
                 
                   title: {
		                text: "服务<%=Title%>性能统计图",
		                x: -20 ,
		                style:{
		            		 fontSize: '16px',
	                        fontFamily: 'Microsoft YaHei'
		                }
	                
		            },
		            subtitle:{
		            	text:"时间:<%=startTime%>至<%=endTime%>",
		            	 x: -20 ,
		            
		                style:{
		            		 fontSize: '12px',
	                        fontFamily: 'Microsoft YaHei'
		                }
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
        			
		            
   					navigator:{
            			
            			enabled:true,
				        xAxis: {
            			labels: {
	            			style:{
			            		 fontSize: '12px',
		                        fontFamily: 'Microsoft YaHei'
			                }
            			},
            			type: 'datetime',
			            showEmpty: false,
			            dateTimeLabelFormats: {
			              millisecond: '%Y-%m',
			              second: '%Y-%m-%d %H:%M:%S',
			              minute: '%Y-%m-%d %H:%M',
			              hour:   '%Y-%m-%d %H时',
			              day:    '%Y-%m-%d',
			              week:   '%Y-%m-%d',
			              month:  '%Y-%m-%d',
			              year:   '%Y'
			            }
			          }
   					},
   					credits:{
		       			enabled: false
		        	},
   					legend:{
   						enabled: true,
	                    labelFormatter: function() {
	                        return this.name;
	                    },
	                    itemStyle: {
	                       width:30,
						   cursor: 'pointer',
						   color: '#274b6d',
						   fontSize: '12px',
						   fontFamily: 'Microsoft YaHei'
						},
	                 	floating:true,
	                   // layout :'vertical',
	                    width :335,
	                  	symbolWidth :40,
	                    borderRadius :10,
	                  	align:'right',
	                    verticalAlign :'top',
	                    x:-20,
	                    y:30
	              	},
   				
                    rangeSelector: {
						
                   		buttonTheme: { 
					    		//fill: 'none',
					    		//stroke: 'none',
					    		//'stroke-width': 0,
					    		// r: 8,
					    		style: {
					    			color: '#274b6d',
					    			//fontWeight: 'bold',
					    			fontFamily: 'Microsoft YaHei'
					    		},
					    		states: {
					    			hover: {
					    			},
					    			select: {
					    				fill: '#039',
					    				style: {
					    					color: 'white'
					    				}
					    			}
					    		}
					    	},
				    	//inputBoxBorderColor: 'gray',
				    	inputBoxWidth: 120,
				    	inputBoxHeight: 18,
				    	inputStyle: {
				    		color: '#039'
						},
				    	labelStyle: {
				    		color: '#274b6d',
				    		fontFamily: 'Microsoft YaHei'

				    	},
                        buttons: [{
                                type: 'hour',
                                count: 1,
                                text: '1时'
                            },
                        	{
                                type: 'day',
                                count: 1,
                                text: '1天'
                            },
                        	{
                                type: 'month',
                                count: 1,
                                text: '1月'
                            }, {
                                type: 'month',
                                count: 3,
                                text: '3月'
                            }, {
                                type: 'month',
                                count: 6,
                                text: '6月'
                            },{ 
                                type: 'all',
                                text: '所有'
                            }],
                        // 默认选择域：0（缩放按钮中的第一个）、1（缩放按钮中的第二个）……
                        selected: 7,
                        // 是否允许input标签选框
                        inputEnabled: false
                    },
                    
                  
                    tooltip:{
                        // 日期时间格式化
                        xDateFormat: '%Y-%m-%d %A',
                        enabled: true,
			            crosshairs: true,
			            shared:false,
			            formatter: function() {
							var	header = '<b>时间: </b>' + Highcharts.dateFormat('%Y年%m月%e日  %H时%M分', this.x);

			                var label = '<br/><b>' + this.point.series.name +'使用率</b> :'+this.point.y+'%</span> ';
			                if (label != "") {
			                    return header + label;
			                } else {
			                    return false;
			                }
			            },
			            style:{
		            		 fontSize: '12px',
	                         fontFamily: 'Microsoft YaHei'
		                }
                    },
                    
                  
                    xAxis:{
                    	 //tickInterval: 1,
						 gridLineWidth: 1,
                    	 type: 'datetime', //定义x轴上日期的显示格式  
                        // 如果X轴刻度是日期或时间，该配置是格式化日期及时间显示格式
                         dateTimeLabelFormats: {
                            second: '%Y-%m-%d<br/>%H:%M',
                            minute: '%Y-%m-%d<br/>%H:%M',
                            hour: '%Y-%m-%d<br/>%H:%M',
                            day: '%Y<br/>%m-%d',
                            week: '%Y<br/>%m-%d',
                            month: '%Y-%m',
                            year: '%Y'
                        },
                        labels: {
                        	 style: {
								fontFamily: 'Microsoft yahei',
								fontSize: '12px'
							 }
                        }
                     	
                    },
                    yAxis:{
						tickInterval: 10,
	                    labels: {
	                         formatter: function() {
	                         	return this.value+"%";
	                         },
	                         style: {
								fontFamily: 'Microsoft yahei',
								fontSize: '12px'
							 }
	                    },
	                    min:0
                    },
                   
                    series: seriesOptions
                });
	}
</script>
</body>
</html>