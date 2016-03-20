package com.xyl.mmall.task.dto;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * 验证码实体.
 * 
 * @author wangfeng
 * 
 */
public class VerificationCode implements Serializable {

	private static final long serialVersionUID = -6418553736648297745L;

	private static final String OWNER_KEY = "gIvEmE5_VC_T0T";

	/** mobile/userId/email etc.. **/
	private Object credential;

	/** 过期时间 **/
	private long expiredTime;

	/** 签名(用于校验) **/
	private String sign;

	public VerificationCode() {
		super();
	}

	/**
	 * 
	 * 
	 * @param credential
	 * @param effectiveTime
	 *            过期时间,以分为单位
	 */
	public VerificationCode(Object credential, int effectiveTime, int verificationCode) {
		super();
		this.credential = credential;
		// 过期时间
		this.expiredTime = System.currentTimeMillis() + effectiveTime * 60 * 1000L;
		this.sign = genSignature(credential, expiredTime, verificationCode);
	}

	public Object getCredential() {
		return credential;
	}

	public void setCredential(Object credential) {
		this.credential = credential;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static String genSignature(Object credential, long expiredTime, int verificationCode) {
		String[] arrays = new String[] { String.valueOf(credential), String.valueOf(expiredTime),
				String.valueOf(verificationCode) };
		Arrays.sort(arrays);
		StringBuilder verifyCode = new StringBuilder(256);
		for (Object parameter : arrays) {
			verifyCode.append(parameter);
			verifyCode.append("&");
		}
		String md5Value = DigestUtils.md5Hex(verifyCode.append(OWNER_KEY).toString());
		return md5Value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectUtil.genToString(this);
	}

}
