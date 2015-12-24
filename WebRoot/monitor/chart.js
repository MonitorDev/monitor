var rateTimer,jvmThreadTimer, ioTimer,overviewTimer;
var timerInterval = 1000*60;
var cateOption,jvmThreadOption,ioOption,cpuseries,memoryseries,jvmMemoryseries,xAxis,jvmThreadSeries,jvmThreadXAxis,ioSeries,ioXAxis;
function initChart(name, chartOption) {
	cateOption = chartOption;
	chartOption.chart.events = {
		load : function() {
			cpuseries = this.series[0];                                
			memoryseries = this.series[1]; 
			jvmMemoryseries = this.series[2];
			xAxis = this.xAxis[0];
			//rateTimer = setInterval(rateIntervalFunction,timerInterval);
		}
	}
	jQuery('#'+name).highcharts(chartOption);                                                                         
}

function jvmThreadChart(name, chartOption) {
	jvmThreadOption = chartOption;
	chartOption.chart.events = {
		load : function() {
			jvmThreadSeries = this.series[0];      
			jvmThreadXAxis = this.xAxis[0];
			//jvmThreadTimer = setInterval(jvmThreadIntervalFunction,timerInterval);
		}
	}
	jQuery('#'+name).highcharts(chartOption);                                                                         
}

function ioChart(name, chartOption) {
	ioOption = chartOption;
	chartOption.chart.events = {
		load : function() {
			ioSeries = this.series[0];      
			ioXAxis = this.xAxis[0];
			//ioTimer = setInterval(ioIntervalFunction,timerInterval);
		}
	}
	jQuery('#'+name).highcharts(chartOption);                                                                         
}

function rateIntervalFunction() {
	if(isRemove()) {
		clearInterval(rateTimer);
	}else {
		jQuery.get("clientMonitor.sp?act=cycleRefreshCharts&type=rate&id="+cateOption.taskId+"&timerInterval="+timerInterval , function(data){
			console.log("rate:" + data);
			var rateData = JSON.parse(data);
			if(data) {
				for(var i=0; i<rateData.length; i++) {
					var d = rateData[i];
					xAxis.categories.push(d[0]);
					cpuseries.addPoint([d[1]], true, true);
					memoryseries.addPoint([d[2]], true, true);
					jvmMemoryseries.addPoint([d[3]], true, true);
				}
			}
		})
	}
}

function jvmThreadIntervalFunction() {
	if(isRemove()) {
		clearInterval(jvmThreadTimer);
	}else {
		jQuery.get("clientMonitor.sp?act=cycleRefreshCharts&type=jvmThead&id="+jvmThreadOption.taskId+"&timerInterval="+timerInterval , function(data){
			console.log("jvm:" + data);
			var rateData = JSON.parse(data);
			if(data) {
				for(var i=0; i<rateData.length; i++) {
					var d = rateData[i];
					jvmThreadXAxis.categories.push(d[0]);
					jvmThreadSeries.addPoint([d[1]], true, true);
				}
			}
		})
	}
}

function ioIntervalFunction() {
	if(isRemove()) {
		clearInterval(ioTimer);
	}else {
		jQuery.get("clientMonitor.sp?act=cycleRefreshCharts&type=io&id="+ioOption.taskId+"&timerInterval="+timerInterval , function(data){
			console.log("io:" + data);
			var rateData = JSON.parse(data);
			if(data) {
				for(var i=0; i<rateData.length; i++) {
					var d = rateData[i];
					ioXAxis.categories.push(d[0]);
					ioSeries.addPoint([d[1]], true, true);
				}
			}
		})
	}
}

function isRemove() {
	try {
		if(jQuery(".snmpView").parent().parent().length > 0) {
			return jQuery(".snmpView").parent().parent().is(":hidden");
		}else {
			return true;
		}
	}catch(e) {
		return true;
	}
}


function removeTimer() {
	clearTimeout(rateTimer);
	clearTimeout(jvmThreadTimer);
	clearTimeout(ioTimer);
}

function registerTimer() {
//	console.log("register timer")
	rateTimer = setInterval(rateIntervalFunction,timerInterval);
	jvmThreadTimer = setInterval(jvmThreadIntervalFunction,timerInterval);
	ioTimer = setInterval(ioIntervalFunction,timerInterval);
}

function removeOverviewTimer() {
	clearTimeout(overviewTimer);
}

function isOVerviewRemove() {
	try {
		if(jQuery(".overviewView").parent().parent().length > 0) {
			return jQuery(".overviewView").parent().parent().is(":hidden");
		}else {
			return true;
		}
	}catch(e) {
		return true;
	}
}
function registerOverviewTimer() {
	overviewTimer = setInterval(function(){
		console.log("ajax ---------------------update-----  " + isOVerviewRemove())
		if(isOVerviewRemove()) {
			clearInterval(overviewTimer);
		}else {
			VM(this).cmd( { tagName : 'ajax',   src : 'clientMonitor.sp?act=updateOverView&update=1&isTimer=true' } );
		}
	},1000*60*2);
}

function buildChart(name, buildChartOption) {
//	console.log(buildChartOption);
	jQuery('#'+name).highcharts(buildChartOption);                                                                         
}
