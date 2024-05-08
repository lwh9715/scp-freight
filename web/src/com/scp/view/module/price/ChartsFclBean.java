package com.scp.view.module.price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBoxGroup;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.chartsfclBean", scope = ManagedBeanScope.REQUEST)
public class ChartsFclBean extends GridView{
    @Bind
    @SaveState
    private String pol;
    
    @Bind
    @SaveState
    private String pod;
    
    @Bind
    @SaveState
    private Date dateFrom = null;
    
    @Bind
    @SaveState
    private Date dateTo = new Date();
    
    @Bind
    @SaveState
    private String line;
    
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

    @Bind("shipping")
    @SaveState
    private UICheckBoxGroup shipping;
    
    @Bind(id = "shippings")
    @SaveState
    public SelectItem[] shippings = null;
    
    @Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.refresh();
		}
	}
    
    @Override
    public void refresh() {
    	if(this.pol == null || this.pol.isEmpty()){
    		this.pol = "YANTIAN";
    	}
    	if(this.dateFrom == null){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    		try {
				this.dateFrom = sdf.parse(sdf.format(new Date()));
			} catch (ParseException e) {
				this.dateFrom = new Date();
			}
    	}
    	update.markUpdate(UpdateLevel.Data,"dateFrom");
    	update.markUpdate(UpdateLevel.Data,"pol");
    	
    	initShipping();
    	initData();
    }
    
    private void initShipping(){
    	StringBuffer shipsql = new StringBuffer("SELECT DISTINCT shipping");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	shipsql.append(" FROM price_fcl WHERE isdelete = FALSE AND isrelease = TRUE");
    	if(this.pol != null && !this.pol.isEmpty()){
    		shipsql.append("\n AND pol = '"+this.pol+"'");
    	}
    	if(this.pod != null && !this.pod.isEmpty()){
    		shipsql.append("\n AND pod = '"+this.pod+"'");
    	}
    	if(this.line != null && !this.line.isEmpty()){
    		shipsql.append("\n AND line = '"+this.line+"'");
    	}
    	if(this.dateFrom != null){
    		shipsql.append("\n AND datefm >= '"+sdf.format(this.dateFrom)+"'");
    	}
    	if(this.dateTo != null){
    		shipsql.append("\n AND dateto <= '"+sdf.format(this.dateTo)+"'");
    	}
    	try {
			List<Map> list = this.serviceContext.pricefclMgrService.daoIbatisTemplate.queryWithUserDefineSql(shipsql.toString());
			if(list != null && list.size() > 0){
				this.shippings = new SelectItem[list.size()];
				for (int i = 0;i < list.size();i++) {
					this.shippings[i] = new SelectItem(list.get(i).get("shipping"), list.get(i).get("shipping").toString());
				}
			}else{
				this.shippings = null;
			}
		} catch (NoRowException e) {
			this.shippings = null;
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
		update.markUpdate(UpdateLevel.Data, "shippings");
    }
    
    private void initData(){
    	StringBuffer sbsql = new StringBuffer();
    	sbsql.append("\n SELECT * FROM f_charts_pricefcl('");
    	if(this.pol != null && !this.pol.isEmpty()){
    		sbsql.append("pol=");
    		sbsql.append(this.pol);
    		sbsql.append(";");
    	}else{
    		return;
    	}
    	if(this.pod != null && !this.pod.isEmpty()){
    		sbsql.append("pod=");
    		sbsql.append(this.pod);
    		sbsql.append(";");
    	}else{
    		return;
    	}
    	if(this.line != null && !this.line.isEmpty()){
    		sbsql.append("line=");
    		sbsql.append(this.line);
    		sbsql.append(";");
    	}
    	try {
			Set<Object> set =  (Set<Object>) shipping.getValue();
			StringBuffer sbship = new StringBuffer();
			sbship.append("shipping=");
			if(set!=null && set.size() > 0){
				for (Object o : set) {
					sbship.append(o.toString());
					sbship.append(",");
				}
				sbsql.append(sbship.toString().endsWith(",") ? sbship.substring(0, sbship.length()-1) : sbship.toString());
				sbsql.append(";");
			}else{
				sbsql.append("shipping=;");
			}
		} catch (Exception e) {
			sbsql.append("shipping=;");
			e.printStackTrace();
		}
    	if(this.dateFrom != null){
    		sbsql.append("datefm=");
    		sbsql.append(new SimpleDateFormat("yyyy-MM-dd").format(this.dateFrom));
    		sbsql.append(";");
    	}else{
    		return;
    	}
    	if(this.dateTo != null){
    		sbsql.append("dateto=");
    		sbsql.append(new SimpleDateFormat("yyyy-MM-dd").format(this.dateTo));
    		sbsql.append(";");
    	}else{
    		return;
    	}
    	if(this.dateType != null){
    		sbsql.append("type=");
    		sbsql.append(this.dateType);
    		sbsql.append(";");
    	}else{
    		return;
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
    	sbsql.append("') as json;");
    	Map map = this.serviceContext.pricefclMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbsql.toString());
    	if(map != null && map.containsKey("json") && map.get("json") != null){
    		this.json = map.get("json").toString();
    	}else{
    		this.json = "''";
    	}
    	update.markUpdate(UpdateLevel.Data, "json");
    	Browser.execClientScript("initData();");
    }
    
    @Action(id="pol",event="onselect")
    public void polChange(){
    	if(this.pol == null || this.pol.isEmpty()){
    		MessageUtils.alert("");
    		return;
    	}else{
    		initShipping();
    		initData();
    	}
    }
    
    @Action(id="pod",event="onselect")
    public void podChange(){
    	if(this.pod == null || this.pod.isEmpty()){
    		MessageUtils.alert("");
    		return;
    	}else{
    		initShipping();
    		initData();
    	}
    }
    
    @Action(id="dateFrom",event="onselect")
    public void dateFromChange(){
    	if(this.dateFrom == null || this.dateTo == null || this.dateFrom.getTime() > this.dateTo.getTime()){
    		this.dateFrom = this.dateTo;
    		return;
    	}else{
    		initShipping();
    		initData();
    	}
    }
    
    @Action(id="dateTo",event="onselect")
    public void dateToChange(){
    	if(this.dateFrom == null || this.dateTo == null || this.dateFrom.getTime() > this.dateTo.getTime()){
    		this.dateTo = this.dateFrom;
    		return;
    	}else{
    		initShipping();
    		initData();
    	}
    }
    
    
    @Action(id="line",event="onselect")
    public void lineChange(){
    	if(this.line == null || this.line.isEmpty()){
    		MessageUtils.alert("");
    		return;
    	}else{
    		initShipping();
    		initData();
    	}
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
    
    @Action(id="shipping",event="oncheck")
    public void select_shipping(){
    	initData();
    }
    
    @Bind(id="qryPol")
    public List<SelectItem> getQryPol() {
    	try {
			return CommonComBoxBean.getComboxItems("DISTINCT pol","pol","(select pol,pod FROM _price_fcl t WHERE isdelete = false AND isvalid = 'R' ) AS T","WHERE 1=1","ORDER BY pol");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
    @Bind(id="qryPod")
    public List<SelectItem> getQryPod() {
    	try {
    		return CommonComBoxBean.getComboxItems("DISTINCT pod","pod","(select pol,pod FROM _price_fcl t WHERE isdelete = false AND isvalid = 'R' ) AS T","WHERE 1=1","ORDER BY pod");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
    
}
