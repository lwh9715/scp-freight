package com.scp.view.base;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

@ManagedBean(name = "main.headerBean", scope = ManagedBeanScope.REQUEST)
public class HeaderBean {
	
//	IndexBean indexBean;
//	
//	@Inject
//	private PartialUpdateManager update = PartialUpdateManager.getInstance();
//	
//	
//	CommonDBCache l = (CommonDBCache) AppUtils.getBeanFromSpringIoc("commonDBCache");
//	
//	
//	@BeforeRender
//	public void beforeRender(boolean isPostback) {
//		if (!isPostback) {
//			waitfor();
//		}
//		
//	}
//	
//	@Action
//	public void timerjudge(){
//		waitfor();
//	}
//	
//	@Bind
//	@SaveState
//	public String waitforrq = "";
//	
//	
//	@Bind
//	@SaveState
//	public String waitforgoods = "";
//	
//	public String getwaitforgoods() {
//		try {
//			if (AppUtils.isLogin()) {
//				String sql = "SELECT COUNT(DISTINCT id) AS counts FROM _wf_jobs_fcl t WHERE  (t.workitemState = 0 OR t.workitemState = 1) AND t.processId = 'ReleaseBillProcess'"
//					+"\nAND (actor = '" + AppUtils.getUserSession().getUsercode() +"'" +")";
//							//" OR processcreatorid = '"+AppUtils.getUserSession().getUsercode()+"')";
//				Map map = AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//				if(map.get("counts").toString().equals("0")){
//					waitforgoods="";
//				}else{
//					MLType mlType = AppUtils.getUserSession().getMlType();
//					if(mlType.equals(LMapBase.MLType.en)){
//						waitforgoods = (String)l.getLs("放货")+"("+ map.get("counts").toString()+")";
//					}else{
//						waitforgoods = "放货("+ map.get("counts").toString()+")";
//					}
//					
//				}
//			}
//		} catch (Exception e) {
//			 e.printStackTrace();
//		}
//		return waitforgoods;
//	}
//	
//	public String getwaitforrq() {
//		try {
//			if (AppUtils.isLogin()) {
//				String sql = "SELECT count(DISTINCT id) FROM _wf_payreq j WHERE (j.workitemState = 0 OR j.workitemState = 1) AND j.processId = 'PaymentRequest'"
//					+"\nAND (actor = '" + AppUtils.getUserSession().getUsercode() +"' " + ") AND suspended = FALSE";
//							//"OR processcreatorid = '"+AppUtils.getUserSession().getUsercode()+"')";
//				Map map = AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//				if(map.get("count").toString().equals("0")){
//					waitforrq = "" ;
//				}else{
//					MLType mlType = AppUtils.getUserSession().getMlType();
//					if(mlType.equals(LMapBase.MLType.en)){
//						waitforrq = (String)l.getLs("付款")+"("+map.get("count").toString()+")";
//					}else{
//						waitforrq = "付款("+ map.get("count").toString()+")";
//					}
//				}
//			}
//		} catch (Exception e) {
//			 e.printStackTrace();
//		}
//		return waitforrq;
//	}
//	
////	public String time = "";
////	
////	public String gettime() {
////		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
////		time = dateformat.format(new Date());
////		return time;
////	}
//	@Action
//	public void waitfor(){
//		getwaitforgoods();
//		getwaitforrq();
////		gettime();
//		gettobedealt();
//		update.markUpdate(true, UpdateLevel.Data, "waitforPanel");
//		Browser.execClientScript("processTipsShowUp()");
//	}
//	
//	@Bind
//	@SaveState
//	public String tobedealt = "";
//	
//	public String gettobedealt(){
//		getwaitforgoods();
//		getwaitforrq();
//		if(waitforgoods.equals("")&&waitforrq.equals("")){
//			tobedealt = "";
//		}else{
//			MLType mlType = AppUtils.getUserSession().getMlType();
//			if(mlType.equals(LMapBase.MLType.en)){
//				tobedealt = (String)l.getLs("待办")+":";
//			}else{
//				tobedealt = "待办：";
//			}
//		}
//		return tobedealt;
//	}
//	
//	@Bind
//	@SaveState
//	public String ufmsurl = "";
//	public String getufmsurl(){
//		String ufms_url = ConfigUtils.findSysCfgVal("ufms_url");
//		if(StrUtils.isNull(ufms_url)){
//			return "";
//		}
//		return "UFMS";
//	}
}
