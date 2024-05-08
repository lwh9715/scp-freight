package com.scp.view.bpm;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bpm.BpmAssignRef;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "bpm.bpmassignrefuserBean", scope = ManagedBeanScope.REQUEST)
public class BpmAssignrefUserBean extends GridFormView {
	
	
	@Bind
	@SaveState
	public String libid = "-1";
	
	@SaveState
	@Accessible
	public BpmAssignRef selectedRowData = new BpmAssignRef();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("libid");
			if(!StrUtils.isNull(id)) {
				libid = id;
			}
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("libid", libid);
		return map;
	}

	@Action
	public void del() {
		try {
			String[] ids = this.grid.getSelectedIds();
			this.serviceContext.bpmAssignRefService.remove(ids);
			this.alert("OK!");
			this.grid.reload();
			Browser.execClientScript("freashagginref()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.bpmAssignRefService.bpmAssignRefDao.findById(this.pkVal);
	}

	@Override
	public void save() {
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length <= 0) {
				return;
			}
			this.serviceContext.bpmAssignRefService.update(ids , selectedRowData.getExpression());
			this.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doServiceSave() {
	}
	
}
