package com.rongji.websiteMonitor.service.impl.message;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.rongji.websiteMonitor.common.ConfigConstants;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.persistence.AlarmInformHistory;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.service.message.SenderMailService;
import com.rongji.websiteMonitor.webapp.task.helper.thread.DetectionUrlThread;



public class SenderMailServiceImpl implements SenderMailService{

	private JavaMailSender mailSender;
	@Override
	public void sendMail(String from, String to, String title, String content) {
		// TODO Auto-generated method stub
		 //通过MailSender创建一个邮件  
        MimeMessage message=mailSender.createMimeMessage();  
        try {  
            //这里的MimeMessageHelper是用来封装邮件的一些基本信息  
            MimeMessageHelper helper=new MimeMessageHelper(message,true,"UTF-8");  
            helper.setFrom(from);//发件人的地址  
            helper.setTo(to);//收件人的地址  
            helper.setSubject(title);//邮件的主题  
            MimeBodyPart body=new MimeBodyPart();//邮件体  
          
            body.setContent(content,"text/html;charset=UTF-8");//);
            MimeMultipart mp=new MimeMultipart();  
            mp.addBodyPart(body);  
            message.setContent(mp);  
           
            mailSender.send(message);//发送邮件  
            System.out.println("邮件发送成功。。。。。");  
          
        } catch (MessagingException e) {  
            System.out.println("邮件发送失败。。。。。");  
            e.printStackTrace();  
        }  
	}
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public static void main(String[] args) {
		DetectionUrlThread detectionUrlThread = new DetectionUrlThread();
		String str = "[{\"content\":\"734844277@qq.com\",\"type\":\"eMail\"},{\"content\":\"15080458317,15080458317\",\"type\":\"phone\"}]";
		String [] email = detectionUrlThread.getNotifycation(str,Constants.EMAIL_TYPE);
		String [] tel = detectionUrlThread.getNotifycation(str,Constants.PHONE_TYPE);
		
		if(email!=null&&email.length>0){
			StringBuilder sb = new StringBuilder();
			for(String eml:email){
				ServiceLocator.getSenderMailService().sendMail(ConfigConstants.getInstance().getEmail(), eml, "ceshi", "测试");
			}
		}
//		if(tel!=null&&tel.length>0){
//			ServiceLocator.getSendNoteMesaageService().sendMessageForMany(tel, "测试");
//		}
	}


}
