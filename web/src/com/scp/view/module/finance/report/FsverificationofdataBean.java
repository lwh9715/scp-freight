package com.scp.view.module.finance.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.report.fs_verification_of_dataBean", scope = ManagedBeanScope.REQUEST)
public class FsverificationofdataBean extends GridView {

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
	public String isarap = "AR";

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			try {
				this.qryMapAst.put("astypename", "往来单位");
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
			this.periodfm = period;
			this.yearto = year;
			this.periodto = period;
			
			update.markUpdate(true, UpdateLevel.Data, "yearfm");
			update.markUpdate(true, UpdateLevel.Data, "periodfm");
			update.markUpdate(true, UpdateLevel.Data, "yearto");
			update.markUpdate(true, UpdateLevel.Data, "periodto");
			
			gridAst.reload();
		}
	}
	/*
	 * 预览报表
	 */
	@Action
	public void showReport() {
		String[] ids = gridAst.getSelectedIds();
		if(ids != null && ids.length > 1){
			this.alert("往来单位一次只能选一个！");
			return;
		}
		
		String url = "";
		url = AppUtils.getRptUrl()
			+ "/reportJsp/showReport.jsp?raq=/finance/fs_verification_of_data.raq"
			+ getArgs();
		AppUtils.openWindow("fs_verification_of_data", url);
	}

	private String getArgs() {
		String astid = "-1";
		String[] ids = gridAst.getSelectedIds();
		if(ids != null && ids.length == 1){
			astid = ids[0];
		}
		
		String actcode = "2121";
		if(isarap.equals("AP")){
			actcode = "2121";
		}else{
			actcode = "1131";
		}
		Long corpidCurrent = AppUtils.getUserSession().getCorpidCurrent();
		String args = "&year=" + yearfm + "&periodfm=" + periodto + "&periodto=" + periodto + "&currency =*" 
		+"&actsetid=" + AppUtils.getUserSession().getActsetid()+"&actcode="+actcode+"&astid="+astid+"&user="+AppUtils.getUserSession().getUsercode()
		+"&corpid="+AppUtils.base64Encoder(String.valueOf(corpidCurrent));
		return args;
	}
	
	@Action(id="yearfm",event="onchange")
	public void yearfmChange(){
		if(yearfm == null || yearfm.isEmpty()){
			yearfm = new SimpleDateFormat("yyyy").format(new Date());
		}
		yearto = yearfm;
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
						.queryForList(sqlId, getQryClauseWhere1(qryMapAst),
								start, limit).toArray();
			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.doc.indtlBean.gridAst.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere1(qryMapAst));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	//根据对应的核算项目类别提取核算代码
	public Map getQryClauseWhere1(Map<String, Object> queryMapAst) {
		
		
		
		Map m = super.getQryClauseWhere(queryMapAst);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND t.actsetid = " + AppUtils.getUserSession().getActsetid();
		
		String astype = "*";
		String actcodefm = "1131";
		if("AP".equals(isarap))actcodefm = "2121";
		String filter = "\nAND exists(SELECT 1 FROm _fs_vchdtl_rpt x where x.actsetid = t.actsetid and x.astid = t.id AND x.isdelete = false AND x.year = "+yearfm+" AND x.actcode = '"+actcodefm+"' AND x.period BETWEEN "+periodfm+" AND "+periodto+")";
		filter = "";
		if("CD".equals(astype)){
			qry += "\nAND t.astypecode IN ('C','D')";
			m.put("filter1", "\nAND 1=2");
			m.put("filter2", "\nAND 1=1" + filter);
		}else{
			//qry += "\nAND t.astypecode = '"+astype+"'";
			m.put("filter1", "\nAND 1=1" + filter);
			m.put("filter2", "\nAND 1=2");
		}
		
		m.put("qry", qry);
		return m;
	}
	
	
	@Action
	public void clearQryKeyAst() {
		this.qryMapAst.clear();
		qryRefreshAst();
	}
	
	@Action
	public void qryRefreshAst() {
		this.gridAst.reload();
	}
	
}
