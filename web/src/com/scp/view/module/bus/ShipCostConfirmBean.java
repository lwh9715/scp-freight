package com.scp.view.module.bus;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.model.data.DatLine;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.bus.shipcostconfirmBean", scope = ManagedBeanScope.REQUEST)
public class ShipCostConfirmBean extends GridFormView {

	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	public UIWindow showamountWindow;

	@Bind
	public UIWindow showSendMessageWindow;

	@Bind
	public Set chooseRole;

	@Bind
	public String messageContext;

	@Bind
	public UIButton sendMessages;

	@Bind
	public String hintMessage;
	
	@Bind
	@SaveState
	private boolean islocks;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			this.isnocost = true;
			//读取定制列表
			String gridid  = this.getMBeanName() + ".grid";
			String gridJsvar = "gridJsvar";
			applyGridUserDef(gridid , gridJsvar);
			this.gridLazyLoad = true;
		}
	}
	
	@Action
	public void sendMessageBtn() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择一条记录");
		} else {
//			this.hintMessage = "您好,您本次操作一共选择了" + ids.length
//					+ "条记录,推送需要一定时间,预计耗时" + ids.length + "秒,推送后请耐心等候系统提示。";
			this.hintMessage = "";
			update.markUpdate(true, UpdateLevel.Data, "sendMessagePanelGrid");
			showSendMessageWindow.show();
		}
	}

	@Action
	public void sendMessages() {
		if (this.chooseRole == null || this.chooseRole.size() == 0
				|| this.messageContext == null
				|| this.messageContext.trim().length() == 0) {
			MessageUtils.alert("至少勾选一个角色以及发送内容不能为空或者全空格!");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		String tmpid = "";
		for (String s : ids) {
			tmpid += s.split("-")[0] + ",";
		}
		tmpid = tmpid.substring(0, tmpid.length() - 1);
		String tmprole = "";
		for (Object c : chooseRole) {
			tmprole += c.toString() + ",";
		}
		tmprole = tmprole.substring(0, tmprole.length() - 1);
		String serachsql = "SELECT * FROM f_ship_cost_confirm_send_message('userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";roletypes="
				+ tmprole
				+ ";arapids="
				+ tmpid
				+ ";context="
				+ messageContext
				+ ";');";
		Map map = this.serviceContext.userMgrService.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(serachsql);
		String sql = map.get("f_ship_cost_confirm_send_message").toString();
		MessageUtils.alert("OK");
		showSendMessageWindow.close();
//		if (applicationConf.getIsUseDzz() && sql.length() > 0) {
//			serviceContext.dzzService.sendMessageOfShipCostConfirm(sql);
//		}

	}
	
	@Bind
	@SaveState
	public boolean isnocost;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private String clsstart;
	
	@Bind
	@SaveState
	private String clsend;
	
	@Bind
	@SaveState
	private String etdstart;
	
	@Bind
	@SaveState
	private String etdend;

	@Bind
	@SaveState
	private String atdstart;

	@Bind
	@SaveState
	private String atdend;

	@Bind
	@SaveState
	private String bargeclsstart;
	
	@Bind
	@SaveState
	private String bargeclsend;
	
	@Bind
	@SaveState
	private String bargeetdstart;
	
	@Bind
	@SaveState
	private String bargeetdend;
	
	@Bind
	@SaveState
	private String dategateinstart;
	
	@Bind
	@SaveState
	private String dategateinend;
	
	@Bind
	@SaveState
	private Long agentid;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//qry += "\n AND isconfirm = " +islocks;
		//System.out.println("agent--->"+agentid);
		if(agentid != null && agentid > 0){
			qry +="\n AND EXISTS (SELECT 1 FROM bus_shipping bs where bs.agentid = "+agentid+" AND bs.jobid = t.jobid AND bs.isdelete = FALSE)";
		}
		
		if(!StrUtils.isNull(dateastart)||!StrUtils.isNull(dateend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM fina_jobs AS x WHERE x.id = t.jobid AND x.isdelete = FALSE AND x.jobdate::DATE BETWEEN '"
						+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
						+ "' AND '"
						+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
						+ "')";
		}
		if(!StrUtils.isNull(clsstart)||!StrUtils.isNull(clsend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = t.jobid AND x.isdelete = FALSE AND x.cls::DATE BETWEEN '"
				+ (StrUtils.isNull(clsstart) ? "0001-01-01" : clsstart)
				+ "' AND '"
				+ (StrUtils.isNull(clsend) ? "9999-12-31" : clsend)
				+ "')";
		}
		if(!StrUtils.isNull(etdstart)||!StrUtils.isNull(etdend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = t.jobid AND x.isdelete = FALSE AND x.etd::DATE BETWEEN '"
				+ (StrUtils.isNull(etdstart) ? "0001-01-01" : etdstart)
				+ "' AND '"
				+ (StrUtils.isNull(etdend) ? "9999-12-31" : etdend)
				+ "')";
		}
		if(!StrUtils.isNull(atdstart)||!StrUtils.isNull(atdend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = t.jobid AND x.isdelete = FALSE AND x.atd::DATE BETWEEN '"
				+ (StrUtils.isNull(atdstart) ? "0001-01-01" : atdstart)
				+ "' AND '"
				+ (StrUtils.isNull(atdend) ? "9999-12-31" : atdend)
				+ "')";
		}
		if(!StrUtils.isNull(bargeclsstart)||!StrUtils.isNull(bargeclsend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = t.jobid AND x.isdelete = FALSE AND x.bargecls::DATE BETWEEN '"
				+ (StrUtils.isNull(bargeclsstart) ? "0001-01-01" : bargeclsstart)
				+ "' AND '"
				+ (StrUtils.isNull(bargeclsend) ? "9999-12-31" : bargeclsend)
				+ "')";
		}
		if(!StrUtils.isNull(bargeetdstart)||!StrUtils.isNull(bargeetdend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = t.jobid AND x.isdelete = FALSE AND x.bargeetd::DATE BETWEEN '"
				+ (StrUtils.isNull(bargeetdstart) ? "0001-01-01" : bargeetdstart)
				+ "' AND '"
				+ (StrUtils.isNull(bargeetdend) ? "9999-12-31" : bargeetdend)
				+ "')";
		}
		
		if(!StrUtils.isNull(dategateinstart)||!StrUtils.isNull(dategateinend)){
			qry += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = t.jobid AND x.isdelete = FALSE AND x.dategatein::DATE BETWEEN '"
				+ (StrUtils.isNull(dategateinstart) ? "0001-01-01" : dategateinstart)
				+ "' AND '"
				+ (StrUtils.isNull(dategateinend) ? "9999-12-31" : dategateinend)
				+ "')";
		}
		
		//航线
		if(!StrUtils.isNull(linedesc)){
			String line = linedesc.substring(0, linedesc.length()-1);
			qry +="\n AND t.routecode in("+line+")";
		}
		
		m.put("qry", qry);
		
		if(isnocost){//无OCF，开关控制
			m.put("filter1", "\n AND NOT EXISTS(select 1 from fina_arap fa where fa.isdelete =false and fa.jobid = b.id and fa.araptype ='AP' AND fa.feeitemid = ANY(SELECT x.id FROM dat_feeitem x WHERE x.isdelete = FALSE AND x.code LIKE 'OCF%'))");
		}else{
			m.put("filter1", "\n AND EXISTS(select 1 from fina_arap fa where fa.isdelete =false and fa.jobid = b.id and fa.araptype ='AP' AND fa.feeitemid = ANY(SELECT x.id FROM dat_feeitem x WHERE x.isdelete = FALSE AND x.code LIKE 'OCF%'))");
		}
		return m;
	}
	
	// @Action(id = "save")
	// public void save() {
	// 	try {
	// 		if (modifiedData != null) {
	// 			serviceContext.arapMgrService.updateBatchEditGrid(modifiedData);
	// 		}
	// 		grid.reload();
	// 	} catch (Exception e) {
	// 		MessageUtils.showException(e);
	// 	}
	// }

	@Action
	public void editamount() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择一条记录");
		} else {
			showamountWindow.show();
		}
	}

	@Action
	public void changamount() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录!");
		} else {
			String unit20gp = AppUtils.getReqParam("unit20gp");
			String unit40gp = AppUtils.getReqParam("unit40gp");
			String unit40hq = AppUtils.getReqParam("unit40hq");
			String str = "";
			for (String s : ids) {
				str += s.split("-")[0] + ",";
			}
			str = str.substring(0, str.length() - 1);
			// BigDecimal amount = new BigDecimal(sAmount);
			// BigDecimal xrate = new BigDecimal(sXrate);
			try {
				serviceContext.arapMgrService.batchUpdate(ids, unit20gp,
						unit40gp, unit40hq, null, null, null, null);
				showamountWindow.close();
				refresh();
				MessageUtils.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	}
	
	/**
	 * 锁定费用
	 */
	@Action
	public void confirmAp() {
		lockAP(true);
	}
	
	
	
	/**
	 * 解锁费用
	 */
	@Action
	public void confirmApCancel() {
		lockAP(false);
	}
	
	private void lockAP(boolean islock){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		String usercode = AppUtils.getUserSession().getUsercode();
		for (String id : ids) {
			argsMap.put("arapid",id.split("-")[0]);
			argsMap.put("usercode",usercode);
			argsMap.put("type",islock?"LOCK":"UNLOCK");
			String urlArgs2 = AppUtils.map2Url(argsMap, ";");
			//System.out.println(urlArgs2);
			String sqlQry = "SELECT * FROM f_fina_jobs_lock_ap_ocf('"+urlArgs2+"')";
			try {
				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.refresh();
	}

	@Override
	protected void doServiceFindData() {
	}

	@Override
	protected void doServiceSave() {
	}
	
	@Bind
	public UIWindow searchWindow;
	
	@Action
	public void search() {
		this.searchWindow.show();
	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
	}
	
	@Bind
	public UIWindow nosislockwindow;
	
	@Bind
	public String showmsg;


	@Bind
	public UIWindow importDataHandWindow;

	@Bind
	public UIIFrame importDataHandIFrame;

	@Action
	public void updatefclprice0() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String jobidstr = StringUtils.join(ids,",");
		importDataHandIFrame.setSrc("/pages/module/bus/shipcostchoosefclbyhand.xhtml?jobidstr=" + jobidstr);
		update.markAttributeUpdate(importDataHandIFrame, "src");
		importDataHandWindow.show();
	}


	
	
	
	@Bind(id="qryPol")
    public List<SelectItem> getQryPol() {
		try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		String sql = "WITH rc_pol AS("
						+ "\n SELECT DISTINCT pol FROM price_fcl WHERE isdelete = false and pol <> '' and pol is not null"
						+ "\n UNION ALL"
						+ "\n SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispol = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee))"
						+ "\n SELECT DISTINCT pol FROM rc_pol ORDER BY pol;";
        	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
        	if(list!=null){
        		Object value = null;
        		for (Map dept : list) {
        			value = dept.get("pol");
    				items.add(new SelectItem(String.valueOf(value),
    						String.valueOf(value)));
        		}	
        	}
    		return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryPod")
    public List<SelectItem> getQryPod() {
		try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		//1733 运价维护及运价查询列表调整(起运港提取：收货地 UNION ALL 运价起运港数据)
    		String sql = "WITH rc_pol AS("
						+ "\n SELECT DISTINCT pod FROM price_fcl WHERE isdelete = false and pod <> '' and pod is not null"
						+ "\n UNION ALL"
						+ "\n SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispod = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee))"
						+ "\n SELECT DISTINCT pod FROM rc_pol ORDER BY pod;";
        	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
        	if(list!=null){
        		Object value = null;
        		for (Map dept : list) {
        			value = dept.get("pod");
    				items.add(new SelectItem(String.valueOf(value),
    						String.valueOf(value)));
        		}	
        	}
    		return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryLine")
    public List<SelectItem> getQryLine() {
    	try {
    		return CommonComBoxBean.getComboxItems("DISTINCT line","line","price_fcl AS d"
    				,"WHERE isdelete = false and line <> '' and line is not null","ORDER BY line");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryLinecode")
    public List<SelectItem> getQryLinecode() {
    	try {
    		return CommonComBoxBean.getComboxItems("DISTINCT shipline","shipline","price_fcl AS d"
    				,"WHERE isdelete = false and shipline <> '' and shipline is not null","ORDER BY shipline");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="qryCar")
    public List<SelectItem> getQryCar() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT shipping","shipping"
					,"price_fcl AS d","WHERE isdelete = false and shipping <> '' and shipping is not null","ORDER BY shipping");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Action
	public void clear() {
		this.clearQryKey();
	}
	
	@Override
	public void clearQryKey() {
		this.isnocost = false;
		dateastart = "";
		dateend = "";
		clsstart = "";
		clsend = "";
		etdstart = "";
		etdend = "";
		atdstart = "";
		atdend = "";
		bargeclsstart = "";
		bargeclsend = "";
		bargeetdstart = "";
		bargeetdend = "";
		this.lineid = "";
		this.routecode = "";
		this.linedesc = "";
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
		Browser.execClientScript("$('#priceuser_input').val('');");
		update.markUpdate(true, UpdateLevel.Data, "searpanel");
	}
	
	
	
	
	
	@Bind
	public UIDataGrid gridLine;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapLine = new HashMap<String, Object>();
	
	@Bind(id = "gridLine", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.data.lineBean.grid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapLine), start, limit)
						.toArray();

			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.data.lineBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapLine));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qrylinedesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap2) {
		Map map = getQryClauseWhere3(queryMap2);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(qrylinedesc) ){
			qrylinedesc = StrUtils.getSqlFormat(qrylinedesc);
			qrylinedesc = qrylinedesc.toUpperCase();
			qry += "AND (code ILIKE '%"+qrylinedesc+"%' OR namec ILIKE '%"+qrylinedesc+"%' OR namee ILIKE '%"+qrylinedesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";
				if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='"
								+ dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index)
									+ "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							buffer.append("\nAND UPPER(" + key
									+ ") LIKE UPPER('%'||" +"TRIM('"+ val+"')" + "||'%')");
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		map.put("limit", limits+"");
		map.put("start", starts+"");
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;
	
	@Action
	public void qryline() {
		this.gridLine.reload();
	}
	
	@Bind
	@SaveState
	public String routecode = "";
	
	@Bind
	@SaveState
	public String lineid = "";
	
	@SaveState
	public String linedesc = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridLine.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.lineid.contains(id)){
				this.lineid = lineid + id +",";
				DatLine line = serviceContext.lineMgrService.datLineDao.findById(Long.valueOf(id));
				this.routecode = routecode + line.getNamec() + ",";
				this.linedesc = linedesc + "'"+line.getNamec()+"'" + ",";
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "lineid");
		update.markUpdate(true, UpdateLevel.Data, "routecode");
		update.markUpdate(true, UpdateLevel.Data, "linedesc");
	}
	
	@Action
	public void empty() {
		this.lineid = "";
		this.routecode = "";
		this.linedesc = "";
		update.markUpdate(true, UpdateLevel.Data, "lineid");
		update.markUpdate(true, UpdateLevel.Data, "routecode");
		update.markUpdate(true, UpdateLevel.Data, "linedesc");
		this.gridLine.repaint();
	}
	
	@Action
	public void clearcheck() {
		this.gridLine.repaint();
	}
}