package com.scp.view.module.finance.books;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.otherBean", scope = ManagedBeanScope.REQUEST)
public class OtherBean extends GridView {

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
	public String astUserId;
	
	@Bind
	@SaveState
	public String astDeptId;

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
		if(yearfm==null||datefm==null||dateto==null||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		String url ="";
		if(this.currency.equals("*")){
			url= AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/books/book_other.raq"
			+ getArgs();
		}else{
			url= AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/books/book_other_filter.raq"
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
		if(yearfm==null||datefm==null||dateto==null||currency==null){
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
		
		String astdeptid = "0";
		String astuserid = "0";
		if(!StrUtils.isNull(astDeptId))astdeptid = astDeptId;
		if(!StrUtils.isNull(astUserId))astuserid = astUserId;
		
		String args = "&year=" + yearfm  + "&datefm="
				+ datfrom + "&dateto=" + datTo + "&astdeptid=" + astdeptid + "&astuserid=" + astuserid
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
	
	// 往来单位部门(astdept)
	@Bind(id = "astDept")
	public List<SelectItem> getAstDept() {
		try {
			return CommonComBoxBean.getComboxItems("y.id",
					"y.code||'/'||y.name ", "fs_ast y , fs_astype z", "WHERE y.astypeid = z.id AND y.isdelete = FALSE AND z.name = '部门' AND y.actsetid = " + AppUtils.getUserSession().getActsetid(),
					"ORDER BY y.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	// 往来单位部门(astuser)
	@Bind(id = "astUser")
	public List<SelectItem> getAstUser() {
		try {
			String parentid = "0";
			if(!StrUtils.isNull(astDeptId))parentid = astDeptId;
			
			return CommonComBoxBean.getComboxItems("y.id",
					"y.name ", "fs_ast y , fs_astype z"
					, "WHERE y.astypeid = z.id AND y.isdelete = FALSE " +
							"\n	AND ("+parentid+" = 0 OR COALESCE(y.parentid,0) = "+parentid+")" +
							"\n	AND z.name = '职员' " +
							"\n	AND EXISTS(SELECT 1 FROM fs_vchdtl xx WHERE xx.astid = y.id AND xx.isdelete = FALSE AND xx.actsetid = y.actsetid) " +
							"\n	AND y.actsetid = " + AppUtils.getUserSession().getActsetid(),
					"ORDER BY y.name");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	
	@Action
	public void refreshUserComboAjax(){
		update.markUpdate(UpdateLevel.Data, "astUserId");
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
}
