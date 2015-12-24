/**
 * about
 * 1. 本js在首页通过script标签引用
 * 2. 本js内引入所有业务模块js
 * 3. 本js定义模块类Module
 * 
 * @author: cmy
**/ 

/* 加载模块 */
//信息管理
Ajax.require( 'm/notice/notice.js' );
Ajax.require( 'm/formDeclare/formDeclare.js' );
Ajax.require( 'm/manifest/manifest.js' );
Ajax.require( 'm/dfish.js' );