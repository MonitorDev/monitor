package com.rongji.websiteMonitor.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;

import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.MonitoringPointDao;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;

public class MonitoringPointDaoImpl extends PubCommonDAOImpl implements MonitoringPointDao  {
	private static final Logger logger = Logger.getLogger(MonitoringPointDaoImpl.class);
	private static final String tblName = "MonitoringPoint";
	private static final String idName = "mpId";
	private static final String initId = "00000001";
	@Override
	public List<MonitoringPoint> findMonitorPointsByPage(Page page,
			boolean ignoreSearchConditions) {
		List <MonitoringPoint>list = null;
		if (ignoreSearchConditions) {
			list = getQueryList("SELECT t FROM " + tblName + " t ORDER BY t.mpId desc  ", page, true);
		} else {
			list = getQueryList("SELECT  FROM " + tblName + " t ORDER BY t.mpId desc  ", page, false);
		}
		return list;
	}
	@Override
	public MonitoringPoint getMonitoringPoint(String mpId) {		
		String hql = "SELECT t FROM " + tblName + " t where t.mpId = ?  ";
		List<MonitoringPoint> list = getQueryList(hql, mpId);
		
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<MonitoringPoint> findMonitorPoints(String mpName) {
		String hql = "SELECT t FROM " + tblName + " t where t.mpName = ?  ";
		return getQueryList(hql, mpName);
	}
	@Override
	public void saveMonitorPoint(MonitoringPoint point) {
		if (point == null)
			return;
		
		if (point.getCreateTime() == null) {
			point.setCreateTime(new Date());
		}
		
		if (Utils.isEmpty(point.getMpId())) {
			point.setMpId(FrameworkHelper.getNewId(tblName, idName, initId));
		}
		saveObject(point);
		
		
	}
	@Override
	public void updateMonitorPoint(MonitoringPoint point) {
		if (point != null) {
			updateObject(point);
		}
		
	}
	@Override
	public List<MonitoringPoint> findMonitorPointByContainer(
			SearchContainer container, Page page) {
		String hql = "From "+tblName+" where 1=1 and ";
		hql = hql+container.toHqlWitOutWhere();
		if(page!=null){
			return getQueryList(hql,page,true, container.getArgs());
		}else{
			return getQueryList(hql, container.getArgs());
		}
		
	}
	@Override
	public boolean delete(MonitoringPoint point) throws Exception {
		if(point!=null){
			super.delete(point);
		}
		return true;
	}
	@Override
	public List<MonitoringPoint> findMonitorPointsByPageAndIsExternal(
			Page page, boolean ignoreSearchConditions, String isExternal) {
		List <MonitoringPoint>list = null;
		if (ignoreSearchConditions) {
			if(Utils.isEmpty(isExternal)) {
				list = getQueryList("SELECT t FROM " + tblName + " t ORDER BY t.mpId desc  ", page, true);
			}else {
				list = getQueryList("SELECT t FROM " + tblName + " t where t.isExternal=? ORDER BY t.mpId desc  ", page, true, isExternal);
			}
		} else {
			if(Utils.isEmpty(isExternal)) {
				list = getQueryList("SELECT t FROM " + tblName + " t ORDER BY t.mpId desc  ", page, false);
			}else {
				list = getQueryList("SELECT t FROM " + tblName + " t where t.isExternal=? ORDER BY t.mpId desc  ", page, false, isExternal);
			}
		}
		return list;
	}


}
