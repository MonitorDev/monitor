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

var sRow = "2";
var sCol = "2";
var sAlign = "";
var sBorder = editor.inf ? "0" : "1";
var sCellPadding = "3";
var sCellSpacing = "2";
var sWidth = "";
var sHeight = "";
var sBorderColor = "#000000";
var sBgColor = "";

var sImage = "";
var sRepeat = "";
var sAttachment = "";
var sBorderStyle = "";

var sWidthUnit = "%";
var bWidthCheck = true;
var bWidthDisable = false;
var sWidthValue = "99.9";

var sHeightUnit = "%";
var bHeightCheck = false;
var bHeightDisable = true;
var sHeightValue = "";

var oCtr = $.r_o( editor.win() ),
	oSel = $.r_s( editor.win() ),
	oCell;
	
if ( Br.ie && ! oCtr )
	oCtr = getParentObject( oSel.createRange().parentElement(), "TABLE" );

if ( oCtr ) {
	oCell = oCtr.rows[0].cells[0];
	sAction = "MODI";
	sRow = oCtr.rows.length;
	sCol = getColCount(oCtr);
	sAlign = oCtr.align;
	sBorder = parseInt(oCell.style.borderWidth);
	if (isNaN(sBorder)) sBorder = '1';
	sCellPadding = oCtr.cellPadding;
	sCellSpacing = oCtr.cellSpacing;
	sWidth = oCtr.style.width||oCtr.width;
	sHeight = oCtr.style.height||oCtr.height;
	sBgColor = oCtr.bgColor;
	sImage = oCtr.style.backgroundImage;
	sRepeat = oCtr.style.backgroundRepeat;
	sAttachment = oCtr.style.backgroundAttachment;
	sBorderColor = oCell.style.borderBottomColor;
	sBorderStyle = oCell.style.borderBottomStyle;
	sImage = sImage.substr(4, sImage.length-5);
}



function getParentObject(obj, tag){
	while(obj!=null && obj.tagName!=tag)
		obj=obj.parentElement;
	return obj;
}

function SelectBorderColor(){
	var dEL = $('d_bordercolor');
	var sEL = $('s_bordercolor');
	var url = 'webapp/edt/selcolor.jsp';
	top.VM().cmd(
		{	tagName : 'dialog',
			t :	'选择颜色',
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
			t :	'选择颜色',
			src : url,
			w : 280,
			h : 245,
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
			t :	'背景图',
			src : 'webapp/edt/backimage.jsp',
			w : 340,
			h : Br._ie6 ? 210 : 205,
			poper : dialog,
			args : window
		} );
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
	SearchSelectValue($('d_borderstyle'), sBorderStyle.toLowerCase());


	if (sAction == "MODI"){
		if (sWidth == ""){
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

	$('d_row').value = sRow;
	$('d_col').value = sCol;
	$('d_border').value = sBorder;
	$('d_cellspacing').value = sCellSpacing;
	$('d_cellpadding').value = sCellPadding;
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

	$('d_row').focus();
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

function getColCount(oTable) {
	var intCount = 0;
	if (oTable != null) {
		for(var i = 0; i < oTable.rows.length; i++){
			if (oTable.rows[i].cells.length > intCount) intCount = oTable.rows[i].cells.length;
		}
	}
	return intCount;
}

function InsertRows( oTable ) {
	if ( oTable ) {
		oSelection.select();
		editor.hO(1);
	}
}

function InsertCols( oTable ) {
	if ( oTable ) {
		oSelection.select();
		editor.hI(1);
	}
}

function DeleteRows( oTable ) {
	if ( oTable ) {
		oTable.deleteRow();
	}
}

function DeleteCols( oTable ) {
	if ( oTable ) {
		for(var i=0;i<oTable.rows.length;i++){
			oTable.rows[i].deleteCell();
		}
	}
}
function IsDigit(){
  return ((event.keyCode >= 48) && (event.keyCode <= 57));
}

function IsColor(color){
	var temp=color;
	if (temp=="") return true;
	if (temp.length!=7) return false;
	return (temp.search(/\#[a-fA-F0-9]{6}/) != -1);
}

function BaseAlert(theText,notice){
	top.DFish.alert(notice);
	theText.focus();
	theText.select();
	return false;
}
</script>

<SCRIPT event=onclick for=Ok language=JavaScript>

	sBorderColor = $('d_bordercolor').value;
	if (!IsColor(sBorderColor)){
		BaseAlert($('d_bordercolor'),'非法颜色值');
		return;
	}

	sBgColor = $('d_bgcolor').value;
	if (!IsColor(sBgColor)){
		BaseAlert($('d_bgcolor'),'非法颜色值');
		return;
	}

	if (!MoreThanOne($('d_row'),'非法行数')) return;

	if (!MoreThanOne($('d_col'),'非法列数')) return;

	if ($('d_border').value == "") $('d_border').value = "0";
	if ($('d_cellpadding').value == "") $('d_cellpadding').value = "0";
	if ($('d_cellspacing').value == "") $('d_cellspacing').value = "0";

	$('d_border').value = parseFloat($('d_border').value);
	$('d_cellpadding').value = parseFloat($('d_cellpadding').value);
	$('d_cellspacing').value = parseFloat($('d_cellspacing').value);

	var sWidth = "";
	if ($('d_widthcheck').checked){
		if (!MoreThanOne($('d_widthvalue'),'非法宽度值')) return;
		sWidth = $('d_widthvalue').value + $('d_widthunit').value;
	}

	var sHeight = "";
	if ($('d_heightcheck').checked){
		if (!MoreThanOne($('d_heightvalue'),'非法高度值')) return;
		sHeight = $('d_heightvalue').value + $('d_heightunit').value;
	}

	sRow = $('d_row').value;
	sCol = $('d_col').value;
	sAlign = $('d_align').options[$('d_align').selectedIndex].value;
	sBorder = $('d_border').value;
	sCellPadding = $('d_cellpadding').value;
	sCellSpacing = $('d_cellspacing').value;
	sImage = $('d_image').value;
	sRepeat = $('d_repeat').value;
	sAttachment = $('d_attachment').value;
	sBorderStyle = $('d_borderstyle').options[$('d_borderstyle').selectedIndex].value;
	if (sImage!="") {
		sImage = "url(" + sImage + ")";
	}

	if (sAction == "MODI") {
		var xCount = sRow - oCtr.rows.length;
  		if (xCount > 0)
	  		for (var i = 0; i < xCount; i++) InsertRows(oCtr);
  		else
	  		for (var i = 0; i > xCount; i--) DeleteRows(oCtr);
			var xCount = sCol - getColCount(oCtr);
  		if (xCount > 0)
  			for (var i = 0; i < xCount; i++) InsertCols(oCtr);
  		else
  			for (var i = 0; i > xCount; i--) DeleteCols(oCtr);

		try {
			oCtr.width = sWidth;
		}
		catch(e) {
			//alert("¶Բ»ǰ£¬ȫźˤɫԐЧµĿ?ֵ£¡\n£¨ɧ£º90%  200  300px  10cm£©");
		}
		try {
			oCtr.height = sHeight;
		}
		catch(e) {
			//alert("¶Բ»ǰ£¬ȫźˤɫԐЧµĸ߶ɖµ£¡\n£¨ɧ£º90%  200  300px  10cm£©");
		}

		oCtr.align			= sAlign;
  		oCtr.cellSpacing	= sCellSpacing;
  		oCtr.cellPadding	= sCellPadding;
  		oCtr.bgColor		= sBgColor;
		oCtr.style.backgroundImage = sImage;
		oCtr.style.backgroundRepeat = sRepeat;
		oCtr.style.backgroundAttachment = sAttachment;
  		//oCtr.border		= sBorder;
  		//oCtr.borderColor	= sBorderColor;
		//oCtr.style.borderStyle = sBorderStyle;
		editor.rszTbl(oCtr, sBorder, sBorderColor, sBorderStyle);

	}else{
		var sTable = "<table class=xdLayout align='"+sAlign+"' border=1 cellpadding=3 cellspacing=0 width='"+sWidth+"' height='"+
			sHeight+"' bgcolor='"+sBgColor+
			"' style='BORDER:medium none;border-collapse:collapse;background-image:"+sImage+";background-repeat:"+sRepeat+
			";background-attachment:"+sAttachment+";'><colgroup>";
		if ( editor.inf ) {
			if ( sBorder < 1 ) {
				sBorder = 1;
				sBorderColor = '#898989';
				sBorderStyle = 'dashed';
			}
		}
		var sTdWidth,
			sBDcss = 'border:' + sBorder + 'px '+ sBorderStyle + ' ' + sBorderColor + ';';
		if ($('d_widthunit').value == '%') {
			sTdWidth = String(100/sCol).substr(0,5) + '%';
		} else {
			sTdWidth = String(sWidth/sCol).substr(0,5) + 'px';
		}
		for (var j=1;j<=sCol;j++){
			sTable += '<col>';
		}
		sTable += '</colgroup>';
		for (var i=1;i<=sRow;i++){
			sTable += '<tr>';
			for (var j=1;j<=sCol;j++){
				sTable += '<td style="border:' + sBorder + 'px '+(sBorder ? (sBorderStyle||'solid') : 'dashed')+' '+sBorderColor+';';
				sTable += '">&nbsp;</td>';
			}
			sTable = sTable + '</tr>';
		}
		sTable = sTable + '</table>';
		if ( Br.ie )
			oSel.createRange().select();
		editor.insertHTML( sTable );
	}
	dialog.close();
</SCRIPT>
<SCRIPT>
document.write('<style>');
document.write('.btd{width:'+(Cfg.cn?40:5)+'px;}');
document.write('.lw{width:'+(Cfg.cn?235:220)+'px;}');
document.write('.file{height:' +(Br.ie?18:22)+ 'px}');
document.write('.ts{width:'+(Cfg.cn?233:218)+'px;}');
//document.write('.sel{margin:-'+(Br.ie?2:1)+'px;}');
document.write('</style>');
</SCRIPT>

</head>
<body bgcolor=white onload="InitDocument()" style="padding:6px;overflow:hidden;" scroll=no>

<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
	<td>
	<fieldset style=padding:0 class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.table.size",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.table.rows",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_row size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=3></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.table.cols",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_col size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=3></td>
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
	<fieldset style=padding:0 class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.table.layout",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.align",new String[0])%>:</td>
		<td width=5></td>
		<td>
		  <div style="width:68px;height:16px;overflow:hidden;" class=bd-gray>
		  <select id="d_align" style="width:72px;height:19px;margin:-2px" class=sel>
			<option value=''><%=FrameworkHelper.getMsg(request,"p.e.default",new String[0])%></option>
			<option value='left'><%=FrameworkHelper.getMsg(request,"p.e.align_left2",new String[0])%></option>
			<option value='center'><%=FrameworkHelper.getMsg(request,"p.e.align_center",new String[0])%></option>
			<option value='right'><%=FrameworkHelper.getMsg(request,"p.e.align_right2",new String[0])%></option>
		  </select></div>
		</td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.border_width",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_border size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();"></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.cell_spacing",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_cellspacing size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=3></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.cell_padding",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_cellpadding size=10 value="" ONKEYPRESS="event.returnValue=IsDigit();" maxlength=3></td>
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
	<fieldset style=padding:0 class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.table.size2",new String[0])%></legend>
	<table border=0 cellpadding=0 cellspacing=0 width='100%'>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td onclick="d_widthcheck.click()" noWrap valign=middle><input id="d_widthcheck" type="checkbox" onclick="d_widthvalue.disabled=(!this.checked);d_widthunit.disabled=(!this.checked);" value="1"/> <%=FrameworkHelper.getMsg(request,"p.e.set_table_width",new String[0])%></td>
		<td align=right width="60%">
			<table cellspacing=0 cellpadding=0 align=right>
			  <tr>
				<td><input name="d_widthvalue" id="d_widthvalue" type="text" class=mx_text value="" size="5" ONKEYPRESS="event.returnValue=IsDigit();" maxlength="4"></td>
				<td><div style="width:60px;height:17px;overflow:hidden;" class=bd-gray>
						<select name="d_widthunit" id="d_widthunit" style="width:64px;height:19px;margin:-2px" class=sel>
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
		<td onclick="d_heightcheck.click()" noWrap valign=middle><input id="d_heightcheck" type="checkbox" onclick="d_heightvalue.disabled=(!this.checked);d_heightunit.disabled=(!this.checked);" value="1"/> <%=FrameworkHelper.getMsg(request,"p.e.set_table_height",new String[0])%></td>
		<td align=right height="60%">
			<table cellspacing=0 cellpadding=0 align=right>
			  <tr>
				<td><input name="d_heightvalue" id="d_heightvalue" type="text" class=mx_text value="" size="5" ONKEYPRESS="event.returnValue=IsDigit();" maxlength="4"></td>
				<td><div style="width:60px;height:17px;overflow:hidden;" class=bd-gray>
					<select name="d_heightunit" id="d_heightunit" style="width:64px;height:19px;margin:-2px" class=sel>
					<option value='px'><%=FrameworkHelper.getMsg(request,"p.e.unit.px",new String[0])%></option><option value='%'><%=FrameworkHelper.getMsg(request,"p.e.unit.percent",new String[0])%></option>
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
	<fieldset style=padding:0 class=bd-gray>
	<legend><%=FrameworkHelper.getMsg(request,"p.e.table.style",new String[0])%></legend>
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
		  <div style="width:68px;height:16px;overflow:hidden;" class=bd-gray>
			<select id=d_borderstyle size=1 style="width:72px;height:19px;margin:-2px" class=sel>
			<option value=""><%=FrameworkHelper.getMsg(request,"p.e.default",new String[0])%></option>
			<option value="solid"><%=FrameworkHelper.getMsg(request,"p.e.bs.solid",new String[0])%></option>
			<option value="dotted"><%=FrameworkHelper.getMsg(request,"p.e.bs.dotted",new String[0])%></option>
			<option value="dashed"><%=FrameworkHelper.getMsg(request,"p.e.bs.dashed",new String[0])%></option>
			<option value="double"><%=FrameworkHelper.getMsg(request,"p.e.bs.double",new String[0])%></option>
			<option value="groove"><%=FrameworkHelper.getMsg(request,"p.e.bs.groove",new String[0])%></option>
			<option value="ridge"><%=FrameworkHelper.getMsg(request,"p.e.bs.ridge",new String[0])%></option>
			<option value="inset"><%=FrameworkHelper.getMsg(request,"p.e.bs.inset",new String[0])%></option>
			<option value="outset"><%=FrameworkHelper.getMsg(request,"p.e.bs.outset",new String[0])%></option>
			</select></div>
		</td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	<tr>
		<td width=7></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.bg_color",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_bgcolor size=7 value=""></td>
		<td><img border=0 src="../../img/p/e/rect.gif" width=18 style="cursor:hand" id=s_bgcolor onclick="SelectBgColor()"/></td>
		<td class=btd></td>
		<td><%=FrameworkHelper.getMsg(request,"p.e.bg_img",new String[0])%>:</td>
		<td width=5></td>
		<td><input type=text class=mx_text id=d_image size=7 value=""><input type=hidden id=d_repeat /><input type=hidden id=d_attachment /></td>
		<td><img border=0 src="../../img/p/e/rectimg.gif" width=18 style="cursor:hand" id=s_bgimage onclick="SelectImage()"/></td>
		<td width=7></td>
	</tr>
	<tr><td colspan=9 height=5></td></tr>
	</table>
	</fieldset>
	</td>
</tr>
<tr><td height=5></td></tr>
<tr><td align=right><input type=submit value=' <%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%> ' id=Ok class=btn4/>&nbsp;&nbsp;<input type=button value=' <%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%> ' onclick="dialog.close();" class=btn4 /></td></tr>
</table>



</body>
</html>
