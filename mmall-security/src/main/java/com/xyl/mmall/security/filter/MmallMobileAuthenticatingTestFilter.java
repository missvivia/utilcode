/**
 * B2b & IDC
 */
package com.xyl.mmall.security.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.entity.ContentType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import com.netease.print.security.util.LoginUtils;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.security.meta.MmallAccessUser;
import com.xyl.mmall.security.meta.MmallAuthenticationToken;
import com.xyl.mmall.security.meta.MmallUserType;
import com.xyl.mmall.security.utils.CookieAuthenUtils;
import com.xyl.mmall.security.utils.CookieUtils;

/**
 * MmallCMSAuthenticatingTestFilter.java created by yydx811 at 2015年10月7日 下午2:40:58
 * mobile
 *
 * @author yydx811
 */
public class MmallMobileAuthenticatingTestFilter extends AccessControlFilter {
	
	private List<String> asyncRequestList = null;

	private long cookieTimeOut = 259200L;

	private String domain;

	private Collection<String> savedRequestUrls;

	protected static final String AUTHORIZATION_HEADER = "Authorization";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax
	 * .servlet .ServletRequest, javax.servlet.ServletResponse,
	 * java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.
	 * servlet .ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		boolean logined = false;
		if (isLoginAttempt(request, response)) {
			logined = executeLogin(request, response);
		}
		if (!logined) {
			writeFailedResultToResponse(response, ResponseCode.RES_ENOTAUTH, "用户未登录");
		}
		return logined;
	}

	protected void clearSavedRequest(ServletRequest request) {
		if (CollectionUtils.isEmpty(this.savedRequestUrls)) {
			WebUtils.getAndClearSavedRequest(request);
			return;
		}

		boolean clearSavedRequest = true;
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String requestURI = httpRequest.getRequestURI();
		for (String savedRequestUrl : this.savedRequestUrls) {
			if (requestURI.contains(savedRequestUrl)) {
				clearSavedRequest = false;
				break;
			}
		}
		if (clearSavedRequest) {
			WebUtils.getAndClearSavedRequest(request);
		}
	}

	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		return true;
	}

	protected String getTokenValue(ServletRequest request) {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		String value = httpServletRequest.getParameter(MmallConstant.XYL_MAINSITE_SESS);
		if (!StringUtils.hasText(value)) {
			return "";
		}
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	protected String getAuthzHeader(ServletRequest request) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		return httpRequest.getHeader("Authorization");
	}

	protected boolean isLogined(ServletRequest request, ServletResponse response) {
		boolean logined = false;

		Subject currentUser = getSubject(request, response);
		if (currentUser != null && currentUser.isAuthenticated()) {
			HttpServletRequest httpServletRequest = WebUtils.toHttp(request);

			Cookie exp = CookieUtils.getCookieByName(httpServletRequest, MmallConstant.XYL_MAINSITE_EXPIRES);
			if (exp == null || (Long.valueOf(exp.getValue()) > System.currentTimeMillis())) {
				return false;
			}
			
			MmallAccessUser mmallAccessUser = (MmallAccessUser) currentUser.getPrincipal();
			Cookie[] credentials = CookieUtils.getCredentials(httpServletRequest);
			if (mmallAccessUser != null && (mmallAccessUser.getUserType() == MmallUserType.NORMAL)
					&& credentials != null) {
				String username = mmallAccessUser.getVistorName();
				String credentialToken = (String) mmallAccessUser.getCredential();

				String requestToken;
				try {
					requestToken = CookieAuthenUtils.generateToken(username, credentials[0].getValue(), "");
					logined = credentialToken.equals(requestToken);
				} catch (Exception e) {
				}
			}
		}

		if (!logined) {
			currentUser.logout();
		}
		return logined;
	}

	public MmallAuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);

		Cookie exp = CookieUtils.getCookieByName(httpRequest, MmallConstant.XYL_MAINSITE_EXPIRES);
		String expires = "";
		if (exp != null && (Long.valueOf(exp.getValue()) > System.currentTimeMillis())) {
			long expiresTime = Long.valueOf(exp.getValue());
			DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z", Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			expires = df.format(new Date(expiresTime));
		} else {
			return null;
		}

		Cookie[] credentials = CookieUtils.getCredentials(httpRequest);
		if (credentials == null) {
			return null;
		}
		String username = CookieUtils.getCookieByName(httpRequest, MmallConstant.XYL_MAINSITE_USERNAME).getValue();

		int[] number = { 48, 120, 67, 65, 70, 69, 66, 65, 66, 69 };
		StringBuilder data = new StringBuilder();
		for (int i : number) {
			data.append("" + (char) i);
		}
		String requestToken = "";
		try {
			requestToken = CookieAuthenUtils.generateToken(username, data.toString(), expires);
			if (!requestToken.equals(credentials[0].getValue())) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

		try {
			String newCredential = CookieAuthenUtils.generateToken(username, requestToken, "");
			MmallAuthenticationToken token = new MmallAuthenticationToken(username, newCredential, MmallUserType.NORMAL);

			return token;
		} catch (Exception e) {
			return null;
		}
	}

	protected boolean executeLogin(ServletRequest request, ServletResponse response) {
		MmallAuthenticationToken token = createToken(request, response);
		if (token == null) {
			return false;
		}
		if (!StringUtils.hasText(token.getUsername())) {
			return false;
		}

		try {
			Subject subject = getSubject(request, response);
			subject.login(token);
			return true;
		} catch (AuthenticationException ae) {
			return LoginUtils.onLoginFailure(token, ae, request, response);
		}
	}

	/**
	 * @param message
	 * @param code
	 * @param response
	 * 
	 */
	private void writeFailedResultToResponse(ServletResponse response, int code, String message) {
		response.setContentType(ContentType.APPLICATION_JSON.toString());
		response.setCharacterEncoding("UTF-8");
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(code);
		result.setMessage(message);
		try {
			response.getWriter().print(JsonUtils.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCookieTimeOut(long cookieTimeOut) {
		this.cookieTimeOut = cookieTimeOut;
	}

	public long getCookieTimeOut() {
		return this.cookieTimeOut;
	}

	public void setSavedRequestUrls(Collection<String> savedRequestUrls) {
		if (savedRequestUrls == null) {
			throw new IllegalArgumentException("SavedRequestUrl collection argument cannot be null.");
		}
		if (savedRequestUrls.isEmpty()) {
			throw new IllegalArgumentException("SavedRequestUrl collection argument cannot be empty.");
		}
		this.savedRequestUrls = savedRequestUrls;
	}

	public void setSavedRequestUrl(String savedRequestUrl) {
		if (savedRequestUrl == null) {
			throw new IllegalArgumentException("SavedRequestUrl argument cannot be null");
		}
		Collection<String> savedRequestUrls = new ArrayList<String>(1);
		savedRequestUrls.add(savedRequestUrl);
		setSavedRequestUrls(savedRequestUrls);
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setAsyncRequestPath(String asyncRequestPath) {
		if (!StringUtils.isEmpty(asyncRequestPath)) {
			String[] array = asyncRequestPath.trim().split(",");
			asyncRequestList = Arrays.asList(array);
		}
	}
}
