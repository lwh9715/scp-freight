package com.scp.view.bpm;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scp.model.finance.FinaArap;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bpm.BpmAssign;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;


/**
 *
 * 待办工单列表
 * @author neo
 */
@ManagedBean(name = "bpm.bpmtasktodoBean", scope = ManagedBeanScope.REQUEST)
public class BpmTaskTodoBean extends GridFormView{

	public Long userid;

	public String language;

	@Bind
	@SaveState
	public String actorerwhere;

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
	private String jobdatestart;

	@Bind
	@SaveState
	private String jobdateend;

	@Bind
	@SaveState
	private String jobstatedescqry;

	@Bind
	@SaveState
	private String linecode;

	@Bind
	@SaveState
	private String carrierid;

	@Bind
	@SaveState
	private String bltype;

	@Bind
	@SaveState
	private String hblno;

	@Bind
	@SaveState
	private String sono;

	@Bind
	@SaveState
	private String cntno;

	@Bind
	@SaveState
	private String mblnumber;

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		language = AppUtils.getUserSession().getMlType().toString();
		if (!isPostBack) {
			super.applyGridUserDef();
			this.qryMap.put("actorid",AppUtils.getUserSession().getUsercode());
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("userid", AppUtils.getUserSession().getUserid());

		map.put("ORDER", "ORDER BY timeoutorder,replace(nos,'GSZPM','')::BIGINT desc,displayname,taskname");


		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(actorerwhere)){
			qry += "AND EXISTS(SELECT 1 FROM sys_user x WHERE  x.code = t.actorid AND (namec ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%' " +
				   "\nOR EXISTS(SELECT 1 FROM sys_department WHERE x.deptid = id AND name ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%')))";
		}

		String existsSql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole a,sys_role b WHERE a.roleid =  b.id AND a.userid = " + AppUtils.getUserSession().getUserid()+" AND b.code = 'dev') AS existflag";
		Map existsMap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(existsSql);
		if(existsMap != null && "true".equals(StrUtils.getMapVal(existsMap, "existflag"))){//20200109 neo 开发测试组可以看到所有的
//			if(!StrUtils.isNull(StrUtils.getMapVal(qryMap, "actorid"))){
//				qry += "\n AND t.actorid = '" + AppUtils.getUserSession().getUsercode() + "'";
//			}
			qry += "\n AND t.procestartedtime > (NOW() + '-3Month' )";
			map.put("ORDER", ""); //Neo 20200109 如果查全部，去掉排序，非常慢
		}else{
			//1835 用户管理增加上级(待办已办中，递归查询，比如当前账号是文件A，文件A的上级是文件主管B，文件主管B的上级是文件部经理C那么C的任务里面可以看到C的和所有A和B的
			//B能看到自己和C的C只能看到自己的)

			//neo 20200215 系统设置待办显示自己已经提交的任务
			String cs_url_base = ConfigUtils.findSysCfgVal("bpm_taskto_showsubmit");
			if("Y".equals(cs_url_base)){
				qry +=
					 "\n AND ((CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'bpm_task_filterbyorg' AND val = 'Y') THEN "
					+"\n	t.actorid = ANY(ARRAY(WITH RECURSIVE rc AS ("
					+"\n		SELECT * FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()
					+"\n		UNION "
					+"\n		SELECT a.* FROM sys_user a,rc WHERE  a.parentid = rc.id"
					+"\n	)"
					+"\n	SELECT code FROM rc ))"
					+"\n ELSE "
					+"\n 	t.actorid = '" + AppUtils.getUserSession().getUsercode() + "'"
					+"\n END) OR t.procecreateduser = '" + AppUtils.getUserSession().getUsercode() + "')";
			}else{
				qry +=
					 "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'bpm_task_filterbyorg' AND val = 'Y') THEN "
					+"\n	t.actorid = ANY(ARRAY(WITH RECURSIVE rc AS ("
					+"\n		SELECT * FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()
					+"\n		UNION "
					+"\n		SELECT a.* FROM sys_user a,rc WHERE  a.parentid = rc.id"
					+"\n	)"
					+"\n	SELECT code FROM rc ))"
					+"\n ELSE "
					+"\n 	t.actorid = '" + AppUtils.getUserSession().getUsercode() + "'"
					+"\n END)";
			}
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
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.cls::DATE BETWEEN '"
				+ (StrUtils.isNull(clstimestart) ? "0001-01-01" : clstimestart) + "' AND '"+ (StrUtils.isNull(clstimeend) ? "9999-12-31" : clstimeend)+ "')";
		}
		if(!StrUtils.isNull(sidatestart)||!StrUtils.isNull(sidateend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM bus_shipping AS x WHERE x.jobid = j.id AND x.isdelete = FALSE AND x.sidate::DATE BETWEEN '"
				+ (StrUtils.isNull(sidatestart) ? "0001-01-01" : sidatestart) + "' AND '"+ (StrUtils.isNull(sidateend) ? "9999-12-31" : sidateend)+ "')";
		}
		if(!StrUtils.isNull(jobno)){
			dynamicClauseWhere +="\nAND j.nos ILIKE '"+jobno+"'";
		}
		if(!StrUtils.isNull(jobdatestart)||!StrUtils.isNull(jobdateend)){
			dynamicClauseWhere += "\n AND  EXISTS (SELECT 1 FROM fina_jobs AS x WHERE x.id = j.id AND x.isdelete = FALSE AND x.jobdate::DATE BETWEEN '"
				+ (StrUtils.isNull(jobdatestart) ? "0001-01-01" : jobdatestart) + "' AND '"+ (StrUtils.isNull(jobdateend) ? "9999-12-31" : jobdateend)+ "')";
		}
		if(!StrUtils.isNull(jobstatedescqry)){
			dynamicClauseWhere += "\n AND j.jobstate = '"+jobstatedescqry+"'";
		}

		//航线代码
		if(!StrUtils.isNull(linecode)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.linecode = '"+linecode+"')";
		}
		//船公司
		if(!StrUtils.isNull(carrierid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.carrierid = "+carrierid+")";
		}
		//提单类型
		if(!StrUtils.isNull(bltype)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.bltype = '"+bltype+"')";
		}
		//提单号码
		if(!StrUtils.isNull(hblno)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND (s.hblno = '"+hblno+"' OR s.mblno = '"+hblno+"'))";
		}
		//SO
		if(!StrUtils.isNull(sono)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND s.sono = '"+sono+"')";
		}
		//柜号
		if(!StrUtils.isNull(cntno)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_ship_container s WHERE s.jobid = j.id AND s.cntno = '"+cntno+"')";
		}
		//MBL提单号
		if(!StrUtils.isNull(mblnumber)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = j.id AND (s.mblno = '"+mblnumber+"'))";
		}

		map.put("dynamicClauseWhere", dynamicClauseWhere);
		return map;
	}

	@Action
	public void searchfee() {
		this.qryRefresh();
	}

	@Override
	protected void doServiceFindData() {

	}

	@Override
	protected void doServiceSave() {

	}

	@SaveState
	public boolean formatLinkBPMNo = false;//1832 待办打开时，区分是链接打开还是双击打开

	@SaveState
	public boolean isOpenNewWindow = false;

	@Action
	public void openTaskWin(){
		isOpenNewWindow = true;
		formatLinkBPMNo = true;
		grid_ondblclick();
	}

	@Override
	public void grid_ondblclick() {
		String url = "";
		this.pkVal = getGridSelectId();
		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(this.pkVal);
		if (bpmTask.getState() == 0 || bpmTask.getState() == 1) {
			MessageUtils.alert("请先签收");
			return;
		}
		super.grid_ondblclick();
		String[] ids = grid.getSelectedIds();
		serviceContext.bpmTaskService.readed(ids);
		grid.reload();
		BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
		List<BpmAssign> lists = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere("process_id = '"+bpmProcess.getId()+"' AND taskname = '"+bpmTask.getTaskname()+"' AND isdelete = FALSE");
		if(lists != null && lists.size() > 0){
			url = lists.get(0).getUrl();
			if(!StrUtils.isNull(url)){
				if(url.indexOf("?")>0){
					url = url+"&taskId="+this.pkVal+"&p="+bpmTask.getProcessinstanceid()+"&formatLink="+formatLinkBPMNo;
				}else{
					url = url+"?taskId="+this.pkVal+"&p="+bpmTask.getProcessinstanceid()+"&formatLink="+formatLinkBPMNo;
				}
				//System.out.println(url);
				String actionJsText="";
				List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+bpmProcess.getId()+" AND taskname = '"+bpmTask.getTaskname()+"' AND actiontype = 'windows.js'");
				for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
					actionJsText+=bpmWorkItem.getActions();
				}
				//System.out.println(actionJsText);
				if(!StrUtils.isNull(actionJsText)){
					Browser.execClientScript(actionJsText);
				}
				if(isOpenNewWindow){
					AppUtils.openNewPage(url);
				}else{
					taskUserDefineIframe.load(url);
					editUserDefineWindow.show();
				}
				editWindow.close();
				return;
			}
		}
		url = "./bpmtasktodoprocess.xhtml?taskId="+this.pkVal+"&m=bpm.bpmdesignerBean."+bpmProcess.getCode()+"&p="+bpmTask.getProcessinstanceid()+"&formatLink="+formatLinkBPMNo;
		if(isOpenNewWindow){
			AppUtils.openNewPage(url);
		}else{
			taskIframe.load(url);
			editWindow.show();
		}
		editUserDefineWindow.close();
		formatLinkBPMNo = false;
	}

	@Bind
	public UIIFrame taskUserDefineIframe;

	@Bind
	public UIWindow editUserDefineWindow;


	@Bind
	public UIIFrame taskIframe;

	@Action
	public void claim(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("Please choose one row!");
			return;
		}
		boolean isError = false;
		for (String id : ids) {
			long pk = Long.valueOf(id);
			try{
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(pk);
				//BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
				String sqlsub = "SELECT f_bpm_task_sign('taskid="+bpmTask.getId()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			}catch(Exception e){
				isError = true;
				MessageUtils.showException(e);
				e.printStackTrace();
			}
		}
		if(!isError){
			MessageUtils.alert("OK!");
		}
		this.refresh();
	}


	@Bind
	public UIWindow detailsWindow;

	@Bind
	@SaveState
	public String usercode = "";

	@Action
	public void changeactor() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		detailsWindow.show();
	}


	@Action
	public void confimchange() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try{
			for(String id:ids){
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(id));
				if (bpmTask.getState() == 9) {
					MessageUtils.alert("已完成不能更改签收人");
					return;
				}
				SysUser sysUser = this.serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(usercode));
				String sql = "UPDATE bpm_task SET actorid = '" + sysUser.getCode() + "' WHERE id =" + Long.valueOf(id) + ";";
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);

				String logSql = "SELECT f_bpm_log(" + Long.valueOf(id) + ",'Change Sign','"+bpmTask.getTaskname()+":更改签收人 from "+bpmTask.getActorid()+"  to "+sysUser.getCode()+"  ','" + AppUtils.getUserSession().getUsercode()+"');";
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(logSql);
			}
			MessageUtils.alert("OK");
			detailsWindow.close();
			grid.reload();
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}


	@Action
	public void hang() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		this.pkVal = getGridSelectId();
		String sql = "UPDATE bpm_task SET suspended = TRUE WHERE id =" + this.pkVal + ";";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
		MessageUtils.alert("OK");
		grid.reload();
	}


	@Action
	public void recovery() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		this.pkVal = getGridSelectId();
		String sql = "UPDATE bpm_task SET suspended = FALSE WHERE id =" + this.pkVal + ";";
		serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
		MessageUtils.alert("OK");
		grid.reload();
	}


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
	@Action
	public void allnext(){
		String[] ids = this.grid.getSelectedIds();
 		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose row!");
			return;
		}
		try{
			for(String id:ids){
				BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(id));

				bpmTask.setComments("");
				bpmTask.setCommentuserid(AppUtils.getUserSession().getUserid());
				bpmTask.setCommentime(Calendar.getInstance().getTime());

				serviceContext.bpmTaskService.bpmTaskDao.createOrModify(bpmTask);
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
				String sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Next;assignuserid="+"0"+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";

				Long currentUserid = AppUtils.getUserSession().getUserid();
				//途曦结算地
				if (AppUtils.getUserSession().getCorpid() == 11540072274L) {
					//副总审核人 北美-danly 251411452274 中东-demi 665176902274
					if (bpmTask.getTaskname().equals("副总审核")) {
						String sqlts = "SELECT EXISTS(SELECT 1 FROM sys_user WHERE id = "+currentUserid+" AND deptid = 516228802274) as isret";
						//判断途曦部门
						Map ts = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlts.toString());
						if (ts.get("isret").toString().equals("true")) {
							currentUserid = 665176902274l;
						} else {
                            currentUserid = 251411452274l;
                        }
					}
					sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Next;assignuserid="+currentUserid+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
				//深圳结算地 副总审核人 黄虹 89536042274
				} else if (AppUtils.getUserSession().getCorpid() == 100L && bpmTask.getTaskname().equals("副总审核")) {
					currentUserid = 89536042274l;
					sqlsub = "SELECT f_bpm_createTask('processid="+bpmProcess.getId()+";processinstanceid="+bpmTask.getProcessinstanceid()+";type=Next;assignuserid="+currentUserid+";taskname="+bpmTask.getTaskname()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
				}
				//副总审核和经理审核后需要新增增减费用
				if ((bpmTask.getTaskname().equals("经理审核1") || bpmTask.getTaskname().equals("副总审核")) && bpmProcess.getNamec().equals("增减费用申请")) {
					insertFee(bpmTask.getId());
				}
				Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
				String sub =  sm.get("rets").toString();
			}
			this.grid.reload();
			Browser.execClientScript("alert('OK')");
		}catch(Exception e){
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

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


	@Action
	public void readed() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.bpmTaskService.readed2(ids);
		MessageUtils.alert("OK");
		grid.reload();
	}

	@Action
	public void unread() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.bpmTaskService.unread(ids);
		MessageUtils.alert("OK");
		grid.reload();
	}

	@Action
	public void markwait() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		serviceContext.bpmTaskService.markwait(ids);
		MessageUtils.alert("OK");
		grid.reload();
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
		jobdatestart = "";
		jobdateend = "";
		jobstatedescqry = "";
		linecode = "";
		carrierid = "";
		bltype = "";
		hblno = "";
		sono = "";
		cntno = "";
		update.markUpdate("actorerwhere");
		super.clearQryKey();
	}

	@Action
	public void clear() {
		this.clearQryKey();
	}


	@Action
	public void showLogs() {
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		String url = "./bpmlogs.xhtml?taskid="+ids[0];
		attachIframe.load(url);
		showAttachWindow.setTitle("Logs");
		showAttachWindow.show();
	}

    public void insertFee(Long taskId) {
        //副总审核和经理审核后需要新增增减费用
        String sql = "SELECT arapinfo FROM bpm_task WHERE processid||''||processinstanceid = (SELECT processid||''||processinstanceid FROM bpm_task WHERE id = " + taskId + " LIMIT 1) AND taskname='Start'  LIMIT 1";
        try {
            Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            if (m.containsKey("arapinfo")) {
                Object obj = m.get("arapinfo");
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(obj);
                List<Map> l = new ArrayList<Map>();
                for (int i = 0; i < array.size(); i++) {
                    Map map = (Map) array.get(i);
                    Map<String, String> dateMap = (Map) map.get("arapdate");
                    Map<String, String> dateMap2 = (Map) map.get("inputtime");
                    map.put("arapdate", fmt.format(dateMap.get("time")));
                    map.put("inputtime", fmt.format(dateMap2.get("time")));
                    l.add(map);
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                Type type = new TypeToken<List<FinaArap>>() {
                }.getType();
                List<FinaArap> arap = gson.fromJson(l.toString(), type);

                Integer num = 1;
                List<FinaArap> finaArapList = new ArrayList<FinaArap>();
                for (FinaArap li : arap) {
                    FinaArap finaArap = new FinaArap();
                    finaArap.setCorpid(li.getCorpid());
                    finaArap.setInputer(li.getInputer());
                    finaArap.setTaxrate((null == li.getTaxrate() || li.getTaxrate().compareTo(BigDecimal.ZERO) == 0) ? new BigDecimal(1) : li.getTaxrate());//早期提交的付款申请流程没有记录税率。默认为1
                    finaArap.setAraptype(li.getAraptype());
                    finaArap.setArapdate(li.getArapdate());
                    finaArap.setRptype(li.getRptype());
                    finaArap.setCustomerid(li.getCustomerid());
                    finaArap.setFeeitemid(li.getFeeitemid());
                    finaArap.setAmount(li.getAmount());
                    finaArap.setCurrency(li.getCurrency());
                    if (li.getRemarks() != null) {
                        finaArap.setRemarks(li.getRemarks());   //界面无备注，这里会全部变成空的 neo 20170202
                    }
                    finaArap.setPiece(li.getPiece());
                    finaArap.setPrice(li.getPrice());
                    finaArap.setUnit(li.getUnit());
                    finaArap.setPpcc(li.getPpcc());
                    finaArap.setSharetype("N");
                    finaArap.setJobid(li.getJobid());
                    finaArap.setPayplace(li.getPayplace());
                    finaArap.setIsamend(li.getIsamend());
                    long etime1 = System.currentTimeMillis() + num * 1000;//延时函数，单位毫秒，这里是延时了num秒钟
                    finaArap.setInputtime(new Date(etime1));
                    num++;

                    finaArap.setPricenotax(new BigDecimal(-1.01));
                    finaArapList.add(finaArap);
                }
                serviceContext.arapMgrService.saveOrModify(finaArapList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
            return;
        }
    }

}
