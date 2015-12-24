package com.rongji.websiteMonitor.dao;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;

public interface MonitoringPointDao extends PubCommonDAO {

	public List<MonitoringPoint> findMonitorPointsByPage(Page page,
			boolean ignoreSearchConditions);

	public MonitoringPoint getMonitoringPoint(String mpId);

	public List<MonitoringPoint> findMonitorPoints(String mpName);

	public void saveMonitorPoint(MonitoringPoint point);

	public void updateMonitorPoint(MonitoringPoint point);

	public List<MonitoringPoint> findMonitorPointByContainer(
			SearchContainer container, Page page);
	
	public boolean delete(MonitoringPoint point)throws Exception;

	/**
	 * 根据网络类型分页查找
	 * @param page
	 * @param ignoreSearchConditions
	 * @param isExternal 网络类型（0：内部网络；1:外部网络）
	 * @return
	 */
	public List<MonitoringPoint> findMonitorPointsByPageAndIsExternal(Page page, boolean ignoreSearchConditions, String isExternal);
}
