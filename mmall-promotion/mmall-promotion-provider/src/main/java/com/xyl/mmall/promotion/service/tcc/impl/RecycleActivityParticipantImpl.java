package com.xyl.mmall.promotion.service.tcc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.backend.tcc.error.ParticipantException;
import com.xyl.mmall.framework.util.TCCUtil;
import com.xyl.mmall.promotion.service.tcc.RecycleActivityParticipant;
import com.xyl.mmall.promotion.service.tcc.RecycleTCCService;

/**
 * @author dingmingliang
 * 
 */
@Service("recycleActivityParticipant")
public class RecycleActivityParticipantImpl implements RecycleActivityParticipant {

	@Autowired
	private RecycleTCCService recycleTCCService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.backend.tcc.Participant#cancel(java.lang.Long)
	 */
	public void cancel(Long uuid) throws ParticipantException {
		TCCUtil.checkUUID(uuid);
		boolean isSucc = recycleTCCService.cancelAddRecycleTCC(uuid);
		if (!isSucc) {
			String message = "RecycleActivity Cancel Fail!";
			short code = 101;
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
		boolean isSucc = recycleTCCService.confirmAddRecycleTCC(uuid);
		if (!isSucc) {
			String message = "RecycleActivity Confirm Fail!";
			short code = 151;
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
		boolean isSucc = recycleTCCService.cancelAddRecycleTCC(uuid);
		if (!isSucc) {
			String message = "RecycleActivity Expired Fail!";
			short code = 101;
			throw new ParticipantException(message, code);
		}
	}

}
