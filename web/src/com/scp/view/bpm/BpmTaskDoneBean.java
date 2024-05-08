package com.scp.view.bpm;

import java.util.Calendar;
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

import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "bpm.bpmtaskdoneBean", scope = ManagedBeanScope.REQUEST)
public class BpmTaskDoneBean extends GridFormView{
	
	public Long userid;
	
	public String language;
	
	@Bind
	@SaveState
	public String actorerwhere;

	@Bind
	@SaveState
	private String mblnumber;

	@Bind
	@SaveState
	private Boolean ischeckall = false;
	
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
		String qry = map.get("qry").toString();
		
		String order = "ORDER BY replace(nos,'GSZPM','')::BIGINT desc,taskcreatedtime DESC";
		
		
		if(!StrUtils.isNull(actorerwhere)){
			qry += "AND EXISTS(SELECT 1 FROM sys_user x WHERE  x.code = t.actorid AND (namec ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%' " +
					"OR EXISTS(SELECT 1 FROM sys_department WHERE x.deptid = id AND name ILIKE '%"+actorerwhere+"%' OR namee ILIKE '%"+actorerwhere+"%')))";
		}
		
		String existsSql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole a,sys_role b WHERE a.roleid =  b.id AND a.userid = " + AppUtils.getUserSession().getUserid()+" AND b.code = 'dev') AS existflag";
		Map existsMap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(existsSql);
		if (existsMap != null && "true".equals(StrUtils.getMapVal(existsMap, "existflag"))) {//20200109 neo 开发测试组可以看到所有的
			if (!ischeckall) {
				qry += "\n AND t.procestartedtime > (NOW() + '-1Month')";
			} else {
				qry += "\n AND t.procestartedtime > (NOW() + '-3year')";
			}
			map.put("order", "");
		} else {
			//1835 用户管理增加上级(待办已办中，递归查询，比如当前账号是文件A，文件A的上级是文件主管B，文件主管B的上级是文件部经理C那么C的任务里面可以看到C的和所有A和B的
			//B能看到自己和C的C只能看到自己的)
			qry +=
					"\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'bpm_task_filterbyorg' AND val = 'Y') THEN "
							+ "\n	t.actorid = ANY(ARRAY(WITH RECURSIVE rc AS ("
							+ "\n		SELECT * FROM sys_user WHERE id = " + AppUtils.getUserSession().getUserid()
							+ "\n		UNION "
							+ "\n		SELECT a.* FROM sys_user a,rc WHERE  a.parentid = rc.id"
							+ "\n	)"
							+ "\n	SELECT code FROM rc ))"
							+ "\n ELSE "
							+ "\n 	t.actorid = '" + AppUtils.getUserSession().getUsercode() + "'"
							+ "\n END)";
			map.put("order", order);
		}

		//MBL提单号
		if(!StrUtils.isNull(mblnumber)){
			qry +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.jobid AND (s.mblno = '"+mblnumber+"'))";
		}
		map.put("qry", qry);
		return map;
	}
	
	
	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void clearQryKey() {
		actorerwhere = "";
		update.markUpdate("actorerwhere");
		super.clearQryKey();
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
	
	@Action
	public void openTaskWin(){
		grid_ondblclick();
	}

	@Bind
	public UIIFrame taskUserDefineIframe;
	
	@Bind
	public UIWindow editUserDefineWindow;
	
	@Bind
	public UIIFrame taskIframe;
	
	@Override
	public void grid_ondblclick() {
		String url = "";
		this.pkVal = getGridSelectId();
		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(this.pkVal);
		
		if("Start".equalsIgnoreCase(bpmTask.getTaskname()) || "End".equalsIgnoreCase(bpmTask.getTaskname())){
			this.alert("Start or End can not be show!");
			return;
		}
		BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
		url = "./bpmtaskdoneshow.xhtml?taskId="+this.pkVal+"&m=bpm.bpmdesignerBean."+bpmProcess.getCode()+"&p="+bpmTask.getProcessinstanceid();
		AppUtils.openWindow(Calendar.getInstance().getTimeInMillis()+"", url);
	}

}
