package com.xyl.mmall.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.util.AreaUtils;

/**
 * 
 * 
 * @author wangfeng
 *
 */
@Controller
public class CommonController {

	private static String[] PROVINCE_SUPER_USERS = { "missdev@163.com" };

	@Autowired
	protected Environment env;

	@Autowired
	private IPServiceFacade ipServiceFacade;
	
	@Autowired
	private LocationFacade locationFacade;

	@PostConstruct
	public void init() {
		String provinceSuperUsers = env.getProperty("province.super.users");
		String[] superUserArray = StringUtils.split(provinceSuperUsers, ",");
		PROVINCE_SUPER_USERS = superUserArray;
	}

	// ------------- ip省份相关 -------------
	/**
	 * 省份切换.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
//	@RequestMapping(value = "/province/s", method = RequestMethod.GET)
	public void provinceSwitch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String springProfilesActive = env.getProperty("spring.profiles.active");
		String userName = SecurityContextUtils.getUserName();
		Map<String, Object> map = new HashMap<>();
		// dev/test mode或超级名单都可以访问
		boolean isAttempt = springProfilesActive.equalsIgnoreCase("test")
				|| springProfilesActive.equalsIgnoreCase("dev")
				|| springProfilesActive.equalsIgnoreCase("performance")
				|| CollectionUtil.isInArray(PROVINCE_SUPER_USERS, userName);
		if (isAttempt) {
			String provinceName = ServletRequestUtils.getStringParameter(request, "p", "浙江");
			long areaCode = ipServiceFacade.getProvinceCode(provinceName);
			AreaUtils.setAreaCookie((HttpServletResponse) response, areaCode);
			map.put("result", true);
		} else {
			map.put("result", false);
			map.put("msg", "403 Forbidden");
		}
		printJsonResult(response, JsonUtils.toJson(map));
	}

	/**
	 * 省份查看.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping(value = "/province/cookie", method = RequestMethod.GET)
	public void provinceCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
		long areaCode = AreaUtils.getAreaCode();
		String provinceName = ipServiceFacade.getProvinceName(areaCode);
		Map<String, Object> map = new HashMap<>();
		map.put("areaCode", areaCode);
		map.put("province", provinceName);
		printJsonResult(response, JsonUtils.toJson(map));
	}
	
	
	/**
	 * 区切换.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
//	@RequestMapping(value = "/section/s", method = RequestMethod.GET)
	public void sectionSwitch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<>();
		Long sectionCode = ServletRequestUtils.getLongParameter(request, "s", 500112l); // 默认重庆市 渝北区
		if (sectionCode != 500112l) {
			LocationCode locationCode = locationFacade.getLocationCodeByCode(sectionCode);
			if (locationCode == null || locationCode.getCode() != sectionCode) {
				sectionCode = 500112l;
			}
		}
		AreaUtils.setAreaCookie((HttpServletResponse) response, sectionCode);
		map.put("result", true);
		printJsonResult(response, JsonUtils.toJson(map));
	}

	public void printJsonResult(HttpServletResponse response, String result) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(result);
	}

}
