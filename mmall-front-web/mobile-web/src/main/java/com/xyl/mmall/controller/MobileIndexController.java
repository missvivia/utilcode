package com.xyl.mmall.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.config.MobileFileDirConfiguration;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.mainsite.vo.MainsiteIndexVO;
import com.xyl.mmall.mainsite.vo.MainsiteIndexVO.MainsiteStoreVO;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

@Controller
public class MobileIndexController {
	private static final Logger logger = LoggerFactory.getLogger(MobileIndexController.class);

	@Resource(name = "mobileFileDirConfiguration")
	private MobileFileDirConfiguration mobileFileDirConfiguration;

	@Autowired
	private MainsiteHelper mainsiteHelper;

	/**
	 * 获取首页数据
	 * 
	 * @param fresh
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public ModelAndView index(Model model,HttpServletRequest request) throws Exception {
		// BaseJsonVO baseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
		// String path =
		// request.getServletContext().getRealPath(mobileFileDirConfiguration.getIndexDataFilePath());
		// IndexVO indexVO = MobileIndexFacade.getIndexVO(fresh,
		// path,mobileFileDirConfiguration.getIndexDataFileName());
		// baseJsonVO.setResult(indexVO);
		// setRequestUrlPath(indexVO,request);
		long userId = SecurityContextUtils.getUserId();
		MainsiteIndexVO mainsiteIndexVO = mainsiteHelper.getMainsitMainsiteIndexVO(userId);
		List<MainsiteStoreVO> mainsiteStores = mainsiteIndexVO.getMainsiteStoreVOs();
		if (!CollectionUtils.isEmpty(mainsiteStores)) {
			for (MainsiteStoreVO mainsiteStoreVO : mainsiteStores) {
				String url = mainsiteStoreVO.getStoreUrl();
				if (StringUtils.isNotBlank(url)) {
					String[] temp = url.split("/");
					if (temp != null && temp.length > 0) {
						String u = temp[temp.length - 1];
						if (NumberUtils.isNumber(u)) {
							String requestUrl = request.getRequestURL().toString();
							String requestUri = request.getRequestURI();
							mainsiteStoreVO.setStoreUrl(requestUrl.substring(0, requestUrl.indexOf(requestUri))+"m/store/searchProduct?businessId="+u);
						}

					}

				}
			}
		}

		ModelAndView modelAndView = new ModelAndView("/ios/index");
		modelAndView.addObject("mainsiteIndexVO", mainsiteIndexVO);

		return modelAndView;
	}

	// private void setRequestUrlPath(IndexVO indexVO,HttpServletRequest
	// request) {
	// // TODO Auto-generated method stub
	// if(indexVO == null){
	// return;
	// }
	// List<ADColumn> adColumns = indexVO.getAds();
	// StringBuffer s = request.getRequestURL();
	// s.delete(s.length() - request.getRequestURI().length(), s.length());
	// for(ADColumn adColumn:adColumns){
	// StringBuffer sBuffer = new StringBuffer(s);
	// sBuffer.append("/m/store/searchProduct?orderColumn=price&asc=true");
	// if(adColumn.getId()!=null && adColumn.getId().longValue()!=0){
	// sBuffer.append("&businessId=");
	// sBuffer.append(adColumn.getId());
	// }
	//
	// if(adColumn.getCategoryId()!=null&&adColumn.getCategoryId().longValue()!=0){
	// sBuffer.append("&contentCategoryId=");
	// sBuffer.append(adColumn.getCategoryId());
	// }
	// adColumn.setLink(sBuffer.toString());
	// }
	//
	// List<SkuClassify> skuClassifies = indexVO.getSkuClassify();
	// for(SkuClassify skuClassify:skuClassifies){
	// List<MobileIndexSku> mobileSkus = skuClassify.getSkus();
	// for(MobileIndexSku mobileSku :mobileSkus){
	// StringBuffer sBuffer = new StringBuffer(s);
	// sBuffer.append("/m/getPODetail?skuId=");
	// sBuffer.append(mobileSku.getSkuId());
	// mobileSku.setLink(sBuffer.toString());
	// }
	// }
	//
	// }

}
