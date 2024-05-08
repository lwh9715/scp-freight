package com.scp.model.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.operamasks.faces.user.util.Browser;

import com.scp.model.ship.BusGoodstrack;
import com.scp.model.ship.BusGoodstrackTemp;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.common.goodstracktemplateBean", scope = ManagedBeanScope.REQUEST)
public class GoosTrackTemplateBean extends GridFormView {

	@SaveState
	@Accessible
	public BusGoodstrackTemp selectedRowData = new BusGoodstrackTemp();
	
	
	@SaveState
	@Accessible
	public Long linkid;
	
	@SaveState
	@Bind
	public Long fkid;
	
	@SaveState
	@Bind
	public  String type;
	
	@SaveState
	@Bind
	public String imtype;//用于区分是从工作单里面进来的还是主页导航栏进来的
	
//	@SaveState        
//	@Bind             
//	public String blno; 
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id = AppUtils.getReqParam("fkid").trim();	
			 type = AppUtils.getReqParam("type").trim();
			imtype = AppUtils.getReqParam("imtype").trim();;
			if(!StrUtils.isNull(id)) {
				fkid = Long.valueOf(id);
				this.update.markUpdate(UpdateLevel.Data, "fkid");
				//trackIframe.load("../common/busgoodstrack.xhtml?fkid="+fkid);
			}
				this.qryMap.put("tmptype",type);
		}
	}
	

	@Override
	public void add() {
		super.add();
		selectedRowData = new BusGoodstrackTemp();
		selectedRowData.setTmptype(type);
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
		this.update.markUpdate(UpdateLevel.Data, "tmptype");
	}
	
	
	@Action
	public void addCopy(){
		BusGoodstrackTemp old = selectedRowData;
		selectedRowData = new BusGoodstrackTemp();
		selectedRowData.setIsauto(old.getIsauto());
		selectedRowData.setIssys(old.getIssys());
		selectedRowData.setLocationc(old.getLocationc());
		selectedRowData.setLocatione(old.getLocatione());
		selectedRowData.setOrderno(old.getOrderno()+10);
		
		selectedRowData.setStatusc(old.getStatusc());
		selectedRowData.setStatuse(old.getStatuse());
		selectedRowData.setTmptype(old.getTmptype());
		selectedRowData.setAssigntype(old.getAssigntype());
		
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
		this.update.markUpdate(UpdateLevel.Data, "tmptype");
	}
	

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			createNow();
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
//				 String type = querychoose(Long.valueOf(id),fkid);
//				 if(!type.equals("F")){
//					 MessageUtils.alert(type);
//					 return;
//				 }
				serviceContext.busGoodstrackTempMgrService.removeDate(Long.parseLong(id));
			}
			MessageUtils.alert("OK!");
//			this.add();
			this.grid.reload();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void remove(){
		serviceContext.busGoodstrackTempMgrService.BusGoodstrackTempDao.remove(selectedRowData);
		add();
		MessageUtils.alert("OK!");
	}
	
	
	
	@Action
	public void saveTempAndClose(){
		this.saveTemp();
		Browser.execClientScript("parent.closeWindow.submit()");
	}
	

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.busGoodstrackTempMgrService.BusGoodstrackTempDao.findById(this.pkVal);
		if(this.selectedRowData !=null &&this.selectedRowData.getAssignuserid()!=null){
			SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getAssignuserid());
			if(sysUser!=null){
				Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
			}else{
				Browser.execClientScript("$('#priceuser_input').val('')");
			}
		}else{
			Browser.execClientScript("$('#priceuser_input').val('')");
		}
		String findUser = selectedRowData.getAssignuserids();
		if(!StrUtils.isNull(findUser)){
			findUser = findUser.substring(0, findUser.length()-1);
			String sql = "SELECT string_agg(namec,',') AS namec from sys_user where isdelete = false AND id in("+findUser+")";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
			this.user = StrUtils.getMapVal(map, "namec");
		}else{
			this.user = "";
		}
		this.update.markUpdate(true , UpdateLevel.Data, "detailsPanel");
	}

	@Override
	protected void doServiceSave() {
		try {
//			 String type = querychoose(selectedRowData.getId(),fkid);
//			 if(!type.equals("F")){
//				 MessageUtils.alert(type);
//				 return;
//			 }
			serviceContext.busGoodstrackTempMgrService.saveData(selectedRowData);
			
			//MessageUtils.alert("ok");
			this.grid.reload();
			//add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	
	


	@Override
	public void refresh() {
		super.refresh();
//		this.update.markUpdate(UpdateLevel.Data, "editPanel");
//		trackIframe.load("/web/blno_query.jsp?blno="+blno);
//		trackIframe.load("../air/busgoodstrack.xhtml?fkid="+fkid);
	}
	
	@Bind
	@SaveState
	public Date trackTime = new Date();
	
	@Action
	public void saveTemp(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		Map<String,Object> m = this.grid.getAttributes();
		try {
			for(String id : ids) {
				linkid = Long.parseLong(id);
//				 String type = querychoose(linkid,fkid);
//				 if(!type.equals("F")){
//					 MessageUtils.alert(type);
//					 return;
//				 }
				BusGoodstrack busGoodstrack = new BusGoodstrack();
				busGoodstrack.setFkid(fkid);
				busGoodstrack.setLinkid(linkid);
				createNow();
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			    String dateString = formatter.format(trackTime);
//				busGoodstrack.setDealdate(dateString);
				BusGoodstrackTemp busGoodstrackTemp = this.serviceContext.busGoodstrackTempMgrService.BusGoodstrackTempDao.findById(linkid);
				try {
					busGoodstrack.setLocationc(busGoodstrackTemp.getLocationc());
					busGoodstrack.setLocatione(busGoodstrackTemp.getLocatione());
					busGoodstrack.setStatusc(busGoodstrackTemp.getStatusc());
					busGoodstrack.setStatuse(busGoodstrackTemp.getStatuse());
					busGoodstrack.setOrderno(busGoodstrackTemp.getOrderno());
					busGoodstrack.setAssignuserid(busGoodstrackTemp.getAssignuserid());
					busGoodstrack.setAssigntype(busGoodstrackTemp.getAssigntype());
					busGoodstrack.setIscs(busGoodstrackTemp.getIscs());
					busGoodstrack.setAssignuserids(busGoodstrackTemp.getAssignuserids());
					busGoodstrack.setIsinner(busGoodstrackTemp.getIsinner());
				} catch (Exception e) {
					return;
				}
//				busGoodstrack.setDealdate(dateString);
				
				serviceContext.busGoodstrackMgrService.saveData(busGoodstrack);
			}
			//MessageUtils.alert("OK!");
			this.update.markUpdate(UpdateLevel.Data, "editPanel");			
			//trackIframe.load("../air/busgoodstrack.xhtml?fkid="+fkid);
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 处理数据库now()跟实际时间相差几分钟
	 */
	public void createNow(){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(trackTime);
		 cal.add(Calendar.MINUTE, +1);
		 trackTime = cal.getTime();
	}
	
	//@Bind
	//public UIIFrame trackIframe;
	
	

//	/**
//	 * 判断是否已选择过
//	 */
//	public String querychoose(Long linkid,Long jobid){
//		String sql = "SELECT ( CASE WHEN EXISTS(SELECT 1 FROM bus_goodstrack WHERE linkid = "+linkid+" AND fkid = "+jobid+") THEN (SELECT '地址(中):'||locationc||'地址(英文):'||locatione||'已勾选,已勾选选择过的数据不能再进行选择,修改,删除!' FROM bus_goodstrack WHERE linkid = "+linkid+" AND fkid = "+jobid+") ELSE 'F' END)AS type ";
//		 Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//		  String type = (String)map.get("type");
//		  return type;
//	}

	@Override
	public void grid_ondblclick() {
		if(StrUtils.isNull(imtype)){
			super.grid_ondblclick();
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		m.put("userid", AppUtils.getUserSession().getUserid());
		return m;
	}
	
	@Bind
	public UIDataGrid gridUser;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Bind
	@SaveState
	public String nextAssignUser = "";
	
	@Bind
	@SaveState
	public String user = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.nextAssignUser.contains(id)){
				this.nextAssignUser = nextAssignUser + id +",";
				this.selectedRowData.setAssignuserids(nextAssignUser);
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "detailsPanel");
	}
}
