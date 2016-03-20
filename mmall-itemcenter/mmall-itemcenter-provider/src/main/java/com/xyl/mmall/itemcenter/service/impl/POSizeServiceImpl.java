package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeValueDao;
import com.xyl.mmall.itemcenter.dao.size.OriginalSizeDao;
import com.xyl.mmall.itemcenter.dao.size.OriginalSizeValueDao;
import com.xyl.mmall.itemcenter.dao.size.SizeColumnDao;
import com.xyl.mmall.itemcenter.dao.size.TemplateSizeDao;
import com.xyl.mmall.itemcenter.dao.size.TemplateSizeValueDao;
import com.xyl.mmall.itemcenter.dao.sku.SkuSpecMapDao;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.service.POSizeService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;

@Service
public class POSizeServiceImpl extends SizeBaseService implements POSizeService {
	private static final Logger logger = LoggerFactory.getLogger(POSizeServiceImpl.class);

	@Autowired
	private SizeTemplateService sizeTemplateService;

	@Autowired
	OriginalSizeDao originalSizeDao;

	@Autowired
	TemplateSizeDao templateSizeDao;

	@Autowired
	TemplateSizeValueDao templateSizeValueDao;

	@Autowired
	OriginalSizeValueDao originalSizeValueDao;

	@Autowired
	CustomizedSizeDao customizedSizeDao;

	@Autowired
	CustomizedSizeValueDao customizedSizeValueDao;

	@Autowired
	SizeColumnDao sizeColumnDao;

	@Autowired
	SkuSpecMapDao skuSpecMapDao;

	@Override
	public String getSizePrimaryValue(long poId, long skuId) {
		try {
			return skuSpecMapDao.getSkuSpecMap(poId, skuId, 1).getValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xyl.mmall.itemcenter.service.impl.POSizeService#getPOSizeList
	 * (long, com.xyl.mmall.itemcenter.enums.SizeType)
	 */
	@Override
	@Cacheable(value = "prodSizeCache")
	public List<Size> getSizeList(long templatekey, SizeType sizeType) {
		try {
			List<Size> retList = new ArrayList<Size>();
			if (sizeType == SizeType.ORIG_SIZE) {
				return originalSizeDao.getSizeList(templatekey);
			} else if (sizeType == SizeType.TMPL_SIZE) {
				return templateSizeDao.getSizeList(templatekey);
			} else if (sizeType == SizeType.CUST_SIZE) {
				return customizedSizeDao.getSizeList(templatekey, ConstantsUtil.IN_PO);
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xyl.mmall.itemcenter.service.impl.POSizeService#getSizeValue
	 * (long, long, long, com.xyl.mmall.itemcenter.enums.SizeType)
	 */
	@Override
	@Cacheable(value = "prodSizeCache")
	public SizeValue getSizeValue(long templatekey, long columnId, long sizeId, SizeType sizeType) {
		try {
			if (sizeType == SizeType.ORIG_SIZE)
				throw new ServiceException("get size value failed");
			else if (sizeType == SizeType.TMPL_SIZE)
				return templateSizeValueDao.getSizeValue(templatekey, columnId, sizeId);
			else
				return customizedSizeValueDao.getSizeValue(templatekey, columnId, sizeId, ConstantsUtil.IN_PO);
		} catch (Exception e) {
			logger.error("====error get sizeValue,templatekey:" + templatekey + ",columnId:" + columnId + ",sizeId:"
					+ sizeId + ",sizeType:" + sizeType);
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public boolean deleteCustomizeSizeValue(long productId) {
		try {
			return customizedSizeValueDao.deleteCustomizedSizeValue(productId, ConstantsUtil.IN_PO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public boolean deleteCustomizeSizeValue(long productId, int sizeIndex) {
		try {
			return customizedSizeValueDao.deleteCustomizedSizeValue(productId, sizeIndex, ConstantsUtil.IN_PO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Cacheable(value = "prodSizeCache")
	public List<SizeValue> getSizeValueList(long templatekey, SizeType sizeType) {
		if (sizeType == SizeType.TMPL_SIZE)
			return templateSizeValueDao.getSizeValueList(templatekey);
		else if (sizeType == SizeType.CUST_SIZE)
			return customizedSizeValueDao.getSizeValueList(templatekey, ConstantsUtil.IN_PO);
		else
			throw new ServiceException("get size value list failed");
	}

	@Override
	public SizeColumn getSizeColumn(long colId) {
		return sizeTemplateService.getSizeColumn(colId);
	}

}
