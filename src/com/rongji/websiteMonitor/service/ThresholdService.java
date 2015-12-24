package com.rongji.websiteMonitor.service;


import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.persistence.Threshold;

public interface ThresholdService{

	public void saveThreshold(Threshold threshold);
	public Threshold getThresholdById(String id);
	public List<Threshold> findAllThreshold(String type, boolean isIgnoreUsable);
	public void updateThreshold(Threshold threshold);
	public List<Threshold> findThresholdByName(String type, String name);
	
	public void deleteThreshold(Threshold threshold);
	
	public List<Threshold> findThresholdByPage(String type, Page page);
	
	public List<Threshold> findThresholdByPage(Page page);
}
