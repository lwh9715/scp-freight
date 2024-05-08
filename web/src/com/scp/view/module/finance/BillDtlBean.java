package com.scp.view.module.finance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaArap;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.module.finance.billdtlBean", scope = ManagedBeanScope.REQUEST)
public class BillDtlBean extends GridSelectView {
	
	
	@Bind
	@SaveState
	private String billid;
	
	@Bind
	@SaveState
	private String customerid;
	
	
	@SaveState
	@Accessible
	public FinaArap selectedRowData = new FinaArap();
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			billid = (String) AppUtils.getReqParam("id");
			customerid = (String) AppUtils.getReqParam("customerid");
			this.update.markUpdate(UpdateLevel.Data,"billid");
			this.update.markUpdate(UpdateLevel.Data,"customerid");
		}
	}


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map =  super.getQryClauseWhere(queryMap);
		if("-1".equals(billid)){
			map.put("clause", "(a.customerid = "+customerid+" OR a.customerid = (SELECT x.clientid FROM fina_bill x WHERE x.id = "+billid +"))");
		}else{
			Map result = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COUNT(*) FROM fina_arap WHERE isdelete = FALSE AND billid = "+ billid);
			if(null == result || result.size() == 0 || "0".equals(String.valueOf(result.get("count")))){
				map.put("clause","a.customerid = (SELECT x.clientid FROM fina_bill x WHERE x.id = "+billid +") AND a.billid IS NULL");
			}else{
				map.put("clause"," a.billid = "+billid +" ");
			}
		}
		
		return map;
	}

	@Override
	public void grid_ondblclick() {
		Long id = this.getGridSelectId();
		this.selectedRowData = this.serviceContext.arapMgrService.finaArapDao.findById(id);
		showEditBillDtlWin.show();
	}

	
	@Bind
	public UIWindow showEditBillDtlWin;
	
	
	@Override
	public int[] getGridSelIds() {
		
		String sql;
		if("-1".equals(billid)){
			return null;
		}else{
			sql = 
				"\nselect " +
				"\n(CASE WHEN a.billid = "+billid +" THEN TRUE ELSE FALSE END) AS flag"+
				"\nFROM _fina_arap a " +
				"\nWHERE (a.billid = "+billid + ")"+
				"\nORDER BY arapdate";
		}
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if((Boolean)map.get("flag"))rowList.add(rownum);
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
		if(billid.equals("-1")){
			MessageUtils.alert("请先保存账单！");
			return;
		}
		
		String[] ids = this.grid.getSelectedIds();
		String dmlSql = "\nUPDATE fina_arap SET billid = NULL WHERE billid = "+billid+" AND EXISTS (SELECT 1 FROM fina_bill WHERE id = fina_arap.billid AND isdelete = FALSE AND bcledgercompid IS NULL);";
		if(ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				dmlSql += "\nUPDATE fina_arap SET billid = " + billid + " WHERE id = " + ids[i]+";";
			}
		}
		
		if(!StrUtils.isNull(dmlSql)){
			try {
				this.serviceContext.userMgrService.sysUserDao.executeSQL(dmlSql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.grid.repaint();
	}
	
	@Action
	public void dosave(){
		
		this.serviceContext.arapMgrService.saveData(selectedRowData);
		alert("OK");
		refresh();
		
	}
}
