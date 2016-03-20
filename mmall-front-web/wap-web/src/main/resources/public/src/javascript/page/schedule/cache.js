/*
 * ------------------------------------------
 * 商品列表缓存控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'util/cache/abstract',
    'pro/extend/request'
],function(_k,_t,_req,_p,_o,_f,_r,_pro){
    /**
     * 商品列表缓存控件
     * 
     * 列表标识
     * 
     * * schedule  - 品购页商品列表
     * 
     */
    _p._$$ProductCache = _k._$klass();
    _pro = _p._$$ProductCache._$extend(_t._$$CacheListAbstract);
    /**
     * 从服务器端载入列表，子类实现具体逻辑
     * 
     * @abstract
     * @method   module:util/cache/abstract._$$CacheListAbstract#__doLoadList
     * @param    {Object}   arg0   - 请求信息
     * @property {String}   key    - 列表标识
     * @property {Number}   offset - 偏移量
     * @property {Number}   limit  - 数量
     * @property {String}   data   - 请求相关数据
     * @property {Function} onload - 列表项载入回调
     * @return   {Void}
     */
    _pro.__doLoadList = (function(){
        var _urls = {
            schedule:'/rest/schedule/product'
        };
        return function(_options){
            var _key = _options.key.split('-')[0],
                _callback = _options.onload;
            _req(_urls[_key],{
                method:'POST',
                data:_options.data,
                onload:function(_json){
                    _callback(_json.result);
                },
                onerror:function(_error){
                    _callback(null);
                }
            });
        };
    })();
    
    return _p;
});