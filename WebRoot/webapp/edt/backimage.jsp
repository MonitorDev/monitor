<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.rongji.dfish.framework.FrameworkHelper" %>
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script>var BaseCfg=parent.Object.clone({type:'js_css'},parent.Cfg);</script>
<script src=../../js/base.js></script>
<script>
var dialog = parent.DFish.g_dialog( parent.VM( window.name ) );
var args = dialog.getDialogArguments();
var fw = args;
var sAction = "OTHER";
var sTitle = "";

var sImage =  fw.d_image.value;
var sRepeat = fw.d_repeat.value;
var sAttachment = fw.d_attachment.value;

sCheckFlag = "url";


// 搜索下拉框值与指定值匹配，并选择匹配项
function SearchSelectValue(o_Select, s_Value){
	for (var i=0;i<o_Select.length;i++){
		if (o_Select.options[i].value == s_Value){
			o_Select.selectedIndex = i;
			return true;
		}
	}
	return false;
}

function InitDocument() {
	SearchSelectValue($('d_repeat'), sRepeat.toLowerCase());
	SearchSelectValue($('d_attachment'), sAttachment.toLowerCase());
	document.forms[0].d_fromurl.value = sImage;
	if (sImage) {
		document.getElementsByName('rdo_from')[1].click();
	}
}

function ReturnValue() {
	var f = document.forms[0];
	var sFromUrl = f.d_fromurl.value;
	if (sFromUrl == ""){
		sImage = "";
		sRepeat = "";
		sAttachment = "";
	}else{
		if (sAction == "OTHER"){
			sImage = sFromUrl;
		}else{
			sImage = "url(" + sFromUrl + ")";
		}
		sRepeat = $('d_repeat').options[$('d_repeat').selectedIndex].value;
		sAttachment = $('d_attachment').options[$('d_attachment').selectedIndex].value;
	}

	switch(sAction){
	case "MODI":
		oControl.style.backgroundImage = sImage;
		oControl.style.backgroundRepeat = sRepeat;
		oControl.style.backgroundAttachment = sAttachment;
		break;
	case "OTHER":
		fw.d_image.value = sImage;
		fw.d_repeat.value = sRepeat;
		fw.d_attachment.value = sAttachment;
		break;
	default:
		fw.setHTML("<table border=0 cellpadding=0 cellspacing=0 width='100%' height='100%'><tr><td valign=top style='background-image:"+sImage+";background-repeat:"+sRepeat+";background-attachment:"+sAttachment+";'>"+dialogArguments.getHTML()+"</td></tr></table>");
		break;
	}
	dialog.close();
}

function ok(){
	var f = document.forms[0];

	if (f.d_checkfromfile.checked) {
		if (f.pictureFile.value == '') {
			BaseAlert(f.pictureFile, '<%=FrameworkHelper.getMsg(request,"p.e.sel_upload_file",new String[0])%>');
			return;
		}
		parent.VM().cmd( { tagName : 'loading' } );
		f.action = Cfg.path + Cfg.edt_up_src;
		f.submit();
		return;
	}

	if (f.d_checkfromurl.checked){
		ReturnValue();
	} else if (f.d_checkcancel.checked) {
		ReturnValue();
	}
}
function uploadEnd(sImgSrc) {
	var f = document.forms[0];
	f.rdo_from[1].click();
	f.d_fromurl.value = sImgSrc;
	ok();
}

function changeMode(rdo) {
	var f = document.forms[0];
	f.d_fromurl.disabled = rdo.value==1;
	f.pictureFile.disabled = rdo.value == '2';
}
function unload() {
	parent.VM().cmd( { tagName : 'loading', hide : 1 } );
}

document.write('<style>');
document.write('.btd{width:'+(Cfg.cn?40:5)+'px;}');
document.write('.file{width:'+(Cfg.cn?235:220)+'px;}');
document.write('.filetr{'+(Cfg.edt_up_src?'':'display:none')+';}');
document.write('</style>');

</script>
</HEAD>

<BODY bgcolor=white style="padding:6px" onunload=unload() onload="InitDocument()" scroll=no>
<form action="/cmsContentAction.do?act=upload" method="post" enctype="multipart/form-data" target="hiddenframe">
<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset class=bd-gray style="padding:0">
	<legend><%=FrameworkHelper.getMsg(request,"p.e.image_source",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0 align=center>
	<tr><td colspan=3 height=5></td></tr>
	<tr class="filetr">
		<td width=54 align=right><input type=radio id=d_checkfromfile name=rdo_from value=1 onclick=changeMode(this) checked><label for=d_checkfromfile><%=FrameworkHelper.getMsg(request,"p.e.local_machine",new String[0])%></label>:</td>
		<td width=5></td>
		<td><input type=file class="mx_text file" id="pictureFile" name="pictureFile" size=26 style="height:18px;" value=""></td>
	</tr>
	<tr><td colspan=3 height=5></td></tr>
	<tr>
		<td width=54 align=right><input type=radio id=d_checkfromurl name=rdo_from value=2 onclick=changeMode(this) class="filetr"><label for=d_checkfromurl><%=FrameworkHelper.getMsg(request,"p.e.network",new String[0])%></label>:</td>
		<td width=5></td>
		<td><input type=text name=d_fromurl id="d_fromurl" class="mx_text lw" id=d_alt size=38></td>
	</tr>
	<tr><td colspan=3 height=5></td></tr>
	<tr>
		<td colspan=3 style=padding-left:4px;>
        <input type=radio id=d_checkcancel name=rdo_from value=3 onclick=changeMode(this)><label for=d_checkcancel><%=FrameworkHelper.getMsg(request,"p.e.cancel_bg_image",new String[0])%></label></td>
	</tr>
	<tr><td colspan=3 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>

<tr><td height=5></td></tr>
<tr>
	<td>
	<fieldset class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request, "p.e.display_effet", new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=5></td>
		  <td><%=FrameworkHelper.getMsg(request, "p.e.span_style", new String[0])%>:</td>
		<td width=3></td>
		  <td width="99">
            <select id=d_repeat size=1 style="width:72px">
			<option value='' selected><%=FrameworkHelper.getMsg(request, "p.e.span_style1", new String[0])%></option>
			<option value='repeat'><%=FrameworkHelper.getMsg(request, "p.e.span_style2", new String[0])%></option>
			<option value='no-repeat'><%=FrameworkHelper.getMsg(request, "p.e.span_style3", new String[0])%></option>
			<option value='repeat-x'><%=FrameworkHelper.getMsg(request, "p.e.span_style4", new String[0])%></option>
			<option value='repeat-y'><%=FrameworkHelper.getMsg(request, "p.e.span_style5", new String[0])%></option>
			</select>
		</td>
		  <td width=3></td>
		  <td><%=FrameworkHelper.getMsg(request, "p.e.roll_fix", new String[0])%>:</td>
		<td width=3></td>
		  <td width="80">
            <select id=d_attachment style="width:72px" size=1>
			<option value='' selected><%=FrameworkHelper.getMsg(request, "p.e.roll_fix_style1", new String[0])%></option>
			<option value='scroll'><%=FrameworkHelper.getMsg(request, "p.e.roll_fix_style2", new String[0])%></option>
			<option value='fixed'><%=FrameworkHelper.getMsg(request, "p.e.roll_fix_style3", new String[0])%></option>
            </select>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value=' <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%> ' class=btn4 id=Ok onclick="ok()"/>&nbsp;&nbsp;<input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%> ' class=btn4 onclick="dialog.close();"/></td></tr>
</table>
</form>
<iframe class=hide name=hiddenframe id=hiddenframe></iframe>
</body>
</html>
