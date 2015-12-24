package com.rongji.websiteMonitor.common.trigger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.quartz.TriggerAction;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.service.ServiceLocator;
//com.rongji.websiteMonitor.common.trigger.ArchiveTrigger
public class ArchiveTrigger implements TriggerAction{

	@Override
	public void execute(String config) throws Exception {
		Map<String, String> configMap = Utils.string2Map(config);
		String area = configMap.get("area");
		String type = configMap.get("type");
		String first = configMap.get("first");
		if("true".equals(first)&&Utils.notEmpty(type)){
			archiveFirst(type);
		}else if(Utils.notEmpty(area)&&Utils.notEmpty(type)){
			archiveAreaMonth(area,type);
		}	else if(Utils.notEmpty(type)){
//			archiveOneMonth(type);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,-1);
			Date d=cal.getTime();
		    String yesterday = sdf.format(d);
		    archiveOneDay(type, yesterday);
		}
		
	}

	
	private void archiveFirst(String type) {
		List<Object []> list = ServiceLocator.getTaskResultService().getStartAndEndTime();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date [] times = new Date[2];  
		if(Utils.notEmpty(list)){
			times[0] = (Date)list.get(0)[0];
			times[1] = (Date)list.get(0)[1];
		}
		String statType = type;
		Date startTime = null;
		Date endTime = null;
		Calendar calendar = null;
		Calendar calendar2 = null;
		if(times!=null){
			startTime = times[0];
			endTime = times[1];
			calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			calendar2 = Calendar.getInstance();
			calendar2.setTime(endTime);
			boolean isThisYearAndMonth = isThisYearAndMonth(endTime);
			Long difMonth = 0L;
			if(isThisYearAndMonth){
				difMonth = (calendar2.get(Calendar.YEAR)-calendar.get(Calendar.YEAR))*12L+(calendar2.get(Calendar.MONTH)-calendar.get(Calendar.MONTH));//(endTime/10000-startTime/10000)*12+(endTime/100%100-startTime/100%100)-1;
			}else{
				difMonth = (calendar2.get(Calendar.YEAR)-calendar.get(Calendar.YEAR))*12L+(calendar2.get(Calendar.MONTH)-calendar.get(Calendar.MONTH))+1;
			}
			
			if(difMonth>0){
				int beginYear = calendar.get(Calendar.YEAR);
				int beginMonth = calendar.get(Calendar.MONTH)+1;
				for(int i=0;i<difMonth;i++){
					String begintime = beginYear+"-"+beginMonth+"-01 00:00:01";
					String endtime =  beginYear+"-"+beginMonth+"-"+getLastDayByMonth(beginYear,beginMonth)+" 23:59:59";
					handleArchive(begintime,endtime,statType);
					beginMonth = beginMonth+1;
					if(beginMonth>12){
						beginMonth = beginMonth%12;
						beginYear = beginYear+1;
					}
				}
			}
			//时间到今年的这个月份
			if(isThisYearAndMonth){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE,-1);
				int year = cal.get(Calendar.YEAR);    //获取年
				int month = cal.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
				Date d=cal.getTime();
			    String yesterday = sdf.format(d);
			    String startDate = year+"-"+month+"-01 00:00:01";
			    String endDate = yesterday+" 23:59:59";
			    handleArchive(startDate,endDate,statType);
			}
		}
	
		
	}

	

	private void archiveAreaMonth(String area, String type) {
		String[] times = area.split("-");
		String statType = type;
		Date endTime = null;
		Calendar calendar = Calendar.getInstance(); 
		String [] begin1 = times[0].split("/");
		String [] begin2 = times[1].split("/");
//		if (Integer.parseInt(begin2[1]) >= 10) {
//			calendar.add(Calendar.YEAR, Integer.parseInt(begin2[0]));
//			calendar.add(Calendar.MONTH, Integer.parseInt(begin2[1])-1);
//			endTime = calendar.getTime();//Long.parseLong((begin2[0] + "" + begin2[1] + "00"));
//		} else {
//			calendar.add(Calendar.YEAR, Integer.parseInt(begin2[0]));
//			calendar.add(Calendar.MONTH, Integer.parseInt(begin2[1])-1);
//			endTime = calendar.getTime();
//			endTime = Long.parseLong((begin2[0] + "0" + begin2[1] + "00"));
//		}
		calendar.add(Calendar.YEAR, Integer.parseInt(begin2[0]));
		calendar.add(Calendar.MONTH, Integer.parseInt(begin2[1])-1);
		endTime = calendar.getTime();
		boolean isThisYearAndMonth = isThisYearAndMonth(endTime);
		int difMonth = 0;
		if(isThisYearAndMonth){
			difMonth = (Integer.parseInt(begin2[0])-Integer.parseInt(begin1[0]))*12+(Integer.parseInt(begin2[1])-Integer.parseInt(begin1[1]));
		}else{
			difMonth = (Integer.parseInt(begin2[0])-Integer.parseInt(begin1[0]))*12+(Integer.parseInt(begin2[1])-Integer.parseInt(begin1[1]))+1;
		}
		
		if(difMonth>0){
			int beginYear = Integer.parseInt(begin1[0]);
			int beginMonth = Integer.parseInt(begin1[1]);
			for(int i=0;i<difMonth;i++){
				String begintime = beginYear+"-"+beginMonth+"-01 00:00:01";
				String endtime =  beginYear+"-"+beginMonth+"-"+getLastDayByMonth(beginYear,beginMonth)+" 23:59:59";
				handleArchive(begintime,endtime,statType);
				beginMonth = beginMonth+1;
				if(beginMonth>12){
					beginMonth = beginMonth%12;
					beginYear = beginYear+1;
				}
			}
		}
	
	}
//	
//

	private void archiveOneMonth(String type) {
		String statType = null;
		if (Utils.notEmpty(type)) {
			statType = type;
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			if (month == 1) {
				year = year - 1;
				month = 12;
			} else {
				month = month - 1;
			}
			String begintime = year + "-" + month + "-01 00:00:01";
			String endtime = year + "-" + month + "-"+getLastDayByMonth(year,month)+" 23:59:59";
			handleArchive(begintime,endtime,statType);
		}
		
	}	
	
	private void archiveOneDay(String type,String day) {
		if (Utils.notEmpty(type)&&Utils.notEmpty(day)) {
//			statType = type;
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DATE,-1);
//			Date d=cal.getTime();
//		    String yesterday = sdf.format(d);
			String begintime = day+" 00:00:01";
			String endtime = day+" 23:59:59";
			handleArchive(begintime,endtime,type);
		}
		
	}	

	//处理归档
	private void handleArchive(String begintime, String endtime,
			String statType) {
		// 查询到的是min(t.tr_id),t.task_id,t.isuseable,count(t.isuseable) ,min(t.tr_resp_time),max(t.tr_resp_time),
//		to_char(avg(t.tr_resp_time),'999999.999'),to_char(avg(t.ext_1),'999999.999')
//		,to_char(avg(t.ext_2),'999999.999'),to_char(avg(t.ext_3),'999999.999'),to_char(avg(t.ext_4),'999999.999'),
//		t.tr_type,t.mp_id,sum(t.times),to_char(t.sta_date,'yyyy-MM-dd')
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		List<Object[]> dataList = ServiceLocator.getTaskResultService()
		.getSumResult(conver2Date(begintime), conver2Date(endtime),
				statType);
		TaskResult taskResult = null,temp = null;;
		if(Utils.notEmpty(dataList)){
			String [] times = begintime.split("-");
			int month = Integer.parseInt(times[1]);
			int year = Integer.parseInt(times[0]);
			boolean isExit = false;
			List<TaskResult> list = null;
			for (Object[] obj : dataList) {
				taskResult = new TaskResult();
				taskResult.setTrId((String)obj[0]);
				taskResult.setTaskId((String)obj[1]);
				taskResult.setIsuseable((String)obj[2]);
				taskResult.setTimes(Long.parseLong(Utils.notEmpty(String.valueOf(obj[3]!=null?obj[3]:"0"))?String.valueOf(obj[3]!=null?obj[3]:"0"):"0"));
				taskResult.setTrMinRespTime(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[4]!=null?obj[4]:"0"))?String.valueOf(obj[4]!=null?obj[4]:"0"):"0"));
				taskResult.setTrMaxRespTime(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[5]!=null?obj[5]:"0"))?String.valueOf(obj[5]!=null?obj[5]:"0"):"0"));
				taskResult.setTrRespTime(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[6]!=null?obj[6]:"0"))?String.valueOf(obj[6]!=null?obj[6]:"0"):"0"));
				taskResult.setExt1(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[7]!=null?obj[7]:"0"))?String.valueOf(obj[7]!=null?obj[7]:"0"):"0"));
				taskResult.setExt2(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[8]!=null?obj[8]:"0"))?String.valueOf(obj[8]!=null?obj[8]:"0"):"0"));
				taskResult.setExt3(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[9]!=null?obj[9]:"0"))?String.valueOf(obj[9]!=null?obj[9]:"0"):"0"));
				taskResult.setExt4(Double.parseDouble(Utils.notEmpty(String.valueOf(obj[10]!=null?obj[10]:"0"))?String.valueOf(obj[10]!=null?obj[10]:"0"):"0"));
				taskResult.setTrType((String)obj[11]);
				taskResult.setMpId((String)obj[12]);
				if(obj[13]==null){
					taskResult.setTimes(0L);
				}else{
					taskResult.setTimes(Long.parseLong(String.valueOf(obj[13])));
				}
				
				
				taskResult.setMonitoringPoint((String)obj[15]);
				taskResult.setArchiveLevel(Constants.ARCHIVE_LEVEL_DAY);
				taskResult.setDataTime((obj[14]!=null?((Long)obj[14]):0L));
				try {
					taskResult.setStaDate(taskResult.getDataTime()!=0?sdf.parse(taskResult.getDataTime()+" 00:00:01"):null);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				temp = ServiceLocator.getTaskResultService().getTaskResultById(taskResult.getTrId());
				if(temp!=null){
					isExit = true;
				}
				ServiceLocator.getTaskResultService().archiveResult(taskResult,isExit);		
				
			
			}
			ServiceLocator.getTaskResultService().deleteRedundance(
					begintime, endtime,
					statType);
			
		}
	}


	/**
	 * 获取一个月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */

	private int getLastDayByMonth(int year,int month) {
		int lastDay = 0;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				lastDay = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				lastDay = 30;
				break;
			case 2:
				if((year%4==0&&year%100!=0)||year%400==0){
					lastDay = 29;
				}else{
					lastDay = 28;
				}
				break;
		}
		return lastDay;
	}
	/**
	 * 转化为时间格式
	 * 
	 * @param date
	 * @return
	 */
	private Date conver2Date(String date) {
		Date d = null;
		if (Utils.notEmpty(date)) {
			try {
				d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}
	
	/**
	 * 判断该时间是否在当前日期的本月内
	 * @param endTime
	 * @return
	 */
	private boolean isThisYearAndMonth(Date endTime) {
		boolean isThisYearAndMonth = false;
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		int tempYear = calendar.get(Calendar.YEAR);
		int tempMonth = calendar.get(Calendar.MONTH) + 1;
		if(year==tempYear&&month == tempMonth){
			isThisYearAndMonth = true;
		}else {
			isThisYearAndMonth = false;
		}
		return isThisYearAndMonth;
	}
	
	@Override
	public String getDescription(Locale loc) {
		// TODO Auto-generated method stub
		return "归档每天的监测数据";
	}

}
