package com.scp.view.module.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.PriceFclAsk;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.data.analyseBean", scope = ManagedBeanScope.REQUEST)
public class AnalyseBean extends GridFormView {
   
	
	
	@SaveState
	@Accessible
	public PriceFclAsk selectedRowData = new PriceFclAsk();
	
	@Override
	public void add() {
		selectedRowData = new PriceFclAsk();
		super.add();
	}
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			
		}
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
				stringBuilder.append("\nUPDATE price_fcl_asking set isdelete = TRUE WHERE id = " + id + ";");
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
			this.serviceContext.priceFclAskService.removeDate(selectedRowData.getId());
			refresh();
			this.add();
			this.alert("OK");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");

		String existsSql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole a,sys_role b WHERE a.roleid =  b.id AND a.userid = "+AppUtils.getUserSession().getUserid()+" AND b.code = 'dev') AS existflag";
		Map existsMap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(existsSql);
		if(existsMap != null && "true".equals(StrUtils.getMapVal(existsMap, "existflag"))){//开发测试组可以看到所有的
			
		}else{
			qry+="AND (askerid='"+AppUtils.getUserSession().getUserid()+"' OR resaskerid='"+AppUtils.getUserSession().getUserid()+"')";
			
		}
		
		
		m.put("qry", qry);
		return m;
	}
	
	
	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		selectedRowData = this.serviceContext.priceFclAskService.priceFclDao.findById(this.pkVal);
	}
  
	
	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		selectedRowData.setResaskerid(AppUtils.getUserSession().getUserid());
		selectedRowData.setResper(AppUtils.getUserSession().getUsername());
		selectedRowData.setResptime(new Date());
		this.serviceContext.priceFclAskService.saveData(selectedRowData);
		this.alert("OK!");
		this.grid.reload();
	}
	@Bind
	@SaveState
	public String corpid;
	
	@Action
	public void showCreateJobs() {
	
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选择一条记录!");
			return;
		}
		try{
			String sql = "select b.id from sys_user a,sys_corporation b where a.corpid=b.id AND a.id="+AppUtils.getUserSession().getUserid();
			Map map=this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			corpid=map.get("id").toString();
			update.markUpdate(true, UpdateLevel.Data, "corpid");
		}catch(Exception e){
			
		}
		Browser.execClientScript("createJobsWindowJsVar.show();polGridSelectJsVar.setValue(gridJsvar.getSelectionModel().getSelected().get('pol'));");
	}
	@Bind
	@SaveState
	public String z20gp = "0";
	@Bind
	@SaveState
	public String z40gp = "0";
	@Bind
	@SaveState
	public String z40hq = "0";
	@Bind
	@SaveState
	public String z45hq = "0";
	@Bind
	@SaveState
	public Long customerid;
	@Bind
	@SaveState
	public String corpidop;
	
	@Bind
	@SaveState
	public String customerabbr="";
	
	@SaveState
	public Long userid;
	@Bind
	@SaveState
	public String pieceother = "0";
	
	@Action
	public void createJobs() {
		try {
			String[] ids = this.grid.getSelectedIds();
			Long userid = AppUtils.getUserSession().getUserid();
			PriceFclAsk priceFclAsk=this.serviceContext.priceFclAskService.priceFclDao.findById(Long.parseLong(ids[0]));
			String uuid = priceFclAsk.getFclid();
			String priceid = uuid.split("-")[0];
			String bargeid = uuid.split("-")[1];
			//System.out.println("corpid"+corpid);
			String sql = "SELECT unnest(f_price_askresp2job('priceid="+priceid+";bargeid="+bargeid+";20gp="+z20gp
			+";40gp="+z40gp+";40hq="+z40hq+";45hq="+z45hq+";pieceother="+pieceother+";userid="+userid
			+";customerid="+customerid+";pol="+priceFclAsk.getPol().replaceAll("'", "''")+";pollink="+priceFclAsk.getPol().replaceAll("'", "''")+";corpid="+corpid+";corpidop="+corpidop+"')) AS jobid";
		    System.out.println(sql);
			List<Map> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			String jobid = StrUtils.getMapVal(map.get(0), "jobid");
			String alter45hq = StrUtils.getMapVal(map.get(1), "jobid");
			if(!StrUtils.isNull(alter45hq)){
				Browser.execClientScript("layer.alert('"+alter45hq+"', {icon: 7},function(){window.open('/scp/pages/module/ship/jobsedit.xhtml?id="
						+jobid+"');layer.close(layer.index);});");
			}else{
				Browser.execClientScript("window.open('/scp/pages/module/ship/jobsedit.xhtml?id="+jobid+"');");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
