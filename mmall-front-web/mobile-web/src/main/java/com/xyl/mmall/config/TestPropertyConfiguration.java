package com.xyl.mmall.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.xyl.mmall.mobile.facade.converter.MobileConfig;

@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/config.properties" })
public class TestPropertyConfiguration {
	
	@Value("${lockArea}")
	private boolean lockArea;
	
	@Value("${lockVersion}")
	private String lockVersion;
	
	@Value("${areaCode}")
	private String areaCode;

	public boolean isLockArea() {
		if(MobileConfig.islock == 1)
			return true;
		if(MobileConfig.islock == 0)
			return false;
		return lockArea;
	}

	public void setLockArea(boolean lockArea) {
		this.lockArea = lockArea;
	}

	public String getLockVersion() {
		if(StringUtils.isNotBlank(MobileConfig.lock_version))
			return MobileConfig.lock_version;
		return lockVersion;
	}

	public void setLockVersion(String lockVersion) {
		this.lockVersion = lockVersion;
	}

	public String getAreaCode() {
		if(StringUtils.isNotBlank(MobileConfig.lock_area))
			return MobileConfig.lock_area;
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


}
