package com.rongji.websiteMonitor.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.dao.ThresholdDao;
import com.rongji.websiteMonitor.persistence.Threshold;

public class ThresholdDaoImpl extends PubCommonDAOImpl implements ThresholdDao {

	private static final Logger logger = Logger.getLogger(TaskDaoImpl.class);
	private static final String tblName = "Threshold";
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	@Override
	public void saveThreshold(Threshold threshold) {
		if(threshold == null) {
			return ;
		}
		threshold.setId(FrameworkHelper.getNewId(tblName, idName, initId));
		saveObject(threshold);
	}

	@Override
	public Threshold getThresholdById(String id) {
		return (Threshold) this.getHibernateTemplate().get(Threshold.class, id);
	}

	@Override
	public List<Threshold> findAllThreshold(String type, boolean isIgnoreUsable) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from " + tblName + " t where t.type=?");
		if(!isIgnoreUsable) {
			sb.append(" and t.isUsable = 1");
		}
		sb.append(" order by t.createTime desc");
		return this.getQueryList(sb.toString(), type);
	}

	@Override
	public void updateThreshold(Threshold threshold) {
		this.updateObject(threshold);
	}

	@Override
	public List<Threshold> findThresholdByName(String type, String name) {
		return this.getQueryList("from " + tblName + " t where t.name=? and t.type=?", name, type);
	}

	@Override
	public void deleteThreshold(Threshold threshold) {
		this.delete(threshold);
	}

	@Override
	public List<Threshold> findThresholdByPage(String type, Page page) {
		return this.getQueryList(" from " + tblName + " t where t.type=?  order by t.createTime desc", page, type);
	}

	@Override
	public List<Threshold> findThresholdByPage(Page page) {
		return this.getQueryList(" from " + tblName + " t  order by t.createTime desc", page);
	}
}
