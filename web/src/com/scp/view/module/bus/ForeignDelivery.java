	package com.scp.view.module.bus;

	import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.ship.BusCustomsDao;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusCustoms;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

	@ManagedBean(name = "pages.module.bus.foreigndeliveryBean", scope = ManagedBeanScope.REQUEST)
	public class ForeignDelivery extends EditGridFormView {
		
		
		/**
		 * 工作单id
		 */
		@SaveState
		@Accessible
		public Long jobid;
		
		/**
		 * 单号下拉
		 */
		@Bind
	    @SelectItems
	    @SaveState
	    private List<SelectItem> clearancenoses;
		
		/**
		 * 状态描述（I：初始；F：完成）
		 */
		@Bind
		@SaveState
		public String customstatedesc;

		@Resource
		private BusCustomsDao busCustomsDao;

		@SaveState
		@Accessible
		public BusCustoms selectedRowData = new BusCustoms();


		@SaveState
		@Accessible
		public BusShipContainer dtlData = new BusShipContainer();
		
		@Bind
		@SaveState
		public Date ata;
		
		@Bind
		@SaveState
		public Date atd;
		
		@Bind
		@SaveState
		public int transday;
		
		@Bind
		@SaveState
		public String pod;
		
		@Bind
		@SaveState
		public String totalbox;
		
		@Override
		public void beforeRender(boolean isPostBack) {
			super.beforeRender(isPostBack);
			if (!isPostBack) {
				init();
				if(!getSysformcfg().equals("")){
					String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
					Browser.execClientScript(js);
				}
				super.applyGridUserDef();
			}
		}

		@Action
		public void saveMaster(){
			try {
				doServiceSaveMaster(); //Master
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			update.markUpdate(true, UpdateLevel.Data, "pkVal");
		}

		/**
		 * 保存
		 */
		public void doServiceSaveMaster() {
			if (modifiedData != null) {
                update(modifiedData);
            }
			if (addedData != null) {
                add(addedData);
            }
			if(removedData != null){
				remove(removedData);
			}
			this.selectedRowData.setAreatype("C");
			//拿到页面上的报关公司id
			Long customid = this.selectedRowData.getCustomid();
			//根据id拿到公司的namec赋值到customabbr
			if(customid != null) {
				SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(customid);
				this.selectedRowData.setCustomabbr(sc.getNamec());
			}
			//保存
			this.serviceContext.busCustomsMgrService.saveData(this.selectedRowData);
			this.pkVal = this.selectedRowData.getId();
			this.initCombox();
			Browser.execClientScript("showmsg()");
		}
		
		protected void update(Object modifiedData) {
			serviceContext.busShipContainerMgrService.updateBatchEditGridForeegnde(modifiedData);
		}

		
		/**
		 * 拖车关联的所有货柜id 拼接：xxx,xxx,xxx
		 */
		@Bind
		@SaveState
		public String containerids;

		public void init() {
			selectedRowData = new BusCustoms();
			String jobid = AppUtils.getReqParam("id");
			jobid = jobid.replaceAll("#", "");
			this.jobid = Long.valueOf(jobid);
			this.initCombox();
			if (this.jobid != null) {
				FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao
						.findById(this.jobid);
				this.selectedRowData.setJobid(this.jobid);
				this.containerids = this.serviceContext.busTruckMgrService
						.getLinkContainersid(this.pkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
			}
		}
		
		
		
		public void initCombox() {
			if (this.jobid != null) {
				try{
					List<BusCustoms> buscustomsList = this.serviceContext.busCustomsMgrService.getBusCustomsListByJobid(this.jobid , "I");
					if(buscustomsList != null && buscustomsList.size() > 0) {
						List<SelectItem> items = new ArrayList<SelectItem>();
						for(BusCustoms bc : buscustomsList) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String data = sdf.format(bc.getInputtime()==null?Calendar.getInstance().getTime():bc.getInputtime());
							items.add(new SelectItem(bc.getNos(), bc.getNos()));
						}
						items.add(new SelectItem(null, ""));
						this.clearancenoses = items;
						if(this.pkVal==null||this.pkVal == -1L) {
							this.pkVal = buscustomsList.get(0).getId();
						}
						this.refreshMasterForm();
					} else {
						this.addMaster();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		@SaveState
		public String customcompany="";
		
		@Bind
		private UIIFrame busshipaddresiform;
		
		public void refreshMasterForm() {
			//查询委托中的实到港日、委托中的实开船日、标准包清天数
			try{
				BusShipping busShipping = serviceContext.busShippingMgrService.findByjobId(this.jobid);
				if(busShipping!=null){
					ata = busShipping.getAta();
					atd = busShipping.getAtd();
					pod = busShipping.getPod();
					if(busShipping.getDestination()!=null&&busShipping.getDestination().length()>0
							&&(this.selectedRowData.getDestination()==null||this.selectedRowData.getDestination().equals(""))){
						if(this.selectedRowData==null||(this.selectedRowData!=null&&StrUtils.isNull(this.selectedRowData.getDestination()))){
							this.selectedRowData.setDestination(busShipping.getDestination());
							Browser.execClientScript("$('#destination_input').val('"+busShipping.getDestination()+"')");
						}
					}
				}
			}catch(NoRowException e){
			}
			String totalboxsql = "SELECT f_fina_jobs_cntdesc('jobid="+this.jobid+"') AS cntdesc";
			try {
				Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(totalboxsql);
				if(map != null && map.size() > 0){
					this.totalbox = map.get("cntdesc").toString();
				}else{
					this.totalbox = "";
				}
			} catch (NoRowException e) {
				this.totalbox = "";
			}catch (Exception e) {
				this.totalbox = "ERROR";
			}
			if(this.pkVal!=null&&this.pkVal != -1L) {
				this.selectedRowData = this.busCustomsDao.findById(this.pkVal);
				if(this.selectedRowData != null) {
					if("I".equals( this.selectedRowData.getCustomstate())) {
						this.customstatedesc = "初始";
					} else {
						this.customstatedesc = "完成";
					}
					if(this.selectedRowData.getClearancecusid()>0){
						SysCorporation sysCorporation = 
							serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getClearancecusid());
						String namee = sysCorporation.getAbbr();
						this.customcompany=(namee!=null&&!namee.equals("")?namee:sysCorporation.getCode());
					}
					try {
						
						//根据货物类别、箱型、起运港、目的地查询标准包清天数
						String sql ="SELECT transday FROM dat_door2doorday a WHERE "+
									"EXISTS(SELECT 1 FROM bus_shipping WHERE isdelete = FALSE AND " +
									"jobid = "+this.selectedRowData.getJobid()+" AND goodstypeid = a.googstypeid)"+
									"AND EXISTS(SELECT 1 FROM bus_shipping x,dat_port y WHERE x.isdelete = FALSE AND " +
									"x.jobid = "+this.selectedRowData.getJobid()+" AND x.destinationcode=y.code AND y.id = a.destinationid)"+
									"AND EXISTS(SELECT 1 FROM bus_ship_container WHERE isdelete = FALSE AND " +
									"jobid = "+this.selectedRowData.getJobid()+" AND cntypeid = a.cntypeid)"+
									"LIMIT 1";
						Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
						if(m!=null&&m.size()>0){
							transday = Integer.parseInt(m.get("transday")==null?"":m.get("transday").toString());
						}
					} catch (Exception e) {
					}
					//加载派送信息（1436 清关派送中增加权限控制派送信息页面是否显示）
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_checkright('sendinformation',"
							+AppUtils.getUserSession().getUserid()+") AS result");
					String url = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
					if(m!=null&&m.get("result")!=null&&m.get("result").toString().equals("1")){
						url = AppUtils.getContextPath() + "/common/busshipaddres.xhtml?linkid=" + this.pkVal;
					}
					busshipaddresiform.load(url);
					busshipaddresiform.repaint();
				}
			} else {
				String url = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				busshipaddresiform.load(url);
			}
			
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.refresh();
			String nos = this.selectedRowData.getNos();
		}


		@Override
		protected void doServiceFindData() {
			
		}


		@Override
		protected void doServiceSave() {
			
		}
		
		@Override
		public Map getQryClauseWhere(Map<String, Object> queryMap) {
			Map m = super.getQryClauseWhere(queryMap);
			String qry = StrUtils.getMapVal(m, "qry");
			qry += //"\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x WHERE x.containerid = t.id)"
					//+ "\nOR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid ="
					//+ this.pkVal
//					+ " AND k.containerid = t.id))"
					 "\nAND t.jobid = " + this.jobid;
			m.put("qry", qry);
 			return m;
		}
		
		@Action
		public void addMaster() {
			this.pkVal = -1L;
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
			this.pkVal = -1l;
			this.refreshMasterForm();
			refresh();

		}
		
		@Override
		public void refresh() {
			super.refresh();
//			refreshMasterForm();
		}
		
		@Action
		public void delMaster() {
			if (selectedRowData.getId() == 0) {
				this.addMaster();
			} else {
				serviceContext.busCustomsMgrService.removeDate(selectedRowData
						.getId());
				this.addMaster();
				this.initCombox();
				this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.pkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
				Browser.execClientScript("showmsg()");
			}
		}
		
		/**
		 * 清关单号下拉变化时，更新清关信息
		 */
		@Action
		public void changeCustomsInfo() {
			String nos = AppUtils.getReqParam("customsnos") ;
	        if(nos != null&&!"".equals(nos)) {
	        	String sql = "nos ='"+nos+"'";
	        	List<BusCustoms> busCustoms = this.serviceContext.busCustomsMgrService.busCustomsDao.findAllByClauseWhere(sql);
	        	if(busCustoms!=null&&busCustoms.size()>0){
	        		this.selectedRowData = busCustoms.get(0);
	        	}
	        	if(this.selectedRowData != null) {
	        		this.pkVal = this.selectedRowData.getId();
	        		this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.pkVal);
	        		update.markUpdate(true, UpdateLevel.Data, "containerids");
	        		this.refreshMasterForm();
	        	}
	        }
	    }
		
		
		@Action
		public void showDispatchInfo() {
			busshipaddresiform.repaint();
		}
}