package com.xyl.mmall.base;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 加载继承了BaseJob的job类到关系map中
 * @author hzzhaozhenzuo
 *
 */
@Component
public class JobPathMapingLoadService implements InitializingBean,ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	@SuppressWarnings("rawtypes")
	private static final Class BASE_JOB_CLASS=BaseJob.class;
	
	private static final Logger logger=LoggerFactory.getLogger(JobPathMapingLoadService.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,Object> jobMap=applicationContext.getBeansOfType(BASE_JOB_CLASS);
		if(jobMap==null || jobMap.isEmpty()){
			logger.error("no job found");
			return;
		}
		
		for(Entry<String, Object> entry:jobMap.entrySet()){
			logger.info("try to load job:"+entry.getKey());
			Class jobClass=entry.getValue().getClass();
			
			//get the jobPath
			Annotation annotation=jobClass.getAnnotation(JobPath.class);
			if(annotation==null){
				logger.error("load job error,no jobPath annotation in job:"+entry.getKey());
				throw new RuntimeException("load job error,no jobPath annotation in job:"+entry.getKey());
			}
			
			JobPath jobPathAnno=(JobPath) annotation;
			
			//add to mapping
			if(JobMaapingStore.getMappingClassByKey(jobPathAnno.value())!=null){
				throw new RuntimeException("duplicate job path,jobPath:"+jobPathAnno.toString());
			}
			JobMaapingStore.putMapping(jobPathAnno.value(), jobClass);
			logger.info("suc load job:"+entry.getKey()+",path:"+jobPathAnno.value());
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

}
