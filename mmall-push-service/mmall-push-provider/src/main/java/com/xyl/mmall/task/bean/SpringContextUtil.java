package com.xyl.mmall.task.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
 
import java.util.Locale;
 
/**
 * 
 * @author jiangww
 *
 */
public class SpringContextUtil implements ApplicationContextAware {
 
    private static ApplicationContext context = null;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
 
    public static <T> T getBean(String beanName){
        return (T) context.getBean(beanName);
    }
 
    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }
 
}