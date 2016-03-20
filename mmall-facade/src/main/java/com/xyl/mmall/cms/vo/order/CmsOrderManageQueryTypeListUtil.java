package com.xyl.mmall.cms.vo.order;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CMS订单管理
 * 
 * 各种“查询类型”Util类
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class CmsOrderManageQueryTypeListUtil {

// **************** [common code] - start **************** 
	public static enum QueryCategory {
		ORDER_BY_ORDER(0, "订单查询：按订单查询"), ORDER_BY_TIME_RANGE(1, "订单查询：按用户信息查询"), ORDER_BY_USER(2, "订单查询：按时间查询"), 
		COD_BY_SEARCH(3, "到付审核查询"), RETURN_BY_SEARCH(4, "退货退款查询"), BLACKLIST_BY_SEARCH(5, "黑名单查询");
		private int tag;	private String desc;
		private QueryCategory(int tag, String desc) { this.tag = tag; this.desc = desc;}
		public int getTag() { return tag; }
		public String getDesc() {return desc; }
	}
	public static class KVPair {
		int tag;	int id;		String name;
		public KVPair(QueryCategory tag, int id, String name) { this.tag = tag.getTag(); this.id = id; this.name = name; }
		public int getTag() { return tag; }
		public int getId() { return id; }
		public String getName() { return name; }
	}
// **************** [common code] - end **************** 
	
	
// **************** [订单查询] - start **************** 
	public static class OrderQueryCategoryListVO {
		private static final OrderQueryCategoryListVO instance = new OrderQueryCategoryListVO();
		
		private OrderQueryCategoryListVO() {}
		
		public static OrderQueryCategoryListVO getInstance() { return instance; }
		
		private List<KVPair> tradeTypeList = OrderCategory.getTypeList();
		private List<KVPair> timeTypeList = TimeRangeCategory.getTypeList();
		private List<KVPair> userTypeList = UserCategory.getTypeList();
		
		public List<KVPair> getOrderTypeList() { return tradeTypeList; }
		
		public List<KVPair> getTimeRangeTypeList() { return timeTypeList; }
		
		public List<KVPair> getUserTypeList() { return userTypeList; }

		public static enum OrderCategory implements AbstractEnumInterface<OrderCategory> {
			ORDER_ID(new KVPair(QueryCategory.ORDER_BY_ORDER, 0, "订单ID")), MAIL_NO(new KVPair(QueryCategory.ORDER_BY_ORDER, 4, "快递单号")), PACKAGE_ID(new KVPair(QueryCategory.ORDER_BY_ORDER, 5, "包裹号"));
			private KVPair pair;
			private OrderCategory(KVPair pair) { this.pair = pair; }
			public static OrderCategory getOrderTradeType(int id) {
				for(OrderCategory ott : OrderCategory.values()) { if(id == ott.pair.getId()) { return ott; } }
				return null;
			}
			@Override
			public int getIntValue() { return pair.id; }
			@Override
			public OrderCategory genEnumByIntValue(int intValue) {
				for(OrderCategory oc : OrderCategory.values()) { if(intValue == oc.getIntValue()) { return oc; } }
				return null;
			}
			public static List<KVPair> getTypeList() {
				OrderCategory[] ocArray = OrderCategory.values();
				List<KVPair> ret = new ArrayList<KVPair>(ocArray.length);
				for(OrderCategory oc : ocArray) { ret.add(oc.pair); }
				return ret;
			}
		}
		
		public static enum TimeRangeCategory implements AbstractEnumInterface<TimeRangeCategory> {
			NOT_LACK(new KVPair(QueryCategory.ORDER_BY_TIME_RANGE, 0, "非缺货订单")), LACK(new KVPair(QueryCategory.ORDER_BY_TIME_RANGE, 1, "缺货订单"));
			private KVPair pair;
			private TimeRangeCategory(KVPair pair) { this.pair = pair; }
			@Override
			public int getIntValue() { return pair.id; }
			@Override
			public TimeRangeCategory genEnumByIntValue(int intValue) {
				for(TimeRangeCategory tc : TimeRangeCategory.values()) { if(intValue == tc.getIntValue()) { return tc; } }
				return null;
			}
			public static List<KVPair> getTypeList() {
				TimeRangeCategory[] tcArray = TimeRangeCategory.values();
				List<KVPair> ret = new ArrayList<KVPair>(tcArray.length);
				for(TimeRangeCategory tc : tcArray) { ret.add(tc.pair); }
				return ret;
			}
		}
		
		public static enum UserCategory implements AbstractEnumInterface<UserCategory> {
			USER_NAME(new KVPair(QueryCategory.ORDER_BY_USER, 0, "用户名")), NICKNAME(new KVPair(QueryCategory.ORDER_BY_USER, 1, "昵称")), USER_ID(new KVPair(QueryCategory.ORDER_BY_USER, 2, "用户ID")), 
			BIND_MOBILE(new KVPair(QueryCategory.ORDER_BY_USER, 3, "绑定手机号")), BIND_MAIL(new KVPair(QueryCategory.ORDER_BY_USER, 4, "绑定邮箱")), CONSIGNEE_MOBILE(new KVPair(QueryCategory.ORDER_BY_USER, 5, "收件人手机号"));
			private KVPair pair;
			private UserCategory(KVPair pair) { this.pair = pair; }
			@Override
			public int getIntValue() { return pair.id; }
			@Override
			public UserCategory genEnumByIntValue(int intValue) {
				for(UserCategory uc : UserCategory.values()) { if(intValue == uc.getIntValue()) { return uc; } }
				return null;
			}
			public static List<KVPair> getTypeList() {
				UserCategory[] ucArray = UserCategory.values();
				List<KVPair> ret = new ArrayList<KVPair>(ucArray.length);
				for(UserCategory uc : ucArray) { ret.add(uc.pair); }
				return ret;
			}
		}
	}
// **************** [订单查询] - end **************** 

	
// **************** [到付审核] - start **************** 
	public static class CODAuditQueryCategoryListVO {
		private static final CODAuditQueryCategoryListVO instance = new CODAuditQueryCategoryListVO();
		
		private CODAuditQueryCategoryListVO() { }

		public static CODAuditQueryCategoryListVO getInstance() { return instance; }
		
		private List<KVPair> typeList = CODSearchType.getTypeList();
		
		public List<KVPair> getSearchTypeList() { return typeList; }

		public static enum CODSearchType implements AbstractEnumInterface<CODSearchType> {
			USER_ID(new KVPair(QueryCategory.COD_BY_SEARCH, 0, "用户ID")), CONSIGNEE_NAME(new KVPair(QueryCategory.COD_BY_SEARCH, 1, "收货人姓名")), 
			CONSIGNEE_TEL(new KVPair(QueryCategory.COD_BY_SEARCH, 2, "收货人电话")) /**, CONSIGNEE_ADDRESS(new KVPair(QueryCategory.COD_BY_SEARCH, 3, "收货地址"))*/ ;
			private KVPair pair;
			private CODSearchType(KVPair pair) { this.pair = pair; }
			@Override
			public int getIntValue() { return pair.id; }
			@Override
			public CODSearchType genEnumByIntValue(int intValue) {
				for(CODSearchType t : CODSearchType.values()) { if(intValue == t.getIntValue()) { return t; } }
				return null;
			}
			public static List<KVPair> getTypeList() {
				CODSearchType[] stArray = CODSearchType.values();
				List<KVPair> ret = new ArrayList<KVPair>(stArray.length);
				for(CODSearchType st : stArray) { ret.add(st.pair); }
				return ret;
			}
		}
	}
// **************** [到付审核] - end **************** 

	
// **************** [退货退款管理] - start **************** 
	public static class ReturnPackageQueryCategoryListVO {

		private static final ReturnPackageQueryCategoryListVO instance = new ReturnPackageQueryCategoryListVO();
		
		private ReturnPackageQueryCategoryListVO() { }

		public static ReturnPackageQueryCategoryListVO getInstance() { return instance; }

		private List<KVPair> typeList = ReturnPackageKFSearchType.getTypeList();
		
		public List<KVPair> getSearchTypeList() { return typeList; }

		public static enum ReturnPackageKFSearchType implements AbstractEnumInterface<ReturnPackageKFSearchType> {
			RETURN_ID(new KVPair(QueryCategory.RETURN_BY_SEARCH, 0, "申请编号")),  ORDER_ID(new KVPair(QueryCategory.RETURN_BY_SEARCH, 1, "订单号")), 
			MAIL_NO(new KVPair(QueryCategory.RETURN_BY_SEARCH, 2, "快递单号")), PACKAGE_ID(new KVPair(QueryCategory.RETURN_BY_SEARCH, 3, "包裹号"));
			private KVPair pair;
			private ReturnPackageKFSearchType(KVPair pair) { this.pair = pair; }
			@Override
			public int getIntValue() { return pair.id; }
			@Override
			public ReturnPackageKFSearchType genEnumByIntValue(int intValue) {
				for(ReturnPackageKFSearchType t : ReturnPackageKFSearchType.values()) { if(intValue == t.getIntValue()) { return t; } }
				return null;
			}
			public static List<KVPair> getTypeList() {
				ReturnPackageKFSearchType[] stArray = ReturnPackageKFSearchType.values();
				List<KVPair> ret = new ArrayList<KVPair>(stArray.length);
				for(ReturnPackageKFSearchType st : stArray) { ret.add(st.pair); }
				return ret;
			}
		}
	}
// **************** [退货退款管理] - end **************** 

	
// **************** [到付黑名单管理] - start **************** 
	public static class BlacklistQueryCategoryListVO {

		private static final BlacklistQueryCategoryListVO instance = new BlacklistQueryCategoryListVO();
		
		private BlacklistQueryCategoryListVO() { }

		public static BlacklistQueryCategoryListVO getInstance() { return instance; }

		private List<KVPair> typeList = BlacklistSearchType.getTypeList();
		
		public List<KVPair> getSearchTypeList() { return typeList; }

		public static enum BlacklistSearchType implements AbstractEnumInterface<BlacklistSearchType> {
			USER_ID(new KVPair(QueryCategory.BLACKLIST_BY_SEARCH, 0, "用户ID")), CONSIGNEE_NAME(new KVPair(QueryCategory.BLACKLIST_BY_SEARCH, 1, "收货人姓名")), 
			CONSIGNEE_TEL(new KVPair(QueryCategory.BLACKLIST_BY_SEARCH, 2, "收货人电话"))/**, CONSIGNEE_ADDRESS(new KVPair(QueryCategory.BLACKLIST_BY_SEARCH, 3, "收货地址"))*/;
			private KVPair pair;
			private BlacklistSearchType(KVPair pair) { this.pair = pair; }
			@Override
			public int getIntValue() { return pair.id; }
			@Override
			public BlacklistSearchType genEnumByIntValue(int intValue) {
				for(BlacklistSearchType t : BlacklistSearchType.values()) { if(intValue == t.getIntValue()) { return t; } }
				return null;
			}
			public static List<KVPair> getTypeList() {
				BlacklistSearchType[] stArray = BlacklistSearchType.values();
				List<KVPair> ret = new ArrayList<KVPair>(stArray.length);
				for(BlacklistSearchType st : stArray) { ret.add(st.pair); }
				return ret;
			}
		}
	}
// **************** [到付黑名单管理] - end **************** 

}
