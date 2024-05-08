package com.scp.base;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.grid.CellSelectionModel;
import org.operamasks.faces.component.grid.CheckboxSelectionModel;
import org.operamasks.faces.component.grid.GridSelectionModel;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

@SuppressWarnings("deprecation")
@ManagedBean(name = "constantBean", scope = ManagedBeanScope.APPLICATION)
public class ConstantBean {

	@Accessible
	public GridSelectionModel cell = new CellSelectionModel();

	@Accessible
	public GridSelectionModel checkBox = new CheckboxSelectionModel();

	private String contexPath = "/";

	@Accessible
	public String timeZone = "GMT+8";

	@Accessible
	public String editPanelId = "editPanel";

	@Accessible
	public String gridId = "grid";

	private String headTitle;

	public String getContextPath() {
		return AppUtils.getContextPath();
	}

	public void setHeadTitle(String headTitle) {
		this.headTitle = headTitle;
	}
	
	@Accessible
	public String getHeadTitle() {
		headTitle = ConfigUtils.findSysCfgVal("sys_header_title");
		return StrUtils.isNull(headTitle)?"航迅软件":headTitle;
	}

	public enum Module {
		fina_arap("费用管理", 299000L), 
		fina_jobs("工作单管理", 250000100200L),
		fs_vch("凭证",500000200300L),
		fina_actpayrec("收付款",513000L),
		bus_ship_bill("提单管理",250000100550L),
		bus_order("订单管理",250000100100L),
		fina_rpreq_payapplycheck("收付款申请审核",517500L),
		user_info_mgr("个人面板",690000L),
		fina_air("空运工作单管理",250000100900L),
		fina_jobs_land("陆运工作单", 250000300100L),
		fina_jobs_kahang("卡航工作单",250000300130L),
		wms_in("入仓单", 241000L);
		
		
		private String _name;
		private Long _value;

		private Module(String name, Long value) {
			_name = name;
			_value = value;
		}

		public String getName() {
			return _name;
		}

		public Long getValue() {
			return _value;
		}
	}



}
