package com.scp.view.module.finance.books;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.dayBean", scope = ManagedBeanScope.REQUEST)
public class DayBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String yearfm;

	@Bind
	@SaveState
	public Date datefm;

	@Bind
	@SaveState
	public Date dateto;

	@Bind
	@SaveState
	public String actcodefm;

	@Bind
	@SaveState
	public String currency = "*";
	
	@Bind
	@SaveState
	public Long comid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			// 根据当前登录用户，找对应的帐套，过滤兑换率列表
			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			String year = "";
			String period = "";
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				year = m.get("year").toString();
				period = m.get("period").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.yearfm = year;
			
			datefm = new Date();
			dateto = new Date();
			//this.periodfm = period;
			//this.periodto = period;
			//this.currency = "*";
			this.comid = 1l;
			
			update.markUpdate(true, UpdateLevel.Data, "year");
//			update.markUpdate(true, UpdateLevel.Data, "period");
			update.markUpdate(true, UpdateLevel.Data, "currency");
		}
	}
	
	
	@Bind
	public UIIFrame showReportIframe;

	
	
	@Action
	public void showReportAjax() {
		showReport();
	}
	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		if(yearfm==null||comid==null||datefm==null||dateto==null||StrUtils.isNull(actcodefm)||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		String url ="";
		if(this.currency.equals("*")){
			url= AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/books/book_day.raq"
			+ getArgs();
		}else{
			url= AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/books/book_day_filter.raq"
			+ getArgs();
		}
		
		 showReportIframe.load(url);
//		AppUtil.openWindow("book_detail_base", url);
	}
	
	/*
	 * 预览报表
	 */
	@Action
	public void showReportGrid() {
		if(yearfm==null||comid==null||datefm==null||dateto==null||StrUtils.isNull(actcodefm)||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		String url ="";
		if(this.currency.equals("*")){
			url= "./daydetail.xhtml?type=grid"
			+ getArgs();
		}else{
			url= "./daydetail.xhtml?type=grid"
				+ getArgs();
		}
		 showReportIframe.load(url);
	}
	
	private String getArgs() {
		
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		String datfrom = f1.format(datefm);
		String datTo = f1.format(dateto);
		
		String args = "&year=" + yearfm  + "&datefm="
				+ datfrom + "&dateto=" + datTo + "&actcode=" + actcodefm
				+ "&currency="
				+ currency;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		return args;
	}
	
	// 币制
	@Bind(id="fscurrency")
    public List<SelectItem> getFscurrency() {
    	try {
    		List<SelectItem> rs = CommonComBoxBean.getComboxItems("d.code","d.code","_fs_currency AS d","","order by code");
			return rs;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	// 科目(code)
	@Bind(id = "actcode")
	public List<SelectItem> getActcode() {
		try {
			return CommonComBoxBean.getComboxItems("d.code",
					"d.code||'/'||d.name ", "fs_act AS d", "WHERE d.actsetid="
							+ AppUtils.getUserSession().getActsetid(),
					"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	
	/**
	 * 上一条
	 */
	@Action
	public void pageUp() {
		String sql = "SELECT code FROM fs_act where code <'"+actcodefm + "' AND actsetid="
							+ AppUtils.getUserSession().getActsetid() + " ORDER BY code DESC LIMIT 1";
		Map map;
		try {
			map = this.serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (MoreThanOneRowException e) {
			e.printStackTrace();
			this.alert("第一条!");
			return;
		}
		if(map == null || map.size() == 0) {
			
		}else {
			actcodefm = StrUtils.getMapVal(map,"code");
		}
		this.showReport();
	}

	/**
	 * 下一条
	 */
	@Action
	public void pageDown() {
		String sql = "SELECT code FROM fs_act where code >'"+actcodefm + "' AND actsetid="
						+ AppUtils.getUserSession().getActsetid() + " ORDER BY code LIMIT 1";
		Map map;
		try {
			map = this.serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (MoreThanOneRowException e) {
			e.printStackTrace();
			this.alert("最后一条!");
			return;
		}
		if(map == null || map.size() == 0) {
			
		}else {
			actcodefm = StrUtils.getMapVal(map,"code");
		}
		this.showReport();
	}
	
	
	@Action
	public void dayUp() {
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(datefm); 
		gc.add(5,-1); 
		datefm = gc.getTime();
		gc.setTime(dateto); 
		gc.add(5,-1); 
		dateto = gc.getTime();
		this.showReport();
	}

	
	@Action
	public void dayDown() {
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(datefm); 
		gc.add(5,1); 
		datefm = gc.getTime();
		gc.setTime(dateto); 
		gc.add(5,1); 
		dateto = gc.getTime();
		this.showReport();
	}
	
	@Action(id="yearfm",event="onchange")
	public void yearfmChange(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] datefmstrs = sdf.format(datefm).split("-");
		String[] datetostrs = sdf.format(dateto).split("-");
		if(yearfm == null || yearfm.isEmpty()){
			yearfm =  new SimpleDateFormat("yyyy").format(new Date());
		}
		StringBuffer sbfm = new StringBuffer();
		sbfm.append(yearfm);
		sbfm.append("-");
		sbfm.append(datefmstrs[1]);
		sbfm.append("-");
		sbfm.append(datefmstrs[2]);
		StringBuffer sbto = new StringBuffer();
		sbto.append(yearfm);
		sbto.append("-");
		sbto.append(datetostrs[1]);
		sbto.append("-");
		sbto.append(datetostrs[2]);
		try {
			datefm = sdf.parse(sbfm.toString());
			dateto = sdf.parse(sbto.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Action(id="datefm",event="onchange")
	public void datefmChange(){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		if(datefm != null){
//			String datefmstr = sdf.format(datefm);
//			if(datefmstr.isEmpty() || datefmstr.split("-") == null || datefmstr.split("-").length != 3){
//				datefm = new Date();
//			}
//		}else{
//			datefm = new Date();
//		}
//		datefm = datefm.after(dateto) ? dateto : datefm;
//		String[] datefmstrs = sdf.format(datefm).split("-");
//		String[] datetostrs = sdf.format(dateto).split("-");
//		yearfm = datefmstrs[0];
//		StringBuffer sb = new StringBuffer();
//		sb.append(datefmstrs[0]);
//		sb.append("-");
//		sb.append(datetostrs[1]);
//		sb.append("-");
//		sb.append(datetostrs[2]);
//		try {
//			dateto = sdf.parse(sb.toString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
	
	@Action(id="dateto",event="onchange")
	public void datetoChange(){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		if(dateto != null){
//			String datetostr = sdf.format(dateto);
//			if(datetostr.isEmpty() || datetostr.split("-") == null || datetostr.split("-").length != 3){
//				dateto = new Date();
//			}
//		}else{
//			dateto = new Date();
//		}
//		dateto = dateto.before(datefm) ? datefm : dateto;
//		String[] datefmstrs = sdf.format(datefm).split("-");
//		String[] datetostrs = sdf.format(dateto).split("-");
//		yearfm = datetostrs[0];
//		StringBuffer sb = new StringBuffer();
//		sb.append(datetostrs[0]);
//		sb.append("-");
//		sb.append(datefmstrs[1]);
//		sb.append("-");
//		sb.append(datefmstrs[2]);
//		try {
//			datefm = sdf.parse(sb.toString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
	
}
