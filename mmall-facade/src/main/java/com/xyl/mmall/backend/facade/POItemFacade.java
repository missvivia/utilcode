package com.xyl.mmall.backend.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.backend.vo.BatchUploadPOProd;
import com.xyl.mmall.backend.vo.ExportPoSkuVO;
import com.xyl.mmall.backend.vo.PoProductSearchVO;
import com.xyl.mmall.backend.vo.PoProductSortVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.param.PoAddProductReqVO;
import com.xyl.mmall.itemcenter.param.PoDeleteProdVO;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;

public interface POItemFacade {

	public BaseJsonVO searchProduct(PoProductSearchVO searchVO, long supplierId);

	public BaseJsonVO addProductToPo(PoAddProductReqVO reqVO);

	public BaseJsonVO deleteProductFromPo(PoDeleteProdVO reqVO);

	public ProductEditVO getProductVO(long poId, long productId);

	public BaseJsonVO saveProduct(ProductSaveParam productSaveParam);

	public SizeVO genSizeVO(long productId, long lowCategoryId, long sizeTemplateId, long supplierId);

	public BaseJsonVO getPoCategory(long poId, boolean isCache);

	public BaseJsonVO sortPoProduct(PoProductSortVO param);

	public BaseJsonListResultVO getProductList(PoProductListSearchVO param);

	public Map<String, List<String>> batchUploadProductInfo(List<BatchUploadPOProd> retList, long supplierId,
			long poId, int poType);

	public List<ExportPoSkuVO> getExportSkuVO(long supplierId, long poId);

	public List<PoSku> getPoSkuListByParam(PoSkuSo so);

	public ScheduleDTO getScheduleDTO(long poId);

}
