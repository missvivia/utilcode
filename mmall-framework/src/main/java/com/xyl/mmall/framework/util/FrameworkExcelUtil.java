package com.xyl.mmall.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.netease.print.common.util.FileOperationUtil;
import com.netease.print.exceljar.ExcelExportUtil;

/**
 * @author dingmingliang
 *
 */
public final class FrameworkExcelUtil {

	private static Logger logger = Logger.getLogger(FrameworkExcelUtil.class);

	public static void writeExcel(String sheetName, LinkedHashMap<String, String> columnMap, Class<?> clazz,
			String filePath, Collection<? extends Serializable> collection, HttpServletRequest request,
			HttpServletResponse response) {
		ExcelExportUtil util = new ExcelExportUtil();
		util.initExcelExportUtil(sheetName, columnMap, clazz);
		util.write(collection);

		if (StringUtils.isNotBlank(filePath))
			util.close(filePath);

		writeToResponse(request, response, filePath);
	}

	public static void writeToResponse(HttpServletRequest request, HttpServletResponse response, String filePath) {
		FileOperationUtil.mkdirByFilePath(filePath);
		File file = new File(filePath);

		if (!file.exists()) {
			return;
		}

		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			int fileLength = (int) file.length();
			response.setContentLength(fileLength);

			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());

			byte[] buf = new byte[4096];
			int byteLength = -1;
			while (((byteLength = bis.read(buf)) != -1)) {
				bos.write(buf, 0, byteLength);
			}
			bos.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
