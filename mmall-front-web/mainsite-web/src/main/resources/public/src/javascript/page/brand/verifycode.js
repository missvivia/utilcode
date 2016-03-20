/*
 * ------------------------------------------
 * 品牌故事验证码
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass'
    ,'pro/widget/module'
    ,'util/ajax/xdr'
    ,'util/chain/chainable'
    ,'pro/extend/util'
], function (_k, _m, _j,$,_,_p, _o, _f, _r, _pro) {
    /**
     * 品牌介绍页模块基类
     *
     * @class   _$$Story
     * @extends _$$Story
     */
    _p._$$VerifyCode = _k._$klass();
    _pro = _p._$$VerifyCode._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
        this.__initNode(_options);
        this.__getVerifyCode(_options);
        this.__5minitesClear(_options);
        this.__bindEvents(_options);
    };
    
    _pro.__initNode=function(_options){
    	this.__imageNode=$(".f-ff1")[0];
    	this.__refresh$=$(".f-ff2");
    	this.__submit$=$(".u-btn4");
    	var _forms=$(".j-vcode");
    	this.__formNode=_forms[0];
    	this.__inputId=_forms[1];
    	this.__inputCode=_forms[2];
    	this.__errorNode=$(".j-error")[0];
    };
    
    _pro.__getVerifyCode=function(_options){
    	this.__imageNode.src="/brand/genverifycode?"+"t="+new Date();
    	this.__errorNode.value="";
    };
    
    _pro.__submitVerifyCode=function(_options){
    	var _qs=_._$queryStrings();
    	this.__inputId.value=_qs["id"];
    	this.__inputCode.value=$(".f-ff0")[0].value;
    	this.__formNode.submit();
    };
    _pro.__5minitesClear=function(_options){
    	this.__clear=setInterval(function(){
    		this.__getVerifyCode();
    		$(".f-ff0")[0].value="";
    		this.__errorNode.value="";
    	}._$bind(this),1000*60*5);
    };
    _pro.__bindEvents=function(){
    	this.__refresh$._$on("click",this.__getVerifyCode._$bind(this));
    	this.__submit$._$on("click",this.__submitVerifyCode._$bind(this));
    	var _data=window["verifydata"];
    	if(_data&&_data.info!="ok"){
    		this.__errorNode.innerHTML=_data.info;
    	}else{
    		this.__errorNode.innerHTML="";
    	}
    };


    // init page
    _p._$$VerifyCode._$allocate();
});