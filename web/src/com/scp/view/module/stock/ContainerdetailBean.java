package com.scp.view.module.stock;

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
import com.scp.view.comp.EditGridView;
@ManagedBean(name = "pages.module.stock.containerdetailBean", scope = ManagedBeanScope.REQUEST)
public class ContainerdetailBean extends EditGridView {
	@SaveState
	@Accessible
	@Bind
	public Long cid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("cid").trim();
		if(id!=null&&id!=""){
			cid = Long.valueOf(id);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry+="\n AND linkid =" + cid;
		m.put("qry", qry);
		return m;
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busContainerMgrService.updateBatchEditGrid(modifiedData);
		this.refresh();
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busContainerMgrService.addBatchEditGrid(addedData, cid);
		this.refresh();
	}
	@Override
	protected void remove(Object removedData) {
		serviceContext.busContainerMgrService.removedBatchEditGrid(removedData);
		this.refresh();
	}
	
	
	@Action(id = "removes")
    public void removes() {
    	editGrid.remove();
    	
    }
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;
	
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
				String callFunction = "f_imp_containerdetai";
				String args = this.cid + ",'" + AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.editGrid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
}
