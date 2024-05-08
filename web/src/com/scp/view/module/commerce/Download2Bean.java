package com.scp.view.module.commerce;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.FileOperationUtil;
import com.scp.util.StrUtils;
import com.ufms.base.utils.AppUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author CIMC 并单列表 批量下载H单 入仓核实单
 */
@WebServlet("/downloadWarehouseEntry")
@ManagedBean(name = "pages.module.commerce.download2Bean", scope = ManagedBeanScope.REQUEST)
public class Download2Bean extends HttpServlet {

    /**
     * 从数据库中获取全部数据
     *
     * @throws Exception
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject session = (JSONObject) JSONObject.toJSON(request.getSession().getAttribute("UserSession"));
        ZipOutputStream zipOut = null;
        try {
            String id = request.getParameter("id");
            String querySql = "SELECT nos FROM fina_jobs WHERE id = " + id + " AND isdelete = FALSE LIMIT 1";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            List<Map> nos = daoIbatisTemplate.queryWithUserDefineSql(querySql);
            List<Map> mapList = this.findAttachFile(Long.valueOf(id));
            List<File> fileList = new ArrayList<File>();
            if (mapList.size() > 0) {
                //上传路径
                String filepath = AppUtils.getWebApplicationPath() + File.separator + "upload" + File.separator + "storehouse" + File.separator + "attachfile";

                for (Map m : mapList) {
                    String fileName = StrUtils.getMapVal(m, "nos") + "-入仓核实单" + ".pdf";
                    String contentType = StrUtils.getMapVal(m, "content_type");
                    File f = new File(filepath);

                    String overlaidUrl = filepath + File.separator + fileName;
                    if (new File(overlaidUrl).exists()) {
                        f = new File(overlaidUrl);
                        fileList.add(f);
                    }
                }
                zipOut = new ZipOutputStream(response.getOutputStream());
                //設置压缩包文件名
                String headerName = nos.get(0).get("nos") + "（" + mapList.size() + "票）";
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(headerName, "utf8") + ".zip\"");

                for (File file : fileList) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);

                    FileInputStream fileIn = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileIn.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, bytesRead);
                    }

                    fileIn.close();
                    zipOut.closeEntry();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            zipOut.close();
        }
    }

    /**
     * 批量下載M單下H单的入仓核实单
     *
     * @param attachFileId
     * @return
     */
    public List<Map> findAttachFile(Long attachFileId) throws Exception {

        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        String querySql = "SELECT id as pid,nos FROM fina_jobs WHERE parentid = " + attachFileId + " AND isdelete = FALSE";
        List<Map> nosList = daoIbatisTemplate.queryWithUserDefineSql(querySql);

        if (nosList.size() <= 0) {
            return null;
        }

        //创建连接池
        DruidDataSource dataSource = (DruidDataSource) AppUtils.getBeanFromSpringIoc("dataSource");
        Connection connection = dataSource.getConnection();

        //找到入仓核实文件
        File file = new File(AppUtils.getReportFilePath());
        File[] listFiles = file.listFiles();

        try {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].getAbsolutePath().endsWith("commerce")) {
                    if (listFiles[i].isDirectory()) {
                        File[] files = listFiles[i].listFiles();
                        for (int j = 0; j < files.length; j++) {
                            if (files[j].getAbsolutePath().endsWith(594 + ".jasper")) {

                                for (Map map : nosList) {

                                    String generateFileName = StrUtils.getMapVal(map, "nos") + "-入仓核实单" + ".pdf";
                                    //解析完excel 并上传附件
                                    String path = AppUtils.getWebApplicationPath() + File.separator + "upload";
                                    FileOperationUtil.newFolder(path);

                                    path += File.separator + "storehouse";
                                    FileOperationUtil.newFolder(path);

                                    path += File.separator + "attachfile";
                                    FileOperationUtil.newFolder(path);

                                    File shpFile = new File(path + File.separator + generateFileName);
                                    shpFile.createNewFile();

                                    OutputStream stream = new FileOutputStream(shpFile);
                                    FileInputStream fis = new FileInputStream(files[j]);

                                    JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

                                    fis.close();
                                    stream.close();
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            connection.close();
        }

        return nosList;
    }

}
