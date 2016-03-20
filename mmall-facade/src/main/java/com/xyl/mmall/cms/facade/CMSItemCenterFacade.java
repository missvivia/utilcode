package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.backend.vo.ItemReviewPassVO;
import com.xyl.mmall.cms.vo.ExportMaterialVO;
import com.xyl.mmall.cms.vo.ItemReviewRejectVO;
import com.xyl.mmall.cms.vo.ProductReviewSearchVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;

public interface CMSItemCenterFacade {

	public BaseJsonVO productReviewPass(ItemReviewPassVO pidList);

	public BaseJsonVO productReviewReject(ItemReviewRejectVO param);

	public BaseJsonVO skuReviewPass(ItemReviewPassVO param);

	public BaseJsonVO skuReviewReject(ItemReviewRejectVO param);

	public BaseJsonVO searchReviewSKUYouhua(ProductReviewSearchVO param);

	public BaseJsonVO searchReviewProductYouhua(ProductReviewSearchVO param);

	public List<ExportMaterialVO> getExportMaterialVO(long poId);
}