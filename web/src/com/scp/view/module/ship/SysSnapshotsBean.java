package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.syssnapshotsBean", scope = ManagedBeanScope.REQUEST)
public class SysSnapshotsBean extends GridFormView {

	@Bind
	@SaveState
	public String linkid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			linkid = AppUtils.getReqParam("id");
		}
	}

	@Override
	public void grid_ondblclick() {
		grid_onrowselect();
	}

	@Override
	public void refresh() {
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {

	}

	@Bind
	private UIIFrame snapshotsIframe;

	@Override
	public void grid_onrowselect() {
		super.grid_onrowselect();
		String id = this.grid.getSelectedIds()[0];
		String csUrlbase = ConfigUtils.findSysCfgVal("cs_url_base");
		String url = csUrlbase+"esihistory.html?id="+this.getGridSelectId();
		snapshotsIframe.setSrc(url);
		update.markAttributeUpdate(snapshotsIframe, "src");
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		qry += "\nAND linkid = "+linkid;
		m.put("qry", qry);
		return m;
	}
	
}
