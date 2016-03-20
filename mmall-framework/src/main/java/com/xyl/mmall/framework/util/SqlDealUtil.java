package com.xyl.mmall.framework.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author lhp
 * 2015年11月12日 下午4:34:57
 */
public class SqlDealUtil {
	
	/**
	 * 转义sql 特殊字符  %,',_
	 * @param content
	 * @return
	 */
	public static String escapeSpecialChars(String content){
		if(StringUtils.isBlank(content)){
			return null;
		}
		String afterDecodeContent =  content.replaceAll( "'", "\\\\'");
		afterDecodeContent = afterDecodeContent.replaceAll("%", "\\\\%");
		afterDecodeContent = afterDecodeContent.replaceAll("_", "\\\\_");
		return afterDecodeContent;
	}
	
	/**
	 * escapse sql拼接字符串中单引号，防止sql注入
	 * @param content
	 * @return 非null字符串
	 */
	public static String escapeSingleQuoteChars(String content){
		if(StringUtils.isBlank(content)){
			return "";
		}
		String afterDecodeContent =  content.replaceAll( "'", "\\\\'");
		return afterDecodeContent;
	}
	
	/**
	 * escapse like 条件输入部分
	 * @param content
	 * @return 非null字符串
	 */
	public static String escapeLikeClauseChars(String content){
		if(StringUtils.isBlank(content)){
			return "";
		}
		String afterDecodeContent =  content.replaceAll( "'", "\\\\'");
		afterDecodeContent = afterDecodeContent.replaceAll("%", "\\\\%");
		afterDecodeContent = afterDecodeContent.replaceAll("_", "\\\\_");
		return afterDecodeContent;
	}

}
