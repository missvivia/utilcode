/**
 * 
 */
package com.xyl.mmall.framework.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.DecoderException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

/**
 * @author jmy
 *
 */
public class ZXingUtil {
	private static final String CHARSET = "UTF-8";

	/**
	 * 创建对应参数<code>content</code>的二维码矩阵。
	 * 
	 * @param content 编码信息
	 * @param width 像素宽度
	 * @param height 像素高度
	 * @return
	 * @throws WriterException 
	 * @throws UnsupportedEncodingException 
	 */
	public static BitMatrix createQRCodeMatrix(String content, Integer width, Integer height) throws WriterException, UnsupportedEncodingException {
		return createBarCodeMatrix(BarcodeFormat.QR_CODE, content, width, height);
	}


	/**
	 * 根据点矩阵生成图片。
	 * @author jmy
	 *
	 * @param matrix
	 * @return
	 */
	public static BufferedImage createBufferedImage(BitMatrix matrix) {
		return MatrixToImageWriter.toBufferedImage(matrix);
	}

	/**
	 * 创建对应参数<code>content</code>的一维条码的矩阵
	 * <p>
	 * 一维条码只能用数字，字母，或符号。
	 * 如要需要代表中文意思，只能设置对应的参照表，可以把条码中的几位固定设置成类别，型号，颜色，供应商等信息。
	 * 
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 * @throws WriterException 
	 * @throws UnsupportedEncodingException 
	 */
	public static BitMatrix createBarCodeMatrix(String content, Integer width, Integer height) throws WriterException, UnsupportedEncodingException {
		return createBarCodeMatrix(BarcodeFormat.CODE_128, content, width, height);
	}
	
	/**
	 * 创建对应参数<code>content</code>的一维条码的矩阵
	 * <p>
	 * 一维条码只能用数字，字母，或符号。
	 * 如要需要代表中文意思，只能设置对应的参照表，可以把条码中的几位固定设置成类别，型号，颜色，供应商等信息。
	 * 
	 * @author jmy
	 *
	 * @param barcodeFormat
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 * @throws WriterException
	 * @throws UnsupportedEncodingException
	 */
	public static BitMatrix createBarCodeMatrix(BarcodeFormat barcodeFormat, String content, Integer width, Integer height) throws WriterException, UnsupportedEncodingException {
		// 文字编码
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);

		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, barcodeFormat, width, height, hints);

		return bitMatrix;
	}

	/**
	 * 解码
	 * @author jmy
	 *
	 * @param image
	 * @return
	 * @throws NotFoundException 
	 */
	public static String decode(BufferedImage image) throws NotFoundException {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		// 解码设置编码方式
		Map<DecodeHintType, String> hints = new HashMap<>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);

		String content = new MultiFormatReader().decode(bitmap, hints).getText();
		return content;
	}

	/**
	 * @author jmy
	 *
	 * @param args
	 * @throws WriterException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws URISyntaxException 
	 * @throws NotFoundException 
	 * @throws DecoderException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, WriterException, URISyntaxException, NotFoundException, DecoderException {
		MatrixToImageWriter.writeToStream(createQRCodeMatrix("jiaomingyang顶顶顶顶", 300, 300),"png", new FileOutputStream(new File("/tmp/QRCode.png")));
		MatrixToImageWriter.writeToStream(createBarCodeMatrix("6925303733704", 300, 100),"jpg", new FileOutputStream(new File("/tmp/BarCode.jpg")));
		System.out.println(decode(ImageIO.read(new File("/tmp/QRCode.png"))));
		System.out.println(decode(ImageIO.read(new File("/tmp/BarCode.jpg"))));
	}

}
