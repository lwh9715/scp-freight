package com.scp.view.module.api.vgm;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.api.ApiVgm;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.api.vgm.vgmlistBean", scope = ManagedBeanScope.REQUEST)
public class VgmListBean extends GridView {
	@Bind
	@SaveState
	public String masterBillNoq;

	@Bind
	@SaveState
	public String containerNoq;

	@Bind
	@SaveState
	public String inputerp;

	@Bind
	@SaveState
	public String inputerq;

	@SaveState
	@Accessible
	public ApiVgm selectedRowData = new ApiVgm();

	public Long userid;

	@Override
	public void beforeRender(boolean isPostBack) {
		
		if (!isPostBack) {
			super.applyGridUserDef();
			init();
		}
	}

	private void init() {
		this.userid = AppUtils.getUserSession().getUserid();
		String id = AppUtils.getReqParam("id");
		this.qryMap.put("issubmit$",true);
	}

	@Bind
	public UIWindow editWindow;

	@Bind
	public UIIFrame editIframe;

	@Override
	public void grid_ondblclick() {
		String gridid = this.grid.getSelectedIds()[0];
		String vgmid = gridid.split("-")[0];
		String url = "./vgmedit.xhtml?id=" + vgmid;
		editIframe.load(url);
		editWindow.show();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		return m;
	}

	@Bind
	public UIButton copy;

	@Action
	public void copy() {
		
	}


	@Action
	public void send() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		
		for (String id : ids) {
			String vgmid = id.split("-")[0];
			ApiVgmTools.postVgm(vgmid, AppUtils.getUserSession().getUserid().toString());
		}
	}
	
	@Action
	public void sendTest() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		
		for (String id : ids) {
			String vgmid = id.split("-")[0];
			ApiVgmTools.postVgmTest(vgmid, AppUtils.getUserSession().getUserid().toString());
		}
	}
	


	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		try {
			for (String id : ids) {
				String vgmid = id.split("-")[0];
				serviceContext.apiVgmService.removeDate(Long.parseLong(vgmid));
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void getStatus() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		try {
			for (String id : ids) {
				String vgmid = id.split("-")[0];
				ApiVgmTools.getVgmStatus(vgmid, AppUtils.getUserSession().getUserid().toString());
			}
			this.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		this.refresh();
	}
}
