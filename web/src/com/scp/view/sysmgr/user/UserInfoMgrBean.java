package com.scp.view.sysmgr.user;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.DownloadListener;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.dao.sys.SysDepartmentDao;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.EncoderHandler;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
import com.scp.view.comp.action.ActionGridExport;

@ManagedBean(name = "pages.sysmgr.user.userinfomgrBean", scope = ManagedBeanScope.REQUEST)
public class UserInfoMgrBean extends FormView {
	
	@ManagedProperty("#{comboxBean}")
	protected CommonComBoxBean commonComBoxBean;
	
	@Resource
	public SysDepartmentDao sysDepartmentDao;
	
	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	public Long company;
	
	@Bind
	public Long department;
	
	@Bind
	public Long corpidCurrent;
	
	//@Bind
	//public UIButton changeCorpid;
	
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> departments;
	
	@Bind
    @SelectItems
    @SaveState
	private String warehouses;
	
	@SaveState
	@Accessible
	public SysUser selectedRowData = new SysUser();
	
	@SaveState
	public String username;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			// 初始仓库下拉
			this.warehouses = this.getWarehouse();
			selectedRowData = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
			if(selectedRowData.getSysCorporation() != null ) {
				this.company = selectedRowData.getSysCorporation().getId();
			}
			if(selectedRowData.getSysDepartment() != null) {
				this.department = selectedRowData.getSysDepartment().getId();
			}
			
			if(departments == null) {
				departments = this.queryDepts(this.company);
			}
			this.initCtrl();
			this.update.markUpdate(UpdateLevel.Data, "company");
			this.update.markUpdate(UpdateLevel.Data, "department");
			this.refresh();
			
			if(this.selectedRowData.getParentid() != null && this.selectedRowData.getParentid() > 0){
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getParentid());
				if(us != null){
					if(us.getDeptid() != null){
						SysDepartment sd = serviceContext.sysDeptService.sysDepartmentDao.findById(us.getDeptid());
						this.username = us.getNamec()+"/"+us.getNamee()+"/"+sd.getName();
					}else{
						this.username = us.getNamec()+"/"+us.getNamee();
					}
				}
			}
			
		}
		
	}
	private void initCtrl() {
		//changeCorpid.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.user_info_mgr.getValue())) {
			if (s.endsWith("_changecorp")) {
				//changeCorpid.setDisabled(false);
			} 
		}
	}
	@Action
    private void changeDept() {
		String company = AppUtils.getReqParam("company");
        if(company !=null) {
        	this.company = Long.parseLong(company);
        	departments = queryDepts(this.company);
        	this.department = null;
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
	
	/**
	 * 查询用户属于的仓库（下拉）
	 * @return
	 */
	private String getWarehouse() {
		try {
			String warehouses ="";
			Long userid = AppUtils.getUserSession().getUserid();
			String SQL = "select f_lists(D.namec) " +
					"from sys_user_assign S join dat_warehouse D on S.linkid = D.id " +
					"where linktype='W' and userid ="+userid+"";
			List<Object[]> lists = serviceContext.userMgrService.sysUserDao.executeQuery(SQL);
			warehouses =lists.toString();    
			return warehouses;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Override
	public void save() {
//		SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(this.company);
		SysDepartment sd = this.sysDepartmentDao.findById(this.department);
//		this.selectedRowData.setSysCorporation(sc);
		this.selectedRowData.setSysDepartment(sd);
		try {
			this.serviceContext.userMgrService.saveUser(selectedRowData);
			//MessageUtils.alert("OK");
			Browser.execClientScript("showmsg()");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Bind
	public UIWindow uiWindow;
	@Bind
	public String oldPWD;
	@Bind
	public String newPWD1;
	@Bind
	public String newPWD2;
	
	@Action
	public void modifyPWD(){
		this.oldPWD = "";
		this.newPWD1 = "";
		this.newPWD2 = "";
		this.uiWindow.show();
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
			if(!serviceContext.userMgrService.checkPwd(selectedRowData , oldPWD)) {
				MessageUtils.alert("当前密码不正确，请重新输入！");
				this.oldPWD = "";
				this.newPWD1 = "";
				this.newPWD2 = "";
				return;
			}
//			if(applicationConf.getIsUseDzz()){
//				//serviceContext.userMgrService.modifyPwd(newPWD1 , data);
//				if(selectedRowData.getFmsid()==null || selectedRowData.getFmsid() <= 0){
//					MessageUtils.alert("fmsid为空!");
//				}else{
//					//UPDATE MySQL DataBase.yos_user
//					String dzznewpwd = "SELECT f_dzz_accountnewpwd("+selectedRowData.getFmsid()+",'"+newPWD1+"');";
//					Map m2 = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(dzznewpwd);
//					
//					//UPDATE sys_user
//					String dzz_user_pwd = "SELECT password,salt FROM yos_user WHERE uid = "+selectedRowData.getFmsid();
//					Map m3 = this.serviceContext.dzzService.daoIbatisTemplateMySql.queryWithUserDefineSql4OnwRow(dzz_user_pwd);
//					if(m3!=null && m3.containsKey("password") && m3.containsKey("salt")){
//						selectedRowData.setCiphertext(String.valueOf(m3.get("password")));
//						selectedRowData.setSecretkey(String.valueOf(m3.get("salt")));
//						serviceContext.userMgrService.sysUserDao.modify(selectedRowData);
//					}
//				}
//			}else{
				String salt = CommonUtil.getRandom(5);
				selectedRowData.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(newPWD1)+salt));
				selectedRowData.setSecretkey(salt);
				serviceContext.userMgrService.sysUserDao.modify(selectedRowData);
//			}
			MessageUtils.alert("密码修改成功！");
			this.uiWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow changeCorpidWindow;
	
	@Action
	public void changeCorpid(){
		this.corpidCurrent = AppUtils.getUserSession().getCorpidCurrent() == null ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent();
		update.markUpdate(UpdateLevel.Data, "corpidCurrent");
		changeCorpidWindow.show();
	}
	
	@Action
	public void saveChangeCorpid(){
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.user_info_mgr.getValue())) {
			if (s.endsWith("_changecorp")) {
				AppUtils.getUserSession().setCorpidCurrent(this.corpidCurrent);
				try {
					ConfigUtils.refreshUserCfg("corpidCurrent",this.corpidCurrent.toString(),AppUtils.getUserSession().getUserid());
				} catch (Exception e) {
					MessageUtils.showException(e);
				}
				MessageUtils.alert("OK!");
				changeCorpidWindow.close();
			}
		}
	}
	
	@Bind(id="parentid")
	private List<SelectItem> getqueryParentid(){
		try {
			if(selectedRowData!=null){
				return CommonComBoxBean.getComboxItems("d.id", "d.namec||'/'||d.namee", "sys_user as d", "WHERE d.isdelete = FALSE AND d.code !='"
						+selectedRowData.getCode()+"'", "");
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.sysmgr.user.userinfomgrBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.sysmgr.user.userinfomgrBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		qry += "AND t.parentid = "+selectedRowData.getId()+"";
		map.put("qry", qry);
		return map;
	}
	
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";

				if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='"
								+ dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index)
									+ "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							buffer.append("\nAND UPPER(" + key
									+ ") LIKE UPPER('%'||" +"TRIM('"+ val+"')" + "||'%')");
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		map.put("limit", limits+"");
		map.put("start", starts+"");
		map.put("qry", qry);
		return map;
	}
	
	@Bind
	public UIWindow searchWindow;
	
	@Action
	public void qureyguarantee() {
		this.searchWindow.show();
	}
	
	@Bind
	public UIDataGrid guaranteegrid;
	
	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		Map map = queryMap;
		map.put("qry", "\n AND userid = "+ AppUtils.getUserSession().getUserid());
		return map;
	}
	
	
	@Bind(id = "guaranteegrid", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider2() {
		return new GridDataProvider() {
			@SuppressWarnings("deprecation")
			@Override
			public Object[] getElements() {
				String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapUser), start, limit).toArray();

			}

			@SuppressWarnings("deprecation")
			@Override
			public int getTotalCount() {
				String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapUser));
//				UserBean u = new UserBean();
//				newcreditlimit = String.valueOf(list.get(0).get("creditlimit"));
//				u.setNewcreditlimit(String.valueOf(list.get(0).get("creditlimit")));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Action
	public void export() {
		ActionGridExport actionGridExport = new ActionGridExport();
		actionGridExport.setKeys((String) AppUtils.getReqParam("key"));
		actionGridExport.setVals((String) AppUtils.getReqParam("value"));
		int limitsNew = limits;
		int startsNew = starts;
		try {
			limits = 100000;
			starts = 0;
			
			String sqlId = "pages.sysmgr.user.userBean.guaranteegrid.page";
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapUser));
			
			if(guaranteegrid != null){
				List<Map> listNew = new ArrayList<Map>();
				String[] ids = this.guaranteegrid.getSelectedIds();
				boolean flag = false;
				if(ids != null && ids.length != 0) {
					flag = true;
					for (Map map : list) {
						for (String id : ids) {
							if(StrUtils.getMapVal(map, "id").equals(id)){
								listNew.add(map);
							}
						}
					}
				}
				if(flag){
					list = listNew;
				}
			}
			
			actionGridExport.execute(list);
			Browser.execClientScript("simulateExport.fireEvent('click');");
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally{
			limits = limitsNew;
			starts = startsNew;
		}
	}
	
	@DownloadListener
	public void doSimulateExport(FacesContext context, ResponseStream out) {
		ActionGridExport actionGridExport = new ActionGridExport();
		try {
			actionGridExport.exportExcelFile(context, out);
		} catch (IOException e) {
			MessageUtils.showException(e);
		}
	}
	
}
