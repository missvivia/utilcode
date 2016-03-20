package com.xyl.mmall.obj;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

/**
 * mainsite专用key生成类
 * <p>
 * 如果传进来的target为代理类
 * @author hzzhaozhenzuo
 *
 */
@Component("mainSiteKeyGenerator")
public class MainSiteKeyGenerator extends SimpleKeyGenerator {
	public Object generate(Object target, Method method, Object... params) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(target.getClass().getName()).append(".");
		buffer.append(method.getName()).append(".");
		if (params.length > 0) {
			for (Object each : params) {
				if (each != null) {
					if (each instanceof Boolean || each instanceof Character || each instanceof Void
							|| each instanceof Short || each instanceof Byte || each instanceof Double
							|| each instanceof Float || each instanceof Integer || each instanceof Long) {
						buffer.append(each);
					} else if (each instanceof Object[]) {
						buffer.append(Arrays.hashCode((Object[]) each)); // 后面会说到可替换Arrays.deepHashCode
					} else if (each instanceof HttpServletRequest || each instanceof HttpServletResponse) {
						continue;
					} else {
						buffer.append(each.hashCode()); // list,map,set其内的元素类型一直才好
					}
				} else {
					buffer.append(SimpleKey.EMPTY);
				}
			}
		} else {
			buffer.append(SimpleKey.EMPTY);
		}
		return buffer.toString().hashCode();
	}
}