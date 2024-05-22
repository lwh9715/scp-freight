package com.scp.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.sys.SysAttachment;
import com.scp.model.sys.SysLog;
import com.scp.service.ServiceContext;

public class PDFUtil {

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        try {
            // File storefile = new File("D:\\工作文件\\需求\\解析pdf_上传附件\\SO 模板\\WHL 南沙.pdf");
            File storefile = new File("c:\\Users\\Administrator\\Desktop\\so解析文件\\ZIM成功解析.pdf");
            String linkid = "78263902274";
            String roleid = "6594862199";
            SysAttachment sysAttachment = new SysAttachment();
            SysLog sysLog = new SysLog();
            PDFUtil.parsePdf(storefile, linkid, roleid, sysAttachment, sysLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读PDF文件，使用了pdfbox开源项目
     *
     * @param fileName
     */
    public static void readPDF(String fileName) {
        try {
            File thisFile = new File(fileName);
            PDDocument pdfdocument = PDDocument.load(thisFile);
            // 新建一个PDF文本剥离器
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true); //sort设置为true 则按照行进行读取，默认是false
            // 从PDF文档对象中剥离文本
            String result = stripper.getText(pdfdocument);
            FileWriter fileWriter = new FileWriter(new File("pdf.txt"));
            fileWriter.write(result);
            fileWriter.flush();
            fileWriter.close();
            //System.out.println("PDF文件的文本内容如下：");
            //System.out.println(result);

            pdfdocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parsePdf(File thisfile, String jobid, String roleid, SysAttachment sysAttachment, SysLog sysLog) {
        PDDocument pdfdocument = null;
        try {
            if (thisfile != null && thisfile.exists() && !StrUtils.isNull(jobid) && !StrUtils.isNull(roleid)) {
                String surfix = thisfile.getAbsolutePath().substring(thisfile.getAbsolutePath().lastIndexOf(".") + 1);

                ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
                String sql = "SELECT id FROM sys_role where name='SO' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
                Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
                String soroleid = StrUtils.getMapVal(map, "id");
                if ("PDF".equals(surfix.toUpperCase()) && roleid.equals(soroleid)) {
                    String getshipsql = " isdelete = false AND jobid =" + jobid;
                    //解析pdf
                    pdfdocument = PDDocument.load(thisfile);
                    if (pdfdocument.isEncrypted()) {//加密
                        pdfdocument.decrypt(null);
                    }
                    PDFTextStripper stripper = new PDFTextStripper("UTF-8");   // 新建一个PDF文本剥离器
                    stripper.setSortByPosition(true); //sort设置为true 则按照行进行读取，默认是false
                    stripper.setWordSeparator("|");
                    String thisResult = stripper.getText(pdfdocument);  // 从PDF文档对象中剥离文本
                    sysAttachment.setRemarks("上传文件解析成功");
                }
            }
        } catch (Exception e) {
            sysAttachment.setRemarks("上传文件解析pdf失败,失败原因:" + e.getMessage().replaceAll("'", "\""));
            e.printStackTrace();
        } finally {
            if (pdfdocument != null) {
                try {
                    pdfdocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String getDataByCut(String thisResult, String Prefix, String suffix) {
        try {
            if (thisResult.contains(Prefix) && thisResult.contains(suffix)) {
                String thisStr = thisResult.substring(thisResult.indexOf(Prefix) + Prefix.length());
                thisStr = thisStr.substring(0, thisStr.indexOf(suffix));
                return thisStr;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    private static String getDataByLocation(String thisResult, int hang, int lie) {
        try {
            String returnText = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(thisResult.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (count == hang && !line.trim().equals("")) {
                    returnText = line.split("\\|")[lie - 1];
                }
            }
            return returnText;
        } catch (Exception e) {
            throw new RuntimeException("getDataByParameByPosition失败,参数thisResult_" + thisResult + ",hang_" + hang + ",lie_" + lie + "," + e.getMessage());
        }
    }

    private static String getDataByLocation(String thisResult
            , String locationStr, int hang, int lie) {
        if (!thisResult.contains(locationStr)) {
            return "";
        }

        int locationHang = getLocationHang(thisResult, locationStr);
        hang = locationHang + hang;
        try {
            String returnText = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(thisResult.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (count == hang && !line.trim().equals("")) {
                    String[] returnTextArray = line.split("\\|");
                    if (returnTextArray.length >= lie) {
                        returnText = returnTextArray[lie - 1];
                    } else {
                        returnText = "";
                    }
                }
            }
            return returnText;
        } catch (Exception e) {
            return "";
        }
    }

    private static int getLocationHang(String thisResult
            , String standardColumnStr) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(thisResult.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (!line.trim().equals("")) {
                    if (line.contains(standardColumnStr)) {
                        return count;
                    }
                }
            }
            throw new RuntimeException("getStandardColumnNumber失败,无法找到standardColumnStr,参数thisResult_" + thisResult + ",standardColumnStr_" + standardColumnStr);
        } catch (Exception e) {
            throw new RuntimeException("getStandardColumnNumber失败,运行异常,参数thisResult_" + thisResult + ",standardColumnStr_" + standardColumnStr + "," + e.getMessage());
        }
    }

    public static Timestamp stringToDate(String dateStr) {
        try {
            if (null == dateStr) {
                return null;
            } else {
                dateStr = dateStr.trim();
                int length = dateStr.length();

                if (!dateStr.contains(":")) {
                    if (length == "ddMMMyy".length()) {
                        return new Timestamp(new SimpleDateFormat("ddMMMyy", Locale.US).parse(dateStr).getTime());
                    }
                    if (length == "yyyy/M/d".length() && dateStr.contains("/")) {
                        return new Timestamp(new SimpleDateFormat("yyyy/M/dd").parse(dateStr).getTime());
                    }
                    if (length == "yyyy/MM/dd".length() && dateStr.split("/").length == 3
                            && dateStr.split("/")[0].length() == 4 && dateStr.split("/")[1].length() == 2 && dateStr.split("/")[2].length() == 2) {
                        return new Timestamp(new SimpleDateFormat("yyyy/MM/dd").parse(dateStr).getTime());
                    }
                    if (length == "dd/MM/yyyy".length() && dateStr.split("/").length == 3
                            && dateStr.split("/")[0].length() == 2 && dateStr.split("/")[1].length() == 2 && dateStr.split("/")[2].length() == 4) {
                        return new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(dateStr).getTime());
                    }
                    if (length == "yyyy-MM-dd".length() && dateStr.split("-").length == 3
                            && dateStr.split("-")[0].length() == 4 && dateStr.split("-")[1].length() == 2 && dateStr.split("-")[2].length() == 2) {
                        return new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime());
                    }
                    if (length == "dd-MMM-yyyy".length() && dateStr.split("-").length == 3
                            && dateStr.split("-")[0].length() == 2 && dateStr.split("-")[1].length() == 3 && dateStr.split("-")[2].length() == 4) {
                        return new Timestamp(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(dateStr).getTime());
                    }
                    if (length == "dd MMM yyyy".length() && dateStr.split(" ").length == 3
                            && dateStr.split(" ")[0].length() == 2 && dateStr.split(" ")[1].length() == 3 && dateStr.split(" ")[2].length() == 4) {
                        return new Timestamp(new SimpleDateFormat("dd MMM yyyy", Locale.US).parse(dateStr).getTime());
                    }
                } else {
                    if (dateStr.split(" ").length == 2) {
                        String dateStrPrefix = dateStr.split(" ")[0];
                        if (length == "ddMMM HH:mm".length() && dateStrPrefix.length() == 5) {
                            return new Timestamp(new SimpleDateFormat("ddMMM HH:mm", Locale.US).parse(dateStr).getTime());
                        }
                        if (length == "ddMMMyy HH:mm".length() && dateStrPrefix.length() == 7) {
                            return new Timestamp(new SimpleDateFormat("ddMMMyy HH:mm", Locale.US).parse(dateStr).getTime());
                        }
                        if (length == "yyyy-MM-dd HH:mm".length() && dateStrPrefix.split("-").length == 3
                                && dateStrPrefix.split("-")[0].length() == 4 && dateStrPrefix.split("-")[1].length() == 2 && dateStrPrefix.split("-")[2].length() == 2) {
                            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr).getTime());
                        }
                        if (length == "yyyy.MM.dd HH:mm".length() && dateStrPrefix.split(".").length == 3
                                && dateStrPrefix.split(".")[0].length() == 4 && dateStrPrefix.split(".")[1].length() == 2 && dateStrPrefix.split(".")[2].length() == 2) {
                            return new Timestamp(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse(dateStr).getTime());
                        }
                        if (length == "yyyy/MM/dd HH:mm".length() && dateStrPrefix.split("/").length == 3
                                && dateStrPrefix.split("/")[0].length() == 4 && dateStrPrefix.split("/")[1].length() == 2 && dateStrPrefix.split("/")[2].length() == 2) {
                            return new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(dateStr).getTime());
                        }
                        if (length == "dd-MMM-yy HH:mm".length() && dateStrPrefix.split("-").length == 3  //05-Dec-21 17:00
                                && dateStrPrefix.split("-")[0].length() == 2 && dateStrPrefix.split("-")[1].length() == 3 && dateStrPrefix.split("-")[2].length() == 2) {
                            String thisdateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yy", Locale.US).parse(dateStrPrefix)) + " " + dateStr.split(" ")[1];
                            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(thisdateStr).getTime());
                        }
                        if (length == "dd-MMM-yyyy HH:mm".length() && dateStrPrefix.split("-").length == 3
                                && dateStrPrefix.split("-")[0].length() == 2 && dateStrPrefix.split("-")[1].length() == 3 && dateStrPrefix.split("-")[2].length() == 4) {
                            String thisdateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(dateStr.split(" ")[0])) + " " + dateStr.split(" ")[1];
                            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(thisdateStr).getTime());
                        }
                        if (length == "dd-MMM-yyyy  HH:mm".length() && dateStrPrefix.split("-").length == 3
                                && dateStrPrefix.split("-")[0].length() == 2 && dateStrPrefix.split("-")[1].length() == 3 && dateStrPrefix.split("-")[2].length() == 4) {
                            String thisdateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(dateStr.split(" ")[0])) + " " + dateStr.split(" ")[1];
                            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(thisdateStr).getTime());
                        }
                        if (length == "dd/MM/yyyy HH:mm".length() && dateStrPrefix.split("/").length == 3
                                && dateStrPrefix.split("/")[0].length() == 2 && dateStrPrefix.split("/")[1].length() == 2 && dateStrPrefix.split("/")[2].length() == 4) {
                            return new Timestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateStr).getTime());
                        }
                        if (length == "dd-MMM-yyyy  HH:mm".length() && dateStrPrefix.split("-").length == 3
                                && dateStrPrefix.split("-")[0].length() == 2 && dateStrPrefix.split("-")[1].length() == 3 && dateStrPrefix.split("-")[2].length() == 4) {
                            String thisdateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(dateStr.split("  ")[0])) + " " + dateStr.split("  ")[1];
                            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(thisdateStr).getTime());
                        }
                        if (length == "yyyyMMdd HH:mm".length() && dateStrPrefix.length() == 8) {
                            return new Timestamp(new SimpleDateFormat("yyyyMMdd HH:mm").parse(dateStr).getTime());
                        }
                    } else if (dateStr.split(" ").length == 3) {
                        if (length == "yyyy/MM/dd hh:mm aa".length()) {
                            return new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm aa", Locale.ENGLISH).parse(dateStr).getTime());
                        }
                        if (length == "dd-MMM-yyyy  hh:mm".length()) {
                            return new Timestamp(new SimpleDateFormat("dd-MMM-yyyy  hh:mm", Locale.ENGLISH).parse(dateStr).getTime());
                        }
                    } else if (dateStr.split(" ").length == 4) {
                        if (length == "dd MMM yyyy HH:mm".length() && dateStr.contains(" ")) {
                            String thisdateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd MMM yyyy", Locale.US)
                                    .parse(dateStr.substring(0, dateStr.lastIndexOf(" ")))) + " " + dateStr.substring(dateStr.lastIndexOf(" ") + 1);
                            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(thisdateStr).getTime());
                        }
                        if (length == "dd-MMM-yyyy   hh:mm".length() || length == "dd-MMM-yyyy   h:mm".length()) {
                            return new Timestamp(new SimpleDateFormat("dd-MMM-yyyy   hh:mm", Locale.ENGLISH).parse(dateStr).getTime());
                        }
                    } else if (dateStr.split(" ").length == 5) {
                        if (length > 20) {  //Saturday, Nov 06, 2021 17:00
                            return new Timestamp(new SimpleDateFormat("E, MMM dd, yyyy hh:mm", Locale.ENGLISH).parse(dateStr).getTime());
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("stringToDate失败,参数dateStr_" + dateStr + "," + e.getMessage());
        }
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    public static String firstCharToUpperCase(String content) {
        if (!isEmpty(content)) {
            String tou = content.substring(0, 1);
            String wei = content.substring(1);
            content = tou.toUpperCase() + wei;
        }
        return content;
    }

    private static String converStr(String str) {
        String systemName = System.getProperty("os.name");
        if (systemName.indexOf("Windows") >= 0) {
            if ("\r\n".equals(str)) {
                return "\r\n";
            }
        } else if (systemName.indexOf("Linux") >= 0) {
            if ("\r\n".equals(str)) {
                return "\n";
            }
        }

        throw new RuntimeException("ConverStr失败,参数systemName_" + systemName + ",str_" + str);
    }

    /**
     * 功能 : 只复制source对象的非空属性到target对象上
     */
    public static void copyNoNullProperties(Object source, Object target) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
                        if (value != null && !"getId".equals(readMethod.getName())) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

}
