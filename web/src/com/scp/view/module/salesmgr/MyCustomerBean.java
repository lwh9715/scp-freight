package com.scp.view.module.salesmgr;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.salesmgr.mycustomerBean", scope = ManagedBeanScope.REQUEST)
public class MyCustomerBean extends GridView {
	
    @Bind
    public String salesnamec;

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

    @Action
    public void addBlacklist() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请选择记录");
            return;
        }
        try {
            for (String id : ids) {

                String querySql = "SELECT f_sys_corporation_black('id=" + id + ";usercode=" + AppUtils.getUserSession().getUsercode() + "');";
                this.serviceContext.attachmentService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            }
            MessageUtils.alert("OK!");
            this.grid.reload();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
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
                if (idfm.equals(idto)) continue;
                sql = "\nSELECT f_sys_corporation_join('idfm=" + idfm + ";idto=" + idto + ";user=" + AppUtils.getUserSession().getUsercode() + "');";
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

        if (ids == null || ids.length < 2) {
            this.alert("请至少选择两行!");
            return;
        }
        ishow = true;
        join2Window.show();
        this.update.markUpdate(UpdateLevel.Data, "join2Customerid");
        //ishow = false;
    }

    @Bind(id = "join2Customer")
    public List<SelectItem> getJoin2Customer() {
        if (!ishow) return null;
        String ids[] = this.grid.getSelectedIds();
        if (ids == null || ids.length < 2) {
            return null;
        }
        String id = StrUtils.array2List(ids);
        try {
            return CommonComBoxBean.getComboxItems("d.id", "d.code||'/'||COALESCE(abbr,'')", "sys_corporation AS d", "WHERE d.id IN (" + id + ")", "ORDER BY d.code");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }


    @Override
    public void grid_ondblclick() {
        super.grid_ondblclick();
        String winId = "_edit_mycustomer";
        String url = "./mycustomeredit.xhtml?id=" + this.getGridSelectId();
        AppUtils.openWindow(winId, url);
    }

    @Action
    public void applyMonth() {
        Long id = this.getGridSelectId();

        if (id <= 0) {
            MessageUtils.alert("请先勾选客户");
            return;
        }

        super.grid_ondblclick();
        String winId = "_applyMonth";
        String url = "./customerapplymonth.xhtml?id=" + id;
        AppUtils.openWindow(winId, url);
    }


    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        Long userid = AppUtils.getUserSession().getUserid();

        String linkSalesSql =
                "\n	OR EXISTS"
                        + "\n				(SELECT "
                        + "\n					1 "
                        + "\n				FROM sys_custlib x , sys_custlib_user y  "
                        + "\n				WHERE y.custlibid = x.id  "
                        + "\n					AND y.userid = " + AppUtils.getUserSession().getUserid()
                        + "\n					AND x.libtype = 'S'  "
                        + "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = a.salesid AND z.isdelete = false) " //关联的业务员的单，都能看到
                        + ")"
                        + "\n	OR EXISTS"
                        + "\n				(SELECT "
                        + "\n					1 "
                        + "\n				FROM sys_custlib x , sys_custlib_role y  "
                        + "\n				WHERE y.custlibid = x.id  "
                        + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + AppUtils.getUserSession().getUserid() + " AND z.roleid = y.roleid)"
                        + "\n					AND x.libtype = 'S'  "
                        + "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = a.salesid) " //组关联业务员的单，都能看到
                        + ")";

        String filterByInputerOrUpdaterConfig = ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2link");
        String filterByInputOrUpdate = "\nOR inputer = '" + AppUtils.getUserSession().getUsercode() + "'" + "\nOR updater = '" + AppUtils.getUserSession().getUsercode() + "'";

        map.put("filter", " AND (salesid = " + userid
                + ("Y".equals(filterByInputerOrUpdaterConfig) ? filterByInputOrUpdate : "")
                + "\n" + ("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2assign")) ? "OR EXISTS(SELECT 1 FROM _sys_user_assign y WHERE y.linkid=a.id AND linktype='C' AND y.userid=" + userid + ")" : "")
                + "\n" + ("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2saleslink")) ? linkSalesSql : "")

                + "\nOR EXISTS(SELECT 1 FROM _sys_user_assign y WHERE y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=" + userid + "))");
        
        String salesnamec = "";
		StringBuilder buffer = new StringBuilder();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";

				if(!StrUtils.isNull(val.toString()) && "salesnamec".equals(key)){
					salesnamec = "\n AND salesid = ANY(SELECT id FROM sys_user WHERE isdelete = FALSE AND (namec ILIKE '%"+val+"%' OR namee ILIKE '%"+val+"%' OR code ILIKE '%"+val+"%')) ";
		        }else if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='" + dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index) + "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							int indexNot = val.toString().indexOf("!");
							if (indexNot > 0) {
								val = val.toString().substring(0, indexNot);
								buffer.append("\nAND " + key + " NOT ILIKE '%'||" +"TRIM('"+ val+"')" + "||'%'");
							}else{
								buffer.append("\nAND " + key + " ILIKE '%'||" +"TRIM('"+ val+"')" + "||'%'");
							}
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		qry += salesnamec;
		map.put("qry", qry);

        return map;
    }

    @Bind
    public UIWindow searchWindow;

    @Action
    public void search() {
        this.searchWindow.show();
    }

    @Bind
    @SaveState
    public String qrycode;
    @Bind
    @SaveState
    public String datemonth;

    @Action
    public void qryinfo() {
        try {
            String sql = "select count(1) AS count from sys_corporation a where 1=1 AND iscustomer = TRUE AND isofficial = TRUE AND a.isdelete = false ";
            if (!StrUtils.isNull(qrycode)) {
                qrycode = qrycode.trim();
                sql += "AND (namee ILIKE '%" + StrUtils.getSqlFormat(qrycode) + "%' OR namec ILIKE '%" + StrUtils.getSqlFormat(qrycode) + "%' " +
                        "OR code ILIKE '%" + StrUtils.getSqlFormat(qrycode) + "%' OR abbr ILIKE '%" + StrUtils.getSqlFormat(qrycode) + "%')";
            }

            if (!StrUtils.isNull(datemonth)) {
                datemonth = datemonth.trim();
                sql += ("\nAND COALESCE((select MAX(b.jobdate) from fina_jobs b where a.id = b.customerid AND b.isdelete = false),'1999-01-01') <= (now()- INTERVAL '" + datemonth + " month" +
                        "')::date");
            }

            Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
            Long i = (Long) s.get("count");
            if (i > 0) {
                MessageUtils.alert("该客户系统中已存在， 如有需要请联系客商管理组核实细节!");
                return;
            } else {
                MessageUtils.alert("该客户系统中不存在!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


