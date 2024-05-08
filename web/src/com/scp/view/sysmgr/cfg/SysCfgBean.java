package com.scp.view.sysmgr.cfg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.annotation.Resource;
import javax.faces.FacesException;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.ajax.ProgressAction;
import org.operamasks.faces.component.ajax.ProgressState;
import org.operamasks.faces.component.ajax.ProgressStatus;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.hx.hlp.rmi.HlpUpgService;
import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.cache.CommonDBCache;
import com.scp.dao.cache.EhCacheUtil;
import com.scp.util.AppUtilBase;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.Base64;
import com.scp.util.CfgUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.EMailSendUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.util.ConfigUtils.UsrCfgKey;
import com.scp.view.module.insurance.InsuranceUtils;

@ManagedBean(name = "pages.sysmgr.cfg.syscfgBean", scope = ManagedBeanScope.REQUEST)
public class SysCfgBean extends BaseCfgBean {
	
	@Autowired
	public ApplicationConf applicationConf;
	
	
	
	@BeforeRender
	protected void beforeRender(boolean isPostback) {
		if (!isPostback) {
//			String str = (String)AppUtil.getReqParam("type");
//			if (!str.equalsIgnoreCase("modify"))
//				Browser.execClientScript("saveSysCfgBut.setDisabled(true)");
			try {
				initData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Action
	private void saveDate() {
		save();
	}

	@Action(id = "saveSysCfgBut")
	private void save() {
		Set<String> set = this.getCfgDataMap().keySet();
		try {
			for (String key : set) {
				String val = this.getCfgDataMap().get(key);
				if(key.equals("email_pop3_pwd")){
					if(val.length()<200){ //如果是明文，加密，如果已经加密过，不能再加密，否则无法解密
						val = new EMailSendUtil().encrypt(val);
					}
				}
				ConfigUtils.refreshSysCfg(key, val, AppUtils.getUserSession().getUserid());
			}
			
			
			applicationConf.setSaas(false);
			if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("sys_cfg_saas"))){
				applicationConf.setSaas(true);
			}
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	private void lsCfgBut() {
		AppUtils.openWindow("_lsMgr", "lsmgr.xhtml");
	}
	
    @Action
    public void rptMgr(){
    	AppUtils.openWindow("_rpt_Mgr", "../rpt/rptMgr.xhtml");
//    	AppUtils.openWindow("_lsMgr", "lsmgr.xhtml");
    }


	@Override
	protected Vector<String> defineCfgKey() {
		Vector<String> vector = new Vector<String>();
		vector.add(ConfigUtils.SysCfgKey.rpt_srv_url.name());
		vector.add(ConfigUtils.SysCfgKey.login_leftimg.name());
		vector.add(ConfigUtils.SysCfgKey.login_logo.name());
		vector.add("shipquote_feeitemid");
		vector.add("fs_vch_checkact_55_dir");
		vector.add("cs_url_base");
		vector.add("cs_url_base_time");
		vector.add("Version");
		vector.add("Cli_Version");
		vector.add("DB_Version");
		vector.add("CSNO");
		vector.add("Licenses");
		vector.add("sys_header_title");
		vector.add("sys_customer_alert_auto");
		vector.add("sys_customer_alert_day");
		vector.add("sys_customer_transpublic_auto");
		vector.add("sys_customer_transpublic_day");
		vector.add("sys_saled_check_date");
		vector.add("ufms_url");
		vector.add("sys_ICP_NO");
		vector.add("sys_logo_height");
		vector.add("sys_cfg_saas");
		vector.add("dash_show_fee_over");
		vector.add("dash_show_profit_less");
		vector.add("work_sheet_tracking_reminding");
		vector.add("bu_shipping_hblrpt_check");
		vector.add("sys_cfg_branch_share_arapcom");
		vector.add("sys_cfg_mult_language");
		vector.add("sys_cfg_rpdate_jobdate");
		vector.add("sys_cfg_salesmgr_cus2assign");
		vector.add("sys_cfg_salesmgr_cus2saleslink");
		vector.add("WeiXin_AppID");
		vector.add("WeiXin_Appsecret");
		vector.add("WeiXin_CallBackUrl");
		vector.add("WeiXin_BindUrl");
		vector.add("sys_cfg_file_soa");
		vector.add("weixin_template_task");
		vector.add("weixin_template_jobs");
		vector.add("email_type");
		vector.add("email_srv_smtp");
		vector.add("email_srv_pop3");
		vector.add("email_srv_port");
		vector.add("email_pop3_account");
		vector.add("email_pop3_pwd");
		vector.add("email_receive_port");
		vector.add("email_sign");
		vector.add("sys_cfg_etd_update_jobdate");
		vector.add("sys_attachment_url");
		vector.add("sys_order_check");
		vector.add("sys_bpm");
		vector.add("sys_factoryneedcheck");
		vector.add("sys_billprintlock");
		vector.add("sys_public_url");
		vector.add("sys_loginsafe"); //安全登录客户端
		vector.add("sys_arapfromprice_lock");
		vector.add("sys_noslink"); //单号连号
		vector.add("jobs_sales_filterby_client");
		vector.add("bpm_task_filterbyorg");
		vector.add("sys_settlement_method");
		vector.add("fina_arap_aotosync_fromprice");
		vector.add("customer_management");
		vector.add("fina_arap_mainjobshidechildren");
		vector.add("sys_login_tips");
		vector.add("attachment_filterby_role");
		vector.add("SFT_filterby_client");
		vector.add("fina_rp_banknotfilterbycorp");
		vector.add("lock_jobs_afterbpm");
		vector.add("arap_filter_unchek_customer");
		vector.add("arap_filter_orderby_inputtime");
		vector.add("rp2vchtemp");
		vector.add("bill_port_connect_country");
		vector.add("rp2vch_fix_arap_corpid");
		vector.add("bill_cntseal_newline");
		vector.add("bill_markno_with_cntinfo");
		vector.add("contrainer_cntno_notrepeat");
		vector.add("job_refno_notrepeat");
		vector.add("rp2vch_no_rpdate_as_checkfee");
		vector.add("fina_arap_profit_disclosure");
		vector.add("sys_cfg_salesmgr_cus2link");
		vector.add("bill_account");
		vector.add("job_so_notrepeat");
		vector.add("sys_attachment_size");
		vector.add("fina_jobs_addchild_copycntinfo_fromparent");
		vector.add("fina_jobs_changecustomer_sync_assign");
		vector.add("sys_invsvr_url");
		vector.add("sys_invsvr_electron_url");
		vector.add("sys_invsvr_electron_find_url");
		vector.add("sys_print_html2pdf_URL");
		vector.add("sys_knowledge_auto_tips");
		vector.add("bus_air_automatic_calculation");
		vector.add("bus_air_automatic_calculation_val");
		vector.add("bus_shipping_mbl_shiper");
		vector.add("bus_shipping_mbl_con_copyfromhblagent");
		vector.add("bus_shipping_mbl_notify");
		vector.add("finajobs_iscomplete_isamend_lock");
		vector.add("fina_invoice_bank_filterbycorpid");
		vector.add("bus_ship_container_checkcntno");
		vector.add("sys_rpt_required_condition");
		vector.add("weixin_template_goodstrack");
		vector.add("bpm_taskto_showsubmit");
		vector.add("rp_newpage");
		vector.add("fina_arap_feehide");
		vector.add("customer_isolation");
		vector.add("hbl_agent_modify");
		vector.add("fina_actpayrec_bankslip_novch");
		vector.add("sys_cfg_etd_update_atd");
		vector.add("login_by_openid_only");
		vector.add("sys_print_html2pdf_args");
		vector.add("sys_cfg_jobedit_redirect_url");
		vector.add("fina_invoice_accountid_by_corpid");
		vector.add("fs_vch_isamend_allowededit");
		vector.add("fina_jobs_ai_operation");
		vector.add("fina_arap_porfit_isamend_jobdate");
		vector.add("sys_maersk_qryprice_url");
		vector.add("sys_maersk_qryprice_key");
		vector.add("sys_attachment_absurl");
		vector.add("pdd_pod_destination");
		vector.add("JobsMessageAutoOpen");
		vector.add("uplat_url");
		vector.add("job_mbl_notrepeat");
		vector.add("sys_insurance_apiurl");
		vector.add("sys_insurance_client_id");
		vector.add("sys_insurance_client_secret");
		vector.add("sys_insurance_userId");
		vector.add("sys_insurance_company");
		vector.add("sys_insurance_email");
		vector.add("sys_insurance_phone");
		vector.add("sys_taxtno");
		vector.add("sys_taxtno");
		vector.add("fina_arap_addbywindow");
		vector.add("hbl_att_rows");
		vector.add("hbl_att_rows_tips");
		vector.add("mbl_report");
		vector.add("fina_payapply_showar");
		vector.add("bill_single_new");
		vector.add("bus_shipping_label_other1");
		vector.add("bus_shipping_label_other2");
		vector.add("bus_shipping_label_other3");
		vector.add("bus_shipping_label_other4");
		vector.add("bus_shipping_label_other5");
		vector.add("bus_shipping_label_other6");
		vector.add("bus_shipping_label_other7");
		vector.add("bus_shipping_label_other8");
		vector.add("bus_shipping_label_other9");
		vector.add("bus_shipping_label_other10");
		vector.add("bus_shipping_label_other11");
		vector.add("bus_shipping_label_other12");
		vector.add("login_bg_img_open");
		vector.add("bg_color");
		vector.add("bgImg1");
		vector.add("bgImg2");
		vector.add("fina_bill_nos_by_rule");
		vector.add("open_account_login_auth");
		vector.add("sys_cosco_api");
		vector.add("sys_cosco_secret");
		vector.add("sys_api_ufms_url");
		vector.add("sys_api_shipquery_url");
		vector.add("sys_ftp_soedi_hostname");
		vector.add("sys_ftp_soedi_username");
		vector.add("sys_ftp_soedi_password");
		vector.add("sys_bms_url");//BMS URL
		vector.add("sys_bms_expense_url");//BMS URL
		vector.add("sys_bms_kpsq_url");//BMS URL
		vector.add("fs_vch_sap");
		vector.add("system_ctrl_only");
		vector.add("sys_edi_api_syncCustomer");
		vector.add("inv_ht_page");
		vector.add("inv_ht_e");
		vector.add("invoice_identity");
		vector.add("jobs_dept_notchangeby_sales");
		vector.add("jobs_show_bankbill_attachment");
		vector.add("sys_attachment_so_autogetdata");
		vector.add("sys_rpt_customer_filter_by_sales");
		vector.add("sys_forbidden_find_and_view_receipt");
		vector.add("fee_customer_filterby_araptype");
		vector.add("sys_cosco_server_url");
		
		return vector;
	}

	@Override
	protected String getQuerySql() {
		return 
		"\nSELECT * " +
		"\nFROM sys_config " +
		"\nWHERE 1=1 ";
	}
	
	
	@Bind
	public String getLogo(){
		String logoname = ConfigUtils.findSysCfgVal(ConfigUtils.SysCfgKey.login_logo.name());
		if(StrUtils.isNull(logoname))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(AppUtils.getAttachFilePath() + logoname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	
	@Bind
	public String getWeixinXCXImg(){
		String logoname = ConfigUtils.findSysCfgVal("weixinXCXImg");
		if(StrUtils.isNull(logoname))return "";
		String result = "";
		try {
			result = "data:image/png;base64,"+Base64.getImageStr(AppUtils.getAttachFilePath() + logoname);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	
	@Action
	private void refreshLogo() {
		this.update.markUpdate("logoImg");
	}
	
	@Action
	private void clearLogo() {
		try {
			ConfigUtils.refreshSysCfg(ConfigUtils.SysCfgKey.login_logo.name(), "" , AppUtils.getUserSession().getUserid());
			 String logopath = AppUtils.getReportFilePath() + "\\image\\logo.png";
             File logofile = new File(logopath);
             logofile.delete();
			 refreshLogo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	private void refreshWeixinImg() {
		this.update.markUpdate("weixinImg");
	}
	
	@Action
	private void clearWeixinImg() {
		try {
			String name = ConfigUtils.findSysCfgVal("weixinImg");
		    String logopath = AppUtils.getAttachFilePath() + name;
            File logofile = new File(logopath);
            logofile.delete();
            ConfigUtils.refreshSysCfg("weixinImg", "" , AppUtils.getUserSession().getUserid());
         refreshWeixinImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Action
	private void refreshWeixinXCXImg() {
		this.update.markUpdate("weixinXCXImg");
	}
	
	@Action
	private void clearWeixinXCXImg() {
		try {
			String name = ConfigUtils.findSysCfgVal("weixinXCXImg");
		    String logopath = AppUtils.getAttachFilePath() + name;
            File logofile = new File(logopath);
            logofile.delete();
            ConfigUtils.refreshSysCfg("weixinXCXImg", "" , AppUtils.getUserSession().getUserid());
            refreshWeixinXCXImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIFileUpload fileUpload1;
	
    public void processUpload1(FileUploadItem fileUploadItem) {
    	 InputStream input = null;
         FileOutputStream output = null;
         FileOutputStream outputreport = null;

         try {
        	 String fileName = "_logo_" + fileUploadItem.getName();
             input = fileUploadItem.openStream();
             String filepath = AppUtils.getAttachFilePath() + fileName;
             File f = new File(filepath);
             
             //AppUtils.debug(f.getAbsolutePath());
             output = new FileOutputStream(f);
             
            //将文件放到报表images下面logo.png
             String logopath = AppUtils.getReportFilePath() + File.separator + "image" + File.separator + "logo.png";
             //System.out.println(logopath);
             File logofile = new File(logopath);
             outputreport = new FileOutputStream(logofile);
             byte[] buf = new byte[4096];
             // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
             int length = UIFileUpload.END_UPLOADING;

             while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                 output.write(buf, 0, length); //写到附件中
                 outputreport.write(buf, 0, length); //写到报表目录images下
             }
             output.flush();
             outputreport.flush();
             
             long fileSize = new File(filepath).length();
             ConfigUtils.refreshSysCfg(ConfigUtils.SysCfgKey.login_logo.name(), fileName , AppUtils.getUserSession().getUserid());
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
             if (outputreport != null){
            	 try {
                	 outputreport.close();
                 } catch (IOException e) {
                	 e.printStackTrace();
                 }
             }
         }
    }
    
    public void weixinImgUpload1(FileUploadItem fileUploadItem) {
   	 InputStream input = null;
        FileOutputStream output = null;
        try {
       	 	String fileName = "_weixinImg_" + fileUploadItem.getName();
            input = fileUploadItem.openStream();
            String filepath = AppUtils.getAttachFilePath() + fileName;
            File f = new File(filepath);
            output = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;
            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length);
            }
            output.flush();
            long fileSize = new File(filepath).length();
            ConfigUtils.refreshSysCfg("weixinImg", fileName , AppUtils.getUserSession().getUserid());
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
    
    public void weixinXCXImgUpload1(FileUploadItem fileUploadItem) {
      InputStream input = null;
       FileOutputStream output = null;
	   try {
	  	 	String fileName = "_weixinXCXImg_" + fileUploadItem.getName();
		   input = fileUploadItem.openStream();
		   String filepath = AppUtils.getAttachFilePath() + fileName;
		   File f = new File(filepath);
		   output = new FileOutputStream(f);
		   byte[] buf = new byte[4096];
		   // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
		   int length = UIFileUpload.END_UPLOADING;
		   while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
		       output.write(buf, 0, length);
		   }
		   output.flush();
		   long fileSize = new File(filepath).length();
		   ConfigUtils.refreshSysCfg("weixinXCXImg", fileName , AppUtils.getUserSession().getUserid());
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
	public UIFileUpload fileUpload2;
    
    @Bind
    @SaveState
    public String errMsg;
    
    public void processUpload2(FileUploadItem fileUploadItem) {
    	InputStream input = null;
        FileOutputStream output = null;

        try {
       	 String fileName = fileUploadItem.getFieldName();
            input = fileUploadItem.openStream();
            String filepath = "./"+fileName;
            File f = new File(filepath);
            
            //AppUtils.debug(f.getAbsolutePath());
            output = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            // UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
            int length = UIFileUpload.END_UPLOADING;

            while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
                output.write(buf, 0, length);
            }
            long fileSize = new File(filepath).length();
            ConfigUtils.refreshSysCfg(ConfigUtils.SysCfgKey.login_logo.name(), filepath , AppUtils.getUserSession().getUserid());
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
               	 e.printStackTrace();
                }
        }
    }
    
    public void progressAction(ProgressStatus status) {
    	 // progress启动
        if (status.getAction().ordinal() == ProgressAction._START) {
            setStatusToStart(status);
            // progress处于轮询状态，每隔1s会到服务器端查询一次
        } else if (status.getAction().ordinal() == ProgressAction._POLL) {
            // uploading出现错误，设置progress的错误提示，并通知progress组件监控结束
            if (isErrorStatus()) {
                setStatusToError(status);
                return;
            }

            // 正在等待uploading开始，设置progress提示等待，并通知progress组件继续监控
            if (isWaittingStatus()) {
                setWaittingStatus(status);
                return;
            }
                
            // uploading已经结束，设置progress显示上传结束，并通知progress组件监控结束
            if (isCompletedStatus()) {
                setCompletedStatus(status);
                return;
            }

            // 正在上传，设置progress显示上传进度，并通知progress继续监控
            setRunningStatus(status);
            // progress已经停止，设置progress显示结束信息，并通知progress监控结束
        } else if (isStoppedStatus(status)) {
            setStoppedStatus(status);
        }
    }
	
 // 通知progress组件监控结束
    private void setStoppedStatus(ProgressStatus status) {
        status.setState(ProgressState.STOPPED);
    }

    // progress组件是否已经得到监控结束的通知
    private boolean isStoppedStatus(ProgressStatus status) {
        return status.getAction().ordinal() == ProgressAction._STOP;
    }

    // 上传是否出现例外，可以通过调用FileUploadItem.getUploadingStatus.getError()，获取错误信息
    private boolean isErrorStatus() {
        return fileUpload1.getUploadingStatus().getError() != null;
    }

    // 上传是否已经正常结束
    private boolean isCompletedStatus() {
        return fileUpload1.getUploadingStatus().getContentLength().equals(
                fileUpload1.getUploadingStatus().getBytesRead());
    }

    // 是否在等待启动上传任务
    private boolean isWaittingStatus() {
        return fileUpload1.getUploadingStatus().getContentLength() == null
                || fileUpload1.getUploadingStatus().getContentLength() == 0;
    }
    
    // 设置上传状态提示，并通知progress继续监控
    private void setRunningStatus(ProgressStatus status) {
        status.setPercentage(getPercentage());
        status.setMessage("Total " + getTotal() + "k, " + getKilosRead() + "k("
                + getPercentage() + "%) have read");
        setWaittingStatus(status);
    }

    // 设置上传结束提示，并通知progress监控结束
    private void setCompletedStatus(ProgressStatus status) {
        status.setMessage("Uploading has completed. Total " + getTotal() + "k");
        status.setPercentage(100);
        status.setState(ProgressState.COMPLETED);
    }

    // 通知progress继续监控
    private void setWaittingStatus(ProgressStatus status) {
        status.setState(ProgressState.RUNNING);
    }

    // 设置上传错误提示，并通知progress监控结束
    private void setStatusToError(ProgressStatus status) {
        status.setMessage("Uploading error: " + fileUpload1.getUploadingStatus(
                ).getError().getCause().getMessage());
        status.setState(ProgressState.COMPLETED);
    }

    // 设置准备上传提示，并通知progress继续监控
    private void setStatusToStart(ProgressStatus status) {
        status.setMessage("Ready to upload files...");
        status.setPercentage(0);
        status.setState(ProgressState.RUNNING);
    }
    
 // 获取文件数据已上传部分的尺寸（k）
    private long getKilosRead() {
        return bytesToKilo(fileUpload1.getUploadingStatus().getBytesRead());
    }

    // 获取上传文件的总尺寸（k）
    private long getTotal() {
        return bytesToKilo(fileUpload1.getUploadingStatus().getContentLength());
    }

    // 获取文件已经上传至服务器的比例
    private int getPercentage() {
        return (int)(100 * fileUpload1.getUploadingStatus().getBytesRead() /
                fileUpload1.getUploadingStatus().getContentLength());
    }
    
    // 转换数据尺寸为k
    public long bytesToKilo(long bytes) {
        return bytes / 1024 + ((bytes % 1024 > 0) ? 1 : 0);
    }
    
    @Autowired(required=false)
    private StringRedisTemplate stringRedisTemplate;
    
    @Resource
	public CommonDBCache commonDBCache;
    

	@Bind
	@SaveState
	public String cacheLogs;
    
    //清除缓存
    @Action
    public void clearCache(){
    	try {
    		//清空redis
			if(stringRedisTemplate != null){
				stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
			}
			//commonDBCache.clearCacheComboxItems();
			
			EhCacheUtil.clear();
			
			Browser.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    @Action
    public void showCache(){
    	try {
			cacheLogs = EhCacheUtil.showCache().toString();
			this.update.markUpdate(UpdateLevel.Data, "cacheLogs");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Inject
	protected PartialUpdateManager update;
    
    @Action
    public void licensesSave(){
    	save();
    	update.markUpdate(UpdateLevel.Data,"Licenses");
    }
    
    @Action
    public void refreshLicenses(){
    	update.markUpdate(UpdateLevel.Data,"Licenses");
        try {
        	String content = "refresh Licenses CSNO:" + CfgUtil.findSysCfgVal("CSNO");
			String subject = "refresh Licenses";
			String acceptAddress = "neo@ufms.cn";
			String ccAddress = "";
			EMailSendUtil.sendEmailBySystem(content, subject, acceptAddress,ccAddress);
        	MessageUtils.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    

    
    @Bind
    public String getLicenses(){
    	return AppUtils.getLicences();
    }
    
    @Bind
    @SaveState
    public String upglogs;
    
    @Action
    public void upg(){
    	try {
			HlpUpgService hlpTransMonitorService = (HlpUpgService)AppUtilBase.getBeanFromSpringIoc("hlpUpgService");
			String csno = CfgUtil.findSysCfgVal("CSNO");
			String fixVerNoStr = CfgUtil.findSysCfgVal("DB_Version");
			Integer fixVerNo = Integer.parseInt(fixVerNoStr);
			
			
			try {
				String sql = hlpTransMonitorService.checkUserAndModule(csno);
				DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
				daoIbatisTemplate.updateWithUserDefineSql(sql);
				//System.out.println("~~~~~~~~~~~~~sql:" + sql);
				//System.out.println("~~~~~~~~~~~~~checkUserAndModule OK;");
				upglogs = csno + "~~~~~~~~~checkUserAndModule OK!" + "\n";
				update.markUpdate(UpdateLevel.Data,"upglogs");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			boolean check = hlpTransMonitorService.checkPause(csno, fixVerNo);
			if(check)return;
			List<Map<String, String>> lists = hlpTransMonitorService.getUpgSql(csno, fixVerNo);
			
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			
			if(lists != null){
				if(lists.size() == 0){
					upglogs += csno + ":无升级语句!";
					update.markUpdate(UpdateLevel.Data,"upglogs");
				}else{
					StringBuilder stringBuilder = new StringBuilder();
					for (Map<String, String> map : lists) {
						String sql = StrUtils.getMapVal(map, "upgsql");
						String pkId = StrUtils.getMapVal(map, "pkid");
						String indexno = StrUtils.getMapVal(map, "indexno");
						
						String response = "OK";
						//System.out.println(map);
						try {
							
							daoIbatisTemplate.updateWithUserDefineSql(sql);
							stringBuilder.append(sql);
							upglogs += indexno + "~~~~~~~~~~~~~~~~~~~~~" + response + "\n";
//							//System.out.println(upglogs);
							update.markUpdate(UpdateLevel.Data,"upglogs");
						} catch (Exception e1) {
							//System.out.println(e1.getLocalizedMessage());
							response = e1.getLocalizedMessage();
							upglogs += indexno + "~~~~~~~~~~~~~~~~~~~~~" + response + "/n";
							update.markUpdate(UpdateLevel.Data,"upglogs");
						}
						String ret = hlpTransMonitorService.responseUpgSql(csno, pkId, response);
						//System.out.println(ret);
					}
				}
			}
			String pkId = "";
			try {
				String shells = hlpTransMonitorService.execShell(csno, fixVerNo);
				if(StrUtils.isNull(shells)){
					upglogs += "shell:~~~~~~~~~~~~~no shell need to exec:";
					//System.out.println(upglogs);
					//return;
				}else{
					String[] shellArray = shells.split(":");
					pkId = shellArray[0];
					String shellCmd = shellArray[1];
					//System.out.println(shellCmd);
					hlpTransMonitorService.responseExecShell(pkId, "OK,Start at:"+Calendar.getInstance().getTime().toGMTString());
					AppUtilBase.process(shellCmd);
					upglogs = "3.~~~~~~~~~~~~~shell:" +  shells;
					
					//更新当前客户端显示的版本号
					try {
						String sqlVersion= "UPDATE sys_config set val = to_char(NOW(),'YYYYMM') where key = 'Version';";
						daoIbatisTemplate.updateWithUserDefineSql(sqlVersion);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				hlpTransMonitorService.responseExecShell(pkId, StrUtils.getSqlFormat(e.getLocalizedMessage()));
				upglogs += "\n3.~~~~~~~~~~~~~shell:"+e.getLocalizedMessage();
				update.markUpdate(UpdateLevel.Data,"upglogs");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			upglogs += "\nERROR:"+e.getLocalizedMessage();
			update.markUpdate(UpdateLevel.Data,"upglogs");
		}
		
		try {
			AppUtils.checkSysModule();
			upglogs += "\n4.refesh~~~~~~~~~~~~~";
		} catch (Exception e) {
			upglogs += "\n4.refesh~~~~~~~~~~~~~:"+e.getLocalizedMessage();
			update.markUpdate(UpdateLevel.Data,"upglogs");
			e.printStackTrace();
		}
    }
    
    @Action
    public void saveDateshipping(){
    	save();
    }
    
    @Action
    public void saveDateair(){
    	save();
    }
    
    @Action
    public void save1(){
    	save();
    }
    
    @Action
    public void save2(){
    	save();
    }
    
    @Action
    public void save3(){
    	save();
    }
    
    @Action
    public void save4(){
    	save();
    }
    
    @Action
    public void saveInsurance(){
    	save();
    }
    
    @Action
    public void saveUI(){
    	save();
    }
    
    @Action
    public void saveApi(){
    	save();
    }
    
    
    @Action
    public void getInsuranceLogin(){
    	try {
			String userId = InsuranceUtils.getUserId(ConfigUtils.findSysCfgVal("sys_taxtno"),ConfigUtils.findSysCfgVal("sys_insurance_company"),ConfigUtils.findSysCfgVal("sys_insurance_email"),ConfigUtils.findSysCfgVal("sys_insurance_phone"));
			ConfigUtils.refreshSysCfg("sys_insurance_userId", userId , AppUtils.getUserSession().getUserid());
			Browser.execClientScript("insuranceUserIdJsVar.setValue('"+userId+"')");
			MessageUtils.alert("获取授权成功!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
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
			ConfigUtils.refreshSysCfg("email_sign", content , AppUtils.getUserSession().getUserid());
			showEmailSignWindow.close();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    @Action
	public void emailTest() {
    	Set<String> set = this.getCfgDataMap().keySet();
		try {
			if(StrUtils.isNull(ConfigUtils.findSysCfgVal(UsrCfgKey.email_srv_smtp.name())) 
					|| StrUtils.isNull(ConfigUtils.findSysCfgVal(UsrCfgKey.email_srv_port.name()))
					|| StrUtils.isNull(ConfigUtils.findSysCfgVal(UsrCfgKey.email_pop3_account.name()))
					|| StrUtils.isNull(ConfigUtils.findSysCfgVal(UsrCfgKey.email_pop3_pwd.name()))
			  ){
				MessageUtils.alert("Email config error , please confirm again!");
				return;
			}
			EMailSendUtil.sendEmailByAdmin("This Mail is testing from UFMS System!","UFMS Mail Send testing", ConfigUtils.findSysCfgVal(UsrCfgKey.email_pop3_account.name()), null);
			MessageUtils.alert("OK!");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
    
}
