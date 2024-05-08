package com.scp.view.module.finance;

import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.base.ApplicationConf;
import com.scp.base.ConstantBean.Module;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.payapplycheckarBean", scope = ManagedBeanScope.REQUEST)
public class PayApplyCheckarBean extends GridView {
	
	
	@Bind
	public UIButton checkBatch;
	
	@Bind
	public UIButton cancelCheckBatch;
	
	@Bind
	public UIButton generateRP;
	
	@Bind
	public UIButton delGenerate;
	
	@Bind
	public UIButton setIspay;
	
	@Bind
	public UIButton setIsnotpay;
	
	@Bind
    @SaveState
    @Accessible
	public String processInstanceId;
	
	@Bind
    @SaveState
    @Accessible
	public String workItemIdStr;
	
	@Bind
	@SaveState
	public boolean ispublic = false;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
			workItemIdStr = (String) AppUtils.getReqParam("workItemId");
			if(!StrUtils.isNull(workItemIdStr)){
				String nos = (String) AppUtils.getReqParam("sn");
				this.qryMap.put("nos",nos);
//				ProcessInstance processInstance = (ProcessInstance)AppUtils.getWorkflowSession().findProcessInstanceById(processInstanceId);
//				Map<String, Object> m = processInstance.getProcessInstanceVariables();
//				ispublic = (m.get("ispublic")!=null&&((String) m.get("ispublic")).equals("0")?false:true);
			}
		}
		initCtrl();
	}

	private void initCtrl() {
		checkBatch.setDisabled(true);
		cancelCheckBatch.setDisabled(true);
		generateRP.setDisabled(true);
		delGenerate.setDisabled(true);
		setIspay.setDisabled(true);
		setIsnotpay.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_rpreq_payapplycheck.getValue())) {
			if (s.endsWith("_checkBatch")) {
				checkBatch.setDisabled(false);
			} else if (s.endsWith("_cancelCheckBatch")) {
				cancelCheckBatch.setDisabled(false);
			} else if (s.endsWith("_generateRP")) {
				generateRP.setDisabled(false);
			} else if (s.endsWith("_delGenerate")) {
				delGenerate.setDisabled(false);
			} else if (s.endsWith("_setIspay")) {
				setIspay.setDisabled(false);
			} else if (s.endsWith("_setIsnotpay")) {
				setIsnotpay.setDisabled(false);
			}
		}
	}

	@Action
	public void add() {
		String winId = "_pay_apply_dtlcheck";
		String url = "./payapplydtlcheck.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String[] ids = this.grid.getSelectedIds();
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[i];;
		}
		String winId = "_pay_apply_dtlcheck";
		String url = "./payapplydtlcheck.xhtml?araptype=AR&id="+Long.valueOf(ids[0]);
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void checkBatch() {
		String[] ids = this.grid.getSelectedIds();
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[0];;
		}
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.reqMgrService.updateCheck(ids , AppUtils.getUserSession().getUsername());
			MessageUtils.alert("审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelCheckBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[i];;
		}
		try {
			serviceContext.reqMgrService.updateCancelCheck(ids);
			MessageUtils.alert("取消审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void generateRP() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要生成的行");
			return;
		}
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[0];
		}
		try {
			serviceContext.reqMgrService.createRPmany(ids , AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("生成成功");
			//procInst("generateRP");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Bind
	@SaveState
	private String amtreq= "";
	
	@Bind
	@SaveState
	private String checkternamec="";
	
	@Action
	public void delGenerate() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要清除的行");
			return;
		}
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[0];;
		}
		try {
			serviceContext.reqMgrService.delGenerates(ids);
			MessageUtils.alert("清除成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = (String)map.get("qry");
		if(!amtreq.equals("")){
			qry+="\n AND '%"+amtreq+"%' LIKE amtreq::TEXT";
		}
		if(!checkternamec.equals("")){
			qry+="\n AND (EXISTS(SELECT 1 FROM sys_user x,fina_rpreq y where y.id = t.id AND x.code = y.checkter and x.isdelete = false AND x.namec LIKE '%"+
			checkternamec+"%') OR t.checkter LIKE '%"+checkternamec+"%')";
		}
		
		String qry1 = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x,sys_corporation y WHERE x.corpid = y.corpid AND y.id = t.customerid AND y.isdelete = false AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())qry += qry1;//非saas模式不控制
		if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))qry += qry1;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
		map.put("qry", qry);
		return map;
	}


	@Override
	public void clearQryKey() {
		super.clearQryKey();
		this.amtreq="";
		this.checkternamec="";
	}
	
	@Action
	public void setIspay() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要标记的行");
			return;
		}
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[0];;
		}
		try {
			serviceContext.reqMgrService.setIspay(ids,true);
			MessageUtils.alert("标记成功");
			//procInst("setIspay");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void setIsnotpay() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要标记的行");
			return;
		}
		for(int i =0;i<ids.length;i++){
			ids[i]=ids[i].split("\\.")[0];
		}
		try {
			serviceContext.reqMgrService.setIspay(ids,false);
			MessageUtils.alert("标记成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0||ids.length>1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		
		try {
			String idalies = ids[0];
			String id = idalies.split("\\.")[0];
			this.serviceContext.reqMgrService.removeDate(Long.valueOf(id));
			this.alert("OK!");
			this.refresh();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Resource(name="transactionTemplate")
	private TransactionTemplate transactionTemplate;
	
	@Bind
	@SaveState
	private String processId = "PaymentRequest";
	
	@SaveState
	@Accessible
	public String currwork;
	
}
