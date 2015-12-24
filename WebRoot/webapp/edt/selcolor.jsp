<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.rongji.dfish.framework.FrameworkHelper" %>
<HTML>
<HEAD>
<META content="text/html; charset=UTF-8" http-equiv=Content-Type/>
<script>var BaseCfg=parent.Object.clone({type:'js_css'},parent.Cfg);</script>
<script src=../../js/base.js></script>
<script>
var dialog = parent.DFish.g_dialog( parent.VM( window.name ) );
var args = dialog.getDialogArguments();
var sAction = args[0];
var editor = args[1];
var handler = args[2];
var color = args[3]||'';

var oCtr = editor && $.r_o( editor.win() ),
	oSel = editor && $.r_s( editor.win() ),
	oRng = editor && $.r_r( editor.win() );

switch (sAction) {
	case "forecolor":
		sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.forecolor",new String[0])%>";
		if ( Br.ie )
			color = oRng.queryCommandValue("ForeColor");
		if (color) color = N2Color(color);
		break;
	case "backcolor":
		sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.backcolor",new String[0])%>";
		if ( Br.ie )
			color = oRng.queryCommandValue("BackColor");
		if (color) color = N2Color(color);
		break;
	case "bgcolor":
		sTitle = "<%=FrameworkHelper.getMsg(request,"p.e.bgcolor",new String[0])%>";
		if ( oCtr ) {
			oCtr = GetControl( oRng, "TABLE" );
		}else{
			oCtr = GetParent( Br.ie ? oRng.parentElement() : oRng.startContainer );
		}
		if (oCtr) {
			switch( oCtr.tagName ) {
			case "TD":
				sTitle += " - <%=FrameworkHelper.getMsg(request,"p.e.scope_td",new String[0])%>";
				break;
			case "TR":
			case "TH":
				sTitle += " - <%=FrameworkHelper.getMsg(request,"p.e.scope_tr",new String[0])%>";
				break;
			default:
				sTitle += " - <%=FrameworkHelper.getMsg(request,"p.e.scope_table",new String[0])%>";
				break;
			}
			color = oCtr.bgColor;
		}else{
			sTitle += " - <%=FrameworkHelper.getMsg(request,"p.e.scope_page",new String[0])%>";
		}
		break;
	default:
	//if (URLParams['color']){
		//	color = decodeURIComponent(URLParams['color']) ;
		//}
		break;
}

if (!color) color = "#000000";

function GetParent(obj){
	while(obj!=null && obj.tagName!="TD" && obj.tagName!="TR" && obj.tagName!="TH" && obj.tagName!="TABLE")
		obj=obj.parentElement;
	return obj;
}

function GetControl( obj, sTag ) {
	obj = Br.ie ? obj.item( 0 ) : obj.startContainer;
	if (obj.tagName==sTag){
		return obj;
	}
	return null;
}

function N2Color(s_Color){
	s_Color = s_Color.toString(16);
	switch (s_Color.length) {
	case 1:
		s_Color = "0" + s_Color + "0000";
		break;
	case 2:
		s_Color = s_Color + "0000";
		break;
	case 3:
		s_Color = s_Color.substring(1,3) + "0" + s_Color.substring(0,1) + "00" ;
		break;
	case 4:
		s_Color = s_Color.substring(2,4) + s_Color.substring(0,2) + "00" ;
		break;
	case 5:
		s_Color = s_Color.substring(3,5) + s_Color.substring(1,3) + "0" + s_Color.substring(0,1) ;
		break;
	case 6:
		s_Color = s_Color.substring(4,6) + s_Color.substring(2,4) + s_Color.substring(0,2) ;
		break;
	default:
		s_Color = "";
	}
	return '#' + s_Color;
}

function InitDocument(){
	$('ShowColor').bgColor = color;
	$('RGB').innerHTML = color;
	$('SelColor').value = color;
}


var SelRGB = color;
var DrRGB = '';
var SelGRAY = '120';

var hexch = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');

function ToHex(n) {
	var h, l;

	n = Math.round(n);
	l = n % 16;
	h = Math.floor((n / 16)) % 16;
	return (hexch[h] + hexch[l]);
}

function DoColor(c, l){
	var r, g, b;

	r = '0x' + c.substring(1, 3);
	g = '0x' + c.substring(3, 5);
	b = '0x' + c.substring(5, 7);

	if(l > 120){
		l = l - 120;

		r = (r * (120 - l) + 255 * l) / 120;
		g = (g * (120 - l) + 255 * l) / 120;
		b = (b * (120 - l) + 255 * l) / 120;
	}else{
		r = (r * l) / 120;
		g = (g * l) / 120;
		b = (b * l) / 120;
	}

	return '#' + ToHex(r) + ToHex(g) + ToHex(b);
}

function EndColor(){
	var i;

	if(DrRGB != SelRGB){
		DrRGB = SelRGB;
		for(i = 0; i <= 30; i ++)
		$('GrayTable').rows[i].bgColor = DoColor(SelRGB, 240 - i * 8);
	}

	$('SelColor').value = DoColor($('RGB').innerText, $('GRAY').innerText);
	$('ShowColor').bgColor = $('SelColor').value;
}

function IsColor(color){
	var temp=color;
	if (temp=="") return true;
	if (temp.length!=7) return false;
	return (temp.search(/\#[a-fA-F0-9]{6}/) != -1);
}

function ctClick() {
	if (event.srcElement.bgColor) {
		SelRGB = event.srcElement.bgColor;
		EndColor();
	}
}
function ctOver() {
	$('RGB').innerText = event.srcElement.bgColor;
	EndColor();
}
function ctOut() {
	$('RGB').innerText = SelRGB;
	EndColor();
}
function gtClick() {
	SelGRAY = event.srcElement.title;
	EndColor();
}
function gtOver() {
	$('GRAY').innerText = event.srcElement.title;
	EndColor();
}
function gtOut() {
	$('GRAY').innerText = SelGRAY;
	EndColor();
}
function okClick() {
	color = $('SelColor').value;
	if (!IsColor(color)){
		parent.DFish.alert('<%=FrameworkHelper.getMsg(request,"p.e.invalidate_color",new String[0])%>');
		return;
	}
	try {
		editor.focus();
		oRng.select();
	}catch(e){}
	switch (sAction) {
		case "forecolor":
			editor.format('ForeColor', color) ;
			break;
		case "backcolor":
			editor.format('BackColor', color) ;
			break;
		case "bgcolor":
			if (oCtr){
				oCtr.bgColor = color;
			}else{
				editor.setHTML("<table border=0 cellpadding=0 cellspacing=0 width='100%' height='100%'><tr><td valign=top bgcolor='"+color+"'>"+editor.getHTML()+"</td></tr></table>");
			}
			break;
		default:
			handler.call(editor,color);
			break;
	}
	editor = null;
	oSel = null;
	dialog.close();
}
</SCRIPT>

</HEAD>

<BODY onload="InitDocument()" style="padding:0px;overflow:hidden" scroll=no>
<DIV align=center>
<CENTER>
<TABLE border=0 cellPadding=0 cellSpacing=10>
<TBODY>
<TR>
<TD>
<TABLE border=0 cellPadding=0 cellSpacing=0 id=ColorTable onmouseover=ctOver() onmouseout=ctOut() onclick=ctClick() style="CURSOR: hand">
<SCRIPT language=JavaScript>
function wc(r, g, b, n){
	r = ((r * 16 + r) * 3 * (15 - n) + 0x80 * n) / 15;
	g = ((g * 16 + g) * 3 * (15 - n) + 0x80 * n) / 15;
	b = ((b * 16 + b) * 3 * (15 - n) + 0x80 * n) / 15;
	var hr = ToHex(r);
	var hg = ToHex(g);
	var hb = ToHex(b);
	document.write('<TD BGCOLOR=#' + hr + hg + hb + ' height=8 width=8></TD>');
}

var cnum = new Array(1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0);

for(i = 0; i < 16; i ++){
	document.write('<TR>');
	for(j = 0; j < 30; j ++){
		n1 = j % 5;
		n2 = Math.floor(j / 5) * 3;
		n3 = n2 + 3;

		wc((cnum[n3] * n1 + cnum[n2] * (5 - n1)),
		(cnum[n3 + 1] * n1 + cnum[n2 + 1] * (5 - n1)),
		(cnum[n3 + 2] * n1 + cnum[n2 + 2] * (5 - n1)), i);
	}

	document.writeln('</TR>');
}
</SCRIPT>

<TBODY></TBODY></TABLE></TD>
<TD>
<TABLE border=0 cellPadding=0 cellSpacing=0 id=GrayTable onmouseover=gtOver() onmouseout=gtOut() onclick=gtClick() style="CURSOR: hand">
<SCRIPT language=JavaScript>
for(i = 255; i >= 0; i -= 8.5)
document.write('<TR BGCOLOR=#' + ToHex(i) + ToHex(i) + ToHex(i) + '><TD TITLE=' + Math.floor(i * 16 / 17) + ' height=4 width=20></TD></TR>');
</SCRIPT>

<TBODY></TBODY></TABLE></TD></TR></TBODY></TABLE></CENTER></DIV>
<DIV align=center>
<CENTER>
<TABLE border=0 cellPadding=0 cellSpacing=10>
<TBODY>
<TR>
<TD align=middle rowSpan=2><%=FrameworkHelper.getMsg(request,"p.e.selected_color",new String[0])%>
<TABLE border=1 cellPadding=0 cellSpacing=0 height=30 id=ShowColor width=40 bgcolor="">
<TBODY>
<TR>
<TD></TD></TR></TBODY></TABLE></TD>
<TD rowSpan=2><%=FrameworkHelper.getMsg(request,"p.e.base_color",new String[0])%>: <SPAN id=RGB></SPAN><BR><%=FrameworkHelper.getMsg(request,"p.e.brightness",new String[0])%>: <SPAN
id=GRAY>120</SPAN><BR><%=FrameworkHelper.getMsg(request,"p.e.code",new String[0])%>: <INPUT id=SelColor size=7 value="" class=mx_text></TD>
<TD><BUTTON id=Ok type=submit class=btn4 onclick=okClick()>&nbsp;<%=FrameworkHelper.getMsg(request,"p.e.confirm",new String[0])%>&nbsp;</BUTTON></TD></TR>
<TR>
<TD><BUTTON onclick=dialog.close(); class=btn4>&nbsp;<%=FrameworkHelper.getMsg(request,"p.e.cancel",new String[0])%>&nbsp;</BUTTON></TD></TR></TBODY></TABLE></CENTER></DIV>

</BODY></HTML>
