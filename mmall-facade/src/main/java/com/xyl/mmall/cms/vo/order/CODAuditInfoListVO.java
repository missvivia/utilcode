package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.dto.CODAuditLogDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.enums.OrderFormState;

public class CODAuditInfoListVO {

	private int total;
	
	private List<CODAuditInfo> list = new ArrayList<CODAuditInfo>();
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<CODAuditInfo> getList() {
		return list;
	}

	public void setList(List<CODAuditInfo> list) {
		this.list = list;
	}

	public static class CODAuditInfo {
		private String id;	// 13,
		
		private String userId;	//  123123,
		
		private String name;	//  "xxx",
		
		private String telp;	//  "150xxxxx",
		
		private String address;	//  "xxxxxxxxxx号",
		
		private BigDecimal orderPay;	//  "￥23.0",
		
		private String orderId;	//  222,
		
		private String saleArea;
		
		private int status;	//  1,
		
		private String checkName;	//  "大叔",
		
		private String account = "无";	//  "xx"
		
		private boolean canPass = false;	// 是否允许"通过"操作
		
		private boolean canRefuse = false;	// 是否允许"拒绝"操作
		
		private boolean canCancelRefuse = false;	// 是否允许"撤销拒绝"操作
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTelp() {
			return telp;
		}

		public void setTelp(String telp) {
			this.telp = telp;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public BigDecimal getOrderPay() {
			return orderPay;
		}

		public void setOrderPay(BigDecimal orderPay) {
			this.orderPay = orderPay;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getCheckName() {
			return checkName;
		}

		public void setCheckName(String checkName) {
			this.checkName = checkName;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}
		
		public static CODAuditInfo convertDTOs2CODInfo(CODAuditLogDTO codDTO, OrderFormBriefDTO ordDTO, 
				OrderExpInfoDTO expDTO, String auditUser, String area) {
			CODAuditInfo info = new CODAuditInfo();
			if(null != codDTO && null != ordDTO) {
				info.setId(String.valueOf(codDTO.getId()));
				info.setUserId(String.valueOf(codDTO.getUserId()));
				if(null != expDTO) {
					info.setName(expDTO.getConsigneeName());
					String userTel = expDTO.getConsigneeMobile();
					if(null == userTel || 0 == userTel.length()) {
						userTel = expDTO.getConsigneeTel();
					}
					info.setTelp(userTel);
					info.setAddress(expDTO.fullAddress());
				}
				BigDecimal realPayPrice = ordDTO.getCartRPrice();
				if(null != realPayPrice && null != ordDTO.getExpUserPrice()) {
					realPayPrice = realPayPrice.add(ordDTO.getExpUserPrice());
				}
				if(null != realPayPrice) {
					realPayPrice = realPayPrice.setScale(2, RoundingMode.HALF_UP);
				}
				info.setOrderPay(realPayPrice);
				info.setOrderId(String.valueOf(ordDTO.getOrderId()));
				info.setSaleArea(area);
				info.setStatus(codDTO.getAuditState().getIntValue());
				info.setCheckName(auditUser);
				String extInfo = codDTO.getExtInfo();
				if(null != extInfo && !("null".equals(extInfo))) {
					info.setAccount(extInfo);
				}
				info.canPass = CODAuditState.WAITING == codDTO.getAuditState() && OrderFormState.WAITING_COD_AUDIT == ordDTO.getOrderFormState();
				info.canRefuse = CODAuditState.WAITING == codDTO.getAuditState() && OrderFormState.WAITING_COD_AUDIT == ordDTO.getOrderFormState();
				info.canCancelRefuse = CODAuditState.REFUSED == codDTO.getAuditState() && OrderFormState.COD_AUDIT_REFUSE == ordDTO.getOrderFormState();
			}
			return info;
		}

		public String getSaleArea() {
			return saleArea;
		}

		public void setSaleArea(String saleArea) {
			this.saleArea = saleArea;
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
	}
}
