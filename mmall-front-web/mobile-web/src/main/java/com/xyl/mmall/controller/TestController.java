/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.service.IPService;
import com.xyl.mmall.mobile.facade.TestFacade;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author lihui
 *
 */
@Controller
public class TestController {

	@Autowired
	private IPService ipService;
	@Autowired
	private TestFacade testFacade;
	@Autowired
	private Configuration configuration;
	
	@RequestMapping(value="/test",method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO genKey() {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		baseJsonVO.setMessage("这个是首页");
		return baseJsonVO;
	}
	
	@RequestMapping(value="/test4j",method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO test() {
		//
		return new BaseJsonVO();
	}
	
	@RequestMapping(value="/createHtml",method = RequestMethod.GET)
	@ResponseBody
	public void createHtml(HttpServletRequest request) throws Exception {
		List<ShowCourseView> list = new ArrayList<>();
		ShowCourseView showCourseView = new ShowCourseView();
		showCourseView.setName("中文可以吗？");
		showCourseView.setCode("A");
		ShowCourseView showCourseView2 = new ShowCourseView();
		showCourseView2.setName("虾米");
		showCourseView2.setCode("B");
		list.add(showCourseView);
		list.add(showCourseView2);
		File saveDir = new File(request.getSession().getServletContext().getRealPath("/html/product"));
		initConfig(request.getServletContext().getRealPath("/WEB-INF"));
		createHtml(list, saveDir);
	}

	private void initConfig(String templatePath) throws IOException {
		// TODO Auto-generated method stub
		configuration.setDirectoryForTemplateLoading(new File(templatePath));
//		configuration.setEncoding(Locale.CHINA, "utf-8");
	}
	
    
    public <T>  void createHtml(List<T> list, File saveDir) throws Exception{
    	if(!saveDir.exists()) saveDir.mkdirs();
    	 Map<String, Object> root = new HashMap<String, Object>(); 
    	 root.put("courseList",list);
    	 Template temp = configuration.getTemplate("test/test.ftl"); 
    	 FileOutputStream outStream = new FileOutputStream(new File(saveDir, "create.shtml"));
    	 OutputStreamWriter writer =  new OutputStreamWriter(outStream,"UTF-8");
    	 BufferedWriter sw = new BufferedWriter(writer);
    	 temp.process(root, sw);
    	 sw.flush();
		 sw.close();
		 outStream.close();
    }
	
    public class ShowCourseView {
    	private String name;
    	private String code;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
    	
	}
}

