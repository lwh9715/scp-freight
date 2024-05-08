package com.scp.view.module.api.robot;

import java.util.Set;
import java.util.Vector;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.api.ApiRobotConfig;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.sysmgr.cfg.BaseCfgBean;


@ManagedBean(name = "pages.module.api.robot.robotconfigBean", scope = ManagedBeanScope.REQUEST)
public class RobotConfigBean extends BaseCfgBean{
	
	@SaveState
	@Accessible
	public ApiRobotConfig selectedRowData =new ApiRobotConfig();
	
	
	@Resource
	public ServiceContext serviceContext;
	
	@BeforeRender
	protected void beforeRender(boolean isPostback) {
		if (!isPostback) {
			try {
				initData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Action
	public void saveMain(){
		saveRobot();
	}
	
	
	@Action
	public void saveBKRbot(){
		saveRobot();
	}
	
	@Action
	public void saveEIRbot(){
		saveRobot();
	}
	@Action
	private void saveRobot() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				String val = this.getCfgDataMap().get(key);
				if(key.endsWith("pwd")){
					if(!StrUtils.isNull(val)){
						if(val.length()>0&&val.length()<200){ //如果是明文，加密，如果已经加密过，不能再加密，否则无法解密
							val = new EMailSendUtil().encrypt(val);
						}
					}
				}
				ConfigUtils.refreshRobotMap(key, val, AppUtils.getUserSession().getUserid());
			}
			
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add("server_url");
		vector.add("cosoc_bk_user");
		vector.add("cosoc_bk_pwd");
		vector.add("cosoc_bk_email");
		vector.add("hpl_bk_user");
		vector.add("hpl_bk_pwd");
		vector.add("hpl_bk_email");
		vector.add("cosoc_esi_user");
		vector.add("cosoc_esi_pwd");
		vector.add("cosoc_esi_email");
//		vector.add("hpl_esi_user");
//		vector.add("hpl_esi_pwd");
//		vector.add("hpl_esi_email");
		
		vector.add("yml_esi_user");
		vector.add("yml_esi_pwd");
		vector.add("yml_esi_email");
		
		vector.add("inttra_esi_user");
		vector.add("inttra_esi_pwd");
		vector.add("inttra_esi_email");
		
		vector.add("oocl_esi_user");
		vector.add("oocl_esi_pwd");
		vector.add("oocl_esi_email");
		
		vector.add("emc_esi_user");
		vector.add("emc_esi_pwd");
		vector.add("emc_esi_email");
		
		vector.add("whl_esi_user");
		vector.add("whl_esi_pwd");
		vector.add("whl_esi_email");
		
		vector.add("cosoc_ele_user");
		vector.add("cosoc_ele_pwd");
		vector.add("cosoc_ele_email");
		vector.add("cosoc_ele_paypwd");
		return vector;
	}

	@Override
	protected String getQuerySql() {
		return 
		"\nSELECT * " +
		"\nFROM api_robot_config " +
		"\nWHERE 1=1 "+
		"\nAND userid="+AppUtils.getUserSession().getUserid();
	}
	
	
	
	

	

}
