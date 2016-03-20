package com.xyl.mmall.controller;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.task.enums.PlatformType;
import com.xyl.mmall.util.AreaUtils;

/**
 * 消息推送controller
 * @author hzzhaozhenzuo
 *
 */
@Controller
@RequestMapping("/message")
public class MessagePushController {
	@Autowired
	private MessagePushFacade messagePushFacade;
	
	@Autowired
	private static final Logger logger=LoggerFactory.getLogger(MessagePushController.class);
	
	//消息推送示例
//	@RequestMapping(value="/sendMsg",method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO sendMsg(){
//		BaseJsonVO resVo=new BaseJsonVO();
//		long userId=SecurityContextUtils.getUserId();
//		if(userId<=0){
//			resVo.setCode(CodeInfoUtil.FAIL_CODE);
//			return resVo;
//		}
//		
//		long areaId=AreaUtils.getProvinceCode();
//		String userKey=userId+CodeInfoUtil.VERTICAL_PREFIX+areaId;
//		
//		boolean flag=messagePushFacade.pushMessageForPrivate(userKey, PlatformType.ALL_PLATFORM, "hello");
//		logger.info("result:"+flag);
//		resVo.setResult(flag);
//		resVo.setCode(ErrorCode.SUCCESS);
//		
//		return resVo;
//	}
	
	//短信发送示例
//	@RequestMapping(value="/sendSms",method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO sendSms(){
//		BaseJsonVO resVo=new BaseJsonVO();
//		long userId=SecurityContextUtils.getUserId();
//		if(userId<=0){
//			resVo.setCode(CodeInfoUtil.FAIL_CODE);
//			return resVo;
//		}
//		boolean flag=messagePushFacade.sendSms("15372051232", "hello send is ok");
//		logger.info("result:"+flag);
//		resVo.setResult(flag);
//		resVo.setCode(ErrorCode.SUCCESS);
//		
//		return resVo;
//	}
	
	
	/**
	 * 私信获取签名，都以用户id加区域作为唯一的一个签名参数
	 * @return
	 */
	@RequestMapping(value="/getsignature",method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getSignature(){
		BaseJsonVO resVo=new BaseJsonVO();
		long userId=SecurityContextUtils.getUserId();
		if(userId<=0){
			resVo.setCode(CodeInfoUtil.FAIL_CODE);
			return resVo;
		}
		
//		long areaId=AreaUtils.getProvinceCode();
//		String userKey=userId+CodeInfoUtil.VERTICAL_PREFIX+areaId;
		String userKey=String.valueOf(userId);
		
		//获取签名
		Map<String,Object> signatureMap=messagePushFacade.getSignatureByUserKey(userKey);
		resVo.setResult(signatureMap);
		if(signatureMap==null || signatureMap.isEmpty()){
			resVo.setCode(CodeInfoUtil.FAIL_CODE);
			return resVo;
		}
		
		/*logger.info("key:"+userKey);
		for(Entry<String, Object> entry:signatureMap.entrySet()){
			logger.info("map key:"+entry.getKey()+",value:"+entry.getValue());
		}*/
		
		signatureMap.put("user", userKey);
		resVo.setCode(ErrorCode.SUCCESS);
		
		return resVo;
	}
	
}
