package com.scp.view.module.jobs;

import java.util.ArrayList;
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
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.bus.BusAir;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipBill;
import com.scp.model.ship.BusShipping;
import com.scp.model.ship.BusTruck;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.jobs.jobsinfoBean", scope = ManagedBeanScope.REQUEST)
public class JobsinfoBean extends GridView{
	
	@SaveState
	public String jobid;
	
	@SaveState
	public String userid;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMap = new HashMap<String, Object>();

	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();
	
	@SaveState
	@Accessible
	public BusShipping shipRowData = new BusShipping();
	
	@SaveState
	@Accessible
	public BusAir airRowData = new BusAir();
	
	@SaveState
	@Accessible
	public BusTruck landRowData = new BusTruck();
	
	@SaveState
	@Accessible
	public String priceuser;
	
	@SaveState
	@Accessible
	public String jobstate;
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				String id1 = AppUtils.getReqParam("jobid");
				String id2 = AppUtils.getReqParam("userid");
				if(!StrUtils.isNull(id1)) {
					jobid = id1;
				}
				if(!StrUtils.isNull(id2)) {
					userid = id2;
				}
				initjobs();
				super.applyGridUserDef(this.getMBeanName() + ".arapgrid" , "arapgridJsvar");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initjobs() {
		String id = AppUtils.getReqParam("jobid");
		if (!StrUtils.isNull(id)) {
			Long jobid = Long.parseLong(id);
			this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(jobid);
			if(this.selectedRowData == null){
				BusShipBill busShipBill = serviceContext.busShipBillMgrService.busShipBillDao.findById(jobid);
				if(busShipBill!=null){
					jobid = busShipBill.getJobid();
					this.jobid = busShipBill.getJobid().toString();  
					this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(jobid);
				}else{
					return;
				}
			}
			if("S".equals(selectedRowData.getJobtype())){
				this.shipRowData = serviceContext.busShippingMgrService.findByjobId(jobid);
				if(!StrUtils.isNull(this.shipRowData.getCnortitle()))this.shipRowData.setCnortitle(this.shipRowData.getCnortitle().split("\n")[0]);
				if(!StrUtils.isNull(this.shipRowData.getCneetitle()))this.shipRowData.setCneetitle(this.shipRowData.getCneetitle().split("\n")[0]);
				Browser.execClientScript("showship()");
			}else if("A".equals(selectedRowData.getJobtype())){
				this.airRowData = serviceContext.busAirMgrService.findjobsByJobid(jobid);
				if(!StrUtils.isNull(this.airRowData.getCnortitle()))this.airRowData.setCnortitle(this.airRowData.getCnortitle().split("\n")[0]);
				if(!StrUtils.isNull(this.airRowData.getCneetitle()))this.airRowData.setCneetitle(this.airRowData.getCneetitle().split("\n")[0]);
				Browser.execClientScript("showair()");
			}else if("L".equals(selectedRowData.getJobtype())){
				this.landRowData = serviceContext.busTruckMgrService.findjobsByJobid(jobid);
				Browser.execClientScript("showland()");
			}else if("C".equals(selectedRowData.getJobtype())){
				Browser.execClientScript("showcustoms()");
			}else{
				Browser.execClientScript("showcustoms()");
			}
			
			this.deptids = getqueryDepartid();
			if(selectedRowData != null){
				if(selectedRowData.getJobstate().equals("I")){
					this.jobstate = "未安排";
				}else if(selectedRowData.getJobstate().equals("S")){
					this.jobstate = "已选舱";
				}else if(selectedRowData.getJobstate().equals("TI")){
					this.jobstate = "安排拖车";
				}else if(selectedRowData.getJobstate().equals("TF")){
					this.jobstate = "拖车完成";
				}else if(selectedRowData.getJobstate().equals("CI")){
					this.jobstate = "安排报关";
				}else if(selectedRowData.getJobstate().equals("CF")){
					this.jobstate = "报关完成";
				}else if(selectedRowData.getJobstate().equals("BI")){
					this.jobstate = "做提单";
				}else if(selectedRowData.getJobstate().equals("BF")){
					this.jobstate = "提单已打印";
				}else if(selectedRowData.getJobstate().equals("DI")){
					this.jobstate = "出账单";
				}else if(selectedRowData.getJobstate().equals("DF")){
					this.jobstate = "账单已打印";
				}else if(selectedRowData.getJobstate().equals("J")){
					this.jobstate = "已并单";
				}else if(selectedRowData.getJobstate().equals("E")){
					this.jobstate = "已寄单";
				}else if(selectedRowData.getJobstate().equals("QG")){
					this.jobstate = "已清关";
				}else if(selectedRowData.getJobstate().equals("TG")){
					this.jobstate = "已拖柜";
				}else if(selectedRowData.getJobstate().equals("F")){
					this.jobstate = "完成";
				}
			}
			getCntdesc();
			getinputuser();
			getidcode();
			initArapInfo();
		} 
	}
	
	
	@Bind
	@SaveState
	public String arTip;

	@Bind
	@SaveState
	public String apTip;

	@Bind
	@SaveState
	public String profitTip;
	
	@Bind
	@SaveState
	public String currency="USD";
	
	@Action
	public void initArapInfo() {
		String sql = "\nSELECT f_findarapinfo('jobid=" + this.jobid
				+ ";tax=Y;userid=" + AppUtils.getUserSession().getUserid()
				+ ";currency="+this.currency
				+ "') AS arapinfo";
		try {
			Map m = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String arapinfo = StrUtils.getMapVal(m, "arapinfo");
			String[] tips = arapinfo.split(",");
			if (tips != null && tips.length == 5) {
				arTip = tips[0];
				apTip = tips[1];
				profitTip = tips[2];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.update.markUpdate(UpdateLevel.Data, "arTip");
		this.update.markUpdate(UpdateLevel.Data, "apTip");
		this.update.markUpdate(UpdateLevel.Data, "profitTip");
	}
	
	@Action
	public void changeArapInfo(){
		if(StrUtils.isNull(currency)){
			initArapInfo();
		}
	}
	

	@Bind(id="deptids")
	@SelectItems
	@SaveState
	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
	public List<SelectItem> getqueryDepartid() {
		try {
			Long id = this.selectedRowData != null && this.selectedRowData.getCorpid() != null ? this.selectedRowData.getCorpid() : -1L;
			List<SelectItem> list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d", "WHERE d.isdelete = FALSE AND d.corpid =" +id, "ORDER BY d.name");
			return list;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("jobid", jobid);
		map.put("userid", userid);
		return map;
	}
	
	@Bind
	public UIDataGrid shipgrid;
	
	@Bind(id = "shipgrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider1() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.jobs.jobsinfoBean.shipgrid.page";
				return serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere(qryMap), start, limit)
				.toArray();
			}

			@Override
			public int getTotalCount() {
				return 100;
			}
		};
	}
	
	@Bind
	public UIDataGrid agentgrid;
	
	@Bind(id = "agentgrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider2() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.jobs.jobsinfoBean.agentgrid.page";
				return serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere(qryMap), start, limit)
				.toArray();
			}

			@Override
			public int getTotalCount() {
				return 100;
			}
		};
	}
	
	
	@Bind
	public UIDataGrid cusgrid;
	
	@Bind(id = "cusgrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider3() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.jobs.jobsinfoBean.cusgrid.page";
				return serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere(qryMap), start, limit)
				.toArray();
			}

			@Override
			public int getTotalCount() {
				return 100;
			}
		};
	}
	
	
	@Bind
	public UIDataGrid arapgrid;
	
	@Bind(id = "arapgrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider4() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.jobs.jobsinfoBean.arapgrid.page";
				return serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere(qryMap), start, limit)
				.toArray();
			}

			@Override
			public int getTotalCount() {
				return 100;
			}
		};
	}
	
	@Bind(id = "hblgrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProviderhblgrid() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.jobs.jobsinfoBean.hblgrid.page";
				return serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere(qryMap), start, limit)
				.toArray();
			}

			@Override
			public int getTotalCount() {
				return 100;
			}
		};
	}
	
	@SaveState
	@Accessible
	public String cntdesc;
	
	private void getCntdesc() {
		try {
			String sql = "SELECT * FROM f_fina_jobs_cntdesc('jobid="
				+ selectedRowData.getId() + "') AS cntdesc";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			cntdesc = m.get("cntdesc").toString();
		} catch (Exception e) {
			cntdesc = "";
		}

	}
	
	@SaveState
	@Accessible
	public String inputer;
	
	@SaveState
	@Accessible
	public String updater;
	
	@SaveState
	@Accessible
	public String documenter;
	
	private void getinputuser() {
		try {
			String sql1 = "SELECT namec FROM sys_user where isdelete = false AND isinvalid = true AND code = '"+selectedRowData.getInputer()+"'";
			Map m1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql1);
			inputer = m1.get("namec").toString();
		} catch (Exception e) {
			inputer = "";
		}
		
		try {
			String sql2 = "SELECT namec FROM sys_user where isdelete = false AND isinvalid = true AND code = '"+selectedRowData.getUpdater()+"'";
			Map m2 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2);
			updater = m2.get("namec").toString();
		} catch (Exception e) {
			updater = "";
		}
		
		try {
			documenter = "";
			String sql3 = "" +
					"\nselect "+
					"\n		COALESCE(rolearea,'') || ':' "+ 
					"\n		|| COALESCE((SELECT namec FROM dat_filedata WHERE fkcode = 160 AND isleaf = TRUE AND isdelete = false AND code = t.roletype),'') "+ 
					"\n		|| ':'  "+
					"\n		|| COALESCE((SELECT namec||'/'||namee||'/'||COALESCE((SELECT name FROM sys_department WHERE id = x.deptid),'') FROM sys_user x WHERE id = t.userid),'') AS assigndesc"+
					"\nFROM sys_user_assign t ,bus_shipping b "+
					"\nWHERE b.jobid = "+this.selectedRowData.getId()+" AND t.linkid = b.id "+  
					"\n		AND t.userid IS NOT NULL AND t.isdelete = false"+
					"\nORDER BY rolearea,roletype desc";
			List<Map> m3 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql3);
			for (Map map : m3) {
				documenter += StrUtils.getMapVal(map, "assigndesc") + "<br/>";
			}
		} catch (Exception e) {
			e.printStackTrace();
			documenter = "";
		}

	}
	
	
	@SaveState
	@Accessible
	public String idcode;
	
	private void getidcode() {
		try {
			String sql = "SELECT idcode from sys_user WHERE id = "+selectedRowData.getSaleid()+"";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			idcode = m.get("idcode").toString();
		} catch (Exception e) {
			idcode = "";
		}

	}
	
	
}
