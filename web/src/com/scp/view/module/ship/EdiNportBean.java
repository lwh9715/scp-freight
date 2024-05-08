package com.scp.view.module.ship;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;


@ManagedBean(name = "pages.module.ship.edi_nportBean", scope = ManagedBeanScope.REQUEST)
public class EdiNportBean extends GridView {
	
	@Bind
	@SaveState
	private String startDateJ;
	
	@Bind
	@SaveState
	private String endDateJ;
	
	@Bind
	@SaveState
	private String startDateA;
	
	@Bind
	@SaveState
	private String endDateA;
	
	@Bind
	public UIWindow searchWindow;
	
	@Bind
	UIWindow cfgUrlWindow;
	
	@Bind
	@SaveState
	private String url;
	
	@SaveState
	public String jobid;
	
	@Bind
	@SaveState
	private String fileName;
	
	@Bind
	@SaveState
	private String contentType;
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_jobs";
		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void exportEDI() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!Only one!");
			return;
		}
		jobid = ids[0];
		
		this.fileName = "NPORT_" + jobid + ".txt";
		this.contentType = "text/plain";
	}
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() throws Exception {
		Long userid = AppUtils.getUserSession().getUserid();
		String sql = "SELECT f_edi_nbport('jobid=" + jobid + ";userid=" + userid + "') AS edi;";
		Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String ediContent = m.get("edi").toString();
		
		InputStream input = new ByteArrayInputStream(ediContent.getBytes());
		return input;
    }
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		// ATD日期和工作单日期区间查询
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND t.jobdate BETWEEN '" + (StrUtils.isNull(startDateJ) ? "0001-01-01" : startDateJ)
				+ "' AND '" + (StrUtils.isNull(endDateJ) ? "9999-12-31" : endDateJ) + "'";
		
		qry += "\nAND ((t.atd BETWEEN '" + (StrUtils.isNull(startDateA) ? "0001-01-01" : startDateA)
				+ "' AND '" + (StrUtils.isNull(endDateA) ? "9999-12-31" : endDateA) + "')"
				+ "\n OR t.atd IS NULL)";
		m.put("qry", qry);
		
		return m;
	}
	
	@Override
	public void clearQryKey() {
		this.startDateJ = "";
		this.endDateJ = "";
		this.startDateA = "";
		this.endDateA = "";
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		super.clearQryKey();
	}
	
	@Action
	public void search(){
		this.searchWindow.show();
	}
	
	@Action
	public void clear(){
		this.clearQryKey();
	}
	
	@Action
	public void searchfee(){
		this.qryRefresh();
	}
	
}
