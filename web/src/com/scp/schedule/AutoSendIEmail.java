package com.scp.schedule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.scp.service.ServiceContext;
import com.scp.service.sysmgr.SysEmailService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * 自动发送邮件
 *
 * @author Neo
 */
public class AutoSendIEmail {

    private static boolean isRun = false;

    public void execute() throws Exception {
        if (isRun) {
            System.out.println("@@@ AutoSendIEmail wraning:another process is running!");
            return;
        }
        isRun = true;
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
            sendAllEmail(serviceContext);
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
        }
    }


    public static void main(String[] args) {
        try {
            ServiceContext serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIocJunit("serviceContext");
            sendAllEmail(serviceContext);
        } catch (Exception e) {
            System.out.println(1);
        }
    }

    public static void sendAllEmail(ServiceContext serviceContext) throws Exception {
        StringBuffer sb = new StringBuffer("sendAllEmail开始");
        try {
            String sql0 = "SELECT customerid\n" +
                    "            FROM (\n" +
                    "                     SELECT fa.customerid\n" +
                    "                     FROM fina_jobs fj\n" +
                    "                              inner JOIN fina_arap fa ON (fa.isdelete = FALSE AND fj.id = fa.jobid)\n" +
                    "                              inner JOIN sys_corporation sc ON (sc.isdelete = FALSE AND sc.iscustomer = TRUE AND fa.customerid = sc.id)\n" +
                    "                     WHERE fj.isdelete = FALSE\n" +
                    "                       AND fa.araptype = 'AR'\n" +
                    "                       AND to_char(NOW() - (DATE_TRUNC('MONTH', (DATE_TRUNC('MONTH', fj.jobdate ::DATE) + INTERVAL '1 MONTH'))), 'DD')::INT >\n" +
                    "                           (select daysar from sys_corporation where isdelete = FALSE and id = fa.customerid limit 1)\n" +
                    "                       AND fa.amount - fa.amtstl2 > 0\n" +
                    "                 ) t\n" +
                    "            GROUP BY t.customerid limit 100";
            List<Map> list0 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
            if (list0 != null && list0.size() > 0) {
                sb.append("，ssendAllEmail已经获取到客商数据，list0长度为").append(list0.size());
                for (int i = 0; i < list0.size(); i++) {
                    String customerid = String.valueOf(list0.get(i).get("customerid"));
                    createAndsendEmail(sb, serviceContext, customerid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.append(",异常为").append(e.getMessage());
        }
        LogBean.insertLog(sb);
    }

    public static boolean createAndsendEmail(StringBuffer sb, ServiceContext serviceContext, String customerid) throws Exception {
        String filename = System.currentTimeMillis() + "zhangdanchaoqi.xlsx";
        String fileUrl = AppUtils.getAttachFilePath() + filename;
        sb.append("\r\n\r\n,createAndsendEmail开始");

        BufferedOutputStream bos = null;
        File file2local = new File(fileUrl);
        bos = new BufferedOutputStream(new FileOutputStream(file2local));
        if (!file2local.exists()) {
            file2local.createNewFile();
        }

        XSSFWorkbook wb = new XSSFWorkbook();//建立新HSSFWorkbook对象
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment((short) 1);
        cellStyle.setVerticalAlignment((short) 0);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet("1");


        String[] aa = new String[]{"业务员", "部门", "客户", "结算天数", "业务号", "工作单日期", "委托人简称", "参考号", "MBL", "取单/放单及时间", "ETA"
                , "ETD", "目的港", "箱量箱型", "CNY", "USD", "HKD", "折合(USD)", "提单号", "发票号"};
        String[] bb = new String[]{"sales", "dept", "customernamec", "daysar", "nos", "jobdate", "customerabbr2", "refno", "mblnos", "getrelacembl", "eta", "etd", "pod", "cntdesc", "amt_ar_cny_stl_all",
                "amt_ar_usd_stl_all", "amt_ar_hkd_stl_all", "ar2sum", "mblnos", "invoicenos"};


        Row row = null;
        Cell cell = null;

        //第一行
        row = sheet.createRow(0);
        for (int j = 0; j < aa.length; j++) {
            cell = row.createCell(j);
            cell.setCellValue(aa[j]);
        }

        List<Map> list1 = getListdata1(sb, serviceContext, customerid);
        sb.append("，list1长度为").append(list1.size());
        for (int i = 0; i < list1.size(); i++) {
            row = sheet.createRow(i + 1);
            Map<String, String> map1 = list1.get(i);
            for (int j = 0; j < bb.length; j++) {
                cell = row.createCell(j);
                String thisValue = String.valueOf(map1.get(bb[j])).replaceAll("null", "");
                cell.setCellValue(thisValue);
            }
        }

        List<Map> list2 = getListdata2(sb, serviceContext, customerid);
        Map<String, Object> map2 = list2.get(0);
        row = sheet.createRow(list1.size() + 1);
        for (int j = 0; j < aa.length; j++) {
            if (j == 13) {
                cell = row.createCell(j);
                cell.setCellValue("合计");
            }
            if (j == 14) {
                cell = row.createCell(j);
                cell.setCellValue(String.valueOf(map2.get("amt_ar_cny_stl_all")));
            }
            if (j == 15) {
                cell = row.createCell(j);
                cell.setCellValue(String.valueOf(map2.get("amt_ar_usd_stl_all")));
            }
            if (j == 16) {
                cell = row.createCell(j);
                cell.setCellValue(String.valueOf(map2.get("amt_ar_hkd_stl_all")));
            }
            if (j == 17) {
                cell = row.createCell(j);
                cell.setCellValue(String.valueOf(map2.get("amount_all")));
            }
        }
        wb.write(bos);
        bos.close();


        List<Map> list3 = getListdata3(sb, serviceContext, customerid);
        sb.append("，list3长度为").append(list3.size());
        if (list3 != null && list3.size() > 0) {
            for (int i = 0; i < list3.size(); i++) {
                Map<String, String> map3 = list3.get(i);
                Map mapPkId1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT getid() AS newPkId");
                String emailId = StrUtils.getMapVal(mapPkId1, "newpkid");

                String sqlemail = "\nINSERT INTO sys_email(id, addressee,copys, subject, content, sender, emailtype, inputer, inputtime,linktbl,isautosend)"
                        + "\nVALUES(" + emailId + ",'" + map3.get("email") + "','','账期过期提醒：'||(select namec from sys_corporation where id = " + customerid + " limit 1),''"
                        + "\n, (SELECT COALESCE(val, '') FROM sys_config WHERE key = 'email_pop3_account'),'S','ZHALE',now(),'createAndsendEmail',true);";
                serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sqlemail);

                String fileType = SysEmailService.getFileTypelinux(file2local.getAbsolutePath());
                String sqlAttachment = "INSERT INTO sys_attachment(id, linkid, filename, contenttype, filesize, inputer,inputtime, roleid,remarks)" +
                        "   VALUES (getid(), " + emailId + ", '" + filename + "', '" + fileType + "' , " + (new BigDecimal(file2local.length())) + ", 'createAndsendEmail', NOW(), " + -222 + ", '');";
                serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sqlAttachment);

                sb.append("\r\n，sqlemail为").append(sqlemail);
                sb.append("\r\n，sqlAttachment为").append(sqlAttachment);
                sb.append("\r\n，file2local为").append(file2local.getAbsolutePath());
            }
        }
        return true;
    }

    private static List<Map> getListdata1(StringBuffer sb, ServiceContext serviceContext, String customerid) {
        List<Map> list3 = null;
        try {
            String sql0 = "select sales\n" +
                    "                         , (SELECT name\n" +
                    "                            FROM sys_department d\n" +
                    "                            WHERE d.isdelete = FALSE\n" +
                    "                              AND d.id =\n" +
                    "                                  (SELECT u.deptid FROM sys_user u WHERE u.isdelete = FALSE AND u.namec = aa.sales LIMIT 1)\n" +
                    "                            LIMIT 1)                                                                                                                                                       AS" +
                    " dept\n" +
                    "                         , (SELECT c.namec FROM sys_corporation c WHERE c.isdelete = FALSE AND c.id = aa.customerid LIMIT 1)                                                               AS" +
                    " customernamec\n" +
                    "                         , daysar\n" +
                    "                         , nos\n" +
                    "                         , jobdate\n" +
                    "                         , (select abbr from sys_corporation where id = (select customerid from fina_jobs where id = aa.jobid))                                                            as" +
                    " customerabbr2\n" +
                    "                         , (SELECT x.csname FROM _fina_jobs x where x.id = aa.jobid and x.isdelete = false)                                                                                AS" +
                    " csname\n" +
                    "                         , refno\n" +
                    "                         , mblnos\n" +
                    "                         , (SELECT COALESCE(to_char(dategetmbl, 'yyyy/MM/dd'), '') || '/' || COALESCE(to_char(datereleasembl, 'yyyy/MM/dd'), '') FROM bus_shipping WHERE jobid = aa.jobid) as" +
                    " getrelacembl\n" +
                    "                         , eta\n" +
                    "                         , etd\n" +
                    "                         , pod\n" +
                    "                         , cntdesc\n" +
                    "                         , mblnos\n" +
                    "                         , invoicenos\n" +
                    "                         , sum(amt_ar_cny_stl)                                                                                                                                             as" +
                    " amt_ar_cny_stl_all\n" +
                    "                         , sum(amt_ar_usd_stl)                                                                                                                                             as" +
                    " amt_ar_usd_stl_all\n" +
                    "                         , sum(amt_ar_hkd_stl)                                                                                                                                             as" +
                    " amt_ar_hkd_stl_all\n" +
                    "                         , sum(amt_ar_dhs_stl)                                                                                                                                             as" +
                    " amt_ar_dhs_stl_all\n" +
                    "                         , sum(amt_ar_eur_stl)                                                                                                                                             as" +
                    " amt_ar_eur_stl_all\n" +
                    "                         , sum(amt_ar_mmk_stl)                                                                                                                                             as" +
                    " amt_ar_mmk_stl_all\n" +
                    "                         , sum(amt_ar_omr_stl)                                                                                                                                             as" +
                    " amt_ar_omr_stl_all\n" +
                    "                         , sum(amt_ar_aud_stl)                                                                                                                                             as" +
                    " amt_ar_aud_stl_all\n" +
                    "                         , sum(amt_ar_jpy_stl)                                                                                                                                             as" +
                    " amt_ar_jpy_stl_all\n" +
                    "                         , sum(amt_ar_gbp_stl)                                                                                                                                             as" +
                    " amt_ar_gbp_stl_all\n" +
                    "                         , sum(amt_ar_krw_stl)                                                                                                                                             as" +
                    " amt_ar_krw_stl_all\n" +
                    "                         , sum(amount)                                                                                                                                                     as" +
                    " ar2sum\n" +
                    "                    from (\n" +
                    "                             SELECT fj.id                                                                                                                       as jobid\n" +
                    "                                  , fj.nos\n" +
                    "                                  , fj.saleid\n" +
                    "                                  , fj.sales\n" +
                    "                                  , fj.jobdate\n" +
                    "                                  , fj.refno\n" +
                    "\n" +
                    "                                  , bs.eta\n" +
                    "                                  , bs.etd\n" +
                    "                                  , bs.pod\n" +
                    "                                  , (SELECT CASE\n" +
                    "                                                WHEN fj.jobtype = 'S' THEN (SELECT x.mblno FROM bus_shipping x WHERE x.jobid = fj.id AND x.isdelete = FALSE)\n" +
                    "                                                ELSE (SELECT y.mawbno FROM bus_air as y WHERE y.isdelete = FALSE AND y.jobid = fj.id) END)::TEXT                AS mblnos\n" +
                    "                                  , (SELECT f_bus_shipping_cntdesc('shipid=' || bs1.id) FROM bus_shipping bs1 WHERE bs1.jobid = fj.id AND bs1.isdelete = FALSE) AS cntdesc\n" +
                    "                                  , (SELECT string_agg(DISTINCT x.invoiceno, ',')\n" +
                    "                                     FROM fina_invoice x,\n" +
                    "                                          fina_arap y\n" +
                    "                                     WHERE x.isdelete = FALSE\n" +
                    "                                       AND x.clientid = fa.customerid\n" +
                    "                                       AND y.isdelete = FALSE\n" +
                    "                                       AND y.invoiceid = x.id\n" +
                    "                                       AND y.jobid = fj.id)                                                                                                     AS invoicenos\n" +
                    "\n" +
                    "                                  , fa.id                                                                                                                       as arapid\n" +
                    "                                  , fa.customerid\n" +
                    "                                  , fa.currency\n" +
                    "                                  , (case when fa.currency = 'CNY' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_cny_stl\n" +
                    "                                  , (case when fa.currency = 'USD' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_usd_stl\n" +
                    "                                  , (case when fa.currency = 'HKD' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_hkd_stl\n" +
                    "                                  , (case when fa.currency = 'DHS' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_dhs_stl\n" +
                    "                                  , (case when fa.currency = 'EUR' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_eur_stl\n" +
                    "                                  , (case when fa.currency = 'MMK' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_mmk_stl\n" +
                    "                                  , (case when fa.currency = 'OMR' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_omr_stl\n" +
                    "                                  , (case when fa.currency = 'OMR' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_aud_stl\n" +
                    "                                  , (case when fa.currency = 'JPY' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_jpy_stl\n" +
                    "                                  , (case when fa.currency = 'GBP' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_gbp_stl\n" +
                    "                                  , (case when fa.currency = 'KRW' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end)             AS amt_ar_krw_stl\n" +
                    "                                  , f_amtto(fa.arapdate, fa.currency, 'USD', (fa.amount - fa.amtstl2))::NUMERIC(18, 2)             AS amount\n" +
                    "                                  , sc.daysar\n" +
                    "                             FROM fina_jobs fj\n" +
                    "                                      inner JOIN bus_shipping bs ON (bs.isdelete = FALSE AND fj.id = bs.jobid)\n" +
                    "                                      inner JOIN fina_arap fa ON (fa.isdelete = FALSE AND fj.id = fa.jobid)\n" +
                    "                                      inner JOIN sys_corporation sc ON (sc.isdelete = FALSE AND sc.iscustomer = TRUE AND fa.customerid = sc.id)\n" +
                    "                             WHERE fj.isdelete = FALSE\n" +
                    "                               AND fa.araptype = 'AR'\n" +
                    "                               AND to_char(NOW() - (DATE_TRUNC('MONTH', (DATE_TRUNC('MONTH', fj.jobdate ::DATE) + INTERVAL '1 MONTH'))), 'DD')::INT >\n" +
                    "                                   (select daysar from sys_corporation where isdelete = FALSE and id = fa.customerid limit 1)\n" +
                    "                               AND fa.amount - fa.amtstl2 > 0\n" +
                    "                               and sc.id = " + customerid + " ::bigint\n" +
                    "                         ) aa\n" +
                    "                    group by sales, dept, customernamec, daysar, nos, jobdate, customerabbr2, csname, refno, mblnos, getrelacembl, eta, etd, pod, cntdesc, mblnos, invoicenos\n" +
                    "                    ORDER BY sales, dept, customernamec, daysar, nos";
            list3 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
        } catch (Exception e) {
            sb.append("\r\n,getListdata异常为").append(e.getMessage());
        }
        return list3;
    }

    private static List<Map> getListdata2(StringBuffer sb, ServiceContext serviceContext, String customerid) {
        List<Map> list3 = null;
        try {
            String sql0 = "   select sum(amt_ar_cny_stl) as amt_ar_cny_stl_all\n" +
                    "                     , sum(amt_ar_usd_stl) as amt_ar_usd_stl_all\n" +
                    "                     , sum(amt_ar_hkd_stl) as amt_ar_hkd_stl_all\n" +
                    "                     , sum(amt_ar_dhs_stl) as amt_ar_dhs_stl_all\n" +
                    "                     , sum(amt_ar_eur_stl) as amt_ar_eur_stl_all\n" +
                    "                     , sum(amt_ar_mmk_stl) as amt_ar_mmk_stl_all\n" +
                    "                     , sum(amt_ar_omr_stl) as amt_ar_omr_stl_all\n" +
                    "                     , sum(amt_ar_aud_stl) as amt_ar_aud_stl_all\n" +
                    "                     , sum(amt_ar_jpy_stl) as amt_ar_jpy_stl_all\n" +
                    "                     , sum(amt_ar_gbp_stl) as amt_ar_gbp_stl_all\n" +
                    "                     , sum(amt_ar_krw_stl) as amt_ar_krw_stl_all\n" +
                    "                     , sum(amount)         as amount_all\n" +
                    "                from (\n" +
                    "                         SELECT (case when fa.currency = 'CNY' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_cny_stl\n" +
                    "                              , (case when fa.currency = 'USD' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_usd_stl\n" +
                    "                              , (case when fa.currency = 'HKD' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_hkd_stl\n" +
                    "                              , (case when fa.currency = 'DHS' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_dhs_stl\n" +
                    "                              , (case when fa.currency = 'EUR' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_eur_stl\n" +
                    "                              , (case when fa.currency = 'MMK' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_mmk_stl\n" +
                    "                              , (case when fa.currency = 'OMR' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_omr_stl\n" +
                    "                              , (case when fa.currency = 'OMR' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_aud_stl\n" +
                    "                              , (case when fa.currency = 'JPY' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_jpy_stl\n" +
                    "                              , (case when fa.currency = 'GBP' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_gbp_stl\n" +
                    "                              , (case when fa.currency = 'KRW' then COALESCE((COALESCE(fa.amount, 0) - COALESCE(fa.amtstl2, 0)), 0) else 0 end) AS amt_ar_krw_stl\n" +
                    "                              , f_amtto(fa.arapdate, fa.currency, 'USD', (fa.amount - fa.amtstl2))::NUMERIC(18, 2)                              AS amount\n" +
                    "                         FROM fina_jobs fj\n" +
                    "                                  inner JOIN bus_shipping bs ON (bs.isdelete = FALSE AND fj.id = bs.jobid)\n" +
                    "                                  inner JOIN fina_arap fa ON (fa.isdelete = FALSE AND fj.id = fa.jobid)\n" +
                    "                                  inner JOIN sys_corporation sc ON (sc.isdelete = FALSE AND sc.iscustomer = TRUE AND fa.customerid = sc.id)\n" +
                    "                         WHERE fj.isdelete = FALSE\n" +
                    "                           AND fa.araptype = 'AR'\n" +
                    "                           AND to_char(NOW() - (DATE_TRUNC('MONTH', (DATE_TRUNC('MONTH', fj.jobdate ::DATE) + INTERVAL '1 MONTH'))), 'DD')::INT >\n" +
                    "                               (select daysar from sys_corporation where isdelete = FALSE and id = fa.customerid limit 1)\n" +
                    "                           AND fa.amount - fa.amtstl2 > 0\n" +
                    "                            and sc.id = " + customerid + " ::bigint\n" +
                    "                     ) aa;";
            list3 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
        } catch (Exception e) {
            sb.append("\r\n,getListdata1异常为").append(e.getMessage());
        }
        return list3;
    }

    private static List<Map> getListdata3(StringBuffer sb, ServiceContext serviceContext, String customerid) {
        List<Map> list3 = null;
        try {
            String sql0 = " select y.*\n" +
                    "                             , tt.roletypedesc2\n" +
                    "                             , COALESCE(COALESCE(y.email1, y.email2), y.namec || '没填邮件') AS email\n" +
                    "                        from sys_corporation sc\n" +
                    "                                 inner join\n" +
                    "                             (\n" +
                    "                                 select *\n" +
                    "                                      , (SELECT df.namec FROM dat_filedata df WHERE fkcode = 160 AND isleaf = TRUE AND isdelete = false AND code = t.roletype) AS roletypedesc2\n" +
                    "                                 FROM _sys_user_assign t\n" +
                    "                                 WHERE 1 = 1\n" +
                    "                                   AND linktype ILIKE '%' || TRIM('C') || '%'\n" +
                    "                                   AND linktype = 'C'\n" +
                    "                             ) tt on sc.id = tt.linkid\n" +
                    "                                 inner join sys_user y on y.isdelete = FALSE and tt.userid = y.id AND f_common_checkemail(COALESCE(y.email1, y.email2)) = TRUE\n" +
                    "                        where sc.isdelete = false\n" +
                    "                          and sc.iscustomer = true\n" +
                    "                          and (tt.roletype = 'S')\n" +
                    "                          and sc.id = " + customerid + " ::bigint";
            ;
            list3 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql0);
        } catch (Exception e) {
            sb.append("\r\n,getListdata1异常为").append(e.getMessage());
        }
        return list3;
    }
}
