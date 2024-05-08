package com.scp.view.sysmgr.company;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.company.companyBean", scope = ManagedBeanScope.REQUEST)
public class CompanyBean extends GridView{
	
	
	@SaveState
	@Bind
	protected String CSNO = "";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		CSNO = ConfigUtils.findSysCfgVal("CSNO");
	}
	
	
	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		String winId = "_companyedit";
		String url = "./companyedit.xhtml?id=" + id;
		AppUtils.openWindow(winId, url);
	}
	
	
	@Action
	public void add() {
		//String id = this.grid.getSelectedIds()[0];
		String winId = "_companyedit";
		String url = "./companyedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void autoBusno(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录！");
			return;
		}
		for(int i=0;i<ids.length;i++){
			String sql = "SELECT f_auto_busno_release('corpid="+ids[i]+"');";
			try {
				serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		alert("OK");
	}


	
}
