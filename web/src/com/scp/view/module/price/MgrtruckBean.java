package com.scp.view.module.price;

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
import com.scp.model.price.PriceTruck;
import com.scp.service.price.PriceTruckService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
@ManagedBean(name = "pages.module.price.mgrtruckBean", scope = ManagedBeanScope.REQUEST)
public class MgrtruckBean extends EditGridFormView{
	
	@ManagedProperty("#{priceTruckService}")
	public PriceTruckService priceTruckService;
	
	@SaveState
	@Accessible
	public PriceTruck selectedRowData = new PriceTruck();
	
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
			return CommonComBoxBean.getComboxItems("DISTINCT pricename","pricename","price_truck","WHERE isdelete = FALSE AND pricename IS NOT NULL AND pricename <> ''","ORDER BY pricename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryInputer")
    public List<SelectItem> getQryInputer() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT inputer","inputer","price_truck AS d","WHERE 1=1","ORDER BY inputer");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryUpdater")
    public List<SelectItem> getQryUpdater() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT updater","updater","price_truck AS d","WHERE updater is not null","ORDER BY updater");
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
		this.selectedRowData = priceTruckService.priceTruckDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			priceTruckService.priceTruckDao.createOrModify(selectedRowData);
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
				this.serviceContext.priceTruckService.removePriceByPriceName(pricename);
				String callFunction = "f_imp_price_truck";
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
		//priceTruckService.release(ids);
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	@Action
	public void releaseThis() {
		//priceTruckService.release(new String[]{String.valueOf(this.pkVal)});
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
		priceTruckService.removes(ids);
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
			this.selectedRowData = priceTruckService.priceTruckDao.findById(this.pkVal);
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
		this.selectedRowData = new PriceTruck();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		if(editWindow != null)editWindow.show();
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
		PriceTruck old = priceTruckService.priceTruckDao.findById(id);
		this.addForm();
		this.selectedRowData.setDatefm(old.getDatefm());
		this.selectedRowData.setDateto(old.getDateto());
		this.selectedRowData.setPod(old.getPod());
		this.selectedRowData.setPoa_area(old.getPoa_area());
		this.selectedRowData.setPoa_city(old.getPoa_city());
		this.selectedRowData.setPoa_street(old.getPoa_street());
		this.selectedRowData.setRemark_in(old.getRemark_in());
		this.selectedRowData.setRemark_out(old.getRemark_out());
		this.selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
		this.selectedRowData.setInputtime(Calendar.getInstance().getTime());
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
	public String podcode;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		qry = qry.replace("AND CAST(datefm AS DATE) =", "AND CAST(datefm AS DATE) >");
		qry = qry.replace("AND CAST(dateto AS DATE) =", "AND CAST(dateto AS DATE) <");
		map.put("qry",qry);
		
		if(!StrUtils.isNull(podcode))map.put("pod", "AND  t.pod = '"+podcode+"'");
		return map;
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
	
	@Override
	public void editGrid_ondblclick(){
		this.pkVal = Long.parseLong(editGrid.getSelectedIds()[0]);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
}
