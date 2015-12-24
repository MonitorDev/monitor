function showFusionCharts(data){
 		var myChart = new FusionCharts("FusionCharts/Charts/Line.swf", "myChartId", "100%", "300");
 		myChart.setJSONData(data);
    	myChart.render("chartdiv");
    }
    
    function showFusionLines(data,name){
 		var myChart = new FusionCharts("FusionCharts/Charts/Line.swf", "myChartId_1", "100%", "300");
 		myChart.setJSONData(data);
    	myChart.render(name);
    }
    
    
    function showFusionCharts2(id,data,num){
 		var myChart = new FusionCharts("FusionCharts/Charts/Pie3D.swf", "myChartId2", "100%", "300");
 		myChart.setJSONData(data);
    	myChart.render("chartdiv"+id);
    }
    
    function showFusionZoomLine(data,name){
 		var myChart = new FusionCharts("FusionCharts/Charts/ZoomLine.swf", "myChartId5", "100%", "300");
 		myChart.setJSONData(data);
    	myChart.render(name);
    }

    
     function showFusionZoomLine2(data,name){
 		var myChart = new FusionCharts("FusionCharts/Charts/ZoomLine.swf", "myChartId6", "100%", "300");
 		myChart.setJSONData(data);
    	myChart.render(name);
    }
    
