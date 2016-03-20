package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.MobileCategoryVO;
import com.xyl.mmall.mobile.util.MobileHelper;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/m")
public class MobileCategoryController {


	@Autowired
	private MobileHelper mobileHelper;

	private static final Logger logger = LoggerFactory.getLogger(MobileCategoryController.class);
	
	/**
	 * mobile-web 商品分类
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = { "/categoryList"}, method = RequestMethod.GET)
	public BaseJsonVO categoryList( Long curSupplierAreaId, Boolean hasAllDate,Long categoryId) throws Exception {
		long oldtime = System.currentTimeMillis();

		if (curSupplierAreaId == null || curSupplierAreaId < 1){
			curSupplierAreaId = AreaUtils.getAreaCode();
		}
		
		/**
		 * 商品分类
		 */
		List<CategoryContentDTO> categoryContentDTOs = mobileHelper.getCategoryList(curSupplierAreaId,hasAllDate,categoryId) ;
		BaseJsonVO ret = new BaseJsonVO();
		List<MobileCategoryVO> list = new ArrayList<>();
		converToMobileCategoryVo(categoryContentDTOs,list);
		ret.setResult(list);
		ret.setCodeAndMessage(ErrorCode.SUCCESS.getIntValue(), ErrorCode.SUCCESS.getDesc());
		long cost = System.currentTimeMillis() - oldtime;
		logger.info("=== time cost:" + cost);
		
		return ret;
	}
	private void converToMobileCategoryVo(List<CategoryContentDTO> list, List<MobileCategoryVO> mobileCategoryVOs) {
		// TODO Auto-generated method stub
		if (!CollectionUtils.isEmpty(list) && mobileCategoryVOs != null) {
			MobileCategoryVO mobileCategoryVO = null;
			for (CategoryContentDTO categoryContentDTO : list) {
				mobileCategoryVO = new MobileCategoryVO(categoryContentDTO);
				mobileCategoryVOs.add(mobileCategoryVO);
				if (!CollectionUtils.isEmpty(categoryContentDTO.getSubCategoryContentDTOs())) {
					List<MobileCategoryVO> subCategoryContentDTOs = new ArrayList<>();
					mobileCategoryVO.setSubCategoryContentDTOs(subCategoryContentDTOs);
					converToMobileCategoryVo(categoryContentDTO.getSubCategoryContentDTOs(), subCategoryContentDTOs);
				}
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = { "/categoryById"}, method = RequestMethod.GET)
	public BaseJsonVO categoryById( @RequestParam(value = "categoryId", required = true) Long categoryId) throws Exception {
		long oldtime = System.currentTimeMillis();
		CategoryContentVO categoryContentVO = mobileHelper.getCategoryById(categoryId) ;
		MobileCategoryVO mobileCategoryVO = new MobileCategoryVO();
		if(categoryContentVO != null){
			
			mobileCategoryVO.setId(categoryContentVO.getId());
			mobileCategoryVO.setLevel(categoryContentVO.getLevel());
			mobileCategoryVO.setName(categoryContentVO.getName());
			mobileCategoryVO.setShowIndex(categoryContentVO.getShowIndex());
		}
		
		BaseJsonVO ret = new BaseJsonVO();
		ret.setResult(mobileCategoryVO);
		ret.setCodeAndMessage(ErrorCode.SUCCESS.getIntValue(), ErrorCode.SUCCESS.getDesc());
		long cost = System.currentTimeMillis() - oldtime;
		logger.info("=== time cost:" + cost);
		return ret;
	}
	

}
