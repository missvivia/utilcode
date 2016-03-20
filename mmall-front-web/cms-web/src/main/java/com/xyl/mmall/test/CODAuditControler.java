package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import java.io.IOException;
//
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.netease.print.security.util.SecurityContextUtils;
//import com.xyl.mmall.cms.facade.LeftNavigationFacade;
//import com.xyl.mmall.cms.facade.OrderCODAuditFacade;
//import com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam;
//import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
//import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam.TimeSearchTag;
//import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.CODAuditQueryCategoryListVO;
//import com.xyl.mmall.controller.BaseController;
//import com.xyl.mmall.framework.vo.BaseJsonVO;
//
///**
// * 订单管理：到付审核
// * 
// * @author hzwangjianyi@corp.netease.com
// * @create 2014年9月29日 上午11:05:05
// *
// */
//@Controller
//@RequestMapping("/test/order")
//public class CODAuditControler extends BaseController {
//
//	@Autowired
//	private OrderCODAuditFacade cmsOrderCODFacade;
//	
//	@Autowired
//	private LeftNavigationFacade leftNavigationFacade;
//	
//	/**
//	 * 请求参数Json模板数据
//	 * 
//	 * @param tag
//	 * @return
//	 */
//	@RequestMapping(value = "/sample/cod", method = RequestMethod.GET)
//	@ResponseBody 
//	public Object sampleJson(@RequestParam(value="tag") int tag)  {
//		return CODAuditJsonDeserializor.smapleJson(tag);
//	}
//	
//	/**
//	 * 到付审核页面
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/topay", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "order:topay" })
//	public String getTypeList(Model model) {
//		appendStaticMethod(model);
//		CODAuditQueryCategoryListVO vo = cmsOrderCODFacade.getCmsCODSearchTypeList();
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//		model.addAttribute("data", vo);
//		return "/pages/order/topay";
//	}
//
//	/**
//	 * 按照条件查找到付审核列表
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/topay/getlist", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO getTypeList(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontTimeRangeSearchTypeParam param = CODAuditJsonDeserializor.decodeFrontTimeRangeSearchTypeParam(jsonParam);
//		BaseJsonVO retVO = new BaseJsonVO();
//		retVO.setCode(200);
//		retVO.setMessage("successful");
//		if(param.getTag() == TimeSearchTag.TIME.getTag()) {
//			retVO.setResult(cmsOrderCODFacade.getCODInfoListByTime(param));
//			return retVO;
//		}
//		if(param.getTag() == TimeSearchTag.SEARCH.getTag()) {
//			retVO.setResult(cmsOrderCODFacade.getCODInfoListBySearch(param));
//			return retVO;
//		}
//		retVO.setCode(201);
//		retVO.setMessage("illegal tag:" + param.getTag());
//		return retVO;
//	}
//	
//	/**
//	 * 到付审核：通过
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/topay/pass", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO pass(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontCODAuditOperationParam param = CODAuditJsonDeserializor.decodeFrontCODAuditOperationParam(jsonParam);
//		return auditOperationExec(cmsOrderCODFacade.passAudit(param));
//	}
//	
//	/**
//	 * 到付审核：拒绝
//	 * @param model
//	 * @param param
//	 * @return
//	 */
//	@RequestMapping(value = "/topay/reject", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO reject(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontCODAuditOperationParam param = CODAuditJsonDeserializor.decodeFrontCODAuditOperationParam(jsonParam);
//		return auditOperationExec(cmsOrderCODFacade.rejectAudit(param));
//	}
//	
//	/**
//	 * 到付审核：撤销拒绝
//	 * @param model
//	 * @param param
//	 * @return
//	 */
//	@RequestMapping(value = "/topay/goback", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO cancelReject(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontCODAuditOperationParam param = CODAuditJsonDeserializor.decodeFrontCODAuditOperationParam(jsonParam);
//		return auditOperationExec(cmsOrderCODFacade.cancelReject(param));
//	}
//	
//	/**
//	 * 用户黑名单
//	 * @param model
//	 * @param param
//	 * @return
//	 */
//	@RequestMapping(value = "/topay/addblackuser", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO addBlackUser(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontCODAuditOperationParam param = CODAuditJsonDeserializor.decodeFrontCODAuditOperationParam(jsonParam);
//		return auditOperationExec(cmsOrderCODFacade.addUserToBlack(param));
//	}
//	
//	/**
//	 * 地址黑名单
//	 * @param model
//	 * @param param
//	 * @return
//	 */
//	@RequestMapping(value = "/topay/addblackaddress", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO addBlackAddress(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontCODAuditOperationParam param = CODAuditJsonDeserializor.decodeFrontCODAuditOperationParam(jsonParam);
//		return auditOperationExec(cmsOrderCODFacade.addAddressToBlack(param));
//	}
//	
//	private BaseJsonVO auditOperationExec(boolean successful) {
//		BaseJsonVO retVO = new BaseJsonVO();
//		if(successful) {
//			retVO.setCode(200);
//			retVO.setMessage("successful");
//		} else {
//			retVO.setCode(201);
//			retVO.setMessage("failed");
//		}
//		return retVO;
//	}
//}
