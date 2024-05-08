package com.scp.view.sysmgr.user;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.user.usershowreportBean", scope = ManagedBeanScope.REQUEST)
public class UserShowReportBean extends GridSelectView {

	@Bind
	@SaveState
	private String userid ="-100";

	@Bind
	@SaveState
	private String reportType;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			userid = (String) AppUtils.getReqParam("id");
			this.update.markUpdate(UpdateLevel.Data, "userid");
			reportType = "profit";
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = " modcode = '"+reportType+"'";
		map.put("filter", filter);
		map.put("userid", userid);
		return map;
	}
	
	@Action(id="reportType",event="onselect")
	public void reportType_select(){
		this.refresh();
	}

}
