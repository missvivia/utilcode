/**
 * 用户信息详情
 * author cheng-lin(cheng-lin@corp.netease.com)
 *
 */

NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/widget/module',
    'pro/page/userinfo/user/user',
    '{pro}page/userinfo/address/address.js?v=1.0.0.0',
    'pro/page/userinfo/ticket/ticket',
    'pro/page/userinfo/red/red',
    'pro/page/userinfo/order/order'
  ],
  function(_k,_e,_v,_$$Module,_i0,_i1,_i2,_i3,_i4,_p,_o,_f,_r) {
    var _pro;

    _p._$$UserModule = _k._$klass(),
    _pro = _p._$$UserModule._$extend(_$$Module);

    _pro.__init = function(_options) {
      this.__super(_options);
    };


    _p._$$UserModule._$allocate({
      data: {}
    });

    return _p;
  });