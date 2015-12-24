/*
 * 名称: index.js
 * 描述: 用于首页模块
 * 作者: cmy
 */

$.css_import_link( "m/index/index.css" );

var index = {
	
	init : function() {
		// FIXME 目前放在门户中加载
//		pub.msg.init();
		pub.sso.keepConnect();
//		this.cancelBack();
//		pub.msg.show( [ {msgId:'pp', msgContent:'消息pp'}, {msgId:'qq', msgContent:'消息qq'}  ] );
	},
	
	// 取消回退键
//	cancelBack: function() {
//		$.e_attach( document, 'keydown', function() {
//			if ( event.keyCode === 8 && ! event.srcElement.isContentEditable )
//				$.e_stop( event );
//		} );
//	},

	wstab: {
	
		clk: function( btn ) {
			var opt  = Module.getOption( btn.x.id ),
				blurBtn = VM().find( 'main_oper' ).getSelected( -1 ),
				blurOpt = blurBtn && Module.getOption( blurBtn.x.id );
			
			if ( blurOpt && blurOpt.clk && blurOpt.id !== opt.id ) {
				Function( blurOpt.clk ).call( blurBtn );
			}
			if ( opt.clk ) {
				Function( opt.clk ).call( btn );
				if ( ! btn.__rm ) {
					btn.addEvent( 'remove', function() {
						var r = this.rootNode,
							p = this.parentNode;
						if ( this.isFocus() && r !== p && r.getSelected( -1 ).parentNode !== p ) {
							Function( opt.clk ).call( p );
						}
					} );
					btn.__rm = true;
				}	
			}
				
		},
		
	    // 填写第三个参数会打开新tab，第四个参数是新tab标题
		openfile: function( mod, url, tabId, title, icon, clz ) {
			var opt = typeof mod ==='string' ? Module.getOption( mod ) : mod.options,  
				fileId = opt.id + ( tabId ? '_' + tabId : '' ); 
			var btn = this.contains( fileId );
			if( btn ) {
				var vm = VM( '/' + fileId );
				if ( ! vm  && VM().find( fileId ))
					VM().find( fileId ).attr( 'src', url );
				btn.clk();
				btn.attr( "t", js.s_h2t(title) );
				if ( vm )
					vm.reload( url );
			} else {
				this.openfile_newtab( opt, tabId, title, url, icon, clz );
			}
			// 切换tab时删除star
		},
		
		/**
		 * log file open history
		 */
		saveRecentFiles:function(fileId,title,url,icon){
			  var iconPostfix=icon==null?'':icon.substring(icon.lastIndexOf('/')+1);
	    	if(icon &&( icon.indexOf('img/m/x32')==0||icon.indexOf('img/m/x48')==0)){
	    	     	icon=icon==null?'':('img/m/x16/'+iconPostfix);
	    	}
			VM().cmd({
				tagName:'ajax',
				src:'index.sp?act=saveRecentFiles&fileId='+fileId+
					'&fileText='+js.url_esc(title)+
					'&fileUrl='+js.url_esc(url)+
					'&icon='+js.url_esc(icon)});
		},
		
		// inner function. do not call when file exsits.
		openfile_newtab: function(opt, tabId, title, url, icon, clz){
			var oper = VM().find( 'main_oper' ),
				bt   = oper.attr( 'id' ),
				bw   = 'beforeend',
				ft   = oper.attr( 'film' ),
				fw   = 'beforeend',
				s    = '';
			if( icon && icon.indexOf( 'img/m/x' ) === 0 ) {
				icon = 'img/m/x32/3d/' + js.s_fr( icon, '/', true );
			}
			var t = js.s_h2t( js.s_h2t( title || opt.title ) ),
				fileId = opt.id + ( tabId ? '_' + tabId : '' ); 
			if ( Cfg.ws_style === 'folder' && tabId ) {
				var b = VM().button( opt.id + ':btn' ),
					f = VM().button( opt.id + ':makefolder' ) || ( b && ( b.folderNode || b.makeFolder() ) );
				if ( f ) {
					bt = f.x.id;
					bw = 'beforeend';
					ft = f[ f.length - 1 ].getTabPanel( 'id' );
					fw = 'afterend';
				} else {
					s = '<b t="' + opt.title + '" menu="index.wstab.menu(this)" id="' + opt.id + ':makefolder"' +
						( opt.xclk ? ' xclk="1"' : '' ) + ' ic="' + icon + '" folder="1">';
				}
			}
			var c = '<cmd><insert type="oper" target="' + bt + '" where="' + bw + '">' + s +
					'<b t="' + t + '" alt="' + t + '" menu="index.wstab.menu(this)" clk="index.wstab.clk(this)" id="' + fileId +
					':btn" xclk="1" ic="' + ( icon || opt.icon ) + '"' + ( clz ? ' clz="' + clz + '"' : '' ) + '/>' + ( s ? '</b>' : '' ) +
					'</insert>' +
					'<insert type="panel" target="' + ft + '" where="' + fw + '">' +
					'<fr type="src" cache="1" id="' + fileId + '" src="' + ( url || opt.src ).replace(/&/g,'&amp;') + '" class="m-wspage m-wsp-' + opt.id + '"/>' +
					'</insert></cmd>';
			VM().cmd( c );
			VM().button(fileId+':btn').clk();
		},
		contains : function( fileId ) {
			return VM().button( fileId + ":btn" );
		},
		graytip: (function() {
			var f = { 'my_task': '任务', 'TASK': '本任务', 'personal_messging': '消息', 'km_res': '文件库', 'km_ask': 'E问E答', 'app_meeting': '即时通讯', 'daily': '日程',
					  'app_work_report': '工作汇报', 'my_mail': '个人邮件', 'notice_manage': '通知公告' },
				g = {}, c;
			return function( a ) {
				var d = js.s_to( a.x.id, ':' );
				if ( c !== d ) {
					var q = VM().f( 'q' ),
						t = $( q.id + '-graytip' ),
						v = q.value;
					t.innerHTML = '搜索' + ( f[ d.indexOf( 'TASK_' ) === 0 ? 'TASK' : d ] || '' );
					if ( g[ d ] ) {
						q.value = g[ d ];
						$.class_add( t, 'none' );
					} else {
						if ( ! ( d in g ) )
							a.addEvent( 'remove', function() { if ( this.isFocus() ) c = null; delete g[ d ] } );
						q.value = '';
						$.class_del( t, 'none' );
					}
					g[ c || d ] = v;
				}
				c = d;
			}
		})(),
		
		menu: function( a ) {
			var m = new Fn.Menu( a );
			if ( a.x.xclk == 1 )
				m.add( { t : '关闭', bold : 1, clk : 'this.getOwner().xclk()' } );
			m.add( { t : '关闭其他', clk : 'this.getOwner().closeOther()' } );
			//m.addsplit();
			//m.add( { t : '收藏', clk : 'M.addFavor(index.wstab.favVal(this.getOwner()))' } );
			m.show();
		},
		
		favVal: function( a ) {
			// type -> 0：文件夹 1：附件 2：链接
			return { type : 2, id : a.x.id.replace( ':btn', '' ), icon : a.x.ic, name : a.x.t, src : a.getTabPanel( 'src' ) }
		},
		
		plusClick: function( a ) {
			$.class_add( a.$(), 'z-on' );
			var b = $.bcr( a.$() ),
				c = $.canrec();
			VM().exec( 'app_dialog', null, { magnet : a.$(), magtype: '32,41', onclose: function() { $.class_del( a.$(), 'z-on' ); } } );
		},

		list: function( a ) {
			function htm( a ) {
				var f = a.isFocus();
				return '<div class="index_app_open menu_row bd-low bd-noleft bd-noright' + ( f ? ' z-on' : '' ) + '" data-btn="' + a.x.id + '" onclick=index.wstab.focus(this)><div class=ln></div><span class="i">' + ( f ? '<i class=va-i></i><em class=i-point></em>' : '' ) + '</span><span class="t fix">' + a.x.t + '</span><span class="x">' + ( a.x.xclk ? $.htm.ico( '.i-x', { clk : 'index.wstab.close(this,event)' } ) : '' ) + '</span></div>'
			}
			var b = VM().find( 'main_oper' ),
				s = [];
			for ( var i = 0; i < b.length; i ++ ) {
				if ( b[ i ].x.folder ) {
					for ( var j = 0; j < b[ i ].length; j ++ )
						s.push( htm( b[ i ][ j ] ) ); ;
				} else {
					s.push( htm( b[ i ] ) );
				}
			}
			VM( a ).find( 'tabs' ).html( '<div class=index_app_open_wrap>' + s.join('') + '</div>' );
		},
		
		focus: function( a ) {
			var d = Q( a ).closest( '.index_app_open' );
			VM().button( d.data( 'btn' ) ).clk();
			DFish.close( a );
		},
		
		close: function( a, e ) {
			var d = Q( a ).closest( '.index_app_open' );
			VM().button( d.data( 'btn' ) ).xclk();
			if ( e )
				$.e_stop();
			Q( a ).closest( '.index_app_open' ).remove();
 		},
 		
 		collapse: function( a ) {
 			var colla = a.attr( 'ic' ) === '.index_ico_dn';
  			$.class_add( VM().find( 'layout' ).$(), 'index_top_collapse', colla );
  			$.class_add( VM().find( 'ly-top' ).$(), 'z-s', colla );
 			VM().find( 'root' ).reset( ( colla ? 80 : 40 ) + ',*,36' );
 			a.attr( 'ic', colla ? '.index_ico_up' : '.index_ico_dn' );
 			if ( colla ) {
 				portal.menuHeight = 80;
 				VM().button('collapseBtn').attr('alt', '收起');
 				VM().cmd('setPersonConfig', 'wstabCollapse', '0');
 			} else {
 				portal.menuHeight = 40;
 				VM().button('collapseBtn').attr('alt', '展开');
 				VM().cmd('setPersonConfig', 'wstabCollapse', '1');
 			}
 			// 刷新门户
 			portal.reloadPortal();
 		},
 		
 		// 刷新tab /@a -> 模块ID数组 [ 'm-main', 'task', ... ]
 		reload: function( a ) {
 			
 		}
				
	},

	skin: {
		pop : function( src ) {
			VM().cmd( { tagName : 'dialog', t : "皮肤设置", src : 'vm:|index.sp?act=styleWin',
				w : 645, h : 599, independent : true,
				on_close: function() {
					var v = this.vm(), c = Cfg.skin;
					DFish.skin( Cfg.skin, { theme : v.fv( 'theme' ), color : v.fv( 'color' ), bg : v.fv( 'bg' ) } );
					delete Cfg.skin.runTheme;
				} } );
		},
		preview: function( a, b ) {
			b = b.split( ',' );
			if ( b[ 0 ] !== a.fv( 'theme' ) )
				a.find( 'img_' + a.fv( 'theme' ) ).focus( false );
			var rl = DFish.skin( { theme : b[ 0 ], color : b[ 1 ], bg : b[ 2 ] }, { theme : a.fv( 'theme' ), color : a.fv( 'color' ), bg : a.fv( 'bg' ) } );
			a.fs( 'theme', b[ 0 ] );
			a.fs( 'color', b[ 1 ] );
			a.fs( 'bg',    b[ 2 ] );
			Cfg.skin.runTheme = b[ 0 ];
			if ( rl ) {
				delete M.task.leftWidth;
			}
		}
	}

}
	