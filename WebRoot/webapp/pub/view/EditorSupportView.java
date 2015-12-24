package com.rongji.dfish.webapp.pub.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.Fieldset;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.Horizontalgroup;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.TabPanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Checkbox;
import com.rongji.dfish.engines.xmltmpl.form.File;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.engines.xmltmpl.form.Radiogroup;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Spinner;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.dfish.pluigins.xmltmpl.BlockPanel;
import com.rongji.dfish.base.Utils;

public class EditorSupportView {

	public static BaseView buildEditorImgView(ViewFactory viewFactory,
			String edtId) {
		BaseView view = viewFactory.buildFormView(false, true, false);

		fillButtonBarPanel(viewFactory.findBttonPanel(view), edtId, view);

		// ��formPanel
		// ��Ϊ����������;��tabPanel:����ͼƬѡ��(���ҵĵ��ԡ��ҵ�ͼƬ������ͼƬ���),��ImgPanel:��ʾ��ѡ���ͼƬ
		VerticalPanel vp = new VerticalPanel(ViewFactory.ID_PANEL_FORM, "*,80");

		TabPanel tb = new TabPanel("f_form_up");
		SourcePanel mycomputer = new SourcePanel("f_my_computer", Utils
				.joinString("./editorImgswfupload.jsp?"));
		mycomputer.setCache(true);
		tb.addSubPanel(null, "�ҵĵ���", mycomputer);


		BlockPanel imgPanel = new BlockPanel("f_img",60,60,5);
		imgPanel.setJsCodeOnLoad("var id=this.$().id; setTimeout(function(){window.editorImg.initCandidate(id,'divImg');},2000) ");
		imgPanel.setCss("#blockList_f_img ul li .selected{display:none; position:absolute;width:14px;height:14px;background:url(img/b/delete-s.gif);right:0;top:0;cursor:pointer;}" +
				"#blockList_f_img ul li .divImg{border:2px solid #E4E4E4 ;padding:1px;cursor:move;height:52px}#blockList_f_img ul li .divImg:hover{ border:2px solid #87A8F7;}" +
				"#blockList_f_img ul li .divImg img{width:100%;height:100%}");
		
		List<String> data = new ArrayList<String>();
        
		data.add(Utils.joinString("<div class='divImg' n='����' imgId='01'><a href='javascript:void(0);' class='selected' ></a><img src='img/bg.jpg'></div>"));
		data.add(Utils.joinString("<div class='divImg' n='����' imgId='02'><a href='javascript:void(0);' class='selected' ></a><img src='img/titleImg.jpg'></div>"));
		data.add(Utils.joinString("<div class='divImg' n='����' imgId='03'><a href='javascript:void(0);' class='selected' ></a><img src='img/bg.jpg'></div>"));
		
		imgPanel.setData(data);
		
		tb.addSubPanel("", "�ҵ�ͼƬ",imgPanel);
		
		FormPanel form = viewFactory.getDefaultFormPanel();
		form.setId("f_img_net");

		Text url = new Text("url", "����ͼƬ��ַ", "http://", 200);

		Horizontalgroup hg = new Horizontalgroup("hg", "");
		hg.setFilter(true);

		Checkbox cb = new Checkbox("upload", "", new String[] {}, Arrays
				.asList(new String[][] { { "1", "�ϴ���������" } }));
		Label l = new Label(
				"",
				"",
				"<input type='button' onclick='VM(this).cmd(\"add\");' value='���'>",
				false, false, true);
		hg.add(cb);
		hg.add(l);

		form.add(url);
		form.add(hg);
		form.setFilter(false);

		tb.addSubPanel("", "����ͼƬ", form);

		BlockPanel bp = new BlockPanel("id", 60, 60, 15);
		bp.setJsSrc(new String[] { "js/pl/edt/editorImg.js" });
		bp.setJsCodeOnLoad(" window.editorImg = new PL.EditorImg(this,'divImg','"
						+ edtId + "');window.editorImg.init(); ");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			list
					.add("<div class='divImg'><a  href='javascript:void(0)' class='delete'></a> <img src='img/b/save.jpg'></div>");
		}
		bp
				.setCss("#blockList_id ul li .delete{display:none; position:absolute;width:14px;height:14px;background:url(img/b/delete-s.gif);right:0;top:0;cursor:pointer;}"
						+ "#blockList_id ul li .divImg{border:2px solid #E4E4E4 ;padding:1px;cursor:move;height:52px}#blockList_id ul li .divImg:hover{ border:2px solid #87A8F7;}"
						+ "#blockList_id ul li .divImg img{width:100%;height:100%}");

		bp.setData(list);
		vp.addSubPanel(tb, bp);
		view.replacePanelById(vp);
		
		BaseView view2 = viewFactory.buildFormView(false, true, false);
		
		ButtonBarPanel b2 = viewFactory.findBttonPanel(view2);
		b2.addButton(new ClickButton("img/b/save.gif","����","VM(this).cmd('save')"));
		b2.addButton(new ClickButton("img/b/close.gif","�ر�","DFish.g_dialog(this).close();"));
		
		FormPanel f = viewFactory.findFormPanel(view2);
		Fieldset fs = new Fieldset("ͼƬ��Դ");
		fs.setOpenStatus(Fieldset.OPEN_STATUS_ALWAYS_OPEN);
		Radiogroup source = new Radiogroup("source", "ͼƬ��Դ", "0", new Object[]{"0","1"});   
	    source.add(new File("file","����","",null));
	    source.add(new Text("file","����","",200));
	    fs.add(source);
	    
	    Fieldset fs2 = new Fieldset("��ʾЧ��");
	    fs2.add(new Text("n","ͼƬ˵��","",40));
	    fs2.add(new Spinner("w","��ʾ���",0,0,800,10));
	    fs2.add(new Spinner("h","��ʾ�߶�",0,0,800,10));
	    fs2.add(new Select("float","��ʾ��ʽ",null,Arrays.asList(new String[][]{{"0","Ĭ��"},{"1","����"},{"2","����"},{"3","�в�"}})));
	    
	    
       f.add(fs);
       f.add(fs2);
		
		view2.add(new JSCommand("save","var h = VM(this).f('w',DFish.GET_VALUE);VM(this).f('h',DFish.SET_VALUE,20);DFish.alert(h); //VM(this).cmd('submit')"));
        view2.add(new SubmitCommand("submit","",null,false));		
        view2.addLoadEvent("var c");
        
		return view;
	}

	/**
	 * ��䰴ť��,�������������
	 * @param bbp
	 * @param edtId
	 * @param view
	 */
	private static void fillButtonBarPanel(ButtonBarPanel bbp, String edtId,
			BaseView view) {
		bbp.addButton(new ClickButton("img/b/save.gif", "����ͼƬ",
				"VM(this).cmd('insertImg');"));
		bbp.addButton(new ClickButton("img/b/close.gif", "ȡ��", ""));

		view
				.add(new JSCommand("insertImg",
						"window.editorImg.insertImgToEditor();DFish.g_dialog(this).close();"));
		view
				.add(new JSCommand(
						"add",
						"var url=VM(this).f('url',DFish.GET_VALUE);var ups = VM(this).f('upload',DFish.GET_VALUE);"
								+ "var up;if(ups && ups.length > 0){up='1'}else{up='0'} var img=document.createElement('img');img.src=url;img.up=up;window.editorImg.addImg(new Date().getTime(),img,'net');"
								+ "VM(this).f('url',DFish.SET_VALUE,'http://');VM(this).f('upload',DFish.SET_VALUE,'1');"));
		//   view.add(new JSCommand("add","DFish.alert('d')"));

	}

}
