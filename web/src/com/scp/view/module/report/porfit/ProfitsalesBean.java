package com.scp.view.module.report.porfit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UICardLayout;

import com.scp.util.AppUtils;
import com.scp.util.DateTimeUtil;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.profit.profitsalesBean", scope = ManagedBeanScope.REQUEST)
public class ProfitsalesBean extends GridView {
	@Bind
	@SaveState
	public String argsSale = "";

	@Bind
	@SaveState
	public String argsClientele = "";

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
    private UICardLayout info;

	
	@Action
    public void datetype_onchange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        if("YM".equals(datetype)){
	        	info.activeItem(0);
	        	ym = new Date();
			}else if("YMD Before".equals(datetype)){
				info.activeItem(1);
				ymd = df.parse(DateTimeUtil.getLastDay());
			}else if("YMD".equals(datetype)){
				info.activeItem(2);
				jobDateFrom  = df.parse(DateTimeUtil.getFirstDay());
				jobdateTo = df.parse(DateTimeUtil.getLastDay());
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	private String datetype = "YM";
	

	@Bind
	@SaveState
	public String reporttype = "";
	
	@Bind
	@SaveState
	public String currency = "";
	
	@Bind
	@SaveState
	public boolean isnos ;

	@SaveState
	@Bind
	private UIIFrame dtlIFrameClientele;
	@SaveState
	@Bind
	private UIIFrame dtlIFrameSale;

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			reporttype = AppUtils.getReqParam("reportfilename");
			//dtlIFrameClientele.setSrc("../qry/dtlIFrameClientele.xhtml");
			//update.markAttributeUpdate(dtlIFrameClientele, "src");
			dtlIFrameSale.setSrc("../qry/dtlIFrameSale.xhtml");
			update.markAttributeUpdate(dtlIFrameSale, "src");
			currency = "USD";
			isnos=false;
		}
	}

	@Action
	public void updateClientele(){
		////System.out.println("argsClientele:"+argsClientele);
		if(argsClientele!=null && argsClientele.isEmpty()){
			dtlIFrameClientele.setSrc("../qry/dtlIFrameClientele.xhtml");
			update.markAttributeUpdate(dtlIFrameClientele, "src");
		}
	}
	
	@Action
	public void report() {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("user", AppUtils.getUserSession().getUsercode());
		
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
		argsMap.put("currency", currency);
		argsMap.put("datetype", datetype);
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
		}
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
//		argsMap.clear();
		argsMap.put("salesid", argsSale);
		argsMap.put("clienteleid", argsClientele);		
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		//System.out.println(urlArgs2);
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/static/profit/" + reporttype;
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		
	}
}
