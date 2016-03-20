package com.xyl.mmall.controller;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.jms.service.util.ResourceTextUtil;
import com.xyl.mmall.mainsite.util.ChannelCacheHelper;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.mainsite.util.SignUtilOfCommon;
import com.xyl.mmall.mainsite.vo.ChannelDataVo;
import com.xyl.mmall.mainsite.vo.MainSiteDataVo;
import com.xyl.mmall.obj.MainSiteKeyGenerator;
import com.xyl.mmall.util.AopTargetUtils;

/**
 * 提供缓存预热功能
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Controller
@RequestMapping(value = "/m")
public class MainSiteCacheController {

	@Autowired
	private EhCacheCacheManager ehCacheCacheManager;

	@Autowired
	private MainsiteHelper mainsiteHelper;
	
	@Autowired
	private ScheduleController scheduleController;

	private static final Logger logger = LoggerFactory.getLogger(MainSiteCacheController.class);

	private static final String INDEX_PAGE_OLD_CACHE = "mainSiteCache";

	private static final String INDEX_PAGE_FRESH_CACHE = "mainSiteFreshCache";

	@Autowired
	private ChannelCacheHelper channelCacheHelper;

	@Autowired
	private MainSiteKeyGenerator mainSiteKeyGenerator;
	
	private static final ResourceBundle contentResourceBundle = ResourceTextUtil
			.getResourceBundleByName("config.content");

	private static final String secretKey = ResourceTextUtil.getTextFromResourceByKey(contentResourceBundle,
			"secret.key");

	/**
	 * 首页，为当天准备档期开始之后的新缓存块内容
	 * <p>
	 * 模拟当天档期开始之后访问
	 * 
	 * @param curSupplierAreaId
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@RequestMapping(value = "/presync/indexpage/fresh/currentDay", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO presyncIndexpageForFreshCurrentDay(@RequestParam long curSupplierAreaId) throws NoSuchMethodException,
			SecurityException {
		BaseJsonVO vo = new BaseJsonVO();
		logger.info("===begin presync fresh indexpage cache");

		// 安全认证
		if (!this.canAccess()) {
			logger.error("无权限访问");
			vo.setCode(ErrorCode.NOT_SUPPORT);
			vo.setResult("无访问权限");
			return vo;
		}

		Cache cacheTemp = ehCacheCacheManager.getCache(INDEX_PAGE_FRESH_CACHE);
		if (cacheTemp == null) {
			vo.setResult("can not find cache," + INDEX_PAGE_FRESH_CACHE);
			return vo;
		}

		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(INDEX_PAGE_FRESH_CACHE);

		// 目标对象
		Object targetObject = this.getTargetObject(mainsiteHelper);

		// 预先填充新的10后需要访问的缓存块
		Method method = mainsiteHelper.getClass().getMethod("getMainSiteFromFreshCache", Long.class);
		Object elementKey = mainSiteKeyGenerator.generate(targetObject, method, curSupplierAreaId);
		cache.evict(elementKey);

		// 模拟10点档期开始后调用
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, CodeInfoUtil.PO_BEGIN_HOUR + 1);
		MainSiteDataVo mainSiteDataVo = mainsiteHelper.getHomeDataInner(curSupplierAreaId, calendar.getTime());

		cache.put(elementKey, mainSiteDataVo);

		boolean success = mainSiteDataVo != null ? true : false;

		logger.info("===finish presync fresh indexpage cache:" + INDEX_PAGE_FRESH_CACHE + ",curSupplierAreaId:"
				+ curSupplierAreaId);

		vo.setCode(success ? ErrorCode.SUCCESS : ErrorCode.NOT_SUPPORT);
		return vo;
	}

	private Object getTargetObject(Object proxyObj) {
		try {
			return AopTargetUtils.getTarget(proxyObj);
		} catch (Exception e) {
			logger.error("===can not get aopTargetProxy", e);
		}
		return proxyObj;
	}

	/**
	 * 首页，为当天准备档期开始之前的旧缓存块内容
	 * <p>
	 * 模拟当天档期开始之前的访问
	 * 
	 * @param curSupplierAreaId
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@RequestMapping(value = "/presync/indexpage/old/currentDay", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO presyncIndexpageForOldCurrentDay(@RequestParam long curSupplierAreaId) throws NoSuchMethodException,
			SecurityException {
		BaseJsonVO vo = new BaseJsonVO();
		logger.info("===begin presync old indexpage cache");

		// 安全认证
		if (!this.canAccess()) {
			logger.error("无权限访问");
			vo.setCode(ErrorCode.NOT_SUPPORT);
			vo.setResult("无访问权限");
			return vo;
		}

		Cache cacheTemp = ehCacheCacheManager.getCache(INDEX_PAGE_OLD_CACHE);
		if (cacheTemp == null) {
			vo.setResult("can not find cache," + INDEX_PAGE_OLD_CACHE);
			return vo;
		}

		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(INDEX_PAGE_OLD_CACHE);

		// 目标对象
		Object targetObject = this.getTargetObject(mainsiteHelper);

		// 预先填充新的10后需要访问的缓存块
		Method method = mainsiteHelper.getClass().getMethod("getMainSiteFromOldCache", Long.class);
		Object elementKey = mainSiteKeyGenerator.generate(targetObject, method, curSupplierAreaId);
		cache.evict(elementKey);

		// 模拟10点档期开始后调用
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, CodeInfoUtil.PO_BEGIN_HOUR - 1);
		MainSiteDataVo mainSiteDataVo = mainsiteHelper.getHomeDataInner(curSupplierAreaId, calendar.getTime());

		cache.put(elementKey, mainSiteDataVo);

		boolean success = mainSiteDataVo != null ? true : false;

		logger.info("===finish presync old indexpage cache:" + INDEX_PAGE_OLD_CACHE + ",curSupplierAreaId:"
				+ curSupplierAreaId);

		vo.setCode(success ? ErrorCode.SUCCESS : ErrorCode.NOT_SUPPORT);
		return vo;
	}

	/**
	 * 频道页，为当天准备档期开始之后的新缓存块内容
	 * <p>
	 * 模拟当天档期开始之后访问
	 * 
	 * @param curSupplierAreaId
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@RequestMapping(value = "/presync/channelpage/fresh/currentDay", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO presyncChannelpageForFreshCurrentDay(@RequestParam int channelType, @RequestParam long curSupplierAreaId)
			throws NoSuchMethodException, SecurityException {
		BaseJsonVO vo = new BaseJsonVO();
		logger.info("===begin presync fresh channelpage cache");

		// 安全认证
		if (!this.canAccess()) {
			logger.error("无权限访问");
			vo.setCode(ErrorCode.NOT_SUPPORT);
			vo.setResult("无访问权限");
			return vo;
		}

		Cache cacheTemp = ehCacheCacheManager.getCache(INDEX_PAGE_FRESH_CACHE);
		if (cacheTemp == null) {
			vo.setResult("can not find cache," + INDEX_PAGE_FRESH_CACHE);
			return vo;
		}

		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(INDEX_PAGE_FRESH_CACHE);

		// 目标对象
		Object targetObject = this.getTargetObject(channelCacheHelper);

		// 预先填充新的10后需要访问的缓存块
		Method method = channelCacheHelper.getClass().getMethod("getChannelDataForFresh", Integer.class, Long.class);
		Object elementKey = mainSiteKeyGenerator.generate(targetObject, method, channelType, curSupplierAreaId);
		cache.evict(elementKey);

		// 模拟10点档期开始后调用频道页
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, CodeInfoUtil.PO_BEGIN_HOUR + 1);
		ChannelDataVo channelDataVo = channelCacheHelper.getChannelDataInner(channelType, curSupplierAreaId,
				calendar.getTime());

		cache.put(elementKey, channelDataVo);

		boolean success = channelDataVo != null ? true : false;

		logger.info("===finish presync fresh channelpage cache:" + INDEX_PAGE_FRESH_CACHE + ",channelType:"
				+ channelType + ",curSupplierAreaId:" + curSupplierAreaId);

		vo.setCode(success ? ErrorCode.SUCCESS : ErrorCode.NOT_SUPPORT);
		return vo;
	}

	/**
	 * 频道页，为当天准备档期开始之前的旧缓存块内容
	 * <p>
	 * 模拟当天档期开始之前访问
	 * 
	 * @param curSupplierAreaId
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@RequestMapping(value = "/presync/channelpage/old/currentDay", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO presyncChannelpageForOldCurrentDay(@RequestParam int channelType, @RequestParam long curSupplierAreaId)
			throws NoSuchMethodException, SecurityException {
		BaseJsonVO vo = new BaseJsonVO();
		logger.info("===begin presync old channelpage cache");

		// 安全认证
		if (!this.canAccess()) {
			logger.error("无权限访问");
			vo.setCode(ErrorCode.NOT_SUPPORT);
			vo.setResult("无访问权限");
			return vo;
		}

		Cache cacheTemp = ehCacheCacheManager.getCache(INDEX_PAGE_OLD_CACHE);
		if (cacheTemp == null) {
			vo.setResult("can not find cache," + INDEX_PAGE_OLD_CACHE);
			return vo;
		}

		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(INDEX_PAGE_OLD_CACHE);

		// 目标对象
		Object targetObject = this.getTargetObject(channelCacheHelper);

		// 预先填充旧的档期开始之前的缓存块
		Method method = channelCacheHelper.getClass().getMethod("getChannelDataForOld", Integer.class, Long.class);
		Object elementKey = mainSiteKeyGenerator.generate(targetObject, method, channelType, curSupplierAreaId);
		cache.evict(elementKey);

		// 模拟10点档期开始后调用频道页
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, CodeInfoUtil.PO_BEGIN_HOUR - 1);
		ChannelDataVo channelDataVo = channelCacheHelper.getChannelDataInner(channelType, curSupplierAreaId,
				calendar.getTime());

		cache.put(elementKey, channelDataVo);

		boolean success = channelDataVo != null ? true : false;

		logger.info("===finish presync old channelpage cache:" + INDEX_PAGE_FRESH_CACHE + ",channelType:" + channelType
				+ ",curSupplierAreaId:" + curSupplierAreaId);

		vo.setCode(success ? ErrorCode.SUCCESS : ErrorCode.NOT_SUPPORT);
		return vo;
	}
	
	/**
	 * 用于品购页缓存预热
	 * 
	 * @param model
	 * @param pageId
	 * @param scheduleId
	 * @param sign
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/presync/schedulepage", method = RequestMethod.GET)
	public String mainSiteScheduleForPresync(Model model,
			@RequestParam(value = "pageId", required = false) Long pageId,
			@RequestParam(value = "scheduleId", required = false) Long scheduleId,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "timestamp", required = false) String timestamp) {
		boolean signIsTrue = SignUtilOfCommon.checkSign(secretKey, timestamp, sign);
		if (!signIsTrue) {
			return "cannot access";
		}
		return scheduleController._viewPageDetail(model, pageId, scheduleId, false);
	}

	private boolean canAccess() {
		return true;
	}

}
