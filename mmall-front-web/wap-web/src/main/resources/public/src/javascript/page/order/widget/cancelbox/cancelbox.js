/*
 * ------------------------------------------
 * 取消订单实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
/* pro/page/order/widget/cancelbox/cancelbox */
NEJ.define([
    'base/klass',
    'pro/widget/layer/window',
    'util/template/tpl',
    'util/template/jst',
    'text!./cancelbox.html'
],function(_k,_t0,_t1,_t2,_html,_p,_o,_f,_r){
    var _pro,
        _seed_ui = _t1._$parseUITemplate(_html),
        _seed_box = _seed_ui['cancelbox'],
        _seed_simple = _seed_ui['cancelboxsimple'];

    _p._$$CancelBox = _k._$klass();
    _pro = _p._$$CancelBox._$extend(_t0._$$LWindow);

    /**
     * 初始化方法
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__init = function(_options){
        this.__simple = _options.simple||true;
        this.__data = _options.data;
        this.__super(_options);
    };

    /**
     * 生成结构
     * @return {Void}
     */
    _pro.__initXGui = function(){
        var _seed_html =  this.__simple ? _t2._$get(_seed_simple,this.__data) : _t2._$get(_seed_box,this.__data)
        this.__seed_html = _t1._$addNodeTemplate(_seed_html);
    };

    /**
     * 初始化节点
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__initNode = function(_options){
        this.__super(_options);
    }

});