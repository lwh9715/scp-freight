package com.scp.view.module.customer;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

import com.scp.exception.NoRowException;
import com.scp.model.bus.BusAir;
import com.scp.model.bus.BusTrain;
import com.scp.model.order.BusOrder;
import com.scp.model.ship.BusShipping;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.SysUserAssign;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.assigneduserBean", scope = ManagedBeanScope.REQUEST)
public class AssignedUserBean extends GridFormView{

	@SaveState
	@Accessible
	public SysUserAssign selectedRowData = new SysUserAssign();
	
	@SaveState
	@Accessible
	public SysUser sysuser;
	
	@SaveState
	@Accessible
	@Bind
	public Long linkid = -1l;
	
	@SaveState
	@Accessible
	@Bind
	public Long jobid = -1l;
	
	@SaveState
	@Accessible
	@Bind
	public Long currentUserid;
	
	@SaveState
	@Accessible
	@Bind
	public String linktype = "X";
	
	@SaveState
	@Bind
	public String msg;
	
	
	@SaveState
	public String type = "";

	@Bind
	public UIButton refresh;
	@Bind
	public UIButton add;
	@Bind
	public UIButton del;
	@Bind
	public UIButton mark;
	@Bind
	public UIButton clear;
	@Bind
	public UIButton sendMsg;
	@Bind
	public UIButton showEmail;
	@Bind
	public UIButton oneKeyEmailNotify;
	@Bind
	public UIButton save;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			String flag =AppUtils.getReqParam("flag").trim();
			this.type =AppUtils.getReqParam("type").trim();
			if(!StrUtils.isNull(id)){
				linktype =AppUtils.getReqParam("linktype").trim();
				linkid=Long.valueOf(id);
				qryMap.put("linkid$", this.linkid);
				qryMap.put("linktype",linktype);
			}
			if(!"D".equals(linktype)){
//				sendMsg.setStyle("display:none");
//				sendMsg.setDisabled(true);
			}

			//获取jobid
			try {
				BusShipping busshipping = serviceContext.busShippingMgrService.busShippingDao.findById(linkid);
				Long jobid = 0l;
				Long orderid = 0l;
				String src0 = "jobs";
				BusOrder busOrder = null;
				if(busshipping == null){
					busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(linkid);
					if(busOrder != null){
						orderid = busOrder.getId();
						jobid = busOrder.getJobid();
						src0 = "order";
					}
				}else{
					jobid = busshipping.getJobid();
				}
				BusAir busair = null;
				if(busOrder == null){
					busair = serviceContext.busAirMgrService.busAirDao.findById(linkid);
					if(busair!=null){
						jobid = busair.getJobid();
					}
				}
				BusTrain bustrain = null;
				if(busair == null){
					bustrain = serviceContext.busTrainMgrService.busTrainDao.findById(linkid);
					if(bustrain!=null){
						jobid = bustrain.getJobid();
					}
				}
				BusTruck busTruck = null;
				if(bustrain == null){
					busTruck = serviceContext.busTruckMgrService.busTruckDao.findById(linkid);
					if(busTruck!=null){
						jobid = busTruck.getJobid();
					}
				}
				this.jobid = jobid;

				if ("customs".equals(flag)) {
					this.jobid = linkid;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			currentUserid = AppUtils.getUserSession().getUserid();
			this.update.markUpdate(UpdateLevel.Data, "currentUserid");
			initAdd();
			String src =AppUtils.getReqParam("src").trim();
			if("MyCustomer".equals(src)){
				if(ConfigUtils.findSysCfgVal("customer_management")!=null&&ConfigUtils.findSysCfgVal("customer_management").toString().equals("Y")){
					add.setDisabled(true);
					del.setDisabled(true);
					save.setDisabled(true);
				}
			}
			// this.grid.repaint();
		}

		String flag = AppUtils.getReqParam("flag");
		if ("CustomereditBean".equals(flag)) {
			String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
			try {
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
					refresh.setDisabled(true);
					add.setDisabled(true);
					del.setDisabled(true);
					mark.setDisabled(true);
					clear.setDisabled(true);
					sendMsg.setDisabled(true);
					showEmail.setDisabled(true);
					oneKeyEmailNotify.setDisabled(true);
					save.setDisabled(true);
				}
			} catch (Exception e) {
			}
		}
		try {
			if("jobs".equals(this.type)){
				initCrop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@SaveState
//	public List<Map> list = new ArrayList<Map>();
	
	@Bind
	private UIIFrame linkCorpIFrame;
	
	/**
	 * 初始化操作公司
	 */
	public void initCrop() {
		String url = "./finacorp.xhtml?jobid="+this.jobid;
		linkCorpIFrame.setSrc(url);
		update.markAttributeUpdate(linkCorpIFrame, "src");
	}


	protected void initAdd() {
		try {
			selectedRowData = new SysUserAssign();

			if ("jobs".equals(type) && jobid>10000) {
				String sql = "select abbcode\n" +
						"from sys_corporation\n" +
						"WHERE iscustomer = false\n" +
						"  and id = (select corpid from sys_user su where id = (select saleid from fina_jobs fj where isdelete = false and id = " + jobid + "));";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				selectedRowData.setRolearea(StrUtils.getMapVal(m, "abbcode"));
			}

			Browser.execClientScript("$('#sales_input').val('')");
			this.update.markUpdate(UpdateLevel.Data, "editPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void add() {
		super.add();
		initAdd();
	}

	

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysUserAssignMgrService.removeDate(Long.parseLong(id));
			}
			Browser.execClientScript("showmsg()");
//			this.add();
			this.grid.reload();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	

	@Override
	protected void doServiceFindData() {
		Long id = this.getGridSelectId();
		if(id<=0)return;
		this.selectedRowData = serviceContext.sysUserAssignMgrService.sysUserAssignDao.findById(id);
		if(this.selectedRowData == null)return;
		Long userid = selectedRowData.getUserid();
		if(userid!=null){
			SysUser sysuser = serviceContext.userMgrService.sysUserDao.findById(userid);
			if(sysuser != null)Browser.execClientScript("$('#sales_input').val('"+sysuser.getNamec()+"')");
		}else{
			Browser.execClientScript("$('#sales_input').val('')");
		}
	}

	@Override
	protected void doServiceSave() {
		selectedRowData.setLinkid(this.linkid);
		selectedRowData.setLinktype(linktype);
		try {
			serviceContext.sysUserAssignMgrService.saveData(selectedRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		add();
	}



	@Override
	public void refresh() {
		this.grid.reload();
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void copyadd(){
		this.pkVal = -100L;
		SysUserAssign demp = selectedRowData;
		SysUserAssign newobj = new SysUserAssign();
		newobj.setLinkid(demp.getLinkid());
		newobj.setLinktype(demp.getLinktype());
		newobj.setUserid(demp.getUserid());
		newobj.setRolearea(demp.getRolearea());
		newobj.setRoletype(demp.getRoletype());
		newobj.setIsdefault(demp.getIsdefault());
		newobj.setPolid(demp.getPolid());
		this.selectedRowData = newobj;
		this.update.markUpdate(true, UpdateLevel.Data, "pkId");
	}
	
	
	@Action
	public void sendMsg(){
		String[] ids = this.grid.getSelectedIds();
		if(ids==null || ids.length == 0){
			MessageUtils.alert("请至少选中一条,按住Ctrl可多选");
			return;
		}
		try {
			String sql = "";
			if("D".equals(linktype)){
				sql = "SELECT a.userid,"+
					"\n(SELECT namec FROM sys_user x WHERE id = a.userid AND x.isdelete = false),(SELECT namee FROM sys_user x WHERE id = a.userid AND x.isdelete = false),orderno,a.id"+ 
					"\nFROM bus_order j, sys_user_assign a"+
					"\nWHERE a.linkid = j.id"+
					"\nAND a.id IN ("+StrUtils.array2List(ids)+")";
			}else if("C".equals(linktype)){ //客户
				sql = "SELECT a.userid,"+
					"\n(SELECT namec FROM sys_user x WHERE id = a.userid AND x.isdelete = false),(SELECT namee FROM sys_user x WHERE id = a.userid AND x.isdelete = false),'' AS orderno,a.id"+ 
					"\nFROM sys_user_assign a"+
					"\nWHERE a.id IN ("+StrUtils.array2List(ids)+")";
			}else{
				sql = "SELECT a.userid,"+
					"\n(SELECT namec FROM sys_user x WHERE id = a.userid AND x.isdelete = false),(SELECT namee FROM sys_user x WHERE id = a.userid AND x.isdelete = false),j.nos,a.id"+ 
					"\nFROM fina_jobs j,bus_shipping y , sys_user_assign a"+
					"\nwhere y.jobid = j.id "+
					"\nAND a.linkid = y.id"+
					"\nAND a.id IN ("+StrUtils.array2List(ids)+")";
			}
			List<Map> lists = this.serviceContext.sysUserAssignMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for (Map map : lists) {
				if(map != null && map.size() > 0){
					Long uid = serviceContext.userMgrService.sysUserDao.findById(
							AppUtils.getUserSession().getUserid()).getFmsid();
					String id = map.get("id").toString();
			    	String namec = StrUtils.getMapVal(map, "namec");
					String namee = StrUtils.getMapVal(map, "namee");
					
					if(StrUtils.isNull(namec) && StrUtils.isNull(namee)){
						MessageUtils.alert("No user assign!");
						return;
					}
					
					String orderno = "";
					if("D".equals(linktype)){
						orderno=map.get("orderno").toString();
					}else if("C".equals(linktype)){
						orderno="";
					}else{
						orderno=map.get("nos").toString();
					}
					
					if(null == msg)msg="";
					msg = msg.trim();
					
					if("D".equals(linktype)){
						serviceContext.sysUserAssignMgrService.sendMessageToCustomerService(AppUtils.getUserSession().getUserid(),uid,id,orderno,AppUtils.getUserSession().getUsername(),AppUtils.getBasePath(),namec,namee , msg);
					}else if("C".equals(linktype)){
						AppUtils.sendIMMsg(AppUtils.getUserSession().getUserid().toString(), uid.toString(), msg);
					}else{
						serviceContext.sysUserAssignMgrService.sendMessageToCustomerServicejob(AppUtils.getUserSession().getUserid(),uid,id,orderno,AppUtils.getUserSession().getUsername(),AppUtils.getBasePath(),namec,namee , msg);
					}
					if(map.get("userid")!=null){
						serviceContext.sysUserAssignMgrService.sendweixin(map.get("userid").toString(),orderno,AppUtils.getUserSession().getUsername(),msg);
					}
				}
			}
			Browser.execClientScript("showmsg()");
		} catch (NoRowException e) {
			e.printStackTrace();
			MessageUtils.alert("Please choose one!");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", AppUtils.getUserSession().getUserid());
		String filter = "\nAND linkid = " + linkid + " AND linktype = '" + linktype + "'";
		map.put("filter", filter);
		return map;
	}
	
	@Bind
	public UIWindow showUserWindow;
	
	@Action
	public void showsysUser(){
		String id = AppUtils.getReqParam("id");
		String sql = "SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign WHERE id = "+id+" AND userid = x.id)";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(m!=null&&m.size()>0&&m.get("id")!=null){
				String uid = m.get("id").toString();
				sysuser = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(uid));
				showUserWindow.show();
				this.update.markUpdate(true,UpdateLevel.Data, "showUserPanel");
			}
		} catch (Exception e) {
		}
	}
	
	@Bind
	public UIIFrame sendEmailIframe;
	
	@Bind
	public UIWindow sendEmailWindow;
	
	@Action
	public void sendEmail(){
		sendEmailIframe.load("../../sysmgr/mail/emailedit.xhtml?addressee="+sysuser.getEmail1()+";"+sysuser.getEmail2());
		sendEmailWindow.show();
	}
	
	@Bind
	public UIIFrame emailIframe;
	
	@Bind
	public UIWindow showEmailWindow;
	
	/**
	 * 显示邮件页面
	 */
	@Action
	public void showEmail() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("请至少勾选一条");
			return;
		}else{
			try {
				BusShipping busshipping = serviceContext.busShippingMgrService.busShippingDao.findById(linkid);
				Long jobid = 0l;
				Long orderid = 0l;
				String src = "jobs";
				BusOrder busOrder = null;
				if(busshipping == null){
					busOrder = serviceContext.busOrderMgrService.busOrderDao.findById(linkid);
					if(busOrder != null){
						orderid = busOrder.getId();
						jobid = busOrder.getJobid();
						src = "order";
					}
				}else{
					jobid = busshipping.getJobid();
				}
				BusAir busair = null;
				if(busOrder == null){
					busair = serviceContext.busAirMgrService.busAirDao.findById(linkid);
					if(busair!=null){
						jobid = busair.getJobid();
					}
				}
				BusTruck busTruck = null;
				if(busair == null){
					busTruck = serviceContext.busTruckMgrService.busTruckDao.findById(linkid);
					if(busTruck!=null){
						jobid = busTruck.getJobid();
					}
				}
				/*emailIframe.load("/scp/pages/sysmgr/mail/emailedit.aspx?type=A&id="
								+ "-1" + "&src=" + src
								+ "&jobid=" + jobid + "&orderid="+orderid
								+ "&userids=" + StrUtils.array2List(ids));
				showEmailWindow.show();*/
				
                this.jobid = jobid;
				String url = AppUtils.getContextPath();
				String openurl = url + "/pages/sysmgr/mail/emailedit.aspx";
				AppUtils.openWindow("_showEmail", openurl + "?type=A&id="
								+ "-1" + "&src=" + src
								+ "&jobid=" + jobid + "&orderid="+orderid
								+ "&userids=" + StrUtils.array2List(ids));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	@Action
	public void showAssigner(){
		String id = AppUtils.getReqParam("id");
		try {
			this.selectedRowData = serviceContext.sysUserAssignMgrService.sysUserAssignDao.findById(Long.parseLong(id));
			if(this.selectedRowData == null)return;
			Long userid = selectedRowData.getUserid();
			if(userid!=null){
				SysUser sysuser = serviceContext.userMgrService.sysUserDao.findById(userid);
				if(sysuser != null)Browser.execClientScript("$('#sales_input').val('"+sysuser.getNamec()+"')");
			}else{
				Browser.execClientScript("$('#sales_input').val('')");
			}
				editWindow.show();
				this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
		} catch (Exception e) {
		}
	}
	
	@Action
	public void mark() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			serviceContext.sysUserAssignMgrService.mark(Long.parseLong(ids[0]),AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getUsername());
			this.grid.reload();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void clear() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			serviceContext.sysUserAssignMgrService.clear(Long.parseLong(ids[0]),AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getUsername());
			this.grid.reload();
		} catch (NumberFormatException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Action
	public void oneKeyEmailNotify(){
		try {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要一键通知的行");
				return;
			}
			String querySql = "SELECT f_createmail_jobassign('shipid="+linkid+";userid="+AppUtils.getUserSession().getUserid()+";ids="+ StringUtils.join(ids, ",")+"');";
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

}
