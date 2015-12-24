/*
 *  百度地图
 */


PL( 'BaiduMap', {
	
	// 构造方法 /@pl -> plugin, wd -> width, ht -> height
	// 取值(json字串): { center_lng, center_lat, point_lng, point_lat, zoom }
	Const : function( pl, wd, ht ) {
		this.pl = pl;
		this.wd = wd;
		this.ht = ht;
		this.v  = pl.getDefaultValue(); // 获取插件对象<value>标签内的值
		this.id = PL.BaiduMap.g_id();
		PL.BaiduMap._a[ this.id ] = this;
		pl.bind( {
			handler  : this,			// 必选项
			getValue : this.getValue,	// 可选项，执行 VM(this).fv('map') 或 plugin.getValue() 时会调用此方法
			setValue : this.setValue,	// 可选项，执行 VM(this).fs('map','xx') 或 plugin.setValue() 时会调用此方法
			disable  : this.disable,	// 可选项，执行 VM(this).f('map',DFish.DISABLE) 时会调用此方法
			save     : this.save,		// 可选项，执行submit命令时会调用
			check	 : this.check,		// 可选项，执行submit命令时会调用。如果有错误，需返回错误信息的字串
			resize   : this.resize,		// 可选项，调整大小时会调用
			unload   : this.destroy		// 可选项，销毁本插件时调用
		} );
		pl.html( this.html() ); // 显示插件内容
	},
	
	// 静态方法
	Helper : {
		_a    : {},
		idp   : 'baidumap-',
		idc   : 0,
		g_id  : function(){ return this.idp + ( this.idc ++ ) }
	},
	
	// 实例方法
	Prototype : {
		getValue : function() {
			this.save();
			return this.v;
		},
		save : function() {
			try {
				$( this.id ).value = this.v = window.frames[ this.id + '-fr' ].getValue();
			} catch( e ){}
		},
		html : function() {
			return $.htm.iframe( this.wd, this.ht, 'js/pl/map/baidu.html', this.id + '-fr' ) +
				'<input type=hidden id=' + this.id + ' name=' + this.pl.name + ' value="' + js.s_comma( this.v ) + '">';
		},
		destroy : function() {
			this.pl = this.v = null;
			delete PL.BaiduMap._a[ this.id ];
		}
	}
	
} );