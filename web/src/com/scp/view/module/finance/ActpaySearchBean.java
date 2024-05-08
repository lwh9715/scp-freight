package com.scp.view.module.finance;

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

import com.scp.base.ApplicationConf;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.actpaysearchBean", scope = ManagedBeanScope.REQUEST)
public class ActpaySearchBean extends GridView {

	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();

	@Bind
	public UIDataGrid arapfeel;
	
	@Bind
	@SaveState
	public Long actpayrecid =  -1l;
	
	@Bind
	@SaveState
	public String search;
	
	@Bind
	@SaveState
	private Long branchComs = null;
	
	@Bind
	@SaveState
	private String isvch = null;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
				clear();
			//this.actpayrecChooseBean.qry(null,null,null,null,-1l,null,null,null,null,null);
		}
	}

	@Override
	public void grid_onrowselect() {
		try {
			String id = grid.getSelectedIds()[0];
			 actpayrecid = Long.parseLong(id.split(",")[0]);
			 update.markUpdate(true, UpdateLevel.Data, "actpayrecid");
			 arapfeel.reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// AppUtil.openWindow("_newRP",
		// "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
	}

	@Bind(id = "arapfeel", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				//if(qryMapShip.isEmpty()) return null;
				String sqlId = "pages.module.finance.actpayrecBean.arapfeel.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				//if(qryMapShip.isEmpty()) return 0;
				String sqlId = "pages.module.finance.actpayrecBean.arapfeel.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
//		Map m = super.getQryClauseWhere(queryMap);
//		String qry = StrUtils.getMapVal(m, "qry");
//			qry += "\nAND t.id = " + actpayrecid;
		Map m = new HashMap();
		String qry = "\n t.id = " + actpayrecid;
			
		String qry1 = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x,sys_corporation y WHERE x.corpid = y.corpid AND y.id = b.corpid AND y.isdelete = false AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())qry += qry1;//非saas模式不控制
		if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))qry += qry1;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
		
			
			
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	@SaveState
	private String nos;
	
	@Bind
	@SaveState
	private String actpayrecno;
	
	@Bind
	@SaveState
	private Date actpayrecdate;
	
	@Bind
	@SaveState
	private Date actpayrecdateEnd;
	
	@Bind
	@SaveState
	private Date rpdate;
	
	@Bind
	@SaveState
	private Date rpdateEnd;
	
	@Bind
	@SaveState
	private Long amount;
	
	@Bind
	@SaveState
	private String chequeno;
	
	@Bind
	@SaveState
	private String sales;
	
	@Bind
	@SaveState
	public String cntno;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		
		String actpayrecdatesql = "";
		if(actpayrecdate==null&&actpayrecdateEnd==null){
			actpayrecdatesql = "";
		}else{
			actpayrecdatesql = "\nAND actpayrecdate::DATE BETWEEN '" + actpayrecdate + "' AND '" + actpayrecdateEnd+ "'";
		}
		String rpdatesql = "";
		if(rpdate==null&&rpdateEnd==null){
			rpdatesql = "";
		}else{
			rpdatesql = "\nAND rpdate::DATE BETWEEN '" + rpdate + "' AND '" + rpdateEnd  + "'";
		}
		String isvchsql = "";
		if(isvch==null){
			isvchsql = "";
		}else if(isvch.equals("T")){
			isvchsql = "\nAND EXISTS(SELECT 1 FROM fs_vch x WHERE x.srcid = actpayrecid)";
		}else if(isvch.equals("F")){
			isvchsql = "\nAND NOT EXISTS(SELECT 1 FROM fs_vch x WHERE x.srcid = actpayrecid)";
		}
		String jobnosql = "";
		if(!StrUtils.isNull(nos)){
			jobnosql += "\nAND EXISTS(SELECT 1 FROM fina_jobs x , fina_arap y , fina_actpayrecdtl z WHERE x.id = y.jobid AND y.id = z.arapid AND z.actpayrecid = t.id AND x.nos ILIKE '%"+StrUtils.getSqlFormat(nos)+"%')";
		}
		if(!StrUtils.isNull(sales)){
			jobnosql += "\nAND EXISTS(SELECT 1 FROM fina_jobs x , fina_arap y , fina_actpayrecdtl z WHERE x.id = y.jobid AND y.id = z.arapid AND z.actpayrecid = t.id AND x.sales ILIKE '%"+StrUtils.getSqlFormat(sales)+"%')";
		}
		
		if(!StrUtils.isNull(cntno)){
			qry += "\n AND EXISTS (SELECT 1 FROM fina_actpayrecdtl b, fina_arap c, bus_ship_container d where b.isdelete = false and c.isdelete = false and d.isdelete = false and  b.actpayrecid = t.id and c.id = b.arapid and d.jobid = c.jobid and d.cntno = '"+cntno+"')";
		}
		if(branchComs != null){
			qry += "\n AND t.corpid = "+branchComs+" ";
		}

		m.put("qry", qry + actpayrecdatesql + rpdatesql + isvchsql + jobnosql);
		
		String filter = SaasUtil.filterByCorpid("t");
		
		//华展，凌润 不要这个控制 2208
		String csno = ConfigUtils.findSysCfgVal("CSNO");
		if("2199".equals(csno) || "2208".equals(csno)){
		}else{
			filter +="\n AND EXISTS (SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.agentcorpid) AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")";
		}
		
		m.put("filter", filter);
		
		return m;
	}
	
	
	
	@Action
	public void actpayrecsearch(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String actdate;
		Long amounts;
		String pdate;
		String actdateEnd;
		String pdateEnd;
		if(actpayrecdate ==null){
			actdate = null;
		}else{
			actdate = df.format(actpayrecdate);
		}
		if(rpdate==null){
			pdate = null;
		}else{
			pdate = df.format(rpdate);
		}
		if(rpdateEnd==null){
			pdateEnd = null;
		}else{
			pdateEnd = df.format(rpdateEnd);
		}
		if(actpayrecdateEnd==null){
			actdateEnd = null;
		}else{
			actdateEnd = df.format(actpayrecdateEnd);
		}
		if(amount ==null){
			amounts =-1l;
		}else{amounts = amount;}
		//this.actpayrecChooseBean.qry(nos,actpayrecno,actdate,pdate,amounts,chequeno,actdateEnd,pdateEnd,branchComs,isvch);
		this.grid.reload();
	}
	
	public void clear(){
		if(qryMapShip !=null){
			qryMapShip.clear();
		}
	}
	
//	public void createChildPanel(String type){
//		if(type.equals("CD")){//制单日期
//			UIDateField cd = new UIDateField();
//			cd.setId("search");
//			changefield.getChildren().add(cd);
//		}else if(type.equals("SD")){//收付日期
//			UIDateField sd = new UIDateField();
//			sd.setId("search");
//			changefield.getChildren().add(sd);
//		}else if(type.equals("SB")){//收付款编号
//			UITextField sb = new UITextField();
//			sb.setId("search");
//			changefield.getChildren().add(sb);
//		}else if(type.equals("GZ")){//工作号
//			UITextField gz = new UITextField();
//			gz.setId("search");
//			changefield.getChildren().add(gz);
//		}else if(type.equals("SF")){//收付金额
//			UINumberField sf = new UINumberField();
//			sf.setId("search");
//			changefield.getChildren().add(sf);
//		}else if(type.equals("ZP")){//支票号
//			UITextField sf = new UITextField();
//			sf.setId("search");
//			changefield.getChildren().add(sf);
//		}else{
//			UITextField zy = new UITextField();//摘要
//			zy.setId("search");
//			changefield.getChildren().add(zy);
//		}
//	}
//	
//	@Action
//	public void typechanged(){
//		String type = AppUtil.getReqParam("type");
//		changefield.getChildren().clear();
//		this.type = type;
//		this.createChildPanel(type);
//		this.update.markUpdate(UpdateLevel.Data, "clientGridPanel");
//		this.update.markUpdate(UpdateLevel.Data, "search");
//	}
}
