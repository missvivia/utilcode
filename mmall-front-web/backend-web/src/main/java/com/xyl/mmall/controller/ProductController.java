/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.backend.param.IndexRecommendParam;
import com.xyl.mmall.backend.vo.ProdParamBackendVO;
import com.xyl.mmall.backend.vo.ProdParamOptionVO;
import com.xyl.mmall.backend.vo.ProdPicVO;
import com.xyl.mmall.backend.vo.ProdSpeciBackendVO;
import com.xyl.mmall.backend.vo.ProductPriceVO;
import com.xyl.mmall.backend.vo.ProductSKUBackendVO;
import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemModelFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.annotation.CheckFormToken;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.ExcelUtil;
import com.xyl.mmall.framework.util.ObjectTransformUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;
import com.xyl.mmall.itemcenter.dto.ProdSpeciDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDetailDTO;
import com.xyl.mmall.itemcenter.dto.SkuRecommendationDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;

/**
 * ProductController.java created by yydx811 at 2015年5月14日 下午7:31:42
 * 商品controller
 *
 * @author yydx811
 */
@Controller
@RequestMapping(value = "/item")
public class ProductController {

	private static Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private CategoryFacade categoryFacade;

	@Autowired
	private ItemSPUFacade itemSPUFacade;

	@Autowired
	private ItemModelFacade itemModelFacade;

	@Autowired
	private ItemCenterFacade itemCenterFacade;

	@Resource
	private ItemSPUService itemSPUService;

	@Resource
	private ItemProductService itemProductService;

	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;

	@RequestMapping(value = "/product")
	@RequiresPermissions({ "item:product" })
	public String itemProduct(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/item/product";
	}

	@CheckFormToken(isCheckRepeat = false)
	@RequestMapping(value = "/product/create", method = RequestMethod.GET)
	@RequiresPermissions({ "item:create" })
	public String createProduct(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/item/create";
	}

	@CheckFormToken
	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO addProduct(@RequestBody ProductSKUBackendVO productSKUVO) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		// 检测单品是否存在
		if (productSKUVO.getItemSPUId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(productSKUVO.getItemSPUId());
		ItemSPUVO spuVO = itemSPUFacade.getItemSPU(spuDTO);
		if (spuVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "单品不存在！");
		}
		// 基本参数校验
		String result = checkProductSKUParam(productSKUVO);
		if (result != null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, result);
		}
		// 商品名
		if (StringUtils.isBlank(productSKUVO.getProductName())) {
			productSKUVO.setProductName(spuVO.getSpuName());
		} else {
			if (productSKUVO.getProductName().length() > 255) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "商品名过长，最长200个字！");
			}
		}
		// 规格校验
		if (CollectionUtils.isEmpty(productSKUVO.getSpeciList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "库存信息不能为空");
		}
		productSKUVO.setBrandId(spuVO.getBrandId());
		productSKUVO.setCategoryNormalId(spuVO.getCategoryNormalId());
		List<ProductSKUDTO> skuDTOList = new ArrayList<ProductSKUDTO>(productSKUVO.getSpeciList().size());
		for (int i = 0; i < productSKUVO.getSpeciList().size(); i++) {
			skuDTOList.add(productSKUVO.convertToDTO());
		}
		// 校验属性
		List<ProdParamDTO> paramDTOList = null;
		if (!CollectionUtils.isEmpty(productSKUVO.getParamList())) {
			paramDTOList = new ArrayList<ProdParamDTO>();
			// 获取模型属性
			Map<Long, Set<Long>> modelParamMap = itemModelFacade.getModelParamKeyValues(spuVO.getCategoryNormalId());
			if (modelParamMap == null) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性参数错误！");
			}
			for (ProdParamBackendVO paramBackendVO : productSKUVO.getParamList()) {
				// 校验属性id是否存在
				Set<Long> idSet = modelParamMap.keySet();
				if (!idSet.contains(paramBackendVO.getParameterId())) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性参数错误！");
				}
				if (!CollectionUtils.isEmpty(paramBackendVO.getOptionList())) {
					for (ProdParamOptionVO optionVO : paramBackendVO.getOptionList()) {
						// 选项是否选中
						if (optionVO.getIsCheck() != 1) {
							continue;
						}
						if (!modelParamMap.get(paramBackendVO.getParameterId()).contains(optionVO.getParamOptionId())) {
							return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "属性参数错误！");
						}
						// 创建属性dto
						ProdParamDTO prodParamDTO = new ProdParamDTO();
						prodParamDTO.setModelParamId(paramBackendVO.getParameterId());
						prodParamDTO.setModelParamOptionId(optionVO.getParamOptionId());
						paramDTOList.add(prodParamDTO);
					}
				}
			}
		}
		for (ProductSKUDTO productSKUDTO : skuDTOList) {
			productSKUDTO.setParamList(paramDTOList);
			productSKUDTO.setStatus(ProductStatusType.ONLINE.getIntValue());
			productSKUDTO.setBusinessId(businessId);
			productSKUDTO.setCreateBy(uid);
			productSKUDTO.setUpdateBy(uid);
		}
		// 获取模型规格
		Map<Long, Set<Long>> modelSpeciMap = itemModelFacade
				.getModelSpecificationKeyValues(spuVO.getCategoryNormalId());
		int i = 0;
		for (ProdSpeciBackendVO speciBackendVO : productSKUVO.getSpeciList()) {
			ProductSKUDTO productSKUDTO = skuDTOList.get(i);
			// if (StringUtils.isBlank(speciBackendVO.getProdInnerCode())) {
			// return setCodeAndMessage(ret, ResponseCode.RES_EPARAM,
			// "商品内码不能为空！");
			// }
			// 商品内码
			productSKUDTO.setInnerCode(speciBackendVO.getProdInnerCode());
			// 商品库存
			if (speciBackendVO.getProductNum() < 1) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "库存应大于0！");
			}
			productSKUDTO.setSkuNum(speciBackendVO.getProductNum());
			// 商品库存不足提醒
			if (speciBackendVO.getAttentionNum() < 0) {
				speciBackendVO.setAttentionNum(0);
			}
			productSKUDTO.setSkuAttention(speciBackendVO.getAttentionNum());
			// 取出规格-选项map
			Map<Long, Long> speciIdMap = speciBackendVO.getSpeciIdMap();
			// 如果列表长度为1，允许map为空，空则为单一库存
			if (productSKUVO.getSpeciList().size() == 1) {
				// 如果map不为空
				if (!CollectionUtils.isEmpty(speciIdMap)) {
					// 如果模型规格map为空
					if (modelSpeciMap == null) {
						return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格参数错误！");
					} else {
						// 校验规格id
						List<ProdSpeciDTO> prodSpeciDTOList = new ArrayList<ProdSpeciDTO>(speciIdMap.size());
						for (long id : speciIdMap.keySet()) {
							// 取出模型规格id，判断是否存在
							Set<Long> idSet = modelSpeciMap.keySet();
							if (!idSet.contains(id)) {
								return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格参数错误！");
							} else {
								// 判断选项id是否存在
								long optionId = speciIdMap.get(id);
								if (!modelSpeciMap.get(id).contains(optionId)) {
									return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格参数错误！");
								}
								// 创建商品规格dto
								ProdSpeciDTO prodSpeciDTO = new ProdSpeciDTO();
								prodSpeciDTO.setModelSpeciId(id);
								prodSpeciDTO.setModelSpeciOptionId(optionId);
								prodSpeciDTOList.add(prodSpeciDTO);
							}
						}
						productSKUDTO.setSpeciList(prodSpeciDTOList);
					}
				}
			} else {
				// 如果map不为空
				if (!CollectionUtils.isEmpty(speciIdMap)) {
					// 如果模型规格map为空
					if (modelSpeciMap == null) {
						return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格参数错误！");
					} else {
						// 校验规格id
						List<ProdSpeciDTO> prodSpeciDTOList = new ArrayList<ProdSpeciDTO>(speciIdMap.size());
						for (long id : speciIdMap.keySet()) {
							// 取出模型规格id，判断是否存在
							Set<Long> idSet = modelSpeciMap.keySet();
							if (!idSet.contains(id)) {
								return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格参数错误！");
							} else {
								// 判断选项id是否存在
								long optionId = speciIdMap.get(id);
								if (!modelSpeciMap.get(id).contains(optionId)) {
									return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "规格参数错误！");
								}
								// 创建商品规格dto
								ProdSpeciDTO prodSpeciDTO = new ProdSpeciDTO();
								prodSpeciDTO.setModelSpeciId(id);
								prodSpeciDTO.setModelSpeciOptionId(optionId);
								prodSpeciDTOList.add(prodSpeciDTO);
							}
						}
						productSKUDTO.setSpeciList(prodSpeciDTOList);
					}
				}
			}
			i++;
		}
		// 添加商品
		int res = productFacade.addProductSKUs(skuDTOList);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "添加成功！");
		} else if (res == -2) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "添加商品成功，添加库存失败！");
		} else if (res == -3) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "添加商品成功，添加库存缓存失败！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "添加失败！");
		}
	}

	@CheckFormToken(isCheckRepeat = false)
	@RequestMapping(value = "/product/edit", method = RequestMethod.GET)
	@RequiresPermissions({ "item:create" })
	public String editProduct(Model model, @RequestParam(required = true, value = "skuId") long id) {
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(id);
		skuDTO.setBusinessId(businessId);
		model.addAttribute("productSKU", productFacade.getProductSKUVO(skuDTO, true));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/item/edit";
	}

	@RequestMapping(value = "/product/scan", method = RequestMethod.GET)
	@RequiresPermissions({ "item:product" })
	public String scanProduct(Model model, @RequestParam(required = true, value = "skuId") long id) {
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(id);
		skuDTO.setBusinessId(businessId);
		model.addAttribute("productSKU", productFacade.getProductSKUVO(skuDTO, true));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/item/scan";
	}

	@RequestMapping(value = "/product/getProdSku", method = RequestMethod.GET)
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO getProductSKU(@RequestParam(required = true, value = "skuId") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(id);
		skuDTO.setBusinessId(businessId);
		ProductSKUBackendVO skuBackendVO = productFacade.getProductSKUVO(skuDTO, true);
		if (skuBackendVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "商品信息未找到！");
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(skuBackendVO);
		return ret;
	}

	@RequestMapping(value = "/product/searchSPU", method = RequestMethod.GET)
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO searchSPU(@RequestParam(value = "spuBarCode", required = true) String barCode) {
		BaseJsonVO ret = new BaseJsonVO();
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setBarCode(barCode);
		ItemSPUVO spuVO = itemSPUFacade.getItemSPU(spuDTO);
		if (spuVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "单品不存在！");
		}
		spuVO.setCategoryNormalName(categoryFacade.getCategoryFullName(spuVO.getCategoryNormalId()));
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(spuVO);
		return ret;
	}

	@RequestMapping(value = "/product/action")
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO operateProductSKU(@RequestParam(required = true, value = "productSKUId") long id,
			@RequestParam(required = true, value = "action") String action) {
		BaseJsonVO ret = new BaseJsonVO();
		long loginId = SecurityContextUtils.getUserId();
		if (productFacade.updateProductSKUStatus(id, action, loginId)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "操作成功！");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_EUNKNOWN, "操作失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/product/batchAction")
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO batchOperateProductSKU(
			@RequestParam(required = true, value = "productSKUIds") String ids,
			@RequestParam(required = true, value = "action") String action) {
		long loginId = SecurityContextUtils.getUserId();
		String[] idArray = StringUtils.split(ids, ',');
		List<Long> idlist = new ArrayList<Long>();
		BaseJsonVO ret = new BaseJsonVO();
		for (String id : idArray) {
			if (!StringUtils.isNumeric(id)) {
				return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "商品ids参数有误，不是数字");
			}
			idlist.add(Long.parseLong(id));
		}
		if (productFacade.batchUpdateProductSKUStatus(idlist, action, loginId)) {
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "操作成功！");
		} else {
			ret.setCodeAndMessage(ResponseCode.RES_EUNKNOWN, "操作失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/product/getModel", method = RequestMethod.GET)
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO getItemModel(@RequestParam(value = "categoryId", required = true) long categoryId) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(itemModelFacade.getItemModel(categoryId, true));
		return ret;
	}

	@CheckFormToken
	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO updateProductSKU(@RequestBody ProductSKUBackendVO skuBackendVO) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		if (skuBackendVO.getSkuId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(skuBackendVO.getSkuId());
		skuDTO.setBusinessId(businessId);
		ProductSKUBackendVO old = productFacade.getProductSKUVO(skuDTO, true);
		if (old == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "商品不存在！");
		}
		String result = checkUpdate(skuBackendVO, old);
		if (result != null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, result);
		}
		// 如果是限购商品
		ProductSKULimitConfigDTO limitConfigDTO = null;
		if (old.getIsSKULimited() == 1 || skuBackendVO.getIsSKULimited() == 1) {
			// 获取限购配置
			ProductSKULimitConfigVO limitConfigVO = productFacade.getProductSKULimitConfig(old.getSkuId());
			if (old.getIsSKULimited() != 1) {
				long id = limitConfigVO == null ? 0l : limitConfigVO.getLimitConfigId();
				limitConfigVO = skuBackendVO.getSkuLimitConfigVO();
				if (limitConfigVO == null) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "添加限购配置不能为空！");
				}
				result = checkAddLimitConfig(limitConfigVO);
				if (result != null) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, result);
				}
				int batchNum = skuBackendVO.getBatchNum() < 0 ? old.getBatchNum() : skuBackendVO.getBatchNum();
				if (limitConfigVO.getSkuLimitNum() < batchNum) {
					return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "限购购买数应大于等于起批量！");
				}
				limitConfigVO.setSkuId(old.getSkuId());
				limitConfigVO.setLimitConfigId(id);
			} else {
				if (skuBackendVO.getIsSKULimited() == 1) {
					if (skuBackendVO.getSkuLimitConfigVO() == null) {
						return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "修改限购配置不能为空！");
					}
					result = checkAddLimitConfig(skuBackendVO.getSkuLimitConfigVO());
					if (result != null) {
						return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, result);
					}
					if (limitConfigVO != null) {
						skuBackendVO.getSkuLimitConfigVO().setSkuId(limitConfigVO.getSkuId());
						skuBackendVO.getSkuLimitConfigVO().setLimitConfigId(limitConfigVO.getLimitConfigId());
					}
					limitConfigVO = skuBackendVO.getSkuLimitConfigVO();
				}
			}
			limitConfigDTO = limitConfigVO.convertToDTO();
			limitConfigDTO.setLastModifyId(uid);
		}
		skuDTO = skuBackendVO.convertToDTO();
		skuDTO.setBusinessId(businessId);
		skuDTO.setUpdateBy(uid);
		if (!CollectionUtils.isEmpty(skuBackendVO.getSpeciList())) {
			ProdSpeciBackendVO speciVO = skuBackendVO.getSpeciList().get(0);
			ProdSpeciBackendVO oldSpeciVO = old.getSpeciList().get(0);
			if (StringUtils.equals(speciVO.getProdInnerCode(), oldSpeciVO.getProdInnerCode())) {
				skuDTO.setInnerCode(null);
			} else {
				skuDTO.setInnerCode(speciVO.getProdInnerCode());
			}
			if (speciVO.getAttentionNum() == oldSpeciVO.getAttentionNum()) {
				skuDTO.setSkuAttention(-1);
			} else {
				skuDTO.setSkuAttention(speciVO.getAttentionNum());
			}
			if (speciVO.getProductNum() < 0 || speciVO.getProductNum() == oldSpeciVO.getProductNum()) {
				skuDTO.setSkuNum(-1);
			} else {
				skuDTO.setSkuNum(speciVO.getProductNum());
			}
		}
		int res = productFacade.updateProductSKU(skuDTO, limitConfigDTO);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else if (res == -2) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新商品成功，更新库存失败！");
		} else if (res == -3) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新商品成功，更新库存缓存失败！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新失败！");
		}
	}

	@RequestMapping(value = "/product/delPic")
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO deleteProdPic(@RequestParam(value = "prodPicId", required = true) long id,
			@RequestParam(value = "skuId", required = true) long productSKUId) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(productSKUId);
		skuDTO.setBusinessId(businessId);
		ProductSKUBackendVO old = productFacade.getProductSKUVO(skuDTO, false);
		if (old == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "商品不存在！");
		}
		int res = productFacade.deleteProdPic(productSKUId, id);
		if (res == -1) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "只剩最后一张图片，不允许删除！");
		} else if (res < 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "删除失败！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		}
	}

	@RequestMapping(value = "/product/updateParam")
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO updateParam(@RequestBody ProdParamBackendVO paramBackendVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (paramBackendVO.getSkuId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		if (paramBackendVO.getParameterId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		if (CollectionUtils.isEmpty(paramBackendVO.getOptionList())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(paramBackendVO.getSkuId());
		skuDTO.setBusinessId(businessId);
		ProductSKUBackendVO old = productFacade.getProductSKUVO(skuDTO, false);
		if (old == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "商品不存在！");
		}
		Map<Long, Set<Long>> paramMap = itemModelFacade.getModelParamKeyValues(old.getCategoryNormalId());
		if (CollectionUtils.isEmpty(paramMap)) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "无属性！");
		}
		Set<Long> keySet = paramMap.keySet();
		List<ProdParamDTO> addList = new ArrayList<ProdParamDTO>();
		List<ProdParamDTO> delList = new ArrayList<ProdParamDTO>();
		for (ProdParamOptionVO optionVO : paramBackendVO.getOptionList()) {
			long paramId = paramBackendVO.getParameterId();
			if (keySet.contains(paramId)) {
				Set<Long> optionIdSet = paramMap.get(paramId);
				if (!CollectionUtils.isEmpty(optionIdSet) && optionIdSet.contains(optionVO.getParamOptionId())) {
					ProdParamDTO prodParamDTO = new ProdParamDTO();
					prodParamDTO.setProductSKUId(paramBackendVO.getSkuId());
					prodParamDTO.setModelParamId(paramId);
					prodParamDTO.setModelParamOptionId(optionVO.getParamOptionId());
					prodParamDTO.setCreateBy(uid);
					prodParamDTO.setUpdateBy(uid);
					if (optionVO.getIsCheck() == 1) {
						addList.add(prodParamDTO);
					} else {
						prodParamDTO.setId(paramBackendVO.getProdParamId());
						delList.add(prodParamDTO);
					}
				}
			}
		}
		if (CollectionUtils.isEmpty(addList) && CollectionUtils.isEmpty(delList)) {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新商品属性失败！");
		}
		productFacade.updateProdParam(addList, delList);
		return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新商品属性成功！");
	}

	@RequestMapping(value = "/product/list")
	@RequiresPermissions({ "item:product" })
	public String itemProductList(Model model) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		Map<ProductStatusType, Integer> statusCountMap = productFacade.countProductSKUByBusinessId(supplierId);
		// 审核中的数量
		model.addAttribute("pendingCount", statusCountMap.get(ProductStatusType.PENDING));
		// 销售中的数量
		model.addAttribute("onlineCount", statusCountMap.get(ProductStatusType.ONLINE));
		// 下架的数量
		model.addAttribute("offlineCount", statusCountMap.get(ProductStatusType.OFFLINE));

		BasePageParamVO<CategoryNormalVO> basePageParamVO = new BasePageParamVO<CategoryNormalVO>();// 不分页，取全部
		List<CategoryNormalVO> categoryNormalVOs = categoryFacade.getCategoryNormalList(basePageParamVO);
		// 商品类目
		model.addAttribute("catetoryList", categoryNormalVOs);
		ProductSKUSearchParam searchParam = new ProductSKUSearchParam();
		searchParam.setBusinessId(supplierId);
		searchParam.setStockStatus(1);
		int count = productFacade.countProductSKUDTOBySearchParam(searchParam);
		// 库存不足
		model.addAttribute("stockCount", count);
		// 搜索条件
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NkvConstant.NKV_BUSINESSID_SEARCHPARAM + "_" + supplierId).getBytes(), NkvConstant.NKV_OPTION_0);
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				searchParam = (ProductSKUSearchParam) ObjectTransformUtil.byteToObject(result.getResult());
			}
			model.addAttribute("searchParam", searchParam);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("Failed to get value by key " + NkvConstant.NKV_BUSINESSID_SEARCHPARAM + "_" + supplierId, e);
			model.addAttribute("searchParam", searchParam);
		}
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/item/productList";
	}

	@RequestMapping(value = "/product/delete/{id}")
	@RequiresPermissions(value = { "item:product" })
	public @ResponseBody BaseJsonVO deleteProduct(@PathVariable long id) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		BaseJsonVO retObj = new BaseJsonVO();
		int result = productFacade.deleteProduct(supplierId, id);
		if (result != -2) {
			// 删库存表和缓存失败，不影响删除商品
			retObj.setCodeAndMessage(ResponseCode.RES_SUCCESS, "删除商品成功！");
		} else {
			retObj.setCodeAndMessage(ResponseCode.RES_ENOTAUTH, "删除商品失败！");
		}
		// if(result==-2||result==-1){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH, "删除商品失败！");
		// }else if(result==-3){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH,
		// "删除商品成功，但删除商品库存失败！");
		// }else if(result==-4){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH,
		// "删除商品成功，但删除商品库存缓存失败！");
		// }else{
		// setCodeAndMessage(retObj, ResponseCode.RES_SUCCESS, "删除商品成功！");
		// }
		return retObj;
	}

	@RequestMapping(value = "/product/remove", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:product" })
	public @ResponseBody BaseJsonVO removeProduct(@RequestBody List<Long> ids) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		BaseJsonVO retObj = new BaseJsonVO();
		int result = productFacade.deleteProducts(supplierId, ids);
		if (result != -2) {
			// 删库存表和缓存失败，不影响删除商品
			retObj.setCodeAndMessage(ResponseCode.RES_SUCCESS, "批量删除商品成功！");
		} else {
			retObj.setCodeAndMessage(ResponseCode.RES_ENOTAUTH, "批量删除商品失败！");
		}
		// if(result==-2||result==-1){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH, "批量删除商品失败！");
		// }else if(result==-3){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH,
		// "批量删除商品成功，但批量删除商品库存失败！");
		// }else if(result==-4){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH,
		// "批量删除商品成功，但批量删除商品库存缓存失败！");
		// }else if(result==-5){
		// setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH,
		// "批量删除商品失败,商品Ids参数有误！");
		// }else{
		// setCodeAndMessage(retObj, ResponseCode.RES_SUCCESS, "批量删除商品成功！");
		// }
		return retObj;
	}

	@RequestMapping(value = "/product/searchProduct")
	@RequiresPermissions({ "item:product" })
	public @ResponseBody BaseJsonVO searchProduct(ProductSKUSearchParam searchParam) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		searchParam.setBusinessId(supplierId);
		try {
			// 将搜索参数保存在nvk中
			Result<Void> result = defaultExtendNkvClient.put(NkvConfiguration.NKV_MDB_SESSION_NAMESPACE,
					(NkvConstant.NKV_BUSINESSID_SEARCHPARAM + "_" + supplierId).getBytes(),
					ObjectTransformUtil.objectToByte(searchParam), NkvConstant.NKV_OPTION_0);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("Failed to put value by key " + NkvConstant.NKV_BUSINESSID_SEARCHPARAM + "_" + supplierId, e);
		}
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setResult(productFacade.searchProductSKU(searchParam));
		retObj.setCode(ResponseCode.RES_SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "/product/updateStock")
	@RequiresPermissions(value = { "item:product" })
	public @ResponseBody BaseJsonVO updateProductSKUStock(
			@RequestParam(required = true, value = "productSKUId") long id,
			@RequestParam(value = "skuCount", required = true) int count) {
		BaseJsonVO retObj = new BaseJsonVO();
		if (count < 0) {
			return setCodeAndMessage(retObj, ResponseCode.RES_ERROR, "参数有误，库存不能为负数！");
		}
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		int result = productFacade.updateProductSKUStock(id, count, supplierId);
		if (result > 0) {
			return setCodeAndMessage(retObj, ResponseCode.RES_SUCCESS, "更新库存成功！");
		} else if (result == -4) {
			return setCodeAndMessage(retObj, ResponseCode.RES_ENOTAUTH, "没有权限更新！");
		} else {
			return setCodeAndMessage(retObj, ResponseCode.RES_ERROR, "更新库存失败！");
		}
	}

	/**
	 * 商品导入
	 * 
	 * @param file
	 * @param response
	 */
	@RequestMapping(value = "/product/import")
	@RequiresPermissions(value = { "item:create" })
	public void importProductSKU(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		// 返回结果写入文件
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=result.txt");
		OutputStream out = null;
		BufferedOutputStream buffer = null;
		long uid = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(uid);
		try {
			out = response.getOutputStream();
			buffer = new BufferedOutputStream(out);
			// 初始化workboot
			Workbook workbook = ExcelUtil.initWorkbook(file);
			if (workbook == null) {
				buffer.write("无法读取文件，请确保文件是xlsx或xls格式的！\n".getBytes("utf-8"));
			} else {
				StringBuilder message = new StringBuilder(255);
				// 工作簿
				int sheetNum = workbook.getNumberOfSheets();
				int total = 0;
				for (int i = 0; i < sheetNum; i++) {
					Sheet sheet = workbook.getSheetAt(i);
					int rowNum = sheet.getLastRowNum();
					total = total + rowNum;
				}
				if (total > 1000) {
					buffer.write("导入商品行数不能超过1000行！\n".getBytes("utf-8"));
					buffer.write("导入结束！".getBytes("utf-8"));
				} else {
					for (int i = 0; i < sheetNum; i++) {
						Sheet sheet = workbook.getSheetAt(i);
						// 行数，第一行为标题头
						int rowNum = sheet.getLastRowNum();
						for (int j = 1; j <= rowNum; j++) {
							ProductSKUDTO skuDTO = new ProductSKUDTO();
							Row row = sheet.getRow(j);
							// 第一个格 单品条形码
							Cell cell = row.getCell(0);
							String barCode = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(barCode)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("单品条形码不能为空！\n");
								continue;
							}
							ItemSPUDTO spuDTO = new ItemSPUDTO();
							spuDTO.setBarCode(barCode);
							spuDTO = itemSPUService.getItemSPU(spuDTO);
							if (spuDTO == null) {
								message.append("第").append(j + 1).append("行，添加失败！").append("单品条形码不存在！\n");
								continue;
							}
							skuDTO.setSpuId(spuDTO.getId());
							// 第二格 商品大图1
							cell = row.getCell(1);
							String imgURL = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(imgURL)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("商品大图1不能为空！\n");
								continue;
							}
							List<ProdPicDTO> picList = new ArrayList<ProdPicDTO>();
							picList.add(createProdPicDTO(imgURL, uid));
							// 第三格-第六格 商品大图2-5 选填
							for (int k = 2; k < 6; k++) {
								cell = row.getCell(k);
								imgURL = ExcelUtil.getValue(cell);
								if (StringUtils.isNotBlank(imgURL)) {
									picList.add(createProdPicDTO(imgURL, uid));
								}
							}
							skuDTO.setPicList(picList);
							// 第七格 销售单位
							cell = row.getCell(6);
							String prodUnit = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(prodUnit)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("销售单位不能为空！\n");
								continue;
							}
							skuDTO.setUnit(prodUnit);
							// 第八格 保质期 选填
							cell = row.getCell(7);
							String expire = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(expire)) {
								skuDTO.setExpire("");
							} else {
								skuDTO.setExpire(expire);
							}
							// 第九格 生产日期
							cell = row.getCell(8);
							String produceDate = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(produceDate)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("生产日期不能为空！\n");
								continue;
							}
							Date date = DateUtil.stringToDate(produceDate, DateUtil.PATTERN_FIVE);
							if (date == null) {
								message.append("第").append(j + 1).append("行，添加失败！").append("生产日期格式不正确！")
										.append(produceDate).append("\n");
								continue;
							}
							skuDTO.setProduceDate(date);
							// 第十格 退货政策
							cell = row.getCell(9);
							String canReturn = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(canReturn)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("退货政策不能为空！\n");
								continue;
							}
							if (StringUtils.equals(canReturn.trim(), "允许")) {
								skuDTO.setCanReturn(1);
							} else {
								skuDTO.setCanReturn(2);
							}
							// 第十一格 起批数量
							cell = row.getCell(10);
							String batchNum = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(batchNum)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("起批数量不能为空！\n");
								continue;
							}
							try {
								int num = Integer.parseInt(batchNum);
								skuDTO.setBatchNum(num);
							} catch (Exception e) {
								message.append("第").append(j + 1).append("行，添加失败！").append("起批数量不正确！\n");
								continue;
							}
							// 第十二格 建议零售价 选填
							cell = row.getCell(11);
							if (cell != null) {
								String salePrice = String.valueOf(cell.getNumericCellValue());
								if (StringUtils.isBlank(salePrice)) {
									skuDTO.setSalePrice(BigDecimal.ZERO);
								} else {
									try {
										skuDTO.setSalePrice(new BigDecimal(salePrice));
									} catch (Exception e) {
										message.append("第").append(j + 1).append("行，添加失败！").append("建议零售价不正确！\n");
										continue;
									}
								}
							}
							// 第十三格 批发价
							cell = row.getCell(12);
							if (cell == null) {
								message.append("第").append(j + 1).append("行，添加失败！").append("批发价不能为空！\n");
								continue;
							}
							String price = String.valueOf(cell.getNumericCellValue());
							if (StringUtils.isBlank(price)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("批发价不能为空！\n");
								continue;
							}
							try {
								List<ProductPriceDTO> priceList = new ArrayList<ProductPriceDTO>();
								ProductPriceDTO priceDTO = new ProductPriceDTO();
								priceDTO.setPrice(new BigDecimal(price));
								priceDTO.setMinNumber(skuDTO.getBatchNum());
								priceList.add(priceDTO);
								skuDTO.setPriceList(priceList);
							} catch (Exception e) {
								message.append("第").append(j + 1).append("行，添加失败！").append("批发价不正确！\n");
								continue;
							}
							// 第十四格 库存
							cell = row.getCell(13);
							String skuStock = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(skuStock)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("库存不能为空！\n");
								continue;
							}
							try {
								int num = Integer.parseInt(skuStock);
								skuDTO.setSkuNum(num);
							} catch (Exception e) {
								message.append("第").append(j + 1).append("行，添加失败！").append("库存不正确！\n");
								continue;
							}
							// 第十五格 最小库存报警数
							cell = row.getCell(14);
							String attention = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(attention)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("最小库存报警数不能为空！\n");
								continue;
							}
							try {
								int num = Integer.parseInt(attention);
								skuDTO.setSkuAttention(num);
							} catch (Exception e) {
								message.append("第").append(j + 1).append("行，添加失败！").append("最小库存报警数不正确！\n");
								continue;
							}
							// 第十六格 商品内码
							cell = row.getCell(15);
							String innerCode = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(attention)) {
								skuDTO.setInnerCode("");
							} else {
								skuDTO.setInnerCode(innerCode);
							}
							// 第十七格 商品详情
							cell = row.getCell(16);
							String detail = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(detail)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("商品详情不能为空！\n");
								continue;
							}
							ProductSKUDetailDTO detailDTO = new ProductSKUDetailDTO();
							detailDTO.setCreateBy(uid);
							detailDTO.setCustomEditHTML(detail);
							skuDTO.setDetailDTO(detailDTO);
							// 第十八格 商品状态
							cell = row.getCell(17);
							String state = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(state)) {
								message.append("第").append(j + 1).append("行，添加失败！").append("商品状态不能为空！\n");
								continue;
							}
							if (StringUtils.equals(state.trim(), "上架")) {
								skuDTO.setStatus(4);
							} else {
								skuDTO.setStatus(5);
							}
							// 第十九格 商品副标题
							cell = row.getCell(18);
							String title = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(title)) {
								skuDTO.setTitle("");
							} else {
								skuDTO.setTitle(title);
							}
							// 第二十格 商品名
							cell = row.getCell(19);
							String name = ExcelUtil.getValue(cell);
							if (StringUtils.isBlank(name)) {
								skuDTO.setName(spuDTO.getName());
							} else {
								skuDTO.setName(name);
							}
							skuDTO.setBusinessId(businessId);
							skuDTO.setCategoryNormalId(spuDTO.getCategoryNormalId());
							skuDTO.setBrandId(spuDTO.getBrandId());
							skuDTO.setCreateBy(uid);
							try {
								long res = productFacade.addProductSKU(skuDTO);
								if (res > 0) {
									logger.info("Import product sku successful! skuId : {}.", res);
								} else if (res == -1) {
									message.append("第").append(j + 1).append("行，导入成功，添加库存失败！\n");
								} else if (res == -3) {
									message.append("第").append(j + 1).append("行，导入成功，添加库存缓存失败！\n");
								} else {
									message.append("第").append(j + 1).append("行，导入失败！\n");
								}
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
					buffer.write(message.toString().getBytes("utf-8"));
					buffer.write("导入结束！".getBytes("utf-8"));
				}
			}
		} catch (Exception e) {
			logger.error("Can't import productSKU!", e);
			try {
				if (null != buffer) {
					buffer.write("导入失败！".getBytes("utf-8"));
				}
			} catch (Exception e1) {
				logger.error("Can't import productSKU! Can't write back error message!", e1);
			}
		} finally {
			if (null != buffer) {
				try {
					buffer.flush();
					buffer.close();
				} catch (IOException e) {
					logger.error("Can't import productSKU! BufferedOutputStream error!", e);
				}
			}
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					logger.error("Can't import productSKU! OutputStream error!", e);
				}
			}
		}
	}

	@RequestMapping(value = "/product/recommendation")
	@RequiresPermissions(value = { "item:recommendation" })
	public String productSKURecommendation(Model model) {
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		List<SkuRecommendationDTO> skuRecommendationDTOs = itemProductService
				.getSKuRecommendationListByBusinessId(businessId);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("skuRecommendations", skuRecommendationDTOs);
		return "/pages/item/recommendation";
	}

	@RequestMapping(value = "/product/recommendationEdit")
	@RequiresPermissions(value = { "item:recommendation" })
	public String productSKURecommendationEdit(Model model) {
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		List<SkuRecommendationDTO> skuRecommendationDTOs = itemProductService
				.getSKuRecommendationListByBusinessId(businessId);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(loginId));
		model.addAttribute("skuRecommendations", skuRecommendationDTOs);
		return "/pages/item/recommendationEdit";
	}

	@RequestMapping(value = "/product/checkStatus")
	public @ResponseBody BaseJsonVO checkProductStatus(long productId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		ProductSKUDTO productSKUDTO = new ProductSKUDTO();
		productSKUDTO.setId(productId);
		productSKUDTO = itemProductService.getProductSKUDTO(productSKUDTO, false);
		if (productSKUDTO == null) {
			baseJsonVO.setMessage("商品不存在");
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			return baseJsonVO;
		}
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		if (productSKUDTO.getBusinessId() != businessId) {
			baseJsonVO.setMessage("该商品不属于本店铺");
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			return baseJsonVO;
		}
		if (productSKUDTO.getStatus() == 5) {
			baseJsonVO.setMessage("商品已下架");
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			return baseJsonVO;
		}
		baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		return baseJsonVO;
	}

	@RequestMapping(value = "/product/addOrUpdateRecommendation", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:recommendation" })
	public @ResponseBody BaseJsonVO addOrUpdateIndexRecommend(@RequestBody IndexRecommendParam param) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		List<SkuRecommendationDTO> skuRecommendationDTOs = param.getSkuRecommendationDTOs();
		if (CollectionUtil.isEmptyOfList(skuRecommendationDTOs)) {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("参数有误");
			return baseJsonVO;
		}
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		if (businessId <= 0) {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("登录失败，未取得商家信息");
			return baseJsonVO;
		}
		boolean isUpdate = false;
		List<Long> productIds = new ArrayList<Long>();
		for (SkuRecommendationDTO skuRecommendationDTO : skuRecommendationDTOs) {
			if (skuRecommendationDTO.getId() > 0) {
				isUpdate = true;
			}
			if (skuRecommendationDTO.getId() == 0 && skuRecommendationDTO.getProductSKUId() <= 0) {
				baseJsonVO.setCode(ResponseCode.RES_ERROR);
				baseJsonVO.setMessage("非法参数");
				return baseJsonVO;
			}
			if (skuRecommendationDTO.getProductSKUId() > 0) {
				productIds.add(skuRecommendationDTO.getProductSKUId());
			}
			skuRecommendationDTO.setBusinessId(businessId);
		}
		Map<Long, Boolean> skuStatusMap = itemProductService.getProductStatusIsOnline(productIds);
		if (skuStatusMap.containsValue(false) || skuStatusMap.containsValue(null)) {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("包含已下架或者删除商品,请重新验证");
			return baseJsonVO;
		}
		boolean result = true;
		try {
			result = itemProductService.addOrUpdateSkuRecommendationDTOs(skuRecommendationDTOs);
		} catch (ItemCenterException e) {
			result = false;
		}
		if (result) {
			logger.info("add or update index Recommend by {}", loginId);
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		} else {
			baseJsonVO.setMessage(isUpdate ? "更新失败" : "新增失败");
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
		}
		return baseJsonVO;
	}

	private String checkUpdate(ProductSKUBackendVO update, ProductSKUBackendVO old) {
		// 商品名与原来相同，则不更新
		if (StringUtils.equals(update.getProductName(), old.getProductName())) {
			update.setProductName(null);
		} else {
			if (StringUtils.isBlank(update.getProductName())) {
				update.setProductName(null);
			} else {
				if (update.getProductName().length() > 255) {
					return "商品名过长，最长200个字符！";
				}
			}
		}
		if (StringUtils.equals(update.getProdUnit(), old.getProdUnit())) {
			update.setProdUnit(null);
		}
		if (StringUtils.equals(update.getProductTitle(), old.getProductTitle())) {
			update.setProductTitle(null);
		}
		if (StringUtils.equals(update.getExpireDate(), old.getExpireDate())) {
			update.setExpireDate(null);
		}
		if (update.getProdProduceDate() == old.getProdProduceDate()) {
			update.setProdProduceDate(-1l);
			;
		}
		if (update.getCanReturn() == old.getCanReturn()) {
			update.setCanReturn(0);
		} else {
			if (update.getCanReturn() != 1) {
				update.setCanReturn(2);
			}
		}
		if (update.getBatchNum() == old.getBatchNum()) {
			update.setBatchNum(-1);
		} else {
			if (update.getBatchNum() < 0) {
				update.setBatchNum(1);
			}
		}
		if (update.getSalePrice() != null) {
			if (old.getSalePrice().compareTo(update.getSalePrice()) == 0) {
				update.setSalePrice(null);
			} else {
				if (update.getSalePrice() == null || update.getSalePrice().doubleValue() < 0.00d) {
					return "请添加正确的建议零售价！";
				}
			}
		}
		if (CollectionUtils.isEmpty(update.getPriceList())) {
			return "价格不能为空！";
		}
		Set<Integer> minNumSet = new TreeSet<Integer>();
		for (ProductPriceVO priceVO : update.getPriceList()) {
			int minNumber = priceVO.getProdMinNumber();
			if (minNumber < 0) {
				return "请输入正确的最小批发数！";
			}
			if (minNumSet.contains(minNumber)) {
				return "价格区间数量重复！";
			}
			if (update.getPriceList().size() == 1) {
				minNumSet.add(update.getBatchNum() < 0 ? old.getBatchNum() : update.getBatchNum());
				priceVO.setProdMinNumber(update.getBatchNum() < 0 ? old.getBatchNum() : update.getBatchNum());
			} else {
				minNumSet.add(minNumber);
			}
			if (priceVO.getProdPrice() == null || priceVO.getProdPrice().doubleValue() < 0.00d) {
				return "请添加正确的批发价！";
			}
			priceVO.setProductId(old.getSkuId());
		}
		int batchNum = update.getBatchNum() < 0 ? old.getBatchNum() : update.getBatchNum();
		if (minNumSet.iterator().next() != batchNum) {
			return "起批数和最小批发数不等！";
		}
		if (!CollectionUtils.isEmpty(update.getPicList())) {
			if ((update.getPicList().size() + old.getPicList().size()) > 5) {
				return "最多添加5张图片";
			}
			for (ProdPicVO picVO : update.getPicList()) {
				if (StringUtils.isBlank(picVO.getPicPath())) {
					return "图片地址为空！";
				}
				picVO.setPicType(1);
				picVO.setSkuId(old.getSkuId());
				picVO.setProdPicId(0l);
			}
		}
		if (update.getProdDetail() != null
				&& old.getProdDetail() != null
				&& StringUtils.equals(update.getProdDetail().getCustomEditHTML(), old.getProdDetail()
						.getCustomEditHTML())) {
			update.setProdDetail(null);
		}
		return null;
	}

	private String checkProductSKUParam(ProductSKUBackendVO productSKUVO) {
		if (StringUtils.isBlank(productSKUVO.getProdUnit())) {
			return "销售单位不能为空！";
		}
		// if (StringUtils.isBlank(productSKUVO.getExpireDate())) {
		// return "保质期不能为空！";
		// }
		// if (productSKUVO.getProdProduceDate() < 0) {
		// return "请添加生产日期！";
		// }
		if (StringUtils.isNotBlank(productSKUVO.getProductTitle()) && productSKUVO.getProductTitle().length() > 76) {
			return "副标题过长！";
		}
		if (productSKUVO.getCanReturn() != 1) {
			productSKUVO.setCanReturn(2);
		}
		if (productSKUVO.getBatchNum() < 0) {
			productSKUVO.setBatchNum(1);
		}
		// if (productSKUVO.getSalePrice() == null ||
		// productSKUVO.getSalePrice().doubleValue() < 0.00d) {
		// return "请添加正确的建议零售价！";
		// }
		if (CollectionUtils.isEmpty(productSKUVO.getPriceList())) {
			return "价格不能为空";
		}
		Set<Integer> minNumSet = new TreeSet<Integer>();
		for (ProductPriceVO priceVO : productSKUVO.getPriceList()) {
			int minNumber = priceVO.getProdMinNumber();
			if (minNumber < 0) {
				return "请输入正确的最小批发数！";
			}
			if (minNumSet.contains(minNumber)) {
				return "价格区间数量重复！";
			}
			if (productSKUVO.getPriceList().size() == 1) {
				minNumSet.add(productSKUVO.getBatchNum());
				priceVO.setProdMinNumber(productSKUVO.getBatchNum());
			} else {
				minNumSet.add(minNumber);
			}
			if (priceVO.getProdPrice() == null || priceVO.getProdPrice().doubleValue() < 0.00d) {
				return "请添加正确的批发价！";
			}
		}
		if (minNumSet.iterator().next() != productSKUVO.getBatchNum()) {
			return "起批数和最小批发数不等！";
		}
		if (CollectionUtils.isEmpty(productSKUVO.getPicList())) {
			return "图片列表不能为空！";
		}
		if (productSKUVO.getPicList().size() > 5) {
			return "图片最多5张！";
		}
		for (ProdPicVO picVO : productSKUVO.getPicList()) {
			if (StringUtils.isBlank(picVO.getPicPath())) {
				return "图片地址为空！";
			}
			picVO.setPicType(1);
		}
		if (productSKUVO.getProdDetail() == null
				|| StringUtils.isBlank(productSKUVO.getProdDetail().getCustomEditHTML())) {
			return "商品详情参数错误！";
		}
		return null;
	}

	private ProdPicDTO createProdPicDTO(String url, long uid) {
		ProdPicDTO picDTO = new ProdPicDTO();
		picDTO.setCreateBy(uid);
		picDTO.setPath(url);
		return picDTO;
	}

	private BaseJsonVO setCodeAndMessage(BaseJsonVO ret, int code, String message) {
		ret.setCode(code);
		ret.setMessage(message);
		return ret;
	}

	private String checkAddLimitConfig(ProductSKULimitConfigVO limitConfigVO) {
		if (StringUtils.isBlank(limitConfigVO.getLimitComment())) {
			return "限购说明过不能为空！";
		}
		if (limitConfigVO.getLimitComment().length() > 20) {
			return "限购说明过长！最多20个字符。";
		}
		if (limitConfigVO.getLimitStartTime() >= limitConfigVO.getLimitEndTime()) {
			return "限购起始时间要小于限购结束时间！";
		}
		if (limitConfigVO.getLimitPeriod() < 1l) {
			return "限购周期应大于0！";
		}
		if (limitConfigVO.getSkuLimitNum() < 1) {
			return "限购数量应大于0！";
		}
		return null;
	}

}
