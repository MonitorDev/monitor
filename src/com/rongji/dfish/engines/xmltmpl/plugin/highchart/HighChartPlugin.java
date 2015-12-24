package com.rongji.dfish.engines.xmltmpl.plugin.highchart;

import java.util.Arrays;

import com.google.gson.Gson;
import com.rongji.dfish.engines.xmltmpl.component.PluginPanel;

/**
 * @author LinLW
 *
 */
public class HighChartPlugin extends PluginPanel{
	Gson gson = new Gson(); 
	HighChart hc =new HighChart();
	public HighChartPlugin(String id,HighChart hc) {
		super(id);
		super.setJsSrcs(Arrays.asList(new String[]{
			"js/pl/highchart/highcharts.js",
			"js/pl/highchart/highcharts_dfish.js"
		}));
		this.hc=hc;
		super.setJsCodeOnBeforeLoad("return 'loading...';");
	}
	@Override
	public void buildXML(StringBuilder sb) {
		super.setJsCodeOnLoad("new PL.Highchart( this, "+gson.toJson(hc)+" );");
		super.buildXML(sb);
	}
	


}
