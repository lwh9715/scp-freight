package com.scp.view.module.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.data.DatWarehouse;
import com.scp.model.price.PriceTrain;
import com.scp.service.price.PriceTrainService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
import com.scp.view.comp.action.ActionGridExport;
@ManagedBean(name = "pages.module.price.mgrtrainBean", scope = ManagedBeanScope.REQUEST)
public class MgrtrainBean extends EditGridFormView{
	
	@ManagedProperty("#{priceTrainService}")
	public PriceTrainService priceTrainService;
	
	@SaveState
	@Accessible
	public PriceTrain selectedRowData = new PriceTrain();
	
	@Bind
	public String pricename;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExt = new HashMap<String, Object>();
	
	@Bind(id="qryPdd")
	public List<SelectItem> getQryPdd(){	
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT pdd","pdd","price_train AS d","WHERE 1=1","ORDER BY pdd");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}		
	}
	
	@Bind(id="qryInputer")
    public List<SelectItem> getQryInputer() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT inputer","inputer","price_train AS d","WHERE 1=1","ORDER BY inputer");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Bind(id="qryUpdater")
    public List<SelectItem> getQryUpdater() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT updater","updater","price_train AS d","WHERE updater is not null","ORDER BY updater");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Override
	public void clearQryKey() {
		if (qryMapExt != null) {
			qryMapExt.clear();
		}
		super.clearQryKey();
	}
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = priceTrainService.priceTrainDao.findById(this.pkVal);
	}
	
	@Override
	protected void doServiceSave() {
		try {
			priceTrainService.priceTrainDao.createOrModify(selectedRowData);
			this.pkVal = selectedRowData.getId();
			this.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow importDataWindow;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;
	
	@Bind
	@SaveState
	public String modtype;
	
	@Action
	protected void startImport() {
		importDataBatch();
	}
	
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
				//this.serviceContext.priceTrainService.removePriceByPriceName(pricename);
				String callFunction = "f_imp_price_train";
				String args = -100 + ",'"
						+ AppUtils.getUserSession().getUsercode() + "','"+pricename+"'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				pricename = "";
				update.markUpdate(true, UpdateLevel.Data, pricename);
				MessageUtils.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void sumbaseprice(){
		BigDecimal feefreight = new BigDecimal(0);
		BigDecimal feecnt = new BigDecimal(0);
		BigDecimal feebg = new BigDecimal(0);
		BigDecimal feeldyf = new BigDecimal(0);
		BigDecimal feelz = new BigDecimal(0);
		BigDecimal feetruck = new BigDecimal(0);
		BigDecimal feezc = new BigDecimal(0);
		BigDecimal feelyf = new BigDecimal(0);
		if(selectedRowData.getCyidfeefreight()==null||selectedRowData.getCyidfeefreight()==""){
			MessageUtils.alert("运费币制不能为空!");
			return;
		}
		if(selectedRowData.getCyidfeebg()==null || selectedRowData.getCyidfeebg()==""){
			MessageUtils.alert("报关费币制不能为空!");
			return;
			
		}
		if(selectedRowData.getCyidfeecnt()==null||selectedRowData.getCyidfeecnt()==""){
			MessageUtils.alert("租箱价格币制不能为空!");
			return;
		}
		if(modtype=="T"){
			
			if(selectedRowData.getCyidfeetruck()==null||selectedRowData.getCyidfeetruck()==""){
				MessageUtils.alert("拖车运费币制不能为空!");
				return;
			}
			
		}else if(modtype=="J"){
			
			if(selectedRowData.getCyidfeeldyf()==null||selectedRowData.getCyidfeeldyf()==""){
				MessageUtils.alert("零担运费币制不能为空!");
				return;
			}
			if(selectedRowData.getCyidfeelz()==null||selectedRowData.getCyidfeelz()==""){
				MessageUtils.alert("内装费币制不能为空!");
				return;
			}
			
		}else if(modtype=="D"){
			if(selectedRowData.getCyidfeezc()==null||selectedRowData.getCyidfeezc()==""){
				
				MessageUtils.alert("转场费币制不能为空!");
				return;
			}
			
			if(selectedRowData.getCyidfeetlyf()==null||selectedRowData.getCyidfeetlyf()==""){
				MessageUtils.alert("铁路运费币制不能为空!");
				return;
			}
			
			
		}
		
		try{
		
			//运费
			if(selectedRowData.getFeefreight()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeefreight()+"','"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeefreight()+"') AS feefreight";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feefreight = new BigDecimal(map.get("feefreight").toString());
			}
			//
			
			//租箱价格
			if(selectedRowData.getFeecnt() != null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeecnt()+"', '"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeecnt()+"') AS feecnt";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feecnt = new BigDecimal(map.get("feecnt").toString());
			}
			//报关费
			if(selectedRowData.getFeebg()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeebg()+"','"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeebg()+"') AS feebg";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feebg = new BigDecimal(map.get("feebg").toString());
			}
			//零担
			if(selectedRowData.getFeeldyf()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeeldyf()+"','"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeeldyf()+"') AS feeldyf";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feeldyf = new BigDecimal(map.get("feeldyf").toString());
			}
			//内装费
			if(selectedRowData.getFeelz()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeelz()+"','"+selectedRowData.getCurrency()+"','"+selectedRowData.getFeelz()+"') AS feelz";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feelz = new BigDecimal(map.get("feelz").toString());
			}
			//拖车运费
			if(selectedRowData.getFeetruck()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeetruck()+"','"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeetruck()+"') AS feetruck";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feetruck = new BigDecimal(map.get("feetruck").toString());
			}
			//转场费
			if(selectedRowData.getFeezc()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeezc()+"','"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeezc()+"') AS feezc";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feezc = new BigDecimal(map.get("feezc").toString());
			}
			//铁路运费
			if(selectedRowData.getFeetlyf()!= null){
				String sql = "SELECT f_amtto(now(), '"+selectedRowData.getCyidfeetlyf()+"','"+selectedRowData.getCurrency()+"', '"+selectedRowData.getFeetlyf()+"') AS feelyf";
				Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				feelyf = new BigDecimal(map.get("feelyf").toString());
			}
			
			
			Browser.execClientScript("sumbaseprice("+feefreight+","+feecnt+","+feebg+","+feeldyf+","+feelz+","+feetruck+","+feezc+","+feelyf+")");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Action
	public void release() {
		String[] ids = this.editGrid.getSelectedIds();
		
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请勾选要发布的记录!");
		}
		priceTrainService.release(ids);
		MessageUtils.alert("OK!");
		this.refresh();
	
	}
	
	@Action
	public void del() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		priceTrainService.removes(ids);
		this.refresh();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		if(!qryMapExt.isEmpty()){
			Set<String> set = qryMapExt.keySet();
			for (String object : set) {
				this.qryMap.put(object, qryMapExt.get(object));
			}
		}
	}

	@Action
	public void close() {
		this.editWindow.close();
	}
	
	@Action
	public void addForm(){
		this.pkVal = -1L;
		this.selectedRowData = new PriceTrain();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		Browser.execClientScript("changeType('J')");
	}
	
	@Bind
	@SaveState
	public String warehouse="";
	
	@Action
	public void linkEdit(){
		try {
			String pkid = AppUtils.getReqParam("pkid");
			if(StrUtils.isNull(pkid)){
				this.alert("无效pkid");
				return;
			}
			this.pkVal = Long.parseLong(pkid);
			doServiceFindData();

			this.refreshForm();
			Browser.execClientScript("initFlexVal()");
			if(editWindow != null)editWindow.show();
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			editWindow.show();
			
			String transType = this.selectedRowData.getTranstype();
			Browser.execClientScript("changeType('"+transType+"')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Action
	public void addcopy() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		Long id = Long.parseLong(ids[0]);
		PriceTrain old = priceTrainService.priceTrainDao.findById(id);
		this.addForm();
		this.selectedRowData.setLine(old.getLine());
		this.selectedRowData.setDatefm(old.getDatefm());
		this.selectedRowData.setDateto(old.getDateto());
		this.selectedRowData.setLine(old.getLine());
		this.selectedRowData.setPod(old.getPod());
		this.selectedRowData.setPol(old.getPol());
		this.selectedRowData.setRemark_in(old.getRemark_in());
		this.selectedRowData.setRemark_out(old.getRemark_out());
		this.selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
		this.selectedRowData.setInputtime(Calendar.getInstance().getTime());
		this.selectedRowData.setPoa(old.getPoa());
		this.selectedRowData.setPdd(old.getPdd());
		this.selectedRowData.setBaseprice(old.getBaseprice());
		this.selectedRowData.setCostprice(old.getCostprice());
		this.selectedRowData.setCurrency(old.getCurrency());
		this.selectedRowData.setTransitem(old.getTransitem());
		this.selectedRowData.setTranstype(old.getTranstype());
		this.selectedRowData.setTt(old.getTt());
		this.selectedRowData.setLlt(old.getLlt());
	}
	
	@Action
    private void releaseCheck_oncheck() {
        if(this.selectedRowData.getIsrelease()){
        	this.selectedRowData.setDaterrelease(Calendar.getInstance().getTime());
        }else{
        	this.selectedRowData.setDaterrelease(null);
        }
        this.saveForm();
    }
	
	
	@Bind
	@SaveState
	public String polcode;
	
	@Bind
	@SaveState
	public String podcode;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		qry = qry.replace("AND CAST(datefm AS DATE) =", "AND CAST(datefm AS DATE) >");
		qry = qry.replace("AND CAST(dateto AS DATE) =", "AND CAST(dateto AS DATE) <");
		map.put("qry",qry);
		
		if(!StrUtils.isNull(polcode))map.put("pol", "AND  t.pol = '"+polcode+"'");
		if(!StrUtils.isNull(podcode))map.put("pod", "AND  t.pod = '"+podcode+"'");
		return map;
	}
	
	@Bind
	public UIWindow feeAddWindows;
	
	@Action
    private void feeAdd() {
		feeAddWindows.show();
		String blankUrl = AppUtils.chaosUrlArs("./addtrainfee.xhtml") + "&id="+this.pkVal;
		addFeeIframe.load(blankUrl);
	}
	
	@Bind
	public UIIFrame addFeeIframe;
	
	@Bind
	public UIIFrame localChargeIframe;
	
	@Override
	public void refreshForm() {
		if(this.pkVal != null && this.pkVal>0){
			this.selectedRowData = priceTrainService.priceTrainDao.findById(this.pkVal);
			localChargeIframe.load("localchargetrain.xhtml?linkid="+this.pkVal + "&type=edit");
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}


	@Override
	public void export() {
		ActionGridExport actionGridExport = new ActionGridExport();
		actionGridExport.setKeys((String) AppUtils.getReqParam("key"));
		actionGridExport.setVals((String) AppUtils.getReqParam("value"));
		int limitsNew = limits;
		int startsNew = starts;
		try {
			limits = 100000;
			starts = 0;

			String sqlId = getMBeanName() + ".grid.page";
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId, getQryClauseWhere(this.qryMap));

			if(editGrid != null){
				List<Map> listNew = new ArrayList<Map>();
				String[] ids = this.editGrid.getSelectedIds();
				boolean flag = false;
				if(ids != null && ids.length != 0) {
					flag = true;
					for (Map map : list) {
						for (String id : ids) {
							if(StrUtils.getMapVal(map, "id").equals(id)){
								listNew.add(map);
							}
						}
					}
				}
				if(flag){
					list = listNew;
				}
			}

			for (Map map : list) {
				if ("S2S".equals(map.get("transitem"))) {
					map.put("transitem", "站到站");
				} else if ("D2S".equals(map.get("transitem"))) {
					map.put("transitem", "门到站");
				}
			}

			actionGridExport.execute(list);
			Browser.execClientScript("simulateExport.fireEvent('click');");
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally{
			limits = limitsNew;
			starts = startsNew;
		}
	}
}
