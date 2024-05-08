package com.scp.view.module.salesmgr;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoRowException;
import com.scp.model.data.DatCntype;
import com.scp.model.data.DatFeeitem;
import com.scp.model.price.BusPrice;
import com.scp.model.price.BusPriceDtl;
import com.scp.model.price.PriceFcl;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.salesmgr.priceeditBean", scope = ManagedBeanScope.REQUEST)
public class PriceeditBean extends GridFormView{
	
	@Resource(name="hibTtrTemplate")  
    private TransactionTemplate transactionTemplate;
	
	@SaveState
	@Accessible
	public BusPrice selectedRowData = new BusPrice();
	
	@SaveState
	@Accessible
	public BusPriceDtl dtlRowData = new BusPriceDtl();

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Bind
	@SaveState
	public String language;
	
	@Bind
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.userid = AppUtils.getUserSession().getUserid();
			this.update.markUpdate(UpdateLevel.Data, "userid");
			this.language = AppUtils.getUserSession().getMlType().toString();
			this.initOtherCnyTypeForJson();
			String id = AppUtils.getReqParam("id");
			if (!StrUtils.isNull(id)) {
				pkVal = Long.valueOf(id);
			} else {
				pkVal = -1L;
			}
			refresh();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
		}
	}
	
	@Bind
	@SaveState
	public String jsonCnyType = "''";//其它箱型json
	
	@Bind
	@SaveState
	public String jsonData = "''";
	
	/**
	 * 初始化其它箱型
	 */
	private void initOtherCnyTypeForJson(){
		try {
			String ret = "''";
			ret = this.serviceContext.cntypeMgrService.getOtherCnyTypeForJson();
			this.jsonCnyType = ret == null ? "''" : ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.jsonCnyType = "''";
		} finally {
			update.markUpdate(true, UpdateLevel.Data, "jsonCnyType");
		}
		
	}
	
	@Override
	@Transactional
	public void save() {
        	try{
    			serviceContext.busPriceService.busPriceDao.createOrModify(selectedRowData);
    			pkVal = selectedRowData.getId();
    			update.markUpdate(true, UpdateLevel.Data, "pkVal");
    			//selectedRowData = serviceContext.busPriceService.busPriceDao.findById(pkVal);
    			//update.markUpdate(true, UpdateLevel.Data, "nos");
    			
    			dtlRowData.setPriceid(this.pkVal);
    			serviceContext.busPriceDtlService.busPriceDtlDao.createOrModify(dtlRowData);
    			changeCorpidOps();
    			refresh();
    			MessageUtils.alert("OK!");
    		} catch (Exception e) {
    			MessageUtils.showException(e);
    		}
	    
	}
	
	@Override
	public void refresh() {
		if(pkVal > 0){
			this.selectedRowData = serviceContext.busPriceService.busPriceDao.findById(pkVal);
			//this.dtlRowData = serviceContext.busPriceDtlService.busPriceDtlDao.findOneRowByClauseWhere("priceid = "+this.pkVal+"");
			
			if(this.selectedRowData !=null &&this.selectedRowData.getCustomerid()!=null){
				SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getCustomerid());
				Browser.execClientScript("$('#customer_input').val('"+sysCorporation.getAbbr()+"')");
			}else{
				Browser.execClientScript("$('#customer_input').val('')");
			}
			
			this.jsonData = getJsonDatas();
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
		}else{
			selectedRowData = new BusPrice();
			if(selectedRowData.getPol() == null){
				selectedRowData.setPol("");
			}
			if(selectedRowData.getPod() == null){
				selectedRowData.setPod("");
			}
			if(selectedRowData.getSalesname() == null){
				selectedRowData.setSalesname("");
			}
		}
		
	}
	
	@Bind
	@SaveState
	public String jsonDataStr = "''";
	
	@Action
	private void changeCorpidOps(){
		//this.dtlRowData = serviceContext.busPriceDtlService.busPriceDtlDao.findOneRowByClauseWhere("priceid = "+this.pkVal+"");
		this.selectedRowData = serviceContext.busPriceService.busPriceDao.findById(pkVal);
		
		
		List<Long> idsOldArray = new ArrayList<Long>();
		idsOldArray.clear();
		List<Long> idsNewArray = new ArrayList<Long>();
		idsNewArray.clear();
		
		// 保存明细数据
		if (!StrUtils.isNull(jsonDataStr) && !"null".equals(jsonDataStr) && !"''".equals(jsonDataStr)) {
			Type listType = new TypeToken<ArrayList<com.scp.vo.order.BusPricedtl>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new Gson();

			ArrayList<com.scp.vo.order.BusPricedtl> list = gson.fromJson(
					jsonDataStr, listType);// 使用该class属性，获取的对象均是list类型的

			ArrayList<com.scp.vo.order.BusPricedtl> listOld = gson.fromJson(
					serviceContext.busPriceDtlService.getPricedtlById(pkVal),
					listType);

			List<BusPriceDtl> busPriceDtlList = new ArrayList<BusPriceDtl>();

			for (com.scp.vo.order.BusPricedtl li : list) {
				if (li.getFeeitemid() < 1) {
					continue;
				}
				if (li.getAmt20() == null && li.getAmt40gp() == null
						&& li.getAmt40hq() == null && li.getAmt() == null
						&& li.getAmt20ar() == null && li.getAmt40gpar() == null
						&& li.getAmt40hqar() == null && li.getAmtar() == null) {
					continue;
				}
				BusPriceDtl busPriceDtl = new BusPriceDtl();
				if (li.getId() > 0) {
					busPriceDtl = serviceContext.busPriceDtlService.busPriceDtlDao.findById(li.getId());
					idsNewArray.add(li.getId());
				}
				busPriceDtl.setPriceid(pkVal);
				busPriceDtl.setFeeitemid(li.getFeeitemid());
				busPriceDtl.setFeeitemcode(li.getFeeitemcode());
				try {
					DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
							.findById(li.getFeeitemid());
					busPriceDtl.setFeeitemname(datFeeitem.getName());
				} catch (Exception e) {
					busPriceDtl.setFeeitemname(li.getFeeitemname());
				}
				busPriceDtl.setPpcc(li.getPpcc());
				busPriceDtl.setCurrency(li.getCurrency());
				busPriceDtl.setUnit(li.getUnit());
				busPriceDtl.setAmt20(li.getAmt20());
				busPriceDtl.setAmt40gp(li.getAmt40gp());
				busPriceDtl.setAmt40hq(li.getAmt40hq());
				busPriceDtl.setAmtother(li.getAmtother());
				busPriceDtl.setAmt(li.getAmt());
				busPriceDtl.setAmt20ar(li.getAmt20ar());
				busPriceDtl.setAmt40gpar(li.getAmt40gpar());
				busPriceDtl.setAmt40hqar(li.getAmt40hqar());
				busPriceDtl.setAmtother_ar(li.getAmtother_ar());
				busPriceDtl.setAmtar(li.getAmtar());
				busPriceDtl.setPiece20(li.getPiece20());
				busPriceDtl.setPiece40gp(li.getPiece40gp());
				busPriceDtl.setPiece40hq(li.getPiece40hq());
				busPriceDtl.setPieceother(li.getPieceother());
				busPriceDtl.setPiece(li.getPiece());
				busPriceDtl.setRemarks(li.getRemarks());
				busPriceDtl.setCntypeothercode(li.getCntypeothercode());
				busPriceDtlList.add(busPriceDtl);
			}
			serviceContext.busPriceDtlService.saveOrModify(busPriceDtlList);

			if (listOld != null && listOld.size() > 0) {
				for (com.scp.vo.order.BusPricedtl li : listOld) {
					idsOldArray.add(li.getId());
				}
				List<Long> lists = getDiffrent(idsOldArray, idsNewArray);

				if (!lists.isEmpty()) {
					serviceContext.busPriceDtlService.removes(lists);
				}
			}
			this.jsonData = getJsonDatas();
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
			Browser.execClientScript("showmsg()");
		}
	}
	
	private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
		long st = System.nanoTime();
		List<Long> diff = new ArrayList<Long>();
		for (Long str : list1) {
			if (!list2.contains(str)) {
				diff.add(str);
			}
		}
		return diff;
	}
	
	public String getJsonDatas() {
		try {
			String ret = "''";
			ret = serviceContext.busPriceDtlService.getPricedtlById(pkVal);
			return StrUtils.isNull(ret) ? "''" : ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "''";
		}
	}
	
	@Inject(value = "l")
	private MultiLanguageBean l;
	
	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			if(l.m.getMlType()==MLType.en){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namee",
						"sys_report AS d", " WHERE modcode = 'jobprice' AND isdelete = FALSE",
						"order by filename");
			}else if(l.m.getMlType()==MLType.ch){
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'jobprice' AND isdelete = FALSE",
						"order by filename");
			}else{
				return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
						"sys_report AS d", " WHERE modcode = 'jobprice' AND isdelete = FALSE",
						"order by filename");
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename = "price_jobs.raq";
	
	
	@Action
	public void scanReport() {
		if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
			MessageUtils.alert("请选择格式！");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		
		String openUrl = rpturl
		+ "/reportJsp/showReport.jsp?design=false&raq=/ddp/"
		+ showwmsinfilename;
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs()
				+ "&userid="
				+ AppUtils.getUserSession().getUserid());
	}
	
	private String getArgs() {
		String args = "";
		args += "&id=" + this.pkVal;
		return args;
	}
	
	
	@Bind
	@SaveState
	public String priceid = "-1";
	
	@Action
	public void getsurcharge(){
		if(pkVal>0){
			String sql = "SELECT id FROM price_fcl a WHERE isdelete = FALSE AND EXISTS(SELECT 1 FROM bus_price x " 
						+"WHERE x.id = "+pkVal+" AND EXISTS(SELECT 1 FROM sys_corporation WHERE x.carrierid = id AND abbr = a.shipping))"
						+"AND pol = '"+this.selectedRowData.getPol()+"' AND pod = '"+this.selectedRowData.getPod()+"' ORDER BY daterelease LIMIT 1;";
			List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if(list.size()<1){
				MessageUtils.alert("未查到匹配的运价");
				return;
			}
			String priceidStr = list.get(0).get("id").toString();
			if (!StrUtils.isNull(priceidStr)) {
				this.priceid = priceidStr;
				this.initOtherCnyTypeForJson();
				initDatagetsurcharge();//
			}
		}else{
			MessageUtils.alert("请先保存");
		}
	}
	
	@Bind
	@SaveState
	public String selectcnytype;
	
	
	private void initDatagetsurcharge() {
		PriceFcl priceFcl = serviceContext.pricefclMgrService.pricefclDao
				.findById(Long.valueOf(priceid));
		this.dtlRowData.setCntypeothercode(priceFcl.getCntypeothercode());
		String cost20gp = priceFcl.getCost20().toString();
		String cost40gp = priceFcl.getCost40gp().toString();
		String cost40hq = priceFcl.getCost40hq().toString();
		String costother = (priceFcl.getPieceother()==null?"0":priceFcl.getPieceother()).toString();
		String cntypeothercode = (priceFcl.getCntypeothercode()==null?"-1":priceFcl.getCntypeothercode()).toString();
		DatCntype datCntype = null;
		datCntype = serviceContext.cntypeMgrService.datcntypeDao.findById(Long.parseLong((cntypeothercode!=null&&!cntypeothercode.equals(""))?cntypeothercode:"-1"));
		if(datCntype!=null){
			selectcnytype = datCntype.getCode();
			this.dtlRowData.setCntypeothercode(selectcnytype);
		}
		Browser.execClientScript("addCostItem(-100,-100,2236256200,'OCF','海运费','PP','USD','箱型',"+cost20gp+","+cost20gp+","+cost40gp+","+cost40gp+","+cost40hq+","+cost40hq+","+costother+","+costother+",0,0,0,0,0,0,'');");
		Browser.execClientScript("computer();");
		/*try {
			String sql = "SELECT DISTINCT templatename FROM price_fcl_feeadd AS d WHERE istemplate=true AND isdelete = FALSE AND templatename ILIKE '%"+this.selectedRowData.getShiping()+"%' ORDER BY templatename LIMIT 1";
			Map map = this.serviceContext.busOrderMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map != null && map.containsKey("templatename")){
				autoImportTemplate(map.get("templatename").toString());
			}
		}catch (NoRowException e) {
			//模版名称未匹配上,不作任何处理
		}catch (NullPointerException e) {
		}catch (Exception e) {
			MessageUtils.showException(e);
		}*/
	}
	
	
	@Action
	public void customerSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		if(!StrUtils.isNull(customerid)){
			SysCorporation sysCorporation = serviceContext.customerMgrService.sysCorporationDao.findById(Long.valueOf(customerid));
			this.selectedRowData.setContact(sysCorporation.getContact());
			this.selectedRowData.setEmail(sysCorporation.getEmail1());
			this.selectedRowData.setCustomerid(sysCorporation.getId());
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		}
	}
	
	
	/**
	 * 运费模版数据源
	 * @return
	 */
	@Bind(id = "template")
	public List<SelectItem> getTemplate() {
		try {
			return CommonComBoxBean.getComboxItems("DISTINCT templatename",
					"templatename", "price_fcl_feeadd AS d",
					"WHERE istemplate=true AND isdelete=FALSE", "ORDER BY templatename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	
	@Bind
	@SaveState
	public String templateComBo;
	
	/**
	 * 运费模版选择事件
	 */
	@Action
	public void templateComBoAction() {
		String cntypeothercode = AppUtils.getReqParam("cntypeothercode");
		templateComBo = AppUtils.getReqParam("templateComBov");
		this.jsonData = "";
		update.markUpdate(true, UpdateLevel.Data, "jsonData");

		if (StrUtils.isNull(templateComBo))
			return;
		List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao
				.findAllByClauseWhere("templatename='"
						+ templateComBo
						+ "' AND isdelete = false AND istemplate = TRUE ORDER BY id");

		List<BusPriceDtl> voList = new ArrayList<BusPriceDtl>();
		for (PriceFclFeeadd priceFclFeeadd : list) {
			BusPriceDtl busPriceDtl = new BusPriceDtl();
			busPriceDtl.setId(-100L);
			busPriceDtl.setPriceid(pkVal);
			busPriceDtl.setAmt(priceFclFeeadd.getAmt());
			busPriceDtl.setAmt20(priceFclFeeadd.getAmt20());
			busPriceDtl.setAmt40gp(priceFclFeeadd.getAmt40gp());
			busPriceDtl.setAmt40hq(priceFclFeeadd.getAmt40hq());
			busPriceDtl.setCurrency(priceFclFeeadd.getCurrency());
			busPriceDtl.setFeeitemid(priceFclFeeadd.getFeeitemid());
			busPriceDtl.setFeeitemcode(priceFclFeeadd.getFeeitemcode());
			try {
				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao
						.findById(priceFclFeeadd.getFeeitemid());
				busPriceDtl.setFeeitemname((AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en))?datFeeitem.getNamee():datFeeitem.getName());
			} catch (Exception e) {
				busPriceDtl.setFeeitemname(priceFclFeeadd.getFeeitemname());
			}
			busPriceDtl.setPpcc(priceFclFeeadd.getPpcc());
			busPriceDtl.setUnit(priceFclFeeadd.getUnit());
			busPriceDtl.setAmt(priceFclFeeadd.getAmt());
			busPriceDtl.setAmtar(priceFclFeeadd.getAmt());
			busPriceDtl.setAmt20(priceFclFeeadd.getAmt20());
			busPriceDtl.setAmt20ar(priceFclFeeadd.getAmt20());
			busPriceDtl.setAmt40gp(priceFclFeeadd.getAmt40gp());
			busPriceDtl.setAmt40gpar(priceFclFeeadd.getAmt40gp());
			busPriceDtl.setAmt40hq(priceFclFeeadd.getAmt40hq());
			busPriceDtl.setAmt40hqar(priceFclFeeadd.getAmt40hq());
			Map map = null;
			try {
				map = serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow("SELECT code FROM dat_cntype WHERE id ="
								+ priceFclFeeadd.getCntypeid());
			} catch (NoRowException e) {
				map = null;
			}
			//其他箱型一样就赋值
			if(map!=null&&map.get("code")!=null&&cntypeothercode!=null&&cntypeothercode.equals(map.get("code"))){
				busPriceDtl.setAmtother(priceFclFeeadd.getAmtother());
				busPriceDtl.setAmtother_ar(priceFclFeeadd.getAmtother());
			}
			Short tmps = 0;
			busPriceDtl.setPiece(tmps);
			busPriceDtl.setPiece20(tmps);
			busPriceDtl.setPiece40gp(tmps);
			busPriceDtl.setPiece40hq(tmps);
			busPriceDtl.setPieceother(tmps);
			voList.add(busPriceDtl);
		}
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		for (BusPriceDtl vo : voList) {
			String str = gson.toJson(vo);
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(str);
			jsonArray.add(el);
		}
		this.jsonData = jsonArray.toString();
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		Browser.execClientScript("init();");
	}
	
	
}
