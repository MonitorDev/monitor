<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.rongji.dfish.framework.FrameworkHelper" %>
<html>
<head>
<title>find replace</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script>var BaseCfg=parent.Object.clone({type:'js_css'},parent.Cfg);</script>
<script src=../../js/base.js></script>
<script>
var dialog = parent.DFish.g_dialog( parent.VM( window.name ) );
var args = dialog.getDialogArguments();
var sAction = args[0];
var editor = args[1];
var oRng;
var oDoc = editor.dc();

var oCtr = $.r_o( editor.win() ),
	oRng = $.r_r( editor.win() );


function searchtype(){
    var retval = 0;
    var matchcase = 0;
    var matchword = 0;
    if (document.frmSearch.blnMatchCase.checked) matchcase = 4;
    if (document.frmSearch.blnMatchWord.checked) matchword = 2;
    retval = matchcase + matchword;
    return(retval);
}

function checkInput(){
    if (document.frmSearch.strSearch.value.length < 1) {
        top.DFish.alert("<%=FrameworkHelper.getMsg(request,"p.e.enter_search_key",new String[0])%>");
        return false;
    } else {
        return true;
    }
}

function findtext(){
    if (checkInput()) {
        var searchval = document.frmSearch.strSearch.value;
        oRng.collapse(false);
        if (oRng.findText(searchval, 1000000000, searchtype())) {
            oRng.select();
            if (findtext._turnhead) findtext._turnhead = 0;
        } else {
        	if (!findtext._turnhead) {
	            if (confirm("<%=FrameworkHelper.getMsg(request,"p.e.search_from_top",new String[0])%>")) {
	            	findtext._turnhead = 1;
	                oRng.expand("textedit");
	                oRng.collapse();
	                oRng.select();
	                findtext();
	            }
	        } else {
	        	top.DFish.alert('<%=FrameworkHelper.getMsg(request,"p.e.can_not_find",new String[0])%>');
	        	findtext._turnhead = 0;
	        }
        }
    }
}

function replacetext(){
    if (checkInput()) {
        if (document.frmSearch.blnMatchCase.checked){
            if (oRng.text == document.frmSearch.strSearch.value) oRng.text = document.frmSearch.strReplace.value
        } else {
            if (oRng.text.toLowerCase() == document.frmSearch.strSearch.value.toLowerCase()) oRng.text = document.frmSearch.strReplace.value
        }
        findtext();
    }
}

function replacealltext(){
    if (checkInput()) {
        var searchval = document.frmSearch.strSearch.value;
        var wordcount = 0;
        var msg = "";
        oRng.expand("textedit");
        oRng.collapse();
        oRng.select();
        while (oRng.findText(searchval, 1000000000, searchtype())){
            oRng.select();
            oRng.text = document.frmSearch.strReplace.value;
            wordcount++;
        }
        if (wordcount == 0) msg = "<%=FrameworkHelper.getMsg(request,"p.e.can_not_find",new String[0])%>"
        else msg = Loc.ps("<%=FrameworkHelper.getMsg(request,"p.e.replace_times",new String[0])%>", wordcount);
        top.DFish.alert(msg);
    }
}
</script>

</head>
<body scroll=no >
<form name="frmSearch" method="post">
<table cellspacing="0" cellpadding="5" border="0" height=130 class=df-cn>
<tr><td valign="top" align="left" nowrap>
    <label for="strSearch"><%=FrameworkHelper.getMsg(request,"p.e.cont_to_search",new String[0])%></label><br/>
    <input type=text name=strSearch id="strSearch" style="width : 200px;" class=mx_text/><br/>
    <label for="strReplace"><%=FrameworkHelper.getMsg(request,"p.e.cont_to_replace",new String[0])%></label><br/>
    <input type=text name=strReplace id="strReplace" style="width : 200px;" class=mx_text/><br/>
    <input type=checkbox size=40 name=blnMatchCase id="blnMatchCase"/><label for="blnMatchCase"><%=FrameworkHelper.getMsg(request,"p.e.case_sensitive",new String[0])%></label><br/>
    <input type=checkbox size=40 name=blnMatchWord id="blnMatchWord"/><label for="blnMatchWord"><%=FrameworkHelper.getMsg(request,"p.e.full_text_match",new String[0])%></label>
</td>
<td rowspan="2" valign="top">
    <input type=button style="margin-top:15px;width:80px" name="btnFind" onclick="findtext();" value="<%=FrameworkHelper.getMsg(request,"p.e.find_next",new String[0])%>"/><br/>
    <input type=button style="margin-top:5px;width:80px" name="btnCancel" onclick="dialog.close();" value="<%=FrameworkHelper.getMsg(request,"p.e.close",new String[0])%>"/><br/>
    <input type=button style="margin-top:5px;width:80px" name="btnReplace" onclick="replacetext();" value="<%=FrameworkHelper.getMsg(request,"p.e.replace",new String[0])%>"/><br/>
    <input type=button style="margin-top:5px;width:80px" name="btnReplaceall" onclick="replacealltext();" value="<%=FrameworkHelper.getMsg(request,"p.e.replace_all",new String[0])%>"/><br/>
</td>
</tr>
</table>
</form>
</body>
</html>
