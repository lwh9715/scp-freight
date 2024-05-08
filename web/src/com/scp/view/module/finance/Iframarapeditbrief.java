package com.scp.view.module.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.annotation.Transactional;

import com.scp.model.data.DatFeeitem;
import com.scp.model.finance.FinaArap;
import com.scp.model.sys.SysFormdef;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.finance.iframarapeditbriefBean", scope = ManagedBeanScope.REQUEST)
public class Iframarapeditbrief extends FormView {

	@Bind
	@SaveState
	private String jsonData;
	
	@Bind
	@SaveState
	private Long jobid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			usrCfg();
			String jobid = AppUtils.getReqParam("jobid");
			this.jobid = Long.parseLong(jobid);
			jsonData = serviceContext.arapMgrService.getArapAndFormdefJsonByJobid(jobid,"pages.module.finance.iframarapeditbriefBean");
		}
	}
	
	@SaveState
	public String currencycyadd = "CNY";//个人设置币制
	
	@SaveState
	public String payplaceadd = "CS";
	
	/*
	 * 查个人设置
	 * */
	public void usrCfg(){
		Map map = ConfigUtils.findUserCfgVals(new String[]{"fee_profits_cy2","fee_add_cyid","fee_add_payplace"}
		, AppUtils.getUserSession().getUserid());
		currencycyadd = AppUtils.getUserSession().getBaseCurrency();
		if(map != null && map.size() > 0){
			if(map.get("fee_add_cyid")!=null&&!map.get("fee_add_cyid").equals("")){
				this.currencycyadd = map.get("fee_add_cyid").toString();
			}
			String payplace = StrUtils.getMapVal(map, "fee_add_payplace");
			if(!StrUtils.isNull(payplace)){
				payplaceadd = payplace;
			}
		}
	}
	
	@Action
	@Transactional
	private void saveAjaxSubmit() {
		try{
			String dataar = AppUtils.getReqParam("dataar");
			String dataap = AppUtils.getReqParam("dataap");
    		if (!StrUtils.isNull(dataar) && !"null".equals(dataar)) {
    			saveformarap(dataar, "AR");
    		}
    		if (!StrUtils.isNull(dataap) && !"null".equals(dataap)) {
    			saveformarap(dataap, "AP");
    		}
    		MessageUtils.alert("OK");
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	/**
	 * 保存费用
	 * @param data Json字符串
	 * @param araptype 应收应付类型
	 */
	public void saveformarap(String data,String araptype){
		ArrayList<Long> ids = new ArrayList<Long>();
		ArrayList<Long> feeidsdel = new ArrayList<Long>();
		if (!StrUtils.isNull(data) && !"null".equals(data)) {
			JSONArray jsonarry = JSONArray.fromObject(data);
			for(int i=0;i<jsonarry.size();i++){
				JSONObject jsonar = JSONObject.fromObject(jsonarry.get(i));
				String id = jsonar.get("id").toString();
				String feeitemid = jsonar.get("feeitemid").toString();
				String price = jsonar.get("price").toString();
				if(!StrUtils.isNull(feeitemid)&&!feeitemid.equals("-100")){
    				SysFormdef sysFormdef = new SysFormdef();
    				DatFeeitem datFeeitem = serviceContext.feeItemMgrService.datFeeitemDao.findById(Long.parseLong(feeitemid));
    				sysFormdef.setInputlable(datFeeitem.getName());
    				sysFormdef.setColumnname(feeitemid);
    				sysFormdef.setDefvalue(araptype);
    				sysFormdef.setBeaname("pages.module.finance.iframarapeditbriefBean");
    				if(!StrUtils.isNull(id)&&!id.equals("-100")){
    					sysFormdef.setId(Long.parseLong(id));
    					sysFormdef.setIshidden(false);
    					serviceContext.sysFormdefService.sysFormdefDao.modify(sysFormdef);
    					ids.add(Long.parseLong(id));
    				}else{
    					sysFormdef.setIshidden(false);
    					serviceContext.sysFormdefService.sysFormdefDao.create(sysFormdef);
    					ids.add(sysFormdef.getId());
    				}
    				if(!StrUtils.isNull(price)&&(new BigDecimal(price).intValue())>0){//如果有值就去对应费用表查找对应费用更新，没有就新增这条费用
    					serviceContext.arapMgrService.updateOrAddarapByfeeid(jobid
    							, araptype, Long.parseLong(feeitemid), new BigDecimal(price));
    				}else{//没有费用就删除这条费用
    					feeidsdel.add(Long.parseLong(feeitemid));
    				}
				}
			}
		}
		try {
			serviceContext.arapMgrService.delarapByfeeid(jobid, araptype, feeidsdel);
			serviceContext.sysFormdefService.delbefor(ids,"pages.module.finance.iframarapeditbriefBean",araptype);
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Action
	public void delfeeAjaxSubmit(){
		String feeid = AppUtils.getReqParam("feeid");
		String araptype = AppUtils.getReqParam("araptype"); 
		List<FinaArap> araps = serviceContext.arapMgrService.finaArapDao.findAllByClauseWhere(" isdelete = FALSE AND araptype='"+araptype+"' AND feeitemid="+feeid);
		if(araps!=null&&araps.size()>0){
			MessageUtils.alert("工作单中存在此费用，不可删除");
			return;
		}else{
			Browser.execClientScript("delfeeon();");
		}
	}
}
