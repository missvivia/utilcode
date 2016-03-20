/**
 * 
 */
package com.xyl.mmall.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 运维后台错误处理controller，用以处理带错误代码的重定向请求。 示例代码：
 * <code>WebUtils.issueRedirect(request, response, "error/401");</code>
 * 默认的错误路径为：/error
 * 
 * @author lihui
 *
 */
@Controller
public class CmsErrorController extends BasicErrorController {

	/**
	 * 构造函数
	 * 
	 * @param errorAttributes
	 */
	public CmsErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	/**
	 * 构造函数
	 */
	public CmsErrorController() {
		super(new DefaultErrorAttributes());
	}

	/**
	 * 处理带错误代码的页面跳转请求。
	 * 
	 * @param code
	 *            错误代码
	 * @param request
	 *            请求
	 * @return 错误视图
	 */
	@RequestMapping(value = "${error.path:/error}/{code}", produces = "text/html")
	public ModelAndView errorHtml(@PathVariable String code, HttpServletRequest request) {
		return new ModelAndView("error/" + code);
	}

	@RequestMapping(value = "${error.path:/error}", produces = {"text/html", "*/*"})
	public ModelAndView errorHtml(HttpServletRequest request) {
		Object statusCode = request.getAttribute("javax.servlet.error.status_code");
		// 当请求的错误类型非404、403、402、401时，返回500的错误页面
		if (statusCode == null
				|| (!statusCode.equals(HttpStatus.NOT_FOUND.value())
						&& !statusCode.equals(HttpStatus.UNAUTHORIZED.value())
						&& !statusCode.equals(HttpStatus.PAYMENT_REQUIRED.value()) && !statusCode
							.equals(HttpStatus.FORBIDDEN.value()))) {
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
		}
		ModelAndView modelAndView = new ModelAndView("error/" + (Integer) statusCode);
		Object username = request.getAttribute("authentication.username");
		modelAndView.addObject("username", null == username ? "" : (String) username);
		return modelAndView;
	}
}
