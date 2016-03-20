package com.xyl.mmall.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.framework.config.NkvConfiguration;

/**
 * backend全局变量help类
 * 
 * @author hzhuangluqian
 *
 */
@Component
public class NkvStaticDataHelpler {
	private static final Logger LOGGER = LoggerFactory.getLogger(NkvStaticDataHelpler.class);

	private static final String NKV_PIS_STATIC_KEY = "NKV_PIS_STATIC_KEY";

	private static final long DEFAULT_NKV_SESSION_OPTION_TIMEOUT = 5000L;

	@Autowired
	private DefaultExtendNkvClient defaultExtendNkvClient;

	public boolean putToNkv(String key, Object value) {
		if (StringUtils.isBlank(key) || null == value) {
			return false;
		}
		try {
			Result<Void> result = defaultExtendNkvClient.put(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_PIS_STATIC_KEY + key).getBytes(), SerializationUtils.serialize(value), new NkvOption(
							DEFAULT_NKV_SESSION_OPTION_TIMEOUT));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create/Update pis static data, key = {}, and result code = {} in {}", key,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			return null != result && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to put key {} and value {} due to {}", key, value.toString(), e.getMessage());
		}
		return false;
	}

	public Object getFromNkv(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_PIS_STATIC_KEY + key).getBytes(), new NkvOption(DEFAULT_NKV_SESSION_OPTION_TIMEOUT));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read pis static data, key = {}, and result code = {} in {}", key, null != result ? result
						.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {

				return result.getResult() != null ? SerializationUtils.deserialize(result.getResult()) : null;
			}
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to get value by key {} exist due to {}", key, e.getMessage());
		}
		return null;
	}
}
