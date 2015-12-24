/**
 *	top
 *	<p> Title: m.js
 *	<p> Copyright: RJSOFT (c) 2006 - 2008 iTask Team </p>
 *	<p> Company: RJSOFT </p>
 *	@  author Chen MingYuan
 *	@ version 5.5
 *  @ created 2007-4-16
 *  @modified 2007-6-15
 */

$.css_import_link('m/pub/ums.css');
function img_sf(simg){
	var db=document.body;
	var h_x = document.getElementById('hx');
	var h_y = document.getElementById('hy');
	var h_h = document.getElementById('hh');
	var h_w = document.getElementById('hw');
	var h_cut_w = document.getElementById('h_cut_w');
	var img1=document.getElementById('img_1');
	var img2=document.getElementById('img_2');
	var img3=document.getElementById('img_3');
	var bl=[60/120,60/40];
	var div=document.getElementById('img_b3');
	var d_t=document.getElementById('img_b1');
	var d_y=document.getElementById('img_b4');
	var d_x=document.getElementById('img_b5');
	var d_l=document.getElementById('img_b2');
	var self={};
	var iwh=Math.min(simg.height,simg.width);
	var sf=document.getElementById('img_dsf');
	hw();
	yd(div.offsetTop,div.offsetLeft);
	div.onmousedown=function(e){
		var e=e||event;
		self.x=e.clientX-this.offsetLeft;
		self.y=e.clientY+document.documentElement.scrollTop-this.offsetTop;
		try{e.preventDefault();}catch(o){e.returnValue = false;}
		document.onmousemove=function(e){
			var e=e||event;
			var t=e.clientY+document.documentElement.scrollTop-self.y;
			var l=e.clientX-self.x;
			t=Math.max(t,0);
			l=Math.max(l,0);
			t=Math.min(t,simg.height-div.offsetHeight);
			l=Math.min(l,simg.width-div.offsetWidth);
			yd(t,l);
		}
	}
	sf.onmousedown=div.onmouseup=function(){
		document.onmousemove='';
	}
	
	sf.onmousedown=function(e){
		var e=e||event;
		self.x=e.clientX-this.offsetLeft;
		self.y=e.clientY+document.documentElement.scrollTop-this.offsetTop;
		try{e.preventDefault();}catch(o){e.returnValue = false;}
		try{e.stopPropagation();}catch(o){e.cancelBubble = true;}
		document.onmousemove=function(e){
			var e=e||event;
			var t=e.clientY+document.documentElement.scrollTop-self.y;
			var l=e.clientX-self.x;
			l=Math.max(t,l);
			l=l>iwh?iwh:l;
			sff(l,l);
		}
	}
	
	function sff(t,l){	
		sf.style.top=t+'px';
		sf.style.left=l+'px';
		div.style.width=(l+10)+'px';
		div.style.height=(t+10)+'px';
		h_cut_w.value=l+10;
		yd(div.offsetTop,div.offsetLeft);
		var w=div.offsetWidth;
		bl=[w/120,w/40];
		hw();
		db.imgh=l+10;
	}
	
	function yd(t,l){
		d_t.style.height=t+'px';
		d_x.style.height=420-t-div.offsetHeight+'px';
		d_l.style.top=d_y.style.top=div.style.top=t+'px';
		d_l.style.width=div.style.left=l+'px';
		d_y.style.width=420-l-div.offsetWidth+'px';
		d_l.style.height=d_y.style.height=div.offsetHeight+'px';
		img1.style.top=-t/bl[0]+'px';
		img1.style.left=-l/bl[0]+'px';
		img2.style.top=-t/bl[1]+'px';
		img2.style.left=-l/bl[1]+'px';
		db.xy=[t,l];
		h_x.value = t;
		h_y.value = l;
	}
	
	function hw(){
		h_h.value = simg.height;
		h_w.value = simg.width;
		img1.height=simg.height/bl[0];
		img1.width=simg.width/bl[0];
		img2.height=simg.height/bl[1];
		img2.width=simg.width/bl[1];
	}
}
