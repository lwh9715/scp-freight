package com.scp.view.module.report.boxs;

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

@ManagedBean(name = "pages.module.report.boxs.infoBean", scope = ManagedBeanScope.REQUEST)
public class InfoBean extends GridView {

	@Bind
	@SaveState
	public String argsSale = "";

	@Bind
	@SaveState
	public String argsCustomer = "";

	@Bind
	@SaveState
	public Date jobDateFrom = new Date();

	@Bind
	@SaveState
	public Date jobdateTo = new Date();

	@Bind
	@SaveState
	public String reporttype = "";

	@SaveState
	@Bind
	private UIIFrame dtlIFrameCustomer;
	@SaveState
	@Bind
	private UIIFrame dtlIFrameSale;
	
	@Bind
	@SaveState
	public boolean isnos ;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			reporttype = AppUtils.getReqParam("reportfilename");
			//dtlIFrameCustomer.setSrc("../qry/dtlIFrameCustomer.xhtml");
			//update.markAttributeUpdate(dtlIFrameCustomer, "src");

			dtlIFrameSale.setSrc("../qry/dtlIFrameSale.xhtml");
			update.markAttributeUpdate(dtlIFrameSale, "src");
			isnos=false;
		}
	}
	@Bind
    private UICardLayout info;
	@Bind
	@SaveState
	public Date ym = new Date();
	
	@Bind
	@SaveState
	public Date ymd = new Date();
	@Bind
	private String datetype = "YMD";
	
	@Action
    public void datetype_onchange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        if("YM".equals(datetype)){
	        	info.activeItem(0);
	        	ym = new Date();
			}
			else if("YMD".equals(datetype)){
				info.activeItem(2);
				jobDateFrom  = df.parse(DateTimeUtil.getFirstDay());
				jobdateTo = df.parse(DateTimeUtil.getLastDay());
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void updateCustomer(){
		////System.out.println("argsCustomer:"+argsCustomer);
		if(argsCustomer!=null && argsCustomer.isEmpty()){
			dtlIFrameCustomer.setSrc("../qry/dtlIFrameCustomer.xhtml");
			update.markAttributeUpdate(dtlIFrameCustomer, "src");
		}
	}
	@Action
	public void report() {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("user", AppUtils.getUserSession().getUsercode());
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM").format(ym));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd")
					.format(jobDateFrom)
					+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
		argsMap.clear();
		argsMap.put("salesid", argsSale);
		argsMap.put("customerid", argsCustomer);
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM").format(ym));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd")
					.format(jobDateFrom)
					+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
		}
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/static/boxs/" + reporttype;
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
	}

}
