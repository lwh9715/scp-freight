package com.scp.view.module.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;

import com.scp.model.finance.FinaArap;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.module.ship.businvoicedtlBean", scope = ManagedBeanScope.REQUEST)
public class BusInvoiceDtlBean extends GridSelectView {

	@Bind
	@SaveState
	private String invoiceid;

	@Bind
	@SaveState
	private String customerid;
	
	@SaveState
	@Accessible
	private String change;

	@Bind
	@SaveState
	private String jobid;

	@SaveState
	@Accessible
	public FinaArap selectedRowData = new FinaArap();
	
	@Bind
	@SaveState
	private String startDateF;
	
	@Bind
	@SaveState
	private String endDateF;
	
	@Bind
	@SaveState
	private String startDateJ;
	
	@Bind
	@SaveState
	private String endDateJ;
	
	
	@Bind
	@SaveState
	private String startDateA;
	
	@Bind
	@SaveState
	private String endDateA;
	
	@SaveState
	@Accessible
	private String whereSql = "";
	
	
	@Bind
	public UIWindow searchWindow;
	
	@Bind
	public UIWindow exchangeRateWindow;
	
	@Bind
	private UIEditDataGrid rateGrid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			invoiceid = (String) AppUtils.getReqParam("id");
			customerid = (String) AppUtils.getReqParam("customerid");
			jobid = (String) AppUtils.getReqParam("jobid");
			change = (String) AppUtils.getReqParam("change");
			qryMap.put("araptype","AR");
			this.update.markUpdate(UpdateLevel.Data, "invoiceid");
			this.update.markUpdate(UpdateLevel.Data, "customerid");
			this.update.markUpdate(UpdateLevel.Data, "jobid");
			
		}
		
	}
	
	@Action
	public void showRate(){
		if(Long.parseLong(invoiceid)>0){
			this.rateGrid.reload();
			this.exchangeRateWindow.show();
		}else{
			MessageUtils.alert("请先保存发票!");
		}
	}
	@Action
	public void exchangeRate(){
		if(this.rateGrid.getModifiedData()!=null){
			JSONArray obj =   (JSONArray) this.rateGrid.getModifiedData();
			if(jobid!=null&&!jobid.isEmpty()&&invoiceid!=null&&!invoiceid.isEmpty()){
				this.serviceContext.arapMgrService.updateRateForInvoice(obj, jobid, invoiceid);
			}
		}
		this.exchangeRateWindow.close();
		this.refresh();
	}
	@Bind(id="rateGrid")
	public List<Map> getRateGrids() throws Exception{
		List<Map> list = null;
		if(invoiceid!=null&&Long.parseLong(invoiceid)>0){
			Map map = new HashMap();
			String clauseWhere = "\n jobid = "+jobid
					+ "\n AND invoiceid="+invoiceid;
			map.put("qry", clauseWhere);
			
			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.ship.businvoicedtlBean.grid.rate", map);
		}
		return list;
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		// 费用日期和工作单日期区间查询
		String qry = StrUtils.getMapVal(map, "qry");
		qry += "\nAND a.arapdate BETWEEN '" + (StrUtils.isNull(startDateF) ? "0001-01-01" : startDateF) 
				+ "' AND '" + (StrUtils.isNull(endDateF) ? "9999-12-31" : endDateF) + "'";
		qry += "\nAND a.jobdate BETWEEN '" + (StrUtils.isNull(startDateJ) ? "0001-01-01" : startDateJ)
				+ "' AND '" + (StrUtils.isNull(endDateJ) ? "9999-12-31" : endDateJ) + "'";
		
		qry += "\nAND ((a.atd BETWEEN '" + (StrUtils.isNull(startDateA) ? "0001-01-01" : startDateA)
				+ "' AND '" + (StrUtils.isNull(endDateA) ? "9999-12-31" : endDateA) + "')"
				+ "\n OR a.atd IS NULL)";
		map.put("qry", qry);
		if ("-1".equals(invoiceid)) {
			if("-1".equals(this.jobid)) {
				map.put("clause", " a.invoiceid is  null AND a.customerid = " + this.customerid);
			} else {
				map.put("clause", " a.invoiceid is  null AND a.jobid  =" + this.jobid);
			}
		} else {
			if("-1".equals(this.jobid)) {
				this.whereSql = "( a.invoiceid = "
								+ invoiceid
								+ "  OR (a.invoiceid is  null AND a.customerid = (SELECT x.clientid FROM fina_invoice x WHERE x.id = "
								+ invoiceid + ")))";
				map.put("clause",whereSql);
								
			} else {
				this.whereSql = " a.jobid  ="
								+ this.jobid
								+ " AND ( a.invoiceid = "
								+ invoiceid
								+ "  OR (a.invoiceid is  null AND a.customerid = (SELECT x.clientid FROM fina_invoice x WHERE x.id = "
								+ invoiceid + ")))";
				map.put("clause",whereSql);
						
			}
			
		}
		map.put("filter", "EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid="
				+ this.jobid + ";userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()
				+ "') x WHERE x.id = a.id)");
		return map;
	}

	@Override
	public void grid_ondblclick() {
		Long id = this.getGridSelectId();
		this.selectedRowData = this.serviceContext.arapMgrService.finaArapDao
				.findById(id);
		showEditBillDtlWin.show();
	}

	@Bind
	public UIWindow showEditBillDtlWin;

	@Override
	public int[] getGridSelIds() {

		String sql;
		if ("-1".equals(invoiceid)) {
			return null;
		} else {
			if("-1".equals(this.jobid)) {
				sql = "\nselect "
					+ "\n(CASE WHEN a.invoiceid = "
					+ invoiceid
					+ " THEN TRUE ELSE FALSE END) AS flag"
					+ "\nFROM _fina_arap a "
					+ "\nWHERE ( a.invoiceid = "
					+ invoiceid
					+ "  OR (a.customerid = (SELECT x.clientid FROM fina_invoice x WHERE x.id = "
					+ invoiceid + ") AND a.invoiceid IS NULL))"
					+ "\nORDER BY jobno,araptype";
			} else {
				sql = "\nselect "
					+ "\n(CASE WHEN a.invoiceid = "
					+ invoiceid
					+ " THEN TRUE ELSE FALSE END) AS flag"
					+ "\nFROM _fina_arap a "
					+ "\nWHERE a.jobid = "
					+ this.jobid
					+ " AND ( a.invoiceid = "
					+ invoiceid
					+ "  OR (a.customerid = (SELECT x.clientid FROM fina_invoice x WHERE x.id = "
					+ invoiceid + ") AND a.invoiceid IS NULL))"
					+ "\nORDER BY jobno,araptype";
			}
		}
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if ((Boolean) map.get("flag"))
					rowList.add(rownum);
				rownum++;
			}
			int row[] = new int[rowList.size()];
			for (int i = 0; i < rowList.size(); i++) {
				row[i] = rowList.get(i);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void save() {

		if (invoiceid.equals("-1")) {
			MessageUtils.alert("请先保存发票！");
			return;
		}

		String[] ids = this.grid.getSelectedIds();
		whereSql = whereSql.replaceAll("a\\.", "");
		String dmlSql = "\nUPDATE fina_arap SET invoiceid = NULL,invoicexrate = NULL,invoicextype = NULL , invoiceamount = NULL WHERE "
				 + whereSql + ";";
		for (int i = 0; i < ids.length; i++) {
			dmlSql += "\nUPDATE fina_arap SET invoiceid = " + invoiceid
					+ " WHERE id = " + ids[i] + ";";
		}
		if (!StrUtils.isNull(dmlSql)) {
			try {
				this.serviceContext.userMgrService.sysUserDao
						.executeSQL(dmlSql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.grid.repaint();
		
		Browser.execClientScript("parent.refreshForm.submit()");
	}
	
	@Action
	public void dosave(){
		
		this.serviceContext.arapMgrService.saveData(selectedRowData);
		alert("OK");
		refresh();
		
	}

	@Override
	public void clearQryKey() {
		this.startDateF = "";
		this.endDateF = "";
		this.startDateJ = "";
		this.endDateJ = "";
		this.startDateA = "";
		this.endDateA = "";
		this.update.markUpdate(UpdateLevel.Data, "startDateF");
		this.update.markUpdate(UpdateLevel.Data, "endDateF");
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		super.clearQryKey();
	}
	
	@Action
	public void search(){
		this.searchWindow.show();
	}
	
	@Action
	public void clear(){
		this.clearQryKey();
	}
	
	@Action
	public void searchfee(){
		this.qryRefresh();
	}
	
}
