function webprev(url,w,h){
var i18n = {
  file: {
    title: "Some document.pdf"
  },
  label: {
    actualSize: "ԭʼ��С",
    fitPage: "��Ӧҳ��",
    fitWidth: "��Ӧ���",
    fitHeight: "��Ӧ�߶�",
    fullscreen: "ȫ��",
    fullwindow: "���",
    fullwindow_escape: "�� Esc �˳����ģʽ",
    page: "ҳ��",
    pageOf: "/"
  },
  errors: {
    "error": "The content cannot be displayed due to an unknown error.",
    "content": "�����޷���ʾ���� png, jpg, gif or swf ������.",
    "io": "�޷��ӷ���������Ԥ���ļ�."
  }
};
function changeMax() {
  var prewrapper2 = document.getElementById("prewrapper");
  prewrapper2.style.width = "100%";
  prewrapper2.style.height = "100%";
  prewrapper2.style.position = "static";
  alert('1233');
}
var onPreviewEvent = function(event) {
  if (event.event) {
    var prewrapper;
    if (event.event.type == "onFullWindowClick") {
      changeMax();
    } else if (event.event.type == "onFullWindowEscape") {
      prewrapper = document.getElementById("prewrapper");
      prewrapper.style.width = "786px";
      prewrapper.style.height = "672px";
      prewrapper.style.position = "static";
    }
  } else if (event.error) {
    alert(i18n.errors[event.error.code] || "Error");
  }
};
var myPreviewLogger = function(msg, level) {
  if (console && console.log) {
    console.log("WebPreviewer [" + level + "]: " + msg);
  }
};

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
  wmode: "transparent"
};
var attributes = {
  id: "previewer",
  name: "previewer"
};
swfobject.embedSWF("pl/webpreviewer/WebPreviewer.swf", "previewer", "100%", "100%", "9.0.124", true, flashvars, params, attributes);
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
      break;
    }
  }
  prep.style.cellSpacing = "0px";
  prep.setAttribute("cellSpacing", "0px");
}
initcss();
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




}