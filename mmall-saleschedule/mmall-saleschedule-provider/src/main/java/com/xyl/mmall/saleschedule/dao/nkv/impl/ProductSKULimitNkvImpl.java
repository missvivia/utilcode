/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.dao.nkv.impl;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.saleschedule.dao.nkv.ProductSKULimitNkv;

/**
 * ProductSKULimitNkvImpl.java created by yydx811 at 2015年11月16日 下午11:56:39
 * 商品限购nkv接口实现
 *
 * @author yydx811
 */
@Repository
public class ProductSKULimitNkvImpl implements ProductSKULimitNkv {
	
	private Logger logger = Logger.getLogger(ProductSKULimitNkvImpl.class);
	
	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;

	@Override
	public int changeProductSKULimit(long skuId, long userId, int deltaCount, int expire) {
		Result<Long> hincrby;
		try {
			hincrby = defaultExtendNkvClient.hincrby(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
					NkvConstant.genProductLimitNkvKey(skuId).getBytes(), Long.toString(userId).getBytes("UTF-8"), 
					deltaCount, new NkvOption(3000l, (short) 0, expire));
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			logger.error("Change productSKU limit error! SkuId : " + skuId + ", UserId : " + userId 
					+ ", DeltaCount : " + deltaCount, e);
			return Integer.MIN_VALUE;
		}
		return hincrby.getResult().intValue();
	}

	@Override
	public boolean clearProductSKULimit(long skuId) {
		Result<Void> invalidByProxy = null;
		try {
			invalidByProxy = defaultExtendNkvClient.invalidByProxy(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
					NkvConstant.genProductLimitNkvKey(skuId).getBytes(), NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | NumberFormatException e) {
			logger.error("Clear productSKU limit error! SkuId : " + skuId, e);
			return false;
		}
		if (ResultCode.OK.equals(invalidByProxy.getCode())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getProductSKULimit(long skuId, long userId) {
		Result<byte[]> hget;
		try {
			hget = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
					NkvConstant.genProductLimitNkvKey(skuId).getBytes(), 
					Long.toString(userId).getBytes("UTF-8"), NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			logger.error(e);
			return -1;
		}
		return ResultCode.OK.equals(hget.getCode()) ? Integer.valueOf(new String(hget.getResult())) : -1;
	}

	@Override
	public boolean setProductSKULimit(long skuId, long userId, int total, int expire) {
		Result<Void> result = null;
		try {
			result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
					NkvConstant.genProductLimitNkvKey(skuId).getBytes(), Long.toString(userId).getBytes("UTF-8"), 
					Long.toString(total).getBytes(), new NkvOption(3000l, (short) 0, expire));
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("Set productSKU limit error! SkuId : " + skuId + ", UserId : " + userId 
					+ ", total : " + total, e);
			return false;
		}
		if (ResultCode.OK.equals(result.getCode()) || ResultCode.ALREADY_EXIST.equals(result.getCode())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean delProductSKULimit(long skuId, long userId) {
		Result<Void> result = null;
		try {
			result = defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
					NkvConstant.genProductLimitNkvKey(skuId).getBytes(), 
					Long.toString(userId).getBytes("UTF-8"), NkvConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("Delete productSKU limit error! SkuId : " + skuId + ", UserId : " + userId, e);
			return false;
		}
		if (ResultCode.OK.equals(result.getCode())) {
			return true;
		} else {
			return false;
		}
	}
}
