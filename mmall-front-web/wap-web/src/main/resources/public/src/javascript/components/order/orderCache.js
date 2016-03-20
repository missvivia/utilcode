/**
 *
 * 订单缓存实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
NEJ.define([
    'base/klass',
    'base/util',
    'lib/util/ajax/rest',
    'util/cache/abstract',
    'pro/extend/config',
    'util/chain/NodeList'
],function(_k,_u,_j,_t,_config,$,_p){
    var _pro;
    // 自定义列表缓存
    _p._$$OrderCache = _k._$klass();
    _pro = _p._$$OrderCache._$extend(_t._$$CacheListAbstract);

    // 实现数据载入逻辑
    _pro.__doLoadList = function(_options){
        var _self = this;
    	var _onload = _options.onload;
        // 补全请求数据，也可在模块层通过cache参数传入
        var _data = _options.data||{};
        _u._$merge(_data,{limit:_options.limit,offset:_options.offset});
        switch(_options.key){
          default :
            var _url = _options.ext.url;
            // TODO load list from server
            _j._$request(_url,{
               cookie:!0,
               data:_data,
               onload:function(_json){
            	   _onload(_json.code==200?_json.result.list:[]);
            	   if(_json.result.total > 0){
	            	   var currentPage = _options.offset/_options.limit + 1;
	                   var totalPage = Math.ceil(parseFloat(_json.result.total)/_options.limit);
	                   $("#page span")._$text(currentPage + "/" + totalPage);
	                   $("#page")._$style("display","block");
	                   setTimeout(function(){
	                	   $("#page")._$style("display","none");
	                   },1000);
            	   }
               },
               onerror:_onload._$bind(null,[])
           });
          break;
        }
    };
});
