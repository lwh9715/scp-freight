package com.scp.view.sysmgr.message;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.sysmgr.message.jobnotesBean", scope = ManagedBeanScope.REQUEST)
public class JobnotesBean extends GridView{
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		qry += "\nAND sidate BETWEEN '"
		+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
		+ "' AND '"
		+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
		+ "'";
		m.put("qry",qry);
		return m;
	}
	
	@Bind
	public UIWindow importDataWindow;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_sys_jobnotes";
				String args =  "'"+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		
	}
	
	@Action
	public void dtlDel(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please chose one row first!");
		}else{
			try {
				for (String id : ids){
					this.daoIbatisTemplate.updateWithUserDefineSql("DELETE from sys_jobnotes where id  = "+Long.valueOf(id).longValue()+"");
				}
				MessageUtils.alert("OK");
			} catch (Exception e) {
			}
			this.grid.reload();
		}
	}
}
