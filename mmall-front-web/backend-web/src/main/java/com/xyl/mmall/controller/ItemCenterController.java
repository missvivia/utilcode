package com.xyl.mmall.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.netease.push.util.JSONUtils;
import com.netease.vstore.photocenter.meta.AlbumImg;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.util.ConvertUtil;
import com.xyl.mmall.backend.vo.BacthUploadProduct;
import com.xyl.mmall.backend.vo.BatchUploadSize;
import com.xyl.mmall.backend.vo.ExportProductBodyVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.ProductParamVO;
import com.xyl.mmall.backend.vo.ProductSearchVO;
import com.xyl.mmall.backend.vo.SizeAssistVO;
import com.xyl.mmall.backend.vo.SizeTemplateEditVO;
import com.xyl.mmall.backend.vo.SizeTmplSearchVO;
import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.param.ProductAttr;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.excelparse.ExcelParse;
import com.xyl.mmall.excelparse.ExcelParseExceptionInfo;
import com.xyl.mmall.excelparse.ExcelParseExeption;
import com.xyl.mmall.excelparse.ExcelUtils;
import com.xyl.mmall.excelparse.XLSExport2007;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.NOSUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.param.BatchUploadPicParam;
import com.xyl.mmall.itemcenter.param.ChangeProductNameParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTemplateSaveParam;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.service.NkvStaticDataHelpler;

@Controller
public class ItemCenterController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterController.class);

	private static final String GOODS_NO = "货号+色号";

	private static final String BAR_CODE = "条码";

	private static final int buffer = 2048;

	private static final int MAXIMPORT_EXCELCOLUM_NUMBER = 16;

	@Autowired
	private ItemCenterFacade itemCenterFacade;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Resource(name = "commonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Autowired
	private NkvStaticDataHelpler staticDataHelper;

	
	@RequestMapping(value = "/product/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "product:edit" })
	public String editProduct(Model model, HttpServletRequest request, HttpServletResponse response) {
		appendStaticMethod(model);
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		long id = 0;
		String strId = request.getParameter("id");
		if (StringUtils.isNotBlank(strId)) {
			id = Long.parseLong(strId);
		}

		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		ProductEditVO product = itemCenterFacade.getProductVO(id, supplierId);
		model.addAttribute("productVO", product);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/product/edit";
	}

	@RequestMapping(value = "/rest/product/{id}", method = RequestMethod.DELETE)
	@RequiresPermissions(value = { "product:edit" })
	public @ResponseBody BaseJsonVO deleteProduct(@PathVariable long id) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		itemCenterFacade.deleteProduct(supplierId, id);
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "/rest/product/remove", method = RequestMethod.POST)
	@RequiresPermissions(value = { "product:edit" })
	public @ResponseBody BaseJsonVO removeProduct(@RequestBody List<Long> ids) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		BaseJsonVO retObj = itemCenterFacade.deleteProducts(supplierId, ids);
		return retObj;
	}

	@RequestMapping(value = "/sizeTemplate/edit", method = RequestMethod.GET)
	public String editSizeTemplate(Model model, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		long id = 0;
		String strId = request.getParameter("id");
		if (strId != null && !StringUtils.isBlank(strId)) {
			id = Long.parseLong(strId);
		}
		SizeTemplateEditVO sizeTemplate = itemCenterFacade.getSizeTemplateVO(id);

		model.addAttribute("sizeTemplateVO", sizeTemplate);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/product/add.sizetmp";
	}

	@RequestMapping(value = "/sizeTemplate/delete", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO deleteSizeTemplate(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			long loginId = SecurityContextUtils.getUserId();
			long supplierId = itemCenterFacade.getSupplierId(loginId);
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			long id = 0;
			String strId = request.getParameter("id");
			if (strId != null && !StringUtils.isBlank(strId)) {
				id = Long.parseLong(strId);
			}
			BaseJsonVO retObj = itemCenterFacade.deleteSizeTemplate(supplierId, id);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@RequestMapping(value = "/rest/product/getSizeTemplate", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getSize(Model model, HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			long loginId = SecurityContextUtils.getUserId();
			long supplierId = itemCenterFacade.getSupplierId(loginId);
			String productId = request.getParameter("productId");
			String lowCategoryId = request.getParameter("lowCategoryId");
			SizeVO sizeVO = itemCenterFacade.genSizeVO(Long.parseLong(productId), Long.parseLong(lowCategoryId), 0,
					supplierId);
			BaseJsonVO retObj = new BaseJsonVO(sizeVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	/**
	 * 尺码模板页面的读取尺码模板
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/rest/sizeTemplate/getSizeTemplateTable", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getSizeTemplateTable(Model model, HttpServletResponse response,
			HttpServletRequest request) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			String strSizeTemplateId = request.getParameter("sizeTemplateId");
			String strLowCategoryId = request.getParameter("lowCategoryId");
			long sizeTemplateId = 0;
			long lowCategoryId = 0;
			if (!StringUtils.isBlank(strLowCategoryId))
				sizeTemplateId = Long.parseLong(strSizeTemplateId);
			if (!StringUtils.isBlank(strLowCategoryId))
				lowCategoryId = Long.parseLong(strLowCategoryId);
			SizeTmplTableVO sizeTableVO = itemCenterFacade.getSizeTemplateTable(lowCategoryId, sizeTemplateId);
			BaseJsonVO retObj = new BaseJsonVO(sizeTableVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@RequestMapping(value = "/rest/product/getProductParams", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getProductParams(Model model, HttpServletResponse response,
			HttpServletRequest request) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			String categoryId = request.getParameter("categoryId");
			List<ProductParamVO> ProductParamList = commonFacade
					.getProductParamVOList(Long.parseLong(categoryId), null);
			BaseJsonVO retObj = new BaseJsonVO(ProductParamList);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	@RequestMapping(value = "/rest/product/changeProductName", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO changeProductName(@RequestBody ChangeProductNameParam param) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		param.setSupplierId(supplierId);
		BaseJsonVO retObj = itemCenterFacade.batchChangeProductName(param);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = { "/product/list" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "product:list" })
	public String getCategoryList(Model model) {
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		model.addAttribute("catetoryList", categoryList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/product/list";
	}

	@RequestMapping(value = "/rest/product/getCategories", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getCategoryList() {
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		BaseJsonVO retObj = new BaseJsonVO(categoryList);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "/rest/product/searchProduct", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO searchProduct(@RequestBody ProductSearchVO searchVO) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		searchVO.setSupplierId(supplierId);
		BaseJsonVO retObj = itemCenterFacade.searchProduct(searchVO);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	// 批量导出文件
	@RequestMapping(value = "/product/export", method = RequestMethod.GET)
	public void searchProduct(HttpServletResponse response, @RequestParam String pid) throws Exception {
		String zipFileName = "product.zip";
		Map<Long, List<ExportProductBodyVO>> voMap = new HashMap<Long, List<ExportProductBodyVO>>();
		List<Long> productIds = null;
		try {
			productIds = JSONUtils.parseArray(pid, Long.class);
		} catch (Exception e) {
			return;
		}

		if (productIds == null || productIds.size() == 0)
			return;
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		List<ExportProductBodyVO> list = itemCenterFacade.searchExportProduct(supplierId, productIds);
		if (list != null && list.size() > 0) {
			for (ExportProductBodyVO single : list) {
				long supId = single.getSuperCategoryId();
				List<ExportProductBodyVO> tmpList = voMap.get(supId);
				if (tmpList == null) {
					tmpList = new ArrayList<ExportProductBodyVO>();
					tmpList.add(single);
					voMap.put(supId, tmpList);
				} else {
					tmpList.add(single);
				}
			}
		}
		List<XLSExport2007> result = new ArrayList<XLSExport2007>();
		for (Long supId : voMap.keySet()) {
			List<ExportProductBodyVO> tmpList = voMap.get(supId);
			XLSExport2007 export = getExcelExport(supId);
			export.createEXCEL(tmpList, ExportProductBodyVO.class, 2);
			result.add(export);
		}

		List<String> fileList = new ArrayList<String>();
		File[] files = null;
		try {
			if (result != null && result.size() > 0) {
				// 导出EXCEL文件
				int count = 1;
				for (XLSExport2007 export : result) {
					String fileName = (count++) + ".xlsx";
					FileOutputStream output = new FileOutputStream(fileName);
					export.exportXLS(output);
					fileList.add(fileName);
				}
			}
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			files = new File[fileList.size()];
			for (int i = 0; i < fileList.size(); i++) {
				files[i] = new File(fileList.get(i));
			}
			zipFile(files, "", zos);
			zos.flush();
			zos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 删除临时文件
			if (files != null) {
				for (File f : files) {
					FileUtils.deleteQuietly(f);
				}
			}
			File f = new File(zipFileName);
			FileUtils.deleteQuietly(f);
		}
	}

	@RequestMapping(value = "/product/exportAll", method = RequestMethod.GET)
	public void searchProduct(HttpServletResponse response, @RequestParam String lowCategoryId,
			@RequestParam String isBaseInfo, @RequestParam String isPicInfo, @RequestParam String isDetailInfo,
			@RequestParam String isSizeSet, @RequestParam String productName, @RequestParam String goodsNo,
			@RequestParam String barCode, @RequestParam String stime, @RequestParam String etime) throws Exception {
		String zipFileName = "product.zip";
		Map<Long, List<ExportProductBodyVO>> voMap = new HashMap<Long, List<ExportProductBodyVO>>();
		ProductSearchVO searchVO = new ProductSearchVO();
		if (lowCategoryId != null && !"".equals(lowCategoryId))
			searchVO.setLowCategoryId(Long.valueOf(lowCategoryId));
		if (isBaseInfo != null && !"".equals(isBaseInfo))
			searchVO.setIsBaseInfo(Integer.valueOf(isBaseInfo));
		if (isPicInfo != null && !"".equals(isPicInfo))
			searchVO.setIsPicInfo(Integer.valueOf(isPicInfo));
		if (isDetailInfo != null && !"".equals(isDetailInfo))
			searchVO.setIsDetailInfo(Integer.valueOf(isDetailInfo));
		if (isSizeSet != null && !"".equals(isSizeSet))
			searchVO.setIsSizeSet(Integer.valueOf(isSizeSet));
		if (productName != null && !"".equals(productName))
			searchVO.setProductName(productName);
		if (goodsNo != null && !"".equals(goodsNo))
			searchVO.setGoodsNo(goodsNo);
		if (barCode != null && !"".equals(barCode))
			searchVO.setBarCode(barCode);
		if (stime != null && !"".equals(stime) && isNumeric(stime))
			searchVO.setStime(Long.valueOf(stime));
		if (etime != null && !"".equals(etime) && isNumeric(etime))
			searchVO.setEtime(Long.valueOf(etime));
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		searchVO.setSupplierId(supplierId);
		List<ExportProductBodyVO> list = itemCenterFacade.searchExportProduct(searchVO);
		if (list != null && list.size() > 0) {
			for (ExportProductBodyVO single : list) {
				long supId = single.getSuperCategoryId();
				List<ExportProductBodyVO> tmpList = voMap.get(supId);
				if (tmpList == null) {
					tmpList = new ArrayList<ExportProductBodyVO>();
					tmpList.add(single);
					voMap.put(supId, tmpList);
				} else {
					tmpList.add(single);
				}
			}
		}
		List<XLSExport2007> result = new ArrayList<XLSExport2007>();
		for (Long supId : voMap.keySet()) {
			List<ExportProductBodyVO> tmpList = voMap.get(supId);
			XLSExport2007 export = getExcelExport(supId);
			export.createEXCEL(tmpList, ExportProductBodyVO.class, 2);
			result.add(export);
		}

		List<String> fileList = new ArrayList<String>();
		File[] files = null;
		try {
			if (result != null && result.size() > 0) {
				// 导出EXCEL文件
				int count = 1;
				for (XLSExport2007 export : result) {
					String fileName = (count++) + ".xlsx";
					FileOutputStream output = new FileOutputStream(fileName);
					export.exportXLS(output);
					fileList.add(fileName);
				}
			}
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			files = new File[fileList.size()];
			for (int i = 0; i < fileList.size(); i++) {
				files[i] = new File(fileList.get(i));
			}
			zipFile(files, "", zos);
			zos.flush();
			zos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 删除临时文件
			if (files != null) {
				for (File f : files) {
					FileUtils.deleteQuietly(f);
				}
			}
			File f = new File(zipFileName);
			FileUtils.deleteQuietly(f);
		}
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	private XLSExport2007 getExcelExport(long categoryId) {
		String path = this.getClass().getResource("/public/res/files/" + categoryId + ".xlsx").getPath();
		logger.info("path::" + path);
		File f = new File(path);
		InputStream in = null;
		try {
			in = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XLSExport2007 export = new XLSExport2007(in);
		export.changeSheet(0);
		export.deleteRow(2);
		export.deleteRow(3);
		return export;
	}

	// 将文件写入zip包
	private void zipFile(File[] subs, String baseName, ZipOutputStream zos) throws IOException {
		for (int i = 0; i < subs.length; i++) {
			File f = subs[i];
			zos.putNextEntry(new ZipEntry(baseName + f.getName()));
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			int r = 0;
			while ((r = fis.read(buffer)) != -1) {
				zos.write(buffer, 0, r);
			}
			fis.close();
		}
	}

	@RequestMapping(value = { "/product/size" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "product:size" })
	public String sizeTemplatePage(Model model) {
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/product/sizesearch";
	}

	@RequestMapping(value = "/rest/sizeTemplate/search", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO searchSizeTemplate(@RequestBody SizeTmplSearchVO searchVO) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		searchVO.setSupplierId(supplierId);
		BaseJsonVO retObj = itemCenterFacade.searchSizeTemplate(searchVO);
		return retObj;
	}

	@RequestMapping(value = "/rest/sizeTemplate/save", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO addNewSizeTemplate(@RequestBody SizeTemplateSaveParam saveParam) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		saveParam.setSupplierId(supplierId);
		BaseJsonVO retObj = itemCenterFacade.saveSizeTemplate(saveParam);
		return retObj;
	}

	@RequestMapping(value = "/rest/product/{id}", method = RequestMethod.PUT)
	public @ResponseBody BaseJsonVO saveProduct(@PathVariable String id, @RequestBody String param) {
		JSONObject json = JSONObject.parseObject(param);
		ProductSaveParam productSave = ConvertUtil.convertJSONToProductParam(json);
		long pid = Long.parseLong(id);
		productSave.setId(pid);

		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		productSave.setSupplierId(supplierId);

		Map<String, Object> map = ItemCenterUtil.checkSaveProductParam(productSave);
		if ((boolean) map.get("result")) {
			BaseJsonVO retObj = itemCenterFacade.SingleSaveProduct(productSave);
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

	@RequestMapping(value = "/rest/product", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO addProduct(@RequestBody String param) {
		JSONObject json = JSONObject.parseObject(param);
		ProductSaveParam productSave = ConvertUtil.convertJSONToProductParam(json);
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		productSave.setSupplierId(supplierId);
		Map<String, Object> map = ItemCenterUtil.checkSaveProductParam(productSave);
		if ((boolean) map.get("result")) {
			BaseJsonVO retObj = itemCenterFacade.SingleSaveProduct(productSave);
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

	@RequestMapping(value = "/product/addtmp", method = RequestMethod.GET)
	public String addSizeTmp(Model model) {
		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/product/add.sizetmp";
	}

	@RequestMapping(value = "/product/viewtmp", method = RequestMethod.GET)
	public String viewSizeTmp(Model model) {
		model.addAttribute("name", "aaa");
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/product/view.sizetmp";
	}

	@RequestMapping(value = "/product/helper", method = RequestMethod.GET)
	public String sizeHelperPage(Model model) {
		model.addAttribute("name", "aaa");
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/product/helper";
	}

	@RequestMapping(value = "/product/viewhelper", method = RequestMethod.GET)
	public String viewHelper(Model model, HttpServletRequest request) {
		String paramId = request.getParameter("id");
		long id = 0;
		SizeAssistVO vo = null;
		if (!StringUtils.isBlank(paramId)) {
			try {
				id = Long.parseLong(paramId);
				vo = itemCenterFacade.getSizeAssistVO(id);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				vo = itemCenterFacade.getNewSizeAssistVO();
			}
		} else {
			vo = itemCenterFacade.getNewSizeAssistVO();
		}

		model.addAttribute("helperVO", vo);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/product/view.helper";
	}

	@RequestMapping(value = "rest/helpers/{id}", method = RequestMethod.DELETE)
	public @ResponseBody BaseJsonVO deleteHelper(@PathVariable long id) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		BaseJsonVO vo = itemCenterFacade.deleteSizeAssist(supplierId, id);
		return vo;
	}

	@RequestMapping(value = "rest/helpers/{id}", method = RequestMethod.PUT)
	public @ResponseBody BaseJsonVO saveHelper(@PathVariable long id, @RequestBody SizeAssistVO param) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		param.setSupplierId(supplierId);
		param.setId(String.valueOf(id));
		BaseJsonVO vo = itemCenterFacade.savesizeAssist(param);
		return vo;
	}

	@RequestMapping(value = "rest/helpers", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO addHelper(@RequestBody SizeAssistVO param) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		param.setSupplierId(supplierId);
		param.setId("0");
		BaseJsonVO vo = itemCenterFacade.savesizeAssist(param);
		return vo;
	}

	@RequestMapping(value = "rest/helpers", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO searchHelper(@RequestParam String limit, @RequestParam String offset,
			@RequestParam String lastId) {
		int sLimit = 0;
		int sOffset = 0;
		if (!StringUtils.isBlank(limit) && !"undefined".equals(limit)) {
			sLimit = Integer.valueOf(limit);
		}
		if (!StringUtils.isBlank(offset) && !"undefined".equals(offset)) {
			sOffset = Integer.valueOf(offset);
		}
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		BaseJsonVO vo = itemCenterFacade.searchSizeAssists(supplierId, sLimit, sOffset);
		return vo;
	}

	@RequestMapping(value = "/previewUnSavePage", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO previewUnSavePage(Model model, @RequestBody String param) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		JSONObject json = JSONObject.parseObject(param);
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		ProductSaveParam productSave = ConvertUtil.convertJSONToProductParam(json);
		productSave.setSupplierId(supplierId);
		DetailProductVO productVO = commonFacade.transferDetailProductVO(productSave);
		retMap.put("product", productVO);
		if (productSave.getScheduleId() <= 0) {
			List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(supplierId, productVO.getGoodsNo());
			if (productSave.getId() == 0) {
				DetailColorVO vo = new DetailColorVO();
				vo.setProductId(String.valueOf(productSave.getId()));
				List<String> picList = productSave.getProdShowPicList();
				if (picList != null && picList.size() > 0)
					vo.setThumb(picList.get(0));
				vo.setColorName(productSave.getColorName());
				colorList.add(vo);
			}
			retMap.put("colors", colorList);
		} else {
			List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(supplierId, productVO.getGoodsNo());

			retMap.put("colors", colorList);
		}

		BaseJsonVO retObj = new BaseJsonVO(retMap);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO productPreview(@RequestParam String id, RedirectAttributes model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		long pid = Long.valueOf(id);
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		DetailProductVO productVO = commonFacade.getDetailPageProduct(pid, false, false);
		List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(supplierId, productVO.getGoodsNo());
		retMap.put("product", productVO);
		retMap.put("colors", colorList);
		BaseJsonVO retObj = new BaseJsonVO(retMap);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}

	@RequestMapping(value = "/rest/batchUploadProductInfo", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO batchUploadProductInfo(@RequestParam MultipartFile myfile) {
		String key_prefix = "batchUploadProductInfo_";
		Map<String, String> brandNameMap = null;
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		String key = key_prefix + supplierId;
		try {
			Boolean flag = (Boolean) staticDataHelper.getFromNkv(key);
			if (flag == null || flag == false) {
				staticDataHelper.putToNkv(key, true);
				// 获取工作表，兼容03及以上版本
				List<String> retErrInfo = new ArrayList<String>();
				Workbook book = WorkbookFactory.create(myfile.getInputStream());
				List<BacthUploadProduct> retList = new ArrayList<BacthUploadProduct>();
				if (book != null) {
					int numberOfSheet = book.getNumberOfSheets();
					if (numberOfSheet > 0) {
						Sheet sheet = book.getSheetAt(0);
						String sheetName = sheet.getSheetName();
						Iterator<Row> iter = sheet.rowIterator();
						// 1.生成字段名和列明的关系
						Row rowOfFirst = iter.next();
						iter.next();
						if (!iter.hasNext()) {
							BaseJsonVO retObj = new BaseJsonVO();
							retObj.setCode(ErrorCode.SUCCESS);
							staticDataHelper.putToNkv(key, false);
							return retObj;
						}
						long categoryId = Long.valueOf(sheetName.split("_")[1]);

						while (iter.hasNext()) {
							Row row = iter.next();
							boolean isEmpty = ExcelUtils.isRowEmpty(row);
							if (!isEmpty) {
								if (brandNameMap == null || brandNameMap.isEmpty()) {
									brandNameMap = itemCenterFacade.getBrandName(supplierId);
								}
								int rowNum = row.getRowNum() + 1;
								Cell cell = row.getCell(3);
								String barcode = (String) ExcelParse.getFieldValue(cell, String.class);
								try {
									BacthUploadProduct productRow = generateProductBacthUpload(row, categoryId);
									if (!productRow.getBrandName().equals(brandNameMap.get("English"))
											&& !productRow.getBrandName().equals(brandNameMap.get("China"))) {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行").append("，");
										msg.append("条形码").append(barcode).append("，与商家的品牌名称不符。");
										retErrInfo.add(msg.toString());
										continue;
									}
									long superCateId = itemCenterFacade.getSuperCategory(productRow.getLowCategoryId());
									if (superCateId != categoryId) {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行").append("，");
										msg.append("条形码").append(barcode).append("，三级类目与导入文件的一级类目不符。");
										retErrInfo.add(msg.toString());
										continue;
									}
									if (retList.indexOf(productRow) >= 0) {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行").append("，");
										msg.append("条形码").append(barcode).append("，与其它货号色号商品已有条形码重复。");
										retErrInfo.add(msg.toString());
										continue;
									}
									Map<String, String> goodsColor = itemCenterFacade.getGoodsNoAndColorNum(supplierId,
											barcode);
									if (!goodsColor.isEmpty()
											&& (!goodsColor.get("goodsNo").equals(productRow.getGoodsNo()) || !goodsColor
													.get("colorNum").equals(productRow.getColorNum()))) {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行").append("，");
										msg.append("条形码").append(barcode).append("，与其它货号色号商品已有条形码重复。");
										retErrInfo.add(msg.toString());
										continue;
									}
									Long lowCategory = itemCenterFacade.getLowCategoryId(supplierId,
											productRow.getGoodsNo(), productRow.getColorNum());
									if (lowCategory != null && lowCategory != productRow.getLowCategoryId()) {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行").append("，");
										msg.append("条形码").append(barcode).append("，类目不可修改");
										retErrInfo.add(msg.toString());
										continue;
									}

									productRow.setRowNum(rowNum);
									retList.add(productRow);
								} catch (ExcelParseExeption e) {
									List<ExcelParseExceptionInfo> errInfos = e.getInfoList();
									if (errInfos != null && errInfos.size() > 0) {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行").append("，");
										msg.append("条形码").append(barcode).append("，");
										for (ExcelParseExceptionInfo errInfo : errInfos) {
											errInfo.getColumnName();
											errInfo.getErrMsg();
											msg.append("字段“").append(errInfo.getColumnName()).append("”").append("，")
													.append(errInfo.getErrMsg()).append(";");
										}
										retErrInfo.add(msg.toString());
									}
								}
							}
						}
					}
				}
				Map<String, List<String>> map = itemCenterFacade.batchUploadProductInfo(retList, supplierId);
				if (map != null) {
					List<String> saveDBMsg = map.get("saveDBMsg");
					if (saveDBMsg != null && saveDBMsg.size() > 0) {
						retErrInfo.addAll(saveDBMsg);
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

	@RequestMapping(value = "rest/batchUploadSizeInfo", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO batchUploadSizeInfo(@RequestParam MultipartFile myfile) {
		String key_prefix = "batchUploadSizeInfo_";
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		List<String> retErrInfo = new ArrayList<String>();
		String key = key_prefix + supplierId;
		try {
			Boolean flag = (Boolean) staticDataHelper.getFromNkv(key);
			if (flag == null || flag == false) {
				staticDataHelper.putToNkv(key, true);
				// 获取工作表，兼容03及以上版本
				Workbook book = WorkbookFactory.create(myfile.getInputStream());
				List<BatchUploadSize> sizeVOList = new ArrayList<BatchUploadSize>();
				if (book != null) {
					int numberOfSheet = book.getNumberOfSheets();
					if (numberOfSheet > 0) {
						Sheet sheet = book.getSheetAt(0);
						Iterator<Row> iter = sheet.rowIterator();
						// 1.生成字段名和列明的关系
						Map<Integer, String> header = null;
						while (iter.hasNext()) {
							Row headerRow = iter.next();
							header = ExcelUtils.genCellIdxAndFNameMap(headerRow);
							if (GOODS_NO.equals(header.get(0)) && BAR_CODE.equals(header.get(1)))
								break;
						}

						String goodNoColor = null;
						while (iter.hasNext()) {
							Row row = iter.next();
							boolean isEmpty = ExcelUtils.isRowEmpty(row);
							if (!isEmpty) {
								int rowNum = row.getRowNum() + 1;
								BatchUploadSize sizeVO = new BatchUploadSize();
								sizeVO.setRowNum(rowNum);
								List<String> sizeValues = new ArrayList<String>();
								boolean isSucc = true;
								String barCode = ExcelUtils.getValueOfCell(row.getCell(1));
								if (barCode == null)
									barCode = "";
								for (int cellIdx : header.keySet()) {
									Cell cell = row.getCell(cellIdx);
									String value = ExcelUtils.getValueOfCell(cell);
									if (cellIdx == 0) {
										if (StringUtils.isBlank(value)) {
											if (StringUtils.isBlank(goodNoColor)) {
												StringBuffer msg = new StringBuffer();
												msg.append("第").append(rowNum).append("行，");
												msg.append("条形码").append(barCode).append("，字段“货号+色号”，必填项未录入");
												retErrInfo.add(msg.toString());
												isSucc = false;
												break;
											} else
												value = goodNoColor;
										}
										String[] values = value.split("\\+");
										if (values.length != 2) {
											StringBuffer msg = new StringBuffer();
											msg.append("第").append(rowNum).append("行，");
											msg.append("条形码").append(barCode).append("，字段“货号+色号”没有录入色号或者货号与色号之间缺少“+”号");
											retErrInfo.add(msg.toString());
											isSucc = false;
											break;
										}
										sizeVO.setGoodNo(values[0]);
										sizeVO.setColorNum(values[1]);
										if (!value.equals(goodNoColor)) {
											goodNoColor = value;
										}
									} else if (cellIdx == 1) {
										if (StringUtils.isBlank(value)) {
											StringBuffer msg = new StringBuffer();
											msg.append("第").append(rowNum).append("行，");
											msg.append("条形码").append(barCode).append("，字段“条形码”，必填项未录入");
											retErrInfo.add(msg.toString());
											isSucc = false;
											break;
										} else if (value.length() > 32) {
											StringBuffer msg = new StringBuffer();
											msg.append("第").append(rowNum).append("行，");
											msg.append("条形码").append(barCode).append("，字段“条形码”，必须在32个字符以内");
											retErrInfo.add(msg.toString());
											isSucc = false;
											break;
										}
										sizeVO.setBarCode(value);
									} else if (cellIdx > 1) {
										if (value == null)
											value = "";
										sizeValues.add(value);
									}
								}
								if (isSucc) {
									sizeVO.setSizeValue(sizeValues);
									if (sizeVOList.indexOf(sizeVO) < 0)
										sizeVOList.add(sizeVO);
									else {
										StringBuffer msg = new StringBuffer();
										msg.append("第").append(rowNum).append("行，");
										msg.append("条形码").append(barCode).append("，字段“条形码”重复");
										retErrInfo.add(msg.toString());
										isSucc = false;
										break;
									}
								}
							}
						}
						List<String> colHeader = new ArrayList<String>();
						for (int cellIdx : header.keySet()) {
							String headName = header.get(cellIdx);
							if (!GOODS_NO.equals(headName) && !BAR_CODE.equals(headName)
									&& !StringUtils.isBlank(headName)) {
								colHeader.add(headName);
							}
						}
						Map<String, List<String>> map = itemCenterFacade.batchSaveSkuSize(supplierId, colHeader,
								sizeVOList);
						if (map != null) {
							List<String> saveDBMsg = map.get("saveDBMsg");
							if (saveDBMsg != null && saveDBMsg.size() > 0) {
								retErrInfo.addAll(saveDBMsg);
							}
						}
						BaseJsonVO retObj = new BaseJsonVO();
						retObj.setCode(ErrorCode.SUCCESS);
						retObj.setMessage(JsonUtils.toJson(retErrInfo));
						retObj.setResult(retErrInfo);
						staticDataHelper.putToNkv(key, false);
						return retObj;
					}
				}
				BaseJsonVO retObj = new BaseJsonVO();
				retObj.setCode(ErrorCode.ITEM_CENTER_ERROR);
				retObj.setMessage("上传有效的文件");
				staticDataHelper.putToNkv(key, false);
				return retObj;
			} else {
				BaseJsonVO retObj = new BaseJsonVO();
				retObj.setCode(ErrorCode.SUCCESS);
				retObj.setMessage("uploading...");
				return retObj;
			}
		} catch (Exception ex) {
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.ITEM_CENTER_ERROR);
			staticDataHelper.putToNkv(key, false);
			return retObj;
		}
	}

	@RequestMapping(value = "rest/batchUploadCustomHtml", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO batchUploadCustomHtml(@RequestParam MultipartFile myfile) {
		String key_prefix = "batchUploadCustomHtml_";
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		String key = key_prefix + supplierId;
		File file = null;
		try {
			Boolean flag = (Boolean) staticDataHelper.getFromNkv(key);
			if (flag == null || flag == false) {
				staticDataHelper.putToNkv(key, true);
				String fullName = myfile.getOriginalFilename();
				List<String> retErrInfo = new ArrayList<String>();
				Map<String, String> htmlMap = new HashMap<String, String>();
				if (!fullName.endsWith(".zip")) {
					BaseJsonVO retObj = new BaseJsonVO();
					retObj.setCode(ErrorCode.SUCCESS);
					retErrInfo.add("压缩文件格式错误，目前除zip外，其他扩展名暂不支持。");
					retObj.setMessage(JsonUtils.toJson(retErrInfo));
					retObj.setResult(retErrInfo);
					staticDataHelper.putToNkv(key, false);
					return retObj;
				}
				String fpath = this.getClass().getResource("/public/res/files/htmlUpload/0.zip").getPath();
				fpath = StringUtils.remove(fpath, "0.zip");
				logger.info("path::" + fpath);
				file = new File(fpath+supplierId + ".zip");
				
				boolean isZip = false;
				myfile.transferTo(file);
				ZipInputStream zis = new ZipInputStream(new FileInputStream(file), Charset.forName("GBK"));
				ZipEntry entry = null;
				while ((entry = zis.getNextEntry()) != null) {
					isZip = true;
					if (!entry.isDirectory()) {
						StringBuffer sb = new StringBuffer();
						String temp = entry.getName();
						if (StringUtils.endsWith(temp, ".txt")) {
							String goodsNo = StringUtils.substringBeforeLast(temp, ".txt");
							InputStreamReader read = new InputStreamReader(zis, "gbk");
							BufferedReader bufferedReader = new BufferedReader(read);
							String lineTxt = null;
							while ((lineTxt = bufferedReader.readLine()) != null) {
								sb.append(lineTxt);
							}
							String html = sb.toString();
							htmlMap.put(goodsNo, html);
						} else {
							retErrInfo.add("“" + temp + "”文本导入失败。出错原因如下：文本格式错误，目前除txt格式外，其他扩展名文本暂不支持。");
						}
					}
				}
				zis.close();
				if (!isZip) {
					BaseJsonVO retObj = new BaseJsonVO();
					retObj.setCode(ErrorCode.SUCCESS);
					retErrInfo.add("压缩文件格式错误，目前除zip外，其他扩展名暂不支持。");
					retObj.setMessage(JsonUtils.toJson(retErrInfo));
					retObj.setResult(retErrInfo);
					staticDataHelper.putToNkv(key, false);
					return retObj;
				}
				Map<String, List<String>> map = itemCenterFacade.batchSaveCustomHtml(supplierId, htmlMap);
				if (map != null) {
					List<String> saveDBMsg = map.get("saveDBMsg");
					if (saveDBMsg != null && saveDBMsg.size() > 0) {
						retErrInfo.addAll(saveDBMsg);
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

		} catch (IOException ex) {
			BaseJsonVO retObj = new BaseJsonVO();
			retObj.setCode(ErrorCode.ITEM_CENTER_ERROR);
			staticDataHelper.putToNkv(key, false);
			return retObj;
		}
	}

	@RequestMapping(value = "rest/batchUploadPic", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO batchUploadPic(@RequestParam MultipartFile myfile) {
		String key_prefix = "batchUploadPic_";
		File file = null;
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		String key = key_prefix + supplierId;
		try {
			Boolean flag = (Boolean) staticDataHelper.getFromNkv(key);
			if (flag == null || flag == false) {
				staticDataHelper.putToNkv(key, true);
				String fpath = this.getClass().getResource("/public/res/files/picUpload/0.zip").getPath();
				fpath = StringUtils.remove(fpath, "0.zip");
				logger.info("path::" + fpath);
				file = new File(fpath+supplierId + ".zip");
				logger.info("path::" + file.getAbsolutePath());
				List<String> retErrInfo = new ArrayList<String>();
				List<BatchUploadPicParam> paramList = new ArrayList<BatchUploadPicParam>();
				String fullName = myfile.getOriginalFilename();
				if (!fullName.endsWith(".zip")) {
					BaseJsonVO retObj = new BaseJsonVO();
					retObj.setCode(ErrorCode.SUCCESS);
					retErrInfo.add("压缩文件格式错误，目前除zip外，其他扩展名暂不支持。");
					retObj.setMessage(JsonUtils.toJson(retErrInfo));
					retObj.setResult(retErrInfo);
					staticDataHelper.putToNkv(key, false);
					return retObj;
				}
				if(!file.exists())
					file.createNewFile();
				myfile.transferTo(file);
				ZipInputStream zis = new ZipInputStream(new FileInputStream(file), Charset.forName("GBK"));
				ZipEntry entry = null;
				boolean isZip = false;
				while ((entry = zis.getNextEntry()) != null) {
					isZip = true;
					String temp = entry.getName();
					if (entry.isDirectory()) {
						String folderName = StringUtils.substringBeforeLast(temp, "/");
						String[] goodNoAndColor = folderName.split("\\+");
						if (goodNoAndColor.length != 2) {
							retErrInfo.add("“" + folderName + "”文件夹导入失败。出错原因如下：文件夹命名错误，“货号+色号”没有写色号或者货号与色号之间缺少“+”号。");
							continue;
						}
						String goodNo = goodNoAndColor[0];
						String colorNum = goodNoAndColor[1];
						BatchUploadPicParam param = new BatchUploadPicParam();
						param.setColorNum(colorNum);
						param.setGoodsNo(goodNo);
						int index = paramList.indexOf(param);
						if (index < 0) {
							paramList.add(param);
						}
						continue;
					} else {
						String folderName = StringUtils.substring(temp, 0, temp.indexOf("/"));
						if (StringUtils.endsWith(temp, ".jpg") || StringUtils.endsWith(temp, ".JPG")
								|| StringUtils.endsWith(temp, ".JPEG") || StringUtils.endsWith(temp, ".jpeg")) {
							String picName = null;
							if (StringUtils.endsWith(temp, ".jpg"))
								picName = StringUtils.substring(temp, temp.lastIndexOf("/") + 1,
										temp.lastIndexOf(".jpg"));
							else if (StringUtils.endsWith(temp, ".JPG")) {
								picName = StringUtils.substring(temp, temp.lastIndexOf("/") + 1,
										temp.lastIndexOf(".JPG"));
							} else if (StringUtils.endsWith(temp, ".JPEG"))
								picName = StringUtils.substring(temp, temp.lastIndexOf("/") + 1,
										temp.lastIndexOf(".JPEG"));
							else {
								picName = StringUtils.substring(temp, temp.lastIndexOf("/") + 1,
										temp.lastIndexOf(".jpeg"));
							}
							String[] goodNoAndColor = folderName.split("\\+");
							if (goodNoAndColor.length != 2) {
								continue;
							}
							String goodNo = goodNoAndColor[0];
							String colorNum = goodNoAndColor[1];
							if (!StringUtils.isBlank(picName) && StringUtils.isNumeric(picName)) {
								BatchUploadPicParam param = new BatchUploadPicParam();
								param.setColorNum(colorNum);
								param.setGoodsNo(goodNo);
								int order = Integer.valueOf(picName);
								long size = entry.getSize();
								if (size <= 1024 * 1204 * 2) {
									List<String> list = null;
									if (order >= 1 && order <= 16) {
										list = param.getProdShowPicList();
									} else if (order == 17 || order == 18) {
										list = param.getListShowPicList();
									} else if (order > 18) {
										list = param.getHtmlPicList();
									} else {
										retErrInfo.add("文件夹“" + folderName + "”，“" + temp + "”图片导入失败。出错原因如下：图片命名错误，“"
												+ temp + "”已超出图片命名规范。");
										continue;
									}
									BufferedImage img = genImgObject(zis);
									AlbumImg albumImg = new AlbumImg();
									albumImg.setInputStream(getImageStream(img));
									albumImg.setImgName(temp);
									String path = uploadPic(albumImg);
									if (StringUtils.isBlank(path)) {
										retErrInfo.add("文件夹“" + folderName + "”，“" + temp + "”图片导入失败。出错原因如下：上传失败。");
										continue;
									}
									list.add(path);

									int index = paramList.indexOf(param);
									if (index < 0) {
										paramList.add(param);
									} else {
										BatchUploadPicParam tmpParam = paramList.get(index);
										tmpParam.getProdShowPicList().addAll(param.getProdShowPicList());
										tmpParam.getListShowPicList().addAll(param.getListShowPicList());
										tmpParam.getHtmlPicList().addAll(param.getHtmlPicList());
									}
								} else {
									retErrInfo.add("文件夹“" + folderName + "”，“" + temp
											+ "”图片导入失败。出错原因如下：图片已超出单张2M以内大小的规范。");
									continue;
								}
							} else {
								retErrInfo.add("文件夹“" + folderName + "”，“" + temp + "”图片导入失败。出错原因如下：图片命名错误，“" + temp
										+ "”已超出图片命名规范。");
								continue;
							}
						} else {
							retErrInfo.add("文件夹“" + folderName + "”，“" + temp
									+ "”图片导入失败。出错原因如下：图片格式错误，目前除jpg图片外，其他扩展名图片暂不支持。");
							continue;
						}
					}

				}
				zis.close();
				if (!isZip) {
					BaseJsonVO retObj = new BaseJsonVO();
					retObj.setCode(ErrorCode.SUCCESS);
					retErrInfo.add("压缩文件格式错误，目前除zip外，其他扩展名暂不支持。");
					retObj.setMessage(JsonUtils.toJson(retErrInfo));
					retObj.setResult(retErrInfo);
					staticDataHelper.putToNkv(key, false);
					return retObj;
				}
				Map<String, List<String>> map = itemCenterFacade.batchSaveProductPic(supplierId, paramList);
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
			staticDataHelper.putToNkv(key, false);
			return retObj;
		} finally {
			if (file != null) {
				FileUtils.deleteQuietly(file);
			}
		}
	}

	@RequestMapping(value = "rest/clearStaticData", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO clearStaticData() {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		String key1 = "batchUploadProductInfo_" + supplierId;
		String key2 = "batchUploadSizeInfo_" + supplierId;
		String key3 = "batchUploadCustomHtml_" + supplierId;
		String key4 = "batchUploadPic_" + supplierId;
		String key5 = "batchUploadPoProduct_"+supplierId;
		staticDataHelper.putToNkv(key1, false);
		staticDataHelper.putToNkv(key2, false);
		staticDataHelper.putToNkv(key3, false);
		staticDataHelper.putToNkv(key4, false);
		staticDataHelper.putToNkv(key5, false);
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);
		retObj.setMessage("success");
		return retObj;
	}

	private BufferedImage genImgObject(ZipInputStream zis) {
		try {
			BufferedImage image = ImageIO.read(zis);
			return image;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private BacthUploadProduct generateProductBacthUpload(Row row, long categoryId) throws ExcelParseExeption {
		boolean isExcelExp = false;
		List<ExcelParseExceptionInfo> infoList = new ArrayList<ExcelParseExceptionInfo>();
		int rowNum = row.getRowNum();
		ExcelParse<BacthUploadProduct> excelParse = new ExcelParse<BacthUploadProduct>();
		BacthUploadProduct productRow = null;
		try {
			productRow = excelParse.getObject(row, BacthUploadProduct.class);
		} catch (ExcelParseExeption e) {
			throw e;
		}

		if (productRow == null)
			return null;

		List<ProductAttr> attrList = null;
		long sheet = 0;
		if (categoryId == 1 || categoryId == 2)
			sheet = 1;
		else if (categoryId == 3)
			sheet = 2;
		else
			sheet = 3;
		attrList = ItemCenterUtils.attrMap.get(sheet);
		// 生成导入excel的扩展字段的处理
		Map<Long, String> extendProperties = new HashMap<Long, String>();
		for (int colIdx = MAXIMPORT_EXCELCOLUM_NUMBER; colIdx < MAXIMPORT_EXCELCOLUM_NUMBER + attrList.size(); colIdx++) {
			int attrInx = colIdx - MAXIMPORT_EXCELCOLUM_NUMBER;
			ProductAttr attr = attrList.get(attrInx);
			Cell cell = row.getCell(colIdx);

			String content = (cell != null ? cell.toString() : null);
			if (attr.isRequired() && StringUtils.isBlank(content)) {
				isExcelExp = true;
				ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, attr.getAttrName(),
						ExcelUtils.BLANK);
				infoList.add(expInfo);
				continue;
			}
			if (!StringUtils.isBlank(content)) {
				extendProperties.put(attr.getId(), content);
			}
		}
		if (isExcelExp) {
			throw new ExcelParseExeption(infoList);
		}
		productRow.setProperties(extendProperties);
		return productRow;
	}

	private String uploadPic(AlbumImg img) {
		if (img == null) {
			logger.error("error::img is null");
			return null;
		}
		List<AlbumImg> list = new ArrayList<AlbumImg>();
		list.add(img);
		try {
			NOSUtil.uploadImgList(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return img.getImgUrl();
	}

	private InputStream getImageStream(BufferedImage bi) {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut;
		try {
			imOut = ImageIO.createImageOutputStream(bs);
			ImageIO.write(bi, "jpg", imOut);
			is = new ByteArrayInputStream(bs.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	// @RequestMapping(value = "rest/performance", method = RequestMethod.GET)
	// public @ResponseBody BaseJsonVO performance() {
	// long loginId = SecurityContextUtils.getUserId();
	// long supplierId = itemCenterFacade.getSupplierId(loginId);
	// itemCenterFacade.performance(supplierId);
	// BaseJsonVO retObj = new BaseJsonVO();
	// retObj.setCode(ErrorCode.SUCCESS);
	// return retObj;
	// }
}
