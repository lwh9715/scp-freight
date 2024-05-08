package com.scp.view.module.bus;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusBookingrpt;
import com.scp.model.data.DatPort;
import com.scp.model.sys.SysUser;
import com.scp.service.bus.BusBookingrptService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
@ManagedBean(name = "pages.module.bus.bookingrpteditBean", scope = ManagedBeanScope.REQUEST)
public class BookingrpteditBean extends MastDtlView{
	
	@SaveState
	@Accessible
	public BusBookingrpt selectedRowData = new BusBookingrpt();
	
	@ManagedProperty("#{busBookingrptService}")
	public BusBookingrptService busBookingrptService;
	
	@SaveState
	public Long userid;
	
	@Bind
	public UIButton addMaster;
	
	
	@Override
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			init();
			initCtrl();
		}
	}
	
	@Override
	public void init() {
		selectedRowData = new BusBookingrpt();
		String id = AppUtils.getReqParam("id");
		if(!StrUtils.isNull(id)){
			try {
				this.mPkVal = Long.parseLong(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.refreshMasterForm();
			this.commonCheck();
		}else{
			selectedRowData.setSodate(new Date());
		}
	}
	
	
	
	
	@Bind
	public UIButton saveMaster;
	
	@Bind
	public UICheckBox isSubmitCheck;
	
	//权限控制
	private void initCtrl() {
		isSubmitCheck.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(100900900L)) {
			if (s.endsWith("_isSubmit")) {
				isSubmitCheck.setDisabled(false);
			} 
		}
	}
	
	@Override
	public void saveMaster(){
		try {
			doServiceSaveMaster(); //Master
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}
	
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isSubmit = selectedRowData.getIsSubmit();
		String updater = AppUtils.getUserSession().getUsername();
		String sql = "";
		try {
			if (this.mPkVal == -1l)
				throw new CommonRuntimeException("Plese save first!");
			if (isSubmit) {
				sql = "UPDATE bus_bookingrpt SET issubmit = TRUE,updater = '"
						+ updater + "',updatetime = NOW() WHERE id =" + this.mPkVal + ";";
			} else {
				sql = "UPDATE bus_bookingrpt SET issubmit = FALSE,updater = '"
						+ updater
						+ "',updatetime=NOW() WHERE id ="
						+ this.mPkVal + ";";
			}
			serviceContext.busBookingrptService.bookingrptDao.executeSQL(sql);
			this.commonCheck();
			refreshMasterForm();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	private void commonCheck() {
		Boolean isSubmit = selectedRowData.getIsSubmit();
		if(isSubmit == null) isSubmit = true;
		if(isSubmit){
			saveMaster.setDisabled(true);
		}else{
			saveMaster.setDisabled(false);
		}
	}



	@Bind
	@SaveState
	@Accessible
	public Long salescorpid = null;
	
	@Bind
	@SaveState
	public String deptname = null;
	
	@Bind
	@SaveState
	public String line = null;
	
	@Action
	public void lineChangeAjaxSubmit(){
		String podid = AppUtils.getReqParam("podid");
		String sql = "SELECT line FROM dat_port WHERE ispod = TRUE AND isdelete = FALSE AND id = " + podid;
		try{
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			line = StrUtils.getMapVal(map, "line");
			selectedRowData.setLine(line);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "line");
		}
	}
	

	@Action
	public void salesChangeAjaxSubmit() {
		String salesid = AppUtils.getReqParam("salesid");
		String sql  = "SELECT corpid,deptid FROM sys_user WHERE id = " + salesid;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String deptid = StrUtils.getMapVal(m, "deptid");
			//selectedRowData.setDeptid(Long.parseLong(deptid));
			String cid = StrUtils.getMapVal(m, "corpid");
			if(!StrUtils.isNull(deptid)){
				this.selectedRowData.setDeptid(StrUtils.isNull(deptid)?0l:Long.valueOf(deptid));
			}
			if(!StrUtils.isNull(cid)){
				this.salescorpid = Long.valueOf(cid);
				//this.selectedRowData.setCorpid(StrUtils.isNull(cid)?0l:Long.valueOf(cid));
			}
			//update.markUpdate(true, UpdateLevel.Data, "deptcomb");
			update.markUpdate(true, UpdateLevel.Data, "corpid");
//			this.deptids = getqueryDepartid();
		} catch (NoRowException e) {
			this.selectedRowData.setDeptid(0l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//2223 工作单编辑界面原部门下拉框 ，改为 文本显示
			deptname = serviceContext.userMgrService.getDepartNameByUserId(Long.parseLong(salesid));
		}catch(NoRowException e){
			deptname = "";
		} catch (Exception e) {
			MessageUtils.showException(e);
		}finally{
			update.markUpdate(true, UpdateLevel.Data, "deptname");
		}
	}
	
	@Override
	public void doServiceSaveMaster() {
		//System.out.println("对象："+selectedRowData.toString());
		try{
			busBookingrptService.saveData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			//System.out.println("id---->"+this.mPkVal);
			alert("OK");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}


	@Override
	public void refreshMasterForm() {
		//System.out.println("this.mPkVal--->"+this.mPkVal);
		this.selectedRowData = serviceContext.busBookingrptService.bookingrptDao.findById(this.mPkVal);
		if(this.selectedRowData.getPolid()!=null && this.selectedRowData.getPodid()!=null){
			DatPort portPol = serviceContext.portyMgrService.datPortDao.findById(this.selectedRowData.getPolid());
			DatPort portPod = serviceContext.portyMgrService.datPortDao.findById(this.selectedRowData.getPodid());
			Browser.execClientScript("$('#pol_input').val('"+portPol.getNamee()+"')");
			Browser.execClientScript("$('#pod_input').val('"+portPod.getNamee()+"')");
		}else{
			Browser.execClientScript("$('#pol_input').val('')");
			Browser.execClientScript("$('#pod_input').val('')");
		}
		if(this.selectedRowData.getSaleid()!=null){
			SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getSaleid());
			Browser.execClientScript("$('#sales_input').val('"+sysUser.getNamec()+"')");
		}else{
			Browser.execClientScript("$('#sales_input').val('')");
		}
		deptname = serviceContext.userMgrService.getDepartNameByUserId(this.selectedRowData.getSaleid());
		line = serviceContext.portyMgrService.datPortDao.findById(this.selectedRowData.getPodid()).getLine();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "deptname");
		update.markUpdate(true, UpdateLevel.Data, "line");
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	


}

