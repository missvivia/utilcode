package com.xyl.mmall.cart.clean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.constant.NkvConstant;

/**
 * 集中管理涉及到购物车超时清理程序中用的cache key的获取
 * @author hzzhaozhenzuo
 *
 */
public class CartCleanKeyUtil {
	
	private static Logger logger = LoggerFactory.getLogger(CartCleanKeyUtil.class);
	
	public static final int SPLITS_OF_ONE_AREA=5;
	
	/**
	 * 得到当前【清除区域处理指针】最外层key
	 * @param areaId
	 * @param distributeProcess
	 * @return
	 */
	public static  String getPointKey(int areaId,boolean distributeProcess){
		String pointKey=CartCleanCodeInfo.NKV_CART_CLEAN_POINT_OUT;
		if(distributeProcess){
			logger.info("distributeProcess!");
			pointKey+=CartCleanCodeInfo.SPLIT+areaId;
		}
		return pointKey;
	}
	
	/**
	 * 当前需要清理的某一块cache的key
	 * <p>
	 * 此处对于具体一个大区域areaId，再进行切分
	 * <p>业务上用于放入用户购物车时间数据时调用
	 * @param curPos
	 * @param areaId
	 * @param distributeProcess
	 * @return
	 */
	public static String getKeyOfCleanAreaForCar(int curPos,int areaId,boolean distributeProcess,long userId){
		String areaKey=CartCleanCodeInfo.NKV_CART_CLEAN_AREA_TIME_PREFIX;
		if(distributeProcess){
			areaKey+=CartCleanCodeInfo.SPLIT+areaId;
		}
		areaKey+=CartCleanCodeInfo.SPLIT+curPos+CartCleanCodeInfo.SPLIT+getSplitNumberOfOneArea(userId);
		return areaKey;
	}
	
	/**
	 * 获取某块定时大区域中要清理的数据
	 * <p>
	 * 业务上用于清除时调用
	 * @param curPos
	 * @param areaId
	 * @param distributeProcess
	 * @return
	 */
	public static String[] getKeyOfCleanAreasToClean(int curPos,int areaId,boolean distributeProcess){
		String areaKey=CartCleanCodeInfo.NKV_CART_CLEAN_AREA_TIME_PREFIX;
		if(distributeProcess){
			areaKey+=CartCleanCodeInfo.SPLIT+areaId;
		}
		String[] keysArr=new String[SPLITS_OF_ONE_AREA];
		for(int i=0;i<SPLITS_OF_ONE_AREA;i++){
			keysArr[i]=areaKey+CartCleanCodeInfo.SPLIT+curPos+CartCleanCodeInfo.SPLIT+i;
		}
		return keysArr;
	}
	
	/**
	 * 根据用户id取模，得到某个大区域中，分片的位置
	 * @param userId
	 * @return
	 */
	private static long getSplitNumberOfOneArea(long userId){
		return userId%SPLITS_OF_ONE_AREA;
	}
	
	/**
	 * 用于【清除区域】某个桶中，对应某个用户购物车的key值
	 * @param userId
	 * @param areaId
	 * @return
	 */
	public static String getOrginalCartUpdateTimeKey(long userId,int areaId){
		return NkvConstant.NKV_CART_UPDATETIME + CartCleanCodeInfo.SPLIT+ userId + CartCleanCodeInfo.SPLIT + areaId;
	}
	
	public static long getUserIdFromOginalCartUpdateTimeKey(String key){
		String[] arr=key.split("\\"+CartCleanCodeInfo.SPLIT);
		return Long.valueOf(arr[1]);
	}
	
	public static int getAreaIdFromOginalCartUpdateTimeKey(String key){
		String[] arr=key.split("\\"+CartCleanCodeInfo.SPLIT);
		return Integer.valueOf(arr[2]);
	}

}
