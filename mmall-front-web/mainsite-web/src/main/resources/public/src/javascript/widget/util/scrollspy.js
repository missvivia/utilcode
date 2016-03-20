/**
 * 购物车结算栏float
 * @author hzwuyuedong@corp.netease.com
 *
 */
NEJ.define([
  '{pro}extend/util.js',
  'base/klass',
  'base/element',
  'base/event',
  'base/util',
  'util/event',
  'util/effect/effect.api'
], function (_, _k, _e, _v, _u, _t, _t0, _p, _o, _f, _r) {
  var _pro;

  _p._$$ScrollSpy = _k._$klass();
  _pro = _p._$$ScrollSpy._$extend(_t._$$EventTarget);

  _pro.__reset = function (_options) {
    this.__body = _e._$get(_options.target);
    this.__floatBar = _e._$getByClassName(this.__body, 'j-flag')[0]
    this.__doInitDomEvent([
      [window, 'scroll', _._$throttle._$bind(this, this.__position,this)],
      [window, 'resize', _._$throttle._$bind(this, this.__position,this)]
    ]);
    this.__position();
  };

  _pro.__position = function () {
    var _offset = _e._$offset(this.__body),
      _box = _e._$getPageBox();
    if(_offset.y > (_box.clientHeight+_box.scrollTop)){
      _e._$addClassName(this.__floatBar, 'j-float');
    }else{
      _e._$delClassName(this.__floatBar, 'j-float');
    }
  };

  _pro._$updatewgt = function(){
    this.__position();
  };

  return _p;
})