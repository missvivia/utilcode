package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.ItemCenterException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.itemcenter.dao.category.CategoryDao;
import com.xyl.mmall.itemcenter.dao.category.CategoryOSizeMapDao;
import com.xyl.mmall.itemcenter.dao.product.ProductDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeValueDao;
import com.xyl.mmall.itemcenter.dao.size.OriginalSizeDao;
import com.xyl.mmall.itemcenter.dao.size.OriginalSizeValueDao;
import com.xyl.mmall.itemcenter.dao.size.SizeColumnDao;
import com.xyl.mmall.itemcenter.dao.size.SizeTemplateDao;
import com.xyl.mmall.itemcenter.dao.size.TemplateSizeDao;
import com.xyl.mmall.itemcenter.dao.size.TemplateSizeValueDao;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.dto.SizeTemplateDTO;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CustomizedSizeValue;
import com.xyl.mmall.itemcenter.meta.Product;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;
import com.xyl.mmall.itemcenter.meta.TemplateSize;
import com.xyl.mmall.itemcenter.meta.TemplateSizeValue;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeTemplateSaveParam;
import com.xyl.mmall.itemcenter.param.SizeTemplateSearchParam;
import com.xyl.mmall.itemcenter.param.SizeTmplTable;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ConstantsUtil;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;

@Service
public class SizeTemplateServiceImpl extends SizeBaseService implements SizeTemplateService {
	private static final Logger logger = LoggerFactory.getLogger(SizeTemplateServiceImpl.class);

	@Autowired
	CategoryOSizeMapDao categoryOSizeMapDao;

	@Autowired
	OriginalSizeDao originalSizeDao;

	@Autowired
	SizeColumnDao sizeColumnDao;

	@Autowired
	SizeTemplateDao sizeTemplateDao;

	@Autowired
	TemplateSizeDao templateSizeDao;

	@Autowired
	TemplateSizeValueDao templateSizeValueDao;

	@Autowired
	OriginalSizeValueDao originalSizeValueDao;

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	CustomizedSizeDao customizedSizeDao;

	@Autowired
	CustomizedSizeValueDao customizedSizeValueDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public SizeTemplate getSizeTemplate(long sizeTemplateId) {
		try {
			return sizeTemplateDao.getObjectById(sizeTemplateId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public long getOriginalSizeId(long lowCategoryId) {
		try {
			return categoryOSizeMapDao.getCategoryOSizeMap(lowCategoryId);
		} catch (Exception e) {
			logger.error("####### lowCategoryId:" + lowCategoryId + " is not exist!!");
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public void saveSizeTemplate(SizeTemplateSaveParam saveDTO) {
		try {
			SizeTemplate sizeTemplate = (SizeTemplate) ItemCenterUtil.extractData(saveDTO, SizeTemplate.class);
			sizeTemplate.setLastModifyTime(new Date().getTime());
			sizeTemplateDao.saveSizeTemplate(sizeTemplate);
			long tmpId = sizeTemplate.getId();
			SizeTmplTable table = saveDTO.getSizeTable();
			templateSizeDao.deleteTemplateSizeByTemplId(tmpId);
			List<SizeColumnParam> colList = table.getHeader();
			for (int i = 0; i < colList.size(); i++) {
				SizeColumnParam column = colList.get(i);
				if (column.getId() <= 0) {
					SizeColumn tempCol = (SizeColumn) ItemCenterUtil.extractData(column, SizeColumn.class);
					sizeColumnDao.addNewSizeColumn(tempCol);
					column.setId(tempCol.getId());
				}
				TemplateSize tmplSize = new TemplateSize();
				tmplSize.setSizeTemplateId(tmpId);
				tmplSize.setColIndex(i);
				tmplSize.setColumnId(column.getId());
				tmplSize.setIsRequired(column.isRequired());
				templateSizeDao.addNewTemplateSize(tmplSize);
			}

			templateSizeValueDao.deleteTemplateSizeValue(tmpId);
			List<List<String>> records = table.getBody();
			for (int i = 0; i < records.size(); i++) {
				List<String> values = records.get(i);
				int rowIndex = i;
				for (int j = 0; j < values.size(); j++) {
					String value = values.get(j);
					SizeColumnParam column = colList.get(j);
					TemplateSizeValue tmplSV = new TemplateSizeValue();
					tmplSV.setValue(value);
					tmplSV.setRecordIndex(rowIndex);
					tmplSV.setColumnId(column.getId());
					tmplSV.setSizeTemplateId(tmpId);
					templateSizeValueDao.addNewTemplateSizeValue(tmplSV);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public BaseSearchResult<SizeTemplateDTO> searchSizeTemplate(SizeTemplateSearchParam searchDTO) {
		try {
			List<SizeTemplateDTO> retList = new ArrayList<SizeTemplateDTO>();
			BaseSearchResult<SizeTemplate> result = sizeTemplateDao.searchSizeTemplate(searchDTO);
			List<SizeTemplate> list = result.getList();
			for (int i = 0; i < list.size(); i++) {
				SizeTemplate simple = list.get(i);
				SizeTemplateDTO dto = new SizeTemplateDTO(simple);
				long categoryId = simple.getLowCategoryId();
				Category category = categoryDao.getCategoryById(categoryId);
				dto.setLowCategoryName(category.getName());
				retList.add(dto);
			}
			BaseSearchResult<SizeTemplateDTO> ret = new BaseSearchResult<SizeTemplateDTO>();
			ret.setList(retList);
			ret.setHasNext(result.isHasNext());
			ret.setTotal(result.getTotal());
			return ret;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	@Transaction
	public int deleteSizeTemplate(long supplierId, long sizeTemplateId) throws ItemCenterException {
		try {
			List<Product> list = productDao.getListBySizeTemplateId(supplierId, sizeTemplateId);
			if (list != null && list.size() > 0)
				return ErrorCode.DELETE_FAILED.getIntValue();
			templateSizeValueDao.deleteTemplateSizeValue(sizeTemplateId);
			templateSizeDao.deleteTemplateSizeByTemplId(sizeTemplateId);
			sizeTemplateDao.deleteById(sizeTemplateId);
			return ErrorCode.SUCCESS.getIntValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ErrorCode.SERVICE_EXCEPTION.getIntValue();
		}
	}

	@Override
	public List<SizeTemplate> getSizeTemplateList(long categoryId, long supplierId) {
		try {
			return sizeTemplateDao.getSizeTemplate(categoryId, supplierId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<Size> getSizeList(long templatekey, SizeType sizeType) {
		try {
			List<Size> retList = new ArrayList<Size>();
			if (sizeType == SizeType.ORIG_SIZE) {
				return originalSizeDao.getSizeList(templatekey);
			} else if (sizeType == SizeType.TMPL_SIZE) {
				return templateSizeDao.getSizeList(templatekey);
			} else if (sizeType == SizeType.CUST_SIZE) {
				return customizedSizeDao.getSizeList(templatekey, ConstantsUtil.NOT_IN_PO);
			}
			return retList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public SizeColumn getSizeColumn(long columnId) {
		try {
			SizeColumn column = sizeColumnDao.getSizeColumn(columnId);
			return column;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public void addCustomizedSizeValue(long pid, long recordIndex, long columnId, String value) {
		try {
			CustomizedSizeValue cSizeValue = new CustomizedSizeValue();
			cSizeValue.setValue(value);
			cSizeValue.setProductId(pid);
			cSizeValue.setRecordIndex(recordIndex);
			cSizeValue.setColumnId(columnId);
			customizedSizeValueDao.addObject(cSizeValue);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public List<SizeValue> getSizeValueList(long templatekey, SizeType sizeType) {
		if (sizeType == SizeType.TMPL_SIZE)
			return templateSizeValueDao.getSizeValueList(templatekey);
		else if (sizeType == SizeType.CUST_SIZE)
			return customizedSizeValueDao.getSizeValueList(templatekey, ConstantsUtil.NOT_IN_PO);
		else
			throw new ServiceException("get size value list failed");
	}

	@Override
	public boolean deleteCustomizeSizeValue(long productId) {
		try {
			return customizedSizeValueDao.deleteCustomizedSizeValue(productId, ConstantsUtil.NOT_IN_PO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}

	@Override
	public boolean deleteCustomizeSizeValueBySku(long pid, int recordIndex) {
		try {
			return customizedSizeValueDao.deleteCustomizedSizeValue(pid, recordIndex, ConstantsUtil.NOT_IN_PO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getServiceException(e);
		}
	}
}