package com.scp.view.module.finance.books;


import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.astdtlBean", scope = ManagedBeanScope.REQUEST)
public class AstdtlBean extends GridView {

	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
				this.qryMap.put("actsetid$", AppUtils.getUserSession().getActsetid());
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			
		}
	}
	
	@Override
	public void grid_onrowselect() {
		refershs();
	}
	
	@Action
	public void grid_onrowdeselect() {
		refershs();
	}
	
	private void refershs() {
		String args = "";
		String args2="";
		if (this.grid.getSelectedIds().length > 0
				&& this.grid.getSelectedIds() != null) {
			args = StrUtils.array2List(this.grid.getSelectedIds());
		}
		Browser.execClientScript("parent.window","argsSaleJsVar.setValue('"+args+"');");
	}

}