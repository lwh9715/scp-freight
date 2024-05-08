package com.scp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.operamasks.faces.developer.util.FacesUtils;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.LMap;
import com.scp.base.LMapBase;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.cache.CommonDBCache;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoSessionException;
import com.scp.service.sysmgr.SysMlService;

/**
 */
public class MessageUtils {

	public static void alert(String message) {
		try {
			if(LMapBase.MLType.en.equals(AppUtils.getUserSession().getMlType()) || AppUtils.isDebug){
				if(AppUtils.isContainChinese(message)){
					LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
					if(message.indexOf("[")>-1&&message.indexOf("]")>-1){//判断是否有动态内容
						String messageing = message.substring( message.indexOf("["), message.length());//动态部分
						String messagefix = message.substring(0, message.indexOf("["));//固定部分
						message = (l.get(messagefix) == null? messagefix+messageing : String.valueOf(l.get(messagefix)+messageing));
					}else{
						message = (l.get(message) == null? message : String.valueOf(l.get(message)));
					}
				}
				String regex = "[\u4e00-\u9fa5]";   //汉字的Unicode取值范围
			    Pattern pattern = Pattern.compile(regex);
			    Matcher match = pattern.matcher(message);
			    if(match.find()){

			    }
			}
			Browser.execClientScript("window.alert('" + message.replaceAll("\\n|\"|'|\\r", " ") + "');");
		} catch (NoSessionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param e
	 */
	public static void showMsg(String msg) {
		showMsg("Tips", msg);
	}

	/**
	 * @param e
	 */
	public static void showException(Exception e) {
		showException("Error", e);
		//reportException("Error", e);
	}

	/**
	 * 显示提示 2010-10-15 neo
	 *
	 * @param title
	 *            窗口的title
	 * @param e
	 */
	public static void showMsg(String title, String msg) {

		title = StrUtils.isNull(title) ? "" : title;
		msg = StrUtils.isNull(msg) ? "" : msg;

		msg = msg.replace("\n", " ");

//		Flash flash = FacesUtils.getFlash();
//		if (flash != null) {
//			flash.put("TitleMsg", title);
//			flash.put("Message", msg);
//			flash.put("MessageDetail", "");
//		}

		Browser.execClientScript("window.alert('" + msg + "');");
		/*String contexPath = AppUtils.getContextPath();
		String url = contexPath + "/messageTip.xhtml";
		String winId = "_showMessage";

		// AppUtil.openWindow(winId, url, 350, 250);
		Browser.execClientScript("window.open('"
						+ url
						+ "','"
						+ winId
						+ "','toolbar=no, menubar=no, scrollbars=yes, resizable=no,width=820,height=250,  location=center, status=no');");
*/
	}

	/**
	 * 使用系统帐户发送error到it.list邮箱
	 */
	public static void reportException(String title, Exception e) {
		String message = e.getLocalizedMessage();
		String messageDtl = ExceptionUtil.getFullExceptionMessage(e);

		if(!AppUtils.isDebug) {
			try {
				EMailSendUtil.sendEmailBySystem(messageDtl, message, "it@hangxunkeji.com.cn", null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}


	/**
	 */
	public static void showException(String title, Exception e) {

		e.printStackTrace();

		String message = e.getLocalizedMessage();
		String messageDtl = ExceptionUtil.getFullExceptionMessage(e);

		title = StrUtils.isNull(title) ? "" : title;
		message = StrUtils.isNull(message) ? "" : message;
		messageDtl = StrUtils.isNull(messageDtl) ? "" : messageDtl;

		int indexBegin = messageDtl.indexOf("<DB Exception>:");
		if (indexBegin > 0) {
			messageDtl = messageDtl.substring(indexBegin);
			indexBegin = 0;
			int indexEnd = messageDtl.indexOf("\n");
			String tip = messageDtl.substring(indexBegin
					+ "<DB Exception>:".length(), indexEnd);
			// showMsg("Tips:", tip);
			MLType mlType = AppUtils.getUserSession().getMlType();
			if(mlType.equals(LMapBase.MLType.en)){
				if(!AppUtils.isReadOnlyDB && AppUtils.isAutoFillLs) {
					SysMlService sysMlService = (SysMlService) AppUtils.getBeanFromSpringIoc("sysMlService");
					if(tip.indexOf("[")>-1&&tip.indexOf("]")>-1){
						String s = tip.substring(0, tip.indexOf("["));
						sysMlService.saveAutoFill(s);
					}
				}
				CommonDBCache l = (CommonDBCache) AppUtils.getBeanFromSpringIoc("commonDBCache");
				if(tip.indexOf("[")>-1&&tip.indexOf("]")>-1){
					alert((String) l.getLs(tip.substring(0, tip.indexOf("[")))+tip.substring( tip.indexOf("["), tip.length()));
				}else{
					alert((String) l.getLs(tip));
				}
			}else{
				alert(tip);
			}
			return;
		}

		if(e instanceof CommonRuntimeException){
			String tip = e.getLocalizedMessage();
			alert(tip);
			return;
		}


		String dbException = ExceptionUtil.getDBException(e);
		if(!StrUtils.isNull(dbException)) {
			alert(dbException);
			return;
		}

		FacesUtils.getFlash().put("TitleMsg", title);
		FacesUtils.getFlash().put("Message", message);
		FacesUtils.getFlash().put("MessageDetail", messageDtl);

		String contexPath = AppUtils.getContextPath();
		String url = contexPath + "/message.xhtml";
		String winId = "_showError";
		AppUtils.openWindow(winId, url, 400, 300);
		// Browser.execClientScript("window.open('" + url + "','" + winId +
		// "','toolbar=no, menubar=no, scrollbars=yes, resizable=no,width=400,height=300, location=center, status=no');");

		/**
		 * 使用系统帐户发送邮箱
		 * @param content
		 * @param subject
		 * @param acceptAddress
		 * @param ccAddress
		 */
		if(!AppUtils.isDebug) {
			try {
				//EMailSendUtil.sendEmailBySystem(messageDtl, message, "89115698@qq.com", null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public static String returnException(Exception e) {

		e.printStackTrace();

		String message = e.getLocalizedMessage();
		String messageDtl = ExceptionUtil.getFullExceptionMessage(e);

		message = StrUtils.isNull(message) ? "" : message;
		messageDtl = StrUtils.isNull(messageDtl) ? "" : messageDtl;

		int indexBegin = messageDtl.indexOf("<DB Exception>:");
		if (indexBegin > 0) {
			messageDtl = messageDtl.substring(indexBegin);
			indexBegin = 0;
			int indexEnd = messageDtl.indexOf("\n");
			String tip = messageDtl.substring(indexBegin
					+ "<DB Exception>:".length(), indexEnd);
			// showMsg("Tips:", tip);
			MLType mlType = AppUtils.getUserSession().getMlType();
			if(mlType.equals(LMapBase.MLType.en)){
				if(!AppUtils.isReadOnlyDB && AppUtils.isAutoFillLs) {
					SysMlService sysMlService = (SysMlService) AppUtils.getBeanFromSpringIoc("sysMlService");
					if(tip.indexOf("[")>-1&&tip.indexOf("]")>-1){
						String s = tip.substring(0, tip.indexOf("["));
						sysMlService.saveAutoFill(s);
					}
				}
				CommonDBCache l = (CommonDBCache) AppUtils.getBeanFromSpringIoc("commonDBCache");
				if(tip.indexOf("[")>-1&&tip.indexOf("]")>-1){
					return (String) l.getLs(tip.substring(0, tip.indexOf("[")))+tip.substring( tip.indexOf("["), tip.length());
				}else{
					return (String) l.getLs(tip);
				}
			}else{
				return tip;
			}
		}

		if(e instanceof CommonRuntimeException){
			String tip = e.getLocalizedMessage();
			return tip;
		}
		return "";
	}

	public static String returnExceptionForservlet(Exception e) {
		e.printStackTrace();
		String message = e.getLocalizedMessage();
		String messageDtl = ExceptionUtil.getFullExceptionMessage(e);

		message = StrUtils.isNull(message) ? "" : message;
		messageDtl = StrUtils.isNull(messageDtl) ? "" : messageDtl;

		int indexBegin = messageDtl.indexOf("<DB Exception>:");
		if (indexBegin > 0) {
			messageDtl = messageDtl.substring(indexBegin);
			indexBegin = 0;
			int indexEnd = messageDtl.indexOf("\n");
			String tip = messageDtl.substring(indexBegin
					+ "<DB Exception>:".length(), indexEnd);
			return tip;
		}
		if(e instanceof CommonRuntimeException){
			String tip = e.getLocalizedMessage();
			return tip;
		}
		return "";
	}



	public static  String getDBException(String title, Exception e) {
		e.printStackTrace();
		String message = e.getLocalizedMessage();
		String messageDtl = ExceptionUtil.getFullExceptionMessage(e);
		title = StrUtils.isNull(title) ? "" : title;
		message = StrUtils.isNull(message) ? "" : message;
		messageDtl = StrUtils.isNull(messageDtl) ? "" : messageDtl;
		int indexBegin = messageDtl.indexOf("<DB Exception>:");
		String tip = "";
		if (indexBegin > 0) {
			messageDtl = messageDtl.substring(indexBegin);
			indexBegin = 0;
			int indexEnd = messageDtl.indexOf("\n");
			tip = messageDtl.substring(indexBegin
					+ "<DB Exception>:".length(), indexEnd);
		}
		return tip;

	}

    /**
     *
     */
    public static String showCommerceException(Exception e) {
        e.printStackTrace();
        String messageDtl = ExceptionUtil.getFullExceptionMessage(e);
        String tip = null;

        messageDtl = StrUtils.isNull(messageDtl) ? "" : messageDtl;

        int indexBegin = messageDtl.indexOf("<DB Exception>:");
        if (indexBegin > 0) {
            messageDtl = messageDtl.substring(indexBegin);
            indexBegin = 0;
            int indexEnd = messageDtl.indexOf("\n");
             tip = messageDtl.substring(indexBegin
                    + "<DB Exception>:".length(), indexEnd);
        }
        return tip;
    }

}
