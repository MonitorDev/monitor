<%@page import="com.rongji.websiteMonitor.webapp.system.controller.PortalIndexController"%><%
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page import="com.rongji.dfish.framework.FrameworkHelper"%>
<%@page import="com.rongji.dfish.base.Utils"%>
<%@page import="com.rongji.dfish.engines.xmltmpl.ViewFactory"%><%
 response.setHeader("Cache-Control", "no-store");
 response.setHeader("Pragma", "no-cache");
 response.setDateHeader("Expires", 0);
%><%@ page contentType="text/html; charset=UTF-8" %><!DOCTYPE html>
<html>
<head>
<!--meta http-equiv="X-UA-Compatible" content="IE=edge"/-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>监控平台 </title>
<link rel="shortcut icon" href="m/index/img/favicon.ico" />
<script>
<%
String cfgVer = "20141123";
String queryString=request.getQueryString();%>
var Cfg = {
debug	: true,
path	: '<%=path%>/',
src		: 'vm:|index.sp?act=index<%=queryString==null?"":("&"+queryString)%>',
msgSrc	: 'communication.sp?act=msg',
msgH2LCounter	: 20,
msgHighFrequency : 10,
msgLowFrequency : 10,
cn		: true,
lang    : 'zh_CN',
skin	: {
	theme : 'i7_3d',
	color : 'default',
	bg    : 'layout-1'
},

ver		: '<%=cfgVer%>',

dg_slide : true,		// 对话框滑动显示
dg_dft_tpl : 'f_std',	// 对话框默认模板
dg_alert_tpl : 'f_std_x',	// 对话框默认模板
shortcut : {
		src : '',	// 快捷输入视图
		w : 400,		// 宽度
		h : 270,		// 高度
		tpl : 'none',	// Optional 使用模板
		t: '快捷输入'
	},
cn_bytes : 3,			// 双字节字符在数据库中的长度
php:0,
intf:{
	ssoPath:""
},
ws_style : '',

moduleOptions:<%=PortalIndexController.getMenuJson(null)%>,
sysArg : ''
}


</script>
<script src="js/base.js?v=<%=cfgVer%>"></script>
<script src="m/index.js?v=<%=cfgVer%>"></script>
<script src="FusionCharts/js/showFusionCharts.js"></script>
<script src="FusionCharts/js/FusionCharts.js"></script>
 <script src="js/pl/highchart/highcharts.js"></script>
<script src="monitor/chart.js"></script>
<style type="text/css">
/*检测模块模块样式*/
.tpanner{margin:0;padding:0; list-style:none; font-size:12px; line-height:20px;}
.tpanner li{ list-style:none; float:left; display:inline-block; padding:5px 10px; background:#f5fafd; border:#aacee4 solid 1px; min-width:150px; margin-left:10px; border-radius:5px;}
.tpanner li.first_child{ margin-left:0;}	
.tpanner table td{ padding:0 5px; text-align:left;}
.tpanner p{ margin:0; padding:0;}
.tpanner .panner-icon{text-align:center;width:42px;}
.tpanner .panner-icon img{ max-width:32px;}
.font-color-blue{ color:#09F;}	
.font-color-green{ color:#090 ;}	
.font-color-red{ color:#FF0000 ;}	
.font-color-lightblue{ color:#6CF ;}	

*{
	padding:0;
	margin:0;
	
}
.clearfix:after{ content:""; height:0; visibility:hidden; display:block; clear:both;}
.clearfix{ zoom:1;}

.mod_tab{
font-size:14px;
	margin-top: 10px;
	padding:0 10px;
}
.mod_tab .tab{
	padding:10px 0;
	border-bottom:#CCC solid 1px;
	
}
.mod_tab .tab li{
	line-height:20px;
	height:20px;
	border-right:#CCC solid 1px;
	float:left;
	padding:0 10px;
	list-style:none;
	cursor:pointer;
}
.mod_tab .tab li.nb{
	border:0;
}
.mod_tab .tab li.on{
	font-weight:bold;
}
.mod_tab .modcon{
	padding:10px;
}
</style>
</head>
<body scroll="no" style="overflow:hidden;margin:0" leftmargin="0" topmargin="0" >

</body>

</html>




