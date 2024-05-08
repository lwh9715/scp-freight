package com.scp.view.module.finance;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.FinaRpreq;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.finance.checkbillydtlBean", scope = ManagedBeanScope.REQUEST)
public class CheckBillYdtlBean extends MastDtlView {

	@SaveState
	@Accessible
	public FinaRpreq selectedRowData = new FinaRpreq();
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			
		}
	}
	

	@Override
	public void init() {
		selectedRowData = new FinaRpreq();
		String id = AppUtils.getReqParam("id");
		if(!StrUtils.isNull(id)){
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
			
		}
	}

	
	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "DZ_FE.raq";

	@Action
	public void preview() {
		String rpturl = AppUtils.getRptUrl();
		try{
			String filename = showwmsinfilename;
			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/static/stateaccount/"+filename+"&id="+this.mPkVal;
			AppUtils.openWindow("_print", openUrl);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'StateAccount' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.reqMgrService.finaRpreqDao.findById(this.mPkVal);
		disableAllButton(this.selectedRowData.getIscheck());
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(UpdateLevel.Data, "mPkVal");
		showSumDtl();
		
	}



	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		//其实第一句检索没必要加 ..但是不加查询速度太慢 主要是 union rpreqid  0 AS rpreqid
		qryMap.put("customerid$", selectedRowData.getCustomerid());
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "clause");
		qry += " rpreqid = " + mPkVal;
		m.put("clause", qry);
		return m;
	}
	
	
	@Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if (isCheck) {
			sql = "UPDATE fina_rpreq SET ischeck = TRUE ,checktime = NOW(),checkter = '"+updater+"' WHERE id ="+this.mPkVal+";";
		}else {
			sql = "UPDATE fina_rpreq SET ischeck = FALSE ,checktime = NULL,checkter = NULL WHERE id ="+this.mPkVal+";";
		}
		try {
			serviceContext.reqMgrService.finaRpreqDao.executeSQL(sql);
			refreshMasterForm();
		}catch (Exception e) {
			MessageUtils.showException(e);
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck?null:AppUtils.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck?null:Calendar.getInstance().getTime());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			this.disableAllButton(!isCheck);
		}
	}
	
	@Action
	public void generateRP() {
		try {
			this.serviceContext.statementMgrService.createRP(this.mPkVal,AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Action
	public void showSumDtl() {
		String url = "./rpreqsum.xhtml?id="+this.mPkVal;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
	}
	
	
	@Bind
	public UIButton save;
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton generateRP;
	
	
	private void disableAllButton(Boolean ischeck) {
		save.setDisabled(ischeck);
		del.setDisabled(ischeck);
		generateRP.setDisabled(!ischeck);
	}


	@Override
	public void del() {
		serviceContext.reqMgrService.finaRpreqDao.remove(selectedRowData);
		this.alert("OK");
		this.refreshMasterForm();
	}
	
	
}
