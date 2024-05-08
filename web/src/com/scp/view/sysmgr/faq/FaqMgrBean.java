package com.scp.view.sysmgr.faq;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.faq.faqmgrBean", scope = ManagedBeanScope.REQUEST)
public class FaqMgrBean extends GridView {
	
	@Action
	public void add() {
		AppUtils.openWindow("_faqMgr", "faqedit.xhtml?actionType=new");
	}
	
	

	@Override
	public void grid_ondblclick(){
		AppUtils.openWindow("_faqMgr", "faqedit.xhtml?actionType=edit&id="+this.grid.getSelectedIds()[0]);
	}
	
	
	@Action
	public void dtlDel(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please chose one row first!");
		}else{
			try {
				for (String id : ids){
					this.serviceContext.sysFaqService.removeDate(Long.valueOf(id).longValue());
				}
				MessageUtils.alert("OK");
			} catch (Exception e) {
				
			}
			this.grid.reload();
		}
	}
}
