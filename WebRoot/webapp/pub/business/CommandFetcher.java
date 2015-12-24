package com.rongji.dfish.webapp.pub.business;

import java.util.Queue;

import com.rongji.dfish.engines.xmltmpl.Command;

/**
 * ÿ��ͨѶ��ʱ�򣬶���������ע�����CommandFetcher
 * ��ȡ����������͵�ǰ��
 * @author I-TASK TEAM
 *
 */
public interface CommandFetcher {
	/**
	 * ��ȡ����
	 * @param pubUser
	 * @return
	 */
	Queue<Command> getCommands(String pubUser);
	int hashCode();
	boolean equals(Object o);
}
