package com.scp.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * JSON工具类
 */
/**
 * @author Administrator
 *
 */
public class JSONUtil
{
    private static Gson gson = new Gson();

    private static Gson gjson = null;// 带日期转换
    static
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.sql.Date.class, new DateTypeAdapter());
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new UDateTypeAdapter());
        gsonBuilder.registerTypeAdapter(java.sql.Timestamp.class, new TimestampTypeAdapter());
        gsonBuilder.disableHtmlEscaping();
        // gsonBuilder.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gjson = gsonBuilder.create();
    }

    /**
     * JSON字符串转换为Map对象(url转码)
     * @param jsonStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map json2map(String jsonStr)
    {
        if (jsonStr != null)
        {
            return gson.fromJson(jsonStr, Map.class);
        }
        else
        {
            return null;
        }
    }

    /***
     * JSON字符串转换为Map对象
     * @param jsonStr
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    public static Map convertObject(String jsonStr)
    {
        if (jsonStr != null)
        {
            Map jsonObject = gson.fromJson(jsonStr, Map.class);
            return jsonObject;
        }
        else
        {
            return null;
        }
    }

    /***
     * JSON字符串转换为List对象
     * @param jsonStr
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    public static List convertList(String jsonStr) throws UnsupportedEncodingException
    {
        return gson.fromJson(jsonStr, List.class);
    }

    /***
     * JSON字符串转换为String对象
     * @param jsonStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String convertString(Object obj)
    {
        return gjson.toJson(obj);
    }
    
    /***
     * 将奇数个转义字符变成偶数个
     * @param s
     * @return
     */
    public static String getDescodeJSONStr(String s){
    	StringBuilder sb = new StringBuilder();
    	char c;
    	for(int i = 0;i<s.length();i++){
    		c = s.charAt(i);
    		switch(c){
    		case '\\':
    			sb.append("\\\\");
    			break;
    		default :
    			sb.append(c);
    		}
    	}
    	return sb.toString();
    }
    
    /**拼接模板要求的JSON
     * @param openid(被发送人的绑定微信的id)
     * @param templateid(模板id)
     * @param datafv
     * @param datakeyv
     * @param datakey2v
     * @param datekey3v
     * @param datekey4v
     * @return
     */
    public static String sendWeixin(String openid,String templateid,String datafv,String datakeyv,String datakey2v,String datekey3v,String datekey4v){
    	JSONObject jsonObject = new JSONObject();
		jsonObject.element("touser", openid);
		jsonObject.element("template_id", templateid);
		jsonObject.element("url", "http://www.ufms.cn");
		jsonObject.element("topcolor", "#40447E");
		
		JSONObject datas = new JSONObject();
		
		JSONObject dataf = new JSONObject();
		dataf.element("value",datafv);
		dataf.element("color","#173177");
		datas.element("first", dataf);
		
		JSONObject datakey = new JSONObject();
		datakey.element("value", datakeyv);
		datakey.element("color","#40447E");
		datas.element("keyword1",datakey);
		
		JSONObject datakey2 = new JSONObject();
		datakey2.element("value", datakey2v);
		datakey2.element("color","#40447E");
		datas.element("keyword2",datakey2);
		JSONObject datekey3 = new JSONObject();
		datekey3.element("value", datekey3v);
		datekey3.element("color", "#40447E");
		datas.element("keyword3", datekey3);
		JSONObject datekey4 = new JSONObject();
		datekey4.element("value", datekey4v);
		datekey4.element("color", "#40447E");
		datas.element("keyword4", datekey4);
		jsonObject.element("data", datas);
		String json =jsonObject.toString();
		return json;
    }
    
    /**拼接模板要求的JSON
     * @param url
     * @param openid(被发送人的绑定微信的id)
     * @param templateid(模板id)
     * @param first
     * @param keyword1
     * @param keyword2
     * @param keyword3
     * @param keyword4
     * @param keyword5
     * @param remark
     * @return
     */
    public static String sendWeixinTemplate(String url,String openid,String templateid,String first,String keyword1,String keyword2,String keyword3,String keyword4,String keyword5,String remark){
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.element("touser", openid);
		jsonObject.element("template_id", templateid);
		jsonObject.element("url", url==null?"http://www.ufms.cn":url);
		jsonObject.element("topcolor", "#40447E");
		
		JSONObject datas = new JSONObject();
		
		JSONObject dataf = new JSONObject();
		dataf.element("value",first);
		dataf.element("color","#173177");
		datas.element("first", dataf);
		
		if(keyword1!=null){
			JSONObject datakey1 = new JSONObject();
			datakey1.element("value", keyword1);
			datakey1.element("color","#40447E");
			datas.element("keyword1",datakey1);
		}
		
		if(keyword2!=null){
			JSONObject datakey2 = new JSONObject();
			datakey2.element("value", keyword2);
			datakey2.element("color","#40447E");
			datas.element("keyword2",datakey2);
		}
		
		
		if(keyword3!=null){
			JSONObject datakey3 = new JSONObject();
			datakey3.element("value", keyword3);
			datakey3.element("color","#40447E");
			datas.element("keyword3",datakey3);
		}
		
		if(keyword4!=null){
			JSONObject datakey4 = new JSONObject();
			datakey4.element("value", keyword4);
			datakey4.element("color","#40447E");
			datas.element("keyword4",datakey4);
		}
		
		if(keyword5!=null){
			JSONObject datakey5 = new JSONObject();
			datakey5.element("value", keyword5);
			datakey5.element("color","#40447E");
			datas.element("keyword5",datakey5);
		}
		if(remark!=null){
			JSONObject datar = new JSONObject();
			datar.element("value",remark);
			datar.element("color","#173177");
			datas.element("remark", datar);
		}
		jsonObject.element("data", datas);
    	String json = jsonObject.toString();
    	return json;
    }
}

class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
{
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public JsonElement serialize(Date ts, Type t, JsonSerializationContext jsc)
    {
        String dfString = format.format(new java.util.Date(ts.getTime()));
        return new JsonPrimitive(dfString);
    }

    public Date deserialize(JsonElement json, Type t, JsonDeserializationContext jsc) throws JsonParseException
    {
        if (!(json instanceof JsonPrimitive))
        {
            throw new JsonParseException("The date should be a string value");
        }
        try
        {
            java.util.Date date = format.parse(json.getAsString());
            return new Date(date.getTime());
        }
        catch (ParseException e)
        {
            throw new JsonParseException(e);
        }
    }
}

class UDateTypeAdapter implements JsonSerializer<java.util.Date>, JsonDeserializer<java.util.Date>
{
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public JsonElement serialize(java.util.Date ts, Type t, JsonSerializationContext jsc)
    {
        String dfString = format.format(new java.util.Date(ts.getTime()));
        return new JsonPrimitive(dfString);
    }

    public java.util.Date deserialize(JsonElement json, Type t, JsonDeserializationContext jsc)
        throws JsonParseException
    {
        if (!(json instanceof JsonPrimitive))
        {
            throw new JsonParseException("The date should be a string value");
        }
        try
        {
            return format.parse(json.getAsString());
        }
        catch (ParseException e)
        {
            throw new JsonParseException(e);
        }
    }
}

class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp>
{
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public JsonElement serialize(Timestamp ts, Type t, JsonSerializationContext jsc)
    {
        String dfString = format.format(new Date(ts.getTime()));
        return new JsonPrimitive(dfString);
    }

    public Timestamp deserialize(JsonElement json, Type t, JsonDeserializationContext jsc) throws JsonParseException
    {
        if (!(json instanceof JsonPrimitive))
        {
            throw new JsonParseException("The date should be a string value");
        }
        try
        {
            java.util.Date date = format.parse(json.getAsString());
            return new Timestamp(date.getTime());
        }
        catch (ParseException e)
        {
            throw new JsonParseException(e);
        }
    }
}