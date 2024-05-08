package com.scp.view.sysmgr.message;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.message.msgmgrBean", scope = ManagedBeanScope.REQUEST)
public class MsgMgrBean extends GridView {
	
	
	@SaveState
	private String modulecode;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(!StrUtils.isNull(AppUtils.getReqParam("modulecode"))){
				modulecode = AppUtils.getReqParam("modulecode");
			}
			super.applyGridUserDef();
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "\n AND modulecode = '"+modulecode+"'";
		//2479 公告管理过滤一下，按当前账号所选分公司过滤
		filter += "AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.ischoose = TRUE " +
		"AND EXISTS(SELECT 1 FROM sys_user WHERE code = t.inputer AND corpid = x.corpid AND userid = "+AppUtils.getUserSession().getUserid()+")))";
		m.put("filter", filter);
		return m;
	}
	
	
	
	@Action
	public void add() {
		AppUtils.openWindow("_msgMgr", "msgedit.xhtml?actionType=new&modulecode="+modulecode);
	}
	
	

	@Override
	public void grid_ondblclick(){
		AppUtils.openWindow("_msgMgr", "msgedit.xhtml?actionType=edit&id="+this.grid.getSelectedIds()[0]+"&modulecode="+modulecode);
	}
	
	
	@Action
	public void dtlDel(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please chose one row first!");
		}else{
			try {
				for (String id : ids){
					this.serviceContext.sysMessageService.removeQuoteDate(Long.valueOf(id).longValue());
				}
				MessageUtils.alert("OK");
			} catch (Exception e) {
				
			}
			this.grid.reload();
		}
	}
	
	
	
}
