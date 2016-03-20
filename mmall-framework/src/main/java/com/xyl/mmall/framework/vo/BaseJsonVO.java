package com.xyl.mmall.framework.vo;

import java.io.Serializable;

import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.util.JsonUtils;

/**
 * 基础的json返回对象最外层
 * 
 * @author jiangww
 *
 */
public class BaseJsonVO implements Serializable {

	private static final long serialVersionUID = -6907060097996722962L;

	/**
	 * code 返回状态码
	 */
	private int code;

	/**
	 * 异常消息
	 */
	private String message;

	/**
	 * 封装的对象
	 */
	private Object result;

	/**
	 * 基础的构造对象
	 */
	public BaseJsonVO() {
		this.code = ErrorCode.NULL.getIntValue();
	}

	public void setCodeAndMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 异常的构造对象
	 * 
	 * @param e
	 */
	public BaseJsonVO(ErrorCode e) {
		this.code = e.getIntValue();
		this.message = e.getDesc();
	}
	public BaseJsonVO(MobileErrorCode e) {
		this.code = e.getIntValue();
		this.message = e.getDesc();
	}

	/**
	 * 直接构造包含值的对象
	 * 
	 * @param r
	 */
	public BaseJsonVO(Object r) {
		this();
		this.result = r;
	}

	/**
	 * 直接构造包含值的错误对象
	 * 
	 * @param r
	 */
	public BaseJsonVO(ErrorCode e, Object r) {
		this(e);
		this.result = r;
	}

	/**
	 * json 转换接口
	 * 
	 * @return
	 */
	public String toJson() {
		return JsonUtils.toJson(this);
	}

	/**
	 * 
	 * 以下get,set 方法
	 */

	public int getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code.getIntValue();
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
}
