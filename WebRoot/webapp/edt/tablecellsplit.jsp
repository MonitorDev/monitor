<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.rongji.dfish.framework.FrameworkHelper" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script>var BaseCfg=parent.Object.clone({type:'js_css'},parent.Cfg);</script>
<script src=../../js/base.js></script>
<script>
var dialog = parent.DFish.g_dialog( parent.VM( window.name ) );
var args = dialog.getDialogArguments();
var sAction = args[0];
var editor = args[1];
var handler = args[2];
var color = args[3]||'';

var oRng = $.r_r( editor.win() );

function MoreThanOne(obj, sErr){
	var b=false;
	if (obj.value!=""){
		obj.value=parseFloat(obj.value);
		if (obj.value!="0"){
			b=true;
		}
	}
	if (b==false){
		BaseAlert(obj,sErr);
		return false;
	}
	return true;
}
// »�Ϣ͡ʾ£¬µõ½½¹µ㲢ѡ¶¨
function BaseAlert(theText,notice){
	top.DFish.alert(notice,'warn');
	theText.focus();
	theText.select();
	return false;
}

// Ԥ@
function doView(opt){
	if (opt=="col"){
		$('d_col').checked=true;
		$('d_row').checked=false;
	}else{
		$('d_col').checked=false;
		$('d_row').checked=true;
	}
	if ($('d_col').checked){
		$('d_view').innerHTML = "<table border=1 cellpadding=0><tr><td width=25>&nbsp;</td><td width=25>&nbsp;</td></tr></table>";
		$('d_label').innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;<%=FrameworkHelper.getMsg(request,"p.e.cols",new String[0])%>:";
	}
	if ($('d_row').checked){
		$('d_view').innerHTML = "<table border=1 cellpadding=0 width=50><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr></table>";
		$('d_label').innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;<%=FrameworkHelper.getMsg(request,"p.e.rows",new String[0])%>:";
	}
}
function IsDigit(){
  return ((event.keyCode >= 48) && (event.keyCode <= 57));
}
function ok() {
	if (!MoreThanOne($('d_num'),'<%=FrameworkHelper.getMsg(request,"p.e.invalidate_rows_cols",new String[0])%>')) return;
	if ( Br.ie )
		oRng.select();
	if ($('d_row').checked){
		editor.tableRowSplit(parseInt($('d_num').value));
	}
	if ($('d_col').checked){
		editor.tableColSplit(parseInt($('d_num').value));
	}
	dialog.close();
}
</script>

</head>
<body bgcolor=menu style="overflow:hidden" scroll=no>

<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=3 height=5></td></tr>
	<tr><td><input type=radio id=d_col checked onclick="doView('col')"><label for="d_col"><%=FrameworkHelper.getMsg(request,"p.e.split_to_cols",new String[0])%></label></td><td rowspan=3 width=30></td><td width=60 rowspan=3 id=d_view valign=middle align=center></td></tr>
	<tr><td height=5></td></tr>
	<tr><td><input type=radio id=d_row onclick="doView('row')"><label for="d_row"><%=FrameworkHelper.getMsg(request,"p.e.split_to_rows",new String[0])%></label></td></tr>
	<tr><td height=5 colspan=3></td></tr>
	<tr>
		<td id=d_label></td>
		<td></td>
		<td><input type=text id=d_num size=8 value="2" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=3></td>
	</tr>
	</table>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%> ' class=btn4 onclick=ok() />&nbsp;&nbsp;<input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%> ' class=btn4 onclick="dialog.close();"/></td></tr>
</table>

<Script Language=JavaScript>
doView('col');
</Script>

</body>
</html>
