﻿if(top!=window)top.location=location;var ua=navigator.userAgent;var ie=navigator.appName=='Microsoft Internet Explorer'?true:false;var ieVer=ie&&parseFloat(ua.substr(ua.indexOf('MSIE')+5));var IE6=ie&&ieVer>=6;var _IE6=IE6&&ieVer<7;var IE7=ie&&ieVer>=7;var trh=0;var vah=40;var mnh=0;var jserr=null;function ft(){var stdh=561+71;var ceh=document.body.clientHeight;var sch=screen.height;if(stdh>ceh){mnh=Math.min(stdh-ceh,vah);document.write('<style>.maindv,.maintd{height:'+(561-mnh)+'px;}.maintbl{top:'+(-138-mnh)+'px;}</style>');}var c=ceh-stdh;if(c>0){if(c%3)w('<style>BODY{background-position-y:'+(-(c%3))+'px;}</style>');if(screen.width<=1152){w('<div style=height:'+c+'px;></div>');}else{trh=Math.floor(c/2);w('<div style=height:'+(c-trh)+'px;></div>');}}};function sd(){if(trh){w('<div style=background-color:#1183cb;>'+png(trh,0,0,'treed',0,-387,10,859)+'</div>');}};function br(){if(!IE6){alert(Loc.base_ie6);}else if(_IE6){var servers=['MSXML2','Microsoft','MSXML','MSXML3'];for(var i=0;i<servers.length;i++){try{var o=new ActiveXObject(servers[i]+'.XMLHTTP');o=null;break;}catch(e){}}if(i==servers.length){alert(Loc.xmlhttp_ie6);jserr=Loc.xmlhttp_ie6;}}else if(IE7){try{var o=new XMLHttpRequest();o=null;}catch(e){alert(Loc.xmlhttp_ie7);jserr=Loc.xmlhttp_ie7;}}};function ch(f){if(ch._loading){return false;}if(!IE6){alert(Loc.base_ie6);return false;}else if(jserr){alert(jserr);return false;}else if(!f.userId.value){alert(Loc.f_id);f.userId.focus();return false;}else if(!f.passWord.value){alert(Loc.f_ps);f.passWord.focus();return false;}else if(f.checkword&&!f.checkword.value){alert(Loc.f_au);f.checkword.focus();return false;}ch._loading=true;setTimeout('ch._loading=false',20000);return true;};function png(h1,w2,h2,n,al,ml,mt,w1){return(w2&&h2)?'<div style='+(w1?('width:'+w1+'px;'):'')+'height:'+h1+'px;'+(al==0?'':('text-align:'+(al==1?'center':'right')))+'><div style='+(ml?('margin-left:'+ml+'px;'):'')+(mt?('margin-top:'+mt+'px;'):'')+'width:'+w2+'px;height:'+h2+'px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image,src=/img/m/login/'+n+'.png);></div></div>':'<div style=height:'+h1+'px;><img style="'+(ml?('margin-left:'+ml+'px;'):'')+(mt?('margin-top:'+mt+'px;'):'')+'" src=/img/m/login/'+n+'.gif></div>';};function w(s){document.write(s);};function wp(){var a=[];for(var i=0;i<arguments.length;i++){a.push(png.apply(null,arguments[i]));}w(a.join(''));};function ld(){if(!document.activeElement||!document.forms[0].contains(document.activeElement)){document.forms[0].userId.focus();}};br(); 