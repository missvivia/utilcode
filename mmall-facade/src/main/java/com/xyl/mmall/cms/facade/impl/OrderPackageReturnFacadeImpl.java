package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.OrderPackageReturnFacade;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.order.ReturnPackageBriefInfoListVO;
import com.xyl.mmall.cms.vo.order.ReturnPackageDetailInfoVO;
import com.xyl.mmall.cms.vo.order.ReturnSkuInfoListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.ReturnPackageQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.ReturnPackageQueryCategoryListVO.ReturnPackageKFSearchType;
import com.xyl.mmall.common.facade.ReturnPackageCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnFormDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月14日 下午7:38:38
 *
 */
@Facade("OrderPackageReturnFacade")
public class OrderPackageReturnFacadeImpl implements OrderPackageReturnFacade {
	
	private static final Logger logger = Logger.getLogger(OrderPackageReturnFacadeImpl.class);
	
	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;
	
	public static class CWBatchConfirmResult {
		private int length;
		private List<Long> list = new ArrayList<Long>();
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public List<Long> getList() {
			return list;
		}
		public void setList(List<Long> list) {
			this.list = list;
		}
	}

	@Resource
	private ReturnPackageQueryService retPkgQueryService;
	
	@Resource
	private ReturnPackageUpdateService retPkgUpdateService;
	
	@Resource
	private OrderPackageSimpleService ordPkgSimpleService;
	
	@Resource
	private CouponOrderService couponOrderService;
	
	@Resource
	private CouponService couponService;
	
	@Resource
	private BusinessService businessService;
	
	@Resource
	private ReturnPackageCommonFacade retPkgCommonFacade;
	
	/**
	 * FrontTimeRangeSearchTypeParam.status
	 * @param tag
	 * @return
	 */
	private ReturnPackageState[] getStateArrayByStatus(int status) {
		switch(status) {
		case 0:
			return ReturnPackageState.stateArrayOfAll();
		case 1:
			return ReturnPackageState.stateArrayOfWaitingConfirm();
		case 2:
			return ReturnPackageState.stateArrayOfAbormalWaitingReturn();
		case 3:
			return ReturnPackageState.stateArrayOfNormalWaitingReturn();
		case 4:
			return ReturnPackageState.stateArrayOfRefused();
		case 5:
			return ReturnPackageState.stateArrayOfReturned();
		case 6:
			return ReturnPackageState.stateArrayOfCancelled();
		case 7:
			return ReturnPackageState.stateArrayOfCODAll();
		case 8:
			return ReturnPackageState.stateArrayOfCODWaitingReturn();
		case 9:
			return ReturnPackageState.stateArrayOfCODReturned();
		default:
			return ReturnPackageState.stateArrayOfAll();
		}
	}
	
	/**
	 * 站点信息
	 * 
	 * @param retPkgDTOList
	 * @return: key->retPkgId, value->area
	 */
	private Map<Long, String> getSaleAreas(List<ReturnPackageDTO> retPkgDTOList) {
		Map<Long, String> saleAreas = new HashMap<Long, String>();
		if(null == retPkgDTOList) {
			return saleAreas;
		}
		for(ReturnPackageDTO retPkg : retPkgDTOList) {
			OrderFormBriefDTO ordForm = null;
			if(null == retPkg || null == (ordForm = retPkg.getOrdFormBriefDTO())) {
				continue;
			}
			long provinceId = ordForm.getProvinceId();
			AreaDTO area = businessService.getAreaById(provinceId);
			if(null != area) {
				saleAreas.put(retPkg.getRetPkgId(), area.getAreaName());
			}
		}
		return saleAreas;
	}
	
	/**
	 * 站点信息
	 * 
	 * @param retPkgDTOList
	 * @return: key->retPkgId, value->earliestPOEndTime
	 */
	private Map<Long, Long> getEarliestPOEndTimes(List<ReturnPackageDTO> retPkgDTOList) {
		Map<Long, Long> earliestPOEndTimes = new HashMap<Long, Long>();
		if(null == retPkgDTOList) {
			return earliestPOEndTimes;
		}
		for(ReturnPackageDTO retPkg : retPkgDTOList) {
			OrderPackageSimpleDTO ordPkgDTO = null;
			if(null == retPkg || null == (ordPkgDTO = retPkg.getOrdPkgDTO())) {
				continue;
			}
			long earliestPOEndTime = retPkgCommonFacade.getEarliestPOEndTime(ordPkgDTO);
			earliestPOEndTimes.put(retPkg.getRetPkgId(), earliestPOEndTime);
		}
		return earliestPOEndTimes;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#cmsReturnOrderKFSearchTypeList()
	 */
	@Override
	public ReturnPackageQueryCategoryListVO cmsReturnOrderKFSearchTypeList() {
		return ReturnPackageQueryCategoryListVO.getInstance();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#getReturnPackageBriefInfoListByTime(com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam)
	 */
	@Override
	public ReturnPackageBriefInfoListVO getReturnPackageBriefInfoListByTime(FrontTimeRangeSearchTypeParam param) {
		if(null == param || null == param.getTimeRange()) {
			return new ReturnPackageBriefInfoListVO();
		}
		ReturnPackageState[] stateArray = getStateArrayByStatus(param.getStatus());
		// ugly code: start
		boolean fromCWPage = false;
		if(CollectionUtil.isInArray(new int[] {7, 8, 9}, param.getStatus())) {
			fromCWPage = true;
		}
		// ugly code: end
		RetArg retArg = retPkgQueryService.queryReturnPackageByTimeRange2(
			param.getTimeRange().getStartTime(), 
			param.getTimeRange().getEndTime(), 
			false, stateArray, 
			new DDBParam("ctime", false, param.getLimit(), param.getOffset()));
		return getReturnPackageInfoListExec(retArg, fromCWPage);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#getReturnPackageBriefInfoListBySearch(com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam)
	 */
	@Override
	public ReturnPackageBriefInfoListVO getReturnPackageBriefInfoListBySearch(FrontTimeRangeSearchTypeParam param) {
		if(null == param || null == param.getSearch()) {
			return new ReturnPackageBriefInfoListVO();
		}
		int searchType = param.getSearch().getSearchType();
		String searchKey = param.getSearch().getSearchKey();
		ReturnPackageKFSearchType t = ReturnPackageKFSearchType.RETURN_ID.genEnumByIntValue(searchType);
		if(null == t) {
			return new ReturnPackageBriefInfoListVO();
		}
		ReturnPackageState[] stateArray = getStateArrayByStatus(param.getStatus());
		// ugly code: start
		boolean fromCWPage = false;
		if(CollectionUtil.isInArray(new int[] {7, 8, 9}, param.getStatus())) {
			fromCWPage = true;
		}
		// ugly code: end
		RetArg retArg = null;
		switch(t) {
		case RETURN_ID:
			List<ReturnPackageDTO> retPkgDTOList = new ArrayList<ReturnPackageDTO>(1);
			ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(Long.parseLong(searchKey));
			if(null != retPkgDTO && CollectionUtil.isInArray(stateArray, retPkgDTO.getReturnState())) {
				retPkgDTOList.add(retPkgDTO);
			}
			ReturnPackageBriefInfoListVO retVO = new ReturnPackageBriefInfoListVO();
			retVO.fillWithReturnPackageList(retPkgDTOList, getSaleAreas(retPkgDTOList), getEarliestPOEndTimes(retPkgDTOList), fromCWPage);
			retVO.setTotal(1);
			return retVO;
		case ORDER_ID:
			retArg = retPkgQueryService.queryReturnPackageByOrderIdWithState2(Long.parseLong(searchKey), false, stateArray, 
					new DDBParam("ctime", false, param.getLimit(), param.getOffset()));
			return getReturnPackageInfoListExec(retArg, fromCWPage);
		case MAIL_NO:
			retArg = retPkgQueryService.queryReturnPackageByMailNO2(searchKey, false, stateArray, 
					new DDBParam("ctime", false, param.getLimit(), param.getOffset()));
			return getReturnPackageInfoListExec(retArg, fromCWPage);
		case PACKAGE_ID:
			retArg = retPkgQueryService.queryReturnPackageByOrderPackageIdWithState2(Long.parseLong(searchKey), false, stateArray, 
					new DDBParam("ctime", false, param.getLimit(), param.getOffset()));
			return getReturnPackageInfoListExec(retArg, fromCWPage);
		default:
			return new ReturnPackageBriefInfoListVO();
		}
	}
	
	/**
	 * 
	 * @param retArg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ReturnPackageBriefInfoListVO getReturnPackageInfoListExec(RetArg retArg, boolean fromCWPage) {
		ReturnPackageBriefInfoListVO ret = new ReturnPackageBriefInfoListVO();
		List<ReturnPackageDTO> retPkgDTOList = RetArgUtil.get(retArg, ArrayList.class);
		if(null == retPkgDTOList) {
			return ret;
		}
		Map<Long, String> saleAreas = getSaleAreas(retPkgDTOList);
		Map<Long, Long> earliestPOEndTimes = getEarliestPOEndTimes(retPkgDTOList);
		ret.fillWithReturnPackageList(retPkgDTOList, saleAreas, earliestPOEndTimes, fromCWPage);
		DDBParam remoteDDBParam = RetArgUtil.get(retArg, DDBParam.class);
		if(null != remoteDDBParam && null != remoteDDBParam.getTotalCount()) {
			ret.setTotal(remoteDDBParam.getTotalCount());
		}
		return ret;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#getReturnDetailInfo(long, long)
	 */
	@Override
	public ReturnPackageDetailInfoVO getReturnDetailInfo(long retId, long userId) {
		ReturnPackageDetailInfoVO vo = new ReturnPackageDetailInfoVO();
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retId);
		if(null == retPkgDTO) {
			return vo;
		}
		CouponOrder couponOrder = null;
		Coupon coupon = null;
		ReturnCODBankCardInfoDTO bankCard = retPkgQueryService.queryReturnCODBankCardInfo(retPkgDTO.getBankCardInfoId(), userId);
		ReturnFormDTO retForm = retPkgDTO.getRetFormDTO();
		if(null != retForm && ReturnOrderSkuNumState.APPLY_ORDER_FULL_RETURN == retForm.getApplyedNumState()) {
			ReturnPackageState state = retPkgDTO.getReturnState();
			boolean hit = false;
			if(ReturnPackageState.FINISH_RETURN == state || ReturnPackageState.CW_WAITING_RETURN == state || 
			   ReturnPackageState.FINALLY_RETURNED_TO_USER == state || 
			   ReturnPackageState.FINALLY_COD_RETURNED_TO_USER == state
			   ) {
				if(ReturnOrderSkuNumState.CONFIRM_ORDER_FULL_RETURN == retForm.getConfirmedNumState()) {
					hit = true;
				}
			} else if (ReturnPackageState.WAITING_CONFIRM == state || ReturnPackageState.APPLY_RETURN == state) {
				if(ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN != retForm.getConfirmedNumState()) {
					hit = true;
				}
			} else {
				hit = false;
			}
			if(hit) {
				couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, retPkgDTO.getOrderId());
				if(null != couponOrder) {
					coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
				}
			}
		}
		vo.fillWithReturnPackage(retPkgDTO, bankCard, couponOrder, coupon);
		return vo;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#getReturnSkuInfoList(long, long)
	 */
	@Override
	public ReturnSkuInfoListVO getReturnSkuInfoList(long retId, long userId) {
		ReturnSkuInfoListVO ret = new ReturnSkuInfoListVO();
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retId);
		if(null == retPkgDTO) {
			return ret;
		}
		ret.fillWithReturnOrderSkuList(retPkgDTO.getRetOrdSkuMap());
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#passReturn(com.xyl.mmall.order.param.PassReturnOperationParam)
	 */
	@Override
	public RetArg passReturn(PassReturnOperationParam param) {
		return retPkgUpdateService.passReturn(param, getKFParam());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#rejectReturn(com.xyl.mmall.order.param.ReturnOperationParam)
	 */
	@Override
	public RetArg rejectReturn(ReturnOperationParam param) {
		return retPkgUpdateService.refuseReturn(param, getKFParam());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#cancelReject(com.xyl.mmall.order.param.ReturnOperationParam)
	 */
	@Override
	public RetArg cancelReject(ReturnOperationParam param) {
		return retPkgUpdateService.cancelRefuse(param, getKFParam());
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#cancelReturnByKf(long, long)
	 */
	@Override
	public RetArg cancelReturnByKf(long retPkgId, long userId) {
		ReturnPackageDTO retPkgDTO = retPkgQueryService.queryReturnPackageByRetPkgId(userId, retPkgId);
		OrderPackageSimpleDTO ordPkgDTO = null;
		if(null == retPkgDTO || null == (ordPkgDTO = retPkgDTO.getOrdPkgDTO())) {
			RetArg retArg = new RetArg();
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null _ReturnPackageDTO or null _OrderPackageSimpleDTO for [retPkgId:" + retPkgId + ", userId:" + userId + "]");
			return retArg;
		}
		long earliestPOEndTime = retPkgCommonFacade.getEarliestPOEndTime(ordPkgDTO);
		return retPkgUpdateService.deprecateApplyByKf(retPkgDTO, earliestPOEndTime, getKFParam());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#cwConfirmReturn(long, long)
	 */
	@Override
	public RetArg cwConfirmReturn(long retId, long userId) {
		RetArg retArg = retPkgUpdateService.finishReturnForCOD(retId, userId, getKFParam());
		Boolean result = RetArgUtil.get(retArg, Boolean.class);
		if(null != result && result) {
			try {
				ReturnPackageDTO retPkg = RetArgUtil.get(retArg, ReturnPackageDTO.class);
				if(null != retPkg) {
					mobilePushManageFacade.push(userId, 2, null, null, retPkg.getOrderId());
				}
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
			}
		}
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderPackageReturnFacade#cwBatchConfirmReturn(java.util.Map)
	 */
	@Override
	public RetArg cwBatchConfirmReturn(Map<Long, Long> batchParam) {
		RetArg retArg = new RetArg();
		long total = 0;
		long successful = 0;
		CWBatchConfirmResult result = new CWBatchConfirmResult();
		if(!CollectionUtil.isEmptyOfMap(batchParam)) {
			for(Entry<Long, Long> entry : batchParam.entrySet()) {
				total++;
				long retId = entry.getKey();
				long userId = entry.getValue();
				RetArg ret = null;
				try {
					ret = cwConfirmReturn(retId, userId);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
					result.getList().add(retId);
					continue;
				}
				Boolean isSucc = RetArgUtil.get(ret, Boolean.class);
				if(null != isSucc && Boolean.TRUE == isSucc) {
					successful++;
				} else {
					result.getList().add(retId);
				}
			}
		}
		RetArgUtil.put(retArg, (total == successful) ? Boolean.TRUE : Boolean.FALSE);
		RetArgUtil.put(retArg, "total:" + total + ", successful:" + successful);
		RetArgUtil.put(retArg, result);
		return retArg;
	}

	private KFParam getKFParam() {
		KFParam param = new KFParam();
		param.setKfId(SecurityContextUtils.getUserId());
		param.setKfName(SecurityContextUtils.getUserName());
		return param;
	}
	
}
