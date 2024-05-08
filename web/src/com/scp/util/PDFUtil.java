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
import com.scp.model.ship.BusShipBooking;
import com.scp.model.ship.BusShipping;
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
                    BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere(getshipsql);
                    sysLog.setLogdesc(sysLog.getLogdesc() + ",上传文件解析pdf开始,busShipping为" + busShipping.toString());

                    //解析pdf
                    pdfdocument = PDDocument.load(thisfile);
                    if (pdfdocument.isEncrypted()) {//加密
                        pdfdocument.decrypt(null);
                    }
                    PDFTextStripper stripper = new PDFTextStripper("UTF-8");   // 新建一个PDF文本剥离器
                    stripper.setSortByPosition(true); //sort设置为true 则按照行进行读取，默认是false
                    stripper.setWordSeparator("|");
                    String thisResult = stripper.getText(pdfdocument);  // 从PDF文档对象中剥离文本
                    //System.out.println(thisResult);

                    setUpdateBusShipingData(thisResult, busShipping, sysLog);
                    sysAttachment.setRemarks("上传文件解析成功");
                    sysLog.setLogdesc(sysLog.getLogdesc() + ",上传文件解析pdf结束,解析成功,thisResult为" + thisResult + ",busShipping为" + busShipping.toString());
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

    private static void setUpdateBusShipingData(String thisResult, BusShipping busShipping, SysLog sysLog) {
        String thisPdfType = "";
        if (thisResult.contains("CMA CGM SHENZHEN")) {   //ANL
            thisPdfType = "ANL";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Shipping Order No.: ", "|Electronic Ref"));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "Booking Date: ", converStr("\r\n")));   //SO日期
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split(" / ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split(" / ")[1]);   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD:|", converStr("\r\n")));  //etd
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM Cut-Off Date:|", converStr("\r\n")));   //截VGM时间
        } else if (thisResult.contains("CMA CGM Shenzhen")) {  //CMA
            thisPdfType = "CMA";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Shipping Order No.: ", "|Electronic Ref"));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "Booking Date: ", converStr("\r\n")));   //SO日期
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split(" / ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split(" / ")[1]);   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD:|", converStr("\r\n")));  //etd
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM Cut-Off Date:|", converStr("\r\n")));   //截VGM时间
        } else if (thisResult.contains("CMA CGM GUANGZHOU")) {  //CNC
            thisPdfType = "CNC";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Shipping Order No.: ", "|Electronic Ref"));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "Booking Date: ", converStr("\r\n")));   //SO日期
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split(" / ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split(" / ")[1]);   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD:|", converStr("\r\n")));  //etd
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM Cut-Off Date:|", converStr("\r\n")));   //截VGM时间
        } else if (thisResult.contains("http://elines.coscoshipping.com")) { //中远海运
            if (thisResult.contains("Container Details & Intended Schedule Plan")) {  //COSCO 深圳    中远海运
                thisPdfType = "COSCO 深圳    中远海运";
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "订舱号：", converStr("\r\n")));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "Vessel Name", 1, 2));   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "Voyage", 1, 3));   //船次
                setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, "ETD", 1, 5));  //ETD    etd
                setBusShippingByData(sysLog, busShipping, "eta", getDataByLocation(thisResult, "ETA", 1, 7));   //ETA    eta
                setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM截止时间：|", converStr("\r\n")));   //截VGM时间 vgmdate
            } else if (thisResult.contains("@cosfretj.com")) {  //COSCO 天津    中远海运
                thisPdfType = "COSCO 天津    中远海运";
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "订舱号：", converStr("\r\n")));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "船名/航次：", converStr("\r\n")).split("   ")[0]);   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "船名/航次：", converStr("\r\n")).split("   ")[1]);   //船次
                setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, "ETD", 1, 4));  //ETD    etd
                setBusShippingByData(sysLog, busShipping, "eta", getDataByLocation(thisResult, "ETA", 1, 6));   //ETA    eta
            } else if (thisResult.contains("@coslina")) {  //COSCO 香港    中远海运
                thisPdfType = "COSCO 香港    中远海运";
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "BOOKING NO.订舱号：", "      提箱校验码"));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "VESSEL船名：", "                  VOYAGE航次"));   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "VOYAGE航次：", converStr("\r\n")));   //船次
                setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD(POL)：", "               ETA(POD)"));  //ETD
                setBusShippingByData(sysLog, busShipping, "eta", getDataByCut(thisResult, "ETA(POD)：", converStr("\r\n")));   //ETA    ETA(POD)
                setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM CUT OFF: UPTO：", converStr("\r\n")));   //截VGM时间
            } else {
                throw new RuntimeException("暂时无法解析此类型pdf文件");
            }
        } else if (thisResult.contains("https://www.ewinlu.com")) {  //CUL
            thisPdfType = "CUL";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Booking No : ", "|Booking Ref. No."));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "Booking Date : ", converStr("\r\n")));  //SO
            String VESSEL_VOYAGE = getDataByCut(thisResult, "Trunk Vessel :|", "|ETA/ETD");
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "|ETA/ETD   : ", converStr("\r\n")).split("/")[0]);  //ETD
            setBusShippingByData(sysLog, busShipping, "eta", getDataByCut(thisResult, "|ETA/ETD   : ", converStr("\r\n")).split("/")[1]);   //ETA    ETA(POD)
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM Cut-off :|", converStr("\r\n")));   //截VGM时间
        } else if (thisResult.contains("(http://www.evergreen-line.com/)")) {  //EMC    evergreen
            thisPdfType = "EMC    evergreen";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "(订舱号码):|", "    APPLICATION NO"));  //SO
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "订舱号码", 1, 2);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD DATE        :", converStr("\r\n")));  //ETD    etd
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM CUT OFF via EDI/WEB/APP :", converStr("\r\n")));   //截VGM时间
        } else if (thisResult.contains("http://www.oocl.com")) {    //HMM o/OOCL/2670405072
            thisPdfType = "HMM o/OOCL/2670405072";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "BOOKING NUMBER|:|", converStr("\r\n")));  //SO
            String VESSEL_VOYAGE = getDataByCut(thisResult, "INTENDED VESSEL/VOYAGE :|", converStr("\r\n"));
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD|:|", converStr("\r\n")));  //ETD    etd
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "INTENDED VGM CUT-OFF|:|", converStr("\r\n")));   //截VGM时间    INTENDED VGM CUT-OFF
        } else if (thisResult.contains("Thank you for booking with HMM,")) {  //HMM
            thisPdfType = "HMM";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Booking Number|:|", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "Booking Date|:|", "|VGM PIN"));   //SO日期
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Shipping Information", 1, 3);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD|:|", converStr("\r\n")));  //ETD    etd
            if (thisResult.contains("(DEM.Free) Gate-in (DC)")) {
                setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByLocation(thisResult, 42, 2));   //截VGM时间
            } else {
                setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByLocation(thisResult, 41, 4));   //截VGM时间
            }
        } else if (thisResult.contains("HAPAG-LLOYD (CHINA) SHIPPING LTD")) {  //HPL
            thisPdfType = "HPL";
            if (thisResult.contains("Your Reference")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Our Reference:|", "|Your Reference"));  //SO
                setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "Booking Date:|", converStr("\r\n")));   //SO日期
            } else {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, 4, 2));  //SO
                setBusShippingByData(sysLog, busShipping, "sodate", getDataByLocation(thisResult, 14, 4));   //SO日期
            }
        } else if (thisResult.contains("深圳市運達國際船舶代理有限公司廣州分公司")) {  //IAL    深圳市運達國際船舶代理有限公司廣州分公司
            thisPdfType = "IAL";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Book No|", converStr("\r\n")));  //SO
            String VESSEL_VOYAGE = getDataByCut(thisResult, "航名/航次|", "|CLOSE DATE");
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.split("/ ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.split("/ ")[1]);   //船次
        } else if (thisResult.contains("@EKMTC.COM")) {  //KMTC
            thisPdfType = "KMTC";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "(订舱号)|", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "(船名/航次)", 1, 1).split("/ ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "(船名/航次)", 1, 1).split("/ ")[1]);   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, "ETD(预计开船日)", 2, 1));  //ETD
            setBusShippingByData(sysLog, busShipping, "eta", getDataByLocation(thisResult, "ETA(预计到卸货港日)", 2, 2));   //ETA    ETA(POD)
        } else if (thisResult.contains("https://www.sealandmaersk.com")) {  //MCC
            thisPdfType = "MCC";
            if (thisResult.contains("Sealand Spot")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Booking No.:|", "|Sealand Spot"));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "|Vessel|", 1, 2));   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "|Voy No.|", 1, 3));   //船次
                setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, "|ETD|", 1, 4));  //ETD
                setBusShippingByData(sysLog, busShipping, "eta", getDataByLocation(thisResult, "|ETA", 1, 5));   //ETA    ETA(POD)
            } else {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, 61, 2));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, 20, 3));   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, 20, 4));   //船次
                setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, 20, 5));  //ETD
                setBusShippingByData(sysLog, busShipping, "eta", getDataByLocation(thisResult, 20, 6));   //ETA    ETA(POD)
            }
        } else if (thisResult.contains("MEDITERRANEAN SHIPPING COMPANY S.A.")) {  //MSC    地中海运输公司
            thisPdfType = "MSC";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "订舱号:|", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "船名/航次", 0, 2).split("   /   ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "船名/航次", 0, 2).split("   /   ")[1]);   //船次
        } else if (thisResult.contains("<http://www.maersk.com>")) {  //MSK    马士基
            thisPdfType = "MSK";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, 2, 2));  //Booking No
        } else if (thisResult.contains("www.one-line.com")) {  //ONE
            thisPdfType = "ONE";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Booking No : ", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "BKG Date : ", converStr("\r\n")));   //SO日期
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Vessel Voyage Dir", 0, 2);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByLocation(thisResult, "VGM Cut-off", 0, 2));   //截VGM时间
        } else if (thisResult.contains("http://dpay.cmclink.com")) {  //PIL
            thisPdfType = "PIL";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Booking No :|", converStr("\r\n")));  //SO     Booking No :
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "BKG Date :|", converStr("\r\n")));   //SO日期    BKG Date :
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Vessel Voyage Dir", 0, 2);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, "Vessel Voyage Dir", 1, 1));  //ETD
        } else if (thisResult.contains("REGIONAL CONTAINER LINES SHIPPING") || thisResult.contains("We are pleased to confirm the booking as below")
                || thisResult.contains("宏海箱运船务有限公司广州分公司放柜纸")) {  //RCL
            thisPdfType = "RCL";
            if (thisResult.contains("REGIONAL CONTAINER LINES SHIPPING")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "B/L NO.|", converStr("\r\n")));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "Vessel|Voyage No", 1, 1));   //船名       Vessel Voyage Dir :
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "Vessel|Voyage No", 1, 2));   //船次
            } else if (thisResult.contains("We are pleased to confirm the booking as below")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "Booking NO.:", 0, 2));  //SO
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "Vessel:", 0, 2));   //船名       Vessel Voyage Dir :
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "Vessel:", 0, 4));   //船次
            } else if (thisResult.contains("宏海箱运船务有限公司广州分公司放柜纸")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "BKG NO.", 0, 2));  //SO
                String VESSEL_VOYAGE = getDataByLocation(thisResult, "船名/航次:", 0, 2);
                setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
                setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            }
        } else if (thisResult.contains("@tslines.com.cn")) {  //TSL
            thisPdfType = "TSL";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "BOOKING NO.:", 0, 2));  //SO
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "|VESSEL|", 1, 3));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "|VOYAGE|", 1, 4));   //船次
        } else if (thisResult.contains("SM LINE")) {  //SML
            thisPdfType = "SML";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Booking No : ", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "BKG Date : ", converStr("\r\n")));   //SO日期
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Vessel Voyage Dir", 0, 2);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
        } else if (thisResult.contains("上海联骏国际船舶代理有限公司广州分公司") || thisResult.contains("上海聯駿國際船舶代理有限公司廣州分公司")
                || thisResult.contains("WAN HAI LINES (H.K.) LTD.") || thisResult.contains("上海聯駿國際船舶代理有限公司 深圳分公司")) {  //WHL
            thisPdfType = "WHL";
            if (thisResult.contains("上海联骏国际船舶代理有限公司广州分公司")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "S/O NO", 0, 2));  //SO     Booking No :
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "1st VSL/VOY   ", converStr("\r\n")).split("/ ")[0]);   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "1st VSL/VOY   ", converStr("\r\n")).split("/ ")[1]);   //船次
            } else if (thisResult.contains("上海聯駿國際船舶代理有限公司廣州分公司")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "出口　S/O NO", 0, 1).split(":")[1]
                        .replaceAll(" ", "").replaceAll("_", ""));  //SO
                String VESSEL_VOYAGE = getDataByLocation(thisResult, "船名航次:", 2, 1);
                setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf("/")));   //船名       Vessel Voyage Dir :
                setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf("/") + 2));   //船次
            } else if (thisResult.contains("WAN HAI LINES (H.K.) LTD.")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, 6, 2));  //SO
                String VESSEL_VOYAGE = getDataByLocation(thisResult, 9, 1);
                setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.split(" / ")[0]);   //船名       Vessel Voyage Dir :
                setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.split(" / ")[1]);   //船次
            } else if (thisResult.contains("上海聯駿國際船舶代理有限公司 深圳分公司")) {
                setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "Book No|", converStr("\r\n")));  //SO     Booking No :
                setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "航名/航次", 0, 2).split("/ ")[0]);   //船名
                setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "航名/航次", 0, 2).split("/ ")[1]);   //船次
                setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByLocation(thisResult, 14, 2));   //截VGM时间
            }
        } else if (thisResult.contains("https://o-www.yangming.com")) {  //YML
            thisPdfType = "YML";
            // setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "S/O.NO", 0, 4));  //SO
            // setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "大船", 0, 2).split(" - ")[0]);   //船名
            // setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "大船", 0, 2).split(" - ")[1]);   //船次
            // setBusShippingByData(sysLog, busShipping, "etd", getDataByCut(thisResult, "ETD:", "截关日"));  //ETD
            setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, "S/O.NO", 0, 4));  //SO
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByLocation(thisResult, "Vessel/Voyage", 0, 2).split(" - ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByLocation(thisResult, "Vessel/Voyage", 0, 2).split(" - ")[1]);   //船次
        } else if (thisResult.contains("HTTPS://WWW.GSLLTD.COM.HK/BOOKING-CLAUSE.PHP")) {  //ZIM(金星）
            thisPdfType = "ZIM(金星）";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "]-", converStr("\r\n")));  //SO
            String VESSEL_VOYAGE = getDataByCut(thisResult, "Vessel/Voyage:|", "|Vessel Code");
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
        } else if (thisResult.contains("HTTPS://WWW.ZIM.COM/HELP/APPENDIX-I-EXPEDITED-SERVICE-GUARANTEE")) {  //ZIM
            thisPdfType = "ZIM";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "] - ", converStr("\r\n")));  //SO
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Vessel/Voyage", 0, 2);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "etd", getDataByLocation(thisResult, "ETD:", 0, 2));  //ETD
        } else if (thisResult.contains("HTTP://EPORT.SCCTCN.COM")) {  //箱东 ECON
            thisPdfType = "箱东 ECON";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByCut(thisResult, "SHIPPING ORDER NO.:   ", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "sodate", getDataByCut(thisResult, "BOOKING DATE:    ", converStr("\r\n")));  //SO
            setBusShippingByData(sysLog, busShipping, "vessel", getDataByCut(thisResult, "VESSEL/VOYAGE:  ", converStr("\r\n")).split(" / ")[0]);   //船名
            setBusShippingByData(sysLog, busShipping, "voyage", getDataByCut(thisResult, "VESSEL/VOYAGE:  ", converStr("\r\n")).split(" / ")[1]);   //船次
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByCut(thisResult, "VGM CUT-OFF DATE:  ", converStr("\r\n")));   //截VGM时间
        } else if (thisResult.contains("？？？")) {  //长锦     无法识别，全是图片
        } else if (thisResult.contains("？？？")) {  //ASL     无法识别，未知字体字符集
        } else if (thisResult.contains("http://www.sitcline.com")) {  //SITC
            thisPdfType = "SITC";
            setBusShippingByData(sysLog, busShipping, "sono", getDataByLocation(thisResult, 2, 2));  //SO
            String VESSEL_VOYAGE = getDataByLocation(thisResult, 6, 2);
            setBusShippingByData(sysLog, busShipping, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.indexOf(" ")));   //船名       Vessel Voyage Dir :
            setBusShippingByData(sysLog, busShipping, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.indexOf(" ") + 1));   //船次
            setBusShippingByData(sysLog, busShipping, "vgmdate", getDataByLocation(thisResult, 13, 3));   //截VGM时间
        } else {
            throw new RuntimeException("暂时无法解析此类型pdf文件");
        }
        sysLog.setLogdesc(sysLog.getLogdesc() + ",setUpdateBusShipingData获取pdf船公司类型成功,船公司类型为" + thisPdfType);
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
            // throw new RuntimeException("getDataByParame失败,参数thisResult_" + thisResult + ",Prefix_" + Prefix + ",suffix_" + suffix + "," + e.getMessage());
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
            // throw new RuntimeException("getDataByParameByPosition失败,参数thisResult_" + thisResult + ",hang_" + hang + ",lie_" + lie + "," + e.getMessage());
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

    private static void setBusShippingByData(SysLog sysLog, BusShipping busShipping
            , String attribute, String attributeValue) {
        try {
            attributeValue = attributeValue.replaceAll("\\|", "").trim();

            Class clazz = busShipping.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field f = fields[j];
                f.setAccessible(true);
                if (f.getName().equals(attribute)) {
                    Method method = clazz.getDeclaredMethod("get" + firstCharToUpperCase(attribute), new Class[]{});
                    String typename = method.getReturnType().getName();
                    if ("java.lang.String".equals(typename)) {
                        if (!isEmpty(attributeValue)) {
                            if ("sono".equals(attribute) && !StrUtils.isNull(busShipping.getSono())) {    //有一个单，多个so的情况,再上传的时候，so追加进去,号码不存在的，后面追加
                                if (Arrays.asList(busShipping.getSono().split(",")).contains(attributeValue)) {
                                    break;
                                } else {
                                    f.set(busShipping, busShipping.getSono() + "," + attributeValue);
                                }
                            } else {
                                f.set(busShipping, attributeValue);
                            }
                        }
                    } else if ("java.util.Date".equals(typename)) {
                        if ("etd".equals(attribute) && busShipping.getEtd() != null) {    //如果单子里面如果有值就不赋值,etd为空则赋值，
                            break;
                        }

                        Timestamp timestamp = stringToDate(attributeValue);
                        if (!isEmpty(timestamp)) {
                            f.set(busShipping, timestamp);
                        }
                    }

                    Object o = f.get(busShipping);
                    if (!isEmpty(o)) {
                        String sql = "";
                        try {
                            sql = "UPDATE bus_shipping SET " + attribute + " = '" + o + "' WHERE id = " + busShipping.getId() + ";";
                            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
                            daoIbatisTemplate.updateWithUserDefineSql(sql);
                        } catch (Exception e) {
                            sysLog.setLogdesc(sysLog.getLogdesc() + ",setUpdateBusShippingByparame失败,sql为" + sql + ",失败原因为" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("setUpdateBusShippingByparame失败,参数busShipping_" + busShipping.toString() + ",attribute_" + attribute + ",attributeValue_" + attributeValue + "," + e.getMessage());
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


    //邮件解析pdf生成so数据
    public static void parsePdfToSo(File thisfile, BusShipBooking busShipBooking, String roleid, SysLog sysLog) {
        PDDocument pdfdocument = null;
        try {
            if (thisfile != null && thisfile.exists() && !StrUtils.isNull(roleid)) {
                String surfix = thisfile.getAbsolutePath().substring(thisfile.getAbsolutePath().lastIndexOf(".") + 1);

                ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
                String sql = "SELECT id FROM sys_role where name='SO' AND isdelete = FALSE AND roletype = 'F' LIMIT 1";
                Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
                String soroleid = StrUtils.getMapVal(map, "id");
                if ("PDF".equals(surfix.toUpperCase()) && roleid.equals(soroleid)) {
                    sysLog.setLogdesc(sysLog.getLogdesc() + ",上传文件解析pdf开始");

                    //解析pdf
                    pdfdocument = PDDocument.load(thisfile);
                    if (pdfdocument.isEncrypted()) {//加密
                        pdfdocument.decrypt(null);
                    }
                    PDFTextStripper stripper = new PDFTextStripper("UTF-8");   // 新建一个PDF文本剥离器
                    stripper.setSortByPosition(true); //sort设置为true 则按照行进行读取，默认是false
                    stripper.setWordSeparator("|");
                    String thisResult = stripper.getText(pdfdocument);  // 从PDF文档对象中剥离文本

                    setUpdateBusShipBookingData(thisResult, busShipBooking, sysLog, serviceContext);
                    sysLog.setLogdesc(sysLog.getLogdesc() + ",上传文件解析pdf结束,解析成功,thisResult为" + thisResult + ",busShipBooking为" + busShipBooking.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sysLog.setLogdesc(sysLog.getLogdesc() + ",parsePdfToSo失败,失败原因为" + e.getMessage());
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

    private static void setUpdateBusShipBookingData(String thisResult, BusShipBooking busShipBooking, SysLog sysLog, ServiceContext serviceContext) throws InvocationTargetException, IllegalAccessException {
        String thisPdfType = "";
        if (thisResult.contains("http://dpay.cmclink.com") || thisResult.contains("@can.pilship.com")) {  //PIL
            thisPdfType = "PIL";
            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByCut(thisResult, "Booking No :|", converStr("\r\n")));  //SO     Booking No :
            setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByCut(thisResult, "BKG Date :|", converStr("\r\n")));   //SO日期    BKG Date :

            //船公司
            try {
                String carrierabbr = "PIL";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Vessel Voyage Dir", 0, 2);
            setBusShipBookingByData(sysLog, busShipBooking, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次

            setBusShipBookingByData(sysLog, busShipBooking, "masterbillno", getDataByLocation(thisResult, "Bill of Lading", 0, 4));  //提单号

            //ETD
            if (thisResult.contains("E|T|D|   :|")) {
                String etdStr = getDataByCut(thisResult, "E|T|D|   :|", converStr("\r\n")).substring(0, 7);
                setBusShipBookingByData(sysLog, busShipBooking, "etd", etdStr);
            } else {
                setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByLocation(thisResult, "Vessel Voyage Dir", 1, 1));
            }

            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "Port of Discharge", 0, 2));  //卸货港
            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "Port of Loading", 0, 4));  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByLocation(thisResult, "Commodity", 0, 2));  //货品英文名
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByLocation(thisResult, "Doc Cut-off", 0, 2));  //SI
            setBusShipBookingByData(sysLog, busShipBooking, "clsbig", getDataByLocation(thisResult, "Full Return CY Cut-off", 0, 4));  //大船截关日期
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", getDataByCut(thisResult, "VGM Cut-off                       :", converStr("\r\n")));  //vgm

            String EQT_QTY = getDataByLocation(thisResult, "EQ Type/Q'ty", 0, 2);
            String EQT = EQT_QTY.split("-")[0];
            String QTY = EQT_QTY.split("-")[1];
            setBusShipBookingByData(sysLog, busShipBooking, "cntype1", EQT);  //箱型
            setBusShipBookingByData(sysLog, busShipBooking, "piece1", QTY);  //箱量
        } else if (thisResult.contains("(http://www.evergreen-line.com/)")) {  //EMC    evergreen
            thisPdfType = "EMC    evergreen";
            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByCut(thisResult, "(订舱号码):|", "    APPLICATION NO"));  //SO
            setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByCut(thisResult, "DATE: ", " "));   //SO日期    BKG Date :
            String VESSEL_VOYAGE = getDataByLocation(thisResult, "VESSEL/VOYAGE", 0, 2);
            setBusShipBookingByData(sysLog, busShipBooking, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次

            //船公司
            try {
                String carrierabbr = "EMC";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByCut(thisResult, "PORT OF LOADING", ",").split(":")[1].trim());  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "clsbig", getDataByCut(thisResult, "关时间       :", converStr("\r\n")));  //大船截关日期
            setBusShipBookingByData(sysLog, busShipBooking, "cv", getDataByCut(thisResult, "关放行条截止时间:", converStr("\r\n")));  //cv
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", getDataByCut(thisResult, "VGM CUT OFF via EDI/WEB/APP :", converStr("\r\n")));   //vgm
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByCut(thisResult, "单补料截止时间 :", converStr("\r\n")));  //SI
            setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByCut(thisResult, "ETD DATE        :", converStr("\r\n")));  //ETD
            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByCut(thisResult, "PORT OF DISCHARGE (卸货港):", ","));  //卸货港
            setBusShipBookingByData(sysLog, busShipBooking, "eta", getDataByLocation(thisResult, "PLACE OF DELIVERY", 1, 1).split(":")[1].split(" ")[0]);//
            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByCut(thisResult, "COMMODITY(货物品名)  :", converStr("\r\n")));  //货品英文名
            String EQT_QTY = getDataByLocation(thisResult, "QTY_TYPE", 2, 1).split("              ")[0];
            String EQT = EQT_QTY.split("  /")[1];
            String QTY = EQT_QTY.split("  /")[0];
            setBusShipBookingByData(sysLog, busShipBooking, "cntype1", EQT);  //箱型
            setBusShipBookingByData(sysLog, busShipBooking, "piece1", QTY);  //箱量

            setBusShipBookingByData(sysLog, busShipBooking, "masterbillno", getDataByCut(thisResult, "协约号               :", converStr("\r\n")));  //协约号
        } else if ((thisResult.contains("HTTPS://WWW.GSLLTD.COM.HK/BOOKING-CLAUSE.PHP") || thisResult.contains("We thank you for following booking:"))
                && thisResult.contains("GOSU")) {  //GSL(金星）
            thisPdfType = "GSL(金星）";
            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByCut(thisResult, "]-", converStr("\r\n")));  //SO
            String VESSEL_VOYAGE = getDataByCut(thisResult, "Vessel/Voyage:|", "|Vessel Code");
            setBusShipBookingByData(sysLog, busShipBooking, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次

            //船公司
            try {
                String carrierabbr = "GSL";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "Port of Loading", 0, 2).split(" ")[0]);  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "Port of Discharge", 0, 2).split(" ")[0]);  //卸货港
            setBusShipBookingByData(sysLog, busShipBooking, "pod", getDataByLocation(thisResult, "Final Destination", 0, 2).split(" ")[0]);  //目的港

            if (thisResult.contains("Sailing Date")) {
                setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByLocation(thisResult, "Sailing Date", 0, 2));  //ETD
                setBusShipBookingByData(sysLog, busShipBooking, "clsbig", getDataByLocation(thisResult, "Sailing Date", 1, 2));  //大船截关日期
                String vgmStr = getDataByLocation(thisResult, "VGM Closing Date", 0, 2) + " " + getDataByLocation(thisResult, "VGM Closing Date", 1, 2);
                setBusShipBookingByData(sysLog, busShipBooking, "vgm", vgmStr);   //vgm

                setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByLocation(thisResult, "Remarks - Unit Reference for Empty Pick-up", 1, 1));  //SI
                setBusShipBookingByData(sysLog, busShipBooking, "cy", getDataByLocation(thisResult, "Remarks - Unit Reference for Empty Pick-up", 2, 2));  //cy
                setBusShipBookingByData(sysLog, busShipBooking, "cv", getDataByLocation(thisResult, "Remarks - Unit Reference for Empty Pick-up", 3, 1));  //cv
            } else if (thisResult.contains("ETD")) {
                setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByLocation(thisResult, "ETD", 0, 2));  //ETD

                setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByLocation(thisResult, "Remarks - Unit Reference for Empty Pick-up", 2, 1));  //SI
                String vgmStr = "";
                if (thisResult.contains("VGM|Closing|Date")) {
                    vgmStr = getDataByLocation(thisResult, "VGM|Closing|Date:", 0, 4) + " " + getDataByLocation(thisResult, "VGM|Closing|Date:", 0, 5);
                } else if (thisResult.contains("VGM Closing Date")) {
                    vgmStr = getDataByLocation(thisResult, "VGM Closing Date", 0, 2) + " " + getDataByLocation(thisResult, "VGM Closing Date", 0, 3);
                }
                setBusShipBookingByData(sysLog, busShipBooking, "vgm", vgmStr);   //vgm
            }

            String EQT_QTY = "";
            if (thisResult.contains("B ooking details")) {
                EQT_QTY = getDataByLocation(thisResult, "B ooking details", 1, 1);
            } else if (thisResult.contains("Booking details")) {
                EQT_QTY = getDataByLocation(thisResult, "Booking details", 1, 1);
            }
            if (!isEmpty(EQT_QTY)) {
                String QTY = EQT_QTY.split("X")[0];
                String EQT = EQT_QTY.split("X")[1];
                setBusShipBookingByData(sysLog, busShipBooking, "piece1", QTY);  //箱量
                setBusShipBookingByData(sysLog, busShipBooking, "cntype1", EQT);  //箱型
            }
            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByCut(thisResult, "Commodity: ", converStr("\r\n")));  //货品英文名
        } else if (thisResult.contains("Thank you for booking with HMM,")) {  //HMM
            thisPdfType = "HMM";
            //船公司
            try {
                String carrierabbr = "HMM";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByCut(thisResult, "Booking Number|:|", converStr("\r\n")));  //SO
            setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByCut(thisResult, "Booking Date|:|", "|VGM PIN"));   //SO日期

            if (thisResult.contains("Contract")) {
                setBusShipBookingByData(sysLog, busShipBooking, "contractnumber", getDataByLocation(thisResult, "Contract", 0, 3).split(" : ")[1]);  //协约号
            } else if (thisResult.contains("S/C")) {
                setBusShipBookingByData(sysLog, busShipBooking, "contractnumber", getDataByLocation(thisResult, "S/C", 0, 3).split(" : ")[1]);  //协约号
            }

            String VESSEL_VOYAGE = getDataByLocation(thisResult, "Shipping Information", 1, 3);
            setBusShipBookingByData(sysLog, busShipBooking, "vessel", VESSEL_VOYAGE.substring(0, VESSEL_VOYAGE.lastIndexOf(" ")));   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", VESSEL_VOYAGE.substring(VESSEL_VOYAGE.lastIndexOf(" ") + 1));   //船次
            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "Port of Loading", 0, 3).split(",")[0]);  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "Port of Discharging", 0, 3).split(", ")[0]);  //卸货港

            String EQT = getDataByLocation(thisResult, "Dry Container", 0, 6);
            String QTY = getDataByLocation(thisResult, "Dry Container", 0, 9);
            setBusShipBookingByData(sysLog, busShipBooking, "cntype1", EQT);  //箱型
            setBusShipBookingByData(sysLog, busShipBooking, "piece1", QTY);  //箱量
            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByLocation(thisResult, "Commodity Desc", 0, 6));  //货品英文名

            setBusShipBookingByData(sysLog, busShipBooking, "cy", getDataByLocation(thisResult, "Cargo Cut-off (Terminal Facility) (DC)", 0, 2));  //cy
            setBusShipBookingByData(sysLog, busShipBooking, "clsbig", getDataByLocation(thisResult, "Cargo Cut-off (Return Facility) (DC)", 0, 4));  //大船截关日期
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByLocation(thisResult, "SI General Cut-off", 0, 2));  //SI
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", getDataByLocation(thisResult, "VGM Cut-off", 0, 4));   //vgm

            if (getDataByLocation(thisResult, "Booking Notice", 0, 2).contains("Cancelled")) {
                busShipBooking.setBookstate("1");
            }
        } else if (thisResult.contains("<http://www.maersk.com>")) {  //MAERSK
            thisPdfType = "MAERSK";
            //船公司
            try {
                String carrierabbr = "MAERSK";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByLocation(thisResult, "Booking No.:", 0, 2));  //SO
            setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByLocation(thisResult, "Print Date:", 0, 4).split("UTC")[0]);   //SO日期

            setBusShipBookingByData(sysLog, busShipBooking, "userbook", getDataByLocation(thisResult, "Contact Name", 0, 2));   //订舱负责人
            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "From:", 0, 4).split(",")[0]);  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "To:", 0, 3).split(",")[0]);  //卸货港
            setBusShipBookingByData(sysLog, busShipBooking, "contractnumber", getDataByLocation(thisResult, "Service Contract", 0, 2));  //协约号
            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByLocation(thisResult, "Customer Commodity:", 0, 4));  //货品英文名

            String QTY = getDataByLocation(thisResult, "Quantity", 1, 1);
            String EQT = getDataByLocation(thisResult, "Quantity", 1, 2) + " " + getDataByLocation(thisResult, "Quantity", 1, 3);
            setBusShipBookingByData(sysLog, busShipBooking, "piece1", QTY);  //箱量
            setBusShipBookingByData(sysLog, busShipBooking, "cntype1", EQT);  //箱型
            // setBusShipBookingByData(sysLog, busShipBooking, "grswgt1", getDataByLocation(thisResult, "Quantity", 1, 4));  //重量

            setBusShipBookingByData(sysLog, busShipBooking, "vessel", getDataByLocation(thisResult, "|Vessel|", 1, 2));   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", getDataByLocation(thisResult, "|Voy No.|", 1, 3));   //船次

            String etdStr = getDataByLocation(thisResult, "|ETD|", 1, 4);
            if (etdStr.length() != 10) {
                etdStr = getDataByLocation(thisResult, "|ETD|", 1, 5);
            }
            setBusShipBookingByData(sysLog, busShipBooking, "etd", etdStr);  //ETD
            setBusShipBookingByData(sysLog, busShipBooking, "eta", getDataByLocation(thisResult, "If you would like", -1, 5));   //ETA

            setBusShipBookingByData(sysLog, busShipBooking, "cy", getDataByCut(thisResult, "(截重柜时间 - 最晚还柜时间):", converStr("\r\n")));  //cy
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", getDataByCut(thisResult, "VGM Submission Deadline:", converStr("\r\n")));   //vgm
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByCut(thisResult, "(截补料时间):", converStr("\r\n")));  //SI
            setBusShipBookingByData(sysLog, busShipBooking, "remarks2", getDataByCut(thisResult, "Time|Load Ref.", "Description|Quantity"));  //备注2
        } else if (thisResult.contains("HAPAG-LLOYD")) {  //HPL
            thisPdfType = "HPL";
            //船公司
            try {
                String carrierabbr = "HPL";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            if (thisResult.contains("Booking Confirmation|")) {
                setBusShipBookingByData(sysLog, busShipBooking, "userbook", getDataByLocation(thisResult, "Booking Confirmation|", -3, 2));   //订舱负责人
                setBusShipBookingByData(sysLog, busShipBooking, "remarks", getDataByLocation(thisResult, "Booking Confirmation|", 0, 2).split("- ")[1]);  //备注1
                setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByLocation(thisResult, "Booking Confirmation|", 1, 2));  //SO
                setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByLocation(thisResult, "Booking Confirmation|", 1, 4));   //SO日期
                setBusShipBookingByData(sysLog, busShipBooking, "piece1", getDataByLocation(thisResult, "Summary:", 0, 2).split("x")[0]);  //箱量
                setBusShipBookingByData(sysLog, busShipBooking, "cntype1", getDataByLocation(thisResult, "Summary:", 0, 2).split("x")[1]);  //箱型
                setBusShipBookingByData(sysLog, busShipBooking, "remarks2", getDataByCut(thisResult, "Export terminal delivery address", "From|To|By|ETD|ETA"));  //备注2
                setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "From|To|By|ETD|ETA", 1, 1).split(",")[0].split(" ")[0]);  //起运港
                setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "From|To|By|ETD|ETA", 1, 1).split(",")[0].split(" ")[1]);  //卸货港
                String vesselStr = getDataByLocation(thisResult, "From|To|By|ETD|ETA", 2, 1).split(" ")[6]
                        + " " + getDataByLocation(thisResult, "From|To|By|ETD|ETA", 2, 1).split(" ")[7];
                setBusShipBookingByData(sysLog, busShipBooking, "vessel", vesselStr);   //船名
                setBusShipBookingByData(sysLog, busShipBooking, "voyage", getDataByLocation(thisResult, "Voy. No", 0, 1).split(": ")[1]);   //船次

                String[] fromArray = getDataByLocation(thisResult, "From|To|By|ETD|ETA", 1, 1).split(" ");
                setBusShipBookingByData(sysLog, busShipBooking, "etd", fromArray[fromArray.length - 2]);  //ETD
                setBusShipBookingByData(sysLog, busShipBooking, "eta", fromArray[fromArray.length - 1]);   //ETA
                setBusShipBookingByData(sysLog, busShipBooking, "contractnumber", getDataByLocation(thisResult, "Contract No.:|", 0, 4));  //合约号
                setBusShipBookingByData(sysLog, busShipBooking, "masterbillno", getDataByLocation(thisResult, "BL/SWB No(s)", 0, 2));  //提单号
            } else if (thisResult.contains("订舱确认书")) {
                setBusShipBookingByData(sysLog, busShipBooking, "userbook", getDataByLocation(thisResult, "订舱确认书", -3, 2));   //订舱负责人
                setBusShipBookingByData(sysLog, busShipBooking, "remarks", getDataByLocation(thisResult, "订舱确认书", 0, 2).split("-")[1]);  //备注1
                setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByLocation(thisResult, "订舱确认书", 1, 2));  //SO
                setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByLocation(thisResult, "订舱确认书", 1, 4));   //SO日期
                setBusShipBookingByData(sysLog, busShipBooking, "piece1", getDataByLocation(thisResult, "摘要 :", 0, 2).split("x")[0]);  //箱量
                setBusShipBookingByData(sysLog, busShipBooking, "cntype1", getDataByLocation(thisResult, "摘要 :", 0, 2).split("x")[1]);  //箱型
                setBusShipBookingByData(sysLog, busShipBooking, "remarks2", getDataByCut(thisResult, "起运港指定送箱点", "起点  终点 运输方式 预计开船时间预计到达时间"));  //备注2
                setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "起点  终点 运输方式 预计开船时间预计到达时间", 1, 1).split(",")[0].split(" ")[0]);  //起运港
                setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "起点  终点 运输方式 预计开船时间预计到达时间", 1, 1).split(",")[0].split(" ")[1]);  //卸货港
                String vesselStr = getDataByLocation(thisResult, "起点  终点 运输方式 预计开船时间预计到达时间", 2, 1).split(" ")[6]
                        + " " + getDataByLocation(thisResult, "起点  终点 运输方式 预计开船时间预计到达时间", 2, 1).split(" ")[7];
                setBusShipBookingByData(sysLog, busShipBooking, "vessel", vesselStr);   //船名
                setBusShipBookingByData(sysLog, busShipBooking, "voyage", getDataByLocation(thisResult, "Voy. No", 0, 1).split(": ")[1]);   //船次
                setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByLocation(thisResult, "起点  终点 运输方式 预计开船时间预计到达时间", 1, 1).split(" ")[4]);  //ETD
                setBusShipBookingByData(sysLog, busShipBooking, "eta", getDataByLocation(thisResult, "起点  终点 运输方式 预计开船时间预计到达时间", 1, 1).split(" ")[5]);   //ETA
                setBusShipBookingByData(sysLog, busShipBooking, "contractnumber", getDataByLocation(thisResult, "报价单号", 0, 4));  //合约号
                setBusShipBookingByData(sysLog, busShipBooking, "masterbillno", getDataByLocation(thisResult, "提单号", 0, 2));  //提单号
            }

            String sidateStr = getDataByLocation(thisResult, "Shipping instruction closing", 0, 2).split(" ")[1]
                    + " " + getDataByLocation(thisResult, "Shipping instruction closing", 1, 2);
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", sidateStr);  //SI
            String vgmStr = getDataByLocation(thisResult, "VGM cut-off", 0, 2).split(" ")[1]
                    + " " + getDataByLocation(thisResult, "VGM cut-off", 1, 2);
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", vgmStr);   //vgm
            String cyStr = getDataByLocation(thisResult, "FCL delivery cut-off", 0, 2).split(" ")[1]
                    + " " + getDataByLocation(thisResult, "FCL delivery cut-off", 1, 2);
            setBusShipBookingByData(sysLog, busShipBooking, "cy", cyStr);  //cy
            String clsbigStr = getDataByLocation(thisResult, "Estimated time of arrival", 0, 2).split(" ")[1]
                    + " " + getDataByLocation(thisResult, "Estimated time of arrival", 1, 2);
            setBusShipBookingByData(sysLog, busShipBooking, "clsbig", clsbigStr);  //大船截关日期

            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByCut(thisResult, "Description:", " Gross Weight:"));  //货品英文名


            if ("CANCELLATION".equals(busShipBooking.getRemarks())) {
                busShipBooking.setBookstate("1");
            }
        } else if (thisResult.contains("MEDITERRANEAN SHIPPING COMPANY S.A.")) {  //MSC  地中海运输公司
            thisPdfType = "MSC";
            //船公司
            try {
                String carrierabbr = "MSC";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByLocation(thisResult, "订舱号", 0, 2));  //SO
            setBusShipBookingByData(sysLog, busShipBooking, "vessel", getDataByLocation(thisResult, "船名/航次", 0, 2).split("   /   ")[0]);   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", getDataByLocation(thisResult, "船名/航次", 0, 2).split("   /   ")[1]);   //船次
            setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByCut(thisResult, "预计开船日:", converStr("\r\n")));  //ETD
            setBusShipBookingByData(sysLog, busShipBooking, "cntype1", getDataByLocation(thisResult, "柜型/数量:", 0, 2));  //箱型
            setBusShipBookingByData(sysLog, busShipBooking, "servicename", getDataByLocation(thisResult, "航线", 0, 4));  //航线
            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "装货港", 0, 2).split(";")[0]);  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "masterbillno", getDataByLocation(thisResult, "提单号", 0, 4));  //提单号
            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "卸货港", 0, 2).split(";")[0]);  //卸货港
            setBusShipBookingByData(sysLog, busShipBooking, "pod", getDataByLocation(thisResult, "目的港", 0, 2).split(";")[0]);  //目的港
            setBusShipBookingByData(sysLog, busShipBooking, "cy", getDataByLocation(thisResult, "交重截数时间", 0, 2));  //cy
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", getDataByLocation(thisResult, "通过电子方式提交的截止时间", 0, 5));   //vgm
            setBusShipBookingByData(sysLog, busShipBooking, "cv", getDataByLocation(thisResult, "截放行时间", 0, 2));  //cv
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByCut(thisResult, "提单补料 (Shipping Instruction) 截数时间 : ", " hr"));  //SI
            setBusShipBookingByData(sysLog, busShipBooking, "contractnumber", getDataByCut(thisResult, "Service Contract no.\\VIP Code:", "  Named Account"));  //合约号
            setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByLocation(thisResult, "Printed on", 0, 3).split("Printed on : ")[1]);   //SO日期
        } else if (thisResult.contains("CMA CGM")) {  //CNC
            thisPdfType = "CNC";
            //船公司
            try {
                String carrierabbr = "CNC";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByCut(thisResult, "Electronic Ref.: ", converStr("\r\n")));  //SO
            setBusShipBookingByData(sysLog, busShipBooking, "sodate", getDataByCut(thisResult, "Booking Date: ", converStr("\r\n")).substring(0, 11));   //SO日期
            setBusShipBookingByData(sysLog, busShipBooking, "vessel", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split("/")[0]);   //船名
            setBusShipBookingByData(sysLog, busShipBooking, "voyage", getDataByCut(thisResult, "Vessel/Voyage:|", converStr("\r\n")).split("/")[1]);   //船次
            setBusShipBookingByData(sysLog, busShipBooking, "pol", getDataByLocation(thisResult, "Port of Loading", 0, 2));  //起运港
            setBusShipBookingByData(sysLog, busShipBooking, "etd", getDataByLocation(thisResult, "Port of Loading", 0, 5));  //ETD
            setBusShipBookingByData(sysLog, busShipBooking, "vgm", getDataByLocation(thisResult, "VGM Cut-Off Date", 0, 2));   //vgm
            setBusShipBookingByData(sysLog, busShipBooking, "clsbig", getDataByLocation(thisResult, "Port Cut-off", 0, 2).split("Time:")[1]);  //大船截关日期
            setBusShipBookingByData(sysLog, busShipBooking, "sidate", getDataByLocation(thisResult, "SI Cut-off Date", 0, 3));  //SI
            setBusShipBookingByData(sysLog, busShipBooking, "pdd", getDataByLocation(thisResult, "Port of Discharge", 0, 3).replace("( ", "").replace(" )", ""));  //卸货港

            String podStr = getDataByLocation(thisResult, "Place of Delivery", 0, 3);
            if (!isEmpty(podStr)) {
                setBusShipBookingByData(sysLog, busShipBooking, "pod", podStr.replace("( ", "").replace(" )", ""));  //目的港
            }

            setBusShipBookingByData(sysLog, busShipBooking, "goodsnamee", getDataByLocation(thisResult, "Cargo Type", 0, 2));  //货品英文名
            setBusShipBookingByData(sysLog, busShipBooking, "piece1", getDataByLocation(thisResult, "Total", 1, 3).split("X")[1]);  //箱量
            setBusShipBookingByData(sysLog, busShipBooking, "cntype1", getDataByLocation(thisResult, "Container Type", 1, 1));  //箱型
            setBusShipBookingByData(sysLog, busShipBooking, "grswgt1", getDataByLocation(thisResult, "Gross Weight", 1, 4));  //箱型
        } else if (thisResult.contains("http://www.oocl.com")) {    //HMM o/OOCL/2670405072
            thisPdfType = "OOCL";
            //船公司
            try {
                String carrierabbr = "OOCL";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }
            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByCut(thisResult, "BOOKING NUMBER|:|", converStr("\r\n")));  //SO
        } else if (thisResult.contains("http://elines.coscoshipping.com")) { //中远海运
            thisPdfType = "COSCO";
            //船公司
            try {
                String carrierabbr = "COSCO";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            String sono = getDataByCut(thisResult, "订舱号：", converStr("\r\n")).split(" ")[0];
            setBusShipBookingByData(sysLog, busShipBooking, "sono", sono);  //SO
        } else if (thisResult.contains("https://o-www.yangming.com")) {  //YML
            thisPdfType = "YML";
            //船公司
            try {
                String carrierabbr = "YML";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "remarks2", getDataByLocation(thisResult, "S/O.NO.", 0, 4));  //remarks2
            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByLocation(thisResult, "Cargo Tracking Reference No.", 1, 5));  //sono
        } else if (thisResult.contains("HTTPS://WWW.ZIM.COM/HELP/BOOKING-CONFIRMATION-CLAUSES") && thisResult.contains("ZIMUS")) {  //ZIM
            thisPdfType = "ZIM";
            //船公司
            try {
                String carrierabbr = "ZIM";
                String carriersql = "SELECT id as carrierid FROM sys_corporation WHERE isdelete = FALSE AND iscustomer = TRUE AND iscarrier = TRUE AND abbr = '" + carrierabbr + "'";
                String carrierid = StrUtils.getMapVal(serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(carriersql), "carrierid");
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", carrierid);   //船名
            } catch (Exception e) {
                setBusShipBookingByData(sysLog, busShipBooking, "carrierid", "");   //船名
            }

            setBusShipBookingByData(sysLog, busShipBooking, "sono", getDataByLocation(thisResult, "Shipping Order No", 0, 2).split("-")[1]);  //sono
        }



        //保存busShipBooking
        if (!StrUtils.isNull(busShipBooking.getSono())) {
            List<BusShipBooking> list = serviceContext.busBookingMgrService.busShipBookingDao.findAllByClauseWhere("sono = '" + busShipBooking.getSono() + "' AND isdelete = false");
            if (list.isEmpty()) {
                if (StrUtils.isNull(busShipBooking.getBookstate())) {
                    busShipBooking.setBookstate("6");
                }
                serviceContext.busBookingMgrService.saveData(busShipBooking);
                String dmlSql = "UPDATE bus_ship_booking SET inputer = 'email', isdelete = false , srctype = 'email'  WHERE id = " + busShipBooking.getId() + ";";
                serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
            } else if (list.size() == 1) {
                BusShipBooking oldbsb = list.get(0);
                copyNoNullProperties(busShipBooking, oldbsb);
                oldbsb.setInputtime(new Date());
                serviceContext.busBookingMgrService.saveData(oldbsb);
                busShipBooking.setId(oldbsb.getId());
            } else {
                throw new RuntimeException("busShipBooking超过两条数据");
            }
        }

        sysLog.setLogdesc(sysLog.getLogdesc() + ",setUpdateBusShipingData获取pdf船公司类型成功,船公司类型为" + thisPdfType);
    }

    private static void setBusShipBookingByData(SysLog sysLog, BusShipBooking busShipBooking
            , String attribute, String attributeValue) {
        try {
            attributeValue = attributeValue.replaceAll("\\|", "").trim();

            Class clazz = busShipBooking.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field f = fields[j];
                f.setAccessible(true);
                if (f.getName().equals(attribute)) {
                    Method method = clazz.getDeclaredMethod("get" + firstCharToUpperCase(attribute), new Class[]{});
                    String typename = method.getReturnType().getName();
                    if ("java.lang.String".equals(typename)) {
                        if (!isEmpty(attributeValue)) {
                            if ("cntype1".equals(attribute)) {
                                String attributeValueConver = attributeValue;
                                if (attributeValue.contains("'")) {
                                    attributeValueConver = attributeValue.replaceAll("'", "''");
                                }
                                String sql = "SELECT COALESCE(vale,'') as vale FROM edi_map WHERE editype = 'INTTRA'  AND key = '" + attributeValueConver + "';";
                                DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
                                List<Map> mapList = daoIbatisTemplate.queryWithUserDefineSql(sql);
                                if (!mapList.isEmpty()) {
                                    attributeValue = StrUtils.getMapVal(mapList.get(0), "vale");
                                }
                            }

                            f.set(busShipBooking, attributeValue);
                        }
                    } else if ("java.util.Date".equals(typename)) {
                        Timestamp timestamp = stringToDate(attributeValue);
                        if (!isEmpty(timestamp)) {
                            f.set(busShipBooking, timestamp);
                        }
                    } else if ("java.lang.Short".equals(typename)) {
                        if (!isEmpty(attributeValue)) {
                            f.set(busShipBooking, Short.parseShort(attributeValue));
                        }
                    } else if ("java.lang.Long".equals(typename)) {
                        if (!isEmpty(attributeValue)) {
                            f.set(busShipBooking, Long.parseLong(attributeValue));
                        }
                    } else if ("java.math.BigDecimal".equals(typename)) {
                        if (!isEmpty(attributeValue)) {
                            f.set(busShipBooking, new BigDecimal(attributeValue));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("setUpdateBusShipBookingByparame失败,参数busShipBooking_" + busShipBooking.toString() + ",attribute_" + attribute + ",attributeValue_" + attributeValue + "," + e.getMessage());
        }
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
