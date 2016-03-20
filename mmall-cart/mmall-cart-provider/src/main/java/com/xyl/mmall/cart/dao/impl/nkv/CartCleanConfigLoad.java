package com.xyl.mmall.cart.dao.impl.nkv;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.cart.clean.CartCleanCodeInfo;
import com.xyl.mmall.cart.clean.CartRDBOperUtil;
import com.xyl.mmall.cart.clean.RDBResult;

/**
 * 负责加载并初始化购物车超时清除程序中用到的参数配置
 * @author hzzhaozhenzuo
 *
 */
@Component
public class CartCleanConfigLoad implements InitializingBean{
	
	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;
	
	/**
	 * job任务每几分钟开始运行清除某一块区域
	 */
	private int jobRunPerMinute;
	
	/**
	 * 多少分钟后，清除有效购物车中的sku
	 */
	private int minuteOfCleanCart;
	
	/**
	 * job任务清除完全部【超时sku清除区域】所需要的时间
	 * 由参数：【job运行间隔时间】及【多少分钟后清除购物车时间】决定
	 */
	private int totalLength;
	
	/**
	 * 总共分成多少块cache区域
	 */
	private int areaNumberOfCache;
	
	public int getJobRunPerMinute() {
		return jobRunPerMinute;
	}

	public void setJobRunPerMinute(int jobRunPerMinute) {
		this.jobRunPerMinute = jobRunPerMinute;
	}

	public int getMinuteOfCleanCart() {
		return minuteOfCleanCart;
	}

	public void setMinuteOfCleanCart(int minuteOfCleanCart) {
		this.minuteOfCleanCart = minuteOfCleanCart;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public int getAreaNumberOfCache() {
		return areaNumberOfCache;
	}

	public void afterPropertiesSet() throws Exception {
		jobRunPerMinute=5;
		minuteOfCleanCart=20;
		totalLength=2*jobRunPerMinute+minuteOfCleanCart;
		areaNumberOfCache=totalLength/jobRunPerMinute;
		this.checkParam();
		this.initDefaultPointRecord();
	}
	
	private void checkParam(){
		if(jobRunPerMinute<=0 || minuteOfCleanCart<=0 || totalLength<=0 || areaNumberOfCache<=0){
			throw new RuntimeException("errro,购物车超时清除程序中用到的参数配置，参数不能小于0,jobRunPerMinute:"+jobRunPerMinute
					+ ",minuteOfCleanCart:"+minuteOfCleanCart+",totalLength:"+totalLength+",areaNumberOfCache:"+areaNumberOfCache);
		}
		
		/**
		 * 区域块数不为整数
		 */
		if(totalLength%jobRunPerMinute!=0){
			throw new RuntimeException("errro,购物车超时清除程序中用到的参数配置，算出来的区域总数不为整数,jobRunPerMinute:"+jobRunPerMinute
					+ ",minuteOfCleanCart:"+minuteOfCleanCart+",totalLength:"+totalLength+",areaNumberOfCache:"+areaNumberOfCache);
		}
		
	}
	
	private boolean initDefaultPointRecord(){
		RDBResult oldResult=cartRDBOperUtil.getFromRDBOfMapByKeyAndField(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_OUT, CartCleanCodeInfo.NKV_CART_CLEAN_POINT_COUNT);
		if(oldResult!=null && oldResult.getByteRes()!=null){
			return true;
		}
		
		Map<byte[],byte[]> map=new HashMap<byte[], byte[]>();
		//初始化指针位置
		map.put(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_COUNT.getBytes(), String.valueOf(CartCleanCodeInfo.INIT_DEFAULT_POINT).getBytes());
		
		//初始化处理状态
		map.put(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_STATUS.getBytes(), String.valueOf(CartCleanCodeInfo.AREA_CLEAN_NO_PROCESSING).getBytes());
		
		return cartRDBOperUtil.putPairFieldsValuesToRDBOfMap(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_OUT, map);
	}

}
