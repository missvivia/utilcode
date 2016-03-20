/**
 * 
 */
package com.xyl.mmall.oms.warehouse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;
import com.xyl.mmall.oms.warehouse.util.AnnotationBeanMapConfiguration;
import com.xyl.mmall.oms.warehouse.util.BeanMapConfiguration;
import com.xyl.mmall.oms.warehouse.util.BeanMapConvert;

/**
 * adapter抽象基类。提供一些通用方法：base64、MD5、基于Jaxb的XML-Object互转、基于{@link MapClass}和
 * {@link MapField}注解的Object-Object互转
 * 
 * @author hzzengchengyuan
 */
public abstract class AbstractWarehouseAdapter implements WarehouseAdapter, WarehouseAdapterCallback {

	private WarehouseShipCaller[] shipCallers;

	private WarehouseShipOutCaller[] shipOutCallers;

	private WarehouseSalesOrderCaller[] salesOrderCallerers;

	private WarehouseReturnOrderCaller[] returnOrderCallers;

	private WarehouseOrderTraceCaller[] orderTraceCallers;

	private BeanMapConvert convert;

	private Jaxb2Marshaller marshaller;

	private static final String DEFAULT_CALLER_FAIL_MESSAGE = "服务处理失败.";

	@PostConstruct
	public final void init() {
		BeanMapConfiguration configuration = null;
		marshaller = new Jaxb2Marshaller();
		Map<String, Object> properties = new HashMap<String,Object>();
		properties.put(Marshaller.JAXB_FRAGMENT, true);
		marshaller.setMarshallerProperties(properties);
		Class<?>[] classes = getBoundClasses();
		if (classes != null) {
			marshaller.setClassesToBeBound(classes);
			configuration = new AnnotationBeanMapConfiguration(classes);
		} else if (getPackagesToScan() != null) {
			marshaller.setPackagesToScan(getPackagesToScan());
			configuration = new AnnotationBeanMapConfiguration(getPackagesToScan());
		}
		convert = new BeanMapConvert(configuration);
		childInit();
	}

	protected void childInit() {

	}

	/**
	 * 对传入的字符串进行MD5加密，同时转为十六进制字符串
	 * 
	 * @param plainText
	 * @param charset
	 * @return
	 */
	public String MD5(String plainText, String charset) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			md.update(plainText.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/**
	 * base64编码，编码失败返回null
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String base64(String str, String charset) {
		try {
			return (new sun.misc.BASE64Encoder()).encode(str.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Object对象根据jaxb注解转换为xml字符串
	 * 
	 * @param obj
	 * @return
	 * @throws XmlMappingException
	 * @throws UnsupportedEncodingException
	 */
	public String marshal(Object obj) throws XmlMappingException, UnsupportedEncodingException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		marshaller.marshal(obj, new StreamResult(stream));
		return stream.toString(getCharSet());
	}

	/**
	 * 将xml字符串依据jaxb注解反转为Object对象
	 * 
	 * @param xml
	 * @return
	 * @throws XmlMappingException
	 * @throws UnsupportedEncodingException
	 */
	public Object unmarshal(String xml) throws XmlMappingException, UnsupportedEncodingException {
		return marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes(getCharSet()))));
	}

	@SuppressWarnings("unchecked")
	public <T> T unmarshal(String xml, Class<T> clazz) throws XmlMappingException, UnsupportedEncodingException {
		return (T) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes(getCharSet()))));
	}

	/**
	 * 将Object转换为目标对象，如果两个类型之间未设置映射关系将返回null
	 * 
	 * @param obj
	 * @param type
	 *            需要转换的目标对象类型
	 * @return
	 */
	public <T> T map(Object obj, Class<T> type) {
		return convert.map(obj, type);
	}

	public void map(Object obj1, Object obj2) {
		convert.map(obj1, obj2);
	}

	@Autowired(required = false)
	public void setWarehouseShipCallers(WarehouseShipCaller[] p_shipCallers) {
		this.shipCallers = p_shipCallers;
	}

	@Autowired(required = false)
	public void setWarehouseShipOutCallers(WarehouseShipOutCaller[] p_shipOutCallers) {
		this.shipOutCallers = p_shipOutCallers;
	}

	@Autowired(required = false)
	public void setWarehouseSalesOrderCallers(WarehouseSalesOrderCaller[] p_salesOrderCallerers) {
		this.salesOrderCallerers = p_salesOrderCallerers;
	}

	@Autowired(required = false)
	public void setWarehouseReturnOrderCallers(WarehouseReturnOrderCaller[] p_returnOrderCallers) {
		this.returnOrderCallers = p_returnOrderCallers;
	}

	@Autowired(required = false)
	public void setWarehouseOrderTraceCallers(WarehouseOrderTraceCaller[] p_orderTraceCallers) {
		this.orderTraceCallers = p_orderTraceCallers;
	}

	@Transaction
	protected boolean onShipOrderStateChange(WMSShipOrderUpdateDTO shipOrder) throws WarehouseCallerException {
		if (this.shipCallers != null) {
			for (WarehouseShipCaller caller : this.shipCallers) {
				if (!caller.onShipOrderStateChange(shipOrder)) {
					throw new WarehouseCallerException(DEFAULT_CALLER_FAIL_MESSAGE);
				}
			}
		}
		return true;
	}

	@Transaction
	protected boolean onShipOutOrderStateChange(WMSShipOutOrderUpdateDTO shipOutOrder) throws WarehouseCallerException {
		if (this.shipOutCallers != null) {
			for (WarehouseShipOutCaller caller : this.shipOutCallers) {
				if (!caller.onShipOutOrderStateChange(shipOutOrder)) {
					throw new WarehouseCallerException(DEFAULT_CALLER_FAIL_MESSAGE);
				}
			}
		}
		return true;
	}

	@Transaction
	protected boolean onSalesOrderStateChange(WMSSalesOrderUpdateDTO salesOrder) throws WarehouseCallerException {
		if (this.salesOrderCallerers != null) {
			for (WarehouseSalesOrderCaller caller : this.salesOrderCallerers) {
				if (!caller.onSalesOrderStateChange(salesOrder)) {
					throw new WarehouseCallerException(DEFAULT_CALLER_FAIL_MESSAGE);
				}
			}
		}
		return true;
	}

	@Transaction
	protected boolean onReturnOrderStateChange(WMSReturnOrderUpdateDTO returnOrder) throws WarehouseCallerException {
		if (this.returnOrderCallers != null) {
			for (WarehouseReturnOrderCaller caller : this.returnOrderCallers) {
				if (!caller.onReturnOrderStateChange(returnOrder)) {
					throw new WarehouseCallerException(DEFAULT_CALLER_FAIL_MESSAGE);
				}
			}
		}
		return true;
	}

	@Transaction
	protected boolean onOrderTraceChange(List<WMSOrderTrace> orderTraces) throws WarehouseCallerException {
		if (this.orderTraceCallers != null) {
			for (WarehouseOrderTraceCaller caller : this.orderTraceCallers) {
				if (!caller.onOrderTraceChange(orderTraces)) {
					throw new WarehouseCallerException(DEFAULT_CALLER_FAIL_MESSAGE);
				}
			}
		}
		return true;
	}

	/**
	 * 返回该Adapter需要互转(xml-Object，Object-Object)的DTOclass。亦可通过
	 * {@link #getPackagesToScan()}设置， 如果该实现返回值不为空，则
	 * {@link #getPackagesToScan()}不起作用。
	 * 
	 * @return
	 */
	protected abstract Class<?>[] getBoundClasses();

	/**
	 * 返回该Adapter需要互转(xml-Object，Object-Object)的DTOclass的包名。亦可通过
	 * {@link #getBoundClasses()}设置， 如果{@link #getBoundClasses()}
	 * 返回不为空，则该返回值不起作用。
	 * 
	 * @return
	 */
	protected abstract String[] getPackagesToScan();

	/**
	 * 返回仓库接口地址
	 * 
	 * @return
	 */
	protected abstract String getUrl();

	/**
	 * 返回和接口通信时采用的编码
	 * 
	 * @return
	 */
	protected abstract String getCharSet();

}
