	
	package com.scp.view.module.salesmgr;

	import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

	@ManagedBean(name = "pages.module.salesmgr.pubcustomerBean", scope = ManagedBeanScope.REQUEST)
	public class PubCustomerBean extends GridView {
		
		
		
		@Override
		public void beforeRender(boolean isPostBack) {
			// TODO Auto-generated method stub
			super.beforeRender(isPostBack);
			super.applyGridUserDef();
		}

		@Action
		public void add() {
			String winId = "_edit_mycustomer";
			String url = "./mycustomeredit.xhtml";
			AppUtils.openWindow(winId, url);
		}
		
		
		@Bind
		public UIWindow join2Window;
		
		
		@Action
		public void joinConfirm() {
			String ids[] = this.grid.getSelectedIds();
			try {
				String idto = join2Customerid;
				for (String id : ids) {
					//AppUtils.debug(id);
					String sql = new String();
					String idfm = id;
					if(idfm.equals(idto))continue;
					sql = "\nSELECT f_sys_corporation_join('idfm="+idfm+";idto="+idto+";user="+AppUtils.getUserSession().getUsercode()+"');";
					//AppUtils.debug(sql);
					this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
				}
				this.alert("OK!");
				this.refresh();
				join2Window.close();
				ishow = false;
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}
		
		@SaveState
		private boolean ishow;
		
		@Bind
		@SaveState
		private String join2Customerid;
		
		/**
		 * 合并客户
		 */
		@Action
		public void join() {
			String ids[] = this.grid.getSelectedIds();

			if(ids == null || ids.length < 2) {
				this.alert("请至少选择两行!");
				return;
			}
			ishow = true;
			join2Window.show();
			this.update.markUpdate(UpdateLevel.Data, "join2Customerid");
			//ishow = false;
		}
		
		@Bind(id="join2Customer")
	    public List<SelectItem> getJoin2Customer() {
			if(!ishow)return null;
			String ids[] = this.grid.getSelectedIds();
			if(ids == null || ids.length < 2) {
				return null;
			}
			String id = StrUtils.array2List(ids);
	    	try {
				return CommonComBoxBean.getComboxItems("d.id","d.code||'/'||COALESCE(abbr,'')","sys_corporation AS d","WHERE d.id IN ("+id+")","ORDER BY d.code");
			} catch (Exception e) {
				MessageUtils.showException(e);
				return null;
			}
	    }
		
		
		@Override
		public void grid_ondblclick() {
			super.grid_ondblclick();
			String winId = "_edit_mycustomer";
			String url = "./mycustomeredit.xhtml?id="+this.getGridSelectId();
			AppUtils.openWindow(winId, url);
		}

		
		@Override
		public Map getQryClauseWhere(Map<String, Object> queryMap) {
			Map map = super.getQryClauseWhere(queryMap);
			Long userid = AppUtils.getUserSession().getUserid();
			map.put("filter", "AND EXISTS (SELECT 1 FROM sys_custlib x,sys_custlib_cust y,sys_custlib_user z WHERE x.libtype = 'L' AND " +
					"code = 'PubCustomer' AND y.custlibid = x.id AND z.custlibid = x.id AND y.corpid = a.id AND z.userid = "+AppUtils.getUserSession().getUserid()+")");
			return map;
		}
	}


