package com.scp.view.bpm;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.bpm.BpmCommentsTemp;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "bpm.bpmcommentstempBean", scope = ManagedBeanScope.REQUEST)
public class BpmcommentstempBean extends GridView {
	
	@Bind
	@SaveState
	private String commentsText = "";
	
	@Bind
	@SaveState
	private String taskname;
	
	@Bind
	@SaveState
	private Long processid;
	
	@Bind
	private boolean publics;
	
	@Bind
	private boolean append;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
//			this.qryMap.put("t.reqtype", "Q");
			String currentcomment = AppUtils.getReqParam("currentcomment");
			String current="";
			try {
				taskname = Base64.decode(AppUtils.getReqParam("taskname").replaceAll("_","+"),"GB2312");
				processid = Long.parseLong(AppUtils.getReqParam("processid").toString());
				current = new String(Base64.decode(currentcomment));
			} catch (Exception e) {
				current="";
			}
			commentsText = StrUtils.isNull(currentcomment)?"":current;
		}
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}
	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
		this.grid.reload();
	}

	@Action
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0||ids.length>1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			selectedRowData = serviceContext.bpmCommentsTempService.bpmCommentsTempDao.findById(Long.parseLong(ids[0]));
			serviceContext.bpmCommentsTempService.bpmCommentsTempDao.remove(selectedRowData);
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m =  super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		qry += "\n AND (userid ="+AppUtils.getUserSession().getUserid()+" OR ispublic =true)";
		qry += "\n AND taskname = '"+taskname+"'";
		qry += "\n AND processid = "+processid;
		m.put("qry", qry);
		return m;
	}
	
	
	
	@Action
	public void backfill(){
		String Jsstrin = "setComment('"+commentsText.replace("\n","")+"')";
		Browser.execClientScript("parent.window",Jsstrin);
	}
	
	@SaveState
	@Accessible
	public BpmCommentsTemp selectedRowData;
	
	@Action
	public void saveTemplate(){
		if(StrUtils.isNull(commentsText)){
			alert("批复意见不能为空");
			return;
		}
		selectedRowData = new BpmCommentsTemp();
		selectedRowData.setProcessid(processid);
		selectedRowData.setIspublic(publics);
		selectedRowData.setTaskname(taskname);
		selectedRowData.setComments(commentsText);
		selectedRowData.setUserid(AppUtils.getUserSession().getUserid());
		serviceContext.bpmCommentsTempService.bpmCommentsTempDao.createOrModify(selectedRowData);
		qryRefresh();
	}


	@Override
	public void grid_onrowselect() {
		String[] ids = this.grid.getSelectedIds();
		try {
			selectedRowData = serviceContext.bpmCommentsTempService.bpmCommentsTempDao.findById(Long.parseLong(ids[0]));
			if(append){
				commentsText += (StrUtils.isNull(selectedRowData.getComments())?"":selectedRowData.getComments());
			}else{
				commentsText = (StrUtils.isNull(selectedRowData.getComments())?"":selectedRowData.getComments());
			}
			this.update.markUpdate("commentsText");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
}
