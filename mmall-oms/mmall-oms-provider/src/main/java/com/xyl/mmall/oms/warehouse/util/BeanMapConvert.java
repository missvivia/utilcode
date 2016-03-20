/**
 * 
 */
package com.xyl.mmall.oms.warehouse.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsOrderStatusUpdate;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * Object->Object映射工具，支持多层映射，支持List、Map等，不支持enum
 * 
 * @author hzzengchengyuan
 * 
 */
public class BeanMapConvert {
	private BeanMapConfiguration configuration;

	public BeanMapConvert() {
		this(null);
	}

	public BeanMapConvert(BeanMapConfiguration configuration) {
		setBeanMapConfiguration(configuration);
	}

	public void setBeanMapConfiguration(BeanMapConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * <pre>
	 * source -> target，通过映射配置将source的属性值设置到target对象中.
	 * 通过该方法可以将多个对象映射到一个对象中，如：
	 * A a = new B();
	 * ....
	 * B b = new B();
	 * ....
	 * C c = new C();
	 * ....
	 * Test result = map(a,A.class);
	 * map(b,result);
	 * map(c,result);
	 * </pre>
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public void map(Object sourceObject, Object targetObject) {
		if (sourceObject == null || targetObject == null) {
			return;
		}
		BeanMapDescribe desc = getBeanMapDescribe(sourceObject.getClass(), targetObject.getClass());
		if (desc == null) {
			desc = getBeanMapDescribe(targetObject.getClass(), sourceObject.getClass());
			if (desc != null) {
				doMap(targetObject, sourceObject, desc, true);
			} else {
				try {
					BeanUtilsBean.getInstance().copyProperties(targetObject, sourceObject);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} else {
			doMap(sourceObject, targetObject, desc, false);
		}
	}

	/**
	 * object -> new targetClass()，通过配置映射将object的属性值设置到targetClass类型的新建对象中
	 * 
	 * @param object
	 * @param targetClass
	 * @return
	 * @return
	 */
	public <T> T map(Object object, Class<T> targetClass) {
		try {
			T targetObject = targetClass.newInstance();
			map(object, targetObject);
			return targetObject;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	private void doMap(Object sourceObject, Object targetObject, BeanMapDescribe desc, boolean isReverse) {
		for (BeanMapFieldDescribe fieldDesc : desc.getFieldDescribes()) {
			try {
				Object setObject = null;
				String setFieldName = null;
				Object setValue = null;
				if (isReverse) {
					Object value = getProperty(targetObject, fieldDesc.getTargetFieldName());
					setValue = convertData(value, isReverse, fieldDesc);
					setObject = sourceObject;
					setFieldName = fieldDesc.getSourceFieldName();
				} else {
					Object value = getProperty(sourceObject, fieldDesc.getSourceFieldName());
					setValue = convertData(value, isReverse, fieldDesc);
					setObject = targetObject;
					setFieldName = fieldDesc.getTargetFieldName();
				}
				if (setValue != null) {
					setProperty(setObject, setFieldName, setValue);
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchFieldException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	private Object convertData(Object value, boolean isReverse, BeanMapFieldDescribe fieldDesc) {
		 MapField fieldAnnotation = fieldDesc.getSourceFieldAnnotation();
		 Class<?> type = isReverse ? fieldDesc.getSourceFieldType() : fieldDesc.getTargetFieldType();
		 Class<?>[] pzTypes = isReverse ? fieldDesc.getSourceFieldPzTypes() : fieldDesc.getTargetFieldPzTypes();
		 Object result = null;
		if (value == null) {
			return null;
		} else if ((type == ArrayList.class || type == List.class) && pzTypes.length == 1) {
			result = convertListData(value, pzTypes);
		} else if ((type == HashMap.class || type == Map.class) && pzTypes.length == 2) {
			result = convertMapData(value, pzTypes);
		} else if (value.getClass() == type) {
			result = value;
		} else if (fieldAnnotation != null && fieldAnnotation.isDate()) {
			result = convertDate(value, type);
		} else if(fieldAnnotation != null && (fieldAnnotation.isKG() || fieldAnnotation.isG())){
			result = convertWeight(value, fieldAnnotation, type, isReverse);
		} else if(fieldAnnotation != null && (fieldAnnotation.isM() || fieldAnnotation.isDM() || fieldAnnotation.isCM() || fieldAnnotation.isMM())) {
			result = convertLength(value, fieldAnnotation, type, isReverse);
		} else if (value.getClass() != type) {
			if (!BasicTypeConvertUtils.isWrapClass(value.getClass())) {
				result = map(value, type);
			} else {
				result = BasicTypeConvertUtils.convert(value, type);
			}
		} else {
			result = value;
		}
		if (result != null && BasicTypeConvertUtils.isWrapClass(type) && result.getClass() != type) {
			result = BasicTypeConvertUtils.convert(result, type);
		} 
		return result;
	}
	
	private Object convertDate (Object value, Class<?> type) {
		Date date = null;
		Class<?> valueClass = value.getClass();
		if (valueClass == String.class) {
			date = DateUtils.parseToDate((String) value);
		} else if (valueClass == long.class || valueClass == Long.class) {
			date = DateUtils.parseToDate((long) value);
		} else if (valueClass == Date.class) {
			date = (Date) value;
		} else {
			date = null;
		}
		if (date == null) {
			return null;
		}
		if (type == Date.class) {
			return date;
		} else if (type == String.class) {
			return DateUtils.format(date);
		} else if (type == long.class || type == Long.class) {
			return date.getTime();
		}
		return date;
	}
	
	private Object convertWeight (Object value,MapField fieldAnnotation, Class<?> type, boolean isReverse) {
		int multiple = fieldAnnotation.isG() ? 1 : fieldAnnotation.isKG() ? 1000 : 1;
		Object result = 0;
		if (value == null) {
			return result;
		}
		if (value.getClass().equals(double.class) || value.getClass().equals(Double.class)) {
			if (isReverse) {
				result = ((Double)value) / multiple;
			} else {
				result = new BigDecimal(((Double)value) * multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			}
		} else if (value.getClass().equals(float.class) || value.getClass().equals(Float.class)) {
			if (isReverse) {
				result = ((Float)value * 1.0) / multiple;
			} else {
				result = new BigDecimal(((Float)value) * multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			}
		} else if (value.getClass().equals(long.class) || value.getClass().equals(Long.class)) {
			if (isReverse) {
				result = ((Long)value * 1.0) / multiple;
			} else {
				result = new BigDecimal(((Long)value) * multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			}
		} else if (value.getClass().equals(String.class)) {
			try {
				Object temp = Double.parseDouble((String)value);
				return convertWeight (temp, fieldAnnotation, type, isReverse);
			} catch (Exception e) {
				try {
					Object temp = Float.parseFloat((String)value);
					return convertWeight (temp, fieldAnnotation, type, isReverse);
				} catch (Exception e1) {
					try {
						Object temp = Long.parseLong((String)value);
						return convertWeight (temp, fieldAnnotation, type, isReverse);
					} catch (Exception e2) {
						
					}
				}
			}
		} else {
			return convertWeight(value.toString(), fieldAnnotation, type, isReverse);
		}
		return result;
	}
	
	private Object convertLength (Object value,MapField fieldAnnotation, Class<?> type, boolean isReverse) {
		int multiple = fieldAnnotation.isMM() ? 1 : fieldAnnotation.isCM() ? 10 : fieldAnnotation.isDM() ? 100 : fieldAnnotation.isM() ? 1000 : 1;
		Object result = 0;
		if (value == null) {
			return result;
		}
		if (value.getClass().equals(double.class) || value.getClass().equals(Double.class)) {
			if (isReverse) {
				result = ((Double)value) / multiple;
			} else {
				result = new BigDecimal(((Double)value) * multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			}
			
		} else if (value.getClass().equals(float.class) || value.getClass().equals(Float.class)) {
			if (isReverse) {
				result = ((Float)value * 1.0) / multiple;
			} else {
				result = new BigDecimal(((Float)value) * multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			}
		} else if (value.getClass().equals(long.class) || value.getClass().equals(Long.class)) {
			if (isReverse) {
				result = ((Long)value * 1.0) / multiple;
			} else {
				result = new BigDecimal(((Long)value) * multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
			}
		} else if (value.getClass().equals(String.class)) {
			try {
				Object temp = Double.parseDouble((String)value);
				return convertLength (temp, fieldAnnotation, type, isReverse);
			} catch (Exception e) {
				try {
					Object temp = Float.parseFloat((String)value);
					return convertLength (temp, fieldAnnotation, type, isReverse);
				} catch (Exception e1) {
					try {
						Object temp = Long.parseLong((String)value);
						return convertLength (temp, fieldAnnotation, type, isReverse);
					} catch (Exception e2) {
						
					}
				}
			}
		} else {
			return convertLength(value.toString(), fieldAnnotation, type, isReverse);
		}
		return result;
	}
	
	private Map<?, ?> convertMapData(Object value, Class<?>[] pzTypes) {
		Map<Object, Object> data = new HashMap<Object, Object>();
		Map<?, ?> valueMap = (Map<?, ?>) value;
		for (Object key : valueMap.keySet()) {
			Object v = valueMap.get(key);
			Object dataKey;
			Object dataVal;
			if (BasicTypeConvertUtils.isWrapClass(key.getClass())) {
				dataKey = key;
			} else {
				dataKey = map(key, pzTypes[0]);
			}
			if (BasicTypeConvertUtils.isWrapClass(v.getClass())) {
				dataVal = v;
			} else {
				dataVal = map(v, pzTypes[1]);
			}
			data.put(dataKey, dataVal);
		}
		return data.size() == 0 ? null : data;
	}

	private List<?> convertListData(Object value, Class<?>[] pzTypes) {
		List<Object> data = new ArrayList<Object>();
		for (Object obj : (List<?>) value) {
			Object temp = map(obj, pzTypes[0]);
			if (temp != null) {
				data.add(temp);
			}
		}
		return data.size() == 0 ? null : data;
	}

	private BeanMapDescribe getBeanMapDescribe(Class<?> clazz1, Class<?> clazz2) {
		for (BeanMapDescribe desc : this.configuration.getBeanMapDescribe()) {
			if (clazz1 == desc.getSourceType() && desc.getTargetType() == clazz2) {
				return desc;
			}
		}
		return null;
	}

	private static Object getProperty(Object obj, String field) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String[] filedArray = field.split("\\.");
		Object result = obj;
		int index = 0;
		int length = filedArray.length;
		while (index < length && result != null) {
			result = executeGetMethod(result, filedArray[index]);
			index++;
		}
		return result;
	}

	private static void setProperty(Object obj, String field, Object value) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchFieldException, InstantiationException {
		String[] filedArray = field.split("\\.");
		Object result = obj;
		int index = 0;
		int length = filedArray.length;
		while (index < length - 1) {
			String fieldName = filedArray[index];
			Object parentObj = executeGetMethod(result, fieldName);
			if (parentObj == null) {
				Field temp = result.getClass().getDeclaredField(fieldName);
				parentObj = temp.getType().newInstance();
				executeSetMethod(result, fieldName, parentObj);
			}
			result = parentObj;
			index++;
		}
		executeSetMethod(result, filedArray[index], value);
	}

	private static Object executeGetMethod(Object obj, String field) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = null;
		Class<?> clazz = obj.getClass();
		while (method == null && !BasicTypeConvertUtils.isBaseClass(clazz)) {
			try {
				method = clazz.getDeclaredMethod(getFieldGetMethodName(field));
			} catch (NoSuchMethodException | SecurityException e) {
				method = null;
			}
			clazz = clazz.getSuperclass();
		}
		if (method == null) {
			throw new NoSuchMethodException(field);
		}
		method.setAccessible(true);
		return method.invoke(obj);
	}

	private static String getFieldGetMethodName(String field) {
		return new StringBuilder().append("get").append(Character.toUpperCase(field.charAt(0)))
				.append(field.substring(1)).toString();
	}

	private static void executeSetMethod(Object obj, String field, Object value) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// if (value == null) {
		// return;
		// }
		// // 基本上数据类型做一下特殊处理
		// Class<?> paramClass = value.getClass();
		// Method method =
		// obj.getClass().getDeclaredMethod(getFieldSetMethodName(field),
		// paramClass);
		// method.setAccessible(true);
		// method.invoke(obj, value);
		PropertyUtils.setProperty(obj, field, value);
	}

	@SuppressWarnings("unused")
	private static String getFieldSetMethodName(String field) {
		return new StringBuilder().append("set").append(Character.toUpperCase(field.charAt(0)))
				.append(field.substring(1)).toString();
	}

	public static void main(String args[]) throws Exception {
		EmsWarehouseAdapter adapter = new EmsWarehouseAdapter(){
			@Override
			protected void childInit() {
			}
		};
		adapter.init();
		String content = "\n<RequestOrderStatusInfo>\n  <warehouse_code>40717521</warehouse_code>\n  <owner_code>WYTM</owner_code>\n  <order_id>S_3710</order_id>\n  <status>SHIP</status>\n  <LogisticProviderId>UNDEFINED</LogisticProviderId>\n  <remark>1203000005|10</remark>\n  <operate_time>2014-12-03 19:50:09</operate_time>\n  <product_code>4310202991</product_code>\n  <ship_details>\n    <ship_detail>\n      <mailno>1192482124303</mailno>\n      <length>20</length>\n      <width>2.3</width>\n      <height>0</height>\n      <weight>8.245</weight>\n      <sku_details>\n        <sku_detail>\n          <sku_code>2019</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2022</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2034</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2041</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2043</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2047</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2051</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2055</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2059</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n        <sku_detail>\n          <sku_code>2103</sku_code>\n          <count>1</count>\n          <sku_length>0</sku_length>\n          <sku_width>0</sku_width>\n          <sku_height>0</sku_height>\n          <sku_weight>0</sku_weight>\n        </sku_detail>\n      </sku_details>\n    </ship_detail>\n  </ship_details>\n</RequestOrderStatusInfo>";
		WmsOrderStatusUpdate orderStatus = adapter.unmarshal(content, WmsOrderStatusUpdate.class);
		orderStatus.setOrder_id(OmsIdUtils.backEmsOrderId(orderStatus.getOrder_id()));
		WMSSalesOrderUpdateDTO salesOrder = adapter.map(orderStatus, WMSSalesOrderUpdateDTO.class);
		salesOrder.calculateCount();
		System.out.println(salesOrder.getPackages().get(0).getWeight());
		WmsOrderStatusUpdate temp = adapter.map(salesOrder, WmsOrderStatusUpdate.class);
		System.out.println(temp.getList().get(0).getWeight());
		System.out.println(BasicTypeConvertUtils.convert("2.4",Integer.class));
		System.out.println(BasicTypeConvertUtils.convert("2.4",Float.class));
		System.out.println(BasicTypeConvertUtils.convert("2.4",Long.class));
		System.out.println(BasicTypeConvertUtils.convert("2.6",Long.class));
		System.out.println(BasicTypeConvertUtils.convert("7772.6",Short.class));
	}
}
