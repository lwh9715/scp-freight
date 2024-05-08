package com.scp.view.sysmgr.mail;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.model.sys.SysEmailTemplate;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.sysmgr.mail.emailtemplateBean", scope = ManagedBeanScope.REQUEST)
public class EmailtemplateBean extends GridFormView{
	
	@SaveState
	@Accessible
	public SysEmailTemplate selectedRowData = new SysEmailTemplate();
	
	@Bind
	public UIIFrame editIframe;

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysEmailTemplateService.sysEmailTemplateDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.sysEmailTemplateService.saveData(selectedRowData);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap){
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	@Override
	public void add() {
		selectedRowData = new SysEmailTemplate();
		super.add();
		editIframe.load("./emailtemplateedit.xhtml?id=" + this.pkVal);
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		editIframe.load("./emailtemplateedit.xhtml?id=" + this.pkVal);
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
			for(String id : ids) {
				this.serviceContext.sysEmailTemplateService.removeDate(Long.parseLong(id));
			}
			MessageUtils.alert("OK");
			this.refresh();
	}
}
