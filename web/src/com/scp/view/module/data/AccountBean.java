package com.scp.view.module.data;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.data.DatAccount;
import com.scp.service.data.AccountMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.accountBean", scope = ManagedBeanScope.REQUEST)
public class AccountBean extends GridFormView {

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String corpabbr = serviceContext.userMgrService
					.getCorporationNameByUserId(AppUtils.getUserSession()
							.getUserid());
			this.qryMap.put("corpabbr", corpabbr);
		}
		
	}

	@ManagedProperty("#{accountMgrService}")
	public AccountMgrService accountMgrService;
	
	@SaveState
	@Accessible
	public DatAccount selectedRowData = new DatAccount();
	
	
	@Override
	public void add() {
		selectedRowData= new DatAccount();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpidCurrent());
		selectedRowData.setOrderno(0);
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = accountMgrService.datAccountDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		accountMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
    @Override
    public void grid_ondblclick() {
    	// TODO Auto-generated method stub
    	super.grid_ondblclick();
    }
    
    @Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			accountMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
    
    @Override
	public void del(){
    	if(this.pkVal == null) {
    		MessageUtils.alert("Please choose!");
    		return;
    	}
    	accountMgrService.removeDate(this.pkVal);
    	this.refresh();
    	this.add();
	}
	
    @Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		return AppUtils.filterByCorperChoose(super.getQryClauseWhere(queryMap) , "t");
	}
}
