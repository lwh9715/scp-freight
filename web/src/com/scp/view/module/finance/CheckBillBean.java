package com.scp.view.module.finance;

import java.util.Map;

import com.scp.util.StrUtils;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.model.finance.FinaStatement;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.checkbillBean", scope = ManagedBeanScope.REQUEST)
public class CheckBillBean extends GridView {
	
	@SaveState
	@Accessible
	public FinaStatement selectedRowData = new FinaStatement();

	@Bind
	@SaveState
	public String searchAr = "ap";

	@Bind
	@SaveState
	public boolean lazyfilter = false;

	@Action(id="searchAr",event="onchange")
	public void filterIframeClientele(){
		if (lazyfilter) {
			this.customerQry();
		}
	}
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			super.applyGridUserDef();
//			this.qryMap.put("t.reqtype", "S");
		}
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String id = grid.getSelectedIds()[0];
		AppUtils.openNewPage("./checkbilledit.xhtml?type=edit&id="+id+"&customerid=-1");
		
	}

	
	@Bind
	public UIDataGrid clientGrid;

	
	@Action
	public void clientGrid_ondblclick() {
		String id = clientGrid.getSelectedIds()[0];
		AppUtils.openNewPage("./checkbilledit.xhtml?type=new&id=-1&customerid="+id+"");
	}
	
	@Action
	public void clientGrid_onrowselect() {
		String id = clientGrid.getSelectedIds()[0];
		this.qryMap.put("customerid$", id);
		this.grid.reload();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void qryRefresh() {
		this.qryMap.remove("customerid$");
		super.qryRefresh();
		this.grid.reload();
		this.clientGrid.setSelectedRow(-1);
		this.clientGrid.reload();
	}
	
	@Bind
	private String qryCustomerKey;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.qry(qryCustomerKey , true);
		this.clientGrid.reload();
	}
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {

		//默认应付
		boolean isRet = true;
		if (this.searchAr.equals("ar")) {
			isRet = false;
		}

		lazyfilter = true;

		return this.customerChooseBean.getStatementClientDataProvider(isRet);
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

			String usercode = AppUtils.getUserSession().getUsercode();
			String sql = "SELECT inputer FROM fina_rpreq WHERE id = " + id + "  AND reqtype = 'S' AND isdelete = FALSE LIMIT 1";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (StrUtils.getMapVal(map, "inputer").equals(usercode) || usercode.equals("SUJUN") || usercode.equals("LIANGWENHUI")) {
				this.serviceContext.reqMgrService.removeDate(Long.valueOf(id));
				this.alert("OK!");
				this.refresh();
			}else {
				this.alert("请联系录入人 "+StrUtils.getMapVal(map, "inputer")+" 或 IT管理员进行删除");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer sbcorp = new StringBuffer();
//			sbcorp.append("\n AND t.corpid ="+AppUtils.getUserSession().getCorpidCurrent());
//			m.put("corpfilter", sbcorp.toString());
//		}
		
		String corpfilter = "\n AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE t.corpid = x.corpid AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		m.put("corpfilter", corpfilter);
		return m;
	}
	
}
