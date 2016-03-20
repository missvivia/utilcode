/**
 * ==================================================================
 * Copyright (c) XINYUNLIAN Co.ltd Hangzhou, 2015-2016
 * 
 * 杭州新云联技术有限公司拥有该文件的使用、复制、修改和分发的许可权
 * 如果你想得到更多信息，请访问 <http://www.xinyunlian.com>
 *
 * XINYUNLIAN Co.ltd Hangzhou owns permission to use, copy, modify and
 * distribute this documentation.
 * For more information, please see <http://www.xinyunlian.com>
 * ==================================================================
 */

package com.xyl.mmall.framework.util;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EncryptUtils.java created by skh at 2015年6月8日 下午3:24:35
 * 
 *
 * @author skh
 * @version 1.0
 */

public class EncryptUtils
{
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);
    
    public static String getMD5(String s)
    {
        char hexDigits [] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f' };
        if (s == null)
            return null;
        try
        {
            byte [] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte [] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str [] = new char [j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e)
        {
            logger.error("There is an exception when generate MD5 in class EncrytpUtils!", e);
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println(getMD5("32001202d302b087-809a-4c10-b96c-3630cd87c1e31436854750"));
    }
}
