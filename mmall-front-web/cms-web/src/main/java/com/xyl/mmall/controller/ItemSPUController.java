/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.ItemSPUFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.cms.vo.ItemSPUVO;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.ExcelUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.enums.CategoryNormalLevel;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.meta.Brand;

/**
 * ItemSPUController.java created by yydx811 at 2015年5月6日 下午5:41:54
 * 单品controller
 *
 * @author yydx811
 */
@Controller
public class ItemSPUController {

	private static Logger logger = LoggerFactory.getLogger(ItemSPUController.class);
	
	private static final int MAX_NUM = 100;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private ItemSPUFacade itemSPUFacade;
	
	@Autowired
	private CategoryFacade categoryFacade;
	
	@Autowired
	private BrandFacade brandFacade;
	
	@Autowired
	private ProductFacade productFacade;

	@Resource
	private ItemSPUService itemSPUService;

	@RequestMapping(value = "/item/spu")
	@RequiresPermissions(value = { "item:spu" })
	public String itemSPU(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/item/spu";
	}

	/**
	 * 获取单品列表
	 * @param pageParamVO
	 * @param spuVO
	 * @param searchValue
	 * @return
	 */
	@RequestMapping(value = "/item/spu/list")
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO getItemSPUList(BasePageParamVO<ItemSPUVO> pageParamVO, 
			ItemSPUVO spuVO, String searchValue) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ErrorCode.SUCCESS);
		pageParamVO.setList(itemSPUFacade.getItemSPUList(pageParamVO, spuVO, searchValue));
		ret.setResult(pageParamVO);
		return ret;
	}

	/**
	 * 创建单品
	 * @param spuVO
	 * @return
	 */
	@RequestMapping(value = "/item/spu/create", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO createItemSPU(@RequestBody ItemSPUVO spuVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isBlank(spuVO.getSpuBarCode())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "条形码不能为空！");
		}
//		if (!RegexUtils.isAllNumber(spuVO.getSpuBarCode())) {
//			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "条形码格式错误！");
//		}
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setBarCode(spuVO.getSpuBarCode());
		ItemSPUVO old = itemSPUFacade.getItemSPU(spuDTO);
		if (old != null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "条形码已存在！");
		}
		// 单品名称
		if (StringUtils.isBlank(spuVO.getSpuName())) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "单品名称不能为空！");
		}
		if (spuVO.getSpuName().length() > 32) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "单品名称过长！");
		}
		if (spuVO.getCategoryNormalId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择分类！");
		}
		CategoryNormalVO c = categoryFacade.getCategoryNormalById(spuVO.getCategoryNormalId(), false);
		if (c == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "分类不存在！");
		}
		if (spuVO.getBrandId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "请选择品牌！");
		}
		BrandDTO b = brandFacade.getBrandByBrandId(spuVO.getBrandId());
		if (b == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "品牌不存在！");
		}
		spuDTO = spuVO.convert();
		spuDTO.setAgentId(SecurityContextUtils.getUserId());
		int res = itemSPUFacade.addItemSPU(spuDTO);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "创建成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "创建失败！");
		}
	}

	/**
	 * 更新单品
	 * @param spuVO
	 * @return
	 */
	@RequestMapping(value = "/item/spu/update", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO updateItemSPU(@RequestBody ItemSPUVO spuVO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (spuVO.getSpuId() < 1l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		if (StringUtils.isNotBlank(spuVO.getSpuBarCode())) {
//			if (!RegexUtils.isAllNumber(spuVO.getSpuBarCode())) {
//				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "条形码格式错误！");
//			}
			ItemSPUDTO spuDTO = new ItemSPUDTO();
			spuDTO.setBarCode(spuVO.getSpuBarCode());
			ItemSPUVO old = itemSPUFacade.getItemSPU(spuDTO);
			if (old != null && old.getSpuId() != spuVO.getSpuId()) {
				return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "条形码已存在在单品【" + spuVO.getSpuName() + "】中！");
			}
		}
		// 单品名称
		if (StringUtils.isNotBlank(spuVO.getSpuName()) && spuVO.getSpuName().length() > 32) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "单品名称过长！");
		}
		ItemSPUDTO spuDTO = spuVO.convert();
		spuDTO.setAgentId(SecurityContextUtils.getUserId());
		
		int res = itemSPUFacade.updateItemSPU(spuDTO);
		if (res > 0) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "更新成功！");
		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "更新失败！");
		}
	}

	/**
	 * 获取单品
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/item/spu/edit")
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO getItemSPU(@RequestParam(required = true, value = "spuId") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(id);
		ItemSPUVO spuVO = itemSPUFacade.getItemSPU(spuDTO);
		if (spuVO == null) {
			return setCodeAndMessage(ret, ResponseCode.RES_ENOTEXIST, "单品不存在！");
		}
		ret.setResult(spuVO);
		return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "");
	}

	/**
	 * 批量删除
	 * @param spuIds
	 * @return
	 */
	@RequestMapping(value = "/item/spu/del")
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO deleteItemSPUs(@RequestParam(required = true, value = "spuIds") String spuIds) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isBlank(spuIds)) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		}
		// 拆分转换id
		String[] ids = spuIds.split(",");
		Set<Long> idSet = new HashSet<Long>();
		for (String id : ids) {
			try {
				long spuId = Long.parseLong(id);
				// 判断是否有商品
				if (productFacade.countProductSKUBySPUId(spuId) > 0) {
					return setCodeAndMessage(ret, ResponseCode.RES_ERROR, "所选单品有商品正在使用！");
				}
				idSet.add(spuId);
			} catch (NumberFormatException e) {
				logger.error("Delete itemSPU error! id : " + id);
			}
		}
		if (CollectionUtils.isEmpty(idSet)) {
			return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
		}
		itemSPUFacade.deleteBulkItemSPU(idSet);
		logger.info("Delete item spu success! SpuIds : " + idSet.toString() 
				+ ", AgentId : " + SecurityContextUtils.getUserId());
		return setCodeAndMessage(ret, ResponseCode.RES_SUCCESS, "删除成功！");
	}
	
	@RequestMapping(value = "/item/spu/import")
	@RequiresPermissions(value = { "item:spu" })
	public void importSPU(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		// 返回结果写入文件
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=result.txt");
		OutputStream out = null;
        BufferedOutputStream buffer = null; 
        long uid = SecurityContextUtils.getUserId();
        try {
        	out = response.getOutputStream();
        	buffer = new BufferedOutputStream(out);
        	// 初始化workboot
        	Workbook workbook = ExcelUtil.initWorkbook(file);
        	if (workbook == null) {
        		buffer.write("无法读取文件，请确保文件是xlsx或xls格式的！\n".getBytes("utf-8"));
        	} else {
        		List<ItemSPUDTO> addList = new ArrayList<ItemSPUDTO>();
        		Set<String> barCodeSet = new HashSet<String>();
        		StringBuilder message = new StringBuilder(255);
        		// 工作簿
        		int sheetNum = workbook.getNumberOfSheets();
        		for (int i = 0; i < sheetNum; i++) {
					Sheet sheet = workbook.getSheetAt(i);
					// 行数，第一行为标题头
					int rowNum = sheet.getLastRowNum();
					for (int j = 1; j <= rowNum; j++) {
						Row row = sheet.getRow(j);
						// 第一个格 单品条形码
						Cell cell = row.getCell(0);
						String barCode = ExcelUtil.getValue(cell);
						ItemSPUVO spuVO = new ItemSPUVO();
						spuVO.setSpuBarCode(barCode);
						// 第二个格 单品名称
						cell = row.getCell(1);
						spuVO.setSpuName(ExcelUtil.getValue(cell));
						// 第三个格 品牌
						cell = row.getCell(2);
						spuVO.setBrandName(ExcelUtil.getValue(cell));
						// 第四个格 一级类目
						cell = row.getCell(3);
						spuVO.setFirstCategoryName(ExcelUtil.getValue(cell));
						// 第五个格 二级类目
						cell = row.getCell(4);
						spuVO.setSecondCategoryName(ExcelUtil.getValue(cell));
						// 第六个格 三级类目
						cell = row.getCell(5);
						spuVO.setThirdCategoryName(ExcelUtil.getValue(cell));
						String err = checkImportCell(spuVO, j, barCodeSet);
						if (err != null) {
							message.append(err);
							continue;
						}
						barCodeSet.add(barCode);
						ItemSPUDTO spuDTO = spuVO.convert();
						spuDTO.setAgentId(uid);
						addList.add(spuDTO);
					}
				}
        		if (itemSPUFacade.addBulkItemSPU(addList)) {
        			buffer.write(message.toString().getBytes("utf-8"));
        			buffer.write("导入结束！".getBytes("utf-8"));
        		} else {
        			buffer.write(message.toString().getBytes("utf-8"));
					buffer.write("导入失败！".getBytes("utf-8"));
        		}
        	}
		} catch (Exception e) {
			logger.error("Can't import spu!", e);
			try {
				if (null != buffer) {
					buffer.write("导入失败！".getBytes("utf-8"));
				}
			} catch (Exception e1) {
				logger.error("Can't import spu! Can't write back error message!", e1);
			}
		} finally {
			if (null != buffer) {
				try {
					buffer.flush();
					buffer.close();
				} catch (IOException e) {
					logger.error("Can't import spu! BufferedOutputStream error!", e);
				}
			}
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					logger.error("Can't import spu! OutputStream error!", e);
				}
			}
		}
	}
	
	@RequestMapping(value = "/item/spu/syncPre", method = RequestMethod.GET)
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO syncPre(
			@RequestParam(value = "spuId", required = true) long spuId) {
		BaseJsonVO ret = new BaseJsonVO();
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(spuId);
		spuDTO = itemSPUService.getItemSPU(spuDTO);
		if (spuDTO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "单品不存在！");
			return ret;
		}
		StringBuilder message = new StringBuilder(30);
		message.append("总计");
		int skuCount = 0;
		int syncCount = 0;
		skuCount = productFacade.countProductSKUBySPUId(spuId);
		if (skuCount <= 0) {
			message.append("0件商品，需同步0件商品，预计0秒内完成");
		} else {
			syncCount = productFacade.countSyncSKUBySPU(spuDTO);
			message.append(skuCount).append("件商品，需同步").append(syncCount).append("件商品，预计");
			if (syncCount > 0) {
				int min = syncCount * 1 / 1000;
				int max = syncCount * 1 / 500;
				if (min <= 0) {
					if (max <= 0) {
						message.append(1);
					} else {
						message.append(max);
					}
				} else {
					message.append(min).append(" - ").append(max);
				}
			} else {
				message.append(0);
			}
			message.append("秒内完成");
		}
		ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, message.toString());
		ret.setResult(syncCount);
		return ret;
	}
	@RequestMapping(value = "/item/spu/doSync", method = RequestMethod.POST)
	@RequiresPermissions(value = { "item:spu" })
	public @ResponseBody BaseJsonVO doSync(@RequestBody ItemSPUVO spuVO) {
		long start = System.currentTimeMillis();
		BaseJsonVO ret = new BaseJsonVO();
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(spuVO.getSpuId());
		spuDTO = itemSPUService.getItemSPU(spuDTO);
		if (spuDTO == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "更新商品信息失败，单品不存在！");
			return ret;
		}
		List<Long> skuIds = productFacade.getSyncSKUIdBySPU(spuDTO);
		int peroid = skuIds.size() / MAX_NUM + 1;
		for (int i = 0; i < peroid; ++ i) {
			int end = (i + 1) * MAX_NUM;
			if (end > skuIds.size()) {
				end = skuIds.size();
			}
			StringBuilder ids = new StringBuilder();
			for (int j = i * MAX_NUM; j < end; ++ j) {
				ids.append(skuIds.get(j)).append(",");
			}
			if (ids.length() > 0) {
				ids.deleteCharAt(ids.length() - 1);
				try {
					productFacade.syncSKUByIds(ids.toString(), spuDTO);
					logger.info("Sync sku successful! ids : [{}].", ids);
				} catch (Exception e) {
					logger.error("Sync sku error! ids : [" + ids + "].", e);
					ret.setCodeAndMessage(ResponseCode.RES_ERROR, "更新商品信息失败！请重试！");
					return ret;
				}
			}
		}
		ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "已成功更新商品信息");
		System.out.println("##########   " + (System.currentTimeMillis() - start));
		return ret;
	}
	
	private String checkImportCell(ItemSPUVO spuVO, int rowNum, Set<String> barCodeSet) {
		StringBuilder err = new StringBuilder(50);
		err.append("第").append(rowNum + 1).append("行，添加失败！");
		// 单品条形码
		if (StringUtils.isBlank(spuVO.getSpuBarCode())) {
			err.append("单品条形码不能为空！\n");
			return err.toString();
		}
//		if (!StringUtils.isNumeric(spuVO.getSpuBarCode())) {
//			err.append("单品条形码数字格式错误！\n");
//			return err.toString();
//		}
		if (barCodeSet.contains(spuVO.getSpuBarCode())) {
			err.append("单品条形码导入在数据中重复！\n");
			return err.toString();
		}
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setBarCode(spuVO.getSpuBarCode());
		ItemSPUVO old = itemSPUFacade.getItemSPU(spuDTO);
		if (old != null) {
			err.append("单品条形码已存在！\n");
			return err.toString();
		}
		// 单品名称
		if (StringUtils.isBlank(spuVO.getSpuName())) {
			err.append("单品名称不能为空！\n");
			return err.toString();
		}
		if (spuVO.getSpuName().length() > 32) {
			err.append("单品名称过长！\n");
			return err.toString();
		}
		// 品牌
		if (StringUtils.isBlank(spuVO.getBrandName())) {
			err.append("品牌不能为空！\n");
			return err.toString();
		}
		List<Brand> brandList = brandFacade.getBrandByName(spuVO.getBrandName());
		if (CollectionUtils.isEmpty(brandList)) {
			err.append("找不到名为“").append(spuVO.getBrandName()).append("”的品牌！\n");
			return err.toString();
		}
		if (brandList.size() > 1) {
			err.append("品牌名“").append(spuVO.getBrandName()).append("”有重复，请确认并手动添加！\n");
			return err.toString();
		}
		spuVO.setBrandId(brandList.get(0).getBrandId());
		// 一级类目
		long parentId = 0l;
		if (StringUtils.isBlank(spuVO.getFirstCategoryName())) {
			err.append("一级类目不能为空！\n");
			return err.toString();
		}
		List<CategoryNormal> categoryNormalList = categoryFacade.getCategoryNormalByName(
				spuVO.getFirstCategoryName(), CategoryNormalLevel.LEVEL_FIRST.getIntValue(), parentId);
		if (CollectionUtils.isEmpty(categoryNormalList)) {
			err.append("找不到名为“").append(spuVO.getFirstCategoryName()).append("”的一级类目！\n");
			return err.toString();
		}
		if (categoryNormalList.size() > 1) {
			err.append("一级类目名“").append(spuVO.getFirstCategoryName()).append("”有重复，请确认并手动添加！\n");
			return err.toString();
		}
		parentId = categoryNormalList.get(0).getId();
		// 二级类目
		if (StringUtils.isBlank(spuVO.getSecondCategoryName())) {
			err.append("二级类目不能为空！\n");
			return err.toString();
		}
		categoryNormalList = categoryFacade.getCategoryNormalByName(
				spuVO.getSecondCategoryName(), CategoryNormalLevel.LEVEL_SECOND.getIntValue(), parentId);
		if (CollectionUtils.isEmpty(categoryNormalList)) {
			err.append("在一级类目“").append(spuVO.getFirstCategoryName()).append("”中，找不到名为“")
				.append(spuVO.getSecondCategoryName()).append("”的二级类目！\n");
			return err.toString();
		}
		if (categoryNormalList.size() > 1) {
			err.append("二级类目名“").append(spuVO.getSecondCategoryName()).append("”有重复，请确认并手动添加！\n");
			return err.toString();
		}
		parentId = categoryNormalList.get(0).getId();
		// 三级类目
		if (StringUtils.isBlank(spuVO.getThirdCategoryName())) {
			err.append("三级类目不能为空！\n");
			return err.toString();
		}
		categoryNormalList = categoryFacade.getCategoryNormalByName(
				spuVO.getThirdCategoryName(), CategoryNormalLevel.LEVEL_THIRD.getIntValue(), parentId);
		if (CollectionUtils.isEmpty(categoryNormalList)) {
			err.append("在二级类目“").append(spuVO.getSecondCategoryName()).append("”中，找不到名为“")
				.append(spuVO.getThirdCategoryName()).append("”的三级类目！\n");
			return err.toString();
		}
		if (categoryNormalList.size() > 1) {
			err.append("三级类目名“").append(spuVO.getThirdCategoryName()).append("”有重复，请确认并手动添加！\n");
			return err.toString();
		}
		spuVO.setCategoryNormalId(categoryNormalList.get(0).getId());
		return null;
	}
	
	private BaseJsonVO setCodeAndMessage(BaseJsonVO ret, int code, String message) {
		ret.setCode(code);
		ret.setMessage(message);
		return ret;
	}
}
