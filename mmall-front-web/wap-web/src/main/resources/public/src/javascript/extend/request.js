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
    '{lib}util/ajax/xdr.js',
    'pro/extend/config'
    ], function( progress , rest, xdr,config) {


  /**
   * 平台request, 避免后续需要统一处理
   * opt:  其他参数如 $request
   *   - progress:  是否使用进度条提示(假)
   *   - norest:  是否 不使用REST接口
   */
  var noop = function(){};
  var request = function(url, opt){
    opt = opt || {};
//    if(url.indexOf('http://')==-1){
//    	url = config.MAINSITE+url;
//    }
    opt.cookie = opt.cookie!=null?opt.cookie:!0;
    var olderror = typeof opt.onerror === "function" ? opt.onerror : noop,
      oldload = typeof opt.onload === "function"? opt.onload : noop;

    if(opt.progress){
      progress.start();
    }
    opt.onload = function(json){
      if(json && json.code>=200 && json.code < 400 ){
        progress.end();
        oldload.apply(this, arguments);
      }else{
        progress.end(true)
        olderror.apply(this, arguments);
      }
    }
    opt.onerror = function(json){
      progress.end(true)
      olderror.apply(this, arguments);
    }
    if (!opt.method||opt.method.toLowerCase() == 'get'){
      opt.data = opt.data || {};
      opt.data.t = +new Date();
    }
    if(opt.norest){
      xdr._$request(url, opt)
    }else{
      rest._$request(url, opt)
    }
  }
  return request;
})