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
var oRng = $.r_r( editor.win() );
var text = oRng.htmlText;

function InitDocument(){

}

function BaseAlert(theText,notice){
	DFish.alert(notice);
	theText.focus();
	theText.select();
	return false;
}

function ok(){
	var t = $.f_vals( $.db(), 'd_tag' ),
		v = $( 'd_fromurl' ).value,
		w = $( 'd_target' ).checked;	
	if ( Br.ie )
		oRng.select();
	editor.insertHTML( '<span tag=' + t + ' class=tag v="' + v + '"' + ( w ? ' target="_blank"' : '' ) + '>' + text + '</span>' );
	dialog.close();
}
function rd_tag( a ) {
	var s = '链接地址';
	if ( a.value == 'search' || a.value == 'baike' )
		s = '关键词';
	$( 'd_tag_label' ).innerHTML = s;
}
</script>
<script>
document.write('<style>');
document.write('.file{width:'+(Cfg.cn?235:220)+'px;}');
document.write('.rdo{margin:0 2px;}.btn4{width:60px;}');
document.write('</style>');
</script>

<BODY onload="InitDocument()" style="padding:6px;overflow:hidden" scroll=no>
<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset class=bd-gray style="padding:0;margin:0">
	<legend>标签属性</legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=5 height=10></td></tr>
	<tr>
		<td width=7></td>
		<td width=54 align=right>方 式:</td>
		<td width=5></td>
		<td><input type=radio id="d_tag0" name="d_tag" class=rdo onclick="rd_tag(this)" value="link" checked><label for=d_tag0>链接</label>
			<input type=radio id="d_tag1" name="d_tag" class=rdo onclick="rd_tag(this)" value="search"><label for=d_tag1>搜索</label>
			<input type=radio id="d_tag2" name="d_tag" class=rdo onclick="rd_tag(this)" value="baike"><label for=d_tag2>百科</label>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=5 height=10></td></tr>
	<tr>
		<td width=7></td>
		<td width=54 align=right><span id="d_tag_label">链接地址</span>:</td>
		<td width=5></td>
		<td><input type=text class="mx_text file" id="d_fromurl" name="d_fromurl" style="width:233px" size=30 value=""></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=5 height=10></td></tr>
	<tr>
		<td width=7></td>
		<td width=54 align=right></td>
		<td colspan=2><input type=checkbox id="d_target" name="d_target" value="1" checked><label for=d_target>新窗口打开</label></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=5 height=15></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value=' <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%> ' class=btn4 id=Ok onclick="ok()"/>&nbsp;&nbsp;<input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%> ' class=btn4 onclick="dialog.close();"/></td></tr>
</table>
</body>
</html>
