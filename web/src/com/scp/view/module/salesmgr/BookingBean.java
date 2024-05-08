package com.scp.view.module.salesmgr;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UITextArea;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.UICell;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusShipSchedule;
import com.scp.model.order.BusOrder;
import com.scp.model.ship.BusShipBooking;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.salesmgr.bookingBean", scope = ManagedBeanScope.REQUEST)
public class BookingBean extends GridFormView {
	
	
	@SaveState
	public BusShipBooking selectedRowData;
	
	@Bind
	public UITextArea cancelBookingRemarks;
	
	@Bind
	public UIButton creatingBooking;
	
	@Bind
	public UIButton save;
	
	@Bind
	public UICell bookingdtlCell;
	
	@Bind
	public UICell bookingOperationCell;
	
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			String type = (String)AppUtils.getReqParam("type");
			
			String id = (String)AppUtils.getReqParam("id");
			if(!StrUtils.isNull(AppUtils.getReqParam("taskid"))){
				selectedRowData = serviceContext.busBookingMgrService.busShipBookingDao.findOneRowByClauseWhere("orderid="+id);
				this.pkVal = selectedRowData.getId();
				id = this.pkVal.toString();
			}
			
			
			if("O".equals(type)){ //订单生成订舱信息 neo 20160721
				String orderid = (String)AppUtils.getReqParam("orderid");
				this.addFromOrder(orderid);
				this.checkBookingButtonsState(this.selectedRowData.getId());
			}else{
				if(!StrUtils.isNull(id)){
					Long e= Long.parseLong(id);
					this.pkVal = e;
					update.markUpdate(true, UpdateLevel.Data, "pkVal");
					this.checkBookingButtonsState(e);
					this.refresh();
				}
			}
		}
	}
	
	private void checkBookingButtonsState(Long bookingId) {
		String r = (String)AppUtils.getReqParam("r");
		String type = (String)AppUtils.getReqParam("type");
		if(r.length()>0) {
			if("edit".equals(AppUtils.base64Decoder(r))) {
				if(null==bookingId) {
					save.setDisabled(false);
//					creatingBooking.setDisabled(false);
					cancelBookingRemarks.setHidden(true);
				}else {
					BusShipBooking busShipBooking = serviceContext.busBookingMgrService.busShipBookingDao.findById(bookingId);
					if(busShipBooking != null && !busShipBooking.getIsdelete()) {
						if("I".equals(busShipBooking.getBookstate())) {//订舱中I
//							creatingBooking.setDisabled(false);
							cancelBookingRemarks.setHidden(true);
							save.setDisabled(false);
						}else {//订舱完成For取消订舱Cor其它
//							creatingBooking.setDisabled(true);
							save.setDisabled(false);
							if("C".equals(busShipBooking.getBookstate())) {
								cancelBookingRemarks.setHidden(false);
							}else {
								cancelBookingRemarks.setHidden(true);
							}
						}
					}else {
						save.setDisabled(false);
//						creatingBooking.setDisabled(false);
						cancelBookingRemarks.setHidden(true);
					}
				}
				if("O".equals(type)) {
//					this.creatingBooking.setDisabled(true);
					bookingdtlCell.setStyle("visibility: hidden;");
					bookingOperationCell.setStyle("visibility: hidden;");
				}
			}else {
				save.setDisabled(true);
//				creatingBooking.setDisabled(true);
				cancelBookingRemarks.setHidden(false);
			}
		}else {
			save.setDisabled(true);
//			creatingBooking.setDisabled(true);
			cancelBookingRemarks.setHidden(false);
		}
	}
	
	@Override
	public void add() {
		selectedRowData = new BusShipBooking();
		selectedRowData.setPpcctype("PP");
		selectedRowData.setBarge("O");
		selectedRowData.setFeetype("F");
		super.add();
	}
	
	public void addFromOrder(String orderid) {
		if(StrUtils.isNull(orderid))return;
		BusOrder busOrder = this.serviceContext.busOrderMgrService.busOrderDao.findById(Long.parseLong(orderid));
		
		try {
			selectedRowData = serviceContext.busBookingMgrService.busShipBookingDao.findOneRowByClauseWhere("orderid="+busOrder.getId());
			if(selectedRowData != null){
				pkVal = selectedRowData.getId();
				String userbookcode = selectedRowData.getUserbook();
				if(!StrUtils.isNull(userbookcode)){
					List<SysUser> users = serviceContext.userMgrService.sysUserDao.findAllByClauseWhere(" code = '"+userbookcode + "'");
					if(users != null && users.size() ==1)userbookname = users.get(0).getNamec();
					update.markUpdate(true, UpdateLevel.Data, "userbookname");
				}
				selectedRowData.setBargepol(selectedRowData.getBargepol()==null?"":selectedRowData.getBargepol());
				return;
			}
		}catch (MoreThanOneRowException e) {
			e.printStackTrace();
		}catch (NoRowException e) {
			
		}
		selectedRowData = new BusShipBooking();
		pkVal = -1L;
		//selectedRowData.setId(-100L);
		selectedRowData.setPpcctype("PP");
		selectedRowData.setBarge("O");
		selectedRowData.setFeetype("F");
		if(selectedRowData!=null){
			selectedRowData.setBargepol(selectedRowData.getBargepol()==null?"":selectedRowData.getBargepol());
		}
		selectedRowData.setOrderid(busOrder.getId());
		selectedRowData.setOrderno(busOrder.getOrderno());
		selectedRowData.setPod(busOrder.getPod());
		selectedRowData.setPol(busOrder.getPol());
		selectedRowData.setPolid(busOrder.getPolid());
		selectedRowData.setPodid(busOrder.getPodid());
		
		selectedRowData.setPddid(busOrder.getPddid());
		selectedRowData.setPdd(busOrder.getPdd());
		selectedRowData.setUserbook("");
		selectedRowData.setUserprice("");
		selectedRowData.setCarrierid(busOrder.getShipid());
		
		selectedRowData.setIsdelete(false);
		selectedRowData.setGoodsnamec(busOrder.getGoodsdesc());
		
		selectedRowData.setPiece1(busOrder.getPiece20());
		selectedRowData.setPiece2(busOrder.getPiece40gp());
		selectedRowData.setPiece3(busOrder.getPiece40hq());
		
		try {
			String sql = "SELECT pieceother,cntypeothercode FROM bus_orderdtl WHERE cntypeothercode IS NOT NULL AND pieceother IS NOT NULL AND  orderid = "+busOrder.getId()+" ORDER BY pieceother DESC,cntypeothercode LIMIT 1;";
			Map map = this.serviceContext.busOrderMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String code = map.get("cntypeothercode").toString();
			if("45'HQ".equals(code)){
				selectedRowData.setPiece4(Short.parseShort(map.get("pieceother").toString()));
			}else if("20'HD".equals(code)){
			}else if("20'RF".equals(code)){
				selectedRowData.setPiece5(Short.parseShort(map.get("pieceother").toString()));
			}else if("40'FR".equals(code)){
			}else if("40'RH".equals(code)){
			}
		} catch (NoRowException e) {
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
//		question1=(selectedRowData.getPiece1()>0)?true:false;
//		question2=(selectedRowData.getPiece2()>0)?true:false;
//		question3=(selectedRowData.getPiece3()>0)?true:false;
		
		super.add();
	}
	
	
	
	@SaveState
	@Bind
	public String userbookname;

	@Override
	public void refresh() {
		selectedRowData = serviceContext.busBookingMgrService.busShipBookingDao.findById(this.pkVal);
		String userbookcode = "";
		if(selectedRowData!=null){
			userbookcode = selectedRowData.getUserbook();
			selectedRowData.setBargepol(selectedRowData.getBargepol()==null?"":selectedRowData.getBargepol());
		}
		if(!StrUtils.isNull(userbookcode)){
			List<SysUser> users = serviceContext.userMgrService.sysUserDao.findAllByClauseWhere(" code = '"+userbookcode + "'");
			if(users != null && users.size() ==1)userbookname = users.get(0).getNamec();
			update.markUpdate(true, UpdateLevel.Data, "userbookname");
		}
		
		super.refreshForm();
	}

	@Override
	public void save() {
		try {
			if(this.selectedRowData.getBookstate() == null) {
				this.selectedRowData.setBookstate("I");
			}
			this.serviceContext.busBookingMgrService.busShipBookingDao.createOrModify(selectedRowData);
			this.pkVal = selectedRowData.getId();
			update.markUpdate(true, UpdateLevel.Data, "pkVal");
			MessageUtils.alert("OK");
			setordercls();
			messageServiceOfSave();
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	//保存时将订舱的大船截关日期赋值给订单的CLS,船名、航次带过去
	public void setordercls(){
		BusOrder busOrder = this.serviceContext.busOrderMgrService.busOrderDao.findById(selectedRowData.getOrderid());
		busOrder.setCls(selectedRowData.getClsbig());
		busOrder.setVessel(selectedRowData.getVessel());
		busOrder.setVoyage(selectedRowData.getVoyage());
		this.serviceContext.busOrderMgrService.busOrderDao.createOrModify(busOrder);
	}
	
	
	@Autowired
	public ApplicationConf applicationConf;
	
	private void messageServiceOfSave() {
//		if(selectedRowData.getUserbook().isEmpty()) {
//			MessageUtils.alert("订舱负责人为空,请检查!");
//			return;
//		}
//		if(!applicationConf.getIsUseDzz()){
//			return;
//		}
		
		BusOrder busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(selectedRowData.getOrderid());
		if(busOrder.getSalesid()==null){
			MessageUtils.alert("客服为空,请检查订单!");
			return;
		}
		List<String> receiveCodes = new ArrayList<String>();
		receiveCodes.add("");
//		receiveCodes.add(selectedRowData.getUserbook());
		String remindTitle = "订舱消息提醒";
		String remindContext = AppUtils.getUserSession().getUsername()+"发来的订舱安排提醒";
		String url = AppUtils.getBasePath()+"pages/module/order/busorder.aspx?type=edit&id="+busOrder.getId();
		SysUser su = serviceContext.userMgrService.sysUserDao.findById(busOrder.getSalesid());
		String sendContext = "业务员"+su.getNamee()+"("+su.getNamee()+")"+"的订单[url="+url+"]"+busOrder.getOrderno()
			+"[/url]请提交订舱资料，请尽快安排!";
		AppUtils.sendMessage(receiveCodes, remindTitle, remindContext, url, sendContext);
		MessageUtils.alert("订舱安排消息已推送!");
	}


	@Inject
    private PartialUpdateManager update;
    
   
    @Override
	public void del() {
		super.del();
	}
    
	@Bind
	public UIWindow shipscheduleWindow;

	@Bind
	public UIDataGrid gridShipschedule;
	
	@SaveState
	@Accessible
	public BusShipSchedule shipschedule = new BusShipSchedule();
    
    
    @Action
	public void chooseShip() {
		shipscheduleWindow.show();
		this.gridShipschedule.reload();
	}

	@Action
	public void gridShipschedule_ondblclick() {
		this.selectedRowData.setScheduleid(Long.valueOf(this.gridShipschedule
				.getSelectedIds()[0]));
		setShipschedule();
		this.selectedRowData.setCarrierid(getCarrierid(this.shipschedule
				.getCarrier()));
		this.selectedRowData.setVessel(this.shipschedule.getVes());
		this.selectedRowData.setVoyage(this.shipschedule.getVoy());
		this.selectedRowData.setCy(this.shipschedule.getCy());
		this.selectedRowData.setCv(this.shipschedule.getCv());
		this.selectedRowData.setVgm(this.shipschedule.getVgm());
		
		Long polid = this.getPolorPodId(shipschedule.getPol());
		if(polid==null) {
			this.selectedRowData.setPol(null);
			this.selectedRowData.setPolid(null);
			this.selectedRowData.setPdd(null);
			this.selectedRowData.setPddid(null);
		}else {
			this.selectedRowData.setPol(shipschedule.getPol());
			this.selectedRowData.setPolid(polid);
			this.selectedRowData.setPdd(shipschedule.getPol());
			this.selectedRowData.setPddid(polid);
		}
		Long podid = this.getPolorPodId(shipschedule.getPod());
		if(podid==null) {
			this.selectedRowData.setPod(null);
			this.selectedRowData.setPodid(null);
		}else {
			this.selectedRowData.setPod(shipschedule.getPod());
			this.selectedRowData.setPodid(podid);
		}
		
		//qryMapShip.put("carrier", this.selectedRowData.getc);
		
		StringBuffer sb = new StringBuffer();
		sb.append("alterShip('");
		sb.append(polid!=null?this.selectedRowData.getPol():"");
		sb.append("','");
		sb.append(podid!=null?this.selectedRowData.getPod():"");
		sb.append("','");
		sb.append(polid!=null?this.selectedRowData.getPdd():"");
		sb.append("','");
		sb.append(polid!=null?this.selectedRowData.getPolid():"");
		sb.append("','");
		sb.append(podid!=null?this.selectedRowData.getPddid():"");
		sb.append("','");
		sb.append(polid!=null?this.selectedRowData.getPddid():"");
		sb.append("')");
		//
		//if(this.selectedRowData.getBookstate() == null) {
		//	this.selectedRowData.setBookstate("I");
		//}
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		Browser.execClientScript(sb.toString());
		shipscheduleWindow.close();
	}
	
	
	@Bind(id = "gridShipschedule", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.shipcosteditBean.gridShipschedule.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.shipcosteditBean.gridShipschedule.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	

	@Action
	public void qryRefreshShip2() {
		this.gridShipschedule.reload();
	}
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Action
	public void clearQryKeysc() {
		if (qryMapShip != null) {
			qryMapShip.clear();
			update.markUpdate(true, UpdateLevel.Data, "shipschedulePanel");
			this.gridShipschedule.reload();
		}
	}

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		return super.getQryClauseWhere(queryMap);
	}

	
	
	public Long getPolorPodId(String data) {
		String qry = "";
		if (StrUtils.isNull(data)) {
			return null;
		} else {
			qry = "SELECT id FROM dat_port WHERE (namee = '" + data
					+ "' OR  code = '" + data
					+ "' ) AND isdelete = FALSE limit 1";
		}

		try {
			Map map = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(qry);
			Long id = (Long) map.get("id");
			// ApplicationUtils.debug("id="+id);
			return id;

		} catch (Exception e) {
			return null;
		}

	}
	
	private Long getCarrierid(String carriercode) {

		try {
			String sql = "SELECT id FROM sys_corporation WHERE abbr = '"
					+ carriercode
					+ "' AND iscarrier = true AND isdelete = FALSE";
			Map m = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			Long carrierid = (Long) m.get("id");

			return carrierid;

		} catch (Exception e) {
			MessageUtils.alert("没找到对应的船公司id,请手动下拉");
			return null;
		}

	}
	
	public void setShipschedule() {
		if (this.selectedRowData.getScheduleid() == null) {
			shipschedule = new BusShipSchedule();
		} else {
			shipschedule = serviceContext.shipScheduleService.busShipScheduleDao
					.findById(selectedRowData.getScheduleid());
		}
	}


	@Override
	protected void doServiceFindData() {
		
	}


	@Override
	protected void doServiceSave() {
		
	}
	
	@Action
	public void creatingBooking() {
		if(0==selectedRowData.getId()) {
			MessageUtils.alert("请先保存订舱单再操作订舱!");
			return;
		}
		if(this.selectedRowData.getSono().trim().isEmpty()) {
			//MessageUtils.alert("SO号不能为空!");
			return;
		}
		
		this.selectedRowData.setBookstate("F");
//		this.creatingBooking.setDisabled(true);
		this.save.setDisabled(true);
		serviceContext.busBookingMgrService.busShipBookingDao.modify(this.selectedRowData);
		MessageUtils.alert("OK!");
		MessageServiceOfCreate();
	}
	
	private void MessageServiceOfCreate() {
		if(selectedRowData.getUserbook().isEmpty()) {
			MessageUtils.alert("订舱负责人为空,请检查!");
			return;
		}
		BusOrder busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(selectedRowData.getOrderid());
		SysUser su = serviceContext.userMgrService.sysUserDao.findById(busOrder.getSalesid());
		List<String> receiveCodes = new ArrayList<String>();
		receiveCodes.add(su.getCode());
		String remindTitle = "订舱消息提醒";
		String remindContext = AppUtils.getUserSession().getUsername()+"发来的订舱完成提醒";
		String url = AppUtils.getBasePath()+"pages/module/order/busorder.aspx?type=edit&id="+busOrder.getId();
		String sendContext = "订舱部:订单[url="+url+"]"+busOrder.getOrderno()
			+"[/url],订舱负责人"+selectedRowData.getUserbook()+"已确定SO，订舱已完成。";
		AppUtils.sendMessage(receiveCodes, remindTitle, remindContext, url, sendContext);
		MessageUtils.alert("订舱完成消息已推送!");
	}
	
	@Bind
	public UIWindow showAttachWindow;
	
	@Bind
	public UIIFrame attachIframe;
	
	@Action
	public void attachment(){
		attachIframe.load("/scp/pages/module/common/attachment.xhtml?linkid="+this.selectedRowData.getOrderid());
		showAttachWindow.show();
	}
}
