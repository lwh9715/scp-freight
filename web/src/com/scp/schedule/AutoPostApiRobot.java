package com.scp.schedule;

import com.google.gson.Gson;
import com.scp.dao.api.ApiRobotEsiDao;
import com.scp.model.api.ApiRobotEsi;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.ConfigUtils;
import com.ufms.base.utils.HttpUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoPostApiRobot {
    private static boolean isRun = false;

    public void execute() throws Exception {
        // if(AppUtils.isDebug)return;
        // AppUtils.debug("AutoFixBugJob Start:" + new Date());
        if (isRun) {
            System.out.print("AutoPostApiRobot wraning:another process is running!");
            return;
        }
        isRun = true;
        try {
            submitBook();
            submitEsi();
        } catch (Exception e) {
            throw e;
        } finally {
            isRun = false;
        }
    }

    private void submitBook() throws Exception {
        String sql = "";
        String stauts = "";
    }


    private void submitEsi() throws Exception {
        String sql = "";
        String stauts = "";
        ApiRobotEsiDao apiRobotEsiDao = (ApiRobotEsiDao) ApplicationUtilBase.getBeanFromSpringIoc("apiRobotEsiDao");
        try {
            List<ApiRobotEsi> list = apiRobotEsiDao.findAllByClauseWhere("uuid is null AND isdelete = false");
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    ApiRobotEsi apiRobotEsi = list.get(i);
                    String usercode = apiRobotEsi.getInputer();
                    String json = apiRobotEsi.getJsonpost();
                    JSONObject jsonObject = JSONObject.fromObject(json);
                    String url = ConfigUtils.findRobotCfgVal("server_url");
                    url = url + "/cargoService/reqSI";
                    String result = null;
                    String jsondate = null;

                    jsondate = jsonObject.toString();
                    Gson gson = new Gson();

                    String res = HttpUtil.post(url, jsondate);
                    System.out.println("AutoPostApiRobot submitEsi res:" + res);
                    Map<String, Object> ret = new HashMap<String, Object>();
                    ret = gson.fromJson(res, ret.getClass());
                    try {
                        result = ret.get("result").toString();
                        if (!result.isEmpty()) {
                            String insertSql = "update api_robot_esi set uuid = '" + result + "' where id=" + apiRobotEsi.getId() + ";";
                            apiRobotEsiDao.executeSQL(insertSql);
                        }
                    } catch (Exception e) {
                        isRun = false;
                        return;
                    }
                }
            }
        } catch (Exception e1) {
            isRun = false;
            return;
        }
    }
}
