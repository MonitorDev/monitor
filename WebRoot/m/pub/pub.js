/*
 * 名称: index.js
 * 描述: 公共方法
 * 作者: cmy
**/
$.css_import_link('m/pub/pub.css'); 

var pub = {

	/*
	   
	   范例：
		 打开任务首页: pub.open( "task" );
		 打开任务详细: pub.open( "task",  'view',   { taskId : '**', title : '**' } );
		 
	  返回:
	    当发生错误时要返回一个json，格式: { error : true, msg : '错误描述' }
	    
	 **/
	open: function( mod, act, data ) {
		var m = Pub.get( mod );
		if ( m ) {
			m.open( act, data );
		} else {
			index.wstab.openfile( mod );
		}
	},
	
	// 范例  <a href="javascript:" onclick="pub.close(this)">关闭</a>
	close: function( obj ) {
		var dialog = DFish.g_dialog( obj );
		if ( dialog ) {
			dialog.close();			
		} else {
			var p = typeof obj === 'string' ? obj : VM( obj ).path.split( '/' )[ 1 ];
			if ( p = VM().button( p + ':btn' ) )
				p.xclk();
		}
	},
		
	
	sso : {
		keepConnect: function(){
			var img = new Image();
			img.src = Cfg.intf.ssoPath+ '/dot.png?v='+(new Date()).getTime();
			setTimeout(arguments.callee, 300000);
		}
	},
	
	/* ! 把range内的图片变成缩略图
	 * @range: htmlElement
	 * @width: 图片最大宽度。超出这个值会等比缩小
	 * @fn: boolean|String|function 可选，点击缩略图执行的动作
	 
	 *  范例1: 点击缩略图，使用引擎默认的预览效果
	 		pub.thumbnail( htmlPanel.$(), 500 );
	 		
	 *  范例2: 点击缩略图，在新窗口打开预览图片
	 		pub.thumbnail( htmlPanel.$(), 500, "imgpreview.jsp?src=$0&title=$1" );
	 		
	 *  范例3: 点击缩略图，执行一个函数
	 		pub.thumbnail( htmlPanel.$(), 500, function(){ alert( this.src ) } );
	 
	 *  范例4: 只做缩略处理，不附加点击效果
	 		pub.thumbnail( htmlPanel.$(), 500, false );
	 		
	 */
	thumbnail: function( range, width, fn ) {
		DFish.thumbnail( range, width, fn );
	},
	
	user : {
	    onlinemenu : function( a ) {
		    var d = typeof a == 'object' ? js.s_fr( a.id, '-', 1 ) : a,
		    	e = this.ckUic(),
		    	m = new Fn.Menu( a );
		    m.add( { t: '发送消息', clk : 'pub.msg.pop("' + d + '")' } );
		    m.addsplit();
		    m.add( { t: '查看资料', clk : 'pub.user.pop("' + d + '")' } );
		    m.addsplit();
		    m.add( { t: '查看小图标', ic : ! e || e == 'small' ? 'img/m/x16/check.png' : '', clk : 'pub.user.setIco("small")' } );
		    m.add( { t: '查看大图标', ic : e == 'big' ? 'img/m/x16/check.png' : '', clk : 'pub.user.setIco("big")' } );
		    m.show();
	    },
	    menu : function( a ) {
		    var d = typeof a == 'object' ? js.s_fr( a.id, '-', 1 ) : a,
		    	m = new Fn.Menu( a );
		    m.add( { t: '查看资料', clk : 'pub.user.pop("' + d + '")' } );
		    m.addsplit();
		    m.add( { t: '发送消息', clk : 'pub.msg.pop("' + d + '")' } );
		    //m.addsplit();
		    //m.add( { t: '归类此人消息', clk : 'VM(\'/\').cmd({tagName:\'ajax\',src:\'portalIndex.sp?act=classifyMsg&classifyType=0&msgSender='+d+'\'});' } );
		    //m.add( { t: '屏蔽此人消息', clk : 'VM(\'/\').cmd({tagName:\'ajax\',src:\'msgCenter.sp?act=shieldMsg&shieldType=0&msgSender='+d+'\'});' } );
		    m.show();
	    },
	    setIco : function( a ) {
	    	var b = this.ckUic();
	    	this.ckUic( a );
	   		pub.msg.refreshLinkman();
	    },
	    
	    pop : function( a ) {
	    	if ( a === 'SYSTEM' ) {
	    		DFish.alert('该消息由系统自动产生',null,5);
	    		return;
	    	}
		    var d = typeof a === 'object' ? js.s_fr( a.id, '-', 1 ) : a;
		    var type='U';
		    var id=d;
		    if(d.indexOf('UD')>=0){
		    	type='U';
		    	id=d.substring(2);
		    }else if(d.indexOf('RC')>=0){
		    	type='RC';
		    	id=d.substring(2);
		    }else if(d.indexOf('RD')>=0){
		    	type='RD';
		    	id=d.substring(2);
		    }else {
		    	type='U';
		    	 id=d;
		    }
		    var typeName=type=='U'?'人员信息':'部门/角色';
		    var cmd=null;
		    if(type=='U'){
		    	cmd = '<dialog n="user'+id+'" w="600" h="400" tpl="f_std" pos="0" t="'+typeName+'" independent="1" src="vm:|orgAndUserPicker.sp?act=show&amp;type='+type+'&amp;id='+id+'"/>';
		    }else{
		    	cmd = '<dialog n="role'+id+'" w="600" h="400" tpl="f_std" pos="0" t="'+typeName+'" independent="1" src="vm:|orgAndUserPicker.sp?act=show&amp;type='+type+'&amp;id='+id+'"/>';
		    }
		    VM('/').cmd($.x_doc(cmd));
	    },
	    parser : function( data ,showAsGrid ) {
	    	if(showAsGrid){
	    		var r = [];
	    		r.push('<div class=clearfix>');
	    		for(var i=0;i<data.length;i++){
	    			var id_ = data[i][0];
	    			var name_ = data[i][1];
	    			r.push("<div class=\"left fix bg-form\" style='width:80px;text-align:center;margin-top:5px;margin-left:5px;padding:1px;' onclick=\"pub.user.pop('"+id_+"')\" oncontextmenu=\"pub.user.menu('" + id_ + "')\" >"+
	    			"<a href=\"javascript:;\">" +js.s_h2t(name_)+"</a>"+
	    			"</div>");
	    		}
	    		r.push('</div>');
	    		return r.join( '' );
	    	}else{
	    		var r = [];
	    		for(var i=0;i<data.length;i++){
	    			var id_ = data[i][0];
	    			var name_ = data[i][1];
	    			r.push( "<a href=\"javascript:;\" onclick=\"pub.user.pop('"+id_+"')\" oncontextmenu=pub.user.menu('" + id_ + "')><nobr>"+js.s_h2t(name_)+"</nobr></a>" );
	    		}
	    		return r.join( ', ' );
	    	}
	    },
	    photoParser : function(userId, photoSize, bOnLine, noAct, enterTalk) { // 用户编号, 头像大小, 在线状态(false:不在线, true:在线, 其他不显示) 
	        if (!photoSize){
	           photoSize = 40;
	        }
	        var showHtml = "";
	        if (userId) {
	        	var isUser = true;
	        	var imgHtml = "";
	        	imgHtml += "<img src=\"orgAndUserPicker.sp?act=downAttach";
	        	if (userId.indexOf("R") == 0) { // 角色部门
	        		isUser = false;
	        		// 显示角色的头像
	        		imgHtml += "&roleId=" + userId;
	        	} else { // 其他情况都当作人员来处理
	        		isUser = true;
	        		userId = this.getUserRealId(userId);
	        		// 显示人员的头像
	        		imgHtml += "&userId=" + userId;
	        	}
                imgHtml += "&h=" + photoSize + "&w=" + photoSize + "&type=1\"" + " style=\"width:" + photoSize + "px;height:" + photoSize + "px;\" ";
                var handClass = '';
                if (isUser && !noAct) { // 必须是人员才有点击动作,否则将无点击动作
                	if (!enterTalk) {
                		imgHtml += " onclick=\"pub.user.pop('" + userId + "');$.e_stop();\"";
                	} else {
                		imgHtml += " onclick=\"pub.msg.pop('" + userId + "');$.e_stop();\"";
                	}
                	handClass = 'hand';
                }
                imgHtml += " class='" + handClass + " icon_radius'/>";
                if (isUser && typeof bOnLine === 'boolean') { // 必须满足需要显示人员状态以及是人员时才显示状态
 		            showHtml = "<div class=\"ps-rel\" style=\"width:" + photoSize + "px\">" + imgHtml + 
		                  "<span class=\"olusr_status" + ( bOnLine ? " z-on" : "" ) + "\"></span></div>";
                } else { // null
                    showHtml = imgHtml;
                }
	        }
	        return showHtml;
	    },
	    nameParser : function(userId, userName, hasPhoto, color, showAlt) {
	        var showHtml = "";
	        if (userId) {
	            userId = this.getUserRealId(userId);
	            var nameClick = "pub.user.pop('" + userId + "');";
	            if (hasPhoto) {
	            	nameClick = "pub.msg.pop('" + userId + "');";
	            }
	            var showName = js.s_h2t(userName);
		        showHtml = "<a href='javascript:;' " + (color ? "style=\"color:" + color + "\" " : "") +
		        	"onclick=\"" + nameClick + "$.e_stop();\" oncontextmenu=\"pub.user.menu('" + userId + "');$.e_stop();\"";
		        showHtml += (showAlt ? " title='" + showName + "'" : "") + ">" + showName + "</a>";
	        }
	        return showHtml;
        },
        getUserRealId : function(userId){
            if (userId) {
	            if (userId.indexOf('UD') == 0){
	                userId = userId.substr(2);
	            }
            } else {
                userId = "";
            }
            return userId;
        },
        getUserPhotoSrc: function( userId, photoSize ) {
	        return "orgAndUserPicker.sp?act=downAttach&userId=" + this.getUserRealId(userId) + "&h=" + photoSize + "&w=" + photoSize + "&type=1";
        }
	   
	},
	msg : { // FIXME 需要将portal.js,pub.js中msg,msg.js这三个部分关于消息的部分重新整理整合
        all   : [],
        hfc   : 0,
        timer : null,
        type_unread : '0',
        type_todo : '1',
        reload_portlet : '0',
        reload_dialog : '0',
        init : function() {
            clearTimeout( this.timer );
            if ( Cfg.msgSrc )
                this.doRequest();
        },
        
        play_new_sound: function() {
        	if ( $( 'msg_new_mp3' ) ) {
        		try {
        			$( 'msg_new_mp3' ).dewstop();
        			$( 'msg_new_mp3' ).dewplay();
        		} catch( e ) {}
        	} else {
        		$.db( '<object type="application/x-shockwave-flash" id="msg_new_mp3" data="img/swf/dewplayer_mini.swf" width="0" height="0">' +
					'<param name="wmode" value="transparent">' +
					'<param name="flashvars" value="mp3=m/im/img/new.mp3&amp;javascript=on&amp;autostart=1&amp;autoreplay=0&amp;volume=100">' +
					'<param name="movie" value="img/swf/dewplayer_mini.swf" />' +
				'</object>' );
	       	}
        },
        
        url : function() {
        	var u  = js.url_set( Cfg.msgSrc, 'u_t', new Date().getTime() );
        	if ( im.onlineUser.requestValid() )
        		u = js.url_set( u, 'ol', 1 );
            return u;
        },
        doRequest : function() {
            this.hfc ++;
            Ajax.sendx( this.url(), this.read, this, null, false, this.error );
        },
        request : function() {
            clearTimeout( this.timer );
            this.timer = setTimeout( 'pub.msg.doRequest()', ( this.hfc < Cfg.msgH2LCounter - 1 ? Cfg.msgHighFrequency : Cfg.msgLowFrequency ) * 1000 );
        },
        
        read : function( a ) {
            if ( a === false ) {
            } else {
                VM().cmd( a );
            }
            this.request();
        },
        show : function( a ) {
        	var hasNew = a && a.length;
        	if ( hasNew ) {
        		this.all = a.concat( this.all );
        	}
            if ( this.all && this.all.length ) {
            	if ( hasNew ) {
            		this.reload_portlet = '1';
            		this.reload_dialog = '1';
            		this.play_new_sound();
                	this.popPrompt();
					this.docTitle( this.all.length );
            	}
                this.hfc = 0;
                this.refreshPortlet( this.all );
            }
        },
        docTitle: function( a ) {
			var t = document.title.replace( /^\(\d+\)/, '' );
			document.title = a ? '(' + a + ')' + t : t;        	
        },
        popDialog : function() {
        	var b  = msg.recentView,
            	e  = DFish.g_dialog( b );
        	if (!e || !e.isShow()) {
        		var btn = VM().button( b + ':btn' );
        		btn.clk();
//        		tools.dialog.pop(btn, 'recentMsg');
        	}
        	this.release();
        },
        // 最小化
        _prompt_min: false,
        promptZoom: function( a ) {
        	var b = this._prompt_min;
        	if ( b ) {
        		$.class_replace( a, 'i-dlg-max', 'i-dlg-ori' );
        	} else {
        		$.class_replace( a, 'i-dlg-ori', 'i-dlg-max' );
        	}
        	this._prompt_min = ! b;
         	this.fixPromptSize(); // 最小化调整到1条的高度
       		DFish.g_dialog( 'pub_msg' ).snap();
        },
        json2xml: function( json ) {
        	return {
        		getAttribute: function( a ) {
        			return json[ a ];
        		}
        	};
        },
        // pub.msg.show( [ {msgId:'pp', msgContent:'消息pp'}, {msgId:'qq', msgContent:'消息qq'}  ] );
        popPrompt: function() {
        	var w = 360, s = '',
        		a = new Fn.DivSet( 'rows=40,' + ( this._prompt_min ? 56 : '*' ), w, null, 'class=dlg-msg', '', 2, 2 ),
        		b = a.addset( 'cols=*,55', 'class=d-t' ),
        		c = a.add( null, null, null, null, 'miniscroll' );
        	b.add( 'onmousedown=Fn.Dialog.move(this)' ).data( '<div style=line-height:40px><span class=d-tit>您有新消息</span><span id=pub_msg_count class=d-count>' + this.all.length + '</span></div>' );
        	b.add( '', 'padding-right:10px;text-align:right', 10 ).data( $.htm.sic( '.i-dlg-' + ( this._prompt_min ? 'max' : 'ori' ) + ' .hand', 'margin-right:10px', null, 'pub.msg.promptZoom(this)' ) + $.htm.sic( '.tools_close .hand', null, null, 'DFish.close(this)' ) );
        	var nameColor = "#8c8c8c";
        	for ( var i = 0; i < this.all.length; i ++ ) {
        		var item = this.all[ i ];
        		var msgId = item.msgId;
        		var msgContent = item.msgContent;
        		var msgSender = item.msgSender;
        		var senderName = item.senderName;
        		var msgFrom = item.msgFrom;
        		var msgType = item.msgType;
        		var msgTime = item.msgTime;
        		var detailTime = item.detailTime;
        		var msgRead = item.msgRead;
        		var msgYear = item.msgYear;
        		var msgAction = item.msgAction;
        		var icon = item.icon;
        		var iconName = item.iconName;
        		msgAction = js.s_t2h(msgAction);
        		
        		var showHtml = "";
        		var msgClickAction = msg.getMsgClickAction(msgId, msgSender, msgFrom, msgAction, msgType, msgRead, msgYear);
        		showHtml += "<div class='left' style='padding:10px 10px 10px 5px;'><img src='" + icon + "' title='" + iconName + "'/></div>";
        		showHtml += "<div>";
                showHtml += "<div class='nobr fix bold' style=\"padding-top:7px;line-height:150%;\">";
                showHtml += "<a href=javascript:; onclick=\"" + js.s_attr(msgClickAction) + "\"";
                showHtml += " title='" + msgContent + "'>" + msgContent + "</a>";
                showHtml += "</div>";
                showHtml += "<div style='font-size:12px;color:#8c8c8c;line-height:150%'>";
                var nameHtml = pub.user.nameParser(msgSender, senderName, false, nameColor);
                showHtml += "<div class='left fix' style='width:50px;'>" + nameHtml + "</div>";
                showHtml += "<div style='padding:0 12px;' class='left' title='" + detailTime + "'>" + msgTime + "</div>";
                showHtml += "<div class='pub_over_vis right' style=\"width:60px\">" + portal.msg.operParser(null, this.json2xml(item)) + "</div>";
                showHtml += "</div>";
                showHtml += "</div>";
        		
        		s += '<div class="pub_over_wrap d-item" id=pub_msg_' + msgId + '>' + showHtml + '</div>';
        	}
        	c.data( '<div id=pub_msg_list class=d-list>' + s + '</div>' );
        	var d = DFish.g_dialog( 'pub_msg' );
        	if ( d )
        		d.doClose();
 	        d = new Fn.Dialog( { n: 'pub_msg', tpl: 'none', w: w, h: '*', pos: 5, magnet: VM().find('main_film').$(), args: c, independentChild: 1,
				onclose: function() {
	        		pub.msg.all.length = 0; // 关闭则清空
	        		pub.msg.docTitle( 0 );
	        	} }, a );
			d.render();
			pub.msg.fixPromptSize();
        },
        //调整消息框大小。当消息框里数据被增/删后要调用这个方法
		fixPromptSize: function( ht ) {
			var d = DFish.g_dialog( 'pub_msg' );
			if ( d ) {
				if ( this._prompt_min ) {
					ht = 56;
				} else {
					ht = $.canrec().height - 42 - 35;
					ht = $( 'pub_msg_list' ).offsetHeight > ht ? ht : -1;
				}
				d.args().resize( null, ht );
			}
		},
		//删除一条消息框里的数据
		removePromptItem: function( msgId ) {
			var o = $( 'pub_msg_' + msgId );
			if ( o ) {
				for ( var i = 0; i < this.all.length; i ++ ) {
					if ( this.all[ i ].msgId === msgId ) {
						this.all.splice( i, 1 );
						break;
					}
				}
				$.slideOut( o, function() {
					var l = pub.msg.all.length;
					if ( l === 0 ) {
						DFish.close( 'pub_msg' );
					} else {
						$( 'pub_msg_count' ).innerText = l;
						pub.msg.fixPromptSize();
					}
					pub.msg.docTitle( l );
				} );
			}
		},
        release : function() {
//            var vm = VM( msg.recentViewPath ),
//                pa = vm && vm.find( msg.pLatest );
//            if ( pa ) {
//                var o = $.before( vm.find( 'typeFilm' ).$(), '<div class=im_newcome_loading></div>' );
//            }
            if (this.reload_dialog != '1' && this.reload_portlet != '1') {
            	this.all.length = 0;
        	}
        },
        error : function( a ) {
            if ( a.status > 400 ) {
               // if ( Cfg.debug )
               //     alert( 'msg error: ' + a.status );
            }
            pub.msg.request();
        },
        setHighFrequency : function() {
            this.hfc = 0;
            this.request();
        },
        setLowFrequency : function() {
            this.hfc = Cfg.msgH2LCounter;
            this.request();
        },
        clear : function( a ) {
            delete this.all[ a ];
            this.cnt --;
        },
        pop : function( receiver ) { //
            var isEnterSession = false;
            var talkUser = "";
            if ( receiver == 'SYSTEM' ){
                pub.user.pop( receiver );
            } else {
                if (receiver){
                    isEnterSession = true;
                    try {
                        var receiverArray = receiver.split(',');
                        if (receiverArray.length > 1) {
                            isEnterSession = false;
                        } else if (receiverArray[0].indexOf('R') >= 0){
                            isEnterSession = false;
                        } else {
                            talkUser = pub.user.getUserRealId(receiverArray[0]); 
                        }
                    } catch(e){
                        isEnterSession = false;
                    }
                }
                if (isEnterSession) { 
                    // 进入聊天框
                    im.chat.enterTalk(talkUser);
                } else { // 其他情况,使用旧版对话框
                    var dialogSrc = "vm:|msg.sp?act=editMsg&msgId=NEWID";
                    if (receiver) {
                        var msgReceiver = typeof receiver == 'object' ? js.s_fr( receiver.id, '-', 1 ) : receiver;
                        if(msgReceiver.indexOf('UD')<0&& msgReceiver.indexOf('RC')<0&& msgReceiver.indexOf('RD')<0) {
                            msgReceiver="UD"+msgReceiver;
                        }
                        dialogSrc += "&msgReceiver=" + msgReceiver;
                    }
                    VM().cmd( { tagName : "dialog", t : "发送消息", id : "editMsg", n : "msg", w : "620", h : "420",
                            src : dialogSrc } );            
                }
            }
        },
        refreshPortlet : function(msgList) {// 刷新门户待阅或待办  消息状态为0时刷新待阅,消息状态为1时刷新待办
        	if (this.reload_portlet != '1') {
        		return;
        	}
        	// FIXME 理论上需要将这个函数放在portal.js或msg.js 2014-10-5 By YLM
            var reloadUnRead = false;
            var reloadRead = false;
            if (msgList && msgList.length > 0) {
                for (var i=0; i < msgList.length; i++) {
                    var msg = msgList[i];
                    if(!reloadUnRead && msg.msgStatus == 0) {// 0:待阅
                        reloadUnRead = true;
                    }
                    if(!reloadRead && msg.msgStatus == 3) { // 3:待办
                        reloadRead = true;
                    }
                    if (reloadUnRead && reloadRead) {
                        break;
                    }
                }
            }
            var complete = function(){pub.msg.release();};
            if (reloadUnRead) {
            	// 聚焦到待阅第一页
                portal.refreshSinglePortlet(null, portal.msg.code_unread, 1, '0', complete);  // 刷新待阅
            }
            if (reloadRead) {
            	// 聚焦到待办第一页
                portal.refreshSinglePortlet(null, portal.msg.code_todo, 1, '0', complete);  // 刷新待办
            }
            this.release();
        },
        refreshRecentMsg : function(msgId, reload) { // 刷新最新消息
        	if (!reload && this.reload_dialog != '1') {
        		return;
        	}
            var vm = VM(msg.recentViewPath);
            if (vm) {
                if (msgId) {
                	var latestGridId = msg.pLatest;
            		var latestGrid = vm.find(latestGridId);
            		var row = latestGrid.getRowElement( { msgId : msgId } );
            		if (row) {
            			latestGrid.deleteRow({ msgId : msgId });
            			var np = vm.fv('np_' + latestGridId);
            			msg.fixMessagePanel(vm, latestGridId, np);
            		}
                	var historyGridId = msg.pHistory;
            		var historyGrid = vm.find(historyGridId);
            		var rowElement = historyGrid.getRowElement({ msgId : msgId });
            	    if (rowElement) {
            	    	var aElement = Q('a[class="bold"]', rowElement);
            	    	aElement.removeClass('bold');
            	    	aElement.css( { 'font-size' : '12px'} );
            	    }
                } else {
                	vm.cmd({tagName:'ajax',src:'msg.sp?act=reloadRecent',
                		complete: function() {
                			Q('.im_newcome_loading').remove();
                			vm.find('typeFilm').resize(null, vm.ext('film_ht'));
                		}
                	});
                }
            }
            // FIXME 在线交流的最新消息
        },
        changeStatus : function(msgId, msgType, msgYear, msgStatus, msgRead, cmdComplete) {
            if (!msgType && msgStatus != msg.status_delete) {
            	return;
            }
            if (msgId) {
                if (!msgStatus) {
                    msgStatus = "";
                }
                if (!msgYear) {
                    msgYear = "";
                }
                if (!cmdComplete) {
                	cmdComplete = function(){};
                }
                VM().cmd({tagName:'ajax',src:'msg.sp?act=changeMsgStatus&msgId='+msgId+'&msgType='+msgType+'&msgYear='+msgYear+'&msgStatus='+msgStatus, sync:'1', complete : cmdComplete});
            }
            if (msg.status_delete == msgStatus) {
            	if (msgType == this.type_unread) {
	                // 刷新待阅
	                portal.refreshSinglePortlet(null, portal.msg.code_unread);
	            } else if (msgType == this.type_todo) {
	                // 刷新待办
	                portal.refreshSinglePortlet(null, portal.msg.code_todo);
	            }
            	var vm = VM(msg.recentViewPath);
            	if (vm) {
            		// 修复最新消息
            		if (msgRead == '0') {
            			var latestGridId = msg.pLatest;
            			var latestNp = vm.fv('np_' + latestGridId);
            			vm.find(latestGridId).deleteRow({ msgId : msgId });
            			msg.fixMessagePanel(vm, latestGridId, latestNp);
            			
            		}
            		var historyGridId = msg.pHistory;
            		var historyNp = vm.fv('np_' + historyGridId);
            		var pHistory = vm.find(historyGridId);
            		if (pHistory.getRowElement({ msgId : msgId })) { // 这行数据存在的时候
            			pHistory.deleteRow({ msgId : msgId });
            			msg.fixMessagePanel(vm, historyGridId, historyNp);
            		}
            	}
            	pub.msg.removePromptItem( msgId );
            } else {
            	this.onStatusChange(msgType);
            }
        },
        markMessageRead : function(msgId, msgType, msgYear) {
        	if (msgId && msgType) {
                if (!msgYear) {
                    msgYear = "";
                }
                VM().cmd({tagName:'ajax',src:'msg.sp?act=markMessageRead&msgId=' + msgId + '&msgType=' + msgType + '&msgYear=' + msgYear, sync:'1'});
                this.onStatusChange(msgType, msgId);
            }
        },
        onStatusChange : function(msgType, msgId) { // 消息改变时刷新门户面板
            if (msgType) {
	            if (msgType == this.type_unread) {
	                // 刷新待阅
	                portal.refreshSinglePortlet(null, portal.msg.code_unread);
	            } else if (msgType == this.type_todo) {
	                // 刷新待办
	                portal.refreshSinglePortlet(null, portal.msg.code_todo);
	            }
	            // 刷新最新消息
	            this.refreshRecentMsg(msgId, true);
	            pub.msg.removePromptItem( msgId );
            }
        }
        
	}
};