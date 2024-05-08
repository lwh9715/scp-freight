package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.commerce.BusCommerceResponse;
import com.scp.model.commerce.BusPackListResponse;
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
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author CIMC
 */

@WebServlet("/buspacklist")
@ManagedBean(name = "pages.module.commerce.buspacklistBean")
public class BusPackListBean extends BaseServlet {

    /**
     * 快速入仓
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "quickSave")
    public BaseView quickSaveBusPack(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            JSONObject finajobs = JSONObject.parseObject(object.get("finajobs").toString());
            finajobs.put("inputer", session.get("username").toString());
            object.put("finajobs", finajobs);

            String querySql = "";
            querySql = "SELECT f_commerce_buspacklist_quick_join('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");

            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 更新入仓时间
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "updateWmsindate")
    public BaseView updateWmsindate(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);

            String querySql = "";
            querySql = "update bus_packlist set inputtime = '" + object.get("inputtime") + "' ,inputer = '" + session.get("username").toString() +
                    "' where id = " + object.get("id") + " AND isdelete = FALSE;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            daoIbatisTemplate.updateWithUserDefineSql(querySql);
            view.setSuccess(true);
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }


    /**
     * 删除入仓
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "removeBusPack")
    public BaseView removeBusPack(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            JSONObject object = JSONObject.parseObject(json);
            object.put("updater", session.get("username").toString());

            String querySql = "";
            querySql = "SELECT f_commerce_buspacklist_remove('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");

            view.setData(map.get("json").toString());
        } catch (Exception e) {
            String tip = MessageUtils.showCommerceException(e);
            view.setData(null);
            view.add("tip", tip);
        }
        return view;
    }

    /**
     * 拣货接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "getPickingData")
    public BaseView getPickingData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            Map args = new HashMap();
            args.put("qry", "AND fj.nos = '" + json + "'");
            String querySql = "base.commerce.goodslist";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("goodslist", StrUtils.getMapVal(list.get(0), "goodslist"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 確認拣货接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "pickGoodsData")
    public BaseView pickGoodsData(HttpServletRequest request) {
        BaseView view = new BaseView();
        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            object.put("inputer", session.get("username").toString());
            String querySql = "SELECT f_commerce_buspacklist_pickgoods('" + object.toString() + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            view.setData(map.get("json").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 快速入仓页面 数据查询
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "queryJob")
    public BaseView queryJob(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);

            StringBuffer sql = new StringBuffer();
            sql.append("\n SELECT id FROM fina_jobs WHERE nos = '" + json + "' LIMIT 1");

            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map<String, Long> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());

            if (!result.isEmpty()) {
                String querySql = "base.commerce.quickFinajobs";
                String querySql1 = "base.commerce.quickBusCommerce";
                String querySql2 = "base.commerce.quickBusPacklist";
                Map args = new HashMap();
                args.put("id", result.get("id"));
                List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
                List<Map> list1 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql1, args);
                List<Map> list2 = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql2, args);
                view.add("jobs", StrUtils.getMapVal(list.get(0), "jobs"));
                view.add("commerce", StrUtils.getMapVal(list1.get(0), "commerce"));
                view.add("buspacklist", StrUtils.getMapVal(list2.get(0), "buspacklist"));
            } else {
                view.setData("沒有查询到该工作单");
            }
        } catch (Exception e) {
            view.setData("沒有查询到该工作单");
        }
        return view;
    }

    /**
     * 获取入仓数据列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "queryBusPackListData")
    public BaseView queryBusPackListData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "";

            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                String replaceAll = object.get("nos").toString().replaceAll(" ", "','");
                sql += " AND fj.nos in ('" + replaceAll + "')";
            }

            if (object.containsKey("nostype") || object.get("nostype") != null) {
                if (object.get("nostype").toString().equals("TSZ")) {
                    sql += " AND fj.nos ilike 'TSZ%'";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    sql += " AND fj.nos ilike 'TAE%'";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    sql += " AND fj.nos ilike 'TDA%'";
                } else if (object.get("nostype").toString().equals("TXM")) {
                    sql += " AND fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%'";
                } else if (object.get("nostype").toString().equals("GQD")) {
                    sql += " AND fj.nos ilike 'GQD%'";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " limit 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            sql += " AND fj.nos ilike 'TSZ%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            sql += " AND fj.nos ilike 'TAE%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            sql += " AND fj.nos ilike 'TDA%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            sql += " AND fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%'";
                        }
                        //统计报表只显示途曦的工作单
                        sql += " AND (fj.corpid = 11540072274 OR fj.corpidop = 11540072274 " +
                                "OR (SELECT EXISTS(SELECT 1 FROM fina_corp WHERE jobid = fj.id AND corpid = 11540072274 AND isdelete = FALSE)))";
                    } else if (session.get("corpid").toString().equals("1122274")) {
                        sql += " AND fj.corpid = 1122274";
                    }
                }
            }

            String querySql = "base.commerce.BusPacklistData";
            Map args = new HashMap();
            args.put("qry", sql);
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("buspacklist", StrUtils.getMapVal(list.get(0), "buspacklist"));

        } catch (Exception e) {
            view.setData("沒有查询到入仓数据");
        }
        return view;
    }

    /**
     * 获取装箱单列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "query")
    public BaseView queryBusPackList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql = "base.commerce.bus_packlist";
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            if (!list.isEmpty()) {
                view.add("buspacklist", list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 获取装箱单列表 -- 入仓后的数据
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "queryWarehouseData")
    public BaseView queryWarehouseData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql = "base.commerce.queryWarehouseData";
            String querytotalSql = "base.commerce.queryWarehouseTotal";
            Map args = new HashMap();
            args.put("id", jsonObject.get("id").toString());
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            List<Map> totallist = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querytotalSql, args);
            if (!list.isEmpty()) {
                view.add("buspacklist", StrUtils.getMapVal(list.get(0), "buspacklist"));
                view.add("buspacktotal", StrUtils.getMapVal(totallist.get(0), "buspacktotal"));
                view.setData(String.valueOf(view));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 混装单列表
     *
     * @param request
     * @return
     */
    @Action(method = "buspackbrieflylist")
    public BaseView BusPackBrieflyList(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject jsonObject = JSONObject.parseObject(json);

            String querySql = "base.commerce.buspackbrieflylist";
            Map args = new HashMap();
            args.put("jobid", jsonObject.get("jobid").toString());
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            if (!list.isEmpty()) {
                view.add("buspackbrieflylist", list);
                view.setData(String.valueOf(view));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 添加装箱单
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "save")
    public BaseView BusPackSave(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            String querySql = "";
            querySql = "SELECT f_commerce_buspacklist_join('" + json + "') AS json;";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            String reRet = "";
            reRet = StrUtils.getMapVal(map, "json");

            view.setData(map.get("json").toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 产品图片上传
     *
     * @param request
     */
    @Action(method = "uploadpicfile")
    public void uploadPicFile(HttpServletRequest request) {
        try {
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");

            request.setCharacterEncoding("utf-8");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> fileItems = upload.parseRequest(request);
            List<FileItem> needFileItem = new LinkedList<FileItem>();
            String linkid = "";
            for (FileItem fileItem : fileItems) {
                if (!fileItem.isFormField()) {
                    needFileItem.add(fileItem);
                } else {
                    if ("linkid".equals(fileItem.getFieldName())) {
                        linkid = fileItem.getString("utf-8");
                    }
                }
            }

            for (FileItem fileItem : needFileItem) {
                String thisattachidsql = "select getid() as attachid";
                Map thisattachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(thisattachidsql);
                String attachid = StrUtils.getMapVal(thisattachidmap, "attachid");

                String fileurl = AppUtils.getAttachFilePath() + attachid + fileItem.getName();
                File file = new File(fileurl);
                fileItem.write(file);

                String sql = "SELECT id FROM sys_role where name='跨境电商' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
                Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
                String roleid = StrUtils.getMapVal(map, "id");

                String sqlAttachment = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer,inputtime, roleid,remarks,url)" +
                        "   VALUES (getid(), " + linkid + ", '" + fileItem.getName() + "', '" + fileItem.getContentType() + "' , " + (new BigDecimal(fileItem.getSize()))
                        + ",'" + "" + "', NOW(), " + roleid + ", '','" + fileurl + "');";
                daoIbatisTemplate.updateWithUserDefineSql(sqlAttachment);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 批量导入箱单发票
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "importtemplate")
    public BaseView ImportTemplate(HttpServletRequest request) throws IOException, FileUploadException {
        BaseView view = new BaseView();

        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));

        request.setCharacterEncoding("utf-8");
        InputStream stream = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = upload.parseRequest(request);
        List<FileItem> needFileItem = new LinkedList<FileItem>();
        String jobid = "";
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                needFileItem.add(fileItem);
            } else {
                if ("jobid".equals(fileItem.getFieldName())) {
                    jobid = fileItem.getString("utf-8");
                }
            }
        }

        try {
            String querySql = "";

            stream = needFileItem.get(0).getInputStream();
            Workbook workbook = new XSSFWorkbook(stream);
            if (workbook != null) {
                int sheetCount = workbook.getNumberOfSheets();
                if (sheetCount > 0) {
                    List<BusCommerceResponse> list = new ArrayList();
                    List<BusPackListResponse> packListResponseList = new ArrayList();

                    if (session.get("corpid").toString().equals("1122274")) {

                        // 青岛 - 文本内容
                        for (int i = 0; i < sheetCount; i++) {
                            if (i == 1) {
                                break;
                            }
                            Sheet sheet = workbook.getSheetAt(i);
                            //工作单信息
                            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                if (rowNum == 14) {
                                    break;
                                }
                                Row row = sheet.getRow(rowNum);
                                if (row == null || row.getFirstCellNum() < 0 || StringUtils.isBlank(row.getCell(0).toString())) {
                                    break;
                                }
                            }
                            //装箱单内容
                            for (int rowNum = 15; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                Row row = sheet.getRow(rowNum);
                                if (row == null || row.getFirstCellNum() < 0 || row.getLastCellNum() < 10) {
                                    break;
                                }
                                packListResponseList.add(new BusPackListResponse(row, session.get("username").toString()));
                            }
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("jobid", jobid);
                        jsonObject.put("invoice", JSONObject.toJSON(packListResponseList));

                        querySql = "SELECT f_commerce_buspacklist_import_join_qd('" + jsonObject.toString() + "') AS json;";

                    } else {
                        // 途曦 - 文本内容
                        for (int i = 0; i < sheetCount; i++) {
                            Sheet sheet = workbook.getSheetAt(i);
                            for (int rowNum = 10; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                Row row = sheet.getRow(rowNum);
                                if (row == null || row.getFirstCellNum() < 0 || StringUtils.isBlank(row.getCell(0).toString())) {
                                    break;
                                }
                                packListResponseList.add(new BusPackListResponse(row));
                            }
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("jobid", jobid);
                        jsonObject.put("invoice", JSONObject.toJSON(packListResponseList));

                        querySql = "SELECT f_commerce_buspacklist_import_join('" + jsonObject.toString() + "') AS json;";
                    }

                    DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                    Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

                    //解析完excel 并上传附件
                    String serverName = request.getContextPath().replace("/", "");
                    String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                    FileOperationUtil.newFolder(path);

                    path += File.separator + serverName;
                    FileOperationUtil.newFolder(path);

                    path += File.separator + "attachfile" + File.separator;
                    FileOperationUtil.newFolder(path);

                    //获取唯一ID
                    String attachidsql = "select getid() as attachid";
                    Map attachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachidsql);
                    String attachid = StrUtils.getMapVal(attachidmap, "attachid");

                    File shpFile = new File(path + File.separator + attachid + needFileItem.get(0).getName());
                    shpFile.createNewFile();

                    copyFile(needFileItem.get(0).getInputStream(), new FileOutputStream(shpFile));

                    //上传到箱单发票附件
                    String querySql2 = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer, inputtime, roleid)\n" +
                            "VALUES (" + attachid + ", " + jobid + ",'" + needFileItem.get(0).getName() + "','" +
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" + "', '" + needFileItem.get(0).getSize() +
                            "','" + session.get("usercode").toString() + "', NOW(), 997852212274);";
                    daoIbatisTemplate.updateWithUserDefineSql(querySql2);

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
     * 客户端导入
     *
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    @Action(method = "soimporttemplate")
    public BaseView ImportTemplateso(HttpServletRequest request) throws IOException, FileUploadException {
        BaseView view = new BaseView();
        String resultStr = (new StringBuffer().append("{\"success\":").append(false) + "}".toString());

        request.setCharacterEncoding("utf-8");
        InputStream stream = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = upload.parseRequest(request);
        List<FileItem> needFileItem = new LinkedList<FileItem>();
        String jobid = "";
        String corpid = "";
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                needFileItem.add(fileItem);
            } else {
                if ("jobid".equals(fileItem.getFieldName())) {
                    jobid = fileItem.getString("utf-8");
                }
                if ("corpid".equals(fileItem.getFieldName())) {
                    corpid = fileItem.getString("utf-8");
                }
            }
        }

        try {
            stream = needFileItem.get(0).getInputStream();
            Workbook workbook = new XSSFWorkbook(stream);
            if (workbook != null) {
                int sheetCount = workbook.getNumberOfSheets();
                if (sheetCount > 0) {

                    List<BusPackListResponse> list = new ArrayList();

                    // 文本内容
                    for (int i = 0; i < sheetCount; i++) {
                        Sheet sheet = workbook.getSheetAt(i);
                        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            Row row = sheet.getRow(rowNum);
                            if (row == null || row.getFirstCellNum() < 0 || StringUtils.isBlank(row.getCell(0).toString())) {
                                break;
                            }
                            list.add(new BusPackListResponse("", row));
                        }
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("jobid", jobid);
                    jsonObject.put("corpid", corpid);
                    jsonObject.put("invoice", JSONObject.toJSON(list));

                    String querySql = "";
                    querySql = "SELECT f_so_commerce_buspacklist_import_join('" + jsonObject.toString() + "') AS json;";
                    DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                    Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

                    //获取导入生成工作单的id和单号
                    String querySql3 = "SELECT id,nos from fina_jobs where id = " + StrUtils.getMapVal(map, "json") + " and isdelete = false LIMIT 1";
                    Map map3 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql3);

                    //解析完excel 并上传附件
                    String serverName = request.getContextPath().replace("/", "");
                    String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                    FileOperationUtil.newFolder(path);

                    path += File.separator + serverName;
                    FileOperationUtil.newFolder(path);

                    path += File.separator + "attachfile" + File.separator;
                    FileOperationUtil.newFolder(path);

                    //获取唯一ID
                    String attachidsql = "select getid() as attachid";
                    Map attachidmap = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(attachidsql);
                    String attachid = StrUtils.getMapVal(attachidmap, "attachid");

                    File shpFile = new File(path + File.separator + attachid + needFileItem.get(0).getName());
                    shpFile.createNewFile();

                    copyFile(needFileItem.get(0).getInputStream(), new FileOutputStream(shpFile));

                    //上传到箱单发票附件
                    String querySql2 = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer, inputtime, roleid)\n" +
                            "VALUES (" + attachid + ", " + map3.get("id") + ",'" + needFileItem.get(0).getName() + "','" +
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" + "', '" + needFileItem.get(0).getSize() +
                            "','demo', NOW(), 997852212274);";
                    daoIbatisTemplate.updateWithUserDefineSql(querySql2);

                    resultStr = (new StringBuffer().append("{\"success\":").append(true).append(",\"id\":\"").append(map3.get("id"))
                            .append("\",\"nos\":\"").append(map3.get("nos") + "\"}").toString());
                    view.setData(resultStr);
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
     * 导出箱单发票模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @Action(method = "exporttemplate")
    public void ExportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {

        File file = new File(AppUtils.getImportTemplateFilePath());
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getAbsolutePath().endsWith("commerce")) {
                if (listFiles[i].isDirectory()) {
                    File[] files = listFiles[i].listFiles();
                    for (int j = 0; j < files.length; j++) {
                        if (files[j].getAbsolutePath().endsWith("InvoiceTemplate.xlsx")) {
                            try {
                                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(files[j]));
                                byte[] buffer = new byte[bis.available()];
                                bis.read(buffer);
                                bis.close();
                                //输出流导出文件
                                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                                //设置响应头
                                response.setCharacterEncoding("utf-8");
                                //设置导出类型
                                response.setContentType("application/vnd.ms-excel");
                                response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(files[j].getName(), "utf8"));
                                bos.write(buffer);
                                bos.flush();
                                bos.close();
                                break;
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }
                }
            }
        }
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
     * 获取库存数据列表
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Action(method = "queryInventoryData")
    public BaseView queryInventoryData(HttpServletRequest request) {
        BaseView view = new BaseView();
        try {
            JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
            String json = "";
            InputStream is = request.getInputStream();
            json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            JSONObject object = JSONObject.parseObject(json);
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            String sql = "";

            if (object.containsKey("nos") && object.get("nos") != null && !StrUtils.isNull(object.get("nos").toString())) {
                String replaceAll = object.get("nos").toString().replaceAll(" ", "','");
                sql += " AND fj.nos in ('" + replaceAll + "')";
            }

            if (object.containsKey("nostype") || object.get("nostype") != null) {

                if (object.get("nostype").toString().equals("TSZ")) {
                    sql += " AND fj.nos ilike 'TSZ%'";
                } else if (object.get("nostype").toString().equals("TAE")) {
                    sql += " AND fj.nos ilike 'TAE%'";
                } else if (object.get("nostype").toString().equals("TDA")) {
                    sql += " AND fj.nos ilike 'TDA%'";
                } else if (object.get("nostype").toString().equals("TXM")) {
                    sql += " AND fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%'";
                } else if (object.get("nostype").toString().equals("GQD")) {
                    sql += " AND fj.nos ilike 'GQD%'";
                } else {
                    //途曦按照部分区分工作单
                    if (session.get("corpid").toString().equals("11540072274")) {
                        String deptsql = "SELECT deptid FROM sys_user where id = " + session.get("userid").toString() + " LIMIT 1";
                        Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(deptsql);
                        if (StrUtils.getMapVal(map, "deptid").equals("516230252274")) {
                            sql += " AND fj.nos ilike 'TSZ%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516228802274")) {
                            sql += " AND fj.nos ilike 'TAE%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("516239022274")) {
                            sql += " AND fj.nos ilike 'TDA%'";
                        } else if (StrUtils.getMapVal(map, "deptid").equals("1565607982274")) {
                            sql += " AND fj.nos ilike 'TXM%' OR fj.nos ilike 'TBZ%'";
                        }
                        sql += " AND fj.corpid = 11540072274";

                    } else if (session.get("corpid").toString().equals("1122274")) {
                        sql += " AND fj.corpid = 1122274";
                    }
                }

            }

            String querySql = "base.commerce.InventoryData";
            Map args = new HashMap();
            args.put("qry", sql);
            List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql, args);
            view.add("list", StrUtils.getMapVal(list.get(0), "inventorydata"));

        } catch (Exception e) {
            view.setData("沒有查询到入仓数据");
        }
        return view;
    }

}
