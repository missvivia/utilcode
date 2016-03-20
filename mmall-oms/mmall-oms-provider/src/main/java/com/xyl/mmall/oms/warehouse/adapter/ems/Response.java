package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
	public static final String KEY_SUCCESS = "T";
	
	public static final String KEY_FARILURE = "F";
	
    private String success;
    
    private String reason;
    
    public Response() {
    }

    public Response(String success) {
        this.success = success;
    }

    public Response(String success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    
    public boolean isSuccess(){
    	return KEY_SUCCESS.equals(getSuccess());
    }
    
    public boolean isFailure(){
    	return KEY_FARILURE.equals(getSuccess());
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
