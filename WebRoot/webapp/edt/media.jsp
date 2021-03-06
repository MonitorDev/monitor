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

var sFromUrl = "";
var sWidth = "";
var sHeight = "";

var oCtr = $.r_o( editor.win() ),
	oSel = $.r_s( editor.win() );

if ( oCtr && oCtr.tagName == 'EMBED' ) {
	sAction = 'MODI';
	sFromUrl = oCtr.src;
	sWidth = oCtr.width;
	sHeight = oCtr.height;
}


function ReturnValue(){
	var f = document.forms[0];
	var sFromUrl = f.d_fromurl.value;
	var sWidth = $('d_width').value;
	var sHeight = $('d_height').value;
	var a = sFromUrl.substr( sFromUrl.lastIndexOf( '.' ) + 1 ).toLowerCase();
	var sHTML = ( a == 'rm' || a == 'rmvb' || a == 'ra' ) ? '<EMBED src="' + sFromUrl + '" width="' + sWidth + '" height="' + sHeight + '" type="audio/x-pn-realaudio-plugin" console="Clip1" controls="IMAGEWINDOW,ControlPanel,StatusBar" autostart="false"></EMBED>' :
		'<object id="player" height="' + sHeight + '" width="' + sWidth + '" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6"><param NAME="AutoStart" VALUE="0"><param name="enabled" value="-1"><param NAME="EnableContextMenu" VALUE="-1"><param NAME="url" value="' + sFromUrl + '"><param name="uiMode" value="mini"></object>';
	editor.insertHTML(sHTML, oRange);
	dialog.close();
}

function ok(){
	$('d_width').value = ToInt($('d_width').value);
	$('d_height').value = ToInt($('d_height').value);
	var f = document.forms[0];
	if (f.rdo_from[0].checked) {
		if (f.pictureFile.value == '') {
			BaseAlert(f.pictureFile, '<%=FrameworkHelper.getMsg(request,"p.e.sel_upload_file",new String[0])%>');
			return;
		}
		//if (!Form.isAvail(f))return;
		//Form.lock(f);
		f.rdo_from[0].disabled = true;
		top.VM().cmd( { tagName : 'loading' } );
		f.action = Cfg.edt_up_src;
		f.submit();
		return;
	}

		ReturnValue();

}
function uploadEnd(sImgSrc) {
	var f = document.forms[0];
	f.rdo_from[1].click();
	f.d_fromurl.value=sImgSrc;
	top.VM().cmd( { tagName : 'loading', hide : 1 } );
	ok();
}

function BaseAlert(theText,notice){
	top.DFish.alert(notice);
	theText.focus();
	theText.select();
	return false;
}

function BaseTrim(str){
	  lIdx=0;rIdx=str.length;
	  if (BaseTrim.arguments.length==2)
	    act=BaseTrim.arguments[1].toLowerCase()
	  else
	    act="all"
      for(var i=0;i<str.length;i++){
	  	thelStr=str.substring(lIdx,lIdx+1)
		therStr=str.substring(rIdx,rIdx-1)
        if ((act=="all" || act=="left") && thelStr==" "){
			lIdx++
        }
        if ((act=="all" || act=="right") && therStr==" "){
			rIdx--
        }
      }
	  str=str.slice(lIdx,rIdx)
      return str
}

function ToInt(str){
	str=BaseTrim(str);
	if (str!=""){
		var sTemp=parseFloat(str);
		if (isNaN(sTemp)){
			str="";
		}else{
			str=sTemp;
		}
	}
	return str;
}
function IsDigit(){
  return ((event.keyCode >= 48) && (event.keyCode <= 57));
}
function changeMode(rdo) {
	var f = document.forms[0];
	f.d_fromurl.disabled = rdo.value==1;
	f.pictureFile.disabled = rdo.value == '2';
}
function onLoad() {
	var f = document.forms[0];
	f.d_fromurl.value = sFromUrl;
	if (sAction == 'MODI') {
		$('d_width').value = sWidth;
		$('d_height').value = sHeight;
		if ( Cfg.edt_up_src )
			f.rdo_from[1].click();
		f.d_fromurl.value = sFromUrl;
		f.d_fromurl.focus();
	} else {
		if ( Cfg.edt_up_src ) {
			f.rdo_from[0].click();
			f.pictureFile.focus();
		} else {
			f.d_fromurl.focus();
		}
	}
}
function unload() {
	top.VM().cmd( { tagName : 'loading', hide : 1 } );
}
document.write('<style>');
document.write('.btd{width:'+(Cfg.cn?40:5)+'px;}');
document.write('.lw{width:'+(Cfg.cn?235:220)+'px;}');
document.write('.file{height:' +(Br.ie?18:22)+ 'px}');
document.write('.ts{width:'+(Cfg.cn?233:218)+'px;}');
document.write('.filetr{'+(Cfg.edt_up_src?'':'display:none')+';}');
document.write('</style>');
</script>
</HEAD>

<BODY onload="onLoad()" onunload="unload()" style="padding:6px;overflow:hidden" scroll=no>
<form action="/cmsContentAction.do?act=upload" method="post" enctype="multipart/form-data" target="hiddenframe">
<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset class=bd-gray style=padding:0>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.media_source",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=3 height=10></td></tr>
	<tr class="filetr">
		<td width=54 align=right><input type=radio id=rdo_from_1 name=rdo_from value=1 onclick=changeMode(this) checked><label for=rdo_from_1><%=FrameworkHelper.getMsg(request,"p.e.local_machine",new String[0])%></label>:</td>
		<td width=5></td>
		<td><input type=file class="mx_text lw file" id="pictureFile" name="pictureFile" size=26></td>
	</tr>
	<tr><td colspan=3 height=5></td></tr>
	<tr>
		<td width=54 align=right><input type=radio id=rdo_from_2 name=rdo_from value=2 onclick=changeMode(this) class="filetr"><label for=rdo_from_2><%=FrameworkHelper.getMsg(request,"p.e.network",new String[0])%></label>:</td>
		<td width=5></td>
		<td><input type=text name=d_fromurl id="d_fromurl" class="mx_text lw" id=d_alt size=38></td>
	</tr>
	<tr><td colspan=3 height=10></td></tr>
	</table>
	</fieldset>
	</td>
</tr>

<tr><td height=10></td></tr>
<tr>
	<td>
	<fieldset class=bd-gray style=padding:0>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.play_effet",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td noWrap><%=FrameworkHelper.getMsg(request,"p.e.play_width",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_width size=10 value="350" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=4></td>
		<td class=btd></td>
		<td noWrap><%=FrameworkHelper.getMsg(request,"p.e.play_height",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_height size=10 value="310" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=4></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=10></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value='  <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%>  ' id=Ok class=btn4 onclick="ok()"/>&nbsp;&nbsp;<input type=button value='  <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%>  ' class=btn4 onclick="dialog.close();"/></td></tr>
</table>
</form>
<iframe class=hide name=hiddenframe id=hiddenframe></iframe>
</body>
</html>
