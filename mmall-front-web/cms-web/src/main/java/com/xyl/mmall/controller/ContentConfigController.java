package com.xyl.mmall.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.netease.ndir.common.util.FileUtil;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.netease.print.common.util.FileOperationUtil;
import com.netease.print.exceljar.ExcelUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.ContentConfigureFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.common.enums.FileTypeEnum;
import com.xyl.mmall.framework.config.FileDirConfiguration;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.poi.ExcelDataExtracter;
import com.xyl.mmall.framework.poi.IExcelDataExtracter;
import com.xyl.mmall.framework.poi.IllegalConfigException;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.vo.MainsiteCategoryContentVO;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 买家首页动态配置管理
 * 
 * @author author:lhp
 * @version date:2015年10月10日下午3:45:18
 */
@Controller
public class ContentConfigController {

	private static final Logger logger = LoggerFactory.getLogger(ContentConfigController.class);

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private FileDirConfiguration fileDirConfiguration;

	@Autowired
	private ContentConfigureFacade contentConfigureFacade;

	private static final String keyOfSkuList = "goodsList";

	@Resource(name = "freemarkerConfig4Template")
	private Configuration fmConfig;

	@RequestMapping(value = { "/content/config" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	public String homeNew(Model model) {
		long lastModifiedTime = 0l;
		Map<String, String> fileTypeMap = new HashMap<String, String>();
		for (FileTypeEnum type : FileTypeEnum.values()) {
			File file = new File(fileDirConfiguration.getIndexDataFileDir() + type.getBackUpFileName());
			if (file.exists()) {
				lastModifiedTime = file.lastModified();
				fileTypeMap.put(String.valueOf(type.getIntValue()),
						DateFormatEnum.TYPE5.getFormatDate(lastModifiedTime));
			}
		}
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("backupFileMap", fileTypeMap);
		return "pages/contentConfig/config";
	}

	/**
	 * 文件上传成功页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/content/uploadSuccess" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	public String uploadSuccess(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/contentConfig/uploadSuccess";
	}

	/**
	 * 发布成功页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/content/publishSuccess" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	public String publishSuccess(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/contentConfig/publishSuccess";
	}

	/**
	 * 恢复成功页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/content/recoverSuccess" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	public String recoverSuccess(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/contentConfig/recoverSuccess";
	}

	/**
	 * 生成预览页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/content/previewIndex" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	@ResponseBody
	public BaseJsonVO previewIndex(@RequestParam(value = "fileType", defaultValue = "1") int fileType) {
		BaseJsonVO ret = new BaseJsonVO();
		FileTypeEnum fileTypeEnum = FileTypeEnum.genFileTypeEnumByValue(fileType);

		// 目前只有wap和app需要生成预览页面
		if (fileTypeEnum == null) {
			ret.setCode(ErrorCode.INCOMING_PARAMETER_IS_ILLEGAL);
			ret.setMessage(ErrorCode.INCOMING_PARAMETER_IS_ILLEGAL.getDesc());
			return ret;
		}
		String indexPreHtmlPath = "";
		String preHtmlUrl = "";

		switch (fileTypeEnum) {
		case WEB_INDEX_FILE:
			preHtmlUrl = fileDirConfiguration.getWebPreHtmlUrl();
			break;
		case WAP_INDEX_FILE:
			// 生成wap预览首页逻辑
			indexPreHtmlPath = fileDirConfiguration.getWapPreHtmlDir() + "preIndex.html";
			preHtmlUrl = fileDirConfiguration.getWapPreHtmlUrl();
			ret = publishWapIndex(ret, indexPreHtmlPath);
			break;
		case APP_INDEX_FILE:
			// 生成app预览首页逻辑
			indexPreHtmlPath = fileDirConfiguration.getAppIndexHtmlDir() + "preIndex.html";
			logger.info("preHtml path>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + indexPreHtmlPath);
			preHtmlUrl = fileDirConfiguration.getAppPreHtmlUrl();
			ret = publishAppIndex(ret, indexPreHtmlPath);
			break;
		default:
			break;
		}

		if (ret.getCode() == ResponseCode.RES_ERROR) {
			return ret;
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setMessage("successful");

		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("preHtmlUrl", preHtmlUrl);
		ret.setResult(hashMap);
		return ret;
	}

	/**
	 * 发布首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/content/publishIndex", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	@ResponseBody
	public BaseJsonVO publishIndex(@RequestParam(value = "fileType", defaultValue = "1") int fileType) {
		BaseJsonVO ret = new BaseJsonVO();
		FileTypeEnum fileTypeEnum = FileTypeEnum.genFileTypeEnumByValue(fileType);
		if (fileTypeEnum == null) {
			ret.setMessage("参数有误!");
			return ret;
		}
		String indexHtmlPath = "", indexPreHtmlPath = "", backupFileNamePath = "";
		switch (fileTypeEnum) {
		case WEB_INDEX_FILE:
			indexHtmlPath = fileDirConfiguration.getWebIndexHtmlDir() + "index.ftl";
			indexPreHtmlPath = fileDirConfiguration.getWebIndexHtmlDir() + "preIndex.ftl";
			backupFileNamePath = fileDirConfiguration.getWebIndexHtmlDir() + fileTypeEnum.getBackUpFileName();
			// 生成web首页逻辑
			ret = publishWebIndex(ret);
			break;
		case WAP_INDEX_FILE:
			// 生成wap首页逻辑
			indexHtmlPath = fileDirConfiguration.getWapIndexHtmlDir() + "index.html";
			indexPreHtmlPath = fileDirConfiguration.getWapIndexHtmlDir() + "preIndex.html";
			backupFileNamePath = fileDirConfiguration.getWapIndexHtmlDir() + fileTypeEnum.getBackUpFileName();
			ret = publishWapIndex(ret, indexPreHtmlPath);// 因预览页和发布页不在同一个目录，所以发布时重新生成html
			break;
		case APP_INDEX_FILE:
			// 生成app首页逻辑
			indexHtmlPath = fileDirConfiguration.getAppIndexHtmlDir() + "index.html";
			indexPreHtmlPath = fileDirConfiguration.getAppIndexHtmlDir() + "preIndex.html";
			backupFileNamePath = fileDirConfiguration.getAppIndexHtmlDir() + fileTypeEnum.getBackUpFileName();
			// ret = publishAppIndex(ret, indexPreHtmlPath);
			break;

		default:
			// 生成web首页逻辑
			ret = publishWebIndex(ret);
			break;
		}
		if (ret.getCode() == ResponseCode.RES_ERROR) {
			return ret;
		}

		// 将先前的index备份
		try {
			File indexHtml = new File(indexHtmlPath);
			if (indexHtml.exists()) {
				FileUtil.copyFile(indexHtml,
						new File(fileDirConfiguration.getIndexDataFileDir() + fileTypeEnum.getBackUpFileName()));
			}
		} catch (IOException e) {
			logger.error("backup index file failed:", e);
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("备份文件失败！");
			return ret;
		}
		FileUtil.delDirFile(new File(backupFileNamePath));
		FileOperationUtil.renameFile(indexHtmlPath, backupFileNamePath, false);
		// 将新生成的首页改成index,发布
		FileOperationUtil.renameFile(indexPreHtmlPath, indexHtmlPath, false);
		logger.info("restore fileType {} html by {}", fileType, SecurityContextUtils.getUserId());
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setMessage("successful");
		return ret;
	}

	private BaseJsonVO publishWapIndex(BaseJsonVO ret, String preFile) {
		Writer outWap = null;
		try {
			IExcelDataExtracter excel = new ExcelDataExtracter(new File(fileDirConfiguration.getIndexDataFileDir()
					+ FileTypeEnum.WAP_INDEX_FILE.getFileName()));
			Map<String, Object> data = excel.getAllData();

			// 将配置的商品sku转换为显示用的对象
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<Long> goodsList = (List) data.get(keyOfSkuList);
			Set<Long> nonExists = contentConfigureFacade.validateSkuExists(goodsList);
			if (!nonExists.isEmpty()) {
				throw new IllegalConfigException("不存在商品SKU" + Arrays.toString(nonExists.toArray(new Long[0])));
			}
			data.put(keyOfSkuList, contentConfigureFacade.getSKUBy(goodsList));

			Template tplWap = fmConfig.getTemplate("wap-index-template.ftl");
			outWap = new FileWriter(preFile);
			tplWap.process(data, outWap);
			outWap.flush();
		} catch (IllegalConfigException e) {
			ret.setCode(ResponseCode.RES_ERROR);
			String msg = "Excel配置错误：" + e.getMessage();
			ret.setMessage(msg);
		} catch (Exception e) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage(e.getMessage());
		} finally {
			if (outWap != null)
				try {
					outWap.close();
				} catch (IOException e) {
					ret.setCode(ResponseCode.RES_ERROR);
					ret.setMessage(e.getMessage());
				}
		}
		return ret;
	}

	private BaseJsonVO publishAppIndex(BaseJsonVO ret, String preFile) {
		FileOutputStream outStream = null;
		OutputStreamWriter writer = null;
		BufferedWriter sw = null;
		try {
			IExcelDataExtracter excel = new ExcelDataExtracter(new File(fileDirConfiguration.getIndexDataFileDir()
					+ FileTypeEnum.APP_INDEX_FILE.getFileName()));
			Map<String, Object> data = excel.getAllData();

			// 将配置的商品sku转换为显示用的对象
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<Long> goodsList = (List) data.get(keyOfSkuList);
			Set<Long> nonExists = contentConfigureFacade.validateSkuExists(goodsList);
			if (!nonExists.isEmpty()) {
				throw new IllegalConfigException("不存在商品SKU" + Arrays.toString(nonExists.toArray(new Long[0])));
			}
			data.put(keyOfSkuList, contentConfigureFacade.getSKUBy(goodsList));

			Template tplWap = fmConfig.getTemplate("app-index-template.ftl");
			outStream = new FileOutputStream(new File(preFile));
			writer = new OutputStreamWriter(outStream, "UTF-8");
			sw = new BufferedWriter(writer);
			sw.flush();
			tplWap.process(data, sw);
		} catch (IllegalConfigException e) {
			ret.setCode(ResponseCode.RES_ERROR);
			String msg = "Excel配置错误：" + e.getMessage();
			ret.setMessage(msg);
		} catch (Exception e) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage(e.getMessage());
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					ret.setCode(ResponseCode.RES_ERROR);
					ret.setMessage(e.getMessage());
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					ret.setCode(ResponseCode.RES_ERROR);
					ret.setMessage(e.getMessage());
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					ret.setCode(ResponseCode.RES_ERROR);
					ret.setMessage(e.getMessage());
				}
			}

		}
		return ret;
	}

	private BaseJsonVO publishWebIndex(BaseJsonVO ret) {
		// 先从上传的excel读取数据
		List<Sheet> sheets = ExcelUtil.getSheetList(fileDirConfiguration.getIndexDataFileDir()
				+ FileTypeEnum.WEB_INDEX_FILE.getFileName());
		if (CollectionUtil.isEmptyOfList(sheets)) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("该文件不存在");
			return ret;
		}
		Map<Integer, String> excelColumnMap = contentConfigureFacade.getMainsiteIndexDataFromSheet(sheets.get(0));
		if (excelColumnMap.get((int) ResponseCode.RES_ERROR) != null) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage(excelColumnMap.get((int) ResponseCode.RES_ERROR));
			return ret;
		}
		// 再将模板文件数据替换掉
		BufferedReader br = FileOperationUtil
				.genBufferedReader("public/htmlTemplate/mainsite-index-template.ftl", true);

		if (br == null) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("该文件不存在");
			return ret;
		}

		BufferedWriter bw = FileOperationUtil.genBufferedWriter(fileDirConfiguration.getWebIndexHtmlDir()
				+ "preIndex.ftl", false, false);
		try {
			String line = "";
			int count = 0;
			while (true) {
				line = FileOperationUtil.readLine(br);
				if (StringUtils.isEmpty(line)) {
					continue;
				}
				if (line.contains("--fileEnd--")) {
					break;
				}
				if (line.contains("{0}")) {
					line = MessageFormat.format(line, excelColumnMap.get(count));
					++count;
				}
				bw.write(line);
				bw.newLine();
			}
			bw.flush();

		} catch (IOException e) {
			ret.setCode(ResponseCode.RES_ERROR);
			logger.error("publish index error:", e);
			return ret;
		}
		FileOperationUtil.closeBufferedReader(br);
		FileOperationUtil.closeBufferedWriter(bw);
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;

	}

	@RequestMapping(value = "/content/uploadIndexData")
	@RequiresPermissions(value = { "content:config" })
	public void uploadIndexData(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "fileType", defaultValue = "4") int fileType, HttpServletResponse response) {
		// 返回结果写入文件
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=result.txt");
		OutputStream out = null;
		BufferedOutputStream buffer = null;
		try {
			out = response.getOutputStream();
			buffer = new BufferedOutputStream(out);
			// 初始化workboot
			Workbook workbook = com.xyl.mmall.framework.util.ExcelUtil.initWorkbook(file);
			if (workbook == null) {
				buffer.write("无法读取文件，请确保文件是xlsx！\n".getBytes("utf-8"));
			} else {
				FileTypeEnum fileTypeEnum = FileTypeEnum.genFileTypeEnumByValue(fileType);
				if (fileTypeEnum == null) {
					buffer.write("文件类型参数有误！\n".getBytes("utf-8"));
					return;
				}
				switch (fileTypeEnum) {
				case WEB_INDEX_FILE:
					// 验证web首页上传数据逻辑
					Map<Integer, String> excelColumnMap = contentConfigureFacade.getMainsiteIndexDataFromSheet(workbook
							.getSheetAt(0));
					if (excelColumnMap.get((int) ResponseCode.RES_ERROR) != null) {
						buffer.write(excelColumnMap.get((int) ResponseCode.RES_ERROR).getBytes("utf-8"));
						buffer.write("上传文件失败！".getBytes("utf-8"));
						return;
					}
					break;
				case WAP_INDEX_FILE:
					// 验证wap首页上传数据逻辑 执行app的逻辑
				case APP_INDEX_FILE:
					// 验证app首页上传数据逻辑
					try (InputStream inp = file.getInputStream()) {
						IExcelDataExtracter excel = new ExcelDataExtracter(inp);
						Map<String, Object> data = excel.getAllData();
						if (data == null || data.isEmpty()) {
							buffer.write("上传文件失败，文件中没有数据！".getBytes("utf-8"));
							return;
						}
						Set<Long> invalidSkuIds = contentConfigureFacade.validateSkuExists((List) data
								.get(keyOfSkuList));
						if (!invalidSkuIds.isEmpty()) {
							throw new IllegalConfigException("不存在商品SKU"
									+ Arrays.toString(invalidSkuIds.toArray(new Long[0])));
						}
					} catch (IllegalConfigException e) {
						String msg = "Excel配置错误：" + e.getMessage();
						buffer.write(msg.getBytes("utf-8"));
						return;
					} catch (Exception e) {
						buffer.write(e.getMessage().getBytes("utf-8"));
						return;
					}
					break;
				case WEB_CATEGORY_FILE:
					// 验证web首页类目上传数据逻辑 TODO
					break;
				case WAP_CATEGORY_FILE:
					// 验证wap首页类目上传数据逻辑 TODO
					break;
				case APP_CATEGORY_FILE:
					// 验证app首页类目上传数据逻辑 TODO
					break;

				default:
					break;
				}
				// 转存文件
				file.transferTo(new File(fileDirConfiguration.getIndexDataFileDir() + fileTypeEnum.getFileName()));
				buffer.write("200".getBytes());// 上传成功code 200
				logger.info("upload fileType {} excel data by {}", fileType, SecurityContextUtils.getUserId());
			}
		} catch (Exception e) {
			logger.error("upload indexData failed!", e);
			try {
				if (null != buffer) {
					buffer.write("上传文件失败！".getBytes("utf-8"));
				}
			} catch (Exception e1) {
				logger.error("upload indexData failed! Can't write back error message!", e1);
			}
		} finally {
			if (null != buffer) {
				try {
					buffer.flush();
					buffer.close();
				} catch (IOException e) {
					logger.error("upload indexData failed! BufferedOutputStream error!", e);
				}
			}
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					logger.error("upload indexData failed! OutputStream error!", e);
				}
			}
		}
	}

	/**
	 * 预览时生成类目html
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/content/buildCategoryHtml" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	@ResponseBody
	public BaseJsonVO buildCategoryHtml(@RequestParam(value = "fileType", defaultValue = "4") int fileType) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_ERROR);
		FileTypeEnum fileTypeEnum = FileTypeEnum.genFileTypeEnumByValue(fileType);
		if (fileTypeEnum == null) {
			ret.setMessage("参数有误!");
			return ret;
		}
		String categroyFtlName = "", categroyHtmlPath = "";
		switch (fileTypeEnum) {
		case WEB_CATEGORY_FILE:
			// 设置web首页类目html目录
			categroyFtlName = "web-category.ftl";
			categroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-web-category.html";
			break;
		case WAP_CATEGORY_FILE:
			// 设置wap首页类目html目录
			categroyFtlName = "wap-category.ftl";
			categroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-wap-category.html";
			break;
		case APP_CATEGORY_FILE:
			// 设置app首页类目html目录
			categroyFtlName = "app-category.ftl";
			categroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-app-category.html";
			break;

		default:
			categroyFtlName = "web-category.ftl";
			categroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-web-category.html";
			break;
		}
		Writer out = null;
		try {
			// 先从上传的excel读取数据
			List<Sheet> sheets = ExcelUtil.getSheetList(fileDirConfiguration.getIndexDataFileDir()
					+ fileTypeEnum.getFileName());
			if (CollectionUtil.isEmptyOfList(sheets)) {
				ret.setMessage("该文件不存在");
				return ret;
			}
			// 创建数据模型
			Map<String, List<MainsiteCategoryContentVO>> data = new HashMap<String, List<MainsiteCategoryContentVO>>();
			data.put("categoryList", contentConfigureFacade.getWebMainsiteCategoryDataFromSheet(sheets.get(0)));
			// 获取指定模板文件
			Template template = fmConfig.getTemplate(categroyFtlName);
			// 定义输入文件，默认生成在工程根目录
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(categroyHtmlPath), "UTF-8"));
			// 最后开始生成
			template.process(data, out);
			logger.info("build fileType {} preview category html by {}", fileType, SecurityContextUtils.getUserId());
		} catch (Exception e) {
			logger.error("build category html error:", e.getMessage());
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("生成类目静态文件失败");
			return ret;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					ret.setCode(ResponseCode.RES_ERROR);
					ret.setMessage(e.getMessage());
					return ret;
				}
			}
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}

	/**
	 * 发布类目
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/content/publishCategoryHtml", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	@ResponseBody
	public BaseJsonVO publishCategoryHtml(@RequestParam(value = "fileType", defaultValue = "4") int fileType) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_ERROR);
		FileTypeEnum fileTypeEnum = FileTypeEnum.genFileTypeEnumByValue(fileType);
		if (fileTypeEnum == null) {
			ret.setMessage("参数有误!");
			return ret;
		}
		String preCategroyHtmlPath = "", categroyHtmlPathDir = "", backcategroyHtmlName = fileTypeEnum
				.getBackUpFileName();
		switch (fileTypeEnum) {
		case WEB_CATEGORY_FILE:
			// 设置web首页类目html目录
			preCategroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-web-category.html";
			categroyHtmlPathDir = fileDirConfiguration.getWebCategroyHtmlDir();
			break;
		case WAP_CATEGORY_FILE:
			// 设置wap首页类目html目录
			preCategroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-wap-category.html";
			categroyHtmlPathDir = fileDirConfiguration.getWapCategroyHtmlDir();
			break;
		case APP_CATEGORY_FILE:
			// 设置app首页类目html目录
			preCategroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-app-category.html";
			categroyHtmlPathDir = fileDirConfiguration.getAppCategroyHtmlDir();
			break;

		default:
			preCategroyHtmlPath = fileDirConfiguration.getWebPreHtmlDir() + "pre-web-category.html";
			categroyHtmlPathDir = fileDirConfiguration.getWebCategroyHtmlDir();
			break;
		}
		try {
			// 将将预览类目页面赋值到首页类目目录下
			FileUtil.copyFile(new File(preCategroyHtmlPath), new File(categroyHtmlPathDir + "new-category.html"));
			// 备份
			FileUtil.copyFile(new File(categroyHtmlPathDir + "category.html"),
					new File(fileDirConfiguration.getIndexDataFileDir() + backcategroyHtmlName));
		} catch (IOException e) {
			logger.error("backup category file failed:", e);
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("备份文件失败！");
			return ret;
		}
		FileUtil.delDirFile(new File(categroyHtmlPathDir + fileTypeEnum.getBackUpFileName()));
		FileOperationUtil.renameFile(categroyHtmlPathDir + "category.html",
				categroyHtmlPathDir + fileTypeEnum.getBackUpFileName(), false);
		// 将新生成的首页类目发布
		FileOperationUtil.renameFile(categroyHtmlPathDir + "new-category.html", categroyHtmlPathDir + "category.html",
				false);
		logger.info("publish fileType {} category html by {}", fileType, SecurityContextUtils.getUserId());
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setMessage("successful");
		return ret;
	}

	/**
	 * 还原上一版
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/content/restore", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:config" })
	@ResponseBody
	public BaseJsonVO restoreFile(@RequestParam(value = "fileType", defaultValue = "4") int fileType) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_ERROR);
		FileTypeEnum fileTypeEnum = FileTypeEnum.genFileTypeEnumByValue(fileType);
		if (fileTypeEnum == null) {
			ret.setMessage("参数有误!");
			return ret;
		}
		String onLineHtmlDir = "", onLineHtmlPath = "", backHtmlName = fileTypeEnum.getBackUpFileName();
		switch (fileTypeEnum) {
		case WEB_INDEX_FILE:
			onLineHtmlDir = fileDirConfiguration.getWebIndexHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "index.ftl";
			break;
		case WAP_INDEX_FILE:
			onLineHtmlDir = fileDirConfiguration.getWapIndexHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "index.html";
			break;
		case APP_INDEX_FILE:
			onLineHtmlDir = fileDirConfiguration.getAppIndexHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "index.html";
			break;
		case WEB_CATEGORY_FILE:
			// 设置web首页类目html目录
			onLineHtmlDir = fileDirConfiguration.getWebCategroyHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "category.html";
			break;
		case WAP_CATEGORY_FILE:
			// 设置wap首页类目html目录
			onLineHtmlDir = fileDirConfiguration.getWapCategroyHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "category.html";
			break;
		case APP_CATEGORY_FILE:
			// 设置app首页类目html目录
			onLineHtmlDir = fileDirConfiguration.getAppCategroyHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "category.html";
			break;

		default:
			onLineHtmlDir = fileDirConfiguration.getWebCategroyHtmlDir();
			onLineHtmlPath = onLineHtmlDir + "index.ftl";
			break;
		}
		if (new File(fileDirConfiguration.getIndexDataFileDir() + backHtmlName).exists()) {
			try {
				// 首页类目存在备份分类时
				if (!new File(onLineHtmlDir + backHtmlName).exists()) {
					FileUtil.copyFile(new File(fileDirConfiguration.getIndexDataFileDir() + backHtmlName), new File(
							onLineHtmlDir + backHtmlName));
				}
				FileUtil.copyFile(new File(onLineHtmlPath), new File(fileDirConfiguration.getIndexDataFileDir()
						+ backHtmlName));
			} catch (IOException e) {
				logger.error("backup category  file failed:", e);
				ret.setMessage("备份文件失败！");
				return ret;
			}
		} else {
			ret.setMessage("没有备份文件!");
			return ret;
		}
		// 将先前的首页分类备份还原
		FileOperationUtil.renameFile(onLineHtmlPath, onLineHtmlPath + "_bak", false);
		FileOperationUtil.renameFile(onLineHtmlDir + backHtmlName, onLineHtmlPath, false);
		FileOperationUtil.renameFile(onLineHtmlPath + "_bak", onLineHtmlDir + backHtmlName, false);
		logger.info("restore fileType {} category html by {}", fileType, SecurityContextUtils.getUserId());
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setMessage("successful");
		return ret;

	}

}
