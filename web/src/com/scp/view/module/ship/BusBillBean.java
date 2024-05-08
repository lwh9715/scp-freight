package com.scp.view.module.ship;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.sys.SysLogDao;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusAir;
import com.scp.model.bus.BusTrain;
import com.scp.model.data.DatAccount;
import com.scp.model.data.DatBank;
import com.scp.model.finance.FinaBill;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipping;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysLog;
import com.scp.model.sys.SysReport;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.ReadExcel;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.finance.ArapEditBean;
import com.scp.view.module.finance.SynUfmsAndBms;

@ManagedBean(name = "pages.module.ship.busbillBean", scope = ManagedBeanScope.REQUEST)
@SuppressWarnings("deprecation")
public class BusBillBean extends GridSelectView {

	@Resource(name="hibTtrTemplate")  
    private TransactionTemplate transactionTemplate;  
	
	@Bind
	@SaveState
	@Accessible
	public Long jobid = -100L;
	
	@SaveState
	@Accessible
	public FinaBill selectdRowData;
	
	@SaveState
	@Accessible
	public Long customer;
	
	@SaveState
	@Accessible
	public String processInstanceId;
	
	@SaveState
	@Accessible
	public String workItemId;
	
	@Bind
	public UICheckBox isprintlock;
	
	@SaveState
	@Accessible
	public Map<String, Object> rateQryMap = new HashMap<String, Object>();
	
	@Bind
	public UIEditDataGrid rateGrid;
	
	@Bind
	private UIWindow exchangeRateWindow;
	
	@Bind
	private Long corpidcurrent;
	
	@SaveState
	public String type;
	
	@Bind
	@SaveState
	public boolean ishold;

	@Bind
	@SaveState
	public boolean isen;
	
	@Bind
	@SaveState
	public FinaJobs finajob;
	
	@Bind
	@SaveState
	public String keys;
	
	@Bind
	public UIWindow cntsWindow;
	
	@Bind
	public UIIFrame cntsIframe;
	
	@Bind(id="save")
	public UIButton save;
	
	@Bind(id="del")
	public UIButton del;
	
	@Bind
	@SaveState
	public Boolean isconfirm = false;
	
	@Bind
	@SaveState
	public Long userid;
	
	@Bind
	public String idsnum;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			init();
			initCtrl();
			super.applyGridUserDef();
			this.corpidcurrent = AppUtils.getUserSession().getCorpidCurrent();
			this.userid = AppUtils.getUserSession().getUserid();
		}
	}
	public void init(){
		String jobid = AppUtils.getReqParam("jobid").trim();
		String billid = AppUtils.getReqParam("id").trim();
		processInstanceId = (String) AppUtils.getReqParam("processInstanceId");
		workItemId = (String) AppUtils.getReqParam("workItemId");
		
		if(!StrUtils.isNull(jobid)){//必须要jobid
			this.jobid = Long.parseLong(jobid);
			try {
				this.finajob = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			} catch (NoRowException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			return;
		}
		if(!StrUtils.isNull(billid)){//编辑
			this.pkId = Long.parseLong(billid);
			try {
				this.selectdRowData = this.serviceContext.billMgrService.finaBillDao.findById(this.pkId);
				if(this.selectdRowData.getIsconfirm()!=null&&this.selectdRowData.getIsconfirm()){
					this.isconfirm = true;
				}
				if(this.selectdRowData.getIscheck()!=null&&this.selectdRowData.getIscheck()){
					this.ischeck = true;
				}
			} catch (NumberFormatException e) {
				MessageUtils.alert("格式转换错误!");
				e.printStackTrace();
			} catch (NoRowException e) {
				MessageUtils.alert("当前账单条目无数据,请联系管理员查询!");
				e.printStackTrace();
			} catch (Exception e) {
				MessageUtils.showException(e);
				e.printStackTrace();
			}
		}else{//新增
			this.add();
		}
		//初始化账单下拉框
		this.initCombox();
		this.billladingnbox();
		this.update.markUpdate(true,UpdateLevel.Data,"editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkId");
	}
	
	@Override
	public void add() {
		this.selectdRowData = new FinaBill();
		this.pkId = -100L;
		this.selectdRowData.setId(-100L);
		this.selectdRowData.setCurrency(AppUtils.getUserSession().getBaseCurrency());
		this.selectdRowData.setJobid(this.jobid);
		this.selectdRowData.setBilldate(Calendar.getInstance().getTime());
		this.selectdRowData.setCorpidtitile(AppUtils.getUserSession().getCorpid());
		//新增设置汇率
		this.exchangeRate();
		update.markUpdate(true, UpdateLevel.Data, "pkId");
	}
	
	@Action
	public void addMaster(){
		if(this.jobid > 0){
			Browser.execClientScript("addBill('"+this.jobid+"')");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map =  super.getQryClauseWhere(queryMap);
		if(map.containsKey("clause")){
			map.remove("clause");
		}
		StringBuffer sb = new StringBuffer();
		if(this.jobid > 0){
			sb.append("\n AND a.jobid = ANY(SELECT "+this.jobid+" UNION SELECT id FROM fina_jobs WHERE parentid = "+this.jobid+")");
		}else{
			map.put("clause","\n AND FALSE");
		}
		if(this.pkId > 0){
			sb.append("\n AND (a.billid = "+this.pkId+" OR a.billid IS NULL OR a.invoiceid <= 0) ");
		}else{
			sb.append("\n AND (a.billid IS NULL OR a.invoiceid <= 0)");
		}
		if(this.selectdRowData != null && this.selectdRowData.getClientid() != null){
			sb.append("\n AND (a.customerid = "+this.selectdRowData.getClientid()+" OR EXISTS (SELECT 1 FROM sys_corporation_join x WHERE x.idto = a.customerid AND x.idfm = "+this.selectdRowData.getClientid()+"))");
		}
		
		sb.append("\n AND (a.parentid IS NULL)");
		
		map.put("clause",sb.toString());
		
		// neo 控制费用显示
		map.put("jobs", "\n (a.id = ANY(SELECT id FROM f_fina_arap_filter('jobid="
				+ this.jobid + ";userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()
				+ "')) "
				+ " OR (a.corpid = 157970752274 AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = "+ this.jobid +" AND isdelete = FALSE AND corpid = " +
						"ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = "+AppUtils.getUserSession().getUserid()+" and ischoose = true))))");
		
		String orderby = ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime");
		if(StrUtils.isNull(orderby) || "N".equals(orderby)){
			map.put("orderSql", "");
		}else{
			map.put("orderSql", "inputtime,");
		}
		
		return map;
	}
	
	

	@Override
	public int[] getGridSelIds(){
		if(this.pkId > 0){
			String orderSql = "";
			if("Y".equals(ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime"))){
				orderSql = "inputtime,";
			}
			
			// neo 控制费用显示
			String filterJobs = "\nAND (a.id = ANY(SELECT id FROM f_fina_arap_filter('jobid=" + this.jobid + ";userid=" + AppUtils.getUserSession().getUserid() + ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"'))"
			+ " OR (a.corpid = 157970752274 AND EXISTS(SELECT 1 FROM fina_jobs WHERE id = "+ this.jobid +" AND isdelete = FALSE AND corpid = " +
			"ANY(SELECT COALESCE(corpid,0) from sys_user_corplink WHERE userid = "+AppUtils.getUserSession().getUserid()+" and ischoose = true))))";
			//filterJobs += "\nAND (t.jobid = " + this.jobid + " OR EXISTS(SELECT 1 FROM fina_jobs x where x.parentid = " + this.jobid + " AND x.id = t.jobid AND x.isdelete = false))";
			
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT (CASE WHEN a.billid is not null then true else false end) AS ischoose");
			sb.append("\n 		FROM _fina_arap AS a");
			sb.append("\n 		WHERE a.isdelete = FALSE");
			sb.append("\n 			AND a.jobid = ANY(SELECT "+jobid+" UNION SELECT id FROM fina_jobs WHERE parentid = "+jobid+")");
			sb.append(filterJobs);
			sb.append("\n			AND a.customerid = ANY(SELECT x.clientid FROM fina_bill x WHERE x.id="+this.pkId+" AND x.isdelete = FALSE)");
			sb.append("\n 	AND (a.billid = "+this.pkId+" OR a.billid IS NULL)");
			if(this.selectdRowData != null && this.selectdRowData.getClientid() != null){
				sb.append("\n AND a.customerid = "+this.selectdRowData.getClientid());
			}
			sb.append("\n ORDER BY " + orderSql + " ordno,jobno,a.araptype DESC,a.ppcc,a.customecode,a.currency DESC , a.feeitemdec");
			List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sb.toString());
			if(list != null && list.size() > 0){
				ArrayList<Integer> li = new ArrayList<Integer>();
				for(int i = 0; i < list.size() ; i++){
					if(Boolean.parseBoolean(list.get(i).get("ischoose").toString())){
						li.add(i);
					}
				}
				int[] tmp = new int[li.size()];
				for (int i =0;i<li.size();i++) {
					tmp[i] = li.get(i);
				}
				return tmp;
			}
			return null;
		}else{
			return null;
		}
	}
	
	@Action
	public void saveAction(){
		this.save();
		this.addMaster();
	}
	
	@Override
	@Transactional
	protected void save() {
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select COUNT(1) AS c from sys_Attachment WHERE isdelete = FALSE AND roleid = 271632352274 AND linkid = "+selectdRowData.getJobid());
			if(Integer.valueOf(String.valueOf(m.get("c"))) == 0){
				try {
					StringBuffer buffer = new StringBuffer();
					buffer.append("SELECT * ");
					buffer.append("\n ,(SELECT namec FROM sys_corporation WHERE id = j.corpid AND isdelete = FALSE) AS 接单公司");
					buffer.append("\n ,(CASE WHEN j.ldtype = 'F' THEN	'FCL' ELSE 'LCL' END) AS 装箱");
					buffer.append("\n ,(SELECT namec FROM sys_user WHERE id = j.saleid AND isdelete = FALSE) as sales");
					buffer.append("\n ,to_char(T.etd2::DATE+ INTERVAL '1 DAY', 'YYYY/MM/DD')||'止' AS etd止");
					buffer.append("\n ,to_char(T.etd2::DATE- INTERVAL '1 DAY', 'YYYY/MM/DD')||'起' AS etd起");
					buffer.append("\n ,(SELECT SUM(f_amtto(a.arapdate, a.currency, 'USD', a.amount))||'USD' FROM fina_arap a WHERE a.jobid = T.jobid AND a.isdelete = FALSE AND feeitemid = ANY(SELECT id FROM dat_feeitem WHERE name = '海运费' and isdelete = FALSE)) AS 海运费");
					buffer.append("\n FROM f_fina_bill_detailed('billid='||"
							+ selectdRowData.getId() + "||';userid='||"
							+ AppUtils.getUserSession().getUserid()
							+ "||';corpidtitile='||100||';') T ");
					buffer.append("\n LEFT JOIN	fina_jobs j ON (j.isdelete = FALSE AND j.id = T.jobid);");
					List<Map> li = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(buffer.toString());
					
					// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
					String exportFileName = "freightQuotation.xlsx";

					// 模版所在路径
					String fromFileUrl = AppUtils.getHttpServletRequest()
							.getSession().getServletContext().getRealPath("")
							+ File.separator
							+ "upload"
							+ File.separator
							+ "finace"
							+ File.separator + exportFileName;
					if (!exportAndUp(new File(fromFileUrl),
							li)) {
//						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select COUNT(1) AS c from sys_Attachment WHERE isdelete = FALSE AND roleid = 615512022274 AND linkid = "+selectdRowData.getJobid());
			if(Integer.valueOf(String.valueOf(m.get("c"))) == 0){
				try {
					StringBuffer buffer = new StringBuffer();
					buffer.append("SELECT *");
					buffer.append("\n ,(SELECT contractno FROM bus_shipping WHERE isdelete = FALSE AND jobid = T.jobid) AS 合约号");
					buffer.append("\n FROM f_fina_bill_detailed('billid='||?||';userid='||?||';corpidtitile='||?||';') T");
					List<Map> li = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(buffer.toString());
					
					// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
					String exportFileName = "客户对账单.xlsx";

					// 模版所在路径
					String fromFileUrl = AppUtils.getHttpServletRequest()
							.getSession().getServletContext().getRealPath("")
							+ File.separator
							+ "upload"
							+ File.separator
							+ "finace"
							+ File.separator + exportFileName;
					if (!exportAndUp2(new File(fromFileUrl),li)) {
//						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
        	try {
    			Object id = transactionTemplate.execute(new TransactionCallback() {
    				@Override
    				public String doInTransaction(TransactionStatus status) {
    	    			if(selectdRowData.getClientid() == null){
    	    				MessageUtils.alert("请选择结算单位!");
    	    				return "";
    	    			}
    	    			if(selectdRowData.getBilldate()==null){
    	    				MessageUtils.alert("账单日期不为空");
    	    				return "";
    	    			}
    	    			
    	    			if(StrUtils.isNull(selectdRowData.getCurrency())){
    	    				MessageUtils.alert("账单币值不为空");
    	    				return "";
    	    			}
//    	    			String billadingno = selectdRowData.getBillladingno();
//    	    			
//    	    			if(!billadingno.matches("^[a-z0-9A-Z]+$")){
//    	    				MessageUtils.alert("提单号不允许含有特殊字符");
//    	    				return "";
//    	    			}
//    	    			selectdRowData.setBillladingno(billadingno);
    	    			
    	    			if(selectdRowData.getId()==-100L){
    	    				serviceContext.billMgrService.finaBillDao.create(selectdRowData);
    	    				pkId = selectdRowData.getId();
    	    				selectdRowData = serviceContext.billMgrService.finaBillDao.findById(selectdRowData.getId());
    	    			}else{
    	    				serviceContext.billMgrService.finaBillDao.modify(selectdRowData);
    	    			}
    	    			StringBuffer sb = new StringBuffer();
    	    			sb.append("UPDATE fina_arap SET billid = NULL,ordno = NULL WHERE isdelete = FALSE AND billid = "+selectdRowData.getId()+" AND jobid=ANY(SELECT "+jobid+" UNION SELECT id FROM fina_jobs WHERE parentid = "+jobid+");");
    	    			String[] idsnym = StrUtils.isNull(idsnum)?null:idsnum.substring(0, idsnum.length()-1).split(";");
    	    			if(idsnym!=null&&idsnym.length>0){//如果记录行有值，才按记录的顺序赋值
    	    				if(keys != null && !keys.isEmpty()){
    		    				String[] ks = keys.split(",");
    		    				for (int i = 0;i< ks.length ; i++) {
    		    					for(String idbum :idsnym){
    		    						String[] idv = idbum.split(":");
//    		        					System.out.println(idv[1].toString());
    		        					if(idv[1].equals(ks[i])){
    		        						sb.append(" UPDATE fina_arap SET billid = "+selectdRowData.getId()
    			    							+",ordno = "+idv[0]+" WHERE isdelete = FALSE AND jobid="+jobid+" AND (id="+ks[i]+" OR parentid in "+ks[i]+");");
    		        					}
    		    					}
    		    				}
    		    			}
    	    			}else{
    	    				if(keys != null && !keys.isEmpty()){
    		    				String[] ks = keys.split(",");
    		    				for (int i = 0;i< ks.length ; i++) {
    		    					sb.append(" UPDATE fina_arap SET billid = "+selectdRowData.getId()+",ordno = "+i+" WHERE isdelete = FALSE AND jobid=ANY(SELECT "+jobid+" UNION SELECT id FROM fina_jobs WHERE parentid = "+jobid+") AND (id="+ks[i]+" OR parentid ="+ks[i]+");");
    		    				}
    		    			}
    	    			}
    	    			//serviceContext.billMgrService.finaBillDao.executeSQL(sb.toString());
    	    			final String  sq = sb.toString();
    	    			SessionFactory sessionFactory = (SessionFactory)AppUtils.getBeanFromSpringIoc("sessionFactory");
    	    			HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
    	    			List list = (List)hibernateTemplate.execute(new HibernateCallback() {
    	    				public Object doInHibernate(Session session)
    	    				throws HibernateException {
    	    					session.flush();
    	    					session.createSQLQuery(sq).executeUpdate();
    	    					return null;
    	    				}
    	    			});
    	    			serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_bus_bill_update('billid="+pkId+";')");
    	    			Browser.execClientScript("showmsg()");
    	    			//保存后跳转到编辑界面
    	    			String jsFunction = "sendRedirect('./busbill.aspx?jobid="+jobid+"&id="+selectdRowData.getId()+"');";
    	    			Browser.execClientScript(jsFunction);
    	    			return "";
    	        	}
    	        });
    	    		}catch (Exception e) {
    	    			MessageUtils.showException(e);
    	    		}
		}
	}
	
	/**
	 * 预览时上传客户对账单
	 * @param fromFile
	 * @param li
	 * @return
	 * @throws Exception
	 */
	public boolean exportAndUp2(File fromFile,List<Map> li) throws Exception {
		if(li.size() == 0){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		
		//导出EXCEL读取sheet模板
		Sheet sheet = wb.getSheetAt(0);
		int count = 0;
		
		//遍历存入数据
		for (int i = 0; i < 20; i++) {
			Map	m = li.get(0);
			Row row = sheet.getRow(i);
			if(null == row){
				sheet.createRow(i);
			}
			
			Cell cell = null;
			//模板每行涉及到合并单元格，空行，都需要单独switch处理
			switch (i) {
				case 0:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("公司抬头中文名")));
					break;
				case 1:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("公司抬头英文名")));
					break;
				case 2:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("公司抬头地址中文")));
					break;
				case 3:
//					cell = row.getCell(1);
//					cell.setCellValue(String.valueOf(m.get("运 费 报 价 单")));
					break;
				case 5:
//					cell = row.getCell(2);
//					cell.setCellValue(String.valueOf(m.get("结算单位")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("账单号")));
					break;
				case 6:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("结算单位")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("账单日期")));
					break;
				case 10:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("船名"))+"/"+String.valueOf(m.get("航次")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("etd")));
					break;
				case 11:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("pol")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("eta")));
					break;
				case 12:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("pod")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("hb号码")));
					break;
				case 13:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("箱量箱型")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("mb号码")));
					break;
				case 14:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("柜号")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("包装"))+"/"+String.valueOf(m.get("毛重"))+"/"+String.valueOf(m.get("体积")));
					break;
				case 15:
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("船公司简称")));
					break;
				case 19:
					BigDecimal usd = new BigDecimal(0);
					BigDecimal cny = new BigDecimal(0);
					BigDecimal hkd = new BigDecimal(0);
					BigDecimal aed = new BigDecimal(0);
					BigDecimal gbp = new BigDecimal(0);
					BigDecimal eur = new BigDecimal(0);
					BigDecimal omr = new BigDecimal(0);
					Map currency = new HashMap();
					for (int j = 19; j < li.size()+19; j++) {
						m = li.get(j-19);
						
						row = sheet.getRow(j);
						if(null == row){
							row = sheet.createRow(j);
						}
						cell = row.createCell(1);
						cell.setCellValue(String.valueOf(m.get("费用中文名")));
						cell = row.createCell(2);
						cell.setCellValue(String.valueOf(m.get("费用备注")));
						cell = row.createCell(3);
						cell.setCellValue(String.valueOf(m.get("数量")));
						cell = row.createCell(4);
						cell.setCellValue(String.valueOf(m.get("含税单价")));
						cell = row.createCell(5);
						cell.setCellValue(String.valueOf(m.get("币制")));
						cell = row.createCell(6);
						cell.setCellValue(String.valueOf(m.get("正负金额")));
						
						if("USD".equals(String.valueOf(m.get("币制")))){
							usd = usd.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("USD", usd);
						}else if("CNY".equals(String.valueOf(m.get("币制")))){
							cny = cny.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("CNY", cny);
						}else if("HKD".equals(String.valueOf(m.get("币制")))){
							hkd = hkd.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("HKD", hkd);
						}else if("AED".equals(String.valueOf(m.get("币制")))){
							aed = aed.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("AED", aed);
						}else if("GBP".equals(String.valueOf(m.get("币制")))){
							gbp = gbp.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("GBP", gbp);
						}else if("EUR".equals(String.valueOf(m.get("币制")))){
							eur = eur.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("EUR", eur);
						}else if("OMR".equals(String.valueOf(m.get("币制")))){
							omr = omr.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("OMR", omr);
						}
						count = j;
					}
					Iterator<Map.Entry<String, BigDecimal>> it = currency.entrySet().iterator();
					
					row = sheet.getRow(count+1);
					if(null == row){
						row = sheet.createRow(count+1);
					}
					cell = row.createCell(4);
					cell.setCellValue("合计:");
					while (it.hasNext()) {
						Map.Entry<String, BigDecimal> entry = it.next();
						count++;
						row = sheet.getRow(count);
						if(null == row){
							row = sheet.createRow(count);
						}
						cell = row.createCell(5);
						cell.setCellValue(String.valueOf(entry.getKey()));
						cell = row.createCell(6);
						cell.setCellValue(String.valueOf(entry.getValue()));
					}
					row = sheet.getRow(count+2);
					if(null == row){
						row = sheet.createRow(count+2);
					}
					cell = row.createCell(4);
					cell.setCellValue("ISSUED BY");
					cell = row.createCell(5);
					cell.setCellValue(String.valueOf(li.get(0).get("用户代码")));
					break;
				default:
					break;
			}
		}
		
		BufferedOutputStream bos = null;
		SysAttachment sysAttachment = null;
		
		sysAttachment = new SysAttachment();
		sysAttachment.setContenttype("application/vnd.ms-excel");
		sysAttachment.setLinkid(selectdRowData.getJobid());
		sysAttachment.setFilename(String.valueOf(li.get(0).get("工作单号"))+ "客户对账单.xlsx");
		sysAttachment.setRoleid(Long.valueOf("615512022274"));
		sysAttachment.setInputer("Auto");
		serviceContext.attachmentService.saveData(sysAttachment);
		
		String attachPath = null;
		try {
			attachPath = AppUtils.getAttachFilePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String originalFileName = sysAttachment.getId() +String.valueOf(li.get(0).get("工作单号"))+ "运费报价单.xlsx";//id+name拼成文件名
		
		File file2local = new File(attachPath + originalFileName);
		bos = new BufferedOutputStream(new FileOutputStream(file2local));
		
		if(!file2local.exists()){
			file2local.createNewFile();
		}
		
		wb.write(bos);
		bos.close();
		return true;
	}
	
	
	@Action
	public void refresh(){
		String jsFunction = "";
		if(this.selectdRowData != null && this.selectdRowData.getId() > 0){
			jsFunction = "sendRedirect('./busbill.aspx?jobid="+this.jobid+"&id="+selectdRowData.getId()+"');";
		}else{
			jsFunction = "sendRedirect('./busbill.aspx?jobid="+this.jobid+"');";
		}
		Browser.execClientScript(jsFunction);
	}
	
	@Action
	public void del() {
		if(this.selectdRowData.getId() < 1){
			MessageUtils.alert("账单还未保存,无需删除!");
			return;
		}
		try {
			String sql = "SELECT COUNT(1) as c :: varchar from  fina_bill  WHERE isdelete = FALSE AND id = "+this.selectdRowData.getId()+" AND corpid = (SELECT corpid FROM sys_user WHERE isdelete = FALSE AND id = " +AppUtils.getUserSession().getUserid() + ")";
			Map<String,String> m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("");
			if("0".equals(m.get("c"))){
				MessageUtils.alert("无权限删除!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			//删账单主表,触发器级联清空明细
			this.serviceContext.billMgrService.removeBillAndDtl(this.selectdRowData.getId());
			Browser.execClientScript("showmsg()");
			String jsFunction = "sendRedirect('./busbill.aspx?jobid="+this.jobid+"');";
			Browser.execClientScript(jsFunction);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showRate(){
		this.resetRate();
	}
	
	@Bind(id="rateGrid")
	public List<Map> getRateGrids() throws Exception{
		List<Map> list = null;
		if(rateQryMap.size() > 0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date billData = this.selectdRowData.getBilldate();
				String billDataStr = sdf.format(billData);
				rateQryMap.put("date", "'"+billDataStr+"'"+"::DATE");
			} catch (Exception e) {
				String billDataStr = sdf.format(Calendar.getInstance().getTime());
				rateQryMap.put("date", "'"+billDataStr+"'"+"::DATE");
				e.printStackTrace();
			}
			
			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.finance.bus.rate.grid", rateQryMap);
		}
		return list;
	}
	@Action
	public void resetRate(){
		if(this.selectdRowData==null || this.selectdRowData.getCurrency()==null || this.selectdRowData.getCurrency().isEmpty()){
			MessageUtils.alert("折合币制不能为空,如界面已选择,还能看到此提示的话请保存重试。");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1){
			MessageUtils.alert("请至少勾选一条费用。");
			return;
		}else{
			StringBuffer sb = new StringBuffer();
			sb.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
			rateQryMap.put("qry", sb.toString());
		}
		rateQryMap.put("date","NOW()");
		
		String filter = "AND x.currencyto = '"+this.selectdRowData.getCurrency()+"' AND x.currencyfm != '"+this.selectdRowData.getCurrency()+"'";
		filter += SaasUtil.filterByCorpid("x");
		
		rateQryMap.put("filter", filter);
		
		
		this.exchangeRateWindow.show();
		this.rateGrid.reload();
	}
	
	@Action
	public void exchangeRate(){
		JSONArray modified = (JSONArray) this.rateGrid.getModifiedData();
		ArrayList<Object> value = (ArrayList<Object>) this.rateGrid.getValue();
		String[] ids = this.grid.getSelectedIds();
		//汇率计算 如果勾选 则只计算勾选 如果无勾选则计算现有列表中ids
		if(ids == null || ids.length < 1){
			StringBuffer sbs = new StringBuffer();
			sbs.append("SELECT a.id FROM _fina_arap AS a WHERE 1=1 ");
			if(this.jobid > 0){
				sbs.append("\n AND a.jobid = "+this.jobid);
			}
			if(this.pkId > 0){
				sbs.append("\n AND (a.billid = "+this.pkId+" OR a.billid IS NULL) ");
			}else{
				sbs.append("\n AND (a.billid IS NULL)");
			}
			if(this.selectdRowData != null && this.selectdRowData.getClientid() != null){
				sbs.append("\n AND a.customerid = "+this.selectdRowData.getClientid());
			}
			List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sbs.toString());
			if(list != null){
				ids = new String[list.size()];
			}
			for (int i = 0;i<list.size();i++) {
				ids[i] = list.get(i).get("id").toString();
			}
		}
		this.serviceContext.arapMgrService.updateRateForBill(value, modified, this.selectdRowData.getCurrency(), ids);
		this.grid.reload();
		this.exchangeRateWindow.close();
	}
	
	@Action(id="currency",event="onselect")
	public void currency_onChange(){
		JSONArray modified = (JSONArray) this.rateGrid.getModifiedData();
		ArrayList<Object> value = (ArrayList<Object>) this.rateGrid.getValue();
		this.serviceContext.arapMgrService.updateRateForBill(value, modified, this.selectdRowData.getCurrency(), this.grid.getSelectedIds());
		this.grid.reload();
		//改币制后自动算一次汇率
		this.exchangeRate();
		if(this.selectdRowData.getId() > 0){
			this.save();
		}
	}
	@Action
	public void changeAccountAction(){
		String accountid = AppUtils.getReqParam("accountid");
		String ishold = AppUtils.getReqParam("ishold");
		String isen = AppUtils.getReqParam("isen");
		boolean isEnAccount = (isen!=null&&isen.equals("true")?true:false);
		if(!accountid.isEmpty()){
			try {
				DatAccount account = this.serviceContext.accountMgrService.datAccountDao.findById(Long.parseLong(accountid));
				DatBank bank = this.serviceContext.bankMgrService.datBankDao.findById(account.getBankid());
				StringBuffer accountBuffer = new StringBuffer();
				MLType mlType = AppUtils.getUserSession().getMlType();
				if(isEnAccount == true || "en".equals(mlType.toString())){
					accountBuffer.append("Account Name:");
					accountBuffer.append(account.getAccountnoe() == null || account.getAccountnoe() == "" ? account.getAccountnoe() : account.getAccountnoe());
					accountBuffer.append("\n");
					accountBuffer.append("Account No.:");
					accountBuffer.append(account.getAccountconte());
					accountBuffer.append("\n");
					accountBuffer.append("Bank:");
					accountBuffer.append(bank.getNamee() == null || bank.getNamee() == "" ? bank.getNamee() : bank.getNamee());
					accountBuffer.append("\n");
					accountBuffer.append("SWIFT CODE:");
					accountBuffer.append(bank.getSwiftcode() == null?"":bank.getSwiftcode());
					accountBuffer.append("\n");
					accountBuffer.append("Address:");
					accountBuffer.append(bank.getAddresse() == null?"":bank.getAddresse());
					
				}else{
					accountBuffer.append("开户行:");
					accountBuffer.append(bank.getNamec() == null || bank.getNamec() == "" ? bank.getNamee() : bank.getNamec());
					accountBuffer.append("\n");
					accountBuffer.append("开户名:");
					accountBuffer.append(account.getAccountno() == null || account.getAccountno() == "" ? account.getAccountnoe() : account.getAccountno());
					accountBuffer.append("\n");
					accountBuffer.append("账号:");
					accountBuffer.append(account.getAccountcont());
				}
				String tmp = this.selectdRowData.getAccountcont() == null ? "" : this.selectdRowData.getAccountcont() + "\n";
				if(ishold.equals("true")){//如果勾选就追加，否则直接赋值
					this.selectdRowData.setAccountcont(tmp + accountBuffer.toString());
				}else {
					this.selectdRowData.setAccountcont(accountBuffer.toString());
				}
				this.selectdRowData.setAccountid(Long.parseLong(accountid));
				this.update.markUpdate(UpdateLevel.Data,"accountcont");
			} catch (NumberFormatException e) {
				MessageUtils.alert("格式转换错误");
				e.printStackTrace();
			} catch (NoRowException e) {
				MessageUtils.alert("所选帐号名下无帐号数据,请联系管理员查验此帐号!");
				e.printStackTrace();
			} catch (NullPointerException e) {
				MessageUtils.alert("所选帐号名下无帐号数据不全,请联系管理员查验此帐号!");
				e.printStackTrace();
			}catch (Exception e) {
				MessageUtils.showException(e);
				e.printStackTrace();
			}
		}
	}
	
	@Bind
	private String qryCustomerKey;
	
	@Bind
	public UIWindow showCustomerWindow;
	
	@Bind
	public UIDataGrid customerGrid;
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@Action
	public void showCustomer() {
		String customercode = "";
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			customerQry();
			Browser.execClientScript("sc.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
				this.selectdRowData.setClientid((Long) cs.get(0).get("id"));
				this.selectdRowData.setClientname(cs.get(0).get("code")
						+ "/" + cs.get(0).get("abbr"));
				this.update.markUpdate(UpdateLevel.Data, "customerid");
				this.update.markUpdate(UpdateLevel.Data, "customercode");

				showCustomerWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
				showCustomerWindow.show();
				customerQry();
				Browser.execClientScript("sc.focus");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}
	
	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		//如果是从结算下面的收据过来的，客户提取所有
		return this.customerService.getCustomerDataProvider(getCustomerids(this.jobid));
		
	}
	@SaveState
	@Accessible
	public String sql;
	
	public String getCustomerids(Long jobid){
		String sql;
//		sql= serviceContext.billMgrService.getCustomerids(jobid);
//		if(StrUtils.isNull(sql)){
//			sql="AND 1=2";
//		}else{
			sql= "\nAND id = ANY(SELECT DISTINCT a.customerid FROM fina_arap a where a.isdelete = false and a.jobid = "+jobid+")";
//		}
		if("N".equals(ConfigUtils.findSysCfgVal("fina_arap_mainjobshidechildren"))){
			sql= "\nAND id = ANY(SELECT DISTINCT a.customerid FROM fina_arap a where a.isdelete = false and a.jobid = ANY(SELECT xx.id FROM fina_jobs xx WHERE isdelete = FALSE AND xx.id = "+jobid+" UNION SELECT xx.id FROM fina_jobs xx WHERE isdelete = FALSE AND xx.parentid = "+jobid+"))";
		}else{
			sql= "\nAND id = ANY(SELECT DISTINCT a.customerid FROM fina_arap a where a.isdelete = false and a.jobid = "+jobid+")";
		}
		return sql;
	}
	@Action
	public void customerGrid_ondblclick() {
		
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
		this.selectdRowData.setClientid((Long) m.get("id"));
		String customercode=(String) m.get("code")+"/"+(String) m.get("abbr");
		this.selectdRowData.setClientname(customercode);
//		this.selectdRowData.setClientitle((String) m.get("abbr") + "/" + (String) m.get("namec"));
//		this.selectdRowData.setClientitle(!StrUtils.isNull((String) m.get("namec"))?(String) m.get("namec"):(String) m.get("namee"));
		
		List<Map> list = null;
		try {
			list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql("SELECT billtxt from  sys_corpcontacts WHERE customerid = "+Long.valueOf(String.valueOf(m.get("id")))+" AND contactype = 'Z' ORDER BY inputtime desc LIMIT 1");
			this.selectdRowData.setClientitle(list.size() > 0 && !StrUtils.isNull(String.valueOf(list.get(0).get("billtxt")))? String.valueOf(list.get(0).get("billtxt")):!StrUtils.isNull((String) m.get("namec"))?(String) m.get("namec"):(String) m.get("namee") );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customercode");
		this.update.markUpdate(UpdateLevel.Data, "client");
		this.update.markUpdate(true,UpdateLevel.Data, "editPanel");
		//this.grid.rebind();
		this.grid.reload();//按照勾选序号调整后rebind 会导致js取不到selectid 已经修改成reload 20170207
		//选择客户后自动设置一次汇率
		this.exchangeRate();
		showCustomerWindow.close();
	}
	/**
	 * 单号下拉
	 */
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> billnos;
	
	public void initCombox() {
		if (this.jobid != null && this.jobid != -100L) {
			List<FinaBill> finabillList = this.serviceContext.billMgrService.getFinaBillListByJobid(this.jobid , AppUtils.getUserSession().getUserid());
			if(finabillList != null && finabillList.size() > 0) {
				List<SelectItem> items = new ArrayList<SelectItem>();
				for(FinaBill fb : finabillList) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String data = sdf.format(fb.getInputtime()==null?Calendar.getInstance().getTime():fb.getInputtime());
					items.add(new SelectItem(fb.getId(), fb.getBillno()+'/'+fb.getInputer()+'/'+data));
				}
				this.billnos = items;
			} else {
				if(this.billnos != null)this.billnos.clear();
				this.add();
			}
		}
	}
	
	@Bind
	@SaveState
	public String detailsContent;
	
	@Bind
	@SaveState
	public String detailsTitle;
	
	@Bind
	public UIWindow detailsWindow;
	
	
	/**
	 * 显示输入框(大框)
	 */
	@Action
	public void showDetailsAction() {
		this.type = AppUtils.getReqParam("type");
		String content = AppUtils.getReqParam("content");
		

		if ("1".equals(type)) { // 账号大框
			this.detailsContent = content;
			this.detailsTitle = "账号";
		}
		this.update.markUpdate(UpdateLevel.Data, "detailsContent");
		this.update.markUpdate(UpdateLevel.Data, "detailsTitle");
		
		this.detailsWindow.show();
		
	}
	/**
	 * 输入框(大框)保存
	 */
	@Action
	public void saveDetails() {
		setDetails(this.type);
		this.save();
		this.detailsWindow.close();
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
		if ("1".equals(type)) { 
			this.selectdRowData.setAccountcont(this.detailsContent);
			this.update.markUpdate(UpdateLevel.Data, "accountcont");
		}
	}

	
	/**
	 * 提单号下拉
	 * */
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> billladingn;
	
	public void billladingnbox(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		BusShipping busShipping = null;
		try {
			busShipping = this.serviceContext.busShippingMgrService.findByjobId(this.jobid);
		} catch (Exception e1) {
//			e1.printStackTrace();
		}
		if (this.jobid != null && this.jobid != -100L) {
			if(this.finajob!=null&&this.finajob.getIsair()){
				
			}else{
				if(busShipping != null && !StrUtils.isNull(busShipping.getHblno())){
					items.add(new SelectItem(busShipping.getHblno(),busShipping.getHblno()));
				}
				if(busShipping != null && !StrUtils.isNull(busShipping.getMblno())){
					items.add(new SelectItem(busShipping.getMblno(),busShipping.getMblno()));
				}
			}
			try{
				String sql="SELECT DISTINCT mblno,hblno FROM bus_ship_bill WHERE isdelete = FALSE AND jobid="+this.jobid;
				List<Map> busShipBills = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
				for(int i=0;i<busShipBills.size();i++){
					String hblno=busShipBills.get(i).get("hblno").toString();
					String mblno=busShipBills.get(i).get("mblno").toString();
					if(!StrUtils.isNull(hblno)){
						items.add(new SelectItem(hblno,hblno));
					}
					if(!StrUtils.isNull(mblno)){
						items.add(new SelectItem(mblno,mblno));
					}
				}
				//加入SO
				if(this.finajob!=null&&!"A".equals(this.finajob.getJobtype())){
					String sono = busShipping!=null?busShipping.getSono():"";
					if(sono!=null&&!StrUtils.isNull(sono)){
						if (sono.length() > 30){
							sono = sono.substring(0,30);
							
						}
						items.add(new SelectItem(sono,sono));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		BusAir busAir = null;
		try {
			busAir = this.serviceContext.busAirMgrService.findByJobId(this.jobid);
			if(!StrUtils.isNull(busAir.getHawbno())){
				items.add(new SelectItem(busAir.getHawbno(),busAir.getHawbno()));
			}
			if(!StrUtils.isNull(busAir.getMawbno())){
				items.add(new SelectItem(busAir.getMawbno(),busAir.getMawbno()));
			}
		} catch (Exception e1) {
//			e1.printStackTrace();
		}
		BusTruck busTruck = null;
		try {
			busTruck = this.serviceContext.busTruckMgrService.findByJobId(this.jobid);
			if(!StrUtils.isNull(busTruck.getSono())){
				items.add(new SelectItem(busTruck.getSono(),busTruck.getSono()));
			}
		} catch (Exception e1) {
		}
		
		BusTrain busTrain = null;
		try {
			busTrain = this.serviceContext.busTrainMgrService.findByjobId(this.jobid);
			if(busTrain != null && !StrUtils.isNull(busTrain.getHblno())){
				items.add(new SelectItem(busTrain.getHblno(),busTrain.getHblno()));
			}
			if(busTrain != null && !StrUtils.isNull(busTrain.getMblno())){
				items.add(new SelectItem(busTrain.getMblno(),busTrain.getMblno()));
			}
		} catch (Exception e1) {
		}
		this.billladingn = items;
	}
	
	
	@Action(id="billno",event="onselect")
	public void billcomboxOnselect(){
		if(this.selectdRowData != null && this.selectdRowData.getBillno() !=null){
			String jsFunction = "sendRedirect('./busbill.aspx?jobid="+this.jobid+"&id="+this.selectdRowData.getBillno()+"');";
			Browser.execClientScript(jsFunction);
		}
	}
	
	@Override
	public void qryRefresh() {
		if (grid != null) {
			this.grid.reload();
		}
	}
	
	
	
	//以下为处理报表部分
	@Bind(id = "billformat")
	public List<SelectItem> getBillformat() {
		try {
			String modcode = null;
			//this.finajob!=null && this.finajob.getJobtype() != null && "A".equals(this.finajob.getJobtype()) ? "arapbill_air" : "arapbill";
			if(this.finajob!=null && this.finajob.getJobtype() != null && "A".equals(this.finajob.getJobtype())){
				modcode = "arapbill_air"; //空运格式
			}else if(this.finajob!=null && this.finajob.getJobtype() != null && "S".equals(this.finajob.getJobtype())){
				modcode = "arapbill";	//海运格式
			}else if(this.finajob!=null && this.finajob.getJobtype() != null && "L".equals(this.finajob.getJobtype())){
				modcode = "arapbill_bus";	//陆运格式
			}else if(this.finajob!=null && this.finajob.getJobtype() != null && "C".equals(this.finajob.getJobtype())){
				modcode = "arapbill_cus";	//报关格式
			}else if(this.finajob!=null && this.finajob.getJobtype() != null && "D".equals(this.finajob.getJobtype())){
				modcode = "arapbill_commerce";	//电商格式
			} else if(this.finajob!=null && this.finajob.getJobtype() != null && "H".equals(this.finajob.getJobtype())){
				modcode = "arapbill_train";	//铁运格式
			}else{
				modcode = "arapbill_other";	//临时其他格式
			}
			List<SelectItem> list = CommonComBoxBean.getComboxItems("d.id", "d.namec ",
					"sys_report AS d", "WHERE modcode='"+modcode+"' AND isdelete = FALSE",
			"order by namec");
			return list;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	public void isprintlockAjaxSubmit() {
		Boolean isprintlock = selectdRowData.getIsprinted();
		if(isprintlock == true){
			MessageUtils.alert("不能手动锁定");
			selectdRowData.setIsprinted(false);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			return;
		}
		String sql = "UPDATE fina_bill  SET isprinted = " + isprintlock
				+ " ,updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW() WHERE id = " + this.pkId+";";

		try {
			serviceContext.billMgrService.finaBillDao.executeSQL(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	public String getReportName(){
		if(this.selectdRowData.getReportid() == null) {
			return null;
		}
		
		SysReport sy = serviceContext.sysReportMgrService.sysReportDao.findById(this.selectdRowData.getReportid());
		return sy == null ? null : sy.getFilename();
	}
	
	@Action
	public void scanReportHipint() {
		if(getReportName() == null) {
			MessageUtils.alert("请选择收据格式！");
			return;
		}
		String rpturl = "/scp/reportWeb/designertext.html?rp="+getReportName();
		AppUtils.openWindow("_scanReportHipint", rpturl + getArgs());
	}
	
	/**
	 * 预览报表 --没有控制
	 */
	@Action
	public void scanReport() {
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select COUNT(1) AS c from sys_Attachment WHERE isdelete = FALSE AND roleid = 271632352274 AND linkid = "+selectdRowData.getJobid());
			if(Integer.valueOf(String.valueOf(m.get("c"))) == 0){
				try {
					StringBuffer buffer = new StringBuffer();
					buffer.append("SELECT * ");
					buffer.append("\n ,(SELECT namec FROM sys_corporation WHERE id = j.corpid AND isdelete = FALSE) AS 接单公司");
					buffer.append("\n ,(CASE WHEN j.ldtype = 'F' THEN	'FCL' ELSE 'LCL' END) AS 装箱");
					buffer.append("\n ,(SELECT namec FROM sys_user WHERE id = j.saleid AND isdelete = FALSE) as sales");
					buffer.append("\n ,to_char(T.etd2::DATE+ INTERVAL '1 DAY', 'YYYY/MM/DD')||'止' AS etd止");
					buffer.append("\n ,to_char(T.etd2::DATE- INTERVAL '1 DAY', 'YYYY/MM/DD')||'起' AS etd起");
					buffer.append("\n ,(SELECT SUM(f_amtto(a.arapdate, a.currency, 'USD', a.amount))||'USD' FROM fina_arap a WHERE a.jobid = T.jobid AND a.isdelete = FALSE AND feeitemid = ANY(SELECT id FROM dat_feeitem WHERE name = '海运费' and isdelete = FALSE)) AS 海运费");
					buffer.append("\n FROM f_fina_bill_detailed('billid='||"
							+ selectdRowData.getId() + "||';userid='||"
							+ AppUtils.getUserSession().getUserid()
							+ "||';corpidtitile='||100||';') T ");
					buffer.append("\n LEFT JOIN	fina_jobs j ON (j.isdelete = FALSE AND j.id = T.jobid);");
					List<Map> li = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(buffer.toString());
					
					// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
					String exportFileName = "freightQuotation.xlsx";

					// 模版所在路径
					String fromFileUrl = AppUtils.getHttpServletRequest()
							.getSession().getServletContext().getRealPath("")
							+ File.separator
							+ "upload"
							+ File.separator
							+ "finace"
							+ File.separator + exportFileName;
					if (!exportAndUp(new File(fromFileUrl),
							li)) {
//						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("Select COUNT(1) AS c from sys_Attachment WHERE isdelete = FALSE AND roleid = 615512022274 AND linkid = "+selectdRowData.getJobid());
			if(Integer.valueOf(String.valueOf(m.get("c"))) == 0){
				try {
					StringBuffer buffer = new StringBuffer();
					buffer.append("SELECT *");
					buffer.append("\n ,(SELECT contractno FROM bus_shipping WHERE isdelete = FALSE AND jobid = T.jobid) AS 合约号");
					buffer.append("\n FROM f_fina_bill_detailed('billid='||"
							+ selectdRowData.getId() + "||';userid='||"
							+ AppUtils.getUserSession().getUserid()
							+ "||';corpidtitile='||100||';') T");
					List<Map> li = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(buffer.toString());
					
					// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
					String exportFileName = "客户对账单.xlsx";

					// 模版所在路径
					String fromFileUrl = AppUtils.getHttpServletRequest()
							.getSession().getServletContext().getRealPath("")
							+ File.separator
							+ "upload"
							+ File.separator
							+ "finace"
							+ File.separator + exportFileName;
					if (!exportAndUp2(new File(fromFileUrl),li)) {
//						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(getReportName() == null) {
				MessageUtils.alert("请选择收据格式！");
				return;
			}
			String dir = "ship/";
			if(this.finajob!=null && this.finajob.getJobtype() != null && "L".equals(this.finajob.getJobtype())){
				dir = "land/";	//陆运格式
			}else if(this.finajob!=null && this.finajob.getJobtype() != null && "C".equals(this.finajob.getJobtype())){
				dir = "declare/";	//报关格式
			}
			
			
			String rpturl = AppUtils.getRptUrl();
			for (String s : AppUtils.getUserRoleModuleCtrl(900000L)){
				if (s.endsWith("eportdesign")) {
					String openUrl = rpturl + "/reportJsp/showReport_finabill.jsp?design=true&raq=/"+dir
					+ getReportName();
					AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
					return;
				} 
			}
			String openUrl = rpturl + "/reportJsp/showReport_finabill.jsp?design=false&raq=/"+dir
					+ getReportName();
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
		}
	}
	
	public boolean exportAndUp(File fromFile,List<Map> li) throws Exception {
		if(li.size() == 0){
			return false;
		}
		Workbook wb = ReadExcel.createWb(fromFile);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) 1);
		cellStyle.setVerticalAlignment((short) 0);
		cellStyle.setWrapText(true);
		
		//导出EXCEL读取sheet模板
		Sheet sheet = wb.getSheetAt(0);
		int count = 0;
		
		//遍历存入数据
		for (int i = 0; i < 16; i++) {
			Map	m = li.get(0);
			Row row = sheet.getRow(i);
			if(null == row){
				sheet.createRow(i);
			}
			
			Cell cell = null;
			//模板每行涉及到合并单元格，空行，都需要单独switch处理
			switch (i) {
				case 0:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("公司抬头中文名")));
					break;
				case 1:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("公司抬头英文名")));
					break;
				case 2:
					cell = row.getCell(1);
					cell.setCellValue(String.valueOf(m.get("公司抬头地址中文")));
					break;
				case 3:
//					cell = row.getCell(1);
//					cell.setCellValue(String.valueOf(m.get("运 费 报 价 单")));
					break;
				case 5:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("结算单位")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("提单公司联系人")));
					break;
				case 6:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("sales")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("当前日期")));
					break;
				case 10:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("pol")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("pod")));
					break;
				case 11:
					cell = row.getCell(2);
					cell.setCellValue(String.valueOf(m.get("装箱")));
					cell = row.getCell(5);
					cell.setCellValue(String.valueOf(m.get("船公司中文名")));
					break;
				case 15:
					BigDecimal usd = new BigDecimal(0);
					BigDecimal cny = new BigDecimal(0);
					BigDecimal hkd = new BigDecimal(0);
					BigDecimal aed = new BigDecimal(0);
					BigDecimal gbp = new BigDecimal(0);
					BigDecimal eur = new BigDecimal(0);
					BigDecimal omr = new BigDecimal(0);
					Map currency = new HashMap();
					for (int j = 15; j < li.size()+15; j++) {
						m = li.get(j-15);
						
						row = sheet.getRow(j);
						if(null == row){
							row = sheet.createRow(j);
						}
						cell = row.createCell(1);
						cell.setCellValue(String.valueOf(m.get("费用中文名")));
						cell = row.createCell(2);
						cell.setCellValue(String.valueOf(m.get("费用备注")));
						cell = row.createCell(3);
						cell.setCellValue(String.valueOf(m.get("数量")));
						cell = row.createCell(4);
						cell.setCellValue(String.valueOf(m.get("含税单价")));
						cell = row.createCell(5);
						cell.setCellValue(String.valueOf(m.get("币制")));
						cell = row.createCell(6);
						cell.setCellValue(String.valueOf(m.get("正负金额")));
						
						if("USD".equals(String.valueOf(m.get("币制")))){
							usd = usd.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("USD", usd);
						}else if("CNY".equals(String.valueOf(m.get("币制")))){
							cny = cny.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("CNY", cny);
						}else if("HKD".equals(String.valueOf(m.get("币制")))){
							hkd = hkd.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("HKD", hkd);
						}else if("AED".equals(String.valueOf(m.get("币制")))){
							aed = aed.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("AED", aed);
						}else if("GBP".equals(String.valueOf(m.get("币制")))){
							gbp = gbp.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("GBP", gbp);
						}else if("EUR".equals(String.valueOf(m.get("币制")))){
							eur = eur.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("EUR", eur);
						}else if("OMR".equals(String.valueOf(m.get("币制")))){
							omr = omr.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("正负金额")))));
							currency.put("OMR", omr);
						}
						count = j;
					}
					Iterator<Map.Entry<String, BigDecimal>> it = currency.entrySet().iterator();
					
					row = sheet.getRow(count+1);
					if(null == row){
						row = sheet.createRow(count+1);
					}
					cell = row.createCell(4);
					cell.setCellValue("合计:");
					while (it.hasNext()) {
						Map.Entry<String, BigDecimal> entry = it.next();
						count++;
						row = sheet.getRow(count);
						if(null == row){
							row = sheet.createRow(count);
						}
						cell = row.createCell(5);
						cell.setCellValue(String.valueOf(entry.getKey()));
						cell = row.createCell(6);
						cell.setCellValue(String.valueOf(entry.getValue()));
					}
					row = sheet.getRow(count+2);
					if(null == row){
						row = sheet.createRow(count+2);
					}
					cell = row.createCell(1);
					cell.setCellValue("备注：");
					row = sheet.createRow(count+3);
					cell = row.createCell(1);
					cell.setCellValue("以上运价有效期从"+String.valueOf(li.get(0).get("etd起"))+String.valueOf(li.get(0).get("etd止")));
					row = sheet.createRow(count+6);
					cell = row.createCell(4);
					cell.setCellValue("署名：");
					cell = row.createCell(5);
					cell.setCellValue(String.valueOf(li.get(0).get("接单公司")));
					row = sheet.createRow(count+7);
					cell = row.createCell(4);
					cell.setCellValue("报价日期：");
					cell = row.createCell(5);
					cell.setCellValue(String.valueOf(li.get(0).get("当前日期")));
					break;
				default:
					break;
			}
		}
		
		BufferedOutputStream bos = null;
		SysAttachment sysAttachment = null;
		
		sysAttachment = new SysAttachment();
		sysAttachment.setContenttype("application/vnd.ms-excel");
		sysAttachment.setLinkid(selectdRowData.getJobid());
		sysAttachment.setFilename(String.valueOf(li.get(0).get("工作单号"))+ "运费报价单.xlsx");
		sysAttachment.setRoleid(Long.valueOf("271632352274"));
		sysAttachment.setInputer("Auto");
		serviceContext.attachmentService.saveData(sysAttachment);
		
		String attachPath = null;
		try {
			attachPath = AppUtils.getAttachFilePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String originalFileName = sysAttachment.getId() +String.valueOf(li.get(0).get("工作单号"))+ "运费报价单.xlsx";//id+name拼成文件名
		
		File file2local = new File(attachPath + originalFileName);
		bos = new BufferedOutputStream(new FileOutputStream(file2local));
		
		if(!file2local.exists()){
			file2local.createNewFile();
		}
		
		wb.write(bos);
		bos.close();
		return true;
	}
	
	@Bind
	public UIWindow onlineLinkWindow;
	
	@Action
	public void onlineLink(){
		StringBuilder sb = new StringBuilder();
		long currentTimeMillis = System.currentTimeMillis();//时间戳
		
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport_finabill.jsp?raq=/ship/"
				+ getReportName();
		String url = openUrl + getArgs();
		
		
		String billno = this.selectdRowData.getBillno();
		
		update.markUpdate(true,UpdateLevel.Data,"hxurl");
		onlineLinkWindow.show();
		Browser.execClientScript("billno.setValue('"+billno+"');testUrl.setValue('"+url+"');refreshQRCode();");
	}

	/**
	 * 打印报表 --控制
	 */
	@Action
	public void printReport() {
		if (selectdRowData.getIsprinted()) {
			MessageUtils.alert("您已打印过改收据报表,请先解锁");
			return;
		}
		if(getReportName() == null) {
			MessageUtils.alert("请选择收据格式！");
			return;
		}
		//String val = ConfigUtils.findUserSysCfgVal("rpt_srv_url");
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport_finabill.jsp?raq=/ship/"
				+ getReportName();
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
		String sql = "UPDATE fina_bill SET isprinted  = TRUE ,updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW() WHERE id = "
				+ this.pkId+";";
		//ApplicationUtils.debug(sql);
		try {
			serviceContext.billMgrService.finaBillDao.executeSQL(sql);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	private String getArgs() {
		String args = "";
		args += "&billid=" + this.pkId;

		args += "&userid=" + AppUtils.getUserSession().getUserid();
		args += "&corpidtitile=" + this.selectdRowData.getCorpidtitile();
		args += "&currency=" + this.selectdRowData.getCurrency();
		// args+="&isencode = 'N'";
		return args;
	}     

	@Action
	public void showmessage() {
		if (this.jobid != null && this.jobid != -1) {
			String sql = "SELECT max(t.cneetitle) FROM bus_ship_bill t WHERE t.bltype = 'H' AND t.isdelete = FALSE AND t.jobid = "
					+ this.jobid;  
			Map map = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			String cneetitle;
			try {
				cneetitle = map.get("max").toString();
				if (cneetitle == null || "".equals(cneetitle)) {
					MessageUtils.alert("提单收货人信息为空!");
					return;
				}
				String onecneetitle = cneetitle.split("\n")[0];
				this.selectdRowData.setClientitle(onecneetitle);
			} catch (Exception e) {
				MessageUtils.alert("HBL提单尚未建立,无法获取提单中收货人信息!");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭整个流程
	 */
	@Action
	public void closeProcess(){
//		if (!StrUtils.isNull(processInstanceId)) {
//			try {
//				IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//				IProcessInstance processInstance = workflowSession
//						.findProcessInstanceById(processInstanceId);
//				processInstance.abort();
//				//MessageUtils.alert("OK!整个流程已关闭");
//				Browser.execClientScript("showmsg()");
//			} catch (EngineException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (KernelException e) {
//				MessageUtils.alert(e.getErrorMsg());
//				e.printStackTrace();
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
//		}
	}
	@Action
	public void refreshAcountCont() {
		Long customerid = Long.parseLong(AppUtils.getReqParam("customerid"));
		String customercode = (String) AppUtils.getReqParam("customercode");
		String customernamec = (String) AppUtils.getReqParam("customernamec");
		
		this.customer = customerid;
		this.selectdRowData.setClientid(customerid);
		this.selectdRowData.setClientname(customernamec);
		this.selectdRowData.setClientitle(customernamec);
		
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customercode");
		this.update.markUpdate(UpdateLevel.Data, "customertitle");
		
	}
	@Action
	public void showCnts(){
		if(this.selectdRowData == null || this.selectdRowData.getId() <= 0){
			MessageUtils.alert("请先保存账单!");
			return;
		}
		String url = "./busbillcnts.aspx?jobid="+this.selectdRowData.getJobid()+"&billid="+this.selectdRowData.getId();
		this.cntsIframe.load(url);
		this.cntsWindow.show();
	}
	
	@Action
	public void isconfirm_oncheck(){
//		save();
		try {
			String sql;
			if(this.selectdRowData == null || this.selectdRowData.getId() <=0){
				alert("请先保存");
				if(isconfirm = true){
					isconfirm = false;
				}else{
					isconfirm = true;
				}
				return;
			}
			if(isconfirm){
				// boolean flag = billConfirm(null);//确认前优先BMS确认
				// if(!flag){//确认失败
				// 	isconfirm = false;
				// 	return;
				// }
				sql = "UPDATE fina_bill SET isconfirm = TRUE , confirmdate = now() ,updater = '"+AppUtils.getUserSession().getUsercode()+"' ,updatetime = NOW(), confirmer = '"+AppUtils.getUserSession().getUsercode()+"' WHERE id = "+this.selectdRowData.getId()+";";
			}else{
				// boolean flag = billConfirmCancle(null);//取消确认前需要先取消BMS确认
				// if(!flag){//取消确认失败
				// 	isconfirm = true;
				// 	return;
				// }
				sql = "UPDATE fina_bill SET isconfirm = FALSE , confirmdate = NULL ,updater = '"+AppUtils.getUserSession().getUsercode()+"' ,updatetime = NOW(), confirmer = NULL WHERE id = "+this.selectdRowData.getId()+";";
			}
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			this.selectdRowData.setIsconfirm(this.selectdRowData.getIsconfirm()!=null?(!this.selectdRowData.getIsconfirm()):true);
			this.selectdRowData.setConfirmer(isconfirm?AppUtils.getUserSession().getUsername():null);
			this.selectdRowData.setConfirmdate(isconfirm?Calendar.getInstance().getTime():null);
			initCtrl();
		} catch (Exception e) {
			if(isconfirm = true){
				isconfirm = true;
			}else{
				isconfirm = false;
			}
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	//勾选了确认后，保存，删除按钮灰掉。
	private void initCtrl() {
		save.setDisabled(false);
		del.setDisabled(false);
		if(isconfirm){
			save.setDisabled(true);
			del.setDisabled(true);
		}
		if(ischeck){
			save.setDisabled(true);
			del.setDisabled(true);
		}
	}
	
	@Bind
	@SaveState
	public Boolean ischeck = false;
	
	@Action
	public void ischeck_oncheck(){
//		save();
		
		try {
			if(this.selectdRowData == null || this.selectdRowData.getId() <=0){
				alert("请先保存");
				if(ischeck = true){
					ischeck = false;
				}else{
					ischeck = true;
				}
				return;
			}
			String sql;
			if(ischeck){
				sql = "UPDATE fina_bill SET ischeck = TRUE , checkdate = now() ,updater = '"+AppUtils.getUserSession().getUsercode()+"' ,updatetime = NOW(), checker = '"+AppUtils.getUserSession().getUsercode()+"' WHERE id = "+this.selectdRowData.getId()+";";
			}else{
				sql = "UPDATE fina_bill SET ischeck = FALSE , checkdate = NULL ,updater = '"+AppUtils.getUserSession().getUsercode()+"' ,updatetime = NOW(), checker = NULL WHERE id = "+this.selectdRowData.getId()+";";
			}
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			initCtrl();
			this.selectdRowData.setIscheck(this.selectdRowData.getIscheck()!=null?(!this.selectdRowData.getIscheck()):true);
			this.selectdRowData.setChecker(ischeck?AppUtils.getUserSession().getUsername():null);
			this.selectdRowData.setCheckdate(ischeck?Calendar.getInstance().getTime():null);
		} catch (Exception e) {
			if(ischeck = true){
				ischeck = true;
			}else{
				ischeck = false;
			}
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIWindow setBillnosWindow;
	
	@Action
	public void creatbillno() {
		this.billnogrid.reload();
		setBillnosWindow.show();
	}
	
	@Bind
	public UIDataGrid billnogrid;
	
	@SaveState
	@Accessible
	public Map<String, Object> bookqryMap = new HashMap<String, Object>();
	
	public Map getQryClauseWhereBook(Map<String, Object> queryMap) {
		Map map = new HashMap();
		String qry = "\n 1=1";
		map.put("qry", qry);
		return map;
	}
	
	@Bind(id = "billnogrid", attribute = "dataProvider")
	protected GridDataProvider getBookDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".billnogrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
				.queryForList(sqlId, getQryClauseWhereBook(bookqryMap)).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".billnogrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhereBook(bookqryMap));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	
	
	@SaveState
	public String codes = "fina_bill_nos";
	
	@Action
	public void chooseBillnos(){
		String[] ids = this.billnogrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String querySql  = "SELECT code FROM sys_busnodesc WHERE id = "+ids[0];
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			codes = StrUtils.getMapVal(m, "code");
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		Date sodate = selectdRowData.getBilldate();
		if(sodate == null){
			alert("账单日期不能为空");
			setBillnosWindow.close();
			return;
		}
		if(!StrUtils.isNull(selectdRowData.getBillno())){
			alert("账单号已生成");
			setBillnosWindow.close();
			return;
		}
		try {
			SimpleDateFormat dayformat = new SimpleDateFormat("yyyy-MM-dd");
			String soDateStr = dayformat.format(sodate);
			String querySqlw = "SELECT f_auto_busno('code="+codes+";date="+soDateStr+";corpid="+this.selectdRowData.getCorpidtitile()+"') AS billno;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySqlw);
			String billno = StrUtils.getMapVal(map, "billno");
			Browser.execClientScript("billnoJs.setValue('"+billno+"')");
			setBillnosWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void billnogrid_ondblclick() {
		chooseBillnos();
	}
	
	/**
	 * BMS账单确认
	 */
	@Action
	public boolean billConfirm(FinaBill selectedRowData){
		if(null != selectedRowData){
			this.selectdRowData = selectedRowData;
		}
		if(null == serviceContext){
			this.serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		}
		long id = this.selectdRowData.getId();
		if (id < 1) {
			MessageUtils.alert("Please choose one!");
			return false;
		}
		String querySql = "SELECT a.id,a.bcfreightid,a.billid AS bizledgercompid,araptype AS rpflag,b.currency AS currencycode" +
				",(SELECT SUM(CASE WHEN aa.currency='CNY' THEN aa.amount ELSE aa.billxrate*aa.amount END) FROM _fina_arap aa WHERE aa.isdelete = FALSE AND aa.billid = a.billid)::numeric(18,2) AS amount,to_char(now()" +
				",'yyyyMMddHHmmss') AS confirmdate,b.inputer AS creator,TO_CHAR(b.inputtime,'yyyyMMddHHmmss') AS createtime" +
				",(select (SELECT xxx.mzccode FROM sys_corpext xxx WHERE xxx.customerid = xx.id) from sys_corporation xx WHERE xx.id = a.corpid AND xx.isdelete = false) AS settleoffice" +
				",(select xx.namec from sys_corporation xx WHERE xx.id = a.corpid AND xx.isdelete = false) AS settleofficename" +
				",(SELECT cc.custcode FROM sys_corpext cc WHERE cc.customerid = c.id) AS settlecustcode,c.namec AS settleCustName,b.billno AS  bizcompno " +
				"FROM fina_arap a LEFT JOIN fina_bill b ON (b.isdelete = FALSE AND b.id = a.billid) LEFT JOIN sys_corporation c ON (c.isdelete = FALSE AND c.id = a.customerid) WHERE a.isdelete = FALSE AND a.billid = "+id+";";
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try {
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql.toString());
			
			if(null != list && list.size() > 0){
				//账单确认前先保证费用审核
				ArapEditBean arapBean = new ArapEditBean();
				String[] ids = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					if(null == list.get(i).get("id")){
						continue;
					}
					ids[i] = String.valueOf(list.get(i).get("id"));
				}
				arapBean.auditByIds(ids,jobid, serviceContext);//费用审核
				
				boolean flag = billConfirmCancle(null);//账单确认之前需要先给账单取消确认，否则可能重复确认
				if(flag){
					flag = synUfmsAndBms.synCheckBillsToBms(list, serviceContext);
				}
				if(!flag){
					return flag;
				}
			}
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
			return false;
		}
		return true;
	}
	
	/**
	 * BMS账单取消确认
	 */
	@Action
	public boolean billConfirmCancle(FinaBill selectedRowData){
		if(null != selectedRowData){
			this.selectdRowData = selectedRowData;
		}
		if(null == serviceContext){
			this.serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
		}
		long id = this.selectdRowData.getId();
		if (id < 1) {
			MessageUtils.alert("Please choose one!");
			return false;
		}
		String querySql  = "SELECT id AS bizledgercompid,billno AS bizcompno  FROM fina_bill WHERE isdelete = false and id = "+id;
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		try {
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql.toString());
			
			if(null != list && list.size() > 0){
				boolean flag = synUfmsAndBms.cancleCheckBillsToBms(list, serviceContext);
				if(!flag){
					return flag;
				}
			}
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
			return false;
		}
		return true;
	}
}
