package com.xyl.mmall.cms.vo.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;

/**
 * CMS订单详情：发票列表
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月25日 下午2:56:49
 *
 */
public class InvoiceListVO {

	private int length = 0;
	private List<Invoice> list = new ArrayList<Invoice>();
	
	public void fillInvoiceList(List<InvoiceInOrdSupplierDTO> invoiceList, Map<Long, String> brandNames) {
		if(null == invoiceList) {
			return;
		}
		for(InvoiceInOrdSupplierDTO invoice : invoiceList) {
			if(null == invoice) {
				continue;
			}
			String brandName = null;
			if(null != brandNames) {
				brandName = brandNames.get(invoice.getSupplierId());
			}
			list.add((new Invoice()).fillInvoice(invoice, brandName));
			length++;
		}
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<Invoice> getList() {
		return list;
	}

	public void setList(List<Invoice> list) {
		this.list = list;
	}

	public static class Invoice {
		private String brandName;
		private String expressCompany;
		private String expressNO;
		public Invoice fillInvoice(InvoiceInOrdSupplierDTO invoice, String brandName) {
			if(null == invoice) {
				return this;
			}
			this.brandName = brandName;
			this.expressCompany = invoice.getExpressCompanyName();
			this.expressNO = invoice.getBarCode();
			return this;
		}
		public String getBrandName() {
			return brandName;
		}
		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}
		public String getExpressCompany() {
			return expressCompany;
		}
		public void setExpressCompany(String expressCompany) {
			this.expressCompany = expressCompany;
		}
		public String getExpressNO() {
			return expressNO;
		}
		public void setExpressNO(String expressNO) {
			this.expressNO = expressNO;
		}
	}

}
