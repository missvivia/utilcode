package com.xyl.mmall.oms.warehouse.adapter.sf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * @author hzzengchengyuan
 *
 */
@WebService(targetNamespace = "http://service.warehouse.integration.sf.com/", name = "IOutsideToLscmService")
public interface SFIOutsideService {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "yxToLscmService", targetNamespace = "http://service.warehouse.integration.sf.com/", className = "com.netease.YxToLscmService")
    @WebMethod
    @ResponseWrapper(localName = "yxToLscmServiceResponse", targetNamespace = "http://service.warehouse.integration.sf.com/", className = "com.netease.YxToLscmServiceResponse")
	public java.lang.String yxToLscmService(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    );

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "jyToLscmService", targetNamespace = "http://service.warehouse.integration.sf.com/", className = "com.netease.JyToLscmService")
    @WebMethod
    @ResponseWrapper(localName = "jyToLscmServiceResponse", targetNamespace = "http://service.warehouse.integration.sf.com/", className = "com.netease.JyToLscmServiceResponse")
    public java.lang.String jyToLscmService(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    );

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "outsideToLscmService", targetNamespace = "http://service.warehouse.integration.sf.com/", className = "com.netease.OutsideToLscmService")
    @WebMethod
    @ResponseWrapper(localName = "outsideToLscmServiceResponse", targetNamespace = "http://service.warehouse.integration.sf.com/", className = "com.netease.OutsideToLscmServiceResponse")
    public java.lang.String outsideToLscmService(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    );
}