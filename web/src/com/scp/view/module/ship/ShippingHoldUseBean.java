package com.scp.view.module.ship;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.shippingholduseBean", scope = ManagedBeanScope.REQUEST)
public class ShippingHoldUseBean extends GridView {
	
	@Bind
	@SaveState
	private String startDateJ;
	
	@Bind
	@SaveState
	private String endDateJ;
	
	@Bind
	@SaveState
	private String startDateA;
	
	@Bind
	@SaveState
	private String endDateA;
	
	@Bind
	@SaveState
	public Boolean iscare = true;
	
	@Bind
	public UIWindow searchWindow;
	
	
	@SaveState
	public Long customerid ;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String cus = AppUtils.getReqParam("customerid");
			if(StrUtils.isNull(cus)||cus.equals("null")){
				customerid = -1L;
			}else{
				customerid = Long.valueOf((String)cus );
			}
			}
			this.qryMap.put("customerid$", customerid);
		}
		
	
	@Action
	public void add() {
		String winId = "_edit_jobs";
		String url = "./jobsedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_jobs";
		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		// ATD日期和工作单日期区间查询
		String qry = StrUtils.getMapVal(m, "qry");
		//单子的收款状态  N:未收款, B :部分,A 已收款
		qry += " AND t.feestate IN ('N','B')";
		m.put("qry", qry);
		return m;
	}
	
	@Action
	public void addcopy(){
	
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0||ids.length>1){
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			FinaJobs jobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.parseLong(ids[0]));
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			String includfee = "true";
			String serialno = "";
			//String nos=
			//serviceContext.jobsMgrService.addcopy(ids[0],AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode(),jobs.getCorpid().toString(),jobs.getCorpidop().toString(),dateformat.format(new Date()),includfee,serialno);
			//MessageUtils.alert("复制生成的单号是:"+nos);
			//this.qryMap.clear();
			//this.qryMap.put("nos", nos);
			//refresh();
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void clearQryKey() {
		this.startDateJ = "";
		this.endDateJ = "";
		this.startDateA = "";
		this.endDateA = "";
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		super.clearQryKey();
	}
	
	@Action
	public void search(){
		this.searchWindow.show();
	}
	
	@Action
	public void clear(){
		this.clearQryKey();
	}
	
	@Action
	public void searchfee(){
		this.qryRefresh();
	}
	
	
	
//	@Action
//	public void showTrace(){
//		Long id = Long.valueOf(AppUtil.getReqParam("id"));
//		//如果没有操作说明就不弹窗
//		String sql = "SELECT 1 FROM bus_goodstrack WHERE fkid = "+id+" LIMIT 1";
//		try {
//			Map m = serviceContext.jobsMgrService.daoIbatisTemplate
//					.queryWithUserDefineSql4OnwRow(sql);
//			String sql2 = "SELECT f_bus_goodstrack_get('id="+id+";type=P')AS trace";
//			
//			Map m2 = serviceContext.jobsMgrService.daoIbatisTemplate
//			.queryWithUserDefineSql4OnwRow(sql2);
//			trace = (String) m.get("trace");
//			this.update.markUpdate(UpdateLevel.Data, "trace");
//			showDtlWindow.show();
//		} catch (Exception e) {
//			showDtlWindow.close();
//		}
//		
//	}
	
	@Bind
	@SaveState
	public String trace;
	
	 @Bind
	 public UIWindow showDtlWindow;
	 
}
	    
	
