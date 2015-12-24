package com.rongji.websiteMonitor.webapp.task.helper.thread;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import dataserver.rmi.stub.UserManagerInterface;

public class PingUrl implements Runnable {
	Gson gson = new Gson();
	Map<String, String> map = new HashMap<String, String>();
	String ipPort = "80";
	String port;
	String url;
	String str;
	String ip;
	String serverName;
	String mpId;
	public PingUrl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PingUrl(String port,String url,String ip,String serverName,String mpId) {
		super();
		this.port = port;
		this.url = url;
		this.ip = ip;
		this.serverName = serverName;
		this.mpId = mpId;
	}

	@Override
	public void run() {
		try { 
//            Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(port)); 
//            UserManagerInterface userManager = (UserManagerInterface) registry.lookup("userManager");
            UserManagerInterface userManager = (UserManagerInterface) Naming.lookup("rmi://"+ip+":"+port+"/userManager");
            str = userManager.getPingDataJson(url, ipPort);
            System.out.println(str);
        }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

}
