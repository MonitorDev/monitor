package com.rongji.dfish.webapp.pub.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import com.rongji.dfish.engines.xmltmpl.Command;

public class CommunicateManager {
	private static CommunicateManager instance;
	private HashSet<CommandFetcher> fetchers;
	private CommunicateManager(){fetchers=new HashSet<CommandFetcher>();}
	public static CommunicateManager getInstance() {
		if (instance == null) {
			synchronized (CommunicateManager.class) {
				if (instance == null) {
					instance=new CommunicateManager();
				}
			}
		}
		return instance;
	}
	/**
	 * 注册命令执行器。
	 * 如果已经注册过了，那么返回真
	 * 否则返回假
	 * 
	 * @param cf
	 * @return
	 * @see java.util.HashSet#add(Object)
	 */
	public boolean registerFetcher(CommandFetcher cf){
		return fetchers.add(cf);
	}
	/**
	 * 获取这个人的所有命令
	 * @param pubUser
	 * @return
	 */
	public List<Command> fetchComman(String pubUser){
		List<Command> cmds=null;
		for (CommandFetcher cf : fetchers) {
			Queue<Command> list=cf.getCommands(pubUser);
			if(list!=null && list.size()>0){
				//添加命令里
				if(cmds==null){
					cmds=new ArrayList<Command>(list);
				}else{
					cmds.addAll(list);
				}
			}
		}
		return cmds;
	}
}