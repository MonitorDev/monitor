package com.rongji.websiteMonitor.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SNMPConstants {

	// cpu使用
	public static final String CPU_OID = "1.3.6.1.4.1.2021.11.10.0";

	//系统内存总量
	public static final String MEMORYSIZE_OID = "1.3.6.1.4.1.2021.4.5.0";
	// 内存使用率
	public static final String MEMORY_OID = "1.3.6.1.4.1.2021.4.6.0";

	// jvm堆使用
	public static final String JVMHEAD_OID = "1.3.6.1.4.1.42.2.145.3.163.1.1.2.11.0";
	//jvm总量
	public static final String JVMHEADSIZE_OID = "1.3.6.1.4.1.42.2.145.3.163.1.1.2.13.0";

	// jvm线程数
	public static final String JVMTHREAD_OID = "1.3.6.1.4.1.42.2.145.3.163.1.1.3.1.0";

	// 网络流量
	public static final String IFIN_OID = "1.3.6.1.2.1.2.2.1.10.2";
	public static final String IFOUT_OID = "1.3.6.1.2.1.2.2.1.16.2";
	
	//IO流量
	public static final String DISKIONREAD_OID = "1.3.6.1.4.1.2021.13.15.1.1.3.26";
	public static final String DISKIONWRITTEN_OID = "1.3.6.1.4.1.2021.13.15.1.1.4.26";

	//系统进程
	public static final String SYSTEMPROCESS_OID = "1.3.6.1.2.1.25.1.6.0";
	
	
	//磁盘空间
	public static final String STORAGESIZE_OID = "1.3.6.1.2.1.25.2.3.1.5.31";
	public static final String STORAGEUSED_OID = "1.3.6.1.2.1.25.2.3.1.6.31";
	
	//IO流量
	//public static final String IOREAD_OID = "1.3.6.1.4.1.2021.13.15.1.1.5.26";
	//public static final String IOWRITEEN_OID = "1.3.6.1.4.1.2021.13.15.1.1.6.26";
	
	private static String address = "";

	public static String getAddress() {
		if ("".equals(address)) {
			Properties prop = new Properties();
			InputStream in = null;
			try {
				in = SNMPConstants.class.getClassLoader().getResourceAsStream("/snmp.properties");
				//in = Object.class.getResourceAsStream("/snmp.properties");
				prop.load(in);
				address = prop.getProperty("address").trim() + "/" + prop.getProperty("port");
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(in != null) {
					try {
						in.close();
						in = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return address;
	}
}
