package com.xyl.mmall.common.util;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;

/**
 * 价格计算工具
 * @author author:lhp
 *
 * @version date:2015年5月14日下午6:26:00
 */
public class PriceCalculateUtil {

	
	
	/**
	 * 根据购买数量,区间价和起批数量,取得购物价格，返回0说明不允许买
	 * @param productCount
	 * @param productPriceList
	 * @param batchNum
	 * @return
	 */
	public static BigDecimal getCartPrice(int productCount,List<ProductPriceDTO> productPriceList,int batchNum){
		if(productCount<batchNum){
			return BigDecimal.ZERO;
		}
		if(productPriceList!=null){
			for(ProductPriceDTO productPriceDTO:productPriceList){
				if(productCount>=productPriceDTO.getMinNumber()&&(productPriceDTO.getMaxNumber()==0||productCount<productPriceDTO.getMaxNumber())){
					return productPriceDTO.getPrice();
				}
			}
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 根据购物商品数量取得单价
	 * @param productCount
	 * @param productPriceList
	 * @return
	 */
	public static BigDecimal getOriRetailPrice(int productCount,List<ProductPriceDTO> productPriceList){
		if(productPriceList!=null){
			for(ProductPriceDTO productPriceDTO:productPriceList){
				if(productCount<productPriceDTO.getMinNumber()){
					return productPriceDTO.getPrice();//小于起批数按最小的价格算
				}
				if(productCount>=productPriceDTO.getMinNumber()&&(productPriceDTO.getMaxNumber()==0||productCount<productPriceDTO.getMaxNumber())){
					return productPriceDTO.getPrice();
				}
			}
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 四舍五人 ，精确到2位
	 * @param price
	 * @return
	 */
	public static BigDecimal getBigDecimalToSec(BigDecimal price){
		price.setScale(2, BigDecimal.ROUND_HALF_UP);
		return price;
		
	}
	
}
