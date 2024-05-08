package com.scp.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.api.ApiMaerskPricesubDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.model.api.ApiMaerskPriceSub;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

/**
 * @author Administrator
 * 自动获取马士基价格
 */
public class AuotoGetMearskPrice {

	private static boolean isRun = false;

	public void execute() throws Exception {
		if (isRun) {
			System.out.print("Auotopotgetbus wraning:another process is running!");
			return;
		}
		isRun = true;
		try {
			fix();
		} catch (Exception e) {
			throw e;
		} finally {
			isRun = false;
		}
	}

	private void fix() throws Exception {
		SysUserDao sysUserDao;
		try {
			List<Map> maps = AppUtils.getServiceContext().daoIbatisTemplate
				.queryWithUserDefineSql("SELECT DISTINCT pol,pod,cntcode,vessel,voyage" +
						"\n,(SELECT inputer FROM api_maersk_pricesub WHERE pol = a.pol AND pod = a.pod AND cntcode = a.cntcode AND vessel = a.vessel AND voyage = a.voyage ORDER BY inputtime DESC LIMIT 1) AS inputer FROM api_maersk_pricesub a;");
			if(maps!=null&&maps.size()>0){
				for(int j=0;j<maps.size();j++){
					String qrypol = maps.get(j).get("pol").toString();
					String qrypod = maps.get(j).get("pod").toString();
					String cntcode = maps.get(j).get("cntcode").toString();
					String vessel = maps.get(j).get("vessel").toString();
					String voyage = maps.get(j).get("voyage").toString();
					String inputer = maps.get(j).get("inputer").toString();
					String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					String sys_maersk_qryprice_url = ConfigUtils.findSysCfgVal("sys_maersk_qryprice_url");
					if(StrUtils.isNull(sys_maersk_qryprice_url)){
						sys_maersk_qryprice_url = "https://offers.api-stage.maersk.com/offers/v2/offers/brand/maeu/departuredate/";
					}
					String returnjson = AppUtils.sendGetToMSJ(sys_maersk_qryprice_url+format
							,"originServiceMode=CY&destinationServiceMode=CY&containers=2x"+cntcode+"&originRkstCode="+qrypol+"&destinationRkstCode="+qrypod);
					if(!StrUtils.isNull(returnjson)){
						JSONObject pricejson = JSONObject.fromObject(returnjson);
						JSONArray offers = JSONArray.fromObject(pricejson.get("offers"));
//						for(int i=0;i<offers.size();i++){
							String offersjson = offers.get(0).toString();
							String productOffer = JSONObject.fromObject(offersjson).getString("productOffer");
							String routeSchedulesWithPrices = JSONObject.fromObject(offersjson).getString("routeSchedulesWithPrices");
							JSONArray routeSchedulesWithPricess = JSONArray.fromObject(routeSchedulesWithPrices);
							int offerId = JSONObject.fromObject(productOffer).getInt("offerId");
							ApiMaerskPricesubDao apiMaerskPricesubDao = (ApiMaerskPricesubDao)ApplicationUtilBase.getBeanFromSpringIoc("apiMaerskPricesubDao");
							DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
							for(int k=0;k<routeSchedulesWithPricess.size();k++){
								String vesselk = JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(routeSchedulesWithPricess.get(j)).get("routeScheduleFull")).get("vessel")).getString("name");
								String voyagek = JSONObject.fromObject(JSONObject.fromObject(routeSchedulesWithPricess.get(j)).get("routeScheduleFull")).getString("voyageNumber");
								if(vessel.equals(vesselk)&&voyage.equals(voyagek)){
									List<ApiMaerskPriceSub> spiMaerskPricesubs = apiMaerskPricesubDao
											.findAllByClauseWhere(" pol = '"+qrypol+"' AND pod = '"+qrypod+"' AND cntcode = '"+cntcode+"' AND vessel = '"+vessel+"' AND voyage = '"+voyage+"' AND offersjson='"+StrUtils.getSqlFormat(routeSchedulesWithPricess.get(k).toString())+"'");
									if(spiMaerskPricesubs!=null&&spiMaerskPricesubs.size()>0){
										System.out.println("此运价已订阅");
									}else{
										ApiMaerskPriceSub apiMaerskPricesub = new ApiMaerskPriceSub();
										apiMaerskPricesub.setCntcode(cntcode);
										apiMaerskPricesub.setPol(qrypol);
										apiMaerskPricesub.setPod(qrypod);
										//apiMaerskPricesub.setOffersjson(routeSchedulesWithPricess.get(k).toString());
										apiMaerskPricesub.setVessel(vesselk);
										apiMaerskPricesub.setVoyage(voyagek);
										apiMaerskPricesub.setInputer(inputer);
										apiMaerskPricesubDao.create(apiMaerskPricesub);
										Map qurtyuser = daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT * FROM sys_user WHERE code = '"+inputer+"'");
										String email = qurtyuser!=null&&qurtyuser.get("email1")!=null?qurtyuser.get("email1").toString():"";
										String sql = "UPDATE api_maersk_pricesub SET inputer = '"+inputer+"' WHERE id = "+apiMaerskPricesub.getId()+";"
													+"INSERT INTO sys_im(id, sendid, receiveid, sendtime, issend, msg, im_type)"
													+"\nVALUES (getid(), 8888, (SELECT id FROM sys_user WHERE code = '"+inputer+"'), now() + interval '2 minute', FALSE"
													+"\n			  , '马斯基价格发生改变，请及时查看,起运港:"+qrypol+",目的港:"+qrypod+",箱型:"+cntcode+",船名:"+vessel+",航次:"+voyage+"'"
													+"\n				, 'q');"
													+"\nINSERT INTO sys_email(id, addressee,copys, subject, content, sender, emailtype, inputer, inputtime,linktbl,isautosend)"
													+"\nVALUES(getid(),'"+email+"','','马斯基价格改变邮件','马斯基价格发生改变，请及时查看,起运港:"+qrypol+",目的港:"+qrypod+",箱型:"+cntcode+",船名:"+vessel+",航次:"+voyage+"'"
													+"\n,'it@hangxunkeji.com.cn','S','api_maersk_pricesub',now(),'api_maersk_pricesub',true);";
										daoIbatisTemplate.updateWithUserDefineSql(sql);
										System.out.println("订阅成功");
									}
								}
							}
//						}
					}
				}
			}
		} catch (Exception e1) {
			System.out.println(e1.getLocalizedMessage());
			isRun = false;
			return;
		}
	}
}