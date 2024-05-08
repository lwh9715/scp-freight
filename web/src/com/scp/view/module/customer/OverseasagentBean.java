package com.scp.view.module.customer;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.sys.SysCorporationAgent;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.customer.overseasagentBean", scope = ManagedBeanScope.REQUEST)
public class OverseasagentBean extends GridFormView{

	@Bind
	public UIButton exportBtn;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			initCtrl();


			String sql = "select f_checkright('overseasagentexportbtn',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 1 as " +
					"overseasagentexportbtn";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if (m != null && m.get("overseasagentexportbtn") != null&& m.get("overseasagentexportbtn").toString().equals("false")) {
					exportBtn.setDisabled(true);
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.sysCorporationAgentnService.sysCorporationAgentDao.findById(this.getGridSelectId());
		
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.sysCorporationAgentnService.saveData(selectedRowData);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@SaveState
	@Accessible
	public SysCorporationAgent selectedRowData = new SysCorporationAgent();

	@Override
	public void grid_ondblclick() {
		
	}
	
	@Override
	public void del() {
		super.del();
		String[] ids = this.grid.getSelectedIds();

		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.sysCorporationAgentnService.removeDate(ids,
					AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void add() {
		super.add();
		selectedRowData = new SysCorporationAgent();
		Browser.execClientScript("$('#sales_input').val('')");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Bind
	@SaveState
	public String sale;

	@Bind
	@SaveState
	public String country;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", AppUtils.getUserSession().getUserid());
		String qry = map.get("qry").toString();
		
		//业务员
		if(!StrUtils.isNull(sale)){
			qry += "\n AND EXISTS (SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND x.id = a.salesid AND x.namec ILIKE '%"+StrUtils.getSqlFormat(sale)+"%')";
		}

		if(!StrUtils.isNull(country)){
			qry += "\n AND country = '"+StrUtils.getSqlFormat(country)+"'";
		}

		String existsSql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole a,sys_role b WHERE a.roleid =  b.id AND a.userid = " + AppUtils.getUserSession().getUserid()+" AND b.code = 'dev') AS existflag";
		Map existsMap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(existsSql);
		if(existsMap != null && "true".equals(StrUtils.getMapVal(existsMap, "existflag"))){//开发测试组可以看到所有的
			
		}else{	//上级可以看到下级的数据
				/*qry += "\n AND	a.salesid = ANY(ARRAY(WITH RECURSIVE rc AS ("
					+"\n		SELECT * FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()
					+"\n		UNION "
					+"\n		SELECT a.* FROM sys_user a,rc WHERE  a.parentid = rc.id"
					+"\n	)"
					+"\n	SELECT id FROM rc ))";*/
				
				
					//（录入人，修改人，业务员，客户组）
				 qry += "\n AND ( a.salesid = "+AppUtils.getUserSession().getUserid()
						+ "\n	OR (a.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_user y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND x.userid = a.salesid " //关联的业务员的单，都能看到
						+ ")"
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_role y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND x.userid = a.salesid " //组关联业务员的单，都能看到
						+ "))";
					
		}
		map.put("qry", qry);
		return map;
	}
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_syscorporationagent";
				String args = "'" + AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Bind
	public UIWindow searchWindow;
	
	@Action
	public void search() {
		this.searchWindow.show();
	}
	
	
	@Bind
	@SaveState
	public String qryagentname;
	
	@Bind
	@SaveState
	public String qrycountry;
	
	
	@Action
	public void qryinfo(){
		try {
			if(!StrUtils.isNull(qryagentname) && !StrUtils.isNull(qrycountry)){
				String sql2 = "INSERT INTO sys_log(id,inputer,logtime,logdesc,logtype)VALUES(getid(),'"+AppUtils.getUserSession().getUsercode()+"',NOW(),'代理名称:"+qryagentname+"  国家:"+qrycountry+"','O')";
				this.serviceContext.userMgrService.sysUserDao.executeSQL(sql2);
				
				String sql = "select count(*) AS count from sys_corporation_agent a where isdelete = false " +
								"AND  upper(agentname) ilike upper('"+StrUtils.getSqlFormat(qryagentname)+"%') AND country = '"+StrUtils.getSqlFormat(qrycountry)+"'";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				Long i = (Long) s.get("count");
				if(i > 0){
					MessageUtils.alert("该客人已经存在!");
					return;
				}else{
					MessageUtils.alert("该客人不存在， 可开发!");
					return;
				}
			}else{
				super.refresh();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Bind
	public UIButton add;
	
	@Bind
	public UIButton save;
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton edit;
	
	@Bind
	public UIButton importData;
	
	@Bind
	public UIButton salesedit;
	
	//权限控制
	private void initCtrl() {
		add.setDisabled(true);
		save.setDisabled(true);
		del.setDisabled(true);
		importData.setDisabled(true);
		edit.setDisabled(true);
		salesedit.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(100900700L)) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
				save.setDisabled(false);
			} else if (s.endsWith("_update")) {
				save.setDisabled(false);
				edit.setDisabled(false);
				salesedit.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				del.setDisabled(false);
			}else if (s.endsWith("_import")) {
				importData.setDisabled(false);
			}
			
		}
	}
	
	
	@Action
	public void searchrecords() {
		showLogsWindow.show();
	}
	
	@Bind
	public UIWindow showLogsWindow;
	
	@Bind
	public UIIFrame traceIframe;
	
	@Action
	public void showTrace() {
		traceIframe.load("/scp/pages/sysmgr/log/logmgr.xhtml?logtype=O");
	}
	
	@Bind
	@SaveState
	@Accessible
	public SysCorporationAgent repetitionTips;
	
	@Bind
	public UIWindow tipsWindow;
	
	@Action
	private void checkRepeatAjaxSubmit() {
		this.pkVal = getGridSelectId();
		String agentname = AppUtils.getReqParam("agentname");
		String country = AppUtils.getReqParam("country");
		if(StrUtils.isNull(agentname)){this.alert("不能为空!");return;}
		try {
			repetitionTips = serviceContext.sysCorporationAgentnService.repeat(this.pkVal , agentname , country);
			if(repetitionTips != null){
				tipsWindow.show();
				update.markUpdate(true, UpdateLevel.Data, "tipsPanel");
				Browser.execClientScript("agentnamejsvar.setValue('');");
			}
		} catch (CommonRuntimeException e) {
			String exception  = e.getLocalizedMessage();
			this.alert(exception);
		}
	}
	
	
	@Bind
	public UIIFrame attachIframe;
	
	@Bind
	public UIWindow showAttachWindow;
	
	@Action
	public void showAttachment(){
		attachIframe.load(AppUtils.getContextPath()
				+ "/pages/module/common/attachment.xhtml?linkid="
				+ this.pkVal);
		showAttachWindow.show();
	}
	
	
	@Bind(id="qryStatus")
    public List<SelectItem> getqryStatus() {
    	try {
    		return CommonComBoxBean.getComboxItems("status","status","sys_corporation_agent AS d"
    				,"WHERE isdelete = false and status <> '' and status is not null group by status","ORDER BY status");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryCountry")
    public List<SelectItem> getqryCountry() {
    	try {
    		return CommonComBoxBean.getComboxItems("country","country","sys_corporation_agent AS d"
    				,"WHERE isdelete = false and country <> '' and country is not null group by country","ORDER BY country");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Action
	public void edit() {
		this.pkVal = getGridSelectId();
		doServiceFindData();

		if (this.selectedRowData.getSalesid() != null) {
			try {
				SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getSalesid());
				Browser.execClientScript("$('#sales_input').val('" + sysUser.getNamec() + "')");
			} catch (Exception e) {
			}
		} else {
			Browser.execClientScript("$('#sales_input').val('')");
		}
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");

	}
	
	
	@Bind
	@SaveState
	public String againsaleid;
	
	@Bind
	public UIWindow showbatchupdateWindow;
	
	@Action
	public void salesedit() {
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0) { 
			MessageUtils.alert("请选择一条记录"); 
		}else{
			showbatchupdateWindow.show();
		}
	}
	
	
	@Action
	public void savesales() { 
		String[] ids = this.grid.getSelectedIds(); 
		String user = AppUtils.getUserSession().getUsercode();
		try {
			String sql = "UPDATE sys_corporation_agent SET salesid = "+againsaleid+", updater = '" + user + "',updatetime = NOW() WHERE id IN("+StrUtils.array2List(ids)+");";
			this.serviceContext.sysCorporationAgentnService.sysCorporationAgentDao.executeSQL(sql);
			super.refresh();
			MessageUtils.alert("OK"); 
			showbatchupdateWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
	
}
