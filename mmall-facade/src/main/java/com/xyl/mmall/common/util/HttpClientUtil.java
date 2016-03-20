package com.xyl.mmall.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.NoRouteToHostException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年8月11日下午5:34:42
 */
public class HttpClientUtil {
    
    // 读取数据超时时间 单位：毫秒
    public static final int                           READ_TIMEOUT       = 3000;

    // 连接超时时间 单位：毫秒
    public static final int                           CONNECTION_TIMEOUT = 3000;
   
    public static String                                      CHARSET               ="UTF-8";
   
    private static String urlEncode(String text) {
        try {
            return URLEncoder.encode(text, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("url encode exception: " + e.toString());
        }
    }

    public static String generateRequestString(String url,
                                                        Map<String, String> paramMap) {
        StringBuilder builder = new StringBuilder(512);
        builder.append(url);
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (null == value) {
                continue;
            }
            if (isFirst) {
                builder.append("?");
                isFirst = false;
            } else {
                builder.append("&");
            }

            builder.append(urlEncode(key));
            builder.append("=");
            builder.append(urlEncode(value));
        }

        return builder.toString();
    }

    public static String sendHttpGet(String url) throws HttpException,IOException {
         //未使用MultiThreadedHttpConnectionManager.牺牲一点性能，避免了最大连接数的设置(需要根据容器数和任务数进行调整)
         HttpClient httpClient = new HttpClient();

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
       
        HttpMethod httpMethod = new GetMethod(url);
        httpMethod.getParams().setSoTimeout(READ_TIMEOUT);
        httpMethod.getParams().setContentCharset(CHARSET);
        //覆盖掉默认的重试策略，不重试
       // httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0,false));
        try{
             int statusCode = httpClient.executeMethod(httpMethod);
             if (statusCode != HttpStatus.SC_OK) {
                 throw new HttpException("status code not 200,but "+statusCode);
               }
               // Read the response body.
             return httpMethod.getResponseBodyAsString();
        }finally{
             httpMethod.releaseConnection();
             httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    public static String sendHttpPost(String destUrl, Map<String, String> httpParams)
            throws HttpException, IOException {
         //未使用MultiThreadedHttpConnectionManager.牺牲一点性能，避免了最大连接数的设置
         HttpClient httpClient = new HttpClient();
         //默认重试3次
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
       
        HttpMethod httpMethod = new PostMethod(destUrl);
        httpMethod.getParams().setSoTimeout(READ_TIMEOUT);
        httpMethod.getParams().setContentCharset(CHARSET);
        //覆盖掉默认的重试策略，不重试
       // httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0,false));
       
        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : httpParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key!=null&&value!=null) {
                 NameValuePair nameValuePair=new NameValuePair();
                 nameValuePair.setName(key);
                 nameValuePair.setValue(value);
                 nameValuePairs.add(nameValuePair);
            }
        }
        NameValuePair[] nameValuePairArray=new NameValuePair[nameValuePairs.size()];
        nameValuePairs.toArray(nameValuePairArray);
        httpMethod.setQueryString(nameValuePairArray);
        try{
             int statusCode = httpClient.executeMethod(httpMethod);
             if (statusCode != HttpStatus.SC_OK) {
                 throw new HttpException("status code not 200,but "+statusCode);
               }
             InputStream inputStream = httpMethod.getResponseBodyAsStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(
                     inputStream));
             StringBuffer stringBuffer = new StringBuffer();
             String str = "";
             while ((str = br.readLine()) != null) {
                 stringBuffer.append(str);
             }
             return  stringBuffer.toString();
            
        }finally{
             httpMethod.releaseConnection();
             httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
       
    }
}


    
/**
* 只对建立连接前的网络因素进行重连：<br/>
* 1.建立连接超时；2.发送数据不完整
*
* @author zhimin.tangzm
*
*/
class SimpleHttpMethodRetryHandler implements HttpMethodRetryHandler{
     private static final int RETRY_COUNT=3;
    
     public boolean retryMethod(final HttpMethod method, final IOException exception, int executionCount) {
       if (method == null) {
           throw new IllegalArgumentException("HTTP method may not be null");
       }
       if (exception == null) {
           throw new IllegalArgumentException("Exception parameter may not be null");
       }
      
       if (executionCount > RETRY_COUNT) {
           // Do not retry if over max retry count
           return false;
       }
      
       //考虑到容器response时不能确定任务是否开始，只对建立连接前的错误进行重试。
       //实际上，DefaultHttpMethodRetryHandler对以下情况是不重试的
       if (exception instanceof ConnectTimeoutException
                 ||exception instanceof UnknownHostException
                 ||exception instanceof NoRouteToHostException) {
             return true;
       }
      
       if (!method.isRequestSent()) {
           // Retry if the request has not been sent fully
           return true;
       }
       // otherwise do not retry
       return false;
   }
}