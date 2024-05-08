package com.scp.view.sysmgr.user;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.AppSessionLister;
import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.dao.sys.SysCustlibDao;
import com.scp.dao.sys.SysDepartmentDao;
import com.scp.dao.sys.SysUseronlineDao;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysCustlib;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.SysUseronline;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.EncoderHandler;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;


@ManagedBean(name = "pages.sysmgr.user.userBean", scope = ManagedBeanScope.REQUEST)
public class UserBean extends GridFormView {
	
	@ManagedProperty("#{comboxBean}")
	protected CommonComBoxBean commonComBoxBean;
	
	@Resource
	public SysDepartmentDao sysDepartmentDao;
	
	@SaveState
	public SysUser data = new SysUser();
	
	@Bind
	public Long company;
	
	@Bind
	public Long department;
	
	@Bind
	public UIWindow linkReportSetWindow;
	
	@Bind
	private UIIFrame linkReportSetIFrame;
	
	@Bind
	public UIWindow showReportSetWindow;
	
	@Bind
	private UIIFrame showReportSetIFrame;

	@Bind
	public UIWindow showLogsWindow;

	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	@SaveState
	public String qryparent;

	@Bind
	public UIIFrame traceIframe;
	
	@Bind
	@SaveState
	public String logocode;


	@Bind(id="deptids")
	@SelectItems
	private List<SelectItem> getqueryDepartid(){
		try {
			if(data!=null && data.getId() > 0&&data.getCustomerid()!=null){
				return CommonComBoxBean.getComboxItems("d.id", "d.name", "sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid ="+
						"(SELECT corpid FROM sys_user WHERE isdelete = FALSE AND id = "+ data.getId()+")", "ORDER BY d.name");
			}else{
				return CommonComBoxBean.getComboxItems("d.id", "d.name", "sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid ="+
						(company == null ?"-1":company), "ORDER BY d.name");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Action
    private void changeDept() {
		String company = AppUtils.getReqParam("company");
        if(company !=null) {
        	this.company = Long.parseLong(company);
//        	departments = queryDepts(this.company);
//        	this.department = -1l;
        	getqueryDepartid();
          this.update.markUpdate(UpdateLevel.Data, "department");
        }
    }
	
	private List<SelectItem> queryDepts(Long company) {
		if(company != null) {
			List<SelectItem> selectItems = commonComBoxBean.getDeptByCorpid(company);
			selectItems.add(new SelectItem(null, ""));
			return selectItems;
		}
		return null;
    }
	
	
	@Override
	public void grid_ondblclick() {
		
		String id = this.grid.getSelectedIds()[0];
		data = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
		this.company = data.getSysCorporation() == null?null:data.getSysCorporation().getId();
//		this.department = data.getSysDepartment() == null?null:data.getSysDepartment().getId();
//		if(departments == null) {
//			departments = this.queryDepts(this.company);
//		}
		if(!data.getIssales()){
			Browser.execClientScript("salesLinkUserJsVar.setDisabled(true);salesLinkGroupJsVar.setDisabled(true);");
		}else{
			Browser.execClientScript("salesLinkUserJsVar.setDisabled(false);salesLinkGroupJsVar.setDisabled(false);");
		}
		
		String url = "./userole.xhtml?id="+id;
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
			url = "/scp/common/blank.html";
			Browser.execClientScript("editWindowJsVar.setHeight(256)");
		}
		linkRolesIFrame.setSrc(url);
		update.markAttributeUpdate(linkRolesIFrame, "src");
		guaranteeuserid = this.grid.getSelectedIds()[0];

		//引用数据浏览器，移除
//		guaranteegrid.reload();
		super.grid_ondblclick();
	}
	
	
	
	@Override
	public void save() {
		SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(this.company);
//		SysDepartment sd = this.sysDepartmentDao.findById(this.department);
		this.data.setSysCorporation(sc);
//		this.data.setSysDepartment(sd);
//		String csno = ConfigUtils.findSysCfgVal("CSNO");
		try {
			if(this.data != null && this.data.getDeptid() != null){
				try {
					SysDepartment depar = this.serviceContext.sysDeptService.sysDepartmentDao.findById(this.data.getDeptid());
					this.data.sysDepartment = depar;
				} catch (NoRowException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(null != data && null != data.getNamec() && null != data.getNamee()){
				data.setNamec(data.getNamec().trim());
				data.setNamee(data.getNamee().trim());
			}
			if(data != null && data.getId()<=0){
				//姓名首尾去空格
				try {
					Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT exists (SELECT 1 FROM sys_user WHERE isdelete = FALSE AND namec = '"+data.getNamec()+"')");
					
					if(map!=null && map.containsKey("exists")){
						if(Boolean.parseBoolean(map.get("exists").toString())){
							MessageUtils.alert("用户中文名已经存在!");
							return;
						}
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
//				if("8888".equalsIgnoreCase(csno)){
//					try {
//						//直接新增DZZ里面的账号
//						String dzzSql = "SELECT f_dzz_accountadd('"+data.getEmail1()+"','"+data.getMobilephone()+"','"+data.getNamec()+"','"+data.getNamec()+"','"+Calendar.getInstance().getTime().getTime()+"') AS T ;";
//						Map m = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(dzzSql);
//						String mapVal = StrUtils.getMapVal(m, "T");
//						if (mapVal.equals("exists")){
//							this.alert("DZZ用户中文名或邮箱重复,请联系开发人员");
//							return ;
//						}
//						String idsql = "SELECT uid FROM yos_user WHERE nickname = '"+data.getNamec()+"' OR email = '"+data.getEmail1()+"'";
//						Map m2 = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(idsql);
//						String mapVal2 = StrUtils.getMapVal(m2, "uid");
//					if(!StrUtils.isNull(mapVal2)){
//						data.setFmsid(Long.parseLong(mapVal2));
//					}
//					
//					} catch(NoRowException e){
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//						return;
//					}
//				}
//			}else if("8888".equalsIgnoreCase(csno) && data != null && data.getId()>0){
//				String idsql = "UPDATE yos_user SET nickname = '"+data.getNamec()+"',email = '"+data.getEmail1()+"',username = '"+data.getNamec()+"' WHERE uid = "+data.getFmsid() + " AND status <> 1;";
//				Vector<String> ve = new Vector<String>();
//				ve.add(idsql);
//				this.serviceContex.t.dzzService.daoIbatisTemplateMySql.executeQueryBatchByJdbc(ve);
			}
			serviceContext.userMgrService.saveUser(data);
			
			this.alert("OK");
			this.refresh();
			update.markAttributeUpdate(linkRolesIFrame, "src");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void refresh() {
		super.refresh();
		try {
			this.data = this.serviceContext.userMgrService.sysUserDao.findById(data.getId());
			update.markUpdate(true,UpdateLevel.Data, "editPanel");
			
			String url = "./userole.xhtml?id="+this.data.getId();
			if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
				url = "/scp/common/blank.html";
			}
			linkRolesIFrame.setSrc(url);
			update.markAttributeUpdate(linkRolesIFrame, "src");
			
		}catch (NoRowException e) {
			this.add();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	@Override
	public void add() {
		if(!checkUserThanLicUserCount()){
			this.editWindow.close();
			return;
		}
		data = new SysUser();
		try {
			serviceContext.userMgrService.rasCrypto(data);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("服务端错误！"+e.getLocalizedMessage());
			return;
		}
		super.add();
		data.setInputer(AppUtils.getUserSession().getUsercode());
		data.setInputtime(Calendar.getInstance().getTime());
	}
	
	@Override
	public void qryAdd() {
		if(!checkUserThanLicUserCount())return;
		super.qryAdd();
		
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
			String url = "/scp/common/blank.html";
			Browser.execClientScript("editWindowJsVar.setHeight(256)");
			linkRolesIFrame.setSrc(url);
			update.markAttributeUpdate(linkRolesIFrame, "src");
		}
	}
	
	/**
	 * @author neo
	 * 检查系统有效用户是否超出授权用户
	 */
	private boolean checkUserThanLicUserCount(){
		Integer sysuser = AppUtils.getSysUser();//授权用户数
		String sql = "SELECT count(id) AS sysusercount FROM  sys_user WHERE isdelete = FALSE AND isinvalid = TRUE;" ;
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String sysusercount = m.get("sysusercount").toString();
		int syscount = Integer.parseInt(sysusercount);
		if(sysuser > syscount){
			return true;
		}else{
			String url = "/scp/pages/sysmgr/user/usercountlack.jsp?sysuser="+sysuser+"&syscount="+syscount+" ";
			usercountlackIFrame.setSrc(url);
			update.markAttributeUpdate(usercountlackIFrame, "src");
			update.markUpdate(true, UpdateLevel.Data, usercountlackWindow);
			usercountlackWindow.setTitle("新增用户失败");
			usercountlackWindow.show();
			//Browser.execClientScript("window.open('/scp/pages/sysmgr/user/usercountlack.jsp?sysuser="+sysuser+"&syscount="+syscount+"')");
		}
		return false;
	}
	
	@Action
	public void viewPermission() {
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String winId = "_viewpermission";
		String url = "./viewpermission.xhtml?id=" + this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Bind
	public UIWindow showModuleCtrlWindow;
	@Bind
	private UIIFrame dtlIFrame;
	
	@Action
	public void showModuleCtrl() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length >1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = "../role/modrole.xhtml?type=user&id="+ids[0];
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, showModuleCtrlWindow);
		showModuleCtrlWindow.show();
	}
	
	@Action
	public void clearOpenid() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			String sql = "UPDATE sys_user SET opneid = null WHERE id IN("+StrUtils.array2List(ids)+");";
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.refresh();
			this.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	@Bind
	public UIWindow linkSalesWindow;
	
	@Bind
	public UIWindow usercountlackWindow;
	
	@Bind
	private UIIFrame linkSalesIFrame;
	
	@Bind
	public UIWindow linkUserWindow;
	
	@Bind
	private UIIFrame linkUserIFrame;
	
	@Bind
	private UIIFrame usercountlackIFrame;
	
	@Bind
	private UIIFrame linkRolesIFrame;
	
	@Action
	public void linkSales() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length >1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = "./linksales.xhtml?id="+ids[0];
		linkSalesIFrame.setSrc(url);
		update.markAttributeUpdate(linkSalesIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkSalesWindow);
		linkSalesWindow.show();
	}
	
	@Resource
	public SysCustlibDao sysCustlibDao;
	
	@Action
	public void salesLinkUser() {
		if(this.pkVal<=0)return;
		Long libid = 0l;
		List<SysCustlib> list = sysCustlibDao.findAllByClauseWhere("userid = "+this.pkVal);
		if(list == null || list.size()<=0){
		}else{
			libid = list.get(0).getId();
		}
		if(libid == 0l)return;
		String url = "/scp/pages/module/customer/custlibuser.aspx?libid="+libid;
		linkUserIFrame.setSrc(url);
		update.markAttributeUpdate(linkUserIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkUserWindow);
		linkUserWindow.setTitle("业务员关联用户");
		linkUserWindow.show();
	}
	
	@Action
	public void salesLinkGroup() {
		if(this.pkVal<=0)return;
		Long libid = 0l;
		List<SysCustlib> list = sysCustlibDao.findAllByClauseWhere("userid = "+this.pkVal);
		if(list == null || list.size()<=0){
		}else{
			libid = list.get(0).getId();
		}
		if(libid == 0l)return;
		String url = "./salesgroup.aspx?libid="+libid+"&userid="+this.pkVal;
		linkUserIFrame.setSrc(url);
		update.markAttributeUpdate(linkUserIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkUserWindow);
		linkUserWindow.setTitle("业务员关联组");
		linkUserWindow.show();
	}
	
	@Bind
	public UIWindow linkActSetWindow;
	
	@Bind
	private UIIFrame linkActSetIFrame;
	
	@Action
	public void linkActSet() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length >1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = "./useractset.xhtml?id="+ids[0];
		linkActSetIFrame.setSrc(url);
		update.markAttributeUpdate(linkActSetIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkActSetIFrame);
		linkActSetWindow.setTitle("关联账套");
		linkActSetWindow.show();
	}
	
	@Action
	public void linkCorp() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length >1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = "./userlinkcorp.xhtml?id="+ids[0];
		linkActSetIFrame.setSrc(url);
		update.markAttributeUpdate(linkActSetIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkActSetIFrame);
		linkActSetWindow.setTitle("授权分公司");
		linkActSetWindow.show();
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
	
	
	@SuppressWarnings("deprecation")
	@Action
	public void markInvalid(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		try {
			serviceContext.userMgrService.modifyUserInvalid(id , false);
			String fmsidsql = "SELECT string_agg(fmsid::TEXT,',') AS fmsid FROM sys_user WHERE id IN ("+StrUtils.array2List(ids)+");";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(fmsidsql);
//			if(applicationConf.getIsUseDzz() && map.containsKey("fmsid")){
//				String dzzinvalsql = "UPDATE yos_user SET status = 1,email = CONCAT(email,'-isdelete'),nickname = CONCAT(nickname,'-isdelete') WHERE status <> 1 AND uid IN ("+map.get("fmsid").toString()+");";
//				serviceContext.dzzService.daoIbatisTemplateMySql.updateWithUserDefineSql(dzzinvalsql);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		offline();
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	
	@Action
	public void markInvalidTrue(){
		if(!checkUserThanLicUserCount()){
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		try {
			serviceContext.userMgrService.modifyUserInvalid(id , true);
//			String fmsidsql = "SELECT string_agg(fmsid::TEXT,',') AS fmsid FROM sys_user WHERE id IN ("+StrUtils.array2List(ids)+");";
//			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(fmsidsql);
//			if(applicationConf.getIsUseDzz() && map.containsKey("fmsid")){
//				String dzzinvalsql = "UPDATE yos_user SET status = 1,email = CONCAT(email,'-isdelete'),nickname = CONCAT(nickname,'-isdelete') WHERE status <> 1 AND uid IN ("+map.get("fmsid").toString()+");";
//				serviceContext.dzzService.daoIbatisTemplateMySql.updateWithUserDefineSql(dzzinvalsql);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			String csno = ConfigUtils.findSysCfgVal("CSNO");
//			if("8888".equalsIgnoreCase(csno)){
//				//serviceContext.userMgrService.modifyPwd(newPWD1 , data);
//				if(data.getFmsid()==null || data.getFmsid() <= 0){
//					MessageUtils.alert("fmsid为空!");
//				}else{
//					
//					//UPDATE MySQL DataBase.yos_user
//					String dzznewpwd = "SELECT f_dzz_accountnewpwd("+data.getFmsid()+",'"+newPWD1+"');";
//					Map m2 = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(dzznewpwd);
//					
//					//UPDATE sys_user
//					String dzz_user_pwd = "SELECT password,salt FROM yos_user WHERE uid = "+data.getFmsid();
//					Map m3 = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(dzz_user_pwd);
//					if(m3!=null && m3.containsKey("password") && m3.containsKey("salt")){
//						data.setCiphertext(String.valueOf(m3.get("password")));
//						data.setSecretkey(String.valueOf(m3.get("salt")));
//						serviceContext.userMgrService.sysUserDao.modify(data);
//					}
//				}
//			}else{
				String salt = CommonUtil.getRandom(5);
				data.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(newPWD1)+salt));
				data.setSecretkey(salt);
				serviceContext.userMgrService.sysUserDao.modify(data);
//			}
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
		try {
			serviceContext.userMgrService.delUser(id);
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}
	
	
	@Bind
	public UIButton importData;
	
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
				String callFunction = "f_imp_sys_user";
				String args = "'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	@Action
	public void showReport(){
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = "./usershowreport.xhtml?id=" + this.getGridSelectId();
		showReportSetIFrame.setSrc(url);
		update.markAttributeUpdate(showReportSetIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, showReportSetIFrame);
		showReportSetWindow.setTitle("查看报表权限");
		showReportSetWindow.show();
	}
	@Action
	public void linkReport(){
		Long id = this.getGridSelectId();
		if(id == null || id <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String url = "./userlinkreport.xhtml?id=" + this.getGridSelectId();
		linkReportSetIFrame.setSrc(url);
		update.markAttributeUpdate(linkReportSetIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, linkReportSetIFrame);
		linkReportSetWindow.setTitle("授权报表权限(独立)");
		linkReportSetWindow.show();
	}

	@Action
	public void setLogincount(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		for(int i=0;i<ids.length;i++){
			SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(ids[i]));
			if(sysUser!=null&&sysUser.getCode()!=null){
				serviceContext.loginService.setLogincount(sysUser.getCode());
			}
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = "";
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = t.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())filter += qry;//非saas模式不控制
		
		if("orgframework".equalsIgnoreCase(src)){
			filter += "\nAND EXISTS(SELECT 1 FROM f_sys_corporation_framework('refid="+refid+"') x WHERE x.id = t.deptid)";
		}
		if(!StrUtils.isNull(qryparent)){
			filter += "\nAND EXISTS(SELECT 1 FROM sys_user WHERE isdelete = FALSE AND t.parentid = id " +
					  "AND (code LIKE '%"+qryparent+"%' OR namec LIKE '%"+qryparent+"%' OR namee LIKE '%"+qryparent+"%'))";
		}
		
		
		String filter2 = "\nAND 1=1";
		if(81433600 != AppUtils.getUserSession().getUserid()){
			filter2 += "\nAND id<>81433600";
		}
		map.put("filter2", filter2);
		map.put("filter", filter);
		return map;
	}
	
	@Bind
	@SaveState
	public String src = "";
	@Bind
	@SaveState
	public String refid = "";

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){
				String js = //"qryAddJsVar.hide();"+
							"del.hide();"+
							//"markInvalid.hide();"+
							//"markInvalidTrue.hide();"+
							"linkCorp.hide();"+
							"addJsVar.hide();"+
							"importDataJsVar.hide();"+
							"linkActSetJsVar.hide();"+
							"salesLinkUserJsVar.hide();"+
							"salesLinkGroupJsVar.hide();"+
							"setParentidsshowJsVar.hide();"+
							"syncGroupCtrlVar.hide();"+
							"";
				Browser.execClientScript(js);
			}
			
			src = AppUtils.getReqParam("src");
			refid = AppUtils.getReqParam("refid");
			
			this.logocode = AppUtils.getUserSession().getUsercode();
		}
	}
	
	@Bind(id="parentid")
	private List<SelectItem> getqueryParentid(){
		try {
			if(data!=null){
				return CommonComBoxBean.getComboxItems("d.id", "d.namec||'/'||d.namee", "sys_user as d", "WHERE d.isdelete = FALSE AND d.code !='"
						+data.getCode()+"'", "");
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Bind(id="parentids")
	private List<SelectItem> getqueryParentids(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			return null;
		}
		try {
//			System.out.println("WHERE d.isdelete = FALSE AND d.id != ALL(array["+StrUtils.array2List(ids)+"])");
			//返回不包含勾选的
			return CommonComBoxBean.getComboxItems("d.id", "d.namec||'/'||d.namee", "sys_user as d"
					, "WHERE d.isdelete = FALSE AND d.id != ALL(array["+StrUtils.array2List(ids)+"])","");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void clearQryKey() {
		// TODO Auto-generated method stub
		qryparent = "";
		super.clearQryKey();
	}
	
	@Action
	public void setParentids(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		for(String id:ids){
			SysUser sysuser = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(id));
			sysuser.setParentid(parentidsgvalue);
			serviceContext.userMgrService.sysUserDao.modify(sysuser);
		}
		qryRefresh();
	}
	
	@Bind
	public UIWindow setParentidswindow;
	
	@Bind
	public Long parentidsgvalue;
	
	@Action
	public void setParentidsshow(){
		setParentidswindow.show();
		update.markUpdate("parentidsgvalue");
	}

	/**
	 * 每勾选或者取消勾选都重新刷新下拉框
	 */
	@Action
	public void onrowselect() {
		setParentidswindow.close();
		update.markUpdate("parentidsgvalue");
	}
	
	
	@Action
	public void syncGroupCtrl() {
		if(this.data.getId() <= 0)return;
		try {
			String sql = "SELECT f_sys_corporation_framework_sync('type=Group2UserChangeDept;userid="+this.data.getId()+";deptidold=0;deptidnew="+this.data.getDeptid()+"');";
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			this.alert("OK");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			this.alert("ERROR");
		}
	}
	
	
	@Resource
	public SysUseronlineDao sysUseronlineDao;
	
	@Action
	public void offline(){
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Long id = Long.valueOf(ids[0]);
		List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere("userid = " + id + " AND isonline = 'Y'");
		if(sysUseronlines != null && sysUseronlines.size() > 0) {
			for (SysUseronline sysUseronline : sysUseronlines) {
				sysUseronline.setIsonline("N");
				sysUseronline.setLogouttime(new Date());
				sysUseronline.setIsvalid(false);
				sysUseronlineDao.modify(sysUseronline);
				
				Map onlineSessionMap  = AppSessionLister.onlineSessionMap;
				onlineSessionMap.remove(sysUseronline.getUserid());
				AppSessionLister.sessionMap.remove(sysUseronline.getSessionid());
			}
		}
		//AppUtils.getHttpSession().invalidate();
		MessageUtils.alert("OK!");
	}

	@Bind
	public UIWindow searchWindow;

    @Bind
    public UIIFrame searchIFrame;

	@Action
	public void qureyguarantee() {
        searchIFrame.setSrc("/scp/pages/module/commerce/userchild.html?id=" + guaranteeuserid);
        update.markAttributeUpdate(searchIFrame, "src");
        searchWindow.setTitle("信用额度明细");
        searchWindow.show();
	}
	
	@Bind
	@SaveState
	public String guaranteeuserid;
	
	@Bind
	@SaveState
	public String newcreditlimit;
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("qry", "\n AND userid = "+ guaranteeuserid);
		return map;
	}
	
	@Bind
	public UIDataGrid guaranteegrid;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind(id = "guaranteegrid", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {
			@SuppressWarnings("deprecation")
			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(guaranteeuserid)){
					return null;
				}
				String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere2(qryMapUser), start, limit).toArray();

			}

			@SuppressWarnings("deprecation")
			@Override
			public int getTotalCount() {
				if(StrUtils.isNull(guaranteeuserid)){
					return 0;
				}
				String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere2(qryMapUser));
//				UserBean u = new UserBean();
//				newcreditlimit = String.valueOf(list.get(0).get("creditlimit"));
//				u.setNewcreditlimit(String.valueOf(list.get(0).get("creditlimit")));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	@Action
	public void showLogs() {
		showLogsWindow.show();
	}

	@Action
	public void showTrace() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.html";
			traceIframe.load(blankUrl);
		} else {
			traceIframe.load("/pages/module/bus/optrace.xhtml?jobid=" + this.pkVal);
		}
	}
}
