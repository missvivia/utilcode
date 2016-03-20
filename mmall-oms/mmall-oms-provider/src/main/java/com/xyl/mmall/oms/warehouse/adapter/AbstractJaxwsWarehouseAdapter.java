/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;

import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter;

/**
 * @author hzzengchengyuan
 * 
 */
public abstract class AbstractJaxwsWarehouseAdapter<T> extends AbstractWarehouseAdapter {
	private JaxWsPortProxyFactoryBean proxy;

	@SuppressWarnings("unchecked")
	@Override
	public void childInit() {
		super.childInit();
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Class<T> entityClass = (Class<T>) params[0];
		if (proxy == null) {
			proxy = new JaxWsPortProxyFactoryBean();
			proxy.setWsdlDocumentUrl(getWsdlDocumentUrl());
			proxy.setEndpointAddress(getUrl());
			proxy.setUsername(getUsername());
			proxy.setPassword(getPassword());
			proxy.setNamespaceUri(getNameSpaceUri());
			proxy.setServiceName(getServiceName());
			proxy.setPortName(getPortName());
			proxy.setServiceInterface(entityClass);
			proxy.afterPropertiesSet();
		}
	}

	@SuppressWarnings("unchecked")
	public T getService() {
		return (T) proxy.getObject();
	}

	protected String getNameSpaceUri() {
		return null;
	}
	
	protected abstract URL getWsdlDocumentUrl();

	/**
	 * 获取端口名称
	 * 
	 * @return
	 */
	protected abstract String getPortName();

	/**
	 * 获取服务名称
	 * 
	 * @return
	 */
	protected abstract String getServiceName();

	/**
	 * 如需要密码，请重写
	 * 
	 * @return
	 */
	protected String getPassword() {
		return null;
	}

	/**
	 * 如接口访问需要用户名，请重写
	 * 
	 * @return
	 */
	protected String getUsername() {
		return null;
	}
}
