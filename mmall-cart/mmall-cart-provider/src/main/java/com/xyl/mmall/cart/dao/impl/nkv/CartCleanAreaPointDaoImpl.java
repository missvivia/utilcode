package com.xyl.mmall.cart.dao.impl.nkv;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.cart.clean.CartCleanCodeInfo;
import com.xyl.mmall.cart.clean.CartRDBOperUtil;
import com.xyl.mmall.cart.clean.RDBResult;
import com.xyl.mmall.cart.clean.meta.CartCleanAreaPoint;
import com.xyl.mmall.cart.dao.CartCleanAreaPointDao;

@Repository
public class CartCleanAreaPointDaoImpl implements CartCleanAreaPointDao {

	private static final Logger logger = LoggerFactory.getLogger(CartCleanAreaPointDaoImpl.class);

	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;

	@Override
	public CartCleanAreaPoint queryPointByDistributeKey(String distributeKeyForPoint) {
		RDBResult result=cartRDBOperUtil.getFromRDBOfMapByKeyAndField(distributeKeyForPoint, CartCleanCodeInfo.NKV_CART_CLEAN_POINT_COUNT);
		if(result==null || result.getByteRes()==null){
			logger.error("no default init point found in cache");
			this.initDefaultPointRecord();
		}
		CartCleanAreaPoint pointObj=new CartCleanAreaPoint();
		int curPoint=Integer.valueOf(new String(result.getByteRes()));
		pointObj.setPoint(curPoint);
		pointObj.setDistributeKey(distributeKeyForPoint);
		return pointObj;
	}

	@Override
	public boolean updatePointAndStatus(String distributeKeyForPoint, int pointToUpdate, int processStatusToUpdate, int oldPoint) {
		return this.setUpdatePoint(distributeKeyForPoint, pointToUpdate, processStatusToUpdate);
	}
	
	private boolean initDefaultPointRecord() {
		return this.setUpdatePoint(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_COUNT, CartCleanCodeInfo.INIT_DEFAULT_POINT, CartCleanCodeInfo.AREA_CLEAN_NO_PROCESSING);
	}
	
	/**
	 * 设置指针位置及处理状态
	 * @param distributeKeyForPoint 指针存储最外层key
	 * @param pointToUpdate 指针位置
	 * @param processStatusToUpdate 当前区域处理状态
	 * @return
	 */
	private boolean setUpdatePoint(String distributeKeyForPoint,int pointToUpdate, int processStatusToUpdate){
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		// 指针位置
		map.put(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_COUNT.getBytes(), String.valueOf(pointToUpdate).getBytes());

		// 处理状态
		map.put(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_STATUS.getBytes(),
				String.valueOf(processStatusToUpdate).getBytes());

		return cartRDBOperUtil.putPairFieldsValuesToRDBOfMap(CartCleanCodeInfo.NKV_CART_CLEAN_POINT_OUT, map);
	}

}
