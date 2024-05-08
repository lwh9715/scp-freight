package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.stock.popqrygridBean", scope = ManagedBeanScope.REQUEST)
public class PopQryGridBean extends GridView{
	
	
	
	@SaveState
	@Bind
	public String type;
	
	
	@SaveState
	@Bind
	public String qryFilter;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.type = AppUtils.getReqParam("type");
		}
	}
	 
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "";
		String table = "";
		
		if("pol".equals(this.type) || "pod".equals(this.type) || "poa".equals(this.type) || "pdd".equals(this.type) || "destination".equals(this.type)){
			table = "dat_port";
			filter += "\n AND isdelete = FALSE";
			filter += "\n AND istrain = TRUE";
		}
		
		if("lines".equals(this.type)){
			table = "dat_line";
			filter += "\n AND isdelete = FALSE";
			filter += "\n AND lintype = 'T'";
		}
		
		if("sales".equals(this.type)){
			table = "sys_user";
			filter += "\n AND isdelete = FALSE";
			filter += "\n AND issales = TRUE AND isinvalid = TRUE";
		}
		
		if("customer".equals(this.type)){
			table = "sys_corporation";
			filter += "\n AND isdelete = FALSE";
			filter += "\n AND isofficial = TRUE";
		}
		
		if("factory".equals(this.type)){
			table = "sys_corporation";
			filter += "\n AND isdelete = FALSE";
			filter += "\n AND isofficial = TRUE";
			filter += "\n AND isfactory = TRUE";
		}
		
		if("agentcnt".equals(this.type) || "agentname".equals(this.type)){
			table = "sys_corporation";
			filter += "\n AND isdelete = FALSE";
			filter += "\n AND isofficial = TRUE";
			filter += "\n AND isagentdes = FALSE";
		}
		
		
		if(this.type.startsWith("wms")){
			table = "dat_warehouse";
			filter += "\n AND isdelete = FALSE";
		}
		
		if("pol".equals(this.type)){
			filter += "\n AND t.ispol = true ";
		}else if("pod".equals(this.type)){
			filter += "\n AND t.ispod = true ";
		}else if("pdd".equals(this.type)){
			filter += "\n AND t.ispdd = true ";
		}else if("poa".equals(this.type)){
			filter += "\n AND t.isbarge = true ";
		}else if("destination".equals(this.type)){
			filter += "\n AND t.isdestination = true ";
		}
		
		if(!StrUtils.isNull(qryFilter)){
			filter += "\n AND (code ILIKE '%"+qryFilter+"%' OR namec ILIKE '%"+qryFilter+"%' OR namee ILIKE '%"+qryFilter+"%' )";
		}
		
		m.put("filter", filter);
		m.put("table", table);
		
		return m;
	}
	
	@Override
	public void grid_ondblclick() {
		Long id = getGridSelectId();
		Browser.execClientScript("parent.hook('"+this.type+"',"+id+")");
	}
	
}
