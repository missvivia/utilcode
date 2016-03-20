package com.xyl.mmall.mainsite.vo.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.order.enums.OrderSkuReturnJudgement;
import com.xyl.mmall.order.enums.PackageReturnJudgement;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.meta.Coupon;


/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
public class ReturnOrderSkuInfoList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3834486502708893706L;

	private List<ReturnOrderSkuInfo> list = new ArrayList<ReturnOrderSkuInfo>();

	private int total = 0;

	public List<ReturnOrderSkuInfo> getList() {
		return list;
	}

	public void setList(List<ReturnOrderSkuInfo> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public static class ReturnOrderSkuInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8060615905842433342L;

	//  1. 商品信息
		private String id;	/** orderSkuId */
		private String thumb;	/** 商品图片 */
		private String productName;		/** 商品名称 */
		private String colorName;	/** 颜色分类 */
		private List<AttributePair> attributes = new ArrayList<AttributePair>();	/** 属性列表 */
	//  --------------------------------

	//  2. 结算信息
		private BigDecimal salePrice = BigDecimal.ZERO;	/** 单价 */
		private int quantity;	/** 数量 */
		private BigDecimal sum;		/** 结算金额 */
		private Promotion promotion = new Promotion();	/** 促销 */
		private CouponCode couponCode = new CouponCode();	/** 活动 */
	//  --------------------------------
		
	//  3. 退货审核
		private Object status;	/** 是否可退款？Reason？ */
		private String comment = "";	/** 退货理由 */
	//  --------------------------------

		/**
		 * 转换商品属性
		 * 
		 * @param snapshot
		 * @return
		 */
		private List<AttributePair> convertAttributes(SkuSPDTO snapshot) {
			List<AttributePair> attrs = new ArrayList<AttributePair>();
			if(null == snapshot) {
				return attrs;
			}
			Map<String, String> skuSpecValue = snapshot.getSkuSpecValueMap();
			if(null == skuSpecValue) {
				return null;
			}
			for(Entry<String, String> entry : skuSpecValue.entrySet()) {
				String value = entry.getKey();
				String desc = entry.getValue();
				attrs.add(new AttributePair(value, desc));
			}
			return attrs;
		}
		
		/**
		 * 填充VO
		 * @param ordSkuDTO
		 * @param prj
		 * @param osrj
		 * @param promotion
		 * @param couponOrder
		 */
		public void fillWithOrderSku(OrderSkuDTO ordSkuDTO, PackageReturnJudgement prj, 
				OrderSkuReturnJudgement osrj, PromotionDTO promotion, Coupon coupon) {
			if(null == ordSkuDTO) {
				return;
			}
			SkuSPDTO snapshot = ordSkuDTO.getSkuSPDTO();
			// 1. 商品信息
			this.id = String.valueOf(ordSkuDTO.getId());
			if(null != snapshot) {
				this.thumb = snapshot.getPicUrl();
				this.productName = snapshot.getProductName();
				this.colorName = snapshot.getColorName();
				this.attributes = convertAttributes(snapshot);
			}
			// 2. 结算信息
			this.quantity = ordSkuDTO.getTotalCount();
			this.salePrice = ordSkuDTO.getRprice();
			if(null != this.salePrice) {
				this.sum = (salePrice.multiply(new BigDecimal(quantity))).setScale(2, RoundingMode.HALF_UP);
				this.salePrice = this.salePrice.setScale(2, RoundingMode.HALF_UP);
			}
			this.promotion.fillWithPromotion(promotion);
			this.couponCode.fillWithCouponOrder(coupon);
			// 3. 退货审核
			if(null == prj) {
				this.status = PackageReturnJudgement.NULL;
				return;
			}
			if(!prj.isCanReturn()) {
				this.status = prj;
			} else {
				this.status = (null == osrj) ? OrderSkuReturnJudgement.NULL : osrj;
			}
		}
		
		/**
		 * 填充VO
		 * @param ordSkuDTO
		 * @param prj
		 * @param osrj
		 * @param promotion
		 * @param couponOrder
		 */
		public void fillWithRetOrderSku(
				ReturnOrderSkuDTO retOrdSkuDTO, 
				PackageReturnJudgement prj, 
				OrderSkuReturnJudgement osrj, 
				PromotionDTO promotion, 
				Coupon coupon
				) {
			if(null == retOrdSkuDTO) {
				return;
			}
			OrderSkuDTO ordSkuDTO = retOrdSkuDTO.getOrdSkuDTO();
			fillWithOrderSku(ordSkuDTO, prj, osrj, promotion, coupon);
			// adjust quantity&sum
			this.quantity = retOrdSkuDTO.getApplyedReturnCount();
			if(null != this.salePrice) {
				this.sum = this.salePrice.multiply(new BigDecimal(retOrdSkuDTO.getApplyedReturnCount()));
				this.sum = this.sum.setScale(2, RoundingMode.HALF_UP);
			}
		}
		
		public String getId() {
			return id;
		}

		public String getThumb() {
			return thumb;
		}

		public String getProductName() {
			return productName;
		}

		public BigDecimal getSalePrice() {
			return salePrice;
		}

		public int getQuantity() {
			return quantity;
		}

		public BigDecimal getSum() {
			return sum;
		}

		public String getColorName() {
			return colorName;
		}

		public List<AttributePair> getAttributes() {
			return attributes;
		}

		public Object getStatus() {
			return status;
		}

		public Promotion getPromotion() {
			return promotion;
		}

		public CouponCode getCouponCode() {
			return couponCode;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public void setColorName(String colorName) {
			this.colorName = colorName;
		}

		public void setAttributes(List<AttributePair> attributes) {
			this.attributes = attributes;
		}

		public void setSalePrice(BigDecimal salePrice) {
			this.salePrice = salePrice;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public void setSum(BigDecimal sum) {
			this.sum = sum;
		}

		public void setPromotion(Promotion promotion) {
			this.promotion = promotion;
		}

		public void setCouponCode(CouponCode couponCode) {
			this.couponCode = couponCode;
		}

		public void setStatus(Object status) {
			this.status = status;
		}
		
		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public static class AttributePair implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6338365731392008879L;
			private String value;
			private String desc;
			public AttributePair() {
			}
			public AttributePair(String value, String desc) {
				this.value = value;
				this.desc = desc;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getDesc() {
				return desc;
			}
			public void setDesc(String desc) {
				this.desc = desc;
			}
		}
		
		public static class Promotion implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5527689418088395786L;
			private String name;
			private String description;
			public void fillWithPromotion(PromotionDTO promotion) {
				if(null == promotion) {
					return;
				}
				name = promotion.getName();
				description = promotion.getDescription();
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
		}
		
		public static class CouponCode implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3780119679172037430L;
			private long id;
			private String couponCode;
			private String name;
			private String description;
			public void fillWithCouponOrder(Coupon coupon) {
				if(null == coupon) {
					return;
				}
				id = coupon.getId();
				couponCode = coupon.getCouponCode();
				name = coupon.getName();
				description = coupon.getDescription();
			}
			public long getId() {
				return id;
			}
			public void setId(long id) {
				this.id = id;
			}
			public String getCouponCode() {
				return couponCode;
			}
			public void setCouponCode(String couponCode) {
				this.couponCode = couponCode;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
		}
	}
	
}
