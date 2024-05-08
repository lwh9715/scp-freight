package com.scp.view.module.price;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.NoRowException;
import com.scp.model.price.PriceFclBargefee;
import com.scp.model.price.PriceFclBargefeeDtl;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.pricefclbargefeeBean", scope = ManagedBeanScope.REQUEST)
public class PriceFclBargefeeBean extends EditGridFormView {
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
//			this.qryMap.put("t.reqtype", "Q");
			super.applyGridUserDef();
		}
	}
	
	@Bind
	@SaveState
	public Date datefm;
	
	@Bind
	@SaveState
	public Date dateto;
	
	@Bind
	@SaveState
	public String datetypefm = ">=";
	
	@Bind
	@SaveState
	public String datetypeto = "<=";
	
	@Bind
	@SaveState
	public String istop = "false";
	
	@Override
	public void qryRefresh() {
		this.qryMap.remove("bargefeeid$");
//		super.qryRefresh();
		this.editGrid.reload();
//		this.clientGrid.setSelectedRow(-1);
//		this.clientGrid.reload();
	}
	
	@Action
	public void clientGridRefresh() {
		this.clientGrid.reload();
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
		priceFclBargefee.setCorpid(AppUtils.getUserSession().getCorpid());
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
					"\n WHERE (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND x.userid ="+AppUtils.getUserSession().getUserid()+"))"+
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
					"FROM price_fcl_bargefee s " +
					"\n WHERE (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = s.corpid AND x.ischoose = TRUE AND x.userid ="+AppUtils.getUserSession().getUserid()+"))";
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
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		
		map.put("qry", qry);
		String corpfilter = "AND (t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		map.put("corpfilter", corpfilter);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String filter = "AND 1=1";
		if(datefm != null && dateto != null ){
			filter += "\n AND datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "' AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
			//filter += "\n AND '" + sdf.format(datefm).toString() + "' " + datetypefm + " datefm  AND '" + sdf.format(dateto).toString() + "' " + datetypeto + " dateto ";
		}
		if(datefm != null && dateto == null){
			filter += "\n AND datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "'";
		}
		if(datefm == null && dateto != null){
			filter += "\n AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
		}
		if(!"all".equals(istop)){
			filter +="\n AND istop ="+istop+"";
		}
		map.put("filter", filter);
		
		return map;
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		try {
			if(0 == dtlData.getId()){
				dtlData.setInputer(AppUtils.getUserSession().getUsercode());
			}
			serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.createOrModify(dtlData);
			this.pkVal = dtlData.getId();
			if(moduleList != null){
				savefclfee(this.pkVal);
			}
			moduleList = null;
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
		dtlData.setCorpid(AppUtils.getUserSession().getCorpid());
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
		String[] ids = clientGrid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请先选择一个左边的列表项");
			return;
		}
		serviceContext.priceFclBargeDtlService.addBatchEditGrid(addedData, Long.parseLong(ids[0]));
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
	
	
	@Bind
	public UIIFrame localChargeIframe;
	
	@Override
	public void refreshForm() {
		if(this.pkVal != null && this.pkVal>0){
			//this.dtlData = serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.findById(this.pkVal);
			localChargeIframe.load("localchargecondition.xhtml?linkid="+this.pkVal);
			//update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}
	
	@Bind
	public UIWindow feeAddWindows;
	
	@Bind
	public UIIFrame addFeeIframe;
	
	@Action
    private void feeAdd() {
		feeAddWindows.show();
		String blankUrl = AppUtils.chaosUrlArs("./addfee.xhtml") + "&id="+this.pkVal;
		addFeeIframe.load(blankUrl);
	}
	
	@Action
	private void addFeeUpdate(){
		String[] ids = this.editGrid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("至少选择一行");
			return;
		}
		feeAddWindows.show();
		String blankUrl = AppUtils.chaosUrlArs("./addfee.xhtml?batchRefIds="+StrUtils.array2List(ids));
		addFeeIframe.load(blankUrl);
	}
	@Action
	public void markInvalid() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		for (String s : ids) {
				serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.executeSQL("update price_fcl_bargefeedtl set isinvalid = false where id="+s);
		}
		editGrid.reload();
	}
	
	@Action
	public void markInvalidTrue() {
		String[] ids = editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		for (String s : ids) {
				serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.executeSQL("update price_fcl_bargefeedtl set isinvalid = true where id="+s);
		}
		editGrid.reload();
	}
	
	@Action
	public void addcopy(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		Long id = Long.parseLong(ids[0]);
		this.pkVal = -1L;
		this.dtlData = new PriceFclBargefeeDtl();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		PriceFclBargefeeDtl old = serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.findById(id);
		this.dtlData.setPolnamee(old.getPolnamee());
		this.dtlData.setPolnamec(old.getPolnamec());
		this.dtlData.setPodnamee(old.getPodnamee());
		this.dtlData.setPodnamec(old.getPodnamec());
		this.dtlData.setBargefeeid(old.getBargefeeid());
		this.dtlData.setCost20(old.getCost20());
		this.dtlData.setCost202(old.getCost202());
		this.dtlData.setCost40gp(old.getCost40gp());
		this.dtlData.setCost40gp2(old.getCost40gp2());
		this.dtlData.setCost40hq(old.getCost40hq());
		this.dtlData.setCost40hq2(old.getCost40hq2());
		this.dtlData.setArea(old.getArea());
		this.dtlData.setIsinvalid(old.getIsinvalid());
		this.dtlData.setLine(old.getLine());
		this.dtlData.setRemark(old.getRemark());
		this.dtlData.setDatefm(old.getDatefm());
		this.dtlData.setDateto(old.getDateto());
		this.dtlData.setCondition(old.getCondition());
		this.dtlData.setConditiontype(old.getConditiontype());
		this.dtlData.setConditionvalue(old.getConditionvalue());
		this.dtlData.setCondition2(old.getCondition2());
		this.dtlData.setConditiontype2(old.getConditiontype2());
		this.dtlData.setConditionvalue2(old.getConditionvalue2());
		this.dtlData.setCorpid(AppUtils.getUserSession().getCorpid());
		this.dtlData.setIsrelease(false);
		if(!StrUtils.isNull(old.getCurrency())){
			this.dtlData.setCurrency(old.getCurrency());
		}else{
			this.dtlData.setCurrency("USD");
		}
		addfclfeea(-1L);
	}
	
	
	@SaveState
	protected List<PriceFclFeeadd> moduleList = null;
	
	/**
	 * 查询并添加附加费
	 */
	public void addfclfeea(Long dataid){
		String[] ids = this.editGrid.getSelectedIds();
		Long id = Long.parseLong(ids[0]);
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid="+id + " AND isdelete = false ORDER BY id");
		moduleList = new ArrayList<PriceFclFeeadd>();
		for (PriceFclFeeadd fclFeeadd : list) {
        	PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
        	priceFclFeeadd.setAmt(fclFeeadd.getAmt());
			moduleList.add(priceFclFeeadd);
		}
		
	}
	
	/**
	 * 查询保存附加费
	 */
	public void savefclfee(Long dataid){
			String[] ids = this.editGrid.getSelectedIds();
			Long id = Long.parseLong(ids[0]);
			List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid="+id + " AND isdelete = false ORDER BY id");
			moduleList = new ArrayList<PriceFclFeeadd>();
			for (PriceFclFeeadd fclFeeadd : list) {
	        	PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
	        	priceFclFeeadd.setAmt(fclFeeadd.getAmt());
	        	priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
	        	priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
	        	priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
	        	priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
	        	priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
	        	priceFclFeeadd.setFclid(dataid);
	        	priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
	        	priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
	        	priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
				priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
				priceFclFeeadd.setUnit(fclFeeadd.getUnit());
				priceFclFeeadd.setInputer(AppUtils.getUserSession().getUsercode());
				priceFclFeeadd.setInputtime(Calendar.getInstance().getTime());
				priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
				moduleList.add(priceFclFeeadd);
			}
			 serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
	}
	
	@Override
	public void saveBatch1(Object obj) {
		String[] ids = clientGrid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请先选择一个左边的列表项");
			return;
		}
		String[] editids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		if("[]".equals(obj.toString())){
			serviceContext.priceFclBargeDtlService.saveBatchEditGrid(editids);
		}else{
			serviceContext.priceFclBargeDtlService.saveBatchEditGrid(modifiedData, Long.parseLong(ids[0]));
		}
		Browser.execClientScript("qryRefreshfun()");
	}
	
	@Bind
	@SaveState
	public Date batchdatefm;
	
	@Bind
	@SaveState
	public Date batchdateto;
	
	@Bind
	public String batchpolnamee;
	
	@Bind
	public String batchpolnamec;
	
	@Bind
	public String batchpodnamee;
	
	@Bind
	public String batchpodnamec;
	
	@Bind
	public String batchcost20;
	
	@Bind
	public String batchcost40gp;
	
	@Bind
	public String batchcost40hq;
	
	@Bind
	public String batchcost45hq;
	
	@Bind
	public String batchcurrency;
	
	@Bind
	public String batcharea;
	
	@Bind
	public String batchline;
	
	@Bind
	public String batchremark;
	
	@Bind
	public String batchconditiontype;
	
	@Bind
	public String batchcondition;
	
	@Bind
	public String batchconditionvalue;
	
	@Bind
	public String schedulev;
	
	@Bind
	public String batchconditiontype2;
	
	@Bind
	public String batchcondition2;
	
	@Bind
	public String batchconditionvalue2;
	
	@Action
    private void saveBatchdate() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		try {
			if(batchdatefm != null){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "datefm" ,batchdatefm.toLocaleString(),AppUtils.getUserSession().getUsercode());
			}
			if(batchdateto != null){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "dateto" ,batchdateto.toLocaleString(),AppUtils.getUserSession().getUsercode());
			}
			if(!StrUtils.isNull(batchpolnamee)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "polnamee" ,StrUtils.getSqlFormat(batchpolnamee),AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchpolnamec)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "polnamec" ,StrUtils.getSqlFormat(batchpolnamec),AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchpodnamee)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "podnamee" ,StrUtils.getSqlFormat(batchpodnamee),AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchpodnamec)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "podnamec" ,StrUtils.getSqlFormat(batchpodnamec),AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcost20)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "cost20" ,batchcost20,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcost40gp)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "cost40gp" ,batchcost40gp,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcost40hq)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "cost40hq" ,batchcost40hq,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcost45hq)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "cost45hq" ,batchcost45hq,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcurrency)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "currency" ,batchcurrency,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batcharea)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "area" ,batcharea,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchline)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "line" ,batchline,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchremark)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "remark" ,batchremark,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchconditiontype)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "conditiontype" ,batchconditiontype,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcondition)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "condition" ,batchcondition,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchconditionvalue)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "conditionvalue" ,batchconditionvalue,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(schedulev)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "schedule" ,schedulev,AppUtils.getUserSession().getUsercode() );
			}
			
			
			if(!StrUtils.isNull(batchconditiontype2)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "conditiontype2" ,batchconditiontype2,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchcondition2)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "condition2" ,batchcondition2,AppUtils.getUserSession().getUsercode() );
			}
			if(!StrUtils.isNull(batchconditionvalue2)){
				serviceContext.priceFclBargeDtlService.updateBatch(ids , "conditionvalue2" ,batchconditionvalue2,AppUtils.getUserSession().getUsercode() );
			}
			
			
			MessageUtils.alert("OK!");
			this.refresh();
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow nosislockwindow;
	
	@SaveState
	public String nosislock = "";
	
	@Action
	public void fclbargeff2jobsupdate(){
		String sql = "SELECT f_price_fclbarge2jobs_update('pricebargeid="+this.pkVal+";userid="
					+AppUtils.getUserSession().getUserid()+"') AS nosislock";
		try{
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null){
				nosislockwindow.show();
				nosislock = m.get("nosislock").toString();
				String js = "setNosislock('"+nosislock+"')";
				Browser.execClientScript(js);
			}
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void fclbargeff2jobsupdateAll(){
		String[] ids = this.editGrid.getSelectedIds();
		if(ids == null || ids.length == 0){
			MessageUtils.alert("请勾选要修改的行!");
			return;
		}
		String priceids = StrUtils.array2List(ids);
		String sql = "SELECT f_price_fclbarge2jobs_update('pricebargeid="+priceids+";userid="
					+AppUtils.getUserSession().getUserid()+"') AS nosislock";
		try{
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null){
				nosislockwindow.show();
				nosislock = m.get("nosislock").toString();
				String js = "setNosislock('"+nosislock+"')";
				Browser.execClientScript(js);
			}
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void delay(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请至少勾选一条记录!");
			return;
		}
		for(String id:ids){
			PriceFclBargefeeDtl old = serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.findById(Long.parseLong(id));
			if(batchdatefm!=null&&batchdatefm!=old.getDatefm()){
				String sql = "UPDATE price_fcl_bargefeedtl SET updater = 'delay"+AppUtils.getUserSession().getUsercode()
							+"',datefm ='"+batchdatefm+"' ,updatetime = now() WHERE id ="+old.getId();
				try {
					serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.executeSQL(sql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
			if(batchdateto!=null&&batchdateto!=old.getDateto()){
				String sql = "UPDATE price_fcl_bargefeedtl SET updater = 'delay"+AppUtils.getUserSession().getUsercode()
							+"',dateto='"+batchdateto+"' ,updatetime = now() WHERE id ="+old.getId();
				try {
					serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.executeSQL(sql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		this.editGrid.reload();
		showNosislock(ids);
		alert("OK");
	}
	
	/**
	 * 运价有效期变更的时候如果工作单委托中运价类型有值就不会撤回运价，此时把没有撤回的工作单显示到页面上
	 * @param ids
	 */
	public void showNosislock(String[] ids){
		String sql = "SELECT logdesc FROM sys_log WHERE refid=any(ARRAY["+StrUtils.array2List(ids)+"])";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null){
				nosislockwindow.show();
				nosislock = m.get("logdesc").toString();
				String js = "setNosislock('"+nosislock+"')";
				Browser.execClientScript(js);
			}
		}catch(NoRowException e){
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void stop() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		for (String s : ids) {
			serviceContext.priceFclBargeDtlService.priceFclBargefeeDtlDao.executeSQL("update price_fcl_bargefeedtl set istop = true where id="+s);
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Action
	public void clearSubmit(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请至少勾选一条记录!");
			return;
		}
		String column = AppUtils.getReqParam("value");
		serviceContext.priceFclBargeDtlService.clearBatch(ids,  column, AppUtils.getUserSession().getUsercode());
		MessageUtils.alert("OK!");
	}
	
	@Bind
	public UIWindow editBatchWindow;
	
	@Action
	public void updateAll(){
		batchdatefm = null;
		batchdateto = null;
		batchpolnamee = "";
		batchpolnamec = "";
		batchpodnamee = "";
		batchpodnamec = "";
		batchcost20 = "";
		batchcost40gp = "";
		batchcost40hq = "";
		batchcost45hq = "";
		batchcurrency = "";
		batcharea = "";
		batchline = "";
		schedulev = "";
		batchremark = "";
		batchconditiontype ="";
		batchcondition = "";
		batchconditionvalue ="";
		batchconditiontype2 ="";
		batchcondition2 = "";
		batchconditionvalue2 ="";
		update.markUpdate(true, UpdateLevel.Data, "editBatchPanel");
		editBatchWindow.show();
	}
	
}
