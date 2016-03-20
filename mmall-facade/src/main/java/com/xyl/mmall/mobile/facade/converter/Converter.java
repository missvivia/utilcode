package com.xyl.mmall.mobile.facade.converter;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.tools.location.search.util.AreaCode;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.mobile.MobileURL;
import com.xyl.mmall.framework.vo.BaseJsonListTimeVO;
import com.xyl.mmall.framework.vo.BaseJsonListVO;
import com.xyl.mmall.framework.vo.BaseJsonPageVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.vo.CartSkuItemVO;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.mainsite.vo.order.OrderPackageVO;
import com.xyl.mmall.mobile.facade.vo.MobileShareVO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.ReturnPackageState;

public class Converter {
	public final static int SHARE_PRODUCT = 1;

	public final static int SHARE_PO = 2;

	public final static int PRODUCT_DETAIL = 3;

	public final static int LOGISTIC_DETAIL = 4;

	public final static int SHARE_GIFT = 5;

	public final static int SIZE_TABLE = 6;

	public final static int LOGISTIC_DETAIL_BY_ORDER = 7;

	// 1：详情，2：物流，3：付款，4：取消订单，5：退货，6：删除
	//private final static int BOTTON_DETAIL = 1;

	private final static int BOTTON_LOGISTIC = 2;

	private final static int BOTTON_PAY = 3;

	private final static int BOTTON_CANCEL = 4;

	//private final static int BOTTON_RETURN = 5;

	private final static int BOTTON_DELETE = 6;

	//private final static int BOTTON_REJECT = 7;

	public static String genMobilePageLink(int type, String id) {
		return MobileURL.genMobilePageLink(type, id);
	}

	/**
	 * 获取主站URL地址
	 * 
	 * @param type
	 *            1主站 商品 2，主站 PO 3，商品详情中的詳細列表,4,物流详情，５分享紅包,6 尺码表
	 * @param id
	 * @return
	 */

	public static String brandName(String ch, String en) {
		String name = "";
		if (StringUtils.isNotBlank(ch)) {
			name = ch;
		}
		if (StringUtils.isNotBlank(en)) {
			if (StringUtils.isBlank(name)) {
				name = en;
			}
		}
		return name;
	}

	public static String genPayLink(long id, SpSource sp, String ursId, String ursToken,int paymethod) {
		return MobileConfig.mobile_pay_url + "/m/pay/userpay?tradeSerialId=" + id + "&sp=" + sp.getIntValue()
				+ "&payMethod="+paymethod+"&ursId=" + ursId + "&ursToken=" + ursToken;
	}

	public static MobileShareVO sharePoTemplate(String cut, String poName, String url,String logo) {
		MobileShareVO vo = new MobileShareVO();
		vo.setShareTitle(MobileConfig.share_po_template_title);
		vo.setShareLogo(logo);
		String t = MobileConfig.share_po_template_body.replace("#brand", poName).replace("#off", cut);
		vo.setShareTemplate(t);
		vo.setShareURL(url);

		return vo;
	}
	
	public static MobileShareVO shareGiftTemplate(String url) {
		MobileShareVO vo = new MobileShareVO();
		vo.setShareTitle(MobileConfig.share_gift_template_title);
		vo.setShareLogo(MobileConfig.share_gift_template_image);
		String t = MobileConfig.share_gift_template_body;
		vo.setShareTemplate(t);
		vo.setShareURL(url);
		return vo;
	}
	

	public static MobileShareVO sharePrdtTemplate(String price, String poName, String productName, String url, String image) {
		MobileShareVO vo = new MobileShareVO();
		vo.setShareTitle(MobileConfig.share_prdt_template_title);
		vo.setShareLogo(image);
		String t = MobileConfig.share_prdt_template_body.replaceAll("#price", price).replace("#brand", poName).replace("#product", productName);
		vo.setShareTemplate(t);
		vo.setShareURL(url);
		return vo;
	}

	public static String genWebSiteLink(int type, long id) {
		return genWebSiteLink(type, String.valueOf(id), "");
	}

	public static String genWebSiteLink(int type, String id, String value) {
		switch (type) {
		// 分享
		case SHARE_PRODUCT:
			return MobileConfig.mobile_web_url + "/m/share/product?id=" + id;
		case SHARE_PO:
			return MobileConfig.mobile_web_url + "/m/share/po/page?scheduleId=" + id;
		case PRODUCT_DETAIL:
			return MobileConfig.mobile_web_url + "/m/product?id=" + id;
		case LOGISTIC_DETAIL:
			return MobileConfig.mobile_web_url + "/m/logistics?expressNO=" + id + "&expressCompany=" + value;
		case SHARE_GIFT:
			return MobileConfig.mobile_web_url + "/m/share/red/apply?id=" + id;
		case SIZE_TABLE:
			return MobileConfig.mobile_web_url + "/m/sizetable?productId=" + id;
		case LOGISTIC_DETAIL_BY_ORDER:
			return MobileConfig.mobile_web_url + "/m/logistics_all?orderId=" + id;
		}
		return null;
	}

	/**
	 * 
	 0, 未付款, 1, 待审核 2, 待发货可以取消 5.申请退货 6, 待发货 8，已退款 9，退货失败 10, 已发货 11.退货中 15,
	 * 交易完成 20, 取消中 21, 已取消 25, 审核未通过
	 * 
	 * @param status
	 * @return 1：详情，2：物流，3：付款，4：取消订单，5：退货，6：删除 顺序按照数组顺序
	 */
	public static List<Integer> coverOrderButtonState(int status, boolean canCancel ,OrderFormState ostatus) {
		List<Integer> button = new ArrayList<Integer>();

		if (canCancel && status != MobileOrderStatus.CANCEL.getIntValue()) {
			button.add(BOTTON_CANCEL);
		}
		if (status == MobileOrderStatus.UNPAY.getIntValue()) {
			button.add(BOTTON_PAY);
		}
		if (status == MobileOrderStatus.CANCEL.getIntValue()) {
			button.add(BOTTON_DELETE);
		} 
		if(hasLogist(ostatus)){
			button.add(BOTTON_LOGISTIC);
		}
		return button;
	}

	public static boolean hasLogist(OrderFormState status) {
		return (status == OrderFormState.PART_DELIVE || 
				status == OrderFormState.ALL_DELIVE ||
				status == OrderFormState.FINISH_DELIVE );
	}

	public static String genCartHash(long time, String skuIds) {
		if (StringUtils.isBlank(skuIds))
			return "0";

		return String.valueOf(time);
	}

	private static MobileOrderStatus defaultStatus(OrderFormState ostatus) {
		switch (ostatus) {
		case COD_AUDIT_REFUSE:
			return MobileOrderStatus.VERIFYFAIL;
		case WAITING_PAY:
			return MobileOrderStatus.UNPAY;
		case WAITING_COD_AUDIT:
			return MobileOrderStatus.WAITVERIFY;
		case WAITING_SEND_ORDER:
			return MobileOrderStatus.WAITSEND1;
		case WAITING_DELIVE:
			return MobileOrderStatus.WAITSEND2;
		case CANCEL_ING:
		case WAITING_CANCEL_OMSORDER:
			return MobileOrderStatus.CANCELING;
		case CANCEL_ED:
			return MobileOrderStatus.CANCEL;
		default:
			break;
		}
		return null;
	}

	public static MobileOrderStatus coverPacketState(OrderFormState ostatus, OrderPackageState pstatus,ReturnPackageState returnstatus) {
		MobileOrderStatus status = defaultStatus(ostatus);
		if (status != null)
			return status;
		if(returnstatus == ReturnPackageState.APPLY_RETURN){
			return MobileOrderStatus._RETURN;
		}
		if(returnstatus == ReturnPackageState.ABNORMAL_REFUSED ||
				returnstatus == ReturnPackageState.ABNORMAL_COD_REFUSED ||
						returnstatus == ReturnPackageState.CANCELLED)
		{
			return MobileOrderStatus.RETURNFAIL;
		}
		switch (pstatus) {
		case INIT:
			return MobileOrderStatus.WAITSEND1;
		case WAITING_DELIVE:
			return MobileOrderStatus.WAITSEND2;
		case WAITING_SIGN_IN:
		case SIGN_FAIL:
		case OUT_TIME:
			return MobileOrderStatus.SENDED;
			//return MobileOrderStatus.LOGISTERROR;
		case SIGN_IN:
		case SIGN_REFUSED:
			return MobileOrderStatus.FINISH;
		case RP_APPLY:
			return MobileOrderStatus.RETURNING;
		case RP_DONE:
			return MobileOrderStatus.RETURNED;
		case CANCEL_OT:
		case CANCEL_SR:
		case CANCEL_OC_UNPAY:
		case CANCEL_OC_PAYED:
		case CANCEL_LOST:
			return MobileOrderStatus.CANCEL;
		default:
			break;
		}
		return MobileOrderStatus.UNKNOW;
	}

	public static MobileOrderStatus coverOrderState(OrderForm2VO dto) {
		MobileOrderStatus status = defaultStatus(dto.getOrderFormState());
		if (status != null)
			return status;
		if (dto.getPackageList() != null) {
			status = MobileOrderStatus.UNKNOW;
			for (OrderPackageVO vo : dto.getPackageList()) {
				MobileOrderStatus childStatus = coverPacketState( dto.getOrderFormState(), vo.getOrderPackageState(),vo.getReturnPackageState());
				if(childStatus == MobileOrderStatus.SENDED){
					return childStatus;
				}
				if(childStatus == MobileOrderStatus.WAITSEND1 || childStatus == MobileOrderStatus.WAITSEND2){
					status = MobileOrderStatus.WAITSEND2;
				}
				if(status != MobileOrderStatus.WAITSEND2 && (childStatus == MobileOrderStatus.FINISH 
						|| childStatus == MobileOrderStatus.RETURNING || 
						childStatus == MobileOrderStatus.RETURNED || childStatus == MobileOrderStatus.RETURNFAIL)) {
					status = MobileOrderStatus.FINISH;
				}
			
				if(status != MobileOrderStatus.WAITSEND2 && status != MobileOrderStatus.FINISH){
					status = childStatus;
				}
			}
			
			return status;
		}
		return MobileOrderStatus.UNKNOW;
	}

	/**
	 * 以后是不是这么取未知，有风险，要改
	 * 
	 * @return
	 */
	public static long getTime() {
		return System.currentTimeMillis();
	}

	/**
	 * sku 状态转描述
	 * 
	 * @param type
	 * @return
	 */
	public static String getSkuStatusInDetailPageDesc(int type) {
		switch (type) {
		case 1:
			return MobileConfig.sku_status_1;
		case 2:
		case 3:
			return MobileConfig.sku_status_3;
		default:
			return "";
		}
	}

	public static String getMD5(Object needHash) {
		String s = needHash.toString();
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		if (s == null)
			return null;
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String coverCartItemStatus(String a, int count) {
		if (CartSkuItemVO.STATE_DELETED.equals(a)) {
			return MobileConfig.cart_item_status_1;
		}
		if (CartSkuItemVO.STATE_OVERTIME.equals(a)) {
			return MobileConfig.cart_item_status_2;
		}
		/*
		 * if (count == 0) { return MobileConfig.cart_item_status_3; }
		 */
		return "";
	}

	public static int coverCartItemStatusCode(String a, int count) {
		if (CartSkuItemVO.STATE_DELETED.equals(a)) {
			return 5;
		}
		if (CartSkuItemVO.STATE_OVERTIME.equals(a)) {
			return 2;
		}
		if (CartSkuItemVO.STATE_OVERPO.equals(a)) {
			return 4;
		}
		if (CartSkuItemVO.STATE_SOLDOUT.equals(a)) {
			return 3;
		}

		return 1;
	}

	/**
	 * product 状态转描述
	 * 
	 * @param type
	 * @return
	 */
	public static String getProductStatusInDetailPageDesc(int type) {
		switch (type) {
		case 1:
			return MobileConfig.prdt_status_1;
		case 2:
			return MobileConfig.prdt_status_2;
		case 3:
			return MobileConfig.prdt_status_3;
		case 4:
			return MobileConfig.prdt_status_4;
		default:
			return "";
		}
	}
	
	public static String getPoGroupTitle() {
		return MobileConfig.po_new_titil;
	}

	public static String timeFormat(Date timestamp) {
		Calendar today = Calendar.getInstance();
		Calendar old = Calendar.getInstance();
		old.setTime(timestamp);
		// 此处好像是去除0
		today.set(Calendar.AM_PM, 0);
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		old.set(Calendar.AM_PM, 0);
		old.set(Calendar.HOUR, 10);
		old.set(Calendar.MINUTE, 0);
		old.set(Calendar.SECOND, 0);
		// 老的时间减去今天的时间
		System.out.print(new Date(old.getTimeInMillis()) + " --- " + new Date(today.getTimeInMillis()));
		long intervalMilli = old.getTimeInMillis() - today.getTimeInMillis();
		int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
		// -2:前天 -1：昨天 0：今天 1：明天 2：后天， out：显示日期
		if (xcts == 0) {
			return "今天";
		}
		if (xcts == 1) {
			return "明天";
		}
		if (xcts == 2) {
			return "后天";
		}
		return null;
	}

	public static boolean isBeforeTen(){
		Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return System.currentTimeMillis() < c.getTimeInMillis();
	}
	public static String getPoGroupTitle(int i,long timestamp) {
		
		if(i == -999){
			return MobileConfig.po_new_titil_before10;
		}
		if(i == 0){
			return MobileConfig.po_new_titil;
		}
		if (timestamp <= System.currentTimeMillis()) {
			return MobileConfig.brand_po_today_titil;
		}
		Date date = new Date(timestamp);
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		String dateString = formatter.format(date);
		String dateF = timeFormat(date);
		if (dateF != null)
			dateString = dateF;

		if(i == -1){
			return dateString + MobileConfig.po_group_titil_color;
		}
		return dateString + MobileConfig.po_group_titil;
	}
	
	public static double doubleFormat(BigDecimal dicount) {
		if (dicount == null)
			return 0;
		double f = dicount.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		return f;
	}
	
	public static String genPrdtName(String brandname,String prdtname) {
		if(StringUtils.isBlank(brandname))
			return prdtname;
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(brandname);
		sb.append("]");
		sb.append(prdtname);
		return sb.toString();
	}

	public static String calDiscountFormat(BigDecimal d1, BigDecimal d2) {
		if (d2.doubleValue() == 0)
			return "?";
		double discout = 100 * (d1.doubleValue() / d2.doubleValue());
		return discountFormat(1, new BigDecimal(discout));
	}

	/**
	 * 折扣广告语配置
	 * 
	 * @param type
	 * @param dicount
	 * @return
	 */
	public static String discountFormat(int type, BigDecimal dicount) {
		if (dicount == null) {
			return MobileConfig.no_discount_title;
		}
		dicount = dicount.divide(new BigDecimal(10));
		StringBuilder info = new StringBuilder();
		double f = dicount.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if (f > 10 || f <= 0) {
			return null;
		}
		if (f % 1 == 0d) {
			info.append((int) f);
		} else {
			info.append(f);
		}
		switch (type) {
		case 1:
			info.append(MobileConfig.discount_tail2);
			break;
		case 0:
		default:
			info.append(MobileConfig.discount_tail);

		}
		return info.toString();
	}

	/**
	 * 地区代码转化，这个到时候看需求
	 * 
	 * @param area
	 * @return
	 */
	public static String AreaCode(AreaCode area) {
		if (area == AreaCode.UnKnow)
			return String.valueOf(area.getCode());
		return String.valueOf(area.getCode());
	}

	/**
	 * 一堆的转换
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private static HashMap<String, Object> genMapValue(String key, Object value) {
		HashMap<String, Object> temp = new HashMap<String, Object>();
		if (key == null)
			return temp;
		temp.put(key, value);
		return temp;
	}

	public static BaseJsonVO genrBaseJsonVO(String key, Object value) {
		HashMap<String, Object> map = genMapValue(key, value);
		return converterBaseJsonVO(map);
	}

	public static void addMapValue(HashMap<String, Object> map, String key, String value) {
		if (key != null)
			map.put(key, value);
	}

	public static BaseJsonVO listBaseJsonVO(List<?> obj, boolean hasNext) {
		BaseJsonListVO tt = new BaseJsonListVO(obj);
		tt.setHasNext(hasNext);
		return new BaseJsonVO(tt);
	}
	public static BaseJsonVO  pageBaseJsonVO(List<?> obj, DDBParam ddbParam) {
		BaseJsonPageVO tt = new BaseJsonPageVO(obj,ddbParam);
		return new BaseJsonVO(ErrorCode.SUCCESS,tt);
	}

	public static BaseJsonVO listTimeBaseJsonVO(List<?> obj, boolean hasNext) {
		BaseJsonListTimeVO tt = new BaseJsonListTimeVO(obj);
		tt.setHasNext(hasNext);
		tt.setTimestamp(getTime());
		return new BaseJsonVO(tt);
	}

	public static BaseJsonVO converterBaseJsonVO(Object obj) {
		BaseJsonVO vo = new BaseJsonVO(obj);
		return vo;
	}

	public static BaseJsonVO errorBaseJsonVO(MobileErrorCode code) {
		BaseJsonVO vo = new BaseJsonVO(code);
		return vo;
	}

	public static BaseJsonVO errorBaseJsonVO(MobileErrorCode code, String extra) {
		BaseJsonVO vo = errorBaseJsonVO(code);
		if (org.apache.commons.lang.StringUtils.isNotBlank(extra) && extra.length() > 100) {
			extra = extra.substring(0, 97) + "...";
		}
		vo.setMessage(vo.getMessage() + ":" + extra);
		return vo;
	}

	public static BaseJsonVO errorBaseJsonVO(ParamNullException e) {
		BaseJsonVO vo = errorBaseJsonVO(MobileErrorCode.PARAM_NO_MATCH);
		vo.setMessage(vo.getMessage() + ":" + e.getMessage());
		return vo;
	}
	final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;  
	/**
	 * Latitude = 0 .Longitude = 1 
	 * @param locs
	 * @return
	 */
	public static double[] changeBD09llToGCJ02(double... locs ){
		    double x = locs[1] - 0.0065, y = locs[0] - 0.006;
		    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		    locs[1] = z * Math.cos(theta);
		    locs[0] = z * Math.sin(theta);
		    return locs;
		}
	
	/**
	 * 尺寸相似度算法。随便写的
	 * @param size
	 * @param sizeList
	 * @return
	 */
	public static String findSmailSize(String size,Set<String> sizeList){
		if(sizeList == null || size == null)
			return "";
		String[] h_w = size.split("x");
		int h = Integer.parseInt(h_w[0]);
		double r_size = Double.parseDouble(h_w[0])/Double.parseDouble(h_w[1]);
		double min_r = r_size * h;
		String result = "";
		for(String temp : sizeList){
			String[] _h_w = temp.split("x");
			int _h = Integer.parseInt(_h_w[0]);
			double _r_size = Double.parseDouble(_h_w[0])/Double.parseDouble(_h_w[1]);
			int mint = Math.abs(_h-h);
			double mintr = Math.abs(r_size-_r_size);
			double t_ = mint + h * mintr;
			if(t_ < min_r){
				min_r = t_;
				result = temp;
			}
				
		}
		
		return result;
		
	}
	
	
	public static long protocolVersion(String version){
		if(StringUtils.isBlank(version))
			return 0l;
		String[] v = version.split("[^\\d]");
		long vs = 0;
		for(int i=0;i<v.length;i++){
			long a = Long.parseLong(v[i]);
			if(a < 10 && i !=0)
				a= a*10;
			vs = vs  + (long) (a * Math.pow(10, (v.length-i)*2));
		}
		return vs;
	}
	
}
