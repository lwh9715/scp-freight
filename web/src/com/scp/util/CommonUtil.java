/*$Id: Util.java,v 1.1 2008/07/15 05:13:54 liqi Exp $
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * author   date   comment
 * chenhongxin  2008-4-14  Created
 */
package com.scp.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * 通用工具类
 * 
 * @author chenhongxin
 */
public class CommonUtil {
	private CommonUtil() {
	}

	/**
	 * 获取数字随机数
	 * 
	 * @param size
	 *            随机数个数
	 * @return 指定个数的数字随机数的字符串
	 */
	public static String getRandom(int numSize) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(numSize);
		for (int i = 0; i < numSize; i++) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}


	/**
	 * 根据随即码以及宽高绘制图片函数
	 * 
	 * @param g
	 *            Graphics
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param randomCode
	 *            随机码
	 */
	public static void drawRandomPicture(Graphics g, int width, int height,
			String randomCode) {
		g.setColor(randColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.setColor(randColor(160, 200));

		Random random = new Random(System.currentTimeMillis());

		//  随机产生155条干扰线，使图像中的验证码不易被识别
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		//  将验证码显示在图像中
		for (int i = 0; i < 4; i++) {
			g.setColor(randColor(20, 130));
			g.drawString(randomCode.substring(i, i + 1), 13 * i + 6, 16);
		}
	}

	/**
	 * 获得随机色
	 * 
	 * @param fc
	 *            前景色
	 * @param bc
	 *            背景色
	 * @return 随机色
	 */
	private static Color randColor(int fc, int bc) {
		Random random = new Random(System.currentTimeMillis());
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 获取绝对物理路径
	 * 
	 * @param path
	 *            相对路径
	 * @return 对应的绝对物理路径字符串
	 */
	public static String getRealPath(String path) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext eContext = context.getExternalContext();
		ServletContext sContext = (ServletContext) eContext.getContext();
		return sContext.getRealPath(path);
	}

	/**
	 * 获取网站的BasePath
	 * 
	 * @return 网站的BasePath的字符串
	 */
	public static String getBasePath() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext eContext = context.getExternalContext();
		ServletRequest req = (ServletRequest) eContext.getRequest();
		return (req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + eContext.getRequestContextPath() + "/");
	}
	
	
	/**
	 * 创建指定EL方法表达式的MethodExpressionActionListener
	 * 
	 * @param context
	 *            FacesContext
	 * @param expression
	 *            EL方法表达式
	 * @return MethodExpressionActionListener
	 */
	public static MethodExpressionActionListener createMethodExpressionActionListener(
			FacesContext context, String expression) {
		return new MethodExpressionActionListener(context.getApplication()
				.getExpressionFactory().createMethodExpression(
						context.getELContext(), expression, Void.class,
						new Class[] { ActionEvent.class }));
	}
	
	/**
	 * @param date 
	 * @param month 月份
	 * @return 返回date+月份
	 */
	public static Date DateMonthUp(Date date,int month){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, month);
		return rightNow.getTime();
	}
	
	/**
	 * @param date 
	 * @param day 天数
	 * @return 返回date+天数
	 */
	public static Date DateDayUp(Date date,int day){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.DAY_OF_YEAR, day);
		return rightNow.getTime();
	}
	
	/**
	 * @param date 
	 * @param year 年数
	 * @return 返回year+年
	 */
	public static Date DateYrarUp(Date date,int year){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.YEAR, year);
		return rightNow.getTime();
	}
	
	//根据周数返回日期范围
	public static String getWeekDays(String year,Integer week){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        Date date = new Date();
       // String year = sdf1.format(date);
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String beginDate = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = sdf.format(cal.getTime());
        return beginDate+"~"+endDate;
    }
	
	public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
//	public static void main(String[] args) {
//		System.out.println(getWeekDays(41));
//		System.out.println(getWeekDays(42));
//		System.out.println(getWeekDays(43));
//	}
}
