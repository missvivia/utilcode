package com.xyl.mmall.itemcenter.dao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.xyl.mmall.itemcenter.dao.product.ProductParamOptDaoImpl;
import com.xyl.mmall.itemcenter.intf.DaoInitialInterface;

@Component
public class OnloadProcesser implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object obj, String arg1) throws BeansException {
		if (obj instanceof DaoInitialInterface) {
			((DaoInitialInterface) obj).loadData(); // 调用方法加载数据
		}
		return obj;
	}

	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
		// TODO Auto-generated method stub
		return arg0;
	}

}
