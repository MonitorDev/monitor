﻿Fn.fW('Calendar',{Const:function(x){if(x.type==='input')x.date=x.field.value;this.dt=x.date?(typeof x.date=='string'?js.d_ps(x.date,x.format):x.date):new Date;this.hi=x.format.indexOf('hh:ii')!=-1?js.d_f(this.dt,'hh:ii'):null;x.dt=new Date(this.dt);this.x=x;this.id=Fn.Calendar.g_id();Fn.Calendar._a[this.id]=this;},Helper:{_a:{},idc:0,g_id:function(){return 'calendar-'+(this.idc++)},clk:function(a,i){var b=a.id.split('_'),c=this._a[b[0]];if(c.hi){c.dt.setDate(js.s_fr(b[1],'-',true));this.sel(c.id,'h');return;}else if(c.x.type){if(c.x.type==='rl')c.x.fcs=b[1];this.set(c,js.d_f(js.d_ps(b[1]),c.x.format),a);return;}Fn.WeekCal.clk(a,i);},set:function(a,b,c){if(a.x.field)a.x.field.value=b;else if(c){Q(c).closest('tbody').find('td.'+a.x.fclz).removeClass(a.x.fclz);Q(c).closest('td').addClass(a.x.fclz);}if(a.x.clk){if(typeof a.x.clk=='string')Function(js.s_printf(a.x.clk,b)).call(c||a.x.poper);else a.x.clk.call(a.x.poper,b);}if(!Br.ie&&a.x.onchange)Function(js.s_printf(a.x.onchange,b)).call(c||a.x.poper);if(a.dg)a.dg.close();},sel:function(a,z){var b=this._a[a],e=event,y=z=='y',m=z=='m',h=z=='h',c=h?b.dt.getHours():(b.dt[y?'getFullYear':'getMonth']()+(m?1:0)),r={top:e?e.clientY-52:b.dg.x.top,left:e?e.clientX-40:b.dg.x.left,bottom:(e?e.clientY-52:b.dg.x.top)+100},s='<div id='+b.id+'-hr class="bd-mid bg-form" align=center onselectstart=return(false) onmousedown=event.cancelBubble=true onmouseout=if(!this.contains(event.toElement))$.rm(this) style=position:absolute;width:60px;top:'+r.top+'px;left:'+r.left+'px;z-index:+999;line-height:1.2;>';var d=$.db(s),ey=b.x.earliest?b.x.earliest.getFullYear():1900;at=' onmouseover=style.background="#ddd" onmouseout=style.background="" style=height:14px';s='<div'+at+'>-</div><div style=overflow:hidden;height:70px;>';if(y){var f=Math.max(ey,c-2),g=f+5;for(var i=f;i<g;i++){s+='<div'+at+(i==c?';font-weight:bold':'')+'>'+i+'</div>';}}else if(m){s+='<div style=height:168px>';for(var i=k=1,v;i<=12;i++){v=i;s+='<div'+at+(i==c?';font-weight:bold':'')+'>'+v+'</div>';if(v==c)k=i;}}else{s+='<div style=height:672px>';var t=30,l=1440/t,h=60/t;for(var i=j=k=0,v,c1=js.d_f(b.dt,'hh:ii').replace(/\:(?!30|00)\d{2}/,':00');i<l;i++){v=(j<10?'0'+j:j)+':'+(js.s_p0((i*t)%60));s+='<div'+at+(v==c1?';font-weight:bold':'')+'>'+v+'</div>';if((i+1)%h==0)j++;if(v==c1)k=i;}}d.innerHTML=s+(y?'':'</div>')+'</div><div'+at+'>+</div>';var e=d.children[1],f,t,p=0;function scr(){if(y){v+=p;if(v>=ey){$.html(e,'<div'+at+(c==v?';font-weight:bold':'')+'>'+v+'</div>',Fn.Dom.ins_pos[p]);e[p>0?'firstChild':'lastChild'].removeNode(true);}}else{e.scrollTop+=p*14;}t=setTimeout(scr,50);};if(!y)e.scrollTop=(k-(h?2:3))*14;d.lastChild.onmousedown=d.firstChild.onmousedown=function(){p=parseFloat(this.innerText+1);v=parseFloat(e[p<0?'firstChild':'lastChild'].innerText);scr();};d.lastChild.onmouseup=d.firstChild.onmouseup=function(){clearTimeout(t);};e.onclick=function(){clearTimeout(t);var f=event.srcElement.innerText;if(f){if(z==='h'){Fn.Calendar.hr2(a,f);}else{f=parseFloat(f);if(z==='y')b.dt.setFullYear(f);else b.dt.setMonth(f-1);b.jump();}}this.onclick=null;try{d.lastChild.onmousedown=d.firstChild.onmousedown=d.lastChild.onmouseup=d.firstChild.onmouseup=null;}catch(ex){}$.rm(d);c=d=e=null;}},mn2:function(a,b,f){var c=this._a[a],d=typeof b==='number'?js.d_add(c.dt,'m',b):js.d_ps(b+'-'+js.d_f(c.dt,'d h:i:s'));if(c.x.earliest&&d<c.x.earliest)return;c.dt=d;c.jump(f);},hr2:function(a,b){var c=this._a[a],d=b.split(':');c.dt.setHours(parseFloat(d[0]));c.dt.setMinutes(parseFloat(d[1]));this.set(c,js.d_f(c.dt,c.x.format));},today:function(a){var d=new Date();this.mn2(a,js.d_f(d,'yyyy-m'),function(){Fn.Calendar.clk($(this.id+'_'+js.d_f(d,'yyyy-mm-dd')));});}},Prototype:{df:'yyyy-mm-dd',pop:function(){var a=!/[ymd]/.test(this.x.format),b=a?new Fn.Div(60,100):new Fn.DivSet('rows=*',207,null,'class="bd-gray white box-shadow"','padding:5px;',12,12);this.div=a?b:b.add();this.jump();this.dg=new Fn.Dialog({pophide:true,poper:this.x.poper,slide:Cfg.dg_slide,w:b.wd,h:'*',magnet:this.x.field||this.x.poper},b).render();this.dg.addEventOnce('close',this.dispose,this);if(a)this.dg.addEventOnce('render',Function('Fn.Calendar.sel("'+this.id+'","h")'));return this;},jump:function(f){if(this.x.upsrc){Ajax.xml(js.s_printf(this.x.upsrc,js.d_f(this.dt,'yyyy-mm')),js.bind(function(x){this.x.val=$.xt(x,'.//css/@v');(this.x.field?this.div:this.x.div).data(this.html());if(f)f.call(this);},this));}else{(this.x.field?this.div:this.x.div).data(this.html());if(f)f.call(this);}},focus:function(a,b){if(!a||a===true){b=a;a=this.x.fcs;}else{var d=js.d_ps(a,this.df),e=this.df.replace(/-\w+$/,'');if(js.d_f(this.dt,e)!=js.d_f(d,e)){this.dt=d;this.jump();}}this.x.fcs=b===false?null:a;Q('td.'+this.x.fclz,$(this.id)).removeClass(this.x.fclz);if($(this.id+'_'+a))$.class_add($(this.id+'_'+a).parentNode,this.x.fclz,b===false);},click:function(a){if($(this.id+'_'+a)){$(this.id+'_'+a).onclick();}},html:function(){var s=[],b=this.x.pad,y=this.dt.getFullYear(),m=this.dt.getMonth(),d=new Date(y,m,1),days=new Date(this.dt.getFullYear(),this.dt.getMonth()+1,0).getDate(),pastDays=d.getDay(),lf=7-(days+pastDays)%7;if(b){d.setDate(1-pastDays);days+=lf+pastDays;}else if(pastDays>0)s.push('<td colspan='+pastDays+'></td>');var rs=1,nw=js.d_f(new Date,'yyyy-mm-dd'),cr=js.d_f(this.x.dt,'yyyy-mm-dd'),fc=this.x.fcs;for(var i=0;i<days;i++){var j=d.getDate();var dd=d.getFullYear()+'-'+js.s_p0((d.getMonth()+1))+'-'+js.s_p0(j);d.setDate(j+1);var v=this.x.val&&this.x.css[this.x.val.charAt(i)];var st=v?v[0]:('color:'+(dd==nw?'blue;font-weight:bold':'#000'));st=(v&&v[1]?' class="'+v[1]+'"':'')+(st?' style="'+st+'"':'');s.push('<td width=14.28%'+(fc&&fc==dd?' class="'+this.x.fclz+'"':'')+'><a href=javascript: on'+(this.hi?'mousedown':'click')+'=Fn.Calendar.clk(this'+(v&&v[2]?','+i:'')+') id='+this.id+'_'+dd+' hidefocus><span'+st+'>'+(j<10?'&nbsp;':'')+j+'</span></a></td>');if(b){if(i&&(i+1)%7==0){s.push('</tr>');if(i+1<days){s.push('<tr>');rs++;}}}else{if((i+1+pastDays)%7==0){s.push('</tr>');if(i+1<days){s.push('<tr>');rs++;}}}}var td='<a href=javascript: onclick=Fn.Calendar.today("'+this.id+'") style=color:blue>'+Loc.date.today+'</a> &nbsp;',ht=(this.div||this.x.div).i_ht-41;if(!b){if(lf>0&&lf<7){s.push('<td colspan='+lf+' align=center>');if(rs==6&&this.x.std)s.push(td);s.push('</td>');}}s.push('</tr></table>');if(this.x.std&&(b||rs<6)){s.push('<div align=center style=line-height:120%;>'+td+'</div>');ht-=18;}return '<table width=100% height=21 cellspacing=0 cellpadding=0 class="'+this.x.ttclz+'"><tr align=center><td width=24 class="'+this.x.ttclz+'"><img src='+Cfg.imgp+'playl.gif onmouseup=Fn.Calendar.mn2("'+this.id+'",-1) class=hand></td><td>'+Loc.ps(Loc.date.ym,'Fn.Calendar.sel("'+this.id+'","y")',this.dt.getFullYear(),'Fn.Calendar.sel("'+this.id+'","m")',this.dt.getMonth()+1)+'</td><td width=24><img src='+Cfg.imgp+'playr.gif onmouseup=Fn.Calendar.mn2("'+this.id+'",1) class=hand></td></tr></table>'+'<table width=100% height=21 style="margin-bottom:2px;border-bottom:1px solid #dcdcdc;line-height:100%"><tr align=center><td>'+Loc.date.calendar_title.join('</td><td>')+'</tr></table>'+'<table class=f-cal-num id='+this.id+' cellspacing=0 cellpadding=0 width=100%'+' style="margin-bottom:3px;line-height:100%"><tr>'+s.join('');},remove:function(){if(this.dg)this.dg.close();this.dispose();},dispose:function(){Fn.WeekCal.prototype.dispose.call(this);}}});Fn.fW('WeekCal',{Const:function(x){this.x=x;this.dt=x.date?(typeof x.date==='string'?js.d_ps(x.date,x.format):x.date):new Date;this.id=Fn.Calendar.g_id();Fn.Calendar._a[this.id]=this;},Extend:[Fn.Calendar],Helper:{clk:function(a,i){var b=a.id.split('_'),c=Fn.Calendar._a[b[0]];var s=c.x.clk;if(i!=null){s=c.x.val&&c.x.css[c.x.val.charAt(i)];if(s)s=s[2];}c.x.fcs=b[1];if($.class_any(a,'f-gray')){var d=js.d_ps(b[1]);if(c.x.earliest&&d<c.x.earliest)return;c.dt=d;c.x.fcs=b[1];c.jump();}else{Q('.'+c.x.fclz,$(c.id)).removeClass(c.x.fclz);a.parentNode.className=c.x.fclz;}if(s)Function(js.s_printf(s,b[1])).call(VM(c.x.id));},over:function(a){if(!a.title){var b=a.parentNode.id.split('_'),c=Fn.Calendar._a[b[0]],d=b[1],y=js.s_to(d,'-'),e=js.d_wk(new Date(y,0,1),c.x.cg,c.x.start);if(e[0]!=y){e=js.d_wk(new Date(y,0,8),c.x.cg,c.x.start);}var f=new Date(e[2].getTime()+(js.m(js.s_fr(d,'-'))-1)*7*js.d_D),g=js.d_wk(f,c.x.cg,c.x.start);a.title=js.d_f(g[2],'yyyy.mm.dd')+' - '+js.d_f(g[3],'yyyy.mm.dd');}},yr2:function(a,b,f){var c=Fn.Calendar._a[a],d=new Date(c.dt.getTime()),y=Math.max(1900,js.m(typeof b==='number'?c.dt.getFullYear()+b:b));d.setFullYear(y);if(c.x.earliest&&d<c.x.earliest)return;c.dt=d;c.jump(f);},today:function(a){var b=js.d_wk(new Date);this.yr2(a,String(b[0]),function(){Fn.WeekCal.clk($(this.id+'_'+b[0]+'-'+b[1]));});}},Prototype:{jump:function(f){if(this.x.upsrc)Ajax.xml(js.s_printf(this.x.upsrc,js.d_f(this.dt,'yyyy')),js.bind(function(x){this.x.val=$.xt(x,'.//css/@v');(this.x.field?this.div:this.x.div).data(this.html());if(f)f.call(this);},this));else{(this.x.field?this.div:this.x.div).data(this.html());if(f)f.call(this);}},html:function(){var s=[],b=this.x.pad,w=js.d_wk(this.dt,this.x.cg,this.x.start),y=w[0];var a=js.d_wk(new Date(y,11,31),this.x.cg,this.x.start);if(a[0]!=y){a=js.d_wk(new Date(y,11,31-7),this.x.cg,this.x.start);}var l=b?56:a[1],lf=b?0:56-l,fc=this.x.fcs&&this.x.fcs.replace(/\b0/g,'');for(var i=0;i<l;i++){var j=b?(i>a[1]?i-a[1]:i+1):i+1;if(b&&i>a[1]){y=w[0]+1;}var dd=y+'-'+j,v=this.x.val&&this.x.css[this.x.val.charAt(i)],st=v?v[0]:'';st=(v&&v[1]?' class="'+v[1]+'"':'')+(st?' style="'+st+'"':'');s.push('<td width=14.28%'+(fc&&fc==dd?' class="'+this.x.fclz+'"':'')+'><a href=javascript:; onclick=Fn.WeekCal.clk(this'+(v&&v[2]?','+i:'')+') id='+this.id+'_'+dd+'><span onmouseover=Fn.WeekCal.over(this) '+st+'>'+(j<10?'&nbsp;':'')+j+'</span></a></td>');if(i&&(i+1)%7==0){s.push('</tr>');if(i+1<l){s.push('<tr height=15 align=center>');}}}var td='<a href=javascript: onclick=Fn.WeekCal.today("'+this.id+'") style=color:blue>'+Loc.date.weeknow+'</a>&nbsp;',ht=(this.div||this.x.div).i_ht-41;if(!b){if(lf&&this.x.std)s.push('<td colspan='+lf+' align=center>'+td+'</td>');}s.push('</tr></table>');if(b&&this.x.std){s.push('<div align=center>'+td+'</div>');ht-=18;}return '<table width=100% height=17 cellspacing=0 cellpadding=0 class="'+this.x.ttclz+'" style=margin-bottom:3px;><tr align=center><td width=24 class="'+this.x.ttclz+'"><img src='+Cfg.imgp+'playl.gif onmouseup=Fn.WeekCal.yr2("'+this.id+'",-1) class=hand></td><td>'+Loc.ps(Loc.date.y,'Fn.Calendar.sel("'+this.id+'","y")',this.dt.getFullYear())+'</td><td width=24><img src='+Cfg.imgp+'playr.gif onmouseup=Fn.WeekCal.yr2("'+this.id+'",1) class=hand></td></tr></table>'+'<table id='+this.id+' cellspacing=0 cellpadding=0 width=100% height='+((this.div||this.x.div).i_ht-41)+' style="margin-bottom:3px;line-height:100%;"><tr height=15 align=center>'+s.join('');},dispose:function(){if(this._disposed)return;if(this.x.field){this.x.field.modal=false;}delete Fn.Calendar._a[this.id];$.rm(this.id+'-hr');js.dps(this.x,'dt','date','field','format');js.dps(this,'id','div','dg','dt','x');this._disposed=true;}}});Fn.fW('MonthCal',{Const:function(x){Fn.WeekCal.call(this,x);},Helper:{yr2:function(a,b,f){var c=Fn.Calendar._a[a],d=new Date(c.dt.getTime());d.setFullYear(js.m(b));if(c.x.earliest&&d<c.x.earliest)return;c.dt=d;c.jump(f);},today:function(a){var d=new Date();this.yr2(a,String(d.getFullYear()),function(){Fn.WeekCal.clk($(this.id+'_'+js.d_f(d,'yyyy-mm')));});}},Extend:[Fn.WeekCal],Prototype:{df:'yyyy-mm',html:function(){var s=[],y=this.dt.getFullYear(),fc=this.x.fcs;for(var i=0;i<12;i++){var j=i+1;var dd=y+'-'+js.s_p0(j);var v=this.x.val&&this.x.css[this.x.val.charAt(i)];var st=v?v[0]:'';st=(v&&v[1]?' class="'+v[1]+'"':'')+(st?' style="'+st+'"':'');s.push('<td width=16.66%'+(fc&&fc==dd?' class="'+this.x.fclz+'"':'')+'><a href=javascript: onclick=Fn.WeekCal.clk(this'+(v&&v[2]?','+i:'')+') id='+this.id+'_'+dd+' hidefocus><span'+st+'>'+Loc.monthname[i]+'</span></a></td>');if(i&&(i+1)%4==0){s.push('</tr>');if(i+1<12){s.push('<tr height=15 align=center>');}}}var td='<a href=javascript: onclick=Fn.MonthCal.today("'+this.id+'") style=color:blue>'+Loc.date.monthnow+'</a>&nbsp;',ht=(this.div||this.x.div).i_ht-41;s.push('</tr></table>');if(this.x.std){s.push('<div align=center>'+td+'</div>');ht-=18;}return '<table width=100% height=17 cellspacing=0 cellpadding=0 class="'+this.x.ttclz+'" style=margin-bottom:3px;><tr align=center><td width=24 class="'+this.x.ttclz+'"><img src='+Cfg.imgp+'playl.gif onmouseup=Fn.WeekCal.yr2("'+this.id+'",-1) class=hand></td><td>'+Loc.ps(Loc.date.y,'Fn.Calendar.sel("'+this.id+'","y")',this.dt.getFullYear())+'</td><td width=24><img src='+Cfg.imgp+'playr.gif onmouseup=Fn.WeekCal.yr2("'+this.id+'",1) class=hand></td></tr></table>'+'<table id='+this.id+' cellspacing=0 cellpadding=0 width=100% height=130'+' style=margin-bottom:3px;><tr height=15 align=center>'+s.join('');}}});Fn.fW('YearCal',{Const:function(x){Fn.WeekCal.call(this,x);},Helper:{yr2:function(a,b,f){var c=Fn.Calendar._a[a],d=new Date(c.dt.getTime());d.setFullYear(js.m(b));if(c.x.earliest&&d<c.x.earliest)return;c.dt=d;c.jump(f);},today:function(a){var y=new Date().getFullYear();this.yr2(a,String(y),function(){Fn.WeekCal.clk($(this.id+'_'+y));});}},Extend:[Fn.WeekCal],Prototype:{df:'yyyy',focus:function(a,b){if(!a||a===true){b=a;a=this.x.fcs;}else{var y=this.dt.getFullYear();y=y-(y%10)-1;if(a<y||a>y+9){this.dt.setFullYear(a);this.jump();}}this.x.fcs=b===false?null:a;Q('.'+this.x.fclz,$(this.id)).removeClass(this.x.fclz);if($(this.id+'_'+a))$.class_add($(this.id+'_'+a).parentNode,this.x.fclz,b===false);},html:function(){var s=[],y=this.dt.getFullYear();y=y-(y%10)-1;for(var i=0;i<12;i++){var d=y+i,v=this.x.val&&this.x.css[this.x.val.charAt(i)],st=v?v[0]:'';st=(v&&v[1]?' class="'+v[1]+'"':'')+(st?' style="'+st+'"':'');s.push('<td width=16.66%'+(this.x.fcs==d?' class="'+this.x.fclz+'"':'')+'><a href=javascript:'+(i===0||i===11?' class=f-gray':'')+' onclick=Fn.WeekCal.clk(this'+(v&&v[2]?','+i:'')+') id='+this.id+'_'+d+' hidefocus><span'+st+'>'+d+'</span></a></td>');if(i&&(i+1)%4==0){s.push('</tr>');if(i+1<12){s.push('<tr height=15 align=center>');}}}var td='<a href=javascript: onclick=Fn.YearCal.today("'+this.id+'") style=color:blue>'+Loc.date.yearnow+'</a>&nbsp;',ht=(this.div||this.x.div).i_ht-41;s.push('</tr></table>');if(this.x.std){s.push('<div align=center>'+td+'</div>');ht-=18;}return '<table width=100% height=17 cellspacing=0 cellpadding=0 class="'+this.x.ttclz+'" style=margin-bottom:3px;><tr align=center><td width=24 class="'+this.x.ttclz+'"><img src='+Cfg.imgp+'playl.gif onmouseup=Fn.WeekCal.yr2("'+this.id+'",-10) class=hand></td><td>'+Loc.ps(Loc.date.y,'Fn.Calendar.sel("'+this.id+'","y")',(y+1)+' - '+(y+10))+'</td><td width=24><img src='+Cfg.imgp+'playr.gif onmouseup=Fn.WeekCal.yr2("'+this.id+'",10) class=hand></td></tr></table>'+'<table id='+this.id+' cellspacing=0 cellpadding=0 width=100% height=130'+' style=margin-bottom:3px;><tr height=15 align=center>'+s.join('');}}}); 