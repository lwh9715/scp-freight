package com.scp.view.module.commerce;

import com.alibaba.fastjson.JSONObject;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.utils.ConfigUtils;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author CIMC
 */
@WebServlet("/downloadPack")
@ManagedBean(name = "pages.module.commerce.downloadBean", scope = ManagedBeanScope.REQUEST)
public class DownloadBean extends HttpServlet {

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
            List<Map> mapList = this.findAttachFile(Long.valueOf(id));
            List<File> fileList = new ArrayList<File>();

            if (mapList.size() > 0) {

                for (Map m : mapList) {
                    String filepath;
                    String attachmenturl = ConfigUtils.findSysCfgVal("sys_attachment_url");

                    if (!StrUtils.isNull(attachmenturl)) {
                        filepath = attachmenturl + StrUtils.getMapVal(m, "id") + StrUtils.getMapVal(m, "filename");
                    } else {
                        filepath = AppUtils.getAttachFilePath() + StrUtils.getMapVal(m, "id") + StrUtils.getMapVal(m, "filename");
                    }
                    String fileName = StrUtils.getMapVal(m, "filename");
                    String contentType = StrUtils.getMapVal(m, "content_type");
                    File f = new File(filepath);

                    String overlaidUrl = AppUtils.getAttachFilePath() + StrUtils.getMapVal(m, "linkid") + StrUtils.getMapVal(m, "filename");

                    if (new File(overlaidUrl).exists()) {
                        f = new File(overlaidUrl);
                        fileList.add(f);
                    }
                    if (!f.exists()) {
                        filepath = AppUtils.getAttachFilePath() + File.separator + StrUtils.getMapVal(m, "id");
                        f = new File(filepath);
                        if (!f.exists()) {
                            File parentFile = new File(AppUtils.getAttachFilePath());
                            File[] tempFile = parentFile.listFiles();
                            for (int i = 0; i < tempFile.length; i++) {
                                if (tempFile[i].getName().startsWith(StrUtils.getMapVal(m, "id"))) {
                                    f = tempFile[i];
                                    fileList.add(f);
                                }
                            }
                            throw new Exception("Can't find the file:" + fileName + " path:" + filepath);
                        }
                    }
                    fileList.add(f);
                }

                zipOut = new ZipOutputStream(response.getOutputStream());

                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".zip\"");

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
        } catch (Exception soaExce) {
            soaExce.printStackTrace();
        } finally {
            zipOut.close();
        }
    }

    /**
     * 批量下載M單下H单的箱单发票
     *
     * @param attachFileId
     * @return
     */
    public List<Map> findAttachFile(Long attachFileId) {
        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
        String querySql =
                "\nSELECT " +
                        "\nfilepath " +
                        "\n, * " +
                        "\nFROM sys_attachment " +
                        "\nWHERE filename IS NOT NULL " +
                        "\nAND filename <> '' " +
                        "\nAND roleid = 997852212274 " +
                        "\nAND isdelete = FALSE " +
                        "\nAND linkid IN (SELECT id FROM fina_jobs WHERE parentid = " + attachFileId + " AND fina_jobs.isdelete = FALSE) " +
                        "\nORDER BY inputtime DESC;";
        List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(querySql);
        return mapList;
    }

}
