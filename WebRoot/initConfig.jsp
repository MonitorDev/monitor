
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page import="com.rongji.dfish.framework.FrameworkHelper"%>
<%@page import="com.rongji.dfish.engines.xmltmpl.ViewFactory"%><%
	response.setHeader("Cache-Control", "no-store");
 response.setHeader("Pragma", "no-cache");
 response.setDateHeader("Expires", 0);
%><%@ page contentType="text/html; charset=UTF-8" %><!DOCTYPE html>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<title> 平台初始化配置 </title>
<link rel="shortcut icon" href="./img/p/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script>
<%
String cfgVer = "1";
%>
var Cfg = {
debug	: true,
path	: '<%=path%>/',
src		: 'vm:|initConfig.sp?act=index',	// ITASK50 face
msgSrc	: 'communication.sp?act=msg',
msgH2LCounter	: 20,
msgHighFrequency : 6,
msgLowFrequency : 60,
cn		: true,
lang    : 'zh_CN',
<%
ViewFactory skin=FrameworkHelper.getViewFactory(request);
%>
skin1	: 'itask7/default',//'<%=skin.getSkinFilePath()%>',
skin	: {
	theme: 'i7_3d',
	color : 'default',
	bg    : 'layout-1'
},
ver		: '<%=cfgVer%>',

dg_slide : false,		// 对话框滑动显示
dg_dft_tpl : 'f_std',	// 对话框默认模板
noshct : 1,

cn_bytes : 3,			// 双字节字符在数据库中的长度
}

</script>
<script src="js/base.js?v=<%=cfgVer%>"></script>
<script src="m/dfish.js?v=<%=cfgVer%>"></script>
</head>
<body scroll="no" style="overflow:hidden;margin:0" leftmargin="0" topmargin="0">

</body>
</html>



