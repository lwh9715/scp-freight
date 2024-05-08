package com.scp.view.module.customer;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.sys.SysCorpinv;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.syscorpinvBean", scope = ManagedBeanScope.REQUEST)
public class SyscorpinvBean extends GridFormView{

	@SaveState
	@Accessible
	public SysCorpinv selectedRowData = new SysCorpinv();
	
	@SaveState
	@Accessible
	@Bind
	public Long linkid;
	
	@SaveState
	@Accessible
	@Bind
	public Long invoiceid;


	@Bind
	public UIButton refresh;
	@Bind
	public UIButton add;
	@Bind
	public UIButton del;
	@Bind
	public UIButton getimportInvoiceInf;
	@Bind
	public UIButton confirm;
	@Bind
	public UIButton invoiceapi;
	@Bind
	public UIButton save;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			linkid=Long.valueOf(id);
			String invid =AppUtils.getReqParam("invoiceid").trim();
			if(!StrUtils.isNull(invid)){
				invoiceid = Long.valueOf(invid);
			}
			if(invoiceid==null){//如果从客户中点进来的就影藏确认按钮
				Browser.execClientScript("confirmJs.hide()");
			}
		}

//		String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
//		try {
//			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//			if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
//				refresh.setDisabled(true);
//				add.setDisabled(true);
//				del.setDisabled(true);
//				getimportInvoiceInf.setDisabled(true);
//				confirm.setDisabled(true);
//				invoiceapi.setDisabled(true);
////				save.setDisabled(true);
//			}
//		} catch (Exception e) {
//		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		if("2208".equalsIgnoreCase(ConfigUtils.findSysCfgVal("CSNO"))){
			
		}else{
			if(isShowAll){
				qry += "";
			}else{
				qry += "\nAND linkid IN("+linkid+","+invoiceid+")";
			}
		}
		m.put("qry", qry);
		return m;
	}

	@Override
	protected void doServiceFindData() {
		Long id = this.getGridSelectId();
		if(id<=0)return;
		this.selectedRowData = serviceContext.sysCorpinvMgrService.sysCorpinvDao.findById(id);
	}

	@Override
	protected void doServiceSave() {
		try {
			selectedRowData.setLinkid(linkid);
			serviceContext.sysCorpinvMgrService.saveData(selectedRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysCorpinvMgrService.removeDate(Long.parseLong(id));
			}
			this.grid.reload();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	protected void initAdd() {
		selectedRowData = new SysCorpinv();
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Override
	public void add() {
		super.add();
		initAdd();
	}
	
	@Action
	public void confirm(){
		long id = this.getGridSelectId();
		if(!(id>0)){
			alert("请选择一行");
			return;
		}
		SysCorpinv sysCorpinv = serviceContext.sysCorpinvMgrService.sysCorpinvDao.findById(id);
		sysCorpinv.setLinkid(linkid);
		serviceContext.sysCorpinvMgrService.sysCorpinvDao.modify(sysCorpinv);
		String invnamec = sysCorpinv.getInvnamec();
		if(!StrUtils.isNull(invnamec)){
			Browser.execClientScript("parent.clinet.setValue('"+invnamec+"')");
		}
	}

	@Action
	public void selectthisone(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0||ids.length>1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		Browser.execClientScript("parent.window","selectedRowDataupdate0('"+ids[0]+"');");
	}

	@Bind
	@SaveState
	public String invoiceInfText;
	
	@Action
	public void setImport(){
		try{
			String updatesql = "DELETE FROM sys_corpinv WHERE linkid = " + linkid;
			serviceContext.sysCorpinvMgrService.sysCorpinvDao.executeSQL(updatesql);
			String sql = "SELECT * FROM array_to_json(f_sys_corpinv_imp('invoiceInfText="+invoiceInfText+"',"+linkid+")) AS notfindcust";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null){
				MessageUtils.alert("OK");
			}
			this.grid.reload();
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void refreshAjaxSubmit() {
		this.refresh();
	}
	
	@SaveState
	public boolean isShowAll = false;

	@Override
	public void clearQryKey() {
		isShowAll = true;
		super.clearQryKey();
	}
	
	@Action
	public void affirminv() {
		Object isaffirm = AppUtils.getReqParam("isaffirm");
		boolean isaff = Boolean.valueOf(String.valueOf(isaffirm));
		String sql = "UPDATE sys_corpinv SET isaffirm = "+isaff+" ,affirmtime = NOW(),affirmuserid = '"
			+ AppUtils.getUserSession().getUserid() + "' WHERE id =" + selectedRowData.getId() + ";";
		try {
			serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(sql);
			MessageUtils.alert(isaff ? "Confirm OK!" : " Cancel OK!");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
