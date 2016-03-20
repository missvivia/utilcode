/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.backend.vo.CategoryVO;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.facade.impl.MainsiteItemFacadeImpl;
import com.xyl.mmall.mainsite.vo.DetailPromotionVO;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.mainsite.vo.ProductListProductVO;
import com.xyl.mmall.mainsite.vo.ProductListSkuVO;
import com.xyl.mmall.mobile.facade.MobilePoFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.converter.MobileConfig;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.facade.vo.MobileKeyPairIconVO;
import com.xyl.mmall.mobile.facade.vo.MobileKeyPairVO;
import com.xyl.mmall.mobile.facade.vo.MobilePODetailVO;
import com.xyl.mmall.mobile.facade.vo.MobilePOGroupVO;
import com.xyl.mmall.mobile.facade.vo.MobilePOSummaryVO;
import com.xyl.mmall.mobile.facade.vo.MobilePoVO;
import com.xyl.mmall.mobile.facade.vo.MobilePrdtSummaryVO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobilePoFacadeImpl implements MobilePoFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ScheduleService scheduleService;

	@Autowired
	private MainsiteItemFacade itemFacade;

	@Autowired
	private POItemFacade poItemFacade;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Resource
	private POProductService poProductService;

	/**
	 * 通过主站的facde解析活动信息标签
	 * 
	 * @param tipList
	 * @return
	 */
	public static List<String> poSaleInfo(List<Map<String, String>> tipList) {
		List<String> list = new ArrayList<String>();
		if (tipList == null)
			return list;
		for (Map<String, String> map : tipList) {
			String key = map.get(MainsiteItemFacadeImpl.desp);
			if (key != null)
				list.add(key);
		}
		return list;
	}

	@Autowired
	private LocationService locationService;

	private long getSaleSiteFlag(long saleSiteCode) {
		List<LocationCode> provinceList = locationService.getAllProvince();
		for (LocationCode province : provinceList) {
			if (province.getCode() == saleSiteCode) {
				return province.getSiteFlag();
			}
		}

		return -1;
	}

	@Override
	public BaseJsonVO getPOList(long userId, int areaId, Integer type, Integer online, MobilePageCommonAO pager) {
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
			MobileChecker.checkNull("TYPE", type);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}
		if (online == null)
			online = 1;
		// 临时，
		if (type == -1) {
			type = MobileConfig.new_po_type;
		}

		boolean hasNext = false;
		List<MobilePOGroupVO> groupList = new ArrayList<MobilePOGroupVO>();
		try {
			long saleSiteFlag = getSaleSiteFlag(areaId);
			if (online == 0) {
				int i = 1;
				if(Converter.isBeforeTen()){
					i = 0;
				}
				for (; i <= MobileConfig.future_day; i++) {
					POListDTO dto = scheduleService.getScheduleListForFuture(type, saleSiteFlag, i,
							MobileConfig.future_number);
					int j = i;
					if(i == 0)
						j = -999;
					MobilePOGroupVO vo = fullInPoList(dto, j, false);
					if (vo.getPoList() != null && vo.getPoList().size() != 0)
						groupList.add(vo);
				}
			} else {
				// int pageNum - pager.getStartIndex()/pager.getPageSize())
				POListDTO dto = scheduleService.getScheduleListForChl(type, saleSiteFlag, 0, 0, 0);
				hasNext = false;
				MobilePOGroupVO vo = fullInPoList(dto, 0, true);
				groupList.add(vo);
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e1.getMessage());
		}

		return Converter.listBaseJsonVO(groupList, hasNext);
	}

	@Override
	public BaseJsonVO getPODetail(long userId, Long poId, Integer sort, String filter, Integer needhead,
			MobilePageCommonAO pager, int areaId,long appversion) {
		try {
			MobileChecker.checkZero("AREA CODE", areaId);
			MobileChecker.checkZero("PO ID", poId);
		} catch (ParamNullException e1) {
			logger.error(e1.getMessage());
			return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_MATCH, e1.getMessage());
		}
		if (sort == null)
			sort = 0;

		try {

			MobilePODetailVO detail_vo = new MobilePODetailVO();
			// po 信息部分
			PODTO po = scheduleService.getScheduleById(poId);

			DetailPromotionVO promotion = itemFacade.getDetailPagePromotionInfo(poId);
			MobilePoVO mobile_po = new MobilePoVO();
			long area = fullInPoSummary(mobile_po, po, 0, areaId);
			if (area == 0l) {
				return Converter.errorBaseJsonVO(MobileErrorCode.DATA_OUTOF_TIME, "PO NOT EXISTS");
			}
			long area_id = (long) areaId;
			if (area != area_id) {
				logger.error("area not match for pId :" + poId);
				mobile_po.setIsNotArea(1);
			}
			if (promotion != null)
				mobile_po.setPoInfo(poSaleInfo(promotion.getTipList()));
			mobile_po.setWebsiteURL(Converter.genWebSiteLink(Converter.SHARE_PO, poId));
			if (po != null && po.getScheduleDTO().getSchedule() != null) {
				Schedule schedule = po.getScheduleDTO().getSchedule();
				mobile_po.setBrandId(schedule.getBrandId());
				// 所有活动收藏变为品牌收藏
				if(appversion >= Converter.protocolVersion("1.2.0")){
				mobile_po.setShareTemplate(Converter.sharePoTemplate(
						Converter.discountFormat(1, schedule.getMaxDiscount()), schedule.getTitle(),
						mobile_po.getWebsiteURL(),schedule.getBrandLogo()));}
				boolean f = mainBrandFacade.getBrandCollectionState(userId, schedule.getBrandId());
				mobile_po.setFavorite(f ? 1 : 0);
			}

			detail_vo.setPo(mobile_po);

			// head 加载
			if (needhead != null && needhead == 1) {
				List<MobileKeyPairVO> filterList = new ArrayList<MobileKeyPairVO>();
				BaseJsonVO b = poItemFacade.getPoCategory(poId, true);
				if (b.getResult() instanceof BaseJsonListResultVO) {
					BaseJsonListResultVO blrvo = (BaseJsonListResultVO) b.getResult();
					for (Object dto : blrvo.getList()) {
						if (dto instanceof CategoryVO) {
							CategoryVO cvo = (CategoryVO) dto;
							MobileKeyPairVO vo = new MobileKeyPairVO();
							vo.setId(Long.parseLong(cvo.getId()));
							vo.setName(cvo.getName());
							filterList.add(vo);
						}
					}
				} else {
					logger.error("header cat not cast to baseJson");
					return Converter.errorBaseJsonVO(MobileErrorCode.DATA_NOT_MATCH,
							"header return can not cast to Json");
				}
				detail_vo.setFilterList(filterList);
			}
			// 商品列表部分
			PoProductListSearchVO param = new PoProductListSearchVO();
			if (StringUtils.isNotBlank(filter)) {
				try {
					param.setCategoryId(Long.parseLong(filter));
				} catch (Exception e) {
					logger.error(e.toString());
				}
			}
			param.setLimit(pager.getPageSize());
			param.setLastId(pager.getFromId());

			if (sort < 0) {
				sort = -sort;
				param.setDesc(false);
			}
			param.setOrder(sort);
			param.setScheduleId(poId);
			BaseJsonListResultVO blrv = itemFacade.getProductList(param);
			List<MobilePrdtSummaryVO> volist = new ArrayList<MobilePrdtSummaryVO>();
			for (Object obj : blrv.getList()) {
				if (obj instanceof ProductListProductVO) {
					// System.out.println(JsonUtils.toJson(obj));
					ProductListProductVO pvo = (ProductListProductVO) obj;
					MobilePrdtSummaryVO vo = new MobilePrdtSummaryVO();
//					vo.setIsRecommend(pvo.getIsRecommend());
					vo.setCountDownTime(mobile_po.getCountDownTime());
					vo.setDiscountDesc(Converter.calDiscountFormat(pvo.getSalePrice(), pvo.getMarketPrice()));
					vo.setEndTime(mobile_po.getEndTime());
					if (pvo.getListShowPicList() != null && pvo.getListShowPicList().size() > 0)
						vo.setImageURL(pvo.getListShowPicList().get(0));
					vo.setOrignPrice(Converter.doubleFormat(pvo.getMarketPrice()));
					vo.setPoPrice(Converter.doubleFormat(pvo.getSalePrice()));
					vo.setPrdtId(Long.parseLong(pvo.getId()));
					vo.setPrdtName(pvo.getProductName());
					if(appversion >= Converter.protocolVersion("1.2.0")){
						if(pvo.getSameAsShop() == 1)
							vo.setTag(MobileConfig.same_as_shop_tag,MobileConfig.same_as_shop_icon);
					}
					if (pvo.getSkuList() != null) {
						int count = 0;
						boolean saleout = true;
						// 这里的SKU 数量貌似不是剩余数量
						for (ProductListSkuVO sku : pvo.getSkuList()) {
							count = count + sku.getNum();
							if (sku.getState() == 1)
								saleout = false;
						}
						if (mobile_po.getPoStatus() != 2) {
							vo.setStatus(2);
						} else if (saleout) {
							count = 0;
							vo.setStatus(3);
						} else {
							vo.setInventoryCount(count);
							vo.setStatus(1);
						}
					}
					volist.add(vo);
				}
			}
			detail_vo.setHasNext(blrv.isHasNext());
			detail_vo.setList(volist);
			// TODO
			return Converter.converterBaseJsonVO(detail_vo);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
		}

	}

	@Override
	public BaseJsonVO getScheduleChannel() {

		try {
			List<ScheduleChannelDTO> scheduleChannelDTOs = scheduleService.getScheduleChannelList();
			if (scheduleChannelDTOs == null || scheduleChannelDTOs.size() == 0)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "category is null");
			List<MobileKeyPairIconVO> category = new ArrayList<MobileKeyPairIconVO>();
			for (ScheduleChannelDTO dto : scheduleChannelDTOs) {
				if (dto.getId() == MobileConfig.new_po_type) {
					continue;
				}
				if (dto.getId() != 2 && dto.getId() != 3 && dto.getId() != 4) {
					//continue;
				}
				MobileKeyPairIconVO vo = new MobileKeyPairIconVO();
				if (dto.getId() == 0) {
					logger.error("could not find scheduleChannelDTOs dto..");
					continue;
				}
				vo.setId(dto.getId());
				vo.setName(dto.getName());
				vo.setIcon(dto.getIconUrl());
				category.add(vo);
			}

			return Converter.listBaseJsonVO(category, false);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e.getMessage());
		}

	}

	/**
	 * 填充的第一层
	 * 
	 * @param dto
	 * @return
	 * @throws ParamNullException
	 */
	public MobilePOGroupVO fullInPoList(POListDTO dto, int i, boolean needSort) throws ParamNullException {
		MobileChecker.checkNull("PO LIST", dto);
		MobilePOGroupVO volist = new MobilePOGroupVO();
		if (needSort) {
			ScheduleUtil.sortPOListForMainsite(dto);
		}
		volist.setPoList(fullInPoSummary(dto.getPoList()));
		long timestamp = getDateTime(dto.getPoList());
		volist.setActiveTime(timestamp);
		volist.setActiveDesc(Converter.getPoGroupTitle(i, timestamp));

		return volist;
	}

	// 获取第一天
	private long getDateTime(List<PODTO> poList) {
		if (poList == null || poList.size() == 0)
			return 0l;
		PODTO po = poList.get(0);
		if (po != null && po.getScheduleDTO() != null && po.getScheduleDTO().getSchedule() != null) {
			return po.getScheduleDTO().getSchedule().getStartTime();
		}
		return 0l;
	}

	/**
	 * 填充 po
	 * 
	 * @param poList
	 * @return
	 */
	private List<MobilePOSummaryVO> fullInPoSummary(List<PODTO> poList) {

		List<MobilePOSummaryVO> list = new ArrayList<MobilePOSummaryVO>();
		if (poList == null)
			return list;
		// 获得POlist id
		List<Long> ids = new ArrayList<Long>();
		if (poList.size() > 200) {
			poList = poList.subList(0, 200);
		}
		for (PODTO dto : poList) {
			ids.add(dto.getScheduleDTO().getSchedule().getId());
		}
		// sameAsShop = 1 专柜同款就是新品
		List<Integer> newPrdts = null;
		try {
			newPrdts = poProductService.getProductCount(ids, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean hasCount = true;
		if (newPrdts == null || newPrdts.size() != poList.size())
			hasCount = false;

		for (int i = 0; i < poList.size(); i++) {
			PODTO dto = poList.get(i);
			MobilePOSummaryVO vo = new MobilePOSummaryVO();
			int count = 0;
			if (hasCount) {
				count = newPrdts.get(i);
			}
			MobilePoFacadeImpl.fullInPoSummary(vo, dto, count, 0);
			list.add(vo);
		}
		return list;
	}

	/**
	 * 填入POSummary转换
	 * 
	 * @param vo
	 * @param po
	 */
	public static long fullInPoSummary(MobilePOSummaryVO vo, PODTO po, int newProduct, long areaId) {
		if (po != null
				&& po.getScheduleDTO() != null
				&& po.getScheduleDTO().getSchedule() != null
				&& (po.getScheduleDTO().getSchedule().getStatus() == ScheduleState.BACKEND_PASSED || po
						.getScheduleDTO().getSchedule().getStatus() == ScheduleState.OFFLINE)) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			if (schedule.getEndTime() < Converter.getTime() || schedule.getStatus() == ScheduleState.OFFLINE) {
				vo.setPoStatus(3);
			} else if (schedule.getStartTime() > Converter.getTime()) {
				vo.setPoStatus(1);
			} else {
				vo.setPoStatus(2);
			}
			vo.setPoId(schedule.getId());
			vo.setBrandLogoURL(schedule.getBrandLogo());
			if (po.getBannerDTO() != null && po.getBannerDTO().getBanner() != null)
				vo.setPoBannerImage(po.getBannerDTO().getBanner().getHomeBannerImgUrl());
			vo.setBrandName(Converter.brandName(schedule.getBrandName(), schedule.getBrandNameEn()));
			vo.setCountDownTime(schedule.getEndTime() - Converter.getTime());
			vo.setDiscountDesc(Converter.discountFormat(0, schedule.getMinDiscount()));
			vo.setEndTime(schedule.getEndTime());
			vo.setStartDate(Converter.getPoGroupTitle(-1, schedule.getStartTime()));
			vo.setFavorite(po.getFavorite());
			vo.setNewProductCount(newProduct);
			vo.setFavoriteNum(po.getFavoriteNum());
			vo.setPoName(schedule.getTitle());
			vo.setStartTime(schedule.getStartTime());
			for (ScheduleSiteRela site : po.getScheduleDTO().getSiteRelaList()) {
				if (site.getSaleSiteId() == areaId)
					return areaId;
			}
			if (po.getScheduleDTO().getSiteRelaList().size() > 0)
				return po.getScheduleDTO().getSiteRelaList().get(0).getSaleSiteId();
			return areaId;
		}
		return 0l;
	}

	/**
	 * 检查是不是同一个分组
	 * 
	 * @param po
	 * @param i
	 * @return
	 * @throws Exception
	 */
	public static boolean checkGroup(List<PODTO> po, int i) throws Exception {
		if (i >= po.size() - 1)
			return true;
		PODTO po1 = po.get(i);
		PODTO po2 = po.get(i + 1);
		if (po1.getScheduleDTO() == null || po2.getScheduleDTO() == null) {
			throw new Exception("scheduleDto in poDto is null");
		}
		if (po1.getScheduleDTO().getSchedule().getStartTime() != po2.getScheduleDTO().getSchedule().getStartTime()) {
			if (po2.getScheduleDTO().getSchedule().getStartTime() > Converter.getTime())
				return true;
		}
		return false;
	}

	/**
	 * 生成一个POgroup
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	// TODO TEST

	public static List<MobilePOGroupVO> fullInPoGroup(List<PODTO> podto, int favorite, List<Integer> counts)
			throws Exception {
		List<MobilePOGroupVO> list_g = new ArrayList<MobilePOGroupVO>();
		MobilePOGroupVO v_g = new MobilePOGroupVO();
		List<MobilePOSummaryVO> list = new ArrayList<MobilePOSummaryVO>();
		boolean hasCount = true;
		if (counts == null || counts.size() != podto.size())
			hasCount = false;
		for (int i = 0; i < podto.size(); i++) {
			int count = 0;
			if (hasCount) {
				count = counts.get(i);
			}
			MobilePOSummaryVO mobilePoVO = new MobilePOSummaryVO();
			long code = MobilePoFacadeImpl.fullInPoSummary(mobilePoVO, podto.get(i), count, -1);
			if (code == 0l)
				continue;
			mobilePoVO.setFavorite(favorite);
			// mobilePoVO.setWebsiteURL(Converter.genWebSiteLink(1,
			// mobilePoVO.getPoId()));
			list.add(mobilePoVO);
			if (MobilePoFacadeImpl.checkGroup(podto, i)) {

				if (list.size() > 0) {
					long activeTime = list.get(0).getStartTime();
					v_g.setActiveDesc(Converter.getPoGroupTitle(1, activeTime));
					v_g.setActiveTime(activeTime);
					if (activeTime <= System.currentTimeMillis())
						v_g.setIsActive(1);
					v_g.setPoList(list);
					list_g.add(v_g);
					v_g = new MobilePOGroupVO();
					list = new ArrayList<MobilePOSummaryVO>();
				} else {
					break;
				}

			}
		}

		return list_g;
	}

}
