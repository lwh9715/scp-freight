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
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.price.PriceFcl;
import com.scp.service.price.PricefclMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.fbaqryfclBean", scope = ManagedBeanScope.REQUEST)
public class FbaqryfclBean extends EditGridFormView{
    
    @ManagedProperty("#{pricefclMgrService}")
	public PricefclMgrService pricefclMgrService;
    
    @SaveState
	@Accessible
	public PriceFcl selectedRowData = new PriceFcl();
    
    
    @Bind
    public String pricename;
    
    /**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExt = new HashMap<String, Object>();
    
    @Bind(id="polJson")
    public String getPolJson() {
    	try {
			String json =  "{\"results\":"+CommonComBoxBean.getComboxItemsAsJson("DISTINCT pod As name","price_fcl AS d","WHERE pod <> ''","ORDER BY pod")+"}";
			//AppUtils.debug(json);
			return json;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return "";
		}
    }

    @Bind(id="qryPricename")
    public List<SelectItem> getQryPricename() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT pricename","pricename","price_fcl","WHERE isdelete = FALSE AND pricename IS NOT NULL AND pricename <> ''","ORDER BY pricename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    	
	@Bind(id="qryPol")
    public List<SelectItem> getQryPol() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT namee","namee","dat_port AS d","WHERE 1=1","ORDER BY namee");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryPod")
    public List<SelectItem> getQryPod() {
    	try {
    		return CommonComBoxBean.getComboxItems("DISTINCT namee","namee","dat_port AS d","WHERE 1=1","ORDER BY namee");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryCar")
    public List<SelectItem> getQryCar() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT shipping","shipping","price_fcl AS d","WHERE 1=1","ORDER BY shipping");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Bind(id="qryInputer")
    public List<SelectItem> getQryInputer() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT inputer","inputer","price_fcl AS d","WHERE 1=1","ORDER BY inputer");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryUpdater")
    public List<SelectItem> getQryUpdater() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT updater","updater","price_fcl AS d","WHERE updater is not null","ORDER BY updater");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qrySchedule")
    public List<SelectItem> getQrySchedule() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT schedule","schedule","price_fcl AS d","WHERE 1=1","ORDER BY schedule");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	
	@Bind
	public UICombo qryPolCom;
	
	@Bind
	public UICombo qryPodCom;
	
	@Bind
	public UICombo qryCarCom;
	
	@Bind
	@SaveState
	public String feedesc;

	@Override
	public void clearQryKey() {
		if (qryMapExt != null) {
			qryMapExt.clear();
		}
		super.clearQryKey();
		if(qryPolCom != null)qryPolCom.repaint();
		if(qryPodCom != null)qryPodCom.repaint();
		if(qryCarCom != null)qryCarCom.repaint();
	}
	
	
	
	@Override
	protected void doServiceFindData() {
		//this.pkVal = getGridSelectId();
		this.selectedRowData = pricefclMgrService.pricefclDao.findById(this.pkVal);
		feedesc = pricefclMgrService.refreshFeeDesc(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			pricefclMgrService.pricefclDao.createOrModify(selectedRowData);
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
				String callFunction = "f_imp_price_fcl";
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
		pricefclMgrService.release(ids);
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Action
	public void releaseThis() {
		pricefclMgrService.release(new String[]{String.valueOf(this.pkVal)});
		MessageUtils.alert("OK!");
		this.refreshForm();
	}
	
	
	
	@Action
	public void del() {
		String[] ids = this.editGrid.getSelectedIds();
		String username = AppUtils.getUserSession().getUsername();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		pricefclMgrService.removes(ids,username);
		this.refresh();
	}
	

	@Override
	public void refresh() {
		super.refresh();
		update.markUpdate(true, UpdateLevel.Data, "qryPolCom");
		update.markUpdate(true, UpdateLevel.Data, "qryPodCom");
		update.markUpdate(true, UpdateLevel.Data, "qryCarCom");
		
		
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
			this.selectedRowData = pricefclMgrService.pricefclDao.findById(this.pkVal);
			feedesc = pricefclMgrService.refreshFeeDesc(this.pkVal);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		}
	}

	@Action
	public void close() {
		
		this.editWindow.close();
	}
	
	
	@Action
	public void addcopy() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		Long id = Long.parseLong(ids[0]);
		PriceFcl old = pricefclMgrService.pricefclDao.findById(id);
		
		this.addForm();
		this.selectedRowData.setCls(old.getCls());
		this.selectedRowData.setContacter(old.getContacter());
		this.selectedRowData.setCost20(old.getCost20());
		this.selectedRowData.setCost40gp(old.getCost40gp());
		this.selectedRowData.setCost40hq(old.getCost40hq());
		this.selectedRowData.setCountry(old.getCountry());
		this.selectedRowData.setDatefm(old.getDatefm());
		this.selectedRowData.setDateto(old.getDateto());
		this.selectedRowData.setEtd(old.getEtd());
		this.selectedRowData.setLine(old.getLine());
		this.selectedRowData.setPod(old.getPod());
		this.selectedRowData.setPol(old.getPol());
		this.selectedRowData.setRemark_fee(old.getRemark_fee());
		this.selectedRowData.setRemark_in(old.getRemark_in());
		this.selectedRowData.setRemark_out(old.getRemark_out());
		this.selectedRowData.setRemark_ship(old.getRemark_ship());
		this.selectedRowData.setSchedule(old.getSchedule());
		this.selectedRowData.setShipping(old.getShipping());
		this.selectedRowData.setTt(old.getTt());
		this.selectedRowData.setVia(old.getVia());;
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
	public String pol;
	
	@Bind
	@SaveState
	public String pod;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		qry = qry.replace("AND CAST(datefm AS DATE) =", "AND CAST(datefm AS DATE) >");
		qry = qry.replace("AND CAST(dateto AS DATE) =", "AND CAST(dateto AS DATE) <");
		map.put("qry",qry);
		
		if(!StrUtils.isNull(pol))map.put("pol", "AND EXISTS (SELECT 1 FROM dat_port x where x.namee = pol AND (x.namee = '"+pol+"' OR x.link = '"+pol+"'))");
		if(!StrUtils.isNull(pod))map.put("pod", "AND EXISTS (SELECT 1 FROM dat_port x where x.namee = pod AND (x.namee = '"+pod+"' OR x.link = '"+pod+"'))");
		
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
			pricefclMgrService.updateBatch(ids , z20gp , c20gp , "20" , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(z40gp)){
			pricefclMgrService.updateBatch(ids , z40gp , c40gp , "40gp" , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(z40hq)){
			pricefclMgrService.updateBatch(ids , z40hq , c40hq , "40hq" , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Action
    private void saveBatchVesVel() {
		
		String[] ids = this.editGrid.getSelectedIds();
		if(!StrUtils.isNull(batchSchedule)){
			pricefclMgrService.updateBatch(ids , "schedule" ,batchSchedule , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(batchTT)){
			pricefclMgrService.updateBatch(ids , "tt" ,batchTT , AppUtils.getUserSession().getUsercode());
		}
		
		if(batchETD != null){
			pricefclMgrService.updateBatch(ids , "etd" ,batchETD.toLocaleString() , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	
	@Action
    private void saveBatchRemarks() {
		String[] ids = this.editGrid.getSelectedIds();
		if(!StrUtils.isNull(batchRemarksInner)){
			pricefclMgrService.updateBatch(ids , "remark_in" ,batchRemarksInner , AppUtils.getUserSession().getUsercode());
		}
		
		if(!StrUtils.isNull(batchRemarksOuter)){
			pricefclMgrService.updateBatch(ids , "remark_out" ,batchRemarksOuter , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	
	@Action
    private void saveBatchData() {
		String[] ids = this.editGrid.getSelectedIds();
		if(batchDateFr != null){
			pricefclMgrService.updateBatch(ids , "datefm" ,batchDateFr.toLocaleString() , AppUtils.getUserSession().getUsercode());
		}
		
		if(batchDateTo != null){
			pricefclMgrService.updateBatch(ids , "dateto" ,batchDateTo.toLocaleString() , AppUtils.getUserSession().getUsercode());
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}


	@Override
	protected void update(Object obj) {
		pricefclMgrService.updateBatchEditGrid(obj);
	}

	
	@Action
    private void clearQry2() {
		this.clearQryKey();
	}
	
}
