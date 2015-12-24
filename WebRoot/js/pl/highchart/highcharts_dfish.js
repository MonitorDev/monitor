/* Highchart插件 */

$.css_import_link( 'js/pl/highchart/highcharts.css' );

(function() {

var count  = 0;

PL ( 'Highchart', {
	
	Const: function( pl, opt ) {
		opt = this.initOptions( opt );
		pl.bind( { handler : this, resize : this.resize } );
		this.id = 'fish-hichart-' + ( count ++ );
		var h = pl.constructor === VM.PLUGIN && pl.ht();
		pl.html( '<div class=pl-highchart id=' + this.id + ' style="width:' + pl.wd() + 'px;' + ( h ? 'height:' + h + 'px;' : '' ) + '"></div>' );
		pl.chart = jQuery( '#' + this.id ).highcharts( opt );
	},
	
	Prototype: {
		
		initOptions: function( opt ) {
			if ( opt.xAxis && opt.xAxis.labels && opt.xAxis.labels.formatter )
				opt.xAxis.labels.formatter = Function( opt.xAxis.labels.formatter );
			if ( opt.yAxis && opt.yAxis.labels && opt.yAxis.labels.formatter )
				opt.yAxis.labels.formatter = Function( opt.yAxis.labels.formatter );
			if ( opt.tooltip && opt.tooltip.formatter )
				opt.tooltip.formatter = Function( opt.tooltip.formatter );
			return opt
		},
		
		resize: function() {
			var pl = this.ownerPlugin;
			$( this.id ).style.width = pl.wd() + 'px';
			if( pl.constructor === VM.PLUGIN ) {
				var ht = pl.ht();
				if ( ht && ht > -1 ) 
					$( this.id ).style.height = ht + 'px';
			}
		}
	}

} );
}());