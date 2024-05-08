package com.scp.view.module.price;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.price.PriceFclBargefee;
import com.scp.model.price.PriceFclBargefeeDtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.bargefeeEditBean", scope = ManagedBeanScope.REQUEST)
public class bargefeeEditBean extends EditGridFormView {
	
	@SaveState
	public String priceid;
	
	@SaveState
	public String line;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
//			this.qryMap.put("t.reqtype", "Q");
			priceid = AppUtils.getReqParam("priceid");
			line = AppUtils.getReqParam("line");
		}
	}
	
	@Override
	public void qryRefresh() {
		this.qryMap.remove("bargefeeid$");
		super.qryRefresh();
		this.editGrid.reload();
	}

	@SaveState
	public PriceFclBargefee priceFclBargefee = new PriceFclBargefee();
	
	@Bind
	public UIWindow priceFclBargefeeWindow;
	
	@Bind
	public UIDataGrid clientGrid;
	
	@Action
	public void clientGrid_ondblclick() {
		String id = clientGrid.getSelectedIds()[0];
		priceFclBargefee = serviceContext.priceFclBargeService.priceFclBargefeeDao.findById(Long.parseLong(id));
		priceFclBargefeeWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "priceFclBargefeeedit");
	}
	
	@Action
	public void savePriceFclBargefee(){
		serviceContext.priceFclBargeService.priceFclBargefeeDao.createOrModify(priceFclBargefee);
		clientGrid.reload();
		alert("OK");
	}
	
	@Action
	public void addBargefee(){
		priceFclBargefee = new PriceFclBargefee();
		priceFclBargefeeWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "priceFclBargefeeedit");
	}
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n s.* " +
					"\nFROM price_fcl_bargefee s " +
					"\nORDER BY namec"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String countSql = 
					"SELECT COUNT(*) AS counts " +
					"FROM price_fcl_bargefee s " ;
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	@Override
	public void del() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for (String s : ids) {
			if(p.matcher(s).matches()){
				serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.executeSQL("delete from price_fcl_bargefeedtl where id="+s);
			}
		}
		editGrid.remove();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(priceid)){
			String[] pricid = priceid.split(",");
			qry+="\n AND (FALSE";
			for(String id:pricid){
				qry += "\nOR EXISTS(SELECT 1 FROM price_fcl x WHERE (pol = t.podnamee"+
					//2016 运价维护中驳船费维护弹窗数据处理 
					//1.PRD类型的，点开驳船费弹窗，要显示所有 关联PRD的港口及这些港口的二级港口(可以用递归方式查询) 到PRD中驳船 的驳船费数据
					"\nOR (x.pol = 'PRD' AND t.podnamee = ANY(ARRAY(WITH RECURSIVE r AS ("+
					"\n		 SELECT * FROM dat_port WHERE isdelete = FALSE AND namee = 'PRD'"+
					"\n	 UNION ALL"+
					"\n		 SELECT x.* FROM dat_port x, r WHERE x.isdelete = FALSE AND x.link = r.namee"+
					"\n	 )"+
					"SELECT namee FROM r ORDER BY link,namee)))"+
					") AND shipping = t.shipping AND id="+id+")"+
					(StrUtils.isNull(line)?"":"AND t.line='"+line+"'");
//				qry+="\n OR EXISTS(SELECT 1 FROM price_fcl WHERE pol = t.podnamee AND shipping = t.shipping AND id="+id+")";
			}
			qry+="\n )";
		}
		m.put("qry", qry);
		return m;
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		try {
			serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.createOrModify(dtlData);
			this.pkVal = dtlData.getId();
			this.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@SaveState
	@Accessible
	public PriceFclBargefeeDtl dtlData = new PriceFclBargefeeDtl();
	
	@Action
	public void addBargefeedtl(){
		dtlData = new PriceFclBargefeeDtl();
		editGrid.appendRow(dtlData);
	}
	
	@Action
	public void saveMaster(){
		try {
			doServiceSaveMaster(); //Master
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		refresh();
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	public void doServiceSaveMaster() {
		try {
			if (modifiedData != null) {
                update(modifiedData);
            }
			if (addedData != null) {
                add(addedData);
            }
			if(removedData != null){
				remove(removedData);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.priceFclBargeDtlService.updateBatchEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.priceFclBargeDtlService.addBatchEditGrid(addedData, -1L);
	}
	@Override
	protected void remove(Object removedData) {
		serviceContext.priceFclBargeDtlService.removedBatchEditGrid(removedData);
	}
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String[] ids = clientGrid.getSelectedIds();
				if(ids==null||ids.length<1){
					alert("请先选择一个左边的列表项");
					return;
				}
				String callFunction = "f_imp_bargefeedtl";
				String args = ids[0] + ",'" + AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.editGrid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void clientGrid_onrowselect() {
		String id = clientGrid.getSelectedIds()[0];
		this.qryMap.put("bargefeeid$", id);
		this.editGrid.reload();
	}
	
	@Action
	public void delClient(){
		String id = clientGrid.getSelectedIds()[0];
		try {
			serviceContext.priceFclBargeService.delClient(id);
			clientGrid.reload();
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void linkEdit(){
		String pkid = AppUtils.getReqParam("pkid");
		if(StrUtils.isNull(pkid)){
			this.alert("无效pkid");
			return;
		}
		this.pkVal = Long.parseLong(pkid);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
}
