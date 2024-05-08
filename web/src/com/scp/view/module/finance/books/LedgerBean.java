package com.scp.view.module.finance.books;




import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.ledgerBean", scope = ManagedBeanScope.REQUEST)
public class LedgerBean extends GridView {

	@Bind
	@SaveState
	private String year = "2019";
	
	@Bind
	@SaveState
	private String mouthto = "1";
	
	@Bind
	@SaveState
	private String  mouthdy = "1";
	
	@Bind
	@SaveState
	private String  actcode = "1131";
	
	
	@Bind
	@SaveState
	private String  argsSale = "2191888";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameSale;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			dtlIFrameSale.setSrc("astdtl.xhtml");
			update.markAttributeUpdate(dtlIFrameSale, "src");
			
		}
	}
	@Action
	public void report() {
		Map<String, String> argsMap = new HashMap<String, String>();
		Long actsetid = AppUtils.getUserSession().getActsetid();
		String idstring = Long.toString(actsetid);
		argsMap.put("actsetid", idstring);
		argsMap.put("year", year);
		argsMap.put("periodfm", mouthto);
		argsMap.put("periodto", mouthdy);
		argsMap.put("actcode", actcode);
		argsMap.put("currency", "*");
		argsMap.put("astid", argsSale);
		String urlArgs= AppUtils.map2Url(argsMap, ":");
		String rpturl = AppUtils.getRptUrl();
		String arg ="&actcode="+actcode+"&" + "args=" +urlArgs;
		String openUrl = rpturl
				+ "/reportJsp/showReport.jsp?raq=/finance/books/book_detail_11312121.raq";
		String a =  openUrl + arg;
		//System.out.println(a);
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		
	}
	
}
