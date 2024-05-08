package com.scp.vo.price;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
 
public class demo {
public static void main(String[] args) {
	try {
		Desktop.getDesktop().open(new File("C:\\Users\\Administrator\\Desktop\\INV.jpg"));
//		Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "C:\\Users\\Administrator\\Desktop\\INV2.jpg"});
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//        drawTextInImg(filePath2, "¥3199", outPath2, 60, 755, "red", 45.0f);//在图片上加商品价格
//        drawTextInImg(word2, "原价：3399", webFilePath_word3, 225, 865, "black", 20.0f);//在图片上加商品原价
        System.out.println("Hello World!");
    }
    public static void drawTextInImg() {
        ImageIcon imgIcon = new ImageIcon("C:\\Users\\Administrator\\Desktop\\INV.jpg");
        Image img = imgIcon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
 
        Graphics2D g = bimage.createGraphics();
 
        int fontSize = (int)20.0f;    //字体大小
//        int rectX = left_n;
//        int rectY = top_n;
//        int rectWidth = text.length() * fontSize;
//        int rectHeight = fontSize + 8;
 
        Font font = new Font("宋体",Font.BOLD, fontSize);   //定义字体
        g.setBackground(Color.white);
        g.drawImage(img, 0, 0, null);
        g.setPaint(Color.black);
        g.setFont(font);
        //g.drawRect(left_n, top_n, rectWidth, rectHeight);
        g.drawString("惠州安博臣科技有限公司", 200, 200);
        g.drawString("91441300MA54EQ074L", 200, 230);
        g.drawString("惠州仲恺高新区和畅东四路7号厂房2楼   0752-2607756", 200, 260);
        g.drawString("中国农业银行股份有限公司惠州仲恺支行  44227001040007176", 200, 290);
        for (int i = 0; i < 1; i++) {
        	g.drawString("*经纪代理服务*代理国际运费", 40, 360);
            g.drawString("票", 520, 360);
            g.drawString("1", 600, 360);
            g.drawString("1830.00", 700, 360);
            g.drawString("1830.00", 850, 360);
            g.drawString("免税", 1000, 360);
            g.drawString("***", 1100, 360);
		}
        g.drawString("1830.00", 850, 570);
        g.drawString("壹千捌佰叁拾圆整", 380, 605);
        g.drawString("1830.00", 1020, 605);
        g.drawString("张咏诗", 130, 770);
        g.drawString("林泽霞", 470, 770);
        g.drawString("张咏诗", 750, 770);
//        g.drawString("91442000MA565ECW5M", 50, 100);
//        g.drawString("91442000MA565ECW5M", 60, 120);
//        g.drawString("91442000MA565ECW5M", 70, 140);
        g.dispose();
 
        try {
            FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\INV2.jpg");
            ImageIO.write(bimage, "png", out);
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
 
 
 
}