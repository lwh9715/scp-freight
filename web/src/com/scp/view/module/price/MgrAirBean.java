package com.scp.view.module.price;

import java.util.Calendar;
import java.util.Date;
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
import com.scp.model.price.PriceAir;
import com.scp.service.price.PriceAirService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.mgrairBean", scope = ManagedBeanScope.REQUEST)
public class MgrAirBean extends EditGridFormView{
    
    @ManagedProperty("#{priceAirService}")
	public PriceAirService priceAirService;
    
    @SaveState
	@Accessible
	public PriceAir selectedRowData = new PriceAir();
    
    
    @Bind
    public String pricename;
    
    /**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExt = new HashMap<String, Object>();
    
    @Bind(id="qryPricename")
    public List<SelectItem> getQryPricename() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT pricename","pricename","price_fcl","WHERE isdelete = FALSE AND pricename IS NOT NULL AND pricename <> ''","ORDER BY pricename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryInputer")
    public List<SelectItem> getQryInputer() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT inputer","inputer","price_air AS d","WHERE 1=1","ORDER BY inputer");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryUpdater")
    public List<SelectItem> getQryUpdater() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT updater","updater","price_air AS d","WHERE updater is not null","ORDER BY updater");
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
		this.selectedRowData = priceAirService.priceAirDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			priceAirService.priceAirDao.createOrModify(selectedRowData);
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
	
	@Action
	protected void startImport() {
		importDataBatch();
	}

	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 1);
		//AppUtils.debug(importDataText);
	}*/
	
	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)||StrUtils.isNull(pricename)||pricename.trim().isEmpty()) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				this.serviceContext.pricefclMgrService.removePriceByPriceName(pricename);
				String callFunction = "f_imp_price_air";
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
	public void release() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请勾选要发布的记录!");
		}
		//priceAirService.release(ids);
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Action
	public void releaseThis() {
		//priceAirService.release(new String[]{String.valueOf(this.pkVal)});
		MessageUtils.alert("OK!");
		this.refreshForm();
	}
	
	
	
	@Action
	public void del() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		priceAirService.removes(ids);
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
	
	@Override
	public void refreshForm() {
		if(this.pkVal != null && this.pkVal>0){
			this.selectedRowData = priceAirService.priceAirDao.findById(this.pkVal);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void close() {
		
		this.editWindow.close();
	}
	
	@Action
	public void addForm(){
		this.pkVal = -1L;
		this.selectedRowData = new PriceAir();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		if(editWindow != null)editWindow.show();
		//if(this.selectedRowData.getPriceuserid()!=null){
		//	SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
		//	Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
		//}else{
		//	Browser.execClientScript("$('#priceuser_input').val('')");
		//}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
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
		Browser.execClientScript("setpol()");
		if(editWindow != null)editWindow.show();
		//if(this.selectedRowData.getPriceuserid()!=null){
		//	SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
		//	Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
		//}else{
		//	Browser.execClientScript("$('#priceuser_input').val('')");
		//}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
	
	
	@Action
	public void addcopy() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		Long id = Long.parseLong(ids[0]);
		PriceAir old = priceAirService.priceAirDao.findById(id);
		this.addForm();
		this.selectedRowData.setLine(old.getLine());
		this.selectedRowData.setLinecode(old.getLinecode());
		this.selectedRowData.setDatefm(old.getDatefm());
		this.selectedRowData.setDateto(old.getDateto());
		this.selectedRowData.setLine(old.getLine());
		this.selectedRowData.setPod(old.getPod());
		this.selectedRowData.setPol(old.getPol());
		this.selectedRowData.setCostkeji(old.getCostkeji());
		this.selectedRowData.setCosthuoji(old.getCosthuoji());
		this.selectedRowData.setCostpaohuo(old.getCostpaohuo());
		this.selectedRowData.setCostbiaoyun(old.getCostbiaoyun());
		this.selectedRowData.setCostbiaoyunp(old.getCostbiaoyunp());
		this.selectedRowData.setCostkuaiyun(old.getCostkuaiyun());
		this.selectedRowData.setCost1300(old.getCost1300());
		this.selectedRowData.setCost1500(old.getCost1500());
		this.selectedRowData.setM(old.getM());
		this.selectedRowData.setN(old.getN());
		this.selectedRowData.setRy(old.getRy());
		this.selectedRowData.setZx(old.getZx());
		this.selectedRowData.setGzf(old.getGzf());
		this.selectedRowData.setTruck(old.getTruck());
		this.selectedRowData.setDoc(old.getDoc());
		this.selectedRowData.setBgf(old.getBgf());
		this.selectedRowData.setRcf(old.getRcf());
		this.selectedRowData.setAmsens(old.getAmsens());
		this.selectedRowData.setByf(old.getByf());
		this.selectedRowData.setOther(old.getOther());
		this.selectedRowData.setRemark_in(old.getRemark_in());
		this.selectedRowData.setRemark_out(old.getRemark_out());
		this.selectedRowData.setVia(old.getVia());
		this.selectedRowData.setAirflight(old.getAirflight());
		this.selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
		this.selectedRowData.setInputtime(Calendar.getInstance().getTime());
	}

	
	@Action
    private void releaseCheck_oncheck() {
        if(this.selectedRowData.getIsrelease()){
        	this.selectedRowData.setDaterelease(Calendar.getInstance().getTime());
        }else{
        	this.selectedRowData.setDaterelease(null);
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
		String blankUrl = AppUtils.chaosUrlArs("./addfee.xhtml") + "&id="+this.pkVal;
		addFeeIframe.load(blankUrl);
	}
	
	
	@Bind
	public UIIFrame addFeeIframe;
	
	
	@Bind
	@SaveState
	public String z20gp;
	
	@Bind
	@SaveState
	public String z40gp;
	
	@Bind
	@SaveState
	public String z40hq;
	
	@Bind
	@SaveState
	public String c20gp;
	
	@Bind
	@SaveState
	public String c40gp;
	
	@Bind
	@SaveState
	public String c40hq;
	
	@Bind
	@SaveState
	public String batchSchedule;
	
	@Bind
	@SaveState
	public String batchTT;
	
	@Bind
	@SaveState
	public Date batchETD;
	
	@Bind
	@SaveState
	public String batchRemarksInner;
	@Bind
	@SaveState
	public String batchRemarksOuter;
	
	
	@Bind
	@SaveState
	public Date batchDateFr;
	@Bind
	@SaveState
	public Date batchDateTo;
	
	
	
	
	@Bind
	public UIWindow editBatchWindowCost;
	
	@Bind
	public UIWindow editBatchWindoVesVel;
	
	@Bind
	public UIWindow editBatchWindowData;
	
	@Bind
	public UIWindow editBatchWindowRemarks;
	
	@Bind
	@SaveState
	private String editBatch;
	
	@Bind
	public String cother;
	
	@Bind
	public String zcntypeother;
	
	@Bind
	public String z20gpv;
	
	@Bind
	public String z40gpv;
	
	@Bind
	public String z40hqv;
	
	@Bind
	public String zcntypeotherv;
	
	@Action(id="editBatch" , event="onselect")
	private void editBatch(){
		////AppUtils.debug(editBatch);
		String[] ids = this.editGrid.getSelectedIds();
		////AppUtils.debug(ids);
		if(ids == null || ids.length == 0){
			MessageUtils.alert("请勾选要修改的行!");
			return;
		}
		if(editBatch==null || editBatch.length() == 0){
			MessageUtils.alert("请勾选要修改的行!");
			return;
		}else if(editBatch.equals("Cost")){
			editBatchWindowCost.show();
		}else if(editBatch.equals("AddFee")){
			feeAddWindows.show();
			String blankUrl = AppUtils.chaosUrlArs("./addfee.xhtml?batchRefIds="+StrUtils.array2List(ids));
			addFeeIframe.load(blankUrl);
		}else if(editBatch.equals("VesVel")){
			editBatchWindoVesVel.show();
		}else if(editBatch.equals("Data")){
			editBatchWindowData.show();
		}else if(editBatch.equals("Remarks")){
			editBatchWindowRemarks.show();
			
		}
	}
	
	@Action
    private void saveBatchCost() {
		String[] ids = this.editGrid.getSelectedIds();
		if(!StrUtils.isNull(z20gp)){
			//priceAirService.updateBatch(ids , z20gp , c20gp , "20" , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(z40gp)){
			//priceAirService.updateBatch(ids , z40gp , c40gp , "40gp" , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(z40hq)){
			//priceAirService.updateBatch(ids , z40hq , c40hq , "40hq" , AppUtils.getUserSession().getUsercode());
		}
		if(!StrUtils.isNull(zcntypeother)){
			//priceAirService.updateBatchother(ids , zcntypeother , cother , AppUtils.getUserSession().getUsercode());
		}
		if(!StrUtils.isNull(z20gpv)){
			//priceAirService.updateBatchValue(ids , z20gpv , "20" , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(z20gpv)){
			//priceAirService.updateBatchValue(ids , z20gpv , "40gp" , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(z40hqv)){
			//priceAirService.updateBatchValue(ids , z20gpv , "40hq" , AppUtils.getUserSession().getUsercode());
		}
		if(!StrUtils.isNull(zcntypeotherv)){
			//priceAirService.updateBatchotherValue(ids , zcntypeotherv , cother , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Action
    private void saveBatchVesVel() {
		
		String[] ids = this.editGrid.getSelectedIds();
		if(!StrUtils.isNull(batchSchedule)){
			//priceAirService.updateBatch(ids , "schedule" ,batchSchedule , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(batchTT)){
			//priceAirService.updateBatch(ids , "tt" ,batchTT , AppUtils.getUserSession().getUsercode());
		}
		
		if(batchETD != null){
			//priceAirService.updateBatch(ids , "etd" ,batchETD.toLocaleString() , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	
	@Action
    private void saveBatchRemarks() {
		String[] ids = this.editGrid.getSelectedIds();
		if(!StrUtils.isNull(batchRemarksInner)){
			//priceAirService.updateBatch(ids , "remark_in" ,batchRemarksInner , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(batchRemarksOuter)){
			//priceAirService.updateBatch(ids , "remark_out" ,batchRemarksOuter , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	
	@Action
    private void saveBatchData() {
		String[] ids = this.editGrid.getSelectedIds();
		if(batchDateFr != null){
			//priceAirService.updateBatch(ids , "datefm" ,batchDateFr.toLocaleString() , AppUtils.getUserSession().getUsercode());
		}
		
		if(batchDateTo != null){
			//priceAirService.updateBatch(ids , "dateto" ,batchDateTo.toLocaleString() , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}


	@Override
	protected void update(Object obj) {
		priceAirService.updateBatchEditGrid(obj);
	}

	
	@Action
    private void clearQry2() {
		this.clearQryKey();
	}
	
	
	@Override
	public void editGrid_ondblclick(){
		this.pkVal = Long.parseLong(editGrid.getSelectedIds()[0]);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		//if(this.selectedRowData.getPriceuserid()!=null){
		//	SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
		//	Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
		//}else{
		//	Browser.execClientScript("$('#priceuser_input').val('')");
		//}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
}
