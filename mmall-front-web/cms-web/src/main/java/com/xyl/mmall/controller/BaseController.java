package com.xyl.mmall.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.netease.print.common.util.CurrTimeUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.util.JsonUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

@SuppressWarnings("rawtypes")
public class BaseController {
	private static Set<Class> classSet;
	static {
		// 添加的静态方法Java的Class
		classSet = new HashSet<Class>();
		classSet.add(StringUtils.class);
		classSet.add(JsonUtils.class);
	}

	/**
	 * @param filePath
	 * @return
	 */
	public ModelAndView genMV(String filePath) {
		ModelAndView mv = new ModelAndView(filePath);
		appendStaticMethod(mv);
		return mv;
	}

	public ModelAndView genJsonMV(boolean result, Object value) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("result", result);
		mv.addObject("value", value);
		return mv;
	}

	/**
	 * 设置页面不缓存
	 * 
	 * @param response
	 */
	public void setNoCache(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
	}

	/**
	 * 添加指定的Java对象的Static方法(附带返回当前时间)
	 * 
	 * @param dataMap
	 * @throws TemplateModelException
	 */
	private static void appendStaticMethod(ModelAndView mv) {
		// 设置给FTL使用的对象
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		try {
			for (Class classItem : classSet)
				mv.addObject(classItem.getSimpleName(), (TemplateHashModel) staticModels.get(classItem.getName()));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加指定的Java对象的Static方法(附带返回当前时间)
	 * 
	 * @param model
	 */
	public static void appendStaticMethod(Model model) {
		// 设置给FTL使用的对象
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		try {
			for (Class classItem : classSet)
				model.addAttribute(classItem.getSimpleName(), (TemplateHashModel) staticModels.get(classItem.getName()));
			model.addAttribute("currTime", CurrTimeUtil.currentTimeMillis());
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}

	public void prepareModel(Model model) {
		appendStaticMethod(model);
	}

	public long[] getTimeRange(String startTimeStr, String endTimeStr) {
		long[] range = new long[2];
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			range[0] = fmt.parse(startTimeStr).getTime();
			range[1] = fmt.parse(endTimeStr).getTime() + 86400L * 1000L - 1L;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return range;
	}

	public DDBParam genDDBParam(int pageSize, int offset) {
		DDBParam ddbParam = DDBParam.genParam1();
		ddbParam.setLimit(pageSize);
		ddbParam.setOffset(offset);
		return ddbParam;
	}
}
