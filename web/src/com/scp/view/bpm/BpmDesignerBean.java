package com.scp.view.bpm;

import java.util.Map;
import java.util.UUID;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.bpm.BpmProcess;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "bpm.bpmdesignerBean", scope = ManagedBeanScope.REQUEST)
public class BpmDesignerBean extends GridFormView {
	
	@SaveState
	@Accessible
	public BpmProcess selectedRowData = new BpmProcess();
	
	@Override
	public void add(){
		super.add();
		selectedRowData = new BpmProcess();
		//String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		UUID id=UUID.randomUUID();
        String[] idd=id.toString().split("-");
		selectedRowData.setCode("BPM-"+idd[0].toUpperCase());
		selectedRowData.setVersion(0);
	}
	
	@Action
	public void grid_onrowselect() {
		String[] ids = this.grid.getSelectedIds();
		if( ids.length > 0 ||ids !=null ){
			this.pkVal = Long.valueOf(ids[0]);
			this.update.markUpdate(UpdateLevel.Data, "pkVal");
			doServiceFindData();
			showFormDefine();
			showStatus();
			showFormShow();
			showTrackGo();
		}
	}
	
	@Bind
	@SaveState
	public String actionJsText;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.pkVal = -1l;
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)){
				this.pkVal = Long.valueOf(id);
				this.update.markUpdate(UpdateLevel.Data, "pkVal");
				showFormDefine();
				doServiceFindData();
			}
			/*if(!AppUtils.isDebug){
				actionJsText = "extractProcessJsVar.hide();";
				this.update.markUpdate(UpdateLevel.Data, "actionJsText");
			}*/
		}
	}
	
	
	@Bind
	public UIIFrame formdefineIframe;
	
	@Action
	public void showFormDefine() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			formdefineIframe.load(blankUrl);
		} else {
			formdefineIframe.load("/scp/pages/sysmgr/cfg/formdefine.xhtml?formtype=B"+"&modelCode="+this.getMBeanName()+"."+selectedRowData.getCode());
		}
	}
	
	@Bind
	public UIIFrame formShowIframe;
	
	@Action
	public void showFormShow() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.xhtml";
			formShowIframe.load(blankUrl);
		} else {
			formShowIframe.load("/scp/pages/module/formdefine/dynamicform.xhtml?m="+this.getMBeanName()+"."+selectedRowData.getCode()+"&p="+this.pkVal);
		}
	}
	
	
	@Bind
	public UIIFrame statusIframe;
	/**
	 * 状态
	 */
	@Action
	public void showStatus() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			statusIframe.load(blankUrl);
		} else {
			statusIframe.load("./bpmtaskassign.xhtml?processId="+ this.pkVal+"&type=S");
		}
	}
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.bpmProcessService.bpmProcessDao.findById(this.pkVal);
	}
	@Override
	protected void doServiceSave() {
		this.serviceContext.bpmProcessService.bpmProcessDao.createOrModify(selectedRowData);
	}
	
	@Bind
	public UIIFrame trackGoIframe;
	
	/**
	 *流程图
	 */
	@Action
	public void showTrackGo() {
		if (this.pkVal == -1l) {
			String blankUrl = AppUtils.getContextPath()
					+ "/pages/module/common/blank.html";
			trackGoIframe.load(blankUrl);
		} else {
			trackGoIframe.load("/bpm/model/index.html"+"?id="+this.pkVal+"&language="+AppUtils.getUserSession().getMlType());
		}
	}
	
	/**
	 *复制新增
	 */
	@Action
	public void addcopy() {
		if (this.pkVal == -1l) {
			MessageUtils.alert("请先选择一条记录");
		} else {
			try{
			UUID id=UUID.randomUUID();
	        String[] idd=id.toString().split("-");
	        String code = "BPM-"+idd[0].toUpperCase();
	        BpmProcess bpmProcess = this.serviceContext.bpmProcessService.findBpmProcessByCode(code);
	        while(bpmProcess != null){
	        	id=UUID.randomUUID();
	        	idd=id.toString().split("-");
		        code = "BPM-"+idd[0].toUpperCase();
	        }
			String sqlsub = "SELECT f_bpm_process_copy('id="+this.pkVal+";code="+code+";model="+this.getMBeanName()+"."+code+";beanname="+this.getMBeanName()+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			MessageUtils.alert(sub);
			this.grid.reload();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.bpmProcessService.removeDate(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	
	@Bind
	public UIWindow extractProcessWindow;
	
	@Bind
	public String extractText;
	
	@Action
	public void extractProcess(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String str = StrUtils.array2List(ids);
		String sql = "SELECT f_bpm_release('id="+str+"') AS value";
		try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null){
				extractText = StrUtils.getMapVal(map, "value");
				update.markUpdate(true, UpdateLevel.Data, "extractText");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		extractProcessWindow.show();
	}
}
