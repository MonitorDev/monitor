package com.rongji.dfish.webapp.pub.business;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.rongji.dfish.framework.SystemData;

public class EditorMethods {
	private static List<String[]> shctInpClz;
	private static Map<String, List<String>> shctInpContent;

	public static void loadDataFromXML() {
		if (shctInpClz == null) {
			synchronized (EditorMethods.class) {
				if (shctInpClz == null) {
					try {
						Document doc = new SAXReader().read(new File(SystemData
								.getInstance().getServletInfo()
								.getServletRealPath()
								+ "WEB-INF/shortcut_input.xml"));
						Element root=doc.getRootElement();
						List<String[]> shctInpClz=new ArrayList<String[]>();
						Map<String, List<String>> shctInpContent=new HashMap<String, List<String>>();
						Iterator clzIter=root.elementIterator("class");
						while(clzIter.hasNext()){
							Element clzEle=(Element)clzIter.next();
							String id=clzEle.attributeValue("id");
							String name=clzEle.attributeValue("name");
							shctInpClz.add(new String[]{id,name});
							Iterator contentIter=clzEle.elementIterator("value");
							List<String> contents=new ArrayList<String>();
							while(contentIter.hasNext()){
								Element contentEle=(Element)contentIter.next();
								contents.add(contentEle.getTextTrim());
							}
							shctInpContent.put(id, contents);
						}
						EditorMethods.shctInpClz=shctInpClz;
						EditorMethods.shctInpContent=shctInpContent;
					} catch (DocumentException ex) {}
				}
			}
		}
	}
	public static List<String[]> getShctInpClz(){
		loadDataFromXML();
		return shctInpClz;
	}
	public static Map<String, List<String>> getShctInpContent(){
		loadDataFromXML();
		return shctInpContent;
	}
}
