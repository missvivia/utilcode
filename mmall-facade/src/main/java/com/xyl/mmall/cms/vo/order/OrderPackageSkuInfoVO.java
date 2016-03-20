package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;

/**
 * CMS订单详情：商品信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月20日 上午9:09:04
 *
 */
public class OrderPackageSkuInfoVO {

	private int total;
	
	private PackageSkuInfoList list = new PackageSkuInfoList();
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public PackageSkuInfoList getList() {
		return list;
	}

	public void setList(PackageSkuInfoList lsit) {
		this.list = lsit;
	}

	public void fillWithOrderFormDTO(OrderFormDTO ordDTO, Map<Long, POProductDTO> products) {
		if(null == ordDTO) {
			return;
		}
		list.fillWithOrderFormDTO(ordDTO, products);
		total = (null == list.list) ? 0 : list.list.size();
	}
	
	public static class PackageSkuInfoList {
		private int number = 0;
		private BigDecimal price = BigDecimal.ZERO;
		private BigDecimal pay = BigDecimal.ZERO;
		private BigDecimal discount = BigDecimal.ZERO;
		private List<PackageSkuInfo> list = new ArrayList<PackageSkuInfo>();
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
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
		public BigDecimal getDiscount() {
			return discount;
		}
		public void setDiscount(BigDecimal discount) {
			this.discount = discount;
		}
		public List<PackageSkuInfo> getList() {
			return list;
		}
		public void setList(List<PackageSkuInfo> list) {
			this.list = list;
		}
		public void fillWithOrderFormDTO(OrderFormDTO ordDTO, Map<Long, POProductDTO> products) {
			if(null == ordDTO) {
				return;
			}
			this.number = ordDTO.getSkuCount();
			this.price = ordDTO.getCartOriRPrice();
			if(null != price) {
				price = price.setScale(2, RoundingMode.HALF_UP);
			}
			this.pay = ordDTO.getCartRPrice();
			if(null != pay) {
				pay = pay.setScale(2, RoundingMode.HALF_UP);
			}
			double priceDouble = (null == price) ? 0 : price.doubleValue();
			double payDouble = (null == pay) ? 0 : pay.doubleValue();
			double discountDouble = (priceDouble >= payDouble) ? (priceDouble - payDouble) : 0;
			this.discount = (new BigDecimal(discountDouble)).setScale(2, RoundingMode.HALF_UP);
			
			Map<Long, OrderSkuDTO> allOrdSku = ordDTO.mapOrderSkusByTheirId();
			if(null == allOrdSku) {
				return;
			}
			int pkgSeq = 1;
			Map<Long, Integer> pkgSeqMap = new HashMap<Long, Integer>();
			for(Entry<Long, OrderSkuDTO> entry : allOrdSku.entrySet()) {
				OrderSkuDTO skuDTO = entry.getValue();
				long pkgId = skuDTO.getPackageId();
				if(!pkgSeqMap.containsKey(pkgId)){
					pkgSeqMap.put(pkgId, pkgSeq++);
				}
				POProductDTO p = null;
				if(null != products) {
					p = products.get(skuDTO.getProductId());
				}
				PackageSkuInfo pkgSkuInfo = new PackageSkuInfo();
				pkgSkuInfo.fillWithOrderSkuDTO(skuDTO, pkgSeqMap.get(pkgId), p);
				this.list.add(pkgSkuInfo);
			}
			Collections.sort(list);
		}
	}
	
	public static class PackageSkuInfo implements Comparable<PackageSkuInfo> {
		private String packageId; // 2,
		private String pack; // "包裹1",
		private String brandName;
		private String name; // "米白色简约箱型短款宽松外套",
		private String colorName;
		private Map<String, String> attributes;
		private long brandId;	// 商家ID
		private long sku; // "YE5P-Y6R8",
		private int prodNum; // 1,
		private long productId;
		private String number; // "货号"
		private BigDecimal price = BigDecimal.ZERO; // 10.00,
		private BigDecimal pay = BigDecimal.ZERO; // 10.00,
		private boolean isGift; // true
		public void fillWithOrderSkuDTO(OrderSkuDTO ordSku, int pkgSeq, POProductDTO product) {
			if(null == ordSku) {
				return;
			}
			packageId = String.valueOf(ordSku.getPackageId()); // 2,
			pack = "包裹" + pkgSeq; // "包裹1",
			SkuSPDTO sspDTO = ordSku.getSkuSPDTO();
			if(null != sspDTO) {
				brandName = sspDTO.getBrandName();
				name = sspDTO.getProductName(); // "米白色简约箱型短款宽松外套",
				colorName = sspDTO.getColorName();
				attributes = sspDTO.getSkuSpecValueMap();
			}
			brandId = ordSku.getSupplierId();
			sku = ordSku.getSkuId(); // "YE5P-Y6R8",
			prodNum = ordSku.getTotalCount(); // 1,
			if(null != product) {
				productId = product.getId();
				number = product.getGoodsNo(); // "货号"
			}
			price = ordSku.getOriRPrice(); // 10.00,
			if(null != price) {
				price = price.setScale(2, RoundingMode.HALF_UP);
			}
			pay = ordSku.getRprice(); // 10.00,
			if(null != pay) {
				pay = pay.setScale(2, RoundingMode.HALF_UP);
			}
			isGift = ordSku.isGift(); // true
		}
		public String getPackageId() {
			return packageId;
		}
		public void setPackageId(String packageId) {
			this.packageId = packageId;
		}
		public String getPack() {
			return pack;
		}
		public void setPack(String pack) {
			this.pack = pack;
		}
		public String getBrandName() {
			return brandName;
		}
		public void setBrandName(String brandName) {
			this.brandName = brandName;
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
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public long getSku() {
			return sku;
		}
		public void setSku(long sku) {
			this.sku = sku;
		}
		public int getProdNum() {
			return prodNum;
		}
		public void setProdNum(int prodNum) {
			this.prodNum = prodNum;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
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
		public boolean isGift() {
			return isGift;
		}
		public void setGift(boolean isGift) {
			this.isGift = isGift;
		}
		@Override
		public int compareTo(PackageSkuInfo o) {
			if(null == o) {
				return 1;
			}
			if(null != packageId && packageId.compareTo(o.packageId) < 0) {
				return -1;
			}
			if(null != packageId && packageId.compareTo(o.packageId) > 0) {
				return 1;
			}
			return pack.compareTo(o.pack);
		}
		public long getProductId() {
			return productId;
		}
		public void setProductId(long productId) {
			this.productId = productId;
		}
		public long getBrandId() {
			return brandId;
		}
		public void setBrandId(long brandId) {
			this.brandId = brandId;
		}
	}
}
