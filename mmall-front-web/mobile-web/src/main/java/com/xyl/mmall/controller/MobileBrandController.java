package com.xyl.mmall.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.MobileBrandFacade;
import com.xyl.mmall.mobile.ios.facade.param.BrandSearchVO;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.util.AreaUtils;

/**
 * 主站的品牌Controller
 * 
 * @author chengximing
 *
 */
@Controller
@RequestMapping("/m")
public class MobileBrandController extends BaseController {
	@Resource
	private MobileBrandFacade mobileBrandFacade;

	@RequestMapping(value = "/brand/listByParams", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getBrandByCategory(BrandSearchVO brandSearchVO) throws Exception {
		BaseJsonVO out = new BaseJsonVO();
		if (brandSearchVO == null ) {
			out.setCode(400);
			out.setMessage("请填写参数");
			return out;
		}

		long areaId = AreaUtils.getAreaCode();
		List<Brand> list = mobileBrandFacade.getBrandListInOrderByCategory(brandSearchVO, areaId);
		out.setCode(ErrorCode.SUCCESS);
		out.setMessage("查找品牌成功");
		out.setResult(list);
		return out;
	}

	@RequestMapping(value = "/brand/listBySkuName", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getBrandByProdName(String skuName) {

		return null;
	}
}
