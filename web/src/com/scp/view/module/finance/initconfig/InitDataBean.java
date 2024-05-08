package com.scp.view.module.finance.initconfig;

import java.util.Calendar;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsXrate;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.initdataBean", scope = ManagedBeanScope.REQUEST)
public class InitDataBean extends GridFormView {

	@SaveState
	@Accessible
	public String periodid;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public FsXrate ddata = new FsXrate();
	
	@Bind
	@SaveState
	public String info;
	
	@Bind
	@SaveState
	public String currency = "CNY";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			this.showInfo();
			
			Calendar calendar = Calendar.getInstance();
			startYear = String.valueOf(calendar.get(Calendar.YEAR));
			startPeriod = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
		}
	}

	private void showInfo() {
		Long actsetid = AppUtils.getUserSession().getActsetid();
		String sql = "SELECT istart,startime FROM fs_actset WHERE id="+actsetid+" AND isdelete = FALSE";
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String istart = String.valueOf(m.get("istart"));
		if(!StrUtils.isNull(istart)) {
			if(Boolean.valueOf(istart)) {
				String startime = String.valueOf(m.get("startime")).substring(0, 19);
				info = "启用:" + startime;
			} else {
				info = "未启用";
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "info");
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		m.put("qry", qry);
		
		m.put("cyid", currency);
		return m;
	}
	
	
	@Bind
	@SaveState
	public String oamt1 = "0";
	@Bind
	@SaveState
	public String camt3 = "0";
	@Bind
	@SaveState
	public String damt2 = "0";
	@Bind
	@SaveState
	public String act = "0";
	
	@Action
	@Override
	public void save() {
		try {
			String sql = "SELECT f_fs_initdata('IAMT="+oamt1+";CAMT="+camt3+";DAMT="+damt2+";CYID="+currency+";ASTCODE="+act+";', "+AppUtils.getUserSession().getActsetid()+",'"+AppUtils.getUserSession().getUsercode()+"' );";
			//AppUtils.debug(sql);
			AppUtils.getServiceContext().fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void startAct() {
		start(true);
		showInfo();
	}
	
	@Action
	public void unStartAct() {
		start(false);
		showInfo();
	}
	
	@SaveState
	@Bind
	@Accessible
	public String startYear;
	
	@SaveState
	@Bind
	@Accessible
	public String startPeriod;

	private void start(boolean start) {
		String usercode = AppUtils.getUserSession().getUsercode();
		Long actsetid = AppUtils.getUserSession().getActsetid();
		
		String sql = "SELECT f_fs_actstart('usercode="+usercode+";actsetid="+actsetid+ ";start="+start+";startyear="+startYear+";startperiod="+startPeriod+"') AS result;";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String result = m.get("result").toString();
			MessageUtils.alert(result);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
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
//			MsgUtil.alert("请先保存主表");
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				//importDataText = importDataText.replaceAll("'", "''");
				Long actsetid = AppUtils.getUserSession().getActsetid();
				String callFunction = "f_fs_initdata";
				String args = "" + actsetid + ",'" + AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	/*
	 * 试算平衡表
	 */
	@Action
	public void showReport() {
		String url = AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/trialbalance_i.raq&actsetid="
			+ AppUtils.getUserSession().getActsetid();
		
		AppUtils.openWindow("trailbalance", url);
	}

	@Override
	public void grid_ondblclick() {
		//super.grid_ondblclick();
	}
	
	@Action
	public void changecurrency(){
		 String currency = AppUtils.getReqParam("currency");
		 this.currency = currency;
		 this.refresh();
	}

	
	@Action
	public void outData(){
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showPreview.jsp?raq=/finance/initdata.raq";
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}
	
	private String getArgs() {
		String args = "";
		args += "&actsetid=" + AppUtils.getUserSession().getActsetid() + "&cyid=" + currency;
		return args;
	}
	
	@Action
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}

	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 2);
	}*/
}
