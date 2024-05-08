package com.scp.view.module.price;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.price.PriceFcl;
import com.scp.model.sys.PriceFclAsk;
import com.scp.model.sys.Sysformcfg;
import com.scp.service.price.PricefclMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.price.qryfclBean", scope = ManagedBeanScope.REQUEST)
public class QryfclBean extends GridFormView{

    @ManagedProperty("#{pricefclMgrService}")
	public PricefclMgrService pricefclMgrService;

    @SaveState
	@Accessible
	public PriceFcl selectedRowData = new PriceFcl();

    @Bind
    public UIIFrame chartsIframe;

    @Bind
    public UIWindow chartsWindow;

	@Bind
	@SaveState
	public String pricetype;

//    @Override
//	public void export(){
//		ActionGridExport actionGridExport = new ActionGridExport();
//		actionGridExport.setKeys((String) ApplicationUtils.getReqParam("key"));
//		actionGridExport.setVals((String) ApplicationUtils.getReqParam("value"));
//		try {
//			actionGridExport.execute(getGridList());
//			Browser.execClientScript("simulateExport.fireEvent('click');");
//			qryRefresh();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//		}
//	}

	@Bind(id="qryPol")
    public List<SelectItem> getQryPol() {
    	try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		//1733 运价维护及运价查询列表调整(起运港提取：收货地 UNION ALL 运价起运港数据)
    		String sql = "WITH rc_pol AS(SELECT DISTINCT polnamee AS pol FROM price_fcl_bargefeedtl"
						+ "\n UNION ALL"
						+ "\n SELECT DISTINCT pol FROM price_fcl WHERE isdelete = false and pol <> '' and pol is not null"
						+ "\n UNION ALL"
						+ "\n SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispol = TRUE  and exists (SELECT 1 FROM dat_port child where child.link = x.namee))"
						+ "\n SELECT DISTINCT pol FROM rc_pol ORDER BY pol;";
        	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
        	if(list!=null){
        		Object value = null;
        		for (Map dept : list) {
        			value = dept.get("pol");
    				items.add(new SelectItem(String.valueOf(value),
    						String.valueOf(value)));
        		}
        	}
    		return items;
//			return CommonComBoxBean.getComboxItems("DISTINCT pol","pol","price_fcl AS d","WHERE 1=1","ORDER BY pol");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind(id="qryPod")
    public List<SelectItem> getQryPod() {
		try {
    		List<SelectItem> items = new ArrayList<SelectItem>();
    		//1733 运价维护及运价查询列表调整(起运港提取：收货地 UNION ALL 运价起运港数据)
    		String sql = "WITH rc_pol AS(SELECT DISTINCT pod FROM price_fcl WHERE isdelete = false and pod <> '' and pod is not null"
						+ "\n UNION ALL"
						+ "\n SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispod = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee))"
						+ "\n SELECT DISTINCT pod FROM rc_pol ORDER BY pod;";
        	List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
        	if(list!=null){
        		Object value = null;
        		for (Map dept : list) {
        			value = dept.get("pod");
    				items.add(new SelectItem(String.valueOf(value),
    						String.valueOf(value)));
        		}
        	}
    		return items;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind(id="qryCar")
    public List<SelectItem> getQryCar() {
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT shipping","shipping"
					,"price_fcl AS d","WHERE isdelete = false and shipping <> '' and shipping is not null","ORDER BY shipping");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind(id="qryLine")
    public List<SelectItem> getQryLine() {
    	try {
    		return CommonComBoxBean.getComboxItems("line","line","price_fcl AS d"
    				,"WHERE isdelete = false and line <> '' and line is not null group by line","ORDER BY convert_to(line,'GBK')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind(id="qryLinecode")
    public List<SelectItem> getQryLinecode() {
    	try {
    		return CommonComBoxBean.getComboxItems("DISTINCT shipline","shipline","price_fcl AS d"
    				,"WHERE isdelete = false and shipline <> '' and shipline is not null","ORDER BY shipline");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind
	public UICombo qryPolCom;

	@Bind
	public UICombo qryPodCom;

	@Bind
	public UICombo qryCarCom;

	@Override
	public void clearQryKey() {
		super.clearQryKey();
		pol = "";
		pod = "";
		bargepol = "";
		if(qryPolCom != null)qryPolCom.repaint();
		if(qryPodCom != null)qryPodCom.repaint();
		if(qryCarCom != null)qryCarCom.repaint();
	}

	@Bind
	@SaveState
	public String feedesc;

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = pricefclMgrService.pricefclDao.findById(this.pkVal);
		feedesc = pricefclMgrService.refreshFeeDesc(this.pkVal);
	}

	@Override
	protected void doServiceSave() {


	}


	@Bind
	@SaveState
	public String pol;

	@Bind
	@SaveState
	public String pod;

	@Bind
	@SaveState
	public Date datefm;

	@Bind
	@SaveState
	public Date dateto;

	@Bind
	@SaveState
	public String datetypefm = ">=";

	@Bind
	@SaveState
	public String datetypeto = "<=";

	@Bind
	@SaveState
	public String bargepol;

	@SaveState
	public Long userid;

	@SaveState
	public String username;

    @SaveState
    public String mobilephone;

	@Bind
	@SaveState
	public String actionJsText;//按不同公司自定义js从 sys_formcfg 获取

	@Bind
	@SaveState
	public String qrycorpid;

	@Bind
	@SaveState
	public String priceuser;

	@Bind
	@SaveState
	public String ispush = "push";

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
        this.username = AppUtils.getUserSession().getUsername();

		String sql = "select mobilephone from sys_user where id = "+AppUtils.getUserSession().getUserid() + "limit 1 ";
        Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);

        String phone =  map.get("mobilephone").toString();
		if (!StrUtils.isNull(phone) && phone.length() > 1) {
			this.mobilephone = map.get("mobilephone").toString().substring(7);
		}

		if (!isPostBack) {
			super.applyGridUserDef();
			gridLazyLoad = true;
		}
		if(!StrUtils.isNull(bargepol)){
			//Browser.execClientScript("showcolumn();");
		}else{
			//Browser.execClientScript("hidecolumn();");
		}
		actionJsText = "";
		List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao.findAllByClauseWhere(" formid = '"+this.getMBeanName()+"' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
		for (Sysformcfg sysformcfg : sysformcfgs) {
			actionJsText+=sysformcfg.getJsaction();
		}
		//System.out.println("actionJsText:"+actionJsText);
		update.markUpdate(true, UpdateLevel.Data, "actionJsText");
	}

	@Bind
	@SaveState
	public String qryshipline = "";

	@Bind
	@SaveState
	public Boolean isShowHistory = false;


	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		map.put("qry",qry);

		map.put("args","isqryPol="+isqryPol+";isshipline="+isshipline+";pol="+StrUtils.getSqlFormat(pol)+";pod="+pod+";carrier="
				+queryMap.get("shipping")+";line="+queryMap.get("line")+";qrycorpid="+qrycorpid+";showHistory="+(isShowHistory?"Y":"N")+";");


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String filter = "1=1";
		if(datefm != null && dateto != null){
			//filter += "\n AND datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "' AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
			filter += "\n AND '" + sdf.format(datefm).toString() + "' " + datetypefm + " datefm  AND '" + sdf.format(dateto).toString() + "' " + datetypeto + " dateto ";
		}
		if(datefm != null && dateto == null){
			filter += "\n AND datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "'";
		}
		if(datefm == null && dateto != null){
			filter += "\n AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
		}

		if(!StrUtils.isNull(qryshipline) ){
			qryshipline = StrUtils.getSqlFormat(qryshipline);
			qryshipline = qryshipline.toUpperCase();
			filter += "\n AND (UPPER(shipline) LIKE '%"+qryshipline+"%')";
		}
		if(!StrUtils.isNull(priceuser) ){
			filter += "\n AND priceuserid = '"+priceuser+"'";
		}
		if(!StrUtils.isNull(pricetype) ){
			filter += "\n AND pricetype = '"+pricetype+"'";
		}

		map.put("filter", filter);
		if(!StrUtils.isNull(ord))map.put("ord", "\nORDER BY " + ord);
		return map;
	}

	@Bind
    @SaveState
    public String isqryPol = "Y";

	@Bind
    @SaveState
    public String isshipline = "Y";

	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Action
	protected void startImport() {
		importDataBatch();
	}

	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 1);
		//AppUtils.debug(importDataText);
	}*/

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_price_fcl";
				String args = -100 + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Bind
	@SaveState
	public String z20gp = "0";
	@Bind
	@SaveState
	public String z40gp = "0";
	@Bind
	@SaveState
	public String z40hq = "0";
	@Bind
	@SaveState
	public String z45hq = "0";
	@Bind
	@SaveState
	public String pieceother = "0";
	@Bind
	@SaveState
	public String cntypeothercodev;
	@Bind
	@SaveState
	public Date cls;
	@Bind
	@SaveState
	public Date etd;
	@Bind
	@SaveState
	public Date bargecls;
	@Bind
	@SaveState
	public Date bargeetd;
	@Bind
	@SaveState
	public Long customerid;

	@Bind
	@SaveState
	public String polGridSelect="";

	@Bind
	@SaveState
	public Date jobdate;

	@Bind
	@SaveState
	public String corpid;

	@Bind
	@SaveState
	public String corpidop;

	@Bind
	@SaveState
	public Date dategatein;

	@Action
	public void showCreateJobs() {

		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选择一条记录!");
			return;
		}
		try{
			String sql = "select b.id from sys_user a,sys_corporation b where a.corpid=b.id AND a.id="+AppUtils.getUserSession().getUserid();
			Map map=this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			corpid=map.get("id").toString();
			update.markUpdate(true, UpdateLevel.Data, "corpid");
		}catch(Exception e){

		}
		Browser.execClientScript("createJobsWindowJsVar.show();polGridSelectJsVar.setValue(gridJsvar.getSelectionModel().getSelected().get('pol'));");
	}

	@Bind
	public UIWindow priceJobAskWindow;

	@SaveState
	public String askPol;

	@SaveState
	public String askPod;

	@SaveState
	public String askCarrier;

	@SaveState
	public String askerid;

	@SaveState
	public String asker;

	@SaveState
	public String reasker;

	@SaveState
	public BigDecimal cost20;

	@SaveState
	public BigDecimal cost40gp;

	@SaveState
	public BigDecimal cost40hq;

	@SaveState
	public BigDecimal cost45hq;


    @Action
    public void openPrice(){
    	String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选择一条记录!");
			return;
		}
		String uuid = ids[0];
		String priceid = uuid.split("-")[0];
		PriceFcl priceFcl=this.serviceContext.pricefclMgrService.pricefclDao.findById(Long.parseLong(priceid));

		askPol=priceFcl.getPol();
		askPod=priceFcl.getPod();
		askCarrier=priceFcl.getShipping()+"GSZ";
		cost20=priceFcl.getCost20();
		cost40gp=priceFcl.getCost40gp();
		cost40hq=priceFcl.getCost40hq();
		cost45hq=priceFcl.getCost45hq();
		asker=AppUtils.getUserSession().getUsername();

		try{
			String sql = "select namec from _sys_user where id="+priceFcl.getPriceuserid();
			Map map=this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			reasker=map.get("namec").toString();
		}catch(Exception e){

		}
		update.markUpdate(true, UpdateLevel.Data, "askPol");
		update.markUpdate(true, UpdateLevel.Data, "askPod");
		update.markUpdate(true, UpdateLevel.Data, "askCarrier");
		update.markUpdate(true, UpdateLevel.Data, "asker");
		update.markUpdate(true, UpdateLevel.Data, "reasker");
		update.markUpdate(true, UpdateLevel.Data, "cost20");
		update.markUpdate(true, UpdateLevel.Data, "cost40gp");
		update.markUpdate(true, UpdateLevel.Data, "cost40hq");
		update.markUpdate(true, UpdateLevel.Data, "cost45hq");

    	priceJobAskWindow.show();

    }

    @Bind
    @SaveState
    public String remarks;
	@Action
	public void savePriceJobs() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选择一条记录!");
			return;
		}
		String uuid = ids[0];
		String priceid = uuid.split("-")[0];
		PriceFcl priceFcl=this.serviceContext.pricefclMgrService.pricefclDao.findById(Long.parseLong(priceid));
		PriceFclAsk pricefclask=new PriceFclAsk();
		pricefclask.setFclid(uuid);
		pricefclask.setPol(priceFcl.getPol());
		pricefclask.setPod(priceFcl.getPod());
		pricefclask.setCarrier(priceFcl.getShipping()+"GSZ");
		pricefclask.setCls(priceFcl.getCls());
		pricefclask.setCost20(priceFcl.getCost20());
		pricefclask.setCost40gp(priceFcl.getCost40gp());
		pricefclask.setCost40hq(priceFcl.getCost40hq());
		pricefclask.setCost45hq(priceFcl.getCost45hq());
		pricefclask.setResper(reasker);
		pricefclask.setResaskerid(priceFcl.getPriceuserid());
		pricefclask.setAskerid(AppUtils.getUserSession().getUserid());
		pricefclask.setAsker(AppUtils.getUserSession().getUsername());
		pricefclask.setAsktime(new Date());
		pricefclask.setRemarks(remarks);
	    this.serviceContext.priceFclAskService.saveData(pricefclask);
		MessageUtils.alert("保存成功!");
		priceJobAskWindow.close();
	}

	@Action
	public void createJobs() {
		try {
			String[] ids = this.grid.getSelectedIds();
			Long userid = AppUtils.getUserSession().getUserid();
			String uuid = ids[0];
			String priceid = uuid.split("-")[0];
			String bargeid = uuid.split("-")[1];
			System.out.println("corpid"+corpid);
			String sql = "SELECT unnest(f_price_fcl2jobs('priceid="+priceid+";bargeid="+bargeid+";20gp="+z20gp+";cntypeothercodev="+cntypeothercodev
								+";40gp="+z40gp+";40hq="+z40hq+";45hq="+z45hq+";pieceother="+pieceother+";userid="+userid
								+";cls="+cls+";etd="+etd+";bargecls="+bargecls+";bargeetd="+bargeetd+";dategatein="+dategatein+";customerid="
								+customerid+";pol="+pol.replaceAll("'", "''")+";pollink="+polGridSelect.replaceAll("'", "''")+";jobdate="+jobdate+";corpid="+corpid+";corpidop="+corpidop+"')) AS jobid";
			//System.out.println(sql);
			List<Map> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			String jobid = StrUtils.getMapVal(map.get(0), "jobid");
			String alter45hq = StrUtils.getMapVal(map.get(1), "jobid");
			if(!StrUtils.isNull(alter45hq)){
				Browser.execClientScript("layer.alert('"+alter45hq+"', {icon: 7},function(){window.open('/scp/pages/module/ship/jobsedit.xhtml?id="
						+jobid+"');layer.close(layer.index);});");
			}else{
				Browser.execClientScript("window.open('/scp/pages/module/ship/jobsedit.xhtml?id="+jobid+"');");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Action
	public void release() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请勾选要发布的记录!");
		}
		pricefclMgrService.release(ids);
		MessageUtils.alert("OK!");
		this.refresh();
	}

	@Override
	public void refresh() {
		super.refresh();
		update.markUpdate(true, UpdateLevel.Data, "qryPolCom");
		update.markUpdate(true, UpdateLevel.Data, "qryPodCom");
		update.markUpdate(true, UpdateLevel.Data, "qryCarCom");
	}

	@Action
    private void clearQry2() {
		this.clearQryKey();
	}

	@Bind
	public UIIFrame localChargeIframe;

	@Action
	public void linkEdit(){
		String pkid = AppUtils.getReqParam("pkid");
		if(StrUtils.isNull(pkid)){
			this.alert("无效pkid");
			return;
		}
		this.pkVal = Long.parseLong(pkid);
		doServiceFindData();
		this.refreshForm();
		localChargeIframe.load("localcharge.xhtml?linkid="+this.pkVal);

		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}

	@Action
	public void createOrder(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}

		String uuid = ids[0];
		String priceid = uuid.split("-")[0];
		String bargeid = uuid.split("-")[1];

		//System.out.println(priceid);
		String blankUrl = AppUtils.chaosUrlArs("../order/busorderfcl.xhtml") + "&priceid=" + priceid + "&type=fromPrice";
		orderIframe.load(blankUrl);
		createOrderWindow.show();
	}
	@Action
	public void close(){
		editWindow.close();
	}

	@Bind
	public UIWindow createOrderWindow;


	@Bind
	public UIIFrame orderIframe;

	@Action
	public void showChartsWindow(){
		String pol_rp =  AppUtils.getReqParam("pol");
		String pod_rp =  AppUtils.getReqParam("pod");
		String shipping_rp =  AppUtils.getReqParam("shipping");
		String dateto_rp =  AppUtils.getReqParam("dateto");
		if(StrUtils.isNull(pol_rp)||StrUtils.isNull(pod_rp)||StrUtils.isNull(shipping_rp)||StrUtils.isNull(dateto_rp)){
			MessageUtils.alert("Data error,please contact Manager!");
			return;
		}
		StringBuffer chartsUrl = new StringBuffer();
		chartsUrl.append("./chartsiframe.xhtml?pol=");
		chartsUrl.append(pol_rp);
		chartsUrl.append("&pod=");
		chartsUrl.append(pod_rp);
		chartsUrl.append("&shipping=");
		chartsUrl.append(shipping_rp);
		chartsUrl.append("&dateto=");
		chartsUrl.append(dateto_rp);
		chartsIframe.load(chartsUrl.toString());
		chartsWindow.show();
	}

	/**
	 * 1737 运价查询中所选港口如果是收货地，处理收货地赋值，不是收货地的清掉收货地，然后查询结果
	 */
	@Action
	public void setbargepolajax(){
		String pol = AppUtils.getReqParam("pol");
		String sql = "SELECT 1 FROM price_fcl_bargefeedtl WHERE polnamee = '"+StrUtils.getSqlFormat(pol)+"'";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
		if(list!=null&&list.size()>0){
			bargepol = StrUtils.getSqlFormat(pol);
		}else{
			bargepol = "";
		}
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
//		qryRefresh();
	}

	@Override
	public void grid_ondblclick() {

	}

	@SaveState
	private String ord = "";

	@Action
	public void sortGridRemote(){
		String desctype = AppUtils.getReqParam("desctype");
		if(!StrUtils.isNull(desctype)){
			if(desctype.equals("DESC")){
				ord = " cost20 DESC,cost40gp DESC,cost40hq DESC,cost45hq DESC";
			}else{
				ord = " cost20,cost40gp,cost40hq,cost45hq";
			}
		}else{
			String argss = AppUtils.getReqParam("argss");
			ord = argss;
		}
		this.grid.reload();
		Browser.execClientScript("setBargeClolr()");
	}

	@Override
	public void qryRefresh(){
		if (grid != null) {
			ord = "";
			gridLazyLoad = false;
			ispush = "nopush";
			this.grid.reload();
			Browser.execClientScript("setBargeClolr()");

			String sql = "INSERT INTO sys_qryfcllog (id,fclpol,fclpod,logdesc,isdelete,inputer,inputtime)"
					+ "\nVALUES (getid(),'" + pod + "','" + pol + "','','f','" + AppUtils.getUserSession().getUsercode() + "',now());";
			daoIbatisTemplate.updateWithUserDefineSql(sql);
		}
	}


	@Action
	public void showCreatePriceJobs() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选择一条记录!");
			return;
		}
		Browser.execClientScript("createJobsWindowJsVar.show();polGridSelectJsVar.setValue(gridJsvar.getSelectionModel().getSelected().get('pol'));");
	}


	@Action
	public void createPriceJobs() {
		try {
			String[] ids = this.grid.getSelectedIds();
			Long userid = AppUtils.getUserSession().getUserid();
			String uuid = ids[0];
			String priceid = uuid.split("-")[0];
			String bargeid = uuid.split("-")[1];
			String sql = "SELECT unnest(f_price_fclPricejobs('priceid="+priceid+";bargeid="+bargeid+";20gp="+z20gp+";cntypeothercodev="+cntypeothercodev
								+";40gp="+z40gp+";40hq="+z40hq+";45hq="+z45hq+";pieceother="+pieceother+";userid="+userid
								+";cls="+cls+";etd="+etd+";customerid="+customerid+";pol="+pol.replaceAll("'", "''")+";pod="+pod.replaceAll("'", "''")+"')) AS jobid";
			List<Map> map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			String jobid = StrUtils.getMapVal(map.get(0), "jobid");
			Browser.execClientScript("window.open('/scp/pages/module/salesmgr/priceedit.xhtml?id="+jobid+"');");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

}
