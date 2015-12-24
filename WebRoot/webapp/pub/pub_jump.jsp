<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%
response.setHeader( "Cache-Control", "no-store" );
response.setHeader( "Pragma", "no-cache" );
response.setDateHeader( "Expires", 0 );
%><%@ page contentType="text/html; charset=UTF-8"%><%
String tp  = ( String ) request.getAttribute( "type" );
String fbk = ( String ) request.getAttribute( "fbk" );
%>

<textarea id="txrTmp" style="display:none"><%=fbk%></textarea>

<% if ( tp.equals( "jsp" ) ) { %>

<script>
var a = document.getElementById( 'txrTmp' ).value;
eval( a );
</script>

<% } else if ( tp.equals( "view" ) ) { %>

<script>
var a = document.getElementById( 'txrTmp' ).value;
if ( a )
( parent.parent.VM( window.name ) || top.VM() )
	.cmd( parent.parent.$.x_doc( a ) );
</script>

<% } %>