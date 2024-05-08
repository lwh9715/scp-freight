	package com.scp.view.module.bus;

	import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.dao.ship.BusCustomsDao;
import com.scp.model.data.DatFeeitem;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusCustoms;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

	@ManagedBean(name = "pages.module.bus.foreignbuscustomsBean", scope = ManagedBeanScope.REQUEST)
	public class ForeignBusCustomsBean extends MastDtlView {

		@Resource
		private BusCustomsDao busCustomsDao;

		@SaveState
		@Accessible
		public BusCustoms selectedRowData = new BusCustoms();


		@SaveState
		@Accessible
		public BusShipContainer dtlData = new BusShipContainer();
		
		/**
		 * 清关单号下拉
		 */
		@Bind
	    @SelectItems
	    @SaveState
	    private List<SelectItem> customsnoses;

		/**
		 * 工作id
		 */
		@Bind
		@SaveState
		@Accessible
		public Long jobid;
		
		/**
		 * 清关关联的所有货柜id
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
		public String customstatedesc;
		
		@Bind
		private UIButton saveMaster;
		
		@Bind
		private UIButton delMaster;
		
		@Bind
		private UIButton chooseship;
		
		@SaveState
		public String customcompany="";
		
		@Accessible
		public String getTimeZone() {
			String findSysCfgVal = ConfigUtils.findUserCfgVal("timestamp_with_time_zone", AppUtils.getUserSession().getUserid());
			return StrUtils.isNull(findSysCfgVal)?"GMT+8":findSysCfgVal;
		}
		
		@Override
		public void init() {
			selectedRowData = new BusCustoms();
			String jobid = AppUtils.getReqParam("id");
			this.jobid = Long.valueOf(jobid);
			this.initCombox();
			if(this.jobid != null) {
				FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
				this.selectedRowData.setJobid(this.jobid);
				this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
			}
			rptUrl = AppUtils.getRptUrl();
		}

		public void initCombox() {
			if (this.jobid != null) {
				List<BusCustoms> buscustomsList = this.serviceContext.busCustomsMgrService.getBusCustomsListByJobid(this.jobid , "I");
				if(buscustomsList != null && buscustomsList.size() > 0) {
					List<SelectItem> items = new ArrayList<SelectItem>();
					for(BusCustoms bc : buscustomsList) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String data = sdf.format(bc.getInputtime()==null?Calendar.getInstance().getTime():bc.getInputtime());
						items.add(new SelectItem(bc.getNos(), bc.getNos()));
					}
					items.add(new SelectItem(null, ""));
					this.customsnoses = items;
					if(this.mPkVal == -1L) {
						this.mPkVal = buscustomsList.get(0).getId();
					}
					this.refreshMasterForm();
				} else {
					this.addMaster();
				}
			}
		}

		@Override
		public void addMaster() {
			this.selectedRowData = new BusCustoms();
			this.selectedRowData.setJobid(this.jobid);
			//设置eta、ata和委托里面相同
			try{
				if(this.jobid>0){
					BusShipping busShipping = serviceContext.busShippingMgrService.findByjobId(this.jobid);
					this.selectedRowData.setEta(busShipping.getEta());
					this.selectedRowData.setAta(busShipping.getAta());
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			// 设置默认签单时间为当天
			this.selectedRowData.setSingtime(new Date());
			// 设置默认状态为“初始”
			this.selectedRowData.setCustomstate("I");
			//设置清关类型为"进口清关"
			this.selectedRowData.setClstype("I");
			this.customstatedesc = "初始";
			java.math.BigDecimal userid=new BigDecimal(AppUtils.getUserSession().getUserid());
			this.selectedRowData.setClsuserid(userid);
			this.mPkVal = -1l;
			this.refreshMasterForm();
			refresh();

		}

		@Override
		public void delMaster() {
			if (selectedRowData.getId() == 0) {
				this.addMaster();
			} else {
				serviceContext.busCustomsMgrService.removeDate(selectedRowData
						.getId());
				this.addMaster();
				this.initCombox();
				this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
				Browser.execClientScript("showmsg()");
			}
		}

		/**
		 * 保存
		 */
		@Override
		public void doServiceSaveMaster() {
			//地域标记,默认设为D,国外
			this.selectedRowData.setAreatype("D");
			//拿到页面上的报关公司id
			Long customid = this.selectedRowData.getCustomid();
			//根据id拿到公司的namec赋值到customabbr
			if(customid != null) {
				SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(customid);
				this.selectedRowData.setCustomabbr(sc.getNamec());
			}
			//保存
			this.serviceContext.busCustomsMgrService.saveData(this.selectedRowData);
			this.mPkVal = this.selectedRowData.getId();
			this.initCombox();
			Browser.execClientScript("showmsg()");
		}

		@Action
		@Override
		public void refreshMasterForm() {
			if(this.mPkVal != -1L) {
				this.selectedRowData = this.busCustomsDao.findById(this.mPkVal);
				if(this.selectedRowData != null) {
					if("I".equals( this.selectedRowData.getCustomstate())) {
						this.customstatedesc = "初始";
						this.disableAllButton(false);
					} else {
						this.customstatedesc = "完成";
						this.disableAllButton(true);
					}
					if(selectedRowData.getCustomid()!=null&&this.selectedRowData.getCustomid()>0){
						SysCorporation sysCorporation = 
							serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getCustomid());
						String namee = sysCorporation.getNamee();
						this.customcompany=(namee!=null&&!namee.equals("")?namee:sysCorporation.getCode());
					}
				}
			} else {
				this.disableAllButton(false);
			}
			
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.refresh();
			this.grid.setSelections(getGridSelIds());
			String nos = this.selectedRowData.getNos();
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
			Browser.execClientScript("showmsg()");
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
			Browser.execClientScript("showmsg()");
		}

		/**
		 * 关联
		 */
		@Action
		public void chooseship() {
			if (this.mPkVal == -1l) {
				MessageUtils.alert("请先保存报关单");
				return;

			}
			// 获取选中的箱/柜id号
			String[] ids = this.grid.getSelectedIds();
			// 关联报关和箱/柜
			try {
				this.serviceContext.busCustomsMgrService.busCustomsLink(this.mPkVal, ids);
				// 根据报关id获得所有箱柜id
				String cotainerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
				this.containerids = cotainerids;
				update.markUpdate(true, UpdateLevel.Data, "gridPanel");
				update.markUpdate(UpdateLevel.Data, "containerids");
				this.refresh();
				this.grid.setSelections(getGridSelIds());
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}

		/**
		 * Neo 2014/5/6	
		 * 完成，改变状态
		 */
		@Action
		public void finish() {
			String customsState = this.selectedRowData.getCustomstate();
			String updater=AppUtils.getUserSession().getUsername();
			if(this.mPkVal == -1L) {
				MessageUtils.alert("请先保存！");
				return;
			}
			if("F".equals(customsState)) {
				MessageUtils.alert("已完成,请勿重复点击!");
				return;
			} else {
				try {
					String sql = "UPDATE bus_customs SET customstate = 'F',updater='" + updater+ "',updatetime=NOW() WHERE id =" + this.mPkVal;
					this.serviceContext.busCustomsMgrService.busCustomsDao.executeSQL(sql);
				} catch (Exception e) {
					MessageUtils.showException(e);
				}
			}
			
			this.refreshMasterForm();
		}
		
		@Action
		public void cancel() {
			String customState = this.selectedRowData.getCustomstate();
			String updater=AppUtils.getUserSession().getUsername();
			if(this.mPkVal == -1L) {
				MessageUtils.alert("请先保存！");
				return;
			}
			if("I".equals(customState)) {
				MessageUtils.alert("尚未完成，无需取消！!");
				return;
			} else {
				try {
					String sql = "UPDATE bus_customs SET customstate = 'I',updater='" + updater+ "',updatetime=NOW() WHERE id =" + this.mPkVal;
					this.serviceContext.busCustomsMgrService.busCustomsDao.executeSQL(sql);
				} catch (Exception e) {
					MessageUtils.showException(e);
				}
			}
			this.refreshMasterForm();
		}
		
		/**
		 * 清关单号下拉变化时，更新清关信息
		 */
		@Action
	    private void changeCustomsInfo() {
			String customsnos = AppUtils.getReqParam("customsnos") ;
	        if(customsnos != null&&!"".equals(customsnos)) {
	        	this.selectedRowData = this.serviceContext.busCustomsMgrService.busCustomsDao.findByNo(customsnos);
	        	if(this.selectedRowData != null) {
	        		this.mPkVal = this.selectedRowData.getId();
	        		this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.mPkVal);
	        		update.markUpdate(true, UpdateLevel.Data, "containerids");
	        		this.refreshMasterForm();
	        	}
	        }
	    }
		
		@Accessible
		public int[] getGridSelIds() {

			String sql = 
					"\nSELECT "+
					"\n(CASE WHEN EXISTS(SELECT 1 FROM bus_customs_link WHERE customsid=" + this.mPkVal + " AND containerid = a.id) THEN TRUE ELSE FALSE END) AS flag"+
					"\nFROM _bus_ship_container a "+
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

		@Override
		public Map getQryClauseWhere(Map<String, Object> queryMap) {
			Map m = super.getQryClauseWhere(queryMap);
			String qry = StrUtils.getMapVal(m, "qry");
			qry += 
				"\nAND (NOT EXISTS (SELECT 1 FROM bus_customs_link x , bus_customs y WHERE x.containerid = t.id AND x.customsid = y.id AND y.clstype = 'I' AND y.isdelete = false)" +
				"\nOR EXISTS(SELECT 1 FROM bus_customs_link c WHERE c.customsid =" + this.mPkVal + " AND c.containerid = t.id))"+
				"\nAND t.jobid = " + this.jobid;
			m.put("qry", qry);
			return m;
		}
		
		public Map getFeeQryClauseWhere(Map<String, Object> queryMap) {
			Map m = super.getQryClauseWhere(queryMap);
			String qry = StrUtils.getMapVal(m, "qry");
			qry += 
				"\nAND t.jobid = " + this.jobid;
			m.put("qry", qry);
			return m;
		}
		
		@Bind(id="reportbuscustomsmat")
		public List<SelectItem> getReportbuscustomsmat(){
			try{
				 return CommonComBoxBean.getComboxItems("d.filename","d.namec",
						"sys_report AS d"," WHERE modcode = 'CustomRpt' AND isdelete = FALSE",
						"order by filename");
			}catch(Exception e){
				MessageUtils.showException(e);
				return null;
			}
		}
		
		@Bind(id="donos")
		public List<SelectItem>getDonos(){
			String sql = "SELECT DISTINCT dono FROM bus_customs WHERE dono is NOT NULL";
			 List<String> donos = this.serviceContext.busCustomsMgrService.busCustomsDao.executeQuery(sql);
			 List<SelectItem> list = new ArrayList<SelectItem>();
			 if(donos != null){
				 for(String dono:donos){
					 list.add(new SelectItem(dono,dono));
				 }
				 return list;
			 }
			 list.add(new SelectItem(null,""));
			 return list;
		}
	
//		@Bind
//		@SaveState
//		@Accessible
//		public String showbuscustomsname="BG_TZ.raq";
//		
//		@Action
//		public void scanReport() {
//			String rpturl = AppUtil.getContextPath();
//			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/BG_TZ.raq";
//			AppUtil.openWindow("_shipbillReport", openUrl + getArgs());
//		}
//		private String getArgs() {
//			String args="";
//			args+="&id="+this.mPkVal;
//			return args;
//		}
//		
		private void disableAllButton(Boolean flag) {
			saveMaster.setDisabled(flag);
			delMaster.setDisabled(flag);
			chooseship.setDisabled(flag);
		}
		
		@Bind
		@SaveState
		public String rptUrl;
		
		/**
		 * 数据显示构件，GRID
		 */
		@Bind
		public UIDataGrid feeGrid;
		
		@SaveState
		@Accessible
		public Map<String, Object> feeQryMap = new HashMap<String, Object>();

		@Bind(id = "feeGrid", attribute = "dataProvider")
		protected GridDataProvider getfeeDataProvider() {
			return new GridDataProvider() {

				@Override
				public Object[] getElements() {
					String sqlId = getMBeanName() + ".grid.feepage";
					Object[] array = daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId, getFeeQryClauseWhere(feeQryMap), start,
							limit).toArray();
					return array;
				}

				@Override
				public int getTotalCount() {
					return 1000;
				}
			};
		}
		
		@Action
		public void feeRefresh(){
			this.feeGrid.reload();
		}
		
		@Bind
		@SaveState
		public String remarks32;
		
		@Action
		public void chooseFeeship(){
			String[] selectedIds = this.feeGrid.getSelectedIds();
			if(selectedIds==null||selectedIds.length<1){
				alert("请至少选择一行！");
				return;
			}
			StringBuffer sb = new StringBuffer();
			BigDecimal totleCNY = new BigDecimal(0);
			BigDecimal totleDHS = new BigDecimal(0);
			BigDecimal totleUSD = new BigDecimal(0);
			BigDecimal totleHKD = new BigDecimal(0);
			for(String id:selectedIds){
				FinaArap arap = serviceContext.arapMgrService.finaArapDao.findById(Long.parseLong(id));
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao.findById(arap.getFeeitemid());
				
				if(arap!=null){
					sb.append(datFeeitem.getCode()+"  ");
					sb.append(arap.getAmount()+"  ");
					if("CNY".equals(arap.getCurrency())){
						totleCNY = totleCNY.add(arap.getAmount());
					}
					if("DHS".equals(arap.getCurrency())){
						totleDHS = totleDHS.add(arap.getAmount());
					}
					if("USD".equals(arap.getCurrency())){
						totleUSD = totleUSD.add(arap.getAmount());
					}
					if("HKD".equals(arap.getCurrency())){
						totleHKD = totleHKD.add(arap.getAmount());
					}
					sb.append(arap.getCurrency()+"\n");
				}
			}
			remarks32 = sb.toString();
			String totleAmt = "";
			if(totleCNY.longValue() != 0){
				totleAmt += "\n " + totleCNY + " CNY";
			}
			if(totleDHS.longValue() != 0){
				totleAmt += "\n " + totleDHS + " DHS";
			}
			if(totleUSD.longValue() != 0){
				totleAmt += "\n " + totleUSD + " USD";
			}
			if(totleHKD.longValue() != 0){
				totleAmt += "\n " + totleHKD + " HKD";
			}
			remarks32 = remarks32 + "\n" + "Totle:"+totleAmt;
			update.markUpdate(true, UpdateLevel.Data, "remarks32");
			Browser.execClientScript("setremarks3()");
		}
		
		@Action
		public void setaddress(){
			String cargolocation = AppUtils.getReqParam("cargolocation");
			String sql = "SELECT address FROM bus_customs WHERE address IS NOT NULL AND cargolocation ='"+cargolocation+"'LIMIT 1";
			List<Map> map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(map!=null&&map.size()>0&&!map.get(0).get("address").equals("")){
				this.selectedRowData.setAddress((String)map.get(0).get("address"));
			}
			this.refresh();
		}
}