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

var oCtr;
var oSeletion;
var sRangeType;

var sFromUrl = "http://";
var sAlt = "";
var bNewwin = false;

var oCtr = $.r_o( editor.win() ),
	oSel = $.r_s( editor.win() ),
	oRng = $.r_r( editor.win() );

if ( Br.ie && oSel.type == 'Text' )
	oCtr = oRng.parentElement();
	
if ( oCtr && oCtr.tagName != 'A' ) {
	oCtr = $.pel( oCtr, 'A' );
}

if ( oCtr && oCtr.tagName == 'A' ) {
	sAction = "MODI";
	var r = oCtr.outerHTML.match( /href="?([^ "]+)"?/ );
	sFromUrl = r ? editor.html2code( r[ 1 ] ) : '';
	sAlt = oCtr.title ? editor.html2code( oCtr.title ) : '';
	bNewwin = oCtr.target && oCtr.target.toLowerCase()=='_blank';
}


function InitDocument(){
	$('d_fromurl').value = sFromUrl;
	$('d_alt').value = sAlt;
	$('d_newwin').checked = bNewwin;
}

function BaseAlert(theText,notice){
	DFish.alert(notice);
	theText.focus();
	theText.select();
	return false;
}

function ok(){
	sFromUrl = $('d_fromurl').value;
	sAlt = $('d_alt').value;
	bNewwin = $('d_newwin').checked;
	if ( Br.ie )
		oRng.select();
	editor.format( 'CreateLink', editor.code2html( sFromUrl ) );
	if (oCtr && oCtr.tagName == 'A') {
		if ( sAlt ) oCtr.title = editor.code2html( sAlt );
		else oCtr.removeAttribute( 'alt' );
		if ( bNewwin ) oCtr.target = '_blank';
		else oCtr.removeAttribute( 'target' );
	}
	dialog.close();
}
</script>
<script>
document.write('<style>');
document.write('.file{width:'+(Cfg.cn?235:220)+'px;}');
document.write('</style>');
</script>

<BODY onload="InitDocument()" style="padding:6px;overflow:hidden" scroll=no>
<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset class=bd-gray style="padding:0;margin:0">
	<legend><%=FrameworkHelper.getMsg(request,"p.e.link_prop",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=5 height=10></td></tr>
	<tr>
		<td width=7></td>
		<td width=54 align=right><%=FrameworkHelper.getMsg(request,"p.e.link_url",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class="mx_text file" id="d_fromurl" name="d_fromurl" style="width:233px" size=30 value=""></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=5 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td width=54 align=right><%=FrameworkHelper.getMsg(request,"p.e.remark_text",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class="mx_text file" id="d_alt" name="d_alt" style="width:233px" size=30 value=""></td>
		<td width=7></td>
	</tr>
	<tr><td height=5></td></tr>
	<tr>
		<td width=7></td>
		<td colspan=3><input type=checkbox id=d_newwin checked><label for=d_newwin><%=FrameworkHelper.getMsg(request,"p.e.open_in_new_win",new String[0])%></label></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=5 height=10></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value=' <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%> ' class=btn4 id=Ok onclick="ok()"/>&nbsp;&nbsp;<input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%> ' class=btn4 onclick="dialog.close();"/></td></tr>
</table>
</body>
</html>
