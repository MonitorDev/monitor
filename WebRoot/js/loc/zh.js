/*
 *	loc.js
 *	---------------------------------------------------------
 *
 *	---------------------------------------------------------
 *
 *	author: Chen MingYuan
 *	for iTASK 5.0
 *	Copyright (c) 2005 - 2006 iTASK Team
 *
 *	created : 2005-10-16
 * modified : 2006-9-21
 *
 */

var Loc = {

colon	: '：',
to		: '到',
none	: '无',
yes		: '是',
no		: '否',
confirm	: '确定',
inherit	: '继承',
search	: '搜索',
query	: '查询',
choose	: '选择',
save	: '保存',
remove	: '删除',
cancel	: '取消',
create	: '新建',
add		: '添加',
share	: '共享',
edit	: '编辑',
remind	: '提醒',
empty	: '清空',
plan	: '计划',
report	: '审批',
execute	: '执行',
finish	: '完成',
pigeonhole : '归档',
person	: '人员',
dept	: '部门',
group	: '群组',
senior	: '高级',
grade	: '评分',
unassort : '未分类',
operfail : '操作失败',
opertip	 : '操作提示',
saveclose : '保存并关闭',
nodata	: '暂无数据',
measure : '尺寸',
click_zoom	: '点击放大',
search_end	: '搜索结束',
newfolder : '新文件夹',
cancel_remove : '取消删除',
choose_onlyone : '只能选择一条记录',
choose_atleastone	: '请至少选择一项',
choose_min		: '"{0}"至少选择{1}项',
choose_max		: '"{0}"最多选择{1}项',
internet_error	: '抱歉，该操作无法执行，造成的原因可能有：\n\n1、该数据已被删除或转移\n2、您没有相关权限\n3、网络连接出现了问题',
xml_unmatch		: 'XML格式不符合需要',
loading			: '正在加载..',
wait			: '请稍候..',
submiting       : '数据提交中，请稍候..',
online_player : '在线播放器',
tree_movefail1	: '无法移动：目标文件夹和源文件夹相同',
tree_movefail2	: '无法移动：目标文件夹是源文件夹的子文件夹',
tree_target	: '请选择目标文件夹',
affair1 : '重要并且紧急',
affair2 : '重要但是不急',
affair3 : '不重要但较急',
affair4 : '不重要也不急',
debug_sorry : '很抱歉，系统发生了错误',
has_exists : '已经存在，不能重复',
value   : '值',
title   : '标题',
text    : '文本',
option  : '选项',
default_value : '默认值',
move_up : '上移',
move_down : '下移',

debug_1 : '#1: 没有配置对话框模板',
debug_2 : '#2: 插件类创建失败',
debug_3 : '#3: JS Command执行失败',
debug_4 : '#4: 视图中没有定义此命令\n\ncommand id: {0}\n view path: {1}',

print_preview : '打印预览',

page_next : '下一页',
page_prev : '上一页',

form : {
	notnull	: '{0}不能为空',
	onlyone : '{0}中只能选择一条记录',
	toolong : '{0}的字符长度限制为{1}个字节，您至少需要减去{2}个字节',
	toolong_2 : '{0}的长度超出限制，请删减一些字符',
	nomatch	: '{0}的格式不符合规范，或包含非法字符',
	number_range : '{0}中的数字应满足以下条件：\n\n1、是一个有效数字\n2、>= {1}\n3、<= {2}',
	time_error	: '{0}的时间格式错误',
	time_start_end : '{0}应满足以下条件：\n1、起始时间 {1} 结束时间',
	max_attach : '附件数量不能超过{0}',
	add_file  : '添加附件',
	add_file1 : '继续添加附件',
	add_multext : '继续添加',
	invalid_obj : '{0}包含无效选项，请重新输入或选择！',
	submit_error : '抱歉，由于存在无法访问的表单信息，该操作已中止'
},

tab : {
	close_current : '关闭当前',
	close_other	  : '关闭其他',
	close_all	  : '关闭所有'
},

weekname : ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
date : {
	year	: '年',
	month	: '月',
	day		: '日',
	sky		: '天',
	hour	: '时',
	minute	: '分',
	second	: '秒',
	today	: '今天',
	weeknow	: '本周',
	monthnow: '本月',
	calendar_title	: ['日','一','二','三','四','五','六'],
	todayis : '今天是yyyy年m月d日 {0}',
	ym : '<a href=javascript: onclick={0} style=color:#000>{1} 年</a> <a href=javascript: onclick={2} style=color:#000>{3} 月</a>',
	y  : '<a href=javascript: onclick={0} style=color:#000>{1} 年</a>',
	y1 : 'yyyy 年'
},

errors : {
	endtime_before_starttime : '结束时间不能早于开始时间',
	editor_invalid : '需转换为编辑状态后才能使用编辑功能或者提交表单！',
	can_not_open_word : '不能打开WORD,请确认:\n1 您的OFFICE是否安装正确，而且现在没有损坏\n2 您必须把当前站点设置为可信站点\n3 在Internet选项里->Internet->自定义级别里对没有标为安全的ActiveX 是否为起用或提示 '
},

confirms : {
	remove : '确定要删除吗？',
	giveup_modify : '放弃目前所做的修改吗？',
	cant_revert : '该操作不可恢复，确定吗？',
	exec : '是否确定要执行当前操作？',
	empty : '是否确定要清空？'
},

debug : {
	view_more : '点击“确定”，可在新窗口查看详细错误信息'
},

week_tree_unexam : '未批示',
export_rtf : '导出成RTF',
export_pdf : '导出成PDF',
search_result : '搜索结果',
search_nomatch : '没有找到匹配项',
search_condi : '搜索依据',
all_extend : '全部展开',
all_collapse : '全部折叠',
online_person : '在线人员',
system_msg : '该消息由系统自动产生',

invalid_part : '{0}中该数据无效：{1}',
start_with : '从{0}开始',
from_to : '从{0}到{1}',


//modules
topweb : {
	logout : '退出',
	desk : '快捷桌面',
	newmsg : '您有新消息',
	config : '配置',
	favor : '收藏夹',
	half : '半屏显示收藏夹',
	min : '最小化收藏夹',
	max : '最大化收藏夹',
	welcome : '欢迎您，{0}！',
	no_module : '请您先在个人设置中启用该模块',
	sess_null : '您当前的SESSION已过期，请把您当前的工作保存到其他地方，以免丢失'
},
cms : {
	newsort : '新建分类',
	newquest : '新建问卷',
	newexpect : '新建期数',
	newcatg : '新建子栏目',
	newmenu : '新建子菜单',
	surveystat : '调查统计'
},
daily : {
	status1 : '计划',
	status2 : '开始',
	status3 : '终止',
	tp1 : '任务',
	tp2 : '商务',
	tp3 : '约会',
	tp4 : '会议',
	tp5 : '学习',
	tp6 : '拜访',
	tp7 : '来访',
	tp8 : '生日',
	tp9 : '杂事',
	tpA : '其他'
},

resource : {
	tree_cantmove1 : '资源不能移出到其他的资源库',
	flow_executor : '{0}({1})的执行人'
},
task : {
	tree_cantmove1 : '只能在"待办任务 - 按树形"之内移动',
	create : '新建任务',
	createchild : '新建子任务',
	createroot : '新建根任务',
	edit : '编辑任务',
	view : '查看任务',
	del : '删除任务',
	fbk : '发表反馈',
	move : '移动任务',
	hand : '移交任务',
	report : '上报任务',
	favor : '加入收藏',
	msgadd : '加入提醒',
	mail : '邮件发送',
	relate : '任务相关',
	secure : '安全属性',
	share : '共享任务',
	gantt : '进度图示',
	lnkmaintain : '链接维护',
	lnkview : '链接查看',
	lnkmsg : '消息',
	lnkfvr : '收藏',
	look : '任务监视',
	nolook : '取消监视',
	lookmaintain : '监视维护',
	lookview : '监视查看',
	exe : '执行任务',
	end : '结束任务',
	cancel : '取消任务',
	pigeonhole : '归档任务',
	reportok : '通过上报',
	reportfail : '回绝上报',
	_export : '导出任务',
	single : '单项',
	multi : '多项',
	publish : '发起流程',
	workon : '办理工作',
	pro_view : '流程图示',
	link : '关联任务',
	quit:"退出任务"
},
orgview : {
	tt : '组织机构图',
	custom_prop : '自定义属性'
},
email : {
 receive_failure:'邮件接收失败',
 clear_folder_before_del:'请在删除前清空文件夹'
},

font : {
	song : '宋体',
	hei : '黑体',
	kai : '楷体_GB2312',
	fang : '仿宋_GB2312',
	lv : '隶书',
	you : '幼圆'
},

editor : {
	full : '全屏编辑',
	bold : '粗体',
	italic : '斜体',
	underline : '下划线',
	strikethrough : '中划线',
	justifyleft : '左对齐',
	justifycenter : '居中对齐',
	justifyright : '右对齐',
	justifyfull : '两端对齐',
	insertunorderedlist : '插入项目符号',
	insertorderedlist : '插入编号',
	outdent : '减少缩进量',
	indent : '增加缩进量',
	fontname : '字体',
	fontsize : '字号',
	forecolor : '字体前景色',
	backcolor : '字体背景色',
	draw : '自选形状',
	cut : '剪切',
	copy : '复制',
	paste : '粘贴',
	pastetext : '纯文本粘贴',
	pasteword : '从Word中粘贴',
	undo : '撤销',
	redo : '恢复',
	find : '查找/替换',
	removeformat : '清除格式',
	createlink : '插入或修改链接',
	unlink : '取消链接',
	image : '插入图片',
	flash : '插入Flash',
	media : '插入多媒体',
	table : '表格',
	shortcut : '快捷输入',
	preview : '全屏编辑',
	html : '编辑源码',
	imgmod : '图片属性',
	tableadd : '插入表格',
	tablemod : '表格属性',
	tablecell : '单元格属性',
	tablecellsplit : '拆分单元格',
	tablecellrow : '表格行属性',
	tablerowins0 : '在上方插入一行',
	tablerowins1 : '在下方插入一行',
	tablerowmerge : '向下合并一行',
	tablerowsplit : '拆分行',
	tablerowdel : '删除行',
	tablecolins0 : '在左侧插入一列',
	tablecolins1 : '在右侧插入一列',
	tablecolmerge : '向右侧合并一列',
	tablecolsplit : '拆分列',
	tablecoldel : '删除列',
	fillcolor : '填充颜色',
	strokecolor : '线条颜色',
	regulation : '规则形状',
	unregulation : '不规则形状',
	line : '线条',
	text : '文本框',
	label : '标注',
	rectangle : '矩形',
	RoundRect : '圆角矩形',
	ArcRect : '弧形矩形',
	Oval : '椭圆',
	Diamond : '菱形',
	Diamond : '菱形',
	Parallelogram : '平行四边形',
	UpTriangle : '向上等腰三角形',
	DownTriangle : '向下等腰三角形',
	Directness : '直角三角形',
	UpTrapezoid : '向上梯形',
	DownTrapezoid : '向下梯形',
	Hexagon : '六边形',
	Pentagon : '五边形',
	Octagon : '八边形',
	BigBracket : '双大括号',
	FBigBracket : '左大括号',
	RBigBracket : '右大括号',
	MidBracket : '双中括号',
	FMidBracket : '左中括号',
	RMidBracket : '右中括号',
	Heart : '心形',
	beenline : '直线',
	zline : '折线',
	strokeweight : '线条粗细',
	lineType : '虚线类型',
	zorder : '叠放次序',
	maxZindex : '置于顶层',
	minZindex : '置于底层',
	topZindex : '上移一层',
	lowerZindex : '下移一层',
	arrowType : '箭头样式',
	addtext : '添加文字',
	_3D : '立体',
	no_3D : '取消立体'
},
datepicker : {
	oper_tip : '单击转到今天日期\n双击选中今天日期'
},

dyn : {
	db : '数据表名',
	field : '字段名',
	dict : '数据字典',
	type : '类型标识',
	jk : '接口'
},

vm : {
	path_err : '调试信息：找不到路径为 "{0}" 的视图对象。请检查路径是否有效。\n\n\(把 Cfg.debug 设为 false 此提示将不再出现)',
	find_err : '调试信息：在路径为 "{0}" 的视图中找不到ID为 "{1}" 的面板。请检查此面板是否存在。\n\n\(把 Cfg.debug 设为 false 此提示将不再出现)'
},

ps : function (s) {
	for (var i = 1; i < arguments.length; i++) {
		s = s.replace('{'+(i-1)+'}', arguments[i]);
	}
	return s;
},
end  : '结束'
}
