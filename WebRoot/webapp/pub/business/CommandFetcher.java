package com.rongji.dfish.webapp.pub.business;

import java.util.Queue;

import com.rongji.dfish.engines.xmltmpl.Command;

/**
 * 每次通讯的时候，都会向，所有注册过的CommandFetcher
 * 获取命令并把它传送到前端
 * @author I-TASK TEAM
 *
 */
public interface CommandFetcher {
	/**
	 * 获取命令
	 * @param pubUser
	 * @return
	 */
	Queue<Command> getCommands(String pubUser);
	int hashCode();
	boolean equals(Object o);
}
