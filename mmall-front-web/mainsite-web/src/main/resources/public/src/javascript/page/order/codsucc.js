/*
 * ------------------------------------------
 * 订单支付结果页
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/extend/util',
    'base/event',
    'base/element',
    'pro/widget/module'
],function(_k,_, _v, _e, _w,_p,_o,_f,_r){
    var _pro;

    _p._$$Index = _k._$klass();
    _pro = _p._$$Index._$extend(_w._$$Module);

    _pro.__reset = function(){
      _v._$addEvent(_e._$get('servicebtn'), 'click', function(event){
        _v._$stop(event);
        _._$openKefuWin();
      });
    };
    _p._$$Index._$allocate({});

    return _p;
});