package com.scp.view.module.train;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.train.jobschildBean", scope = ManagedBeanScope.REQUEST)
public class JobschildBean extends GridFormView {
	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();
	
	
	@SaveState
	@Accessible
	public Long parentid;
	
	@Bind
	@SaveState
	@Accessible
	public Long thisid;
	
	@SaveState
	@Accessible
	public String linktype;
	
	@SaveState
	@Accessible
	public Long jobid;
	
	@Bind
	public UIWindow showManifestWindow;
	
	@Bind
	@SaveState
	public String manifestmsgContent;
	
	@Bind
	@SaveState
	@Accessible
	public String fileName;
	
	@Bind
	@SaveState
	@Accessible
	public String contentType;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			super.applyGridUserDef();
			String id =AppUtils.getReqParam("id").trim();
			linktype =AppUtils.getReqParam("linktype").trim();
			parentid=Long.valueOf(id);
			thisid=parentid;
			update.markUpdate(true, UpdateLevel.Data, "thisid");
			//qryMap.put("parentid$", this.parentid);
			initAdd();
			// this.grid.repaint();
		}
	}

	

	

	protected void initAdd() {
		selectedRowData = (FinaJobs) new FinaJobs();
		
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Override
	public void add() {

		super.add();
		initAdd();
	}

	

	@Override
	public void del() {
//		serviceContext.sysUserAssignMgrService.removeDate(this.pkVal);
//		this.alert("OK");
//		this.add();
//		this.grid.reload();
	}

	

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this
				.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
	
		//serviceContext.jobsMgrService.saveData(selectedRowData);
	}
 
	
	@Override
	public void grid_ondblclick() {
		String winId =System.currentTimeMillis()+"";
		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void jobschoose() {
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipjobschoose.xhtml?jobid="+this.parentid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
 
	

	@Action
	public void cancel() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.jobsMgrService.cancel(ids);
			MessageUtils.alert("cancel success");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String sql="a.id="+this.parentid;
		String sql2="id <> "+this.parentid;
		String sql3="AND parentid IS NOT NULL AND isdelete = false";
		m.put("child", sql);
		m.put("child2", sql2);
		m.put("parentid", this.parentid);
		m.put("sql3", sql3);
		return m;
	}
	
	
	@Action
	public void join() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要合并的委托单！");
			return;
		}
		try {
			serviceContext.jobsMgrService.saveJointrain(ids,AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK!");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	@Action
	public void ediManifest() {
		String[] ids = this.grid.getSelectedIds();
		StringBuilder str = new StringBuilder();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择单行记录！");
			return;
		} else {
			for (int i = 0; i < ids.length; i++) {
				if (i == ids.length - 1) {
					str.append(ids[i]);
				} else {
					str.append(ids[i] + ",");
				}
			}
			 String busjobid = parentid.toString();
			str.append("," + busjobid);
			Long id = Long.parseLong(ids[0]);
			jobid = id; // 柜号id赋值，用于后面给下载文件命名
			String sql;
			try {
				sql = "SELECT f_edi_manifest('type=jobids;id=" + str
						+ "') AS edi;";
				Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				this.manifestmsgContent = m.get("edi").toString();
				this.update.markUpdate(UpdateLevel.Data, "manifestmsgContent");
				showManifestWindow.show();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	@Action
	public void outputMANIFEST(){
		this.fileName = "MFJ_" + parentid + ".txt";
		this.contentType = "text/plain";
	}
	
	@Bind(id="fileDownLoad", attribute="src")
    private InputStream getDownload5() throws Exception {
		InputStream input = new ByteArrayInputStream(this.manifestmsgContent.getBytes());
		return input;
    }
	
	@Action
	public void suminfo(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择至少一条记录！");
			return;
		} 
		String sql = "SELECT * FROM f_fina_jobs_childs_suminfo('jobids="+StrUtils.array2List(ids)+";userid="+AppUtils.getUserSession().getUserid()+"');";
		try {
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			alert("OK");
		}catch(NoRowException e){
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void creatarap(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择至少一条记录！");
			return;
		} 
		StringBuffer stringBuffer = new StringBuffer();
		String id = StrUtils.array2List(ids);
		stringBuffer.append("\nSELECT f_fina_jobs_childs_creatarap('jobids="+id+";usercode="+AppUtils.getUserSession().getUsercode()+"');");
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
