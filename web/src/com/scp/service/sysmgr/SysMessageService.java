package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysMessageDao;
import com.scp.model.sys.SysMessage;

@Component
public class SysMessageService{
	
	@Resource
	public SysMessageDao sysMessageDao;

	public void saveData(SysMessage data) {
		if(0 == data.getId()){
			sysMessageDao.create(data);
		}else{
			sysMessageDao.modify(data);
		}
	}

	public void removeQuoteDate(Long id) {
		SysMessage data = sysMessageDao.findById(id);
		sysMessageDao.remove(data);
	}
	
//	public AutoTips findAutoTips() {
//		AutoTips autoTips = new AutoTips();
//		List<SysMessage> list = sysMessageDao.findAllByClauseWhere("isdelete = false AND NOW() < (inputtime +'3D') ORDER BY inputtime DESC");
//		if(list == null || list.size() < 1)return null;
//		SysMessage sysMessage = list.get(0);
//		autoTips.setTitle(sysMessage.getTitle());
//		String text = dealHtml(sysMessage.getContent());
//		autoTips.setContex(text);
//		autoTips.setUser(sysMessage.getInputer());
//		autoTips.setUrl(sysMessage.getMsgurl());
//		autoTips.setId(sysMessage.getId());
//		return autoTips;
//	}
	
//	private String dealHtml(String html) {
//		html = html.replaceAll("<br/>", "\n");
//		Document doc = Jsoup.parse(html);
//		String text = doc.body().text();
//		text = text.trim();
//		if(text.length() > 90) {
//			text = text.substring(0, 90);
//			text += "...";
//		}
//		return text;
//	}
	
//	String html = "<p>An <a href='http://www.oschina.net/'><b>example</b></a> link.</p>";
//	02	Document doc = Jsoup.parse(html);
//	03	Element link = doc.select("a").first();
//	05	String text = doc.body().text(); // "An example link"
//	06	String linkHref = link.attr("href"); // "http://www.oschina.net/"
//	07	String linkText = link.text(); // "example""
//	08	 
//	09	String linkOuterH = link.outerHtml();
//	10	    // "<a href="http://www.oschina.net/"><b>example</b></a>"
//	11	String linkInnerH = link.html(); // "<b>example</b>"
}
