package com.rongji.websiteMonitor.dao.impl;

import java.util.List;

import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.websiteMonitor.dao.SchedulerDao;
import com.rongji.websiteMonitor.persistence.PubScheduler;

public class SchedulerDaoImpl extends PubCommonDAOImpl  implements SchedulerDao {

	@Override
	public PubScheduler getSchedulerById(String schlId) {
		return (PubScheduler) this.getHibernateTemplate().get(PubScheduler.class, schlId);
	}

	@Override
	public PubScheduler getSchedulerByTaskId(String taskId) {
		List<PubScheduler> list = this.getQueryList("from PubScheduler ps where ps.schlId=(select t.schlId from Task t where t.taskId = ?)", taskId);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
