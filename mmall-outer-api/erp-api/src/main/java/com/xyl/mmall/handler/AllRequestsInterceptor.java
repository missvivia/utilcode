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

package com.xyl.mmall.handler;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.EncryptUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.meta.ERPAccount;
import com.xyl.mmall.utils.ERPAccountUtils;

/**
 * AllRequestsInterceptor.java created by skh at 2015年6月8日 下午4:46:32
 * 
 *
 * @author skh
 * @version 1.0
 */
@Component
public class AllRequestsInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger logger   = LoggerFactory.getLogger(AllRequestsInterceptor.class);
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler)
    {
        Enumeration<String> parameterNames = request.getParameterNames();
        TreeMap<String, String> parametersMap = new TreeMap<String, String>(
                String.CASE_INSENSITIVE_ORDER);
        
        String appId = "";
        logger.info("the request uri is : " + request.getRequestURI());
        logger.info("the request parameters are:");
        while (parameterNames.hasMoreElements())
        {
            StringBuilder values = new StringBuilder();
            String parameterName = parameterNames.nextElement();
            String [] parameterValues = request.getParameterValues(parameterName);
            for (String value : parameterValues)
            {
                values.append(value + " ");
            }
            parametersMap.put(parameterName, values.toString().trim());
            logger.info(parameterName + " : " + parametersMap.get(parameterName));
            if (StringUtils.equalsIgnoreCase("appId", parameterName))
            {
            	appId = parametersMap.get(parameterName);
            }
        }
        // 获取erpaccount
        ERPAccount erpAccount = ERPAccountUtils.getERPAccountByAppId(appId);
        if (erpAccount == null || StringUtils.isBlank(erpAccount.getAppIdKey()))
        {
        	logger.error("Authentication Failure : Unexpected AppId!");
            try
            {
                BaseJsonVO json = new BaseJsonVO();
                json.setCode(ResponseCode.RES_ENOTAUTH);
                json.setMessage("Bad appid.");
                String jsonString = json.toJson();
                String fullContentType = "application/json;charset=UTF-8";
                response.setContentType(fullContentType);
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.getWriter().write(jsonString);
                response.getWriter().flush();
            }
            catch (IOException e)
            {
                logger.error(e.getMessage());
            }
        	return false;
        }
        
        StringBuilder raw = new StringBuilder();
        for (Map.Entry<String, String> entry : parametersMap.entrySet())
        {
            if (!"timetag".equals(entry.getKey()) && !"verifycode".equals(entry.getKey())
                    && !"appid".equals(entry.getKey()))
            {
                raw.append(entry.getValue());
            }
        }
        raw.append(erpAccount.getAppIdKey()).append(parametersMap.get("timetag"));
        String hashedVerify = EncryptUtils.getMD5(raw.toString());
        
        if (hashedVerify == null || (!hashedVerify.equals(parametersMap.get("verifyCode"))))
        {
            logger.error("Authentication Failure : Unexpected Verify Code, the requested  verifycode is "
                    + parametersMap.get("verifycode")
                    + ", the expected hashed verifycode is "
                    + hashedVerify);
            
            // response
            try
            {
                BaseJsonVO json = new BaseJsonVO();
                json.setCode(ResponseCode.RES_ENOTAUTH);
                json.setMessage("Bad verification code.");
                String jsonString = json.toJson();
                String fullContentType = "application/json;charset=UTF-8";
                response.setContentType(fullContentType);
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.getWriter().write(jsonString);
                response.getWriter().flush();
            }
            catch (IOException e)
            {
                logger.error("", e);
            }
            
            return false;
        }
        return true;
    }
}
