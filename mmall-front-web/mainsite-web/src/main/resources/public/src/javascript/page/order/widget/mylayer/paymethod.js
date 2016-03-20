/*
 * ------------------------------------------
 * 支付选择实现文件
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
/* pro/page/order/widget/mylayer/cancelorder 需要layer.css */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/page/order/widget/mylayer/mylayer',
    'util/template/tpl',
    'util/template/jst',
    'text!./paymethod.html'
],function(_k,_e,_v,_t0,_t1,_t2,_tpl,_p,_o,_f,_r){
    var _pro,
       _html_method = _t2._$add(_tpl);
       
        
    _p._$$PayMethod = _k._$klass();
    _pro = _p._$$PayMethod._$extend(_t0._$$Mylayer);

    _pro.__init = function(_options){
        _options.clazz = _options.clazz || 'w-win w-win-paymethod';
        _options.title = _options.title || '请选择付款方式';
        _options.draggable = false;
        _options.mask = true;
        this.__super(_options);
    };
    /**
     * 重置控件
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__reset = function(_options){
        var _cod = _options.canCod||false;
        _options.cnt = _t2._$get(_html_method,{'canCod':_cod});
        this.__super(_options);
    };

    return _p;
});