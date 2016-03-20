/**
 * 
 */
package com.xyl.mmall.mobile.web.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.util.DateFormatEnum;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mobile.web.facade.MobileOrderTraceFacade;
import com.xyl.mmall.mobile.web.util.MobileVOConvertUtil;
import com.xyl.mmall.mobile.web.vo.OrderTraceVO;
import com.xyl.mmall.mobile.web.vo.order.OrderForm2VO;
import com.xyl.mmall.mobile.web.vo.order.OrderPackageVO;
import com.xyl.mmall.oms.meta.OrderTrace;
import com.xyl.mmall.oms.service.OrderTraceService;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageBriefDTO;

/**
 * @author lihui
 *
 */
@Facade("mobileOrderTraceFacade")
public class MobileOrderTraceFacadeImpl implements MobileOrderTraceFacade {

	@Autowired
	private OrderTraceService orderTraceService;
	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private ReturnPackageFacade returnPackageFacade;
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OrderTraceFacade#getTrace(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<OrderTraceVO> getTrace(String expressCompany, String expressNO) {
		OrderTrace[] orderTraces = orderTraceService.getTrace(expressCompany, expressNO);
		List<OrderTraceVO> orderTraceList = new ArrayList<>();
		if (orderTraces != null && orderTraces.length > 0) {
			for (OrderTrace orderTrace : orderTraces) {
				orderTraceList.add(new OrderTraceVO(orderTrace));
			}
		}
		return orderTraceList;
	}
	
	@Override
	public List<HashMap<String,Object>> getTraceByOrderId(long userId,long orderId) {
		 List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		 
		 OrderFormDTO form = orderFacade.queryOrderForm(userId, orderId, true);
		 OrderForm2VO dto = MobileVOConvertUtil.convertToOrderForm2VO(form,returnPackageFacade);
		 if (dto.getPackageList() != null && dto.getPackageList().size() > 0) {
				for (OrderPackageVO orderPackage : dto.getPackageList()) {
					OrderPackageBriefDTO packageBriefDTO = orderFacade.queryOrderPackageBriefDTO(userId, orderPackage.getPackageId());
					HashMap<String, Object> map = new HashMap<String, Object>();
					if (StringUtils.isNotBlank(orderPackage.getMailNO())) {
						String mailNO = orderPackage.getMailNO(), expressCompany = orderPackage.getExpressCompanyReturn();
						OrderTrace trackVO1 = new OrderTrace();
						trackVO1.setTime(DateFormatEnum.TYPE5.getFormatDate(packageBriefDTO.getExpSTime()));
						trackVO1.setOperateOrg(packageBriefDTO.getWarehouseName());
						trackVO1.setNote("商品已发货");
						List<OrderTraceVO> a = new ArrayList<OrderTraceVO>();
						a.add(new OrderTraceVO(trackVO1));
						List<OrderTraceVO> b = getTrace(expressCompany,mailNO);
						if(b!= null){
							a.addAll(b);
						}
						List<OrderTraceVO> c = new ArrayList<OrderTraceVO>();
						for(int i= a.size()-1;i>=0;i--){
							c.add(a.get(i));
						}
						map.put("OrderTrace", c);
					}
					map.put("ExpressCompany", orderPackage.getExpressCompanyReturn());
					map.put("MailNO", orderPackage.getMailNO());
					list.add(map);
				}
				
				
		 }
		return list;
	}
		 
}
