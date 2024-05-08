package com.scp.model.edi;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;
import com.ufms.base.db.SqlObject;
import com.ufms.base.db.SqlUtil;

@ManagedBean(name = "pages.module.edi.esidtlcntlistBean", scope = ManagedBeanScope.REQUEST)
public class EsidtlcntlistBean extends EditGridView{
	
	
	@SaveState
	@Bind
	public String ediesiid;
	
	@SaveState
	@Bind
	public String esidtlcntid;
	
	@SaveState
	@Bind
	public String type;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			esidtlcntid = AppUtils.getReqParam("esidtlcntid");
			ediesiid = AppUtils.getReqParam("ediesiid");
			type = AppUtils.getReqParam("type");
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		m.put("qry", qry);
		
		String filter = "\nAND esidtlcntid = " + esidtlcntid + " AND ediesiid = "+ediesiid+" ";
		m.put("filter", filter);
		
		return m;
	}
	
	  /**
	 * @return
	 */
	@Bind(id="package")
    public List<SelectItem> getPackage() {
    	try {
    		return serviceContext.commonDBCache.getComboxItems("x.code","x.namee","api_data x","WHERE x.srctype = 'ESI-CARGOSMART' AND x.maptype = 'PACKAGE'","ORDER BY x.namee");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	/**
	 * @return
	 */
	@Bind(id="otherCnt")
    public List<SelectItem> getOtherCnt() {
    	try {
			return serviceContext.commonDBCache.getComboxItems("x.id","x.cntno","edi_esidtlcnt x","WHERE x.ediesiid = "+this.ediesiid,"ORDER BY x.cntno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	
	
	
	@Override
	protected void update(Object modifiedData) {
	}
	
	@Override
	protected void add(Object addedData) {
	}
	@Override
	protected void remove(Object removedData) {
	}
	
	@Action
	private void saveEditGrid() {
		try {
			editGrid.commit();
			JSONArray jsonArray = new JSONArray();
			if (modifiedData != null) {
				jsonArray.addAll((JSONArray)modifiedData);
			}
			if (addedData != null) {
				JSONArray addArrays = (JSONArray)addedData;
				for (Object object : addArrays) {
					JSONObject jsonObject = (JSONObject)object;
					jsonObject.put("id", "");
				}
				jsonArray.addAll((JSONArray)addedData);
			}
			if (removedData != null) {
				jsonArray.addAll((JSONArray)removedData);
			}
			if(!jsonArray.isEmpty()){
				SqlObject sqlObject = new SqlObject("edi_esidtlcntlist", jsonArray.toString(), AppUtils.getUserSession().getUsercode());
				sqlObject.setFkVals("esidtlcntid", esidtlcntid);
				sqlObject.setFkVals("ediesiid", ediesiid);
				String sql = SqlUtil.builds(sqlObject);
				System.out.println(sql);
				this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
			}
			editGrid.reload();
			alert("OK");
			Browser.execClientScript("parent.refreshCntGrid()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	
	@Action(id = "removes")
    public void removes() {
		try {
        	String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("Please choose one!");
                return;
            }
            try {
            	StringBuffer buffer = new StringBuffer();
            	for (String id : ids) {
            		if(StrUtils.isNumber(id)){
            			buffer.append("\nDELETE FROM edi_esidtlcntlist WHERE id IN(" + StrUtils.array2List(ids) + ");");
            		}
				}
            	if(buffer.length()>0){
            		daoIbatisTemplate.updateWithUserDefineSql(buffer.toString());
            	}
            } catch (Exception e) {
                MessageUtils.showException(e);
                return;
            }
            MessageUtils.alert("OK!");
            this.editGrid.reload();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
        }
    }
	
	
	@Bind
    public UIDataGrid copyCntGrid;

    @Bind(id = "copyCntGrid", attribute = "dataProvider")
    protected GridDataProvider getCopyCntGridDataProvider() {
        return new GridDataProvider() {

            @Override
            public Object[] getElements() {
                if (gridLazyLoad) {
                    return new Object[]{};
                } else {
                    starts = start;
                    limits = limit;
                    String sqlId = getMBeanName() + ".copyCntGrid.page";
                    return daoIbatisTemplate.getSqlMapClientTemplate()
                    	.queryForList(sqlId, getCopyCntGridQryClauseWhere(qryMap)).toArray();
                }
            }

            @Override
            public int getTotalCount() {
                if (gridLazyLoad) {
                    return 0;
                } else {
                    String sqlId = getMBeanName() + ".copyCntGrid.count";
                    List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
                            .queryForList(sqlId, getCopyCntGridQryClauseWhere(qryMap));
                    if (list == null || list.size() < 1)
                        return 0;
                    Long count = Long.parseLong(list.get(0).get("counts")
                            .toString());
                    return count.intValue();
                }
            }
        };
    }
    
    public Map getCopyCntGridQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String filter = "\nAND ediesiid = " + this.ediesiid;
        m.put("filter", filter);
        return m;
    }
    
    
    @Action
    public void copyOtherCnt() {
    	String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length <= 0) {
            MessageUtils.alert("Please choose one!");
            return;
        }
        Browser.execClientScript("addWindowJsVar.show()"); 
        
    }
	
	@Action
    public void copyConfirm() {
		try {
			String[] ids = this.editGrid.getSelectedIds();
            
            String[] copy2cntids = this.copyCntGrid.getSelectedIds();
            if (copy2cntids == null || copy2cntids.length <= 0) {
                MessageUtils.alert("Please choose one!");
                return;
            }
            
        	String sql = "SELECT f_edi_esidtlcntlistcopy('ids=" + StrUtils.array2List(ids) + ";copy2cntids="+StrUtils.array2List(copy2cntids)+"');";
        	daoIbatisTemplate.updateWithUserDefineSql(sql);
            
            MessageUtils.alert("OK!");
            this.editGrid.reload();
            Browser.execClientScript("parent.refreshCntGrid()");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showException(e);
        }
    }
	
}
