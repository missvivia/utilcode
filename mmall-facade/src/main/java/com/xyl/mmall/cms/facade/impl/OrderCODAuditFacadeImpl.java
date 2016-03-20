package com.xyl.mmall.cms.facade.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.OrderCODAuditFacade;
import com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.order.CODAuditInfoListVO;
import com.xyl.mmall.cms.vo.order.CODAuditInfoListVO.CODAuditInfo;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.CODAuditQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.CODAuditQueryCategoryListVO.CODSearchType;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.order.dto.CODAuditLogDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.param.CODWBlistAddressParam;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.ConsigneeAddressService;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderExpInfoService;
import com.xyl.mmall.promotion.service.RebateService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午9:24:08
 *
 */
@Facade("OrderCODAuditFacade")
public class OrderCODAuditFacadeImpl implements OrderCODAuditFacade {
	
	private static final Logger logger = Logger.getLogger(OrderCODAuditFacadeImpl.class);

	@Resource
	OrderBriefService orderBriefService;
	
	@Resource
	OrderExpInfoService ordExpInfoService;
	
	@Resource
	ConsigneeAddressService consigneeAddressService;
	
	@Resource
	OrderExpInfoService orderExpInfoService;
	
	@Resource
	CODAuditService codAuditService;
	
	@Resource
	AgentService agentService;
	
	@Resource
	RebateService rebateService;
	
	@Resource
	BusinessService businessService;
	
	@Resource
	MobilePushManageFacade mobilePushManageFacade;
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderCODAuditFacade#getCmsCODSearchTypeList()
	 */
	@Override
	public CODAuditQueryCategoryListVO getCmsCODSearchTypeList() {
		return CODAuditQueryCategoryListVO.getInstance();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderCODAuditFacade#getCODInfoListByTime(com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam)
	 */
	@Override
	public CODAuditInfoListVO getCODInfoListByTime(FrontTimeRangeSearchTypeParam param) {
		if(null == param || null == param.getTimeRange()) {
			return new CODAuditInfoListVO();
		}
		CODAuditState codAuditState = CODAuditState.PASSED.genEnumByIntValue(param.getStatus());
		CODAuditState[] states = (null == codAuditState) 
				? new CODAuditState[] {CODAuditState.WAITING, CODAuditState.PASSED, CODAuditState.REFUSED} 
				: new CODAuditState[] {codAuditState};
		RetArg retArg = codAuditService.queryCODAuditLogWithTimeRange2(states, 
				param.getTimeRange().getStartTime(), 
				param.getTimeRange().getEndTime(), 
				new DDBParam("userId", true, param.getLimit(), param.getOffset()));
		return getCODInfoListExec(retArg);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderCODAuditFacade#getCODInfoListBySearch(com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam)
	 */
	@Override
	public CODAuditInfoListVO getCODInfoListBySearch(FrontTimeRangeSearchTypeParam param) {
		if(null == param || null == param.getSearch()) {
			return new CODAuditInfoListVO();
		}
		CODAuditState codAuditState = CODAuditState.PASSED.genEnumByIntValue(param.getStatus());
		CODAuditState[] states = (null == codAuditState) 
				? new CODAuditState[] {CODAuditState.WAITING, CODAuditState.PASSED, CODAuditState.REFUSED} 
				: new CODAuditState[] {codAuditState};
		int searchType = param.getSearch().getSearchType();
		String searchKey = param.getSearch().getSearchKey();
		/** 
		List<Long> userIds = getUserIdListByInfoType(CODSearchType.USER_ID.genEnumByIntValue(searchType), searchKey, null);
		RetArg retArg = codAuditService.queryCODAuditLogWithUserIdList(states, userIds, 
				new DDBParam("userId", true, param.getLimit(), param.getOffset())); 
		*/
		List<Long> orderIds = getOrderIdListByInfoType(CODSearchType.USER_ID.genEnumByIntValue(searchType), searchKey, null);
		RetArg retArg = codAuditService.queryCODAuditLogWithOrderIdList(states, orderIds, 
				new DDBParam("userId", true, param.getLimit(), param.getOffset()));
		return getCODInfoListExec(retArg);
	}

	@SuppressWarnings("unchecked")
	private CODAuditInfoListVO getCODInfoListExec(RetArg retArg) {
		CODAuditInfoListVO ret = new CODAuditInfoListVO();
		List<CODAuditLogDTO> codList = RetArgUtil.get(retArg, ArrayList.class);
		if(null == codList) {
			return ret;
		}
		Set<Long> auditUserIdSet = new HashSet<Long>();
		for(CODAuditLogDTO dto : codList) {
			if(null == dto) {
				continue;
			}
			auditUserIdSet.add(dto.getAuditUserId());
		}
		List<Long> auditUserIdList = new ArrayList<Long>(auditUserIdSet.size());
		for(Long id : auditUserIdSet) {
			auditUserIdList.add(id);
		}
		Map<Long, String> auditUserNames = getAuditUserNames(auditUserIdList);
		Map<Long, String> areaNames = getSaleAreaNames(codList);
		for(CODAuditLogDTO dto : codList) {
			if(null == dto) {
				continue;
			}
			long userId = dto.getUserId();
			long orderId = dto.getOrderId();
			OrderFormBriefDTO ordDTO = orderBriefService.queryOrderFormBrief(userId, orderId, null);
			if(null == ordDTO) {
				continue;
			}
			OrderExpInfoDTO ordExp = ordExpInfoService.queryInfoByUserIdAndOrderId(userId, orderId);
			long auditUserId = dto.getAuditUserId();
			String auditUser = auditUserNames.get(auditUserId);
			Long areaId = new Long(dto.getProvinceId());
			String area = areaNames.get(areaId);
			ret.getList().add(CODAuditInfo.convertDTOs2CODInfo(dto, ordDTO, ordExp, auditUser, area));
		}
		DDBParam remoteDDBParam = RetArgUtil.get(retArg, DDBParam.class);
		if(null != remoteDDBParam && null != remoteDDBParam.getTotalCount()) {
			ret.setTotal(remoteDDBParam.getTotalCount());
		}
		return ret;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderCODAuditFacade#passAudit(com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam)
	 */
	@Override
	public boolean passAudit(FrontCODAuditOperationParam param) {
		long logId = param.getLogId();
		long orderId = param.getOrderId();
		long userId = param.getUserId();
		long auditUserId = SecurityContextUtils.getUserId();
		OrderExpInfoDTO expDTO = orderExpInfoService.queryInfoByUserIdAndOrderId(userId, orderId);				
		CODWBlistAddressParam addressParam = new CODWBlistAddressParam();
		addressParam.fillWithOrderExpInfo(expDTO);
		boolean isSucc = false;
		try {
			isSucc = codAuditService.passCODAuditByCustomerService(userId, logId, auditUserId, addressParam);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return false;
		}
		if(isSucc) {
			if(!rebateService.rebate(userId, orderId)) {
				logger.warn("rebate失败: [userId:" + userId + ", orderId:" + orderId + "]");
			}
		}
		return isSucc;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderCODAuditFacade#rejectAudit(com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam)
	 */
	@Override
	public boolean rejectAudit(FrontCODAuditOperationParam param) {
		long logId = param.getLogId();
		long auditUserId = SecurityContextUtils.getUserId();
		try {
			boolean isSucc = codAuditService.setCODAuditStateToRefused(param.getUserId(), logId, auditUserId, param.getExtInfo());
			if(isSucc) {
				try {
					CODAuditLogDTO log = codAuditService.queryCODAuditLog(logId, param.getUserId());
					if(null != log) {
						mobilePushManageFacade.push(param.getUserId(), 6, null, 
								null, log.getOrderId());
					}
				} catch (Exception e) {
					logger.info(e.getMessage(), e);
				}
			}
			return isSucc;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderCODAuditFacade#cancelReject(com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam)
	 */
	@Override
	public boolean cancelReject(FrontCODAuditOperationParam param) {
		long logId = param.getLogId();
		long auditUserId = SecurityContextUtils.getUserId();
		try {
			boolean isSucc = codAuditService.cancelCODAuditStateToWaiting(param.getUserId(), logId, auditUserId);
			return isSucc;
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean addUserToBlack(FrontCODAuditOperationParam param) {
		long userId = param.getUserId();
		long auditUserId = SecurityContextUtils.getUserId();
		boolean isSucc = codAuditService.addToBlacklistUser(userId, auditUserId);
		return isSucc;
	}

	@Override
	public boolean addAddressToBlack(FrontCODAuditOperationParam param) {
		long orderId = param.getOrderId();
		long userId = param.getUserId();
		long auditUserId = SecurityContextUtils.getUserId();
		OrderExpInfoDTO expDTO = orderExpInfoService.queryInfoByUserIdAndOrderId(userId, orderId);
		CODWBlistAddressParam addressParam = new CODWBlistAddressParam();
		addressParam.fillWithOrderExpInfo(expDTO);
		boolean isSucc = codAuditService.addToBlacklistAddress(auditUserId, addressParam);
		return isSucc;
	}

	/**
	 * 根据CODSearchType获取定案id
	 * @param t
	 * @param value
	 * @return
	 */
	private List<Long> getOrderIdListByInfoType(CODSearchType t, String value, DDBParam ddbParam) {
		List<OrderExpInfoDTO> ordExpList = null;
		switch(t) {
		case USER_ID:
			ordExpList = orderExpInfoService.queryInfoByUserId(Long.parseLong(value), ddbParam);
			break;
		case CONSIGNEE_NAME:
			ordExpList = orderExpInfoService.queryInfoByConsigneeName(value, ddbParam);
			break;
		case CONSIGNEE_TEL:
			ordExpList = orderExpInfoService.queryInfoByConsigneeMobile(value, ddbParam);
			break;
//		case CONSIGNEE_ADDRESS:
//			ordExpList = orderExpInfoService.queryInfoByConsigneeAddress(value, ddbParam);
//			break;
		default:
			ordExpList = new ArrayList<OrderExpInfoDTO>();
		}
		Set<Long> orderIdSet = new HashSet<Long>();
		for(OrderExpInfoDTO dto : ordExpList) {
			orderIdSet.add(dto.getOrderId());
		}
		List<Long> ret = new ArrayList<Long>(orderIdSet.size());
		for(Long id : orderIdSet) {
			ret.add(id);
		}
		return ret;
	}
	
	/**
	 * 获取后台操作人员姓名
	 * 
	 * @param auditUserIdList
	 * @return
	 */
	private Map<Long, String> getAuditUserNames(List<Long> auditUserIdList) {
		Map<Long, String> userNames = new HashMap<Long, String>();
		if(null == auditUserIdList || 0 == auditUserIdList.size()) {
			return userNames;
		}
		List<AgentDTO> agentList = agentService.findAgentByIdList(auditUserIdList);
		if(null == agentList) {
			return userNames;
		}
		for(AgentDTO agent : agentList) {
			if(null == agent) {
				continue;
			}
			userNames.put(agent.getId(), agent.getName());
		}
		return userNames;
	}
	
	private Map<Long, String> getSaleAreaNames(List<CODAuditLogDTO> codList) {
		Map<Long, String> areaNames = new HashMap<Long, String>();
		if(null == codList || 0 == codList.size()) {
			return areaNames;
		}
		Set<Long> areaIdSet = new HashSet<Long>();
		for(CODAuditLogDTO cod : codList) {
			if(null == cod) {
				continue;
			}
			areaIdSet.add(new Long(cod.getProvinceId()));
		}
		List<Long> areaIdList = new ArrayList<Long>(areaIdSet.size());
		for(Long areaId : areaIdSet) {
			areaIdList.add(areaId);
		}
		List<AreaDTO> areaList = businessService.getAreadByIdList(areaIdList);
		for(AreaDTO area : areaList) {
			if(null == area) {
				continue;
			}
			areaNames.put(area.getId(), area.getAreaName());
		}
		return areaNames;
	}
}
