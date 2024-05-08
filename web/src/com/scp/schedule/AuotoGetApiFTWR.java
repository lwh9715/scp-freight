package com.scp.schedule;

import java.util.List;
import java.util.Map;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.FtUtils;
import com.scp.util.StrUtils;
import com.ufms.web.view.sysmgr.LogBean;

/**
 * 自动获取飞驼跟踪信息
 */
public class AuotoGetApiFTWR {

    public static void main(String[] args) throws Exception {
        AuotoGetApiFTWR auotoGetApiFTWR = new AuotoGetApiFTWR();
        auotoGetApiFTWR.running();
    }

    private static boolean isRun = false;

    public void execute() throws Exception {
        if (isRun) {
            System.out.print("AuotoGetApiFTWR wraning:another process is running!");
            return;
        }
        isRun = true;
        try {
            running();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
        }
    }


    private void running() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("AuotoGetApiFTWR开始");
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");

            String sql =
                    "select *\n" +
                            "from (\n" +
                            "         SELECT coalesce(l.sono, '')    AS billno\n" +
                            "              , coalesce((select (SELECT (SELECT x.namee FROM api_data x WHERE isdelete = false and x.srctype = 'FTWR' AND maptype = 'CARRIER' and code = sc.code limit 1)\n" +
                            "                                  FROM sys_corporation sc\n" +
                            "                                  where isdelete = false\n" +
                            "                                    and id = bs.carrierid)\n" +
                            "                          from bus_shipping bs\n" +
                            "                                   left join bus_ship_container bsc on bsc.isdelete = false and bsc.parentid is null and bsc.jobid = bs.jobid\n" +
                            "                          where bs.isdelete = false\n" +
                            "                            and bsc.sono = l.sono\n" +
                            "                          limit 1), '') AS carriercode\n" +
                            "              , inputtime\n" +
                            "              , coalesce(l.editext, '') as editext\n" +
                            "         FROM edi_inttra_report l\n" +
                            "         WHERE editype = 'FTWR'\n" +
                            "           AND inputtime > NOW() + '-3MONTH'\n" +
                            "           AND inputtime < NOW() + '-6HOUR'\n" +
                            "     ) aaa\n" +
                            "where 1 = 1\n" +
                            "  and billno <> ''\n" +
                            "  and carriercode <> ''\n" +
                            "  AND editext ilike '%\"endTime\":null%'\n" +
                            "  AND editext <> ''\n" +
                            "ORDER BY inputtime\n" +
                            "limit 500";
            stringBuffer.append(",sql为").append(sql);
            List<Map> lists = daoIbatisTemplate.queryWithUserDefineSql(sql);
            if (lists != null && lists.size() > 0) {
                stringBuffer.append(",lists长度为").append(lists.size());
                for (int i = 0; i < lists.size(); i++) {
                    Map map = lists.get(i);
                    String sono = StrUtils.getMapVal(map, "billno");
                    String carrierCode = StrUtils.getMapVal(map, "carriercode");

                    FtUtils.handleFt(stringBuffer, sono, "", carrierCode);
                }
            } else {
                stringBuffer.append(",lists为空");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            isRun = false;
        }
        LogBean.insertLastingLog3(stringBuffer, "AuotoGetApiFTWR", "DEBUG");
    }
}