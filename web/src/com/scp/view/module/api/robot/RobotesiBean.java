package com.scp.view.module.api.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.google.gson.Gson;
import com.scp.model.api.ApiRobotEsi;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.utils.HttpUtil;

@ManagedBean(name = "pages.module.api.robot.robotesiBean", scope = ManagedBeanScope.REQUEST)
public class RobotesiBean extends GridFormView{

	
	
	@SaveState
	@Accessible
	public ApiRobotEsi selectedRowData = new ApiRobotEsi();
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			//this.add();
		}
	}
	

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		// ATD日期和工作单日期区间查询
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		return m;
	}
	
	
    @Action
	public void submitApi(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择记录");
			return;
		}
		try {
			for (String idStr : ids) {
				ApiRobotEsi apiRobotEsi = this.serviceContext.apiRobotEsiService.apiRobotEsiDao.findById(Long.valueOf(idStr));
				String usercode=apiRobotEsi.getInputer();
				String json = apiRobotEsi.getJsonpost();
				JSONObject jsonObject = JSONObject.fromObject(json);
				
				String url = ConfigUtils.findRobotCfgVal("server_url");
				//System.out.println(jsonObject);
				
				url=url+"/cargoService/reqSI";
				
				String status=null;
				String result=null;
				String jsondate=null;
				
				jsondate = jsonObject.toString();
				Gson gson = new Gson();
				
				String res = HttpUtil.post(url,jsondate);
				System.out.println("res:"+res);
				Map<String, Object> ret = new HashMap<String, Object>();
				ret= gson.fromJson(res, ret.getClass());
				try{
					result=ret.get("result").toString();
					if(!result.isEmpty()){
						apiRobotEsi.setUuid(result);
						this.serviceContext.apiRobotEsiService.apiRobotEsiDao.modify(apiRobotEsi);
					}
					MessageUtils.alert("OK!");
				}catch(Exception e){	
					MessageUtils.alert("返回结果:连接失败！");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    @Action
	public void checkApi(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择记录");
			return;
		}
		if (ids.length > 1 ) {
			MessageUtils.alert("不能选择多条记录");
			return;
		}
		ApiRobotEsi apiRobotEsi = this.serviceContext.apiRobotEsiService.apiRobotEsiDao.findById(Long.valueOf(ids[0]));
		if (null !=apiRobotEsi) {
			    String usercode = apiRobotEsi.getInputer();
				long id = apiRobotEsi.getId();
				String jobId = apiRobotEsi.getUuid();
				String status=null;
				String result=null;
				if(jobId==null){
					MessageUtils.alert("请先提交本条记录，再进行查询");
					return;
				}
				try {	
					String json="{\"jobID\":\""+jobId+"\"}";
				    String url = ConfigUtils.findRobotCfgVal("server_url")+"/cargoService/respSI";
				    String res = HttpUtil.post(url, json);
					System.out.println(res);
					Gson gson = new Gson();
					Map<String, Object> ret = new HashMap<String, Object>();
					ret= gson.fromJson(res, ret.getClass());
					try{
						result=ret.get("result").toString();
						status=ret.get("msg").toString();
					}catch(Exception e){	
						MessageUtils.alert("返回结果:"+res+"");
						return;
					}
					boolean b1 = Pattern.matches("SI was saved",result);
					boolean b2 = Pattern.matches("Draft has been successfully saved",result);
					if(b1==true||b2==true){
						 status="ok";
					}else{
						status="false";
					}
					String insertSql = "update api_robot_esi set response='" + StrUtils.getSqlFormat(ret.toString())
					+ "',status='" + StrUtils.getSqlFormat(status) + "' where id='" + id + "'";
					MessageUtils.alert("返回结果:'"+ret+"'");
					this.refresh();
					this.serviceContext.apiRobotEsiService.apiRobotEsiDao.executeSQL(insertSql);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
    
    @Override
	public void del() {
    	
    	if(!"demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())){
    		this.alert("无权删除!");
    		return;
    	}
    	
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.apiRobotEsiService.removeDate(ids);
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}


	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.apiRobotEsiService.apiRobotEsiDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.apiRobotEsiService.saveData(selectedRowData);
	     this.alert("OK");
	}
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		Browser.execClientScript("showJson();");
	}
}
