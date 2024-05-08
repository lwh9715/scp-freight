package com.scp.view.module.website;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.Vector;

import javax.faces.FacesException;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.PartialUpdateManager;

import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.sysmgr.cfg.BaseCfgBean;

@ManagedBean(name = "pages.module.website.webconfigBean", scope = ManagedBeanScope.REQUEST)
public class WebconfigBean extends BaseCfgBean{

	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;
	
	@BeforeRender
	protected void beforeRender(boolean isPostback) {
		if (!isPostback) {
			try {
				initData();
				defgridIframe.load("ufmsdefgrid.html?gridid=pages.module.order.fclBean");
				webbannerIframe.load("webbanner.html");
				webnewsIframe.load("webbannernews.html");
				String str = ConfigUtils.findSysCfgVal("sys_attachment_size");
				if(StrUtils.isNull(str)){
					attachmentsize = 5L;
				}else{
					float size = Float.parseFloat(str);
					attachmentsize = size;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Bind
	@SaveState
	private float attachmentsize;
	
	@Override
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add("webPageTail");
		vector.add("webDescription");
		vector.add("webAuthor");
		vector.add("webKeywords");
		vector.add("fsPolRecommend");
		vector.add("fsPodRecommend");
		vector.add("fscurrieRecommend");
		vector.add("webfinabilladdress");
		vector.add("ufmsdefgridwebsql");
		vector.add("attachfileGroup");
		vector.add("webTermsOfUse");
		vector.add("webbannerright");
		vector.add("csuserDefaultPassword");
		vector.add("bookingAgreement");
		return vector;
	}

	@Override
	protected String getQuerySql() {
		return 
		"\nSELECT * " +
		"\nFROM web_config " +
		"\nWHERE 1=1 ";
	}
	
	@Action(id = "saveMaster")
	private void save() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				String val = this.getCfgDataMap().get(key) ;
				serviceContext.webConfigService.refreshWebConfig(key, val);
			}
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action(id = "saveMasterBas")
	private void saveMasterBas() {
		save();
	}
	
	@Action(id = "saveMasterPrice")
	private void saveMasterPrice() {
		save();
	}
	
	@Action(id = "saveWebbannerRight")
	private void saveWebbannerRight() {
		save();
	}
	
	@Action(id = "saveMasterfooter")
	private void saveMasterfooter() {
		save();
	}
	
	@Action(id = "savebooking")
	private void savebooking() {
		save();
	}
	
	public void processUpload1(FileUploadItem fileUploadItem) {
		InputStream input = null;
        FileOutputStream output = null;

        try {
        	String fileName = "_logo_" + fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getAttachFilePath() + fileName;
            File f = new File(filepath);
            //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
           //将文件放到报表images下面logo.png
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length); //写到附件中
            }
            serviceContext.webConfigService.refreshWebConfig("webconfiglog", filepath);
            output.flush();
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
            refreshLogo();
        }
	}
	
	@Inject
	protected PartialUpdateManager update;
	
	@Action
	private void refreshLogo() {
		this.update.markUpdate("logoImg");
	}
	
	@Action
	private void clearLogo() {
		try {
			String logopath = serviceContext.webConfigService.findWebConfig("webconfiglog");
			if(StrUtils.isNull(logopath)){
	            File logofile = new File(logopath);
	            logofile.delete();
			}
			serviceContext.webConfigService.refreshWebConfig("webconfiglog", "");
			refreshLogo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public String getLogo(){
		String logopath = serviceContext.webConfigService.findWebConfig("webconfiglog");
		if(StrUtils.isNull(logopath))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(logopath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Bind
	public String getLoginLogo(){
		String logopath = serviceContext.webConfigService.findWebConfig("webconfigLofinlog");
		if(StrUtils.isNull(logopath))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(logopath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void processLoginUpload1(FileUploadItem fileUploadItem) {
		InputStream input = null;
        FileOutputStream output = null;

        try {
        	String fileName = "_loginlogo_" + fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getAttachFilePath() + fileName;
            File f = new File(filepath);
            //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
           //将文件放到报表images下面logo.png
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length); //写到附件中
            }
            serviceContext.webConfigService.refreshWebConfig("webconfigLofinlog", filepath);
            output.flush();
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
            refreshLogo();
        }
	}
	@Action
	private void refreshLoginLogo() {
		this.update.markUpdate("loginlogoImg");
	}
	
	@Action
	private void clearLoginLogo() {
		try {
			String logopath = serviceContext.webConfigService.findWebConfig("webconfigLofinlog");
			if(StrUtils.isNull(logopath)){
	            File logofile = new File(logopath);
	            logofile.delete();
			}
			serviceContext.webConfigService.refreshWebConfig("webconfigLofinlog", "");
			refreshLoginLogo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public String getRegisterLogo(){
		String logopath = serviceContext.webConfigService.findWebConfig("webconfigRegisterlog");
		if(StrUtils.isNull(logopath))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(logopath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void processRegisterUpload1(FileUploadItem fileUploadItem) {
		InputStream input = null;
        FileOutputStream output = null;

        try {
        	String fileName = "_registerlogo_" + fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getAttachFilePath() + fileName;
            File f = new File(filepath);
            //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
           //将文件放到报表images下面logo.png
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length); //写到附件中
            }
            serviceContext.webConfigService.refreshWebConfig("webconfigRegisterlog", filepath);
            output.flush();
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
            refreshLogo();
        }
	}
	@Action
	private void refreshRegisterLogo() {
		this.update.markUpdate("registerlogoImg");
	}
	
	@Action
	private void clearRegisterLogo() {
		try {
			String logopath = serviceContext.webConfigService.findWebConfig("webconfigRegisterlog");
			if(StrUtils.isNull(logopath)){
	            File logofile = new File(logopath);
	            logofile.delete();
			}
			serviceContext.webConfigService.refreshWebConfig("webconfigRegisterlog", "");
			refreshRegisterLogo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIIFrame defgridIframe;
	
	@Bind
	public UIIFrame webbannerIframe;
	
	@Action
	public void refreshwebbannerIframe(){
		webbannerIframe.load("webbanner.html");
		this.update.markUpdate("webbannerIframe");
	}
	
	@Bind
	public UIIFrame webnewsIframe;
	
	@Action
	public void refreshwebnewsIframe(){
		webnewsIframe.load("webbannernews.html");
		this.update.markUpdate("webnewsIframe");
	}
}
