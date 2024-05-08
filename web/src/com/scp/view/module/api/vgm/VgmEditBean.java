package com.scp.view.module.api.vgm;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.model.api.ApiVgm;
import com.scp.model.api.ApiVgmdtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.api.vgm.vgmeditBean", scope = ManagedBeanScope.REQUEST)
public class VgmEditBean extends GridFormView {

	@SaveState
	public ApiVgm vgm = new ApiVgm();

	@SaveState
	public ApiVgmdtl selectedRowData = new ApiVgmdtl();

	@Bind
	@SaveState
	public Long vgmid;
	
	@Bind
	@SaveState
	public String jobid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("id");
			jobid = AppUtils.getReqParam("jobid");
			String srctype = AppUtils.getReqParam("srctype");
			
			if(!StrUtils.isNull(jobid)){
				
				if(srctype.equals("esi")){
					String sql="select f_api_vgm('jobid="+this.jobid+";"+"userid="+AppUtils.getUserSession().getUserid()+";type=esiCreate');";
					daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				}
				
				String querySql = "SELECT id FROM api_vgm WHERE jobid = "+jobid+" LIMIT 1";
				Map map;
				try {
					map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
					id = StrUtils.getMapVal(map, "id");
				} catch (NoRowException e) {
				}
			}
			
			if (!StrUtils.isNull(id)) {
				Long e = Long.parseLong(id);
				this.vgmid = e;
				update.markUpdate(true, UpdateLevel.Data, "vgmid");
				this.refresh();

				if(!srctype.equals("esi")){
					String querySql = "select fj.nos as nos from fina_jobs fj left join api_vgm av on fj.id=av.jobid where fj.isdelete =false  and av.isdelete=false and av.id= "+id+" LIMIT 1";
					Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
					vgm.setNos( StrUtils.getMapVal(map, "nos"));
				}
			} else {
				this.add();
			}
		}
	}

	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = "\n AND vgmid = " + vgmid + "";
		m.put("filter", filter);
		return m;
	}

	@Override
	public void refresh() {
		vgm = serviceContext.apiVgmService.ApiVgmDao.findById(this.vgmid);
		super.refreshForm();
		this.grid.reload();
	}

	@Override
	public void save() {
		try {
			this.serviceContext.apiVgmService.ApiVgmDao.createOrModify(vgm);
			this.pkVal = selectedRowData.getId();
			update.markUpdate(true, UpdateLevel.Data, "pkVal");
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			String dmlSql = "DELETE FROM api_vgmdtl WHERE id IN("+StrUtils.array2List(ids)+");";
			daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}

	@Override
	public void add() {
		this.selectedRowData = new ApiVgmdtl();
		this.selectedRowData.setId(0L);
		this.selectedRowData.setVgmid(this.vgmid);
		editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}

	@Action
	public void savevgmdtl() {
		try {
			String wp = selectedRowData.getWeighingParty();
			if(!StrUtils.isNull(wp)){
				selectedRowData.setWeighingParty(wp.toUpperCase());
			}
			String signature = selectedRowData.getSignature();
			if(!StrUtils.isNull(signature)){
				selectedRowData.setSignature(signature.toUpperCase());
			}
			
			
			selectedRowData.setVgmid(vgmid);
			this.serviceContext.apiVgmdtlService.saveData(selectedRowData);
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

	}
	
	
	@Action
	public void vgmCreat() {
		try {
			if(!StrUtils.isNull(jobid)){
				String sql="select f_api_vgm('jobid="+jobid+";"+"userid="+AppUtils.getUserSession().getUserid()+";type=create')";
				daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				MessageUtils.alert("OK！");
				this.refresh();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.apiVgmdtlService.ApiVgmdtlDao.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
	}
	
	
	
	@Bind(id="port")
    public List<SelectItem> getPort() {
    	try {
			return CommonComBoxBean.getComboxItems("d.code","d.code || '/' || d.namec ","api_data d","WHERE srctype='wire' AND maptype = 'PORT' AND isdelete = FALSE","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="carrier")
    public List<SelectItem> getCarrier() {
    	try {
			return CommonComBoxBean.getComboxItems("d.code","d.namec","api_data d","WHERE srctype='wire' AND maptype = 'CARRIER' AND isdelete = FALSE","order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="pier")
    public List<SelectItem> getPier() {
    	try {
			return CommonComBoxBean.getComboxItems("d.namec","d.namec","api_data d","WHERE srctype='wire' AND maptype = 'PIER' AND isdelete = FALSE","order by namec");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Bind
	@SaveState
	public String info;
	
	@Action
	public void send() {
		try {
			String resp = ApiVgmTools.postVgm(vgmid.toString(), AppUtils.getUserSession().getUserid().toString());
			//System.out.println(resp);
			info = resp;
			this.refreshForm();
			this.refresh();
			update.markUpdate(true, UpdateLevel.Data, "info");
			Browser.execClientScript("infoJsVar.show()");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void sendTest() {
		try {
			String resp = ApiVgmTools.postVgmTest(vgmid.toString(), AppUtils.getUserSession().getUserid().toString());
			//System.out.println(resp);
			info = resp;
			this.refreshForm();
			this.refresh();
			update.markUpdate(true, UpdateLevel.Data, "info");
			Browser.execClientScript("infoJsVar.show()");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void getStatus() {
		try {
			ApiVgmTools.getVgmStatus(vgmid.toString(), AppUtils.getUserSession().getUserid().toString());
			ApiVgmTools.getVgmStatusTest(vgmid.toString(), AppUtils.getUserSession().getUserid().toString());
			this.alert("OK!");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		this.refresh();
		this.refreshForm();
	}
}
