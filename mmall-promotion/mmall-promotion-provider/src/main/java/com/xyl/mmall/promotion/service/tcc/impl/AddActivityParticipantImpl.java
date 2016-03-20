package com.xyl.mmall.promotion.service.tcc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.backend.tcc.error.ParticipantException;
import com.xyl.mmall.framework.util.TCCUtil;
import com.xyl.mmall.promotion.service.tcc.ActivityTCCService;
import com.xyl.mmall.promotion.service.tcc.AddActivityParticipant;

/**
 * @author dingmingliang
 * 
 */
@Service("addActivityParticipant")
public class AddActivityParticipantImpl implements AddActivityParticipant {

	@Autowired
	private ActivityTCCService activityTCCService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.backend.tcc.Participant#cancel(java.lang.Long)
	 */
	public void cancel(Long uuid) throws ParticipantException {
		TCCUtil.checkUUID(uuid);
		boolean isSucc = activityTCCService.cancelAddActivityTCC(uuid);
		if (!isSucc) {
			String message = "AddActivity Cancel Fail!";
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
		boolean isSucc = activityTCCService.confirmAddActivityTCC(uuid);
		if (!isSucc) {
			String message = "AddActivity Confirm Fail!";
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
		boolean isSucc = activityTCCService.cancelAddActivityTCC(uuid);
		if (!isSucc) {
			String message = "AddActivity Expired Fail!";
			short code = 101;
			throw new ParticipantException(message, code);
		}
	}

}
