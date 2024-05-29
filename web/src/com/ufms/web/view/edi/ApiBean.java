package com.ufms.web.view.edi;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.model.SelectItem;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufms.base.db.DaoUtil;
import com.ufms.base.web.GridDataProvider;
import com.ufms.base.web.base.UserSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.scp.base.CommonComBoxBean;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.CommonRuntimeException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.util.ConfigUtils;
import com.scp.util.ExceptionUtil;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.utils.Md5Util;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.BaseView;
import com.ufms.web.view.sysmgr.LogBean;

@WebServlet("/edi/api")
@ManagedBean(name = "pages.module.edi.apiBean")
public class ApiBean extends BaseServlet {


    //http://192.168.0.188:8888/scp/edi/api?method=getJson&apiId=2199&apiKey=1145A30FF80745B56FB0CECF65305017&args=type:shipping;jobno:NBPN20070001;user:adm
    @Action(method = "getJson")
    public BaseView getJson(HttpServletRequest request) {
        BaseView view = new BaseView();

        String apiId = request.getParameter("apiId");//服务号：2199
        String apiKey = request.getParameter("apiKey"); //2199:1145A30FF80745B56FB0CECF65305017

        String tips = checkApi(apiId, apiKey);
        if (!StrUtils.isNull(tips)) {
            view.setMessage(tips);
            return view;
        }

        try {
            String args = request.getParameter("args");
            args = StrUtils.getSqlFormat(args);
            if (StrUtils.isNull(args) || args.indexOf("UPDATE") > 0 || args.indexOf("DELETE") > 0 || args.indexOf("SELECT") > 0 || args.indexOf("EXECUTE") > 0) {
                args = "";
            }
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String querySql = "SELECT f_api_ufms('" + args + "') AS json";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            if (map != null && map.containsKey("json") && map.get("json") != null) {
                view.setSuccess(true);
                view.setData(map.get("json").toString());
            } else {
                view.setMessage("NoData");
            }
        } catch (NoRowException e) {
            view.setMessage("NoRow");
        } catch (MoreThanOneRowException e) {
            view.setMessage("MoreThanOneRow");
        } catch (Exception e) {
            view.setMessage("Error");
            e.printStackTrace();
        }
        return view;
    }

    private String checkApi(String apiId, String apiKey) {
        String tips = "";
        if (StrUtils.isNull(apiId) || StrUtils.isNull(apiKey)) {
            tips = "Error:Illegal access!";
        }
        String md5 = Md5Util.MD5(apiId);
        if (!md5.equalsIgnoreCase(apiKey)) {
            tips = "Error:apiId or apiKey is wrong!";
        }
        return tips;
    }


    //http://192.168.0.188:8888/scp/edi/api?method=syncJobs
    @Action(method = "syncJobs")
    public BaseView syncJobs(HttpServletRequest request, HttpServletResponse response) {
        BaseView view = new BaseView();
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Access-Control-Allow-Methods, Access-Control-Allow-Origin");

            InputStream is = request.getInputStream();
            String json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            //System.out.println("json:......" + json);
            //            JSONObject jsonobject = JSONObject.fromObject(json);//将json格式的字符串转换成JSONObject 对象
            //            System.out.println("jsonobject:......"+jsonobject);
            if (!StrUtils.isNull(json)) {
                String querySql = "SELECT f_api_ufms_jobs_to('" + json + "') AS json;";
                //System.out.println("querySql:......" + querySql);
                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                String ret = StrUtils.getMapVal(map, "json");
                //System.out.println("ret:......" + ret);
                view.setData(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.setSuccess(false);
            view.setData(e.getMessage());
        }
        return view;
    }


    //http://192.168.0.188:8888/scp/edi/api?method=syncCustomer
    @Action(method = "syncCustomer")
    public synchronized BaseView syncCustomer(HttpServletRequest request) {
        BaseView view = new BaseView();
        String json = "";
        String querySql = "";
        try {
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONArray csCustJSONArray = JSONObject.fromObject(json).getJSONObject("body").getJSONArray("csCust");
            if (csCustJSONArray != null && csCustJSONArray.size() > 5) {
                LogBean.insertLastingLog2(new StringBuffer().append("客户同步syncCustomer开始,数据条数过多,超过5条,不进行同步,json为").append(json), "syncCustomer");
                view.setSuccess(false);
                view.setMessage("数据条数过多,超过5条,不进行同步.");
                return view;
            }

            if ("true".equals(ConfigUtils.findSysCfgVal("sys_edi_api_syncCustomer"))) {
                if (!StrUtils.isNull(json)) {
                    querySql = "SELECT f_api_ufms_imp_customer_process('" + json + "') AS json;";
                    LogBean.insertLastingLog2(new StringBuffer().append("客户同步syncCustomer开始,querySql为").append(querySql), "syncCustomer");

                    DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                    Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
                    String ret = StrUtils.getMapVal(map, "json");
                    String returnret = "";
                    if (ret.contains("OK;")) {
                        returnret = ret.substring(0, ret.indexOf("OK;") + 3);
                    }

                    view.setSuccess(true);
                    view.setMessage(returnret);

                    LogBean.insertLastingLog2(new StringBuffer().append("客户同步syncCustomer完成,ret为").append(ret).append(",querySql为").append(querySql), "syncCustomer");
                }
            } else {
                LogBean.insertLastingLog2(new StringBuffer().append("客户同步syncCustomer关闭状态中,json为").append(json), "syncCustomer");
            }
        } catch (Exception e) {
            view.setSuccess(false);
            view.setData(json);
            view.setMessage(e.getMessage());
            LogBean.insertLastingLog2(new StringBuffer().append("客户同步syncCustomer失败,失败原因为").append(e.getMessage()).append((",失败querySql为")).append(querySql), "syncCustomer");
        }
        return view;
    }

    public static void main(String[] args) {
        String md5 = Md5Util.MD5("2274");
        System.out.println(md5);
    }


    /**
     * 通用接口
     * apiId
     * apiKey
     * methodFlag
     * json
     */
    @Action(method = "commonInterface")
    public BaseView commonInterface(HttpServletRequest request, HttpServletResponse response) {
        BaseView view = new BaseView();
        Map parameterMap = new LinkedHashMap(request.getParameterMap());
        String json = "";
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Access-Control-Allow-Methods, Access-Control-Allow-Origin");
            String methodFlag = request.getParameter("methodFlag");//

            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            parameterMap.put("json", json);

            // LogBean.insertLastingLog2(new StringBuffer().append("commonInterface开始,parameterMap为").append(JSONObject.fromObject(parameterMap).toString()), methodFlag);

            //运价查询
            if ("getFclpol".equals(methodFlag)) {
                view = getFclpol(parameterMap);
            }
            if ("getFclpod".equals(methodFlag)) {
                view = getFclpod(parameterMap);
            }
            if ("getCarrier".equals(methodFlag)) {
                view = getCarrier(parameterMap);
            }
            if ("getFreightRate".equals(methodFlag)) {
                view = getFreightRate(parameterMap);
            }

            //联想api
            if ("synLenovoData".equals(methodFlag)) {
                view = synLenovoData(parameterMap);
            }
            if ("returnLenovoData".equals(methodFlag)) {
                view = returnLenovoData(parameterMap);
            }
        } catch (Exception e) {
            view.setSuccess(false);
            view.setMessage(e.getMessage());
            // LogBean.insertLastingLog(new StringBuffer().append(("commonInterface失败,parameterMap为")).append(JSONObject.fromObject(parameterMap).toString()).append((",失败view为")).append(view.getRet()));
        }
        // LogBean.insertLastingLog(new StringBuffer().append("commonInterface结束,view为").append(view.getRet()));
        return view;
    }

    private BaseView getFclpol(Map parameterMap) throws UnsupportedEncodingException {
        BaseView view = new BaseView();

        // String apikey = MD5Encrypt.getInstance().encrypt(String.valueOf(parameterMap.get("apiKey")));
        // if (!"tyj@123456".equals(apikey)) {
        //     view.setSuccess(false);
        //     view.setMessage("apikey不正确");
        //     return view;
        // }

        String pageIndex = String.valueOf(parameterMap.get("pageIndex")) == "null" ? "1" : String.valueOf(parameterMap.get("pageIndex"));
        String pageSize = String.valueOf(parameterMap.get("pageSize")) == "null" ? "10" : String.valueOf(parameterMap.get("pageSize"));
        String reqStr = String.valueOf(parameterMap.get("reqStr")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("reqStr"))[0]);
        reqStr = new String(reqStr.getBytes("iso8859-1"), "utf-8");

        StringBuffer sb = new StringBuffer();
        sb.append("\n SELECT");
        sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
        sb.append("\n FROM");
        sb.append("\n (SELECT");
        sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') AS namec,COALESCE(namee,'') AS namee");
        sb.append("\n FROM");
        sb.append("\n	dat_port p");
        sb.append("\n WHERE");
        sb.append("\n	isdelete = false AND isship IS TRUE AND p.namee " +
                "= ANY(" +
                "		WITH rc_pol AS(SELECT DISTINCT polnamee AS pol FROM price_fcl_bargefeedtl" +
                "				UNION ALL" +
                "				SELECT DISTINCT pol FROM price_fcl WHERE isdelete = false and pol <> '' and pol is not null" +
                "				UNION ALL" +
                "				SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispol = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee))" +
                "				SELECT DISTINCT pol FROM rc_pol ORDER BY pol" +
                "		)");
        sb.append("\n	AND (code ILIKE '%" + reqStr + "%'");
        sb.append("\n	OR namec ILIKE '%" + reqStr + "%'");
        sb.append("\n	OR namee ILIKE '%" + reqStr + "%')");
        sb.append("\n	ORDER BY code");
        sb.append("\n	LIMIT " + pageSize);
        sb.append("\n	OFFSET " + ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)));
        sb.append("\n	) T");
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

        StringBuffer sbTotal = new StringBuffer();
        sbTotal.append("\n SELECT");
        sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
        sbTotal.append("\n FROM");
        sbTotal.append("\n 		dat_port p");
        sbTotal.append("\n WHERE");
        sbTotal.append("\n 		isdelete = FALSE AND isship IS TRUE AND p.namee " +
                "= ANY(" +
                "		WITH rc_pol AS(SELECT DISTINCT polnamee AS pol FROM price_fcl_bargefeedtl" +
                "				UNION ALL" +
                "				SELECT DISTINCT pol FROM price_fcl WHERE isdelete = false and pol <> '' and pol is not null" +
                "				UNION ALL" +
                "				SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispol = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee))" +
                "				SELECT DISTINCT pol FROM rc_pol ORDER BY pol" +
                "		)");
        sbTotal.append("\n 		AND (code ILIKE '%" + reqStr + "%'");
        sbTotal.append("\n 		OR namec ILIKE '%" + reqStr + "%'");
        sbTotal.append("\n 		OR namee ILIKE '%" + reqStr + "%')");
        Map<String, String> total = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

        view.setSuccess(true);
        view.setData("{\"results\":" + result.get("json") + ",\"total\":" + total.get("total") + "}");
        return view;
    }

    private BaseView getFclpod(Map parameterMap) throws UnsupportedEncodingException {
        BaseView view = new BaseView();

        // String apikey = MD5Encrypt.getInstance().encrypt(String.valueOf(parameterMap.get("apiKey")));
        // if (!"tyj@123456".equals(apikey)) {
        //     view.setSuccess(false);
        //     view.setMessage("apikey不正确");
        //     return view;
        // }

        String pageIndex = String.valueOf(parameterMap.get("pageIndex")) == "null" ? "1" : String.valueOf(parameterMap.get("pageIndex"));
        String pageSize = String.valueOf(parameterMap.get("pageSize")) == "null" ? "10" : String.valueOf(parameterMap.get("pageSize"));
        String reqStr = String.valueOf(parameterMap.get("reqStr")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("reqStr"))[0]);
        reqStr = new String(reqStr.getBytes("iso8859-1"), "utf-8");
        StringBuffer sb = new StringBuffer();
        sb.append("\n SELECT");
        sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
        sb.append("\n FROM");
        sb.append("\n (SELECT");
        sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') AS namec,COALESCE(namee,'') as namee");
        sb.append("\n FROM");
        sb.append("\n	dat_port p");
        sb.append("\n WHERE");
        sb.append("\n	isdelete = false AND isship IS TRUE AND p.namee " +
                "= ANY(" +
                "		WITH rc_pol AS(SELECT DISTINCT pod FROM price_fcl WHERE isdelete = false and pod <> '' and pod is not null " +
                "		UNION ALL " +
                "		SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispod = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee)) " +
                "		SELECT DISTINCT pod FROM rc_pol ORDER BY pod" +
                "		)");
        sb.append("\n	AND (code ILIKE '%" + reqStr + "%'");
        sb.append("\n	OR namec ILIKE '%" + reqStr + "%'");
        sb.append("\n	OR namee ILIKE '%" + reqStr + "%')");
        sb.append("\n	ORDER BY code");
        sb.append("\n	LIMIT " + pageSize);
        sb.append("\n	OFFSET " + ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)));
        sb.append("\n	) T");
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

        StringBuffer sbTotal = new StringBuffer();
        sbTotal.append("\n SELECT");
        sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
        sbTotal.append("\n FROM");
        sbTotal.append("\n 		dat_port p");
        sbTotal.append("\n WHERE");
        sbTotal.append("\n 		isdelete = FALSE AND isship IS TRUE AND p.namee " +
                "= ANY(" +
                "		WITH rc_pol AS(SELECT DISTINCT pod FROM price_fcl WHERE isdelete = false and pod <> '' and pod is not null " +
                "		UNION ALL " +
                "		SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispod = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee)) " +
                "		SELECT DISTINCT pod FROM rc_pol ORDER BY pod" +
                "		)");
        sbTotal.append("\n 		AND (code ILIKE '%" + reqStr + "%'");
        sbTotal.append("\n 		OR namec ILIKE '%" + reqStr + "%'");
        sbTotal.append("\n 		OR namee ILIKE '%" + reqStr + "%')");
        Map<String, String> total = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

        view.setSuccess(true);
        view.setData("{\"results\":" + result.get("json") + ",\"total\":" + total.get("total") + "}");
        return view;
    }

    @Action(method = "getCarrier")
    private BaseView getCarrier(Map parameterMap) throws Exception {
        BaseView view = new BaseView();

        // String apikey = MD5Encrypt.getInstance().encrypt(String.valueOf(parameterMap.get("apiKey")));
        // if (!"tyj@123456".equals(apikey)) {
        //     view.setSuccess(false);
        //     view.setMessage("apikey不正确");
        //     return view;
        // }

        List<SelectItem> list = CommonComBoxBean.getComboxItems("DISTINCT shipping", "shipping"
                , "price_fcl AS d", "WHERE isdelete = false and shipping <> '' and shipping is not null", "ORDER BY shipping");
        view.setSuccess(true);
        JSONArray jsonArray = JSONArray.fromObject(list);
        view.setData(String.valueOf(jsonArray.toString()));
        return view;
    }

    @Action(method = "getFreightRate")
    private BaseView getFreightRate(Map parameterMap) {
        BaseView view = new BaseView();

        // String apikey = MD5Encrypt.getInstance().encrypt(String.valueOf(parameterMap.get("apiKey")));
        // if (!"tyj@123456".equals(apikey)) {
        //     view.setSuccess(false);
        //     view.setMessage("apikey不正确");
        //     return view;
        // }

        String sqlId = "pages.module.price.qryfclBean.grid.page";
        Map map = getQryClauseWhere(parameterMap);
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        List<Map<String, Object>> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, map);
        view.setSuccess(true);
        if (list != null) {
            view.setData(String.valueOf(list));
        } else {
            view.setData("");
        }
        return view;
    }


    public Map getQryClauseWhere(Map<String, Object> parameterMap) {
        String isqryPol = "Y";
        String isshipline = "Y";
        String pol = String.valueOf(parameterMap.get("pol")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("pol"))[0]);
        String pod = String.valueOf(parameterMap.get("pod")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("pod"))[0]);
        String carrier = String.valueOf(parameterMap.get("carrier")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("carrier"))[0]);
        String line = String.valueOf(parameterMap.get("line")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("line"))[0]);
        String qrycorpid = String.valueOf(parameterMap.get("qrycorpid")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("qrycorpid"))[0]);
        Boolean isShowHistory = Boolean.parseBoolean(String.valueOf(parameterMap.get("isShowHistory")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("isShowHistory"))[0]));
        String datefm = null;
        String dateto = null;
        String datetypefm = null;
        String datetypeto = null;
        String qryshipline = null;
        String priceuser = null;
        String pricetype = String.valueOf(parameterMap.get("pricetype")) == "null" ? "" : String.valueOf(((Object[]) parameterMap.get("pricetype"))[0]);
        String ord = null;

        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("args", "isqryPol=" + isqryPol + ";isshipline=" + isshipline + ";pol=" + StrUtils.getSqlFormat(pol) + ";pod=" + pod + ";carrier="
                + carrier + ";line=" + line + ";qrycorpid=" + qrycorpid + ";showHistory=" + (isShowHistory ? "Y" : "N"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filter = "1=1";
        if (datefm != null && dateto != null) {
            filter += "\n AND '" + sdf.format(datefm).toString() + "' " + datetypefm + " datefm  AND '" + sdf.format(dateto).toString() + "' " + datetypeto + " dateto ";
        }
        if (datefm != null && dateto == null) {
            filter += "\n AND datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "'";
        }
        if (datefm == null && dateto != null) {
            filter += "\n AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
        }
        if (!StrUtils.isNull(qryshipline)) {
            qryshipline = StrUtils.getSqlFormat(qryshipline);
            qryshipline = qryshipline.toUpperCase();
            filter += "\n AND (UPPER(shipline) LIKE '%" + qryshipline + "%')";
        }
        if (!StrUtils.isNull(priceuser)) {
            filter += "\n AND priceuserid = '" + priceuser + "'";
        }
        if (!StrUtils.isNull(pricetype)) {
            List<String> list = Arrays.asList(pricetype.split(","));
            String pricetypestr = org.apache.commons.lang.StringUtils.join(list.toArray(), "','");
            filter += "\n AND pricetype in ('" + pricetypestr + "')";
        }
        map.put("filter", filter);
        if (!StrUtils.isNull(ord)) map.put("ord", "\nORDER BY " + ord);
        return map;
    }

    private BaseView synLenovoData(Map parameterMap) {
        BaseView view = new BaseView();
        view.setSuccess(true);
        view.setMessage("synLenovoData成功");
        view.setData(JSONObject.fromObject(parameterMap).toString());
        return view;
    }

    private BaseView returnLenovoData(Map parameterMap) {
        BaseView view = new BaseView();
        view.setSuccess(true);
        view.setMessage("returnLenovoData成功");
        view.setData(JSONObject.fromObject(parameterMap).toString());
        return view;
    }

    @Action(method = "commonInterfaceString")
    public String commonInterfaceString(HttpServletRequest request, HttpServletResponse response) {
        String resultStr = "";
        Map parameterMap = new LinkedHashMap(request.getParameterMap());
        String json = "";
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Access-Control-Allow-Methods, Access-Control-Allow-Origin");
            String methodFlag = request.getParameter("methodFlag");

            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            parameterMap.put("json", json);

            // LogBean.insertLastingLog2(new StringBuffer().append("commonInterface开始,parameterMap为").append(JSONObject.fromObject(parameterMap).toString()), methodFlag);


            //云海api Business
            if ("synYunhaiData".equals(methodFlag)) {
                resultStr = synYunhaiData(parameterMap);
            }
            if ("synOuXuanData".equals(methodFlag)) {
                resultStr = synOuXuanData(parameterMap);
            }
            if ("synWetransData".equals(methodFlag)) {
                resultStr = synWetransData(parameterMap);
            }
        } catch (Exception e) {
        }
        // LogBean.insertLastingLog(new StringBuffer().append("commonInterface结束,view为").append(resultStr));
        return resultStr;
    }

    private String synYunhaiData(Map parameterMap) {
        String resultStr = "";
        String json = "";
        String querySql = "";
        String returnret = "";
        String msgid = "";
        try {
            json = parameterMap.get("json").toString();
            msgid = JSONObject.fromObject(json).getString("msgid");

            querySql = "SELECT f_yunhai_zhongji_sync_jobs('business=" + json + ";') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            returnret = StrUtils.getMapVal(map, "json");

            if (returnret.contains("新增OK;") || returnret.contains("更新OK;")) {
                // LogBean.insertLastingLog(new StringBuffer().append("commonInterface成功,parameterMap为").append(returnret));
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"1\",\"failInfo\":\"\"}").toString());
            } else {
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"0\",\"failInfo\":\"").append(returnret).append("\"}").toString());
            }

            // {
            //     "msgId":"8930000053521201810231659402",    ------报文ID  一次请求和响应的交互，msgId 相同
            //     "resultFlag":"1",                          ------处理结果，0-处理失败，1-处理成功
            //     "failInfo":""                              ------错误信息，resultFlag 为 0时，返回错误信息
            // }
            //响应"resultFlag":"1"，云海系统才不发信息，否则一直反复发。
        } catch (Exception e) {
            resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"0\",\"failInfo\":\"").append(e.getMessage()).append("\"}").toString());
        }
        return resultStr;
    }

    //欧轩对接
    private String synOuXuanData(Map parameterMap) {
        String resultStr = "";
        String json = "";
        String querySql = "";
        String returnret = "";
        String msgid = "";
        try {
            json = parameterMap.get("json").toString();
            msgid = JSONObject.fromObject(json).getString("msgid");

            String ouxuanStr = json.replaceAll(";", ",");

            querySql = "SELECT f_ouxuan_zhongji_sync_jobs('business=" + ouxuanStr + ";') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            returnret = StrUtils.getMapVal(map, "json");

            if (returnret.contains("推送成功") || returnret.contains("更新成功")) {
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"1\",\"failInfo\":\"\"}").toString());
            } else if (returnret.contains("不存在")) {
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"-1\",\"failInfo\":\" " + returnret + " \"}").toString());
            } else {
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"0\",\"failInfo\":\" " + returnret + " \"}").toString());
            }
        } catch (Exception e) {
            resultStr = (new StringBuffer().append("{\"msgId\":\"").append(0).append("\",\"resultFlag\":\"0\",\"failInfo\":\"").append(e.getMessage()).append("\"}").toString());
        }
        return resultStr;
    }

    //途C-对外提供轨迹查询接口
    @Action(method = "tracking")
    public String tracking(HttpServletRequest request) {
        String resultStr = "";
        try {
            Map args = new HashMap();
            args.put("trackingRef", "'" + request.getParameter("trackingRef") + "'");
            String querySql = "base.commerce.tracking";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Object query = daoIbatisTemplate.getSqlMapClientTemplate().queryForObject(querySql, args);
            if (query != null) {
                String querySql1 = "base.commerce.trackingRef";
                String querySql2 = "base.commerce.goodstrack.trackingRef";
                args.put("trackingRef", JSONObject.fromObject(query).get("jobid").toString());
                Object queryForObject = daoIbatisTemplate.getSqlMapClientTemplate().queryForObject(querySql1, args);
                List<Map> queryForList = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql2, args);
                com.alibaba.fastjson.JSONObject parseObject =
                        com.alibaba.fastjson.JSONObject.parseObject(JSONObject.fromObject(queryForObject).get("tracking").toString());
                if (parseObject != null) {
                    resultStr = (new StringBuffer()
                            .append("{\"success\":").append(true)
                            .append(",\"jobnum\":\"").append(parseObject.get("jobnum"))
                            .append("\",\"status\":\"").append(parseObject.get("status"))
                            .append("\",\"packages\":\"").append(parseObject.get("packages"))
                            .append("\",\"destcountrycode\":\"").append(parseObject.get("destcountrycode"))
                            .append("\",\"datalist\":").append(JSONArray.fromObject(StrUtils.getMapVal(queryForList.get(0), "tracking")) + "}")
                            .toString());
                }
            } else {
                resultStr = (new StringBuffer()
                        .append("{\"success\":").append(false)
                        .append(",\"jobnum\":\"").append("")
                        .append("\",\"status\":\"").append("")
                        .append("\",\"packages\":\"").append("")
                        .append("\",\"destcountrycode\":\"").append("")
                        .append("\",\"datalist\":").append(new ArrayList() + "}")
                        .toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    private String synWetransData(Map parameterMap) {
        String resultStr = "";
        String json = "";
        String querySql = "";
        String returnret = "";
        String msgid = "";
        try {
            json = parameterMap.get("json").toString();
            msgid = JSONObject.fromObject(json).getString("msgid");

            querySql = "SELECT f_wetrans_zhongji_sync_jobs('business=" + json + ";') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            returnret = StrUtils.getMapVal(map, "json");

            if (returnret.contains("新增OK;") || returnret.contains("更新OK;")) {
                // LogBean.insertLastingLog(new StringBuffer().append("commonInterface成功,parameterMap为").append(returnret));
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"1\",\"failInfo\":\"\"}").toString());
            } else {
                resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"0\",\"failInfo\":\"").append(returnret).append("\"}").toString());
            }

            // {
            //     "msgId":"8930000053521201810231659402",    ------报文ID  一次请求和响应的交互，msgId 相同
            //     "resultFlag":"1",                          ------处理结果，0-处理失败，1-处理成功
            //     "failInfo":""                              ------错误信息，resultFlag 为 0时，返回错误信息
            // }
            //响应"resultFlag":"1"，云海系统才不发信息，否则一直反复发。
        } catch (Exception e) {
            resultStr = (new StringBuffer().append("{\"msgId\":\"").append(msgid).append("\",\"resultFlag\":\"0\",\"failInfo\":\"").append(returnException(e)).append("\"}").toString());
        }
        return resultStr;
    }

    public static String returnException(Exception e) {
        String tip = "";
        e.printStackTrace();
        String message = e.getLocalizedMessage();
        String messageDtl = ExceptionUtil.getFullExceptionMessage(e);

        message = StrUtils.isNull(message) ? "" : message;
        messageDtl = StrUtils.isNull(messageDtl) ? "" : messageDtl;

        int indexBegin = messageDtl.indexOf("<DB Exception>:");
        if (indexBegin > 0) {
            messageDtl = messageDtl.substring(indexBegin);
            indexBegin = 0;
            int indexEnd = messageDtl.indexOf("\n");
            tip = messageDtl.substring(indexBegin
                    + "<DB Exception>:".length(), indexEnd);
        }

        if (e instanceof CommonRuntimeException) {
            tip = e.getLocalizedMessage();
        }
        return tip;
    }


    @Action(method = "fcllist")
    public String getFcllist(HttpServletRequest request) {
        final String pol = request.getParameter("pol");
        final String pod = request.getParameter("pod");
        final String carrier = request.getParameter("crrier");
        String linev = request.getParameter("line");
        final String line = URLDecoder.decode(StrUtils.isNull(linev) ? "" : linev);
        //2022-04-26 app端查询添加运价类型
        final String pricetype = request.getParameter("pricetype");

        if (StrUtils.isNull(pol) && StrUtils.isNull(pod) && StrUtils.isNull(carrier)) {
            String returns = "{\"code\":0,\"msg\":\"\",\"count\":" + 0 + ",\"data\":\"\"}";
            return returns;
        }

        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        final UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String rtype = "B";
        if (userSession != null) {
            //内部登录后运价不加价
            if (userSession.getSystemUser() == true) {
                rtype = "S";
            } else {
                rtype = "T";
            }
        }
        final String ruletype = rtype;
        GridDataProvider gridDataProvider = new GridDataProvider(page, limit) {
            @Override
            public String getElements() {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("\n SELECT datefm::TEXT||'</br>'||dateto::TEXT AS datefromto");
                sqlBuilder.append("\n ,(CASE WHEN x.cost20 IS NULL THEN 0 ELSE COALESCE(x.cost20,0)+COALESCE(x.barcost20,0) END) AS cost20sum");
                sqlBuilder.append("\n ,(CASE WHEN x.cost40gp IS NULL THEN 0 ELSE COALESCE(x.cost40gp,0)+COALESCE(x.barcost40gp,0) END) AS cost40gpsum");
                sqlBuilder.append("\n ,(CASE WHEN x.cost40hq IS NULL THEN 0 ELSE COALESCE(x.cost40hq,0)+COALESCE(x.barcost40hq,0) END) AS cost40hqsum");
                sqlBuilder.append("\n ,(CASE WHEN COALESCE(TRIM(x.pollink),'') = '' THEN 0 ELSE 1 END) AS ispollink");

                sqlBuilder.append("\n ,(CASE WHEN COALESCE(tt,'')='' THEN '-' ELSE COALESCE(tt,'') END)||'<br>'||COALESCE(via,'') AS ttvia,regexp_replace(route,'-->','<br>-->','g') AS routev");
                sqlBuilder.append("\n ,COALESCE((SELECT shipping FROM price_fcl WHERE id = x.id),'')||'</br>'||COALESCE(line,'')||'</br>' AS shipingline,x.*");
                sqlBuilder.append("\n FROM f_rpa_qryfcl_table('isqryPol=Y;isshipline=Y;pol=" + pol + ";pod=" + pod + ";carrier=;line=;ruletype=" + ruletype + ";corpid=" + (userSession != null ? userSession.getCorpid() : "") + "') x");
                sqlBuilder.append("\n WHERE 1=1");
                sqlBuilder.append((StrUtils.isNull(pricetype) ? "" : "\n AND pricetype IN (" + pricetype + ")"));
                sqlBuilder.append((StrUtils.isNull(carrier) ? "" : "\n AND shipping ILIKE '" + carrier + "%'"));
                sqlBuilder.append((StrUtils.isNull(line) ? "" : "\n AND line = '" + line + "'"));
                sqlBuilder.append("\n LIMIT " + limit + " OFFSET " + offset);
                String querySql = sqlBuilder.toString();
                return DaoUtil.queryForJsonArray(querySql);
            }

            @Override
            public String getTotalCount() {
                String countsql = "SELECT COUNT(1)"
                        + "\n FROM f_rpa_qryfcl_table('isqryPol=Y;isshipline=Y;pol=" + pol + ";pod=" + pod + ";carrier=;line=;ruletype=" + ruletype + ";corpid=" + (userSession != null ? userSession.getCorpid() : "") + "') x"
                        + ("\n WHERE 1=1 ")
                        + (StrUtils.isNull(carrier) ? "" : "\n AND shipping ILIKE '" + carrier + "%'")
                        + (StrUtils.isNull(pricetype) ? "" : "\n AND pricetype IN (" + pricetype + ")")
                        + (StrUtils.isNull(line) ? "" : "\n AND line ='" + line + "'");

                Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countsql);
                return StrUtils.getMapVal(m, "count");
            }
        };
        String ret = gridDataProvider.getGridJsonData();

        return ret;
    }


    @Action(method="getfeeadd")
    public String findPriceFclFeeAddByPriceid(HttpServletRequest request){
        Enumeration enu=request.getParameterNames();
        String priceid = request.getParameter("id");
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        String querySql = "WITH rc_fee AS("
                +"\nSELECT feeitemname||'/'||feeitemcode AS feeitem,ppcc,currency,unit,amt20,amt40gp,amt40hq,amt"
                +"\nFROM price_fcl_feeadd WHERE isdelete = FALSE AND fclid = "+priceid+")"
                +"\n,tx_amts AS (SELECT string_agg(amts,'<br>') amts FROM (SELECT currency||':'||SUM(amt) AS amts FROM rc_fee GROUP BY currency) r)"
                +"\nSELECT a.*,b.amts FROM rc_fee a,tx_amts b";
        String result = "{\"code\":0,\"msg\":\"\",\"count\":"+100+",\"data\":"+DaoUtil.queryForJsonArray(querySql)+"}";
        return result;
    }

}

