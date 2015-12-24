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
var sTitle = "";

var sAlign = "";
var sVAlign = "";
var sWidth = "";
var sHeight = "";
var sBorderColor = "#000000";
var sBgColor = "#FFFFFF";

var sImage = "";
var sRepeat = "";
var sAttachment = "";
var sBorderStyle = "";

var sWidthUnit = "%";
var bWidthCheck = true;
var bWidthDisable = false;
var sWidthValue = "100";

var sHeightUnit = "%";
var bHeightCheck = false;
var bHeightDisable = true;
var sHeightValue = "";

var oSel = $.r_s( editor.win() ),
	oCtr = Br.ie ? oSel.createRange().parentElement() : oSel.getRangeAt( 0 ).startContainer;

if (sAction == "tablecellrow"){
	oControl = getParentObject( oCtr, "TR" );
	sAction = "ROW";
	sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.scope_tr",new String[0])%>";
}else{
	oControl = getParentObject( oCtr, "TD" );
	sAction = "CELL";
	sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.scope_td",new String[0])%>";
}
if (oControl){
	sAlign = oControl.align;
	sVAlign = oControl.vAlign;
	sWidth = oControl.style.width;
	sHeight = oControl.style.height;
	sBgColor = oControl.bgColor;
	sImage = oControl.style.backgroundImage;
	sRepeat = oControl.style.backgroundRepeat;
	sAttachment = oControl.style.backgroundAttachment;
	sBorderColor = oControl.tagName == 'TR' ? oControl.cells[0].style.borderBottomColor : oControl.style.borderBottomColor;
	sBorderStyle = oControl.tagName == 'TR' ? oControl.cells[0].style.borderBottomStyle : oControl.style.borderBottomStyle;
	sImage = sImage.substr(4, sImage.length-5);
}
// »�Ϣ͡ʾ£¬µõ½½¹µ㲢ѡ¶¨
function BaseAlert(theText,notice){
	top.DFish.alert(notice);
	theText.focus();
	theText.select();
	return false;
}

// ˇ·򔑐§ҕɫֵ
function IsColor(color){
	var temp=color;
	if (temp=="") return true;
	if (temp.length!=7) return false;
	return (temp.search(/\#[a-fA-F0-9]{6}/) != -1);
}

function getParentObject(obj, tag){
	if (tag == "TD"){
		while(obj!=null && obj.tagName!=tag && obj.tagName!="TH"){
			obj=obj.parentElement;
		}
	}else{
		while(obj!=null && obj.tagName!=tag){
			obj=obj.parentElement;
		}
	}
	return obj;
}

function SearchSelectValue(o_Select, s_Value){
	for (var i=0;i<o_Select.length;i++){
		if (o_Select.options[i].value == s_Value){
			o_Select.selectedIndex = i;
			return true;
		}
	}
	return false;
}
function InitDocument(){
	SearchSelectValue($('d_align'), sAlign.toLowerCase());
	SearchSelectValue($('d_valign'), sVAlign.toLowerCase());
	SearchSelectValue($('d_borderstyle'), sBorderStyle.toLowerCase());

	if ((sWidth == "")||(sWidth==undefined)){
		bWidthCheck = false;
		bWidthDisable = true;
		sWidthValue = "100";
		sWidthUnit = "%";
	}else{
		bWidthCheck = true;
		bWidthDisable = false;
		if (sWidth.substr(sWidth.length-1) == "%"){
			sWidthValue = sWidth.substring(0, sWidth.length-1);
			sWidthUnit = "%";
		}else{
			sWidthUnit = "";
			sWidthValue = parseInt(sWidth);
			if (isNaN(sWidthValue)) sWidthValue = "";
		}
	}
	if (sHeight == ""){
		bHeightCheck = false;
		bHeightDisable = true;
		sHeightValue = "100";
		sHeightUnit = "%";
	}else{
		bHeightCheck = true;
		bHeightDisable = false;
		if (sHeight.substr(sHeight.length-1) == "%"){
			sHeightValue = sHeight.substring(0, sHeight.length-1);
			sHeightUnit = "%";
		}else{
			sHeightUnit = "";
			sHeightValue = parseInt(sHeight);
			if (isNaN(sHeightValue)) sHeightValue = "";
		}
	}

	switch(sWidthUnit){
	case "%":
		$('d_widthunit').selectedIndex = 1;
		break;
	default:
		sWidthUnit = "";
		$('d_widthunit').selectedIndex = 0;
		break;
	}
	switch(sHeightUnit){
	case "%":
		$('d_heightunit').selectedIndex = 1;
		break;
	default:
		sHeightUnit = "";
		$('d_heightunit').selectedIndex = 0;
		break;
	}

	$('d_widthvalue').value = sWidthValue;
	$('d_widthvalue').disabled = bWidthDisable;
	$('d_widthunit').disabled = bWidthDisable;
	$('d_heightvalue').value = sHeightValue;
	$('d_heightvalue').disabled = bHeightDisable;
	$('d_heightunit').disabled = bHeightDisable;
	$('d_bordercolor').value = sBorderColor;
	$('s_bordercolor').style.backgroundColor = sBorderColor;
	$('d_bgcolor').value = sBgColor;
	$('s_bgcolor').style.backgroundColor = sBgColor;
	$('d_widthcheck').checked = bWidthCheck;
	$('d_heightcheck').checked = bHeightCheck;
	$('d_image').value = sImage;
	$('d_repeat').value = sRepeat;
	$('d_attachment').value = sAttachment;

}

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

function IsDigit(){
  return ((event.keyCode >= 48) && (event.keyCode <= 57));
}

function SelectBorderColor(){
	var dEL = $('d_bordercolor');
	var sEL = $('s_bordercolor');
	var url = 'webapp/edt/selcolor.jsp';
	top.VM().cmd(
		{	tagName : 'dialog',
			t :	'<%=FrameworkHelper.getMsg(request,"p.e.choose_color",new String[0])%>',
			src : url,
			w : 280,
			h : Br._ie6 ? 245 : 240,
			poper : dialog,
			args : [ null, null, SelectBorderColorEnd, dEL.value ]
		} );
}
function SelectBorderColorEnd(color) {
		$('d_bordercolor').value=color;
		$('s_bordercolor').style.backgroundColor=color;
}

function SelectBgColor(){
	var dEL = $('d_bgcolor');
	var sEL = $('s_bgcolor');
	var url = 'webapp/edt/selcolor.jsp';
	top.VM().cmd(
		{	tagName : 'dialog',
			t :	'<%=FrameworkHelper.getMsg(request,"p.e.choose_color",new String[0])%>',
			src : url,
			w : 280,
			h : Br._ie6 ? 245 : 240,
			poper : dialog,
			args : [ null, null, SelectBgColorEnd, dEL.value ]
		} );
}
function SelectBgColorEnd(color) {
		$('d_bgcolor').value=color;
		$('s_bgcolor').style.backgroundColor=color;
}
function SelectImage(){
	top.VM().cmd(
		{	tagName : 'dialog',
			t :	'<%=FrameworkHelper.getMsg(request,"p.e.bg_img",new String[0])%>',
			src : 'webapp/edt/backimage.jsp',
			w : 330,
			h : Br._ie6 ? 210 : 205,
			poper : dialog,
			args : window
		} );
}

function setTDCss(s, sBorderColor, sBorderStyle) {
  	s.borderColor = sBorderColor;
  	if (s.borderTopStyle != 'none')	s.borderTopStyle = sBorderStyle;
  	if (s.borderRightStyle != 'none')	s.borderRightStyle = sBorderStyle;
  	if (s.borderBottomStyle != 'none')	s.borderBottomStyle = sBorderStyle;
  	if (s.borderLeftStyle != 'none')	s.borderLeftStyle = sBorderStyle;
}
</script>

<SCRIPT event=onclick for=Ok language=JavaScript>
	sBorderColor = $('d_bordercolor').value;
	if (!IsColor(sBorderColor)){
		BaseAlert($('d_bordercolor'),'<%=FrameworkHelper.getMsg(request,"p.e.invalidate_border_color",new String[0])%>');
		return;
	}
	sBgColor = $('d_bgcolor').value;
	if (!IsColor(sBgColor)){
		BaseAlert($('d_bgcolor'),'<%=FrameworkHelper.getMsg(request,"p.e.invalidate_bg_color",new String[0])%>');
		return;
	}
	var sWidth = "";
	if ($('d_widthcheck').checked){
		if (!MoreThanOne($('d_widthvalue'),'<%=FrameworkHelper.getMsg(request,"p.e.invalidate_width",new String[0])%>')) return;
		sWidth = $('d_widthvalue').value + $('d_widthunit').value;
	}
	var sHeight = "";
	if ($('d_heightcheck').checked){
		if (!MoreThanOne($('d_heightvalue'),'<%=FrameworkHelper.getMsg(request,"p.e.invalidate_height",new String[0])%>')) return;
		sHeight = $('d_heightvalue').value + $('d_heightunit').value;
	}

	sAlign = $('d_align').options[$('d_align').selectedIndex].value;
	sVAlign = $('d_valign').options[$('d_valign').selectedIndex].value;
	sImage = $('d_image').value;
	sRepeat = $('d_repeat').value;
	sAttachment = $('d_attachment').value;
	sBorderStyle = $('d_borderstyle').options[$('d_borderstyle').selectedIndex].value;
	if (sImage!="") {
		sImage = "url(" + sImage + ")";
	}

	if (oControl) {
		try {
			oControl.width = sWidth;
		}
		catch(e) {
			//alert("¶Բ»ǰ£¬ȫźˤɫԐЧµĿ?ֵ£¡\n£¨ɧ£º90%  200  300px  10cm£©");
		}
		try {
			oControl.height = sHeight;
		}
		catch(e) {
			//alert("¶Բ»ǰ£¬ȫźˤɫԐЧµĸ߶ɖµ£¡\n£¨ɧ£º90%  200  300px  10cm£©");
		}

		oControl.align			= sAlign;
		oControl.vAlign			= sVAlign;
  		oControl.bgColor		= sBgColor;
  		var s = oControl.style;
		s.backgroundImage = sImage;
		s.backgroundRepeat = sRepeat;
		s.backgroundAttachment = sAttachment;
		if (oControl.tagName == 'TD') {
			setTDCss(s, sBorderColor, sBorderStyle);
			if (oControl.cellIndex > 0) {
				oControl.previousSibling.style.borderRightStyle = sBorderStyle;
				oControl.previousSibling.style.borderRightColor = sBorderColor;
			}
		} else {
			for (var i = 0; i < oControl.cells.length; i++) {
				setTDCss(oControl.cells.item(i).style, sBorderColor, sBorderStyle);
			}
		}
	}
	editor = null;
	dialog.close();
</SCRIPT>
<SCRIPT>
document.write('<style>');
document.write('.btd{width:'+(Cfg.cn?40:5)+'px;}');
document.write('.file{width:'+(Cfg.cn?235:220)+'px;}');
document.write('.ts{width:'+(Cfg.cn?233:218)+'px;}');
document.write('</style>');
</SCRIPT>

</head>
<body onload="InitDocument()" style="padding:6px;" scroll=no>

<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.layout",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.align_h",new String[0])%>:</td>
		<td width=5></td>
		<td>
			<div style="width:68px;height:16px;overflow:hidden;" class=bd-gray>
			<select id="d_align" style="width:72px;margin:-2px;height:19px">
			<option value=''><%=FrameworkHelper.getMsg(request,"p.e.default",new String[0])%></option>
			<option value='left'><%=FrameworkHelper.getMsg(request,"p.e.align_left2",new String[0])%></option>
			<option value='right'><%=FrameworkHelper.getMsg(request,"p.e.align_right2",new String[0])%></option>
			<option value='center'><%=FrameworkHelper.getMsg(request,"p.e.align_center2",new String[0])%></option>
			<option value='justify'><%=FrameworkHelper.getMsg(request,"p.e.align_2side",new String[0])%></option>
			</select></div>
		</td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.align_v",new String[0])%>:</td>
		<td width=5></td>
		<td>
			<div style="width:68px;height:16px;overflow:hidden;" class=bd-gray>
			<select id="d_valign" style="width:72px;margin:-2px;height:19px">
			<option value=''><%=FrameworkHelper.getMsg(request,"p.e.default",new String[0])%></option>
			<option value='top'><%=FrameworkHelper.getMsg(request,"p.e.align_top2",new String[0])%></option>
			<option value='middle'><%=FrameworkHelper.getMsg(request,"p.e.align_middle2",new String[0])%></option>
			<option value='baseline'><%=FrameworkHelper.getMsg(request,"p.e.align_baseline",new String[0])%></option>
			<option value='bottom'><%=FrameworkHelper.getMsg(request,"p.e.align_bottom2",new String[0])%></option>
			</select></div>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr>
	<td>
	<fieldset class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.size2",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0 width='100%'>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td onclick="d_widthcheck.click()" noWrap valign=middle><input id="d_widthcheck" type="checkbox" onclick="d_widthvalue.disabled=(!this.checked);d_widthunit.disabled=(!this.checked);" value="1"/> <%=FrameworkHelper.getMsg(request,"p.e.set_width",new String[0])%></td>
		<td align=right width="60%">
			<table cellspacing=0 cellpadding=0 align=right>
			  <tr>
				<td><input id=d_widthvalue name="d_widthvalue" type="text" class=mx_text value="" size="5" ONKEYPRESS="event.returnValue=IsDigit();" maxlength="4"></td>
				<td><div style="width:60px;height:17px;overflow:hidden;" class=bd-gray>
					<select id=d_widthunit name="d_widthunit" style="width:64px;margin:-2px;height:19px;">
					<option value='px'><%=FrameworkHelper.getMsg(request,"p.e.unit.px",new String[0])%></option><option value='%'><%=FrameworkHelper.getMsg(request,"p.e.unit.percent",new String[0])%></option>
				</select></div></td>
			  </tr>
			</table>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td height=7></td>
		<td onclick="d_heightcheck.click()" noWrap valign=middle><input id="d_heightcheck" type="checkbox" onclick="d_heightvalue.disabled=(!this.checked);d_heightunit.disabled=(!this.checked);" value="1"/> <%=FrameworkHelper.getMsg(request,"p.e.set_height",new String[0])%></td>
		<td align=right height="60%">
			<table cellspacing=0 cellpadding=0 align=right>
			  <tr>
				<td><input id=d_heightvalue name="d_heightvalue" type="text" class=mx_text value="" size="5" ONKEYPRESS="event.returnValue=IsDigit();" maxlength="4"/></td>
				<td><div style="width:60px;height:17px;overflow:hidden;" class=bd-gray>
					<select id=d_heightunit name="d_heightunit" style="width:64px;margin:-2px;height:19px">
					<option value='px'><%=FrameworkHelper.getMsg(request,"p.e.unit.px",new String[0])%></option>
					<option value='%'><%=FrameworkHelper.getMsg(request,"p.e.unit.percent",new String[0])%></option>
					</select></div></td>
			  </tr>
			</table>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr>
	<td>
	<fieldset class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.style",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.border_color",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_bordercolor size=7 value=""></td>
		<td><img border=0 src="../../img/p/e/rect.gif" width=18 style="cursor:hand" id=s_bordercolor onclick="SelectBorderColor()"></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.border_style",new String[0])%>:</td>
		<td width=5></td>
		<td colspan=2>
			<div style="width:68px;overflow:hidden;" class=bd-gray>
			<select id=d_borderstyle name=d_borderstyle size=1 style="width:72px;margin:-2px;height:19px">
			<option value=""><%=FrameworkHelper.getMsg(request,"p.e.default",new String[0])%></option>
			<option value="solid"><%=FrameworkHelper.getMsg(request,"p.e.bs.solid",new String[0])%></option>
			<option value="dotted"><%=FrameworkHelper.getMsg(request,"p.e.bs.dotted",new String[0])%></option>
			<option value="dashed"><%=FrameworkHelper.getMsg(request,"p.e.bs.solid",new String[0])%></option>
			<option value="double"><%=FrameworkHelper.getMsg(request,"p.e.bs.double",new String[0])%></option>
			<option value="groove"><%=FrameworkHelper.getMsg(request,"p.e.bs.groove",new String[0])%></option>
			<option value="ridge"><%=FrameworkHelper.getMsg(request,"p.e.bs.ridge",new String[0])%></option>
			<option value="inset"><%=FrameworkHelper.getMsg(request,"p.e.bs.inset",new String[0])%></option>
			<option value="outset"><%=FrameworkHelper.getMsg(request,"p.e.bs.outset",new String[0])%></option>
			</select>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.bg_color",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_bgcolor size=7 value=""></td>
		<td><img border=0 src="../../img/p/e/rect.gif" width=18 style="cursor:hand" id=s_bgcolor onclick="SelectBgColor()"></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.bg_img",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_image size=7 value=""><input type=hidden id=d_repeat><input type=hidden id=d_attachment></td>
		<td><img border=0 src="../../img/p/e/rectimg.gif" width=18 style="cursor:hand" id=s_bgimage onclick="SelectImage()"></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value=' <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%> ' class=btn4 id=Ok />&nbsp;&nbsp;<input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%> ' class=btn4 onclick="dialog.close();"/></td></tr>
</table>



</body>
</html>
