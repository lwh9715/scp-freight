package com.scp.view.module.customer;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysCorporation;
import com.scp.service.sysmgr.SysCorporationService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.utils.Md5Util;

@ManagedBean(name = "pages.module.customer.apiBean", scope = ManagedBeanScope.REQUEST)
public class ApiBean extends GridFormView{
	
	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();
	
	@SaveState
	@Accessible
	public String customerid;
	
	/*@SaveState
	@Accessible
	public String apikey;*/
	
	@SaveState
	@Accessible
	public SysCorporationService sysCorporationService; 
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.customerid =AppUtils.getReqParam("customerid").trim();
			
			init();
		}
		
	}
	
	@Action
	public void setKey(){
		//this.customerid = AppUtils.getReqParam("customerid").trim();
		//System.out.println("customerid--->"+this.customerid);
		String apikey = Md5Util.MD5(this.customerid);
		//System.out.println(apikey);		
		String sql = "UPDATE sys_corporation SET apikey = '"+apikey+"' Where id="+this.customerid+"";
		//System.out.println("sql---->"+sql);
		this.serviceContext.sysCorporationService.sysCorporationDao.executeSQL(sql);
		init();
	}
	
	public void init(){
		//System.out.println("id------>"+customerid);
		if (!StrUtils.isNull(this.customerid)) {
			Long id = Long.parseLong(this.customerid);
			this.selectedRowData = serviceContext.sysCorporationService.sysCorporationDao.findById(id);
			return;
		}
	}
			

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		
	}
}
