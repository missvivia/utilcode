package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState;

/**
 * 退货商品查询信息（退货详情页）
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月23日 上午10:03:40
 *
 */
public class ReturnSkuInfoListVO {
	
	private int total;
	
	private List<RetSkuInfo> list = new ArrayList<RetSkuInfo>();

	public void fillWithReturnOrderSkuList(Map<Long, ReturnOrderSkuDTO> retOrdSkuMap) {
		if(null == retOrdSkuMap) {
			return;
		}
		for(Entry<Long, ReturnOrderSkuDTO> entry : retOrdSkuMap.entrySet()) {
			ReturnOrderSkuDTO retOrdSku = null;
			if(null == entry || null == (retOrdSku = entry.getValue())) {
				continue;
			}
			RetSkuInfo info = new RetSkuInfo();
			info.fillWithReturnOrderSku(retOrdSku);
			list.add(info);
		}
		total = list.size();
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<RetSkuInfo> getList() {
		return list;
	}

	public void setList(List<RetSkuInfo> list) {
		this.list = list;
	}

	public static class RetSkuInfo {

		private String brandName;	// 品牌名
		
		private String name; // 商品名："米白色简约箱型短款宽松外套"
		
		private String colorName;	// 颜色分类 
		
		private Map<String, String> attributes;
		
		private BigDecimal price = BigDecimal.ZERO; // 单价：12
		
		private BigDecimal pay = BigDecimal.ZERO; // 结算价：10
		
		private int number; // 数量：1
		
		private int confirmNumber; // 数量：1
		
		private String status; // 入库状态：0
		
		private String remark = "无"; // 备注："xxx"

		public void fillWithReturnOrderSku(ReturnOrderSkuDTO retOrdSku) {
			if(null == retOrdSku) {
				return;
			}
			OrderSkuDTO ordSkuDTO = retOrdSku.getOrdSkuDTO();
			SkuSPDTO skuSPDTO = (null == ordSkuDTO) ? null : ordSkuDTO.getSkuSPDTO();
			if(null != skuSPDTO) {
				this.brandName = skuSPDTO.getBrandName();
				this.name = skuSPDTO.getProductName();
				this.colorName = skuSPDTO.getColorName();
				this.attributes = skuSPDTO.getSkuSpecValueMap();
			}
			if(null != ordSkuDTO) {
				this.price = ordSkuDTO.getOriRPrice();
				if(null != this.price) {
					this.price = this.price.setScale(2, RoundingMode.HALF_UP);
				}
				this.pay = ordSkuDTO.getRprice();
				if(null != this.pay) {
					this.pay = this.pay.setScale(2, RoundingMode.HALF_UP);
				}
			}
			this.number = retOrdSku.getApplyedReturnCount();
			this.confirmNumber = retOrdSku.getConfirmCount();
			ReturnOrderSkuConfirmState state = retOrdSku.getRetOrdSkuState();
			if(null != state) {
				this.status = state.getTag();
			}
			String extInfo = retOrdSku.getConfirmInfo();
			if(null != extInfo && !("null".equals(extInfo))) {
				this.remark = extInfo;
			}
		}
		
		public String getBrandName() {
			return brandName;
		}

		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getColorName() {
			return colorName;
		}

		public void setColorName(String colorName) {
			this.colorName = colorName;
		}

		public Map<String, String> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, String> attributes) {
			this.attributes = attributes;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public BigDecimal getPay() {
			return pay;
		}

		public void setPay(BigDecimal pay) {
			this.pay = pay;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public int getConfirmNumber() {
			return confirmNumber;
		}

		public void setConfirmNumber(int confirmNumber) {
			this.confirmNumber = confirmNumber;
		}
	}
	
}
