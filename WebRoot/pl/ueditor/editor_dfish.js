/***********************************************************
 * dfish插件模式代码
 * 当前使用版本: UEditor 1.1.8.1
 * by chenmingyuan 2012-3-7 17:07:50
 ************************************************************/
    
PL( 'UEditor', {
	// @a -> plugin, b -> editor cfg
	Const : function( a, b ) {
		if ( b ) b = js.extend( b, UEDITOR_CONFIG );
		this.u = new baidu.editor.ui.Editor( b );
		this.id = PL.UEditor.g_id();
		PL.UEditor._a[ this.id ] = this;
		a.bind( { handler : this, getValue : this.getContent, setValue : this.setContent, save : this.save, unload : this.destroy } );
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
			p.html( '<div id=' + this.id + ' style="padding-right:10px;"></div><input type=hidden id=' + this.id + '-hdn name='+ p.name + '>' );
			this.u.render( this.id );
			this.setContent( p.getDefaultValue() );
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
		destroy : function() {
			this.u.destroy();
			delete PL.UEditor._a[ this.id ];
		}
	}
} );
