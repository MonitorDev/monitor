package com.rongji.websiteMonitor.service;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;

public interface MonitoringPointService {
	/**
	 * 返回所有作者列表(支持带分页列表)
	 * 
	 * @param page
	 *            分页对象
	 * @param ignoreSearchConditions
	 *            如果为true，表示忽略查询条件，按初始条件查询。
	 * @return
	 * @throws Exception
	 */
	public List<MonitoringPoint> getMonitorPointsByPage(Page page, boolean ignoreSearchConditions);

	public MonitoringPoint getMonitoringPoint(String mpId);

	public List<MonitoringPoint> findMonitorPoints(String mpName);

	public MonitoringPoint saveMonitorPoint(MonitoringPoint point);

	public MonitoringPoint updateMonitorPoint(MonitoringPoint point);

	public List<MonitoringPoint> findMonitorPointByContainer(
			SearchContainer container, Page page);

	public boolean deletepointByIds(String[] mpIds)throws Exception;

	/**
	 * 根据网络类型分页查找
	 * @param page
	 * @param ignoreSearchConditions
	 * @param isExternal 网络类型（0：内部网络；1:外部网络）
	 * @return
	 */
	public List<MonitoringPoint> findMonitorPointsByPageAndIsExternal(Page page, boolean ignoreSearchConditions, String isExternal);
}
