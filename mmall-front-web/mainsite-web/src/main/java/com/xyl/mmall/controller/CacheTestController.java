package com.xyl.mmall.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.UserProfileFacade;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;

@Controller
public class CacheTestController {

	private static final Map<String, Object> accessMap = new HashMap<>();

	// init
	{
		accessMap.put("zhaozhenzuo@126.com", "1");
		accessMap.put("zhaozhenzuo123@163.com", "1");
		accessMap.put("missdev@126.com", "1");
	}

	@Autowired
	private EhCacheCacheManager ehCacheCacheManager;

	@Autowired
	private UserProfileFacade userProfileFacade;
	
/*	@Autowired
	private CacheAreaMetaStoreOfMap cacheAreaMetaStoreOfMap;*/
	
	@RequestMapping(value = "/cache/cal", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO calculateSize(@RequestParam String name) {
		long userId = SecurityContextUtils.getUserId();
		BaseJsonVO vo = new BaseJsonVO();

		MainSiteUserVO mainSiteUserVo = userProfileFacade.getUserProfile(userId);
		if (mainSiteUserVo == null || accessMap.get(mainSiteUserVo.getUserProfile()) == null
				|| accessMap.get(mainSiteUserVo.getUserProfile().getUserName()) == null) {
			vo.setResult("can not access");
			return vo;
		}

		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(name);

		Ehcache nativeCache = cache.getNativeCache();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("sizeMem", nativeCache.calculateInMemorySize());
		resultMap.put("nums", nativeCache.getSize());

		vo.setResult(resultMap);
		return vo;
	}

	@RequestMapping(value = "/cache/list", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO listCacheInfoForMainSite() {
		BaseJsonVO vo = new BaseJsonVO();

		long userId = SecurityContextUtils.getUserId();
		MainSiteUserVO mainSiteUserVo = userProfileFacade.getUserProfile(userId);
		if (mainSiteUserVo == null || mainSiteUserVo.getUserProfile() == null
				|| accessMap.get(mainSiteUserVo.getUserProfile().getUserName()) == null) {
			vo.setResult("can not access");
			return vo;
		}

		Collection<String> cacheNameList = ehCacheCacheManager.getCacheNames();

		if (cacheNameList == null || cacheNameList.isEmpty()) {
			vo.setResult("没有找到mainSite的cache列表");
			return vo;
		}

		StringBuilder builder = new StringBuilder();

		for (String name : cacheNameList) {
			builder.append(this.getListInfoForCache(name));
		}

		vo.setResult(builder.toString());
		return vo;
	}
	
	@RequestMapping(value = "/cache/clean", method = RequestMethod.GET)
	public @ResponseBody
	BaseJsonVO cleanCache(@RequestParam String cacheName) {
		BaseJsonVO vo = new BaseJsonVO();

		long userId = SecurityContextUtils.getUserId();
		MainSiteUserVO mainSiteUserVo = userProfileFacade.getUserProfile(userId);
		if (mainSiteUserVo == null || mainSiteUserVo.getUserProfile() == null
				|| accessMap.get(mainSiteUserVo.getUserProfile().getUserName()) == null) {
			vo.setResult("can not access");
			return vo;
		}
		
		Cache cacheTemp=ehCacheCacheManager.getCache(cacheName);
		if(cacheTemp==null){
			vo.setResult("can not find cache,"+cacheName);
			return vo;
		}
		
		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(cacheName);
		cache.clear();
		
		
		vo.setResult("成功删除:"+cacheName);
		return vo;
	}

	private String getListInfoForCache(String name) {
		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(name);
		Ehcache nativeCache = cache.getNativeCache();
		StringBuilder builder = new StringBuilder();
		builder.append("cache名称:" + name);
		builder.append(",内存使用大小:" + nativeCache.calculateInMemorySize());
		builder.append(",cache对象个数:" + nativeCache.getSize());
		builder.append(",最大使用堆大小:" + nativeCache.getCacheConfiguration().getMaxBytesLocalHeap());
		builder.append("----------------------------------------");
		return builder.toString();
	}
	
//	@RequestMapping(value = "/vcache/list", method = RequestMethod.GET)
//	public @ResponseBody
//	BaseJsonVO listVstoreCacheInfoForMainSite() {
//		BaseJsonVO vo = new BaseJsonVO();
//
//		long userId = SecurityContextUtils.getUserId();
//		MainSiteUserVO mainSiteUserVo = userProfileFacade.getUserProfile(userId);
//		if (mainSiteUserVo == null || mainSiteUserVo.getUserProfile() == null
//				|| accessMap.get(mainSiteUserVo.getUserProfile().getUserName()) == null) {
//			vo.setResult("can not access");
//			return vo;
//		}
//
//		StringBuilder builder = new StringBuilder();
//		
//		Iterator<Entry<Object, CacheAreaMeta>> iterator=cacheAreaMetaStoreOfMap.getAllStatics();
//		
//		while(iterator.hasNext()){
//			Entry<Object, CacheAreaMeta> entry=iterator.next();
//			builder.append("--------------------------------------");
//			builder.append("cache块："+entry.getKey().toString());
//			
//			//统计元素
//			CacheAreaMeta cacheAreaMeta=entry.getValue();
//			Map<Object, ElementInfo> allElementInfoMap=cacheAreaMeta.getAllElementInfo();
//			int num=0;
//			if(allElementInfoMap!=null){
//				num=allElementInfoMap.size();
//			}
//			builder.append(",共有对象:"+num+"个");
//			builder.append("---------------------------------------------");
//		}
//
//		vo.setResult(builder.toString());
//		return vo;
//	}
//	
}
