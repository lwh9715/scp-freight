package com.scp.view.module.api.robot;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.google.gson.Gson;
import com.scp.model.api.ApiRobotBook;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.utils.HttpUtil;
@ManagedBean(name = "pages.module.api.robot.robotbookBean", scope = ManagedBeanScope.REQUEST)
public class RobotbookBean extends GridFormView {
	
	
	@SaveState
	@Accessible
	public ApiRobotBook selectedRowData = new ApiRobotBook();
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			//this.add();
		}
	}
	@Override
	public void del() {
		if(!"demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())){
    		this.alert("无权删除!");
    		return;
    	}
		super.del();
		String[] ids = this.grid.getSelectedIds();
		
		if (ids == null || ids.length < 0) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.apiRobotBookService.removeDate(ids);
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
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
				
				ApiRobotBook apiRobotBook = this.serviceContext.apiRobotBookService.apiRobotBookDao.findById(Long.valueOf(idStr));
				String json = apiRobotBook.getJsonpost();
				
				String usercode=apiRobotBook.getInputer();
				JSONObject jsonObject = JSONObject.fromObject(json);
				ApiEncryptUtil apiEncryptUtil = new ApiEncryptUtil();
				
				String carriercode = apiRobotBook.getCarriercode();
				if(StrUtils.isNull(carriercode)){
					MessageUtils.alert("船公司不能为空!");
					continue;
				}
				if("COSCO".equalsIgnoreCase(carriercode)){
					jsonObject.put("authName", apiEncryptUtil.encrypt(ConfigUtils.findRobotCfgVal("cosoc_bk_user",usercode)));
					String pwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("cosoc_bk_pwd",usercode));
					if(!StrUtils.isNull(pwd)){
						pwd = apiEncryptUtil.encrypt(pwd);
					}
					jsonObject.put("loginPin", pwd);
					jsonObject.put("notifyMail", ConfigUtils.findRobotCfgVal("cosoc_bk_email",usercode));
				}else if("HPL".equalsIgnoreCase(carriercode)){
					jsonObject.put("authName", apiEncryptUtil.encrypt(ConfigUtils.findRobotCfgVal("hpl_bk_user",usercode)));
					String pwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("hpl_bk_pwd",usercode));
					if(!StrUtils.isNull(pwd)){
						pwd = apiEncryptUtil.encrypt(pwd);
					}
					jsonObject.put("loginPin", pwd);
					jsonObject.put("notifyMail", ConfigUtils.findRobotCfgVal("hpl_bk_email",usercode));
				}else if("COSCO-E".equalsIgnoreCase(carriercode)){
					String pwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("cosoc_ele_pwd",usercode));
					if(!StrUtils.isNull(pwd)){
						pwd = apiEncryptUtil.encrypt(pwd);
					}
					jsonObject.put("authName", apiEncryptUtil.encrypt(ConfigUtils.findRobotCfgVal("cosoc_ele_user",usercode)));
					jsonObject.put("loginPin", pwd);
					jsonObject.put("notifyMail", ConfigUtils.findRobotCfgVal("cosoc_ele_email",usercode));
					String paypwd = new EMailSendUtil().decrypt(ConfigUtils.findRobotCfgVal("cosoc_ele_paypwd",usercode));
					if(!StrUtils.isNull(paypwd)){
						paypwd = apiEncryptUtil.encrypt(paypwd);
					}
					jsonObject.put("payPin", paypwd);
				}
				String status=null;
				String result=null;
				String jsondate=null;
				jsonObject.put("carrierCode", carriercode);
				String url = ConfigUtils.findRobotCfgVal("server_url",usercode);
				Gson gson = new Gson();
				Map<String, Object> jsonAll = new HashMap<String, Object>();
				jsonAll= gson.fromJson(json, jsonAll.getClass());
				if(jsonAll.get("nMax").toString().equals("1")){
					url=url+"/cargoService/reqBooking";
					System.out.println(url);
					System.out.println(jsonObject);
					jsondate="["+jsonObject.toString()+"]";
				}else{
					url=url+"/cargoService/reqOneBooking";
					System.out.println(url);
					System.out.println(jsonObject);
					jsondate=jsonObject.toString();
				}
				String res = HttpUtil.post(url,jsondate);
				System.out.println("res:"+res);
				Map<String, Object> ret = new HashMap<String, Object>();
				ret= gson.fromJson(res, ret.getClass());
				try{
					result=ret.get("result").toString();
					status=ret.get("msg").toString();
				}catch(Exception e){	
					MessageUtils.alert("返回结果:连接失败！");
					return;
				}
				if(!result.isEmpty()){
					apiRobotBook.setUuid(result);
					apiRobotBook.setStatus(status);
				}
				System.out.print(result);
				apiRobotBook.setResponse(res);
				this.serviceContext.apiRobotBookService.apiRobotBookDao.createOrModify(apiRobotBook);
				MessageUtils.alert("OK");
				this.refresh();
				//String insertSql = "update api_robot_book set response=" + res + ",status=" + res + " where id=" + apiRobotBook.getId() + "";
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
		ApiRobotBook apiRobotBook = this.serviceContext.apiRobotBookService.apiRobotBookDao.findById(Long.valueOf(ids[0]));
		if (null !=apiRobotBook) {
			    String usercode=apiRobotBook.getInputer();
				long id =apiRobotBook.getId();
				String jobId=apiRobotBook.getUuid();
				String status=null;
				String result=null;
				if(jobId==null){
					MessageUtils.alert("请先提交本条记录，再进行查询");
					return;
				}
				try {	
					String json="{\"jobID\":\""+jobId+"\"}";
				    String url = ConfigUtils.findRobotCfgVal("server_url",usercode)+"/cargoService/respBooking";
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
					String insertSql = "update api_robot_book set response='" + StrUtils.getSqlFormat(ret.toString())
					+ "',status='" + StrUtils.getSqlFormat(status) + "' where id='" + id + "'";
					MessageUtils.alert("返回结果:'"+ret+"'");
					this.refresh();
					this.serviceContext.apiRobotBookService.apiRobotBookDao.executeSQL(insertSql);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.apiRobotBookService.apiRobotBookDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		this.serviceContext.apiRobotBookService.saveData(selectedRowData);
		this.alert("OK");
	}


	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		Browser.execClientScript("showJson(jsonJsVar.getValue());");
	}
	
	
	
}
