package com.xyl.mmall;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.push.config.PusherConfiguration;
import com.netease.push.http.ClientConfiguration;
import com.netease.push.service.MessagePusher;
import com.xyl.mmall.framework.codeinfo.MessageCodeInfo;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.task.enums.PlatformType;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.task.service.DriverService;
import com.xyl.mmall.task.service.PushManagementService;
import com.xyl.mmall.task.service.PushService;
import com.xyl.mmall.task.service.PushTaskService;

/**
 * Hello world!
 * 
 */
@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
	PushManagementService pushManagementService;
	@Autowired
	DriverService driverService;
	@Autowired
	PushService pushService;
	@Autowired
	PushTaskService pushTaskService;

	private static final String domain="mmall.163.com";//域名
	
	private static final String destUid = "475";
	
	private static final String appSecret="17ad84a5a84344f7bde055d642de23c0";//productSecret
	
	private static final  String appKey="7c48f34241f94364b84432a8794e94e7";//productKey
	
	private static final  String proxyUrl="http://114.113.202.192:8080/push-proxy/push";//Proxy地址
	
	@RequestMapping(value="/sendPushBiz",method = RequestMethod.GET)
	public void sendPushBiz(){
		Map<String, Object> otherParamMap=new HashMap<String, Object>();
		otherParamMap.put(MessageCodeInfo.USER_FORM_ID, 25685L);
		pushService.pushForAll(15732L, PushMessageType.send_order, 27358L, otherParamMap);
	}
	
	@RequestMapping(value="sendSms",method = RequestMethod.GET)
	public void sendSms(){
		pushService.sendSms("15372051232", "新年");
		System.out.println("ok");
	}
	
	@RequestMapping(value="sendMail",method = RequestMethod.GET)
	public void sendMail(){
		//pushService.sendMail(MailType.NORMAL, "hzzhaozhenzuo@corp.netease.com", "hello", "mail");
	}
	
	@RequestMapping(value="bind",method = RequestMethod.GET)
	public void bind(){
		MessagePusher messagePusher=this.getPushProducer();
	    long expire_time = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000;
	    String nonce = messagePusher.generateNonce();
	    String signature = messagePusher.generateUserAccountSign(domain,destUid,expire_time,nonce);
	    System.out.println(signature);
	}
	
	private MessagePusher getPushProducer(){
		//key/secret配置
	    PusherConfiguration pushConfig = new PusherConfiguration(domain,appKey,appSecret,proxyUrl);
	    
	    //连接池配置
	    ClientConfiguration clientConfig = new ClientConfiguration();
	    clientConfig.setConnectionTimeout(20 * 1000);//注意,单位是ms
	    clientConfig.setMaxConnections(50);
	    clientConfig.setMaxPreRoute(20);
	    pushConfig.setClientConfiguration(clientConfig);
	    
	    //线程池配置
	    ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 10, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(200));
	    pushConfig.setAsyncPusherPool(pool);
	    
	    //使用PusherConfiguration新建实例
	    MessagePusher messagePusher = new MessagePusher(pushConfig);
	    return messagePusher;
	}
	
	@RequestMapping(value="/sendMsg",method = RequestMethod.GET)
	public void sendMsg(@RequestParam String userId){
//		MessagePusher messagePusher=this.getPushProducer();
//		Message message = new Message("I am content");
//		message.setTitle("a");
//		message.setAlert("alert");
//	    String platform = PushConst.WEB;
//	    long ttl = 604800; 
//	    boolean offline = true; 
//	    Map<String,Object> filter = new HashMap<String,Object>();
//	    PushResult result = messagePusher.pushSpecifyMessageWithRet(message,platform,domain, destUid, 
//	    ttl, offline, filter);
//	    if(result.isSuccessFul()) {
//		    System.out.println("send msg suc! msgId = " + result.getMsgId());
//	    } else {
//		    System.out.println("send msg fail! msgId= " + result.getMsgId() + ", errorReason = " + result.getErrorReason());
//	    }
		
		boolean flag=pushService.pushMessageForPrivate(userId, PlatformType.WEB, "hello");
		System.out.println(flag);
		
	}
	

	@RequestMapping(value = "/bindPush", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO genKey(@RequestParam(value = "deviceId", required = false)String deviceId,@RequestParam(value = "userId", required = false)String userId
			,@RequestParam(value = "latitude", required = false)Double latitude,@RequestParam(value = "longitude", required = false)Double longitude) {
		String a = deviceId;
		if(StringUtils.isNotBlank(userId))
			a = userId;
		System.out.println("1----" + deviceId + "--> x:" + latitude + " y:" + longitude);
		Map key = driverService.genSign(a);
		System.out.println(key);
		return new BaseJsonVO(key);
	}

	@RequestMapping(value = "/pushTest", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO push(@RequestParam(value = "userId", required = false)Long userId) {
		System.out.println("----------" + userId);
		for(int i = 1;i< 6;i++)
		 pushService.push(userId, i, "test" + i, "恭喜你中奖了" + i, 9527);
		return new BaseJsonVO();
	}
	@RequestMapping(value = "/pushTest2", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO push2(@RequestParam(value = "userId", required = false)Long userId,@RequestParam(value = "type", required = false)Integer type,@RequestParam(value = "orderId", required = false)Long orderId) {
		System.out.println("----------" + userId);
	
		 pushService.push(userId, type, "give2", "monk2ey", orderId);
		return new BaseJsonVO();
	}
	
	@RequestMapping(value = "/start1", method = RequestMethod.GET)
	public String start1() {
		PushManagement pushManagement = new PushManagement();
		pushManagement.setAreaCode("33");
		pushManagement.setContent("this is an test message");
		pushManagement.setOperator("system");
		pushManagement.setPlatformType("ios,android");
		pushManagement.setPushSuccess(0);
		pushManagement.setPushTime(System.currentTimeMillis() + 10*1000);
		pushManagement.setTitle("hello");
		pushManagement.setUpdateTime(System.currentTimeMillis() );
		pushManagement.setLink("http://www.163.com");
		pushManagementService.addPushConfig(pushManagement);
		return "success";
	}
	@RequestMapping(value = "/test2", method = RequestMethod.GET)
	public String test2() {
		
		pushTaskService.push(System.currentTimeMillis(), System.currentTimeMillis() + 60*60*1000);
		 return null;
	}
	
	
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public String start() {
		pushTaskService.push(System.currentTimeMillis(),System.currentTimeMillis() + 1000000000);
		return "success";
	}

}
