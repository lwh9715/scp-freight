package com.scp.view.module.finance.initconfig;

import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.initdatadtlBean", scope = ManagedBeanScope.REQUEST)
public class InitDataDtlBean extends GridFormView {

	@SaveState
	@Accessible
	public String periodid;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String info;
	
	@Bind
	@SaveState
	public String id = "-1";
	
	@Bind
	@SaveState
	public String type = "B";
	
	@Bind
	@SaveState
	public String currency = "CNY";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			id = AppUtils.getReqParam("id");
			type = AppUtils.getReqParam("type");
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			this.showInfo();
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
		}
	}

	private void showInfo() {
		String actcode = AppUtils.getReqParam("code");
		act = actcode;
		if("B".equals(type)) {
			info = "往来单位";
		}else if("C".equals(type)) {
			info = "部门";
		}else if("E".equals(type)) {
			info = "事业部";
		}else {
			info = "职员";
		}
		this.info = actcode + ":[" + info + "]";
		update.markUpdate(true, UpdateLevel.Data, "info");
	}

	@Override
	protected void doServiceFindData() {
		
	}
	
	@Bind
	@SaveState
	public String astcode = "0";
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

	@Override
	protected void doServiceSave() {
		
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND c.actsetid = " + AppUtils.getUserSession().getActsetid();
		qry += "\nAND yy.code = '" + type + "'";
		qry += "\nAND c.id = " + id;
		m.put("qry", qry);
		
		m.put("cyid", currency);
		m.put("filter", filter);
		return m;
	}
	
	@Action
	public void saveDtl() {
		try {
			String sql = "SELECT f_fs_initdatadtl('ASTCODE="+astcode+";IAMT="+oamt1+";CAMT="+camt3+";DAMT="+damt2+";CYID="+currency+";ACT="+act+";', "+AppUtils.getUserSession().getActsetid()+",'"+AppUtils.getUserSession().getUsercode()+"' );";
			//AppUtils.debug(sql);
			AppUtils.getServiceContext().fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void clear() {
		try {
			filter = "";
			oamt1 = "0";
			String sql = "SELECT f_fs_initdatadtl('TYPE=clear;ASTCODE="+astcode+";IAMT="+oamt1+";CAMT="+camt3+";DAMT="+damt2+";CYID="+currency+";ACT="+act+";', "+AppUtils.getUserSession().getActsetid()+",'"+AppUtils.getUserSession().getUsercode()+"' );";
			//AppUtils.debug(sql);
			AppUtils.getServiceContext().fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
			this.refreshForm();
			update.markUpdate(true, UpdateLevel.Data, "oamt1");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@SaveState
	public String filter = "";
	
	@Action
	public void filterAmt0() {
		filter = "\nAND EXISTS(SELECT 1 FROM _fs_vchdtl zz WHERE zz.astid = xx.id AND zz.period = 0 AND COALESCE(zz.oamt,0) !=0 AND zz.cyid = '"+currency+"' AND zz.actsetid = xx.actsetid AND zz.srctype = 'I' AND zz.year = (SELECT yy.startyear FROM fs_actset yy WHERE yy.id = zz.actsetid))";
		this.grid.reload();
	}
	
	@Action
	public void clearAllData() {
		try {
			String sql = "SELECT 'UPDATE fs_vchdtl set isdelete = true where id = '||d.id||';' AS sql FROM _fs_vchdtl d where actsetid = "+AppUtils.getUserSession().getActsetid()+" and srctype = 'I' and actcode = '"+act+"' and isdelete = false;";
			//AppUtils.debug(sql);
			List<Map> lists = AppUtils.getServiceContext().fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
			Vector<String> vector = new Vector<String>();
			for (Map map : lists) {
				vector.add(StrUtils.getMapVal(map, "sql"));
				AppUtils.getServiceContext().fsAstMgrService.daoIbatisTemplate.updateWithUserDefineSql(StrUtils.getMapVal(map, "sql"));
			}
			//AppUtils.debug(vector);
//			AppUtil.getServiceContext().fsAstMgrService.fsAstDao.executeQueryBatchByJdbc(vector);
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
				String callFunction = "f_fs_initdatadtl";
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

	@Action
	public void exportLinks() {
		String url = "";
		url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport_master.jsp?raq=/finance/fs_ast.raq&type="+type+"&actcode="
				+ act + "&actsetid="
				+AppUtils.getUserSession().getActsetid();

		AppUtils.openWindow("actdtl", url);
	}
	
	@Action
	public void exportResultLinks() {
		String url = "";
		url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport_master.jsp?raq=/finance/fs_ast_result.raq&type="+type+"&actcode="
				+ act + "&actsetid="
				+AppUtils.getUserSession().getActsetid();

		AppUtils.openWindow("actdtlresult", url);
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
