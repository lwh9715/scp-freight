package com.ufms.web.view.edi.ft;

import java.io.InputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scp.util.FtUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.web.BaseServlet;
import com.ufms.web.view.sysmgr.LogBean;


@WebServlet("/edi/ft/api")
public class ApiFT extends BaseServlet {

    @Action(method = "notifySubscribeShip")
    public String notifySubscribeShip(HttpServletRequest request, HttpServletResponse response) {
    	//System.out.println("notifySubscribeShip:......");
        String returnText = "OK";
        try {
        	response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Access-Control-Allow-Methods, Access-Control-Allow-Origin");

            InputStream is = request.getInputStream();
            String json = IOUtils.toString(is, "UTF-8");
            json = StrUtils.getSqlFormat(json);
            //System.out.println.println("json:......" + json);
            
            if (!StrUtils.isNull(json)) {


                JSONObject jsonobject = JSONObject.fromObject(json);//将json格式的字符串转换成JSONObject 对象
                //System.out.println.println("jsonobject:......"+jsonobject);
                
                String sono = jsonobject.getString("referenceno");
                //System.out.println.println("sono:......"+sono);
                
                JSONArray jsonArray = jsonobject.getJSONArray("params");
                
                JSONObject jsonobjectParams = (JSONObject)jsonArray.get(0);
        		String carrierCode = jsonobjectParams.getString("CARRIER_CD");

                StringBuffer stringBuffer = new StringBuffer();
                FtUtils.handleFt(stringBuffer, sono, "", carrierCode);
                // LogBean.insertLastingLog2(stringBuffer, "notifySubscribeShip");

            }
        } catch (Exception e) {
        	e.printStackTrace();
        	returnText = e.getLocalizedMessage();
        }
        return returnText;
    }

    @Action(method = "notifySubscribeAir")
    public String notifySubscribeAir(HttpServletRequest request) {
        String returnText = "";
        try {
           
        } catch (Exception e) {
        }
        return returnText;
    }
    
    public static void main(String[] args) {
		String json = "{\"referenceno\":\"GOSUSHH30802318\",\"params\":[{\"DTP\":\"\",\"CARRIER_CD\":\"GSL\",\"IEMARK\":\"\",\"POL\":\"\",\"REFERENCE_NO\":\"GOSUSHH30802318\",\"CTNRNO\":\"\"}]}";
		
		JSONObject jsonobject = JSONObject.fromObject(json);//将json格式的字符串转换成JSONObject 对象
        System.out.println("jsonobject:......"+jsonobject);
        
        String sono = jsonobject.getString("referenceno");
        System.out.println("sono:......"+sono);
        
        JSONArray jsonArray = jsonobject.getJSONArray("params");
        
        JSONObject jsonobjectParams = (JSONObject)jsonArray.get(0);
		String carrierCode = jsonobjectParams.getString("CARRIER_CD");
		System.out.println("carrierCode:......"+carrierCode);
	}
}
