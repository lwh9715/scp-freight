package com.scp.view.module.data;

import com.scp.model.data.DatPort;
import com.scp.service.data.PortyMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import java.util.Map;

@ManagedBean(name = "pages.module.data.portBean", scope = ManagedBeanScope.REQUEST)
public class PortBean extends GridFormView {

	@ManagedProperty("#{portyMgrService}")
	public PortyMgrService portyMgrService;
	
	@SaveState
	@Accessible
	public DatPort selectedRowData = new DatPort();
	
	@Bind
	public UIIFrame edi_mapping;
	

	@Bind
	public UIWindow knowledgeBaseWindow;
	
	@Bind
	public UIIFrame knowledgeBaseIframe;
	
	@Bind
	@SaveState
	public boolean isship;
	
	@Bind
	@SaveState
	public boolean isair;
	
	@Bind
	@SaveState
	public boolean ispol;
	
	@Bind
	@SaveState
	public boolean ispod;
	
	@Bind
	@SaveState
	public boolean isbarge;
	
	@Bind
	@SaveState
	public boolean istrain;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String ediUrl = "../edi/edi_map.aspx?edittype=port";
			this.edi_mapping.load(ediUrl);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(isship){
			qry += "\nAND isship = true";
		}
		if(isair){
			qry += "\nAND isair = true";
		}
		if(ispol){
			qry += "\nAND ispol = true";
		}
		if(ispod){
			qry += "\nAND ispod = true";
		}
		if(isbarge){
			qry += "\nAND isbarge = true";
		}
		if(istrain){
			qry += "\nAND istrain = true";
		}
		
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void add() {
		selectedRowData = new DatPort();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = portyMgrService.datPortDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		portyMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			StringBuilder stringBuilder = new StringBuilder();
			for (String id : ids) {
				stringBuilder.append("\nUPDATE dat_port set isdelete = TRUE WHERE id = " + id + ";");
			}
			if(stringBuilder.length() > 0){
				this.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
			}
			refresh();
		}
	}
	
	@Override
	public void del() {
		if(selectedRowData.getId()==0){
			this.add();	
		}else{
			portyMgrService.removeDate(selectedRowData.getId());
			refresh();
			this.add();
			this.alert("OK");
		}
	}
	
	@Action
	public void showKnowledgeBase(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			knowledgeBaseIframe.load( "../../sysmgr/knowledge/knowledgeBase.xhtml?linkid="+getGridSelectId()+"&tablename=dat_port");
			knowledgeBaseWindow.show();
		}
	}
	
	
	@Bind
    @SaveState
    public String linkBatch;
	
	@Action
	public void saveLink(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			StringBuilder stringBuilder = new StringBuilder();
			for (String id : ids) {
				stringBuilder.append("\nUPDATE dat_port set link = '"+linkBatch+"' WHERE id = " + id + ";");
			}
			if(stringBuilder.length() > 0){
				this.daoIbatisTemplate.updateWithUserDefineSql(stringBuilder.toString());
			}
			
			refresh();
		}
	}
	
	@Bind
	public UIWindow showbatchupdateWindow;
	
	@Action
	public void batchModify() {
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0) { 
			MessageUtils.alert("请选择一条记录"); 
		}else{
			showbatchupdateWindow.show();
		}
	}
	
	
	@Bind
	@SaveState
	private String country;
	
	@Bind
	@SaveState
	private String province;
	
	@Bind
	@SaveState
	private String city;
	
	@Bind
	@SaveState
	private String line;
	
	@Bind
	@SaveState
	public boolean isshipbatch;
	
	@Bind
	@SaveState
	public boolean isairbatch;
	
	@Bind
	@SaveState
	public boolean ispolbatch;
	
	@Bind
	@SaveState
	public boolean ispodbatch;
	
	@Bind
	@SaveState
	public boolean ispddbatch;
	
	@Bind
	@SaveState
	public boolean isdestinationbatch;
	
	@Bind
	@SaveState
	public boolean isbargebatch;
	
	
	@Action
	public void portChanged() { 
		String[] ids = this.grid.getSelectedIds(); 
		String user = AppUtils.getUserSession().getUsercode();
		try {
			portyMgrService.editPort(ids,country,province,city,line,isshipbatch,isairbatch,ispolbatch,ispodbatch,ispddbatch,isdestinationbatch,isbargebatch,user);
			showbatchupdateWindow.close();
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
}
