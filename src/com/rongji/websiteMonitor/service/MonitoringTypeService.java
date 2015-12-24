package com.rongji.websiteMonitor.service;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;

public interface MonitoringTypeService {
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
	public List<Monitortype> getMonitorTypesByPage(Page page, boolean ignoreSearchConditions);

	public Monitortype getMonitoringType(String mpId);

	public List<Monitortype> findMonitorTypes(String mpName);

	public Monitortype saveMonitorType(Monitortype type);

	public Monitortype updateMonitorType(Monitortype type);

	public List<Monitortype> findMonitorTypeByContainer(
			SearchContainer container, Page page);

	public boolean deleteTypeByIds(String[] mpIds)throws Exception;

}
