package com.rongji.websiteMonitor.platform.ums.style;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.DialogTemplate;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Skin;

public interface ItaskStyle {
	//取得皮肤
	Skin getSkin();
	/**
	 * 按钮栏(默认样式)的高度
	 * @return int 
	 */
	public int getButtonBarHeight();
	/**
	 * 按钮栏(默认样式)的高度
	 * @return int 
	 */
	public int getPagePanelHeight();
	/**
	 * 左边标题栏的高度 38
	 * @return int
	 */
	public int getNavTitleHeight();
	/**
	 * 左边标题的样式 tt-left
	 * @return String
	 */
	public String getNavTitleClass();
	
	/**
	 * 左边标题栏的宽度 38
	 */
	public int getNavWidth();
	
	/**
	 * 左边标题的边框样式 bd-gray
	 * @return String
	 */
	public String getNavBorderClass();

	/**
	 * 右边标题栏的高度 39
	 * @return int
	 */
	public int getMainTitleHeight();
	/**
	 * 右边标题的样式  tt-right
	 * @return String
	 */
	public String getMainTitleClass();
	
	/**
	 * 右边标题的边框样式  bd-gray
	 * @return String
	 */
	public String getMainBorderClass();

	/**
	 * 模块和外边框的边距 
	 * @return int
	 */
	public int getOutterPadding();
	
	public String getOutterPaddingClass();
	
	/**
	 * 取得这个风格下的对话框模板。
	 * 一般来说标准模板ID为f_std
	 * 仅有关闭按钮没有最大化的模板ID为f_std_x
	 * @param id
	 * @return
	 */
	public DialogTemplate getDialogTemplate(String id);
	
	/**
	 * 取得默认的弹出窗口
	 * @return
	 */
	public BaseView getPopupFormView();
	
	/**
	 * 构建主界面
	 * @param request HttpServletRequest
	 * @return BaseView
	 * @throws Exception 
	 */
	public BaseView buildIndexView(HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 * 构建一个左边为菜单，
	 * 右边为内容的壳。
	 * @param request HttpServletRequest
	 * @return BaseView
	 * @throws Exception
	 */
	public BaseView buildMenuAndContentShell();
	/**
	 * 通过它也可以得到国际化语言参数
	 * @return
	 */
	Locale getLocale();
	/**
	 * 通过它也可以得到国际化语言参数
	 * @return
	 */
	void setLocale(Locale locale);
	
	/**
	 * 得到是否来自移动设备
	 * @return
	 */
	boolean getMobile();
	/**
	 * 设置是否来自移动设备
	 * @return
	 */
	void setMobile(boolean isMoble);

	/**
	 * 
	 * 构建右边内容的壳，
	 * 右边为内容的壳。
	 * @param request HttpServletRequest
	 * @return BaseView
	 * @throws Exception
	 */
	BaseView buildContentShell();
	
	public static final int LIST_STYLE_LOOSE=1;
	public static final int LIST_STYLE_NORMAL=2;
	/**
	 * 构建一个主窗口的列表
	 * <table border='1'>
	 * <tr><td>主要按钮<br/>f_btn_left</td>
	 * <td>标题<br/>f_main_title_cat</td>
	 * <td>显示控制按钮<br/>f_main_title_right</td>
	 * </tr>
	 * <tr><td colspan='3'>内容<br/>f_grid<br/><br/><br/></td></tr>
	 * </tr><td colspan='3' align='right'>底部翻页栏<br/>f_page_bottom</td></tr>
	 * </table>
	 * @param style
	 * @param hasPage
	 * @return
	 */
	public Panel getGridContentPanel(int style,boolean hasPage);
}
