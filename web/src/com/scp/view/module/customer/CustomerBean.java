package com.scp.view.module.customer;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.customer.customerBean", scope = ManagedBeanScope.REQUEST)
public class CustomerBean extends GridView {
	
	@Bind
	public UIWindow searchWindow;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	public String assignsalesnamecqry;
	
	@Bind
	@SaveState
	public String operationnamecqry;
	
	@Bind
	@SaveState
	private String checkstart;
	
	@Bind
	@SaveState
	private String checkend;
	
	
	@Bind
	@SaveState
	private Long day1;
	
	@Bind
	@SaveState
	private Long day2;

	@Bind
	@SaveState
	private String address;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			// qryMap.put("iscustomer$", true);
			initCtrl();
		}
	}
	
	private void initCtrl() {
		exportBtn.setStyle("display:none");
		List<String> jobsRoles = AppUtils.getUserRoleModuleCtrl(410000L);
		for (String s : jobsRoles) {
			if (s.endsWith("export")) {
				exportBtn.setStyle("display:block");
			}
		}
		String CSNO = ConfigUtils.findSysCfgVal("CSNO");
		if(!"2199".equals(CSNO)){//除华展外，其他客户关闭此功能
			exportBtn.setStyle("display:none");
		}
	}	
	
	@Action
	public void add() {
		String winId = "_edit_customer";
		String url = "./customeredit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void  addBlacklist(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		try{
			for(String id : ids){
				//System.out.println("usercode--->"+AppUtils.getUserSession().getUsercode());
				String querySql = "SELECT f_sys_corporation_black('id="+id+";usercode="+AppUtils.getUserSession().getUsercode()+"');";
				this.serviceContext.attachmentService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			}
			MessageUtils.alert("OK!");
			this.grid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow join2Window;
	
	
	@Action
	public void joinConfirm() {
		String ids[] = this.grid.getSelectedIds();
		try {
			String idto = join2Customerid;
			for (String id : ids) {
				//AppUtils.debug(id);
				String sql = new String();
				String idfm = id;
				if(idfm.equals(idto))continue;
				sql = "\nSELECT f_sys_corporation_join('idfm="+idfm+";idto="+idto+";user="+AppUtils.getUserSession().getUsercode()+"');";
				//AppUtils.debug(sql);
				this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
			}
			this.alert("OK!");
			this.refresh();
			join2Window.close();
			ishow = false;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@SaveState
	private boolean ishow;
	
	@Bind
	@SaveState
	private String join2Customerid;
	
	/**
	 * 合并客户
	 */
	@Action
	public void join() {
		String ids[] = this.grid.getSelectedIds();

		if(ids == null || ids.length < 2) {
			this.alert("请至少选择两行!");
			return;
		}
		ishow = true;
		join2Window.show();
		this.update.markUpdate(UpdateLevel.Data, "join2Customerid");
		//ishow = false;
	}
	
	@Bind(id="join2Customer")
    public List<SelectItem> getJoin2Customer() {
		if(!ishow)return null;
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 2) {
			return null;
		}
		String id = StrUtils.array2List(ids);
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.code||'/'||COALESCE(abbr,'')||'/'||COALESCE(namec,'')||'/'||COALESCE(namee,'')","sys_corporation AS d","WHERE d.id IN ("+id+")","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_customer";
		String url = "./customeredit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = AppUtils.custCtrlClauseWhere();
		
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())filter += qry;//非saas模式不控制
		
		
		//录入时间
		if(!StrUtils.isNull(dateastart) && !(StrUtils.isNull(dateend))){
			filter += "\nAND inputtime::DATE BETWEEN '" + dateastart  + "' AND '" + dateend + "'";
		}
		
		//审核时间
		if(!StrUtils.isNull(checkstart) && !(StrUtils.isNull(checkend))){
			filter += "\nAND checktime::DATE BETWEEN '" + checkstart  + "' AND '" + checkend + "'";
		}
		
		//未出货天数
		if(day1 != null && day2 != null){
			filter += "\nAND ((select now()::DATE - MAX(b.jobdate) from fina_jobs b where a.id = b.customerid AND b.isdelete = false) > "+day1+") and ((select now()::DATE - MAX(b.jobdate) from fina_jobs b where a.id = b.customerid AND b.isdelete = false) < "+day2+")";
		}

		filter += "\nAND iscustomer = true";

		if(!StrUtils.isNull(address) ){
			filter += "\nAND (addressc like '%" + address  + "%' or addresse  like  '%" + address + "%')";
		}

		map.put("filter", filter);
		String qry1 = map.get("qry").toString();
		if(!StrUtils.isNull(assignsalesnamecqry)){
			qry1 += "\n AND EXISTS(SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign y WHERE y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=x.id)" +
					"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(assignsalesnamecqry)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(assignsalesnamecqry)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(assignsalesnamecqry)+"%'))";
		}
		if(!StrUtils.isNull(operationnamecqry)){
			qry1 += "\n AND EXISTS(SELECT 1 FROM sys_user x , sys_user_assign y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='O' AND y.userid=x.id" +
			"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(operationnamecqry)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(operationnamecqry)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(operationnamecqry)+"%'))";
		}
		map.put("qry",qry1);
		return map;
	}
	
	@Action
	public void searchfee(){
		refresh();
	}
	
	@Action
	public void clear(){
		dateastart="";
		dateend="";
		checkstart="";
		checkend="";
		day1=null;
		day2=null;
		clearQryKey();
	}
	
	
	@Action
	public void savejoin() {
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1) {
			this.alert("请至少选择一行!");
			return;
		}
		try {
			for (String id : ids) {
				String sql = new String();
				sql = "\nSELECT f_sys_corporation_syn('id="+id+";type=JOIN;user="+AppUtils.getUserSession().getUsercode()+"');";
				this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
			}
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void savemodify() {
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1) {
			this.alert("请至少选择一行!");
			return;
		}
		try {
			for (String id : ids) {
				String sql = new String();
				sql = "\nSELECT f_sys_corporation_syn('id="+id+";type=MODIFY;user="+AppUtils.getUserSession().getUsercode()+"');";
				this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
			}
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void deletejoin() {
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1) {
			this.alert("请至少选择一行!");
			return;
		}
		try {
			for (String id : ids) {
				String sql = new String();
				sql = "\nDELETE FROM sys_corporation_join where idfm = "+Long.parseLong(id)+";";
				this.serviceContext.customerMgrService.sysCorporationDao.executeSQL(sql);
			}
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIButton exportBtn;
	
	@Override
	public void export() {
		List<String> jobsRoles = AppUtils.getUserRoleModuleCtrl(410000L);
		if(jobsRoles==null||jobsRoles.size()==0){
			alert("没有导出权限！");
			return;
		}else{
			for(int i = 0;i<jobsRoles.size();i++){
				if (jobsRoles.get(i).endsWith("export")) {
					break;
				}
				if(i == jobsRoles.size()-1){
					alert("没有导出权限！");
					return;
				}
			}
			super.export();
		}
	}
	
}
