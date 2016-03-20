package com.xyl.mmall.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.netease.backend.nkv.client.impl.DefaultNkvClient;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;

/**
 * nkv 相关配置，以及初始化公用nkv client实例
 * @author Yang,Nan
 *
 */
@Configuration
@PropertySource({ "classpath:config/${spring.profiles.active}/nkv.properties" })
public class NkvConfiguration {
	@Value("${nkv.rdb_master_config_server_url}")
	private String rdb_master_config_server_url;

	@Value("${nkv.rdb_slave_config_server_url}")
	private String rdb_slave_config_server_url;

	@Value("${nkv.rdb_group_name}")
	private String rdb_group_name;
	
	public static short NKV_RDB_NAMESPACE;
	
	@Value("${nkv.rdb_cart_namespace}")
	public short rdb_cart_namespace;
	
	public static short NKV_RDB_COMMON_NAMESPACE;
	
	@Value("${nkv.rdb_common_namespace}")
	public short rdb_common_namespace;
	
	@Value("${nkv.mdb_master_config_server_url}")
	private String mdb_master_config_server_url;

	@Value("${nkv.mdb_slave_config_server_url}")
	private String mdb_slave_config_server_url;

	@Value("${nkv.mdb_group_name}")
	private String mdb_group_name;
	
	public static short NKV_MDB_NAMESPACE;
	
	@Value("${nkv.mdb_inventory_namespace}")
	private short mdb_inventory_namespace;
	
	@Value("${nkv.mdb_session_sc_master_config_server_url}")
	private String mdb_session_sc_master_config_server_url;

	@Value("${nkv.mdb_session_sc_slave_config_server_url}")
	private String mdb_session_sc_slave_config_server_url;

	@Value("${nkv.mdb_session_sc_group_name}")
	private String mdb_session_sc_group_name;
	
	public static short NKV_MDB_SESSION_NAMESPACE;
	
	@Value("${nkv.mdb_session_sc_namespace}")
	private short mdb_session_sc_namespace;

	@Bean(initMethod = "init",destroyMethod="close")
	public DefaultExtendNkvClient defaultExtendNkvClient() {
		DefaultExtendNkvClient client = new DefaultExtendNkvClient();
		client.setMaster(rdb_master_config_server_url);
		client.setSlave(rdb_slave_config_server_url);
		client.setGroup(rdb_group_name);
		client.setCompressEnabled(true);
		client.setUseFastCompressed(true);
		client.setCompressThreshold(10240);
		NKV_RDB_NAMESPACE = rdb_cart_namespace;
		NKV_RDB_COMMON_NAMESPACE = rdb_common_namespace;
		return client;
	}

	@Bean(initMethod = "init",destroyMethod="close")
	public DefaultNkvClient mdbNkvClient() {
		DefaultNkvClient client = new DefaultNkvClient();
		client.setMaster(mdb_master_config_server_url);
		client.setSlave(mdb_slave_config_server_url);
		client.setGroup(mdb_group_name);
		client.setCompressEnabled(true);
		client.setUseFastCompressed(true);
		client.setCompressThreshold(10240);
		NKV_MDB_NAMESPACE = mdb_inventory_namespace;
		return client;
	}
	
	@Bean(initMethod = "init",destroyMethod="close")
	public DefaultNkvClient mdbSessionNkvClient() {
		DefaultNkvClient client = new DefaultNkvClient();
		client.setMaster(mdb_session_sc_master_config_server_url);
		client.setSlave(mdb_session_sc_slave_config_server_url);
		client.setGroup(mdb_session_sc_group_name);
		client.setCompressEnabled(true);
		client.setUseFastCompressed(true);
		client.setCompressThreshold(10240);
		NKV_MDB_SESSION_NAMESPACE = mdb_session_sc_namespace;
		return client;
	}
	
}
