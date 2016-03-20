package com.xyl.mmall.order.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.netease.backend.tcc.error.ParticipantException;
import com.xyl.mmall.framework.util.TCCUtil;
import com.xyl.mmall.order.service.CallOffOrderParticipant;
import com.xyl.mmall.order.service.OrderTCCService;

/**
 * @author dingmingliang
 * 
 */
@Service("callOffOrderParticipant")
public class CallOffOrderParticipantImpl implements CallOffOrderParticipant {

	@Resource(name = "orderTCCServiceInternalImpl")
	private OrderTCCService orderTCCServiceInternalImpl;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.backend.tcc.Participant#cancel(java.lang.Long)
	 */
	public void cancel(Long uuid) throws ParticipantException {
		TCCUtil.checkUUID(uuid);
		boolean isSucc = orderTCCServiceInternalImpl.cancelCallOffOrderByTCC(uuid);
		if (!isSucc) {
			String message = "CallOffOrder Cancel Fail!";
			short code = 100;
			throw new ParticipantException(message, code);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.backend.tcc.Participant#confirm(java.lang.Long)
	 */
	public void confirm(Long uuid) throws ParticipantException {
		TCCUtil.checkUUID(uuid);
		boolean isSucc = orderTCCServiceInternalImpl.confirmCallOffOrderByTCC(uuid);
		if (!isSucc) {
			String message = "CallOffOrder Confirm Fail!";
			short code = 150;
			throw new ParticipantException(message, code);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.backend.tcc.Participant#expired(java.lang.Long)
	 */
	public void expired(Long uuid) throws ParticipantException {
		TCCUtil.checkUUID(uuid);
		boolean isSucc = orderTCCServiceInternalImpl.cancelCallOffOrderByTCC(uuid);
		if (!isSucc) {
			String message = "CallOffOrder Expired Fail!";
			short code = 100;
			throw new ParticipantException(message, code);
		}
	}

}
