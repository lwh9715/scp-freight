package com.scp.view.module.customer;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

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

import com.scp.base.CommonComBoxBean;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysCorpservice;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.custserviceeditBean", scope = ManagedBeanScope.REQUEST)
public class CustServiceEditBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorpservice selectedRowData = new SysCorpservice();

	@Bind
	@SaveState
	@Accessible
	public Long pkid;
	
	@SaveState
	@Accessible
	public String customerid;

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.customerid = AppUtils.getReqParam("customerid");
			if(StrUtils.isNull(AppUtils.getReqParam("id"))){
				add();
			}else{
				pkid = Long.valueOf(AppUtils.getReqParam("id"));
				selectedRowData = serviceContext.customerServiceMgrService.sysCorpserviceDao
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
		}
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
	public void save() {
		serviceContext.customerServiceMgrService.saveData(selectedRowData);
		this.alert("OK");
	}

	@Override
	public void del() {
		if(this.pkid==-1l){
			add();
		}else{
			serviceContext.customerServiceMgrService.removeDatedel(this.pkid);
		MessageUtils.alert("OK");
		this.add();
		}
	}

	@Override
	public void add() {
		selectedRowData = new SysCorpservice();
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
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		this.customerService.qry(popQryKey);
		this.customerGrid.reload();
	}
	
	@Bind(id="contacts")
    public List<SelectItem> getContacts() {
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.name","sys_corpcontacts AS d","WHERE d.customerid ="+selectedRowData.getCustomerid(),"ORDER BY d.name");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

}
