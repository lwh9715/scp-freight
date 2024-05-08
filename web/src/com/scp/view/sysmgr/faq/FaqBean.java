package com.scp.view.sysmgr.faq;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.faq.faqBean", scope = ManagedBeanScope.REQUEST)
public class FaqBean extends GridView {
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_faqshow";
		String url = "./faqshow.xhtml?id="+this.getGridSelectId();
		int width = 1024;
		int height = 768;
		AppUtils.openWindow(winId, url);
	}
}
