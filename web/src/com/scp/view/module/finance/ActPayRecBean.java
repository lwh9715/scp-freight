package com.scp.view.module.finance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.actpayrecBean", scope = ManagedBeanScope.REQUEST)
public class ActPayRecBean extends GridView {
	
	@Autowired
	public ApplicationConf applicationConf;
	
	
	@SaveState
	private Long clientid = -1l;
	
	@Bind
	public UIWindow showsearchWindow;
	
	@Bind
	public UIIFrame searchIframe;
	
	@Bind
	public String customergridPanel;
	
	@Bind
	public String customerids;

	
	@Bind
	@SaveState
	public Date rpdateStart;
	
	@Bind
	@SaveState
	public Date rpdateEnd;
	
	
	
	@Bind
	@SaveState
	public boolean newPage = false;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid customergrid;

	@Bind
	public String qryType = "all";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			initData();
			super.applyGridUserDef();
			this.customerChooseBean.setQryforNull();
			this.customerChooseBean.setQrysqlforNull();
			String isRPNewPage = ConfigUtils.findSysCfgVal("rp_newpage");
			if("Y".equals(isRPNewPage)){//neo 20200303 系统设置优于个人设置
				newPage = true;
			}else{
				String str = ConfigUtils.findUserCfgVal("rp_newpage", AppUtils.getUserSession().getUserid());
				if("Y".equals(str)){
					newPage = true;
				}else{
					newPage = false;
				}
			}
			this.gridLazyLoad = true;
		}
	}
	@Override
	public void grid_ondblclick(){
		super.grid_ondblclick();
		String gridid = grid.getSelectedIds()[0];
		String id = gridid.split(",")[0];
		if(newPage){
			AppUtils.openWindow(false , "_newRP", "./actpayreceditnew.xhtml?type=edit&id="+id+"&customerid=-1");
		}else{
			AppUtils.openWindow(false , "_newRP", "./actpayrecedit.xhtml?type=edit&id="+id+"&customerid=-1");
		}
	}
	
	@Override
	public void qryRefresh() {
		clientid = -1l;
		this.qryMap.remove("clientid$");
		super.qryRefresh();
		this.grid.reload();
		this.clientGrid.setSelectedRow(-1);
		this.clientGrid.reload();
	}
	
	@Action
	public void qryRefresh2() {
		
		
		this.grid.reload();
	}
	
	
//	@Override
//	public Map getQryClauseWhere(Map<String, Object> queryMap) {
//		Map m = super.getQryClauseWhere(queryMap);
//		String qry = StrTools.getMapVal(m, "qry");
//		qry += "\nAND customerid = " + AppUtil.getUserSession().getCorpid();
//		m.put("qry", qry);
//		return m;
//	}
	@Bind
	@SaveState
	private Long branchComs;
	
	@Bind
	@SaveState
	private String isvch = null;
	
	@Bind
	@SaveState
	public String rpcomtype;
	@Bind
	@SaveState
	public String rpamtfm;
	@Bind
	@SaveState
	public String rpamtto;
	
	@Bind
	@SaveState
	public String inputer;
	
	@Bind
	@SaveState
	public String cntno;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
//		String sql = "\n(EXISTS" + "\n (SELECT 1 "
//			+ "\n				FROM sys_custlib_cust y , sys_custlib_user z "
//			+ "\n				WHERE y.custlibid = z.custlibid "
//			+ "\n				AND y.corpid = clientid" + "\n				AND z.userid = "
//			+ AppUtils.getUserSession().getUserid() + ")" + ")";
//		//权限控制 neo 2016-07-24
		
		m.put("filter", " 1=1");
		
		
		
		if(!StrUtils.isNull(rpcomtype)){
			String filter = " 1=1";
			if(rpcomtype.equals("between")){
				if(!StrUtils.isNull(rpamtfm) && !StrUtils.isNull(rpamtto)){
					filter = " amount BETWEEN "+rpamtfm+" AND "+rpamtto+"";
				}
			}else{
				filter = " amount "+rpcomtype+" "+rpamtfm+"";
			}
			m.put("filter", filter);
		}
		
		
		String qry = StrUtils.getMapVal(m, "qry");
		if(branchComs!=null){
			qry += "\n AND a.corpid ="+branchComs;
		}
		if(isvch!=null&&isvch.equals("T")){
			qry += "\n AND EXISTS(SELECT 1 FROM fs_vch x WHERE srcid = a.id AND x.isdelete = false)";
		}
		if(isvch!=null&&isvch.equals("F")){
			qry += "\n AND NOT EXISTS(SELECT 1 FROM fs_vch x WHERE srcid = a.id AND x.isdelete = false)";
		}
		String qry1 = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x,sys_corporation y WHERE x.corpid = y.corpid AND y.id = a.clientid AND y.isdelete = false AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())qry += qry1;//非saas模式不控制
		
		
		if(rpdateStart==null&&rpdateEnd==null){
			
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			qry += "\nAND rpdate::DATE BETWEEN '"
			+ (StrUtils.isNull(df.format(rpdateStart)) ? "0001-01-01" : df.format(rpdateStart))
			+ "' AND '"
			+ (StrUtils.isNull(df.format(rpdateEnd)) ? "9999-12-31" : df.format(rpdateEnd))
			+ "'";
		}
		
		if(!StrUtils.isNull(inputer)){
			qry += "\n AND EXISTS (SELECT 1 FROM sys_user x where x.code = a.inputer and x.isdelete = false and x.namec = '"+inputer+"')";
		}
		
		if(!StrUtils.isNull(cntno)){
			qry += "\n AND EXISTS (SELECT 1 FROM fina_actpayrecdtl b, fina_arap c, bus_ship_container d where b.isdelete = false and c.isdelete = false and d.isdelete = false and  b.actpayrecid = a.id and c.id = b.arapid and d.jobid = c.jobid and d.cntno = '"+cntno+"')";
		}
		
		m.put("qry", qry);		
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer corpfilter = new StringBuffer();;
//			corpfilter.append("\n AND (a.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" OR a.agentcorpid = "+AppUtils.getUserSession().getCorpidCurrent()+")");
//			m.put("corpfilter", corpfilter.toString());
//		}
		
		StringBuffer corpfilter = new StringBuffer();
		corpfilter.append("\n AND EXISTS (SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = a.corpid OR x.corpid = a.agentcorpid) AND x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+")");
		m.put("corpfilter", corpfilter.toString());
		String setDays = "\n AND (a.actpayrecdate >= NOW()::DATE-"+this.setDays+" OR a.actpayrecdate IS NULL)";
		m.put("setDays", setDays);
		
		if(clientid>0){
			String filter = (String)m.get("filter");
			filter += "\n AND (a.clientid = "+clientid+" OR EXISTS (SELECT 1 FROM fina_actpayrec_clients xx WHERE xx.clientid = "+clientid+" AND xx.actpayrecid = a.id AND xx.isdelete = FALSE ))";
			m.put("filter", filter);
		}
		
		m.put("clientid", clientid);
		return m;
	}
	@Bind
	private String qryCustomerKey;
	
	@Bind
	public String search;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.setQryforNull();
		this.customerChooseBean.setQrysqlforNull();
		this.customerChooseBean.qryType(qryCustomerKey , true , qryType);
		this.clientGrid.reload();
	}

	@Bind
	public UIDataGrid clientGrid;
	
	@ManagedProperty("#{customerchooseBean}")
	public CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
//		this.setQryNull();
		return this.customerChooseBean.getActPayRecClientDataProvider();
	}
	
	@Action
	public void clientGrid_ondblclick() {
		String id = clientGrid.getSelectedIds()[0];
		if(newPage){
			AppUtils.openWindow(false , "_newRP", "./actpayreceditnew.xhtml?type=new&id=-1&customerid="+id+"");
		}else{
			AppUtils.openWindow(false , "_newRP", "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
		}
		
	}
	
	@Action
	public void actpaysearch(){
		AppUtils.openWindow(false , "_newRP2", "./actpaysearch.xhtml");
	}
	
	
	@Action
	public void clientGrid_onrowselect() {
		String[] ids = clientGrid.getSelectedIds();
		if(ids == null || ids.length !=1)return;
		String id = clientGrid.getSelectedIds()[0];
		clientid = Long.parseLong(id);
		
		this.gridLazyLoad = false;
		//this.qryMap.put("clientid$", clientid);
		this.grid.reload();
		//AppUtil.openWindow("_newRP", "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
	}
	
	@Action
	public void del(){
		String[] ids  = grid.getSelectedIds();
		if(ids.length < 1){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		String actpayrecid = ids[0];
		try {
			this.serviceContext.actPayRecService.delRecPay(Long
					.valueOf(actpayrecid));
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 标记,点高级查询时才加载grid
	 */
	@SaveState
	public boolean isLoad = false;

	@Bind(id = "customergrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(!isLoad) return null;
				String sqlId = "pages.module.finance.actpayrecBean.customergrid.page";
				if(qryMapShip.isEmpty()) return null;
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				if(!isLoad) return 0;
//				String sqlId = "pages.module.finance.actpayrecBean.customergrid.count";
//				List<Map> list = serviceContext.daoIbatisTemplate
//						.getSqlMapClientTemplate().queryForList(sqlId,
//								getQryClauseWhere2(qryMapShip));
//				Long count = (Long) list.get(0).get("counts");
//				return count.intValue();
				return 10000;
			}
		};
	}
	
	@Bind
	@SaveState
	public String comtype;
	@Bind
	@SaveState
	public String arapamtfm;
	@Bind
	@SaveState
	public String arapamtto;

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
				
		String qry = StrUtils.getMapVal(map, "qry");
		
		String qry1 = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x,sys_corporation y WHERE x.corpid = y.corpid AND y.id = t.customerid AND y.isdelete = false AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())qry += qry1;//非saas模式不控制
		if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom")))qry += qry1;//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
		
		map.put("qry", qry);	
		
		if(!StrUtils.isNull(comtype)){
			String filter = "";
			if(comtype.equals("between")){
				if(!StrUtils.isNull(arapamtfm) && !StrUtils.isNull(arapamtto)){
					filter = "\nAND EXISTS(SELECT 1 FROM fina_arap x WHERE x.customerid = t.customerid AND x.isdelete = false and x.amount BETWEEN "+arapamtfm+" AND "+arapamtto+")";
				}
			}else{
				filter = "\nAND EXISTS(SELECT 1 FROM fina_arap x WHERE x.customerid = t.customerid AND x.isdelete = false and x.amount "+comtype+" "+arapamtfm+")";
			}
			map.put("filter", filter);	
		}
		
		
		return map;
	}
	
	@Action
	public void clearQryKey2() {
		if (qryMapShip != null) {
			qryMapShip.clear();
			comtype="";
			arapamtfm="";
			arapamtto="";
			update.markUpdate(true, UpdateLevel.Data, "customergridPanel");
			this.refresh2();
		}
	}
	
	
	
	
	@Action
	public void refresh2() {
		
		if(qryMapShip.isEmpty()){
			return;
		}
		boolean isNotEmpty = false; 
		Set<String> set = qryMapShip.keySet();
		for (String key : set) {
			if(!StrUtils.isNull(StrUtils.getMapVal(qryMapShip, key))){
				isNotEmpty = true; 
			}
		}
		if(!isNotEmpty && StrUtils.isNull(arapamtfm) && StrUtils.isNull(arapamtto)){
			MessageUtils.alert("请输入查询条件!");
			return;
		}
		isLoad = true;
		this.customergrid.reload();
	}

	@Action
	public void search() {
		update.markUpdate(true,UpdateLevel.Data,customergridPanel);
		this.clearQryKey2();
		showsearchWindow.show();
	}
	
	/**
	 * 获取CustomerGrid选中的id,
	 * @return
	 */
	public String getCustomerGridSelectIds() {
		String[] ids = this.customergrid.getSelectedIds();
		if(ids == null || ids.length <= 0){
			return "";
		}
		return StrUtils.array2List(ids);
	}
	
	@Action
	public void okMaster(){
		String ids = this.getCustomerGridSelectIds();
		if(ids ==""){
			MessageUtils.alert("请选择一条数据");
			return;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		buffer.append(ids);
		buffer.append(")");
		//显示也清空
		this.qryCustomerKey ="";
		update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
		//把左边的查询记录清空
		this.customerChooseBean.setQrysqlforNull();
		this.customerChooseBean.setQry(buffer.toString());
		this.getClientDataProvider();
		this.clientGrid.reload();
		showsearchWindow.close();
		isLoad = false;
	}
	
	
	@Action
	public void customergrid_ondblclick() {
		String id = customergrid.getSelectedIds()[0];
		
		if(newPage){
			AppUtils.openWindow(false , "_newRP", "./actpayreceditnew.xhtml?type=new&id=-1&customerid="+id+"");
		}else{
			AppUtils.openWindow(false , "_newRP", "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
		}
	}
	
	
	@Action
	public void createLink() {
		AppUtils.openWindow(false , "_newRPCreateLink", "./rp/actpaycreatelink.xhtml?type=new&id=-1&customerid=");
	}
	
	@Action
	public void customergrid_onrowselect() {
		String id = customergrid.getSelectedIds()[0];
		
		SysCorporation sysCorporation = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.valueOf(id));
//		//System.out.println(sysCorporation.getAbbr());
		qryCustomerKey = sysCorporation.getAbbr();
		clientid = sysCorporation.getId();
		
		update.markUpdate(UpdateLevel.Data, "clientid");
		update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
		Browser.execClientScript("customerQry.fireEvent('click')");
		
		this.qryMap.put("clientid$", clientid);
		this.grid.reload();
//		clientGrid.reload();
	}
	
//	public void setQryNull(){
//		if(this.customerids !=null){
//			this.customerChooseBean.setQryforNull();
//		}
//	}
	
	@Action
	private void createDesktopLink() {
//		if(!applicationConf.getIsUseDzz()){
//			this.alert("DZZ未启用");
//			return;
//		}
		
		String[] ids = this.grid.getSelectedIds();
		if(ids==null||ids.length==0){
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		
		String httport = AppUtils.getServerHttPort();
		String dzzuid = AppUtils.getUserSession().getDzzuid();
		Vector<String> sqlBatch = new Vector<String>();
		
		for (String id : ids) {
			String sqlQry = "SELECT f_dzz_desktop_add('httport="+httport+";type=RP;jobid="+id+";dzzuid="+dzzuid+";') As t;";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			String sql = StrUtils.getMapVal(m, "t");
			sqlBatch.add(sql);
		}
		try {
//			this.serviceContext.dzzService.executeQueryBatchByJdbc(sqlBatch);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow unloaDaysWindows;
	
	@Bind
	@SaveState
	public String setDays = "60";
	
	@Action
	public void cancel(){
		unloaDaysWindows.close();
	}
	
	protected void initData(){
		String findUserCfgVal = ConfigUtils.findUserCfgVal("act_pay_rec_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="60";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("act_pay_rec_date",this.setDays, AppUtils.getUserSession().getUserid());
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void clearQryKey() {
		super.clearQryKey();
		branchComs = null;
		isvch = null;
		rpcomtype="";
		rpamtfm="";
		rpamtto="";
	}
	
	@Action
	public void qryType_onselect(){
		this.customerChooseBean.qryType(qryCustomerKey , true , qryType);
		this.clientGrid.reload();
	}
	
	@Action
	public void newPageAction(){
		String newpage = "";
		String str = AppUtils.getReqParam("newpage");
		if("true".equals(str)){
			newpage = "Y";
		}else{
			newpage = "N";
		}
		String newpageval =  ConfigUtils.findUserCfgVal("rp_newpage", AppUtils.getUserSession().getUserid());
		try {
			if(StrUtils.isNull(newpageval)){
				String sql = "INSERT INTO sys_configuser(id,key,val,userid,inputer,inputtime)VALUES(getid(),'rp_newpage','"+newpage+"',"+AppUtils.getUserSession().getUserid()+",'"+AppUtils.getUserSession().getUsercode()+"',NOW())";
				this.serviceContext.userMgrService.sysUserDao.executeSQL(sql);
			}else{
				String sql = "UPDATE sys_configuser SET val = '"+newpage+"',updater = '"+AppUtils.getUserSession().getUsercode()+"',updatetime = now() WHERE key = 'rp_newpage' and userid = "+AppUtils.getUserSession().getUserid()+";";
				this.serviceContext.userMgrService.sysUserDao.executeSQL(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
