package com.scp.schedule;

import java.util.List;
import java.util.Map;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;

/**
 * 
 * 自动执行fixbug函数
 * 
 * @author neo 201601
 * 
 */
public class AutoSendIM {

//	private static boolean isRun = false;
	
	public AutoSendIM() {
		super();
		//System.out.println("AutoPerformDMLPool ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public void execute() throws Exception {
		//AppUtils.debug("AutoSendIM Start:" + new Date());
//		if (isRun) {
//			System.out.println("@@@ AutoSendIM wraning:another process is running return!");
//			return;
//		}
//		isRun = true;
		try {
			findsysim();
			//System.out.println("AutoSendIM.execute()");
		} catch (Exception e) {
			throw e;
		} finally {
//			isRun = false;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void findsysim()throws Exception{
		DaoIbatisTemplate daoIbatisTemplate;
		try {
			daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql("SELECT * FROM sys_im WHERE issend = FALSE and sendid is not null and receiveid is not null and msg is not null and msg <> '' AND im_type = 'q' ORDER BY sendtime desc limit 10;");
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					AppUtils.sendIMMsg(list.get(i).get("sendid").toString(), list.get(i).get("receiveid").toString(), list.get(i).get("msg").toString());
					String setsensql = "UPDATE sys_im SET issend = TRUE WHERE id="+list.get(i).get("id");
					daoIbatisTemplate.updateWithUserDefineSql(setsensql);
					System.out.println("qq消息已发送成功");
				}
			}
			
//			list = daoIbatisTemplate.queryWithUserDefineSql("SELECT * FROM sys_im WHERE issend = FALSE and sendid is not null and receiveid is not null and msg is not null and msg <> '' AND im_type = 'w' ORDER BY sendtime desc limit 1;");
//			if(list!=null&&list.size()>0){
//				for(int i=0;i<list.size();i++){
//					String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
//					String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
//					String response = WeiXinUtil.messageSendByTemplate(appID, appsecret, list.get(i).get("msg").toString());
//					String setsensql = "UPDATE sys_im SET issend = TRUE WHERE id="+list.get(i).get("id");
//					daoIbatisTemplate.updateWithUserDefineSql(setsensql);
//					System.out.println("微信消息已发送成功");
//				}
//			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
	}

}