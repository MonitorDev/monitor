package com.rongji.websiteMonitor.webapp.visit.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import com.rongji.dfish.engines.xmltmpl.component.Horizontalgroup;
import com.rongji.dfish.engines.xmltmpl.form.DatePicker;
import com.rongji.dfish.engines.xmltmpl.form.Period;
import com.rongji.dfish.engines.xmltmpl.form.Radiogroup;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.websiteMonitor.common.util.DateUtils;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class CommonView {
	/**
	 * 获取一个快捷时段日期选择
	 * @param quickPeriod 
	 * @return 
	 */
	/**
	 * 获取一个快捷时段日期选择
	 * @param quickPeriod 
	 * @return 
	 */
	public static Horizontalgroup getQukSelect(QueryCondition condition){
		Horizontalgroup hg = new Horizontalgroup("", "");
		hg.setFullLine(true);
		Date startTime = condition.getStartTime();
		Date endTime = condition.getEndTime();
		String quickPeriod = condition.getQuickPeriod();
		if (startTime==null && endTime==null) {
			Date now = new Date();
			startTime = DateUtils.getMonthBegin(now);
			endTime = now;
		}
		
		//日期组件
		Period period = new Period("startTime", "endTime", DatePicker.DATE_TIME,
				"", startTime, endTime, Period.NOT_GREATER, false, false,
				false, false, false, false);
		period.setWidth("80");
		period.setWidth2("80");
		period.setRemark("&nbsp;");
		
		//快捷时段下拉选择
		List<Object[]> quickPeriods = new ArrayList<Object[]>();
		quickPeriods.add(new Object[] { "", "选择时段"});
		quickPeriods.add(new Object[] { "lastHour", "最近一小时"});
		quickPeriods.add(new Object[] { "today", "今日"});  
		quickPeriods.add(new Object[] { "yesterday", "昨日"});
		quickPeriods.add(new Object[] { "lastWeek", "最近一周"});
		quickPeriods.add(new Object[] { "lastMonth", "最近一月"});
		quickPeriods.add(new Object[] { "lastYear", "最近一年"});
		Select qukSelect = new Select("quickPeriod","选择时段",new Object[]{quickPeriod},quickPeriods);
		StringBuilder sb0 = new StringBuilder("");
		sb0.append("Date.prototype.Format = function(fmt){  var o = { 'M+' : this.getMonth()+1,'d+' : this.getDate(),'h+' : this.getHours(),"); 
		sb0.append("'m+' : this.getMinutes(),'s+' : this.getSeconds(),'q+' : Math.floor((this.getMonth()+3)/3),'S'  : this.getMilliseconds()};"); 
		sb0.append("if(/(y+)/.test(fmt))fmt=fmt.replace(RegExp.$1, (this.getFullYear()+'').substr(4 - RegExp.$1.length));"); 
		sb0.append("for(var k in o) if(new RegExp('('+ k +')').test(fmt))"); 
		sb0.append("fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (('00'+ o[k]).substr((''+ o[k]).length)));"); 
		sb0.append("return fmt; }; ");
		sb0.append("var quickPeriod = VM(this).f('quickPeriod',DFish.GET_VALUE);");
		sb0.append("var date=new Date();"+
				   "var endTime= new Date(date.getFullYear(),date.getMonth(),date.getDate(),date.getHours(),date.getMinutes()).Format('yyyy-MM-dd hh:mm');"+
				   //date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
				   "var startTime=new Date(date.getFullYear(),date.getMonth(),date.getDate(),date.getHours(),date.getMinutes()).Format('yyyy-MM-dd hh:mm');");
		//date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
		sb0.append("switch(quickPeriod){"+
				   "case 'lastHour':date.setHours(date.getHours()-1);break;"+
				   "case 'today':date.toLocaleString();date.setHours(0);date.setMinutes(0);var endTime=  new Date(date.getFullYear(),(date.getMonth()),date.getDate(),23,59).Format('yyyy-MM-dd hh:mm');break;"+
				   "case 'yesterday':date.toLocaleString();date.setDate(date.getDate()-1);date.setHours(0);date.setMinutes(0);var endTime=  new Date(date.getFullYear(),(date.getMonth()),date.getDate(),23,59).Format('yyyy-MM-dd hh:mm');break;"+
				   "case 'lastWeek':date.toLocaleString();date.setDate(date.getDate()-7);break;"+
				   "case 'lastMonth':date.setMonth(date.getMonth()-1);break;"+
				   "case 'lastYear':date.setFullYear(date.getFullYear()-1);break;"+
				   "}"+
				   "var startTime=new Date(date.getFullYear(),date.getMonth(),date.getDate(),date.getHours(),date.getMinutes()).Format('yyyy-MM-dd hh:mm');"+
//				   date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
				   "VM(this).fs('startTime',startTime);VM(this).fs('endTime',endTime);"+
				   "var startDate = VM(this).f('startTime',DFish.GET_VALUE);"+
				   "var endDate = VM(this).f('endTime',DFish.GET_VALUE);"+
				   "var sel = VM(this).f('sel',DFish.GET_VALUE);");
	
		
		
		qukSelect.setOnchange(sb0.toString());
//		+"VM(this).cmd('filterUpdate');"
		qukSelect.setRemark("&nbsp;&nbsp;&nbsp;");
		
		hg.add(period);
		hg.add(qukSelect);
		return hg;
	}
}
