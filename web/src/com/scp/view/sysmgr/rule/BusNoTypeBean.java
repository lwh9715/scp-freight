package com.scp.view.sysmgr.rule;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysBusnodesc;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.rule.busnotypeBean", scope = ManagedBeanScope.REQUEST)
public class BusNoTypeBean extends GridFormView {

	@SaveState
	@Accessible
	public SysBusnodesc selectedRowData = new SysBusnodesc();
	
	
	@Bind
	public UICombo qryCorpid;

	public Long getCorpid() {
		if ((this.qryCorpid.getValue() == null)
				|| (this.qryCorpid.getValue().equals(""))) {
			return Long.valueOf(-1L);
		}
		return Long.valueOf(this.qryCorpid.getValue().toString());
	}


	public void add() {
		this.selectedRowData = new SysBusnodesc();
		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpidCurrent());
		super.add();
		
	}
	
	@Action
	public void addCopy(){
		this.pkVal = -1L;
		SysBusnodesc old= this.selectedRowData;
		this.selectedRowData = new SysBusnodesc();
		this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpidCurrent());
		this.selectedRowData.setCode(old.getCode()+"_1");
		this.selectedRowData.setDatecol(old.getDatecol());
		this.selectedRowData.setNocol(old.getNocol());
		this.selectedRowData.setTblname(old.getTblname());
		this.selectedRowData.setNamec(old.getNamec()+"_1");
		this.selectedRowData.setRemark(old.getRemark()+"_1");
		
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	

	protected void doServiceFindData() {
		this.selectedRowData = ((SysBusnodesc) this.serviceContext.busNoRuleMgrService.sysBusnodescDao
				.findById(this.pkVal));
	}

	protected void doServiceSave() {
		this.serviceContext.busNoRuleMgrService.saveDataBusNoType(this.selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Action
	public void delGrid() {
		String ids[] = this.grid.getSelectedIds();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请选择删除的行");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.busNoRuleMgrService.sysBusnodescDao.removeDate(Long.valueOf(id));
			}
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	public void del() {
		this.serviceContext.busNoRuleMgrService.sysBusnodescDao.remove(this.selectedRowData);
		add();
		alert("OK");
		refresh();
	}


	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry = qry + "\nAND corpid =" + getCorpid();
		m.put("qry", qry);
		return m;
	}
}