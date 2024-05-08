package com.scp.view.module.finance.report;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.fs_vch_dtlBean", scope = ManagedBeanScope.REQUEST)
public class FsVchDtlBean extends GridView {

	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String periodfm;
	
	@Bind
	@SaveState
	public String periodto;
	
	@Bind
	@SaveState
	public String actcodefm;
	
	@Bind
	@SaveState
	public String actcodeto;
	
	

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

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
			this.year = year;
			this.periodfm = period;
			this.periodto = period;
			
			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "periodfm");
			update.markUpdate(true, UpdateLevel.Data, "periodto");
		}
	}
	
	
	@Bind
	@SaveState
	public String raqName;
	
	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		if(StrUtils.isNull(year)||StrUtils.isNull(year)||StrUtils.isNull(periodfm)||StrUtils.isNull(periodto)||StrUtils.isNull(actcodefm)||StrUtils.isNull(actcodeto)){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		
		String url = "";
		url = AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/"+raqName
			+ getArgs();
		AppUtils.openWindow("fs_fee", url);
	}

	private String getArgs() {
		String args = "&year=" + year + "&periodfm=" + periodfm+ "&periodto=" + periodto
		+ "&actcodefm=" + actcodefm + "&actcodeto=" + actcodeto + "&actsetid=" + AppUtils.getUserSession().getActsetid();
		return args;
	}

}
