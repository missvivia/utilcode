/**
 * 
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.facade.OrderQueryFacade;
import com.xyl.mmall.cms.facade.UserInfoFacade;
import com.xyl.mmall.cms.vo.ConsigneeAddressVO;
import com.xyl.mmall.cms.vo.UserInfoVO;
import com.xyl.mmall.cms.vo.UserProfileVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.UserCategory;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.service.IPService;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.facade.UserRedPacketFacade;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.dto.UserLoginInfoDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.UserSearchType;
import com.xyl.mmall.member.service.AccountService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.order.dto.CODBlacklistAddressDTO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.ConsigneeAddressService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;

/**
 * @author lihui
 *
 */
@Facade("userInfoFacade")
public class UserInfoFacadeImpl implements UserInfoFacade {

	@Resource
	private UserProfileService userProfileService;

	@Resource
	private IPService ipService;

	@Resource
	private UserRedPacketService userRedPacketService;

	@Resource
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Resource
	private UserCouponService userCouponService;

	@Resource
	private OrderQueryFacade cmsOrderQueryFacade;

	@Resource
	private ConsigneeAddressService consigneeAddressService;

	@Resource
	private CODAuditService auditService;
	
	@Resource
	private UserRedPacketFacade userRedPacketFacade;
	
	@Resource
	private OrderService orderService;

	@Autowired
	private ReturnPackageFacade returnPackageFacade;
	
	@Resource
	private AccountService accountService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#getUserList(java.lang.Integer,
	 *      java.lang.String, int, int)
	 */
	@Override
	public BaseJsonVO getUserList(Integer type, String value, int limit, int offset) {
		BaseJsonVO result = new BaseJsonVO();
		BaseJsonListResultVO listVO = new BaseJsonListResultVO();
		Map<Integer, String> searchParams = new HashMap<>();
		List<UserProfileDTO> userList = null;
		UserSearchType searchType = type == null ? UserSearchType.NULL : UserSearchType
				.getUserSearchTypeByIntValue(type);
		if (StringUtils.isNotBlank(value) && UserSearchType.NULL != searchType) {
			if (UserSearchType.CONSIGNEE_MOBILE == searchType) {
				// 先查询该收货手机号对应的收货地址的用户ID
				DDBParam ddbParam = DDBParam.genParamX(limit);
				ddbParam.setOffset(offset);
				List<ConsigneeAddressDTO> addressList = consigneeAddressService.queryUserIdByConsigneeMobile(value,
						limit, offset);
				if (CollectionUtils.isNotEmpty(addressList)) {
					List<Long> userIdList = new ArrayList<Long>();
					for (ConsigneeAddressDTO address : addressList) {
						userIdList.add(address.getUserId());
					}
					// 根据用户ID获取用户信息
					userList = userProfileService.findUserProfileByIdList(userIdList);
					listVO.setTotal(consigneeAddressService.countUserIdByConsigneeMobile(value));
				}
			} else {
				searchParams.put(type, value);
				userList = userProfileService.searchUserByParams(searchParams, limit, offset);
				listVO.setTotal(userProfileService.countUserByParams(searchParams));
			}
		} else {
			userList = userProfileService.searchUserByParams(searchParams, limit, offset);
			listVO.setTotal(userProfileService.countUserByParams(searchParams));
		}
		if (!CollectionUtils.isEmpty(userList)) {
			List<UserInfoVO> userInfoList = new ArrayList<>();
			for (UserProfileDTO userProfile : userList) {
				userInfoList.add(new UserInfoVO(userProfile));
			}
			listVO.setList(userInfoList);
		}
		result.setResult(listVO);
		result.setCode(ErrorCode.SUCCESS);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#getUserInfo(long)
	 */
	@Override
	public BaseJsonVO getUserInfo(long userId) {
		BaseJsonVO result = new BaseJsonVO();
		UserProfileDTO userProfile = userProfileService.findUserProfileById(userId);
		if (userProfile != null) {
			UserInfoVO userInfoVO = new UserInfoVO(userProfile);
			UserLoginInfoDTO userLoginInfo = userProfileService.findLastLoginInfoById(userId);
			if (userLoginInfo != null) {
				userLoginInfo.setLoginProvince(ipService.getProvince(userLoginInfo.getLoginIp()));
				userInfoVO.setUserLoginInfo(userLoginInfo);
				userInfoVO.setBlackListUser(auditService.isUserInBlackList(userId));
				userInfoVO.setRedPocketBalance(userRedPacketService.getTotalCash(userId, new PromotionLock(userId)));
			}
			result.setResult(userInfoVO);
		}
		result.setCode(ErrorCode.SUCCESS);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#updateUserBindMobileEmail(long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public BaseJsonVO updateUserBindMobileEmail(long userId, String type, String value) {
		BaseJsonVO result = new BaseJsonVO();
		UserProfileDTO userProfile = null;
		if ("mobile".equalsIgnoreCase(type)) {
			userProfile = userProfileService.upsertUserMobileAndEmail(userId, value, null);
		} else {
			userProfile = userProfileService.upsertUserMobileAndEmail(userId, null, value);
		}
		if (null != userProfile) {
			result.setCode(ErrorCode.SUCCESS);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#getUserConsigneeAddress(long)
	 */
	@Override
	public BaseJsonVO getUserConsigneeAddress(long userId) {
		BaseJsonVO result = new BaseJsonVO();
		List<ConsigneeAddressDTO> addressList = consigneeAddressFacade.listAddress(userId);
		List<CODBlacklistAddressDTO> blackAddressList = auditService.queryBlacklistAddressByUserId(userId);
		if (CollectionUtils.isNotEmpty(addressList)) {
			BaseJsonListResultVO listVO = new BaseJsonListResultVO();
			List<ConsigneeAddressVO> addressVOList = new ArrayList<>();
			for (ConsigneeAddressDTO address : addressList) {
				ConsigneeAddressVO addressVO = new ConsigneeAddressVO(address);
				if (CollectionUtils.isNotEmpty(blackAddressList)) {
					for (CODBlacklistAddressDTO blacklistAddress : blackAddressList) {
						if (blacklistAddress != null && blacklistAddress.hitBlack(address)) {
							addressVO.setBlackListAddress(true);
						}
					}
				}
				addressVOList.add(addressVO);
			}
			listVO.setList(addressVOList);
			result.setResult(listVO);
		}
		result.setCode(ErrorCode.SUCCESS);
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#getUserCoupon(long,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO getUserCoupon(long userId, int limit, int offset) {
		BaseJsonVO result = new BaseJsonVO();
		BaseJsonListResultVO listVO = new BaseJsonListResultVO();
		List<CouponDTO> couponList = userCouponService.getUserCouponListByState(userId, -1, limit, offset);
		if (!CollectionUtils.isEmpty(couponList)) {
			listVO.setList(couponList);
			listVO.setTotal(userCouponService.getUserCouponCount(userId, -1));
		}
		result.setResult(listVO);
		result.setCode(ErrorCode.SUCCESS);
		result.setMessage("successful");
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#getUserOrder(long, int,
	 *      int)
	 */
	@Override
	public BaseJsonVO getUserOrder(long userId, int limit, int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setResult(cmsOrderQueryFacade.queryByUserInfo(UserCategory.USER_ID, String.valueOf(userId), limit, offset));
		ret.setCode(ErrorCode.SUCCESS);
		ret.setMessage("successful");
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.UserInfoFacade#getUserRedPacket(long,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO getUserRedPacket(long userId, int limit, int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		List<UserRedPacketDTO> redList = userRedPacketFacade.getUserRedPacketList(userId, -1, limit, offset);
		ret.setCode(ErrorCode.SUCCESS);
		ret.setResult(redList);
		return ret;
	}

	@Override
	public List<UserProfileVO> queryUserList(BasePageParamVO<UserProfileVO> basePageParamVO, String searchValue) {
		BasePageParamVO<UserProfileDTO> pageParamVO = new BasePageParamVO<UserProfileDTO>();
		if (basePageParamVO != null) {
			pageParamVO = basePageParamVO.copy(pageParamVO);
		}
		pageParamVO = userProfileService.queryUserList(pageParamVO, searchValue);
		if (pageParamVO == null) {
			return null;
		}
		if (basePageParamVO != null) {
			basePageParamVO = pageParamVO.copy(basePageParamVO);
		}
		return convertToVO(pageParamVO.getList());
	}
	
	private List<UserProfileVO> convertToVO(List<UserProfileDTO> profileDTOList) {
		if (CollectionUtils.isNotEmpty(profileDTOList)) {
			List<UserProfileVO> retList = new ArrayList<UserProfileVO>(profileDTOList.size());
			for (UserProfileDTO profileDTO : profileDTOList) {
				retList.add(new UserProfileVO(profileDTO));
			}
			return retList;
		}
		return null;
	}

	@Override
	public UserProfileVO getUserDetailInfo(long userId) {
		UserProfileDTO userProfile = userProfileService.findUserProfileById(userId);
		if (userProfile != null) {
			UserProfileVO userProfileVO = new UserProfileVO(userProfile);
			UserLoginInfoDTO userLoginInfo = userProfileService.findLastLoginInfoById(userId);
			if (userLoginInfo != null) {
				userProfileVO.setLastLoginIp(userLoginInfo.getLoginIp());
				userProfileVO.setLastLoginAddress(ipService.getAll(userLoginInfo.getLoginIp()));
			}
			return userProfileVO;
		}
		return null;
	}

	@Override
	public List<ConsigneeAddressVO> getUserConsigneeAddressList(long userId) {
		List<ConsigneeAddressDTO> addressList = consigneeAddressFacade.listAddress(userId);
		if (CollectionUtils.isNotEmpty(addressList)) {
			List<CODBlacklistAddressDTO> blackAddressList = auditService.queryBlacklistAddressByUserId(userId);
			List<ConsigneeAddressVO> addressVOList = new ArrayList<ConsigneeAddressVO>();
			for (ConsigneeAddressDTO address : addressList) {
				ConsigneeAddressVO addressVO = new ConsigneeAddressVO(address);
				if (CollectionUtils.isNotEmpty(blackAddressList)) {
					for (CODBlacklistAddressDTO blacklistAddress : blackAddressList) {
						if (blacklistAddress != null && blacklistAddress.hitBlack(address)) {
							addressVO.setBlackListAddress(true);
						}
					}
				}
				addressVOList.add(addressVO);
			}
			return addressVOList;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getUserOrderList(long userId, DDBParam ddbParam) {
		RetArg retArg = orderService.queryAllOrderFormList(userId, true, null, ddbParam);
		List<OrderFormDTO> orderDTOList = RetArgUtil.get(retArg, ArrayList.class);
		ddbParam = RetArgUtil.get(retArg, DDBParam.class);

		List<OrderForm2VO> order2VOList = new ArrayList<OrderForm2VO>();
		if (CollectionUtil.isNotEmptyOfCollection(orderDTOList)) {
			for (OrderFormDTO orderDTO : orderDTOList) {
				OrderForm2VO order2VO = convertToOrderForm2VO(orderDTO);
				// 转换OrderFormState
				MainsiteVOConvertUtil.resetOrderFormState(order2VO);
				CollectionUtil.addOfListFilterNull(order2VOList, order2VO);
			}
		}
		JSONObject json = new JSONObject();
		json.put("list", order2VOList);
		json.put("total", ddbParam != null && ddbParam.getTotalCount() != null ? ddbParam.getTotalCount() : 0);
		return json;
	}
	
	/**
	 * 返回OrderForm2VO,并设置canApplyReturn标记
	 * 
	 * @param orderDTO
	 * @return
	 */
	private OrderForm2VO convertToOrderForm2VO(OrderFormDTO orderDTO) {
		if (orderDTO == null)
			return null;
		OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, returnPackageFacade);
		return order2VO;
	}

	@Override
	@Transaction
	public int updateUserBaseInfo(AccountDTO accountDTO, UserProfileDTO userProfile) throws Exception {
		userProfile = userProfileService.updateUserBaseInfo(userProfile);
		if (userProfile.getUserId() < 1l) {
			return -1;
		}
		if (StringUtils.isBlank(accountDTO.getPassword())) {
			return 1;
		} else {
			if (!accountService.updateAccount(accountDTO)) {
				throw new Exception("Update account error!");
			}
		}
		return 1;
	}

	@Override
	public JSONObject getUserCoupons(long userId, int state, int limit, int offset) {
		JSONObject json = new JSONObject(2);
		List<CouponDTO> list = userCouponService.getUserCouponListByState(userId, state, limit, offset);
		json.put("list", list);
		int total = 0;
		if (list != null && list.size() > 0) {
			total = userCouponService.getUserCouponCount(userId, -1);
		}
		json.put("total", total);
		return json;
	}

	@Override
	@Transaction
	public long addUser(AccountDTO accountDTO, UserProfileDTO userProfileDTO) {
		accountDTO = accountService.createAccount(accountDTO);
		if (accountDTO == null) {
			return -1l;
		}
		userProfileDTO = userProfileService.addUser(userProfileDTO);
		if (userProfileDTO.getUserId() < 1l) {
			throw new DBSupportRuntimeException("Add user profile failed!");
		}
		return userProfileDTO.getUserId();
	}

	@Override
	public long getUserIdByUserName(String userName) {
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileByUserName(userName);
		return userProfileDTO.getUserId();
	}

	@Override
	public UserProfileDTO getUserBaseInfo(long userId) {
		return userProfileService.findUserProfileById(userId);
	}
}
