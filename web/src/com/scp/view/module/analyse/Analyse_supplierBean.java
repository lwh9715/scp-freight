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
@ManagedBean(name = "pages.module.analyse.analyse_supplierBean", scope = ManagedBeanScope.REQUEST)
public class Analyse_supplierBean extends GridFormView{
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
	public String argsCarrierid = "";
	
	@Bind
	@SaveState
	public String argsTruckid = "";
	
	@Bind
	@SaveState
	public String argsCustomid = "";
	
	@Bind
	@SaveState
	public String argsAirlineid = "";
	
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
			dtlIFrameCarrier.setSrc("/pages/module/report/qry/dtlIFrameCarrier.xhtml");
			update.markAttributeUpdate(dtlIFrameCarrier, "src");
			dtlIFrameTruck.setSrc("/pages/module/report/qry/dtlIFrameTruck.xhtml");
			update.markAttributeUpdate(dtlIFrameTruck, "src");
			dtlIFrameCustom.setSrc("/pages/module/report/qry/dtlIFrameCustom.xhtml");
			update.markAttributeUpdate(dtlIFrameCustom, "src");
			dtlIFrameAirline.setSrc("/pages/module/report/qry/dtlIFrameAirline.xhtml");
			update.markAttributeUpdate(dtlIFrameAirline, "src");
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
			year = sdf.format(new Date());
		}
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCarrier;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameTruck;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCustom;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameAirline;
	
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
		argsMap.put("argsCarrierid", argsCarrierid);
		argsMap.put("argsTruckid", argsTruckid);
		argsMap.put("argsCustomid", argsCustomid);
		argsMap.put("argsAirlineid", argsAirlineid);
		argsMap.put("userid",AppUtils.getUserSession().getUserid().toString());
		argsMap.put("year", year);
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		para = urlArgs2;
		language = AppUtils.getUserSession().getMlType().toString();
		Browser.execClientScript("run();");
	}
}
