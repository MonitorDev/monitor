
function changeMax() {
		var prewrapper2 = document.getElementById("prewrapper");
		prewrapper2.style.width = "100%";
		prewrapper2.style.height = "100%";
		prewrapper2.style.position = "static";
}


/**
	初始化预览函数
*/
function initFlashPreview(webPreviewerUrl,preSwfUrl){
	var i18n = {
	file: {
		title: "Some document.pdf"
	},
	label: {
		actualSize: "原始大小",
		fitPage: "适应页面",
		fitWidth: "适应宽度",
		fitHeight: "适应高度",
		fullscreen: "全屏",
		fullwindow: "最大化",
		fullwindow_escape: "按 Esc 退出最大化模式",
		page: "页码",
		pageOf: "/"
	},
	errors: {
		"error": "The content cannot be displayed due to an unknown error.",
		"content": "内容无法显示，非 png, jpg, gif or swf 型内容.",
		"io": "无法从服务器加载预览文件."
	}
	};
	
	var onPreviewEvent = function(event) {
		if (event.event) {
			var prewrapper;
			if (event.event.type == "onFullWindowClick") {
				changeMax();
			}
			else if (event.event.type == "onFullWindowEscape") {
				prewrapper = document.getElementById("prewrapper");
				prewrapper.style.width = "" + (width - 14) + "px";
				prewrapper.style.height = "" + (height - 33) + "px";
				prewrapper.style.position = "static";
			}
		}
		else if (event.error) {
			alert(i18n.errors[event.error.code] || "Error");
		}
	};
	var myPreviewLogger = function(msg, level) {
		if (console && console.log) {
			console.log("WebPreviewer [" + level + "]: " + msg);
		}
	};
	var url = preSwfUrl;
	var flashvars = {
		fileName: i18n.file.title,
		paging: true,
		url: url,
		jsCallback: "onPreviewEvent",
		jsLogger: "myPreviewLogger",
		show_fullscreen_button: true,
		show_fullwindow_button: false,
		disable_i18n_input_fix: true,
		i18n_actualSize: i18n.label.actualSize,
		i18n_fitWidth: i18n.label.fitWidth,
		i18n_fitPage: i18n.label.fitPage,
		i18n_fitHeight: i18n.label.fitHeight,
		i18n_fullscreen: i18n.label.fullscreen,
		i18n_fullwindow: i18n.label.fullwindow,
		i18n_fullwindow_escape: i18n.label.fullwindow_escape,
		i18n_page: i18n.label.page,
		i18n_pageOf: i18n.label.pageOf
	};
	var params = {
		allowScriptAccess: "sameDomain",
		allowFullScreen: "true",
		wmode: "opaque"  //改成opaque后 div不会被遮挡
	};
	var attributes = {
		id: "previewer",
		name: "previewer"
	};

	swfobject.embedSWF(webPreviewerUrl, "previewer", "100%", "100%", "9.0.124", true, flashvars, params, attributes);
	initcss();
}



function initcss() {
	var pree = document.getElementById('prewrapper');
	var prep1 = pree.parentNode;
	var prep2 = prep1.parentNode;
	prep2.style.paddingBottom = "0px";
	prep2.style.paddingLeft = "0px";
	prep2.style.paddingTop = "0px";
	prep2.style.paddingRight = "0px";
	prep1.style.height = "100%";
	prep1.style.marginLeft = "0px";
	pree.style.width = "100%";
	pree.style.height = "100%";
	var prep = pree.parentNode;
	for (var i = 0; i < 10; i++) {
		prep = prep.parentNode;
		prep.style.width = "100%";
		prep.style.height = "100%";
		if (prep.tagName == "TABLE") {
			prep = prep.parentNode;
			prep.style.width = "100%";
			prep.style.height = "100%";
			break;
		}
	}
	prep.style.cellSpacing = "0px";
	prep.setAttribute("cellSpacing", "0px");
}
initcss();

//为对话框最大化事件添加事件
var divs = document.getElementsByTagName("div");
for (var i = 0; i < divs.length; i++) {
	if (divs[i].className == 'dlg-max') {
		divs[i].onclick = function() {
			DFish.g_dialog(this).max(this);
			initcss();
		}
	}
	if (divs[i].className == 'dlg-mid') {
		divs[i].onclick = function() {
			DFish.g_dialog(this).max(this);
			initcss();
		}
	}
}