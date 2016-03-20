package com.xyl.mmall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.backend.util.ConvertUtil;
import com.xyl.mmall.backend.vo.BatchUploadPOProd;
import com.xyl.mmall.backend.vo.ExportPoSkuVO;
import com.xyl.mmall.backend.vo.PoProductSearchVO;
import com.xyl.mmall.backend.vo.PoProductSortVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.cms.facade.CMSItemCenterFacade;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.excelparse.ExcelParse;
import com.xyl.mmall.excelparse.ExcelParseExceptionInfo;
import com.xyl.mmall.excelparse.ExcelParseExeption;
import com.xyl.mmall.excelparse.ExcelUtils;
import com.xyl.mmall.excelparse.XLSExport;
import com.xyl.mmall.exception.ExcelFormatException;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.param.PoAddProductReqVO;
import com.xyl.mmall.itemcenter.param.PoDeleteProdVO;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.service.NkvStaticDataHelpler;

@Controller
public class POItemController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(POItemController.class);

	@Autowired
	private CMSItemCenterFacade cmsItemCenterFacade;

	@Autowired
	private MainsiteItemFacade mainItemCenterFacade;

	@Autowired
	private POItemFacade poItemFacade;

	@Resource
	private MainsiteItemFacade mainSiteItemFacade;

	@Autowired
	private ItemCenterFacade itemCenterFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Autowired
	private NkvStaticDataHelpler staticDataHelper;

	@RequestMapping(value = "/rest/schedule/search", method = RequestMethod.POST)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO getSize(@RequestBody PoProductSearchVO param) {
		try {
			long loginId = SecurityContextUtils.getUserId();
			long supplierId = itemCenterFacade.getSupplierId(loginId);
			BaseJsonVO result = poItemFacade.searchProduct(param, supplierId);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@RequestMapping(value = "/rest/schedule/addProduct", method = RequestMethod.POST)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO addProduct(@RequestBody PoAddProductReqVO param) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		param.setSupplierId(supplierId);
		BaseJsonVO retObj = poItemFacade.addProductToPo(param);
		return retObj;
	}

	@RequestMapping(value = "/rest/schedule/deleteProduct", method = RequestMethod.POST)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO deleteProduct(@RequestBody PoDeleteProdVO param) {
		BaseJsonVO retObj = poItemFacade.deleteProductFromPo(param);
		return retObj;
	}

	@RequestMapping(value = "/schedule/editadd", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:manage" })
	public String editProduct(Model model, @RequestParam long id, @RequestParam long poId) {
		appendStaticMethod(model);
		ProductEditVO product = poItemFacade.getProductVO(poId, id);
		model.addAttribute("productVO", product);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/product/edit";
	}

	@RequestMapping(value = "/rest/schedule/getSizeTemplate", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO getSize(Model model, HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String productId = request.getParameter("productId");
		String lowCategoryId = request.getParameter("lowCategoryId");
		String sizeTemplateId = request.getParameter("sizeTemplateId");
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		SizeVO sizeVO = poItemFacade.genSizeVO(Long.parseLong(productId), Long.parseLong(lowCategoryId),
				Long.parseLong(sizeTemplateId), supplierId);
		BaseJsonVO retObj = new BaseJsonVO(sizeVO);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "/rest/schedule/product/{id}", method = RequestMethod.PUT)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO saveProduct(@PathVariable String id, @RequestBody String param) {
		JSONObject json = JSONObject.parseObject(param);
		ProductSaveParam productSave = ConvertUtil.convertJSONToProductParam(json);
		long pid = Long.parseLong(id);
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		productSave.setSupplierId(supplierId);
		productSave.setId(pid);
		Map<String, Object> map = ItemCenterUtil.checkSaveProductParam(productSave);
		if ((boolean) map.get("result")) {
			BaseJsonVO retObj = poItemFacade.saveProduct(productSave);
			return retObj;
		} else {
			List<String> retErrInfo = new ArrayList<String>();
			for (String key : map.keySet()) {
				if (!"result".equals(key)) {
					String err = (String) map.get(key);
					retErrInfo.add(key + "，" + err);
				}
			}
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.PARAMETER_NOT_VALID);
			retObj.setMessage(JsonUtils.toJson(retErrInfo));
			return retObj;
		}
	}

	@RequestMapping(value = "/rest/schedule/category", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getCategory(Model model, HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String poIdStr = request.getParameter("scheduleId");
		BaseJsonVO retObj = poItemFacade.getPoCategory(Long.valueOf(poIdStr), false);
		return retObj;
	}

	@RequestMapping(value = "/rest/schedule/product", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO getProductList(@RequestBody PoProductListSearchVO param) {
		try {
			BaseJsonListResultVO result = poItemFacade.getProductList(param);
			BaseJsonVO retObj = new BaseJsonVO(result);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@RequestMapping(value = "/rest/schedule/updateSort", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO updateSort(@RequestBody PoProductSortVO param) {

		BaseJsonVO retObj = poItemFacade.sortPoProduct(param);
		return retObj;
	}

	@RequestMapping(value = "rest/schedule/batchUploadProductInfo", method = RequestMethod.POST)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO batchUploadProductInfo(@RequestParam MultipartFile myfile,
			@RequestParam long scheduleId) {
		String key_prefix = "batchUploadPoProduct_";
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		long poId = scheduleId;
		String key = key_prefix + supplierId + "_" + poId;
		try {
			Boolean flag = (Boolean) staticDataHelper.getFromNkv(key);
			if (flag == null || flag == false) {
				staticDataHelper.putToNkv(key, true);
				List<String> retErrInfo = new ArrayList<String>();
				ScheduleDTO poDTO = poItemFacade.getScheduleDTO(poId);
				int poStatus = poDTO.getSchedule().getFlagAuditPrdList();
				int poType = poDTO.getScheduleVice().getSupplyMode().getIntValue();
				if (poStatus != 0 && poStatus != 1 && poStatus != 4) {
					BaseJsonVO retObj = new BaseJsonVO();
					retObj.setCode(ErrorCode.SUCCESS);
					retErrInfo.add("该po在当前状态下只能查看，不能添加、修改或取消商品。");
					retObj.setMessage(JsonUtils.toJson(retErrInfo));
					retObj.setResult(retErrInfo);
					staticDataHelper.putToNkv(key, false);
					return retObj;
				}
				// 获取工作表，兼容03及以上版本
				Workbook book = WorkbookFactory.create(myfile.getInputStream());
				List<BatchUploadPOProd> retList = new ArrayList<BatchUploadPOProd>();
				if (book != null) {
					int numberOfSheet = book.getNumberOfSheets();
					if (numberOfSheet > 0) {
						Sheet sheet = book.getSheetAt(0);
						Iterator<Row> iter = sheet.rowIterator();
						// 1.生成字段名和列明的关系
						Row rowOfFirst = iter.next();
						if (validExcelTitle(poType, rowOfFirst)) {
							while (iter.hasNext()) {
								Row row = iter.next();
								boolean isEmpty = ExcelUtils.isRowEmpty(row);
								if (!isEmpty) {
									int rowNum = row.getRowNum() + 1;
									try {
										BatchUploadPOProd productRow = generateProductBacthUpload(row);
										if (productRow == null) {
											logger.error("parse excel failed! ");
											retErrInfo.add("系统内部错误，第" + rowNum + "行，数据插入失败");
										} else {
											if (poType == 1) {
												productRow.setSupplyNum(0);
												if (productRow.getAddNum() <= 0) {
													StringBuffer msg = new StringBuffer();
													msg.append("第").append(rowNum).append("行").append("，");
													msg.append("条形码").append(productRow.getBarCode())
															.append("，供货量不能小于等于0；");
													retErrInfo.add(msg.toString());
													continue;
												}
											} else {
												if (productRow.getAddNum() < 0 || productRow.getSupplyNum() < 0) {
													StringBuffer msg = new StringBuffer();
													msg.append("第").append(rowNum).append("行").append("，");
													msg.append("条形码").append(productRow.getBarCode())
															.append("，供货量不能小于0；");
													retErrInfo.add(msg.toString());
													continue;
												}
												if (productRow.getAddNum() + productRow.getSupplyNum() <= 0) {
													StringBuffer msg = new StringBuffer();
													msg.append("第").append(rowNum).append("行").append("，");
													msg.append("条形码").append(productRow.getBarCode())
															.append("，供货量不能等于0；");
													retErrInfo.add(msg.toString());
													continue;
												}
											}
											retList.add(productRow);
										}
									} catch (ExcelParseExeption e) {
										List<ExcelParseExceptionInfo> errInfos = e.getInfoList();
										if (errInfos != null && errInfos.size() > 0) {
											StringBuffer msg = new StringBuffer();
											msg.append("第").append(rowNum).append("行").append("，");
											for (ExcelParseExceptionInfo errInfo : errInfos) {
												msg.append("字段“").append(errInfo.getColumnName()).append("”")
														.append("，").append(errInfo.getErrMsg()).append(";");
											}
											retErrInfo.add(msg.toString());
										}
									}
								}
							}
						} else {
							BaseJsonVO retObj = new BaseJsonVO();
							retObj.setCode(ErrorCode.SUCCESS);
							retErrInfo.add("该po为共同供货模式，“品牌商参与供货量”字段缺失。");
							retObj.setMessage(JsonUtils.toJson(retErrInfo));
							retObj.setResult(retErrInfo);
							staticDataHelper.putToNkv(key, false);
							return retObj;
						}
					}
				}
				Map<String, List<String>> map = poItemFacade.batchUploadProductInfo(retList, supplierId, poId, poType);
				if (map != null) {
					List<String> saveDBMsg = map.get("saveDBMsg");
					if (saveDBMsg != null && saveDBMsg.size() > 0) {
						retErrInfo.addAll(saveDBMsg);
					}

					List<String> validMsg = map.get("validMsg");
					if (validMsg != null && validMsg.size() > 0) {
						retErrInfo.addAll(validMsg);
					}
				}
				BaseJsonVO retObj = new BaseJsonVO();
				retObj.setCode(ErrorCode.SUCCESS);
				retObj.setMessage(JsonUtils.toJson(retErrInfo));
				retObj.setResult(retErrInfo);
				staticDataHelper.putToNkv(key, false);
				return retObj;
			} else {
				BaseJsonVO retObj = new BaseJsonVO();
				retObj.setCode(ErrorCode.SUCCESS);
				retObj.setMessage("uploading...");
				return retObj;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.ITEM_CENTER_ERROR);
			retObj.setMessage(ex.getMessage());
			staticDataHelper.putToNkv(key, false);
			return retObj;
		}
	}

	@RequestMapping(value = "/schedule/product/export", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:manage" })
	public void export(@RequestParam long id, HttpServletResponse response) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		List<ExportPoSkuVO> result = poItemFacade.getExportSkuVO(supplierId, id);
		XLSExport export = new XLSExport(String.valueOf(id));
		export.createEXCEL(result, ExportPoSkuVO.class);
		try {
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename=" + id + ".xls");
			export.exportXLS(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/schedule/product/preview", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:manage" })
	public @ResponseBody BaseJsonVO preview(@RequestParam long id) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		long pid = Long.valueOf(id);
		DetailProductVO productVO = commonFacade.getDetailPageProduct(pid, true, false);
		List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(Long.valueOf(productVO.getPoId()),
				productVO.getGoodsNo());
		retMap.put("product", productVO);
		retMap.put("colors", colorList);
		BaseJsonVO retObj = new BaseJsonVO(retMap);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	private boolean validExcelTitle(int type, Row row) {
		Cell cell = null;
		int cellNum = row.getLastCellNum();
		if (cellNum < 2)
			return false;
		if (type == 2 && cellNum <= 2)
			return false;

		cell = row.getCell(0);
		String barCode = (String) ExcelParse.getFieldValue(cell, String.class);
		cell = row.getCell(1);
		String selfSupply = (String) ExcelParse.getFieldValue(cell, String.class);
		String brandSupply = null;
		if (!"条形码".equals(barCode))
			return false;
		if (!"自供货量".equals(selfSupply))
			return false;
		if (type == 2) {
			cell = row.getCell(2);
			brandSupply = (String) ExcelParse.getFieldValue(cell, String.class);
			if (!"品牌商参与供货量".equals(brandSupply))
				return false;
		}
		return true;
	}

	private BatchUploadPOProd generateProductBacthUpload(Row row) throws ExcelParseExeption {
		BatchUploadPOProd productRow = new BatchUploadPOProd();
		boolean isExcelExp = false;
		List<ExcelParseExceptionInfo> infoList = new ArrayList<ExcelParseExceptionInfo>();
		int rowNum = row.getRowNum() + 1;
		Cell cell = null;
		int cellNum = row.getLastCellNum() - 1;

		cell = cellNum >= 0 ? row.getCell(0) : null;
		String barCode = (String) ExcelParse.getFieldValue(cell, String.class);
		if (!StringUtils.isBlank(barCode)) {
			productRow.setBarCode(barCode);
		} else {
			isExcelExp = true;
			ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, "条形码", ExcelUtils.INVALID);
			infoList.add(expInfo);
		}

		cell = cellNum >= 1 ? row.getCell(1) : null;
		String selfSupply = (String) ExcelParse.getFieldValue(cell, String.class);
		if (!StringUtils.isBlank(selfSupply) && StringUtils.isNumeric(selfSupply)) {
			Integer self = Integer.valueOf(selfSupply);
			productRow.setAddNum(self);
		} else if (StringUtils.isBlank(selfSupply)) {
			productRow.setAddNum(0);
		} else {
			isExcelExp = true;
			ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, "自供货量", ExcelUtils.INVALID);
			infoList.add(expInfo);
		}
		cell = cellNum >= 2 ? row.getCell(2) : null;
		String brandSupply = (String) ExcelParse.getFieldValue(cell, String.class);
		if (!StringUtils.isBlank(brandSupply) && StringUtils.isNumeric(brandSupply)) {
			Integer supply = Integer.valueOf(brandSupply);
			productRow.setSupplyNum(supply);
		} else if (StringUtils.isBlank(brandSupply)) {
			productRow.setSupplyNum(0);
		} else {
			isExcelExp = true;
			ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, "品牌商参与供货量", ExcelUtils.INVALID);
			infoList.add(expInfo);
		}
		if (isExcelExp) {
			throw new ExcelParseExeption(infoList);
		}
		productRow.setRowNum(rowNum);
		return productRow;
	}

}
