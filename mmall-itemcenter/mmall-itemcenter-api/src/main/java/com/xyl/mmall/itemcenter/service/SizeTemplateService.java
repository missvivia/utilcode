package com.xyl.mmall.itemcenter.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.SizeColumnDTO;
import com.xyl.mmall.itemcenter.dto.SizeTemplateDTO;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.CustomizedSize;
import com.xyl.mmall.itemcenter.meta.OriginalSize;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.meta.TemplateSize;
import com.xyl.mmall.itemcenter.param.BatchSizePrimaryValueParam;
import com.xyl.mmall.itemcenter.param.SizeDetailPaire;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.itemcenter.param.SizeTemplateSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTemplateSearchParam;

/**
 * 尺码服务接口
 * 
 * @author hzhuangluqian
 *
 */
public interface SizeTemplateService {

	/**
	 * 添加尺寸模板
	 * 
	 * @param sizeTemplate
	 */
	public void saveSizeTemplate(SizeTemplateSaveParam saveDTO);

	/**
	 * 根据条件查找尺寸模板列表
	 * 
	 * @param searchDTO
	 *            筛选条件DTO
	 * @return
	 */
	public BaseSearchResult<SizeTemplateDTO> searchSizeTemplate(SizeTemplateSearchParam searchDTO);

	/**
	 * 获取指定商家的某一类目下所有的尺码模板
	 * 
	 * @param categoryId
	 *            类目id
	 * @param supplierId
	 *            供应商id
	 * @return
	 */
	public List<SizeTemplate> getSizeTemplateList(long categoryId, long supplierId);

	/**
	 * 删除某个商品样本下的自定义尺码值
	 * 
	 * @param productId
	 *            商品样本id
	 * @return
	 */
	public boolean deleteCustomizeSizeValue(long productId);

	/**
	 * 获取尺码字段meta对象
	 * 
	 * @param columnId
	 *            尺码字段id
	 * @return
	 */
	public SizeColumn getSizeColumn(long columnId);

	/**
	 * 添加自定义尺码字段值
	 * 
	 * @param pid
	 *            样本商品id
	 * @param skuId
	 *            skuId
	 * @param columnId
	 *            尺码字段id
	 * @param value
	 *            尺码字段值
	 */
	public void addCustomizedSizeValue(long pid, long skuId, long columnId, String value);

	/**
	 * 获取默认模板id
	 * 
	 * @param lowCategoryId
	 *            最低类目id
	 * @return
	 */
	public long getOriginalSizeId(long lowCategoryId);

	/**
	 * 获取尺码模板meta对象
	 * 
	 * @param sizeTemplateId
	 *            尺码模板id
	 * @return
	 */
	public SizeTemplate getSizeTemplate(long sizeTemplateId);

	/**
	 * 删除尺码模板
	 * 
	 * @param sizeTemplateId
	 *            尺码模板id
	 * @return TODO
	 * @throws ItemCenterException
	 */
	public int deleteSizeTemplate(long supplierId, long sizeTemplateId) throws ItemCenterException;

	/**
	 * 获取尺码字段列表
	 * 
	 * @param templatekey
	 *            模板id
	 * @param sizeType
	 *            模板类型
	 * @return
	 */
	public List<Size> getSizeList(long templatekey, SizeType sizeType);

	public boolean deleteCustomizeSizeValueBySku(long pid, int recordIndex);

	public List<SizeValue> getSizeValueList(long templatekey, SizeType sizeType);

	public SizeTable getSizeTable(long templatekey, SizeType sizeType);
}
