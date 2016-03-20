package com.xyl.mmall.common.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.backend.vo.ProductParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.dto.CategoryVO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;
import com.xyl.mmall.saleschedule.dto.PODTO;

public interface ItemCenterCommonFacade {

	public DetailProductVO transferDetailProductVO(ProductSaveParam productSave);

	public DetailProductVO getDetailPageProduct(long pid, long poId);

	public Map<Long, Integer> getOrderSkuStock(List<Long> skuIdList);

	public List<DetailColorVO> getDetailPageColorList(long supplierId, String goodsNo);

	public DetailProductVO getDetailPageProduct(long pid, boolean isPo, boolean isCache);

	public CategoryArchitect genCategoryArchitect(Category category);

	public List<CategoryArchitect> getCategoryArchitect();

	public void getStock(List<SizeSpecVO> specList);

	public List<ProductParamDTO> getProductParamList(long categoryId, String paramValue);

	public List<ProductParamVO> getProductParamVOList(long categoryId, String paramValue);

	public void operaProductSaveParam(ProductSaveParam productSaveParam);

	public int getProductType(PODTO po, List<POSkuDTO> skuList);

	public List<Size> getSizeList(long tmplateId, SizeType SizeType);

	public DetailProductVO getDetailPageProducForAPP(long pid);

	public DetailProductVO loadData(ProductFullDTO dto);

	public SizeTable getSizeTable(long templatekey, SizeType sizeType);
	
	/**
	 * 分类列表
	 * @return
	 */
	public List<CategoryVO> getCategoryList();
}
