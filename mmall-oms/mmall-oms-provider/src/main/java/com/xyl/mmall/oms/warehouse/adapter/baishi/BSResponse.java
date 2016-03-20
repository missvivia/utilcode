/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.baishi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hzzengchengyuan
 * 
 */
@XmlRootElement(name = "SyncAsnInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BSResponse implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	public static final String FLAG_SUCCESS = "SUCCESS";

	public static final String FLAG_FAILURE = "FAILURE";

	private String flag;

	private String note;

	private String errorCode;

	private String errorDescription;

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isSuccess() {
		return "SUCCESS".equalsIgnoreCase(getFlag());
	}

	public boolean isFailure() {
		return "FAILURE".equalsIgnoreCase(getFlag());
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @param errorDescription
	 *            the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}
