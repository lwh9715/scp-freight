package com.scp.service.sysmgr;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.ship.BusShippingDao;
import com.scp.dao.sys.SysUserAssignDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.SysUserAssign;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.JSONUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.wx.WeiXinUtil;

/**
 * @author Administrator
 *
 */
@Component
public class SysUserAssignMgrService{
	
	/* 转译字符
	  空格 - %20
	" - %22
	# - %23
	% - %25
	& - %26
	( - %28
	) - %29
	+ - %2B
	, - %2C
	/ - %2F
	: - %3A
	; - %3B
	< - %3C
	= - %3D
	> - %3E
	? - %3F
	@ - %40
	\ - %5C
	| - %7C
	*/
	
	
	@Resource
	public SysUserAssignDao sysUserAssignDao;
	
	@Resource
	public SysUserDao sysUserDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public ApplicationConf applicationConf;
	
	@Resource
	public BusShippingDao busShippingDao;
	
	public void saveData(SysUserAssign data) {
		if(0 == data.getId()){
			sysUserAssignDao.create(data);

			String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
					+ "\nVALUES (getid(),'sys_user_assign','新增','新增后数据为" + data.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
			daoIbatisTemplate.updateWithUserDefineSql(operatesql);
		}else{
			sysUserAssignDao.modify(data);

            SysUserAssign dataold = sysUserAssignDao.findById(data.getId());
			String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
					+ "\nVALUES (getid(),'sys_user_assign','修改','修改前数据为" + dataold.toString() + "\r\n\r\n 修改后数据为" + data.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
			daoIbatisTemplate.updateWithUserDefineSql(operatesql);
		}
	}

	public void removeDate(Long id) {
        SysUserAssign dataold = sysUserAssignDao.findById(id);
        String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
                + "\nVALUES (getid(),'sys_user_assign','删除','删除前数据为" + dataold.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
        daoIbatisTemplate.updateWithUserDefineSql(operatesql);

		SysUserAssign data = sysUserAssignDao.findById(id);
		sysUserAssignDao.remove(data);
	} 
	
	public void sendMessageToCustomerService(Long sendid,Long uid , String id,String orderno,String senderUserName,String basePath,String namec,String namee , String msgUserInput) {
		String sql = "select * FROM _sys_user_assign t WHERE id ="+id;
		Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		boolean islanguge = false;//1665 内部QQ及通知内容 中英文处理
		if(map!=null&&map.size()>0){
			String userid = map.get("userid").toString();
			String usercode = map.get("usercode").toString();
			String linkid = map.get("linkid").toString();
			
			String langugesql = ConfigUtils.findSysCfgVal("sys_cfg_mult_language");
			if("Y".equalsIgnoreCase(langugesql)){
				islanguge = true;
			}
			
			List<String> receiveCodes = new ArrayList<String>();
			receiveCodes.add(usercode);
			String remindTitle = "消息提醒";
			String remindContext = senderUserName + " 发来的订舱跟进提醒:";
//			String url = basePath 
//			+ "pages/module/order/busorderfcl.aspx?type=edit"+"%26"+"id="
//			+ linkid;
			String url = "../pages/module/order/busorderfcl.aspx?type=edit"+"%26"+"id="
			+ linkid;//2602 内部qq信息 ，地址改为用相对地址
			String sendContext = "业务员" + namec + "(" + namee + ")"
			+ "的订单[url=" + url + "]" + orderno
			+ "[/url]已生成,请跟进!";
//			if(applicationConf.getIsUseDzz()){
//				AppUtils.sendMessage(uid , receiveCodes, remindTitle, remindContext, url,
//					sendContext);
//			}
			
			String a = "订单[%3Ca target=_blank href=" + url + "%3E" + orderno
				+ "%3C/a%3E]已生成，请跟进！"
				+(islanguge?("<br>Order[%3Ca target=_blank href=" + url + "%3E" + orderno
						+ "%3C/a%3E]Generated, please follow up！"):"");
			String msg = "订单跟进提醒:" + a  + ":" + msgUserInput;
			//System.out.println(msg);
			AppUtils.sendIMMsg(sendid.toString(), userid, msg);
		}
	}
	
	public void sendMessageToCustomerServicejob(Long sendid,Long uid , String id,String orderno,String senderUserName,String basePath,String namec,String namee , String msgUserInput) {
		String sql = "select *,(SELECT x.id FROM fina_jobs x,bus_shipping y WHERE y.id = t.linkid AND y.jobid = x.id AND x.isdelete = FALSE) AS jobid "
					+"FROM _sys_user_assign t WHERE id ="+id;
		Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		boolean islanguge = false;//1665 内部QQ及通知内容 中英文处理
		if(map!=null&&map.size()>0){
			String userid = map.get("userid").toString();
			String usercode = map.get("usercode").toString();
			String jobid = map.get("jobid").toString();
			List<String> receiveCodes = new ArrayList<String>();
			receiveCodes.add(usercode);
			String remindTitle = "消息提醒";
			String remindContext = senderUserName + "发来的工作单跟进提醒：";
//			String url = basePath + "pages/module/ship/jobsedit.aspx?id=" + jobid;
			String url = "../pages/module/ship/jobsedit.aspx?id=" + jobid;//2602 内部qq信息 ，地址改为用相对地址
			String langugesql = "SELECT 1 AS language FROM sys_config WHERE key = 'sys_cfg_mult_language' AND val = 'Y'";
			try {
				Map langugemap = this.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(langugesql);//系统设置是否是多语言
				if (langugemap != null && langugemap.get("language") != null&& langugemap.get("language").toString().equals("1")) {
					islanguge = true;
				}
			} catch (Exception e) {
			}
//			if(applicationConf.getIsUseDzz()){
//				String sendContext = "业务员" + namec + "(" + namee + ")" + "的工作单[url=" + url + "]" + orderno + "[/url]已生成,请跟进!";
//				AppUtils.sendMessage(uid , receiveCodes, remindTitle, remindContext, url, sendContext);
//			}
			String sendContext = "工作单[<a target=_blank href=" + url + ">" + orderno + "</a>]已生成，请跟进!"
				+(islanguge?("<br>Work sheet[<a target=_blank href=" + url + ">" + orderno + "</a>]Generated, please follow up.!"):"");
			String msg = "工作单跟进提醒：" + sendContext + ":" + msgUserInput;
			//System.out.println(msg);
			AppUtils.sendIMMsg(sendid.toString(), userid, msg);
		}
	}
	
	
	/**发送微信消息
	 * @param userid(要发送给此人的id)
	 * @param orderno(单号)
	 * @param username(发送人)
	 * @param msg(发送人要说的话)
	 */
	public void sendweixin(String userid,String orderno,String username,String msg){
		String iswexin = ConfigUtils.findUserCfgVal("subscribe_type_weixin", Long.parseLong(userid));
		String isjobstatus = ConfigUtils.findUserCfgVal("subscribe_msg_jobstatus", Long.parseLong(userid));
		if(iswexin!=null&&iswexin.equals("Y")&&isjobstatus.equals("Y")){//个人设置中如果勾选工作单状态通知和微信通知则发送消息到个人微信中
			String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
			String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
			
			SysUser user = sysUserDao.findById(Long.parseLong(userid));
			if(user==null||user.getOpneid()==null){
				return;
			}
			String openid = (user!=null?user.getOpneid():"");
//	    	String templateid = "Mbj3RAg_rg4o-QFj1xabWPJG_Q1jB-qQoUPn7BjsHMs";
			String weixinTemplatejobs = ConfigUtils.findSysCfgVal("weixin_template_task");
//	    	String json = JSONUtil.sendWeixin(openid, templateid, "亲，"+username+"发来的工作单跟进提醒：", "工作号："+orderno
//	    			, "单号已生成",  msg, user.getNamec());
	    	String json1 = JSONUtil.sendWeixinTemplate(null,openid, weixinTemplatejobs, "亲，"+username+"发来的工作单跟进提醒："
	    			, orderno, "单号已生成"
	    			, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
	    			, username, "", msg);
	    	try {
				String response = WeiXinUtil.messageSendByTemplate(appID, appsecret, json1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void mark(long id,Long userid,String user) {
		try {
			String sql2 = "SELECT COUNT(*) AS cnts FROM sys_user_assign x WHERE x.id = " + id + " AND userid > 0 ";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql2);
			String size = StrUtils.getMapVal(map, "cnts");
			if("0".equals(size)){
				String sql = "\nUPDATE sys_user_assign SET userid = "+userid+",updater = '"+user+"',updatetime = now() WHERE id = " + id + ";";
				sysUserAssignDao.executeSQL(sql);
				MessageUtils.alert("OK!");
			}else{
				MessageUtils.alert("请先清除，再标记!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void clear(long id,Long userid,String user) {
		SysUserAssign dataold = sysUserAssignDao.findById(id);
		String operatesql = "INSERT INTO sys_operatelog (id,operatetable,operatetype,logdesc,isdelete,inputer,inputtime)"
				+ "\nVALUES (getid(),'sys_user_assign','清除','清除前数据为" + dataold.toString() + "','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
		daoIbatisTemplate.updateWithUserDefineSql(operatesql);

		String sql = "\nUPDATE sys_user_assign SET userid = null,updater = '"+user+"',updatetime = now() WHERE id = " + id + ";";
		sysUserAssignDao.executeSQL(sql);
		MessageUtils.alert("OK!");
	}

	public void addsAgainsale(String roletype,Long saleid,String[] jobids){
		for(String jobid:jobids){
			BusShipping busShipping = busShippingDao.findOneRowByClauseWhere(" jobid="+Long.parseLong(jobid));
			SysUserAssign selectedRowData = new SysUserAssign();
			selectedRowData.setRolearea("SZ");
			selectedRowData.setLinkid(busShipping.getId());
			selectedRowData.setLinktype("J");
			selectedRowData.setRoletype(roletype);
			selectedRowData.setUserid(saleid);
			try {
				saveData(selectedRowData);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	public void coverAgainsale(String roletype,Long saleid,String[] jobids){
		String coverSql = "UPDATE sys_user_assign SET userid = "+saleid+" WHERE linkid = ANY(SELECT id FROM bus_shipping WHERE jobid = ANY(ARRAY["+StrUtils.array2List(jobids)+"])) AND roletype = '"+roletype+"'";
		try {
			daoIbatisTemplate.updateWithUserDefineSql(coverSql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

}