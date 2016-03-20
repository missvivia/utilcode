package com.xyl.mmall.task.service.impl;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.push.meta.Attachment;
import com.netease.push.meta.AttachmentMessage;
import com.netease.push.meta.PushResult;
import com.netease.push.service.Message;
import com.netease.push.service.MessagePusher;
import com.netease.push.service.PushConst;
import com.xyl.mmall.task.exception.PushException;
import com.xyl.mmall.task.bean.PushSenderConfig;
import com.xyl.mmall.task.bean.PushServiceConfig;
import com.xyl.mmall.task.dao.PushManagementDao;

/**
 * 
 * @author hzjiangww@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Service("pushSenderService")
public class PushSenderService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private MessagePusher messagePusher;

	@Resource(name = "pushServiceConfig")
	private PushServiceConfig pushServiceConfig;

	@Autowired
	protected PushManagementDao pushManagementDao;

	private long expire_time_base = 2592000000L;

	/**
	 * 初始化
	 * 
	 * @throws PushException
	 */
	public void init() throws PushException {
		if (pushServiceConfig == null || pushServiceConfig.isLostConfig()) {
			throw new PushException("push config is null");
		}
		messagePusher = new MessagePusher(pushServiceConfig.getPusherConfiguration());
	}

	/**
	 * 生成签名
	 * 
	 * @param account
	 * @return
	 * @throws PushException
	 */
	public HashMap<String, Object> GenSign(String account) throws PushException {
		if (messagePusher == null)
			init();
		HashMap<String, Object> result = new HashMap<String, Object>();
		long expire_time = System.currentTimeMillis() + expire_time_base;
		String nonce = messagePusher.generateNonce();
		String sign = messagePusher.generateUserAccountSign(pushServiceConfig.getDomain(), account, expire_time, nonce);
		result.put("expire_time", expire_time);
		result.put("nonce", nonce);
		result.put("sign", sign);
		logger.info(pushServiceConfig.getDomain()+ "|"+ account+"|"+expire_time+"|"+ nonce);
		return result;
	}

	/**
	 * 未完成的接口
	 * 
	 * @param message
	 * @param platform
	 * @throws PushException
	 */
	public boolean pushMessage(String userId, PushSenderConfig config) throws PushException {
		if (messagePusher == null)
			init();
		Message m = genMessage(config);
		logger.info("send msg " + m.toString());
		String platform = config.getPlatform();
		PushResult result = messagePusher.pushSpecifyMessageWithRet(m, platform, pushServiceConfig.getDomain(), userId,
				config.getTtl(), config.isOffline(), config.getFilter());

		if (result.isSuccessFul()) {
			logger.info("send msg suc! msgId = " + result.getMsgId());
			return true;
		} else {
			logger.info("send msg fail! msgId= " + result.getMsgId() + ", errorReason = " + result.getErrorReason());
			return false;
		}
	}

	private Message genMessage(PushSenderConfig config){
		Message m = new Message();
		m.setTitle(config.getTitle());
		m.setContent(config.getContent());
		m.setSummary(config.getSummary());
		m.setAlert(config.getAlert());
		m.setNotifyType(PushConst.CUT_OFF_ALL);
		return m;
	}
	/**
	 * 发送带有文件的
	 * 
	 * @param config
	 * @throws PushException
	 */
	public void pushGroupMessage(PushSenderConfig config, String AttachPath, String areaId) throws PushException {
		if (messagePusher == null)
			init();
		Message m = genMessage(config);
		String platform = config.getPlatform();
		File f = new File(AttachPath);
		if (!f.exists()) {
			throw new PushException("attach file does not exists");
		}
		Attachment attachment = new Attachment(f, config.getAttachType());
		AttachmentMessage attachMsg = new AttachmentMessage(m, platform, pushServiceConfig.getDomain(),
				config.getTtl(), config.isOffline(), config.getFilter(), attachment);
		// 异步发送
		logger.info("message send ->" + config.getContent() + " use area:" + areaId);
		messagePusher.pushAttachmentMessageAsyn(attachMsg, new PushCallBackListener(config, pushManagementDao, areaId));
	}

}
