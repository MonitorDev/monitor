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
import com.rongji.websiteMonitor.dao.MonitoringTypeDao;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;

public class MonitoringTypeDaoImpl extends PubCommonDAOImpl implements MonitoringTypeDao  {
	private static final Logger logger = Logger.getLogger(MonitoringTypeDaoImpl.class);
	private static final String tblName = "Monitortype";
	private static final String idName = "mtId";
	private static final String initId = "00000001";
	@Override
	public List<Monitortype> findMonitorTypesByPage(Page page,
			boolean ignoreSearchConditions) {
		List <Monitortype>list = null;
		if (ignoreSearchConditions) {
			if(page!=null){
				list = getQueryList("SELECT t FROM " + tblName + " t ORDER BY t.mtId desc  ", page, true);
			}else{
				list = getQueryList("SELECT t FROM " + tblName + " t ORDER BY t.mtId desc  ");
			}
			
		} else {
			if(page!=null){
				list = getQueryList("SELECT t  FROM " + tblName + " t ORDER BY t.mtId desc  ", page, false);
			}else{
				list = getQueryList("SELECT t  FROM " + tblName + " t ORDER BY t.mtId desc  ");
			}
			
		}
		return list;
	}
	@Override
	public Monitortype getMonitoringType(String mpId) {		
		String hql = "SELECT t FROM " + tblName + " t where t.mtId = ?  ";
		List<Monitortype> list = getQueryList(hql, mpId);
		
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<Monitortype> findMonitorTypes(String mpName) {
		String hql = "SELECT t FROM " + tblName + " t where t.mtName = ?  ";
		return getQueryList(hql, mpName);
	}
	@Override
	public void saveMonitorType(Monitortype Type) {
		if (Type == null)
			return;
		
		
		if (Utils.isEmpty(Type.getMtId())) {
			Type.setMtId(FrameworkHelper.getNewId(tblName, idName, initId));
		}
		saveObject(Type);
		
		
	}
	@Override
	public void updateMonitorType(Monitortype Type) {
		if (Type != null) {
			updateObject(Type);
		}
		
	}
	@Override
	public List<Monitortype> findMonitorTypeByContainer(
			SearchContainer container, Page page) {
		String hql = "From "+tblName+" where 1=1 and ";
		hql = hql+container.toHqlWitOutWhere();
		if(page!= null){
			return getQueryList(hql,page,true, container.getArgs());
		}else{
			return getQueryList(hql, container.getArgs());
		}
		
	}
	@Override
	public boolean delete(Monitortype Type) throws Exception {
		if(Type!=null){
			super.delete(Type);
		}
		return true;
	}



}
