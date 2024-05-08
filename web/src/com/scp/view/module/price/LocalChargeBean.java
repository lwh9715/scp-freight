package com.scp.view.module.price;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.price.localchargeBean", scope = ManagedBeanScope.REQUEST)
public class LocalChargeBean extends GridView {
	
	@SaveState
	@Bind
	public String linkid = "-123";
	
	@SaveState
	@Bind
	public String bargeid = "-123";
	
	@SaveState
	@Bind
	public String src = "";

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("linkid");
			String bargeidStr = AppUtils.getReqParam("bargeid");
			String type = AppUtils.getReqParam("type");
			src = AppUtils.getReqParam("src");
			
			if(!StrUtils.isNull(id)){
				linkid = id;
			}
			
			if(!StrUtils.isNull(bargeidStr)){
				bargeid = bargeidStr;
			}
			
			if(!StrUtils.isNull(type)){
				Browser.execClientScript("remarkinpanel.hide();remarkoutpanel.hide();remark3panel.hide();");
			}
			getRemarks();
			getBarremark();
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String args = "'priceid="+linkid+";bargeid="+bargeid+"'";

		String filter = "\nAND 1=1";
		if(!StrUtils.isNull(src) && "query".equals(src)){// neo 20181105 运价查询列表中多行显示中已包含了燃油附加费和中转费，查询弹窗中不再显示重复的这两项费用明细
			filter += "\nAND feeitemname != '紧急燃油附加费' AND feeitemname != '中转费' AND feeitemname != '驳船费'";
		}
		map.put("args", args);
		map.put("filter", filter);
		return map;
	}
	
	@SaveState
	public String remarksout;
	
	@SaveState
	public String remarkin;
	
	@SaveState
	public String barremark;
	
	private void getRemarks() {
		try {
			String sql = "SELECT remark_out FROM price_fcl where id = "+linkid+"";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			remarksout = m.get("remark_out").toString().replace("\r\n","<BR>").replace("\n","<BR>").replace("\"","\\\"");
		} catch (Exception e) {
			remarksout = "";
		}
		
		try {
			String sql = "SELECT remark_in FROM price_fcl where id = "+linkid+"";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			remarkin = m.get("remark_in").toString().replace("\r\n","<BR>").replace("\n","<BR>").replace("\"","\\\"");
		} catch (Exception e) {
			remarkin = "";
		}
		
		
	}
	
	private void getBarremark() {
		try {
			String sql = "SELECT remark FROM price_fcl_bargefeedtl where id = "+bargeid+"";
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			barremark = m.get("remark").toString().replace("\r\n","<BR>").replace("\n","<BR>").replace("\"","\\\"");
		} catch (Exception e) {
			barremark = "";
		}
	}
	
	
}
