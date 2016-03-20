package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dao.OrderExpInfoDao;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.meta.OrderExpInfo;
import com.xyl.mmall.order.service.OrderExpInfoService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月15日 上午10:49:04
 *
 */
@Service("orderExpInfoService")
public class OrderExpInfoServiceImpl implements OrderExpInfoService {

	@Autowired
	protected OrderExpInfoDao orderExpInfoDao;
	
	private List<OrderExpInfoDTO> convertMetaToDTO(List<OrderExpInfo> list) {
		if(null == list) {
			return null;
		}
		List<OrderExpInfoDTO> dtoList = new ArrayList<OrderExpInfoDTO>(list.size());
		for(OrderExpInfo oei : list) {
			dtoList.add(new OrderExpInfoDTO(oei));
		}
		return dtoList;
	}

	@Override
	public OrderExpInfoDTO queryInfoByUserIdAndOrderId(long userId, long orderId) {
		OrderExpInfo expInfo = orderExpInfoDao.getObjectByIdAndUserId(orderId, userId);
		return null == expInfo ? null : new OrderExpInfoDTO(expInfo);
	}

	@Override
	public List<OrderExpInfoDTO> queryInfoByUserId(long userId, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoListByUserId(userId, ddbParam);
		return convertMetaToDTO(infoList);
	}

	@Override
	public RetArg queryInfoByUserId2(long userId, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoListByUserId(userId, ddbParam);
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, ddbParam);
		RetArgUtil.put(ret, convertMetaToDTO(infoList));
		return ret;
	}

	@Override
	public List<OrderExpInfoDTO> queryInfoByConsigneeName(String consigneeName, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoByConsigneeName(consigneeName, ddbParam);
		return convertMetaToDTO(infoList);
	}

	@Override
	public RetArg queryInfoByConsigneeName2(String consigneeName, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoByConsigneeName(consigneeName, ddbParam);
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, ddbParam);
		RetArgUtil.put(ret, convertMetaToDTO(infoList));
		return ret;
	}

	@Override
	public List<OrderExpInfoDTO> queryInfoByConsigneeMobile(String consigneeMobile, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoByConsigneeMobile(consigneeMobile, ddbParam);
		return convertMetaToDTO(infoList);
	}

	@Override
	public RetArg queryInfoByConsigneeMobile2(String consigneeMobile, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoByConsigneeMobile(consigneeMobile, ddbParam);
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, ddbParam);
		RetArgUtil.put(ret, convertMetaToDTO(infoList));
		return ret;
	}

	@Override
	public List<OrderExpInfoDTO> queryInfoByConsigneeAddress(String consigneeAddress, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoByConsigneeAddress(consigneeAddress, ddbParam);
		return convertMetaToDTO(infoList);
	}

	@Override
	public RetArg queryInfoByConsigneeAddress2(String consigneeAddress, DDBParam ddbParam) {
		List<OrderExpInfo> infoList = orderExpInfoDao.queryInfoByConsigneeAddress(consigneeAddress, ddbParam);
		RetArg ret = new RetArg();
		RetArgUtil.put(ret, ddbParam);
		RetArgUtil.put(ret, convertMetaToDTO(infoList));
		return ret;
	}

}
