package com.scp.view.module.customer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.customer.customerarapBean", scope = ManagedBeanScope.REQUEST)
public class CustomerarapBean extends GridView {

    @Bind
    @SaveState
    private String customerid;

    @Bind
    @SaveState
    private String dateastart;
    @Bind
    @SaveState
    private String dateend;

    @Override
    public void beforeRender(boolean isPostBack) {
        super.beforeRender(isPostBack);
        if (!isPostBack) {
            customerid = AppUtils.getReqParam("customerid");

            dateastart = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00";

            Calendar calendar = Calendar.getInstance();
            Date date = new Date();//取时间
            calendar.setTime(date); //需要将date数据转移到Calender对象中操作
            calendar.add(calendar.MONTH, 1);//把日期往后增加n天.正数往后推,负数往前移动
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            dateend = new SimpleDateFormat("yyyy-MM-dd").format(date) + " 23:59";

        }
    }


    @SaveState
    @Accessible
    public Map<String, Object> qryMapShip = new HashMap<String, Object>();


    @Bind
    public UIDataGrid arapgrid;

    @Bind(id = "arapgrid", attribute = "dataProvider")
    protected GridDataProvider getArapgridDataProvider() {
        return new GridDataProvider() {
            @Override
            public Object[] getElements() {
                String sqlId = "pages.module.customer.customerarapBean.arapgrid.page";
                return serviceContext.daoIbatisTemplate
                        .getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereArapgrid(qryMapShip), start, limit)
                        .toArray();
            }

            @Override
            public int getTotalCount() {
                String sqlId = "pages.module.customer.customerarapBean.arapgrid.count";
                List<Map> list = serviceContext.daoIbatisTemplate
                        .getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereArapgrid(qryMapShip));
                Long count = 10000L;
                return count.intValue();
            }
        };
    }

    private Object getQryClauseWhereArapgrid(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        map.put("qry0", "and customerid=" + customerid);
        return map;
    }

    @Bind
    public UIDataGrid customearapgrid;
    @Bind(id = "customearapgrid", attribute = "dataProvider")
    protected GridDataProvider getCustomearapgridDataProvider() {
        return new GridDataProvider() {
            @Override
            public Object[] getElements() {
                String sqlId = "pages.module.customer.customerarapBean.customearapgrid.page";
                return serviceContext.daoIbatisTemplate
                        .getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereCustomearapgrid(qryMapShip), start, limit)
                        .toArray();
            }

            @Override
            public int getTotalCount() {
                String sqlId = "pages.module.customer.customerarapBean.customearapgrid.count";
                List<Map> list = serviceContext.daoIbatisTemplate
                        .getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereCustomearapgrid(qryMapShip));
                Long count = 10000L;
                return count.intValue();
            }
        };
    }
    private Object getQryClauseWhereCustomearapgrid(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        map.put("qry0", "and customerid=" + customerid);
        return map;
    }

    @Bind
    public UIDataGrid montharapgrid;
    @Bind(id = "montharapgrid", attribute = "dataProvider")
    protected GridDataProvider getMontharapgridDataProvider() {
        return new GridDataProvider() {
            @Override
            public Object[] getElements() {
                String sqlId = "pages.module.customer.customerarapBean.montharapgrid.page";
                return serviceContext.daoIbatisTemplate
                        .getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereMontharapgrid(qryMapShip), start, limit)
                        .toArray();
            }

            @Override
            public int getTotalCount() {
                String sqlId = "pages.module.customer.customerarapBean.montharapgrid.count";
                List<Map> list = serviceContext.daoIbatisTemplate
                        .getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereMontharapgrid(qryMapShip));
                Long count = 10000L;
                return count.intValue();
            }
        };
    }
    private Object getQryClauseWhereMontharapgrid(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        map.put("qry0", "and customerid=" + customerid);
        return map;
    }


}
