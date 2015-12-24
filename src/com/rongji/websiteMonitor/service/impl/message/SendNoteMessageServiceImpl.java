package com.rongji.websiteMonitor.service.impl.message;

import com.rongji.websiteMonitor.common.ConfigConstants;
import com.rongji.websiteMonitor.service.impl.message.vcm.VCMService;
import com.rongji.websiteMonitor.service.message.SendNoteMessageService;

public class SendNoteMessageServiceImpl implements SendNoteMessageService  {


	@Override
	public void sendMessage(String tel, String content) {
		VCMService service = new VCMService(ConfigConstants.getInstance().getSendArgUserName(),
				ConfigConstants.getInstance().getSendArgPwd(),
				ConfigConstants.getInstance().getsengArgCharset());
		service.sendMessage(tel, content, "1234567");
	}

	@Override
	public void sendMessageForMany(String[] tels, String content) {
		VCMService service = new VCMService(ConfigConstants.getInstance().getSendArgUserName(),
				ConfigConstants.getInstance().getSendArgPwd(),
				ConfigConstants.getInstance().getsengArgCharset());
		service.sendMessage2Many(tels, content, "1234567");
		
	}
	
	

}
