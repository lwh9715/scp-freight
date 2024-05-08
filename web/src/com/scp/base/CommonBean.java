package com.scp.base;

import java.util.Locale;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.base.LMapBase.MLType;
import com.scp.exception.NoSessionException;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;

@ManagedBean(name = "commonBean", scope = ManagedBeanScope.REQUEST)
public class CommonBean{
	
	private String viewTipColor = null;

	//@Cacheable("234")
	public String getViewTipColor() {
		//neo 20170306 拿掉后台查询，不必要的数据库查询
//		if(viewTipColor == null){
//			String cfg = ConfigUtils.findUserCfgVal(ConfigUtils.UsrCfgKey.view_tip_color.name() , AppUtils.getUserSession().getUserid());
//			if(!StrUtils.isNull(cfg)){
//				viewTipColor = cfg;
//			}
//		}
		//if(StrUtils.isNull(viewTipColor))viewTipColor = "color: red";
		viewTipColor = "color: red";
		return viewTipColor;
	}
	

	@Accessible
	public String getCSNO() {
		return ConfigUtils.findSysCfgVal("CSNO");
	}
	
	
//	public String getExtJs() {
//		String js1 = "<script type=\"text/javascript\" src=\"/scp/_global/resource/ext/src/locale/ext-lang.js?"+getLang()+"\"></script>";
//		String js2 = "<script type=\"text/javascript\" src=\"/scp/_global/resource/om/locale/om-lang.js?"+getLang()+"\"></script>";
//		return js1 + "<br/>" + js2;
//	}
	
	public String getLang() {
		try {
			if((AppUtils.getUserSession().getMlType()).equals(MLType.en)){
				return "en_US";
			}
		} catch (NoSessionException e) {
		} catch (Exception e) {
			e.printStackTrace();
			return "zh_CN";
		}
		return "zh_CN";
	}
	
	public String getUserid() {
		try {
			return AppUtils.getUserSession().getUserid().toString();
		} catch (NoSessionException e) {
		} catch (Exception e) {
			return "-1";
		}
		return "-1";
	}
	
	public Locale getLocal() {
		if((AppUtils.getUserSession().getMlType()).equals(MLType.en)){
			return Locale.ENGLISH;
		}
		return Locale.SIMPLIFIED_CHINESE;
	}

}