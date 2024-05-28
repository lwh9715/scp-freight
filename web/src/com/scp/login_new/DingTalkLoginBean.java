package com.scp.login_new;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import com.ufms.base.utils.AppUtil;
import com.ufms.base.utils.EncoderHandler;
import com.ufms.base.web.base.UserSession;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @author CIMC
 */
@WebServlet("/dt/login")
@ManagedBean(name = "login_new.dingTalkLoginBean", scope = ManagedBeanScope.REQUEST)
public class DingTalkLoginBean extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String result = "{\"success\":false,\"message\":\"ERROR\"}";
        PrintWriter writer = resp.getWriter();
        try {
            String username;
            String issysuser;

            InputStream is = req.getInputStream();
            String json = IOUtils.toString(is, "utf-8");

            //将json格式的字符串转换成JSONObject 对象
            JSONObject jsonobject = JSONObject.fromObject(json);
            username = jsonobject.getString("username");
            issysuser = jsonobject.getString("issysuser");

            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(issysuser)) {
                result = "{\"success\":false,\"message\":\"ERROR param cannot be empty!\"}";
                writer.write(result);
                return;
            }

            String querySql = "";
            DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
            if ("on".equals(issysuser) || "true".equals(issysuser)) {
                querySql = "SELECT opneid AS openid,* FROM sys_user x WHERE (UPPER(code) ='" + username + "' OR UPPER(namec)='"
                        + username + "' OR UPPER(namee)='" + username + "' OR UPPER(email1)='" + username + "') AND isinvalid = TRUE AND isdelete = FALSE";
            } else {
                querySql = "SELECT * FROM cs_user x WHERE (UPPER(code) ='" + username + "' OR UPPER(namec)='" + username
                        + "' OR UPPER(namee)='" + username + "' OR UPPER(email1)='" + username + "' OR UPPER(tel1)='" + username + "') ";
            }

            List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
            if (list == null || list.size() == 0) {
                result = "{\"success\":false,\"message\":\"ERROR User not exists!\"}";
            } else if (list.size() > 1) {
                result = "{\"success\":false,\"message\":\"ERROR User not identity!\"}";
            } else {
                Map map = list.get(0);

                UserSession userSession = new UserSession();
                userSession.setIsLogin(true);
                userSession.setUserid(Long.parseLong(StrUtils.getMapVal(map, "id")));
                userSession.setUsercode(StrUtils.getMapVal(map, "code"));
                userSession.setUsername(StrUtils.getMapVal(map, "namec"));
                userSession.setUsernamee(StrUtils.getMapVal(map, "namee"));
                userSession.setSystemUser(true);
                userSession.setDefaultPassword(true);
                userSession.setCorpid(Long.parseLong(StrUtils.getMapVal(map, "corpid")));
                //token：用session,md5(盐：时间戳)生成16位字符串返回
                String token = EncoderHandler.encodeByMD5(System.currentTimeMillis() + StrUtils.getMapVal(map, "code")).substring(8, 24);
                String sid = req.getSession().getId();
                userSession.setSid(sid);
                userSession.setToken(token);
                req.getSession().setAttribute("userSession", userSession);
                result = "{\"success\":true,\"message\":\"OK!\",\"namec\":\"" + StrUtils.getMapVal(map, "namec")
                        + "\",\"id\":" + "\"" + StrUtils.getMapVal(map, "id") + "\",\"opneid\":" + "\"" + StrUtils.getMapVal(map, "openid")
                        + "\",\"token\":" + "\"" + token + "\",\"issysuser\":" + "\"" + ("on".equals(issysuser) ? true : false)
                        + "\",\"defaultPassword\":\"" + userSession.isDefaultPassword() + "\",\"sid\":\"" + sid + "\"}";
            }
            req.getSession().setAttribute("SESSIONKEY", username);
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"success\":false,\"message\":\"ERROR Server error!\"}";
        }
        writer.write(result);
    }
}

