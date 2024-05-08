package com.scp.view.module.ship;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.ship.billistelrelBean", scope = ManagedBeanScope.REQUEST)
public class BillistelrelBean extends EditGridView {

	@SaveState
	@Accessible
	public String periodid;

	@Bind
	@SaveState
	private boolean isdate=false;
	
	@Bind
	@SaveState
	private String dates;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private boolean isjob;
	
	@Bind
	@SaveState
	private String numbers;
	
	@Bind
	@SaveState
	private boolean isrefno;
	
	@Bind
	@SaveState
	private boolean isbook;
	
	@Bind
	@SaveState
	private boolean ishbl;
	
	@Bind
	@SaveState
	private boolean ismbl;
	
	@Bind
	@SaveState
	private boolean issendc;
	
	@Bind
	@SaveState
	private boolean iscustoms;
	
	@Bind
	@SaveState
	private boolean isreceipt;
	@Bind
	@SaveState
	private boolean isinvoice;
	@Bind
	@SaveState
	private boolean isnumber;
	@Bind
	@SaveState
	private boolean isno;
	@Bind
	@SaveState
	private boolean istitle;
	@Bind
	@SaveState
	private boolean iscommission;
	@Bind
	@SaveState
	private boolean isbill;	
	@Bind
	@SaveState
	private boolean islink;	
	
	@Bind
	@SaveState
	private String portss;
	@Bind
	@SaveState
	private String allcustomer="";
	@Bind
	@SaveState
	private long customerid=0;
	@Bind
	@SaveState
	private String usergetmbl;
	@Bind
	@SaveState
	private String userreleasembl;
	
	@Bind
	@SaveState
	private String operationid;
	
	@Bind
	@SaveState
	private String callid;
	
	@Bind
	@SaveState
	private String marketid;
	
	@Bind
	@SaveState
	private String priceuserid;
	
	@Bind
	@SaveState
	private String financeid;
	
	@Bind
	@SaveState
	public String iscl;
	
	@Bind
	@SaveState
	private String isbustruck="";
	
	@Bind
	@SaveState
	private String isbuscustoms="";
	
	@Bind
	@SaveState
	private String isvgm="";
	
	@Bind
	@SaveState
	private String isso="";
	
	@Bind
	@SaveState
	private String isfeeding="";
	
	@Bind
	@SaveState
	private String isbltype="";
	
	@Bind
	@SaveState
	private String istelrelv="N";
	
	@Bind
	@SaveState
	private String vessel="";
	@Bind
	@SaveState
	private String voyage="";
	@Bind
	@SaveState
	private String destination="";
	@Bind
	@SaveState
	private String ldtype="";
	@Bind
	@SaveState
	private String corpid = "" ;
	@Bind
	@SaveState
	private String deptid = "" ;
	@Bind
	@SaveState
	private String corpidop = "" ;
	@Bind
	@SaveState
	private String  saleid="" ;
	@Bind
	@SaveState
	private long sale=0 ;
	@Bind
	@SaveState
	private String port="";
	
	@Bind
	@SaveState
	private String filsid;
	
	@Bind
	@SaveState
	private boolean isprofit;
	
	@Bind
	@SaveState
	private String profit_currency;
	@Bind
	@SaveState
	private String profit_compare;
	
	@Bind
	@SaveState
	private BigDecimal profit ;
	
	@Bind
	@SaveState
	private String qryispaycheck = "" ;
	@Bind
	@SaveState
	private String qryispayagree = "Y" ;
	
	@Bind
	@SaveState
	private String idcode = "" ;
	
	@Bind
	@SaveState
	private String billdatefrom = getdatestar();
	
	@Bind
	@SaveState
	private String billdateto = getdateto();
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}

	@Override
	protected void update(Object companys) {

	}

	@Override
	public void save() {
		String goodscode = "";
		try {
			serviceContext.busShippingMgrService.updateBatchEditGrid(modifiedData);
			MessageUtils.alert("OK");
			//refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void editGrid_ondblclick() {

	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		
		//初始化
		String dynamicClauseWhere = "\nAND 1=1 ";
		
		//电放
		if(!StrUtils.isNull(istelrelv)){
			dynamicClauseWhere +="\nAND istelrel = "
								+(istelrelv.equals("Y")?"TRUE":"FALSE")+"";
		}
		//审核
		if(!StrUtils.isNull(qryispaycheck)){
			dynamicClauseWhere +="\nAND ispaycheck = "+(qryispaycheck.equals("Y")?"TRUE":"FALSE");
		}
		//付款
		if(!StrUtils.isNull(qryispayagree)){
			dynamicClauseWhere +="\nAND ispayagree = "+(qryispayagree.equals("Y")?"TRUE":"FALSE");
		}
		//识别码
		if(idcode!=""){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user s WHERE s.id = t.saleid AND s.idcode ILIKE '%"+idcode+"%')";
		}
		//提单日期（工作单录入时间）
		if(!StrUtils.isNull(billdatefrom) || !StrUtils.isNull(billdateto)){
			dynamicClauseWhere += "\nAND t.inputtime::DATE BETWEEN '"+ (StrUtils.isNull(billdatefrom) ? "0001-01-01" : billdatefrom)+ "' AND '"+ (StrUtils.isNull(billdateto) ? "9999-12-31" : billdateto)+ "'";
		}
		
		String args = "paychecktimestart="+billdatefrom+";paychecktimend="+billdateto+";hblno="+queryMap.get("hbl")+";mblno="+queryMap.get("mblno")+";idcode="+idcode+";jobno="+queryMap.get("nos");
		args += ";telrel="+(istelrelv.equals("Y")?"TRUE":"FALSE")+";ispaycheck="+(qryispaycheck.equals("Y")?"TRUE":"FALSE")+";ispayagree="+(qryispayagree.equals("Y")?"TRUE":"FALSE");
		
		m.put("args", args);
		
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		
//		String corpfilter = " AND EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")";
//		m.put("corpfilter", corpfilter);
		m.put("userid" , AppUtils.getUserSession().getUserid());
		return m;
	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
	}
	
	@Action
	public void clear() {
		this.clearQryKey();
	}
	
	
	
	@Override
	public void clearQryKey() {
		isdate=false;
		dates="";
		dateastart="";
		dateend="";
		isjob=false;
		isrefno=false;
		isbook=false;
		ishbl=false;
		ismbl=false;
		issendc=false;
		iscustoms=false;
		isreceipt=false;
		isinvoice=false;
		isnumber=false;
		isno=false;
		istitle=false;
		iscommission=false;
		isbill=false;
		numbers="";
		allcustomer="";
		customerid=0;
		vessel="";
		voyage="";
		portss="";
		port="";
		destination="";
		sale=0;
		ldtype="";
		saleid="";
		corpid="";
		corpidop="";
		deptid="";
		usergetmbl="";
		userreleasembl="";
		filsid = "";
		operationid = "";
		callid = "";
		marketid = "";
		priceuserid = "";
		financeid = "";
		iscl = "3";
		isbustruck = "";
		isbuscustoms = "";
		isvgm = "";
		isso = "";
		isfeeding = "";
		isbltype = "";
		istelrelv = "";
		qryispaycheck = "";
		qryispayagree = "";
		Browser.execClientScript("$('#sales_input').val('')");
		Browser.execClientScript("$('#inputers_input').val('')");
		Browser.execClientScript("$('#usergetmblflex_input').val('')");
		Browser.execClientScript("$('#userreleasemblflex_input').val('')");
		Browser.execClientScript("$('#fils_input').val('')");
		Browser.execClientScript("$('#operation_input').val('')");
		Browser.execClientScript("$('#call_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		Browser.execClientScript("$('#port_input').val('')");
		Browser.execClientScript("$('#market_input').val('')");
		Browser.execClientScript("$('#finance_input').val('')");
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		this.update.markUpdate(UpdateLevel.Data, "startDateATA");
		this.update.markUpdate(UpdateLevel.Data, "endDateATA");
		this.update.markUpdate(UpdateLevel.Data, "startDateETD");
		this.update.markUpdate(UpdateLevel.Data, "endDateETD");
		this.update.markUpdate(UpdateLevel.Data, "startDateATD");
		this.update.markUpdate(UpdateLevel.Data, "endDateATD");
		if (qryMap != null) {
			qryMap.clear();
		}
	}

	@Action
	public void clearAndClose() {
		this.clearQryKey();
		Browser.execClientScript("searchWindowJsVar.close()");
	}
	
	public String getdateto(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String str = sdf.format(date);
        return str;
	}
	
	public String getdatestar(){
		Date date = new Date();
		date.setYear(date.getYear()-1);
		date.setMonth(date.getMonth()-1); 
		date.setDate(date.getDate()+1); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String str = sdf.format(date);
        return str;
	}
	
	public static String parseTime(String datdString) {
	    datdString = datdString.replace("GMT", "").replaceAll("\\(.*\\)", "");
	    //将字符串转化为date类型，格式2016-10-12
	    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
	    Date dateTrans = null;
	    try {
	        dateTrans = format.parse(datdString);
	        return new SimpleDateFormat("yyyy-MM-dd").format(dateTrans);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return datdString;

	}
}
