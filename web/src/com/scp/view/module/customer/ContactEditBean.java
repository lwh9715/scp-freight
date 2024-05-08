package com.scp.view.module.customer;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.contacteditBean", scope = ManagedBeanScope.REQUEST)
public class ContactEditBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorpcontacts selectedRowData = new SysCorpcontacts();

	@Bind
	@SaveState
	@Accessible
	public Long pkid;
	
	@SaveState
	@Accessible
	public String customerid;

	@SaveState
	@Bind
	public Long userid;

	@SaveState
	public String username;

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.customerid = AppUtils.getReqParam("customerid");
			if(StrUtils.isNull(AppUtils.getReqParam("id"))){
				add();
			}else{
				pkid = Long.valueOf(AppUtils.getReqParam("id"));
				selectedRowData = serviceContext.customerContactsMgrService.sysCorpcontactsDao
						.findById(pkid);
			
				update.markUpdate(true, UpdateLevel.Data, "editPanel");
			}
			if(!StrUtils.isNull(this.customerid)) {
				SysCorporation sysCorporation = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.parseLong(this.customerid));
				if(sysCorporation != null) {
					selectedRowData.setCustomerabbr(sysCorporation.getAbbr());
					selectedRowData.setCustomerid(Long.parseLong(this.customerid));
				}
			}
			userid = AppUtils.getUserSession().getUserid();

			if(selectedRowData.getSalesid() != null){
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(selectedRowData.getSalesid());
				if(us  != null){
					this.username = us.getNamec();
				}
			}

		}
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void save(){
//		this.pkVal = getViewControl().save(this.pkVal , data);
		try {
			doServiceSave();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			this.add();
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refreshForm();
	}

	@Override
	protected void doServiceSave() {
		if (StrUtils.isNull(selectedRowData.getContactype())) {
			selectedRowData.setContactype2(null);
			//selectedRowData.setContactxt(null);
		}
		serviceContext.customerContactsMgrService.saveData(selectedRowData);

	}

	

	@Override
	public void del() {
		if(this.pkid==-1l){
			add();
		}else{
		serviceContext.customerContactsMgrService.removeDatedel(this.pkid);
		MessageUtils.alert("OK");
		this.add();
		}
	}

	@Override
	public void add() {
		selectedRowData = new SysCorpcontacts();
		selectedRowData.setContactype("N");
		pkid = -1L;
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		this.customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		selectedRowData.setCustomerid((Long) m.get("id"));
		selectedRowData.setCustomerabbr((String) m.get("abbr"));
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		this.customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

}
