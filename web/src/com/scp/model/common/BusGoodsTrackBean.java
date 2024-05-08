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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusGoodstrack;
import com.scp.model.sys.CsUser;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.JSONUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.wx.WeiXinUtil;

@ManagedBean(name = "pages.module.common.goodstrackBean", scope = ManagedBeanScope.REQUEST)
public class BusGoodsTrackBean extends GridFormView {

	
	@SaveState
	@Accessible
	public BusGoodstrack selectedRowData = new BusGoodstrack();
	
	@SaveState        
	@Bind             
	public Long fkid; 
	
	@SaveState        
	@Bind             
	public Date dealdate;
	
	
	@SaveState        
	@Bind             
	public Boolean isAutoRefreshTime = false;
	
	@SaveState        
	@Bind             
	public String childjobCount;

	@Override
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {			
			String id = AppUtils.getReqParam("fkid").trim();
			MLType mlType = AppUtils.getUserSession().getMlType();
			if(!StrUtils.isNull(id)) {
				fkid = Long.valueOf(id);
				String jobno = "";
				try {
					FinaJobs jobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(fkid);
					jobno = jobs.getNos();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.update.markUpdate(UpdateLevel.Data, "fkid");
				trackIframe.load("/common/goodstrack.jsp?fkid="+this.fkid+"&language="+mlType+"&userid="+AppUtils.getUserSession().getUserid()+"&jobno="+jobno);
			}  
			dealdate = Calendar.getInstance().getTime();
			isAutoRefreshTime = true;
			refresh();
			String goodstrack_isShowIscs = ConfigUtils.findUserCfgVal("goodstrack_isShowIscs",AppUtils.getUserSession().getUserid());
			if(!StrUtils.isNull(goodstrack_isShowIscs)&&"true".equals(goodstrack_isShowIscs)){
				isShowIscs = true;
			}
			
			childjobCount = this.serviceContext.jobsMgrService.findChildjobCount(this.fkid);
		}
	}
	
	
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
//		String lang = StrUtils.getMapVal(m, "lang");
		MLType mlType = AppUtils.getUserSession().getMlType();
		//qry += "\nAND (fkid= " + fkid + " or fkid = (select x.parentid from fina_jobs x where id= " + fkid + " ))";
		//qry += "\nAND dealdata<=now()";
		qry += "\nAND (fkid= " + fkid+ ")";
		
		String lang = mlType.name();
		if(StrUtils.isNull(lang))lang="ch";
		lang = ""+lang+"";
		String goodstrack_isShowIscs = ConfigUtils.findUserCfgVal("goodstrack_isShowIscs",AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(goodstrack_isShowIscs)||"false".equals(goodstrack_isShowIscs)){
			qry += "\nAND iscs = FALSE";
		}
		Long userid = AppUtils.getUserSession().getUserid();
		if(userid == null)userid=0l;
		m.put("qry", qry);
		m.put("lang", lang);
		m.put("userid", userid);
		//System.out.println("BusGoodsTrackBean:"+m);
		return m;
	}

	

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = this.serviceContext.busGoodstrackMgrService.busGoodstrackDao
				.findById(this.pkVal);
		if(this.selectedRowData !=null &&this.selectedRowData.getAssignuserid()!=null){
			SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getAssignuserid());
			if(sysUser!=null){
				Browser.execClientScript("$('#priceuser_input').val('"+sysUser.getNamec()+"')");
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
	}

	@Override
	protected void doServiceSave() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		    String dateString = formatter.format(dealdate);
			selectedRowData.setDealdate(dateString);
			this.serviceContext.busGoodstrackMgrService.saveData(selectedRowData);
//			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	
	@Action
	public void del(){
		String ids[] = this.grid.getSelectedIds();
		MLType mlType = AppUtils.getUserSession().getMlType();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请选择删除的行");
			return;
		}
		try {
			this.serviceContext.busGoodstrackMgrService.removeDates(ids);
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		trackIframe.load("/common/goodstrack.jsp?fkid="+this.fkid+"&language="+mlType);
	}

	
	
	@Action
	public void finishtrue(){
		finish(true);
	}
	
	private void finish(boolean flag){
		String ids[] = this.grid.getSelectedIds();
		MLType mlType = AppUtils.getUserSession().getMlType();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请选择完成的行");
			return;
		}
		try {
			for (String id : ids){
				this.serviceContext.busGoodstrackMgrService.finishDate(id , flag , isAutoRefreshTime , AppUtils.getUserSession().getUsercode());
				sendWeiXin();
				refresh();
			}
		} catch (Exception e) {
//			MsgUtil.showException(e);
		}
		trackIframe.load("/common/goodstrack.jsp?fkid="+this.fkid+"&language="+mlType);
	}

	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.serviceContext.busGoodstrackMgrService.
		finishDate(String.valueOf(pkVal) , !this.selectedRowData.getIsfinish() , isAutoRefreshTime , AppUtils.getUserSession().getUsercode());
//		sendWeiXin();
		this.qryRefresh();
		Browser.execClientScript("refresh()");
	}
	
	public void sendWeiXin(){
		try {
			this.pkVal = getGridSelectId();
			BusGoodstrack busGoodstrack = serviceContext.busGoodstrackMgrService.busGoodstrackDao.findById(this.pkVal);//当前步骤
			Long assignuserid = busGoodstrack.getAssignuserid();
			if(assignuserid != null){//当下一步有指派人时发送
				String iswexin = ConfigUtils.findUserCfgVal("subscribe_type_weixin", assignuserid);
				String isjobstatus = ConfigUtils.findUserCfgVal("subscribe_msg_jobstatus", assignuserid);
				if(iswexin!=null&&iswexin.equals("Y")&&isjobstatus.equals("Y")){//个人设置中如果勾选工作单状态通知和微信通知则发送消息到个人微信中
					String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
					String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
					
					SysUser user = serviceContext.userMgrService.sysUserDao.findById(assignuserid);
					FinaJobs job = serviceContext.jobsMgrService.finaJobsDao.findById(fkid);
					String openid = (user!=null?user.getOpneid():"");
			    	//String templateid = "Mbj3RAg_rg4o-QFj1xabWPJG_Q1jB-qQoUPn7BjsHMs";
	//		    	String json = JSONUtil.sendWeixin(openid, templateid, "亲，您的工作单有最新进展", job.getNos(), "工作单下一步："+map.get("statusc")
	//		    			, map.get("statusc").toString(), user.getNamec());
			    	String weixinTemplatejobs = ConfigUtils.findSysCfgVal("weixin_template_jobs");
			    	SysUser updateuser = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere(" code ='"+busGoodstrack.getUpdater()+"'");
			    	String json1 = JSONUtil.sendWeixinTemplate(null,openid, weixinTemplatejobs, "亲，您的工作单有最新进展"
			    			, job.getNos(), (busGoodstrack.getIsfinish()==false?"取消":"")+busGoodstrack.getStatusc()
			    			, new SimpleDateFormat("yyyy- MM-dd HH:mm:ss").format(new Date())
			    			, busGoodstrack.getUpdater(), updateuser!=null&&updateuser.getTel1()!=null?updateuser.getTel1():"", "");
					String response = WeiXinUtil.messageSendByTemplate(appID, appsecret, json1);
				}
			}
			if(busGoodstrack.getIscs() && busGoodstrack.getIsfinish()){
				String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
				String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
				
				FinaJobs job = serviceContext.jobsMgrService.finaJobsDao.findById(fkid);
				List<CsUser> users = serviceContext.userMgrService.csUserDao.findAllByClauseWhere("corpid = " + job.getCustomerid() + " AND openid is NOT NULL AND openid <> ''");
				for (CsUser csUser : users) {
					String openid = (csUser!=null?csUser.getOpenid():"");
			    	String weixinTemplatejobs = ConfigUtils.findSysCfgVal("weixin_template_jobs");
			    	SysUser updateuser = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere(" code ='"+busGoodstrack.getUpdater()+"'");
			    	String json1 = JSONUtil.sendWeixinTemplate(null,openid, weixinTemplatejobs, "工作单进度"
			    			, job.getNos(),busGoodstrack.getStatusc()
			    			, new SimpleDateFormat("yyyy- MM-dd HH:mm:ss").format(new Date())
			    			, busGoodstrack.getUpdater(), updateuser!=null&&updateuser.getTel1()!=null?updateuser.getTel1():"", "");
					String response = WeiXinUtil.messageSendByTemplate(appID, appsecret, json1);
				}
			}
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Action
	public void finishfalse(){
		finish(false);
	}
	


	@Override
	public void add() {
		selectedRowData = new BusGoodstrack();
		super.add();
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    String dateString = formatter.format(currentTime);
		this.selectedRowData.setDealdate(dateString);
		this.selectedRowData.setFkid(fkid);
	}
	
	
	@Bind
	public UIWindow importWindow;
	
	@Bind
	public UIIFrame importIframe;
	
	
	@Bind
	public UIIFrame trackIframe;
	
	
	@Action
	public void importTemplate(){
		importIframe.load("./goodstracktemplate.xhtml?fkid="+this.fkid+"&imtype=import");
		importWindow.show();
	}
	
	@Action
	public void closeWindow(){
		this.grid.reload();
		importWindow.close();
		Browser.execClientScript("refresh()");
	}

	@Action
	public void processStatusEdit(){
		String id = AppUtils.getReqParam("id");
		if(!StrUtils.isNull(id) && StrUtils.isNumber(id)){
			this.pkVal = Long.valueOf(id);
			doServiceFindData();
		}
		editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
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
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void deluser(){
		this.selectedRowData.setAssignuserids("");
		user = "";
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Bind
	public UIWindow hxWindow;
	
	@Action
	public void onlineFeedLink(){
		String jobno = "";
		try {
			FinaJobs jobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(fkid);
			jobno = jobs.getNos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		long currentTimeMillis = System.currentTimeMillis();//时间戳
		String csUrlbase = ConfigUtils.findSysCfgVal("cs_url_base");
		String csUrlbasetime = ConfigUtils.findSysCfgVal("cs_url_base_time");//系统中URL有效时间
		int parseInt = csUrlbasetime==null||csUrlbasetime.equals("")?3:Integer.parseInt(csUrlbasetime);
		String url = csUrlbase+"tracking.html?jobno="+jobno;
		sb.append(url);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateformat.format(currentTimeMillis+parseInt * 60 * 60 * 1000).toString();
		sb.append("\n\n链接有效时间至："+time) ;
		update.markUpdate(true,UpdateLevel.Data,"hxurl");
		hxWindow.show();
		String js = "testUrl.setValue('"+url+"');time.setValue('"+time+"');refreshQRCode();";
		Browser.execClientScript(js);
	}
	
	@Bind
	@SaveState
	private Boolean isShowIscs = false;
	
	@Action
	public void isShowIscsAjax(){
		try {
			isShowIscs = !isShowIscs;
			ConfigUtils.refreshUserCfg("goodstrack_isShowIscs",(isShowIscs?"true":""), AppUtils.getUserSession().getUserid());
			this.update.markUpdate(UpdateLevel.Data, "isShowIscs");
			qryRefresh();
			Browser.execClientScript("refresh()");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.update.markUpdate(UpdateLevel.Data, "isShowIscs");
	}
	
	
	@Action
	public void synchroChild(){
		if (this.fkid == -1l){
			MessageUtils.alert("工作单未保存");
			return;
		}
		try {
			String sql = "SELECT f_bus_goodstrack_syn_child('jobid="+fkid+";user="+AppUtils.getUserSession().getUsercode()+"');";
			serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
