package com.rongji.websiteMonitor.dao.impl;

import org.apache.log4j.Logger;

import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.dao.AlarmInformHistoryDao;
import com.rongji.websiteMonitor.persistence.AlarmInformHistory;

public class AlarmInformHistoryDaoImpl extends PubCommonDAOImpl implements
		AlarmInformHistoryDao {

	private static final Logger logger = Logger.getLogger(AlarmInformHistoryDaoImpl.class);
	private static final String tblName = "AlarmInformHistory";
	private static final String idName = "aiId";
	private static final String initId = "0000000000000001";
	@Override
	public void saveAlarmInformHistory(AlarmInformHistory alarmInformHistory) {
		if(alarmInformHistory!=null){
			alarmInformHistory.setAiId(FrameworkHelper.getNewId(tblName, idName, initId));
			this.saveObject(alarmInformHistory);
		}
		
	}
}
