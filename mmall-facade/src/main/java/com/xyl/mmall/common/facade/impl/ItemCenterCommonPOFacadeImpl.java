package com.xyl.mmall.common.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cms.facade.util.POBaseUtil;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.POSizeService;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailPOVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Facade(value = "poCommonFacade")
public class ItemCenterCommonPOFacadeImpl extends ItemCenterCommonFacadeAbstract implements ItemCenterCommonFacade {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterCommonPOFacadeImpl.class);

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private CartService cartService;

	@Resource
	private POSizeService poSizeService;

	@Resource
	private POProductService poProductService;

	@Override
	public List<Size> getSizeList(long tmplateId, SizeType SizeType) {
		return poSizeService.getSizeList(tmplateId, SizeType);
	}

	@Override
	public List<? extends ProductDTO> getProductListByGoodsNo(long supplierId, String goodsNo) {
		List<PoProduct> list = poProductService.getProductListByGoodsNo(supplierId, goodsNo);
		List<POProductDTO> retList = new ArrayList<POProductDTO>();
		if (list != null && list.size() > 0) {
			for (PoProduct p : list) {
				POProductDTO dto = new POProductDTO(p);
				retList.add(dto);
			}
		}
		return retList;
	}

	@Override
	public DetailPOVO getPOVO(PODTO po) {
		// TODO
		// POBaseUtil.calPOQrqmEndTime(po.getScheduleDTO().getSchedule());
		return getPOVOPure(po);
	}

	@Override
	public DetailPOVO getPOVOPure(PODTO po) {
		long poId = po.getScheduleDTO().getSchedule().getId();
		long poEndTime = po.getScheduleDTO().getSchedule().getEndTime();
		long poStartTime = po.getScheduleDTO().getSchedule().getStartTime();
		DetailPOVO poVO = new DetailPOVO();
		poVO.setId(poId);
		poVO.setPoCountDownTime(getCountDownTime(poEndTime));
		poVO.setStartTime(poStartTime);
		poVO.setTitle(po.getScheduleDTO().getSchedule().getTitle());
		long[] areas = new long[po.getScheduleDTO().getSiteRelaList().size()];
		for (int i = 0; i < po.getScheduleDTO().getSiteRelaList().size(); i++)
			areas[i] = po.getScheduleDTO().getSiteRelaList().get(i).getSaleSiteId();
		poVO.setAreaCode(areas);
		return poVO;
	}

	private long getCountDownTime(long endTime) {
		long now = new Date().getTime();
		return endTime - now;
	}

	public List<SizeSpecVO> getSkuStock(List<POSkuDTO> skuList) {
		List<SizeSpecVO> specList = new ArrayList<SizeSpecVO>();
		List<Long> skuIdList = ItemCenterUtils.getSkuList(skuList);
		Map<Long, Integer> cartStock = null;
		Map<Long, Integer> orderStock = null;
		try {
			cartStock = cartService.getInventoryCount(skuIdList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			cartStock = new HashMap<Long, Integer>();
		}
		try {
			orderStock = getOrderSkuStock(skuIdList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			orderStock = new HashMap<Long, Integer>();
		}
		int j = 0;
		for (POSkuDTO sku : skuList) {
			SizeSpecVO sizeSpec = new SizeSpecVO();
			long skuId = sku.getId();

			sizeSpec.setSkuId(String.valueOf(skuId));

			Integer tmpCartStock = cartStock.get(skuId);
			if (tmpCartStock == null) {
				tmpCartStock = 0;
			}
			sizeSpec.setNum(tmpCartStock);
			sizeSpec.setTotal(sku.getSkuNum() + sku.getSupplierSkuNum());
			Integer tmpOrderStock = orderStock.get(skuId);
			if (tmpOrderStock == null) {
				tmpOrderStock = 0;
			}
			sizeSpec.setStock(tmpOrderStock);
			sizeSpec.setType(ItemCenterUtils.getStockType(tmpCartStock, tmpOrderStock));
			specList.add(sizeSpec);
			j++;
		}
		return specList;
	}

	@Override
	public ProductFullDTO getProductDTO(long poId, long pid) {
		if (poId > 0) {
			return poProductService.getProductFullDTO(pid);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public ProductFullDTO getProductDTO(boolean isPo, long pid) {
		if (isPo) {
			return poProductService.getProductFullDTO(pid);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public ProductFullDTO getProductDTOCache(boolean isPo, long pid) {
		if (isPo) {
			return poProductService.getProductFullDTOForMainSite(pid);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public SizeTable getSizeTable(long templatekey, SizeType sizeType) {
		return poSizeService.getSizeTable(templatekey, sizeType);
	}

}
