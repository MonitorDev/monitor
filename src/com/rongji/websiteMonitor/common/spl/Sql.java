package com.rongji.websiteMonitor.common.spl;

import java.util.List;

public interface Sql {
	
	public String toSql();
	
	public List<Object> getArgs();

}
