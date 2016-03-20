package com.xyl.mmall.oms.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author hzzengchengyuan
 *
 */
public final class CHINA extends Region {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CHINAL_CODE = "000000000000";

	private static Region INSTANCE = new CHINA();

	private static Map<String, Region> REGION_CODE_MAP = new HashMap<String, Region>();

	/**
	 * 中国行政区
	 */
	private CHINA() {
		this(CHINAL_CODE, "中国", RegioLevel.ZERO);
	}

	private CHINA(String code, String name, RegioLevel level) {
		super(code, name, level);
	}

	/**
	 * 初始化全国行政区列表
	 * 
	 * @param regions
	 *            全国所有行政区元数据
	 */
	public synchronized static void init(Region[] regions) {
		INSTANCE = new CHINA();
		Map<RegioLevel, List<Region>> levelMap = new HashMap<RegioLevel, List<Region>>();
		for (Region region : regions) {
			List<Region> levelRegions = null;
			if (levelMap.containsKey(region.getLevel())) {
				levelRegions = levelMap.get(region.getLevel());
			} else {
				levelRegions = new ArrayList<Region>();
				levelMap.put(region.getLevel(), levelRegions);
			}
			levelRegions.add(region);
			REGION_CODE_MAP.put(region.getCode(), region);
		}

		// 构造省数据
		List<Region> list = levelMap.get(RegioLevel.ONE);
		if (list != null) {
			for (Region r : list) {
				INSTANCE.addChild(r);
			}
		}
		// 构造二级行政区数据
		list = levelMap.get(RegioLevel.TWO);
		if (list != null) {
			for (Region r : list) {
				REGION_CODE_MAP.get(r.getParentCode()).addChild(r);
			}
		}
		// 构造三级行政区数据
		list = levelMap.get(RegioLevel.THREE);
		if (list != null) {
			for (Region r : list) {
				REGION_CODE_MAP.get(r.getParentCode()).addChild(r);
			}
		}
		
		// 构造四级行政区数据
		list = levelMap.get(RegioLevel.FOUR);
		if (list != null) {
			for (Region r : list) {
				REGION_CODE_MAP.get(r.getParentCode()).addChild(r);
			}
		}
	}

	public static Region queryByCode(String code) {
		return REGION_CODE_MAP.get(code);
	}

	public static Region queryByName(String name) {
		return instance().searchByName(name);
	}

	public static Region queryByName(String name, RegioLevel level) {
		return instance().searchByName(name, level);
	}

	public static List<Region> queryByNames(String name) {
		return instance().searchByNames(name);
	}

	public static List<Region> queryByNames(String name, RegioLevel level) {
		return instance().searchByNames(name, level);
	}

	public synchronized static Region instance() {
		return INSTANCE;
	}
}
