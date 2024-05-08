	package com.scp.view.module.bus;

	import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

	@ManagedBean(name = "pages.module.bus.foreignbustruckBean", scope = ManagedBeanScope.REQUEST)
	public class ForeignBusTruckBean extends MastDtlView {

		@SaveState
		@Accessible
		public BusTruck selectedRowData = new BusTruck();

		@SaveState
		@Accessible
		public BusShipContainer dtlData = new BusShipContainer();
		
		/**
		 * 司机下拉
		 */
		@Bind
	    @SelectItems
	    @SaveState
	    private List<SelectItem> drivers;
		
		/**
		 * 拖车单号下拉
		 */
		@Bind
	    @SelectItems
	    @SaveState
	    private List<SelectItem> trucknoses;

		/**
		 * 工作单id
		 */
		@SaveState
		@Accessible
		public Long jobid;

		/**
		 * 拖车关联的所有货柜id
		 * 拼接：xxx,xxx,xxx
		 */
		@Bind
		@SaveState
		public String containerids;
		
		/**
		 * 状态描述（I：初始；F：完成）
		 */
		@Bind
		@SaveState
		public String truckstatedesc;
		
		@Bind
		private UIButton saveMaster;
		
		@Bind
		private UIButton delMaster;
		
		@Bind
		private UIButton chooseship;

		@Override
		public void init() {
			selectedRowData = new BusTruck();
			String jobid = AppUtils.getReqParam("id");
			jobid = jobid.replaceAll("#", "");
			this.jobid = Long.valueOf(jobid);
			this.initCombox();
			if (this.jobid != null) {
				FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao
						.findById(this.jobid);
				this.selectedRowData.setJobid(this.jobid);
				this.containerids = this.serviceContext.busTruckMgrService.getLinkContainersid(this.mPkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
			}
		}
		
		public void initCombox() {
			String user = AppUtils.getUserSession().getUsercode();
			// 设置拖车号下拉列表值
			if (this.jobid != null) {
				List<BusTruck> bustruckList = this.serviceContext.busTruckMgrService.getForeignBusTruckListByJobid(this.jobid);
				if(bustruckList != null && bustruckList.size() > 0) {
					List<SelectItem> items = new ArrayList<SelectItem>();
					for(BusTruck bt : bustruckList) {
						items.add(new SelectItem(bt.getNos(), bt.getNos()));
					}
					items.add(new SelectItem(null, ""));
					this.trucknoses = items;
					if(this.mPkVal == -1L) {
						this.mPkVal = bustruckList.get(0).getId();
					}
					this.refreshMasterForm();
				} else {
					this.addMaster();
				}
			} 
		}

		@Override
		public void addMaster() {
			this.selectedRowData = new BusTruck();
			this.selectedRowData.setJobid(this.jobid);
			this.selectedRowData.setTruckstate("I");
			this.truckstatedesc = "初始";
			this.selectedRowData.setSingtime(new Date());
			this.selectedRowData.setLoadtime(new Date());
			java.math.BigDecimal userid=new BigDecimal(AppUtils.getUserSession().getUserid());
			this.selectedRowData.setMgruserid(userid);
			this.mPkVal = -1l;
			this.changeDriver();
			this.refreshMasterForm();
			refresh();
			
		}

		@Override
		public void delMaster() {
			if (selectedRowData.getId() == 0) {
				this.addMaster();
			} else {
				try {
					serviceContext.busTruckMgrService.removeDate(selectedRowData.getId());
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
				this.addMaster();
				this.initCombox();
				this.containerids = this.serviceContext.busTruckMgrService.getLinkContainersid(this.mPkVal);
	    		update.markUpdate(true, UpdateLevel.Data, "containerids");
				this.alert("OK");
			}
		}

		/**
		 * 保存
		 */
		@Override
		public void doServiceSaveMaster() {
			// 地域标记,默认设为D
			this.selectedRowData.setAreatype("D");
			// 拿到页面上的报关公司id
			Long truckid = this.selectedRowData.getTruckid();
			// 根据id拿到公司的namec赋值到customabbr
			if (truckid != null) {
				SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao
						.findById(truckid);
				this.selectedRowData.setTruckabbr(sc.getNamec());
			}
			// 保存
			this.serviceContext.busTruckMgrService.saveData(this.selectedRowData);
			this.mPkVal = this.selectedRowData.getId();
			this.initCombox();
			alert("OK");
		}

		@Action
		@Override
		public void refreshMasterForm() {
			if(this.mPkVal != -1L) {
				this.selectedRowData = this.serviceContext.busTruckMgrService.busTruckDao
						.findById(this.mPkVal);
				if (this.selectedRowData != null) {
					if ("I".equals(this.selectedRowData.getTruckstate())) {
						this.truckstatedesc = "初始";
						this.disableAllButton(false);
					} else {
						this.truckstatedesc = "完成";
						this.disableAllButton(true);
					}

					this.changeDriver();
				}
			} else {
				this.disableAllButton(false);
			}
			
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.refresh();
			this.grid.setSelections(getGridSelIds());
		}
		

		@Override
		public void add() {
			dtlData = new BusShipContainer();
			dtlData.setJobid(this.jobid);
			super.add();
		}

		@Override
		public void del() {
			serviceContext.busShipContainerMgrService.removeDate(this
					.getGridSelectId());
			this.alert("OK");
			this.grid.reload();
		}

		@Override
		protected void doServiceFindData() {
			this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
					.findById(this.pkVal);
		}

		@Override
		protected void doServiceSave() {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			alert("OK");
		}

		/**
		 * 关联
		 */
		@Action
		public void chooseship() {
			if (this.mPkVal == -1l) {
				MessageUtils.alert("请先保存拖车单");
				return;

			}
			// 获取选中的箱/柜id号
			String[] ids = this.grid.getSelectedIds();
			// 关联报关和箱/柜
			this.serviceContext.busTruckMgrService.busTruckLink(this.mPkVal, ids);
			// 根据拖车id获得所有箱柜id
			String cotainerids = this.serviceContext.busTruckMgrService.getLinkContainersid(this.mPkVal);
			this.containerids = cotainerids;
			update.markUpdate(true, UpdateLevel.Data, "containerids");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.grid.reload();
			this.grid.setSelections(getGridSelIds());

		}
		
		@Accessible
		public int[] getGridSelIds() {

			String sql = 
					"\nSELECT " +
					"\n(CASE WHEN EXISTS(SELECT 1 FROM bus_truck_link x WHERE truckid = " + this.mPkVal + " AND x.containerid = a.id) THEN TRUE ELSE FALSE END) AS flag" +
					"\nFROM _bus_ship_container a " +
					"\nWHERE a.isdelete = FALSE " +
					"\nAND a.jobid = " + this.jobid +
					"\nORDER BY id";
			try {
				List<Map> list = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql(sql);
				List<Integer> rowList = new ArrayList<Integer>();
				int rownum = 0;
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					if ((Boolean) map.get("flag"))
						rowList.add(rownum);
					rownum++;
				}
				int row[] = new int[rowList.size()];
				for (int i = 0; i < rowList.size(); i++) {
					row[i] = rowList.get(i);
				}
				return row;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
//		/**
//		 * 司机框下拉选择时,更新司机对应的信息
//		 */
//		@Action
//	    private void changeDriverInfo() {
//			String driver = AppUtil.getReqParam("driver");
//	        if(driver !=null) {
//	        	String[] driverInfo = this.serviceContext.busTruckMgrService.foreignqueryDriverInfo(driver);
//	        	if(driverInfo != null) {
//	        		this.selectedRowData.setDriverno(driverInfo[0]);
//	            	this.selectedRowData.setDrivertel(driverInfo[1]);
//	            	this.selectedRowData.setDrivermobile(driverInfo[2]);
//	            	update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
//	        	}
//	        }
//	    }
		
		/**
		 * 拖车公司下拉选择时,更新拖车人信息
		 */
		@Action
	    private void changeDriver() {
			Long truckid = this.selectedRowData.getTruckid();
	        if(truckid != null) {
	        	String user = AppUtils.getUserSession().getUsercode();
	        	this.drivers = this.serviceContext.busTruckMgrService.foreignqueryDriversByInputer(user,truckid);
	        } else {
	        	this.drivers = null;
	        }
	        update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	    }
		
		/**
		 * 客户联系人下拉选择时,更新拖车时间，地址对应的信息
		 */
		@Action
	    private void changeLoadContactInfo() {
			String loadcontact = AppUtils.getReqParam("loadcontact");
	        if(loadcontact !=null&&!"".equals(loadcontact)) {
	        	String  querysql ="loadcontact ='"+loadcontact+"'"+"  AND areatype='D'";
	        	BusTruck bustruck = this.serviceContext.busTruckMgrService.busTruckDao.findOneRowByClauseWhere(querysql);
	        	if(bustruck !=null) {
	            	this.selectedRowData.setLoadaddress(bustruck.getLoadaddress());
	            	this.selectedRowData.setLoadtime(bustruck.getLoadtime());
	            	update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	        	}
	        	    return;
	        }
	    }
		
		/**
		 * 拖车单号下拉变化时，更新拖车信息
		 */
		@Action
	    private void changeTruckInfo() {
			String trucknos = this.selectedRowData.getNos();
	        if(trucknos != null&&!"".equals(trucknos)) {
	        	this.selectedRowData = this.serviceContext.busTruckMgrService.busTruckDao.findByNo(trucknos);
	        	if(this.selectedRowData != null&&!"".equals(this.selectedRowData)) {
	        		this.mPkVal = this.selectedRowData.getId();
	        		this.containerids = this.serviceContext.busTruckMgrService.getLinkContainersid(this.mPkVal);
	        		update.markUpdate(true, UpdateLevel.Data, "containerids");
	        		this.refreshMasterForm();
	        	}
	        }
	    }
		
		/**
		 * 客户联系人下拉
		 */
		@Bind(id="loadcontacts")
	    public List<SelectItem> getLoadcontacts() {
			String user = AppUtils.getUserSession().getUsercode();
			String customerAbbr = null;
			if(this.selectedRowData !=  null) {
				Long jobid = this.selectedRowData.getJobid();
				FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(jobid);
				if(finaJobs != null) {
					customerAbbr = finaJobs.getCustomerabbr();
				}
			}
			List<SelectItem> list  = this.serviceContext.busTruckMgrService.foreignqueryLoadContactsByInputer(user, customerAbbr);
			if(list !=null){
				return list;
			}
			list.add(new SelectItem(null,""));
			return list;
	    }
		
		@Action
		public void finish() {
			String truckState = this.selectedRowData.getTruckstate();
			String updater=AppUtils.getUserSession().getUsername();
			if(this.mPkVal == -1L) {
				MessageUtils.alert("请先保存！");
				return;
			}
			if("F".equals(truckState)) {
				MessageUtils.alert("已完成,请勿重复点击!");
				return;
			} else {
				try {
					String sql = "UPDATE bus_truck SET truckstate = 'F',updater='" + updater+ "',updatetime=NOW() WHERE id =" + this.mPkVal;
					this.serviceContext.busTruckMgrService.busTruckDao.executeSQL(sql);
				} catch (Exception e) {
					MessageUtils.showException(e);
				}
			}
			
			this.refreshMasterForm();
		}
		
		@Action
		public void cancel() {
			String truckState = this.selectedRowData.getTruckstate();
			String updater=AppUtils.getUserSession().getUsername();
			if(this.mPkVal == -1L) {
				MessageUtils.alert("请先保存！");
				return;
			}
			if("I".equals(truckState)) {
				MessageUtils.alert("尚未完成，无需取消！!");
				return;
			} else {
				try {
					String sql = "UPDATE bus_truck SET truckstate = 'I',updater='" + updater+ "',updatetime=NOW() WHERE id =" + this.mPkVal;
					this.serviceContext.busTruckMgrService.busTruckDao.executeSQL(sql);
				} catch (Exception e) {
					MessageUtils.showException(e);
				}
			}
			this.refreshMasterForm();
		}
		
		@Override
		public Map getQryClauseWhere(Map<String, Object> queryMap) {
			Map m = super.getQryClauseWhere(queryMap);
			String qry = StrUtils.getMapVal(m, "qry");
			qry += 
				"\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x , bus_truck y WHERE x.containerid = t.id AND x.truckid = y.id AND y.areatype = 'D' AND y.isdelete = FALSE)" +
				"\nOR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid =" + this.mPkVal + " AND k.containerid = t.id))" +
				"\nAND t.jobid = " + this.jobid;
			m.put("qry", qry);
			return m;
		}
		
		@Bind(id="reportinformat")
		public List<SelectItem> getReportinformat(){
			try{
				return CommonComBoxBean.getComboxItems("d.filename","d.namec",
						"sys_report AS d"," WHERE modcode = 'TruckRpt' AND isdelete = FALSE",
						"order by filename");
			}catch(Exception e){
				MessageUtils.showException(e);
				return null;
			}
		}
		
//		@Bind
//		@SaveState
//		@Accessible
//		public String showwmsinfilename="TC_AP_TZ.raq";
//		
//		@Action
//		public void scanReport() {
//
//			String rpturl = AppUtil.getContextPath();
//			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/TC_AP_TZ.raq";
//			AppUtil.openWindow("_shipbillReport", openUrl + getArgs());
//		}
		
		private String getArgs() {
			String args="";
			args+="&id="+this.mPkVal;
			return args;
		}
		
		private void disableAllButton(Boolean flag) {
			saveMaster.setDisabled(flag);
			delMaster.setDisabled(flag);
			chooseship.setDisabled(flag);
		}
}
	


