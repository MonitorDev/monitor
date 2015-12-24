package com.rongji.websiteMonitor.service.impl;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.spl.SearchContainer.Op;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.MonitoringPointDao;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.service.MonitoringPointService;

public class MointoringPointServiceImpl implements MonitoringPointService {

	private MonitoringPointDao pointDao;
	
	
	
	public MonitoringPointDao getPointDao() {
		return pointDao;
	}



	public void setPointDao(MonitoringPointDao pointDao) {
		this.pointDao = pointDao;
	}



	@Override
	public List<MonitoringPoint> getMonitorPointsByPage(Page page,
			boolean ignoreSearchConditions) {
		
		return this.pointDao.findMonitorPointsByPage(page,ignoreSearchConditions);
	}



	@Override
	public MonitoringPoint getMonitoringPoint(String mpId) {
		return pointDao.getMonitoringPoint(mpId);
	}



	@Override
	public List<MonitoringPoint> findMonitorPoints(String mpName) {
		
		return pointDao.findMonitorPoints(mpName);
	}



	@Override
	public MonitoringPoint saveMonitorPoint(MonitoringPoint point) {
		pointDao.saveMonitorPoint(point);
		return point;
	}



	@Override
	public MonitoringPoint updateMonitorPoint(MonitoringPoint point) {
		pointDao.updateMonitorPoint(point);
		return point;
	}



	@Override
	public List<MonitoringPoint> findMonitorPointByContainer(
			SearchContainer container, Page page) {
		
		return pointDao.findMonitorPointByContainer(container,page);
	}



	@Override
	public boolean deletepointByIds(String[] mpIds) throws Exception {
		if ((mpIds == null) || (mpIds.length == 0))
			return false;
		for (int i = 0; i < mpIds.length; i++) {
			deletePointById(mpIds[i]);
		}
		return true;
	}



	private void deletePointById(String mpId) throws Exception{
	
		if (Utils.isEmpty(mpId)) {
			return ;
		}
		MonitoringPoint point = this.pointDao.getMonitoringPoint(mpId);
		if (point != null) {
			deleteAuthor(point);
		}
	
	}
	public boolean deleteAuthor(MonitoringPoint point) throws Exception {
		if (point == null)
			return false;
		return pointDao.delete(point);
	}



	@Override
	public List<MonitoringPoint> findMonitorPointsByPageAndIsExternal(
			Page page, boolean ignoreSearchConditions, String isExternal) {
		return pointDao.findMonitorPointsByPageAndIsExternal(page, ignoreSearchConditions, isExternal);
	}
}
