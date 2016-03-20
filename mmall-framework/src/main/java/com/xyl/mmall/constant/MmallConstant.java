/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MmallConstant.java created by yydx811 at 2015年8月12日 上午10:44:44 常量
 *
 * @author yydx811
 */
public class MmallConstant {

	/** backend账号后缀. */
	public static final String BUSINESS_ACCOUNT_SUFFIX = "@st.xyl";

	/** cms账号后缀. */
	public static final String CMS_ACCOUNT_SUFFIX = "@op.xyl";

	/** mainsite账号后缀. */
	public static final String MAINSITE_ACCOUNT_SUFFIX = "@zy.xyl";

	/** cms登录成功cookie. */
	public static final String XYL_CMS_SESS = "XYLCMSSESS";

	/** cms cookie有效期. */
	public static final String XYL_CMS_EXPIRES = "XYLCMSEXP";

	/** cms登录失败cookie. */
	public static final String XYL_CMS_FAILED = "XYLCMSERR";

	/** cms用户名cookie. */
	public static final String XYL_CMS_USERNAME = "XYLCMSUN";

	/** backend登录成功cookie. */
	public static final String XYL_BACKEND_SESS = "XYLBACKENDSESS";

	/** backend cookie有效期. */
	public static final String XYL_BACKEND_EXPIRES = "XYLBACKENDEXP";

	/** backend登录失败cookie. */
	public static final String XYL_BACKEND_FAILED = "XYLBACKENDERR";

	/** backend用户名cookie. */
	public static final String XYL_BACKEND_USERNAME = "XYLBACKENDUN";

	/** mainsite登录成功cookie. */
	public static final String XYL_MAINSITE_SESS = "XYLSESS";

	/** mainsite cookie有效期. */
	public static final String XYL_MAINSITE_EXPIRES = "XYLEXP";

	/** mainsite登录失败cookie. */
	public static final String XYL_MAINSITE_FAILED = "XYLERR";

	/** mainsite用户名cookie. */
	public static final String XYL_MAINSITE_USERNAME = "XYLUN";

	/** mainsite代客cookie. */
	public static final String XYL_MAINSITE_PROXY = "XYLPROXY";

	/** 修改密码验证码类型. */
	public static final String SMS_PASS_CODE_TYPE = "P";

	/** 绑定手机验证码类型. */
	public static final String SMS_MOBILE_CODE_TYPE = "B";

	/** 绑定手机验证码重发间隔. */
	public static final String MOBILE_CODE_INTERVAL = "mobile.code.interval";

	/** 绑定手机验证码重发间隔时间. */
	public static final long MOBILE_CODE_INTERVAL_TIME = 45l * 1000l;

	/** 重置密码验证码重发间隔. */
	public static final String PASS_CODE_INTERVAL = "pass.code.interval";

	/** 重置密码验证码重发间隔时间. */
	public static final long PASS_CODE_INTERVAL_TIME = 45l * 1000l;

	/** 重置密码验证码校验成功时效. */
	public static final String PASS_CODE_SUCCESS_EXPIRE = "pass.code.success";

	/** 重置密码验证码校验成功时效时间. */
	public static final long PASS_CODE_SUCCESS_EXPIRE_TIME = 5l * 60l * 1000l;

	/** 主站用户名. */
	public static final String COOKIE_USER_NICK_NAME = "userNickName";

	/** 主站域名. */
	public static final String MAIN_SITE_DOMAIN = ".023.baiwandian.cn";

	/** 买家首页excel模板总共列数. */
	public static final int MAIN_SITE_INDEX_TEMPLATE_CELLNUM = 103;

	/** 买家首页excel模板商品 要填写skuId的列 . */
	public static Set<Integer> SKU_CELL_NUM_SET = new HashSet<Integer>(Arrays.asList(new Integer[] { 9, 17, 26 }));

	/** 买家首页excel模板商品 要填写侧边栏背景图片的列 . */
	public static Set<Integer> BACKGROUND_CELL_NUM_SET = new HashSet<Integer>(Arrays.asList(new Integer[] { 24, 37, 51,
			65, 79, 93 }));

	/** sql批量取 限制1000 . */
	public static final int QUERY_SELECT_LIMIT_NUM = 1000;

	public static final Map<Long, Long> directCityCodeMap = new HashMap<Long, Long>();

	static {
		directCityCodeMap.put(50l, -5001l);// 重庆
		directCityCodeMap.put(11l, -1101l);// 北京
		directCityCodeMap.put(12l, -1201l);// 天津
		directCityCodeMap.put(31l, -3101l);// 上海
	}

}
