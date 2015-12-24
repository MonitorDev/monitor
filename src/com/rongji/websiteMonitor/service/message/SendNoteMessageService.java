package com.rongji.websiteMonitor.service.message;

public interface SendNoteMessageService {
	public void sendMessage(String tel,String content);
	
	public void sendMessageForMany(String[] tels, String content);
}
