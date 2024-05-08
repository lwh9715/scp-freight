package com.scp.view.module.ship;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.ajax.ProgressAction;
import org.operamasks.faces.component.ajax.ProgressState;
import org.operamasks.faces.component.ajax.ProgressStatus;
import org.operamasks.faces.component.widget.UIFileUpload;
import org.operamasks.faces.component.widget.fileupload.FileUploadItem;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.sys.SysLogDao;
import com.scp.model.sys.SysLog;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.util.ReadManifestExcle;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.uploadtempleteBean", scope = ManagedBeanScope.REQUEST)
public class UploadTempleteBean extends GridView {
	
	@Bind
	@SaveState
	public String method;
	
	
	@Bind
	@SaveState
	public String jobid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.method = AppUtils.getReqParam("method");
			this.jobid = AppUtils.getReqParam("jobid");
			
			update.markUpdate(UpdateLevel.Data,"hidenvalue");
		}
	}
	
//	/**
//	 * 
//	 * @param sheetNo 解析第sheetNo工作表(最小1)
//	 * @param startReadRow 从startReadRow行开始解析(最小1)
//	 * @return String
//	 */
//	protected String analyzeExcelData(int sheetNo,int startReadRow){
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0;i<getSuperlist().size();i++) {
//			// sheet
//			if(i+1==sheetNo){
//				for(int j = startReadRow-1;j<getSuperlist().get(i).size();j++){
//					//行
//					for (Object oo : getSuperlist().get(i).get(j)) {
//						// 单元格,去除每个cell中的.0或.00结尾
//						if (oo != null){
//							if (oo.toString().endsWith(".0")) {
//								oo = oo.toString().substring(0,
//										oo.toString().lastIndexOf(".0"));
//							}else if(oo.toString().endsWith(".00")){
//								oo = oo.toString().substring(0,
//										oo.toString().lastIndexOf(".00"));
//							}
//							sb.append(oo);
//							sb.append("\t");
//						}
//					}
//					sb.deleteCharAt(sb.lastIndexOf("\t"));
//					sb.append("\n");
//				}
//				break;
//			}else{
//				continue;
//			}
//		}
//		return sb.toString();
//	}
//	/**
//	 * processListener必须绑定在一个参数为FileUploadItem的无返回值方法上，通过FileUploadItem参数，我们可以拿到
//	 * file input框的客户端文件名，输入框的id等信息，并可以通过FileUploadItem.openStream()方法，获取上传的文件流。
//	 * 这个例子里，我们简单的将文件流令存为一个服务器上的文件，在实际的应用中，这里可以将文件数据流持久到数据库中，或者 做任何你想要的处理。
//	 * 
//	 * @param fileUploadItem
//	 * @throws IOException 
//	 */
//	protected void processUpload111(FileUploadItem fileUploadItem) throws IOException {
//		if (fileUploadItem.getFieldName().equals("fileUpload1"))
//			deleteOldFiles();
//		InputStream input = null;
//		FileOutputStream output = null;
//		
//			input = fileUploadItem.openStream();
//			File file = new File(getSaveToPath(fileUploadItem));
//			output = new FileOutputStream(file);
//			byte[] buf = new byte[4096];
//			// UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
//			int length = UIFileUpload.END_UPLOADING;
//			while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
//				output.write(buf, 0, length);
//			}
//			//AppUtils.debug(file.getCanonicalPath());
//			setSuperlist(ReadExcel.read(file));
//			if (output != null)
//				try {
//					output.close();
//				} catch (IOException e) {
//				}
//	}
	
	
	
	
	private UIFileUpload fileUpload1;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
    /**
     * processListener必须绑定在一个参数为FileUploadItem的无返回值方法上，通过FileUploadItem参数，我们可以拿到
     * file input框的客户端文件名，输入框的id等信息，并可以通过FileUploadItem.openStream()方法，获取上传的文件流。
     * 这个例子里，我们简单的将文件流令存为一个服务器上的文件，在实际的应用中，这里可以将文件数据流持久到数据库中，或者
     * 做任何你想要的处理。
     * @param fileUploadItem
     */
    public void processUpload(FileUploadItem fileUploadItem) {
		if (fileUploadItem.getFieldName().equals("fileUpload1"))
		deleteOldFiles();
		InputStream input = null;
		FileOutputStream output = null;
		try {
			input = fileUploadItem.openStream();
			File file = new File(getSaveToPath(fileUploadItem));
			output = new FileOutputStream(file);
			byte[] buf = new byte[4096];
			// UIFileUpload.END_UPLOADING为-1，这里表示数据输入流已经读取完毕
			int length = UIFileUpload.END_UPLOADING;
			while ((length = input.read(buf)) != UIFileUpload.END_UPLOADING) {
				output.write(buf, 0, length);
			}
			AppUtils.debug(file.getCanonicalPath());
			importMethod(file);
		} catch (FileNotFoundException e1) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e1.toString());
			syslog.setLogtime(new Date());
			//syslog.setLogtype("");
			sysLogDao.create(syslog);
			e1.printStackTrace();
		} catch (Exception e1) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e1.getMessage());
			syslog.setLogtime(new Date());
			//syslog.setLogtype("");
			sysLogDao.create(syslog);
			e1.printStackTrace();
		}
		if (input != null){
			try {
				input.close();
			} catch (IOException e) {
			}
		}
		if (output != null){
			try {
				output.close();
			} catch (IOException e) {
			}
		}
    }
    
    
    @Bind
	@SaveState
	private String types;
    
    /**
     * 
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void importMethod(File file) throws FileNotFoundException, IOException{
    	if(this.method==null || this.method.isEmpty()){
    		
    	}else if("importshipping".equals(this.method)){
    		if("1".equals(types)){
    			importShipping(file);
    		}else if("2".equals(types)){
    			importShippingList(file);
    		}else if("3".equals(types)){
    			importSuitcaseList(file);
    		}else if("4".equals(types)){
    			importShippingNinbo(file);
    		}else if("5".equals(types)){
				importShippingLenovo(file);
			}else if("6".equals(types)){
				importShippingLenovoIndia(file);
			}else{
    			MessageUtils.alert("请选择格式!");
    		}
    	}
    	
    }

	//托书
    private void importShipping(File file) throws FileNotFoundException, IOException{
    	Map<String, String> map = ReadExcel.importJobsForExcel(file);
    	if(map != null && map.size() > 0){
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT f_rpt_import_shipping('cnortitle="+map.get("cnortitle").replaceAll("'", "''")+";");
			sbsql.append("cneetitle="+map.get("cneetitle").replaceAll("'", "''")+";");
			sbsql.append("notifytitle="+map.get("notifytitle").replaceAll("'", "''")+";");
			sbsql.append("pretrans="+map.get("pretrans").replaceAll("'", "''")+";");
			sbsql.append("poa="+map.get("poa").replaceAll("'", "''")+";");
			sbsql.append("vessel="+map.get("vessel").replaceAll("'", "''")+";");
			sbsql.append("voyage="+map.get("voyage").replaceAll("'", "''")+";");
			sbsql.append("pol="+map.get("pol").replaceAll("'", "''")+";");
			sbsql.append("pdd="+map.get("pdd").replaceAll("'", "''")+";");
			sbsql.append("pod="+map.get("pod").replaceAll("'", "''")+";");
			sbsql.append("destination="+map.get("destination").replaceAll("'", "''")+";");
			sbsql.append("cntinfos="+map.get("cntinfos").replaceAll("'", "''")+";");
			sbsql.append("goodsinfo="+map.get("goodsinfo").replaceAll("'", "''")+";");
			sbsql.append("grswgt="+map.get("grswgt").replaceAll("'", "''")+";");
			sbsql.append("cbm="+map.get("cbm").replaceAll("'", "''")+";");
			sbsql.append("agentitle="+map.get("agentitle").replaceAll("'", "''")+";");
			sbsql.append("jobid="+this.jobid+";corpid="+AppUtils.getUserSession().getCorpidCurrent()+"') AS jobid");
			Map result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			////System.out.println(sbsql.toString());
//			Vector<String> sqlBatch = new Vector<String>();
//			sqlBatch.add(sbsql.toString());
//			try {
//				this.serviceContext.daoIbatisTemplate.executeQueryBatchByJdbc(sqlBatch);
//				MessageUtils.alert("OK!");
//				this.refresh();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
			
		}
    }
    
  //提箱单
    private void importSuitcaseList(File file) throws FileNotFoundException, IOException{
    	Map<String, String> map = ReadExcel.importSuitcaseList(file);
    	if(map != null && map.size() > 0){
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT f_pdf_import_shipping('vessel="+map.get("vessel").replaceAll("'", "''")+";");
			sbsql.append("voyage="+map.get("voyage").replaceAll("'", "''")+";");
			sbsql.append("mblterminal="+map.get("mblterminal").replaceAll("'", "''")+";");
			sbsql.append("mblcyopen="+map.get("mblcyopen").replaceAll("'", "''")+";");
			sbsql.append("clstime="+map.get("clstime").replaceAll("'", "''")+";");
			sbsql.append("sidate="+map.get("sidate").replaceAll("'", "''")+";");
			sbsql.append("vgmdate="+map.get("vgmdate").replaceAll("'", "''")+";");
//			sbsql.append("eta="+map.get("eta").replaceAll("'", "''")+";");
			sbsql.append("etd="+map.get("etd").replaceAll("'", "''")+";");
			sbsql.append("jobid="+this.jobid+";corpid="+AppUtils.getUserSession().getCorpidCurrent()+"') AS pdf");
			Map result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			System.out.println(result.get("pdf")+"0000000000");
		}
    }
    
    
    //样单
    private void importShippingList(File file) throws FileNotFoundException, IOException{
    	Map<String, String> map = ReadExcel.importListForExcel(file);
    	if(map != null && map.size() > 0){
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT f_rpt_import_shippinglist('cnortitle="+map.get("cnortitle").replaceAll("'", "''")+";");
			sbsql.append("cneetitle="+map.get("cneetitle").replaceAll("'", "''")+";");
			sbsql.append("notifytitle="+map.get("notifytitle").replaceAll("'", "''")+";");
			sbsql.append("vessel="+map.get("vessel").replaceAll("'", "''")+";");
			sbsql.append("voyage="+map.get("voyage").replaceAll("'", "''")+";");
			sbsql.append("pol="+map.get("pol").replaceAll("'", "''")+";");
			sbsql.append("pdd="+map.get("pdd").replaceAll("'", "''")+";");
			sbsql.append("destination="+map.get("destination").replaceAll("'", "''")+";");
			sbsql.append("cntinfos="+map.get("cntinfos").replaceAll("'", "''")+";");
			sbsql.append("piece="+map.get("piece").replaceAll("'", "''")+";");
			sbsql.append("goodsinfo="+map.get("goodsinfo").replaceAll("'", "''")+";");
			sbsql.append("grswgt="+map.get("grswgt").replaceAll("'", "''")+";");
			sbsql.append("cbm="+map.get("cbm").replaceAll("'", "''")+";");
			String pack = map.get("piece").replaceAll("[^a-z^A-Z]", "");
			sbsql.append("packagee="+pack.replaceAll("'", "''")+";");
			sbsql.append("cntno="+map.get("cntno").replaceAll("'", "''")+";");		
			sbsql.append("sealno="+map.get("sealno").replaceAll("'", "''")+";");
			sbsql.append("piecee="+map.get("piecee").replaceAll("'", "''")+";");
			sbsql.append("vgm="+map.get("vgm").replaceAll("'", "''")+";");
			sbsql.append("gross="+map.get("gross").replaceAll("'", "''")+";");
			sbsql.append("measurement="+map.get("measurement").replaceAll("'", "''")+";");
			sbsql.append("jobid="+this.jobid+";corpid="+AppUtils.getUserSession().getCorpidCurrent()+"') AS jobid");
			
			System.out.println(sbsql.toString());
			Map result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
		}
    }
    
//    //宁波舱单
    private void importShippingNinbo(File file) throws FileNotFoundException, IOException{
    	ReadManifestExcle readEx = new ReadManifestExcle();
    	Map result = null;
    	List<String[]> list	= null;
    	StringBuffer sbsql = null;
    	String[] str = null;
    	Map<String, List<String[]>> map = readEx.readManifest(file);//解析excle得到数据
    	long userid = AppUtils.getUserSession().getUserid();
    	
    	if(null != map && map.size() > 0
    			&& null != map.get("mblno")
    			&& null != map.get("total")
    			&& null != map.get("itemized")
    			&& null != map.get("total_list_mblno")){//存入数据
    		
    		Map<String, String> ldtypeMap = new HashMap<String, String>();
    		for (int i = 0; i < map.get("itemized").size(); i++) {
    			String[] name = map.get("itemized").get(i);
    			//ssss
    			if(ldtypeMap.containsKey(name[1]) && name[0].equals(ldtypeMap.get(name[1])) == false){
    				ldtypeMap.put(name[1], ldtypeMap.get(name[1])+";"+name[0]);
    			}else{
    				ldtypeMap.put(name[1], name[0]);
    			}
			}
    		
    		for (int i = 0; i < 6; i++) {
    			switch (i) {
	    			case 0://处理工作单表
						list =	map.get("total");
						str = null;
						for (int j = 0; j < list.size(); j++) {
							sbsql = new StringBuffer();
							sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=total','");
							str = list.get(j);
							sbsql.append("mblno="+str[0].replaceAll("'", "''")+";");
				    		sbsql.append("piece="+str[4].replaceAll("'", "''")+";");
				    		sbsql.append("packagee="+str[5].replaceAll("'", "''")+";");
				    		sbsql.append("grswgt="+str[6].replaceAll("'", "''")+";");
				    		sbsql.append("cbm="+str[7].replaceAll("'", "''")+";");
				    		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
				    		sbsql.append("ldtype=F;userid="+userid+";");
				    		sbsql.append("') AS total;");
				    		
				    		//System.out.println(sbsql.toString());
				    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
				    		System.out.println(result);
					}
					break;
	    			case 1://处理委托单表
						list =	map.get("mblno");
						str = null;
						for (int j = 0; j < list.size(); j++) {
							sbsql = new StringBuffer();
							sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=mblno','");
							str = list.get(j);
							sbsql.append("mblno="+str[0].replaceAll("'", "''")+";");
	    		    		sbsql.append("piece="+str[3].replaceAll("'", "''")+";");
	    		    		sbsql.append("packagee="+str[4].replaceAll("'", "''")+";");
	    		    		sbsql.append("grswgt="+str[5].replaceAll("'", "''")+";");
	    		    		sbsql.append("cbm="+str[6].replaceAll("'", "''")+";");
	    		    		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
	    		    		String ldtype = "";
	    		    		for (int k = 0; k < map.get("itemized").size(); k++) {
	    		    			String[] name = map.get("itemized").get(k);
	    		    			if(str[0].equals(name[0])){
	    		    				ldtype = ldtypeMap.get(name[1]).split(";").length > 1 ? "L" : "F";
	    		    				break;
	    		    			}
	    					}
	    		    		sbsql.append("ldtype="+ldtype+";");
	    		    		sbsql.append("userid="+userid+";");
	    		    		sbsql.append("') AS mblno;");
	    		    		
	    		    		//System.out.println(sbsql.toString());
	    		    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
	    		    		System.out.println(result);
						}
					break;
					
	    			case 2://处理总单柜子表
	    				//sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=itemized','"+JSONObject.fromObject(map).toString()+");'");
						Map<String,String[]> map3 = new HashMap<String,String[]>();
						str = null;
						List<String[]> list3 = new ArrayList<String[]>();
						String[] str3 = null;
						list3 =	map.get("itemized");
						for (int j = 0; j < list3.size(); j++) {
							str = list3.get(j);
							str3 = new String[20];
							for (int j2 = 0; j2 < str3.length; j2++) {
								str3[j2] = str[j2];
							}
							if(map3.containsKey(str3[1])){
								if(map3.get(str3[1])[4].contains(str3[4])){
									str3[4] = map3.get(str3[1])[4];
								}else{
									str3[4] = map3.get(str3[1])[4]+"\n"+ str3[4];
								}
//								str3[4] = map3.get(str3[1])[4].contains(str3[4]) ? map3.get(str3[1])[4] : map3.get(str3[1])[4]+"\n"+ str3[4];
								str3[6] = (Integer.valueOf(map3.get(str3[1])[6]) + Integer.valueOf(str3[6])) + "";
								str3[8] = (Double.valueOf(map3.get(str3[1])[8]) + Double.valueOf(str3[8])) + "";
								str3[9] = (Double.valueOf(map3.get(str3[1])[9]) + Double.valueOf(str3[9])) + "";
							}
							map3.put(str3[1], str3);
						}
						
						Iterator it2=map3.entrySet().iterator();         
				        String key2 = null;         
				        String[] value2 = null; 
				        int count = 1;
				        while(it2.hasNext()){  
			                Map.Entry entry = (Map.Entry)it2.next();         
			                key2 = entry.getKey().toString();         
			                value2 = map3.get(key2);
			               
			                sbsql = new StringBuffer();
							sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=cnt','");
							sbsql.append("mblno="+map.get("total_list_mblno").get(0)[0].replaceAll("'", "''")+";");//总单提单号
							sbsql.append("cntno="+value2[1].replaceAll("'", "''")+";");
							sbsql.append("sealno="+value2[2].replaceAll("'", "''")+";");
	    		    		sbsql.append("cntype="+value2[3].replaceAll("'", "''")+";");
	    		    		sbsql.append("goodsnamee="+value2[4].replaceAll("'", "''")+";");
	    		    		sbsql.append("hscode="+value2[5].replaceAll("'", "''")+";");
	    		    		sbsql.append("piece="+value2[6].replaceAll("'", "''")+";");
	    		    		sbsql.append("packagee="+value2[7].replaceAll("'", "''")+";");
	    		    		sbsql.append("grswgt="+value2[8].replaceAll("'", "''")+";");
	    		    		sbsql.append("cbm="+value2[9].replaceAll("'", "''")+";");
	    		    		sbsql.append("markno="+value2[10].replaceAll("'", "''")+";");
	    		    		sbsql.append("vgmmethod="+(null==value2[13]?"0":value2[13].replaceAll("'", "''"))+";");
		            		sbsql.append("vgm="+(null==value2[14]?"0":value2[14].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmresponsibleparty="+(null==value2[15]?"":value2[15].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmsignature="+(null==value2[16]?"":value2[16].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmemail="+(null==value2[17]?"":value2[17].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmtelephone="+(null==value2[18]?"":value2[18].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmaddress="+(null==value2[19]?"":value2[19].replaceAll("'", "''"))+";");
		            		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
		            		sbsql.append("orderno="+count+";");
//		            		String ldtype = ldtypeMap.get(value2[1])>1 ? "L" : "F";
		            		sbsql.append("ldtype=F;");
	    		    		sbsql.append("') AS cnt;");
	    		    		
	    		    		//System.out.println(sbsql.toString());
	    		    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
	    		    		System.out.println(result);
	    		    		count++;
				        } 
						break;
					
					case 3://处理分单柜子信息
						//sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=itemized','"+JSONObject.fromObject(map).toString()+");'");
						Map<String,String[]> map2 = new HashMap<String,String[]>();
						str = null;
						List<String[]> list2 = new ArrayList<String[]>();
						String[] str2 = null;
						list = map.get("itemized");
						list2 =	map.get("itemized");
						for (int j = 0; j < list2.size(); j++) {
							str = list2.get(j);
							str2 = new String[20];
							for (int j2 = 0; j2 < str2.length; j2++) {
								str2[j2] = str[j2];
							}
							if(map2.containsKey(str2[0]+";"+str2[1])){
								str2[4] = map2.get(str2[0]+";"+str2[1])[4]+"\n"+ str2[4];
								str2[6] = (Integer.valueOf(map2.get(str2[0]+";"+str2[1])[6]) + Integer.valueOf(str2[6])) + "";
								str2[8] = (Double.valueOf(map2.get(str2[0]+";"+str2[1])[8]) + Double.valueOf(str2[8])) + "";
								str2[9] = (Double.valueOf(map2.get(str2[0]+";"+str2[1])[9]) + Double.valueOf(str2[9])) + "";
							}
							map2.put(str2[0]+";"+str2[1], str2);
						}
						
						Iterator it=map2.entrySet().iterator();         
				        String key = null;         
				        String[] value = null;  
				        int[] counts= new int[map.get("mblno").size()];
				        String[] mblStr = new String[map.get("mblno").size()];
				        for (int j = 0; j < mblStr.length; j++) {
				        	mblStr[j] = map.get("mblno").get(j)[0];
						}
				        while(it.hasNext()){  
			                Map.Entry entry = (Map.Entry)it.next();         
			                key = entry.getKey().toString();         
			                value = map2.get(key);
			               
			                sbsql = new StringBuffer();
							sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=cnt','");
							sbsql.append("mblno="+value[0].replaceAll("'", "''")+";");
							sbsql.append("cntno="+value[1].replaceAll("'", "''")+";");
							sbsql.append("sealno="+value[2].replaceAll("'", "''")+";");
	    		    		sbsql.append("cntype="+value[3].replaceAll("'", "''")+";");
	    		    		sbsql.append("goodsnamee="+value[4].replaceAll("'", "''")+";");
	    		    		sbsql.append("hscode="+value[5].replaceAll("'", "''")+";");
	    		    		sbsql.append("piece="+value[6].replaceAll("'", "''")+";");
	    		    		sbsql.append("packagee="+value[7].replaceAll("'", "''")+";");
	    		    		sbsql.append("grswgt="+value[8].replaceAll("'", "''")+";");
	    		    		sbsql.append("cbm="+value[9].replaceAll("'", "''")+";");
	    		    		sbsql.append("markno="+value[10].replaceAll("'", "''")+";");
	    		    		sbsql.append("vgmmethod="+(null==value[13]?"0":value[13].replaceAll("'", "''"))+";");
		            		sbsql.append("vgm="+(null==value[14]?"0":value[14].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmresponsibleparty="+(null==value[15]?"":value[15].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmsignature="+(null==value[16]?"":value[16].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmemail="+(null==value[17]?"":value[17].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmtelephone="+(null==value[18]?"":value[18].replaceAll("'", "''"))+";");
		            		sbsql.append("vgmaddress="+(null==value[19]?"":value[19].replaceAll("'", "''"))+";");
		            		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
		            		String ldtype = ldtypeMap.get(value[1]).split(";").length > 1 ? "L" : "F";
		            		for (int j = 0; j < mblStr.length; j++) {
								if(value[0].equals(mblStr[j])){
									counts[j] = counts[j] + 1;
									sbsql.append("orderno="+counts[j]+";");
									break;
								}
							}
		            		sbsql.append("ldtype="+ldtype+";");
	    		    		sbsql.append("') AS cnt;");
	    		    		
	    		    		//System.out.println(sbsql.toString());
	    		    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
	    		    		System.out.println(result);
				        } 
						break;
					case 4://处理分单柜子详细信息
						//sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=itemized','"+JSONObject.fromObject(map).toString()+");'");
						str = null;
						for (int j = 0; j < list.size(); j++) {
							sbsql = new StringBuffer();
							sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=itemized','");
							str = list.get(j);
							if(str[0].equals(map.get("total_list_mblno").get(0)[0])){
								continue;
							}
							sbsql.append("mblno="+str[0].replaceAll("'", "''")+";");
							sbsql.append("cntno="+str[1].replaceAll("'", "''")+";");
							sbsql.append("sealno="+str[2].replaceAll("'", "''")+";");
	    		    		sbsql.append("cntype="+str[3].replaceAll("'", "''")+";");
	    		    		sbsql.append("goodsnamee="+str[4].replaceAll("'", "''")+";");
	    		    		sbsql.append("hscode="+str[5].replaceAll("'", "''")+";");
	    		    		sbsql.append("piece="+str[6].replaceAll("'", "''")+";");
	    		    		sbsql.append("packagee="+str[7].replaceAll("'", "''")+";");
	    		    		sbsql.append("grswgt="+str[8].replaceAll("'", "''")+";");
	    		    		sbsql.append("cbm="+str[9].replaceAll("'", "''")+";");
	    		    		sbsql.append("markno="+str[10].replaceAll("'", "''")+";");
		            		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
	    		    		sbsql.append("') AS itemized;");
	    		    		
	    		    		//System.out.println(sbsql.toString());
	    		    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
	    		    		System.out.println(result);
						}
						break;
					case 5://处理总单柜子详细信息
						//sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=itemized','"+JSONObject.fromObject(map).toString()+");'");
						str = null;
						for (int j = 0; j < list.size(); j++) {
							sbsql = new StringBuffer();
							sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=itemized','");
							str = list.get(j);
							sbsql.append("mblno="+map.get("total_list_mblno").get(0)[0]+";");
							sbsql.append("cntno="+str[1].replaceAll("'", "''")+";");
							sbsql.append("sealno="+str[2].replaceAll("'", "''")+";");
	    		    		sbsql.append("cntype="+str[3].replaceAll("'", "''")+";");
	    		    		sbsql.append("goodsnamee="+str[4].replaceAll("'", "''")+";");
	    		    		sbsql.append("hscode="+str[5].replaceAll("'", "''")+";");
	    		    		sbsql.append("piece="+str[6].replaceAll("'", "''")+";");
	    		    		sbsql.append("packagee="+str[7].replaceAll("'", "''")+";");
	    		    		sbsql.append("grswgt="+str[8].replaceAll("'", "''")+";");
	    		    		sbsql.append("cbm="+str[9].replaceAll("'", "''")+";");
	    		    		sbsql.append("markno="+str[10].replaceAll("'", "''")+";");
		            		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
	    		    		sbsql.append("') AS itemized;");
	    		    		
	    		    		//System.out.println(sbsql.toString());
	    		    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
	    		    		System.out.println(result);
						}
						break;
					default:
						break;
				}
    		}
    	}
    	
    	if(null != map.get("shipper") && null != map.get("consignee") 
    			 && null != map.get("notifier")){
    		sbsql = new StringBuffer();
    		sbsql.append("SELECT f_fina_jobs_impso_ninbo('type=edi','");
    		sbsql.append("mblno="+map.get("total_list_mblno").get(0)[0].replaceAll("'", "''")+";");
    		
			//发货人数据填充
    		list =	map.get("shipper");
			str = list.get(0);
//			sbsql.append("cnorname="+str[0].replaceAll("'", "''")+";");
    		sbsql.append("cnorname="+str[1].replaceAll("'", "''")+";");
    		sbsql.append("cnoraddr="+str[2].replaceAll("'", "''")+";");
    		sbsql.append("cnorcode1="+str[3].replaceAll("'", "''")+";");
    		sbsql.append("cnortel="+str[4].replaceAll("'", "''")+";");
    		sbsql.append("cnorcode2="+str[0].replaceAll("'", "''")+";");

    		//收货人数据填充
    		list =	map.get("consignee");
			str = list.get(0);
//			sbsql.append("cneename="+str[0].replaceAll("'", "''")+";"); //代码
    		sbsql.append("cneename="+str[1].replaceAll("'", "''")+";"); //名称
    		sbsql.append("cneeaddr="+str[2].replaceAll("'", "''")+";"); //地址
    		sbsql.append("cneecode1="+str[3].replaceAll("'", "''")+";"); //国家代码
    		sbsql.append("cneetel="+str[4].replaceAll("'", "''")+";"); //联系电话
    		sbsql.append("cneecode2="+str[0].replaceAll("'", "''")+";"); //公司代码
    		sbsql.append("cneecontact="+str[6].replaceAll("'", "''")+";"); //具体联系人
//    		sbsql.append("cneetel="+str[7].replaceAll("'", "''")+";"); //具体联系人电话
    		
    		//通知人数据填充
    		list =	map.get("notifier");
    		str = list.get(0);
//			sbsql.append("notifyname="+str[0].replaceAll("'", "''")+";");
    		sbsql.append("notifyname="+str[1].replaceAll("'", "''")+";");
    		sbsql.append("notifyaddr="+str[2].replaceAll("'", "''")+";");
    		sbsql.append("notifycode1="+str[3].replaceAll("'", "''")+";");
    		sbsql.append("notifytel="+str[4].replaceAll("'", "''")+";");
    		sbsql.append("notifycode2="+str[0].replaceAll("'", "''")+";");
    		sbsql.append("jobid="+this.jobid.replaceAll("'", "''")+";");
    		sbsql.append("') AS edi;");
    		
    		//System.out.println(sbsql.toString());
    		result = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());//执行函数
    		System.out.println(result);
    	}
    	
    }
    
    
    // 获取在服务器端令存到的文件名，上传框fileUpload1另存为${java.io.tmpdir}/uploadFile1，
    // 上传框fileUpload2另存为${java.io.tmpdir}/uploadFile2。${java.io.tmpdir}是一个临时目录，
    // 在不同的操作系统和不同的应用服务器中，这个目录会有所不同，在tomcat中${java.io.tmpdir}目录为
    // ${TOMCAT_HOME}/temp
    private String getSaveToPath(FileUploadItem fileUploadItem) {
        return getSavePath(fileUploadItem.getName());
    }

    private String getSavePath(String fileName) {
        return System.getProperty("java.io.tmpdir") + "/" + fileName;
    }

    public UIFileUpload getFileUpload1() {
        return fileUpload1;
    }

    public void setFileUpload1(UIFileUpload fileUpload2) {
        this.fileUpload1 = fileUpload2;
    }

    /**
     * 可以在这里处理其它非file input的form元素
     */
    public void action() {
        ////System.out.println("You can process other non-fileInput fields here");
    }
    
    /**
     * 这里处理progress组件的状态，关于如何使用progress，请参考相关文档和rcdemos里的例子。
     * @param status
     */
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

    // 删除上次上传的文件
    private void deleteOldFiles() {
        for (int i = 1; i < 4; i++) {
            File file = new File(getSavePath("uploadFile" + i));
            if (file.exists())
                file.delete();
        }
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

    @Bind
	@SaveState
	public Integer splitNum;
	
	@Action
	public void importSplit() {
		try {
			Map map = ConfigUtils.findUserCfgVals(new String[]{"bill_ship_suffix"}, AppUtils.getUserSession().getUserid());
			String suffix =	"en";
			if(null != map && null != map.get("bill_ship_suffix")){
				suffix = map.get("bill_ship_suffix").toString();
			}
			String querySql = "SELECT f_fina_jobs_spilt('jobid="+this.jobid+";inputer="+AppUtils.getUserSession().getUsercode()+";num="+splitNum+";suffix="+suffix+"')";
			this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			this.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void cancelSplit() {
		try {
			String querySql = "DELETE FROM bus_packlist WHERE linkid = ANY(SELECT id FROM bus_ship_container " +
					"WHERE isdelete = FALSE AND jobid = ANY(SELECT id FROM fina_jobs WHERE isdelete = FALSE AND parentid = "+this.jobid+"));";
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(querySql);
			querySql = "UPDATE bus_ship_container SET isdelete = TRUE, UPDATER = '"+AppUtils.getUserSession().getUsercode()+"', UPDATETIME = NOW() " +
					"WHERE isdelete = FALSE AND jobid= ANY(SELECT id FROM fina_jobs WHERE isdelete = FALSE AND parentid = "+this.jobid+")";
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(querySql);
			querySql = "UPDATE bus_shipping SET isdelete = TRUE, UPDATER = '"+AppUtils.getUserSession().getUsercode()+"', UPDATETIME = NOW() " +
					"WHERE isdelete = FALSE AND jobid= ANY(SELECT id FROM fina_jobs WHERE isdelete = FALSE AND parentid = "+this.jobid+")";
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(querySql);
			querySql = "UPDATE fina_jobs SET isdelete = TRUE, UPDATER = '"+AppUtils.getUserSession().getUsercode()+"', UPDATETIME = NOW() " +
					"WHERE isdelete = FALSE AND id= ANY(SELECT id FROM fina_jobs WHERE isdelete = FALSE AND parentid = "+this.jobid+")";
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(querySql);
			this.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}


	@SaveState
	public String temFileUrl2;

	@Action
	public void exportlenovo() {
		this.temFileUrl2 = this.temFileUrl2;
	}

	@Bind(id = "downloadexportlenovo", attribute = "src")
	private File getDownload7() {
		if (StrUtils.isNull(temFileUrl2)) {
			return new File("");
		} else {
			return new File(temFileUrl2);
		}
	}

	//样单
	private void importShippingLenovo(File file) throws FileNotFoundException, IOException {
		try {
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));
			fileUrl.append("importJobsTemplete");
			fileUrl.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			fileUrl.append("_");
			fileUrl.append(AppUtils.getUserSession().getUsercode());
			fileUrl.append(".xlsx");
			this.temFileUrl2 = fileUrl.toString();
			File toFile = new File(fileUrl.toString());
			exportLenovoForExcel(file, toFile, new HashMap());
			System.out.println("导入联想完成!");
		} catch (Exception e) {
			System.out.println("导入联想出错!");
			MessageUtils.showException(e);
		}
	}

	public void exportLenovoForExcel(File fromFile, File toFile, Map map0) throws FileNotFoundException, IOException {
		Workbook wbo = ReadExcel.createWb(fromFile);
		Sheet sheet0 = wbo.getSheetAt(0);

		List<String> cntnoList = new ArrayList<String>();
		if (sheet0 != null) {
			for (int x = sheet0.getFirstRowNum() + 1; x <= sheet0.getLastRowNum(); x++) {
				Row row = sheet0.getRow(x);
				if (row == null || row.getLastCellNum() < 1) {
					continue;
				}
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					Cell cell = row.getCell(y);
					if (cell == null) {
						continue;
					}

					if (sheet0.getRow(0).getCell(y)!= null && cell != null) {
						String firstRowCellValue = ReadExcel.getValueFromCell(sheet0.getRow(0).getCell(y));
						String thisCellValue = ReadExcel.getValueFromCell(cell);
						if ("Equipment#".equals(firstRowCellValue) && !StrUtils.isNull(thisCellValue)) {
							cntnoList.add(thisCellValue);
						}
					}
				}
			}
		}

		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" SELECT\n" +
				"(CASE WHEN \n" +
						"\t\tEXISTS(\n" +
						"\t\t\t\t\t(SELECT 1 FROM _bus_ship_bill T  WHERE jobid = fj.id AND (SELECT String_agg(cntno,',') FROM bus_ship_container WHERE isdelete = FALSE \n" +
						"\t\t\t\t\t\t\tAND ((t.bltype = 'H' AND billid = t.id AND jobid=t.jobid) OR (t.bltype = 'M' AND billmblid = t.id AND jobid=t.jobid))) = bsc.cntno limit 1))  \n" +
						"\t\tTHEN ((SELECT hblno FROM _bus_ship_bill T  WHERE jobid = fj.id AND (SELECT String_agg(cntno,',') FROM bus_ship_container WHERE isdelete = FALSE " +
						"\t\t\t\t\t\t\tAND ((t.bltype = 'H' AND billid = t.id AND jobid=t.jobid) OR (t.bltype = 'M' AND billmblid = t.id AND jobid=t.jobid))) = bsc.cntno limit 1)) \n" +
						"\t\tELSE (bs.hblno) END\n" +
				") AS hblno"+
				// ", (bs.hblno) as hblno" +
				// ", (bs.mblno) as mblno" +
				",(bs.voyage) as voyage" +
				",(bs.vessel) as vessel" +
				",(select code from  sys_corporation  AS d WHERE iscarrier = TRUE AND isdelete = false and id = bs.carrierid) as carriercode" +
				",(bs.etd) as etd \n" +
				",(bs.etd) as etd \n" +
				",(bs.eta) as eta \n" +
				",(bs.eta) as eta \n" +
				",(bs.eta::timestamp + '-3 day') as etadecreasethree  \n" +
				",(bs.returncnttime) as returncnttime \n" +
				",(bs.polcode) as polcode \n" +
				",(bs.podcode) as podcode \n" +
				",(bs.mblno) as mblno \n" +
				",(bs.clstime) as clstime \n" +
				",(bsc.sono) as bscsono \n" +
				",(bsc.cntno) as cntno \n" +
				"FROM\n" +
				"\tfina_jobs fj\n" +
				"\tLEFT JOIN bus_shipping bs ON fj.ID = bs.jobid\n" +
				"\tLEFT JOIN bus_ship_container bsc ON fj.ID = bsc.jobid \n" +
				"\tWHERE fj.isdelete = FALSE \n" +
				"\tAND bsc.isdelete = FALSE \n" +
				"\tAND bsc.parentid IS NULL\n" +
				"\tAND EXISTS(SELECT 1 FROM sys_corporation sc WHERE sc.id = fj.customerid AND sc.isdelete = FALSE AND sc.code ilike '%LENOVO%' )" +
				"\tand bsc.cntno in ('" + org.apache.commons.lang.StringUtils.join(cntnoList.toArray(), "','") + "')");

		List<Map> mapList = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbsql.toString());
		if (mapList == null || mapList.size() == 0 || !fromFile.exists()) {
			return;
		}
		Sheet sheet = wbo.getSheetAt(0);
		if (sheet != null) {
			for (int x = sheet.getFirstRowNum() + 1; x <= sheet.getLastRowNum(); x++) {
				Row row = sheet.getRow(x);
				if (row == null || row.getLastCellNum() < 1) {
					continue;
				}
				if (row.getCell(9) == null && StrUtils.isNull(ReadExcel.getValueFromCell(row.getCell(9)).trim())) {
					continue;
				}

				String equipmentCell = ReadExcel.getValueFromCell(row.getCell(9)).trim();
				Map<String, String> cntnoMap = new HashMap<String, String>();
				for (Map thisMap : mapList) {
					if (equipmentCell.equals(thisMap.get("cntno"))) {
						cntnoMap = thisMap;
						break;
					}
				}

				for (int y = row.getFirstCellNum(); y <= 41; y++) {
					Cell cell = row.getCell(y);
					if (cell == null ) {
						cell = row.createCell(y);
					}

					if (y == 6) {
						Cell editCell = row.createCell(y);
						String keyWord = "hblno";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					}
					if (y == 24) {
						Cell editCell = row.createCell(y);
						String keyWord = "voyage";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 25) {
						Cell editCell = row.createCell(y);
						String keyWord = "vessel";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 26) {
						Cell editCell = row.createCell(y);
						String keyWord = "carriercode";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 27) {
						Cell editCell = row.createCell(y);
						String keyWord = "clstime";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 28) {
						Cell editCell = row.createCell(y);
						String keyWord = "etd";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 29) {
						Cell editCell = row.createCell(y);
						String keyWord = "etd";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 30) {
						Cell editCell = row.createCell(y);
						String keyWord = "eta";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 31) {
						Cell editCell = row.createCell(y);
						String keyWord = "eta";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 32) {
						Cell editCell = row.createCell(y);
						String keyWord = "etadecreasethree";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 33) {
						Cell editCell = row.createCell(y);
						String keyWord = "returncnttime";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 38) {
						Cell editCell = row.createCell(y);
						String keyWord = "polcode";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 39) {
						Cell editCell = row.createCell(y);
						String keyWord = "podcode";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 40) {
						Cell editCell = row.createCell(y);
						String keyWord = "mblno";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					} else if (y == 41) {
						Cell editCell = row.createCell(y);
						String keyWord = "bscsono";
						if (cntnoMap.containsKey(keyWord) && cntnoMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(cntnoMap.get(keyWord)));
						}
					}
				}
			}
		} else {
			return;
		}

		if (!toFile.exists()) {
			toFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(toFile);
		wbo.write(fos);
		fos.close();
	}

	//样单India
	@SaveState
	public String temFileUrl3;

	@Action
	public void exportlenovoIndia() {
		this.temFileUrl3 = this.temFileUrl3;
	}

	@Bind(id = "downloadexportlenovoIndia", attribute = "src")
	private File getDownload8() {
		if (StrUtils.isNull(temFileUrl3)) {
			return new File("");
		} else {
			return new File(temFileUrl3);
		}
	}

	private void importShippingLenovoIndia(File file) throws FileNotFoundException, IOException {
		try {
			StringBuffer fileUrl = new StringBuffer();
			fileUrl.append(System.getProperty("java.io.tmpdir"));
			fileUrl.append("importJobsTemplete");
			fileUrl.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			fileUrl.append("_");
			fileUrl.append(AppUtils.getUserSession().getUsercode());
			fileUrl.append(".xlsx");
			this.temFileUrl3 = fileUrl.toString();
			File toFile = new File(fileUrl.toString());
			exportLenovoForExcelIndia(file, toFile, new HashMap());
			System.out.println("导入联想india完成!");
		} catch (Exception e) {
			System.out.println("导入联想india出错!");
			MessageUtils.showException(e);
		}
	}

	public void exportLenovoForExcelIndia(File fromFile, File toFile, Map map0) throws FileNotFoundException, IOException {
		Workbook wbo = ReadExcel.createWb(fromFile);
		Sheet sheet = wbo.getSheetAt(0);
		if (sheet != null) {
			for (int x = 4; x <= sheet.getLastRowNum(); x++) {
				Row row = sheet.getRow(x);
				if (row == null || row.getLastCellNum() < 1) {
					continue;
				}
				if (row.getCell(2) == null) {
					continue;
				}


				String busShipBillNo = ReadExcel.getValueFromCell(row.getCell(2)).trim();
				if (StrUtils.isNull(busShipBillNo)) {
					continue;
				}

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" SELECT\n" +

						"(bsb.pol) as pol \n" +		//起运港
						",(bsb.cnortitle) as cnortitle \n" +		//发货人
						",(bsb.cneetitle) as cneetitle \n" +		//收货人
						",(bsb.ponumber) as invoicenumber \n" +		//invoicenumber

						",(SELECT namec FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 80 and d.code=fj.tradeway order by code) as tradewaynamec \n" +
						",(bsb.pod) as pod \n" +	//目的港
						",(bsb.piece) as piece \n" +	//件数
						",(bsb.grswgt) as grswgt \n" +	//毛重
						",(bs.voyage) as voyage \n" +	//船名
						",(bs.vessel) as vessel \n" +	//航次
						",(bs.mblno) as mblno \n" +		//MBL提单号
						",(SELECT String_agg(cntno,',') FROM bus_ship_container WHERE isdelete = FALSE AND ((bsb.bltype = 'H' AND billid = bsb.id AND jobid=bsb.jobid) OR " +
						"	(bsb.bltype = 'M' AND billmblid = bsb.id AND jobid= bsb.jobid))) as cntno \n" +		//柜号
						",((SELECT code FROM sys_corporation WHERE id =  bs.carrierid)) as carrier \n" +

						",(CASE WHEN ((SELECT count(1) FROM bus_ship_bill bsb2 WHERE fj.id = bsb2.jobid  AND bsb2.isdelete = FALSE) = 1)" +
						"		THEN (CASE WHEN fj.ldtype = 'F' THEN (SELECT count(*) ::text FROM bus_ship_container WHERE isdelete = false and parentid is null AND jobid = fj.id) ELSE 0 ::text END)" +
						" 	ELSE (SPLIT_PART((SELECT f_bus_ship_bill_cntdesc('billid='||bsb.id)), 'x', 1)) END" +
						") AS cntypenumber \n" +

						",(SELECT f_bus_ship_bill_cntdesc('billid='||bsb.id)) AS cntypecode \n" +

						",(bs.orderdate) as orderdate \n" +	//订单日期
						",(bs.sodate) as sodate \n" +	//so日期
						",(SELECT goodsokdate FROM bus_truck WHERE isdelete = FALSE AND jobid = fj.id  limit 1) as goodsokdate \n" +	//货好日期
						",(bs.cabinetdate) as cabinetdate \n" +	//装柜日期

						",(bs.etd) as etd \n" +
						",(CASE WHEN (bs.etd is not null and bs.atd is null) THEN bs.etd ELSE bs.atd END) as atd  \n" +
						",(bs.eta) as eta \n" +
						",(bs.ata) as ata \n" +



						"FROM\n" +
						"\tfina_jobs fj\n" +
						"\tLEFT JOIN bus_shipping bs ON fj.ID = bs.jobid\n" +
						"\tLEFT JOIN bus_ship_bill bsb ON fj.ID = bsb.jobid \n" +
						"\tWHERE fj.isdelete = FALSE \n" +
						"\tAND bsb.isdelete = FALSE \n" +
						"\tAND EXISTS(SELECT 1 FROM sys_corporation sc WHERE sc.id = fj.customerid AND sc.isdelete = FALSE AND sc.code ilike '%LENOVO%' )" +
						"\tand bsb.hblno = ('" + busShipBillNo + "')");

				Map<String, String> dataMap = new HashMap<String, String>();
				try {
					dataMap  = serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
				} catch (Exception e) {
					continue;
				}


				for (int y = 0; y <= 31; y++) {
					Cell cell = row.getCell(y);
					if (cell == null ) {
						cell = row.createCell(y);
					}

					if (y == 3) {
						Cell editCell = row.createCell(y);
						String keyWord = "pol";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 5) {
						Cell editCell = row.createCell(y);
						String keyWord = "cnortitle";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 6) {
						Cell editCell = row.createCell(y);
						String keyWord = "cneetitle";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 7) {
						Cell editCell = row.createCell(y);
						String keyWord = "invoicenumber";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 8) {
						Cell editCell = row.createCell(y);
						String keyWord = "tradewaynamec";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 9) {
						Cell editCell = row.createCell(y);
						String keyWord = "pod";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 10) {
						Cell editCell = row.createCell(y);
						String keyWord = "piece";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 11) {
						Cell editCell = row.createCell(y);
						String keyWord = "grswgt";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 12) {
						Cell editCell = row.createCell(y);
						String keyWord = "vessel";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 13) {
						Cell editCell = row.createCell(y);
						String keyWord = "voyage";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 15) {
						Cell editCell = row.createCell(y);
						String keyWord = "mblno";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 16) {
						Cell editCell = row.createCell(y);
						String keyWord = "cntno";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 17) {
						Cell editCell = row.createCell(y);
						String keyWord = "carrier";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 18) {
						Cell editCell = row.createCell(y);
						String keyWord = "cntypenumber";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 19) {
						Cell editCell = row.createCell(y);
						String keyWord = "cntypecode";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 20) {
						Cell editCell = row.createCell(y);
						String keyWord = "orderdate";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 21) {
						Cell editCell = row.createCell(y);
						String keyWord = "sodate";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 22) {
						Cell editCell = row.createCell(y);
						String keyWord = "goodsokdate";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 23) {
						Cell editCell = row.createCell(y);
						String keyWord = "cabinetdate";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 24) {
						Cell editCell = row.createCell(y);
						String keyWord = "etd";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 25) {
						Cell editCell = row.createCell(y);
						String keyWord = "atd";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 26) {
						Cell editCell = row.createCell(y);
						String keyWord = "eta";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
					if (y == 27) {
						Cell editCell = row.createCell(y);
						String keyWord = "ata";
						if (dataMap.containsKey(keyWord) && dataMap.get(keyWord) != null) {
							editCell.setCellValue(String.valueOf(dataMap.get(keyWord)));
						}
					}
				}
			}
		} else {
			return;
		}

		if (!toFile.exists()) {
			toFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(toFile);
		wbo.write(fos);
		fos.close();
	}

}

