package com.ufms.web.view.common;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.web.BaseServlet;


@WebServlet("/ufms/common")
@ManagedBean(name = "pages.module.common.commonBean")
public class CommonBean extends BaseServlet {
	
	//http://192.168.0.188:8888/scp/ufms/common?method=getQrCode&url=http://192.168.0.188:8888/scp
	
	@Action(method="getBarCode")
	public byte[] getBarCode(HttpServletRequest request , HttpServletResponse response){
		String message = request.getParameter("message");
		Double height = Double.parseDouble(request.getParameter("height"));
		Double width = Double.parseDouble(request.getParameter("width"));
		boolean withQuietZone = Boolean.parseBoolean(request.getParameter("withQuietZone"));
		boolean hideText = Boolean.parseBoolean(request.getParameter("hideText"));
		
		Code128Bean bean = new Code128Bean();
		
		// 分辨率
        int dpi = 512;
        // 设置两侧是否留白
        bean.doQuietZone(withQuietZone);
 
        // 设置条形码高度和宽度
        bean.setBarHeight(height);
        if (width != null) {
            bean.setModuleWidth(width);
        }
        // 设置文本位置（包括是否显示）
        if (hideText) {
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        }
        // 设置图片类型
        String format = "image/png";
 
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);
 
        // 生产条形码
        bean.generateBarcode(canvas, message);
        try {
            canvas.finish();
        } catch (IOException e) {
 
        }
        return ous.toByteArray();
	}
	
	@Action(method="getQrCode")
	public void getJson(HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        
        int w = 100;
        int h = 100;
 
		/*
         * 得到参数高，宽，都为数字时，则使用设置高宽，否则使用默认值
		 */
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
            w = NumberUtils.toInt(width);
            h = NumberUtils.toInt(height);
        }
 
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
 
        String url = request.getParameter("url");
        try {
            BitMatrix byteMatrix = new MultiFormatWriter().encode(new String(url.getBytes("UTF-8"), "UTF-8"), BarcodeFormat.QR_CODE, 100, 100); //将文字转换成二维矩阵，并设置矩阵大小，这里的矩阵大小就是后面生成的图片像素大小
 
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(byteMatrix);
 
            g.dispose();
 
            OutputStream out = response.getOutputStream();
 
            ImageIO.write(bufferedImage, "JPEG", out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
	
	
	
}

