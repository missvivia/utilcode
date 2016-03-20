package com.xyl.mmall.mobile.facade.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.mobile.facade.vo.MobileKeyPairVO;


public enum MobileOS {
	
	IOS(1, "IOS"),
	ANDROID(2, "Android"),
	OTHER(-1, "unknow");

	private final int value;

	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private MobileOS(int v, String d) {
		value = v;
		desc = d;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	public static MobileOS getOs(String os){
		if (StringUtils.isNotBlank(os)) {
			if (MobileOS.IOS.getDesc().equalsIgnoreCase(os)) {
				return MobileOS.IOS;
			} else {
				return  MobileOS.ANDROID;
			}

		}
		return  MobileOS.OTHER;
	}
	
	public static String getOsNameById(int id){
			if (MobileOS.IOS.getIntValue() == id) {
				return MobileOS.IOS.getDesc();
			} else if(MobileOS.ANDROID.getIntValue() == id){
				return MobileOS.ANDROID.getDesc();
			}else{
				return MobileOS.OTHER.getDesc();
			}

	}
	
	public static List<MobileKeyPairVO> getSupportOs(){
		List<MobileKeyPairVO> list = new ArrayList<MobileKeyPairVO>();
		MobileKeyPairVO a1 = new MobileKeyPairVO();
		a1.setId(MobileOS.IOS.getIntValue());
		a1.setName(MobileOS.IOS.getDesc());
		list.add(a1);
		MobileKeyPairVO a2 = new MobileKeyPairVO();
		a2.setId(MobileOS.ANDROID.getIntValue());
		a2.setName(MobileOS.ANDROID.getDesc());
		list.add(a2);
		return list;
	}
}
