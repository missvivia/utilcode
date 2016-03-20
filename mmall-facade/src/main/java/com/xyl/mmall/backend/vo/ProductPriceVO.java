/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;

/**
 * ProductPriceVO.java created by yydx811 at 2015年5月15日 下午4:38:00
 * 商品价格区间vo
 *
 * @author yydx811
 */
public class ProductPriceVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 2925782172713690586L;

	/** 价格id. */
	private long prodPriceId;

	/** 产品id. */
	private long productId;

	/** 最小数目. */
	private int prodMinNumber;

	/** 最大数目. */
	private int prodMaxNumber;

	/** 价格. */
	private BigDecimal prodPrice;
	
	public ProductPriceVO() {
	}

	public ProductPriceVO(ProductPriceDTO obj) {
		this.prodPriceId = obj.getId();
		this.productId = obj.getProductId();
		this.prodMinNumber = obj.getMinNumber();
		this.prodMaxNumber = obj.getMaxNumber();
		this.prodPrice = obj.getPrice();
	}
	
	public ProductPriceDTO converToDTO() {
		ProductPriceDTO priceDTO = new ProductPriceDTO();
		priceDTO.setId(prodPriceId);
		priceDTO.setProductId(productId);
		priceDTO.setMinNumber(prodMinNumber);
		priceDTO.setMaxNumber(prodMaxNumber);
		priceDTO.setPrice(prodPrice);
		return priceDTO;
	}
	
	public long getProdPriceId() {
		return prodPriceId;
	}

	public void setProdPriceId(long prodPriceId) {
		this.prodPriceId = prodPriceId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getProdMinNumber() {
		return prodMinNumber;
	}

	public void setProdMinNumber(int prodMinNumber) {
		this.prodMinNumber = prodMinNumber;
	}

	public int getProdMaxNumber() {
		return prodMaxNumber;
	}

	public void setProdMaxNumber(int prodMaxNumber) {
		this.prodMaxNumber = prodMaxNumber;
	}

	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}
}
