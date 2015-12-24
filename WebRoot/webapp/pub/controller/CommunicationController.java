package com.rongji.dfish.webapp.pub.controller;

import static com.rongji.dfish.framework.FrameworkConstants.ENCODING;
import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.XMLObject;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.webapp.pub.business.CommunicateManager;
import com.rongji.itask7.web.pub.controller.ExceptionCaptureController;
/**
 * 该类负责与前端通讯。
 * 每次通讯的时候，从待发送命令中获得命令并发送到前端
 * 如果没有命令，则不执行任何动作。
 * HTTP 协议是无连接协议，所以，如果要模拟向前端发送命令，必须通过一些池化手段
 * 比如，应用程序APP1向命令池发送命令CMD1并指明接收者是A。
 * 那么每隔一定时间，当A通讯的时候。就会从命令池中找到所有是A的命令，并发送给他。
 * 发送完毕后，这些命令将清除。
 * @author I-TASK TEAM
 *
 */
public class CommunicationController extends ExceptionCaptureController{
	JSCommand NONE=new JSCommand(null,null);
	public ModelAndView msg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pubUser = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);

		List <Command> cmds= CommunicateManager.getInstance().fetchComman(pubUser);
//		MsgToolDefault.getInstance().addMsg("00000003", "00000003", "你好");
		if(cmds!=null){
			CommandGroup cg=new CommandGroup(null);
			for (Command command : cmds) {
				cg.add(command);
			}
			outPutXML(response, cg);
		}else{
			outPutXMLWithoutLog(response,NONE);
		}
		return null;
	}

	private static void outPutXMLWithoutLog(HttpServletResponse response, XMLObject xo) {
		String xml = xo.asXML();
		BufferedOutputStream bos = null;
		try {
			
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Content-Type", "text/xml; charset="
					+ ENCODING);
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(("<?xml version=\"1.0\" encoding=\"" + ENCODING
					+ "\"?>\r\n").getBytes(ENCODING));
			bos.write(xml.getBytes(ENCODING));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}
}
