package com.scp.view.module.api.maersk;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.api.ApiMaerskPriceSub;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;


@ManagedBean(name = "pages.module.api.maersk.pricesubBean", scope = ManagedBeanScope.REQUEST)
public class PriceSubBean extends GridView{
	
	@Bind
	@SaveState
	public String priceInfo;
	
	@Bind
	@SaveState
	public String qryKey;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String autoqry = ConfigUtils.findUserCfgVal("PriceSub_autoqry", AppUtils.getUserSession().getUserid());
			if("true".equals(autoqry)){
				this.autoQry = true;
			}else{
				this.autoQry = false;
			}
			
			qryKey = ConfigUtils.findSysCfgVal("sys_maersk_qryprice_key");
		}
	}
	
	@Action
	public void showPriceInfoAjax(){
		try {
			String id = AppUtils.getReqParam("id");
			ApiMaerskPriceSub sub = serviceContext.apiMaerskService.apiMaerskPricesubDao.findById(Long.parseLong(id));
			String querySql = "SELECT offersjson FROM api_maersk_pricesubdtl x WHERE x.subid = " + Long.parseLong(id) + " LIMIT 1";
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			priceInfo = StrUtils.getMapVal(map, "offersjson");
			update.markUpdate(true, UpdateLevel.Data, "priceInfo");
			String ves = sub.getVessel();
			String voy = sub.getVoyage();
			String js = "showPriceInfoSub('"+ves+"' , '"+voy+"')";
			Browser.execClientScript(js);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 运价走势
	 */
	@Action
	public void showFeeFunAjax(){
		try {
			String id = AppUtils.getReqParam("id");
			ApiMaerskPriceSub sub = serviceContext.apiMaerskService.apiMaerskPricesubDao.findById(Long.parseLong(id));
			String querysql = 
							"SELECT " +
							/*"\njson_agg((SELECT ocfamt FROM f_priceqry_json_to_recordset(d.offersjson) WHERE vessel = s.vessel AND voyage = s.voyage) ORDER BY d.inputtime) AS amount " +
							"\n,(vessel || '/' ||voyage) AS vesvoy"
							+"\n,json_agg(to_char(d.inputtime,'MMDD') ORDER BY d.inputtime) AS datef"
							+"\nFROM api_maersk_pricesub s , api_maersk_pricesubdtl d"*/
							"(SELECT f_priceqry_json_to_etd(d.offersjson)) AS datef"+
							"\n,(SELECT f_priceqry_json_to_ocfamt(d.offersjson)) AS amount"+
							"\n FROM api_maersk_pricesub s , api_maersk_pricesubdtl d"
							+"\nWHERE "
							+"\npol = '"+sub.getPol() + "'"
							+"\nAND pod = '"+sub.getPod() + "'"
							+"\nAND cntcode = '"+sub.getCntcode() + "'"
							+"\nAND s.id = d.subid"
							+"\nORDER BY d.inputtime DESC"
							+"\nlimit 1";
			/*System.out.println("sql--->\n"+querysql);*/
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querysql);
			
			priceEchart = "{\"amount\":"+m.get("amount").toString()+",\"datef\":"+m.get("datef").toString()+"}";
			update.markUpdate(true, UpdateLevel.Data, "priceInfo");
			Browser.execClientScript("showFeeFunEchart()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*@Action
	public void showContrastAction(){
		try {
			String id = AppUtils.getReqParam("id");
			ApiMaerskPriceSub sub = serviceContext.apiMaerskService.apiMaerskPricesubDao.findById(Long.parseLong(id));
			String querysql = 
							"SELECT " +
							"\njson_agg((SELECT ocfamt FROM f_priceqry_json_to_recordset(d.offersjson) WHERE vessel = s.vessel AND voyage = s.voyage) ORDER BY d.inputtime) AS amount " +
							"\nFROM api_maersk_pricesub s , api_maersk_pricesubdtl d"
							+"\nWHERE "
							+"\npol = '"+sub.getPol() + "'"
							+"\nAND pod = '"+sub.getPod() + "'"
							+"\nAND cntcode = '"+sub.getCntcode() + "'"
							+"\nAND d.subid = "+sub.getId()+""
							+"\nAND vessel ='"+sub.getVessel()+"'"
							+"\nAND voyage='"+sub.getVoyage()+"'"
							+"\nAND voyage ='"+sub.getVoyage()+"'"
							+"\nGROUP BY vessel , voyage"
							+"\nlimit 1";
			System.out.println("sql--->\n"+querysql);
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querysql);	
			
			priceContrast = "{\"id\":"+sub.getId()+",\"amount\":"+m.get("amount").toString()+"}";
			//System.out.println("priceContrast--->"+priceContrast);
			update.markUpdate(true, UpdateLevel.Data, "priceInfo");
			update.markUpdate(true, UpdateLevel.Data, "priceContrast");
			
			Browser.execClientScript("showPriceContrast()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	@Bind
	@SaveState
	public String priceEchart;
	
	
	@Bind
	@SaveState
	public Boolean autoQry;
	
//	@Action
//	public void autoqryrefreshAction(){
//		String autoQryv = AppUtils.getReqParam("autoQry");
//		if("true".equals(autoQryv)){
//			this.autoQry = true;
//		}else{
//			this.autoQry = false;
//		}
//		try {
//			ConfigUtils.refreshUserCfg("PriceSub_autoqry", autoQry.toString(), AppUtils.getUserSession().getUserid());
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//			e.printStackTrace();
//		}
//	}
	
	@Action
	public void getPriceAction(){
		String url = StrUtils.getSqlFormat(AppUtils.getReqParam("url"));
		String priceInfo = StrUtils.getSqlFormat(AppUtils.getReqParam("priceInfo"));
		String querySql = "SELECT f_api_maersk_pricesub('"+url+"' , '"+priceInfo+"')";
		try{
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void deletePrice(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1 ){
			alert("请至少勾选一行！");
			return;
		}
		try{
			serviceContext.apiMaerskService.deleteApiMaerskPricesubOnLastId(ids);
			this.grid.reload();
			alert("OK");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
}
