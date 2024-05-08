package com.scp.login_new;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

/**
 * @author CIMC
 */
@WebServlet("/dingTalkCode")
@ManagedBean(name = "login_new.dingTalkBean", scope = ManagedBeanScope.REQUEST)
public class DingTalkBean extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(req.getParameter("code"));
    }
}

