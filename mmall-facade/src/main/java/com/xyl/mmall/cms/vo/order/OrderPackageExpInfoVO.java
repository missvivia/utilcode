package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageRefundDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * CMS订单详情：配送信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月20日 上午9:09:04
 *
 */
public class OrderPackageExpInfoVO {

	private long total;
	private List<PackageInfo> list = new ArrayList<PackageInfo>();
	
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<PackageInfo> getList() {
		return list;
	}

	public void setList(List<PackageInfo> list) {
		this.list = list;
	}

	public void fillWithOrderFormDTO(OrderFormDTO orderFormDTO, 
			Map<Long, Boolean> canReopenReturnJudge, 
			Map<Long, ReturnPackageDTO> retPkgMap, 
			Map<Long, OmsOrderPackage> omsPkgMap) {
		if(null == orderFormDTO || CollectionUtil.isEmptyOfMap(canReopenReturnJudge)) {
			return;
		}
		List<OrderPackageDTO> orderPackageDTOList = orderFormDTO.getOrderPackageDTOList();
		if(CollectionUtil.isEmptyOfList(orderPackageDTOList)) {
			return;
		}
		boolean isCOD = OrderFormPayMethod.COD == orderFormDTO.getOrderFormPayMethod();
		this.setTotal(orderPackageDTOList.size());
		for(int i = 0; i < orderPackageDTOList.size(); i++) {
			PackageInfo pkgInfo = new PackageInfo();
			OrderPackageDTO ordPkg = orderPackageDTOList.get(i);
			ReturnPackageDTO retPkg = null;
			if(null != ordPkg && null != retPkgMap) {
				retPkg = retPkgMap.get(ordPkg.getPackageId());
			}
			OmsOrderPackage omsPkg = null;
			if(null != ordPkg && null != omsPkgMap) {
				omsPkg = omsPkgMap.get(ordPkg.getPackageId());
			}
			pkgInfo.fillWithOrderPackageInfo(ordPkg, retPkg, omsPkg, i + 1, canReopenReturnJudge, isCOD);
			this.list.add(pkgInfo);
		}
	}
	
	public static class PackageInfo {
		private String pack; // "包裹1",
		private String packNum; // "包裹序列号"
		private String omsPackNum; // "包裹序列号"
		private String expressCompany; // "xxx",
		private String expressNum; // "YE5P-Y6R8",
		private String status; // 1
		private OrderPackageState realStatus;
		private boolean canSetLost = false;	//是否可以设为“丢件”
		private boolean canReopenReturn = false;	//是否可以“重新开放退货入库”
		private boolean canReturnCash = false;	//是否可以设为“拒收”: ugly code here
		private boolean canCancel = false;	//是否可以“取消包裹”
		private boolean canConsign = false;	//是否可以“签收”
		private boolean pkgRefusedOrCanceld = false;	//已拒收/超时未配送
		private long kfOperationTime = 0;	//客服操作处理时间(拒收、超时未配送)
		private BigDecimal returnCash = BigDecimal.ZERO;	//退款金额(拒收、超时未配送)
		private BigDecimal returnHbCash = BigDecimal.ZERO;	//退款红包金额(拒收、超时未配送)
		public void fillWithOrderPackageInfo(OrderPackageDTO pkg, ReturnPackageDTO retPkg, OmsOrderPackage omsPkg, 
				int pkgSeq, Map<Long, Boolean> canReopenReturnJudge, boolean isCOD) {
			if(null == pkg) {
				return;
			}
			long pkgId = pkg.getPackageId();
			pack = "包裹" + pkgSeq;
			packNum = String.valueOf(pkgId); // "包裹序列号"
			omsPackNum = null == omsPkg ? "S_0" : "S_" + omsPkg.getPackageId();
			expressCompany = pkg.getExpressCompanyReturn(); // "xxx",
			expressNum = pkg.getMailNO(); // "YE5P-Y6R8",
			OrderPackageState pkgState = pkg.getOrderPackageState();
			status = (null == pkgState) ? "" : pkgState.getDesc() + "[" + pkgState.getName() + "]"; // 1
			realStatus = pkgState;
			canSetLost = pkgState == OrderPackageState.WAITING_SIGN_IN;
			canReopenReturn = canReopenReturnJudge.containsKey(pkgId) ? canReopenReturnJudge.get(pkgId) : false;
			canReturnCash = pkgState == OrderPackageState.WAITING_SIGN_IN;
			canCancel = pkgState == OrderPackageState.OUT_TIME;
			canConsign = pkgState == OrderPackageState.WAITING_SIGN_IN;
			pkgRefusedOrCanceld = pkgState == OrderPackageState.CANCEL_SR || pkgState == OrderPackageState.CANCEL_OT
					|| pkgState == OrderPackageState.CANCEL_LOST;
			kfOperationTime = pkg.getCancelTime();
			returnCash = computePkgPrice(pkg, retPkg, false);
			returnHbCash = computePkgPrice(pkg, retPkg, true);
		}
		private BigDecimal computePkgPrice(OrderPackageDTO pkg, ReturnPackageDTO retPkg, boolean isRed) {
			BigDecimal price = BigDecimal.ZERO;
			OrderPackageRefundDTO ordPkgRefund = null;
			if(null != pkg && null != (ordPkgRefund = pkg.getOrderPackageRefundDTO())) {
				price = isRed ? ordPkgRefund.getRedCash() : ordPkgRefund.getRealCash();
			}
			// ugly code
			if(null != retPkg) {
				ReturnPackageState state = retPkg.getReturnState();
				if(CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), state)) {
					price = isRed ? retPkg.getPayedHbPriceToUser() : retPkg.getPayedCashPriceToUser();
				} else {
					price = isRed ? retPkg.getApplyedReturnHbPrice() : retPkg.getApplyedReturnCashPrice();
				}
			}
			return price;
		}
		public String getPack() {
			return pack;
		}
		public void setPack(String pack) {
			this.pack = pack;
		}
		public String getPackNum() {
			return packNum;
		}
		public void setPackNum(String packNum) {
			this.packNum = packNum;
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
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public OrderPackageState getRealStatus() {
			return realStatus;
		}
		public void setRealStatus(OrderPackageState realStatus) {
			this.realStatus = realStatus;
		}
		public boolean isCanSetLost() {
			return canSetLost;
		}
		public void setCanSetLost(boolean canSetLost) {
			this.canSetLost = canSetLost;
		}
		public boolean isCanReopenReturn() {
			return canReopenReturn;
		}
		public void setCanReopenReturn(boolean canReopenReturn) {
			this.canReopenReturn = canReopenReturn;
		}
		public boolean isCanReturnCash() {
			return canReturnCash;
		}
		public void setCanReturnCash(boolean canReturnCash) {
			this.canReturnCash = canReturnCash;
		}
		public boolean isCanCancel() {
			return canCancel;
		}
		public void setCanCancel(boolean canCancel) {
			this.canCancel = canCancel;
		}
		public boolean isCanConsign() {
			return canConsign;
		}
		public void setCanConsign(boolean canConsign) {
			this.canConsign = canConsign;
		}
		public long getKfOperationTime() {
			return kfOperationTime;
		}
		public void setKfOperationTime(long kfOperationTime) {
			this.kfOperationTime = kfOperationTime;
		}
		public BigDecimal getReturnCash() {
			return returnCash;
		}
		public void setReturnCash(BigDecimal returnCash) {
			this.returnCash = returnCash;
		}
		public boolean isPkgRefusedOrCanceld() {
			return pkgRefusedOrCanceld;
		}
		public void setPkgRefusedOrCanceld(boolean pkgRefusedOrCanceld) {
			this.pkgRefusedOrCanceld = pkgRefusedOrCanceld;
		}
		public BigDecimal getReturnHbCash() {
			return returnHbCash;
		}
		public void setReturnHbCash(BigDecimal returnHbCash) {
			this.returnHbCash = returnHbCash;
		}
		public String getOmsPackNum() {
			return omsPackNum;
		}
		public void setOmsPackNum(String omsPackNum) {
			this.omsPackNum = omsPackNum;
		}
	}

}
