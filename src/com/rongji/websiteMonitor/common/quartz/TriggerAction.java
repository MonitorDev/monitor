package com.rongji.websiteMonitor.common.quartz;

import java.util.Locale;

/**
 * 定时器触发的动作
 * 在数据库中配置的动作
 * 触发的具体动作必须实现这个接口。
 * 这样就可以通过类反射找到这个动作
 * @author I-TASK team
 * @since dfish-foundation V0.9
 */
public interface TriggerAction {
	/**
	 * 执行动作
	 * @param config 配置的参数将以String的形式传递给这个动作。一般来说这个String是以XML方式出现。但由于这个动作和配置是自行决定的。所以不保证这个值都是有效的。
	 * @throws Exception
	 */
	public void execute(String config)throws Exception;
	/**
	 * 取得这个TriggerAction的描述，以便运维的人员可以知道这个是什么东西
	 * @param loc 语言和地区
	 * @return
	 */
	public String getDescription(Locale loc);
}
