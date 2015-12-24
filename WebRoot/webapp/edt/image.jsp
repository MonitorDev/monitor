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
var sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.insert",new String[0])%>";

var ts;
var sFromUrl = "";
var sAlt = "";
var sBorder = "0";
var sBorderColor = "#000000";
var sFilter = "";
var sAlign = "";
var sWidth = "";
var sHeight = "";
var sVSpace = "";
var sHSpace = "";

var sCheckFlag = "file";

var oCtr = $.r_o( editor.win() ),
	oSel = $.r_s( editor.win() ),
	oRng = $.r_r( editor.win() );

if ( oCtr && oCtr.tagName == "IMG" ) {
	sAction = "MODI";
	sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.modify",new String[0])%>";
	sCheckFlag = "url";
	sFromUrl = oCtr.src;
	sAlt = oCtr.alt;
	sBorder = oCtr.border;
	sBorderColor = oCtr.style.borderColor;
	sFilter = oCtr.style.filter;
	sAlign = oCtr.align;
	sWidth = oCtr.width;
	sHeight = oCtr.height;
	sVSpace = oCtr.vspace;
	sHSpace = oCtr.hspace;
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
	var f = document.forms[0];
	//f.d_fromurl.value = sFromUrl;

	SearchSelectValue($('d_filter'), sFilter);
	SearchSelectValue($('d_align'), sAlign.toLowerCase());
	
	$('d_alt').value = sAlt;
	$('d_border').value = sBorder;
	$('d_bordercolor').value = sBorderColor;
	$('s_bordercolor').style.backgroundColor = sBorderColor;
	$('d_width').value = sWidth;
	$('d_height').value = sHeight;
	$('d_vspace').value = sVSpace;
	$('d_hspace').value = sHSpace;
	if (sAction == 'MODI') {
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
function IsDigit(){
  return ((event.keyCode >= 48) && (event.keyCode <= 57));
}

function ReturnValue(){
	var f = document.forms[0];
	sFromUrl = f.d_fromurl.value;
	sAlt = $('d_alt').value;
	sBorder = $('d_border').value;
	sBorderColor = $('d_bordercolor').value;
	sFilter = $('d_filter').value;
	sAlign = $('d_align').value;
	sWidth = $('d_width').value;
	sHeight = $('d_height').value;
	sVSpace = $('d_vspace').value;
	sHSpace = $('d_hspace').value;

	if (sAction == "MODI") {
		oCtr.src = sFromUrl;
		oCtr.alt = sAlt;
		oCtr.border = sBorder;
		oCtr.style.borderColor = sBorderColor;
		oCtr.style.filter = sFilter;
		oCtr.align = sAlign;
		oCtr.width = sWidth;
		oCtr.height = sHeight;
		oCtr.vspace = sVSpace;
		oCtr.hspace = sHSpace;
		if ( ! Br.ie ) {
			var r = oSel.getRangeAt( 0 );
			r.deleteContents();
			r.insertNode( oCtr );
			oSel.removeAllRanges();
		}
	}else{
		var sHTML = '<img src="'+sFromUrl+'"';
		if (sAlt!=""){
			sHTML+=' alt="'+sAlt+'"';
		}
		if (sWidth!=""){
			sHTML+=' width="'+sWidth+'"';
		}
		if (sHeight!=""){
			sHTML+=' height="'+sHeight+'"';
		}
		if (sBorder!='0' && sBorder!='') {
			sHTML+=' border="'+sBorder+'"';
		}
		if (sAlign!='') {
			sHTML+=' align="'+sAlign+'"';
		}
		if (sVSpace!='0' && sVSpace!='') {
			sHTML+=' vspace="'+sVSpace+'"';
		}
		if (sHSpace!='0' && sHSpace!='') {
			sHTML+=' hspace="'+sHSpace+'"';
		}
		if (sFilter!=''||sBorderColor!='#000000') {
			sHTML+=' style="';
			if (sFilter!='') sHTML+='filter:'+sFilter+';';
			if (sBorderColor!='#000000') sHTML+='border-color:'+sBorderColor+';';
			sHTML+='"';
		}
		sHTML+='>';
		
		if ( Br.ie )
			editor.insertHTML( sHTML,
				oCtr && oCtr.ownerDocument == editor.dc() ?	oCtr : oRng );
		else
			editor.format( 'InsertImage', sFromUrl );
			
	}
	//ts.addData(sFromUrl, sFromUrl);
	dialog.close();
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


function BaseAlert(theText,notice){
	parent.DFish.alert(notice);
	theText.focus();
	theText.select();
	return false;
}


function IsURL(url){
	var sTemp;
	var b=true;
	sTemp=url.substring(0,7);
	sTemp=sTemp.toUpperCase();
	if ((sTemp!="HTTP://")||(url.length<10)){
		b=false;
	}
	return b;
}

function IsColor(color){
	var temp=color;
	if (temp=="") return true;
	if (temp.length!=7) return false;
	return (temp.search(/\#[a-fA-F0-9]{6}/) != -1);
}

function ok(){
	$('d_border').value = ToInt($('d_border').value);
	$('d_width').value = ToInt($('d_width').value);
	$('d_height').value = ToInt($('d_height').value);
	$('d_vspace').value = ToInt($('d_vspace').value);
	$('d_hspace').value = ToInt($('d_hspace').value);
	if (!IsColor(sBorderColor)){
		BaseAlert(d_bordercolor,'<%=FrameworkHelper.getMsg(request,"p.e.invalidate_border_color",new String[0])%>');
		return;
	}

	var f = document.forms[0];

	if (f.rdo_from[0].checked) {
		if (f.pictureFile.value == '') {
			BaseAlert(f.pictureFile, '<%=FrameworkHelper.getMsg(request,"p.e.sel_upload_file",new String[0])%>');
			return;
		}
		//if (!Form.isAvail(f))return;
		//Form.lock(f);
		f.rdo_from[0].disabled = true;
		parent.VM().cmd( { tagName : 'loading' } );
		f.action = Cfg.path + Cfg.edt_up_src;
		f.submit();
		return;
	}

	ReturnValue();

}
function uploadEnd(sImgSrc) {
	var f = document.forms[0];
	f.rdo_from[1].click();
	//ts.setStrictValue( sImgSrc );
	f.d_fromurl.value = sImgSrc;
	parent.VM().cmd( { tagName : 'loading' } );
	ok();
}

function SelectBorderColor(){
	var dEL = $('d_bordercolor');
	var sEL = $('s_bordercolor');
	parent.VM().cmd(
		{	tagName : 'dialog',
			t : '<%=FrameworkHelper.getMsg(request,"p.e.choose_color",new String[0])%>',
			src : 'webapp/edt/selcolor.jsp',
			w : 280,
			h : 255,
			poper : event.srcElement,
			args : [ null, null, SelectBorderColorEnd, dEL.value ]
		}
	);
}
function SelectBorderColorEnd(color) {
		$('d_bordercolor').value=color;
		$('s_bordercolor').style.backgroundColor=color;
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
document.write('.lw{width:'+(Cfg.cn?235:220)+'px;}');
document.write('.file{height:' +(Br.ie?18:22)+ 'px}');
document.write('.ts{width:'+(Cfg.cn?231:218)+'px;}');
document.write('.filetr{'+(Cfg.edt_up_src?'':'display:none')+';}');
document.write('</style>');
</script>
</HEAD>

<BODY onload="InitDocument()" onunload="unload()" style="padding:6px;overflow:hidden;" scroll=no>
<form action="/cmsContentAction.do?act=upload" method="post" enctype="multipart/form-data" target="upfr">
<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset class=bd-gray style="padding:0">
	<legend><%=FrameworkHelper.getMsg(request,"p.e.image_source",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0 align=center>
	<tr><td colspan=3 height=10></td></tr>
	<tr class="filetr">
		<td width=54 align=right><input type=radio id=rdo_from_1 name=rdo_from value=1 onclick=changeMode(this) checked><label for=rdo_from_1><%=FrameworkHelper.getMsg(request,"p.e.local_machine",new String[0])%></label>:</td>
		<td width=5></td>
		<td><input type=file class="mx_text lw file" id="pictureFile" name="pictureFile" size=26></td>
	</tr>
	<tr><td colspan=3 height=5></td></tr>
	<tr>
		<td width=54 align=right><input type=radio id=rdo_from_2 name=rdo_from value=2 onclick=changeMode(this) class=filetr><label for=rdo_from_2><%=FrameworkHelper.getMsg(request,"p.e.network",new String[0])%></label>:</td>
		<td width=5></td>
		<td><input type=text name=d_fromurl id="d_fromurl" class="mx_text lw" id=d_alt size=37></td>
	</tr>
	<tr><td colspan=3 height=10></td></tr>
	</table>
	</fieldset>
	</td>
</tr>

<tr><td height=5></td></tr>
<tr>
	<td>
	<fieldset class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.display_effet",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.remark_text",new String[0])%>:</td>
		<td width=5></td>
		<td colspan=5><input type=text class="mx_text lw" id=d_alt size=38></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td noWrap><%=FrameworkHelper.getMsg(request,"p.e.border_width",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_border size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();"></td>
		<td class=btd></td>
		<td noWrap><%=FrameworkHelper.getMsg(request,"p.e.border_color",new String[0])%>:</td>
		<td width=5></td>
		<td><table border=0 cellpadding=0 cellspacing=0><tr><td><input type=text class=mx_text id=d_bordercolor size=7 value=""></td><td><img border=0 src="../../img/p/e/rect.gif" width=18 style="cursor:hand" id=s_bordercolor onclick="SelectBorderColor()"></td></tr></table></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.especial_effect",new String[0])%>:</td>
		<td width=5></td>
		<td>
		  <div style="width:68px;height:16px;overflow:hidden;" class=bd-gray>
			<select id=d_filter style="width:72px;height:19px;margin:-2px" size=1>
			<option value='' selected><%=FrameworkHelper.getMsg(request,"p.e.none",new String[0])%></option>
			<option value='Alpha(Opacity=50)'><%=FrameworkHelper.getMsg(request,"p.e.effect1",new String[0])%></option>
			<option value='Alpha(Opacity=0, FinishOpacity=100, Style=1, StartX=0, StartY=0, FinishX=100, FinishY=140)'><%=FrameworkHelper.getMsg(request,"p.e.effect2",new String[0])%></option>
			<option value='Alpha(Opacity=10, FinishOpacity=100, Style=2, StartX=30, StartY=30, FinishX=200, FinishY=200)'><%=FrameworkHelper.getMsg(request,"p.e.effect3",new String[0])%></option>
			<option value='blur(add=1,direction=14,strength=15)'><%=FrameworkHelper.getMsg(request,"p.e.effect4",new String[0])%></option>
            <option value='blur(add=true,direction=45,strength=30)'><%=FrameworkHelper.getMsg(request,"p.e.effect5",new String[0])%></option>
			<option value='Wave(Add=0, Freq=60, LightStrength=1, Phase=0, Strength=3)'><%=FrameworkHelper.getMsg(request,"p.e.effect6",new String[0])%></option>
			<option value='gray'><%=FrameworkHelper.getMsg(request,"p.e.effect7",new String[0])%></option>
            <option value='Chroma(Color=#FFFFFF)'><%=FrameworkHelper.getMsg(request,"p.e.effect8",new String[0])%></option>
			<option value='DropShadow(Color=#999999, OffX=7, OffY=4, Positive=1)'><%=FrameworkHelper.getMsg(request,"p.e.effect9",new String[0])%></option>
			<option value='Shadow(Color=#999999, Direction=45)'><%=FrameworkHelper.getMsg(request,"p.e.effect10",new String[0])%></option>
			<option value='Glow(Color=#ff9900, Strength=5)'><%=FrameworkHelper.getMsg(request,"p.e.effect11",new String[0])%></option>
			<option value='flipv'><%=FrameworkHelper.getMsg(request,"p.e.effect12",new String[0])%></option>
			<option value='fliph'><%=FrameworkHelper.getMsg(request,"p.e.effect13",new String[0])%></option>
			<option value='grays'><%=FrameworkHelper.getMsg(request,"p.e.effect14",new String[0])%></option>
			<option value='xray'><%=FrameworkHelper.getMsg(request,"p.e.effect15",new String[0])%></option>
			<option value='invert'><%=FrameworkHelper.getMsg(request,"p.e.effect6",new String[0])%></option>
        </select></div>
		</td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.align",new String[0])%>:</td>
		<td width=5></td>
		<td>
			<div style="width:68px;height:16px;overflow:hidden;" class=bd-gray>
			<select id=d_align size=1 style="width:72px;margin:-2px;height:19px">
			<option value='' selected><%=FrameworkHelper.getMsg(request,"p.e.default",new String[0])%></option>
			<option value='left'><%=FrameworkHelper.getMsg(request,"p.e.align_left",new String[0])%></option>
			<option value='right'><%=FrameworkHelper.getMsg(request,"p.e.align_right",new String[0])%></option>
			<option value='top'><%=FrameworkHelper.getMsg(request,"p.e.align_top",new String[0])%></option>
			<option value='middle'><%=FrameworkHelper.getMsg(request,"p.e.align_middle",new String[0])%></option>
			<option value='bottom'><%=FrameworkHelper.getMsg(request,"p.e.align_bottom",new String[0])%></option>
			<option value='absmiddle'><%=FrameworkHelper.getMsg(request,"p.e.align_absmiddle",new String[0])%></option>
			<option value='absbottom'><%=FrameworkHelper.getMsg(request,"p.e.align_absbottom",new String[0])%></option>
			<option value='baseline'><%=FrameworkHelper.getMsg(request,"p.e.align_baseline",new String[0])%></option>
			<option value='texttop'><%=FrameworkHelper.getMsg(request,"p.e.align_texttop",new String[0])%></option>
			</select></div>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.display_width",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_width size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=4></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.display_height",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_height size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=4></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.vspace",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_vspace size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=2></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.hspace",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_hspace size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=2></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value='&nbsp;<%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%>&nbsp;' class=btn4 id=Ok onclick="ok()"/>&nbsp;&nbsp;<input type=button value='&nbsp;<%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%>&nbsp;' class=btn4 onclick="dialog.close();"/></td></tr>
</table>
</form>
<iframe width=0 height=0 name=upfr id=upfr></iframe>
</body>
</html>
