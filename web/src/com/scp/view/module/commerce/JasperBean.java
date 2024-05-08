package com.scp.view.module.commerce;

import com.alibaba.druid.pool.DruidDataSource;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.FileOperationUtil;
import com.scp.util.StrUtils;
import com.ufms.base.utils.AppUtil;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CIMC
 */
@WebServlet("/jasper")
@ManagedBean(name = "pages.module.commerce.jasperBean", scope = ManagedBeanScope.REQUEST)
public class JasperBean extends HttpServlet {

    /**
     * 从数据库中获取全部数据
     *
     * @throws Exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String id = req.getParameter("id");
            String code = req.getParameter("code");
            String type = req.getParameter("type");
            String date = req.getParameter("date");
            String salesCode = req.getParameter("salesCode");
            String bookingCode = req.getParameter("bookingCode");
            File file = new File(AppUtils.getReportFilePath());
            File[] listFiles = file.listFiles();

            DruidDataSource dataSource = (DruidDataSource) AppUtils.getBeanFromSpringIoc("dataSource");
            Connection connection = dataSource.getConnection();

            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].getAbsolutePath().endsWith("commerce")) {
                    if (listFiles[i].isDirectory()) {
                        File[] files = listFiles[i].listFiles();
                        for (int j = 0; j < files.length; j++) {
                            if (files[j].getAbsolutePath().endsWith(code + ".jasper")) {

                                ServletOutputStream outputStream = resp.getOutputStream();
                                FileInputStream fis = new FileInputStream(files[j]);

                                try {
                                    Map map = new HashMap(10);
                                    //打印pdf
                                    if ("pdf".equals(type)) {
                                        map.put("pid", Long.valueOf(id));
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                                    } else if ("downloadPdfFile".equals(type)) {
                                        //获取工作单号-组装文件名称
                                        String querySql = "SELECT nos AS name FROM bus_commerce WHERE jobid = " + id + " AND isdelete = FALSE LIMIT 1";
                                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                                        Map map1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

                                        //设置响应方式
                                        String generateFileName = StrUtils.getMapVal(map1, "name") + "-入仓核实单" + ".pdf";

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

                                        map.put("pid", Long.valueOf(id));
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

                                        stream.close();

                                    } else if ("downloadPdf".equals(type)) {
                                        map.put("pid", Long.valueOf(id));
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);

                                        String querySql = "SELECT (sonum || '-' || deliverynamec || '-' || bkpkgnum::BIGINT || '箱') AS name FROM bus_commerce WHERE jobid = " + id + " AND isdelete = FALSE LIMIT 1";
                                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                                        Map map1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

                                        //设置响应方式
                                        String generateFileName = StrUtils.getMapVal(map1, "name") + ".pdf";
                                        resp.setContentType("application/pdf");
                                        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(generateFileName, "utf8"));

                                        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                                    } else if ("downloadNosPdf".equals(type)) {
                                        map.put("pid", Long.valueOf(id));
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);

                                        String querySql = "SELECT (nos || '' || '箱唛') AS name FROM bus_commerce WHERE jobid = " + id + " AND isdelete = FALSE LIMIT 1";
                                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                                        Map map1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

                                        //设置响应方式
                                        String generateFileName = StrUtils.getMapVal(map1, "name") + ".pdf";
                                        resp.setContentType("application/pdf");
                                        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(generateFileName, "utf8"));

                                        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                                    } else if ("pdfPil".equals(type)) {
                                        map.put("pid", Long.valueOf(id));
                                        map.put("salesCode", salesCode);
                                        map.put("bookingCode", bookingCode);
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

                                    } else if ("billPdf".equals(type)) {
                                        String ids = id.replaceAll(",", "','").replaceAll("\"", "\'");
                                        map.put("pid", "'" + ids + "'");

                                        //标识已导出账单
                                        String usql = "UPDATE fina_arap SET blno = 11540072274 WHERE isdelete = FALSE AND corpid = 11540072274 AND id IN (" + id + ");";
                                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                                        daoIbatisTemplate.updateWithUserDefineSql(usql);

                                        if (!date.equals("")) {
                                            String[] strings = date.split(",");
                                            map.put("dateFrom", "'" + strings[0] + "'");
                                            map.put("dateTo", "'" + strings[1] + "'");
                                        }
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

                                    } else if ("billExcel".equals(type)) {
                                        String ids = id.replaceAll(",", "','").replaceAll("\"", "\'");
                                        map.put("pid", "'" + ids + "'");

                                        //标识已导出账单
                                        String usql = "UPDATE fina_arap SET blno = 11540072274 WHERE isdelete = FALSE AND corpid = 11540072274 AND id IN (" + id + ");";
                                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                                        daoIbatisTemplate.updateWithUserDefineSql(usql);

                                        if (!date.equals("")) {
                                            String[] strings = date.split(",");
                                            map.put("dateFrom", "'" + strings[0] + "'");
                                            map.put("dateTo", "'" + strings[1] + "'");
                                        }
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JRXlsxExporter exporter = new JRXlsxExporter();
                                        SimpleXlsxReportConfiguration conf = new SimpleXlsxReportConfiguration();
                                        //xlsx属性配置
                                        setExcelReportConfiguration(exporter, conf);

                                        //设置输入项
                                        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                                        exporter.setExporterInput(exporterInput);

                                        //设置输出项
                                        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(resp.getOutputStream());
                                        exporter.setExporterOutput(exporterOutput);

                                        //设置响应方式
                                        String generateFileName = "月结账单.xlsx";
                                        resp.setContentType("application/vnd.ms-excel");
                                        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(generateFileName, "utf8"));
                                        exporter.exportReport();
                                    } else if ("traceExcel".equals(type)) {
                                        String ids = id.replaceAll(",", "','").replaceAll("\"", "\'");
                                        map.put("pid", "'" + ids + "'");
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JRXlsxExporter exporter = new JRXlsxExporter();
                                        SimpleXlsxReportConfiguration conf = new SimpleXlsxReportConfiguration();
                                        //xlsx属性配置
                                        setExcelReportConfiguration(exporter, conf);

                                        //设置输入项
                                        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                                        exporter.setExporterInput(exporterInput);

                                        //设置输出项
                                        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(resp.getOutputStream());
                                        exporter.setExporterOutput(exporterOutput);

                                        //设置响应方式
                                        String generateFileName = "时效跟进表.xlsx";
                                        resp.setContentType("application/vnd.ms-excel");
                                        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(generateFileName, "utf8"));
                                        exporter.exportReport();
                                    } else if ("checkExcel".equals(type)) {
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JRXlsxExporter exporter = new JRXlsxExporter();
                                        SimpleXlsxReportConfiguration conf = new SimpleXlsxReportConfiguration();
                                        //xlsx属性配置
                                        setExcelReportConfiguration(exporter, conf);

                                        //设置输入项
                                        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                                        exporter.setExporterInput(exporterInput);

                                        //设置输出项
                                        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(resp.getOutputStream());
                                        exporter.setExporterOutput(exporterOutput);

                                        //设置响应方式
                                        String generateFileName = "M单查验数据表.xlsx";
                                        resp.setContentType("application/vnd.ms-excel");
                                        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(generateFileName, "utf8"));
                                        exporter.exportReport();
                                    } else if ("excel".equals(type)) {
                                        map.put("pid", Long.valueOf(id));
                                        JasperPrint jasperPrint = JasperFillManager.fillReport(fis, map, connection);
                                        JRXlsxExporter exporter = new JRXlsxExporter();
                                        SimpleXlsxReportConfiguration conf = new SimpleXlsxReportConfiguration();
                                        //xlsx属性配置
                                        setExcelReportConfiguration(exporter, conf);

                                        //设置输入项
                                        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
                                        exporter.setExporterInput(exporterInput);

                                        //设置输出项
                                        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(resp.getOutputStream());
                                        exporter.setExporterOutput(exporterOutput);

                                        String querySql = "SELECT nos AS name FROM fina_jobs WHERE id = " + id + " AND isdelete = FALSE LIMIT 1";
                                        DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
                                        Map map1 = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);

                                        //设置响应方式
                                        String generateFileName = StrUtils.getMapVal(map1, "name") + ".xlsx";
                                        resp.setContentType("application/vnd.ms-excel");
                                        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(generateFileName, "utf8"));
                                        exporter.exportReport();
                                    }

                                    break;
                                } catch (Exception e) {
                                    e.getMessage();
                                } finally {
                                    fis.close();
                                    connection.close();
                                    outputStream.close();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置单元格导出类型
     *
     * @param exporter
     * @param conf
     */
    private void setExcelReportConfiguration(JRAbstractExporter exporter, SimpleXlsReportConfiguration conf) {
        //设置检测单元格类型（否则数字类型字段导出为excel后变为文本类型）
        conf.setDetectCellType(true);
        exporter.setConfiguration(conf);
    }

}
