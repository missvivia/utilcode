/**
 * 
 */
package com.xyl.mmall.oms.warehouse.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * 根据{@link MapClass}进行弱对象类型映射扫描，具有注解{@link MapClass}为转换发起方，而
 * {@link MapClass#value()}值为目标转换对象
 * 
 * @author hzzengchengyuan
 * 
 */
public class AnnotationBeanMapConfiguration implements BeanMapConfiguration {

	private String[] packagesToScan;

	private List<BeanMapDescribe> describes = new ArrayList<BeanMapDescribe>();

	private static final TypeFilter[] TYPE_FILTERS = new TypeFilter[] { new AnnotationTypeFilter(MapClass.class, false) };

	private static final String RESOURCE_PATTERN = "/**/*.class";

	private ResourcePatternResolver resourcePatternResolver;

	public AnnotationBeanMapConfiguration(String... packagesToScan) {
		this.resourcePatternResolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
		this.packagesToScan = packagesToScan;
		genORM(scanPackages());
	}

	public AnnotationBeanMapConfiguration(Class<?>... clazz) {
		genORM(clazz);
	}

	private Class<?>[] scanPackages() {
		List<Class<?>> beanMapClass = new ArrayList<Class<?>>();
		try {
			for (String packageToScan : this.packagesToScan) {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
						+ ClassUtils.convertClassNameToResourcePath(packageToScan) + RESOURCE_PATTERN;
				Resource[] resources = this.resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
						this.resourcePatternResolver);
				for (Resource resource : resources) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					if (isBeanMapClass(metadataReader, metadataReaderFactory)) {
						String className = metadataReader.getClassMetadata().getClassName();
						Class<?> beanMapAnnotatedClass = this.resourcePatternResolver.getClassLoader().loadClass(
								className);
						beanMapClass.add(beanMapAnnotatedClass);
					}
				}
			}
		} catch (Exception e) {
		}
		return beanMapClass.toArray(new Class<?>[beanMapClass.size()]);
	}

	protected boolean isBeanMapClass(MetadataReader reader, MetadataReaderFactory factory) throws IOException {
		for (TypeFilter filter : TYPE_FILTERS) {
			if (filter.match(reader, factory)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.util.BeanMapConfiguration#getBeanMapDescribe()
	 */
	@Override
	public List<BeanMapDescribe> getBeanMapDescribe() {
		// TODO Auto-generated method stub
		return this.describes;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.util.BeanMapConfiguration#setBeanMapDescribe(com.xyl.mmall.oms.warehouse.util.BeanMapDescribe)
	 */
	@Override
	public void addBeanMapDescribe(BeanMapDescribe desc) {
		describes.add(desc);
	}

	private void genORM(Class<?>... clazz) {
		for (Class<?> sourceClass : clazz) {
			BeanMapDescribe desc = genORM(sourceClass);
			if (desc != null) {
				describes.add(desc);
			}
		}
	}

	private BeanMapDescribe genORM(Class<?> configClass) {
		Class<?> fat = null;
		Annotation[] annotations = configClass.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == MapClass.class) {
				fat = ((MapClass) annotation).value();
			}
		}
		BeanMapDescribe desc = null;
		if (fat != null) {
			List<BeanMapFieldDescribe> fields = new ArrayList<BeanMapFieldDescribe>();
			genORM(fields, configClass, fat);
			if (fields != null) {
				desc = new BeanMapDescribe();
				desc.setSourceType(configClass);
				desc.setTargetType(fat);
				desc.setFieldDescribes(fields);
			}
		}
		return desc;
	}

	private void genORM(List<BeanMapFieldDescribe> desc, Class<?> sourceClass, Class<?> targetClass) {
		while ( !BasicTypeConvertUtils.isBaseClass(sourceClass)) {
			genORM(desc, sourceClass, "", targetClass, "");
			sourceClass = sourceClass.getSuperclass();
		}
	}

	private void genORM(List<BeanMapFieldDescribe> desc, Class<?> sourceClass, String sourceLayer,
			Class<?> targetClass, String targetLayer) {
		Class<?> fat = null;
		boolean isAllFieldMap = false;
		Annotation[] annotations = sourceClass.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == MapClass.class) {
				MapClass mapClass = (MapClass) annotation;
				fat = mapClass.value();
				isAllFieldMap = mapClass.isAllField();
			}
		}
		if (fat != null && fat != targetClass) {
			return;
		}
		Field[] fields = sourceClass.getDeclaredFields();
		for (Field field : fields) {
			// 过滤掉static、final变量
			String modifiers = Modifier.toString(field.getModifiers());
			if (modifiers.contains("static") || modifiers.contains("final")) {
				continue;
			}
			String targetFieldName = null;
			String sourceFieldName = null;
			MapField fieldAnnotation = null;
			Annotation[] fieldAnnos = field.getAnnotations();
			for (Annotation fieldAnno : fieldAnnos) {
				boolean isMapField = fieldAnno.annotationType() == MapField.class;
				boolean isMapClass = fieldAnno.annotationType() == MapClass.class;
				if (isMapClass) {
					genORM(desc, field.getType(), targetLayer + field.getName() + ".", targetClass, sourceLayer);
				} else if (isMapField) {
					sourceFieldName = field.getName();
					targetFieldName = ((MapField) fieldAnno).value();
					targetFieldName = ("".equals(targetFieldName)) ? sourceFieldName : targetFieldName;
					fieldAnnotation = (MapField) fieldAnno;
				}
			}
			if (sourceFieldName == null && isAllFieldMap) {
				sourceFieldName = field.getName();
				targetFieldName = sourceFieldName;
			}
			if (sourceFieldName != null && checkExistField(sourceClass, sourceFieldName)
					&& checkExistField(targetClass, targetFieldName)) {
				Class<?> sourceType = getFieldClass(sourceClass, sourceFieldName);
				Class<?> targetType = getFieldClass(targetClass, targetFieldName);
				BeanMapFieldDescribe fieldMapDesc = new BeanMapFieldDescribe();
				fieldMapDesc.setSourceFieldAnnotation(fieldAnnotation);
				fieldMapDesc.setSourceFieldName(sourceLayer + sourceFieldName);
				fieldMapDesc.setSourceFieldType(sourceType);
				fieldMapDesc.setTargetFieldName(targetLayer + targetFieldName);
				fieldMapDesc.setTargetFieldType(targetType);
				fieldMapDesc.setSourceFieldPzTypes(getFieldParameterizedType(sourceClass, sourceFieldName));
				fieldMapDesc.setTargetFieldPzTypes(getFieldParameterizedType(targetClass, targetFieldName));
				desc.add(fieldMapDesc);
			}
		}
	}

	private Class<?>[] getFieldParameterizedType(Class<?> clazz, String fieldName) {
		Type type = null;
		try {
			type = clazz.getDeclaredField(fieldName).getGenericType();
			if (type instanceof ParameterizedType) {
				Type[] pztypes = ((ParameterizedType) type).getActualTypeArguments();
				Class<?>[] result = new Class<?>[pztypes.length];
				for (int i = 0; i < pztypes.length; i++) {
					result[i] = getClassFromType(pztypes[i]);
				}
				return result;
			} else {
				return null;
			}
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
			return null;
		}
	}

	@SuppressWarnings("static-access")
	private Class<?> getClassFromType(Type type) throws ClassNotFoundException {
		String className = type.toString();
		className = className.substring(className.indexOf(" ")).trim();
		return getClass().forName(className);
	}

	private Class<?> getFieldClass(Class<?> clazz, String fieldName) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
		return field == null ? null : field.getType();
	}

	private boolean checkExistField(Class<?> clazz, String fieldName) {
		Field field = null;
		//尝试获取父类属性
		while (field == null && !BasicTypeConvertUtils.isBaseClass(clazz)) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException | SecurityException e) {
			}
			clazz = clazz.getSuperclass();
		}
		return field != null;
	}

}