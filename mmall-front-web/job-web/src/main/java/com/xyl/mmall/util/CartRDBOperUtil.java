package com.xyl.mmall.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.base.CartConstant;
import com.xyl.mmall.base.RDBResult;
import com.xyl.mmall.framework.config.NkvConfiguration;

@Component
public class CartRDBOperUtil {
	
	@Autowired
	private DefaultExtendNkvClient defaultExtendNkvClient;

	private static final String UTF8_PREFIX = "UTF-8";

	private static Logger logger = LoggerFactory.getLogger(CartRDBOperUtil.class);

	/**
	 * 将某个key对应的field值/对放入redis
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean putToRDBOfMap(String key, long field, byte[] value) {
		boolean successFlag = false;
		try {

			Result<Void> result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), Long
					.toString(field).getBytes(UTF8_PREFIX), value, CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putToRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}

	/**
	 * 将某个key对应的field值/对放入redis
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean putToRDBOfMap(String key, String field, byte[] value) {
		boolean successFlag = false;
		try {

			Result<Void> result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), value, CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putToRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}

	/**
	 * 将某个key对应的field值/对放入redis
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean putToRDBOfMap(String key, String field, String value) {
		boolean successFlag = false;
		try {

			Result<Void> result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), value.getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putToRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}

	/**
	 * 将某个key对应的field值/对放入redis 有原值则会先进行替换
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean putOrReplaceForRDBOfMap(String key, String field, long value) {
		boolean successFlag = false;
		try {

			// 先移除
			defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), field.getBytes(UTF8_PREFIX),
					CartConstant.NKV_OPTION);

			Result<Void> result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), Long.toString(value).getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putOrReplaceForRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}
	
	/**
	 * 将某个key对应的field值/对放入redis 有原值则会先进行替换
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean putOrReplaceForRDBOfMap(String key, String field, String value) {
		boolean successFlag = false;
		try {

			// 先移除
			defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), field.getBytes(UTF8_PREFIX),
					CartConstant.NKV_OPTION);

			Result<Void> result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), value.getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putOrReplaceForRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}

	/**
	 * 将某个key对应的field值/对放入redis 有原值则会先进行替换
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean putToRDBOfMap(String key, String field, long value) {
		boolean successFlag = false;
		try {

			// 先移除
			Result<Void> delRes = defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);
			if (delRes == null || !ResultCode.OK.equals(delRes.getCode())) {
				return false;
			}

			Result<Void> result = defaultExtendNkvClient.hset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), Long.toString(value).getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putToRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}

	public boolean delFieldAndValueForRDBOfMap(String key, String field) {
		boolean successFlag = false;
		try {

			// 先移除
			Result<Void> delRes = defaultExtendNkvClient.hdel(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					field.getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);
			successFlag = delRes != null && ResultCode.OK.equals(delRes.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("delFieldAndValueForRDBOfMap error,key:" + key + ",field:" + field, e);
		}
		return successFlag;
	}

	public boolean putPairFieldsValuesToRDBOfMap(String key, Map<byte[], byte[]> field_values) {
		boolean successFlag = false;
		Result<Integer> result;
		try {
			result = defaultExtendNkvClient.hmset(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), field_values,
					CartConstant.NKV_OPTION);
			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("putToRDB error,key:" + key, e);
		}
		return successFlag;
	}

	public boolean putToRDB(String key, long value) {
		boolean successFlag = false;
		try {

			Result<Void> result = defaultExtendNkvClient.put(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), Long
					.toString(value).getBytes(UTF8_PREFIX), CartConstant.NKV_OPTION);

			successFlag = result != null && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | UnsupportedEncodingException | InterruptedException e) {
			logger.error("putToRDB error,key:" + key + ",value:" + value, e);
		}
		return successFlag;
	}

	/**
	 * 获取某个key下的全部field键/值对 无对应key的值时，返回null，外部需要对此做为无数据处理
	 * 
	 * @param key
	 * @return 返回一个map，key为field,value为key对应的值
	 */
	public RDBResult getAllFromRDBOfMap(String key) {
		RDBResult rdbResult = new RDBResult();
		Result<Map<byte[], byte[]>> result = null;
		try {
			result = defaultExtendNkvClient.hgetall(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(),
					CartConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("getAllFromRDBOfMap error,key:" + key, e);
			rdbResult.setSearchSuccess(false);
			return rdbResult;
		}
		rdbResult.setResult(result.getResult());
		rdbResult.setSearchSuccess(true);
		return rdbResult;
	}

	/**
	 * 获取某个key及field下的value
	 * 
	 * @param key
	 * @return 返回一个byte数组
	 */
	public RDBResult getFromRDBOfMapByKeyAndField(String key, String field) {
		RDBResult rdbResult = new RDBResult();
		Result<byte[]> result = null;
		try {
			result = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), field.getBytes(),
					CartConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("getFromRDBOfMapByKeyAndField error,key:" + key, e);
			rdbResult.setSearchSuccess(false);
			return rdbResult;
		}
		rdbResult.setByteRes(result.getResult());
		rdbResult.setSearchSuccess(true);
		return rdbResult;
	}

	/**
	 * 获取某个key及field下的value 适用于value是一个string
	 * 
	 * @param key
	 * @return 返回一个string结果
	 */
	public RDBResult getStringValueFromRDBOfMapByKeyAndField(String key, String field) {
		RDBResult rdbResult = new RDBResult();
		Result<byte[]> result = null;
		try {
			result = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), field.getBytes(),
					CartConstant.NKV_OPTION);
			if(result!=null && result.getResult()!=null){
				rdbResult.setResObj(new String(result.getResult(), UTF8_PREFIX));
			}
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
			logger.error("getFromRDBOfMapByKeyAndField error,key:" + key, e);
			rdbResult.setSearchSuccess(false);
			return rdbResult;
		}
		rdbResult.setSearchSuccess(true);
		return rdbResult;
	}

	/**
	 * 返回单个key对应的值 这时存储的结构仅仅是key,value
	 * 
	 * @param key
	 * @return
	 */
	public byte[] getValueFromRDB(String key) {
		Result<byte[]> result;
		try {
			result = defaultExtendNkvClient
					.get(NkvConfiguration.NKV_RDB_NAMESPACE, key.getBytes(), CartConstant.NKV_OPTION);
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("getValueFromRDB error,key:" + key, e);
			return null;
		}
		return result.getResult();
	}

}
