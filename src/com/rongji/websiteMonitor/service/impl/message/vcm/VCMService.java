package com.rongji.websiteMonitor.service.impl.message.vcm;
import java.text.SimpleDateFormat;

import com.rongji.websiteMonitor.common.ConfigConstants;

/**
 * 封装即时通的短信发送接口
 * @author gyb
 *
 */
public class VCMService extends BaseWebService {
	public VCMService(String userName, String pwd, String charset) {
	        super();
	        setUserName(userName);
	        setPassword((MD5.md5(pwd)).toUpperCase());
	    
	       
	        setCharset(charset);
	    }
	 private int seqno = 1000; //流水号
	    public static final String SEND = //ConfigConstants.getInstance().getSendUrl();
	            "http://userinterface.vcomcn.com/Opration.aspx";
	    public static final String LOG = //ConfigConstants.getInstance().getLogUrl();
	            "http://userinterface.vcomcn.com/GetResult.aspx";
	    
	    /**
	     * 短信下发
	     * @param tel 号码  content 内容  seqNum 序列号
	     */
	    public String sendMessage(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"-1\" OpKind=\"0\">" +
	                  "\r\n");
	        sb.append("<E_Time></E_Time>\r\n");

	        sb.append("<Item>" + "\r\n");
	        sb.append("<Task>" + "\r\n");
	            sb.append("<Recive_Phone_Number>" + tel + "</Recive_Phone_Number>" +
	                      "\r\n");
	            sb.append("<Content>" + content + "</Content>" + "\r\n");

	            sb.append("<Search_ID>" + seqNum + "</Search_ID>" + "\r\n");
	            sb.append("</Task>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Group>");
	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }
	    
	    /**
	     * 同内容多号码批量提交
	     */
	    public String sendMessage2Many(  String[] tel, String content, String clientId) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"-1\" OpKind=\"51\">" +
	                  "\r\n");
	        sb.append("<E_Time></E_Time>\r\n");
	        sb.append("<Mobile>");
	        StringBuffer sbTel = new StringBuffer("");
	        for(String str:tel){
	        	if(sbTel.toString()==null||"".equals(sbTel.toString())){
	        		sbTel.append(str);
	        	}else{
	        		sbTel.append(",").append(str);
	        	}
	        	
	        }
	       
	        sb.append(sbTel.toString()).append("</Mobile>").append("\r\n");
	        sb.append("<Content>").append(content).append("</Content>\r\n");
	        sb.append("<ClientID>").append(clientId).append("</ClientID>\r\n");
	        sb.append("</Group>");
	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }
	    
	    
	    /**
	     * 获取账户余额
	     */
	    public String sendMessage4Account(  String[] tel, String content, String clientId) {
	    	 StringBuffer sb = new StringBuffer();
		        sb.append("<Root Service_Type=\"0\" >"+ 
		                  "\r\n");
		        sb.append("<Item>\r\n");
		        sb.append("<Account_Name>rjrj</Account_Name>");
		       sb.append("</Item>");
		        sb.append("</Root>");
		        System.out.println(sb.toString());
		        String ret = httpPost(LOG, "text/xml", sb.toString());
		        if (ret == null) { //链路有问题
		            return "31";
		        }
		        return ret;
	    }
	    
	    
	    /**
	     * 提交语音通知 
	     */
	    public String sendVoice(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" OpKind=\"2\">" +
	                  "\r\n");
	        sb.append("<B_Time></B_Time>\r\n");
	        sb.append("<E_Time></E_Time>\r\n");
	        sb.append("<Fail_Retry_Times>5</Fail_Retry_Times>\r\n");
	        sb.append("<R_Interval>500</R_Interval>\r\n");
	        sb.append("<Display_Number></Display_Number>\r\n");
	        sb.append("<Voice_Action_Num>3</Voice_Action_Num>\r\n");
	        sb.append("<Link_Tel>15060137970</Link_Tel>\r\n");
	     
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Task>" + "\r\n");
	            sb.append("<Tts_Content><![CDATA[你好呀，四度空间飞洒的金佛撒的金佛撒娇]]></Tts_Content>" +
	                      "\r\n");
	            sb.append("<Recive_Phone_Number1></Recive_Phone_Number1>" + "\r\n");

	            sb.append("<Recive_Phone_Number2>15080458317</Recive_Phone_Number2>" + "\r\n");
	            sb.append("<Recive_Phone_Number3>15059157307</Recive_Phone_Number3>" + "\r\n");
	            sb.append("<Search_ID>" +seqNum+ "</Search_ID>\r\n");
	            sb.append("</Task>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Group>");
	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }
	    
	    
	  
	    /**
	     * 获取结果状态报告 
	     */
	    public String sendMessage4Result(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Root Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" Service_Type=\"1\">");
	    
	     
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Search_ID>1234567</Search_ID>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Root>");
	        System.out.println(sb.toString());
	
	        String ret = httpPost("http://userinterface.vcomcn.com/getResult.aspx", "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }
	    
	  
	    /**
	     * 如何取外呼留言文件 
	     */
	    public String sendMessage6(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Root Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" Service_Type=\"3\">");
	    
	     
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Search_ID>1234567</Search_ID>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Root>");
	        System.out.println(sb.toString());
	
	        String ret = httpPost("http://userinterface.vcomcn.com/getResult.aspx", "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }

	        return ret;
	    }
	    
	    
	  //提交传真
	    public String sendMessage7(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" OpKind=\"1\">" +
	                  "\r\n");
	        sb.append("<B_Time></B_Time>\r\n");
	        sb.append("<E_Time></E_Time>\r\n");
	        sb.append("<Fail_Retry_Times>5</Fail_Retry_Times>\r\n");
	        sb.append("<R_Interval>500</R_Interval>\r\n");
	        sb.append("<Fax_From>15080458317</Fax_From>\r\n");
	        sb.append("<Fax_Hint_Text> <![CDATA[ 哈哈哈哈哈哈哈哈]]> </Fax_Hint_Text>\r\n");
	        sb.append("<Fax_File_Binary xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"binary.base64\">uN+078v5vbK1xLfwyb21xLz1t8p1d2x4emNzbGpkyr+087fyyPa1xA==</Fax_File_Binary>\r\n");
	        sb.append("<Fax_File_Ext>txt</Fax_File_Ext>\r\n");
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Task>" + "\r\n");
	            
	            sb.append("<Recive_Phone_Number>15080458317</Recive_Phone_Number>" + "\r\n");

	            sb.append("<Fax_Title>给你的</Fax_Title>" + "\r\n");
	            sb.append("<Fax_To>高艺斌</Fax_To>" + "\r\n");
	            sb.append("<Search_ID>" +seqNum+ "</Search_ID>\r\n");
	            sb.append("</Task>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Group>");
	        
	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }

	        return ret;
	    }
	    
	    
	    //如何获取传真发送结果
	    public String sendMessage8(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Root Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" Service_Type=\"1\">");
	    
	     
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Search_ID>1234567</Search_ID>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Root>");
	        System.out.println(sb.toString());
	
	        String ret = httpPost("http://userinterface.vcomcn.com/getResult.aspx", "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }

	        return ret;
	    }
	    
	    
	  //如何获取传真发送结果
	    public String sendMessage9(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Root Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" Service_Type=\"6\">");
	    
	     
	        sb.append("<Start_Time>2012-11-01</Start_Time>" + "\r\n");
	        sb.append("<End_Time>2012-12-02</End_Time>" + "\r\n");

	        sb.append("<PageSize>10</PageSize>\r\n");
	        sb.append("<PageIndex>1</PageIndex>\r\n");
	        sb.append("</Root>");
	
	        String ret = httpPost("http://userinterface.vcomcn.com/getResult.aspx", "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }

	        return ret;
	    }
	    
	  //召开会议
	    public String sendMessage10(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" OpKind=\"4\">" +
	                  "\r\n");
	        sb.append("<DisplayNum></DisplayNum>\r\n");
	        sb.append("<B_Time></B_Time>\r\n");
	        sb.append("<Sound_Record>0</Sound_Record>\r\n");
	        sb.append("<Search_ID>1234567</Search_ID>\r\n");
	    
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Person>" + "\r\n");
		        sb.append(" <Phone_Num>15080458317</Phone_Num>\r\n");
		        sb.append("<Action_Num>0</Action_Num>\r\n");
		        sb.append(" <Begin_Voice></Begin_Voice>\r\n");
	            sb.append("<SearchID>" +seqNum+ "</SearchID>\r\n");
	            sb.append("</Person>" + "\r\n");
	            sb.append("<Person>" + "\r\n");
		        sb.append(" <Phone_Num>15060137970</Phone_Num>\r\n");
		        sb.append("<Action_Num>1</Action_Num>\r\n");
		        sb.append(" <Begin_Voice></Begin_Voice>\r\n");
	            sb.append("<SearchID>" +seqNum+ "</SearchID>\r\n");
	            sb.append("</Person>" + "\r\n");
	            sb.append("</Item>" + "\r\n");
	        sb.append("</Group>");
	        

	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }

	        return ret;
	    }
	    
	    
	  //会议过程中增家人员
	    public String sendMessage11(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\"  MeetingID=\""+content+"\" OpKind=\"5\">" +
	                  "\r\n");
	     
	        sb.append("<Person>" + "\r\n");
		        sb.append(" <Phone_Num>15860285502</Phone_Num>\r\n");
		        sb.append("<Action_Num>0</Action_Num>\r\n");
		        sb.append(" <Begin_Voice></Begin_Voice>\r\n");
	            sb.append("<SearchID>" +seqNum+ "</SearchID>\r\n");
	            sb.append("</Person>" + "\r\n");
	   
	         
	        sb.append("</Group>");
	        

	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }
	    
	  //会议过程中进行角色切换
	    public String sendMessage12(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\"  MeetingID=\""+content+"\" OpKind=\"6\">" +
	                  "\r\n");
	     
	        sb.append("<Person>" + "\r\n");
		        sb.append(" <Phone_Num>15080458317</Phone_Num>\r\n");
		      
		        sb.append(" <ControlAction>5</ControlAction>\r\n");
	            sb.append("</Person>" + "\r\n");
	   
	         
	        sb.append("</Group>");
	        
	       

	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }

	  //会议过程中进行角色切换
	    public String sendMessage13(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Group Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\"  OpKind=\"8\">" +
	                  "\r\n");
	     
	        sb.append("<Item>" + "\r\n");
		        sb.append("<MeetingID>"+content+"</MeetingID>\r\n");
		      
	            sb.append("</Item>" + "\r\n");
	   
	         
	        sb.append("</Group>");
	        
	       

	        System.out.println(sb.toString());
	        String ret = httpPost(SEND, "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }
	        return ret;
	    }
	    
	    //会议结果查询
	    public String sendMessage14(  String tel, String content, String seqNum) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("<Root Login_Name=\"" + getUserName() + "\" Login_Pwd=\"" +
	                  getPassword() + "\" InterFaceID=\"0\" Service_Type=\"4\">");
	    
	     
	        sb.append("<Item>" + "\r\n");
	        sb.append("<Search_ID>1234567</Search_ID>" + "\r\n");

	        sb.append("</Item>" + "\r\n");
	        sb.append("</Root>");
	        System.out.println(sb.toString());
	
	        String ret = httpPost("http://userinterface.vcomcn.com/getResult.aspx", "text/xml", sb.toString());
	        if (ret == null) { //链路有问题
	            return "31";
	        }

	        return ret;
	    }
	    
	public static void main(String[] args) {
		VCMService s = new VCMService("rjrj","ab8888","gbk");
		String content = "的开发商更坚实的看过的开发苹果的开发水平跟客人看过的【分开过的反馈给答复个地方看过的发生开工的疯狂公开的方式谁看过；了看个人口的疯狂广阔的 kdfkgrekdf'h'jl脸上的风的gdfgdfjklg";
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rel = "";
        rel = s.sendMessage("18860131216",content.substring(0,50),"1234567");
////        System.out.println(rel);
////        
////        System.out.println();
//        //,"15860285502","1560137970","15059157307"
//        rel = s.sendMessage2(tels,content+"\n"+content+"\n"+content+"\n"+content+"\n"+content+"\n"+content,"1234567");
//        System.out.println(sdf.format(new java.util.Date()));
//        System.out.println(rel);
//		rel = s.sendMessage3(tels,content,"1234567");
//		s.httpPost(LOG, "text/xml", "");
//        rel =  s.sendMessage14("", "", "1234567");
     //   rel =  s.sendMessage11("", rel, "1234567");
       
        System.out.println(rel);
      
  
        System.out.println(sdf.format(new java.util.Date()));
	}

}
