package com.scp.view.module.finance;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.finance.FinaActpayrecdtl;
import com.scp.model.finance.FinaArap;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.unlinkfeechooseractpayBean", scope = ManagedBeanScope.REQUEST)
public class UnLinkFeeChooserActPayBean extends GridView {
	

	@SaveState
	@Bind
	public String customerid;

	@Bind
	@SaveState
	public Long pkVal;


	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			pkVal = Long.valueOf((String) AppUtils.getReqParam("id"));
			customerid = (String) AppUtils.getReqParam("customerid");
		}
	}
	
	
	@Bind
	public UIEditDataGrid arapGrid;
	
	
	@Action
	public void del(){
		this.serviceContext.reqMgrService.removeDate(this.pkVal);
		this.alert("OK!");
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		
		Map map = super.getQryClauseWhere(queryMap);
		
		map.put("customerid", customerid);
		map.put("actpayrecid", this.pkVal);
		
		//20150115 过滤当前单下面已经有的
		String qry = (String) map.get("qry"); 
		if(pkVal != null && pkVal != -1l){
			qry += "\nAND NOT EXISTS(SELECT 1 FROM fina_actpayrecdtl x WHERE x.arapid = a.id AND x.isdelete = FALSE AND x.actpayrecid = "+pkVal+")";
		}else {
			//新增时提前的本身是所有的，不用提取
			//map.put("clause", "(rpreqid != "+pkVal+") ");
			qry += "\nAND FALSE";
		}
		
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			String corpfilter = "\na.corpid="+AppUtils.getUserSession().getCorpidCurrent();
//			map.put("corpfilter", corpfilter);
//		}
		
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE a.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		map.put("corpfilter", corpfilter);
		map.put("qry", qry);
		return map;
	}
	
	
	
	@Action
	public void grid_ondblclick() {
//		Long id = this.getGridSelectId();
//		this.selectedRowDataArap = this.serviceContext.arapMgrService.finaArapDao.findById(id);
//		showEditBillDtlWin.show();
	}


	
		
	@Action
	public void save() {
		try {
			String ids[] = this.grid.getSelectedIds();
			for (String id : ids) {
				FinaActpayrecdtl finaActpayrecdtl = new FinaActpayrecdtl();
				finaActpayrecdtl.setActpayrecid(pkVal);
				finaActpayrecdtl.setArapid(Long.valueOf(id));
				FinaArap finaArap = this.serviceContext.arapMgrService.finaArapDao.findById(Long.valueOf(id));
				finaActpayrecdtl.setCurrencyfm(finaArap.getCurrency());
				finaActpayrecdtl.setCurrencyto(finaArap.getCurrency());
				this.serviceContext.actPayRecService
						.savedData(finaActpayrecdtl);
			}
			MessageUtils.alert("OK");
			this.qryRefresh();
			//Browser.execClientScript("parent.chooseFeeWindows.hide()");
			Browser.execClientScript("parent.refreshArapAction.submit()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
	}

}
