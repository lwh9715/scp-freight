package com.scp.view.module.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.ConstantBean.Module;
import com.scp.model.order.BusOrder;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.order.busorderlistBean", scope = ManagedBeanScope.REQUEST)
public class BusOrderListBean extends GridView {

	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	public UIWindow searchWindow;

	@Bind
	public UIWindow showChildWindow;

	@Bind
	public UIIFrame constructIframe;

	@Bind
	public UIButton add;

	@Bind
	public UIButton del;

	@Bind
	@SaveState
	@Accessible
	public Long orderid;

	@Bind
	public UIWindow copyAndCreatingWindow;
	
	@Bind
	public String copyAndCreateCorpid;
	
	@Bind
	public String copyAndCreateCorpidop;
	
	@Bind
	public Date copyAndCreateOrderdate;
	
	@Bind
	@SaveState
	public String mblno;
	
	@Bind
	@SaveState
	public String priceuser;
	
//	@Bind
//	@SaveState
//	public boolean priceuserconfirm;
	
	@Bind
	@SaveState
	public String customernamee;
	@Bind
	@SaveState
	public String cs;
	
	@Bind
	@SaveState
	private String orderdatestart;
	
	@Bind
	@SaveState
	private String orderdateend;
	//SLCADD
	@Bind
	@SaveState
	public String sortlabel_1;
	
	@Bind
	@SaveState
	public String sortvalue_1;
	
	@Bind
	public UIWindow sortWindow;
	
	@Action
	public void addcopy() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		BusOrder order = this.serviceContext.busOrderMgrService.busOrder.findById(Long.parseLong(ids[0]));
		this.copyAndCreateCorpid = order.getCorpid().toString();
		this.copyAndCreateCorpidop = order.getCorpidop().toString();
		this.copyAndCreateOrderdate = new Date();
		update.markUpdate(UpdateLevel.Data, "copyAndCreateCorpid");
		update.markUpdate(UpdateLevel.Data, "copyAndCreateCorpidop");
		update.markUpdate(UpdateLevel.Data, "copyAndCreateOrderdate");
		this.copyAndCreatingWindow.show();
	}
	
	@Action
	public void cancelCopyAndCreating(){
		this.copyAndCreatingWindow.close();
	}
	
	@Action
	public void copyAndCreating(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String nos = serviceContext.busOrderMgrService.addcopy(ids[0], AppUtils
					.getUserSession().getUserid(), AppUtils.getUserSession()
					.getUsercode(), this.copyAndCreateCorpid, this.copyAndCreateCorpidop, dateFormat.format(this.copyAndCreateOrderdate));
			MessageUtils.alert("复制生成的单号是:[" + nos+"]");
			this.qryMap.clear();
			this.qryMap.put("orderno", nos);
			refresh();
			this.copyAndCreatingWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	//SLCADD
	@Action
	public void showSortWindow(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(!StrUtils.isNull(ordersql)){
			String[] sorts = ordersql.split(",");
			if(sorts != null){
				for (int i = 0; i < sorts.length; i++) {
							sortlabel_1 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_1 = "desc";
							}else{
								sortvalue_1 = "asc";
							}
				}
			}
		}
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		sortWindow.show();
	}
			
	@Action
	public void resetUserColorder(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", null);
		MessageUtils.alert("OK!");
	}
	
	@Action
	public void saveUserColorder(){
		String colorder = "";
		if(!StrUtils.isNull(sortlabel_1)&&!StrUtils.isNull(sortvalue_1)){
			colorder += sortlabel_1 + " " + sortvalue_1 + ",";
		}	
		colorder = colorder.endsWith(",") ? colorder.substring(0, colorder.length()-1) : colorder;
		
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", colorder);
		MessageUtils.alert("OK!");
	}
			
				
	@Action
	private void showgoodsdtl() {
		this.orderid = Long.valueOf(AppUtils.getReqParam("id"));
		constructIframe.load("../salesmgr/booking.xhtml?type=O&orderid="
				+ this.orderid);
		showChildWindow.show();
	}

	@Override
	public void beforeRender(boolean isPostBack) {
		initCtrl();
		if (!isPostBack) {
			initData();
			super.applyGridUserDef();
		}
	}

	private void initCtrl() {
		add.setDisabled(true);
		del.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_order
				.getValue())) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
			} else if (s.endsWith("_update")) {
			} else if (s.endsWith("_createjob")) {
			} else if (s.endsWith("_booking")) {
			} else if (s.endsWith("_delete")) {
				del.setDisabled(false);
			}
		}
	}

	@Action
	public void searchfee() {
		this.qryRefresh();
	}

	@Action
	public void clear() {
		this.clearQryKey();
	}

	@Action
	public void clearQryKey() {
		if (qryMap != null) {
			qryMap.clear();
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefresh();
		}
	}

	@Action
	public void qryRefresh() {
		this.refresh();
	}

	@Action
	private void createDesktopLink() {
//		if(!applicationConf.getIsUseDzz()){
//			this.alert("DZZ未启用");
//			return;
//		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}

		String httport = AppUtils.getServerHttPort();
		String dzzuid = AppUtils.getUserSession().getDzzuid();
		Vector<String> sqlBatch = new Vector<String>();

		for (String id : ids) {
			String sqlQry = "SELECT f_dzz_desktop_add('httport=" + httport
					+ ";type=Order;jobid=" + id + ";dzzuid=" + dzzuid
					+ ";') As t;";
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sqlQry);
			String sql = StrUtils.getMapVal(m, "t");
			sqlBatch.add(sql);
		}
		try {
			//this.serviceContext.dzzService.executeQueryBatchByJdbc(sqlBatch);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void add() {
		String winId = "_edit_order";
		String url = "./busorderfcl.xhtml";
		AppUtils.openWindow(winId, url);
	}


	@Action
	public void search() {
		this.searchWindow.show();
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_order";
		
		BusOrder busOrder = this.serviceContext.busOrderMgrService.busOrderDao.findById(this.getGridSelectId());
		String url = "";
		if(busOrder != null){
			if("FCL".equals(busOrder.getBustype())){
				url = "./busorderfcl.xhtml?type=edit&id=" + this.getGridSelectId();
			}else if("LCL".equals(busOrder.getBustype())){
				url = "./busorderlcl.xhtml?type=edit&id=" + this.getGridSelectId();
			}else if("AIR".equals(busOrder.getBustype())){
				url = "./busorderair.xhtml?type=edit&id=" + this.getGridSelectId();
			}
		}
		AppUtils.openWindow(winId, url);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(!StrUtils.isNull(orderdatestart)||!StrUtils.isNull(orderdateend)){	
			qry  += "\nAND orderdate BETWEEN '"
				+ (StrUtils.isNull(orderdatestart) ? "0001-01-01" : orderdatestart)
				+ "' AND '"
				+ (StrUtils.isNull(orderdateend) ? "9999-12-31" : orderdateend)
				+ "'";
		
		}
		//slc ADD
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}else{
			ordersql = "ORDER BY orderdate DESC ,salesid , orderno DESC";
			m.put("ordersql", ordersql);
		}
		m.put("qry", qry);
		String sql = "\nAND ( cscode =	'" + AppUtils.getUserSession().getUsercode() + "' "
				+ " OR salesid = " + AppUtils.getUserSession().getUserid()
				+ " OR inputer = '" + AppUtils.getUserSession().getUsercode() + "'"
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
								"AND o.corpid <> o.corpidop AND o.corpidop = "+AppUtils.getUserSession().getCorpid()+")"
				//+ " OR updater = '" + AppUtils.getUserSession().getUsercode() + "'"
				+ " OR EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = o.id AND x.userid ="+AppUtils.getUserSession().getUserid()+")"
				+ " OR EXISTS (SELECT 1 FROM bus_ship_booking x WHERE x.orderid = o.id AND x.isdelete = false AND (x.userprice ='"+AppUtils.getUserSession().getUsername()+"' OR x.userprice ='"+AppUtils.getUserSession().getUsercode()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsername()+"' OR x.userbook ='"+AppUtils.getUserSession().getUsercode()+"'))"
				+ " OR" + " (EXISTS" + "\n (SELECT 1 "
				+ "\n		 FROM sys_custlib x , sys_custlib_user y "
				+ "\n	WHERE y.custlibid = x.id AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND x.libtype = 'S' AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = o.salesid))) "
				
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = o.salesid) " //组关联业务员的单，都能看到
				+ ")"
				
				+ ")";
		// 权限控制 neo 2016-07-24
		m.put("filter", sql);
		// 分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			//分公司过滤(忽略迪拜)
//			String corpfilter = "AND (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = o.corpid OR cor.id = o.corpidop)) " +
//					"\nOR o.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR o.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR o.salesid = "+AppUtils.getUserSession().getUserid() +
//					")";
//			m.put("corpfilter", corpfilter);
//		}
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = o.corpid OR x.corpid = o.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
				"\n OR o.salesid ="+AppUtils.getUserSession().getUserid()+") ";
		m.put("corpfilter", corpfilter);
		if("0".equals(this.setDays)){
			String setDays = "\n AND 1=1";
			m.put("setDays", setDays);
		}else{
			String setDays = "\n AND o.orderdate::DATE >= NOW()::DATE-"+(StrUtils.isNull(this.setDays)?"365":this.setDays);
			m.put("setDays", setDays);
		}
		
		if(!StrUtils.isNull(mblno)){
			String sqlmblno = "\n AND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=o.jobid AND bs.mblno ILIKE '%"+mblno+"%') OR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE  isdelete = FALSE AND bs.jobid=o.jobid AND bs.mblno ILIKE '%"+mblno+"%')";
			m.put("sqlmblno", sqlmblno);	
		}
		if(!StrUtils.isNull(cs)){
			String sqlcs = "\n AND EXISTS (SELECT 1 FROM _sys_user_assign sa WHERE isdelete = FALSE AND sa.linkid = o.id AND sa.usernamec ILIKE '%"+cs+"%')";
			m.put("sqlcs", sqlcs);	
		}
		String sqlpriceuser = "";
		if(!StrUtils.isNull(priceuser)){
			sqlpriceuser += "\n AND EXISTS (SELECT 1 FROM sys_user WHERE (namec like '%"+priceuser+"%' OR namee like '%"+priceuser+"%' OR  code like '%"+priceuser+"%') AND id = o.priceuserid AND isdelete = FALSE)";
			m.put("sqlpriceuser", sqlpriceuser);	
		}
//		if(priceuserconfirm == true){
//			sqlpriceuser += "\n AND priceuserconfirm = "+priceuserconfirm;
//			m.put("sqlpriceuser", sqlpriceuser);
//		}
		m.put("userid", AppUtils.getUserSession().getUserid());
		return m;
	}

	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		try {
			List<Long> list = new ArrayList<Long>();
			for (String s : ids) {
				list.add(Long.parseLong(s));
			}
			serviceContext.busOrderMgrService.deleteOrderById(list);
			Browser.execClientScript("showmsg()");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow unloaDaysWindows;
	
	@Bind
	@SaveState
	public String setDays ;
	
	@Action
	public void cancel(){
		unloaDaysWindows.close();
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("bus_order_date",this.setDays, AppUtils.getUserSession().getUserid());
			Browser.execClientScript("showmsg()");
			this.unloaDaysWindows.close();
//			this.qryRefresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void initData(){
		String findUserCfgVal = ConfigUtils.findUserCfgVal("bus_order_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="365";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	@Bind
	public UIIFrame traceIframe;
	
	@Action
	public void showTaskCheckInfo(){
		String jobid = AppUtils.getReqParam("jobid");
		String sql = "SELECT id FROM bpm_processinstance WHERE refid LIKE '%"+jobid+"%' ORDER BY id DESC LIMIT 1";
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		taskCommentsIframe.load("../../../bpm/bpmshowcomments.jsp?processinstanceid="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		traceIframe.load("../../../bpm/model/trace.html?language="+AppUtils.getUserSession().getMlType()
				+"&id="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show();traceWindow.show()");
	}

	
}
