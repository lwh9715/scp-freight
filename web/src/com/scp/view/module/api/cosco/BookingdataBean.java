package com.scp.view.module.api.cosco;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.api.ApiCoscoBookData;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import com.ufms.base.utils.HttpUtil;
@ManagedBean(name = "pages.module.api.cosco.bookingdataBean", scope = ManagedBeanScope.REQUEST)
public class BookingdataBean extends MastDtlView{
	
	@SaveState
	@Accessible
	public ApiCoscoBookData selectedRowData = new ApiCoscoBookData();
	
	@SaveState
	@Accessible
	@Bind
	public String productid;
	
	@SaveState
	@Accessible
	public String shippername = "Patent International Logistics (Shen Zhen) Co.,Ltd.";
	
	@SaveState
	@Accessible
	public String shipperaddressLine1 = "Futian CBD,Shenzhen,China";
	
	@SaveState
	@Accessible
	public String shipperaddressLine2 = "9/F,Excellence Times Plaza";
	
	@Bind
	@SaveState
	public String coscoServerUrl;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}
	
	@Override
	public void init(){
		String pid = AppUtils.getReqParam("productid").trim();
		String pol = AppUtils.getReqParam("pol").trim();
		String pod = AppUtils.getReqParam("pod").trim();
		String etd = AppUtils.getReqParam("etd").trim();
		String vesvoy = AppUtils.getReqParam("vesvoy").trim();
		String linecode = AppUtils.getReqParam("linecode").trim();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		coscoServerUrl = ConfigUtils.findSysCfgVal("sys_cosco_server_url");
		if (!StrUtils.isNull(pid)){
			try {
				Date etddate = sdf.parse(etd);
				//点击订舱按钮，打开订舱页面，一条运价可以进行多次订舱操作
				productid = pid;
				//this.selectedRowData = this.serviceContext.apiCoscoBookdataService.findByproductId(productid);
				//if(this.selectedRowData != null){
				//	this.pkVal = selectedRowData.getId();
				//}
				this.pkVal = 0L;
				SysUser us = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid());
				selectedRowData.setShippername(shippername);
				selectedRowData.setShipperaddressLine1(shipperaddressLine1);
				selectedRowData.setShipperaddressLine2(shipperaddressLine2);
				selectedRowData.setShipperphone(us.getTel1());
				selectedRowData.setConsigneename(shippername);
				selectedRowData.setConsigneeaddressLine1(shipperaddressLine1);
				selectedRowData.setConsigneeaddressLine2(shipperaddressLine2);
				selectedRowData.setConsigneephone(us.getTel1());
				selectedRowData.setNotifyname(shippername);
				selectedRowData.setNotifyaddressLine1(shipperaddressLine1);
				selectedRowData.setNotifyaddressLine2(shipperaddressLine2);
				selectedRowData.setNotifyphone(us.getTel1());
				selectedRowData.setCargopackagetype("PK");
				selectedRowData.setCargoquantity(20);
				BigDecimal cbm20 =new BigDecimal("20");
				selectedRowData.setCargovolume(cbm20);
				selectedRowData.setBookername(shippername);
				selectedRowData.setBookeremail(us.getEmail1());
				selectedRowData.setBookermobile(us.getTel1());
				selectedRowData.setBookerphone(us.getTel2());
				selectedRowData.setPol(pol);
				selectedRowData.setPod(pod);
				selectedRowData.setEtd(etddate);
				selectedRowData.setVesvoy(vesvoy);
				selectedRowData.setLinecode(linecode);
				
				update.markUpdate(true, UpdateLevel.Data, "editPanel");
			} catch (Exception e) {
				this.pkVal = 0L;
				e.printStackTrace();
			}
		}
	}
	
	
	@Action
	public void saveData() {
		try {
			if (this.pkVal > 0) {
				selectedRowData.setCouponid(null);
				this.serviceContext.apiCoscoBookdataService.apiCoscoBookdataDao.modify(this.selectedRowData);
			} else {
				selectedRowData.setAmount(0);
				selectedRowData.setProductid(productid);
				selectedRowData.setCouponid(null);
				this.serviceContext.apiCoscoBookdataService.apiCoscoBookdataDao.create(this.selectedRowData);
				this.pkVal = selectedRowData.getId();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
	}

	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void refreshMasterForm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 包装
	 * @return
	 */
	@Bind(id="packagedesc")
    public List<SelectItem> getPackagedesc() {
    	try {
			return CommonComBoxBean.getComboxItems("d.type","d.type || '/' || d.descs","api_cosco_package AS d","WHERE 1=1 ","ORDER BY d.type");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Action
	public void submitbooking(){
		if(this.pkVal < 1){
			MessageUtils.alert("请先保存数据!");
			return;
		}
		
		if(StrUtils.isNull(coscoServerUrl)){
			coscoServerUrl = "http://hangxun.vicp.io:9696/cosco-api";
   		}
    	String url = coscoServerUrl+"/bookingService?method=booking";
    	//String url = "http://127.0.0.1:8008/cosco-api/bookingService?method=booking";
		try {
			String sql = "SELECT * FROM f_api_cosco_bookingdata('productid="+ productid + ";id="+this.pkVal+"') AS bookingdata";
			Map m =AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String bookingdata = m.get("bookingdata").toString();
			//System.out.println("bookingdata--->" +bookingdata);
			String response = HttpUtil.post(url, bookingdata);
			//System.out.println(response);
			JSONObject jsonRespnse = JSONObject.fromObject(response);
			JSONObject data = jsonRespnse.getJSONObject("data");
			String orderNo = data.getString("orderNo");
			String brNo = data.getString("brNo");
			if(!StrUtils.isNull(orderNo)){
				selectedRowData.setOrderno(orderNo);
				selectedRowData.setBrno(brNo);
				this.serviceContext.apiCoscoBookdataService.apiCoscoBookdataDao.modify(this.selectedRowData);
				this.alert("OK! orderNo:" + orderNo);
			}else{
				String message = data.getString("message");
				this.alert(message);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SaveState
	@Accessible
	List<SelectItem> items = new ArrayList<SelectItem>();
	
	@Action
	public void dischargeAjaxSubmit(){
		if(StrUtils.isNull(coscoServerUrl)){
			coscoServerUrl = "http://hangxun.vicp.io:9696/cosco-api";
   		}
    	String url = coscoServerUrl+"/bookingService?method=intermodalService&productid="+productid+"";
		try {
			String response = HttpUtil.post(url, "");
			JSONObject jsonRespnse = JSONObject.fromObject(response);
			JSONObject data = jsonRespnse.getJSONObject("data");
			JSONArray dischargeServices = (JSONArray)data.get("dischargeServices");
			//System.out.println("dischargeServices-->"+dischargeServices);
			for(int i = 0;i<dischargeServices.size();i++){
				items.add(new SelectItem(dischargeServices.getJSONObject(i).get("intermodalServiceNo").toString(), dischargeServices.getJSONObject(i).get("cityFullName").toString()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Bind(id="pod")
    public List<SelectItem> getpod() {
		return items;
    }
	
}
