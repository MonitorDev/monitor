package test;

import java.util.List;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.persistence.PubScheduler;

public class TestUpdatePubschel {

	public static void main(String[] args) {
		List<PubScheduler> list = null;
		PubCommonDAO dao = FrameworkHelper.getDAO();
		list = dao.getQueryList("FROM PubScheduler t ");
		for(PubScheduler ps : list) {
			String str = ps.getConfigXml();
			str = str.replace("monitorPoint:00000001", "monitorPoint:00000001%3B00000003%3B00000002");
			System.out.println(ps.getSchlId() + ", " + str);
			dao.updateObject(ps);
		}
//		String str = "type:http;monitorPoint:00000001;url:http%3A//www.meilishuo.com/;schlId:00001020;retry:2;hasRetry:0;";
//		str = str.replace("monitorPoint:00000001", "monitorPoint:00000001%3B00000003%3B00000002");
		
	}
}
