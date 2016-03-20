/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.UserFeedback;
import com.xyl.mmall.ip.service.UserFeedbackService;
import com.xyl.mmall.mobile.facade.MobileAppInfoFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.converter.MobileConfig;
import com.xyl.mmall.mobile.facade.converter.MobileOS;
import com.xyl.mmall.mobile.facade.param.MobileFeedBackAO;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.mobile.facade.vo.MobileAppVO;
import com.xyl.mmall.mobile.facade.vo.MobileBannerImageVO;
import com.xyl.mmall.mobile.facade.vo.MobileBannerInfoVO;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobileAppInfoFacadeImpl implements MobileAppInfoFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PromotionContentFacade promotionContentFacade;

	@Resource
	private UserFeedbackService userFeedbackService;

	@Override
	public BaseJsonVO getBannerImage(int areaId, String os) {
		logger.info("getBannerImage -> areaId:<" + areaId + ">");
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {
			List<PromotionContent> images = promotionContentFacade.getMobilePCByProvTimeDevice(Converter.getTime(),
					areaId);
			if (images == null) {
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL);
			}
			List<MobileBannerImageVO> list = new ArrayList<MobileBannerImageVO>();
			for (PromotionContent p : images) {
				if (StringUtils.isNotBlank(p.getPlatformType()) && StringUtils.isNotBlank(os)) {
					if (!p.getPlatformType().toLowerCase().contains(os)) {
						continue;
					}
				}
				if (p.getOnline() == 1)
					continue;
				MobileBannerImageVO vo = new MobileBannerImageVO();
				vo.setId(p.getId());
				vo.setDesc(p.getTitle());
				if (p.getPromotionType() == 1 && StringUtils.isNotBlank(p.getActivityUrl())) {
					vo.setLinkUrl(p.getActivityUrl());
					vo.setType(1);
				} else {
					vo.setLinkUrl(1, p.getBusinessId());
					vo.setType(2);
				}
				vo.setImageUrl(p.getImgUrl());
				list.add(vo);
			}
			MobileBannerInfoVO vo = new MobileBannerInfoVO();
			vo.setImageBanners(list);
			vo.setBannerTitle(MobileConfig.po_new_titil);
			vo.setBannerURL("");

			return Converter.converterBaseJsonVO(vo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	// TODO
	@Override
	public BaseJsonVO getInitImage(int areaId, int os, String r) {
		logger.info("an app start...");
		ArrayList<MobileBannerImageVO> a = new ArrayList<MobileBannerImageVO>();

		MobileBannerImageVO v = new MobileBannerImageVO();
		
		try{
			String url =  MobileConfig.getMobileInitImageMap(r);
			v.setImageUrl(url);
			v.setLinkUrl(MobileConfig.mobile_init_image_linkto);
			if(StringUtils.isNotBlank(url))
				a.add(v);
			return Converter.listBaseJsonVO(a, false);
		} catch (Exception e) {
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	@Override
	public BaseJsonVO getRecommend(String channel, int areaId, MobileHeaderAO ao) {
		// 这个是临时的
		logger.info("channel:<" + channel + ">");
		MobileAppVO a = new MobileAppVO();
		a.setAppName("网易新闻");
		a.setAppDesc("有态度的新闻门户");
		a.setIconImage("http://vstore-photocenter-bucket2.nos.netease.com/89119e83-a642-4a27-bc07-5c7050b34117");

		MobileAppVO b = new MobileAppVO();
		b.setAppName("网易云阅读");
		b.setAppDesc("读之乐 阅之美");
		b.setIconImage("http://vstore-photocenter-bucket2.nos.netease.com/7e9d38bf-11f8-4ce0-bae2-e149408615c0");

		/*
		 * MobileAppVO c = new MobileAppVO(); c.setAppName("一刻");
		 * c.setAppDesc("豆瓣每日内容精选"); c.setIconImage(
		 * "http://vstore-photocenter-bucket2.nos.netease.com/805a3237-d2ea-4deb-9664-085414af1a32"
		 * );
		 */

		if ("ios".equals(ao.getOs())) {
			a.setDownloadURL("https://itunes.apple.com/cn/app/id425349261");
			a.setUrlSchema("newsapp://");
			b.setDownloadURL("https://itunes.apple.com/cn/app/wang-yi-yun-yue-du-xiao-shuo/id462186890?mt=8");
			b.setUrlSchema("neteasereader://");
			// c.setDownloadURL("https://itunes.apple.com/cn/app/id880936974");
		} else {
			a.setDownloadURL("http://3g.163.com/newsapp/#!/scene-1");
			b.setDownloadURL("http://m.yuedu.163.com/client.do?type=android");
			// c.setDownloadURL("http://3g.163.com/links/3882");
		}

		List<MobileAppVO> d = new ArrayList<MobileAppVO>();
		d.add(a);
		d.add(b);
		// d.add(c);

		return Converter.listBaseJsonVO(d, false);
	}

	@Override
	public BaseJsonVO feedBack(String userName, int areaId, MobileFeedBackAO feedBack) {
		logger.info("user:<" + userName + "> say:" + JsonUtils.toJson(feedBack));
		try {
			MobileChecker.checkNull("USER NAME", userName);
			MobileChecker.checkNull("INPUT", feedBack);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(e1);
		}
		try {

			UserFeedback uf = new UserFeedback();
			uf.setAreaId(areaId);
			uf.setFeedBackContent(feedBack.getMessage());
			uf.setSystem(feedBack.getOs());
			uf.setUserAccount(userName);
			String contact = "";
			if (StringUtils.isNotBlank(feedBack.getEmail()))
				contact = "邮箱:" + feedBack.getEmail();
			if (StringUtils.isNotBlank(feedBack.getPhone()))
				contact = contact + " 电话:" + feedBack.getPhone();
			uf.setUserContact(contact);
			uf.setVersion(feedBack.getVersion());
			userFeedbackService.addNewFeedback(uf);
			return Converter.converterBaseJsonVO(null);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}
	}

	@Override
	public BaseJsonVO updateVersion(MobileOS os, String channl, boolean update) {
		HashMap<String, Object> test = new HashMap<String, Object>();
		test.put("latestVersion", MobileConfig.version);
		if (!update) {
			test.put("desc", "");
			test.put("downloadURL", "");
		} else {
			test.put("desc",MobileConfig.version_desc);
			switch (os) {
			case IOS:
				test.put("downloadURL","https://itunes.apple.com/cn/app/id950899740");
				break;
			case ANDROID:
				test.put("downloadURL",MobileConfig.getAndroidChannlDownload(channl));
				break;
			default:
				test.put("downloadURL", "");
			}
		}

		return Converter.converterBaseJsonVO(test);
	}

}
