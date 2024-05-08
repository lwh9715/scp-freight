package com.scp.view.module.website;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.website.webregisterBean", scope = ManagedBeanScope.REQUEST)
public class WebregisterBean extends GridView{
	
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		super.beforeRender(isPostBack);
		super.applyGridUserDef();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void check() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.webRegisterService.check(Long.parseLong(id), AppUtils.getUserSession().getUsercode());
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void uncheck() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length < 1) {
			MessageUtils.alert("请勾选记录!");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.webRegisterService.uncheck(Long.parseLong(id), AppUtils.getUserSession().getUsercode());
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
