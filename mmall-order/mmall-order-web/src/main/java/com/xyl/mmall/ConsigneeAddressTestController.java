package com.xyl.mmall;
//package com.xyl.mmall;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.xyl.mmall.framework.util.JsonUtils;
//import com.xyl.mmall.order.dao.ConsigneeAddressDao;
//import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
//import com.xyl.mmall.order.meta.ConsigneeAddress;
//import com.xyl.mmall.order.service.ConsigneeAddressService;
//
///**
// * Hello world!
// * 
// */
//@Controller()
//@RequestMapping("/ca")
//public class ConsigneeAddressTestController {
//	
//	private static enum OpType {
//		ADD,  QUERY,  UPDATE,  DELETE
//	} 
//
//	@Autowired
//	ConsigneeAddressService caService;
//	
//	@Autowired
//	ConsigneeAddressDao caDao;
//	
//	@RequestMapping(value = "/hello", method = RequestMethod.GET)
//	public String hello(Model model) {
//		
//		/**
//		 * test-group 0
//		 * 
//		testService(OpType.ADD);
//		testService(OpType.QUERY);
//		*/
//		
//		/**
//		 * test-group 1
//		testService(OpType.UPDATE);
//		testService(OpType.DELETE);
//		*/
//		
//		/**
//		 * test-group 2
//		testDao(OpType.ADD);
//		testDao(OpType.QUERY);
//		*/
//		
//		/**
//		 * test-group 3
//		testDao(OpType.UPDATE);
//		testDao(OpType.DELETE);
//		*/
//		
//		return "pages/index";
//	}
//
//	private void testService(OpType ot) {
//		switch(ot) {
//		case ADD:
//			caService.addConsigneeAddress(20, new ConsigneeAddressDTO(createConsigneeAddress(20, 0)));
//			caService.addConsigneeAddress(21, new ConsigneeAddressDTO(createConsigneeAddress(21, 0)));
//			caService.addConsigneeAddress(22, new ConsigneeAddressDTO(createConsigneeAddress(22, 0)));
//			caService.addConsigneeAddress(23, new ConsigneeAddressDTO(createConsigneeAddress(23, 0)));
//			return;
//		case QUERY:
//			System.out.println(JsonUtils.toJson(caService.getConsigneeAddressListByUserId(21)));
//			System.out.println(JsonUtils.toJson(caService.getDefaultConsigneeAddress(20)));
//			System.out.println(JsonUtils.toJson(caService.getDefaultConsigneeAddress(21)));
//			System.out.println(JsonUtils.toJson(caService.getConsigneeAddressByIdAndUserId(0, 21)));
//			return;
//		case UPDATE:
//			caService.updateConsigneeAddress(-1, new ConsigneeAddressDTO(createConsigneeAddress(-1, 11)));
//			ConsigneeAddressDTO caDto = new ConsigneeAddressDTO(createConsigneeAddress(20, 22));
//			caDto.setId(5);
//			caService.updateConsigneeAddress(20, caDto);
//			caService.setDefaultConsigneeAddress(0, 21);
//			return;
//		case DELETE:
//			caService.deleteConsigneeAddressByIdAndUserId(8, 23);
//			caService.deleteConsigneeAddressByIdAndUserId(8, 0);
//			return;
//		default:
//			return;
//		}
//	}
//	
//	private void testDao(OpType ot) {
//		switch(ot) {
//		case ADD:
//			caDao.addObject(createConsigneeAddress(27, 2));
//			caDao.addObject(createConsigneeAddress(27, 5));
//			return;
//		case QUERY:
//			System.out.println(JsonUtils.toJson(caDao.getDefaultByUserId(27)));
//			System.out.println(JsonUtils.toJson(caDao.getConsigneeAddressListByUserId(27)));
//			return;
//		case UPDATE:
//			caDao.updateDefault(10, 27, true);
//			ConsigneeAddress ca = caDao.getDefaultByUserId(27);
//			ca.setConsigneeName("CC");
//			caDao.updateConsigneeAddress(ca);
//			return;
//		case DELETE:
//			caDao.deleteConsigneeAddress(7, 2);
//			return;
//		default:
//			return;
//		}
//	}
//	
//	private ConsigneeAddress createConsigneeAddress(long userId, long tag) {
//		ConsigneeAddress ca = new ConsigneeAddress();
//		ca.setUserId(userId);
//		ca.setProvince("浙江-" + tag);
//		ca.setCity("杭州-" + tag);
//		ca.setSection("滨江-" + tag);
//		ca.setAddress("江汉路1785号-" + tag);
//		// ca.setZipcode("邮政编号-" + tag);
//		ca.setConsigneeName("BB-" + tag);
//		ca.setConsigneeTel("固定电话-" + tag);
//		ca.setConsigneeMobile("手机-" + tag);
//		return ca;
//	}
//	
//}