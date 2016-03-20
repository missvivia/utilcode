package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.service.ReturnPackageUpdateService;

/**
 * 订单管理：退货退款管理 (客服)
 * -- 退货单查询信息
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月23日 下午1:44:21
 *
 */
public class ReturnPackageBriefInfoListVO {

	private int total;
	
	private List<ReturnPackageBriefInfo> list = new ArrayList<ReturnPackageBriefInfo>();
	
	public void fillWithReturnPackageList(
			List<ReturnPackageDTO> retPkgList, 
			Map<Long, String> saleAreas, 
			Map<Long, Long> earliestPOEndTimes, 
			boolean fromCWPage
		) {
		if(null == retPkgList) {
			return;
		}
		for(ReturnPackageDTO retPkg : retPkgList) {
			if(null == retPkg) {
				continue;
			}
			String saleArea = null == saleAreas ? "" : saleAreas.get(retPkg.getRetPkgId());
			Long earliestPOEndTime = null == earliestPOEndTimes ? 0 : earliestPOEndTimes.get(retPkg.getRetPkgId());
			ReturnPackageBriefInfo retPkgInfo = new ReturnPackageBriefInfo();
			retPkgInfo.fillWithReturnPackage(retPkg, saleArea, null == earliestPOEndTime ? 0 : earliestPOEndTime.longValue(), fromCWPage);
			list.add(retPkgInfo);
		}
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int length) {
		this.total = length;
	}

	public List<ReturnPackageBriefInfo> getList() {
		return list;
	}

	public void setList(List<ReturnPackageBriefInfo> list) {
		this.list = list;
	}

	public static class ReturnPackageBriefInfo {
		
		private String saleArea;
		private String retId; // 退货记录Id
		private String orderId;	// 订单Id
		private String ordPkgId;	// 原包裹Id
		private String userId;	// 用户Id
		private long requestTime; // 申请时间
		private long dealTime; // 最后处理
		private List<ReturnProduct> products = new ArrayList<ReturnProduct>();	// 商品信息
		private BigDecimal pay = BigDecimal.ZERO; // 实付金额
		private BigDecimal adjustPay = BigDecimal.ZERO; // 实付金额调整：针对异常件情形
		private BigDecimal expressPay = BigDecimal.ZERO; // 运费
		private BigDecimal returnPrice = BigDecimal.ZERO; // 应退款金额
		private String reason; // 申请原因
		private String expressCompany; //退货物流公司
		private String expressNum; //退货物流单号
		private List<ReturnMethod> returnMethod = new ArrayList<ReturnMethod>();	// ugly: 客服退款页面"实退金额及方式"/财务退款页面"应退款金额"
		private String dealer; // 处理人
		private ReturnPackageState status; // 状态
		private String optionExplain; // 操作说明
		private boolean canPass = false;
		private boolean canRefuse = false;
		private boolean canCancelRefuse = false;
		private boolean canDeprecateReturn = false;
		private boolean canCWConfirmReturn = false;
		private String cwDealer; // 财务操作人
		private String bankCard;
				
		public void fillWithReturnPackage(ReturnPackageDTO retPkgDTO, 
				String saleArea, long earliestPOEndTime, boolean fromCWPage) {
			if(null == retPkgDTO) {
				return;
			}
			this.saleArea = saleArea;
			this.retId = String.valueOf(retPkgDTO.getRetPkgId());
			this.orderId = String.valueOf(retPkgDTO.getOrderId());
			this.ordPkgId = String.valueOf(retPkgDTO.getOrderPkgId());
			this.userId = String.valueOf(retPkgDTO.getUserId());
			this.requestTime = retPkgDTO.getCtime();
			long confirmTime = retPkgDTO.getConfirmTime();
			long returnTime = retPkgDTO.getReturnOperationTime();
			this.dealTime = (returnTime > confirmTime) ? returnTime : confirmTime;
			fillProductsWithRetOrdSkuList(retPkgDTO.getRetOrdSkuMap());
			this.pay = retPkgDTO.getApplyedReturnTotalPrice();
			if(null != this.pay) {
				this.pay = this.pay.setScale(2, RoundingMode.HALF_UP);
			}
			OrderFormBriefDTO ordDTO = retPkgDTO.getOrdFormBriefDTO();
			if(null != ordDTO) {
				this.expressPay = ordDTO.getExpUserPrice();
				if(null != this.expressPay) {
					this.expressPay = this.expressPay.setScale(2, RoundingMode.HALF_UP);
				}
			}
			this.adjustPay = retPkgDTO.getPayedTotalPriceToUser();
			if(null != this.adjustPay) {
				this.adjustPay = this.adjustPay.setScale(2, RoundingMode.HALF_UP);
			}
			this.returnPrice = retPkgDTO.getPayedTotalPriceToUser();
			if(null != this.returnPrice) {
				this.returnPrice = this.returnPrice.setScale(2, RoundingMode.HALF_UP);
			}
			this.reason = "to be removed";
			ExpressCompany ec = retPkgDTO.getExpressCompany();
			if(null != ec) {
				this.expressCompany = ec.getName();
			}
			this.expressNum = retPkgDTO.getMailNO();
			fillReturnMethod(retPkgDTO, fromCWPage);
			// ugly code: start
			String names = retPkgDTO.getKfName();
			if(null != names) {
				String[] namesArray = names.split(";");
				if(null != namesArray && namesArray.length > 0) {
					this.dealer = namesArray[0];
					if(namesArray.length > 1) {
						this.cwDealer = namesArray[namesArray.length - 1];
					}
				}
			}
			// ugly code: end
			this.status = retPkgDTO.getReturnState();
			String extInfo = retPkgDTO.getExtInfo();
			if(null != extInfo && !("null".equals(extInfo))) {
				this.optionExplain = extInfo;
			}
			this.canPass = canPassJudge(retPkgDTO);
			this.canRefuse = canRefuseJudge(retPkgDTO);
			this.canCancelRefuse = canCancelReufseJudge(retPkgDTO);
			this.canDeprecateReturn = canDeprecateReturn(retPkgDTO, earliestPOEndTime);
			this.canCWConfirmReturn = (ReturnPackageState.CW_WAITING_RETURN == retPkgDTO.getReturnState());
			if(null != retPkgDTO.getBankCard()) {
				this.bankCard = retPkgDTO.getBankCard().mergeGeneralInfo();
			}
		}
		
		private boolean canPassJudge(ReturnPackageDTO retPkgDTO) {
			if(null == retPkgDTO) {
				return false;
			}
			ReturnPackageState[] legalState = new ReturnPackageState[] {
					ReturnPackageState.ABNORMAL_WAITING_AUDIT, 
					ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT
			};
			return CollectionUtil.isInArray(legalState, retPkgDTO.getReturnState());
		}
		
		private boolean canRefuseJudge(ReturnPackageDTO retPkgDTO) {
			if(null == retPkgDTO) {
				return false;
			}
			ReturnPackageState[] legalState = new ReturnPackageState[] {
					ReturnPackageState.ABNORMAL_WAITING_AUDIT, 
					ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT
			};
			return CollectionUtil.isInArray(legalState, retPkgDTO.getReturnState());
		}
		
		private boolean canCancelReufseJudge(ReturnPackageDTO retPkgDTO) {
			if(null == retPkgDTO) {
				return false;
			}
			ReturnPackageState[] legalState = new ReturnPackageState[] {
					ReturnPackageState.ABNORMAL_REFUSED, 
					ReturnPackageState.ABNORMAL_COD_REFUSED
			};
			return CollectionUtil.isInArray(legalState, retPkgDTO.getReturnState());
		}
		
		private boolean canDeprecateReturn(ReturnPackageDTO retPkgDTO, long earliestPOEndTime) {
			if(null == retPkgDTO) {
				return false;
			}
			ReturnPackageState[] legalState = new ReturnPackageState[] {
					ReturnPackageState.ABNORMAL_REFUSED, 
					ReturnPackageState.ABNORMAL_COD_REFUSED
			};
			if(!CollectionUtil.isInArray(legalState, retPkgDTO.getReturnState())) {
				return false;
			}
			long lastMoment = System.currentTimeMillis() + ReturnPackageUpdateService.ONE_DAY;
			return lastMoment <= earliestPOEndTime;
		}
		
		private void fillProductsWithRetOrdSkuList(Map<Long, ReturnOrderSkuDTO> retOrdSkuMap) {
			if(null == retOrdSkuMap) {
				return;
			}
			for(Entry<Long, ReturnOrderSkuDTO> entry : retOrdSkuMap.entrySet()) {
				ReturnOrderSkuDTO retOrdSku = null;
				if(null == entry || null == (retOrdSku = entry.getValue())) {
					continue;
				}
				ReturnProduct product = new ReturnProduct();
				product.fillWithReturnOrderSku(retOrdSku);
				this.products.add(product);
			}
		}
		
		private void fillReturnMethod(ReturnPackageDTO retPkgDTO, boolean fromCWPage) {
			if(null == retPkgDTO) {
				return;
			}
			BigDecimal totalPrice = BigDecimal.ZERO;
			BigDecimal cashPrice = BigDecimal.ZERO;
			BigDecimal hbPrice = BigDecimal.ZERO;
			// ugly code: start
			// 万恶之源：客服退款和财务退款共用了一个VO
			if(fromCWPage || CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), retPkgDTO.getReturnState())) {
				totalPrice = retPkgDTO.getPayedTotalPriceToUser();
				cashPrice = retPkgDTO.getPayedCashPriceToUser();
				hbPrice = retPkgDTO.getPayedHbPriceToUser();
			}
			// ugly code: end
			ReturnMethod total = new ReturnMethod("总额", totalPrice, null);
			ReturnMethod hb = new ReturnMethod("红包", hbPrice, null);
			RefundType rt = retPkgDTO.getRefundType();
			String t = (null == rt) ? "" : rt.getDesc();
			String bankCardInfo = "";
			OrderFormBriefDTO ordDTO = retPkgDTO.getOrdFormBriefDTO();
			boolean isCOD = null != ordDTO && OrderFormPayMethod.COD == ordDTO.getOrderFormPayMethod();
			if(isCOD) {
				ReturnCODBankCardInfoDTO bankCard = retPkgDTO.getBankCard();
				if(null != bankCard) {
					bankCardInfo = bankCard.mergeGeneralInfo();
				}
			}
			ReturnMethod refund = new ReturnMethod(t, cashPrice, bankCardInfo);
			this.returnMethod.add(total);
			this.returnMethod.add(hb);
			this.returnMethod.add(refund);
		}
		
		public String getRetId() {
			return retId;
		}

		public void setRetId(String retId) {
			this.retId = retId;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public long getRequestTime() {
			return requestTime;
		}

		public void setRequestTime(long requestTime) {
			this.requestTime = requestTime;
		}

		public long getDealTime() {
			return dealTime;
		}

		public void setDealTime(long dealTime) {
			this.dealTime = dealTime;
		}

		public List<ReturnProduct> getProducts() {
			return products;
		}

		public void setProducts(List<ReturnProduct> products) {
			this.products = products;
		}

		public BigDecimal getPay() {
			return pay;
		}

		public void setPay(BigDecimal pay) {
			this.pay = pay;
		}

		public BigDecimal getAdjustPay() {
			return adjustPay;
		}

		public void setAdjustPay(BigDecimal adjustPay) {
			this.adjustPay = adjustPay;
		}

		public BigDecimal getExpressPay() {
			return expressPay;
		}

		public void setExpressPay(BigDecimal expressPay) {
			this.expressPay = expressPay;
		}

		public BigDecimal getReturnPrice() {
			return returnPrice;
		}

		public void setReturnPrice(BigDecimal returnPrice) {
			this.returnPrice = returnPrice;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getExpressCompany() {
			return expressCompany;
		}

		public void setExpressCompany(String expressCompany) {
			this.expressCompany = expressCompany;
		}

		public String getExpressNum() {
			return expressNum;
		}

		public void setExpressNum(String expressNum) {
			this.expressNum = expressNum;
		}

		public List<ReturnMethod> getReturnMethod() {
			return returnMethod;
		}

		public void setReturnMethod(List<ReturnMethod> returnMethod) {
			this.returnMethod = returnMethod;
		}

		public String getDealer() {
			return dealer;
		}

		public void setDealer(String dealer) {
			this.dealer = dealer;
		}

		public ReturnPackageState getStatus() {
			return status;
		}

		public void setStatus(ReturnPackageState status) {
			this.status = status;
		}

		public String getOptionExplain() {
			return optionExplain;
		}

		public void setOptionExplain(String optionExplain) {
			this.optionExplain = optionExplain;
		}

		public String getOrdPkgId() {
			return ordPkgId;
		}

		public void setOrdPkgId(String ordPkgId) {
			this.ordPkgId = ordPkgId;
		}

		public boolean isCanPass() {
			return canPass;
		}

		public void setCanPass(boolean canPass) {
			this.canPass = canPass;
		}

		public boolean isCanRefuse() {
			return canRefuse;
		}

		public void setCanRefuse(boolean canRefuse) {
			this.canRefuse = canRefuse;
		}

		public boolean isCanCancelRefuse() {
			return canCancelRefuse;
		}

		public void setCanCancelRefuse(boolean canCancelRefuse) {
			this.canCancelRefuse = canCancelRefuse;
		}

		public String getSaleArea() {
			return saleArea;
		}

		public void setSaleArea(String saleArea) {
			this.saleArea = saleArea;
		}

		public boolean isCanCWConfirmReturn() {
			return canCWConfirmReturn;
		}

		public void setCanCWConfirmReturn(boolean canCWConfirmReturn) {
			this.canCWConfirmReturn = canCWConfirmReturn;
		}

		public String getCwDealer() {
			return cwDealer;
		}

		public void setCwDealer(String cwDealer) {
			this.cwDealer = cwDealer;
		}

		public String getBankCard() {
			return bankCard;
		}

		public void setBankCard(String bankCard) {
			this.bankCard = bankCard;
		}

		public boolean isCanDeprecateReturn() {
			return canDeprecateReturn;
		}

		public void setCanDeprecateReturn(boolean canDeprecateReturn) {
			this.canDeprecateReturn = canDeprecateReturn;
		}

		public static class ReturnProduct {
			private ProductInfo product = new ProductInfo();
			private List<ProductInfo> gifts = new ArrayList<ProductInfo>();
			public void fillWithReturnOrderSku(ReturnOrderSkuDTO retOrdSku) {
				if(null == retOrdSku) {
					return;
				}
				product.fillWithReturnOrderSku(retOrdSku);
				// to be continued: gifts
			}
			public ProductInfo getProduct() {
				return product;
			}
			public void setProduct(ProductInfo product) {
				this.product = product;
			}
			public List<ProductInfo> getGifts() {
				return gifts;
			}
			public void setGifts(List<ProductInfo> gifts) {
				this.gifts = gifts;
			}
			public static class ProductInfo {
				private String brand;
				private String product;
				private String color;
				private long number;
				private long confirmNumber;
				private String reason;
				public void fillWithReturnOrderSku(ReturnOrderSkuDTO retOrdSku) {
					if(null == retOrdSku) {
						return;
					}
					this.number = retOrdSku.getApplyedReturnCount();
					this.confirmNumber = retOrdSku.getConfirmCount();
					this.reason = retOrdSku.getReason();
					OrderSkuDTO ordSku = retOrdSku.getOrdSkuDTO();
					if(null == ordSku || null == ordSku.getSkuSPDTO()) {
						return;
					}
					SkuSPDTO skuSP = ordSku.getSkuSPDTO();
					this.brand = skuSP.getBrandName();
					this.product = skuSP.getProductName();
					this.color = skuSP.getColorName();
				}
				public String getBrand() {
					return brand;
				}
				public void setBrand(String brand) {
					this.brand = brand;
				}
				public String getColor() {
					return color;
				}
				public void setColor(String color) {
					this.color = color;
				}
				public String getProduct() {
					return product;
				}
				public void setProduct(String product) {
					this.product = product;
				}
				public long getNumber() {
					return number;
				}
				public void setNumber(long number) {
					this.number = number;
				}
				public long getConfirmNumber() {
					return confirmNumber;
				}
				public void setConfirmNumber(long confirmNumber) {
					this.confirmNumber = confirmNumber;
				}
				public String getReason() {
					return reason;
				}
				public void setReason(String reason) {
					this.reason = reason;
				}
			}
		}
		
		public static class ReturnMethod {
			private String refundType;
			private BigDecimal price = BigDecimal.ZERO;
			private String bankCardInfo;
			public ReturnMethod() {
			}
			public ReturnMethod(String refundType, BigDecimal price, String bankCardInfo) {
				this.refundType = refundType;
				if(null != price) {
					this.price = price.setScale(2, RoundingMode.HALF_UP);
				}
				this.bankCardInfo = bankCardInfo;
			}
			public String getRefundType() {
				return refundType;
			}
			public void setRefundType(String refundType) {
				this.refundType = refundType;
			}
			public BigDecimal getPrice() {
				return price;
			}
			public void setPrice(BigDecimal price) {
				this.price = price;
			}
			public String getBankCardInfo() {
				return bankCardInfo;
			}
			public void setBankCardInfo(String bankCardInfo) {
				this.bankCardInfo = bankCardInfo;
			}
		}
	}
}
