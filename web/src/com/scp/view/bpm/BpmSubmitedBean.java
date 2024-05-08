package com.scp.view.bpm;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bpm.BpmTask;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;


/**
 * 
 * 待办工单列表 
 * @author neo
 */
@ManagedBean(name = "bpm.bpmsubmitedBean", scope = ManagedBeanScope.REQUEST)
public class BpmSubmitedBean extends GridFormView{
	
	public Long userid;
	
	public String language;
	
	@Bind
	@SaveState
	public String actorerwhere;
	
	@Bind
	@SaveState
	public Boolean isclude = true;
	
	@Bind
	@SaveState
	private String isbustruck="";

	@Bind
	@SaveState
	private String isbuscustoms="";

	@Bind
	@SaveState
	private String isvgm="";

	@Bind
	@SaveState
	private String isso="";

	@Bind
	@SaveState
	private String isfeeding="";

	@Bind
	@SaveState
	private String isbltype="";

	@Bind
	@SaveState
	private String istelrelv="";

	@Bind
	@SaveState
	private String etdstart;

	@Bind
	@SaveState
	private String etdend;

	@Bind
	@SaveState
	private String bargeetdstart;

	@Bind
	@SaveState
	private String bargeetdend;

	@Bind
	@SaveState
	private String vgmdatestart;

	@Bind
	@SaveState
	private String vgmdateend;

	@Bind
	@SaveState
	private String clstimestart;

	@Bind
	@SaveState
	private String clstimeend;

	@Bind
	@SaveState
	private String sidatestart;

	@Bind
	@SaveState
	private String sidateend;

	@Bind
	@SaveState
	private String jobno;

	@Bind
	@SaveState
	private String sono;

	@Bind
	@SaveState
	private String cntno;

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		language = AppUtils.getUserSession().getMlType().toString();
		if (!isPostBack) {
			super.applyGridUserDef();
//			this.qryMap.put("actorid",AppUtils.getUserSession().getUsercode());
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			//this.gridLazyLoad = true;
		}

	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", AppUtils.getUserSession().getUserid());
		map.put("args", "'userid="+AppUtils.getUserSession().getUserid()+"'");
		String qry = map.get("qry").toString();


		String userCode = AppUtils.getUserSession().getUsercode();


		map.put("ORDER", "ORDER BY t.nos desc,t.displayname,t.taskname");

		String filter = "\nAND t.id = (SELECT MAX(id) FROM _bpm_task x WHERE x.processinstanceid = t.processinstanceid AND t.instancestate <> 8 AND t.instancestate <> 9 AND state <> 3)";
		filter += "\nAND (CASE WHEN (state = 2 AND actorid = '"+userCode+"') THEN FALSE ELSE TRUE END)";

		filter += "\nAND t.procestartedtime > (NOW() + '-3Month' )";

		String existsSql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole a,sys_role b WHERE a.roleid =  b.id AND a.userid = " + AppUtils.getUserSession().getUserid()+" AND b.code = 'dev') AS existflag";
		Map existsMap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(existsSql);
		//if(existsMap != null && "true".equals(StrUtils.getMapVal(existsMap, "existflag"))){//20200109 neo 开发测试组可以看到所有的
			//if(!StrUtils.isNull(StrUtils.getMapVal(qryMap, "actorid"))){
			//	qry += "\n AND t.actorid = '" + AppUtils.getUserSession().getUsercode() + "'";
			//}
			map.put("ORDER", ""); //Neo 20200109 如果查全部，去掉排序，非常慢
		//}else{
			String f1 = "\nAND t.processinstanceid = ANY( " +
					"\n										SELECT yy.id FROM bpm_processinstance yy  " +
					"\n										WHERE yy.createduser = ANY( " +
					"\n																	SELECT  '" + userCode + "' " +
					"\n																	UNION " +
					"\n																	SELECT y.code FROM sys_configuser x , sys_user y WHERE x.key = 'process_handling_standbyid' AND '" + AppUtils.getUserSession().getUserid() +"' = ANY(string_to_array(x.val,',')) AND y.id = x.userid " +
					"\n																) " +
					"\n										UNION " +
					"\n										SELECT xx.id FROM bpm_processinstance xx , sys_user_assign a , bus_shipping s " +
					"\n										WHERE  s.jobid = (regexp_split_to_array(xx.refid,',')::BIGINT[])[1]  " +
					"\n											AND a.linktype = 'J' " +
					"\n											AND s.id = a.linkid " +
					"\n											AND a.isdelete = FALSE " +
					"\n											AND a.userid =  " + AppUtils.getUserSession().getUserid() +
					"\n											AND xx.refid IS NOT NULL " +
					"\n											AND xx.refid <> '' " +
					"\n										)" +
			"";
			filter += f1;			
			
//			filter += 
//				 "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'bpm_task_filterbyorg' AND val = 'Y') THEN "
//				+"\n	t.actorid = ANY(ARRAY(WITH RECURSIVE rc AS ("
//				+"\n		SELECT * FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()
//				+"\n		UNION "
//				+"\n		SELECT a.* FROM sys_user a,rc WHERE  a.parentid = rc.id"
//				+"\n	)"
//				+"\n	SELECT code FROM rc ))"
//				+"\n ELSE " 
//				+"\n 	t.actorid = '" + AppUtils.getUserSession().getUsercode() + "'"
//				+"\n END)";
//		}
		map.put("filter", filter);
		

		if(!StrUtils.isNull(actorerwhere) && isclude == true){
			qry += "AND EXISTS(SELECT 1 FROM sys_user x WHERE  x.code = t.actorid AND (namec ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%' " +
					"OR EXISTS(SELECT 1 FROM sys_department WHERE x.deptid = id AND name ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%')))";
		}else if(!StrUtils.isNull(actorerwhere) && isclude == false){
			qry += "AND NOT EXISTS(SELECT 1 FROM sys_user x WHERE  x.code = t.actorid AND (namec ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%' " +
			"OR EXISTS(SELECT 1 FROM sys_department WHERE x.deptid = id AND name ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%')))";
		}
		
		
		map.put("qry", qry);
		//高级检索
		String dynamicClauseWhere  ="";
		if(!StrUtils.isNull(isbustruck)){
			dynamicClauseWhere += "\n AND "+(isbustruck.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM bus_truck WHERE jobid = j.id AND isdelete = false)";
			dynamicClauseWhere += "\n AND EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.isdelete = false" +
								  "\n AND EXISTS(SELECT 1 FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 110 AND " +
								  "\n s.trucktype = d.code AND d.namec = '我司安排'))";
		}
		if(!StrUtils.isNull(isbuscustoms)){
			dynamicClauseWhere += "\n AND "+(isbuscustoms.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM bus_customs WHERE jobid = j.id AND isdelete = false)";
			dynamicClauseWhere += "\n AND EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.isdelete = false" +
								  "\n AND EXISTS(SELECT 1 FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 40 AND " +
								  "\n s.custype = d.code AND d.namec = '我司安排'))";
		}
		//VGM
		if(!StrUtils.isNull(isvgm)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id " +
								 "AND s.vgmstatus ='"+(isvgm.equals("Y")?"A":"U")+"')";
		}
		//SO/补料/提单正本：工作单附件，存在文件组类型：SO  原始补料  提单正本
		if(!StrUtils.isNull(isso)){
			dynamicClauseWhere += "\nAND "+(isso.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM sys_attachment x WHERE x.linkid = j.id " +
							      "AND EXISTS(SELECT 1 FROM sys_role WHERE id = x.roleid AND name = 'SO'))";
		}
		if(!StrUtils.isNull(isfeeding)){
			dynamicClauseWhere += "\nAND "+(isfeeding.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM sys_attachment x WHERE x.linkid = j.id " +
		      "AND EXISTS(SELECT 1 FROM sys_role WHERE id = x.roleid AND name = '原始补料'))";
		}
		if(!StrUtils.isNull(isbltype)){
			dynamicClauseWhere += "\nAND "+(isbltype.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM sys_attachment x WHERE x.linkid = j.id " +
		      "AND EXISTS(SELECT 1 FROM sys_role WHERE id = x.roleid AND name = '提单正本扫描'))";
		}
		//电放
		if(!StrUtils.isNull(istelrelv)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.istelrel = "
								+(istelrelv.equals("Y")?"TRUE":"FALSE")+")";
		}
		
		if(!StrUtils.isNull(etdstart)||!StrUtils.isNull(etdend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.etd::DATE BETWEEN '"
				+ (StrUtils.isNull(etdstart) ? "0001-01-01" : etdstart) + "' AND '"+ (StrUtils.isNull(etdend) ? "9999-12-31" : etdend)+ "')";
		}
		if(!StrUtils.isNull(bargeetdstart)||!StrUtils.isNull(bargeetdend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.bargeetd::DATE BETWEEN '"
				+ (StrUtils.isNull(bargeetdstart) ? "0001-01-01" : bargeetdstart) + "' AND '"+ (StrUtils.isNull(bargeetdend) ? "9999-12-31" : bargeetdend)+ "')";
		}
		if(!StrUtils.isNull(vgmdatestart)||!StrUtils.isNull(vgmdateend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.vgmdate::DATE BETWEEN '"
				+ (StrUtils.isNull(vgmdatestart) ? "0001-01-01" : vgmdatestart) + "' AND '"+ (StrUtils.isNull(vgmdateend) ? "9999-12-31" : vgmdateend)+ "')";
		}
		if(!StrUtils.isNull(clstimestart)||!StrUtils.isNull(clstimeend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.clstime::DATE BETWEEN '"
				+ (StrUtils.isNull(clstimestart) ? "0001-01-01" : clstimestart) + "' AND '"+ (StrUtils.isNull(clstimeend) ? "9999-12-31" : clstimeend)+ "')";
		}
		if(!StrUtils.isNull(sidatestart)||!StrUtils.isNull(sidateend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.sidate::DATE BETWEEN '"
				+ (StrUtils.isNull(sidatestart) ? "0001-01-01" : sidatestart) + "' AND '"+ (StrUtils.isNull(sidateend) ? "9999-12-31" : sidateend)+ "')";
		}
		if(!StrUtils.isNull(jobno)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM fina_jobs s WHERE s.id = j.id AND s.nos  ILIKE '"+jobno+"')";
		}
		
		//SO
		if(!StrUtils.isNull(sono)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.sono = '"+sono+"')";
		}
		//柜号
		if(!StrUtils.isNull(cntno)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_ship_container s WHERE s.jobid = j.id AND s.cntno = '"+cntno+"')";
		}
		
		map.put("dynamicClauseWhere", dynamicClauseWhere);
		return map;
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	@SaveState
	public boolean isOpenNewWindow = false;
	
	@Action
	public void openTaskWin(){
		isOpenNewWindow = true;
		grid_ondblclick();
	}

	@Override
	public void grid_ondblclick() {
//		String url = "";
//		this.pkVal = getGridSelectId();
//		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(this.pkVal);
//		if (bpmTask.getState() == 0 || bpmTask.getState() == 1) {
//			MessageUtils.alert("请先签收");
//			return;
//		}
//		super.grid_ondblclick();
//		String[] ids = grid.getSelectedIds();
//		serviceContext.bpmTaskService.readed(ids);
//		grid.reload();
//		BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
//		List<BpmAssign> lists = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere("process_id = '"+bpmProcess.getId()+"' AND taskname = '"+bpmTask.getTaskname()+"'");
//		if(lists != null && lists.size() > 0){
//			url = lists.get(0).getUrl();
//			if(!StrUtils.isNull(url)){
//				if(url.indexOf("?")>0){
//					url = url+"&taskId="+this.pkVal+"&p="+bpmTask.getProcessinstanceid();
//				}else{
//					url = url+"?taskId="+this.pkVal+"&p="+bpmTask.getProcessinstanceid();
//				}
//				//System.out.println(url);
//				String actionJsText="";
//				List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmProcess.getId()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'windows.js'");
//				for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
//					actionJsText+=bpmWorkItem.getActions();
//				}
//				System.out.println(actionJsText);
//				if(!StrUtils.isNull(actionJsText)){
//					Browser.execClientScript(actionJsText);
//				}
//				if(isOpenNewWindow){
//					AppUtils.openWindow(Calendar.getInstance().getTimeInMillis()+"", url);
//				}else{
//					taskUserDefineIframe.load(url);
//					editUserDefineWindow.show();
//				}
//				editWindow.close();
//				return;
//			}
//		}
//		url = "./bpmtasktodoprocess.xhtml?taskId="+this.pkVal+"&m=bpm.bpmdesignerBean."+bpmProcess.getCode()+"&p="+bpmTask.getProcessinstanceid();
//		if(isOpenNewWindow){
//			AppUtils.openWindow(Calendar.getInstance().getTimeInMillis()+"", url);
//		}else{
//			taskIframe.load(url);
//			editWindow.show();
//		}
//		editUserDefineWindow.close();
	}
	
	@Bind
	public UIIFrame taskUserDefineIframe;
	
	@Bind
	public UIWindow editUserDefineWindow;
	
	
	@Bind
	public UIIFrame taskIframe;
	
//	@Action
//	public void claim(){
//		this.pkVal = getGridSelectId();
//		try{
//			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(this.pkVal);
//			//BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
//			String sqlsub = "SELECT f_bpm_task_sign('taskid="+bpmTask.getId()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
//			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
//			String sub =  sm.get("rets").toString();
//			MessageUtils.alert(sub);
//			this.refresh();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	
	@Bind
	public UIWindow detailsWindow;
	
	@Bind
	@SaveState
	public String usercode = "";
	
//	@Action
//	public void changeactor() {
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length != 1) {
//			MessageUtils.alert("Please choose one row!");
//			return;
//		}
//		detailsWindow.show();
//	}
	
	
//	@Action
//	public void confimchange() {
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length != 1) {
//			MessageUtils.alert("Please choose one row!");
//			return;
//		}
//		this.pkVal = getGridSelectId();
//		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(this.pkVal);
//		if (bpmTask.getState() == 9) {
//			MessageUtils.alert("已完成不能更改签收人");
//			return;
//		}
//		String sql = "UPDATE bpm_task SET actorid = (SELECT x.code FROM sys_user x WHERE x.id = " + usercode + ") WHERE id =" + this.pkVal + ";";
//		AppUtils.getServiceContext().tFfRtTaskinstanceDescMgrService().tFfRtTaskinstanceDescDao.executeSQL(sql);
//		MessageUtils.alert("OK");
//		detailsWindow.close();
//		grid.reload();
//	}
	
	
//	@Action
//	public void hang() {
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length != 1) {
//			MessageUtils.alert("Please choose one row!");
//			return;
//		}
//		this.pkVal = getGridSelectId();
//		String sql = "UPDATE bpm_task SET suspended = TRUE WHERE id =" + this.pkVal + ";";
//		AppUtils.getServiceContext().tFfRtTaskinstanceDescMgrService().tFfRtTaskinstanceDescDao
//				.executeSQL(sql);
//		MessageUtils.alert("OK");
//		grid.reload();
//	}
//	
//	
//	@Action
//	public void recovery() {
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length != 1) {
//			MessageUtils.alert("Please choose one row!");
//			return;
//		}
//		this.pkVal = getGridSelectId();
//		String sql = "UPDATE bpm_task SET suspended = FALSE WHERE id =" + this.pkVal + ";";
//		AppUtils.getServiceContext().tFfRtTaskinstanceDescMgrService().tFfRtTaskinstanceDescDao
//				.executeSQL(sql);
//		MessageUtils.alert("OK");
//		grid.reload();
//	}
	
	
	@Bind
	public UIWindow showAttachWindow;
	
	@Bind
	public UIIFrame attachIframe;
	
	@Action
	public void attachment(){
		this.pkVal = getGridSelectId();
		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(this.pkVal);
		attachIframe.load("/scp/pages/module/common/attachment.xhtml?code=bpm&linkid="+bpmTask.getProcessid());
		showAttachWindow.show();
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	
	
	@Action
	public void showTaskCheckInfo(){
		String taskid = AppUtils.getReqParam("taskid");
		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
		taskCommentsIframe.load("./bpmshowcomments.jsp?processinstanceid="+bpmTask.getProcessinstanceid()+"&taskname="+bpmTask.getTaskname()+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show()");
	}
	
	
	
	/**
	 * 数据显示构件，GRID
	 *//*
	@Bind
	public UIDataGrid processGrid;

	@Bind(id = "processGrid", attribute = "dataProvider")
	protected GridDataProvider getProcessGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				Map map = new HashMap();
				map.put("qry", "\n1=1");
				String sqlId = "bpm.bpmapplyBean.grid.page";
					return daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId,map , start,
							limit).toArray();
			}

			@Override
			public int getTotalCount() {
				return 1000;
			}
		};
	}
	@Action
	public void processGrid_onrowselect() {
		
	}*/
	
	
	/**
	 * 批量下一步
	 */
//	@Action
//	public void allnext(){
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length == 0) {
//			MessageUtils.alert("Please choose row!");
//			return;
//		}
//		try{
//			for(String id:ids){
//				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(id));
//				
//				bpmTask.setComments("");
//				bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
//				bpmTask.setCommentime(Calendar.getInstance().getTime());
//				
//				serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
//				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
//				String sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Next;assignuserid="+"0"+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
//				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
//				String sub =  sm.get("rets").toString();
//			}
//			grid.reload();
//			Browser.execClientScript("alert('OK')");
//		}catch(Exception e){
//			e.printStackTrace();
//			MessageUtils.showException(e);
//		}
//	}
	
	@Action
	public void stop(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		serviceContext.bpmTaskService.process("Stop",ids);
		MessageUtils.alert("OK!");
	}
	
	
//	@Action
//	public void readed() {
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length < 1) {
//			MessageUtils.alert("Please choose one row!");
//			return;
//		}
//		serviceContext.bpmTaskService.readed(ids);
//		MessageUtils.alert("OK");
//		grid.reload();
//	}
//	
//	@Action
//	public void unread() {
//		String[] ids = grid.getSelectedIds();
//		if (ids == null || ids.length != 1) {
//			MessageUtils.alert("Please choose one row!");
//			return;
//		}
//		serviceContext.bpmTaskService.unread(ids);
//		MessageUtils.alert("OK");
//		grid.reload();
//	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
	}
	
	@Override
	public void clearQryKey() {
		actorerwhere = "";
		isbustruck = "";
		isbuscustoms = "";
		isvgm = "";
		isso = "";
		isfeeding = "";
		isbltype = "";
		istelrelv = "";
		etdstart = "";
		etdend = "";
		bargeetdstart = "";
		bargeetdend = "";
		vgmdatestart = "";
		vgmdateend = "";
		clstimestart = "";
		clstimeend = "";
		sidatestart = "";
		sidateend = "";
		jobno = "";
		sono = "";
		cntno = "";
		update.markUpdate("actorerwhere");
		super.clearQryKey();
	}
	
	@Action
	public void clear() {
		this.clearQryKey();
	}
}
