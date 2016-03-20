package com.xyl.mmall.ip.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public final class IPUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(IPUtils.class);

	private final static int INADDR4SZ = 4;
	
    private final static String ORIG_CLIENT_IP_HEADER     = "ORIG_CLIENT_IP";
   
    private final static String X_FORWARDED_FOR_HEADER    = "x-forwarded-for";
   
    private final static String PROXY_CLIENT_IP_HEADER    = "Proxy-Client-IP";
   
    private final static String WL_PROXY_CLIENT_IP_HEADER = "WL-Proxy-Client-IP";

	private IPUtils() {
	}

	/**
	 * 获取请求包里的真实IP地址(跳过前端代理)
	 * 
	 * @param request
	 *            请求包对象
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-From-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 如果通过了多级反向代理的话
		// 取X-Forwarded-For中最后一个非unknown的有效IP字符串(具体根据sa配置的nginx规则设置)
		if (StringUtils.isNotBlank(ip) && ip.indexOf(',') > -1) {
			String[] subIp = ip.split(",");
			if (subIp.length > 0) {
				for (String s : subIp) {
					if (!"unknown".equalsIgnoreCase(s))
						ip = StringUtils.trimToEmpty(s);
				}
			}
		}

		// 防止伪造ip.
		Pattern pattern = Pattern.compile("((2[0-4]\\d|25[0-5]|1?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|1?\\d?\\d)");
		Matcher matcher = pattern.matcher(ip);
		if (!matcher.matches()) {
			return "";
		}

		byte[] address = textToNumericFormatV4(ip);
		if (isInternalIp(address))
			return getNetIp();

		return ip;
	}

	/**
	 * 手机端ip地址获取.
	 * 
	 * @return
	 */
	public static String getMobileIpAddr() {
		return getNetIp();
	}

	public static String getNetIp() {
		boolean useIPv4 = true;
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> inetAddressList = Collections.list(intf.getInetAddresses());
				for (InetAddress inetAddress : inetAddressList) {
					// 外网IPv4
					if (!inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()) {
						String sAddr = inetAddress.getHostAddress().toUpperCase();
						boolean isIPv4 = inetAddress instanceof Inet4Address;
						if (useIPv4) {
							byte[] address = textToNumericFormatV4(sAddr);
							if (isIPv4 && !isInternalIp(address))
								return sAddr;
						} else {
							if (!isIPv4) {
								// drop ip6 port suffix
								int delim = sAddr.indexOf('%');
								return delim < 0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		return "";
	}

	public static boolean isInternalIp(byte[] address) {
		final byte b0 = address[0];
		final byte b1 = address[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
		case SECTION_1:
			return true;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4) {
				return true;
			}
		case SECTION_5:
			switch (b1) {
			case SECTION_6:
				return true;
			}
		default:
			return false;
		}
	}

	public static byte[] textToNumericFormatV4(String src) {
		if (src.length() == 0) {
			return null;
		}

		byte[] res = new byte[INADDR4SZ];
		String[] s = src.split("\\.", -1);
		long val;
		try {
			switch (s.length) {
			case 1:
				/*
				 * When only one part is given, the value is stored directly in
				 * the network address without any byte rearrangement.
				 */

				val = Long.parseLong(s[0]);
				if (val < 0 || val > 0xffffffffL)
					return null;
				res[0] = (byte) ((val >> 24) & 0xff);
				res[1] = (byte) (((val & 0xffffff) >> 16) & 0xff);
				res[2] = (byte) (((val & 0xffff) >> 8) & 0xff);
				res[3] = (byte) (val & 0xff);
				break;
			case 2:
				/*
				 * When a two part address is supplied, the last part is
				 * interpreted as a 24-bit quantity and placed in the right most
				 * three bytes of the network address. This makes the two part
				 * address format convenient for specifying Class A network
				 * addresses as net.host.
				 */

				val = Integer.parseInt(s[0]);
				if (val < 0 || val > 0xff)
					return null;
				res[0] = (byte) (val & 0xff);
				val = Integer.parseInt(s[1]);
				if (val < 0 || val > 0xffffff)
					return null;
				res[1] = (byte) ((val >> 16) & 0xff);
				res[2] = (byte) (((val & 0xffff) >> 8) & 0xff);
				res[3] = (byte) (val & 0xff);
				break;
			case 3:
				/*
				 * When a three part address is specified, the last part is
				 * interpreted as a 16-bit quantity and placed in the right most
				 * two bytes of the network address. This makes the three part
				 * address format convenient for specifying Class B net- work
				 * addresses as 128.net.host.
				 */
				for (int i = 0; i < 2; i++) {
					val = Integer.parseInt(s[i]);
					if (val < 0 || val > 0xff)
						return null;
					res[i] = (byte) (val & 0xff);
				}
				val = Integer.parseInt(s[2]);
				if (val < 0 || val > 0xffff)
					return null;
				res[2] = (byte) ((val >> 8) & 0xff);
				res[3] = (byte) (val & 0xff);
				break;
			case 4:
				/*
				 * When four parts are specified, each is interpreted as a byte
				 * of data and assigned, from left to right, to the four bytes
				 * of an IPv4 address.
				 */
				for (int i = 0; i < 4; i++) {
					val = Integer.parseInt(s[i]);
					if (val < 0 || val > 0xff)
						return null;
					res[i] = (byte) (val & 0xff);
				}
				break;
			default:
				return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return res;
	}
	
	   
 
   
    /**
     * 从ServletRequest中根据各种代理服务器规则，尽可能获取请求的原始客户端IP。
     * @param request
     * @return
     */
    public static String getOriginalClientIp(HttpServletRequest request) {
        String originalClientIp = null;

        originalClientIp = getFirstValidIp(request.getHeader(ORIG_CLIENT_IP_HEADER));
       
        if(originalClientIp == null) {
            originalClientIp = getFirstValidIp(request.getHeader(X_FORWARDED_FOR_HEADER));
        }
       
        if(originalClientIp == null) {
            originalClientIp = getFirstValidIp(request.getHeader(PROXY_CLIENT_IP_HEADER));
        }
       
        if(originalClientIp == null) {
            originalClientIp = getFirstValidIp(request.getHeader(WL_PROXY_CLIENT_IP_HEADER));
        }
       
        if(originalClientIp == null) {
            originalClientIp = request.getRemoteAddr();
        }

        return originalClientIp;
    }
   
    /**
     * 获取IP地址代理链中第一个合法的IPV4地址后返回。
     * 注：代理链以半角逗号分隔。
     * @param proxyAddressChain
     * @return
     */
    private static String getFirstValidIp(String proxyAddressChain) {
        String firstIp = null;
        String[] ips = StringUtils.split(proxyAddressChain, ',');
        if(ips != null ) {
            for(String ip : ips) {
                ip = ip.trim();
                if(ip.length()!=0&&!StringUtils.equals("unknown", ip)) {
                    firstIp = ip;
                    break;
               }
            }
        }
        return firstIp;
    }


}
