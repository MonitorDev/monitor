/**
 * about
 * 1. 本js在首页通过script标签引用
 * 2. 本js内引入所有业务模块js
 * 3. 本js定义模块类Module
 * 
 * @author: cmy
**/ 


/*
 * Pub：公共的通用模块
**/
var Pub = Fn.reg( {
	
	Const: function( id, methods ) {
		js.clone( this, methods );
		this.id = id;
		Pub.all[ id ] = this;
	},
	
	Helper: {
		all: {},
		get: function( name ) {
			var n = name.replace( ':btn', '' ).split( '_' )[ 0 ],
				m = Pub.all[ n ];
			//if ( ! m )
			//	alert( n + '尚未注册为模块' );
			return m;
		}
	},
			
	// 需要实现的接口
	Prototype: {
		
		/*
		 * @act  String: 动作名
		 * @data   json：  数据
		 */
		open: function( act, data ) {
			alert( this.id + '.open() 尚未实现' )
		}
		
	}	

} );


/*
 * Module继承Pub，拥有后台配置的模块属性 
**/
var Module = Fn.reg( {
	
	Const: function( id, methods ) {
		this.options = Cfg.moduleOptions[ id ];
		Pub.call( this, id, methods );
	},
	
	Extend: [ Pub ],

	Helper: {
		getOption: function( name ) {
			return Cfg.moduleOptions[ name.replace( ':btn', '' ) ];
		}
	}
	
} );



/* 加载模块 */

//公用
Ajax.require( 'm/pub/pub.js' );
//首页
Ajax.require( 'm/index/index.js' );

//人员头像上传控件
Ajax.require('m/pub/ums.js');
Ajax.require('pl/flv/flowplayer-3.1.4.min.js');
Ajax.require('pl/flv/flowplayer.js');

//人员选择
Ajax.require( 'm/pub/userPicker.js' );



