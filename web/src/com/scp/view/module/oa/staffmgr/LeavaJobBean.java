package com.scp.view.module.oa.staffmgr;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.staffmgr.leavajobBean", scope = ManagedBeanScope.REQUEST)
public class LeavaJobBean extends MastDtlView {

	@Bind
	public UIIFrame leaveframe;

	@Bind
	public UIWindow detailsWindow;

	@Override
	public void grid_ondblclick() {
		Long id = this.getGridSelectId();
		detailsWindow.show();
		leaveframe.load("leavajobedit.xhtml?id=" + id);
	}

	@Action
	public void add() {
		this.mPkVal = -1l;
		detailsWindow.show();
		leaveframe.load("leavajobedit.xhtml");
	}

	@Action
	public void dels() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one at first!");
			return;
		}
		// String id = StrUtils.array2List(ids);
		this.serviceContext.jobChangeService().removeModle(ids);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}

	public void showDetil() {
		if (detailsWindow != null) {
			detailsWindow.show();
			leaveframe.load("leavajobedit.xhtml?id=" + this.mPkVal);
		}
	}

	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		this.mPkVal = this.getGridSelectId();
	}

	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		 qry += "\nAND changetype IN ('C','D')";
		map.put("qry", qry);
		return map;
	}

}
