package com.scp.view.module.customer;

import java.util.Calendar;
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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.sys.SysCorporationDao;
import com.scp.model.sys.CsUser;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.EncoderHandler;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.csuserBean", scope = ManagedBeanScope.REQUEST)
public class CsUserBean extends GridFormView {
	
	
	@SaveState
	public CsUser data = new CsUser();
	
	@SaveState
	public String username = "";
	
	@ManagedProperty("#{sysCorporationDao}")
	protected SysCorporationDao sysCorporationDao;
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@SaveState
	@Accessible
	public String customerid;


	@Bind
	public UIButton qryRefresh;
	@Bind
	public UIButton adddata;
	@Bind
	public UIButton markInvalid;
	@Bind
	public UIButton del;
	@Bind
	public UIButton mailNotice;

	@Override
	public void grid_ondblclick() {
		String id = this.grid.getSelectedIds()[0];
		data = (CsUser)serviceContext.userMgrService.csUserDao.findById(Long.valueOf(id));
		SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(data.getCorpid());
		if(sysCorporation!=null){
			customerabbr =sysCorporation.getAbbr();
		}
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		this.username ="";
		if(this.data!=null&&this.data.getSalesid() != null){
			SysUser us = serviceContext.userMgrService.sysUserDao.findById(this.data.getSalesid());
			if(us  != null){
				if(us.getDeptid() != null){
					SysDepartment sd = serviceContext.sysDeptService.sysDepartmentDao.findById(us.getDeptid());
					this.username = us.getNamec()+"/"+us.getNamee()+"/"+(sd!=null?sd.getName():"");
				}else{
					this.username = us.getNamec()+"/"+us.getNamee();
				}
			}
		}
		Browser.execClientScript("flexboxs('"+data.getCorpid()+"','"+this.username+"')");
		super.grid_ondblclick();
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.customerid =AppUtils.getReqParam("customerid").trim();
			if(!StrUtils.isNull(this.customerid)) {
				qryMap.put("t.corpid$", Long.parseLong(this.customerid));
			}else{
				this.customerid = "0";
			}
		}
		this.update.markUpdate(UpdateLevel.Data, "gridPanel");


		String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
				qryRefresh.setDisabled(true);
				adddata.setDisabled(true);
				markInvalid.setDisabled(true);
				del.setDisabled(true);
				mailNotice.setDisabled(true);
			}
		} catch (Exception e) {
		}
	}
	
	/*@Override
	public void save() {
		data.setId((long)0);
		serviceContext.userMgrService.saveCsUser(data);
		this.data = serviceContext.userMgrService.csUserDao.findById(data.getId());
		this.alert("OK");
		this.refresh();
	}*/
	
	@Override
	public void add() {
		super.add();
		data = new CsUser();
		data.setId(0l);
		data.setInputer(AppUtils.getUserSession().getUsercode());
		data.setInputtime(Calendar.getInstance().getTime());
		data.setCorpid(Long.valueOf(customerid));
		SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(data.getCorpid());
		if(sysCorporation!=null){
			customerabbr =sysCorporation.getAbbr();
		}
		this.username = "";
		SysUser us = serviceContext.sysUserAssignMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
		if(us.getIssales()){
			data.setSalesid(us.getId());
			if(us.getDeptid() != null){
				SysDepartment sd = serviceContext.sysDeptService.sysDepartmentDao.findById(us.getDeptid());
				this.username = us.getNamec()+"/"+us.getNamee()+"/"+(sd!=null?sd.getName():"");
			}else{
				this.username = us.getNamec()+"/"+us.getNamee();
			}
		}
		Browser.execClientScript("flexboxs('"+data.getCorpid()+"','"+this.username+"')");
	}
	
	/**
	 * 邮件通知
	 */
	@Action
	public void mailNotice() {
		String ids[] = this.grid.getSelectedIds();
		if( ids.length<1 || ids==null ) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = AppUtils.getContextPath() + "/pages/sysmgr/mail/emailsendedit.xhtml?type=csuser&id="+ids[0];
		AppUtils.openWindow("_emailNotice_csuser", url);
	}
	
	/**
	 * 短信通知
	 */
	@Action
	public void smsNotice() {
		String ids[] = this.grid.getSelectedIds();
		if(ids == null) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		SysUser su = this.serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(ids[0]));
		String mobilephone = null;
		if(su != null) {
			mobilephone = su.getMobilephone();
		}
		
		String url = AppUtils.getContextPath() + "/pages/sysmgr/sms/smssend.xhtml?mobilephone=" + mobilephone;
		AppUtils.openWindow("_emailNotice_csuser", url, 720, 400);
	}
	
	
	@Bind
	public Long corporationid;
	
	@Bind
	public Long departmentid;

	
	@Bind
	public UIWindow uiWindow;
	@Bind
	public String newPWD1;
	@Bind
	public String newPWD2;
	
	@Bind
	public Long userid;
	
	
	@Action
	public void markInvalid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		serviceContext.userMgrService.modifyUserInvalid(id , false);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void modifyPWD(){
		if(0 == data.getId()){
			MessageUtils.alert("请选择用户!");
			return;
		}
		this.userid = data.getId();
		this.newPWD1 = "";
		this.newPWD2 = "";
		this.uiWindow.show();
		this.update.markUpdate(UpdateLevel.Data,"userid");
	}
	
	@Action
	public void savePWD(){
		if(!newPWD1.equals(newPWD2)){
			MessageUtils.alert("两次输入密码不一致，请重新输入！");
			this.newPWD1 = "";
			this.newPWD2 = "";
			return;
		}
		try {
			serviceContext.userMgrService.modifyPwd(newPWD1 , data);
			MessageUtils.alert("密码修改成功！");
			this.uiWindow.close();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("服务端错误！"+e.getLocalizedMessage());
			return;
		}
	}
	
	

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		serviceContext.userMgrService.delCsUser(id);
		MessageUtils.alert("OK!");
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {
		this.data = serviceContext.userMgrService.csUserDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		String salt = CommonUtil.getRandom(5);
		String ps = serviceContext.webConfigService.findWebConfig("csuserDefaultPassword");
		data.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(ps)+salt));
		data.setSecretkey(salt);
		serviceContext.userMgrService.saveCsUser(data);
		this.data = serviceContext.userMgrService.csUserDao.findById(data.getId());
		this.pkVal = data.getId();
	}
	
	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return customerService.getCustomerDataProvider();
	}

	@Action
	public void showCustomer() {
		this.popQryKey = AppUtils.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}
	
	@Bind
	private String customerabbr;

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		data.setCorpid(((Long)m.get("id")).longValue());
		customerabbr = (String) m.get("abbr");
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customerabbr");
		showCustomerWindow.close();
	}

	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
//		if(StrUtils.isNull(this.customerid)) {
//			Map m = super.getQryClauseWhere(queryMap);
//			//过滤 权限客户
//			m.put("filter", AppUtils.custCtrlClauseWhere());
//			return m;
//		}
		Map m = super.getQryClauseWhere(queryMap);
//		String qry = StrUtils.getMapVal(m, "qry");
//		qry += "\nAND customerid = " + this.customerid;
//		m.put("qry", qry);
//		m.put("filter", AppUtils.custCtrlClauseWhere());
		return m;
	}

}
