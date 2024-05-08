package com.scp.view.module.price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.chartsiframeBean", scope = ManagedBeanScope.REQUEST)
public class ChartsIframeBean extends GridView{
    
    @Bind
    @SaveState
    private String dateType;
    
    @Bind
    @SaveState
    private String json = "''";

    @Bind
    @SaveState
    private boolean cnt20gp;
    
    @Bind
    @SaveState
    private boolean cnt40gp;
    
    @Bind
    @SaveState
    private boolean cnt40hq;

    
    @Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			initUrl();
		}
	}
    
    @Bind
    @SaveState
    private String pol_rp;
    
    @Bind
    @SaveState
    private String pod_rp;
    
    @Bind
    @SaveState
    private String shipping_rp;
    
    @Bind
    @SaveState
    private String dateto_rp;
    
    @Bind
    @SaveState
    private String dateto_c;
    
    private void initUrl(){
    	this.pol_rp =  AppUtils.getReqParam("pol");
    	this.pod_rp =  AppUtils.getReqParam("pod");
    	this.shipping_rp =  AppUtils.getReqParam("shipping");
    	this.dateto_rp =  AppUtils.getReqParam("dateto");
		if(StrUtils.isNull(pol_rp)||StrUtils.isNull(pod_rp)||StrUtils.isNull(shipping_rp)||StrUtils.isNull(dateto_rp)){
			MessageUtils.alert("Data error,please contact Manager!");
			return;
		}
		update.markUpdate(UpdateLevel.Data, "pol_rp");
		update.markUpdate(UpdateLevel.Data, "pod_rp");
		update.markUpdate(UpdateLevel.Data, "shipping_rp");
		update.markUpdate(UpdateLevel.Data, "dateto_rp");
		update.markUpdate(UpdateLevel.Data, "dateto_c");
    	initData();
    }
    
    private void initData(){
    	Date dateto_c = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateto_c = sdf.parse(dateto_rp);
		} catch (ParseException e) {
			dateto_c = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateto_c);
		calendar.add(Calendar.DAY_OF_MONTH,-365);//截至日期往前查1年
		Date datefm_c = calendar.getTime();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("\n SELECT * FROM f_charts_pricefcl('");
		sbsql.append("pol=");
		sbsql.append(pol_rp);
		sbsql.append(";");
		sbsql.append("pod=");
		sbsql.append(pod_rp);
		sbsql.append(";");
		sbsql.append("datefm=");
		sbsql.append(sdf.format(datefm_c));
		sbsql.append(";");
		sbsql.append("dateto=");
		sbsql.append(sdf.format(dateto_c));
		sbsql.append(";");
		if(this.dateType != null){
    		sbsql.append("type=");
    		sbsql.append(this.dateType);
    		sbsql.append(";");
    	}else{
    		sbsql.append("type=week;");
    	}
		StringBuffer cntypesql = new StringBuffer("cntype=");
    	if(this.cnt20gp){
    		cntypesql.append("20gp,");
    	}
    	if(this.cnt40gp){
    		cntypesql.append("40gp,");
    	}
    	if(this.cnt40hq){
    		cntypesql.append("40hq,");
    	}
    	sbsql.append(cntypesql.substring(0, cntypesql.toString().endsWith(",") ? cntypesql.length()-1 : cntypesql.length())+";");
		sbsql.append("shipping=");
		sbsql.append(shipping_rp);
		sbsql.append(";') as json;");
		Map map = this.serviceContext.pricefclMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
		if(map != null && map.containsKey("json") && map.get("json") != null){
    		this.json = map.get("json").toString();
    	}else{
    		this.json = "''";
    	}
    	update.markUpdate(UpdateLevel.Data, "json");
    	Browser.execClientScript("initData();");
    
    }
    
    
    @Action(id="dateType",event="onselect")
    public void dateTypeChange(){
    	if(this.dateType == null || this.dateType.isEmpty()){
    		MessageUtils.alert("");
    		return;
    	}else{
    		initData();
    	}
    }
    
    @Action(id="cnt20gp",event="oncheck")
    public void cntype_cnt20gp(){
    	initData();
    }
    
    @Action(id="cnt40gp",event="oncheck")
    public void cntype_cnt40gp(){
    	initData();
    }
    
    @Action(id="cnt40hq",event="oncheck")
    public void cntype_cnt40hq(){
    	initData();
    }
    
}
