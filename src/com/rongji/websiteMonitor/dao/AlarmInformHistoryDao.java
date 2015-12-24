package com.rongji.websiteMonitor.dao;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.websiteMonitor.persistence.AlarmInformHistory;

public interface AlarmInformHistoryDao extends PubCommonDAO {

	public void saveAlarmInformHistory(AlarmInformHistory alarmInformHistory);

}
