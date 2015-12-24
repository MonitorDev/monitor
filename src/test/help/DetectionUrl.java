package test.help;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import dataserver.rmi.stub.UserManagerInterface;



public class DetectionUrl implements Runnable {
	Gson gson = new Gson();
	Map<String, String> map = new HashMap<String, String>();
	String port;
	String url;
	String str;
	String ip;
	String serverName;
	String mpId;
	long beginTime = 0L;
	boolean interrupted = false;
	boolean isOver = false;
	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public DetectionUrl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DetectionUrl(String port,String url,String ip,String serverName,String mpId) {
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
			if(interrupted) {
                throw new InterruptedException("正式处理前线程已经被请求中断");
            }
            UserManagerInterface userManager = (UserManagerInterface) Naming.lookup("rmi://"+ip+":"+port+"/userManager");
            if(userManager!=null){
//            	Thread thread = new Thread(new MonitorThread());
            	str = userManager.getNetWorkDataJson(url);
            }else{
            	str = "{\"address\":\""+this.serverName+"\",\"ip\":\"无响应\"," +
				"\"result\":\"无响应\",\"status\":\"无响应\",\"responseTime\":\"无响应\",\"size\":\"无响应\"}";
            }
//            registry.lookup("userManager");
            
//            System.out.println(str);
        } catch (RemoteException e) { 
            e.printStackTrace();
            map.put("address",this.serverName);
			map.put("ip","无响应");
			map.put("result","无响应");
			map.put("status","无响应");
			map.put("responseTime","无响应");
			map.put("size","无响应");
			str = gson.toJson(map);
			
        } catch (NotBoundException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
            map.put("address",this.serverName);
			map.put("ip","无响应");
			map.put("result","无响应");
			map.put("status","无响应");
			map.put("responseTime","无响应");
			map.put("size","无响应");
			str = gson.toJson(map);
        } catch (MalformedURLException e) {
        	e.printStackTrace(); 
            map.put("address",this.serverName);
 			map.put("ip","无响应");
 			map.put("result","无响应");
 			map.put("status","无响应");
 			map.put("responseTime","无响应");
 			map.put("size","无响应");
 			str = gson.toJson(map);
		} catch (InterruptedException e) {
			e.printStackTrace();
			map.put("address",this.serverName);
 			map.put("ip","无响应");
 			map.put("result","无响应");
 			map.put("status","无响应");
 			map.put("responseTime","无响应");
 			map.put("size","无响应");
 			str = gson.toJson(map);
		} 

	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

}
