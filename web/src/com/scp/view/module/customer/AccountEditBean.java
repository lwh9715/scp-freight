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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.sys.SysCorpaccount;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.accounteditBean", scope = ManagedBeanScope.REQUEST)
public class AccountEditBean extends GridFormView {

	@SaveState
	@Accessible
	public SysCorpaccount selectedRowData = new SysCorpaccount();

	@Bind
	@SaveState
	@Accessible
	public Long pkid;
	
	@Bind
	@SaveState
	@Accessible
	private String customerabbr;
	
	@SaveState
	@Accessible
	public String customerid;

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	public UIButton add;
	@Bind
	public UIButton del;
	@Bind
	public UIButton save;

	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.customerid = AppUtils.getReqParam("customerid");
			if(StrUtils.isNull(AppUtils.getReqParam("id"))){
				add();
			}else{
				pkid = Long.valueOf(AppUtils.getReqParam("id"));
				selectedRowData = serviceContext.customerAccountMgrService.sysCorpaccountDao.findById(pkid);
			}
			if(!StrUtils.isNull(this.customerid)) {
				SysCorporation sysCorporation = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.parseLong(this.customerid));
				if(sysCorporation != null) {
					customerabbr = sysCorporation.getAbbr();
					selectedRowData.setCustomerid(Long.parseLong(this.customerid));
				}
			}
		} else {
		}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");

		String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
				add.setDisabled(true);
				del.setDisabled(true);
				save.setDisabled(true);
			}
		} catch (Exception e) {
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
		serviceContext.customerAccountMgrService.saveData(selectedRowData);
		this.alert("OK");
	}

	@Override
	public void del() {
		if(this.pkid==-1l){
			add();
		}else{
			serviceContext.customerAccountMgrService.removeDatedel(pkid);
		MessageUtils.alert("OK");
		this.add();
		}
	}

	@Override
	public void add() {
		selectedRowData = new SysCorpaccount();
//		selectedRowData.setId(-1L);
		selectedRowData.setCustomerid(Long.parseLong(customerid));

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
		customerabbr=((String) m.get("abbr"));
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

	@Bind(id="contacts")
    public List<SelectItem> getContacts() {
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.name","sys_corpaccount AS d","WHERE d.customerid ="+selectedRowData.getCustomerid(),"ORDER BY d.name");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
}
