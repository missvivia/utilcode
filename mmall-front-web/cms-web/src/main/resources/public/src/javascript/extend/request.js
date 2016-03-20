/*
 * --------------------------------------------
 * 项目内工具函数集合，此页面尽量写注释
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define([
    '{pro}components/progress/progress.js',
    '{lib}util/ajax/rest.js',
    '{lib}util/ajax/xdr.js'
    ], function( progress , rest, xdr) {


  var noop = function(){}
  /**
   * 平台request, 避免后续需要统一处理
   * opt:  其他参数如 $request
   *   - progress:  是否使用进度条提示(假)
   *   - norest:  是否 不使用REST接口
   */
  var request = function(url, opt){
    opt = opt || {};
    var olderror = opt.onerror || noop,
      oldload = opt.onload || noop;
    
    var _et = function(o1, o2 ,override){
        for( var i in o2 ) if( o1[i] == undefined || override){
            o1[i] = o2[i]
           }
        return o1;
     },
     defaultHeaders = {
    	"Content-Type":"application/json; charset=UTF-8",
    	"Form-Token":token
     };
    if(!opt.norest){
      opt.headers = _et(defaultHeaders,opt.headers,true);
    }
    opt.progress && progress.start();
    opt.onload = function(json){
      //@REMOVE
      if((json && json.code < 400 && json.code >=200)){
        opt.progress && progress.end();
        oldload.apply(this, arguments);
      }else{
        opt.progress && progress.end(true);
        olderror.apply(this, arguments);
      }
    }
    opt.onerror = function(json){
      progress.end(true)
      olderror.apply(this, arguments);
    }
    if(!opt.method||opt.method.toLowerCase()=='GET'){
    	if(!opt.data){
    		opt.data = {};
    	}
    	opt.data.t= +new Date();
    }
    if(opt.norest){
      xdr._$request(url, opt)
    }else{
      rest._$request(url, opt)
    }
  }
  return request;
})