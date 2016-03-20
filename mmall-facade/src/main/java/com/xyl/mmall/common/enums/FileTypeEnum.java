package com.xyl.mmall.common.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lhp
 * @version 2015年12月15日 下午2:07:50
 * 
 */
public enum FileTypeEnum implements AbstractEnumInterface<FileTypeEnum> {

	WEB_INDEX_FILE(1, "web-index-template.xlsx","web-back-index.ftl", "web首页数据文件"),

	WAP_INDEX_FILE(2, "wap-index-template.xlsx","wap-back-index.html", "wap首页数据文件"),

	APP_INDEX_FILE(3, "app-index-template.xlsx","app-back-index.html", "app首页数据文件"),

	WEB_CATEGORY_FILE(4, "web-category-template.xlsx","web-back-category.html", "web类目数据文件"),

	WAP_CATEGORY_FILE(5, "wap-category-template.xlsx","wap-back-category.html", "wap类目数据文件"),

	APP_CATEGORY_FILE(6, "app-category-template.xlsx","app-back-category.html", "app类目数据文件");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 文件名称
	 */
	private final String fileName;
	
	/**
	 * 备份文件名称
	 */
	private final String backUpFileName;

	/**
	 * 描述
	 */
	private final String desc;

	private FileTypeEnum(int value, String fileName,String backUpFileName, String desc) {
		this.value = value;
		this.fileName = fileName;
		this.backUpFileName = backUpFileName;
		this.desc = desc;
	}

	@Override
	public FileTypeEnum genEnumByIntValue(int intValue) {
		for (FileTypeEnum type : values()) {
			if (type.getValue() == intValue) {
				return type;
			}
		}
		return null;
	}

	public static FileTypeEnum genFileTypeEnumByValue(int Value) {
		for (FileTypeEnum type : values()) {
			if (type.getValue() == Value) {
				return type;
			}
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return value;
	}

	public int getValue() {
		return value;
	}

	public String getFileName() {
		return fileName;
	}

	public String getDesc() {
		return desc;
	}

	public String getBackUpFileName() {
		return backUpFileName;
	}

	
}
