package com.xyl.mmall.common.facade;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.oms.facade.PickSkuFacade;
import com.xyl.mmall.oms.service.PoReturnService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.InvoiceService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleBannerService;
import com.xyl.mmall.saleschedule.service.ScheduleLikeService;
import com.xyl.mmall.saleschedule.service.ScheduleMagicCubeService;
import com.xyl.mmall.saleschedule.service.SchedulePageService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 档期base facade
 * @author hzzhanghui
 *
 */
public class ScheduleBaseFacade {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	protected ScheduleService scheduleService;

	@Resource
	protected SchedulePageService pageService;

	@Resource
	protected ScheduleBannerService bannerService;

	@Resource
	protected ScheduleLikeService likeService;

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected POProductService poProductService;

	@Resource
	protected BrandService brandService;

	@Resource
	protected SkuOrderStockService skuOrderStockService;

	@Resource
	protected CartService cartService;

	@Resource
	protected PromotionService promotionService;

	@Resource
	protected CouponService couponService;

	@Resource
	protected RedPacketService redPacketService;

	@Resource
	protected PickSkuFacade pickSkuFacade;

	@Resource
	protected PoReturnService poReturnService;

	@Resource
	protected InvoiceService invoiceService;

	@Resource
	protected AgentService agentService;

	@Resource
	protected BusinessFacade businessFacade;

	@Resource
	protected BusinessService businessService;

	@Resource
	protected CODAuditService codAuditService;

	@Resource
	protected ReturnPackageQueryService retPkgQueryService;

	@Resource
	protected ScheduleMagicCubeService scheduleMagicCubeService;
	
	@Resource
	protected LocationService locationService;
	
	@Resource 
	protected WarehouseService warehouseService;
	
	protected long getSaleSiteFlag(long saleSiteCode) {
		try {
			return ProvinceCodeMapUtil.getProvinceFmtByCode(saleSiteCode);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}
	
	protected long getSaleSiteListFlag(List<Long> saleSiteCodeList) {
		try {
			return ProvinceCodeMapUtil.getProvinceFmtByCodeList(saleSiteCodeList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}
	
	protected void setSiteFlag(ScheduleCommonParamDTO paramDTO) {
		if (paramDTO.curSupplierAreaId != 0) {
			long flag = getSaleSiteFlag(paramDTO.curSupplierAreaId);
			paramDTO.saleSiteFlag = flag;
		}
		
		if (paramDTO.allowedAreaIdList != null && paramDTO.allowedAreaIdList.size() > 0) {
			long flag = getSaleSiteListFlag(paramDTO.allowedAreaIdList);
			paramDTO.allowedAreaIdListFlag = flag;
		}
	}
}
