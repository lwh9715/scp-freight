package com.scp.view.module.finance;

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
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.price.PriceFcl;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.Sysformcfg;
import com.scp.service.price.PricefclMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.finance.choosefclbyhandBean", scope = ManagedBeanScope.REQUEST)
public class ChoosefclbyhandBean extends GridFormView{

	  @ManagedProperty("#{pricefclMgrService}")
		public PricefclMgrService pricefclMgrService;
	    
	    @SaveState
		@Accessible
		public PriceFcl selectedRowData = new PriceFcl();
	    
	    
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
//				return CommonComBoxBean.getComboxItems("DISTINCT pol","pol","price_fcl AS d","WHERE 1=1","ORDER BY pol");
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
		public String shipping;
		
		@Bind
		@SaveState
		public String line;
		
		
		
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
		
		@Bind
		@SaveState
		public String actionJsText;//按不同公司自定义js从 sys_formcfg 获取
		
		@Bind
		@SaveState
		public String qrycorpid;
		
		@Bind
		@SaveState
		public String jobid;
		
		@Override
		public void beforeRender(boolean isPostBack) {
			this.userid = AppUtils.getUserSession().getUserid();
			if (!isPostBack) {
				super.applyGridUserDef();
			}
			String jobid = AppUtils.getReqParam("jobid");
			if(!StrUtils.isNull(jobid)) {
				this.jobid = jobid;
			}
			
			actionJsText = "";
			List<Sysformcfg> sysformcfgs = this.serviceContext.sysformcfgService.sysformcfgDao.findAllByClauseWhere(" formid = '"+this.getMBeanName()+"' AND cfgtype = 'js' AND trim(COALESCE(jsaction,'')) <> ''");
			for (Sysformcfg sysformcfg : sysformcfgs) {
				actionJsText+=sysformcfg.getJsaction();
			}
			//System.out.println("actionJsText:"+actionJsText);
			update.markUpdate(true, UpdateLevel.Data, "actionJsText");
			getpolpod();
		}
		
		@Bind
		@SaveState
		public String qryshipline = "";
		
		@Override
		public Map getQryClauseWhere(Map<String, Object> queryMap) {
			Map<String, String> map = super.getQryClauseWhere(queryMap);
			
			
			
			String qry = map.get("qry");
			map.remove("qry");
			map.put("qry",qry);
			
			map.put("args","isqryPol="+isqryPol+";isshipline="+isshipline+";pol="+StrUtils.getSqlFormat(pol)+";pod="+pod+";carrier="
					+shipping+";line="+line+";qrycorpid="+qrycorpid+";srctype=fee_imp_quote_byhand;jobid="+jobid);
			
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
			
			//华展有订舱代理时不显示手工导入运价 Neo 20190630
			if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
				BusShipping busShipping = this.serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid = '"+this.jobid+"'");
				if(busShipping != null && busShipping.getAgentid() != null){
					filter += "\n AND FALSE";
				}
			}
			
			
			//filter += "\n AND datefm >= COALESCE((SELECT ((COALESCE(COALESCE(x.bargeetd,x.etd),x.cls):: TIMESTAMP) + '-10Day')::DATE FROM bus_shipping x where x.jobid = "+jobid+"),NOW()::DATE)";
			
			map.put("filter", filter);
			if(StrUtils.isNull(ord)){
				map.put("ord", "\nORDER BY datefm");
			}else{
				map.put("ord", "\nORDER BY " + ord + ",datefm");
			}
			return map;
		}
		
		@Bind
	    @SaveState
	    public String isqryPol = "Y";
		
		@Bind
	    @SaveState
	    public String isshipline = "Y";
		

		@Override
		public void refresh() {
			super.refresh();
			update.markUpdate(true, UpdateLevel.Data, "qryPolCom");
			update.markUpdate(true, UpdateLevel.Data, "qryPodCom");
			update.markUpdate(true, UpdateLevel.Data, "qryCarCom");
		}
		
		@Bind
		@SaveState
	    public Boolean isKeepLastQry = true;
		
		@Action
	    private void clearQry2() {
			isKeepLastQry = false;
			
			this.line = "";
			this.qryshipline = "";
			this.shipping = "";
		}
		
		@Bind
		public UIIFrame localChargeIframe;
		
		
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
		}
		
		@SaveState
		private String ord = "";
		
		@Action
		public void sortGridRemote(){
			String desctype = AppUtils.getReqParam("desctype");
			if(desctype.equals("DESC")){
				ord = " cost20 DESC,cost40gp DESC,cost40hq DESC,cost45hq DESC";
			}else{
				ord = " cost20,cost40gp,cost40hq,cost45hq";
			}
			this.grid.reload();
			Browser.execClientScript("setBargeClolr()");
		}
		
		@Override
		public void qryRefresh(){
			if (grid != null) {
				ord = "";
				this.grid.reload();
				Browser.execClientScript("setBargeClolr()");
			}
		}
		
		
		public void getpolpod(){
			BusShipping busShipping = this.serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid = '"+this.jobid+"'");
			if(busShipping != null){
				try {
					if(!StrUtils.isNull(busShipping.getPoa())){
						this.pol = busShipping.getPoa();
					}else if(!StrUtils.isNull(busShipping.getPol())){
						//this.pol = busShipping.getPol();
						this.pol = this.serviceContext.portyMgrService.datPortDao.findById(busShipping.getPolid()).getNamee();//从id反查港口英文名
					}
					if(!StrUtils.isNull(busShipping.getPod())){
						//this.pod = busShipping.getPod();
						this.pod = this.serviceContext.portyMgrService.datPortDao.findById(busShipping.getPodid()).getNamee();//从id反查港口英文名
					}
					
					if(isKeepLastQry){
						if(!StrUtils.isNull(busShipping.getRoutecode())){
							try {
								String sql = "SELECT x.namec AS linenamec FROM dat_line x WHERE x.isdelete = false AND (x.code = '"+busShipping.getRoutecode()+"' OR x.namec = '"+busShipping.getRoutecode()+"') LIMIT 1";
								Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
								this.line = StrUtils.getMapVal(map, "linenamec");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						if(!StrUtils.isNull(busShipping.getPod())){
							this.qryshipline = busShipping.getLinecode();
						}
						
						if(busShipping.getCarrierid() != null && busShipping.getCarrierid() > 0){
							this.shipping = this.serviceContext.sysCorporationService.sysCorporationDao.findById(busShipping.getCarrierid()).getCode();
							this.shipping = this.shipping.replaceAll("1","");//去掉代码中数字，凌润代码前加了个1
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		//导入
		@Action
		public void importData() {
			String[] ids = this.grid.getSelectedIds();
			if (ids == null || ids.length != 1) {
				MessageUtils.alert("请勾选一行记录!");
				return;
			}
			String uuid = ids[0];
			String priceid = uuid.split("-")[0];
			String bargeid = uuid.split("-")[1];
			
			try {
				String sql = "SELECT f_fina_fee_imp_quote('jobid="+this.jobid+";userid="+AppUtils.getUserSession().getUserid()+";type=create;src=pricebyhand;priceid="+priceid+";bargeid="+bargeid+";') AS returntext;";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String alerttext = m.get("returntext").toString();
				if(!StrUtils.isNull(alerttext)){
					alert(alerttext);
				}else{
					this.alert("OK");
					Browser.execClientScript("parent.refreshJs.fireEvent('click')");
				}
				//this.refresh();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}
	
}
