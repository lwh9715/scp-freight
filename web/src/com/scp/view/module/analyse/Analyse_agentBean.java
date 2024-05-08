package com.scp.view.module.analyse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.analyse.analyse_agentBean", scope = ManagedBeanScope.REQUEST)
public class Analyse_agentBean extends GridFormView{
	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();

	@Bind
	@SaveState
	public String reporttype = "";
	
	@Bind
	@SaveState
	public boolean isReadOnly = false;
	
	@Bind
	private String datetype;
	
	@Bind
	@SaveState
	public Date jobDateFrom  = new Date();

	@Bind
	@SaveState
	public Date jobdateTo = new Date();
	
	@Bind
	@SaveState
	public Date ym = new Date();
	
	@Bind
	@SaveState
	public Date ymd = new Date();
	
	@Bind
	@SaveState
	public String argsClientele = "";
	
	@Bind
	@SaveState
	public String para = "";
	
	@Bind
	@SaveState
	public String language = "";
	
	@Bind
	@SaveState
	public String year;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			dtlIFrameClientele.setSrc("/pages/module/report/qry/dtlIFrameAgent.xhtml");
			update.markAttributeUpdate(dtlIFrameClientele, "src");
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
			year = sdf.format(new Date());
		}
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameClientele;
	
	@Override
	public void grid_onrowselect(){
		super.grid_onrowselect();
		this.pkVal = getGridSelectId();
		doServiceFindData();
		String infos = selectedRowData.getInfo();
		reporttype = selectedRowData.getFilename();
		isReadOnly = selectedRowData.getIsreadonly();
	}
	
	@Override
	public void grid_ondblclick(){
		Browser.execClientScript("reportJsVar.fireEvent('click')");
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.sysReportMgrService.sysReportDao
		.findById(this.pkVal);
		
	}
	
	@Action
	public void report() {
		if(reporttype == null || reporttype.isEmpty()){
			MessageUtils.alert("请先选择需要查询的统计表!");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM").format(ym));
		}else if("YMD Before".equals(datetype)){
			argsMap.put("jobdatetype","YMDBefore");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(ymd));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(jobDateFrom)+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}
//		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
		argsMap.put("clienteleid", argsClientele);
		argsMap.put("year", year);
		argsMap.put("userid",AppUtils.getUserSession().getUserid().toString());
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
//		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
//		String openUrl = AppUtils.getRptUrl()
//				+ "/reportJsp/"+(isReadOnly?"showReportReadOnly.jsp":"showReport.jsp")+"?raq=/static/rpa/" + reporttype;
//		AppUtils.openWindow("rpa_product", openUrl + arg);
		para = urlArgs2;
		language = AppUtils.getUserSession().getMlType().toString();
		Browser.execClientScript("run();");
//		AppUtils.openWindow("rpa_product", "lookattherisk.jsp" + "?" + "para=" + urlArgs2);
	}
}
