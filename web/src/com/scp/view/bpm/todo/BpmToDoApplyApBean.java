package com.scp.view.bpm.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import sun.misc.BASE64Encoder;

import com.scp.model.bpm.BpmAssign;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmProcessinstance;
import com.scp.model.bpm.BpmTask;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.bpm.BpmTaskTodoProcessBean;
@ManagedBean(name = "bpm.todo.bpmtodoapplyapBean", scope = ManagedBeanScope.REQUEST)
public class BpmToDoApplyApBean extends BpmTaskTodoProcessBean{
	
	@Bind
	public UIIFrame todoIframe;
	
	@SaveState
	public boolean formatLinkBPMNo = false;//1832 待办打开时，区分是链接打开还是双击打开
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String araptype = AppUtils.getReqParam("araptype");
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
			try {
				formatLinkBPMNo = Boolean.parseBoolean(AppUtils.getReqParam("formatLink"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String formview = "";
			List<BpmAssign> lists = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere("process_id = '"+bpmTask.getProcessid()+"' AND isdelete =FALSE AND taskname = '"+bpmTask.getTaskname()+"'");
			if(lists != null && lists.size() > 0){
				formview = lists.get(0).getFormview();
			}
			
			if(!StrUtils.isNull(formview)){
				if(formview.indexOf("arapid")>0){
					//获取备注信息里的费用ID，账单审核需要用于自动勾选费用
					String remark = bpmProcessinstance.getRemarks();
					remark = remark.indexOf("待审核费用ID：")==-1?"":remark.split("待审核费用ID：")[1];
					remark = remark.split("\r\n")[0].trim();
					formview = formview+remark+"&id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit";
				}else if(formview.indexOf("jobid")>0){
					formview = formview+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit&";
				}else if(formview.indexOf("?")>0){
					formview = formview+"&id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit&";
				}else{
					formview = formview+"?id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId()+"&type=edit";
				}
				todoIframe.load(formview);
			}else{
				todoIframe.load("bpmpayapplydtlcheck.xhtml"+"?araptype="+araptype+"&id="+bpmProcessinstance.getRefid()+"&taskid="+bpmTask.getId());
			}

			//付款申请与增减流程设置默认审核人
			setDefaultApprover(bpmProcessinstance,bpmTask);
		}
	}

	private void setDefaultApprover(BpmProcessinstance bpmProcessinstance, BpmTask bpmTask) {
		try {
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmProcessinstance.getProcessid());
			SysUser createrUser = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere("code = '" + bpmProcessinstance.getCreateduser() + "'");
			if ("付款申请".equals(bpmProcess.getNamec())) {
				if ("总经理审核".equals(bpmTask.getTaskname())) {
					String corpid = String.valueOf(createrUser.getSysCorporation().getId());
					List<BpmAssign> bpmAssignList = serviceContext.bpmAssignService.bpmAssignDao
							.findAllByClauseWhere("process_id = '" + bpmProcess.getId() + "' AND taskname = '财务审核'");
					SysUser touserUser = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere("code = '" + bpmAssignList.get(0).getTousercode()+"'");

					String ss =
							"SELECT\n" +
							"\tuserid\n" +
							"\t,(SELECT x.namec FROM sys_user x where x.id = userid AND x.isdelete = false) AS usernamec\n" +
							"\t,(select corpid from sys_user where id=userid) AS corpid\n" +
							"FROM  bpm_assign_ref a WHERE assignid= " + bpmAssignList.get(0).getId() + "";
					List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(ss);

					if (mapList != null && mapList.size() > 0) {
						for (int i = 0; i < mapList.size(); i++) {
							if (corpid.equals(mapList.get(i).get("corpid").toString())) {
								nextAssignUser = String.valueOf(mapList.get(i).get("userid"));
								user = mapList.get(i).get("usernamec") + ",";
							}
						}
						if ("".equals(nextAssignUser)) {
							nextAssignUser = String.valueOf(touserUser.getId());
							user = touserUser.getNamec() + ",";
						}
					}
					
					//青岛默认财务审核
					if("1122274".equals(corpid)){
						nextAssignUser = "1562718832274";
						user = "孙晓飞,";
					}
					if("100".equals(corpid)){
						nextAssignUser = "2186720888";
						user = "王鹰,";
					}
					//途曦默认财务审核
					if("11540072274".equals(corpid)){
						nextAssignUser = "76639702274";
						user = "侯义敏,";
					}
					//卓航默认财务审核
					if("13632274".equals(corpid)){
						nextAssignUser = "263807042274";
						user = "李新维,";
					}
					
					update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
				}
//				if ("财务审核".equals(bpmTask.getTaskname())) {
//					String corpid = String.valueOf(createrUser.getSysCorporation().getId());
//					//青岛默认放货审核
//					if("1122274".equals(corpid)){
//						nextAssignUser = "376924102274";
//						user = "王燕,";
//					}
//					if("100".equals(corpid)){
//						nextAssignUser = "120230502274";
//						user = "黄永琪,";
//					}
//					
//					if("100".equals(corpid)==false && "1122274".equals(corpid)==false){//付款申请除青岛深圳分办财务审核后直接结束流程
//						taskname = "结束";
//						user = "";
//						nextAssignUser = null;
//					}
//				}
			}
			if ("增减费用申请".equals(bpmProcess.getNamec())) {
				if ("经理审核2".equals(bpmTask.getTaskname())) {
					checkUser(createrUser);
				}
			}
			if ("新版_放货流程".equals(bpmProcess.getNamec())) {
//				Browser.execClientScript("taskRemarksJs.setReadOnly(true)");
//				if ("经理审核".equals(bpmTask.getTaskname()) || "分公司经理审核".equals(bpmTask.getTaskname())||"区域经理审核".equals(bpmTask.getTaskname())||"结束".equals(bpmTask.getTaskname())) {
////					checkUser(createrUser);
//					String billtype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "billtype");
//					String releasetype = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "releasetype");
//					String reason = serviceContext.bpmProcessinsVarService.getBpmProcessinsVarValone(bpmProcessinstance.getId(), "reason");
//					
					nextAssignUser = "";
					user = "";
//					SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:SS");
//					taskRemarks = "批准："+billtype+" "+releasetype+" "+reason+" ";//批准：提单方式 放货方式 取单原因(时间/审核人) --批准 HBL 正本 放货(2022-06-10 16:21:39+08/钱伟荣)
//				}
			}
		} catch (Exception e) {
		}
	}
	
	public	void checkUser(SysUser createrUser){
		if(100 == createrUser.getSysCorporation().getId()){//深圳世联达
			if("87252274,47937178888,53826737888,86548332274,111888462274".contains(createrUser.getDeptid()+"")){//业务7-11部指定副总审核人2245749888
				nextAssignUser = "2245749888";
				user = "马守拥,";
			}else if("30680803888".equals(createrUser.getDeptid()+"")){
				nextAssignUser = "31810130888";
				user = "蔡思文,";
			}else if("50653032274".equals(createrUser.getDeptid()+"")){
				nextAssignUser = "52743402274";
				user = "黄少鹏,";
			}else{
				nextAssignUser = "89536042274";
				user = "黄虹,";
			}
		}
		if(882274 == createrUser.getSysCorporation().getId()){//上海
			nextAssignUser = "43052704888";
			user = "郭恺迪,";
		}
		if("50661672274".equals(String.valueOf(createrUser.getSysCorporation().getId()))){//广州
			nextAssignUser = "11640462274";
			user = "黄健聪,";
		}
		if(1512274 == createrUser.getSysCorporation().getId()){//中山
			nextAssignUser = "2244677888";
			user = "胡志东,";
		}
		if(1122274 == createrUser.getSysCorporation().getId()){//青岛
			nextAssignUser = "2244604888";
			user = "李金华,";
		}
		if(1242274 == createrUser.getSysCorporation().getId()){//天津
			nextAssignUser = "2244762888";
			user = "方学静,";
		}
		if(1002274 == createrUser.getSysCorporation().getId()){//宁波
			nextAssignUser = "1581502382274";
			user = "张婧婧,";
		}
		if(1392274 == createrUser.getSysCorporation().getId()){//南宁
			nextAssignUser = "2244557888";
			user = "殷春燕,";
		}
		if(13632274 == createrUser.getSysCorporation().getId()){//卓航
			nextAssignUser = "102171712274";
			user = "沈毅,";
		}
		if(13932274 == createrUser.getSysCorporation().getId()){//泽世深圳
			nextAssignUser = "67768722274";
			user = "周宇达,";
		}
		if(14052274 == createrUser.getSysCorporation().getId()){//泽世深圳
			nextAssignUser = "67768722274";
			user = "周宇达,";
		}
		if("70033332274".equals(String.valueOf(createrUser.getSysCorporation().getId()))){//江苏
			nextAssignUser = "459851002274";
			user = "张寿银,";
		}
	}

	@Override
	public void next(){
		//付款申请只允许按步骤走
		try {
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			if ("付款申请".equals(bpmProcess.getNamec())) {
				super.next();
			}else if(StrUtils.isNull(taskname)){
				super.next();
			}else{
				super.goToTask(taskname,"next");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void last(){
		super.last();
	}
	
	@Bind
	@SaveState
	public String taskname;
	
	@Action
	public void taskname_onselect(){
		this.gridUser.reload();
	}
	
	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
		try{
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findById(bpmTask.getProcessid());
			String sql = "SELECT DISTINCT step,taskname FROM bpm_assign WHERE process_id = "+bpmTask.getProcessid()+" and (isauto = false OR  taskname = '提交' OR  taskname = '结束') AND taskname <> 'Start' AND taskname <> 'End' AND isdelete = false ORDER BY step,taskname";
			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			for(Map map:maps){
				//放货流程，增减费用流程不显示结束
				if (("新版_放货流程".equals(bpmProcess.getNamec()) || "增减费用申请".equals(bpmProcess.getNamec())) && "结束".equals(StrUtils.getMapVal(map, "taskname"))) {
					continue;
				}
				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
				lists.add(selectItem);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return lists;
    }
	
	@Action
	public void gototask(){
		if(StrUtils.isNull(taskname)){
			MessageUtils.alert("请选择要打回到那个环节!");
			return;
		}
		super.goToTask(taskname,"back");
	}
	
	@Bind
	private UIIFrame commentstempifram;
	
	@Bind
	private UIWindow commentstempwindow;
	
	@Action
	public void commentstempajax(){
		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
		BpmProcessinstance bpmProcessinstance = serviceContext.bpmProcessinstanceService.bpmProcessinstanceDao.findById(bpmTask.getProcessinstanceid());
//		if ("新版_放货流程".equals(bpmProcessinstance.getDisplayname())) {
//			return;
//		}
		BASE64Encoder encoder = new BASE64Encoder();
		String url = AppUtils.getContextPath()
		+ "/bpm/bpmcommentstemp.xhtml?processid="+bpmTask.getProcessid()+"&taskname="
		+Base64.encode(bpmTask.getTaskname(),"GB2312").replaceAll("\\+","_")+"&currentcomment="+Base64.encode(taskRemarks);
		commentstempifram.setSrc(url);
		update.markAttributeUpdate(commentstempifram, "src");
		commentstempwindow.show();
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	
	@Action
	public void showTaskCheckInfo(){
		BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
		taskCommentsIframe.load("../bpmshowcomments.jsp?processinstanceid="+bpmTask.getProcessinstanceid()+"&taskname="+bpmTask.getTaskname()+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show()");
	}

	@Override
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere2(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(taskname)){
			BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(this.taskId));
			qry +=  "\n AND(CASE WHEN EXISTS(SELECT 1 FROM bpm_assign WHERE taskname = '"+taskname+"' AND process_id = "+bpmTask.getProcessid()+" AND isdelete = FALSE  AND ismatchassign = TRUE) THEN" +
					"\n EXISTS(SELECT 1 FROM bpm_assign_ref x WHERE userid = t.id " +
					"\n AND EXISTS(SELECT 1 FROM bpm_assign WHERE ismatchassign = true AND id = x.assignid AND taskname = '"+taskname+"'))" +
					"\n ELSE TRUE END)";
		}
		map.put("qry", qry);
		return map;
	}
	
}
