package com.xyl.mmall.backend.vo;

public class SizeTemplateSaveVO {
	/** 尺码模板id */
	private long id;

	/** 所属的商家id */
	private long supplierId;

	/** 最低的类目id */
	private long lowCategoryId;

	/** 尺寸模板名 */
	private String templateName;

	/** 尺码提示 */
	private String remindText;

	/** 是否使用标准尺寸图 */
	private boolean isStandardPic;

	/** 尺寸图路径 */
	private String picPath;

	
}
