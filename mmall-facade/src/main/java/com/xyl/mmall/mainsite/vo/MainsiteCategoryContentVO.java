package com.xyl.mmall.mainsite.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 内容分类VO
 * 
 * @author lhp
 * @version 2015年12月16日 上午10:30:41
 * 
 */
public class MainsiteCategoryContentVO {

	/**
	 * 类目名称
	 */
	private String name;

	/**
	 * 类目链接
	 */
	private String url;
	
	/**
	 * 类目图标链接
	 */
	private String iconUrl;

	/**
	 * 子类目
	 */
	private List<MainsiteCategoryContentVO> subCategoryContentVOs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			this.name = "";
		} else {
			this.name = StringUtils.trim(name);
			;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			if (StringUtils.isNotEmpty(name)) {
				this.url = "####";
			} else {
				this.url = "";
			}
		} else {
			this.url = StringUtils.trim(url);
		}
	}

	public List<MainsiteCategoryContentVO> getSubCategoryContentVOs() {
		return subCategoryContentVOs;
	}

	public void setSubCategoryContentVOs(List<MainsiteCategoryContentVO> subCategoryContentVOs) {
		this.subCategoryContentVOs = subCategoryContentVOs;
	}

	public void addCategoryContentVOs(MainsiteCategoryContentVO categoryContentVO) {
		if (subCategoryContentVOs == null) {
			subCategoryContentVOs = new ArrayList<MainsiteCategoryContentVO>();
		}
		subCategoryContentVOs.add(categoryContentVO);
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		if (StringUtils.isEmpty(iconUrl)) {
				this.iconUrl = "";
		} else {
			this.iconUrl = StringUtils.trim(iconUrl);
		}
	}

	
}
