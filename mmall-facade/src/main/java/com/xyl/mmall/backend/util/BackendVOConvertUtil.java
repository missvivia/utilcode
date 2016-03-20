package com.xyl.mmall.backend.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.backend.vo.InvoiceInOrdSupplierVO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.dto.InvoiceSkuSPDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;

/**
 * @author dingmingliang
 * 
 */
public final class BackendVOConvertUtil {

	private static Logger logger = LoggerFactory.getLogger(BackendVOConvertUtil.class);

	/**
	 * InvoiceInOrdSupplierDTO->InvoiceInOrdSupplierVO
	 * 
	 * @param dto
	 * @return
	 */
	public static InvoiceInOrdSupplierVO convertToInvoiceInOrdSupplierVO(InvoiceInOrdSupplierDTO dto) {
		try {
			if (dto == null)
				return null;
			OrderExpInfoDTO expDTO = dto.getOrderExpInfoDTO();
			InvoiceInOrdDTO invoiceInOrdDTO = dto.getInvoiceInOrdDTO();

			StringBuilder goods = new StringBuilder(64);
			for (InvoiceSkuSPDTO spDTO : dto.getInvoiceSkuSPDTOList()) {
				goods.append(spDTO.getProductName()).append(";");
			}

			InvoiceInOrdSupplierVO vo = ReflectUtil.convertObj(InvoiceInOrdSupplierVO.class, dto, false);
			vo.setConsigneeName(expDTO.getConsigneeName());
			if (StringUtils.isBlank(vo.getConsigneePhone()) && StringUtils.isNotBlank(expDTO.getConsigneeMobile()))
				vo.setConsigneePhone(expDTO.getConsigneeMobile());
			if (StringUtils.isBlank(vo.getConsigneePhone()) && StringUtils.isNotBlank(expDTO.getConsigneeTel()))
				vo.setConsigneePhone(expDTO.getConsigneeTel());
			vo.setTitle(invoiceInOrdDTO.getTitle());
			vo.setFullAddress(expDTO.getProvince() + expDTO.getCity() + expDTO.getSection() + expDTO.getStreet()
					+ expDTO.getAddress());
			vo.setGoods(goods.toString());
			vo.setOrderDate(DateFormatEnum.TYPE5.getFormatDate(invoiceInOrdDTO.getOrderTime()));
			return vo;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}

	/**
	 * List(InvoiceInOrdSupplierDTO)->List(InvoiceInOrdSupplierVO)
	 * 
	 * @param dtoList
	 * @return
	 */
	public static List<InvoiceInOrdSupplierVO> convertToInvoiceInOrdSupplierVOList(List<InvoiceInOrdSupplierDTO> dtoList) {
		List<InvoiceInOrdSupplierVO> voList = new ArrayList<>();
		if (CollectionUtil.isEmptyOfCollection(dtoList))
			return voList;

		for (InvoiceInOrdSupplierDTO dto : dtoList) {
			CollectionUtil.addOfList(voList, convertToInvoiceInOrdSupplierVO(dto));
		}
		return voList;
	}
}
