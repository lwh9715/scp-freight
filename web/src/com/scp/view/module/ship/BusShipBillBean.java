package com.scp.view.module.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ConstantBean.Module;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.ship.BusShipBill;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.busshipbillBean", scope = ManagedBeanScope.REQUEST)
public class BusShipBillBean extends GridFormView {

	@SaveState
	@Accessible
	public BusShipBill selectedRowData = new BusShipBill();

	@SaveState
	@Accessible
	public Long jobid;
	
	@SaveState
	public String workItemId;
	
	@Bind
	@SaveState
	public String jsworkItemId;
	
	@Bind
	public UIButton addhbl;
	
	@Bind
	public UIButton addmbl;
	
	@Bind
	public UIButton copyadd;
	
	@Bind
	public UIButton copyaddmbl;
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton updatembl;
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Bind
	@SaveState
	public String bpmremark = "";
	
	@Bind
	@SaveState
	public String taskname;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			initCtrl();
			String id = AppUtils.getReqParam("id").trim();
			jobid = Long.valueOf(id);
			workItemId = (String) AppUtils.getReqParam("workItemId");
			jsworkItemId = workItemId;
			qryMap.put("jobid$", this.jobid);
			// this.grid.repaint();
		}
	}

	private void initCtrl() {
		addhbl.setDisabled(true);
		addmbl.setDisabled(true);
		copyadd.setDisabled(true);
		copyaddmbl.setDisabled(true);
		del.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_ship_bill.getValue())) {
			if (s.endsWith("_add")) {
				addhbl.setDisabled(false);
				addmbl.setDisabled(false);
				copyadd.setDisabled(false);
				copyaddmbl.setDisabled(false);
			}else if (s.endsWith("_delete")) {
				del.setDisabled(false);
			}
		}
	}

	@Override
	public void del() {
		try {
			serviceContext.busShipBillMgrService.removeDate(this
					.getGridSelectId());
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_busshipbilledit";
		String url = "./busshipbilledit.xhtml?id=" + this.getGridSelectId()
				+ "&jobid=" + jobid+"&workItemId="+this.workItemId;
		int width = 1024;
		int height = 768;
		//AppUtils.openWindow(winId, url);
		AppUtils.openNewPage(url);
	}

	@Action
	public void addhbl() {

		super.grid_ondblclick();
		String winId = "busshipbilledit";
		String url = "./busshipbilledit.xhtml?jobid=" + jobid+"&bltype=H"+"&workItemId="+this.workItemId;
		int width = 1024;
		int height = 768;
		//AppUtils.openWindow(winId, url);
		AppUtils.openNewPage(url);
	}

	@Action
	public void addmbl() {

		super.grid_ondblclick();
		String winId = "busshipbilledit";
		String url = "./busshipbilledit.xhtml?jobid=" + jobid+"&bltype=M";
		int width = 1024;
		int height = 768;
		//AppUtils.openWindow(winId, url);
		AppUtils.openNewPage(url);
	}
	
	@Action
	public void updatembl() {
		String[] ids = this.grid.getSelectedIds();		
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			serviceContext.busShipBillMgrService.updateDate(ids,jobid);
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void refresh() {
		this.grid.reload();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Action
	public void copyadd() {
		String url = AppUtils.getContextPath()
				+ "/pages/module/ship/shipbillcopychoose.xhtml?jobid="
				+ this.jobid+"&type=H";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	@Action
	public void copyaddmbl() {
		String url = AppUtils.getContextPath()
				+ "/pages/module/ship/shipbillcopychoosembl.xhtml?jobid="
				+ this.jobid+"&type=M";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;
	
	
	@Bind
	private UIIFrame billtraceIframe;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	@Override
	public void grid_onrowselect() {
		super.grid_onrowselect();
		
		String id = this.grid.getSelectedIds()[0];
		BusShipBill busShipBill  = this.serviceContext.busShipBillMgrService.busShipBillDao.findById(Long.valueOf(id));
		String url = "";
		if(busShipBill.getBltype().equals("M")){
			url = "./busshipbilltracembl.xhtml?billid="+this.getGridSelectId();
		}else{
			url = "./busshipbilltrace.xhtml?billid="+this.getGridSelectId();
		}
		billtraceIframe.setSrc(url);
		update.markAttributeUpdate(billtraceIframe, "src");
	}

	@Action
	public void addsconfirm(){
		String hblnumber = AppUtils.getReqParam("hblnumber");
		String mblnumber = AppUtils.getReqParam("mblnumber");
		String addsql = "SELECT f_bus_ship_bill_autosplit('srctype=;hblnumber="+(StrUtils.isNull(hblnumber)?"0":hblnumber)+";mblnumber="
		+(StrUtils.isNull(mblnumber)?"0":mblnumber)+";billnumber=0;usercode="+AppUtils.getUserSession().getUsercode()+";jobid="+jobid+"')";
		try{
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(addsql);
			alert("OK");
			this.grid.reload();
			Browser.execClientScript("addswindow.close()");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Action
	public void applyBPMform() {
		try {
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-D6C2A85F";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '"+bpmProcess.getId()+"' AND refid = '"+this.selectedRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str =  s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
				}
				Browser.execClientScript("bpmWindowJsVar.show();");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIDataGrid gridUser;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@SuppressWarnings("deprecation")
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@SuppressWarnings("deprecation")
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser.contains(id)){
				this.nextAssignUser = nextAssignUser + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		//nextAssignUser = nextAssignUser.substring(0, nextAssignUser.length()-1);
		//user = user.substring(0, user.length()-1);
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Action
	public void applyBPM() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (int i = 0; i < ids.length; i++) {
			if(StrUtils.isNull(ids[i])){
				return;
			}
			String id = ids[i];
			BusShipBill busshipbill = null;
			BusShipping busShipping = null;
			try {
				busshipbill = serviceContext.busShipBillMgrService.busShipBillDao.findById(Long.valueOf(id));
				busShipping = serviceContext.busShippingMgrService.findByjobId(jobid);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String mblno = busshipbill.getMblno();			//BILL表 MBL
	 		String processinsVar = "issign"+"-"+busshipbill.getIssign()+"-"+"取单";
	 		if(busshipbill.getIssign()){
				if(taskname.equals("电放负责人")){
					taskname = "正本提单管理";
				}
			}else{
				if(taskname.equals("正本提单管理") || taskname.equals("正本电放负责人")){
					taskname = "电放负责人";
				}
			}
			try {
				if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
					if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
					String processCode = "BPM-D6C2A85F";
					BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
					Map map = serviceContext.jobsMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT nos FROM fina_jobs WHERE isdelete = FALSE AND id = " + jobid);
					String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+StrUtils.getSqlFormat(bpmremark)+";taskname="+taskname+";refno="+mblno+";refid="+id+";processinsVar="+processinsVar+"') AS rets;";
					Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
					String sub =  sm.get("rets").toString();
					MessageUtils.alert("OK");
					Browser.execClientScript("bpmWindowJsVar.hide();");
				}
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}

	}
	
	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			String processCode = "BPM-D6C2A85F";
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End','提交') order by step";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
            	SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
				lists.add(selectItem);
			}
		}catch(NoRowException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
}
