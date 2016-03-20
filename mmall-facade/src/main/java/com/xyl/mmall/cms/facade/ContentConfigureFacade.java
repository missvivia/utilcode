package com.xyl.mmall.cms.facade;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;

import com.xyl.mmall.framework.poi.IllegalConfigException;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.mainsite.vo.MainsiteCategoryContentVO;

/**
 * mainsite首页内容配置Facade
 * 
 * @author author:lhp
 *
 * @version date:2015年10月12日下午2:54:38
 */
public interface ContentConfigureFacade {

	/**
	 * 从上传的excel读取首页数据
	 * 
	 * @param sheet
	 * @return
	 */
	public Map<Integer, String> getMainsiteIndexDataFromSheet(Sheet sheet);

	/**
	 * 从上传的web类目excel读取数据
	 * 
	 * @param sheet
	 * @return
	 */
	public List<MainsiteCategoryContentVO> getWebMainsiteCategoryDataFromSheet(Sheet sheet);
	
	/**
	 * 根据skuId获取商品详细信息
	 * @param skuIds
	 * @return
	 */
	List<ProductSKUDTO> getSKUBy(List<Long> skuIds);
	
	/**
	 * 验证指定的skuId在数据库中是否存在。
	 * @param skuIds
	 * @return 数据库中不存在的skuId
	 * @throws IllegalConfigException
	 */
	Set<Long>  validateSkuExists(Collection<Long> skuIds);

}
