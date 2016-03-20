/**
 * 
 */
package com.xyl.mmall.security.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.xyl.mmall.framework.util.EncryptUtils;

/**
 * @author lihui
 *
 */
public final class DigestUtils {
	
	private static Logger logger = Logger.getLogger(DigestUtils.class);

	private static final char[] BCD_LOOKUP = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	private static final String HEX_STR = "0123456789ABCDEF";

	/**
	 *
	 * @param bcd
	 * @return
	 */
	public static String byte2Hex(byte[] bcd) {
		StringBuilder sb = new StringBuilder(bcd.length * 2);
		for (int i = 0; i < bcd.length; i++) {
			sb.append(BCD_LOOKUP[(bcd[i] >>> 4) & 0x0f]);
			sb.append(BCD_LOOKUP[bcd[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] hex2Byte(String hexStr) {
		char[] hexs = hexStr.toUpperCase().toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int iTmp = 0x00;
		for (int i = 0; i < bytes.length; i++) {
			iTmp = HEX_STR.indexOf(hexs[2 * i]) << 4;
			iTmp |= HEX_STR.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (iTmp & 0xFF);
		}
		return bytes;
	}

	/**
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encryptAES(String content, byte[] password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password, "AES");
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			// 初始化
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (Exception e) {
			logger.error("Encrypt AES error!", e);
		}
		return null;
	}

	/**
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decryptAES(byte[] content, byte[] password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password, "AES");
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 初始化
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 解密
			byte[] result = cipher.doFinal(content);
			return new String(result);
		} catch (Exception e) {
			logger.error("Decrypt AES error!", e);
		}
		return null;
	}

	public static String decryptDES(byte[] content, byte[] password) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			byte[] result = cipher.doFinal(content);
			return new String(result);
		} catch (Exception e) {
			logger.error("Decrypt DES error!", e);
		}
		return null;
	}

	public static byte[] encryptDES(String content, byte[] password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 获取数据并加密
			byte[] byteContent = content.getBytes("utf-8");
			return cipher.doFinal(byteContent);
		} catch (Exception e) {
			logger.error("Encrypt DES error!", e);
		}
		return null;
	}

	public static void main(String[] args) {
//		String data = "type=0&username=lvtest@163.com&password=6eebd813af9c82fb368a1e54086ff62b";
//		String password = "564BFAF3C02C564DA62B8673F6D7C87A";
//		String result = byte2Hex(encryptAES(data, hex2Byte(password)));
//		System.out.println(result);
//		String password2 = "a37a6d27bc202f5122119e371fb4a1b7";
//		String data2 = "7929DD410506B7FE6191BAB37286615589B45A93940F53A55E5BDAF3FBB851D6050B8FF4DD17CD8CBA89E72D39246776E7D76AE79E42E474C551EFE016A5B7CFD39373CA8B72D6DA70693426672A1739B3E27837A0BE7A306F1246C7C609870BABFFB9EE1AFD8F4823CD7C5E0B4D8951";
//		System.out.println(decryptAES(hex2Byte(data2), hex2Byte(password2)));
//		String resultDES = byte2Hex(encryptDES(data, hex2Byte(password)));
//		System.out.println(resultDES);
//		System.out.println(decryptDES(hex2Byte(resultDES), hex2Byte(password)));
		System.out.println(EncryptUtils.getMD5("7929DD410506B7FE6191B"));
		String encryStr = byte2Hex(encryptAES("111111", hex2Byte("8b31b0cc107cbdd80ddad0843c20601d")));
		System.out.println(encryStr);
		encryStr = byte2Hex(encryptAES(encryStr, hex2Byte(EncryptUtils.getMD5("7929DD410506B7FE6191B"))));
		System.out.println(encryStr);
		encryStr = byte2Hex(encryptAES(encryStr, hex2Byte(EncryptUtils.getMD5("1444812060722"))));
		System.out.println(encryStr);
		System.out.println(encryStr.length());
		
		String decryptStr = decryptAES(hex2Byte(encryStr), hex2Byte(EncryptUtils.getMD5("1444812060722")));
		System.out.println(decryptStr);
		decryptStr = decryptAES(hex2Byte(decryptStr), hex2Byte(EncryptUtils.getMD5("7929DD410506B7FE6191B")));
		System.out.println(decryptStr);
		decryptStr = decryptAES(hex2Byte(decryptStr), hex2Byte("8b31b0cc107cbdd80ddad0843c20601d"));
		System.out.println(decryptStr);
		
	}
	
}
