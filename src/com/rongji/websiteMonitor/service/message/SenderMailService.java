package com.rongji.websiteMonitor.service.message;

public interface SenderMailService {
	public void sendMail(String from,String to,String title,String content);
}
