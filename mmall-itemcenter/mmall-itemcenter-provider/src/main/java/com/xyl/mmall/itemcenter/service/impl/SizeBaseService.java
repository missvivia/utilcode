package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.itemcenter.enums.SizeType;
import com.xyl.mmall.itemcenter.intf.Size;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.SizeColumn;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeTable;

public abstract class SizeBaseService {
	private static final Logger logger = LoggerFactory.getLogger(SizeBaseService.class);

	public SizeTable getSizeTable(long templatekey, SizeType sizeType) {
		try {
			SizeTable retMap = new SizeTable();
			if (sizeType == SizeType.ORIG_SIZE) {
				logger.error("size type is not correct!");
				return null;
			} else {
				List<Size> sizeList = getSizeList(templatekey, sizeType);
				List<SizeColumnParam> header = new ArrayList<SizeColumnParam>();
				Map<String, String> valMap = new HashMap<String, String>();
				Set<Long> recordSet = new HashSet<Long>();
				if (sizeList != null && sizeList.size() > 0) {
					for (Size orginalSize : sizeList) {
						SizeColumnParam cshv = new SizeColumnParam();
						cshv.setId(orginalSize.getColumnId());
						SizeColumn col = getSizeColumn(orginalSize.getColumnId());
						cshv.setName(col.getName());
						cshv.setUnit(col.getUnit() == null ? "" : col.getUnit());
						cshv.setRequired(orginalSize.getIsRequired());
						header.add(cshv);
					}
				}
				retMap.setSizeHeader(header);
				List<SizeValue> valueList = getSizeValueList(templatekey, sizeType);
				if (valueList != null && valueList.size() > 0) {
					for (SizeValue value : valueList) {
						long recordIndex = value.getRecordIndex();
						long columnId = value.getColumnId();
						valMap.put(recordIndex + "+" + columnId, value.getValue());
						recordSet.add(recordIndex);
					}
				}
				retMap.setValueMap(valMap);
				retMap.setRecordList(new ArrayList<Long>(recordSet));
				return retMap;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public abstract List<Size> getSizeList(long templatekey, SizeType sizeType);

	public abstract List<SizeValue> getSizeValueList(long templatekey, SizeType sizeType);

	public abstract SizeColumn getSizeColumn(long colId);
}
