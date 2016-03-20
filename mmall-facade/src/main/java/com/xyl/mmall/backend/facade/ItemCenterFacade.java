package com.xyl.mmall.backend.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.backend.vo.BacthUploadProduct;
import com.xyl.mmall.backend.vo.BatchUploadPic;
import com.xyl.mmall.backend.vo.BatchUploadSize;
import com.xyl.mmall.backend.vo.ExportProductBodyVO;
import com.xyl.mmall.backend.vo.ExportProductVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.ProductParamVO;
import com.xyl.mmall.backend.vo.ProductSearchResultVO;
import com.xyl.mmall.backend.vo.ProductSearchVO;
import com.xyl.mmall.backend.vo.SizeAssistVO;
import com.xyl.mmall.backend.vo.SizeTemplateEditVO;
import com.xyl.mmall.backend.vo.SizeTmplSearchVO;
import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.backend.vo.SizeVO;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.param.BatchUploadPicParam;
import com.xyl.mmall.itemcenter.param.ChangeProductNameParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTemplateSaveParam;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;

public interface ItemCenterFacade {
	public ProductEditVO getProductVO(long productId, long supplierId);

	public BaseJsonVO saveSizeTemplate(SizeTemplateSaveParam sizeTemplate);

	public BaseJsonVO saveProduct(ProductSaveParam productSaveParam);

	public BaseJsonVO SingleSaveProduct(ProductSaveParam productSaveParam);

	public SizeVO genSizeVO(long productId, long lowCategoryId, long sizeTemplateId, long supplierId);

	public BaseJsonVO searchProduct(ProductSearchVO searchVO);

	public SizeTmplTableVO getSizeTemplateTable(long lowCategoryId, long sizeTemplateId);

	public BaseJsonVO searchSizeTemplate(SizeTmplSearchVO searchVO);

	public SizeTemplateEditVO getSizeTemplateVO(long sizeTemplateId);

	public BaseJsonVO deleteSizeTemplate(long supplierId, long sizeTemplateId) throws ItemCenterException;

	public long getSupplierId(long loginId);

	public BaseJsonVO deleteProduct(long supplierId, long pid);

	public SizeAssistVO getSizeAssistVO(long id);

	public BaseJsonVO savesizeAssist(SizeAssistVO vo);

	public BaseJsonVO deleteSizeAssist(long suppierId, long id);

	public BaseJsonVO searchSizeAssists(long supplierId, int limit, int offset);

	public Map<String, List<String>> batchUploadProductInfo(List<BacthUploadProduct> exlProducts, long supplierId);

	public Map<String, List<String>> batchSaveSkuSize(long supplierId, List<String> header,
			List<BatchUploadSize> skuSizeList);

	public BaseJsonVO deleteProducts(long supplierId, List<Long> ids);

	public Map<String, List<String>> batchSaveCustomHtml(long supplierId, Map<String, String> map);

	public Map<String, List<String>> batchSaveProductPic(long supplierId, List<BatchUploadPicParam> paramList);

	public SizeAssistVO getNewSizeAssistVO();

	public List<ExportProductBodyVO> searchExportProduct(long supplierId, List<Long> pids);

	public List<ExportProductBodyVO> searchExportProduct(ProductSearchVO searchVO);

	public Map<String, Long> getProductParamOptMap(long lowCategoryId);

	public List<Map<String, Object>> getProductParamExport(List<Long> categoryList);

	public BaseJsonVO batchChangeProductName(ChangeProductNameParam param);

	public Map<String, String> getGoodsNoAndColorNum(long supplierId, String barcode);

	public Map<String, String> getBrandName(long supplierId);

	public long getSuperCategory(long lowCategory);

	public Long getLowCategoryId(long supplierId, String goodsNo, String colorNum);

}
