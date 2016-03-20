package com.xyl.mmall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(value="mobileFileDirConfiguration")
@PropertySource({ "classpath:config/${spring.profiles.active}/mobileIndex.properties" })
public class MobileFileDirConfiguration {
	
	/**
	 *app 数据excel路径
	 */
	@Value("${mobile.file.indexData.path}")
	private String indexDataFilePath;
	/**
	 *app 数据excel名称
	 */
	@Value("${mobile.file.indexData.name}")
	private String indexDataFileName;
	
	
	

	public String getIndexDataFileName() {
		return indexDataFileName;
	}

	public void setIndexDataFileName(String indexDataFileName) {
		this.indexDataFileName = indexDataFileName;
	}

	public String getIndexDataFilePath() {
		return indexDataFilePath;
	}

	public void setIndexDataFilePath(String indexDataFilePath) {
		this.indexDataFilePath = indexDataFilePath;
	}


}
