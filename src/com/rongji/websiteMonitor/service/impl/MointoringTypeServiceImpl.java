package com.rongji.websiteMonitor.service.impl;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.spl.SearchContainer.Op;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.MonitoringPointDao;
import com.rongji.websiteMonitor.dao.MonitoringTypeDao;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.service.MonitoringPointService;
import com.rongji.websiteMonitor.service.MonitoringTypeService;

public class MointoringTypeServiceImpl implements MonitoringTypeService {

	private MonitoringTypeDao typeDao;
	
	public MonitoringTypeDao getTypeDao() {
		return typeDao;
	}



	public void setTypeDao(MonitoringTypeDao typeDao) {
		this.typeDao = typeDao;
	}



	@Override
	public List<Monitortype> getMonitorTypesByPage(Page page,
			boolean ignoreSearchConditions) {
		
		return this.typeDao.findMonitorTypesByPage(page,ignoreSearchConditions);
	}



	@Override
	public Monitortype getMonitoringType(String mtId) {
		return typeDao.getMonitoringType(mtId);
	}



	@Override
	public List<Monitortype> findMonitorTypes(String mpName) {
		
		return typeDao.findMonitorTypes(mpName);
	}



	@Override
	public Monitortype saveMonitorType(Monitortype type) {
		typeDao.saveMonitorType(type);
		return type;
	}



	@Override
	public Monitortype updateMonitorType(Monitortype point) {
		typeDao.updateMonitorType(point);
		return point;
	}



	@Override
	public List<Monitortype> findMonitorTypeByContainer(
			SearchContainer container, Page page) {
		
		return typeDao.findMonitorTypeByContainer(container,page);
	}



	@Override
	public boolean deleteTypeByIds(String[] mpIds) throws Exception {
		if ((mpIds == null) || (mpIds.length == 0))
			return false;
		for (int i = 0; i < mpIds.length; i++) {
			deleteTypeById(mpIds[i]);
		}
		return true;
	}



	private void deleteTypeById(String mpId) throws Exception{
	
		if (Utils.isEmpty(mpId)) {
			return ;
		}
		Monitortype point = this.typeDao.getMonitoringType(mpId);
		if (point != null) {
			deleteType(point);
		}
	
	}
	public boolean deleteType(Monitortype point) throws Exception {
		if (point == null)
			return false;
		return typeDao.delete(point);
	}
}
