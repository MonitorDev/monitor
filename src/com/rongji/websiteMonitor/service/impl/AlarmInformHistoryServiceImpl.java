package com.rongji.websiteMonitor.service.impl;

import com.rongji.websiteMonitor.dao.AlarmInformHistoryDao;
import com.rongji.websiteMonitor.persistence.AlarmInformHistory;
import com.rongji.websiteMonitor.service.AlarmInformHistoryService;

public class AlarmInformHistoryServiceImpl implements AlarmInformHistoryService {
	private AlarmInformHistoryDao alarmInformHistoryDao;

	public AlarmInformHistoryDao getAlarmInformHistoryDao() {
		return alarmInformHistoryDao;
	}

	public void setAlarmInformHistoryDao(AlarmInformHistoryDao alarmInformHistoryDao) {
		this.alarmInformHistoryDao = alarmInformHistoryDao;
	}

	@Override
	public void saveAlarmInformHistory(AlarmInformHistory alarmInformHistory) {
		
		alarmInformHistoryDao.saveAlarmInformHistory(alarmInformHistory);
		
	}
	

}
