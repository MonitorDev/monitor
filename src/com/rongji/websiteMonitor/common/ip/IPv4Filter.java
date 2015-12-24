package com.rongji.websiteMonitor.common.ip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rongji.dfish.base.Utils;


/**
 * IPv4Filter 是一个用于快速定位
 一 配置
 IP过滤可以配置1个或多个IP(段)。
 每个配置必须用分号;结尾，每个配置中可以有空格或者换行。
 但一个数字中间是不可以有空格或换行的。
 全角的字符将会自动全部替换成半角字符
 比如
 192.168.21.2;  192.168.21.11;
 192.168.14.100;
 配置IP段则用-号隔开。
 比如192.168.21.2 - 192.168.21.16;
 其中如果是从0-255则还可以用通配符*代替
 比如192.168.21.*;
 用通配符的时候，如果高位是*则低位不管是什么，都将当成*处理
 比如说 10.206.*.0 将会被看作10.206.*.*;
 即 10.206.0.0 - 10.206.255.255
 范围包含关系。 如果一个配置里面同时包含
 192.168.21.*;192.168.*.*; 是合法的。但是范围小的部分将会被忽略。

二 数据结构
 IP配置被解析成一个二维长整型数组
 [ [开始IP数值, 结束IP数值], [开始IP数值, 结束IP数值], ...... [开始IP数值, 结束IP数值] ];
 其中IP数值 的算法 为 第一位<<24 +第二位<<16+第三位<<8+第四位

三 算法
 a)读取配置。
 如果单IP, 开始IP数值等于结束IP数值。
 如果是通配符IP，在指定位置增加0和255.获得开始IP数值和结束IP数值。
 否则就正常解析开始IP数值和结束IP数值

 b)优化配置
 这个数组，需要按开始IP的大小排序。
 如果前一个的结束IP数值与后一个的开始IP数值相连,或大等于其数值，则需要合并IP段
 
 c)使用配置
 当进行IP校验的时候。输入的IP，也转化成IP数值。
 然后判断是否落在过滤IP的区间内。
 如果过滤IP数值数组的长度小等于3 则直接循环判断。
 否则先二分查找，然后在判断是否落在IP过滤范围内。
 
 * @author V1.0 杨刚 V1.1 林利炜
 *
 */
public class IPv4Filter {

	/**
	 * IP字符串转换为长整型
	 * 
	 * @param ip
	 * @return
	 * @throws Exception 
	 */
	public static long transform(String ip) throws Exception {
		String[] sIP = ip.trim().split("\\.");
		if(Long.parseLong(sIP[0])>255||Long.parseLong(sIP[1])>255||Long.parseLong(sIP[2])>255||Long.parseLong(sIP[3])>255){
			throw new Exception("ip段超过255");
		}//update in 2013-01-17
		long ipNum = (Long.parseLong(sIP[0]) << 24)
				| (Long.parseLong(sIP[1]) << 16)
				| (Long.parseLong(sIP[2]) << 8) | (Long.parseLong(sIP[3]));
		return ipNum;
		
	}
	/**
	 * IP长整型转换为字符串
	 * @param ip
	 * @return
	 */
	public static String transform(long ip) {
		int i1=(int)(ip>>24)& 0xFF;
		int i2=(int)(ip>>16)& 0xFF;
		int i3=(int)(ip>>8)& 0xFF;
		int i4=(int)(ip)& 0xFF;
		return i1+"."+i2+"."+i3+"."+i4;
	}
	private long[][] ipScope;
	
	public long[][] getIpScope() {
		return ipScope;
	}
	/**
	 * 转换含有*的IP字符串
	 * 
	 * @param ip
	 * @return
	 */
	private static String IPsection(String ip) {
		String[] s = ip.trim().split("\\.");
		if (s[0].contains("*")) {
			ip = "0.0.0.0-255.255.255.255";
		} else if (s[1].contains("*")) {
			ip = s[0] + ".0.0.0-" + s[0] + ".255.255.255";
		} else if (s[2].contains("*")) {
			ip = s[0] + "." + s[1] + ".0.0-" + s[0] + "." + s[1] + ".255.255";
		} else if (s[3].contains("*")) {
			ip = s[0] + "." + s[1] + "." + s[2] + ".0-" + s[0] + "." + s[1]
					+ "." + s[2] + ".255";
		}
		return ip;
	}

	/**
	 * 转换含有-的ip串
	 * 
	 * @param ip
	 * @return
	 */
	private static String[] IPpart(String ip) {
		String[] s = ip.trim().split("\\-");
		return s;
	}

	/**
	 *[开始IP，结束IP]
	 * 
	 * 
	 */
	static class IpSection {
		long startIP;
		long endIP;

		public long getStartIP() {
			return startIP;
		}

		public void setStartIP(long startIP) {
			this.startIP = startIP;
		}

		public long getEndIP() {
			return endIP;
		}

		public void setEndIP(long endIP) {
			this.endIP = endIP;
		}

		public long[] toArray() {
			return new long[]{startIP,endIP};
		}
		public String toString(){
			return transform(startIP)+"-"+transform(endIP);
		}
	}

	/**
	 * 默认构造函数
	 * @param IPConfig
	 * @throws Exception 
	 */
	public IPv4Filter(String IPConfig) throws Exception{
		String IPs[] = IPConfig.split(";");
		List<IpSection> ipCig = new ArrayList<IpSection>();
		for (int i = 0; i < IPs.length; i++) {
			IpSection ips = new IpSection();
			if(IPs[i]!=null&&!("".equals(IPs[i]))){//update in 2013-01-16 cr
				try{
					// 如果是通配符IP，在指定位置增加0和255.获得开始IP数值和结束IP数值。
					if ((IPs[i].contains("*"))) {
						String str = IPsection(IPs[i]);
						String[] str1 = IPpart(str);
						ips.setStartIP(transform(str1[0]));
						ips.setEndIP(transform(str1[1]));
						ipCig.add(ips);
					}
					// 正常解析开始IP数值和结束IP数值
					else if (IPs[i].contains("-")) {
						String[] str = IPpart(IPs[i]);
						if(transform(str[0])>transform(str[1])){
							ips.setStartIP(transform(str[1]));
							ips.setEndIP(transform(str[0]));
						}else{
							ips.setStartIP(transform(str[0]));
							ips.setEndIP(transform(str[1]));
						}
						
						ipCig.add(ips);
					}
					// 如果是单IP，开始IP数值等于结束IP数值
					else  {//if (!(IPs[i].contains("*")) && !(IPs[i].contains("-")))
						ips.setStartIP(transform(IPs[i]));
						ips.setEndIP(transform(IPs[i]));
						ipCig.add(ips);
					}
				}catch (Exception ex){
					throw ex;
				}
			}
			
		}
		this.ipScope= combineIPs(ipCig);
		
		System.out.println("====init====");
		for(long[] ip:ipScope){
			System.out.println(transform(ip[0])+"("+ip[0]+")-"+transform(ip[1])+"("+ip[1]+")");
		}
		System.out.println("====init end====");
	}
	
	/**
	 * make in zdom
	 * 取组合好后的ip集
	 * @return
	 */
	public String getIPconfig(){
		long[][] ips = getIpScope();
		if(Utils.notEmpty(ips)){
			String ipconfig = "";
			for(long[] ip : ips){
                if((ip[1]-ip[0])==0){
                	ipconfig += transform(ip[0])+";";//update in 2013-01-17 cr
                }else if(ip[1]-ip[0]>=65280 && ip[1]-ip[0]<65535){
					String[] temp0 = transform(ip[0]).split("\\.");
					ipconfig += temp0[0] + "." + temp0[1] + "." + "*" + "." + temp0[3] + "-";
					String[] temp1 = transform(ip[1]).split("\\.");
					ipconfig += temp1[0] + "." + temp1[1] + "." + "*" + "." + temp1[3]+";";
				}else if(ip[1]-ip[0]==65535){
					String[] temp0 = transform(ip[0]).split("\\.");
					String[] temp1 = transform(ip[1]).split("\\.");//update in 2013-01-21 cr
					if(temp0[1].equals(temp1[1])){
						ipconfig += temp0[0] + "." + temp0[1] + "." + "*.*;";
					}else{
						ipconfig += transform(ip[0]) + "-" + transform(ip[1]) + ";";
					}
				}else if(ip[1]-ip[0]==255){
					String[] temp0 = transform(ip[0]).split("\\.");
					String[] temp1 = transform(ip[1]).split("\\.");
					if(temp0[2].equals(temp1[2])){
						ipconfig += temp0[0] + "." + temp0[1] + "." + temp0[2] + ".*;";
					}else{
						ipconfig += transform(ip[0]) + "-" + transform(ip[1]) + ";";
					}
				}else{
					ipconfig += transform(ip[0]) + "-" + transform(ip[1]) + ";";
				}
			}
			return ipconfig;
		}
		return null;
	}
	

	/**
	 * 合并IP段
	 * 
	 * @param IPConfig
	 * @return
	 */
	private static long[][] combineIPs(List<IpSection> IPConfig) {		
		// 排序
		Collections.sort(IPConfig,new java.util.Comparator<IpSection>(){
			public int compare(IpSection o1, IpSection o2) {
				if(o1.startIP>o2.startIP)return 1;
				else if(o1.startIP<o2.startIP)return -1;
				return 0;
			}
		});
	
		// 合并
		for (int i = 0; i < IPConfig.size(); i++) {
			IpSection ip = (IpSection) IPConfig.get(i);
			for (int j = i + 1; j < IPConfig.size(); j++) {
				IpSection ip1 = (IpSection) IPConfig.get(j);
				if (ip.getEndIP() + 1 >= ip1.getStartIP()) {
					ip.setStartIP(ip.getStartIP());
					ip.setEndIP(Math.max(ip.getEndIP(), ip1.getEndIP()));
					IPConfig.remove(j);
					j--;
				}
			}
		}
		long[][] ipScope =new long[IPConfig.size()][2];
		for(int i=0;i<IPConfig.size();i++){
			ipScope[i]=IPConfig.get(i).toArray();
		}
		return ipScope;
	}
	
	/**
	 * 是否匹配
	 * 
	 * @param ip
	 * @param IPConfig
	 * @return
	 * @throws Exception 
	 */
	public boolean match(String ip) throws Exception {
		long ipL = transform(ip);
		if (ipScope.length <= 3) {
			for (int i = 0; i < ipScope.length; i++) {
				if(ipL > ipScope[i][1])continue;
				return ipL >= ipScope[i][0];
			}
			return false;
		} else {
			return binSearch(ipL);
		}
	}

	/**
	 * 二分查找，提高查找效率
	 * 
	 * @param IPConfig
	 * @param ipL
	 * @return
	 */
	private boolean binSearch(long ipL) {
		int mid=0;
		int start = 0;
		int end = ipScope.length - 1;
		while (start <= end) {
			mid = (end - start) / 2 + start;
			if (ipL < ipScope[mid][0]) {
				end = mid - 1;
			} else if (ipL > ipScope[mid][1]) {
				start = mid + 1;
			} else {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		/*IPv4Filter filter=new IPv4Filter("218.5.2.250;59.56.185.145;127.0.0.5-127.0.0.1;" +
				"192.168.21.28");*/
		//IPv4Filter filter=new IPv4Filter("45");

		//IPv4Filter filter = null;
		//System.out.println("000wwwwwww000");
		IPv4Filter filter = new IPv4Filter("192.169.101.5;192.168.101.4");
		System.out.println(filter.getIPconfig());
		//System.out.println(filter.match("192.16.1"));
		//System.out.println(filter.getIPconfig());
		//System.out.println(filter.match("59.0.0.245-59.0.0.250"));
		/*System.out.println("----------------"+filter);
		System.out.println(filter.match("192.168.3.9"));
		//System.out.println(filter.match("192.18"));
		System.out.println(filter.getIPconfig());*/
	}

}

