/***********************************************************
 * dfish插件模式代码
 ***********************************************************/
    
PL( 'UEditor', {
	// @a -> plugin, b -> editor cfg
	Const : function( a, b ) {
		var d = DFish.g_dialog( a );
		this.options = js.extend( b || {}, { zIndex: d ? d.zIndex : 1, initialContent: a.getDefaultValue(), readonly: a.xt( '@ds' ) === '1', toolbars: UEDITOR_CONFIG[ b.advanceMode ? 'toolbars' : 'simpleToolbars' ] } );
		this.id = PL.UEditor.g_id();
		PL.UEditor._a[ this.id ] = this;
		a.bind( { handler : this, getValue : this.getContent, setValue : this.setContent,
			resize : this.resize, check : this.check, save : this.save, unload : this.destroy } );
		this.render();
	},
	
	Helper : {
		_a   : {},
		idp  : 'dfueditor-',
		idc  : 0,
		g_id : function() { return this.idp + this.idc ++ }
		
	},
	
	Prototype : {
		render : function() {
			var p = this.ownerPlugin;
			p.html( '<div id=' + this.id + ' style="width:' + ( p.wd() - ( p.opts.fu == 1 ? 3 : 6 ) ) + 'px"></div><input type=hidden id=' + this.id + '-hdn name='+ p.name + '>' );
			this._render();
		},
		_render: function() {
			var u = this.u;
			if ( u ) {
				if ( u.isReady ) {
					this.u.destroy();
				} else {
					var self = this;
					u.ready( function() {
						setTimeout( function() {
							self._render();
						}, 10 )
					} );
					return;
				}
			}
			$( this.id ).innerHTML = '<div id=' + this.id + ':ue></div>';
			this.u = UE.getEditor( this.id + ':ue', this.options );
			this.u.owner = this;
		},
		setAdvanceMode: function( a ) {
			this.options.initialContent = this.getContent();
			this.options.toolbars = UEDITOR_CONFIG[ a === false ? 'simpleToolbars' : 'toolbars' ];
			this.options.elementPathEnabled = a !== false;
			this.options.wordCount = a !== false;
			this.options.fullscreen = this.u.ui.isFullScreen();
			this._render();
		},
		getContent : function() {
			return this.u.getContent();
		},
		setContent : function( a ) {
			this.u.setContent( a );
		},
		// 执行submitCommand时会调此方法
		save : function() {
			$( this.id + '-hdn' ).value = this.getContent();
		},
		check : function() {
			if ( this.options.maximumWords && this.getContent().length > this.options.maximumWords ) {
				return Loc.ps( Loc.form.toolong_2, this.ownerPlugin.title )
			}
			
		},
		resize : function() {
			var u = this.u,
				w = this.ownerPlugin.wd() + 'px';
			$( this.id ).style.width = w;
			if ( u.isReady ) {
				this.u.container.style.width = w;
				this.u.ui.getDom('iframeholder').style.width = w;
			} else {
				u.ready( function() {
					this.u.container.style.width = w;
					this.u.ui.getDom('iframeholder').style.width = w;
				} );
			}
		},
		destroy : function() {
			delete PL.UEditor._a[ this.id ];
			if ( this.u.isReady )
				this.u.destroy();
			delete this.u;
		}
	}
} );


UE.commands['advancemode'] = {
    execCommand : function(){
 		this.owner.setAdvanceMode();
   }
};

UE.commands['simplemode'] = {
    execCommand : function(){
 		this.owner.setAdvanceMode( false );
   }
};