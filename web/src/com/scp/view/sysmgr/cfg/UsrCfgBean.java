package com.scp.view.sysmgr.cfg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.faces.FacesException;
import javax.mail.AuthenticationFailedException;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysUser;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.util.ConfigUtils.UsrCfgKey;
import com.ufms.base.wx.WeiXinUtil;

@ManagedBean(name = "pages.sysmgr.cfg.usrcfgBean", scope = ManagedBeanScope.REQUEST)
public class UsrCfgBean extends BaseCfgBean {
	
	@Resource
	public ServiceContext serviceContext;
	
	@BeforeRender
	protected void beforeRender(boolean isPostback) {
		if (!isPostback) {
//			String str = (String)AppUtil.getReqParam("type");
//			if (!str.equalsIgnoreCase("modify"))
			Browser.execClientScript("userid.setValue("+AppUtils.getUserSession().getUserid()+")");
			try {
				getuser();
				initData();
				timeraction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getuser(){
		serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		String findUserCfgVal = ConfigUtils.findUserCfgVal("process_handling_standbyid", AppUtils.getUserSession().getUserid());
		if(findUserCfgVal!=null && !findUserCfgVal.equals("") && !findUserCfgVal.equals("null")){
			findUserCfgVal = findUserCfgVal.substring(0, findUserCfgVal.length()-1);
			String sql = "SELECT string_agg(namec,',') AS namec from sys_user where isdelete = false AND id in("+findUserCfgVal+")";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
			this.user = StrUtils.getMapVal(map, "namec");
		}
	}

	@Action
	public void save() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				String val = this.getCfgDataMap().get(key);
				if(key.equals(ConfigUtils.UsrCfgKey.email_pop3_pwd.name())){
					if(val.length()<200){ //如果是明文，加密，如果已经加密过，不能再加密，否则无法解密
						val = new EMailSendUtil().encrypt(val);
					}
				}
				ConfigUtils.refreshUserCfg(key, val , AppUtils.getUserSession().getUserid());
			}
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void saveUserCfgBut() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				String val = this.getCfgDataMap().get(key);
				//1557 检查勾选微信，短信，邮件的时候，保存的时候检查  微信要先绑定，短信要有录正确的手机号，邮箱要录有录正确的邮箱地址
				SysUser user = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
				String opneids = (user!=null?user.getOpneid():"");
				if(key.equals("subscribe_type_weixin")&&val.equals("Y")&&StrUtils.isNull(opneids)){
					MessageUtils.alert("选择微信方式必须先绑定微信");
					return;
				}
				if(key.equals("subscribe_type_sms")&&val.equals("Y")&&(StrUtils.isNull(user.getMobilephone())||!isMobileNO(user.getMobilephone()))){
					MessageUtils.alert("选择短信方式必须填写正确的手机号码");
					return;
				}
				boolean checkEmail = checkEmail(user.getEmail1());
				boolean checkEmail2 = checkEmail(user.getEmail2());
				if(key.equals("subscribe_type_email")&&val.equals("Y")&&(StrUtils.isNull(user.getEmail1())||(!checkEmail&&!checkEmail2))){
					MessageUtils.alert("选择邮件方式必须填写正确的邮箱");
					return;
				}
//				if(key.equals(ConfigUtils.UsrCfgKey.email_pop3_pwd.name())){
//					val = new EMailSendUtil().encrypt(val);
//				}
				ConfigUtils.refreshUserCfg(key, val , AppUtils.getUserSession().getUserid());
			}
			getuser();
			update.markUpdate(true, UpdateLevel.Data, "usercfgpanel");
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void saveUserCfgBut2() {
		saveUserCfgBut();
	}

	@Bind
	private UIIFrame signIframe;

    @Bind
    public UIWindow showSignWindow;
	
	@Action
	public void cfgSignature() {
		String url = "./signature.jsp?userid="+AppUtils.getUserSession().getUserid();
		signIframe.load(url);
		showSignWindow.show();
	}
	@Action
	public void emailTest() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			if(StrUtils.isNull(ConfigUtils.findUserCfgVal(UsrCfgKey.email_srv_smtp.name(), AppUtils.getUserSession().getUserid())) 
					|| StrUtils.isNull(ConfigUtils.findUserCfgVal(UsrCfgKey.email_srv_port.name(), AppUtils.getUserSession().getUserid()))
					|| StrUtils.isNull(ConfigUtils.findUserCfgVal(UsrCfgKey.email_pop3_account.name(), AppUtils.getUserSession().getUserid()))
					|| StrUtils.isNull(ConfigUtils.findUserCfgVal(UsrCfgKey.email_pop3_pwd.name(), AppUtils.getUserSession().getUserid()))
			  ){
				MessageUtils.alert("Email config error , please confirm again!邮箱配置错误，请检查!");
				return;
			}
			EMailSendUtil.sendEmailByUser("This Mail is testing from UFMS System!","UFMS Mail Send testing", ConfigUtils.findUserCfgVal(UsrCfgKey.email_pop3_account.name(), AppUtils.getUserSession().getUserid()), "" , AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
		} catch (AuthenticationFailedException e) {
			MessageUtils.alert("Wrong password");
			e.printStackTrace();
		} catch (Exception e) {
			MessageUtils.alert("Email config error , please check the confirm again!邮箱配置错误，发送失败，请检查!");
			//MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	

	@Override
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add(ConfigUtils.UsrCfgKey.view_tip_color.name());
		
		vector.add(ConfigUtils.UsrCfgKey.email_srv_smtp.name());
		vector.add(ConfigUtils.UsrCfgKey.email_srv_pop3.name());
		vector.add(ConfigUtils.UsrCfgKey.email_srv_port.name());
		vector.add(ConfigUtils.UsrCfgKey.email_pop3_account.name());
		vector.add(ConfigUtils.UsrCfgKey.email_pop3_pwd.name());
		vector.add("fee_profits_cy2");  
		vector.add("fee_add_cyid");
		vector.add("bill_ship_abbrs");
		vector.add("bill_export_lock");
		vector.add("timestamp_with_time_zone");
		vector.add("process_handling_standbyid");
		vector.add("rpt_srv_url");
		vector.add("bill_ship_suffix");
		vector.add("busshipping_signplace");
		vector.add("busshipping_carryitem");
		vector.add("busshipping_freightitem");
		vector.add("busshipping_paymentitem");
		vector.add("email_sign");
		vector.add("subscribe_msg_todojobs");
		vector.add("subscribe_msg_jobstatus");
		vector.add("subscribe_msg_email");
		vector.add("subscribe_msg_portnotice_STSP");
		vector.add("subscribe_msg_portnotice_TMPS");
		vector.add("subscribe_msg_portnotice_DSCH");
		vector.add("subscribe_msg_portnotice_GITM");
		vector.add("subscribe_msg_portnotice_PRLD");
		vector.add("subscribe_msg_portnotice_PCAB");
		vector.add("subscribe_msg_portnotice_PASS");
		vector.add("subscribe_msg_portnotice_LOBD");
		vector.add("subscribe_msg_portnotice_RCVE");
		vector.add("subscribe_type_im");
		vector.add("subscribe_type_weixin");
		vector.add("subscribe_type_sms");
		vector.add("subscribe_type_email");
		vector.add("fee_add_payplace");
		vector.add("subscribe_interval_time");
		vector.add("isAddOnUp");
		vector.add("bg_color");
		vector.add("oocl_apikey");
		vector.add("oocl_come_name");
		vector.add("oocl_come_addr1");
		vector.add("oocl_come_addr2");

		return vector;
	}


	@Override
	protected String getQuerySql() {
		return 
		"\nSELECT * " +
		"\nFROM sys_configuser " +
		"\nWHERE 1=1 "+
		"\nAND userid = " + AppUtils.getUserSession().getUserid();
	}
	
	public void processUpload1(FileUploadItem fileUploadItem) {
   	 InputStream input = null;
        FileOutputStream output = null;

        try {
       	 String fileName = AppUtils.getUserSession().getUserid()+"_head_" + fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getAttachFilePath() + fileName;
            File f = new File(filepath);
            
            //System.out.println(logopath);
            
            //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;

            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length);
            }
            long fileSize = new File(filepath).length();
            ConfigUtils.refreshUserCfg("head", fileName , AppUtils.getUserSession().getUserid());
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
        	if (input != null){
                try {
                	input.close();
                } catch (IOException e) {
               		e.printStackTrace();
                }
            }
            if (output != null){
                try {
                    output.close();
                } catch (IOException e) {
               		e.printStackTrace();
                }
            }
        }
   }
	
	@Bind
	public String getHead(){
		String logoname = ConfigUtils.findUserCfgVal("head",AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(logoname))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(AppUtils.getAttachFilePath() + logoname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Inject
	protected PartialUpdateManager update;
	
	@Action
	private void refreshLogo() {
		this.update.markUpdate("headImg");
	}
	
	@Action
	private void clearLogo() {
		try {
			String findUserCfgVal = ConfigUtils.findUserCfgVal("head",AppUtils.getUserSession().getUserid());
            String filepath = AppUtils.getAttachFilePath() + findUserCfgVal;
	        File logofile = new File(filepath);
	        logofile.delete();
	        ConfigUtils.refreshUserCfg("head","",AppUtils.getUserSession().getUserid());
			refreshLogo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
    public UIWindow showEmailSignWindow;
	
	@Action
	public void emailSign() {
		showEmailSignWindow.show();
	}
	
	@Action
	protected void saveAction() {
		String content = AppUtils.getReqParam("editor1");
		try {
			ConfigUtils.refreshUserCfg("email_sign", content , AppUtils.getUserSession().getUserid());
			showEmailSignWindow.close();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Bind
	private boolean ispenid;
	
	@Bind
	private String openid;
	
	@Bind
	private String optext;
	
	
	/*
	 * 二维码Url
	 */
	@Bind
	private String codeUrl;
	
	@Action
	public void timeraction(){
		if(ispenid)return;
		
		String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
		String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
		String bindUrl = ConfigUtils.findSysCfgVal("WeiXin_BindUrl");//http://www.ufms.cn/
		String backurl = ConfigUtils.findSysCfgVal("WeiXin_CallBackUrl");//http://113.116.6.90:8081/scp/
		
		backurl += AppUtils.getUserSession().getUserid() +"/so_user/";
		
		//scope分为两种：一种是静默方式（snsapi_base）；一种是非静默方式（snsapi_userinfo），需要用户去手动点击同意才能获取用户的信息。
		codeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appID +
				"&redirect_uri="+bindUrl+"servlet/OAuthAPIServlet?backurl="+backurl+"" + 
				"&response_type=code" +
				"&scope=snsapi_userinfo" +
				"&state=STATE#wechat_redirect";
		
		SysUser user = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
		String opneids = (user!=null?user.getOpneid():"");
		if(!StrUtils.isNull(opneids)){//如果openid已经赋值就显示已绑定，显示openid
			ispenid = true;
			openid = opneids;
			optext = "已绑定";
		}else{
			ispenid = false;
			optext = "未绑定";
		}
		update.markUpdate(true,UpdateLevel.Data, "ispenid");
		update.markUpdate(true,UpdateLevel.Data, "openid");
		update.markUpdate(true,UpdateLevel.Data, "optext");
		update.markUpdate(true,UpdateLevel.Data, "codeUrl");
	}
	
	
	@Action
	public void delopenid(){
		ispenid = false;
		SysUser user = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
		user.setOpneid("");
		serviceContext.userMgrService.saveUser(user);
		timeraction();
		Browser.execClientScript("refreshQrCode");
	}
	
	@Action
	public void testSendWeiXin(){
		String appID = ConfigUtils.findSysCfgVal("WeiXin_AppID");//"wxe1517b66902b4b8b"
		String appsecret =  ConfigUtils.findSysCfgVal("WeiXin_Appsecret");//"309b0dbb4acba9f09b31143078da10c7";
		
		SysUser user = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
		String openid = (user!=null?user.getOpneid():"");
		String content = "欢迎使用UFMS，您已成功绑定微信公众号，可以通过系统接收到提醒信息！！！！";
		try {
			String response = WeiXinUtil.messageSend(appID , appsecret ,openid , content);
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public String getWeixinimg(){
		String logoname = ConfigUtils.findSysCfgVal("weixinImg");
		if(StrUtils.isNull(logoname))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(AppUtils.getAttachFilePath() + logoname);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	//验证手机号
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			//13********* ,15********,18*********
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证邮箱
	 *
	 * @param email
	 * @return
	 */

	public boolean checkEmail(String email) {
	    boolean flag = false;
	    try {
	        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	        Pattern regex = Pattern.compile(check);
	        Matcher matcher = regex.matcher(email);
	        flag = matcher.matches();
	    } catch (Exception e) {
	        flag = false;
	    }
	    return flag;
	}
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(qryuserdesc) ){
			qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
			qryuserdesc = qryuserdesc.toUpperCase();
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;
	
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";

				if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='"
								+ dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index)
									+ "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							buffer.append("\nAND UPPER(" + key
									+ ") LIKE UPPER('%'||" +"TRIM('"+ val+"')" + "||'%')");
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		map.put("limit", limits+"");
		map.put("start", starts+"");
		map.put("qry", qry);
		return map;
	}
	
	
	@Action
	public void clearQryKeysc() {
		if (qryMapUser != null) {
			qryMapUser.clear();
			update.markUpdate(true, UpdateLevel.Data, "userPanel");
			this.gridUser.reload();
		}
	}

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
	@SaveState
	public String user = "";
	
	@Bind
	@SaveState
	public String salesid = "";
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		for (String id : ids) {
			if(!this.salesid.contains(id)){
				this.salesid = salesid + id +",";
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
				this.user = user + us.getNamec() + ",";
			}
		}
		Browser.execClientScript("setuserid();");
		update.markUpdate(true, UpdateLevel.Data, "usercfgpanel");
		//Browser.execClientScript("userWindowJs.hide();");
	}
	
	@Action
	public void empty() {
		this.salesid = "";
		this.user = "";
		Browser.execClientScript("setuserid();");
		update.markUpdate(true, UpdateLevel.Data, "usercfgpanel");
	}

	@Action
	public void saveApi() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				String val = this.getCfgDataMap().get(key);
				ConfigUtils.refreshUserCfg(key, val , AppUtils.getUserSession().getUserid());
			}
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Bind
	public UIWindow portnoticeWindow;
	@Action
	public void portnotice() {
		portnoticeWindow.show();
	}
}
