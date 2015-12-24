package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rongji.websiteMonitor.common.util.DateUtils;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ServiceLocator;

public class test {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static List<String> getDateBetween2Date(String start_date,String end_date) {
		
		
		List<String> result  = new ArrayList<String>();
		try {
			start_date = start_date.trim();
			end_date = end_date.trim();
			if(Utils.isEmpty(start_date)||Utils.isEmpty(end_date)){
				return result;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long recAfter = diffDate(sdf.parse(start_date),sdf.parse(end_date));
			if(recAfter<0){
				String temp = start_date;
				start_date = end_date;
				end_date = temp;
				recAfter = -1*recAfter;
			}
			
			result.add(start_date.substring(5));
			String tempStart_date = start_date;
			while(recAfter>0){
				tempStart_date = turnDate(tempStart_date, "yyyy-MM-dd", 1);
				result.add(tempStart_date);
				recAfter--;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}

	private static long diffDate(Date parse, Date parse2) {
		long days = 0;
		long day1 = parse.getTime();
		long day2 = parse2.getTime();
		days = (day2 - day1) / (24*3600*1000);
//		if(days > 31){//31天
//		    return -1;
//		    
//	   }else if(days < 0){
//	    return -1;
//	    
//	   }
		    return days;
	}
	
	public static String turnDate(String showDate,String format,int interDay) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(showDate));
		cal.roll(java.util.Calendar.DAY_OF_YEAR,interDay);
		String next = sdf.format(cal.getTime());
		return next;
	}
	
	

}
