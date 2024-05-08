package com.scp.model.edi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.model.SelectItem;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.scp.exception.NoRowException;
import com.scp.model.data.Ediesi;
import com.scp.model.sys.SysCorpcontacts;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;
import com.scp.view.module.customer.CustomerConChooseBean;
import com.ufms.base.db.SqlObject;
import com.ufms.base.db.SqlUtil;

@ManagedBean(name = "pages.module.edi.ediesieditBean", scope = ManagedBeanScope.REQUEST)
public class EdiEsiEditBean extends EditGridFormView {

	@Accessible
    @SaveState
    public Ediesi selectedRowData = new Ediesi();

    @Bind
    @SaveState
    public String esitype;
    
    @Bind
    @SaveState
    public String title = "E-B/L";
    
    @SaveState
	private Map<String, String> cfgDataMap;

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
        	initFtpCfg();
        	String id = "";
        	String type = AppUtils.getReqParam("type");
        	this.corporation = serviceContext.sysCorporationService.sysCorporationDao.findById(this.customerid);
        	if("mblqry".equalsIgnoreCase(type)){
        		String mblno = AppUtils.getReqParam("mblno");
        		if(mblno.length()>10){
        			mblno = mblno.substring(4);
        		}
        		esitype = "IFTMCS";
        		try {
					String sql = "SELECT x.id FROM edi_esi x WHERE x.mblno = '"+mblno+"' AND 1 = f_checkright('ediesi', "+AppUtils.getUserSession().getUserid()+") AND esitype = 'IFTMCS' LIMIT 1";
					Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					id = StrUtils.getMapVal(map, "id");
        		} catch (NoRowException e){
				} catch (Exception e) {
					e.printStackTrace();
				}
				update.markUpdate(true, UpdateLevel.Data, "esitype");
        	}else{
        		id = AppUtils.getReqParam("id");
        	}
        	
            if (!StrUtils.isNull(id)) {
                pkVal = Long.parseLong(id);
                this.selectedRowData = serviceContext.ediesiService.ediesiDao.findById(pkVal);
                esitype = this.selectedRowData.getEsitype();
                update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
            } else {
                this.add();
            }
        }
    }
    
    @Bind
    @SaveState
    public String ftpCfgAcceptcode;
    @Bind
    @SaveState
    public String ftp_host;
    @Bind
    @SaveState
    public String ftp_port;
    @Bind
    @SaveState
    public String ftp_uploadpath;
    @Bind
    @SaveState
    public String ftp_username;
    @Bind
    @SaveState
    public String ftp_password;

    @Action
    public void initFtpCfg() {
    	cfgDataMap = new HashMap<String, String>();
    	
    	String cfgKes[] = {
    			"edi_esi_"+ftpCfgAcceptcode+"_ftp_host",
    			"edi_esi_"+ftpCfgAcceptcode+"_ftp_port",
    			"edi_esi_"+ftpCfgAcceptcode+"_ftp_username",
    			"edi_esi_"+ftpCfgAcceptcode+"_ftp_password",
    			"edi_esi_"+ftpCfgAcceptcode+"_ftp_uploadpath"
    			};
    	this.cfgDataMap = ConfigUtils.findSysCfgVals(cfgKes);
    	ftp_host = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_host");
    	ftp_port = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_port");
    	ftp_uploadpath = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_uploadpath");
    	ftp_username = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_username");
    	ftp_password = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_password");
    	
    	update.markUpdate(true, UpdateLevel.Data, "ftpCfgPanel");
	}
    
    @Bind
    @SaveState
    public String ftp_debuginfo;
    
    @Action
    public void testFtpCfg() {
    	FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
		try {
			ftp_debuginfo = "";
			ftpClient.connect(ftp_host);
			boolean login = ftpClient.login(ftp_username, ftp_password); 
			if(login){
				this.alert("FTP配置配置正确！");

				try {
					ftpClient.setControlEncoding("UTF-8"); // 中文支持
					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
					ftpClient.enterLocalPassiveMode();
					
					//设置上传目录 
					ftpClient.changeWorkingDirectory("/"+ftp_uploadpath);
					FTPFile[] files = ftpClient.listFiles();
					for (FTPFile ftpFile : files) {
						ftp_debuginfo += ftpFile.getName() + "\n";
						System.out.println(ftpFile.getName());
					}
				} catch (Exception e) {
					ftp_debuginfo = e.getLocalizedMessage();
					e.printStackTrace();
				}
			}else{
				this.alert("账号或密码错误1！");
			}
		} catch (SocketException e) {
			this.alert("FTP配置配置错误2！");
			e.printStackTrace();
		} catch (IOException e) {
			this.alert("FTP配置配置错误3！");
			e.printStackTrace();
		} finally{
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    @Action
    public void saveFTPCfg() {
    	cfgDataMap.put("edi_esi_"+ftpCfgAcceptcode+"_ftp_host" , ftp_host);
    	cfgDataMap.put("edi_esi_"+ftpCfgAcceptcode+"_ftp_port" , ftp_port);
    	cfgDataMap.put("edi_esi_"+ftpCfgAcceptcode+"_ftp_uploadpath" , ftp_uploadpath);
    	cfgDataMap.put("edi_esi_"+ftpCfgAcceptcode+"_ftp_username" , ftp_username);
    	cfgDataMap.put("edi_esi_"+ftpCfgAcceptcode+"_ftp_password" , ftp_password);
    	
    	try {
			ConfigUtils.refreshSysCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
			this.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String filter = "\n AND ediesiid = " + pkVal + "";
        m.put("filter", filter);
        return m;
    }
    


	@Bind
    public UIDataGrid feeGrid;

    @Bind(id = "feeGrid", attribute = "dataProvider")
    protected GridDataProvider getFeeGridDataProvider() {
        return new GridDataProvider() {

            @Override
            public Object[] getElements() {
                if (gridLazyLoad) {
                    return new Object[]{};
                } else {
                    starts = start;
                    limits = limit;
                    String sqlId = getMBeanName() + ".feeGrid.page";
                    return daoIbatisTemplate.getSqlMapClientTemplate()
                            .queryForList(sqlId, getFeeGridQryClauseWhere(qryMap)).toArray();
                }
            }

            @Override
            public int getTotalCount() {
                if (gridLazyLoad) {
                    return 0;
                } else {
                    String sqlId = getMBeanName() + ".feeGrid.count";
                    List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
                            .queryForList(sqlId, getFeeGridQryClauseWhere(qryMap));
                    if (list == null || list.size() < 1)
                        return 0;
                    Long count = Long.parseLong(list.get(0).get("counts")
                            .toString());
                    return count.intValue();
                }
            }
        };
    }


    public Map getFeeGridQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String filter = "\n AND ediesiid = " + pkVal + "";
        m.put("filter", filter);
        return m;
    }


    @Override
    public void refresh() {
        doServiceFindData();
        super.refreshForm();
        update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
        this.editGrid.reload();
        Browser.execClientScript("initFlex()");
    }
    
    @Action
    public void submit() {
    	
    	showEdi();
    	
    	if(ediRptStr.startsWith("ERROR")){
    		this.alert("ERROR：报文数据有误，请检查后再上传!");
    		return;
    	}
    	
    	saveFileLocal();
    	
		ftpCfgAcceptcode = this.selectedRowData.getAcceptcode();
		
		initFtpCfg();
			
		ftp_host = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_host");
    	ftp_port = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_port");
    	ftp_uploadpath = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_uploadpath");
    	ftp_username = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_username");
    	ftp_password = cfgDataMap.get("edi_esi_"+ftpCfgAcceptcode+"_ftp_password");
		
		
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 
        try { 
            
        	ftpClient.connect(ftp_host); 
            boolean login = ftpClient.login(ftp_username, ftp_password); 
            if(!login){
            	this.alert("FTP登录失败！");
            	return;
            }
            //设置上传目录 
            ftpClient.changeWorkingDirectory("/"+ftp_uploadpath);
            System.out.println("3.ftp login"+filePath);
            File srcFile = new File(filePath); 
            fis = new FileInputStream(srcFile); 
           // ftpClient.changeWorkingDirectory("/"); 
            ftpClient.setBufferSize(1024); 
            ftpClient.setControlEncoding("utf-8"); 
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            String ftpFileName = randomString+".edi";
            System.out.println("3.5.ftp storeFile start"+ftpFileName);
            ftpClient.storeFile(ftpFileName, fis);
            System.out.println("4.ftp upload file:"+ftpFileName);
            
            String sql = "UPDATE edi_esi set issubmit=true,submittime=now(),submiter='"+AppUtils.getUserSession().getUsercode()+"' , siresp = 'Send'  where id ="+ pkVal;
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
            
			this.alert("上传成功！文件名:"+ftpFileName);
			refresh();
			
        }catch (java.io.FileNotFoundException e){
        	this.alert("导出文件失败！");
        	e.printStackTrace();
        }catch (SocketException e) {
			this.alert("FTP配置配置错误2！");
			e.printStackTrace();
		}catch (Exception e) { 
        	e.printStackTrace(); 
            MessageUtils.showException(e);
        } finally { 
            try { 
            	IOUtils.closeQuietly(fis);
                ftpClient.disconnect(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
                this.alert("关闭FTP连接发生异常！");
            } 
        } 
    }


    @Override
    public void del() {
        try {
            String dmlSql = "\nDELETE FROM edi_esi WHERE id = " + this.pkVal + ";";
            dmlSql += "\nDELETE FROM edi_esidtlcnt WHERE ediesiid = " + this.pkVal + ";";
            dmlSql += "\nDELETE FROM edi_esidtlcntlist WHERE ediesiid = " + this.pkVal + ";";
            daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
        MessageUtils.alert("OK!");
        this.editGrid.reload();
    }

    public void add() {
    	pkVal = -100l;
    	esitype = "IFTMIN";
    	title = "ESI";
    	this.selectedRowData = new Ediesi();
    	this.selectedRowData.setEsitype("IFTMIN");
    	this.selectedRowData.setFiletype("9");
    	this.selectedRowData.setCarryitem("30");
    	this.selectedRowData.setFreightitem("P");
    	this.selectedRowData.setSeafeestatus("26");
    	
    	Long userid = AppUtils.getUserSession().getUserid();
    	SysUser sysUser = this.serviceContext.userMgrService.sysUserDao.findById(userid);
    	
    	this.selectedRowData.setContacter(sysUser.getNamee());
    	this.selectedRowData.setContactel(sysUser.getTel1());
    	this.selectedRowData.setContactemail(sysUser.getEmail1());
    }
    
    @Action
    public void addEditGrid() {
    	this.insert();
    }
    
    

    @Override
	public void saveForm() {
    	try {
			doServiceSave();
			saveEditGrid();
		} catch (Exception e) {
			MessageUtils.showException(e);
			this.addForm();
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
	}


	@Action
    public void saveEditGrid() {
		try {
			editGrid.commit();
			JSONArray jsonArray = new JSONArray();
			if (modifiedData != null) {
				jsonArray.addAll((JSONArray)modifiedData);
			}
			if (addedData != null) {
				JSONArray addArrays = (JSONArray)addedData;
				for (Object object : addArrays) {
					JSONObject jsonObject = (JSONObject)object;
					jsonObject.put("id", "");
				}
				jsonArray.addAll((JSONArray)addedData);
			}
			if (removedData != null) {
				jsonArray.addAll((JSONArray)removedData);
			}
			if(!jsonArray.isEmpty()){
				SqlObject sqlObject = new SqlObject("edi_esidtlcnt", jsonArray.toString(), AppUtils.getUserSession().getUsercode());
				sqlObject.setFkVals("ediesiid", this.pkVal.toString());
				String sql = SqlUtil.builds(sqlObject);
				System.out.println(sql);
				this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			}
			editGrid.reload();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

    @Bind
    @SaveState
    public String ediRptStr;
    
    @Action
    public void showEdi() {
    	try {
			String sql = "SELECT f_edi_esi_iftmin('esiid="+this.pkVal+";userid="+AppUtils.getUserSession().getUserid()+"') AS rpt";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			ediRptStr = StrUtils.getMapVal(map, "rpt");
			Browser.execClientScript("showRpt()");
			update.markUpdate(true, UpdateLevel.Data, "ediRptStr");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
    @Action
    public void soChooseAjaxSubmit() {
    	try {
    		this.saveForm();
    		this.refresh();
    		String sonoAdd = AppUtils.getReqParam("sono");
        	String sql = "SELECT f_edi_esi_choose('type=SO2ESI;esiid="+this.pkVal+";sono="+sonoAdd+"');";
        	this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
        	this.refresh();
//        	String js = "$('#pol_input').val('"+this.selectedRowData.getPolcode()+"');";
//        	js += "$('#pdd_input').val('"+this.selectedRowData.getPddcode()+"');";
//        	js += "$('#destination_input').val('"+this.selectedRowData.getDestinationcode()+"');";
        	String js = "window.location.href='./ediesiedit.aspx?id="+this.pkVal+"'";
        	Browser.execClientScript(js);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
    
	@SaveState
	public String filePath ;
	
	@SaveState
	public String randomString;
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String contentType;
	
	@Bind(id="download5", attribute="src")
    private byte[] getDownload5() {
    	try {
    		return ediRptStr.getBytes();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}finally{
			//获得当前系统桌面路径
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			String absolutePath = desktopDir.getAbsolutePath(); 
			//删除该文件夹下所有文件
		}
    }
	
	@Action
	public void saveFileLocal() {
		Random ra = new Random();
		//获得当前系统桌面路径
		File desktopDir = FileSystemView.getFileSystemView().getDefaultDirectory();
		String absolutePath = desktopDir.getAbsolutePath(); 
		Format format = new SimpleDateFormat("yyyyMMddHHmmsss");
		randomString = format.format(new Date());
		contentType = "text/plain";
		File file = new File(absolutePath+"/edi/iftmin");
		if (!file .exists()  && !file .isDirectory()) {     
			file.mkdirs();
		}
		fileName = randomString+".edi";
		filePath=absolutePath+"/edi/iftmin/"+fileName;
		File newFile = newFile(filePath);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(newFile));
			bw.write(ediRptStr);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(bw!=null){
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/** 创建新文件 */
	public  File newFile(String path){
		//创建文件对象
		File file = new File(path);
		//判断
		if(!file.exists()){ //该文件不存在
			//是目录
			if(file.isDirectory()){
				file.mkdirs();
			}
			//是文件
			if(file.isFile()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return file;
	}
    
    

    @Override
    protected void doServiceFindData() {
    	selectedRowData = serviceContext.ediesiService.ediesiDao.findById(pkVal);
    }

    @Override
    protected void doServiceSave() {
    	try {
            this.serviceContext.ediesiService.ediesiDao.createOrModify(selectedRowData);
            this.pkVal = selectedRowData.getId();
            
            update.markUpdate(true, UpdateLevel.Data, "pkVal");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
    }


    @Action
    public void polajaxSubmit() {
        String polid = AppUtils.getReqParam("polid").toString();
        // 2201 提单港口包含国家 =Y时
        // ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
        String findSysCfgVal = ConfigUtils
                .findSysCfgVal("bill_port_connect_country");
        if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
            String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
                    + "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
                    + polid + ") LIMIT 1";
            try {
                Map m = serviceContext.daoIbatisTemplate
                        .queryWithUserDefineSql4OnwRow(sql1);
                if (m != null && m.get("namee") != null) {
                    Browser
                            .execClientScript("polJsvar.setValue(polJsvar.getValue()+','+'"
                                    + m.get("namee").toString() + "')");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Action
    public void pddajaxSubmit() {
        String pddid = AppUtils.getReqParam("pddid").toString();
        if (!StrUtils.isNull(pddid)) {
            // 2201 提单港口包含国家 =Y时
            // ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
            String findSysCfgVal = ConfigUtils.findSysCfgVal("bill_port_connect_country");
            if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
                String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
                        + "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
                        + pddid + ") LIMIT 1";
                try {
                    Map m = serviceContext.daoIbatisTemplate
                            .queryWithUserDefineSql4OnwRow(sql1);
                    if (m != null && m.get("namee") != null) {
                        Browser.execClientScript("pddJsvar.setValue(pddJsvar.getValue()+','+'"
                                + m.get("namee").toString() + "')");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Action
    public void destinationajaxSubmit() {
        String destinationid = AppUtils.getReqParam("destinationid").toString();
        // 2201 提单港口包含国家 =Y时
        // ：委托中选择港口的时候，后面的英文名自动加这个港口对应的国家的英文名，查港口属于的国家，再到国家档案里面查这个英文名
        String findSysCfgVal = ConfigUtils
                .findSysCfgVal("bill_port_connect_country");
        if (!StrUtils.isNull(findSysCfgVal) && findSysCfgVal.equals("Y")) {
            String sql1 = "SELECT namee FROM dat_country x WHERE isdelete = FALSE "
                    + "	AND EXISTS(SELECT 1 FROM dat_port WHERE country = x.namec AND id = "
                    + destinationid + ") LIMIT 1";
            try {
                Map m = serviceContext.daoIbatisTemplate
                        .queryWithUserDefineSql4OnwRow(sql1);
                if (m != null && m.get("namee") != null) {
                    Browser
                            .execClientScript("destinationJs.setValue(destinationJs.getValue()+','+'"
                                    + m.get("namee").toString() + "')");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Action
    public void refreshCnt() {
        try {
            this.editGrid.reload();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
        }
    }


    @Action
    public void delCnt() {
        try {
        	String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("Please choose one!");
                return;
            }
            try {
                String dmlSql = "\nDELETE FROM edi_esidtlcnt WHERE id IN(" + StrUtils.array2List(ids) + ");";
                dmlSql += "\nDELETE FROM edi_esidtlcntlist WHERE esidtlcntid IN(" + StrUtils.array2List(ids) + ");";
                daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
            } catch (Exception e) {
                MessageUtils.showException(e);
                return;
            }
            MessageUtils.alert("OK!");
            this.editGrid.reload();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
        }
    }
    
    
    /**
	 * @return
	 */
	@Bind(id="cntype")
    public List<SelectItem> getCntype() {
    	try {
			return serviceContext.commonDBCache.getComboxItems("x.code","x.namee","api_data x","WHERE x.srctype = 'ESI-CARGOSMART' AND x.maptype = 'CNTTYPE'","ORDER BY x.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	  /**
	 * @return
	 */
	@Bind(id="carrier")
    public List<SelectItem> getCarrier() {
    	try {
    		return serviceContext.commonDBCache.getComboxItems("x.code","x.namee","api_data x","WHERE x.srctype = 'ESI-CARGOSMART' AND x.maptype = 'CARRIER'","ORDER BY x.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * @return
	 */
	@Bind(id="sendercode")
    public List<SelectItem> getSendercode() {
    	try {
			return serviceContext.commonDBCache.getComboxItems("x.code","x.namee","api_data x","WHERE x.srctype = 'ESI-IFTMIN' AND x.maptype = 'SENDERCODE'","ORDER BY x.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * @return
	 */
	@Bind(id="acceptcode")
    public List<SelectItem> getAcceptcode() {
    	try {
			return serviceContext.commonDBCache.getComboxItems("x.code","x.namee","api_data x","WHERE x.srctype = 'ESI-IFTMIN' AND x.maptype = 'ACCEPTCODE'","ORDER BY x.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@SaveState
	public String type;
	
	@Bind
	@SaveState
	public String detailsContent;
	
	@Bind
	public UIWindow detailsWindow;
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");

		if ("1".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type1()");

		} else if ("2".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type2()");

		} else if ("3".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type3()");

		} else if ("4".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type4()");

		} else if ("5".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type5()");

		} else if ("6".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type6()");

		} else if ("7".equals(type)) { 
			this.detailsContent = content;
			Browser.execClientScript("type7()");

		} 
		
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		/* this.update.markUpdate(UpdateLevel.Data, "detailsTitle"); */

		this.detailsWindow.show();
		//System.out.print("123");
		Browser.execClientScript("coutRowLength();");
	}
		
	/**
	 * 输入框(大框)回填
	 */
	@Action
	public void back() {
		setDetails(this.type);
		this.detailsWindow.close();
	}

	public void setDetails(String type) {
		if ("1".equals(type)) { // 委托要求大框
			this.selectedRowData.setShipper(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "shipper");

		} else if ("2".equals(type)) { // 拖车要求大框
			this.selectedRowData.setConsignee(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "consignee");

		} else if ("3".equals(type)) { // 提单要求大框
			this.selectedRowData.setNotifier1(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifier1");

		} else if ("4".equals(type)) { // 提单要求大框
			this.selectedRowData.setAgent(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "agent");

		} else if ("5".equals(type)) { // 提单要求大框
			this.selectedRowData.setBlremarks(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "blremarks");

		} else if ("6".equals(type)) { // 提单要求大框
			this.selectedRowData.setBlremarksother(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "blremarksother");

		} else if ("7".equals(type)) { // 提单要求大框
			this.selectedRowData.setNotifier2(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "notifier2");

		} 
	}
	
	@Action
	public void clearUnseeChar() {
		try {
			if(detailsContent != null){
//				System.out.println(detailsContent);
				int lenBefore = detailsContent.length();
				//detailsContent = AppUtils.replaceStringByRegEx(detailsContent);
				
				StringBuffer stringBuffer = new StringBuffer();
				String[] strArrs = detailsContent.split("\n");
				int index = 1;
				for (String str : strArrs) {
					System.out.println("before:"+str.length());
					str = str.replaceAll("[^\\w\\u4E00-\\u9FA5\\s\\.,:\\-\\+\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\=\\[\\]\\{\\}\\;\\'\\\\\\?\\|\\/\\<\\>\\\"\\`\\·\\！\\￥\\（\\）\\【\\】\\《\\》]*", "");
					System.out.println("after:"+str.length());
					str = str.trim();
					stringBuffer.append(str);
					if(index<strArrs.length)stringBuffer.append("\n");
					index++;
				}
				detailsContent = stringBuffer.toString();
				System.out.println(detailsContent);
				
				int lenAfter = detailsContent.length();
//				System.out.println(detailsContent);
				Browser.execClientScript("coutRowLength()");
				if(lenBefore != lenAfter){
					this.alert("成功替换隐藏字符:" + (lenBefore - lenAfter));
				}else{
					this.alert("未找到隐藏字符");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	@SaveState
	@Accessible
	public String custypeMBL;
	
	@SaveState
	@Accessible
	public String sql = "AND 1=1";
	
	@SaveState
	@Accessible
	public String sqlMy = "";
	
	@Bind
	public UIWindow showCustomerWindow;
	
	@Bind
	public UIDataGrid customerGrid;
	
	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		return this.customerService.getCustomerDataProvider(sql);
	}
	
	@ManagedProperty("#{customerconchooseBean}")
	private CustomerConChooseBean customerService;
	
	@Bind
	private String qryCustomerKey;
	
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}
	

	
	@Action
	public void showCustomerMBL(){
		custypeMBL = (String) AppUtils.getReqParam("custypeMBL");
		//MBL发货人
		if ("1".equals(custypeMBL)) {
			this.sql = " AND contactype = 'B' AND (contactype2 = 'O' OR (contactype2 = 'S' AND EXISTS (SELECT 1 FROM sys_corporation x where x.ishipper = true AND x.id = customerid AND x.isdelete = false)))";
			//Browser.execClientScript("cnortitlemblname.focus");
		//MBL收货人
		}else if("2".equals(custypeMBL)) {
			this.sql = " AND contactype = 'B' AND (contactype2 = 'P' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))";
			//Browser.execClientScript("cneetitlemblname.focus");
		}//MBL通知人
		else if ("3".equals(custypeMBL)) {
			this.sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))";
			//Browser.execClientScript("notifytitlemblname.focus");
		} //MBL通知人2
		else if ("4".equals(custypeMBL)) {
			this.sql = " AND contactype = 'B' AND (contactype2 = 'Q' OR (contactype2 = 'A' AND EXISTS (SELECT 1 FROM sys_corporation x where x.isagentdes = true AND x.id = customerid AND x.isdelete = false)))";
			//Browser.execClientScript("notifytitlemblname.focus");
		} 
		
		showCustomerWindow.show();
		this.customerGrid.reload();
	}
	
	@SaveState
	@Accessible
	public String custype;
	
	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		if ("1".equals(custypeMBL)) {
			this.selectedRowData.setShipper((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "shipper");
		} else if ("2".equals(custypeMBL)) {
			this.selectedRowData.setConsignee((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "cnortitle");
		} else if ("3".equals(custypeMBL)) {
			this.selectedRowData.setNotifier1((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifier1");
		} else if ("4".equals(custypeMBL)) {
			this.selectedRowData.setNotifier2((String) m.get("contactxt"));
			this.update.markUpdate(UpdateLevel.Data, "notifier2");
		}
		showCustomerWindow.close();
	}
	
	@SaveState
	@Accessible
	public SysCorpcontacts sysCorp = new SysCorpcontacts();
	
	@SaveState
	@Accessible
	public SysCorporation corporation = new SysCorporation();
	
	public Long customerid=100L;
	
	@Action
	public void savecne() {
		String cnetitle = AppUtils.getReqParam("cnetitle").trim();
		String type = AppUtils.getReqParam("type").trim();

		try {
			if ( StrUtils.isNull(cnetitle)) {
				MessageUtils.alert("请输入正确信息");

			} else {
				if(type.equals("4")){
					try{
						String sql = "  contactype2 = 'O' AND isdelete = FALSE AND  contactxt = '"+cnetitle+"'";
						//System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						//sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.corporation.getAbbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
					}catch (Exception e){
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("O");
						//String code = (StrUtils.isNull(cnename) ? (this.jo+ "-O-" + getCusCode("4")) 
						//sysCorp.setName(StrUtils.isNull(cnename) ? code
						sysCorp.setCustomerabbr(this.corporation.getAbbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						//selectedRowData.setCnortitlemblid(Long.valueOf(getCusdesc(code)[0]));
						//selectedRowData.setCnortitlemblname(code);
						update.markUpdate(true, UpdateLevel.Data,"masterEditPanel");
					}
				}
				else if (type.equals("5")) {
					// MBL收货人
					try{
						String sql = "  contactype2 = 'P' AND isdelete = FALSE AND  contactxt = '" + cnetitle+"'";
						//System.out.println(sql);
						List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
						sysCorp = SysCorpcontactss.get(0);
						//sysCorp.setName(cnename);
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerabbr(this.corporation.getAbbr());
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");

					}catch (Exception e){
						sysCorp = new SysCorpcontacts();
						sysCorp.setContactxt(cnetitle);
						sysCorp.setCustomerid(this.customerid);
						sysCorp.setContactype("B");
						sysCorp.setContactype2("P");	
						sysCorp.setCustomerabbr(this.corporation.getAbbr());
						sysCorp.setId(0);
						sysCorp.setSex("M");
						serviceContext.customerContactsMgrService
								.saveData(sysCorp);
						MessageUtils.alert("OK");
						update.markUpdate(true, UpdateLevel.Data,
								"masterEditPanel");
					}
			}else if (type.equals("6")) {
				try{
					String sql = "  contactype2 = 'Q' AND isdelete = FALSE AND  contactxt = '" + cnetitle+"'";
					//System.out.println(sql);
					List<SysCorpcontacts> SysCorpcontactss = this.serviceContext.customerContactsMgrService.sysCorpcontactsDao.findAllByClauseWhere(sql);
					sysCorp = SysCorpcontactss.get(0);
					//sysCorp.setName(cnename);
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerabbr(this.corporation.getAbbr());
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");

				}catch (Exception e){
					sysCorp = new SysCorpcontacts();
					sysCorp.setContactxt(cnetitle);
					sysCorp.setCustomerid(this.customerid);
					sysCorp.setContactype("B");
					sysCorp.setContactype2("Q");	
					sysCorp.setCustomerabbr(this.corporation.getAbbr());
					sysCorp.setId(0);
					sysCorp.setSex("M");
					serviceContext.customerContactsMgrService
							.saveData(sysCorp);
					MessageUtils.alert("OK");
					update.markUpdate(true, UpdateLevel.Data,
							"masterEditPanel");
				}

			}
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	
	//收发通客户刷新
	@Action
	public void refreshCustomer() {
		customerQry();
	}
	
	//收发通客户删除
	@Action
	public void deleteCustomer(){
		String[] ids = this.customerGrid.getSelectedIds();
		if (ids.length != 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}  else {
			serviceContext.customerContactsMgrService.removeDatedel(Long.valueOf(ids[0]));
			MessageUtils.alert("OK");
			this.customerGrid.reload();
		}
	}
	

	
	@Action
	public void showEdireport() {
		String arg = "&id=" + this.pkVal + "&userid="+ AppUtils.getUserSession().getUserid() + "";
		String openUrl = AppUtils.getRptUrl()+ "/reportJsp/showReport.jsp?raq=/define/edi_esi_report.raq";
		AppUtils.openNewPage(openUrl + arg);
		
	}

}
