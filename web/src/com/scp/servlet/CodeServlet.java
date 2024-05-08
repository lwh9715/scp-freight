package com.scp.servlet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.scp.util.MessageUtils;

public class CodeServlet {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private CodeServlet() {
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format "
					+ format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format "
					+ format);
		}
	}
	/**
	 * 生成本地二维码
	 * visturl 扫二维码跳转url
	 * piurl 二维码生成在磁盘的位置
	 */
	public static void CreatePiByUrl(int width,int height,String imagetype,String visturl,String piurl){
        Hashtable hints = new Hashtable(); 
        //内容所使用编码 
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(visturl, 
			        BarcodeFormat.QR_CODE, width, height, hints); 
			//生成二维码 
			File outputFile = new File(piurl); 
			writeToFile(bitMatrix, imagetype, outputFile);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			MessageUtils.alert("生成二维码有误!");
			e.printStackTrace();
		} 
	}
}
