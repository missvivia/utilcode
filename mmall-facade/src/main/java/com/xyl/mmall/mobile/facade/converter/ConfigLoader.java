package com.xyl.mmall.mobile.facade.converter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.mockito.internal.util.reflection.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.backend.db.common.schema.Hash;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.SpringContextUtils;
import com.xyl.mmall.task.service.MobileConfigService;

public class ConfigLoader extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private boolean inited = false;

	private MobileConfigService mobileConfigService = null;

	private String lastHash = "";
	private HashMap<String,Field> configW = new HashMap<String,Field>();
	public ConfigLoader() {
		initConfig();
	}

	private void initConfig() {
		try {
			Class c = MobileConfig.class;
			for(Field f :c.getFields()){
				configW.put(f.getName(),f);
			}	
			mobileConfigService = (MobileConfigService) SpringContextUtils.getBean("mobileConfigService");
			if (mobileConfigService != null)
				inited = true;	
		} catch (Exception e) {
			logger.info("may be not mobile web");
		}
	}

	@Override
	public void run() {
		if (!inited)
			return;

		while (true) {
			try {
				
				if(mobileConfigService == null)
					initConfig();
				Map<String,String> map = mobileConfigService.getAllConfig();
				String hash = Converter.getMD5(JsonUtils.toJson(map));
				if(!lastHash.equals(hash)){
					logger.info("update config:" + hash);
					reloadConfig(map);
					lastHash = hash;
				}
				Thread.sleep(5 * 60 * 1000);
			} catch (Exception e) {
				logger.error(e.toString());
				try {
					Thread.sleep(5 * 60 * 1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	
	
	private void reloadConfig(Map<String,String> map) throws IllegalArgumentException, IllegalAccessException{
		for(String name : map.keySet()){
			Field field = configW.get(name);
			if(field == null)
				continue;
			String type = field.getType().getName();
			String value = map.get(name);
			if(type.contains("String")){
				field.set(null, value);
			}
			
			if(type.contains("int")){
				field.set(null, Integer.parseInt(value));
			}
			
			if(type.contains("HashMap")) {
				field.set(null, (Map<String,String>)JsonUtils.fromJson(value, HashMap.class));
			}
		}
		
	}

}
