package com.scp.view.module.finance.books;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.books.detailBean", scope = ManagedBeanScope.REQUEST)
public class DetailBean extends GridView {

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String yearfm;

	@Bind
	@SaveState
	public String periodfm;

	@Bind
	@SaveState
	public String yearto;

	@Bind
	@SaveState
	public String periodto;

	@Bind
	@SaveState
	public String actcodefm;

	@Bind
	@SaveState
	public String currency = "*";
	
	
	@Bind
	@SaveState
	public String astdesc = "*";
	
	
	@Bind
	@SaveState
	public Boolean filterByInit = false;
	
	
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
			this.yearto = year;
			this.periodfm = period;
			this.periodto = period;
			//this.currency = "*";
			this.comid = 1l;
			
			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "period");
			update.markUpdate(true, UpdateLevel.Data, "currency");
		}
	}
	
	
	@Bind
	public UIIFrame showReportIframe;

	
	
	@Action
	public void showReportAjax() {
		showReport();
	}
	
	@Action
	public void showAst() {
		String winId = "chooseAst";
		String url = AppUtils.getContextPath()+"/pages/module/finance/initconfig/itemschooser.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	
	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		if(yearfm==null||yearto==null||comid==null||periodfm==null||periodto==null||StrUtils.isNull(actcodefm)||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		
		if(yearfm!=null ||!"".equals(yearfm)|| yearto!=null ||!"".equals(yearto)){
			if(!(yearfm).equals(yearto)){
				alert("不能跨年度查询!");
				return;
			}
		}
		
		if(periodfm !=null &&!"".equals(periodfm) && periodto !=null &&!"".equals(periodto)){
			if(Long.valueOf(periodfm).compareTo(Long.valueOf(periodto))==1){
				alert(" 起始日期要在截止日期之前!");
				return;
			}
		}
		String url ="";
		if(this.currency.equals("*")){
			url= AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/books/book_detail_base.raq"
			+ getArgs();
		}else{
			url= AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/books/book_detail_filter.raq"
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
	
		if(yearfm==null||yearto==null||comid==null||periodfm==null||periodto==null||StrUtils.isNull(actcodefm)||currency==null){
			MessageUtils.alert("检索条件不为空!请检查");
			return;
		}
		
		if(yearfm!=null ||!"".equals(yearfm)|| yearto!=null ||!"".equals(yearto)){
			if(!(yearfm).equals(yearto)){
				alert("不能跨年度查询!");
				return;
			}
		}
		
		if(periodfm !=null &&!"".equals(periodfm) && periodto !=null &&!"".equals(periodto)){
			if(Long.valueOf(periodfm).compareTo(Long.valueOf(periodto))==1){
				alert(" 起始日期要在截止日期之前!");
				return;
			}
		}
		String url ="";
		if(this.currency.equals("*")){
			url= "./detailgrid.xhtml?type=grid"
			+ getArgs();
		}else{
			url= "./detailgrid.xhtml?type=grid"
				+ getArgs();
		}
		 showReportIframe.load(url);
	}
	
	
	private String getArgs() {
		String args = "&year=" + yearfm  + "&periodfm="
				+ periodfm + "&periodto=" + periodto + "&actcode=" + actcodefm
				+ "&currency="+ currency 
				+ "&filterByInit=" + filterByInit
				;
		args+="&actsetid="+AppUtils.getUserSession().getActsetid();
		args+="&comid="+this.comid;
		args+="&astid="+this.astidHide;
		args+="&basepath="+AppUtils.getBasePath();
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
					"d.code||'/'||d.name ", "fs_act AS d", "WHERE isdelete = false and d.actsetid="
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
	
	
	
	@Bind
	public UIWindow astWindow;
	
	@Bind
	@SaveState
	public String astidHide="-1";
	
	//凭证详细编辑弹窗界面第一次加载，用于显示如果是选择了勾选往来单位的科目，显示所选择的核算项目code/name
	@Bind
	@SaveState
	public String astdescShow;
	
	@Action
	public void gridAst_ondblclick() {
		String id = gridAst.getSelectedIds()[0];
		String sql = "SELECT (code||'/'||name) AS astdesc FROM fs_ast WHERE isdelete = FALSE AND id = " + id;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			astdescShow = String.valueOf(m.get("astdesc"));
			
			astidHide = id;
			
			this.update.markUpdate(UpdateLevel.Data, "astidHide");
			this.update.markUpdate(UpdateLevel.Data, "astdescShow");
			Browser.execClientScript("restRow();showReportBtn.fireEvent('click');");
			//this.astWindow.close();
		} catch (Exception e) {
			astdescShow = "";
			//e.printStackTrace();
		}
	}
	
	@Bind
	public UIDataGrid gridAst;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapAst = new HashMap<String, Object>();

	@Bind(id = "gridAst", attribute = "dataProvider")
	protected GridDataProvider getDataProvider1() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.doc.indtlBean.gridAst.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere1(qryMapAst , start , limit)).toArray();
			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.doc.indtlBean.gridAst.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere1(qryMapAst , start , limit));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	@Bind
	@SaveState
	public String astype = "*";
	
	
	
	@Action
	public void showAstChooseAjax() {
		astype = AppUtils.getReqParam("astype");
		astWindow.show();
		qryRefreshAst();
	}
	
	//根据对应的核算项目类别提取核算代码
	public Map getQryClauseWhere1(Map<String, Object> queryMapAst, int start, int limit) {
		Map m = super.getQryClauseWhere(queryMapAst);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND t.actsetid = " + AppUtils.getUserSession().getActsetid();
		
		//String filter = "\nAND exists(SELECT 1 FROm _fs_vchdtl_rpt x where x.actsetid = t.actsetid and x.astid = t.id AND x.isdelete = false AND x.year = "+yearfm+" AND x.actcode = '"+actcodefm+"' AND x.period BETWEEN 0 AND "+periodto+")";
//		String filter = "\nAND exists(SELECT 1 FROm _fs_vchdtl_rpt x where x.actsetid = t.actsetid and x.astid = t.id AND x.isdelete = false AND x.actcode = '"+actcodefm+"' AND x.oamt > 0)";
//		if(!this.currency.equals("*")){//如果币制不为*，按币制提取有凭证的核算项目
//			filter = "\nAND exists(SELECT 1 FROm _fs_vchdtl_rpt x where x.actsetid = t.actsetid and x.astid = t.id AND x.isdelete = false AND x.actcode = '"+actcodefm+"' AND x.oamt > 0 AND UPPER(x.cyid) = UPPER('"+currency+"'))";
//		}
		String filter = "\nAND exists(SELECT 1 FROm f_fs_ast_filter('actsetid="+AppUtils.getUserSession().getActsetid()+";actcode="+actcodefm+";cyid="+currency+";filterByInit="+filterByInit+";astypecode="+astype+"') x WHERE x.id = t.id)";
		
		if("CD".equals(astype)){
			qry += "\nAND t.astypecode IN ('C','D')";
			m.put("filter1", "\nAND 1=2");
			m.put("filter2", "\nAND 1=1" + filter);
		}else{
			qry += "\nAND t.astypecode = '"+astype+"'";
			m.put("filter1", "\nAND 1=1" + filter);
			m.put("filter2", "\nAND 1=2");
		}
		
		m.put("qry", qry);
		m.put("limit", limit+"");
		m.put("start", start+"");
		return m;
	}
	
	@Action
	public void doChangeAstGridPageSize() {
		String pageStr = (String)AppUtils.getReqParam("page");
		if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
			//alert("pageStr:"+pageStr);
			Integer page = Integer.parseInt(pageStr);
			this.gridAst.setRows(page);
			gridLazyLoad = false;
			this.gridAst.rebind();
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}
	
	@Action
	public void qryRefreshAst() {
		this.gridAst.reload();
	}
	
	@Action
	public void clearQryKeyAst() {
		this.qryMapAst.clear();
		qryRefreshAst();
	}
	
	@Action(id="yearfm",event="onchange")
	public void yearfmChange(){
		if(yearfm == null || yearfm.isEmpty()){
			yearfm = new SimpleDateFormat("yyyy").format(new Date());
		}
		yearto = yearfm;
	}
}
