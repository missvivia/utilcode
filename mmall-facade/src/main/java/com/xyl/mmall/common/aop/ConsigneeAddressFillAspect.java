/**
 * 
 */
package com.xyl.mmall.common.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * @author lihui
 *
 */
@Aspect
@Component
public class ConsigneeAddressFillAspect {

	@Autowired
	private LocationFacade locationFacade;

	@Pointcut("execution(@com.xyl.mmall.order.annotation.ConsigneeAddressFiller * *(..))")
	public void needFillConsigneeAddressMethod() {
	}

	@Around("needFillConsigneeAddressMethod()")
	public Object fillConsigneeAddressWithLocationName(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();
		if (result instanceof List) {
			@SuppressWarnings("unchecked")
			List<ConsigneeAddressDTO> addressList = (List<ConsigneeAddressDTO>) result;
			if (CollectionUtils.isNotEmpty(addressList)) {
				Set<Long> codeSet = new HashSet<>();
				for (ConsigneeAddressDTO address : addressList) {
					codeSet.add(address.getProvinceId());
					if (0 < address.getCityId()) {
						codeSet.add(address.getCityId());
					}
					if (0 < address.getSectionId()) {
						codeSet.add(address.getSectionId());
					}
					if (0 != address.getStreetId()) {
						codeSet.add(address.getStreetId());
					}
				}
				List<LocationCode> locationList = locationFacade
						.getLocationCodeListByCodeList(new ArrayList<>(codeSet));
				Map<Long, String> locationMap = locationListToMap(locationList);
				for (ConsigneeAddressDTO address : addressList) {
					address.setCity(StringUtils.trimToEmpty(locationMap.get(address.getCityId())));
					address.setProvince(StringUtils.trimToEmpty(locationMap.get(address.getProvinceId())));
					address.setSection(StringUtils.trimToEmpty(locationMap.get(address.getSectionId())));
					address.setStreet(StringUtils.trimToEmpty(locationMap.get(address.getStreetId())));
				}
			}
			return addressList;
		} else {
			ConsigneeAddressDTO address = (ConsigneeAddressDTO) result;
			if (address != null) {
				List<Long> codeList = new ArrayList<>();
				codeList.add(address.getProvinceId());
				if (0 < address.getCityId()) {
					codeList.add(address.getCityId());
				}
				if (0 < address.getSectionId()) {
					codeList.add(address.getSectionId());
				}
				if (0 != address.getStreetId()) {
					codeList.add(address.getStreetId());
				}
				List<LocationCode> locationList = locationFacade.getLocationCodeListByCodeList(codeList);
				Map<Long, String> locationMap = locationListToMap(locationList);
				address.setCity(StringUtils.trimToEmpty(locationMap.get(address.getCityId())));
				address.setProvince(StringUtils.trimToEmpty(locationMap.get(address.getProvinceId())));
				address.setSection(StringUtils.trimToEmpty(locationMap.get(address.getSectionId())));
				address.setStreet(StringUtils.trimToEmpty(locationMap.get(address.getStreetId())));
			}
			return address;
		}
	}

	/**
	 * @param locationList
	 * @return
	 */
	private Map<Long, String> locationListToMap(List<LocationCode> locationList) {
		Map<Long, String> locationMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(locationList)) {
			for (LocationCode location : locationList) {
				locationMap.put(location.getCode(), location.getLocationName());
			}
		}
		return locationMap;
	}
}
