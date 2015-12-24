/* ******************************************
 *	UploadSettings Object
 *	以SWFUpload的配置项为原型的类
 *	其他上传类均继承本类
 * ****************************************** */

(function() {

var uri   = Cfg.path + 'js/pl/swfupload/',
	files = '.xls.xlsx.doc.docx.odt.rtf.ppt.pptx.7z.rar.zip.htm.html.js.css.xml.pdf.psd.ai.fla.swf.txt.dir.wmf.ico.',
	vids  = '.avi.asf.wmv.avs.flv.mkv.mov.3gp.mp4.mpg.mpeg.dat.dsm.ogm.vob.rm.rmvb.ts.tp.ifo.nsv.',
	auds  = '.mp3.aac.wav.wma.cda.flac.m4a.mid.mka.mp2.mpa.mpc.ape.ofr.ogg.ra.wv.tta.ac3.dts.wv.shn.vqf.spc.nsf.adx.psf.minipsf.psf2.minipsf2.rsn.zst.',
	imgs  = '.jpg.jpeg.gif.png.bmp.tif.tiff.',
	getIco = function( url ) {
		var a = js.s_fr( url, '.', true ).toLowerCase(),
			b = '.' + a + '.';
		return files.indexOf( b ) > -1 ? '.i-file-' + a : imgs.indexOf( b ) > -1 ? '.i-file-image' : vids.indexOf( b ) > -1 ? '.i-file-video' : auds.indexOf( b ) > -1 ? '.i-file-audio' : '.i-file'
	};

// 把 "1MB" 转为字节
function fileByte( a ) {
	var b;
	a = js.m( a.replace( /[a-z]+$/i, function( $0 ) { b = $0.charAt( 0 ).toUpperCase(); return '' } ) );
	return a * ( b === 'K' ? 1024 : b === 'M' ? 1024 * 1024 : b === 'G' ? 1024 * 1024 * 1024 : b === 'T' ? 1024 * 1024 * 1024 * 1024 : 1 )
}

function readablizeBytes( bytes ) {
	if ( ! bytes ) bytes = 1;
	if ( isNaN( bytes ) )
		return bytes;
    var s = [ 'Bytes', 'KB', 'MB', 'GB', 'TB', 'PB' ];
    var e = Math.floor(Math.log(bytes)/Math.log(1024));
    return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];
}

/*
IE 9 and Opera 10.62 do not support this feature
*/
var XHRUpload = Fn.reg( {
	
	Const: function( settings ) {
		this.ipts  = 0;
		this.ques  = [];
		this.stats = { files_queued: 0, successful_uploads: 0 };
		if ( this.ownerPlugin )
			this.initSWFUpload( settings );
		this.df_vis = true;
	},
	
	Extend: [ SWFUpload ],
	
	Prototype: {
		// 重构SWFUpload方法
		getFlashHTML: function() {
			var i = this.ipts ++;
			return '<div><input class="ipt-file opc0" type=file id="' + this.id + ':' + i + '" onchange=PL.FileUpload._a["' + this.id + '"].fileSelected(' + i + ')' +
				( this.settings.file_upload_limit !== 1 ? ' multiple' : '' ) + ' accept="' + this.getAccept() + '"><object class=none></object></div>'
		},
		
		getQueueFile: function( i ) {
			return this.ques[ i ]
		},

		startUpload: function() {
			for ( var i = 0, ldr; i < this.loaders.length; i ++ ) {
				ldr = this.loaders[ i ];
				if ( ldr.loading )
					return;
				if ( ! ldr.loading && ! ldr.loaded )
					break;
			}
			if ( ldr ) {
				ldr.loading = true;
				var fd  = new FormData(),
					xhr = new XMLHttpRequest(),
					self = this;
				fd.append( "Filedata", ldr.file );
				xhr.upload.addEventListener( "progress", function( e ) {
					if ( e.lengthComputable )
						self.uploadProgress( ldr.file, e.loaded, e.total );
				}, false );
				xhr.addEventListener( "load", function( e ) {
					self.uploadSuccess( ldr.file, e.target.responseText );
					self.stats.successful_uploads ++;
					js.remove( this.ques, ldr.file );
					self.stats.files_queued --;
					self.uploadComplete( ldr.file );
				}, false );
				xhr.addEventListener( "error", function( e ) {
					self.uploadError( ldr.file, e.error );
				}, false );
				xhr.addEventListener("abort", function( e ) {
					self.cancelUpload( ldr.file );
				}, false );
				xhr.open( "POST", this.settings.upload_url );
				xhr.send( fd );
			}
		},
		
		getStats: function() {
			return this.stats
		},
		
		getAccept: function() {
			var t = this.settings.file_types;
			return t && t !== '*.*' ? t.replace( /;/g, ',' ).replace( /\*/g, '' ).toLowerCase() : ''
		},
						
		fileSelected: function( a ) {
			var b = $( this.id + ':' + a ).files, i = 0, l = b.length,
				c = fileByte( this.settings.file_size_limit ), d = 0, e = this.settings.file_upload_limit,
				t = this.getAccept();
			
			// 单张图片，再次上传则替换原图片
			if ( e === 1 && this.upType === 'image' ) {
				this.empty_loaders();
			}
			this.ques = [];
			for ( ; i < l; i ++ ) {
				if ( b[ i ].size > c ) {
					this.fileQueueError( b[ i ], SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT );
					break;
				} else if ( t && ! js.ids_inc( t, '.' + js.s_fr( b[ i ].name, '.', true ).toLowerCase() ) ) {
					this.fileQueueError( b[ i ], SWFUpload.QUEUE_ERROR.INVALID_FILETYPE );
					break;
				} else {
					if ( e && d + this.stats.files_queued + this.stats.successful_uploads >= e ) {
						this.fileQueueError( b[ i ], SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED );
						break;
					} else {
						b[ i ].id = BaseUpload.g_id();
						this.ques.push( b[ i ] );
						d ++;
					}
				}
			}
			
			this.stats.files_queued += d;
			$.prepend( $( this.id + ':cont' ), this.getFlashHTML() );
			this.fileDialogComplete( l, d );
		},

		setButtonDisabled: function( a ) {
			if ( a == null )
				a = true;
			$.class_add( $( this.id ), 'z-ds', a === false );
		}
		
	}
	
} );

 
var FlashUpload = Fn.reg( {
	
	Const: function( settings ) {
		if ( this.ownerPlugin )
			this.initSWFUpload( settings );
	},
	
	Extend: [ SWFUpload ]
	
} );	
	

var isXhr  = !! window.FormData,
	Upload = isXhr ? XHRUpload : FlashUpload,

BaseUpload = Fn.reg( {
	
	// @a -> plugin, b -> settings
	Const: function( a, b ) {
		
		if ( ! this.id )
			this.id = BaseUpload.g_id();
		FileUpload._a[ this.id ] = this;
		
		var settings = {
			button_width: 91,
			button_height: 25,
			button_text : '',
			button_text_style : '',
			button_text_top_padding: 0,
			button_text_left_padding: 0,
			button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor: SWFUpload.CURSOR.HAND,
			
			file_size_limit : "2MB",
			file_types : "*.*",
			file_types_description : "All Files",
			file_upload_limit : 0,
					
			// Flash Settings
			flash_url  : uri + "g/swfupload.swf",
			flash9_url : uri + "g/swfupload_fp9.swf"
		};
		if ( b ) {
			if ( a && ! b.button_placeholder_id )
				settings.button_placeholder_id = a.id + '-btn';
			if ( ! b.button_image_url )
				b.button_image_url = uri + ( b.button_type || 'image' ) + "_91x25.png"
			js.clone( settings, b );
		}
		if ( a ) {
			if ( a.bind )
				a.bind( { handler : this, getValue : this.df_get_value, setValue : this.df_set_value,
					resetValue : this.df_resetValue, disable : this.setDisabled, check : this.df_check, unload : this.destroy } );
			this.df_value = ( a.xt ? js.parseJSON( a.xt( 'value' ) ) : '' ) || [];
		}
		
		this.settings = b;
		this.customSettings = b.custom_settings || {};
		
		this.loaders = [];
		
		if ( a && this.df_render )
			this.df_render();
			
		Upload.call( this, settings );
		this.vis = true;
	},
	
	Extend : [ Upload, Fn.Event ],
	
	Helper : {
		_a   : {},
		idp  : 'dfishupload-',
		idc  : 0,
		g_id: function() { return this.idp + this.idc ++ },
		
		errmsg: function( errorCode, message ) {
			var ro  = SWFUpload.UPLOAD_ERROR,
				msg = message;
			if ( errorCode == ro.FILE_CANCELLED ) {
				msg = '取消上传';
			} else if ( errorCode == ro.UPLOAD_STOPPED ) {
				msg = '停止上传';
			} else if ( errorCode == ro.UPLOAD_LIMIT_EXCEEDED ) {
				msg = '文件大小超限';
			} else {
				msg = '上传失败';
			}
			return msg;
		}
	},
	
	Prototype : {
				
		vis  : false,

		// 重构 queueEvent 方法
		queueEvent: function( handlerName, argumentArray ) {
			if ( this.settings[ handlerName ] == null ) {
				this.settings[ handlerName ] = this[ handlerName ];
			}
			SWFUpload.prototype.queueEvent.apply( this, arguments );
		},
		
		swfupload_preload_handler: function() {
			if (!this.support.loading) {
				alert("You need the Flash Player 9.028 or above to use SWFUpload.");
				return false;
			}
		},
		swfupload_load_failed_handler: function() {
			alert("Something went wrong while loading SWFUpload. If this were a real application we'd clean up and then give you an alternative");
		},
		
		swfupload_loaded_handler: function() {
			this.df_add_stats_uploads( this.df_value.length );
			this.df_fireEvent( 'render' );
			this.df_vis = true;
		},
		
		/**  下面是dfish扩展方法  **/
		// 修改上传记录
		df_add_stats_uploads: function( i ) {
			var stats = this.getStats();
			if ( i && stats && stats.successful_uploads ) {
				stats.successful_uploads += i;
				this.setStats( stats );
			}
		},
		
		df_write_value: function( a ) {
			var v = js.toJSONString( a || this.df_value );
			$( this.ownerPlugin.id ).value = v;
		},
		
		df_upload: function() {
			this.df_uploading = true;
			this.startUpload();
		},

		df_check: function() {
			if ( this.customSettings.notnull == 1 && ! this.df_value.length )
				return Loc.ps( Loc.form.notnull, this.ownerPlugin.xt( '@t' ) );
			if ( this.df_uploading ) {
				return Loc.wait_upload;
			}
		},
		// @a -> event, b -> serverData, c -> local file
		df_fireCustom: function( a, b, c ) {
			var e = this.customSettings.handler && this.customSettings.handler[ a ];
			if ( e )
				( typeof e === 'function' ? e : Function( '$0,$1', b ? js.s_printx( e, b ) : e ) )
					.call( this, b, c );
		},
		setDisabled: function( a ) {
			if ( this.df_vis )
				this.setButtonDisabled( a );
			else
				this.df_addEvent( 'render', this.setButtonDisabled, this, [ a ], true );
		},
		setValue: function( a ) {
			this.df_value = a;
		},
		//获取尚未生成的loader
		getNewLoaders: function() {
			for ( var i = 0, r = [], o = this.loaders; i < o.length; i ++ ) {
				if ( ! o[ i ].loaded && ! $( o[ i ].id ) )
					r.push( o[ i ] )
			}
			return r
		}
		
	}
	
} );


/* ******************************************
 *	SWFUpload.ImageUpload Object
 *	图片上传
 * ****************************************** */

var ImageUpload = Fn.reg( {
	
	// @a -> plugin, b -> settings
	Const: function( a, b ) {
		var c = {
			file_types : "*.jpg;*.gif;*.png",
			file_types_description : "All Files"
		};
		BaseUpload.call( this, a, b ? js.clone( c, b ) : c );
	},
	
	Extend : [ BaseUpload ],
	
	Helper : {
		
	},
	
	Prototype : {	
		
		upType: 'image',

		file_queue_error_handler: function(file, errorCode, message) {
			var ro  = SWFUpload.QUEUE_ERROR,
				msg = message;
			if ( errorCode == ro.QUEUE_LIMIT_EXCEEDED ) {
				msg = '上传数量超过上限(最多' + Math.max( this.settings.file_upload_limit, this.settings.file_queue_limit ) + '个)';
			} else if ( errorCode == ro.INVALID_FILETYPE ) {
				msg = '无效的文件类型';
			} else if ( errorCode == ro.FILE_EXCEEDS_SIZE_LIMIT ) {
				msg = '文件大小超限(最大' + this.settings.file_size_limit + ')';
			}
			if ( this.customSettings.successful_uploads ) { // 恢复上传次数的记录
				this.df_add_stats_uploads( this.customSettings.successful_uploads );
				this.customSettings.successful_uploads = null;
			}
			DFish.alert( ( file ? file.name + '\n' : '' ) + '上传失败：' + msg );
		},
		
		file_dialog_complete_handler: function(numFilesSelected, numFilesQueued) {
			if (numFilesQueued > 0) {
				if ( this.settings.file_upload_limit == 1 ) {
					this.empty_loaders();
				}
				for ( var i = 0, s = []; i < numFilesQueued; i ++ ) {
					s.push( Loader.create( this, this.getQueueFile( i ) ) );
				}
				if ( this.customSettings.file_selected ) {
					js.call( this.customSettings.file_selected, this );
				} else {
					for ( var i = 0; i < numFilesQueued; i ++ ) {
						s[ i ] = s[ i ].html();
					}
					$.append( $( this.ownerPlugin.id + '-prg' ), s.join( '' ) );
				}
				this._df_value_beforeupload = this.df_value.slice();
				this.df_upload();
			}
		},
		
		upload_progress_handler: function(file, bytesLoaded, bytesTotal) {
			var ldr = Loader.getByFile( file );
			if ( ldr ) {
				var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
				ldr.setProgress( percent );
			}
		},
		
		upload_success_handler: function(file, serverData) {
			var ldr = Loader.getByFile( file ),
				r;
			try {
				eval( 'r=' + serverData );
				ldr.setSuccess( r );
				this.df_fireCustom( 'upload_success', r, file );
			} catch( e ) {
				this.uploadError( file, SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED );
			}
			
		},
		
		upload_complete_handler: function(file) {
			if (this.getStats().files_queued > 0) {
				this.df_upload();
			} else {
				// all completed
				this.df_uploading = false;
				this.df_fireCustom( 'upload_complete', false, file );
				if ( this.df_getEvent( 'change' ) && js.toJSONString( this._df_value_beforeupload ) !== js.toJSONString( this.df_value ) ) {
					this.df_fireEvent( 'change' );
				}
			}
		},
		
		upload_error_handler: function(file, errorCode, message) {
			var ldr = Loader.getByFile( file );
			if ( ldr ) ldr.setError( errorCode, message );
		},
		
		destroy: function() {
			for ( var i = 0; i < this.df_value.length; i ++ ) {
				var ldr = Loader.getByData( this.df_value[ i ], this );
				if ( ldr ) ldr.dispose();
			}
			this.df_value = this.ownerPlugin = null;
			SWFUpload.prototype.destroy.call( this );
		},
		
		/**  dfish 扩展函数开始  **/
		
		// 清空 loader
		empty_loaders: function() {
			var i = this.loaders.length;
			while ( i -- )
				this.loaders[ i ].remove();
		},
		
		html_loaders: function() {
			var s = [];
			if ( this.df_value.length ) {
				for ( var i = 0; i < this.df_value.length; i ++ ) {
					s.push( Loader.create( this, null, this.df_value[ i ] ).html() );
				}
			}
			return s.join( '' );
		},
		
		df_render_option: function() {
			$.html( $( this.ownerPlugin.id + '-prg' ), this.html_loaders() );
		},
		
		// @public  /a -> []
		df_add_value: function( a ) {
			var c = [];
			for ( var i = 0; i < a.length; i ++ ) {
				if ( js.index( this.df_value, a[ i ], 'id' ) < 0 ) c.push( a[ i ] );
			}
			this._add_value( c );
			var s = [];
			for ( var i = 0, d; i < c.length; i ++ ) {
				d = Loader.create( this, null, c[ i ] );
				s.push( d.html() );
			}
			$.append( $( this.ownerPlugin.id + '-prg' ), s.join( '' ) );
		},
		df_get_value: function() {
			return $( this.ownerPlugin.id ).value;
		},
		df_set_value: function( a ) {
			this.df_value = typeof a === 'string' ? js.parseJSON( a ) : a;
			this.df_render_option();
			this.df_write_value();
			this.df_fireEvent( 'change' );
		},
		df_resetValue: function() {
			this.df_set_value( this.ownerPlugin.getDefaultValue() );
		},
		_add_value: function( a ) {
			this.df_value.push.apply( this.df_value, a );
			this.df_write_value();
		},
		df_addEvent : function() {
			Fn.Event.prototype.addEvent.apply( this, arguments );
		},
		df_fireEvent: function( a, b ) {
			var o = this.df_getEvent( a ),
				b = b || this.df_get_value();
			if ( o )
				Function( '$0', o ).call( this.ownerPlugin, b );
			else
				Fn.Event.prototype.fireEvent.call( this, a, b );
		},
		df_getEvent: function( a ) {
			return 'xt' in this.ownerPlugin && this.ownerPlugin.xt( a )
		},
		df_upload_cancel: function( a ) {
			for ( var i = 0, e; i < a.length; i ++ ) {
				e = a[ i ];
				if ( e.filestatus < 0 && this.getFile( e.id ) ) {
					this.cancelUpload( e.id );
					this.df_fireCustom( 'upload_cancel', false, e );
				} else {
					this._df_remove_value( e );
				}
			}
		},
		df_remove_value: function( a ) {
			this.df_fireEvent( 'delete', a );
			for ( var i = 0, e; i < a.length; i ++ ) {
				e = a[ i ];
				if ( e.filestatus < 0 && this.getFile( e.id ) ) {
					this.cancelUpload( e.id );
					this.df_fireCustom( 'upload_cancel', false, e );
				} else {
					this._df_remove_value( e );
				}
				
			}
			this.df_write_value();
			if ( i > -1 ) {
				this.df_fireEvent( 'change' );
			}
		},
		_df_remove_value: function( a ) {
			for ( var j = this.df_value.length -1; j > -1; j -- )
				if ( a.id == this.df_value[ j ].id ) {
					this.df_value.splice( j, 1 );
					this.df_add_stats_uploads( -1 );
					var u = js.s_printx( this.customSettings.remove_url, a ),
						e = Loader.getByData( a, this ),
						o = this;
					if ( this.customSettings.remove_url ) {
						Ajax.send( u, function() { o.df_fireCustom( 'file_remove', a, e && e.file ) } );
					} else
						o.df_fireCustom( 'file_remove', a, e && e.file );
					break;
				}
		},
		
		df_render: function() {
			var t = Br.cmp ? 'div' : 'span';
				s = '<div class="pl-imageupload clearfix' + ( this.customSettings.readonly ? ' none' : '' ) + '"><div id="' + this.id + ':cont" class="btn-' + ( this.settings.button_type || 'image' ) + '"><em id=' + this.ownerPlugin.id + '-btn></em></div></div><input type=hidden id=' + this.ownerPlugin.id + 
					' name='+ this.ownerPlugin.name +'><' + t + ' id=' + this.ownerPlugin.id + '-prg>';
			$.html( this.ownerPlugin.$(), s + this.html_loaders() + '</' + t + '>' );
			this.df_write_value();
		}
	}
	
} );


/* ******************************************
 *	ImageUpload Object
 *	显示图片上传进度和结果
 * ****************************************** */
 
var Loader = Fn.reg( {
	
	Const: function( swfu, file, data ) {
		this.swfu = swfu;
		this.file = file || false;
		this.data = data || false;
		this.id   = Loader.g_id();
		if ( this.data )
			this.loaded = true;
		Loader.all[ this.id ] = this;
		swfu.loaders.push( this );
	},
	
	Helper : {
		all : {},
		idc : 0,
		g_id: function() {
			return 'swfu_ldr-' + ( this.idc ++ )
		},
		getByFile: function( file ) {
			for ( var i in this.all	)
				if ( this.all[ i ].file.id === file.id )
					return this.all[ i ];
		},
		getByData: function( data, swfu ) {
			for ( var i in this.all	)
				if ( this.all[ i ].data.id === data.id && this.all[ i ].swfu === swfu )
					return this.all[ i ];
		},
		create: function( swfu, file, data ) {
			return new ( swfu.upType === 'image' ? ImageLoader : FileLoader )( swfu, file, data )
		}
	},

	Prototype : {
		
		loading   : false,
		loaded    : false,
		_disposed : false,
		
		setSuccess: function( serverData ) {
			this.loading = false;
			this.loaded  = true;
			if ( serverData.ret == 0 ) {
				this.data = serverData;
				this.show( this.data );
				this.swfu._add_value( [ this.data ] );
			} else {
				DFish.alert( serverData.msg );
			}
		},
		
		render : function() {
			$.append( $( this.swfu.ownerPlugin.id + '-prg' ), this.html() );
			return this
		},
		
		remove: function() {
			if ( ! this._disposed && ! this.swfu.settings.button_disabled ) {
				$.rm( this.id );
				if ( this.data )
					this.swfu.df_remove_value( [ this.data ] );
				else
					this.swfu.df_upload_cancel( [ this.file ] );
				this.dispose();
			}
		},
		
		dispose: function() {
			if ( ! this._disposed ) {
				js.remove( this.swfu.loaders, this );
				this.swfu = this.file = null;
				delete Loader.all[ this.id ];
				this._disposed = true;
			}
		}
	}
} );
	

var ImageLoader = Fn.reg( {
	
	Const: function( swfu, file, data ) {
		Loader.apply( this, arguments );
	},
	
	Extend: [ Loader ],
	
	Prototype : {
		
		wh: 80,
		
		setProgress: function( percent ) {
			this.loading = true;
			var w = this.wh - 2;
			$( this.id + '-bar' ).style.width = Math.floor( Math.min( w * percent / 100, w ) ) + 'px';
		},
		
		setSuccess: function( serverData ) {
			this.loading = false;
			this.loaded  = true;
			if ( serverData.ret == 0 ) {
				this.data = serverData;
				this.show( this.data );
				this.swfu._add_value( [ this.data ] );
			} else {
				DFish.alert( serverData.msg );
			}
		},
		
		setError: function( errorCode, message ) {
			var msg = BaseUpload.errmsg( errorCode, message );
			$.html( $( this.id + '-msg' ), '<font color=#ff7921>' + msg + '</font>' );
		},

		show: function( serverData ) {
			$.html( $( this.id + '-img' ), this.html_img( serverData.thumbnailUrl ) );
		},

		render : function() {
			$.append( $( this.swfu.ownerPlugin.id + '-prg' ), this.html() );
			return this;
		},
		
		html_img: function( src ) {
			return '<i></i><img onload=$.imgfix(this,' + this.wh + ',' + this.wh + ');$.fadeIn(this,0) onerror=width=height=' + this.wh + ' src=' + src + '>';
		},
		
		html: function() {
			var n = ( this.data || this.file ).name,
				s = '<div id=' + this.id + ' class="left bd-tab bg-form clearfix ps-rel" style="width:' + this.wh + 'px;height:' + this.wh + 'px;padding:1px;margin:8px 8px 0 0;overflow:hidden"' + ( n ? ' title="' + n + '"' : '' ) + '><div id=' + this.id + '-img class=ic style=width:' + this.wh + 'px;height:' + this.wh + 'px;overflow:hidden>';
			if ( this.data ) {
				s += this.html_img( this.data.thumbnailUrl ) + '</div>';
			} else {
				s += '<div style=height:18px;></div><div class=nobr>' + ( n ? js.s_byte( n, 14, '..' ) : '---' ) +
					'</div><div id=' + this.id + '-msg><div class=bd-low><div id=' + this.id + '-bar class=bg-low style=width:0;height:12px;></div></div></div></div>';
			}
			s += '<div class="i-ul-x i-hv ps-abs hand" style="top:0;right:0" onclick=PL.Loader.all["' + this.id + '"].remove()></div></div>';
			return s;
		}
		
	}

} );


/* ******************************************
 *	FileUpload Object
 *	附件上传: 普通上传选择 + 网盘选择
 * ****************************************** */


var FileUpload = Fn.reg( {
	// @a -> plugin, b -> settings
	Const: function( a, b ) {
		if ( ! b.button_image_url )
			b.button_image_url = '';
		if ( ! b.button_html ) {
			this.upTitle = ( b.button_title || '添加附件' ) + ( b.file_size_limit ? '(最大' + b.file_size_limit + ')' : '' );
			b.button_width = b.button_image_url === '' ? js.s_px( this.upTitle ) + 30 : 91;
		}
		BaseUpload.apply( this, arguments );
	},
	
	Extend : [ ImageUpload ],
	
	Helper : {
		_a : {}
	},
	
	Prototype : {
		
		upType: 'file',
		
		df_render: function() {
			var w = this.settings.button_width || 24,
				h = this.settings.button_height || 24,
				d = this.settings.button_disabled || this.customSettings.readonly,
				s = '<div id=' + this.id + ' class="pl-fileupload clearfix"><div class="ps-rel left clearfix" style=width:' + w + 'px;height:' + h + 'px;margin-left:1px;margin-right:8px;overflow:hidden;' + ( d ? 'display:none' : '' ) +
					'><div id=' + this.id + '-pop class=pl-fileupload-pop>' + ( this.settings.button_html || $.htm.fic( '.i-upload-file', 'margin-right:1px' ) + this.upTitle ) + '</div>' +
					'<div id="' + this.id + ':cont" title="选择文件" class=pl-fileupload-cont onmouseover=$.class_add($("' + this.id + '-pop"),"z-hv") onmouseout=$.class_del($("' + this.id + '-pop"),"z-hv")><span id=' + this.ownerPlugin.id + '-btn></span></div>' +
					'</div>';
			if ( ! d ) {
				if ( this.customSettings.netdiskclk )
					s += '<a id=' + this.id + '-net href="javascript:;" class="left' + ( d ? '"' : '" onclick=PL.FileUpload._a["' + this.id + '"].netdiskclk()' ) +
						'><div style="margin:-right:16px">' + $.htm.fic( '.i-upload-netdisk', 'margin-right:1px' ) + '从文件库选择</div></a>';
				if ( this.customSettings.remark )
					s += '<span class=f-gray>' + this.customSettings.remark + '</span>';
			}
			s += '<input type=hidden id=' + this.ownerPlugin.id + ' name='+ this.ownerPlugin.name +'></div><span id=' + this.ownerPlugin.id + '-prg>';
			$.html( this.ownerPlugin.$(), s + this.html_loaders() + '</span>' );
			this.df_write_value();
			if ( ! Br.cmp ) { // fix ie兼容模式
				$( this.id + ':cont' ).style.cssText = 'width:' + w + 'px;height:' + h + 'px;';
			}
		},
		netdiskclk: function() {
			var s = js.s_printf( this.customSettings.netdiskclk, js.url_esc( js.toJSONString( this.df_value ), js.url_esc( this.settings.file_types || '' ) ) );
			eval( s );
		},
		setDisabled: function( a ) {	
			BaseUpload.prototype.setDisabled.call( this, a );
			$.class_add( $( this.id + '-pop' ), 'rdn', ! a );
			if ( $( this.id + '-net' ) )
				$.class_add( $( this.id + '-net' ), 'a-gray', ! a );
		}
	}
} );


/* ******************************************
 *	SWFUpload.ImageUpload Object
 *	显示图片上传进度和结果
 * ****************************************** */
var FileLoader = Fn.reg( {
	
	Const: function() {
		Loader.apply( this, arguments );
	},
	
	Extend : [ Loader ],
	
	Prototype : {
		setProgress: function( percent ) {
			this.loading = true;
			//if ( ! this.wd ) this.wd = $( this.id ).offsetWidth - 2;
			$( this.id + '-prg' ).style.width = percent + '%';// ( this.wd * percent / 100 ) + 'px';
		},
		show: function( data ) {
			$.class_del( $( this.id ), 'z-prg' );
			$.html( $( this.id + '-oper' ), this.html_btn() );
			if ( this.swfu.settings.down_url )
				Q( 'a', $( this.id + '-lk' ) ).replaceWith( '<a href="' + js.s_printx( this.swfu.settings.down_url, data ) + '">' + data.name + '</a>' );
			if ( this.swfu.settings.face === 'medium' )
				$.prepend( $( this.id + '-oper' ), '<div class="left f-gray ht-100 clearfix"><i class=va-i></i>' + $.htm.icg( '.i-check' ) + ' <em class="ln-mid">' + Loc.upload_success + '</em></div>' );
		},
		more: function( a, b ) {
			Fn.Oper.mapToMenu( new Fn.Menu( this ), js.printJSON( b, this.data ) ).show( a );
		},
		setError: function( errorCode, message ) {
			var msg = BaseUpload.errmsg( errorCode, message );
			$.prepend( $( this.id + '-oper' ), '<font class=left color=#ff7921>' + ( msg || Loc.upload_fail ) + '</font>&nbsp;' );
			
		},
		btnClk: function( a, b ) {
			var e = this.swfu.customSettings.buttons[ b ];
			if ( e.click ) ( typeof e.click === 'string' ? Function( '$0,$1', js.s_printx( e.click, this.data ) ) : e.click ).call( this, this.data, this.file );
			if ( e.childNodes ) this.more( a, e.childNodes );
		},
		html_btn: function() {
			var c = this.swfu.customSettings,
				s = '';
			if ( this.data ) {
				if ( c.buttons ) {
					for ( var i = 0, e; i < c.buttons.length; i ++ ) {
						e = c.buttons[ i ];
						s += '<div class=_btn><a href=javascript:; onclick=PL.Loader.all["' + this.id + '"].btnClk(this,' + i + ')>';
						if ( e.ic )
							s += $.htm.ico( e.ic, { style: 'float:left;height:24px' } );
						if ( e.title )
							s += e.title;
						s += '</a></div>';
					}
				}
			} else if ( this.file ) {
				s += '<div class=_btn>&nbsp; <a href=javascript:; onclick=PL.Loader.all["' + this.id + '"].remove()>' + Loc.cancel + '</a></div>';
			}
			return '<div class=_btnwrp>' + s + '</div>'
		},
		html: function() {
			var c = this.swfu.settings,
				f = c.face || 'small',
				g = { 'medium' : ' .i-32' },
				d = this.data || this.file,
				n = d.name || '---',
				s = '<div id=' + this.id + ' class="pl-fileupload-loader pl-fileupload-loader-' + f + ( this.data ? '' : ' z-prg' ) + '"><div class=_bar><div id=' + this.id +
					'-prg class=_prg></div></div><div class="_gut clearfix">' + $.htm.ico( ( d.icon || getIco( d.name ) ) + ( g[ f ] || '' ) ) +
					'<div id=' + this.id + '-lk class="_tit fix" style="' + ( c.title_width ? 'width:' + c.title_width + 'px;' : '' ) + '" title="' + n + '">' +
					'<a' + ( c.down_url && this.data ? ' href="' + js.s_printx( c.down_url, d ) + '"' : '' ) + '>' + n + '</a>' +
					( g[ f ] ? '<br><span class=_size>' + readablizeBytes( d.size ) + '</span>' : '' ) + '</div><div id=' + this.id + '-oper class=_op>' + this.html_btn() + '</div></div>';
			return s + '</div>'
		}
		
	}
} );

PL.FileUpload  = FileUpload;
PL.ImageUpload = ImageUpload;
PL.Loader = Loader;

} )();