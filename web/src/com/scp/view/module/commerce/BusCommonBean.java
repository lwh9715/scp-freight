package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.commerce.BusCommerceArApResponse;
import com.scp.util.AppUtils;
import com.scp.util.FileOperationUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.operamasks.org.apache.commons.fileupload.FileItemIterator;
import org.operamasks.org.apache.commons.fileupload.FileItemStream;
import org.operamasks.org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.operamasks.org.apache.commons.fileupload.util.Streams;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author CIMC
 */

@WebServlet("/api")
@ManagedBean(name = "pages.module.commerce.busCommonBean")
public class BusCommonBean extends BaseServlet {

    /**
     * 公共查询接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "publicNos")
    public BaseView queryCommonNos(HttpServletRequest request) {
        BaseView view = new BaseView();

        try {

            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            //获取单号
            String nos = object.get("nos").toString();
            String type = object.get("type").toString();

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            if (type.equals("JOB") || type.equals("")) {
                //海运/空运/电商/报关/陆运/铁运
                StringBuffer sql = queryJobNos(nos);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("result", StrUtils.getMapVal(result, "json"));
            } else if (type.equals("RQ")) {
                //付款单号
                StringBuffer sql = queryRQ(nos);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("result", StrUtils.getMapVal(result, "json"));
            } else if (type.equals("RP")) {
                //收付款单号
                StringBuffer sql = queryRP(nos);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("result", StrUtils.getMapVal(result, "json"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    /**
     * 获取工作单
     */
    private StringBuffer queryJobNos(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id,nos,jobtype FROM fina_jobs WHERE isdelete = FALSE AND nos = '" + keyword + "' LIMIT 1) T;");
        return sql;
    }

    /**
     * 获取付款申请
     */
    private StringBuffer queryRQ(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id,nos FROM fina_rpreq WHERE isdelete = FALSE AND nos = '" + keyword + "' LIMIT 1) T;");
        return sql;
    }

    /**
     * 获取收付款
     */
    private StringBuffer queryRP(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id,actpayrecno FROM fina_actpayrec where isdelete = FALSE AND actpayrecno = '" + keyword + "' LIMIT 1) T;");
        return sql;
    }

    /**
     * 获取下拉框数据接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "common")
    public BaseView queryCommon(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject object = getJsonObject(request);

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            Long id = 0L;
            String code = "";
            String type = "";
            String keyword = "";
            StringBuffer sql = null;

            if (object.containsKey("query")) {
                keyword = object.get("query").toString();
            }

            if (object.containsKey("code")) {
                if (!StrUtils.isNull(object.get("code").toString())) {
                    code = object.get("code").toString();
                }
            }

            if (object.containsKey("jobtype")) {
                if (!StrUtils.isNull(object.get("jobtype").toString())) {
                    type = object.get("jobtype").toString();
                }
            }

            if (object.containsKey("id")) {
                if (!StrUtils.isNull(object.get("id").toString())) {
                    id = Long.parseLong(object.get("id").toString());
                }
            }

            if (object.get("methodFlag").toString().equals("sale")) {

                sql = querySale(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("sale", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("saleop")) {

                sql = querySaleOp(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("sale", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("goodstrack")) {

                sql = queryGoodsTrack(keyword, session.get("userid").toString());
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("goodstrack", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("report")) {

                sql = queryJasperReport(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("report", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("custype")) {

                sql = queryCustomCustype(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("custype", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("cusStatus")) {

                sql = queryCustomStatus(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("cusStatus", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("cusclass")) {

                sql = queryCustomCusclass(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("cusclass", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("cusConsignor")) {

                sql = queryCusConsignor(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("cusConsignor", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("port")) {

                sql = queryPort(type, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("port", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("address")) {

                sql = queryAddress(code, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("address", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("product")) {

                sql = queryProduct(id, keyword, session.get("corpid").toString());
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("product", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("consignor")) {

                sql = queryConsignor(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("consignor", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("approver")) {

                sql = queryApprover(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("approver", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("bookingagent")) {

                sql = queryServiceAgent(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("bookingagent", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("deliveryagent")) {

                sql = queryServiceAgent(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("deliveryagent", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("clearanceagent")) {

                sql = queryServiceAgent(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("clearanceagent", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("warehouse")) {

                sql = queryWarehouse(id, keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("warehouse", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("contact")) {

                sql = queryContact(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
                view.add("contact", StrUtils.getMapVal(result, "json"));

            } else if (object.get("methodFlag").toString().equals("common")) {

                StringBuffer company = queryCompany(keyword);
                Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(company.toString());
                view.add("company", StrUtils.getMapVal(result, "json"));

                StringBuffer service = queryService(keyword);
                Map<String, String> result1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(service.toString());
                view.add("service", StrUtils.getMapVal(result1, "json"));

                StringBuffer cntype = queryCntype(keyword);
                Map<String, String> result2 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(cntype.toString());
                view.add("cntype", StrUtils.getMapVal(result2, "json"));

                StringBuffer delivery = queryDelivery(keyword);
                Map<String, String> result3 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(delivery.toString());
                view.add("delivery", StrUtils.getMapVal(result3, "json"));

                StringBuffer serverType = queryServerType(keyword);
                Map<String, String> result4 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(serverType.toString());
                view.add("serverType", StrUtils.getMapVal(result4, "json"));

//                StringBuffer deliveryStatus = queryDeliveryStatus(keyword);
//                Map<String, String> result5 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deliveryStatus.toString());
//                view.add("deliveryStatus", StrUtils.getMapVal(result5, "json"));

                StringBuffer goodtypelist = queryGoodtype(keyword);
                Map<String, String> result6 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(goodtypelist.toString());
                view.add("goodtype", StrUtils.getMapVal(result6, "json"));

                StringBuffer packagelist = queryPackage(keyword);
                Map<String, String> result7 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(packagelist.toString());
                view.add("package", StrUtils.getMapVal(result7, "json"));

                StringBuffer serviceterm = queryServiceterm(keyword);
                Map<String, String> result8 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(serviceterm.toString());
                view.add("serviceterm", StrUtils.getMapVal(result8, "json"));

                StringBuffer deptop = queryDeptop(keyword, session.get("corpid").toString());
                Map<String, String> result9 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptop.toString());
                view.add("deptop", StrUtils.getMapVal(result9, "json"));
            }
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 获取业务员sql
     */
    private StringBuffer querySale(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.corpid,sc.deptid,(select abbr from sys_corporation s where s.id = sc.corpid limit 1) as corper," +
                "(select name from sys_department sd where sd.id = sc.deptid limit 1) as depter FROM sys_user sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND (sc.id = " + id + ")");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND sc.issales = true AND sc.isdelete = false ORDER BY sc.code ASC LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取途曦业务员sql
     */
    private StringBuffer querySaleOp(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.corpid,sc.deptid,(select abbr from sys_corporation s where s.id = sc.corpid limit 1) as corper," +
                "(select name from sys_department sd where sd.id = sc.deptid limit 1) as depter FROM sys_user sc WHERE corpid = 11540072274");
        if (id != 0) {
            sql.append("\n AND (sc.id = " + id + ")");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND sc.issales = true AND sc.isdelete = false ORDER BY sc.code ASC LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取委托人sql
     */
    private StringBuffer queryConsignor(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.namee,sc.salesid,sc.abbr,sc.daysar," +
                "(select merchantcode from sys_corpext where customerid = sc.id limit 1) as merchantcode FROM sys_corporation sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND sc.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%') OR (sc.code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND sc.isdelete = false ORDER BY sc.code LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取分公司sql
     */
    private StringBuffer queryCompany(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id, sc.code, sc.namec, sc.abbr FROM sys_corporation sc WHERE sc.iscustomer = FALSE ORDER BY sc.code) T;");
        return sql;
    }

    /**
     * 获取分公司部门sql
     */
    private StringBuffer queryDeptop(String keyword, String corpid) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id, sc.code, sc.name FROM sys_department sc WHERE sc.isdelete = false ORDER BY sc.code) T;");
        return sql;
    }

    /**
     * 获取服务类型sql
     */
    private StringBuffer queryService(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE t.fkcode = 250 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取派送方式sql
     */
    private StringBuffer queryDelivery(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 1250 and t.code <> '' LIMIT 10) T;");
        return sql;
    }


    /**
     * 获取报关状态sql
     */
    private StringBuffer queryCustomStatus(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 100 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取报关公司sql
     */
    private StringBuffer queryCusConsignor(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.namee,sc.salesid,sc.abbr FROM sys_corporation sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND sc.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (sc.namec ILIKE '%" + keyword + "%' OR  sc.namee ILIKE '%" + keyword + "%')");
        }
        sql.append("\n AND sc.iscustom = true AND sc.isdelete = false ORDER BY sc.code DESC LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取报关方式sql
     */
    private StringBuffer queryCustomCusclass(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 40 and t.namec <> '报关方式' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取报关类型sql
     */
    private StringBuffer queryCustomCustype(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 45 and t.namec <> '报关类型' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取轨迹模板sql
     */
    private StringBuffer queryGoodsTrack(String keyword, String userid) {
        String deptsql = "SELECT deptid FROM sys_user where id = " + userid + " limit 1";
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
        String nodeSql = "";

        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
            nodeSql += " AND node = 'BM'";
        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
            nodeSql += " AND node = 'IN'";
        }

        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.* FROM dat_goodstrack t WHERE isdelete = false " + nodeSql + " ORDER BY id) T;");
        return sql;
    }

    /**
     * 货物类型
     */
    private StringBuffer queryGoodtype(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT code,namec FROM dat_filedata WHERE isleaf = true and fkcode = 1260 and code <> '') T;");
        return sql;
    }

    /**
     * 服务条款
     */
    private StringBuffer queryServiceterm(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE isleaf = true and fkcode = 1270 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 地址簿
     */
    private StringBuffer queryAddress(String code, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id,code,attn,companyname,postcode,countrycode,province,city,addressone,tel FROM dat_address WHERE 1 = 1");
        if (!StrUtils.isNull(code)) {
            sql.append("\n AND code = '" + code + "'");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (companyname ILIKE '%" + keyword + "%' OR  code ILIKE '%" + keyword + "%')");
        }
        sql.append("\n AND isdelete = false ORDER BY code DESC LIMIT 50) T;");
        return sql;
    }

    /**
     * 产品名称
     */
    private StringBuffer queryProduct(Long id, String keyword, String corpid) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id, code, namec, namee FROM dat_product WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND id = " + id + "");
        }

        if (!StrUtils.isNull(corpid) && (corpid.equals("1122274") || corpid.equals("11540072274") || corpid.equals("70033332274"))) {
            sql.append("\n AND corpid = '" + corpid + "'");
        }

        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (code ILIKE '%" + keyword + "%' OR  namec ILIKE '%" + keyword + "%')");
        }
        sql.append("\n AND isdelete = false ORDER BY code DESC LIMIT 20) T;");
        return sql;
    }


    /**
     * 获取派送状态sql
     */
    private StringBuffer queryDeliveryStatus(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT code,namec FROM dat_filedata WHERE fkcode = 1310 AND code <> '' ORDER BY code LIMIT 20) T;");
        return sql;
    }

    /**
     * 获取服务类型sql
     */
    private StringBuffer queryServerType(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.code,t.namec FROM dat_filedata t WHERE fkcode = 1330 and t.code <> '' LIMIT 10) T;");
        return sql;
    }

    /**
     * 获取港口sql
     */
    private StringBuffer queryPort(String type, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.id,t.code,t.namec,t.namee FROM dat_port t WHERE 1 = 1");
        if (!StrUtils.isNull(type) && (type.equals("H") || type.equals("X") || type.equals("T") || type.equals("Z") || type.equals("HT"))) {
            sql.append("\n AND t.isair = false");
        }
        if (!StrUtils.isNull(type) && type.equals("K")) {
            sql.append("\n AND t.isair = true");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND ((namec ILIKE '%" + keyword + "%') OR (namee ILIKE '%" + keyword + "%') OR (code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND isdelete = FALSE ORDER BY t.code LIMIT 10) T;");
        return sql;
    }

    /**
     * 柜型
     */
    private StringBuffer queryCntype(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT id, code  FROM dat_cntype WHERE isdelete = FALSE  ORDER BY code) T;");
        return sql;
    }

    /**
     * 包装
     */
    private StringBuffer queryPackage(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.namee,t.namee FROM dat_package t WHERE isdelete = FALSE ORDER BY namee) T;");
        return sql;
    }

    /**
     * 审核人
     */
    private StringBuffer queryApprover(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT ");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT t.id,t.code,t.namec,t.corper FROM sys_user t WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND t.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n AND (( t.namec ILIKE '%" + keyword + "%') OR (t.namee ILIKE '%" + keyword + "%') OR (t.code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n AND isdelete = FALSE and isadmin = 'N' ORDER BY t.code limit 10) T;");
        return sql;
    }

    /**
     * 订舱代理，派送代理，清关代理
     */
    private StringBuffer queryServiceAgent(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.code,sc.namec,sc.namee,sc.salesid,sc.abbr FROM sys_corporation sc WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND sc.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n and (( sc.namec ILIKE '%" + keyword + "%') OR (sc.namee ILIKE '%" + keyword + "%') OR (sc.code ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n and isdelete = FALSE ORDER BY sc.code limit 10) T;");
        return sql;
    }

    /**
     * 仓库档案
     */
    private StringBuffer queryWarehouse(Long id, String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT dw.id, dw.code, dw.namec, dw.namee, dw.addressc FROM dat_warehouse dw WHERE 1 = 1");
        if (id != 0) {
            sql.append("\n AND dw.id = " + id + "");
        }
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n and (( dw.namec LIKE '%" + keyword + "%') OR (dw.namee iLIKE '%" + keyword + "%'))");
        }
        sql.append("\n and isdelete = FALSE ORDER BY id limit 10) T;");
        return sql;
    }

    /**
     * 报表/面单列表
     */
    private StringBuffer queryJasperReport(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (select code,namec,modcode from sys_report WHERE modcode = 'commerce'");
        sql.append("\n and isdelete = FALSE ORDER BY code limit 10) T;");
        return sql;
    }

    /**
     * 仓库收发通信息
     */
    private StringBuffer queryContact(String keyword) {
        StringBuffer sql = new StringBuffer();
        sql.append("\n SELECT");
        sql.append("\n array_to_json(ARRAY_AGG(row_to_json(T))) AS json");
        sql.append("\n FROM");
        sql.append("\n (SELECT sc.id,sc.customerabbr,sc.name,sc.contactxt,sc.inputer,sc.updater FROM sys_corpcontacts sc where 1 = 1");
        if (!StrUtils.isNull(keyword)) {
            sql.append("\n and (( sc.customerabbr ILIKE '%" + keyword + "%') OR (sc.name ILIKE '%" + keyword + "%'))");
        }
        sql.append("\n and sc.isdelete = FALSE limit 10) T;");
        return sql;
    }


    /**
     * 解析前端传获取值
     *
     * @param request
     * @return
     * @throws IOException
     */
    private JSONObject getJsonObject(HttpServletRequest request) throws IOException {
        String json;
        InputStream is = request.getInputStream();
        json = IOUtils.toString(is, "UTF-8");
        json = StrUtils.getSqlFormat(json);
        return JSONObject.parseObject(json);
    }

    /**
     * 跳转工作单
     *
     * @param request
     * @return
     */
    @Action(method = "skipJob")
    public BaseView skipJob(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject object = getJsonObject(request);
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            String querySql = "base.commerce.querynos";
            String filter = "\nAND ( x.saleid = " + session.get("userid").toString()
                    + "\n	OR (x.inputer ='" + session.get("usercode").toString() + "')"
                    + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND x.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT x.corpidop UNION SELECT x.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = x.id AND c.isdelete =FALSE)))"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib sc , sys_custlib_user y  "
                    + "\n				WHERE y.custlibid = sc.id  "
                    + "\n					AND y.userid = " + session.get("userid").toString()
                    + "\n					AND sc.libtype = 'S'  "
                    + "\n					AND sc.userid = x.saleid " //关联的业务员的单，都能看到
                    + ")"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib sc , sys_custlib_role y  "
                    + "\n				WHERE y.custlibid = sc.id  "
                    + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n					AND sc.libtype = 'S'  "
                    + "\n					AND sc.userid = x.saleid " //组关联业务员的单，都能看到
                    + ")"
                    //过滤工作单指派
                    + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign sua, bus_commerce y WHERE sua.linkid = y.id AND sua.isdelete = FALSE " +
                    "AND y.jobid = x.id AND sua.linktype = 'J' AND sua.userid =" + session.get("userid").toString() + ")"
                    + "\n)";


            Map args = new HashMap();
            args.put("filter", filter);
            args.put("nos", " AND ( x.nos ilike '%" + object.get("nos").toString().toUpperCase().trim() + "%' OR y.ponum ilike '%" + object.get("nos").toString().trim() + "%' )");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("nos", StrUtils.getMapVal(list.get(0), "nos"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 跳转工作单费用页面
     *
     * @param request
     * @return
     */
    @Action(method = "skipJobArAp")
    public BaseView skipJobArAp(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject object = getJsonObject(request);
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

            //工作单过滤
            //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
            String filter = "\nAND ( fj.saleid = " + session.get("userid").toString()
                    + "\n	OR (fj.inputer ='" + session.get("usercode").toString() + "')" //录入人有权限
                    + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + session.get("userid").toString() + ")) " +
                    "AND fj.corpid <> " + session.get("corpidCurrent").toString() + " AND " + session.get("corpidCurrent").toString()
                    + " = ANY(SELECT fj.corpidop UNION SELECT fj.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = fj.id AND c.isdelete =FALSE)))"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib sc , sys_custlib_user y  "
                    + "\n				WHERE y.custlibid = sc.id  "
                    + "\n					AND y.userid = " + session.get("userid").toString()
                    + "\n					AND sc.libtype = 'S'  "
                    + "\n					AND sc.userid = fj.saleid " //关联的业务员的单，都能看到
                    + ")"
                    + "\n	OR EXISTS"
                    + "\n				(SELECT "
                    + "\n					1 "
                    + "\n				FROM sys_custlib sc , sys_custlib_role y  "
                    + "\n				WHERE y.custlibid = sc.id  "
                    + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + session.get("userid").toString() + " AND z.roleid = y.roleid)"
                    + "\n					AND sc.libtype = 'S'  "
                    + "\n					AND sc.userid = fj.saleid " //组关联业务员的单，都能看到
                    + ")"
                    //过滤工作单指派
                    + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign sua, bus_commerce y WHERE sua.linkid = y.id AND sua.isdelete = FALSE " +
                    "AND y.jobid = fj.id AND sua.linktype = 'J' AND sua.userid =" + session.get("userid").toString() + ")"
                    + "\n)";

            String querySql = "base.commerce.querynosarap";
            Map args = new HashMap();
            args.put("filter", filter);
            args.put("nos", " AND nos = '" + object.get("nos").toString().toUpperCase().trim() + "'");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("nosarap", StrUtils.getMapVal(list.get(0), "nosarap"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Action(method = "getBusnodesclist")
    public BaseView BusCommerceGetBusnodesclist(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql = "pages.module.commerce.busCommonBean.grid.page";
            String querySql1 = "pages.module.commerce.busCommonBean.grid.count";

            Map args = new HashMap();
            String qry = "\n1=1 AND r.corpid =" + jsonObject.get("corpid").toString();
            args.put("qry", qry);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> list1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql1, args);
            view.add("list", JSON.toJSONString(list));
            view.add("total", StrUtils.getMapVal(list1.get(0), "counts"));
            view.setData(String.valueOf(view));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    //获取工作单编号
    @Action(method = "getnos")
    public BaseView getnos(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String code = jsonObject.get("code").toString();
            String corpid = jsonObject.get("corpid").toString();
            String querySql = "select f_auto_busno('code=" + code + ";date='||to_char(NOW(),'yyyy-MM-dd')||';corpid='||" + corpid + "||';') as nos;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map<String, String> result1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.add("nos", StrUtils.getMapVal(result1, "nos"));
            view.setData(String.valueOf(view));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    //获取唛头单编号
    @Action(method = "getsono")
    public BaseView getSono(HttpServletRequest request) {

        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String code = jsonObject.get("code").toString();
            String querySql = "select f_auto_busno('code=" + code + ";date='||to_char(NOW(),'yyyy-MM-dd')||';corpid=1122274;') as sono;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map<String, String> result1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.add("sono", StrUtils.getMapVal(result1, "sono"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 导入zip
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "uploadzip")
    public BaseView ImportTemplate(HttpServletRequest request) throws IOException, FileUploadException {

        BaseView view = new BaseView();

        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        view.setSuccess(true);
        File file2local = null;
        InputStream stream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletFileUpload upload = new ServletFileUpload();

        try {
            if (ServletFileUpload.isMultipartContent(request)) {
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    stream = item.openStream();
                    if (!item.isFormField()) {
                        // 上传文件
                        File file = new File(item.getName());
                        String orFileName = file.getName();
                        String originalFileName = orFileName.toLowerCase();
                        // 文件格式
                        String suffix = "";
                        if (originalFileName != null && !originalFileName.trim().equals("")) {
                            String[] strArr = originalFileName.split("\\.");
                            if (strArr != null && strArr.length == 2) {
                                suffix = strArr[1];
                            }
                        }

                        String serverName = request.getContextPath().replace("/", "");
                        String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                        FileOperationUtil.newFolder(path);

                        path += File.separator + serverName;
                        FileOperationUtil.newFolder(path);

                        path += File.separator + "attachfile" + File.separator;
                        FileOperationUtil.newFolder(path);

                        long fileSize = 0l;

                        file2local = new File(path + originalFileName);
                        bis = new BufferedInputStream(stream);
                        bos = new BufferedOutputStream(new FileOutputStream(file2local));
                        fileSize = Streams.copy(bis, bos, true);


                        //获取文件输入流
                        ZipFile zipFile = new ZipFile(path + originalFileName);
                        FileInputStream input = new FileInputStream(path + originalFileName);
                        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));

                        ZipEntry zipEntry = null;
                        byte[] bytes = new byte[1024];
                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

                        //循环遍历
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                            //保存到本地
                            String fileName = zipEntry.getName();

                            if (fileName.contains("png") || fileName.contains("jpg") || fileName.contains("pdf")) {

                                //获取的文件，如果存在父级，文件名会显示父级路径，所有我们需要手动去掉
                                if (fileName.lastIndexOf("/") != -1) {
                                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                                }
                                //获取唯一ID
                                String attachidsql = "select getid() as attachid";
                                Map attachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachidsql);
                                String attachid = StrUtils.getMapVal(attachidmap, "attachid");

                                File shpFile = new File(path + File.separator + attachid + fileName);
                                shpFile.createNewFile();

                                InputStream inputStream = zipFile.getInputStream(zipEntry);
                                copyFile(inputStream, new FileOutputStream(shpFile));

                                inputStream.close();

                                String[] splitFile = fileName.split("\\.");
                                String querySql1 = "SELECT id FROM fina_jobs WHERE nos = '" + splitFile[0] + "' AND isdelete = false LIMIT 1;";
                                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);

                                String querySql2 = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer, inputtime, roleid)\n" +
                                        "VALUES (" + attachid + ", " + map.get("id") + ",'" + fileName + "','" + "image/png" + "', '" + zipEntry.getSize() + "'," +
                                        " '" + session.get("usercode").toString() + "', NOW(), 1118042532274);";
                                daoIbatisTemplate.updateWithUserDefineSql(querySql2);

                            } else {
                                view.setSuccess(false);
                                break;
                            }
                        }
                        //一定记得关闭流
                        input.close();
                        zipInputStream.closeEntry();
                        zipInputStream.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
            bis.close();
            bos.close();
        }
        return view;
    }

    /**
     * 导入zip
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "uploadsozip")
    public BaseView ImportTemplateSo(HttpServletRequest request) throws IOException, FileUploadException {

        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        view.setSuccess(true);
        File file2local = null;
        InputStream stream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletFileUpload upload = new ServletFileUpload();

        try {
            if (ServletFileUpload.isMultipartContent(request)) {
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    stream = item.openStream();
                    if (!item.isFormField()) {
                        // 上传文件
                        File file = new File(item.getName());
                        String orFileName = file.getName();
                        String originalFileName = orFileName.toLowerCase();
                        // 文件格式
                        String suffix = "";
                        if (originalFileName != null && !originalFileName.trim().equals("")) {
                            String[] strArr = originalFileName.split("\\.");
                            if (strArr != null && strArr.length == 2) {
                                suffix = strArr[1];
                            }
                        }

                        String serverName = request.getContextPath().replace("/", "");
                        String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                        FileOperationUtil.newFolder(path);

                        path += File.separator + serverName;
                        FileOperationUtil.newFolder(path);

                        path += File.separator + "attachfile" + File.separator;
                        FileOperationUtil.newFolder(path);

                        long fileSize = 0l;

                        file2local = new File(path + originalFileName);
                        bis = new BufferedInputStream(stream);
                        bos = new BufferedOutputStream(new FileOutputStream(file2local));
                        fileSize = Streams.copy(bis, bos, true);


                        //获取文件输入流
                        ZipFile zipFile = new ZipFile(path + originalFileName);
                        FileInputStream input = new FileInputStream(path + originalFileName);
                        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));

                        ZipEntry zipEntry = null;
                        byte[] bytes = new byte[1024];
                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

                        //循环遍历
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                            //保存到本地
                            String fileName = zipEntry.getName();

                            if (fileName.contains("png") || fileName.contains("jpg") || fileName.contains("pdf")) {

                                //获取的文件，如果存在父级，文件名会显示父级路径，所有我们需要手动去掉
                                if (fileName.lastIndexOf("/") != -1) {
                                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                                }
                                //获取唯一ID
                                String attachidsql = "select getid() as attachid";
                                Map attachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachidsql);
                                String attachid = StrUtils.getMapVal(attachidmap, "attachid");

                                File shpFile = new File(path + File.separator + attachid + fileName);
                                shpFile.createNewFile();

                                InputStream inputStream = zipFile.getInputStream(zipEntry);
                                copyFile(inputStream, new FileOutputStream(shpFile));
                                inputStream.close();

                                String[] splitFile = fileName.split("\\.");
                                String querySql1 = "SELECT jobid FROM bus_shipping WHERE sono = '" + splitFile[0] + "' AND isdelete = false LIMIT 1;";
                                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);

                                String querySql2 = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer, inputtime, roleid)\n" +
                                        "VALUES (" + attachid + ", " + map.get("jobid") + ",'" + fileName + "','" + "application/pdf" + "', '" + zipEntry.getSize() + "'," +
                                        " '" + session.get("usercode").toString() + "', NOW(), 6594862199);";
                                daoIbatisTemplate.updateWithUserDefineSql(querySql2);

                            } else {
                                view.setSuccess(false);
                                break;
                            }
                        }
                        //一定记得关闭流
                        input.close();
                        zipInputStream.closeEntry();
                        zipInputStream.close();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
            bis.close();
            bos.close();
        }
        return view;
    }

    /**
     * 导入zip 提单确认件
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "uploadbookingzip")
    public BaseView ImportTemplateBooking(HttpServletRequest request) throws IOException, FileUploadException {

        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        view.setSuccess(true);
        File file2local = null;
        InputStream stream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ServletFileUpload upload = new ServletFileUpload();

        try {
            if (ServletFileUpload.isMultipartContent(request)) {
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    stream = item.openStream();
                    if (!item.isFormField()) {
                        // 上传文件
                        File file = new File(item.getName());
                        String orFileName = file.getName();
                        String originalFileName = orFileName.toLowerCase();
                        // 文件格式
                        String suffix = "";
                        if (originalFileName != null && !originalFileName.trim().equals("")) {
                            String[] strArr = originalFileName.split("\\.");
                            if (strArr != null && strArr.length == 2) {
                                suffix = strArr[1];
                            }
                        }

                        String serverName = request.getContextPath().replace("/", "");
                        String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                        FileOperationUtil.newFolder(path);

                        path += File.separator + serverName;
                        FileOperationUtil.newFolder(path);

                        path += File.separator + "attachfile" + File.separator;
                        FileOperationUtil.newFolder(path);

                        long fileSize = 0l;

                        file2local = new File(path + originalFileName);
                        bis = new BufferedInputStream(stream);
                        bos = new BufferedOutputStream(new FileOutputStream(file2local));
                        fileSize = Streams.copy(bis, bos, true);


                        //获取文件输入流
                        ZipFile zipFile = new ZipFile(path + originalFileName);
                        FileInputStream input = new FileInputStream(path + originalFileName);
                        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));

                        ZipEntry zipEntry = null;
                        byte[] bytes = new byte[1024];
                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

                        //循环遍历
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                            //保存到本地
                            String fileName = zipEntry.getName();

                            if (fileName.contains("pdf")) {

                                //获取的文件，如果存在父级，文件名会显示父级路径，所有我们需要手动去掉
                                if (fileName.lastIndexOf("/") != -1) {
                                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                                }
                                //获取唯一ID
                                String attachidsql = "select getid() as attachid";
                                Map attachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachidsql);
                                String attachid = StrUtils.getMapVal(attachidmap, "attachid");

                                File shpFile = new File(path + File.separator + attachid + fileName);
                                shpFile.createNewFile();

                                InputStream inputStream = zipFile.getInputStream(zipEntry);
                                copyFile(inputStream, new FileOutputStream(shpFile));
                                inputStream.close();

                                String[] splitFile = fileName.split("\\.");
                                String querySql1 = "SELECT id FROM fina_jobs WHERE nos = '" + splitFile[0] + "' AND isdelete = false LIMIT 1;";
                                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql1);

                                String querySql2 = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer, inputtime, roleid)\n" +
                                        "VALUES (" + attachid + ", " + map.get("id") + ",'" + fileName + "','" + "application/pdf" + "', '" + zipEntry.getSize() + "'," +
                                        " '" + session.get("usercode").toString() + "', NOW(), 6594972199);";
                                daoIbatisTemplate.updateWithUserDefineSql(querySql2);

                            } else {
                                view.setSuccess(false);
                                break;
                            }
                        }
                        //一定记得关闭流
                        input.close();
                        zipInputStream.closeEntry();
                        zipInputStream.close();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
            bis.close();
            bos.close();
        }
        return view;
    }


    /**
     * 导入费用账单 uploadTocBill
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "uploadTocBill")
    public BaseView uploadTocBill(HttpServletRequest request) throws IOException, FileUploadException {

        BaseView view = new BaseView();

        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        request.setCharacterEncoding("utf-8");
        InputStream stream = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
        List<FileItem> fileItems = upload.parseRequest(request);
        List<FileItem> needFileItem = new LinkedList<FileItem>();

        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                needFileItem.add(fileItem);
            }
        }

        try {
            String querySql = "";
            stream = needFileItem.get(0).getInputStream();
            Workbook workbook = new XSSFWorkbook(stream);
            if (workbook != null) {
                int sheetCount = workbook.getNumberOfSheets();
                if (sheetCount > 0) {
                    List<BusCommerceArApResponse> list = new ArrayList();
                    BusCommerceArApResponse busCommerceArApResponse = new BusCommerceArApResponse();

                    // 途曦 - 文本内容
                    for (int i = 0; i < sheetCount; i++) {
                        if (i == 1) {
                            break;
                        }
                        Sheet sheet = workbook.getSheetAt(i);
                        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            Row row = sheet.getRow(rowNum);
                            if (row == null || row.getFirstCellNum() < 0 || StringUtils.isBlank(row.getCell(0).toString())) {
                                break;
                            }
                            list.add(new BusCommerceArApResponse(row, session.get("usercode").toString()));
                        }
                    }
                    querySql = "SELECT f_import_toc_arap('" + JSONObject.toJSON(list).toString() + "') AS json;";
                }
                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                String ret = StrUtils.getMapVal(map, "json");

                if (ret.contains("导入失败") || ret.contains("Error")) {
                    view.setData(ret);
                    view.setSuccess(false);
                } else {
                    view.setSuccess(true);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        } finally {
            stream.close();
        }
        return view;
    }

    /**
     * 复制文件
     *
     * @param in  原文件流输入流
     * @param out 目标文件输出流
     */
    public static void copyFile(InputStream in, OutputStream out) {
        int len;
        byte[] b = new byte[1024];
        try {
            while ((len = in.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
                out.flush();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取信用额度
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "creditlist")
    public BaseView BusCommerceList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "base.user.creditchild";
            Map args = new HashMap();
            args.put("qry", "\n AND userid = " + json);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("creditlist", StrUtils.getMapVal(list.get(0), "creditlist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 欠款账单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "arapowe")
    public BaseView CustomerarapOwe(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {

            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            String querySql = "base.user.arapowe";
            Map args = new HashMap();
            args.put("qry", "\n AND customerid = " + json);

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("arapowelist", StrUtils.getMapVal(list.get(0), "arapowelist"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
