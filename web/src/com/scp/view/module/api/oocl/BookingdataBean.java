package com.scp.view.module.api.oocl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.scp.model.api.ApiOoclBookData;
import com.scp.model.edi.PdiWetransBean;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
import com.scp.view.module.api.ApiTools;
import com.ufms.base.utils.HttpUtil;

@ManagedBean(name = "pages.module.api.oocl.bookingdataBean", scope = ManagedBeanScope.REQUEST)
public class BookingdataBean extends FormView {
	
	@SaveState
	@Accessible
	public ApiOoclBookData selectedRowData = new ApiOoclBookData();
	
	@SaveState
	@Accessible
	@Bind
	public String productid;
	
	@SaveState
	@Accessible
	public String shippername = "";
	
	@SaveState
	@Accessible
	public String shipperaddressLine1 = "";
	
	@SaveState
	@Accessible
	public String shipperaddressLine2 = "";
	
	
	@Bind
	@SaveState
	public String apiServerUrl;

	@Bind
	@SaveState
	public String CouponsNum = "0";

	@Bind
	@SaveState
	public String couponId = "";

	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String src = AppUtils.getReqParam("src");
			if("edit".equalsIgnoreCase(src)){
				String id = AppUtils.getReqParam("id");
				this.pkVal = Long.valueOf(id);
				this.refresh();
			}else{
				init();
			}
			apiServerUrl = ApiTools.getApiUrl();

			getCouponsNum();
		}
	}
	
	public void init(){
		if(StrUtils.isNull(shippername)){
			shippername = ConfigUtils.findUserSysCfgVal("oocl_come_name",AppUtils.getUserSession().getUserid()).trim();
		}
		if(StrUtils.isNull(shipperaddressLine1)){
			shipperaddressLine1 = ConfigUtils.findUserSysCfgVal("oocl_come_addr1",AppUtils.getUserSession().getUserid()).trim();
		}
		if(StrUtils.isNull(shipperaddressLine2)){
			shipperaddressLine2 = ConfigUtils.findUserSysCfgVal("oocl_come_addr2",AppUtils.getUserSession().getUserid()).trim();
		}
		
		String pid = AppUtils.getReqParam("productid").trim();
		String pol = AppUtils.getReqParam("pol").trim();
		String pod = AppUtils.getReqParam("pod").trim();
		String etd = AppUtils.getReqParam("etd").trim();
		String vesvoy = AppUtils.getReqParam("vesvoy").trim();
		String linecode = AppUtils.getReqParam("linecode").trim();
		String porcity = AppUtils.getReqParam("porcity").trim();
		String fndcity = AppUtils.getReqParam("fndcity").trim();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
				selectedRowData.setBookeremail(StrUtils.isNull(us.getEmail2())?us.getEmail1():us.getEmail2());

				selectedRowData.setBookermobile(us.getMobilephone());
				selectedRowData.setBookerphone(!StrUtils.isNull(us.getTel1())?us.getTel1():us.getTel2());

				selectedRowData.setPol(pol);
				selectedRowData.setPod(pod);
				selectedRowData.setEtd(etddate);
				selectedRowData.setVesvoy(vesvoy);
				selectedRowData.setLinecode(linecode);
				selectedRowData.setPorcity(porcity);
				selectedRowData.setFndcity(fndcity);
				selectedRowData.setShipperemail(StrUtils.isNull(us.getEmail2())?us.getEmail1():us.getEmail2());
				selectedRowData.setConsigneeemail(StrUtils.isNull(us.getEmail2())?us.getEmail1():us.getEmail2());
				selectedRowData.setNotifyemail(StrUtils.isNull(us.getEmail2())?us.getEmail1():us.getEmail2());

				selectedRowData.setCargodesc("KITCHENWARE");
				selectedRowData.setCargoweight(new BigDecimal("8000"));

				update.markUpdate(true, UpdateLevel.Data, "editPanel");
			} catch (Exception e) {
				this.pkVal = 0L;
				e.printStackTrace();
			}
		}
	}

	@Action
	public void refreshs() {
		if (pkVal!=0) {
			selectedRowData = serviceContext.apiOoclBookdataService.apiOoclBookdataDao.findById(this.pkVal);
			super.refresh();
		}
	}
	
	@Action
	public void saveData() {
		try {
			if (this.pkVal > 0) {
				selectedRowData.setCouponid(null);
				this.serviceContext.apiOoclBookdataService.apiOoclBookdataDao.modify(this.selectedRowData);
			} else {
				selectedRowData.setAmount(0);
				selectedRowData.setProductid(productid);
				selectedRowData.setCouponid(null);
				this.serviceContext.apiOoclBookdataService.apiOoclBookdataDao.create(this.selectedRowData);
				this.pkVal = selectedRowData.getId();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		MessageUtils.alert("OK!");
	}

	
	
	/**
	 * 包装
	 * @return
	 */
	@Bind(id="packagedesc")
    public List<SelectItem> getPackagedesc() {
    	try {
			return CommonComBoxBean.getComboxItems("d.type","d.type || '/' || d.descs","api_oocl_package AS d","WHERE 1=1 ","ORDER BY d.type");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//提交订舱信息 正式接口
	@Action
	public void submitbooking(){
		if(this.pkVal < 1){
			MessageUtils.alert("请先保存数据!");
			return;
		}
		if (selectedRowData.getIsusecoupon() && !StrUtils.isNull(selectedRowData.getUsecouponnum())) {
			int usecouponnum = Integer.parseInt(selectedRowData.getUsecouponnum());
			if (!(usecouponnum >= 0 && usecouponnum <= Integer.parseInt(CouponsNum))) {
				MessageUtils.alert("请输入有效优惠券数量!");
				return;
			}
		}
		if(StrUtils.isNull(apiServerUrl)){
			apiServerUrl = "http://hangxun.vicp.io:9696/cosco-api";
   		}
		String apiKey = ApiTools.getApiKey("oocl", AppUtils.getUserSession().getUserid());
		
    	String url = apiServerUrl+"/ooclBookingService?method=booking&apiKey="+apiKey;
    	
    	//apiServerUrl = "http://192.168.0.252:8668/cosco-api"+"/ooclBookingService?method=booking";
		try {
			String sql = "SELECT * FROM f_api_oocl_bookingdata('productid="+ productid  + ";couponId="+this.couponId + ";id="+this.pkVal+"') AS bookingdata";
			Map m = AppUtils.getServiceContext().daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String bookingdata = m.get("bookingdata").toString();
			System.out.println("bookingdata--->" +bookingdata);

			if (!StrUtils.isNull(bookingdata)) {
				String[] bookingdataArray = bookingdata.split("&&&&&");
				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				for (String thisbookingdata : bookingdataArray) {
					String response = HttpUtil.post(url, thisbookingdata);
					System.out.println(response);
					JSONObject jsonRespnse = JSONObject.fromObject(response);

					String code = jsonRespnse.getString("code");
					String message = jsonRespnse.getString("message");
					if("0".equals(code)){
						JSONObject data = jsonRespnse.getJSONObject("data");
						String orderNo = data.getString("orderNo");
						String brNo = data.getString("brNo");
						if(!StrUtils.isNull(orderNo)){
							sb.append(orderNo).append(",");
							sb2.append(brNo).append(",");
							selectedRowData.setOrderno(sb.toString());
							selectedRowData.setBrno(sb2.toString());
							this.serviceContext.apiOoclBookdataService.apiOoclBookdataDao.modify(this.selectedRowData);
							this.alert("OK! orderNo:" + sb);
						}else{
							this.alert(message);
							this.alert("OK!");
						}
					}else{
						this.alert(message);
					}
				}
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
		if(StrUtils.isNull(apiServerUrl)){
			apiServerUrl = "http://hangxun.vicp.io:9696/cosco-api";
   		}
    	String url = apiServerUrl + "/ooclBookingService?method=intermodalService&productid="+productid+"";
		try {
			String response = HttpUtil.post(url, "");
			JSONObject jsonRespnse = JSONObject.fromObject(response);
			JSONObject data = jsonRespnse.getJSONObject("data");
			JSONArray dischargeServices = (JSONArray)data.get("dischargeServices");
			//System.out.println("dischargeServices-->"+dischargeServices);
			for(int i = 0;i<dischargeServices.size();i++){
				items.add(new SelectItem(dischargeServices.getJSONObject(i).get("intermodalServiceNo").toString(), dischargeServices.getJSONObject(i).get("cityFullNameEn").toString()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Bind(id="pod")
    public List<SelectItem> getpod() {
		return items;
    }

	@Action
	public String getCouponsNum() {
		try {
			String url = "https://apis.cargosmart.com/openapi/ooclfs/cop/product/prebooking/" + productid;
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("cache-control", "no-cache");
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
			headers.put("client_id", "");
			headers.put("client_secret", "");
			headers.put("grant_type", "client_credentials");
			headers.put("Content-Type", "application/json;charset=utf-8");
			headers.put("appKey", "");

			String result = PdiWetransBean.httpsRequest(headers, url, "GET", "");
			JSONObject jsonRespnse = JSONObject.fromObject(result);
			JSONObject dataJSONObject = jsonRespnse.getJSONObject("data");
			JSONArray couponInfosJSONArray = dataJSONObject.getJSONArray("couponInfos");
			if (couponInfosJSONArray.size() == 0) {
				CouponsNum = "0";
			} else {
				JSONObject couponInfojsonObject = (JSONObject) couponInfosJSONArray.get(0);
				CouponsNum = String.valueOf(couponInfojsonObject.get("inventory"));
                couponId = String.valueOf(couponInfojsonObject.get("couponId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CouponsNum;
	}



	
}
