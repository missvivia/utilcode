package com.xyl.mmall.common.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.mainsite.vo.DetailPOVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Facade(value = "commonFacade")
public class ItemCenterCommonFacadeImpl extends ItemCenterCommonFacadeAbstract implements ItemCenterCommonFacade {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterCommonFacadeImpl.class);

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private CartService cartService;

	@Resource
	private SkuOrderStockService skuOrderStockService;

	@Resource
	private ProductService productService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private BrandFacade brandFacade;

	@Resource
	private BrandService brandService;

	@Resource
	private ScheduleService scheduleService;

	@Override
	public ProductFullDTO getProductDTO(long poId, long pid) {
		if (poId == 0) {
			return productService.getProductFullDTO(pid);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public ProductFullDTO getProductDTO(boolean isPo, long pid) {
		if (isPo) {
			throw new RuntimeException();
		} else {
			return productService.getProductFullDTO(pid);
		}
	}

	@Override
	public List<Size> getSizeList(long tmplateId, SizeType SizeType) {
		return sizeTemplateService.getSizeList(tmplateId, SizeType);
	}

	@Override
	public List<? extends ProductDTO> getProductListByGoodsNo(long supplierId, String goodsNo) {
		List<Product> list = productService.getProductListByGoodsNo(supplierId, goodsNo);
		List<ProductDTO> retList = new ArrayList<ProductDTO>();
		if (list != null && list.size() > 0) {
			for (Product p : list) {
				ProductDTO dto = new ProductDTO(p);
				retList.add(dto);
			}
		}
		return retList;
	}

	@Override
	public DetailPOVO getPOVO(PODTO po) {
		throw new RuntimeException();
	}

	@Override
	public DetailPOVO getPOVOPure(PODTO po) {
		throw new RuntimeException();
	}

	@Override
	public List<SizeSpecVO> getSkuStock(List<POSkuDTO> skuList) {
		throw new RuntimeException();
	}

	@Override
	public ProductFullDTO getProductDTOCache(boolean isPo, long pid) {
		throw new RuntimeException();
	}

	@Override
	public SizeTable getSizeTable(long templatekey, SizeType sizeType) {
		return sizeTemplateService.getSizeTable(templatekey, sizeType);
	}
}
