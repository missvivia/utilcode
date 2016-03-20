package com.xyl.mmall.cart.dao.impl.nkv;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.cart.clean.CartRDBOperUtil;
import com.xyl.mmall.cart.clean.RDBResult;
import com.xyl.mmall.cart.dao.CartDao;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.framework.enums.ErrorCode;

@Repository
public class CartDaoImpl implements CartDao {

	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;

	private static final Logger logger = LoggerFactory.getLogger(CartDaoImpl.class);

	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;

	@Override
	public int changeCartItem(long userId, int provinceId, long skuId, int deltaCount) {
		Result<Long> hincrby;
		try {
			// 增加 或者减少
			hincrby = defaultExtendNkvClient.hincrby(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID
					+ "-" + userId + "-" + provinceId).getBytes(), Long.toString(skuId).getBytes("UTF-8"), deltaCount,
					NkvConstant.NKV_OPTION);
			// 第一次增加时加入创建时间缓存
			if (hincrby.getResult().intValue() == deltaCount) {
				byte[] bDate = Long.toString(new Date().getTime()).getBytes("UTF-8");
				defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID + "-"
						+ userId + "-" + provinceId).getBytes(),
						(NkvConstant.NKV_CART_ITEM_CREATETIME + "-" + skuId).getBytes("UTF-8"), bDate,
						NkvConstant.NKV_OPTION);
			}

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		return hincrby.getResult().intValue();
	}

	@Override
	public List<CartItemDTO> getCart(String type, long userId, int provinceId) {
		try {
			Result<Map<byte[], byte[]>> cart = defaultExtendNkvClient.hgetall(NkvConfiguration.NKV_RDB_NAMESPACE, (type
					+ "-" + userId + "-" + provinceId).getBytes(), NkvConstant.NKV_OPTION);
			Map<byte[], byte[]> cartmap = cart.getResult();
			List<CartItemDTO> skuList = new ArrayList<CartItemDTO>(cartmap.size());
			String key = "";
			Map<Long, CartItemDTO> cartItemMap = new HashMap<Long, CartItemDTO>();
			Map<String, Long> cartItemTimeMap = new HashMap<String, Long>();
			for (Entry<byte[], byte[]> entry : cartmap.entrySet()) {
				key = new String(entry.getKey(), "UTF-8");
				if (StringUtils.isNumeric(key)) {
					CartItemDTO ret = new CartItemDTO();
					ret.setSkuid(Long.parseLong(key));
					ret.setCount(new Integer(new String(entry.getValue(), "UTF-8")));
					cartItemMap.put(ret.getSkuid(), ret);
				} else {
					cartItemTimeMap.put(key, Long.valueOf(new String(entry.getValue(), "UTF-8")));
				}
			}
			for (Entry<Long, CartItemDTO> entry : cartItemMap.entrySet()) {
				if (cartItemTimeMap.get(NkvConstant.NKV_CART_ITEM_CREATETIME + "-" + entry.getKey()) != null) {
					entry.getValue().setCreateTime(
							cartItemTimeMap.get(NkvConstant.NKV_CART_ITEM_CREATETIME + "-" + entry.getKey()));
				}
			}
			skuList = new ArrayList<CartItemDTO>(cartItemMap.values());
			return skuList;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | NumberFormatException
				| UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ArrayList<CartItemDTO>();
		}
	}

	@Override
	public int addItems(String type, long userId, int provinceId, Map<byte[], byte[]> map) {
		Result<Integer> hincrby;
		try {
			hincrby = defaultExtendNkvClient.hmset(NkvConfiguration.NKV_RDB_NAMESPACE,
					(type + "-" + userId + "-" + provinceId).getBytes(), map, NkvConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		return hincrby.getResult().intValue();
	}

	@Override
	public int deleteCartItem(long userId, int provinceId, long skuId) {
		Result<Void> hdel = null;
		try {
			// 删除缓存时间
			hdel = defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID + "-"
					+ userId + "-" + provinceId).getBytes(),
					(NkvConstant.NKV_CART_ITEM_CREATETIME + "-" + skuId).getBytes("UTF-8"), NkvConstant.NKV_OPTION);

			// 删除
			hdel = defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID + "-"
					+ userId + "-" + provinceId).getBytes(), Long.toString(skuId).getBytes("UTF-8"),
					NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		return ResultCode.OK.equals(hdel.getCode()) ? ErrorCode.SUCCESS.getIntValue() : Integer.MIN_VALUE;
	}

	@Override
	public boolean clearCart(String type, long userId, int provinceId) {
		Result<Void> invalidByProxy = null;
		try {
			invalidByProxy = defaultExtendNkvClient.invalidByProxy(NkvConfiguration.NKV_RDB_NAMESPACE, (type + "-"
					+ userId + "-" + provinceId).getBytes(), NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		if (ResultCode.OK.equals(invalidByProxy.getCode()))
			return true;
		else
			return false;
	}

	@Override
	public boolean deleteCart(String type, long userId, int provinceId) {
		Result<Void> invalidByProxy = null;
		try {
			invalidByProxy = defaultExtendNkvClient.invalidByProxy(NkvConfiguration.NKV_RDB_NAMESPACE, (type + "-"
					+ userId + "-" + provinceId).getBytes(), NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		if (ResultCode.OK.equals(invalidByProxy.getCode()) || ResultCode.NOTEXISTS.equals(invalidByProxy.getCode()))
			return true;
		else
			return false;
	}

	// 以下方法目前没使用到
	@Override
	public int changeCartItemOnType(String type, long userId, int provinceId, long skuId, int deltaCount) {
		Result<Long> hincrby;
		try {
			// 先 插入一对field/value 如果field存在则无效，这样以免第一次做增加，没有原始值.
			// Result<Void> hsetnx =
			// defaultExtendNkvClient.hsetnx(NkvConstant.NKV_RDB_NAMESPACE,
			// (NkvConstant.NKV_CART_VALID + "-" + userId + "-" +
			// provinceId).getBytes(), Long.toString(skuId)
			// .getBytes("UTF-8"), Integer.toString(0).getBytes("UTF-8"),
			// NkvConstant.NKV_OPTION);
			// 增加 或者减少
			hincrby = defaultExtendNkvClient.hincrby(NkvConfiguration.NKV_RDB_NAMESPACE,
					(type + "-" + userId + "-" + provinceId).getBytes(), Long.toString(skuId).getBytes("UTF-8"),
					deltaCount, NkvConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		return hincrby.getResult().intValue();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean clearDeletePool(long userId, int provinceId) {
		Result<Void> invalidByProxy;
		try {

			// 去购物车删除记录里面，清楚老的记录。再增加新的记录。
			invalidByProxy = defaultExtendNkvClient.invalidByProxy(NkvConfiguration.NKV_RDB_NAMESPACE,
					(NkvConstant.NKV_CART_DELETE + "-" + userId + "-" + provinceId).getBytes(), NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return ResultCode.OK.equals(invalidByProxy.getCode());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean clearDeleteOvertimePool(long userId, int provinceId) {
		Result<Void> invalidByProxy;
		try {

			// 去购物车删除记录里面，清楚老的记录。再增加新的记录。
			invalidByProxy = defaultExtendNkvClient.invalidByProxy(NkvConfiguration.NKV_RDB_NAMESPACE,
					(NkvConstant.NKV_CART_DELETE_OVERTIME + "-" + userId + "-" + provinceId).getBytes(),
					NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return ResultCode.OK.equals(invalidByProxy.getCode());
	}

	@Override
	public int deleteCartItem(String type, long userId, int provinceId, long skuId) {
		Result<Void> hdel;
		Result<byte[]> hget;
		try {
			// 先得到要删除item原值
			hget = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_NAMESPACE,
					(type + "-" + userId + "-" + provinceId).getBytes(), Long.toString(skuId).getBytes("UTF-8"),
					NkvConstant.NKV_OPTION);
			if (hget.getResult() == null || hget.getResult().length == 0) {
				return Integer.MIN_VALUE;
			}
			// 删除
			hdel = defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE,
					(type + "-" + userId + "-" + provinceId).getBytes(), Long.toString(skuId).getBytes("UTF-8"),
					NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		return ResultCode.OK.equals(hdel.getCode()) ? Integer.valueOf(new String(hget.getResult())) : Integer.MIN_VALUE;
	}

	@Override
	public long resetTime(long userId, int provinceId) {
		Result<Void> putval;
		long now = new Date().getTime();
		try {
			// 先 插入一对field/value 如果field存在则无效，这样以免第一次做增加，没有原始值.
			putval = defaultExtendNkvClient.put(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_UPDATETIME
					+ "-" + userId + "-" + provinceId).getBytes(), Long.toString(now).getBytes("UTF-8"),
					NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return Long.MIN_VALUE;
		}
		return ResultCode.OK.equals(putval.getCode()) ? now : Long.MIN_VALUE;
	}

	@Override
	public long getCartTime(long userId, int provinceId) {
		Result<byte[]> putval;
		try {
			// 先 插入一对field/value 如果field存在则无效，这样以免第一次做增加，没有原始值.
			putval = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_UPDATETIME
					+ "-" + userId + "-" + provinceId).getBytes(), NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			e.printStackTrace();
			return 0;
		}
		if (ResultCode.OK.equals(putval.getCode()))
			return Long.valueOf(new String(putval.getResult()));
		else
			return 0;

	}

	@Override
	public int getCartItemCount(String type, long userId, int provinceId, long skuId) {
		Result<byte[]> hget;
		try {
			// 先得到要删除item原值
			hget = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_NAMESPACE,
					(type + "-" + userId + "-" + provinceId).getBytes(), Long.toString(skuId).getBytes("UTF-8"),
					NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return 0;
		}
		return ResultCode.OK.equals(hget.getCode()) ? Integer.valueOf(new String(hget.getResult())) : 0;
	}

	/**
	 * 省作为最外层的key skuid作为field value为一个Map，Map中key为用户id，Map中value为用户的关注时间
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cart.dao.CartDao#addUserToRemindWhenStorage(long, int,
	 *      long)
	 */
	@SuppressWarnings("unchecked")
	public boolean addUserToRemindWhenStorage(long userId, int provinceId, long skuId) {
		String outKey = this.getOutKeyOfRemindWhenStorage(provinceId);
		String fieldKey = this.getFieldKeyOfRemindWhenStorage(skuId);
		RDBResult rdbResult = cartRDBOperUtil.getStringValueFromRDBOfMapByKeyAndField(outKey, fieldKey);
		if (!rdbResult.isSearchSuccess()) {
			logger.error("search rdb error at method:addUserToRemindWhenStorage");
			return false;
		}

		Object oldUsersData = rdbResult.getResObj();
		Map<String, String> usersMap;
		if (oldUsersData == null) {
			usersMap = new LinkedHashMap<String, String>();
		} else {
			usersMap = JSONObject.parseObject((String) oldUsersData, LinkedHashMap.class);
		}

		if (usersMap.get(Long.toString(userId)) != null) {
			return true;
		}
		usersMap.put(Long.toString(userId), Long.toString(System.currentTimeMillis()));
		return cartRDBOperUtil.putOrReplaceForRDBOfMap(outKey, fieldKey, JSON.toJSONString(usersMap));
	}

	private String getOutKeyOfRemindWhenStorage(int provinceId) {
		return (NkvConstant.NKV_CART_REMIND_WHEN_STORAGE + "-" + provinceId);
	}

	private String getFieldKeyOfRemindWhenStorage(long skuId) {
		return Long.toString(skuId);
	}

	@SuppressWarnings("unchecked")
	public boolean userExistInRemindStorage(long userId, int provinceId, long skuId) {
		String outKey = this.getOutKeyOfRemindWhenStorage(provinceId);
		String fieldKey = this.getFieldKeyOfRemindWhenStorage(skuId);
		RDBResult rdbResult = cartRDBOperUtil.getStringValueFromRDBOfMapByKeyAndField(outKey, fieldKey);
		if (!rdbResult.isSearchSuccess()) {
			logger.error("search rdb error at userExistInRemindStorage");
			return false;
		}
		Object usersDataObj = rdbResult.getResObj();
		if (usersDataObj == null) {
			return false;
		}
		Map<String, String> usersMap = JSONObject.parseObject((String) usersDataObj, LinkedHashMap.class);
		return (usersMap != null && usersMap.get(Long.toString(userId)) != null) ? true : false;
	}

	@Override
	public int getLastClearBucketPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remvoeUserRemind(long userId, int provinceId, long skuId) {
		String outKey = this.getOutKeyOfRemindWhenStorage(provinceId);
		String fieldKey = this.getFieldKeyOfRemindWhenStorage(skuId);
		RDBResult rdbResult = cartRDBOperUtil.getStringValueFromRDBOfMapByKeyAndField(outKey, fieldKey);
		if (!rdbResult.isSearchSuccess()) {
			logger.error("search rdb error at userExistInRemindStorage");
			return false;
		}
		Object usersDataObj = rdbResult.getResObj();
		if (usersDataObj == null) {
			return true;
		}

		Map<String, String> usersMap = JSONObject.parseObject((String) usersDataObj, LinkedHashMap.class);
		if (usersMap == null || !usersMap.containsKey(Long.toString(userId))) {
			return true;
		}
		usersMap.remove(Long.toString(userId));
		return cartRDBOperUtil.putOrReplaceForRDBOfMap(outKey, fieldKey, JSON.toJSONString(usersMap));
	}
	
	@Override
	public Long[] selectCartItems(long userId, int provinceId, Long[] selectedSkuIds) {
		if (selectedSkuIds == null) {
			try {
				defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID + "-" + userId + "-" + provinceId).getBytes(),
							(NkvConstant.NKV_CART_ITEM_SELECTIONS).getBytes("UTF-8"), 
							NkvConstant.NKV_OPTION);
			} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
				e.printStackTrace();
			}
			return new Long[0];
		}

		if (selectedSkuIds.length == 0) {
			Result<byte[]> result = null;
			try {
				result = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID + "-" + userId + "-" + provinceId).getBytes(),
							(NkvConstant.NKV_CART_ITEM_SELECTIONS).getBytes("UTF-8"), 
							NkvConstant.NKV_OPTION);
				if (result != null && result.getResult().length > 0) {
					String sResult = new String(result.getResult(), "UTF-8");
					String[] sArr = sResult.split(",");
					Long[] lResult = new Long[sArr.length];
					for (int i = 0; i < sArr.length; i ++) {
						lResult[i] = Long.valueOf(sArr[i].trim());
					}
					return lResult;
				}
			} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
				e.printStackTrace();
				return new Long[0];
			}
			return new Long[0];
		}
		
		StringBuilder sb = new StringBuilder(selectedSkuIds[0].toString());
		List<Long> lstReturn = new ArrayList<>(selectedSkuIds.length);
		for (int i = 1; i < selectedSkuIds.length; i ++) {
			if (selectedSkuIds[i] != null) {
				sb.append(",");
				sb.append(selectedSkuIds[i]);
				lstReturn.add(selectedSkuIds[i]);
			}
		}
		
		try {
			defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, (NkvConstant.NKV_CART_VALID + "-" + userId + "-" + provinceId).getBytes(),
						NkvConstant.NKV_CART_ITEM_SELECTIONS.getBytes("UTF-8"), sb.toString().getBytes("UTF-8"),
						NkvConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			e.printStackTrace();
			return new Long[0];
		}

		return lstReturn.toArray(new Long[0]);
	}

}
