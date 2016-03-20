package com.xyl.mmall.cart.dao.impl.nkv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.ResultMap;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.client.impl.DefaultNkvClient;
import com.netease.backend.nkv.client.util.NkvUtil;
import com.xyl.mmall.cart.dao.CartInventoryDao;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.config.NkvConfiguration;

@Repository
public class CartInventoryDaoImpl implements CartInventoryDao {

	@Resource
	private DefaultNkvClient mdbNkvClient;

	@Override
	public int decreaseInventory(long skuId, int deltaCount) {
		Result<Integer> result;
		try {
			result = mdbNkvClient.decr(NkvConfiguration.NKV_MDB_NAMESPACE,
					(NkvConstant.NKV_CART_INVENTORY + "-" + skuId).getBytes(), deltaCount, 0, NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		if (ResultCode.OK.equals(result.getCode()))
			return result.getResult();
		else if (ResultCode.DEC_ZERO.equals(result.getCode()))
			return CartService.CART_NO_INVENTORY;
		else
			return Integer.MIN_VALUE;
	}

	@Override
	public int addInventory(long skuId, int deltaCount) {
		Result<Integer> result;
		try {
			result = mdbNkvClient.incr(NkvConfiguration.NKV_MDB_NAMESPACE,
					(NkvConstant.NKV_CART_INVENTORY + "-" + skuId).getBytes(), deltaCount, 0, NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Integer.MIN_VALUE;
		}
		if (ResultCode.OK.equals(result.getCode()))
			return result.getResult();
		else
			return Integer.MIN_VALUE;
	}

	@Override
	public boolean setInventoryCount(long skuId, int count) {
		Result<Void> result;
		try {
			result = mdbNkvClient.setCount(NkvConfiguration.NKV_MDB_NAMESPACE,
					(NkvConstant.NKV_CART_INVENTORY + "-" + skuId).getBytes(), count, NkvConstant.NKV_OPTION);

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (ResultCode.OK.equals(result.getCode()))
			return true;
		else
			return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Map<Long, Integer> getInventoryCount(List<Long> ids) {
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		List<byte[]> keys = new ArrayList<byte[]>();
		ResultMap<byte[], Result<byte[]>> batchGet;
		try {
			for (Long l : ids) {
				keys.add((NkvConstant.NKV_CART_INVENTORY + "-" + l).getBytes());
			}
			// result = mdbNkvClient.get(NkvConfiguration.NKV_MDB_NAMESPACE,
			// (NkvConstant.NKV_CART_INVENTORY + "-" + l).getBytes(),
			// NkvConstant.NKV_OPTION);

			batchGet = mdbNkvClient.batchGet(NkvConfiguration.NKV_MDB_NAMESPACE, keys, NkvConstant.NKV_OPTION);
			for (Entry<byte[], Result<byte[]>> e : batchGet.entrySet()) {
				String key = new String(e.getKey());
				Long id = Long.valueOf(key.substring((NkvConstant.NKV_CART_INVENTORY + "-").length()));
				byte[] bytes = e.getValue().getResult();

				if (bytes == null) {
					map.put(id, 0);
				} else {
					int count = NkvUtil.decodeCountValue(bytes);
					map.put(id, count);
				}
			}

		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = new HashMap<Long, Integer>();
			return map;
		}
		if (batchGet != null
				&& (ResultCode.OK.equals(batchGet.getCode()) || ResultCode.PART_OK.equals(batchGet.getCode())))
			return map;
		else {
			map = new HashMap<Long, Integer>();
			return map;
		}
	}

}
