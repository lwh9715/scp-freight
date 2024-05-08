package com.scp.view.module.del.dispatch;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.del.DelLoadtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.del.dispatch.wmsoutdtlchooserBean", scope = ManagedBeanScope.REQUEST)
public class WmsOutdtlChooserBean extends GridView {

	@SaveState
	@Accessible
	public DelLoadtl delLoadtl = new DelLoadtl();

	@SaveState
	@Accessible
	public Long loadid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("loadid");
			loadid = new Long((Integer.parseInt(code)));
		}
	}

	@Action
	public void saveLoaddtl() {
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			} else {
				serviceContext.delLoaddtlMgrService.saveDtl(ids,loadid);
				MessageUtils.alert("OK");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
