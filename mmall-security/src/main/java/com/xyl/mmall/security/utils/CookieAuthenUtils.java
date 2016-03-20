/**
 * ==================================================================
 * 
 *杭州网新新云联技术有限公司 -- B2B
 *
 * ==================================================================
 */

package com.xyl.mmall.security.utils;

import java.security.SignatureException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * CookieAuthenUtils.java created by skh at 2015年4月27日 下午10:20:19
 * 
 *
 * @author skh
 * @version 1.0
 */

public class CookieAuthenUtils
{
    
    private static final String HMAC_SHA1  = "HmacSHA1";
    private static final String K_APPEND   = "x!yL";
    private static final String SERVER_KEY = "2dc3ecea-571f-47fc-9ab0-92e34a66bd55";
    
    public static String calculateHMAC(String data, String key) throws SignatureException
    {
        String result;
        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            
            byte [] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64String(rawHmac);
        }
        catch (Exception e)
        {
            throw new SignatureException("Failed to generate HMAC: " + e.getMessage());
        }
        
        return result;
    }
    
    public static String calculateDataK(String data, String encryptionKey) throws Exception
    {
        IvParameterSpec iv = new IvParameterSpec("c00ki1@mmaLL4B#b".getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
    }
    
    public static String generateToken(String username, String data, String expires) throws Exception
    {
        String k = calculateHMAC(username + expires, SERVER_KEY);
        String dataK = calculateDataK(data, k + K_APPEND);
        String token = calculateHMAC(username + expires + dataK, k);
        return token;
    }
}
