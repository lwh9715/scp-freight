package com.scp.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysUser;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.HtmlToPdfInterceptor;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.db.SqlObject;

public class RogTempleteServerHandler {
	
	private final String modhbl = "shippinghbl";
	private final String modmbl = "shippingmbl";
	private final String modair = "air";
	private final String modbook = "shippingbook";
	private final String modairmbl = "airmbl";
	
	private final String trainhbl = "trainhbl";
	private final String trainmbl = "trainmbl";
	private final String trainbook = "trainbook";
	
	
	
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request,HttpServletResponse response){
		String rpStr = "";//ROG文件名
		try {
			rpStr = URLDecoder.decode(request.getParameter("rp"), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			if("toRogtemplete".equals(action)) {
				result = this.toTemplete(request,rpStr,response);
			}else if("toRogtempleteair".equals(action)) {
				result = this.toTempleteforair(request,rpStr,response);
			}else if("toRogtempletembl".equals(action)) {
				result = this.toTempleteformbl(request,rpStr,response);
			}else if("saveRogtemplete".equals(action)) {
				result = this.saveTemplete(request,rpStr,response);
			}else if("saveRogtempleteair".equals(action)) {
				result = this.saveTempleteforair(request,rpStr,response);
			}else if("saveRogtempletembl".equals(action)) {
				result = this.saveTempleteformbl(request,rpStr,response);
			}else if("getViewData".equals(action)) {
				result = this.getViewData(request,rpStr,response);
			}else if("getViewDataair".equals(action)) {
				result = this.getViewDataAir(request,rpStr,response);
			}else if("getViewDatambl".equals(action)) {
				result = this.getViewDatambl(request,rpStr,response);
			}else if("checktempletename".equals(action)) {
				result = this.checkAndSaveTempletenameForPrivate(request,rpStr,response);
			}else if("checktempletenamembl".equals(action)) {
				result = this.checkAndSaveTempletenameForPrivatembl(request,rpStr,response);
			}else if("checktempletenameair".equals(action)) {
				result = this.checkAndSaveTempletenameForPrivateair(request,rpStr,response);
			}else if("reverseSaveData".equals(action)){
				result = this.reverseSaveData(request,rpStr,response);
//			}else if("toRogtempletembl2PDF".equals(action)){
//				result = this.toRogtemplete2PDF(request,rpStr,response,"MBL");
//			}else if("toRogtempletehbl2PDF".equals(action)){
//				result = this.toRogtemplete2PDF(request,rpStr,response,"HBL");
			}else if("toRogtempletehbl2PDFfile".equals(action)){
				result = this.toRogtemplete2PDFfile(request,rpStr,response,"HBL");
			}else if("hiprintsave".equals(action)){
				result = this.hiprintSave(request,rpStr,response);
			}else if("getHiprintjson".equals(action)){
				result = this.getHiprintjson(request,rpStr,response);
			}else if("hiprintsaveas".equals(action)){
				result = this.hiprintsaveas(request,rpStr,response);
			}else if("getPrintData2".equals(action)){
				result = this.getPrintData2(request,rpStr,response);
			}else if("toRogtempletetrain".equals(action)) {
				result = this.toTempletefortrain(request,rpStr,response);
			}else if("saveRogtempletetrain".equals(action)) {
				result = this.saveTempletetrain(request,rpStr,response);
			}else if("checktempletenametrain".equals(action)) {
				result = this.checkAndSaveTempletenameForPrivatetrain(request,rpStr,response);
			}else if("getViewDatatrain".equals(action)) {
				result = this.getViewDatatrain(request,rpStr,response);
			}else if("toRogtempletetrainmbl".equals(action)) {
				result = this.toTempletefortrainmbl(request,rpStr,response);
			}else if("saveRogtempletetrainmbl".equals(action)) {
				result = this.saveTempletefortrainmbl(request,rpStr,response);
			}else if("checktempletenametrainmbl".equals(action)) {
				result = this.checkAndSaveTempletenameForPrivatetrainmbl(request,rpStr,response);
			}else if("getViewDatatrainmbl".equals(action)) {
				result = this.getViewDatatrainmbl(request,rpStr,response);
			}else if("getViewDataairnew".equals(action)) {
				result = this.getViewDataAirNew(request,rpStr,response);
			}else if("reverseSaveDataair".equals(action)){
				result = this.reverseSaveDataair(request,rpStr,response);
			}else if("saveDataAirEdit".equals(action)){
				result = this.saveDataAirEdit(request,rpStr,response);
			}
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}
	
	private String getHiprintjson(HttpServletRequest request,String rpStr, HttpServletResponse response) {
		String reporttype = request.getParameter("reporttype");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT templete FROM sys_report");
		sb.append("\nWHERE filename = '"+rpStr+"'");
		sb.append("\nAND modcode='"+reporttype+"';");
		try{
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map!=null&&map.size()>0){
				return map.get("templete").toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"success\":false}";
		}
		return "{\"success\":false}";
	}
	
	private String getPrintData2(HttpServletRequest request,String rpStr, HttpServletResponse response) {
		String reporttype = request.getParameter("reporttype");
		String corpidtitile = request.getParameter("corpidtitile");
		String billid = request.getParameter("billid");
		String userid = request.getParameter("userid");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT row_to_json(Q) AS json FROM(");
		sb.append("\n SELECT ");
		sb.append("\n   b.*");
		sb.append("\n  ,u.code AS usercode ");
		sb.append("\n ,p.vessel AS vessel");
		sb.append("\n  ,p.voyage AS voyage");
		sb.append("\n  ,COALESCE(p.vessel,'')||COALESCE(p.voyage,'') AS vevs");
		sb.append("\n  ,p.pol AS pol");
		sb.append("\n  ,p.pod AS pod");
		sb.append("\n  ,p.pdd AS pdd");
		sb.append("\n  ,p.etd AS etd");
		sb.append("\n  ,s.namec");
		sb.append("\n  ,s.namee");
		sb.append("\n  ,s.addresse");
		sb.append("\n  ,s.tel1");
		sb.append("\n  ,s.fax1");
		sb.append("\n  , (SELECT namec FROM sys_corporation WHERE code = 'DB')AS corpnamec");
		sb.append("\n  , (SELECT namee FROM sys_corporation WHERE code = 'DB')AS crrpnamee");
		sb.append("\n  , (SELECT addresse FROM sys_corporation WHERE code = 'DB')AS corpaddresse");
		sb.append("\n  , (SELECT tel1 FROM sys_corporation WHERE code = 'DB')AS corptel");
		sb.append("\n  , (SELECT fax1 FROM sys_corporation WHERE code = 'DB')AS corpfax");
		sb.append("\n  , (SELECT tel1 FROM sys_corporation WHERE id = b.clientid)AS custel");
		sb.append("\n  , (SELECT fax1 FROM sys_corporation WHERE id = b.clientid)AS cusfax");
		sb.append("\n  , (SELECT contact FROM sys_corporation WHERE id = b.clientid)AS cuscontact");
		sb.append("\n  , to_char(NOW(),'MON.DD,yyyy') AS billdate");
		sb.append("\n  , (SELECT o.namec FROM sys_corporation o WHERE p.carrierid = o.id AND p.isdelete = FALSE AND o.isdelete = FALSE )AS carrier  ");
		sb.append("\n  , (SELECT nos FROM fina_jobs WHERE b.jobid = id AND isdelete = FALSE) AS jobnos");
		sb.append("\n  , (SELECT f_lists(sono)FROM bus_ship_container WHERE jobid = b.jobid AND sono IS NOT NULL AND sono <> '' AND isdelete = FALSE AND parentid IS NULL)AS bookno");
		sb.append("\n  ,(SELECT CASE WHEN (SELECT count(x.id) FROM fina_billcntdtl x WHERE x.billid = b.id) > 0 THEN (SELECT f_lists(y.cntno) FROM bus_ship_container y WHERE y.cntno IS NOT NULL AND y.cntno <> '' AND isdelete = FALSE AND EXISTS(SELECT 1 FROM fina_billcntdtl xx WHERE xx.cntid = y.id AND xx.billid = b.id)) ELSE (SELECT f_lists(y.cntno)FROM bus_ship_container y WHERE y.jobid = b.jobid AND y.cntno IS NOT NULL AND y.cntno <> '' AND y.isdelete = FALSE AND y.parentid IS NULL) END) AS cntno");
		sb.append("\n  ,(SELECT CASE WHEN (SELECT count(x.id) FROM fina_billcntdtl x WHERE x.billid = b.id) > 0 THEN (SELECT f_lists(t.code) FROM bus_ship_container r,dat_cntype t WHERE r.jobid = b.jobid AND r.cntypeid = t.id AND t.isdelete =FALSE AND r.isdelete =FALSE AND t.code IS NOT NULL AND t.code <> '' AND EXISTS(SELECT 1 FROM fina_billcntdtl xx WHERE xx.cntid = r.id AND xx.billid = b.id)) ELSE (SELECT f_lists(t.code) FROM bus_ship_container r,dat_cntype t WHERE r.jobid = b.jobid AND r.cntypeid = t.id AND t.isdelete =FALSE AND r.isdelete =FALSE AND t.code IS NOT NULL AND t.code <> '' AND r.parentid IS NULL) END)AS cnttype");
		sb.append("\n  , (SELECT f_lists(hblno)FROM bus_ship_bill WHERE jobid = b.jobid AND hblno IS NOT NULL AND hblno <> '')AS hblno");
		sb.append("\n  , (SELECT c.accountnoe FROM _dat_account c WHERE b.accountid = c.id AND c.isdelete = FALSE)AS accountnamec");
		sb.append("\n  , (SELECT c.accountno FROM _dat_account c WHERE b.accountid = c.id AND c.isdelete = FALSE)AS accountnamee");
		sb.append("\n  , (SELECT c.bankcode FROM _dat_account c WHERE b.accountid = c.id AND c.isdelete = FALSE)AS banknamec");
		sb.append("\n  , (SELECT c.banknamec FROM _dat_account c WHERE b.accountid = c.id AND c.isdelete = FALSE)AS banknamee");
		sb.append("\n  , (SELECT f_bus_shipping_cntdesc('shipid='||p.id||';billid='||b.id))AS cntcount");
		sb.append("\n  , (SELECT f_fina_bill_getcurrency('billid='||b.id))AS currencydesc");
		sb.append("\n  , f_fina_jobs_cntdesc('jobid='||b.jobid) AS cntdesc");
		sb.append("\n  , (SELECT TRIM(LEADING chr(13)||chr(10) FROM replace(f_fina_bill_getcurrency('billid='||b.id),'  ',f_newline())))AS currencydesc1");
		sb.append("\n  , (select capitalen FROM fina_bill where id = b.id) as capitalen");
		sb.append("\n  , (SELECT namee FROM sys_user x WHERE x.isdelete = FALSE AND u.id = x.id) AS username");
		sb.append("\n  ,COALESCE((SELECT namec FROM sys_corporation WHERE isdelete = FALSE AND CAST(id AS VARCHAR) = '"+corpidtitile+"' ),'') AS namec2");
		sb.append("\n  ,(SELECT namee FROM sys_corporation WHERE isdelete = FALSE AND CAST(id AS VARCHAR) = '"+corpidtitile+"' ) AS namee2");
		sb.append("\n  ,(SELECT addresse FROM sys_corporation WHERE isdelete = FALSE AND CAST(id AS VARCHAR) = '100') AS addresse2");
		sb.append("\n  ,(SELECT COALESCE(tel1,'') FROM sys_corporation WHERE isdelete = FALSE AND CAST(id AS VARCHAR) = '"+corpidtitile+"' ) AS tel12");
		sb.append("\n  ,(SELECT COALESCE(fax1,'') FROM sys_corporation WHERE isdelete = FALSE AND CAST(id AS VARCHAR) = '"+corpidtitile+"' ) AS fax12");
		sb.append("\n	,(SELECT array_to_json (ARRAY_AGG(row_to_json(t))) FROM (SELECT feeitemdec,araptype AS araptypedesc,currency AS  billcurrency,id AS arapid,piece,price");
		sb.append("\n					,(CASE WHEN araptype='AP' THEN a.amount*-1 ELSE a.amount END)AS aamount");
		sb.append("\n					, CAST(a.billamountflag AS numeric(18,3))AS billamountflag");
		sb.append("\n		FROM _fina_arap a WHERE a.billid = b.id AND a.parentid is NULL AND a.isdelete = FALSE");
		sb.append("\n		AND NOT EXISTS (SELECT 1 FROM dat_feeitem x WHERE x.id = a.feeitemid AND UPPER(x.code) = 'TY')) t) AS araprows");
		sb.append("\nFROM _fina_bill b ,_rpt_sys_corporation s ,sys_user u ,bus_shipping p");
		sb.append("\nWHERE  CAST(b.id AS VARCHAR) = '"+billid+"'");
		sb.append("\n  AND CAST(u.id AS VARCHAR) = '"+userid+"'");
		sb.append("\n  AND u.corpid = s.id");
		sb.append("\n  AND p.jobid = b.jobid");
		sb.append("\n  AND b.isdelete = FALSE");
		sb.append("\n  AND s.isdelete = FALSE");
		sb.append("\n  AND u.isdelete = FALSE");
		sb.append("\n  AND p.isdelete = FALSE) Q;");
		try{
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map!=null&&map.size()>0){
				return map.get("json").toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"success\":false}";
		}
		return "{\"success\":false}";
	}
	
	private String hiprintSave(HttpServletRequest request,String rpStr, HttpServletResponse response) {
		String json = request.getParameter("json");
		String reporttype = request.getParameter("reporttype");
		String billid = request.getParameter("billid");
		String usercode = "";
		try {
			String userid = request.getParameter("userid");
			SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid));
			usercode = user.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rpStr != null && !rpStr.isEmpty()&&json != null && !json.isEmpty()){
			json = json.replace("'", "''");
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("UPDATE sys_report SET templete='");
			sbsql.append(json);
			sbsql.append("' ,updater = '"+usercode+"'");
			sbsql.append(" ,updatetime = now()");
			sbsql.append(" WHERE filename='");
			sbsql.append(rpStr);
			sbsql.append("' AND modcode='"+reporttype+"';");
			try {
				daoIbatisTemplate.updateWithUserDefineSql(sbsql.toString());
			} catch (Exception e) {
				String returns = MessageUtils.returnExceptionForservlet(e);
				return returns;
			}
			return "{\"success\":true}";
		}
		return "{\"success\":false}";
	}
	
	private String hiprintsaveas(HttpServletRequest request,String rpStr, HttpServletResponse response) {
		String json = request.getParameter("json");
		String reporttype = request.getParameter("reporttype");
		String pass = request.getParameter("pass");
		return "{\"success\":true}";
	}
	
//	private String toRogtemplete2PDF(HttpServletRequest request,
//			String rpStr, HttpServletResponse response,String billType) {
//		
//		String srcPath = (request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/");
//		
//		String publicUrl = ConfigUtils.findSysCfgVal("sys_public_url");
//		if(!StrUtils.isNull(publicUrl)){
//			srcPath = publicUrl;
//		}
//		
//		
//		CommonService commonService = (CommonService)AppUtils.getBeanFromSpringIoc("commonService");
//		
//		if("MBL".equals(billType)){
//			srcPath += "/scp/reportEdit/file/printmbl.jsp?rp="+rpStr+"&b="+request.getParameter("b")+"&html2pdf=true";
//		}else{
//			srcPath += "/scp/reportEdit/file/print.jsp?rp="+rpStr+"&b="+request.getParameter("b")+"&html2pdf=true&u=0";
//		}
//		//System.out.println(srcPath);
//		//String srcPath = "http://192.168.0.188:8888/scp/reportEdit/file/printmbl.jsp?rp=MBL.raq&b=1720052176";
//        String destPath = request.getParameter("b");
//        String url = "";
//		try {
//			url = commonService.html2Pdf(srcPath, destPath);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return url;
//	}
	
	private String toRogtemplete2PDFfile(HttpServletRequest request,
			String rpStr, HttpServletResponse response,String billType) {
		
		String srcPath = (request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/");
		
		String publicUrl = ConfigUtils.findSysCfgVal("sys_public_url");
		if(!StrUtils.isNull(publicUrl)){
			srcPath = publicUrl;
		}
		
//		CommonService commonService = (CommonService)AppUtils.getBeanFromSpringIoc("commonService");

//		srcPath+="/scp/reportEdit/file/printFile.jsp";
        String destPath = request.getParameter("b");
        String url = "";
        String html = "<html>"+request.getParameter("html")+"</html>";
        html = html.replace("<span>打印</span>", "").replace("<span>正本</span>", "").replace("<span>PDF</span>","").replace("<span>调整</span>","");
        try{
	        String path = AppUtils.getHblReportFilePath() + File.separator+"file"+ File.separator+"Template"+destPath+".html";
	        srcPath+="/scp/reportEdit/file/"+"Template"+destPath+".html";
	        File txt=new File(path);
	        if(txt.exists()){  
	        	//System.out.println("path:"+path);
	        	txt.delete();
	        }
	        if(!txt.exists()){  
	            try {
					txt.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}  
	        }
			byte bytes[];
	        bytes=html.getBytes();  
	        int b=bytes.length;
	    	FileOutputStream fos=new FileOutputStream(txt); 
			fos.write(bytes);
			fos.flush();
	        fos.close();
	        String html2pdfURL = ConfigUtils.findSysCfgVal("sys_print_html2pdf_URL");
	        String html2pdfArgs = ConfigUtils.findSysCfgVal("sys_print_html2pdf_args");
	        if(StrUtils.isNull(html2pdfArgs))html2pdfArgs = "--dpi 600 --encoding 'utf-8'";
	        //2539 系统设置增加 html2pdf URL 如果上面系统设置里面有值，则不做远程调用，本地调用转的软件，生成pdf文件到本地 webapps / ROOT 目录下 ，返回文件的http url 
	        if(StrUtils.isNull(html2pdfURL)){
				//url = commonService.html2Pdf(srcPath, destPath);
        	}else{
        		//System.out.println("htmlToPdf:html2pdfURL:"+html2pdfURL);
        		
        		String destPathv = AppUtils.getWebApplicationPath() + File.separator + "webapps"+ File.separator +"ROOT"+ File.separator +destPath+".pdf";
        		
        		srcPath = html2pdfURL + "/scp/reportEdit/file/"+"Template"+destPath+".html";
        		
        		File file = new File(destPathv);
                File parent = file.getParentFile();
                //如果pdf保存路径不存在，则创建路径
                if(!parent.exists()){
                    parent.mkdirs();
                }
                StringBuilder cmd = new StringBuilder();
        		if(System.getProperty("os.name").indexOf("Windows") == -1){
        			cmd.append("/usr/src/wkhtmltox/bin/wkhtmltopdf");
        		}else{
        			cmd.append("\"C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe\"");
        		}
        		cmd.append(" ");
        		cmd.append(html2pdfArgs);
        		cmd.append(" ");
        		cmd.append(srcPath);
        		cmd.append(" ");
        		cmd.append(destPathv);
        		//System.out.println("htmlToPdf:srcPath:"+srcPath);
        		//System.out.println("htmlToPdf:filePath:"+destPathv);
                //System.out.println("htmlToPdf:cmd:"+cmd.toString());
                try{
                    Process proc = Runtime.getRuntime().exec(cmd.toString());
                    HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
                    HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
                    error.start();
                    output.start();
                    proc.waitFor();
//                    url = destPath.replaceAll("\\\\","\\\\\\\\");
                    InetAddress ia = InetAddress.getLocalHost();
        			String host = ia.getHostName();//获取计算机主机名 
                    String IP= ia.getHostAddress();//获取计算机IP 
        			url = html2pdfURL+"/"+destPath+".pdf";
                }catch(Exception e){
                    e.printStackTrace();
                }
        	}
			if(url.length()>0){
				txt.delete();
			}
			url = "{\"results\":\""+url + "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 读取模版JSON
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String toTemplete(HttpServletRequest request,String rpStr,HttpServletResponse response){
		if(rpStr != null && !rpStr.isEmpty()){
			String reporttype = request.getParameter("reporttype");
			StringBuffer sbsql = new StringBuffer();
			try {
				rpStr = URLDecoder.decode(rpStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("book")){//book
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='shippingbook';");
			}else if(reporttype.equals("shippingsingle")){
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='shippingsingle';");
			}else if(reporttype.equals("shippingorder")){
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='shippingorder';");
			}else{//hbl
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='shippinghbl';");
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			if(map != null && map.size() > 0 && map.containsKey("templete")){
				Object obj = map.get("templete");
				return obj == null ? "''" : obj.toString();
			}
		}
		return "''";
	}
	/**
	 * 读取模版JSON AIR
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String toTempleteforair(HttpServletRequest request,String rpStr,HttpServletResponse response){
		if(rpStr != null && !rpStr.isEmpty()){
			String reporttype = request.getParameter("reporttype");
			StringBuffer sbsql = new StringBuffer();
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("airmbl")){//mbl
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='airmbl';");
			}else if(!StrUtils.isNull(reporttype)&&reporttype.equals("airmodel")){
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='airmodel';");
			}
			else{//hbl
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='air';");
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			if(map != null && map.size() > 0 && map.containsKey("templete")){
				Object obj = map.get("templete");
				return obj == null ? "''" : obj.toString();
			}
		}
		return "''";
	}
	
	/**
	 * 读取模版JSON MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String toTempleteformbl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		if(rpStr != null && !rpStr.isEmpty()){
			String reporttype = request.getParameter("reporttype");
			StringBuffer sbsql = new StringBuffer();
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("book")){//book
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='shippingbook';");
			}else{//mbl
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='shippingmbl';");
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			if(map != null && map.size() > 0 && map.containsKey("templete")){
				Object obj = map.get("templete");
				return obj == null ? "''" : obj.toString();
			}
		}
		return "''";
	}
	
	/**
	 * 保存模版JSON
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String saveTemplete(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String json = request.getParameter("json");
		String reporttype = request.getParameter("reporttype");
		String usercode = "";
		try {
			String userid = request.getParameter("userid");
			SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid));
			usercode = user.getCode();
		} catch (Exception e) {
		}
		if(rpStr != null && !rpStr.isEmpty()&&json != null && !json.isEmpty()){
			json = json.replace("'", "''");
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("UPDATE sys_report SET templete='");
			sbsql.append(json);
			sbsql.append("' ,updater = '"+usercode+"'");
			sbsql.append(" ,updatetime = now()");
			sbsql.append(" WHERE filename='");
			sbsql.append(rpStr);
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("book")){//book
				sbsql.append("' AND modcode='shippingbook';");
			}else if(reporttype.equals("shippingsingle")){
				sbsql.append("' AND modcode='shippingsingle';");
			}else if(reporttype.equals("shippingorder")){
				sbsql.append("' AND modcode='shippingorder';");
			}else{//hbl
				sbsql.append("' AND modcode='shippinghbl';");
			}
			try {
				daoIbatisTemplate.updateWithUserDefineSql(sbsql.toString());
			} catch (Exception e) {
				String returns = MessageUtils.returnExceptionForservlet(e);
				return returns;
			}
			return "SUCCESS";
		}
		return "ERROR";
	}
	/**
	 * 保存模版JSON AIR
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String saveTempleteforair(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String json = request.getParameter("json");
		String reporttype = request.getParameter("reporttype");
		String usercode = "";
		try {
			String userid = request.getParameter("userid");
			SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid));
			usercode = user.getCode();
		} catch (Exception e) {
		}
		if(rpStr != null && !rpStr.isEmpty()&&json != null && !json.isEmpty()){
			json = json.replace("'", "''");
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("UPDATE sys_report SET templete='");
			sbsql.append(json);
			sbsql.append("' ,updater = '"+usercode+"'");
			sbsql.append(" ,updatetime = now()");
			sbsql.append(" WHERE filename='");
			sbsql.append(rpStr);
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("airmbl")){//airmbl
				sbsql.append("' AND modcode='airmbl';");
			}else if(!StrUtils.isNull(reporttype)&&reporttype.equals("airmodel")){
				sbsql.append("' AND modcode='airmodel';");
			}else{//airhbl
				sbsql.append("' AND modcode='air';");
			}
			try {
				daoIbatisTemplate.updateWithUserDefineSql(sbsql.toString());
			} catch (Exception e) {
				String returns = MessageUtils.returnExceptionForservlet(e);
				return returns;
			}
			return "SUCCESS";
		}
		return "ERROR";
	}
	
	/**
	 * 保存模版JSON MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String saveTempleteformbl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String json = request.getParameter("json");
		String usercode = "";
		String reporttype = request.getParameter("reporttype");
		try {
			String userid = request.getParameter("userid");
			SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid));
			usercode = user.getCode();
		} catch (Exception e) {
		}
		if(rpStr != null && !rpStr.isEmpty()&&json != null && !json.isEmpty()){
			json = json.replace("'", "''");
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("UPDATE sys_report SET templete='");
			sbsql.append(json);
			sbsql.append("' ,updater = '"+usercode+"'");
			sbsql.append(" ,updatetime = now()");
			sbsql.append(" WHERE filename='");
			sbsql.append(rpStr);
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("book")){//book
				sbsql.append("' AND modcode='shippingbook';");
			}else{//mbl
				sbsql.append("' AND modcode='shippingmbl';");
			}
			try {
				daoIbatisTemplate.updateWithUserDefineSql(sbsql.toString());
			} catch (Exception e) {
				String returns = MessageUtils.returnExceptionForservlet(e);
				return returns;
			}
			return "SUCCESS";
		}
		return "ERROR";
	}
	
	/**
	 * 根据billid获取打印数据
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String getViewData(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String billid = request.getParameter("b");
		String u = request.getParameter("u");
		String nodetail = request.getParameter("nodetail");
		
		u = StrUtils.getSqlFormat(u);
		u = StrUtils.isNull(u) ? "-1" : AppUtils.base64Decoder(u);
		Long uid = -1L;
		try {
			uid = Long.parseLong(u);
		} catch (NumberFormatException e) {
		}
		
		//查个人设置是否显示船公司简称
		String profitsful = "carrierfullname";
		String profits = "carriername";

		String s = request.getParameter("u");
		String userid = AppUtils.base64Decoder(StrUtils.getSqlFormat(request.getParameter("u")));
		
		Map m = ConfigUtils.findUserCfgVals(new String[]{"bill_ship_abbrs"}, Long.valueOf(userid));
		if(m != null && m.size() > 0){
			if(m.get("bill_ship_abbrs")!=null){
				if(m.get("bill_ship_abbrs").toString().equals("t")){
					profits = "carriername";
					profitsful = "carriername";
				}
				
			}
		}
		
		if(billid != null && !billid.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
			sb.append("\n FROM (SELECT ");
			if(nodetail != null && "true".equals(nodetail)){
				sb.append("\n 		(SELECT REPLACE(REPLACE(REPLACE( f_bus_shipping_goodsinfo_attached('billid='||"+ billid +"||';'), CHR(10), '</br>'), '\\cx', '</br>'), '\\r', '</br>')) AS goodsinfo,");
				sb.append("\n 			'' AS cntinfos,");
				sb.append("\n 			'' AS cntdesc,");
				sb.append("\n 			'' AS markhead,");
				sb.append("\n 			'' AS packer,");
				sb.append("\n 			'' AS piece,");
				sb.append("\n 			id,hblno,billcount,shipid,jobid,cneeid,cneetitle,cneename,cnorid,cnortitle,cnorname,notifyid,notifytitle,notifyname,agenid,agentitle,cnortitlembl,cneetitlembl,notifytitlembl,agentitlembl,agenname,pretrans,poa,hbltype,carrierid,vessel,voyage,polid,pol,podid,pod,pddid,pdd,atd,etd,eta,grswgt,cbm,carryitem,freightitem,loaditem,marksno,goodsdesc,totledesc,corpid,isdelete,inputer,inputtime,updater,updatetime,mblno,bltype,destination,signplace,freightitemdesc,paymentitemdesc,shippingdate,"+profitsful+" AS carrierfullname,piececount,isshowship,mblsono,hblsono,plable,vesselvoyage,atdTitle,dono,piecepacker,remark,dotype,paymentitem,"+profits+" AS carriername");
			}else{
				sb.append("\n 			goodsinfo,cntinfos,cntdesc,markhead,packer,piece,");
				sb.append("\n 			id,hblno,billcount,shipid,jobid,cneeid,cneetitle,cneename,cnorid,cnortitle,cnorname,notifyid,notifytitle,notifyname,agenid,agentitle,cnortitlembl,cneetitlembl,notifytitlembl,agentitlembl,agenname,pretrans,poa,hbltype,carrierid,vessel,voyage,polid,pol,podid,pod,pddid,pdd,atd,etd,eta,grswgt,cbm,carryitem,freightitem,loaditem,marksno,goodsdesc,totledesc,corpid,isdelete,inputer,inputtime,updater,updatetime,mblno,bltype,destination,signplace,freightitemdesc,paymentitemdesc,shippingdate,"+profitsful+" AS carrierfullname,piececount,isshowship,mblsono,hblsono,plable,vesselvoyage,atdTitle,dono,piecepacker,remark,dotype,paymentitem,"+profits+" AS carriername");
			}
			sb.append("\n 			,(SELECT REPLACE(REPLACE(REPLACE( f_bus_shipping_goodsinfo2('billid=' || b.jobid||';'), CHR(10), '</br>'), '\\cx', '</br>'), '\\r', '</br>')) AS goodsinfo2");
			sb.append("\n 			,(SELECT c.abbr FROM sys_corporation c WHERE b.carrierid =c.id LIMIT 1 ) AS carrierinfo");//船公司信息，跟委托下面勾选船公司显示全称无关
			sb.append("\n 			,(SELECT namec FROM sys_user WHERE id = "+uid+") AS usernamec");
			sb.append("\n 			,(SELECT namee FROM sys_user WHERE id = "+uid+") AS usernamee");
			sb.append("\n 			,(SELECT tel1 FROM sys_user WHERE id = "+uid+") AS usertel");
			sb.append("\n 			,(SELECT fax FROM sys_user WHERE id = "+uid+") AS userfax");
			sb.append("\n 			,(CASE WHEN freightitem = 'PP' THEN freightitemdesc ELSE '' END) AS freightitempp");
			sb.append("\n 			,(CASE WHEN freightitem = 'CC' THEN freightitemdesc ELSE '' END) AS freightitemcc");
			sb.append("\n 			,(CASE WHEN paymentitemcode = 'PP' THEN paymentitemdesc ELSE '' END) AS paymentitempp");
			sb.append("\n 			,(CASE WHEN paymentitemcode = 'CC' THEN paymentitemdesc ELSE '' END) AS paymentitemcc");
			sb.append("\n 			,(SELECT namec FROM sys_corporation a WHERE a.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE x.id = "+uid+" AND x.corpid = a.id)) corpnamec");
			sb.append("\n 			,(SELECT namee FROM sys_corporation a WHERE a.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE x.id = "+uid+" AND x.corpid = a.id)) corpnamee");
			sb.append("\n 			,(SELECT addressc FROM sys_corporation a WHERE a.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE x.id = "+uid+" AND x.corpid = a.id)) corpaddressc");
			sb.append("\n 			,(SELECT addresse FROM sys_corporation a WHERE a.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE x.id = "+uid+" AND x.corpid = a.id)) corpaddresse");
			sb.append("\n 			,(SELECT tel1 FROM sys_corporation a WHERE a.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE x.id = "+uid+" AND x.corpid = a.id)) corptel");
			sb.append("\n 			,(SELECT fax1 FROM sys_corporation a WHERE a.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user x WHERE x.id = "+uid+" AND x.corpid = a.id)) corpfax");
			sb.append("\n 			,(to_char(now(), 'MON.DD,yyyy')) AS now");
			
			sb.append("\n 			,(SELECT x.namee FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id LIMIT 1) AS bkagent");//订舱代理全称
			sb.append("\n 			,(SELECT x.namec FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id LIMIT 1) AS bkagentnamec");//订舱代理全称中文
			sb.append("\n 			,(SELECT x.contact FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id LIMIT 1) AS bkagentcontact");//订舱代理 联系人
			//sb.append("\n 			,(SELECT x.marksnombl FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS marksnombl");//
			sb.append("\n 			,plable AS payplace");//
			sb.append("\n 			,(CASE WHEN paymentitemcode = 'PP' THEN plable ELSE '' END) AS payplacepp");//
			sb.append("\n 			,(CASE WHEN paymentitemcode = 'CC' THEN plable ELSE '' END) AS payplacecc");//
			//sb.append("\n 			,(SELECT x.goodsdescmbl FROM bus_shipping x WHERE x.id = b.id  LIMIT 1) AS goodsdescmbl");//
//			sb.append("\n 			,(SELECT string_agg(x.goodsnamee,'</br>') FROM bus_ship_container x WHERE x.jobid = b.jobid AND x.isdelete = false AND x.parentid IS NULL AND x.goodsnamee IS NOT NULL AND x.goodsnamee <> '') AS goodsnamee");//
			sb.append("\n 			,goodsnamee,goodsnamee2,polnamecc,podnamecc,poanamecc,pddnamecc");
			sb.append("\n 			,(SELECT x.price||'' FROM _fina_arap x WHERE x.jobid = b.jobid AND x.isdelete = false AND x.feeitemnamec like '%海运费%' AND x.ppcc = 'CC' AND x.araptype='AR' LIMIT 1) AS ocfprice");//
			sb.append("\n 			,(SELECT SUM(x.amount)||'' FROM _fina_arap x WHERE x.jobid = b.jobid AND x.isdelete = false AND x.feeitemnamec like '%海运费%' AND x.ppcc = 'CC' AND x.araptype='AR') AS ocfamt");//
			sb.append("\n 			,(SELECT SUM(f_amtto(x.jobdate::DATE, x.currency, 'DHS', x.amount))::NUMERIC(18,2)||'' FROM _fina_arap x WHERE x.jobid = b.jobid AND x.isdelete = false AND x.feeitemnamec like '%海运费%' AND x.ppcc = 'CC' AND x.araptype='AR') AS ocfamtdhs");//
			
			sb.append("\n 			,(SELECT string_agg(freightcharge,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS freightcharge");
			sb.append("\n 			,(SELECT string_agg(rate,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS rate");
			sb.append("\n 			,(SELECT string_agg(prepaid,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS prepaid");
			sb.append("\n 			,(SELECT string_agg(collect,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS collect");
			sb.append("\n 			,(SELECT x.mblpol FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblpol");
			sb.append("\n 			,(SELECT x.mblpod FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblpod");
			sb.append("\n 			,(SELECT x.mblpdd FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblpdd");
			sb.append("\n 			,(SELECT x.mbldestination FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mbldestination");
			//sb.append("\n 			,(select SUM(coalesce(substring(f.collect,E'(\\d+)'),'0')::NUMERIC) FROM bus_freightcharge f WHERE f.jobid = b.jobid and f.collect <> '')::TEXT AS total");
			//sb.append("\n 			,''::TEXT AS total");
			sb.append("\n 			,(SELECT f_bus_freightcharge_sum('jobid='||b.jobid)) AS total");
			sb.append("\n 			,(SELECT substring(collect from 1 for 3)  FROM bus_freightcharge WHERE jobid = b.jobid and collect <> '' limit 1) AS totalcurrency");
			sb.append("\n 			,(SELECT refno FROM fina_jobs WHERE id = b.jobid and isdelete = false ) AS refno");
			sb.append("\n 			,(SELECT x.remark_booking FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS remark_booking");
			sb.append("\n 			,(SELECT x.hscode FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS hscode");
			sb.append("\n 			,(SELECT qq FROM sys_user WHERE id = "+uid+") AS userqq");
			sb.append("\n 			,cntno");
			sb.append("\n 			,'&'::text AS 连接符");
			sb.append("\n 			,(SELECT i.nos FROM wms_in i,wms_out o,fina_jobs j where j.id = b.jobid AND i.id = o.inid AND o.nos = j.refno) AS wmsinnos");
			sb.append("\n 			,(SELECT CASE WHEN ldtype = 'F' THEN 'FCL' WHEN ldtype = 'L' THEN 'LCL' ELSE '' END FROM fina_jobs WHERE id = b.jobid and isdelete = false ) AS ldtype");
			sb.append("\n 			,sealno");
			sb.append("\n 			,cnotainerinfo");
			sb.append("\n 			,(SELECT nos FROM fina_jobs WHERE id = b.jobid and isdelete = false ) AS jobno");
			sb.append("\n 			,jcustomerc");
			sb.append("\n 			,jcustomere");
			sb.append("\n 			,派送收货人名称,派送地址,派送联系人,派送电话");
			sb.append("\n 			,mbltype,hbltype");
			sb.append("\n 			,(SELECT bargeetd FROM bus_shipping WHERE jobid = b.jobid AND isdelete = FALSE) AS bargeetd");
			sb.append("\n 			,提单要求,委托要求,备注1,备注2,第二通知人");
			sb.append("\n 			,(SELECT c.namec FROM bus_shipping s, sys_corporation c WHERE s.jobid = b.jobid AND s.isdelete = FALSE and s.agentdesid = c.id) AS agentdesnamec");
			sb.append("\n 			,(SELECT c.namee FROM bus_shipping s, sys_corporation c WHERE s.jobid = b.jobid AND s.isdelete = FALSE and s.agentdesid = c.id) AS agentdesnamee");
			sb.append("\n 			,(SELECT to_char(onboarddate,'MON.DD.yyyy') as onboarddate FROM bus_shipping WHERE jobid = b.jobid AND isdelete = FALSE) AS onboarddate");
			sb.append("\n  		FROM _rpt_bill b");
			sb.append("\n  			WHERE isdelete = FALSE");
			sb.append("\n  			AND id = ");
			sb.append(billid);
			sb.append("\n  		) AS T");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map != null && map.size() > 0 && map.containsKey("json")){
				Object obj = map.get("json");
				return obj == null ? "''" : obj.toString();
			}
			return "''";
		}
		return "''";
	}
	
	/**
	 * 根据billid获取打印数据 AIR
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String getViewDataAir(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String billid = request.getParameter("b");
		String uid = StrUtils.getSqlFormat(request.getParameter("u"));
		uid = StrUtils.isNull(uid) ? "-1" : uid;
		
		if(!StrUtils.isNumber(uid)){
			uid = "-1";
		}
		
		if(billid != null && !billid.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
			sb.append("\n FROM (SELECT	b.*");
			sb.append("\n 				,(SELECT namee FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamee");
			sb.append("\n 				,(SELECT namec FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamec");
			sb.append("\n 				,(SELECT tel1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usertel1");
			sb.append("\n 				,(SELECT tel2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usertel2");
			sb.append("\n 				,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail1");
			sb.append("\n 				,(SELECT email2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail2");
			sb.append("\n 				,(SELECT fax FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS userfax");
			sb.append("\n 				,(SELECT c.namec FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpnamec");
			sb.append("\n 				,(SELECT c.namee FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpnamee");
			sb.append("\n 				,(SELECT c.addressc FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS  corpaddressc");
			sb.append("\n 				,(SELECT c.addresse FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpaddresse");
			
			sb.append("\n 				,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail1");
			
			sb.append("\n  		FROM _rpt_bill_air b");
			sb.append("\n  		where	b.id = ");
			sb.append(billid);
			sb.append("\n  		) AS T");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map != null && map.size() > 0 && map.containsKey("json")){
				Object obj = map.get("json");
				return obj == null ? "''" : obj.toString();
			}
			return "''";
		}
		return "''";
	}
	
	/**
	 * 根据billid获取打印数据 MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String getViewDatambl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String billid = request.getParameter("b");
		
		String uid = StrUtils.getSqlFormat(request.getParameter("u"));
		uid = StrUtils.isNull(uid) ? "-1" : uid;
		if(!StrUtils.isNumber(uid)){
			uid = "-1";
		}
		
		
		if(billid != null && !billid.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
			sb.append("\n FROM (SELECT	* ");
			
			sb.append("\n 			,(SELECT c.abbr FROM sys_corporation c WHERE b.carrierid =c.id LIMIT 1 ) AS carrierinfo");//船公司信息，跟委托下面勾选船公司显示全称无关
			sb.append("\n 			,(SELECT x.namee FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id LIMIT 1) AS bkagent");//订舱代理全称
			sb.append("\n 			,(SELECT x.namec FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id LIMIT 1) AS bkagentnamec");//订舱代理全称中文
			sb.append("\n 			,(SELECT x.contact FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id  LIMIT 1) AS bkagentcontact");//订舱代理 联系人
			//sb.append("\n 			,(SELECT x.marksnombl FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS marksnombl");//
			//sb.append("\n 			,(SELECT x.goodsdescmbl FROM bus_shipping x WHERE x.id = b.id  LIMIT 1) AS goodsdescmbl");//
			sb.append("\n 			,(SELECT x.payplace FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS payplace");//
			sb.append("\n 			,(SELECT (CASE WHEN x.paymentitem = 'PP' THEN x.payplace ELSE '' END) FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS payplacepp");//
			sb.append("\n 			,(SELECT (CASE WHEN x.paymentitem = 'CC' THEN x.payplace ELSE '' END) FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS payplacecc");//
			sb.append("\n 			,(SELECT x.namec FROM dat_filedata x ,bus_shipping y WHERE y.id = b.shipid AND x.isleaf = TRUE AND x.isdelete = false AND x.fkcode = 150 AND x.code = y.mbltype LIMIT 1) AS mbltype");//
			sb.append("\n 			,(CASE WHEN freightitem = 'PP' THEN freightitemdesc ELSE '' END) AS freightitempp");
			sb.append("\n 			,(CASE WHEN freightitem = 'CC' THEN freightitemdesc ELSE '' END) AS freightitemcc");
			sb.append("\n 			,(CASE WHEN paymentitemcode = 'PP' THEN paymentitemdesc ELSE '' END) AS paymentitempp");
			sb.append("\n 			,(CASE WHEN paymentitemcode = 'CC' THEN paymentitemdesc ELSE '' END) AS paymentitemcc");
			//sb.append("\n 			,(SELECT string_agg(x.goodsnamee,'</br>') FROM bus_ship_container x WHERE x.jobid = b.jobid AND x.isdelete = false AND x.parentid IS NULL AND x.goodsnamee IS NOT NULL AND x.goodsnamee <> '') AS goodsnamee");//
			sb.append("\n 			,goodsnamee,goodsnamee2,polnamecc,podnamecc,poanamecc,pddnamecc");
			sb.append("\n 			,(to_char(now(), 'MON.DD,yyyy')) AS now");
			sb.append("\n 			,(SELECT namee FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamee");
			sb.append("\n 			,(SELECT tel1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS tel1");
			sb.append("\n 			,(SELECT tel2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS tel2");
			sb.append("\n 			,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS email1");
			sb.append("\n 			,(SELECT email2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS email2");
			sb.append("\n 			,(SELECT fax FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS fax");
			sb.append("\n 			,(SELECT nos FROM fina_jobs WHERE id = b.jobid AND isdelete = FALSE) AS jobno");
			
			sb.append("\n 			,(SELECT x.mblpol FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblpol");//
			sb.append("\n 			,(SELECT x.mblpod FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblpod");//
			sb.append("\n 			,(SELECT x.mblpdd FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblpdd");//
			
			sb.append("\n 			,(SELECT string_agg(freightcharge,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS freightcharge");
			sb.append("\n 			,(SELECT string_agg(rate,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS rate");
			sb.append("\n 			,(SELECT string_agg(prepaid,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS prepaid");
			sb.append("\n 			,(SELECT string_agg(collect,f_newline())  FROM bus_freightcharge WHERE jobid = b.jobid) AS collect");
			
			sb.append("\n 			,(SELECT x.mbldestination FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mbldestination");//
			sb.append("\n 			,(SELECT x.remark_booking FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS remark_booking");
			sb.append("\n 			,(SELECT x.mblnombl FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS mblnombl");//
			
			sb.append("\n 			,(SELECT qq FROM sys_user WHERE id = "+uid+") AS userqq");
			sb.append("\n 			,(SELECT string_agg(cntno,',') FROM bus_ship_container WHERE jobid = b.jobid AND cntno IS NOT NULL AND cntno <> '' AND isdelete = FALSE AND parentid IS NULL) AS cntno");
			sb.append("\n 			,(SELECT sum(vgm) FROM bus_ship_container WHERE jobid = b.jobid AND isdelete = FALSE AND parentid IS NULL) AS vgm");
			sb.append("\n 			,'&'::text AS 连接符");
			sb.append("\n 			,(SELECT i.nos FROM wms_in i,wms_out o,fina_jobs j where j.id = b.jobid AND i.id = o.inid AND o.nos = j.refno) AS wmsinnos");
			sb.append("\n 			,(SELECT CASE WHEN ldtype = 'F' THEN 'FCL' WHEN ldtype = 'L' THEN 'LCL' ELSE '' END FROM fina_jobs WHERE id = b.jobid and isdelete = false ) AS ldtype");
			sb.append("\n 			,(SELECT string_agg(sealno,',') FROM bus_ship_container WHERE jobid = b.jobid AND sealno IS NOT NULL AND sealno <> '' AND isdelete = FALSE AND parentid IS NULL) AS sealno");
			sb.append("\n 			,(SELECT string_agg(cntno||'/'||(select code from dat_cntype where isdelete =false and id = t.cntypeid)||'/'||t.sealno||'/'||t.piece||t.packagee||'/'||t.grswgt||'KGS/'||t.cbm||'CBM',f_newline()) FROM bus_ship_container t WHERE jobid = b.jobid AND cntno IS NOT NULL AND cntno <> '' AND isdelete = FALSE AND parentid IS NULL) AS cnotainerinfo");
			sb.append("\n 			,(SELECT nos FROM fina_jobs WHERE id = b.jobid and isdelete = false ) AS jobno");
			sb.append("\n 			,(SELECT to_char(sidate,'yyyy/mm/dd hh24:mi') FROM bus_shipping WHERE jobid = b.jobid and isdelete = false ) AS sidate");
			sb.append("\n 			,(SELECT bargeetd FROM bus_shipping WHERE jobid = b.jobid AND isdelete = FALSE) AS bargeetd");
			sb.append("\n 			,(SELECT nos FROM fina_jobs WHERE id = b.jobid and isdelete = false ) AS jobno");
			sb.append("\n  		FROM _rpt_bill b");
			sb.append("\n  			WHERE isdelete = FALSE");
			sb.append("\n  			AND id = ");
			sb.append(billid);
			sb.append("\n  		) AS T");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map != null && map.size() > 0 && map.containsKey("json")){
				Object obj = map.get("json");
				return obj == null ? "''" : obj.toString();
			}
			return "''";
		}
		return "''";
	}
	/**
	 * 另存为模版
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String checkAndSaveTempletenameForPrivate(HttpServletRequest request,String rpStr,HttpServletResponse response){
		try {
			String userid = AppUtils.base64Decoder(StrUtils.getSqlFormat(request.getParameter("u")));
			String text = StrUtils.getSqlFormat(request.getParameter("text"));
			String code = StrUtils.getSqlFormat(request.getParameter("code"));
			String json = StrUtils.getSqlFormat(request.getParameter("json"));
			String reporttype = request.getParameter("reporttype");
			String ispublic = StrUtils.getSqlFormat(request.getParameter("ispublic"));
			String sql = "SELECT count(id) FROM sys_report WHERE code = '"+text+"' AND modcode = '"+modhbl+"'";
			
			String modcode = "shippinghbl";
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("book")){//book
				modcode = "shippingbook";
			}else if(reporttype.equals("shippingsingle")){
				modcode = reporttype;
			}else if(reporttype.equals("shippingorder")){
				modcode = reporttype;
			}else{//hbl
				modcode = "shippinghbl";
			}
			
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null && map.containsKey("count")){
				if(Integer.parseInt(map.get("count").toString()) == 0){
					if(!StrUtils.isNull(userid)&&!StrUtils.isNull(text)){
						String raq = new Date().getTime() + "_" + userid;
						String savesql = "INSERT INTO sys_report(id,code,filename,isleaf,modcode,templete,userid,ispublic)"
							+ "VALUES(getid(),'"+text+"','"+raq+".raq','N','"
							+ modcode +"','"+json+"',"+userid+","+ispublic+")";
						daoIbatisTemplate.updateWithUserDefineSql(savesql);
						return "{"+'"'+"type"+'"'+":"+'"'+"success"+'"'+","+'"'+"value"+'"'+":"+'"'+raq+'"'+"}";
					}
				}else{
					return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"模版名称重复!"+'"'+"}";
				}
			}
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		} catch (NoRowException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NoRowException"+'"'+"}";
		} catch (NullPointerException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NullPointerException"+'"'+"}";
		} catch (NumberFormatException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NumberFormatException"+'"'+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		}
	}
	/**
	 * 另存为模版
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String checkAndSaveTempletenameForPrivatembl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		try {
			String userid = AppUtils.base64Decoder(StrUtils.getSqlFormat(request.getParameter("u")));
			String text = StrUtils.getSqlFormat(URLDecoder.decode(request.getParameter("text"), "UTF-8"));
			String code = StrUtils.getSqlFormat(request.getParameter("code"));
			String json = StrUtils.getSqlFormat(request.getParameter("json"));
			String ispublic = StrUtils.getSqlFormat(request.getParameter("ispublic"));
			String reporttype = request.getParameter("reporttype");
			String sql = "SELECT count(id) FROM sys_report WHERE code = '"+text+"' AND modcode = '"+modmbl+"'";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null && map.containsKey("count")){
				if(Integer.parseInt(map.get("count").toString()) == 0){
					if(!StrUtils.isNull(userid)||!StrUtils.isNull(text)){
						String raq = new Date().getTime() + "_" + userid;
						String savesql = "INSERT INTO sys_report(id,code,filename,isleaf,modcode,templete,userid,ispublic)"
							+ "VALUES(getid(),'"+text+"','"+raq+".raq','N','"+(!StrUtils.isNull(reporttype)&&reporttype.equals("book")?modbook:modmbl)+"','"+json+"',"+userid+","+ispublic+")";
						daoIbatisTemplate.updateWithUserDefineSql(savesql);
						return "{"+'"'+"type"+'"'+":"+'"'+"success"+'"'+","+'"'+"value"+'"'+":"+'"'+raq+'"'+"}";
					}
				}else{
					return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"模版名称重复!"+'"'+"}";
				}
			}
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		} catch (NoRowException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NoRowException"+'"'+"}";
		} catch (NullPointerException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NullPointerException"+'"'+"}";
		} catch (NumberFormatException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NumberFormatException"+'"'+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		}
	}
	/**
	 * 另存为模版
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String checkAndSaveTempletenameForPrivateair(HttpServletRequest request,String rpStr,HttpServletResponse response){
		try {
			String userid = AppUtils.base64Decoder(StrUtils.getSqlFormat(request.getParameter("u")));
			String text = StrUtils.getSqlFormat(URLDecoder.decode(request.getParameter("text"), "UTF-8"));
			String code = StrUtils.getSqlFormat(request.getParameter("code"));
			String json = StrUtils.getSqlFormat(request.getParameter("json"));
			String ispublic = StrUtils.getSqlFormat(request.getParameter("ispublic"));
			String reporttype = request.getParameter("reporttype");
			
			String sql = "";
			if("airmodel".equals(reporttype)){
				 sql = "SELECT count(id) FROM sys_report WHERE code = '"+text+"' AND modcode = 'airmodel'";
			}else{
				sql = "SELECT count(id) FROM sys_report WHERE code = '"+text+"' AND modcode = '"+modair+"'";
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null && map.containsKey("count")){
				if(Integer.parseInt(map.get("count").toString()) == 0){
					if(!StrUtils.isNull(userid)||!StrUtils.isNull(text)){
						String raq = new Date().getTime() + "_" + userid;
						String savesql = "";
						if("airmodel".equals(reporttype)){
							 savesql = "INSERT INTO sys_report(id,code,filename,isleaf,modcode,templete,userid,ispublic)"
								+ "VALUES(getid(),'"+text+"','"+raq+".raq','N','"
								+reporttype+"','"+json+"',"+userid+","+ispublic+")";
						}else{
							savesql = "INSERT INTO sys_report(id,code,filename,isleaf,modcode,templete,userid,ispublic)"
								+ "VALUES(getid(),'"+text+"','"+raq+".raq','N','"
								+(!StrUtils.isNull(reporttype)&&reporttype.equals("airmbl")?modairmbl:modair)
								+"','"+json+"',"+userid+","+ispublic+")";
						}
						daoIbatisTemplate.updateWithUserDefineSql(savesql);
						return "{"+'"'+"type"+'"'+":"+'"'+"success"+'"'+","+'"'+"value"+'"'+":"+'"'+raq+'"'+"}";
					}
				}else{
					return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"模版名称重复!"+'"'+"}";
				}
			}
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		} catch (NoRowException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NoRowException"+'"'+"}";
		} catch (NullPointerException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NullPointerException"+'"'+"}";
		} catch (NumberFormatException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NumberFormatException"+'"'+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private String reverseSaveData(HttpServletRequest request, String rpStr,
			HttpServletResponse response) {
		String billid = StrUtils.getSqlFormat(request.getParameter("billid"));
		String targetId = StrUtils.getSqlFormat(request.getParameter("targetId"));
		String content = StrUtils.getSqlFormat(request.getParameter("content"));
		
		String shippingSQL = "SELECT cnortitle,cneetitle,notifytitle,agentitle,notifytitle2,cnortitlembl,cneetitlembl,notifytitlembl,agentitlembl,otherfeepp FROM bus_shipping WHERE id ="+billid;
		
		String busbillSQL = "SELECT cnortitle,cneetitle,notifytitle,agentitle FROM bus_ship_bill WHERE id ="+billid;
		try {
			Map shipping = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(shippingSQL);
			if(shipping!=null && shipping.size() > 0){
				if(shipping.containsKey(targetId)){
					////System.out.println("old:"+shipping.get(targetId));
					////System.out.println("now:"+content);
					String updateShipping = "UPDATE bus_shipping SET "+targetId+"='"+content +"' WHERE id="+billid;
					daoIbatisTemplate.updateWithUserDefineSql(updateShipping);
					return "成功替换";
				}else{
					return "无匹配项";
				}
			}
		} catch (NoRowException e) {
			//分单情况
			try {
				Map busbill = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(busbillSQL);
				if(busbill!=null && busbill.size() > 0){
					if(busbill.containsKey(targetId)){
						////System.out.println("old:"+busbill.get(targetId));
						////System.out.println("now:"+content);
						String updatebusbill = "UPDATE bus_ship_bill SET "+targetId+"='"+content +"' WHERE id="+billid;
						daoIbatisTemplate.updateWithUserDefineSql(updatebusbill);
						return "成功替换";
					}else{
						return "无匹配项";
					}
				}
			} catch (NoRowException e2) {
				e.printStackTrace();
				return "定制和分单均未找到";
			} catch (Exception e2) {
				e.printStackTrace();
				return e.getLocalizedMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";
	}
	
	/**
	 * 读取模版JSON铁运train
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String toTempletefortrain(HttpServletRequest request,String rpStr,HttpServletResponse response){
		if(rpStr != null && !rpStr.isEmpty()){
			String reporttype = request.getParameter("reporttype");
			StringBuffer sbsql = new StringBuffer();
			try {
				rpStr = URLDecoder.decode(rpStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("trainbook")){
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='trainbook';");
			}else{//hbl
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='trainhbl';");
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			if(map != null && map.size() > 0 && map.containsKey("templete")){
				Object obj = map.get("templete");
				return obj == null ? "''" : obj.toString();
			}
		}
		return "''";
	}
	

	/**
	 * 保存模版JSON 铁运train
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String saveTempletetrain(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String json = request.getParameter("json");
		String reporttype = request.getParameter("reporttype");
		String usercode = "";
		try {
			String userid = request.getParameter("userid");
			SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid));
			usercode = user.getCode();
		} catch (Exception e) {
		}
		if(rpStr != null && !rpStr.isEmpty()&&json != null && !json.isEmpty()){
			json = json.replace("'", "''");
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("UPDATE sys_report SET templete='");
			sbsql.append(json);
			sbsql.append("' ,updater = '"+usercode+"'");
			sbsql.append(" ,updatetime = now()");
			sbsql.append(" WHERE filename='");
			sbsql.append(rpStr);
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("trainbook")){//book
				sbsql.append("' AND modcode='trainbook';");
			}else{//hbl
				sbsql.append("' AND modcode='trainhbl';");
			}
			try {
				daoIbatisTemplate.updateWithUserDefineSql(sbsql.toString());
			} catch (Exception e) {
				String returns = MessageUtils.returnExceptionForservlet(e);
				return returns;
			}
			return "SUCCESS";
		}
		return "ERROR";
	}
	
	/**
	 * 另存为模版铁运train
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String checkAndSaveTempletenameForPrivatetrain(HttpServletRequest request,String rpStr,HttpServletResponse response){
		try {
			String userid = AppUtils.base64Decoder(StrUtils.getSqlFormat(request.getParameter("u")));
			String text = StrUtils.getSqlFormat(URLDecoder.decode(request.getParameter("text"), "UTF-8"));
			String code = StrUtils.getSqlFormat(request.getParameter("code"));
			String json = StrUtils.getSqlFormat(request.getParameter("json"));
			String ispublic = StrUtils.getSqlFormat(request.getParameter("ispublic"));
			String reporttype = request.getParameter("reporttype");
			String sql = "SELECT count(id) FROM sys_report WHERE code = '"+text+"' AND modcode = 'trainhbl'";
			
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null && map.containsKey("count")){
				if(Integer.parseInt(map.get("count").toString()) == 0){
					if(!StrUtils.isNull(userid)||!StrUtils.isNull(text)){
						String raq = new Date().getTime() + "_" + userid;
						String savesql = "INSERT INTO sys_report(id,code,filename,isleaf,modcode,templete,userid,ispublic)"
							+ "VALUES(getid(),'"+text+"','"+raq+".raq','N','"
							+(!StrUtils.isNull(reporttype)&&reporttype.equals("trainbook")?trainbook:trainhbl)
							+"','"+json+"',"+userid+","+ispublic+")";
						daoIbatisTemplate.updateWithUserDefineSql(savesql);
						return "{"+'"'+"type"+'"'+":"+'"'+"success"+'"'+","+'"'+"value"+'"'+":"+'"'+raq+'"'+"}";
					}
				}else{
					return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"模版名称重复!"+'"'+"}";
				}
			}
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		} catch (NoRowException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NoRowException"+'"'+"}";
		} catch (NullPointerException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NullPointerException"+'"'+"}";
		} catch (NumberFormatException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NumberFormatException"+'"'+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		}
	}
	
	/**
	 * 获取铁运提单打印数据
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String getViewDatatrain(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String billid = request.getParameter("b");
		String uid = StrUtils.getSqlFormat(request.getParameter("u"));
		uid = StrUtils.isNull(uid) ? "-1" : uid;
		if(!StrUtils.isNumber(uid)){
			uid = "-1";
		}
		
		if(billid != null && !billid.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
			sb.append("\n FROM (SELECT	b.*");
			sb.append("\n 				,(SELECT namee FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamee");
			sb.append("\n 				,(SELECT namec FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamec");
			sb.append("\n 				,(SELECT tel1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usertel1");
			sb.append("\n 				,(SELECT tel2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usertel2");
			sb.append("\n 				,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail1");
			sb.append("\n 				,(SELECT email2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail2");
			sb.append("\n 				,(SELECT fax FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS userfax");
			sb.append("\n 				,(SELECT c.namec FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpnamec");
			sb.append("\n 				,(SELECT c.namee FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpnamee");
			sb.append("\n 				,(SELECT c.addressc FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS  corpaddressc");
			sb.append("\n 				,(SELECT c.addresse FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpaddresse");
			sb.append("\n 				,(to_char(now(), 'MON.DD,yyyy')) AS now");
			sb.append("\n  				, cntdesc");
			sb.append("\n  		FROM _rpt_bill_train b");
			sb.append("\n  		where	b.id = ");
			sb.append(billid);
			sb.append("\n  		) AS T");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map != null && map.size() > 0 && map.containsKey("json")){
				Object obj = map.get("json");
				return obj == null ? "''" : obj.toString();
			}
			return "''";
		}
		return "''";
	}
	
	/**
	 * 读取模版JSON 铁运MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String toTempletefortrainmbl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		if(rpStr != null && !rpStr.isEmpty()){
			String reporttype = request.getParameter("reporttype");
			StringBuffer sbsql = new StringBuffer();
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("trainbook")){//book
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='trainbook';");
			}else{//mbl
				sbsql.append("SELECT templete FROM sys_report WHERE filename = '"+rpStr+"' AND modcode='trainmbl';");
			}
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
			if(map != null && map.size() > 0 && map.containsKey("templete")){
				Object obj = map.get("templete");
				return obj == null ? "''" : obj.toString();
			}
		}
		return "''";
	}
	
	/**
	 * 保存模版JSON 铁运MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String saveTempletefortrainmbl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String json = request.getParameter("json");
		String usercode = "";
		String reporttype = request.getParameter("reporttype");
		try {
			String userid = request.getParameter("userid");
			SysUser user = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid));
			usercode = user.getCode();
		} catch (Exception e) {
		}
		if(rpStr != null && !rpStr.isEmpty()&&json != null && !json.isEmpty()){
			json = json.replace("'", "''");
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("UPDATE sys_report SET templete='");
			sbsql.append(json);
			sbsql.append("' ,updater = '"+usercode+"'");
			sbsql.append(" ,updatetime = now()");
			sbsql.append(" WHERE filename='");
			sbsql.append(rpStr);
			if(!StrUtils.isNull(reporttype)&&reporttype.equals("trainbook")){//book
				sbsql.append("' AND modcode='trainbook';");
			}else{//mbl
				sbsql.append("' AND modcode='trainmbl';");
			}
			try {
				daoIbatisTemplate.updateWithUserDefineSql(sbsql.toString());
			} catch (Exception e) {
				String returns = MessageUtils.returnExceptionForservlet(e);
				return returns;
			}
			return "SUCCESS";
		}
		return "ERROR";
	}
	
	/**
	 * 另存为模版 铁运MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String checkAndSaveTempletenameForPrivatetrainmbl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		try {
			String userid = AppUtils.base64Decoder(StrUtils.getSqlFormat(request.getParameter("u")));
			String text = StrUtils.getSqlFormat(URLDecoder.decode(request.getParameter("text"), "UTF-8"));
			String code = StrUtils.getSqlFormat(request.getParameter("code"));
			String json = StrUtils.getSqlFormat(request.getParameter("json"));
			String ispublic = StrUtils.getSqlFormat(request.getParameter("ispublic"));
			String reporttype = request.getParameter("reporttype");
			String sql = "SELECT count(id) FROM sys_report WHERE code = '"+text+"' AND modcode = '"+trainmbl+"'";
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map!=null && map.containsKey("count")){
				if(Integer.parseInt(map.get("count").toString()) == 0){
					if(!StrUtils.isNull(userid)||!StrUtils.isNull(text)){
						String raq = new Date().getTime() + "_" + userid;
						String savesql = "INSERT INTO sys_report(id,code,filename,isleaf,modcode,templete,userid,ispublic)"
							+ "VALUES(getid(),'"+text+"','"+raq+".raq','N','"+(!StrUtils.isNull(reporttype)&&reporttype.equals("trainbook")?trainbook:trainmbl)+"','"+json+"',"+userid+","+ispublic+")";
						daoIbatisTemplate.updateWithUserDefineSql(savesql);
						return "{"+'"'+"type"+'"'+":"+'"'+"success"+'"'+","+'"'+"value"+'"'+":"+'"'+raq+'"'+"}";
					}
				}else{
					return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"模版名称重复!"+'"'+"}";
				}
			}
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		} catch (NoRowException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NoRowException"+'"'+"}";
		} catch (NullPointerException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NullPointerException"+'"'+"}";
		} catch (NumberFormatException e) {
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+"NumberFormatException"+'"'+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{"+'"'+"type"+'"'+":"+'"'+"error"+'"'+","+'"'+"value"+'"'+":"+'"'+""+'"'+"}";
		}
	}
	
	
	/**
	 * 根据billid获取打印数据铁运 MBL
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String getViewDatatrainmbl(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String billid = request.getParameter("b");
		
		String uid = StrUtils.getSqlFormat(request.getParameter("u"));
		uid = StrUtils.isNull(uid) ? "-1" : uid;
		if(!StrUtils.isNumber(uid)){
			uid = "-1";
		}
		
		
		if(billid != null && !billid.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
			sb.append("\n FROM (SELECT	* ");
			sb.append("\n 			,(SELECT x.namee FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id LIMIT 1) AS bkagent");//订舱代理全称
			sb.append("\n 			,(SELECT x.namec || ' ' || x.contact FROM sys_corporation x , bus_shipping y WHERE y.agentid =x.id AND x.isdelete = FALSE AND y.id = b.id  LIMIT 1) AS bkagentcontact");//订舱代理 联系人
			sb.append("\n 			,(SELECT x.payplace FROM bus_train x WHERE x.id = b.id LIMIT 1) AS payplace");//
			sb.append("\n 			,(SELECT (CASE WHEN x.paymentitem = 'PP' THEN x.payplace ELSE '' END) FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS payplacepp");//
			sb.append("\n 			,(SELECT (CASE WHEN x.paymentitem = 'CC' THEN x.payplace ELSE '' END) FROM bus_shipping x WHERE x.id = b.id LIMIT 1) AS payplacecc");//
			sb.append("\n 			,(SELECT x.namec FROM dat_filedata x ,bus_train y WHERE y.id = b.trainid AND x.isleaf = TRUE AND x.isdelete = false AND x.fkcode = 150 AND x.code = y.mbltype LIMIT 1) AS mbltype");//
			sb.append("\n 			,(CASE WHEN freightitem = 'PP' THEN freightitemdesc ELSE '' END) AS freightitempp");
			sb.append("\n 			,(CASE WHEN freightitem = 'CC' THEN freightitemdesc ELSE '' END) AS freightitemcc");
			sb.append("\n 			,(SELECT namee FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamee");
			sb.append("\n 			,(SELECT tel1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS tel1");
			sb.append("\n 			,(SELECT tel2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS tel2");
			sb.append("\n 			,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS email1");
			sb.append("\n 			,(SELECT email2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS email2");
			sb.append("\n 			,(SELECT fax FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS fax");
			sb.append("\n 			,(SELECT nos FROM fina_jobs WHERE id = b.jobid AND isdelete = FALSE) AS jobno");
			sb.append("\n 			,(SELECT x.mblpol FROM bus_train x WHERE x.id = b.id LIMIT 1) AS mblpol");
			sb.append("\n 			,(SELECT x.mblpod FROM bus_train x WHERE x.id = b.id LIMIT 1) AS mblpod");
			sb.append("\n 			,(SELECT x.mblpdd FROM bus_train x WHERE x.id = b.id LIMIT 1) AS mblpdd");
			sb.append("\n 			,(SELECT x.mbldestination FROM bus_train x WHERE x.id = b.id LIMIT 1) AS mbldestination");
			sb.append("\n 			,(SELECT x.remark_booking FROM bus_train x WHERE x.id = b.id LIMIT 1) AS remark_booking");
			sb.append("\n 			,cntdesc");
			sb.append("\n  		FROM _rpt_bill_train b");
			sb.append("\n  			WHERE isdelete = FALSE");
			sb.append("\n  			AND id = ");
			sb.append(billid);
			sb.append("\n  		) AS T");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map != null && map.size() > 0 && map.containsKey("json")){
				Object obj = map.get("json");
				return obj == null ? "''" : obj.toString();
			}
			return "''";
		}
		return "''";
	}
	
	
	
	
	/**
	 * 根据billid获取打印数据 AIR
	 * @param request
	 * @param rpStr
	 * @param response
	 * @return
	 */
	private String getViewDataAirNew(HttpServletRequest request,String rpStr,HttpServletResponse response){
		String billid = request.getParameter("b");
		String uid = StrUtils.getSqlFormat(request.getParameter("u"));
		String datatype = StrUtils.getSqlFormat(request.getParameter("datatype"));
		//System.out.println("datatype--->"+datatype);
		//System.out.println("billid--->"+billid);
		
		uid = StrUtils.isNull(uid) ? "-1" : uid;
		if(!StrUtils.isNumber(uid)){
			uid = "-1";
		}
		if(billid != null && !billid.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
			sb.append("\n FROM (SELECT	b.*");
			sb.append("\n 				,(SELECT namee FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamee");
			sb.append("\n 				,(SELECT namec FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usernamec");
			sb.append("\n 				,(SELECT tel1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usertel1");
			sb.append("\n 				,(SELECT tel2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS usertel2");
			sb.append("\n 				,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail1");
			sb.append("\n 				,(SELECT email2 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail2");
			sb.append("\n 				,(SELECT fax FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS userfax");
			sb.append("\n 				,(SELECT c.namec FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpnamec");
			sb.append("\n 				,(SELECT UPPER(c.namee) FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpnamee");
			sb.append("\n 				,(SELECT c.addressc FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS  corpaddressc");
			sb.append("\n 				,(SELECT c.addresse FROM sys_corporation c , sys_user x  WHERE c.isdelete = FALSE AND x.id = "+uid+" AND x.isdelete = FALSE AND x.corpid = c.id) AS corpaddresse");
			sb.append("\n 				,(SELECT email1 FROM sys_user WHERE id = "+uid+" AND isdelete = FALSE) AS useremail1");
			sb.append("\n 				,(SELECT transitport1 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE) AS transitport1 ");
			sb.append("\n 				,(SELECT transitport2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE) AS transitport2 ");
			sb.append("\n 				,(SELECT flightno4 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE) AS flightno4 ");
			sb.append("\n 				,(SELECT to_char(flightdate4,'dd/MON,yyyy') FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE) AS flightdate4");
			sb.append("\n 				,(SELECT s.doccode FROM bus_air a, air_bookingcode s WHERE a.id = "+billid+" AND a.isdelete = FALSE AND a.agentid = s.airlineid AND s.isdelete = false limit 1) AS doccode ");
			sb.append("\n 				,(SELECT s.bookcode FROM bus_air a, air_bookingcode s WHERE a.id = "+billid+" AND a.isdelete = FALSE AND a.agentid = s.airlineid AND s.isdelete = false limit 1) AS bookcode ");
			sb.append("\n 				,(SELECT chargeweight2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE) AS chargeweight2 ");
			sb.append("\n 				,(SELECT sizeremarks FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE) AS sizeremarks ");
			
			sb.append("\n 				,(CASE WHEN b.ppccothfeepp = 'PP' THEN COALESCE(b.amount,'0') ELSE '' END) AS pp1 ");
			sb.append("\n 				,(CASE WHEN b.ppccothfeepp = 'PP' THEN (SELECT SUM(e.amt) from bus_air_extfeedtl e,bus_air a where a.otherfeepp <> '' AND e.extfeeid = a.extfeeid AND a.id = "+billid+")  ELSE null END) AS pp2 ");
			sb.append("\n 				,(CASE WHEN b.ppccothfeepp = 'PP' THEN COALESCE(b.amount,'0')::numeric + (SELECT SUM(e.amt) from bus_air_extfeedtl e,bus_air a where a.otherfeepp <> '' AND e.extfeeid = a.extfeeid AND a.id = "+billid+") ELSE null END) AS pp3 ");
			sb.append("\n 				,(CASE WHEN b.ppccothfeecc = 'CC' THEN COALESCE(b.amount,'0') ELSE '' END) AS cc1 ");
			sb.append("\n 				,(CASE WHEN b.ppccothfeecc = 'CC' THEN (SELECT SUM(e.amt) from bus_air_extfeedtl e,bus_air a where a.otherfeepp <> '' AND e.extfeeid = a.extfeeid AND a.id = "+billid+")  ELSE null END) AS cc2 ");
			sb.append("\n 				,(CASE WHEN b.ppccothfeecc = 'CC' THEN COALESCE(b.amount,'0')::numeric + (SELECT SUM(COALESCE(e.amt,0))::numeric from bus_air_extfeedtl e,bus_air a where a.otherfeepp <> '' AND e.extfeeid = a.extfeeid AND a.id = "+billid+")::numeric ELSE null END) AS cc3 ");
			sb.append("\n 				,(CASE WHEN b.ppccpaytypepp = 'PP' THEN 'FREIGHT PREPAID' WHEN b.ppccpaytypecc = 'CC' THEN 'FREIGHT COLLECT'  ELSE '' END) AS paytypedesc ");
			sb.append("\n 				,(CASE WHEN b.ppccothfeepp = 'PP' THEN 'FREIGHT PREPAID' WHEN b.ppccothfeecc = 'CC' THEN 'FREIGHT COLLECT'  ELSE '' END) AS othfeetypedesc ");
			
			if("CD".equals(datatype)){
				sb.append("\n 				,(SELECT piece4 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS piece4 ");
				sb.append("\n 				,(SELECT weight4 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS weight4 ");
				sb.append("\n 				,(SELECT volwgt4 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS volwgt4 ");
				sb.append("\n 				,(SELECT volume4 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS volume4 ");
				sb.append("\n 				,(SELECT chargeweight4 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS chargeweight4 ");
			}else{
				sb.append("\n 				,(SELECT piece2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS piece4 ");
				sb.append("\n 				,(SELECT weight2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS weight4 ");
				sb.append("\n 				,(SELECT volwgt2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS volwgt4 ");
				sb.append("\n 				,(SELECT volume2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS volume4 ");
				sb.append("\n 				,(SELECT chargeweight2 FROM bus_air WHERE id = "+billid+" AND isdelete = FALSE)::text AS chargeweight4 ");
			}
			
			sb.append("\n  		FROM _rpt_bill_air b");
			sb.append("\n  		where	b.id = ");
			sb.append(billid);
			sb.append("\n  		) AS T");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			if(map != null && map.size() > 0 && map.containsKey("json")){
				Object obj = map.get("json");
				return obj == null ? "''" : obj.toString();
			}
			return "''";
		}
		return "''";
	}
	
	
	
	private String saveDataAirEdit(HttpServletRequest request, String rpStr,HttpServletResponse response) {
		try {
			InputStream is = request.getInputStream();
			String json = IOUtils.toString(is, "utf-8");
			if(StrUtils.isNull(json)){
				return "";
			}
			JSONObject jsonobject = JSONObject.fromObject(json);
			//System.out.println(jsonobject.toString());
			String tblName = getTable(jsonobject.getString("id"));
			SqlObject sqlObject = new SqlObject(tblName , jsonobject.toString() , "");
			String sql = sqlObject.toSql();
			//System.out.println(sql);
			daoIbatisTemplate.updateWithUserDefineSql(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	
	private String getTable(String id) {
		String busairSQL = "SELECT 1 FROM bus_air WHERE id ="+id;
		List<Map> shipping = daoIbatisTemplate.queryWithUserDefineSql(busairSQL);
		if(shipping!=null && shipping.size() > 0){
			return "bus_air";
		}
		String busairbillSQL = "SELECT 1 FROM bus_air_bill WHERE id ="+id;
		List<Map> busbill = daoIbatisTemplate.queryWithUserDefineSql(busairbillSQL);
		if(busbill!=null && busbill.size() > 0){
			return "bus_air_bill";
		}
		return "";
	}

	private String reverseSaveDataair(HttpServletRequest request, String rpStr,
			HttpServletResponse response) {
		String billid = StrUtils.getSqlFormat(request.getParameter("billid"));
		String targetId = StrUtils.getSqlFormat(request.getParameter("targetId"));
		String content = StrUtils.getSqlFormat(request.getParameter("content"));
		String busairSQL = "SELECT cnortitle,cneetitle,notifytitle,pol,pod,polcode,podcode,flightno1,piece4,weight4,volwgt4,volume4,chargeweight4,goodsdesc,to1,to2,to3,by1,by2,by3,markno,agentdesabbr,other3,other2,podcyid,polcyid,insuranceamt,customeamt,transamt,piece,weight,rateclass,chargeweight,charge,otherfeepp,transitport1,chargeweight2,amount,ppccothfee,ppccpaytype,bookinginfo FROM bus_air WHERE id ="+billid;
		
		String busairbillSQL = "SELECT cnortitle,cneetitle,notifytitle,pol,pod,polcode,podcode,flightno1, goodsdesc,bookinginfo FROM bus_air_bill WHERE id ="+billid;
		try {
			Map shipping = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(busairSQL);
			if(shipping!=null && shipping.size() > 0){
				if(shipping.containsKey(targetId)){
					////System.out.println("old:"+shipping.get(targetId));
					////System.out.println("now:"+content);
					String updateShipping = "UPDATE bus_air SET "+targetId+"='"+content +"' WHERE id="+billid;
					daoIbatisTemplate.updateWithUserDefineSql(updateShipping);
					return "成功替换";
				}else{
					return "无匹配项";
				}
			}
		} catch (NoRowException e) {
			//分单情况
			try {
				Map busbill = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(busairbillSQL);
				if(busbill!=null && busbill.size() > 0){
					if(busbill.containsKey(targetId)){
						////System.out.println("old:"+busbill.get(targetId));
						////System.out.println("now:"+content);
						String updatebusbill = "UPDATE bus_air_bill SET "+targetId+"='"+content +"' WHERE id="+billid;
						daoIbatisTemplate.updateWithUserDefineSql(updatebusbill);
						return "成功替换";
					}else{
						return "无匹配项";
					}
				}
			} catch (NoRowException e2) {
				e.printStackTrace();
				return "定制和分单均未找到";
			} catch (Exception e2) {
				e.printStackTrace();
				return e.getLocalizedMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";
	}
	
	
}
