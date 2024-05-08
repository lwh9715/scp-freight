package com.scp.view.module.oa.staffmgr;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.oa.OaUserErp;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;



@ManagedBean(name = "pages.module.oa.staffmgr.userjoinmgerBean", scope = ManagedBeanScope.REQUEST)
public class UserjoinmgerBean extends GridView{
	
	@Bind
	@SaveState
	@Accessible
	public OaUserErp selectedRowData = new OaUserErp();

	@Bind
	public UIWindow join2Window;
	
	@SaveState
	private boolean ishow;
	
	@Action
	public void join() {
	
		ishow = true;
		join2Window.show();
		//ishow = false;
	}

	@SuppressWarnings("deprecation")
	@Action
	public void joinConfirm() {
		try {
			Long oauserid = this.selectedRowData.getOauserid();
			Long erpuserid =this.selectedRowData.getErpuserid();
			if(oauserid==null||erpuserid==null){
				MessageUtils.alert("关联用户两边不能为空");
				return;
			}
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			String ucode=AppUtils.getUserSession().getUsercode();
			String sql = "INSERT INTO oa_user_erp(id,oauserid,erpuserid,inputer,inputtime)VALUES(getid(),"+oauserid+","+erpuserid+",'"+ucode+"',now());";
			daoIbatisTemplate.updateWithUserDefineSql(sql);
			
			join2Window.close();
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}


	@SuppressWarnings("deprecation")
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				
				DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
				//String ucode=AppUtils.getUserSession().getUsercode();
				String sql = "UPDATE oa_user_erp SET isdelete = TRUE WHERE id = "+ id +";";
				daoIbatisTemplate.updateWithUserDefineSql(sql);
			}
			MessageUtils.alert("OK!");
			//this.add();
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}





}
