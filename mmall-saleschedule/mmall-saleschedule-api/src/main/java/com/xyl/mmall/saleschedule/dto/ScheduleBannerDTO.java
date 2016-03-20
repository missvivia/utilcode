package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.meta.ScheduleBanner;

/**
 * Banner DTO
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleBannerDTO implements Serializable {

	private static final long serialVersionUID = 942825602676323470L;

	private ScheduleBanner banner;

	public ScheduleBanner getBanner() {
		return banner;
	}

	public void setBanner(ScheduleBanner banner) {
		this.banner = banner;
	}

	@Override
	public String toString() {
		return "ScheduleBannerDTO [banner=" + banner + "]";
	}
}
