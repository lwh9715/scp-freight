package com.scp.view.module.ship;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.developer.util.FacesUtils;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.faces.util.Flash;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.dao.cache.CommonDBCache;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmTask;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.model.data.DatAccount;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaInvoice;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysCorpinv;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.Sysformcfg;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridSelectView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.finance.InvoiceDtlBean;

@ManagedBean(name = "pages.module.ship.businvoiceBean", scope = ManagedBeanScope.REQUEST)
public class BusInvoiceBean extends EditGridSelectView {

    @Resource(name = "hibTtrTemplate")
    private TransactionTemplate transactionTemplate;

    @Bind
    @SaveState
    private Long jobid = -100L;

    @Bind
    @SaveState
    private String araptype;

    @Bind
    @SaveState
    private String billid;

    @Bind
    @SaveState
    private String invoiceid;

    @Bind
    @SaveState
    private String filterArapType;

    @Bind
    @SaveState
    private String customerid;

    @Bind
    @SaveState
    private String arapids;

    @SaveState
    @Accessible
    public FinaInvoice selectdRowData;

    @Bind
    @SaveState
    public String sono;

    @Bind
    public UIEditDataGrid rateGrid;

    @SaveState
    @Accessible
    public Map<String, Object> rateQryMap = new HashMap<String, Object>();

    @Bind
    private UIWindow exchangeRateWindow;

    @Bind
    public UIWindow updateInvoiceamountflagWindow;

    @Bind
    @SaveState
    @Accessible
    private double tmpinvoiceamountflag;

    @Bind
    @SaveState
    public String xtype;

    @Bind
    @SaveState
    public String xrate;

    @Bind
    public UIButton unionFee;

    @Bind
    public UIButton invoiceSetup;

    @Bind
    public UIButton invoicePrint;

    @Bind
    public UIButton save;

    @Bind
    public UIButton del;


    @Bind
    @SaveState
    public String actionJsText;

    @Bind
    @SaveState
    public String isOpenInvHT_E; //系统设置是否开通航天电子发票接口

    @Bind
    @SaveState
    public String isOpenInvHT_Page; //系统设置是否开通航天纸质发票接口

    @Bind
    @SaveState
    public String onlyShowUSD = "true";
    @Bind
    @SaveState
    public String onlyShowCNY = "true";
    @Bind
    @SaveState
    public String onlyShowHKD = "true";
    @Bind
    @SaveState
    public String onlyShowhavataxrate = "true";
    @Bind
    @SaveState
    public String onlyShownohavataxrate = "true";
    
    @Bind
    @SaveState
    public String emarlAddress = "";
    
    @Bind
    @SaveState
    public String inputter = "";

    @Bind
    @SaveState
    public String customername = "";
    
    @Bind
    @SaveState
    public String taxpayernumber = "";
    
    @Bind
    @SaveState
    public String address = "";
    
    @Bind
    @SaveState
    public String accountbankandaccountnumber = "";
    @Override
    public void beforeRender(boolean isPostBack) {
        super.beforeRender(isPostBack);
        if (!isPostBack) {
            init();
            initCtrl();

            String type = AppUtils.getReqParam("type");
            actionJsText = "";
            if ("bpm".equals(type)) {
                String taskid = AppUtils.getReqParam("taskid");
                if (!StrUtils.isNull(taskid)) {
                    BpmTask bpmTask = serviceContext.bpmTaskService.bpmTaskDao.findById(Long.valueOf(taskid));
                    List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = " + bpmTask.getProcessid() + " AND taskname = '" + bpmTask.getTaskname() + "' AND actiontype = 'js'");
                    for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
                        actionJsText += bpmWorkItem.getActions();
                    }
                } else {
                    String processid = AppUtils.getReqParam("processid");
                    List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = " + processid + " AND taskname = 'Start' AND actiontype = 'js'");
                    for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
                        actionJsText += bpmWorkItem.getActions();
                    }
                }
                update.markUpdate(true, UpdateLevel.Data, "actionJsText");
            }

            List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao.findAllByClauseWhere(" formid = '" + this.getMBeanName() + "' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
            for (Sysformcfg sysformcfg : sysformcfgs) {
                actionJsText += sysformcfg.getJsaction();
            }
            update.markUpdate(true, UpdateLevel.Data, "actionJsText");
            
            try {
                Map<String,String> m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COALESCE(email1,email2) AS email,namec from sys_user WHERE isdelete = false and code = '"+this.selectdRowData.getInputer()+"' limit 1");
                emarlAddress = String.valueOf(m.get("email"));
                inputter = String.valueOf(m.get("namec"));
            } catch (Exception e) {
				e.printStackTrace();
			}
            
            initinv();
        }
    }
    
    /**
     * 查询开票信息
     */
    public void initinv(){
    	if(null != selectdRowData && null != selectdRowData.getClientid() && selectdRowData.getClientid() > 0){
    		try {
            	String sql = "SELECT " +
            			"\n invnamec,licno,addressc,bankandnumber,''''||isaffirm||'''' as isaffirm" +
                    	"\n FROM sys_corpinv c " +
                    	"\n WHERE linkid = " +selectdRowData.getClientid() + "  ORDER BY COALESCE(updatetime,inputtime) DESC LIMIT 1";
                Map<String,String> m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
                customername = String.valueOf(m.get("invnamec"));
                taxpayernumber = String.valueOf(m.get("licno"));
                address = String.valueOf(m.get("addressc"));
                accountbankandaccountnumber = String.valueOf(m.get("bankandnumber"));
                isaffirm = "'true'".equals(m.get("isaffirm"))?true:false;
            } catch (Exception e) {
        		e.printStackTrace();
        	}
    	}
    }
    

    public void init() {
        unionFee.setDisabled(true);
        this.invoiceid = AppUtils.getReqParam("id");
        this.customerid = AppUtils.getReqParam("customerid");
        String jobid = AppUtils.getReqParam("jobid");
        if (jobid != null && !jobid.isEmpty()) {//必须要jobid
            this.jobid = Long.parseLong(jobid);
        } else {
            return;
        }
        if (FacesUtils.getFlash().get("invoiceids") != null) {
            this.arapids = FacesUtils.getFlash().get("invoiceids").toString();
        }
        Flash flash = FacesUtils.getFlash();
        flash.remove("invoiceids");
        if (this.invoiceid != null && !this.invoiceid.isEmpty()) {
            this.selectdRowData = this.serviceContext.invoiceMgrService.finaInvoiceDao.findById(Long.parseLong(invoiceid));
            if (selectdRowData == null || selectdRowData.getInvoicetype() == null || !"U".equalsIgnoreCase(selectdRowData.getInvoicetype())) {
                unionFee.setDisabled(false);
            }
        } else {
            //filterArapType = "AR";
            this.selectdRowData = new FinaInvoice();
            this.selectdRowData.setId(-1L);
            this.selectdRowData.setEditype("E");
            this.selectdRowData.setCurrency("CNY");
            // SysCorporation customer = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.parseLong(this.customerid));
            // this.selectdRowData.setClientid(customer.getId());
            // this.selectdRowData.setClientname(customer.getNamec());
            // this.selectdRowData.setClientitle(customer.getNamec());
            // this.selectdRowData.setInvoicedate(Calendar.getInstance().getTime());
            //2093 综合发票修改 如果勾选的是同一种币制，页面币制默认为此币制
            if (!StrUtils.isNull(arapids)) {
                String sqlcurency =
                        "SELECT DISTINCT currency FROM fina_arap WHERE id = ANY(regexp_split_to_array('" + arapids + "0',',')::BIGINT[])";
                try {
                    List<Map> m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sqlcurency);
                    if (m != null && m.size() == 1) {
                        this.selectdRowData.setCurrency(m.get(0).get("currency").toString());
                    }
                } catch (Exception e) {
                    MessageUtils.showException(e);
                }
            }
            this.selectdRowData.setProductname("A");
            //2641 2.默认值：按fina_invoice.corpid 取上一个设置了值的作为 这三个的默认值
            this.selectdRowData.setInvoicedate(Calendar.getInstance().getTime());
            String querysql =
                    "\nSELECT payee,checker,issuer,COALESCE(accountid,0) AS accountid,accountcont,productname,editype " +
                            "\nFROM fina_invoice " +
                            "\nWHERE corpid = " + AppUtils.getUserSession().getCorpid() +
                            "\nAND COALESCE(payee,'') <> '' AND COALESCE(checker,'') <> '' " +
                            "\nAND COALESCE(issuer,'') <> '' AND accountid > 0 and COALESCE(accountcont,'') <> '' " +
                            "\nAND COALESCE(productname,'') <> '' " +
                            "\nORDER BY id DESC LIMIT 1;";
            try {
                Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querysql);
                if (m != null && m.size() > 0) {
                    this.selectdRowData.setPayee(StrUtils.getMapVal(m, "payee"));
                    this.selectdRowData.setChecker(StrUtils.getMapVal(m, "checker"));
                    this.selectdRowData.setIssuer(StrUtils.getMapVal(m, "issuer"));
                    this.selectdRowData.setAccountid(Long.parseLong(StrUtils.getMapVal(m, "accountid")));
                    this.selectdRowData.setAccountcont(StrUtils.getMapVal(m, "accountcont"));
                    this.selectdRowData.setProductname(StrUtils.getMapVal(m, "productname"));
                    this.selectdRowData.setEditype(StrUtils.getMapVal(m, "editype"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initXrate(selectdRowData.getCurrency(), selectdRowData.getInvoicedate());
        this.initCombox();
        this.update.markUpdate(true, UpdateLevel.Data, "selectdRowData");
        String only_show_account = ConfigUtils.findUserCfgVal("only_show_account", AppUtils.getUserSession().getUserid());
        if ("Y".equals(only_show_account)) {
            onlyShowAccount = true;
        }
        String only_show_writeoff = ConfigUtils.findUserCfgVal("only_show_writeoff", AppUtils.getUserSession().getUserid());
        if ("Y".equals(only_show_writeoff)) {
            onlyShowWriteoff = true;
        }

        String show_payable = ConfigUtils.findUserCfgVal("show_payable", AppUtils.getUserSession().getUserid());
        if ("Y".equals(show_payable)) {
            this.showPayable = true;
        }else{
            this.showPayable = false;
        }

        identity = ConfigUtils.findUserCfgVal("invoice_identity", AppUtils.getUserSession().getUserid());
        kptype = ConfigUtils.findUserCfgVal("invoice_kptype", AppUtils.getUserSession().getUserid());
        fphxz = ConfigUtils.findUserCfgVal("invoice_fphxz", AppUtils.getUserSession().getUserid());

        isOpenInvHT_E = ConfigUtils.findSysCfgVal("inv_ht_e");
        isOpenInvHT_Page = ConfigUtils.findSysCfgVal("inv_ht_page");

        araptype = "";
        this.update.markUpdate(UpdateLevel.Data, "araptype");
        
        //发票显示备注查询
        String qsql = "SELECT   SUM(amount) as amount,currency,(SELECT (CASE WHEN xtype = '*' THEN rate ELSE 1/rate END) as rate from  dat_exchangerate WHERE now() BETWEEN datafm AND datato AND currencyfm = a.currency AND currencyto = 'CNY'  ORDER BY datato LIMIT 1) FROM  fina_arap a WHERE invoiceid  = "+this.selectdRowData.getId()+" GROUP BY currency";
	    try {
	        List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(qsql);
	        if(list.size() == 1){
	        	if("USD".equals(list.get(0).get("currency"))){
	        		remarks2	= String.valueOf(list.get(0).get("currency"))+":"+new BigDecimal(String.valueOf(list.get(0).get("amount")))+"   "
					+"汇率:"+Double.valueOf(String.valueOf(list.get(0).get("rate")))+"   \n"
					+"请按美金金额付款";
	        	}else{
	        		remarks2 = String.valueOf(list.get(0).get("currency"))+":"+new BigDecimal(String.valueOf(list.get(0).get("amount")));
	        	}
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

    /*
     * 刷折合币制兑换率到界面上 2024-01-31 将发票金额的汇率由业务汇率改为发票汇率
     */
    private void initXrate(String currencyfm, Date date) {
        if (currencyfm == "CNY") {
            xtype = "*";
            xrate = "1";
        } else {
            if (date == null) {
                date = new Date();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String sqlcurency = "SELECT x.rate ,x.xtype FROM dat_exchangerate x WHERE x.ratetype = 'inv' AND x.isdelete = false " +
                            " AND x.currencyfm = '" + currencyfm + "' AND x.currencyto = 'CNY' AND '" + df.format(date) + "' BETWEEN x.datafm AND x.datato AND x.isdelete = FALSE;";
            Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlcurency);
            if (m != null) {
                xtype = StrUtils.getMapVal(m, "xtype");
                xrate = StrUtils.getMapVal(m, "rate");

            }
        }
        this.update.markUpdate(true, UpdateLevel.Data, "xtype");
        this.update.markUpdate(true, UpdateLevel.Data, "xrate");
    }

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        if (map.containsKey("clause")) {
            map.remove("clause");
        }
        if (map.containsKey("sonoFilter")) {
            map.remove("sonoFilter");
        }
        if (!StrUtils.isNull(StrUtils.getSqlFormat(this.sono))) {
            map.put("sonoFilter", "AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.sono IS NOT NULL AND x.sono <> '' AND x.jobid = a.jobid AND x.sono ILIKE '%" + StrUtils.getSqlFormat(this.sono) + "%')");
        }

        StringBuffer sb = new StringBuffer();
        if (this.jobid > 0) {
            sb.append("\n AND a.jobid = " + this.jobid);
        } else {
            map.put("clause", "\n AND FALSE");
        }
        if (!StrUtils.isNull(this.invoiceid) && Long.parseLong(this.invoiceid) > 0) {
            sb.append("\n AND (a.invoiceid = " + this.invoiceid + " OR invoiceid IS NULL OR invoiceid <= 0) ");
        } else {
            sb.append("\n AND (invoiceid IS NULL OR invoiceid <= 0)");
            String onlyShowAccount = ConfigUtils.findUserCfgVal("only_show_account", AppUtils.getUserSession().getUserid());
            if ("Y".equals(onlyShowAccount)) {
                //2899 发票部分修改 同一个工作单下，同一个费用名称，araptype相同，金额绝对值相同，一个正式一个负数，负数的是增减费用，如果满足条件，则这两条费用都过滤
                map.put("accountfilter", "AND(CASE WHEN t.amount <0 THEN NOT EXISTS(SELECT 1 FROM rc_arap WHERE araptype = t.araptype AND feeitemid = t.feeitemid AND t.amount <0 AND t.isamend = true AND t.customerid = customerid AND amount = t.amount*-1)" +
                        "\n ELSE NOT EXISTS(SELECT 1 FROM rc_arap WHERE araptype = t.araptype AND feeitemid = t.feeitemid AND amount <0 AND isamend = true AND t.customerid = customerid AND amount = t.amount*-1) END)");
            }
            String onlyShowwriteoff = ConfigUtils.findUserCfgVal("only_show_writeoff", AppUtils.getUserSession().getUserid());
            if ("Y".equals(onlyShowwriteoff)) {
                map.put("onlyShowwriteoff", "\n AND payplace = 'PB' AND COALESCE(amtstl2,0) = 0");
            }
        }
        if (this.selectdRowData != null && this.selectdRowData.getClientid() != null) {
            sb.append("\n AND (a.customerid = " + this.selectdRowData.getClientid() + " OR EXISTS (SELECT 1 FROM sys_corporation_join x WHERE x.idto = a.customerid AND x.idfm = " + this.selectdRowData.getClientid() + "))");
        }
        map.put("clause", sb.toString());

        // neo 控制费用显示
        map.put("jobs", "EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid="
                + this.jobid + ";userid="
                + AppUtils.getUserSession().getUserid()
                + ";corpidcurrent=" + AppUtils.getUserSession().getCorpidCurrent()
                + "') x WHERE x.id = a.id)");

        //显示应付费用
        String showPayable = ConfigUtils.findUserCfgVal("show_payable", AppUtils.getUserSession().getUserid());
        if ("Y".equals(showPayable)) {
            map.put("arapfilter", " AND araptype = 'AR'");
        }

        map.put("cyto", "'" + this.selectdRowData.getCurrency() + "'");

        if (!StrUtils.isNull(selectedIdsStr)) {
            map.put("selectedIdsStr", "AND A.ID ::text in (select regexp_split_to_table('" + selectedIdsStr + "', ',') )");
        }

        String currencyparame = " AND a.currency= ANY(SELECT 'USD' UNION SELECT 'HKD' UNION SELECT 'CNY') ";
        if (!"true".equals(onlyShowUSD) || !"true".equals(onlyShowHKD) || !"true".equals(onlyShowCNY)) {
        	if(!"true".equals(onlyShowUSD)){
        		currencyparame += " AND a.currency != 'USD' ";
        	}
        	if(!"true".equals(onlyShowHKD)){
        		currencyparame += " AND a.currency != 'HKD' ";
        	}
        	if(!"true".equals(onlyShowCNY)){
        		currencyparame += " AND a.currency != 'CNY' ";
        	}
        }
        map.put("currencyparame", currencyparame);

        if ("true".equals(onlyShowhavataxrate) && "true".equals(onlyShownohavataxrate)) {
            map.put("taxrateparame", "AND (true) ");
        } else if ("true".equals(onlyShowhavataxrate) && "false".equals(onlyShownohavataxrate)) {
            map.put("taxrateparame", "AND ((1-a.taxrate) !=0) ");
        } else if ("false".equals(onlyShowhavataxrate) && "true".equals(onlyShownohavataxrate)) {
            map.put("taxrateparame", "AND ((1-a.taxrate) =0) ");
        } else if ("false".equals(onlyShowhavataxrate) && "false".equals(onlyShownohavataxrate)) {
            map.put("taxrateparame", "AND (false) ");
        }
        return map;
    }

    @Override
    public int[] getGridSelIds() {
        if (this.invoiceid != null && !this.invoiceid.isEmpty()) {

            if(selectdRowData!=null && !"U".equalsIgnoreCase(selectdRowData.getInvoicetype())){
                StringBuffer sb = new StringBuffer();
                sb.append("\n SELECT (CASE WHEN a.invoiceid is not null then true else false end) AS ischoose");
                sb.append("\n 		FROM _fina_arap AS a");
                sb.append("\n 		WHERE a.isdelete = FALSE");
                sb.append("\n 			AND a.jobno IS NOT NULL");
                sb.append("\n 			AND a.jobno != ''");
                sb.append("\n			AND EXISTS( SELECT * FROM fina_invoice x WHERE x.id="+ this.invoiceid
                        + " AND x.isdelete = FALSE AND x.clientid = a.customerid)");
                sb.append("\n 	AND (a.invoiceid = " + this.invoiceid
                        + " OR a.invoiceid IS NULL)");
                sb.append("\n ORDER BY a.invoiceid,a.araptype DESC,a.jobdate,a.arapdate");
                List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sb.toString());
                if (list != null && list.size() > 0) {
                    ArrayList<Integer> li = new ArrayList<Integer>();
                    for (int i = 0; i < list.size(); i++) {
                        if (Boolean.parseBoolean(list.get(i).get("ischoose").toString())) {
                            li.add(i);
                        }
                    }
                    int[] tmp = new int[li.size()];
                    for (int i = 0; i < li.size(); i++) {
                        tmp[i] = li.get(i);
                    }
                    return tmp;
                }
            }else{
                int tmp[] = {0};
                return tmp;
            }

            return null;
        } else {//2093 综合发票修改  默认自动全选
            String sqlcurency = "SELECT count(1) FROM fina_arap WHERE id = ANY(regexp_split_to_array('"+(arapids==null?"0":arapids+"0")+"',',')::BIGINT[])";
            ArrayList<Integer> li = new ArrayList<Integer>();
            try{
                Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlcurency);
                if(m!=null){
                    String count = m.get("count").toString();
                    int countint = Integer.parseInt(String.valueOf(count));
                    for (int i = 0; i < countint; i++) {
                        li.add(i);
                    }
                    int[] tmp = new int[li.size()];
                    for (int i = 0; i < li.size(); i++) {
                        tmp[i] = li.get(i);
                    }
                    return tmp;
                }
            }catch(Exception e){
                MessageUtils.showException(e);
            }
            return null;
        }
    }

    @Bind
    @SaveState
    public String total;

    @Action
    public void saveAmountBtn() {
        if (!StrUtils.isNull(invoiceid)) {
            try {
                String sql = "UPDATE fina_invoice set ismodifyamountto = TRUE , amountto = " + selectdRowData.getAmountto() + " WHERE id = " + invoiceid + ";";
                serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(sql);
                //System.out.println(total);
                MessageUtils.alert("OK!");
            } catch (Exception e) {
                e.printStackTrace();
                MessageUtils.showException(e);
            }
        }

    }
    
	@Bind
	public boolean consolidatedInvoices = false;

	@Action
	public void consolidatedInvoicesAction(){
		String str = AppUtils.getReqParam("consolidatedInvoices");
		consolidatedInvoices = Boolean.valueOf(str);
	}
    
    @Bind
    @SaveState
    public String selectedIdsStr;
    
    /**
     * 此备注只计算显示，不存入数据库，用于财务复制粘贴
     */
    @Bind
    @SaveState
    public String remarks2 = "";	

    @Override
    @Transactional
    public void save() {
        try {
            String[] ids = editGrid.getSelectedIds();
            boolean bl = null == selectdRowData.getBusinvoiceconfirm() ? false : selectdRowData.getBusinvoiceconfirm();
            if(!StrUtils.isNull(checkStatus())){
            	MessageUtils.alert(checkStatus());
                return;
            }
            if (ids == null || ids.length == 0) {
                MessageUtils.alert("保存失败,必须勾选费用!");
                return;
            }else {
                selectedIdsStr = StringUtils.join(ids, ",");
                String sqlId = getMBeanName() + ".grid.page";
                List<Map> aalist = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMap), 0, 100000);
                Set<String> thiset = new HashSet<String>();
                for (Map thismap : aalist) {
                    thiset.add(String.valueOf(thismap.get("taxrate2")));
                }
                if (thiset.size() >1) {
                    MessageUtils.alert("税率必须一致!");
                    return;
                }
            }
            if (selectdRowData.getClientid() == null) {
                MessageUtils.alert("请选择结算单位!");
                return;
            }
            if (selectdRowData.getInvoicedate() == null) {
                MessageUtils.alert("请选择日期!");
                return;
            }
            
            
            //根据币制生成一到多条发票
            String qrysql =	"SELECT (SELECT (CASE WHEN xtype = '*' THEN rate ELSE 1/rate END) as rate from  dat_exchangerate WHERE now() BETWEEN datafm AND datato AND currencyfm = currency AND currencyto = 'CNY'  ORDER BY datato LIMIT 1)" +
            		"\n,currency" +
            		"\n,sum(amount) as amount,''''||STRING_AGG(id||'',',')||'''' as ids" +
            		"\n,(SELECT nos FROM fina_jobs WHERE id = jobid AND isdelete =FALSE limit 1)" +
            		"\n FROM fina_arap  " +
            		"\nWHERE isdelete = FALSE " +
            		"\nAND jobid=" + jobid + 
    				"\nAND id IN(" + StrUtils.array2List(ids) + ") " +
					"\ngroup BY currency,jobid;";
            List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(qrysql);
            String invoiceno = selectdRowData.getInvoiceno();
//            remarks2 = "";
            if(!consolidatedInvoices && list.size() > 1 && selectdRowData.getId() == -1L){
            	if(list.size() > 1 && selectdRowData.getId() == -1L){
            		for (int i = 0; i < list.size(); i++) {
            			String rate = String.valueOf(list.get(i).get("rate"));
            			BigDecimal amount = new BigDecimal(String.valueOf(list.get(i).get("amount")));
            			selectdRowData.setCurrency("CNY");
            			selectdRowData.setAmounts(amount.multiply(new BigDecimal(rate)));
            			selectdRowData.setId(0l);
            			selectdRowData.setJobid(jobid);
            			if(StrUtils.isNull(invoiceno)){
            				selectdRowData.setInvoiceno(String.valueOf(list.get(i).get("nos"))+"-"+String.valueOf(list.get(i).get("currency"))+InvoiceDtlBean.uuid());
            			}else{
            				selectdRowData.setInvoiceno(invoiceno+"-"+String.valueOf(list.get(i).get("currency"))+InvoiceDtlBean.uuid());
            			}
//            			if("USD".equals(list.get(i).get("currency"))){
//            				remarks2	= String.valueOf(list.get(i).get("currency"))+":"+new BigDecimal(String.valueOf(list.get(i).get("amount")))+"   \n"
//								+"汇率:"+rate+"   \n"
//								+"请按美金金额付款";
//            			}else{
//            				remarks2 = String.valueOf(list.get(i).get("currency"))+":"+new BigDecimal(String.valueOf(list.get(i).get("amount")));
//            			}
            			
            			serviceContext.invoiceMgrService.finaInvoiceDao.create(selectdRowData);
                        invoiceid = String.valueOf(selectdRowData.getId());
                        selectdRowData = serviceContext.invoiceMgrService.finaInvoiceDao.findById(selectdRowData.getId());
                        
//                        String ss = "update fina_invoice set jobid =" + jobid + " where id=" + selectdRowData.getId() + ";";
//                        serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(ss);
                        
                        StringBuffer sb = new StringBuffer();
                        sb.append("\nUPDATE fina_arap SET invoiceid = NULL WHERE isdelete = FALSE AND invoiceid = " + invoiceid + " AND jobid=" + jobid + ";");
                        if (null != list.get(i).get("ids") && !StrUtils.isNull(String.valueOf(list.get(i).get("ids")))) {
                            sb.append("\nUPDATE fina_arap SET invoiceid = " + invoiceid + " WHERE isdelete = FALSE AND jobid=" + jobid + " AND id =ANY(regexp_split_to_array(" + String.valueOf(list.get(i).get("ids")) + ",',')::BIGINT[]);");
                        }
                        serviceContext.daoIbatisTemplate.queryWithUserDefineSql(String.valueOf(sb));
    				}
            	}
            }else{
            	String rate = "";
        		for (int i = 0; i < list.size(); i++) {
					if("USD".equals(String.valueOf(list.get(i).get("currency")))){
						rate = String.valueOf(list.get(i).get("rate"));
					}
				}
        		String[] totals = total.split("\n");
    			String t = "";
    			for (int i = 0; i < totals.length; i++) {
					if(totals[i].indexOf("USD")>-1){
						t = totals[i];
					}
				}
            	
//    			if(selectdRowData.getInvoiceno().indexOf("USD")>-1){
//        			remarks2 = t+   "\n" +"汇率:"+rate+"   \n" + "请按美金金额付款";
//    			}else{
//    				remarks2 = selectdRowData.getCurrency()+":"+selectdRowData.getAmounts();
//    			}
            	
            	if(StrUtils.isNull(invoiceno)){
        			selectdRowData.setInvoiceno(String.valueOf(list.get(0).get("nos"))+"-"+String.valueOf(list.get(0).get("currency"))+InvoiceDtlBean.uuid());
        		}
        		if (selectdRowData.getId() == -1L) {
//        			selectdRowData.setInvoiceno(String.valueOf(list.get(0).get("nos"))+"-"+String.valueOf(list.get(0).get("currency"))+InvoiceDtlBean.uuid());
            		serviceContext.invoiceMgrService.finaInvoiceDao.create(selectdRowData);
            		invoiceid = String.valueOf(selectdRowData.getId());
            		selectdRowData = serviceContext.invoiceMgrService.finaInvoiceDao.findById(selectdRowData.getId());
            	} else {
            		serviceContext.invoiceMgrService.finaInvoiceDao.modify(selectdRowData);
            	}
            	
            	String ss = "update fina_invoice set jobid =" + jobid + " where id=" + selectdRowData.getId() + ";";
            	serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(ss);
            	
            	StringBuffer sb = new StringBuffer();
            	sb.append("\nUPDATE fina_arap SET invoiceid = NULL WHERE isdelete = FALSE AND invoiceid = " + invoiceid + " AND jobid=" + jobid + ";");
            	if (ids != null && ids.length > 0) {
            		sb.append("\nUPDATE fina_arap SET invoiceid = " + invoiceid + " WHERE isdelete = FALSE AND jobid=" + jobid + " AND id IN(" + StrUtils.array2List(ids) + ");");
            	}
            	serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(String.valueOf(sb));	
        	}

        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        String jsFunction = "sendRedirect('./businvoice.aspx?jobid=" + jobid + "&id=" + selectdRowData.getId() + "');";
        Browser.execClientScript(jsFunction);
    }

    public String checkStatus(){
    	String msg = "";
    	boolean bl = null == selectdRowData.getBusinvoiceconfirm() ? false : selectdRowData.getBusinvoiceconfirm();
        if(bl == true){
        	msg = "保存失败,发票已确认!";
        }
        return msg;
    }
    
    @Action
    public void del() {
        if(!StrUtils.isNull(checkStatus())){
        	MessageUtils.alert(checkStatus());
            return;
        }
        if (this.selectdRowData.getId() < 1) {
            MessageUtils.alert("发票还未保存,无需删除!");
            return;
        }
        // 删发票主表,触发器级联清空明细
        this.serviceContext.invoiceMgrService
                .removeInvoiceAndDtl(this.selectdRowData.getId());
        MessageUtils.alert("OK!");
        Browser.execClientScript("sendRedirect('./businvoice.aspx?id=" + "" + "&jobid=" + jobid + "');");
    }

    @Action
    public void showRate() {
        this.resetRate();
    }

    @Bind(id = "rateGrid")
    public List<Map> getRateGrids() throws Exception {
        List<Map> list = null;
        if (rateQryMap.size() > 0) {
            list = this.daoIbatisTemplate.getSqlMapClientTemplate()
                    .queryForList("pages.module.finance.invoiceBean.grid.rate",
                            rateQryMap);
        }
        return list;
    }

    @Action
    public void resetRate() {
        if (this.selectdRowData == null
                || this.selectdRowData.getCurrency() == null
                || this.selectdRowData.getCurrency().isEmpty()) {
            MessageUtils.alert("折合币制不能为空,如界面已选择,还能看到此提示的话请保存重试。");
            return;
        }
        //		String[] ids = this.editGrid.getSelectedIds();
        //		if (ids == null || ids.length < 1) {
        //			MessageUtils.alert("请至少勾选一条费用。");
        //			return;
        //		} else {
        //			StringBuffer sb = new StringBuffer();
        ////			sb.append(" AND (FALSE ");
        ////			for (String s : ids) {
        ////				sb.append(" OR a.id = " + s);
        ////			}
        ////			sb.append(" )");
        //			sb.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
        //			rateQryMap.put("qry", sb.toString());
        //		}
        rateQryMap.put("date", "NOW()");


        if (selectdRowData != null && selectdRowData.getInvoicedate() != null) {
            Date date = selectdRowData.getInvoicedate();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = df.format(date);
            if (!StrUtils.isNull(dateStr)) {
                rateQryMap.put("date", "'" + dateStr + "'");
            }
        }

        rateQryMap.put("filter", "AND x.currencyto = '"
                + this.selectdRowData.getCurrency() + "' AND x.currencyfm != '"
                + this.selectdRowData.getCurrency() + "'");
        this.rateGrid.reload();
        this.exchangeRateWindow.show();
    }

    @Action
    public void exchangeRate() {
        //		JSONArray modified = (JSONArray) this.rateGrid.getModifiedData();
        //		ArrayList<Object> value = (ArrayList<Object>) this.rateGrid.getValue();
        //		this.serviceContext.arapMgrService.updateRateForInvoice(value,modified, this.selectdRowData.getCurrency(), this.editGrid.getSelectedIds());
        //		this.editGrid.reload();
        //		this.exchangeRateWindow.close();
    }

    @Action(id = "currency", event = "onselect")
    public void currency_onChange() {
        //		JSONArray modified = (JSONArray) this.rateGrid.getModifiedData();
        //		ArrayList<Object> value = (ArrayList<Object>) this.rateGrid.getValue();
        //		this.serviceContext.arapMgrService.updateRateForInvoice(value,modified, this.selectdRowData.getCurrency(), this.editGrid.getSelectedIds());
        String[] ids = this.editGrid.getSelectedIds();
        String sql = "UPDATE fina_arap SET invoiceid = NULL , invoicextype = NULL , invoicexrate = NULL , invoiceamount = NULL  WHERE isdelete = FALSE AND id IN(" + StrUtils.array2List(ids) + ");";
        serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(sql);
        Browser.execClientScript("amounttoJsvar.setValue(null);");
        this.editGrid.reload();
        try {
            initXrate(selectdRowData.getCurrency(), selectdRowData.getInvoicedate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //		if (selectdRowData!=null && selectdRowData.getId() > 0) {
        //			this.save();
        //		}
    }

    @Action
    public void changeAccountAction() {
        String accountid = AppUtils.getReqParam("accountid");
        if (!accountid.isEmpty()) {
            try {
                DatAccount account = this.serviceContext.accountMgrService.datAccountDao.findById(Long.parseLong(accountid));
                if (account != null) {
                    this.selectdRowData.setAccountcont(account.getAccountcont());
                    this.update.markUpdate(UpdateLevel.Data, "accountcont");
                }
            } catch (NumberFormatException e) {
                MessageUtils.alert("格式转换错误:STRING PARSE LONG ERROR");
                e.printStackTrace();
            } catch (NoRowException e) {
                MessageUtils.alert("所选帐号名下无帐号数据,请联系管理员查验此帐号!");
                e.printStackTrace();
            } catch (NullPointerException e) {
                MessageUtils.alert("所选帐号名下无帐号数据不全,请联系管理员查验此帐号!");
                e.printStackTrace();
            } catch (Exception e) {
                MessageUtils.showException(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void grid_ondblclick() {
        try {
            this.tmpinvoiceamountflag = this.serviceContext.arapMgrService.finaArapDao.findById(Long.parseLong(this.editGrid.getSelectedIds()[0])).getInvoiceamount().doubleValue();
            update.markUpdate(true, UpdateLevel.Data, "tmpinvoiceamountflag");
            updateInvoiceamountflagWindow.show();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void exchangeinvoiceamountflag() {
        try {
            FinaArap arap = this.serviceContext.arapMgrService.finaArapDao.findById(Long.parseLong(this.editGrid.getSelectedIds()[0]));
            arap.setInvoiceamount(new BigDecimal(tmpinvoiceamountflag));
            this.serviceContext.arapMgrService.finaArapDao.modify(arap);
            MessageUtils.alert("OK!");
            updateInvoiceamountflagWindow.close();
            Browser.execClientScript("location.reload();");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void unionFee() {
        if (selectdRowData == null || invoiceid.isEmpty()) {
            MessageUtils.alert("保存发票后才能生成综合费用!");
            return;
        }
        if (!"U".equalsIgnoreCase(selectdRowData.getInvoicetype())) {
            serviceContext.invoiceMgrService.transferToUnionInvoice(Long.parseLong(invoiceid));
            Browser.execClientScript("sendRedirect('./businvoice.aspx?id=" + selectdRowData.getId() + "&jobid=" + jobid + "&invid=" + selectdRowData.getId() + "');");
        } else {
            MessageUtils.alert("已生成综合费用!");
        }

    }

    @Bind
    public UIIFrame corpinvIframe;

    @Action
    public void showsyscorpinv() {
        try {
            if (this.selectdRowData.getClientid() != null && this.selectdRowData.getId() != 0) {
                corpinvIframe.load("../customer/syscorpinv.xhtml?id=" + this.selectdRowData.getClientid() + "&invoiceid=" + this.selectdRowData.getId());
                Browser.execClientScript("showSyscorpinvJsVar.show()");
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Bind
    public UIButton showDynamic;

    @Action
    public void showDynamic() {
        String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length == 0 || ids.length > 1) {
            MessageUtils.alert("请选择一条记录");
        } else {
            try {
                FinaArap arap = serviceContext.arapMgrService.finaArapDao.findById(Long.parseLong(ids[0]));
                String url = AppUtils.getContextPath();
                String openurl = url + "/pages/module/jobs/jobsinfo.xhtml";
                AppUtils.openWindow("_showDynamic", openurl + "?jobid=" + arap.getJobid()
                        + "&userid=" + AppUtils.getUserSession().getUserid());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Resource
    public CommonDBCache commonDBCache;

    /**
     * 账号
     *
     * @return
     */
    @Bind(id = "account")
    public List<SelectItem> getAccount() {
        try {
            String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid =" + AppUtils.getUserSession().getUserid() + "))";
            return commonDBCache.getComboxItems("d.id", "d.code||'/'||  COALESCE(d.banknamec,'') ||'/'||  COALESCE(d.accountno,'')", "_dat_account AS d", "WHERE 1=1 " + qry, " ORDER BY d.code");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Action
    public void splitIn() {
        String sql = "SELECT * FROM f_fina_invoice_split('invoiceid=" + selectdRowData.getId() + ";userid=" + AppUtils.getUserSession().getUserid() + "')";
        try {
            serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            alert("OK");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void removeSplit() {
        try {
            String inspectsql = "SELECT EXISTS(SELECT 1 FROM fina_invoice " +
                    "WHERE isdelete = FALSE AND isinvsvr = TRUE AND iscancel = FALSE AND parentid = " + selectdRowData.getId() + ") AS inspectsql";
            Map inspectm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(inspectsql);
            if (inspectm != null && (inspectm.get("inspectsql")).toString().equals("true")) {
                alert("请先作废子单再取消拆分！");
                return;
            }
            String sql = "UPDATE fina_invoice SET isdelete = TRUE WHERE parentid = " + selectdRowData.getId();
            serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(sql);
            alert("OK");
        } catch (NoRowException e) {
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Bind
    @SaveState
    private String invSvr;

    @Bind
    @SaveState
    private String invoiceTypev;

    @Bind
    @SaveState
    private String listed;

    @Bind
    @SaveState
    private String CheckEWM;

    @Bind
    @SaveState
    private String ncpFlag;

    @Action
    public void openinginvoice() {
        if (this.selectdRowData.getId() < 1) {
            MessageUtils.alert("发票还未保存!");
            return;
        }
        String[] ids = new String[1];
        ids[0] = "" + this.selectdRowData.getId();
        try {
            invSvr = serviceContext.invoiceMgrService.openinginvoice(ids, invoiceTypev, listed, CheckEWM, ncpFlag);
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        update.markUpdate(true, UpdateLevel.Data, "setInvoicepennel");
        this.grid.reload();
    }

    @Bind
    @SaveState
    private String infoKindTypev;

    @Bind
    @SaveState
    private String printKind;

    @Bind
    @SaveState
    private String printInvSvr;

    @Action
    public void printinvoice() {
        if (this.selectdRowData.getId() < 1) {
            MessageUtils.alert("发票还未保存!");
            return;
        }
        String[] ids = new String[1];
        ids[0] = "" + this.selectdRowData.getId();
        try {
            printInvSvr = serviceContext.invoiceMgrService.printinvoice(ids, infoKindTypev, printKind);
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        update.markUpdate(true, UpdateLevel.Data, "setPrintInvoicepennel");
        this.grid.reload();
    }

    //权限控制
    private void initCtrl() {
        invoiceSetup.setDisabled(true);
        invoicePrint.setDisabled(true);
        save.setDisabled(true);
        del.setDisabled(true);
        for (String s : AppUtils.getUserRoleModuleCtrl(512000L)) {
            if (s.endsWith("_setup")) {
                invoiceSetup.setDisabled(false);
            } else if (s.endsWith("_print")) {
                invoicePrint.setDisabled(false);
            } else if (s.endsWith("_update")) {
                save.setDisabled(false);
            } else if (s.endsWith("_delete")) {
                del.setDisabled(false);
            }
        }
    }


    @Bind
    @SaveState
    public String taskname;

    @Bind
    @SaveState
    public String bpmremark = "";

    @Bind
    @SaveState
    public String user = "";

    @Bind
    @SaveState
    public String nextAssignUser = "";

    @Action
    public void applyBPMform() {
        if (this.selectdRowData.getId() < 1) {
            MessageUtils.alert("发票还未保存!");
            return;
        }
        try {
            if ("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))) {
                if (StrUtils.isNull(nextAssignUser)) nextAssignUser = "0";
                String processCode = "BPM-28C01EAF";
                BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '" + processCode + "'");
                String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '" + bpmProcess.getId() + "' AND refid = '" + this.selectdRowData.getId() + "' AND isdelete = false AND state <> 9 AND state <>8";
                Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                String str = s.get("count").toString();
                if (Long.valueOf(str) == 0) {
                    Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
                } else {
                    Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
                }
                Browser.execClientScript("bpmWindowJsVar.show();");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bind(id = "taskDefine")
    public List<SelectItem> getTaskDefine() {
        List<SelectItem> lists = new ArrayList<SelectItem>();
        try {
            String processCode = "BPM-28C01EAF";
            BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '" + processCode + "'");
            String sql = "SELECT * FROM bpm_assign WHERE process_id = " + bpmProcess.getId() + " AND isdelete = false AND taskname NOT IN('Start','End') order by step";
            List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
            for (Map map : maps) {
                SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
                lists.add(selectItem);
            }
        } catch (NoRowException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Action
    public void applyBPM() {
        if (this.selectdRowData.getId() < 1) {
            MessageUtils.alert("发票还未保存!");
            return;
        }
        try {
            if ("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))) {
                FinaArap arap = this.serviceContext.arapMgrService.finaArapDao.findById(Long.parseLong(this.editGrid.getSelectedIds()[0]));
                FinaJobs job = this.serviceContext.jobsMgrService.finaJobsDao.findById(arap.getJobid());
                
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-28C01EAF";
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
				
                String count = "SELECT COUNT(1) AS c FROM bpm_processinstance WHERE processid = 84702072274 AND refid = '"+this.selectdRowData.getId()+"' AND isdelete = false AND state <> 9 AND state <>8;";
                Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(count);
                if(Integer.valueOf(String.valueOf(m.get("c"))) > 0){
                	String sqlsub = "SELECT f_bpm_processStart('processid=" + bpmProcess.getId() + ";userid=" + AppUtils.getUserSession().getUserid() + ";assignuserid=" + nextAssignUser + ";bpmremarks=" + bpmremark + ";taskname=" + taskname + ";refno=" + job.getNos() + ";refid=" + this.selectdRowData.getId() + "') AS rets;";
	                Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
	                MessageUtils.alert("OK");
	                Browser.execClientScript("bpmWindowJsVar.hide();");
	                return;
                }
                
                String sql = "SELECT id,invoiceno" +
                		"\nFROM fina_invoice f" +
                		"\nWHERE clientid = "+arap.getCustomerid()+" " +
        				"\nAND corpid = "+this.selectdRowData.getCorpid()+
        				"\nAND businvoiceconfirm = false  " +
        				"\nAND isdelete = false" +
        				"\nAND jobid = " +this.selectdRowData.getJobid()+
        				"\nAND not exists (SELECT 1 FROM bpm_processinstance WHERE processid = 84702072274 AND refid = f.id :: VARCHAR AND isdelete = false AND state <> 9 AND state <>8)";
				List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);

				for (int i = 0; i < list.size(); i++) {
            		String sqlsub = "SELECT f_bpm_processStart('processid="+bpmProcess.getId()+";userid="+AppUtils.getUserSession().getUserid()+";assignuserid="+nextAssignUser+";bpmremarks="+bpmremark+";taskname="+taskname+";refno="+String.valueOf(list.get(i).get("invoiceno"))+";refid="+String.valueOf(list.get(i).get("id"))+"') AS rets;";
            		Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
            	}
                
                MessageUtils.alert("OK");
                Browser.execClientScript("bpmWindowJsVar.hide();");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
        }
    }


    @Action
    public void delBPM() {
        this.nextAssignUser = "";
        this.user = "";
        this.bpmremark = "";
        update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
    }

    @Bind
    public UIDataGrid gridUser;

    @SaveState
    @Accessible
    public Map<String, Object> qryMapUser = new HashMap<String, Object>();

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

    @Action
    public void qryuser() {
        this.gridUser.reload();
    }

    @Bind
    @SaveState
    public String qryuserdesc = "";

    public Map getQryClauseWhere2(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(map, "qry");
        qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
        qryuserdesc = qryuserdesc.toUpperCase();
        if (!StrUtils.isNull(qryuserdesc)) {
            qry += "AND (code ILIKE '%" + qryuserdesc + "%' OR namec ILIKE '%" + qryuserdesc + "%' OR namee ILIKE '%" + qryuserdesc + "%' OR depter2 ILIKE '%" + qryuserdesc + "%' OR company ILIKE '%" + qryuserdesc + "%')";
        }
        map.put("qry", qry);
        return map;
    }

    @Action
    public void confirm() {
        String[] ids = this.gridUser.getSelectedIds();
        if (ids == null) {
            MessageUtils.alert("请勾选一条记录！");
            return;
        }
        for (String id : ids) {
            if (!this.nextAssignUser.contains(id)) {
                this.nextAssignUser = nextAssignUser + id + ",";
                SysUser us = serviceContext.userMgrService.sysUserDao.findById(Long.valueOf(id));
                this.user = user + us.getNamec() + ",";
            }
        }
        update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
    }

    @Action
    public void confirmAndClose() {
        this.confirm();
        Browser.execClientScript("userWindowJs.hide();");
    }

    @Bind
    public UIIFrame attachmentIframe;

    @Bind
    public UIWindow attachmentWindow;

    @Action
    public void mgrAttachment() {
        if (this.invoiceid != null && !this.invoiceid.isEmpty()) {
            this.attachmentWindow.show();
            attachmentIframe.load(AppUtils.getContextPath() + "/pages/module/common/attachment.xhtml?linkid=" + this.invoiceid + "&code=invoice");
        } else {
            alert("请先保存");
        }

    }

    @Bind
    @SaveState
    public boolean onlyShowAccount = false;

    @Action
    public void onlyShowAccountAction() {
        String newpage = "";
        String str = AppUtils.getReqParam("onlyShowAccount");
        if ("true".equals(str)) {
            newpage = "Y";
        } else {
            newpage = "N";
        }
        try {
            ConfigUtils.refreshUserCfg("only_show_account", newpage, AppUtils.getUserSession().getUserid());
            this.editGrid.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bind
    @SaveState
    public boolean showPayable = true;

    @Action
    public void showPayableAction() {
        String newpage = "";
        String str = AppUtils.getReqParam("showPayable");
        if ("true".equals(str)) {
            newpage = "Y";
        } else {
            newpage = "N";
        }
        try {
            ConfigUtils.refreshUserCfg("show_payable", newpage, AppUtils.getUserSession().getUserid());
            this.editGrid.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bind
    @SaveState
    public boolean onlyShowWriteoff = false;

    @Action
    public void onlyShowWriteoffJsAction() {
        String newpage = "";
        String str = AppUtils.getReqParam("onlyShowWriteoff");
        if ("true".equals(str)) {
            newpage = "Y";
        } else {
            newpage = "N";
        }
        try {
            ConfigUtils.refreshUserCfg("only_show_writeoff", newpage, AppUtils.getUserSession().getUserid());
            this.editGrid.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action
    public void gridUser_ondblclick() {
        confirmAndClose();
    }

    /**
     * @return
     */
    @Bind(id = "invGoodsName")
    public List<SelectItem> getInvGoodsName() {
        try {
            return commonDBCache.getComboxItems("d.code", "COALESCE(d.namee,d.namec)", "d.namec", "dat_filedata AS d", "WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 340", "order by CASE WHEN code = 'A' OR code = 'B' OR code = 'C' THEN 1 ELSE 2 END, code");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }


    @Bind
    @SaveState
    private String identity;

    @Bind
    @SaveState
    private String kptype;

    @Bind
    @SaveState
    private String fphxz;

    @Bind
    @SaveState
    private String invSvrElectron;

    @Action
    public void openinginvoiceElectron() {
        try {
            ConfigUtils.refreshUserCfg("invoice_identity", identity, AppUtils.getUserSession().getUserid());
            ConfigUtils.refreshUserCfg("invoice_kptype", kptype, AppUtils.getUserSession().getUserid());
            ConfigUtils.refreshUserCfg("invoice_fphxz", fphxz, AppUtils.getUserSession().getUserid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.invoiceid != null && !this.invoiceid.isEmpty()) {
            try {
                String[] arr = new String[1];
                arr[0] = "" + this.invoiceid + "";
                invSvrElectron = serviceContext.invoiceMgrService.openingElectronInvoice(arr, identity, kptype, fphxz);

                this.selectdRowData = this.serviceContext.invoiceMgrService.finaInvoiceDao.findById(Long.parseLong(invoiceid));
                this.update.markUpdate(true, UpdateLevel.Data, "editPanel");
            } catch (Exception e) {
                MessageUtils.showException(e);
            }
            update.markUpdate(true, UpdateLevel.Data, "setInvoicepennelElectron");
        }
    }

    @Action
    public void getInvoice() {
        try {
            String[] arr = new String[1];
            arr[0] = "" + this.invoiceid + "";
            serviceContext.invoiceMgrService.getInvoiceByOrderno(arr, identity);
            this.selectdRowData = this.serviceContext.invoiceMgrService.finaInvoiceDao.findById(Long.parseLong(invoiceid));
            this.update.markUpdate(true, UpdateLevel.Data, "editPanel");
            MessageUtils.alert("OK");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    /**
     * 显示邮件页面
     */
    @Action
    public void showEmail() {
        String url = AppUtils.getContextPath();
        String openurl = url + "/pages/sysmgr/mail/emailedit.aspx";
        AppUtils.openWindow("_showEmail", openurl + "?type=invoice&id=-1&jobid=" + this.invoiceid);
    }

    @Action
    public void businvoiceconfirmAjaxSubmit() {
        Boolean businvoiceconfirm = selectdRowData.getBusinvoiceconfirm();
        String updater = AppUtils.getUserSession().getUsercode();
        String sql = "";
        try {
            if (businvoiceconfirm) {
                sql = "UPDATE fina_invoice SET businvoiceconfirm = TRUE ,businvoiceconfirm_time = NOW(),businvoiceconfirm_user = '"
                        + updater + "' WHERE id =" + this.invoiceid + ";";
            } else {
                sql = "UPDATE fina_invoice SET businvoiceconfirm = FALSE ,businvoiceconfirm_time = NOW(),businvoiceconfirm_user = '"
                        + updater + "' WHERE id =" + this.invoiceid + ";";
            }
            serviceContext.invoiceMgrService.finaInvoiceDao.executeSQL(sql);
            MessageUtils.alert(businvoiceconfirm ? "Confirm OK!" : " Cancel OK!");
        } catch (CommonRuntimeException e) {
            MessageUtils.alert(e.getLocalizedMessage());
        } catch (Exception e) {
            MessageUtils.showException(e);
        } finally {
            this.selectdRowData = this.serviceContext.invoiceMgrService.finaInvoiceDao.findById(Long.parseLong(invoiceid));
            this.update.markUpdate(true, UpdateLevel.Data, "editPanel");
        }
    }

    @Bind
    public UIDataGrid customerGrid;

    @Bind
    public UIWindow showCustomerWindow;


    @Bind
    private String qryCustomerKey;

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

    public String getCustomerids(Long jobid) {
        sql = serviceContext.invoiceMgrService.getCustomerids(jobid);
        if (StrUtils.isNull(sql)) {
            sql = "AND 1=2";
        } else {
            sql = "AND id in (" + sql + ")";
        }
        return sql;
    }
    
    @Action
    public void customerGrid_ondblclick() {
        try {
            Object[] objs = customerGrid.getSelectedValues();
            Map m = (Map) objs[0];
            this.selectdRowData.setClientid((Long) m.get("id"));
            String customercode = (String) m.get("code") + "/" + (String) m.get("abbr");
            this.selectdRowData.setClientname(customercode);
            this.selectdRowData.setClientitle((String) m.get("namec"));
            araptype = "AR";

            try {
                if (!StrUtils.isNull(String.valueOf(selectdRowData.getClientid()))) {
                    List<SysCorpinv> list = serviceContext.sysCorpinvMgrService.sysCorpinvDao.findAllByClauseWhere("linkid='" + selectdRowData.getClientid() + "' order by id desc");
                    if (list != null && list.size() > 0) {
                        SysCorpinv sysCorpinv = list.get(0);
                        this.selectdRowData.setCustomername(sysCorpinv.getInvnamec());
                        this.selectdRowData.setTaxpayernumber(sysCorpinv.getLicno());
                        this.selectdRowData.setAddress(sysCorpinv.getAddressc());
                        this.selectdRowData.setAccountbankandaccountnumber(sysCorpinv.getBankandnumber());
                        isaffirm = sysCorpinv.getIsaffirm();
                        
                    } else {
                        this.selectdRowData.setCustomername("");
                        this.selectdRowData.setTaxpayernumber("");
                        this.selectdRowData.setAddress("");
                        this.selectdRowData.setAccountbankandaccountnumber("");
                        isaffirm = false;
                    }
                }
            } catch (Exception e) {

            }

            this.update.markUpdate(UpdateLevel.Data, "araptype");
            this.update.markUpdate(UpdateLevel.Data, "customerid");
            this.update.markUpdate(UpdateLevel.Data, "customercode");
            this.update.markUpdate(UpdateLevel.Data, "client");
            this.update.markUpdate(true, UpdateLevel.Data, "editPanel");
            this.editGrid.rebind();
            //选择客户后自动设置一次汇率
            //this.exchangeRate();
            showCustomerWindow.close();
            
            initinv();
            Browser.execClientScript("isaffirminv();");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void addMaster(){
        if(this.jobid > 0){
            Browser.execClientScript("sendRedirect('./businvoice.aspx?jobid=" + jobid + "');");
        }
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
            List<FinaInvoice> finabillList = this.serviceContext.invoiceMgrService.getFinaBillListByJobid(this.jobid,AppUtils.getUserSession().getUserid());
            if(finabillList != null && finabillList.size() > 0) {
                List<SelectItem> items = new ArrayList<SelectItem>();
                for(FinaInvoice fb : finabillList) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String data = sdf.format(fb.getInputtime()==null?Calendar.getInstance().getTime():fb.getInputtime());
                    items.add(new SelectItem(fb.getId(), fb.getInvoiceno()+'/'+fb.getInputer()+'/'+data));
                }
                this.billnos = items;
            }
        }
    }

    @Action(id="invoiceno",event="onselect")
    public void billcomboxOnselect(){
        if(this.selectdRowData != null && this.selectdRowData.getInvoiceno() !=null){
            String jsFunction = "sendRedirect('./businvoice.aspx?jobid="+this.jobid+"&id="+this.selectdRowData.getInvoiceno()+"');";
            Browser.execClientScript(jsFunction);
        }
    }

    @Action
    public void saveAction(){
        this.save();
        this.addMaster();
    }
    
    @Bind
    public boolean isaffirm;
    
    @Action
    public void selectedRowDataupdate(){
        try {
            String sysCorpinvid = (String)AppUtils.getReqParam("sysCorpinvid");
            SysCorpinv sysCorpinv = serviceContext.sysCorpinvMgrService.sysCorpinvDao.findById(Long.valueOf(sysCorpinvid));
            if (sysCorpinv != null) {
                this.selectdRowData.setCustomername(sysCorpinv.getInvnamec());
                this.selectdRowData.setTaxpayernumber(sysCorpinv.getLicno());
                this.selectdRowData.setAddress(sysCorpinv.getAddressc());
                this.selectdRowData.setAccountbankandaccountnumber(sysCorpinv.getBankandnumber());
                isaffirm = sysCorpinv.getIsaffirm();
                
            } else {
                this.selectdRowData.setCustomername("");
                this.selectdRowData.setTaxpayernumber("");
                this.selectdRowData.setAddress("");
                this.selectdRowData.setAccountbankandaccountnumber("");
                isaffirm = false;
            }
            this.update.markUpdate(true, UpdateLevel.Data, "editPanel");
        } catch (Exception e) {

        }
    }
    
	@Bind
	public UIIFrame defineIframe;

	@Bind
	public UIWindow showInvWindow;

	/**
	 * 预览发票
	 */
	@Action
	public void showInv() {
		defineIframe.load("./invpreview.html?id=" + this.selectdRowData.getId());//在发票预览窗口中加载报表
		showInvWindow.show();
	}

    @Bind
    public UIButton showInvoice;

    @Bind
    public UIWindow invoicePdfWindow;

    @Bind
    public UIIFrame invoicePdfIFrame;

    /**
     * 发票预览
     */
    @Action
    public void showInvoice() {
        invoicePdfIFrame.setSrc("/scp/pages/module/commerce/invoicechild.html?id=" + selectdRowData.getId());
        update.markAttributeUpdate(invoicePdfIFrame, "src");
        if (invoicePdfWindow != null) {
            invoicePdfWindow.show();
        }
    }

}
