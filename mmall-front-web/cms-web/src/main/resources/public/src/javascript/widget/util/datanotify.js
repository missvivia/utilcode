/*
 * ------------------------------------------
 * 数据改变通知接口
 * @version  1.0
 * @author   hzzhangzhoujie@corp.netease.com
 * ------------------------------------------
 */
NEJ.define([
  'pro/extend/util',
  'base/klass',
  'base/element',
  'base/event',
  'util/event',
  'util/event/event',
  'pro/extend/request'
], function (_, _k, _e, _v, _t, _t1, _request, _p, _o, _f, _r, _pro) {
  /**
   * 项目模块基类
   *
   * @class   _$$Module
   * @extends _$$EventTarget
   */
  _p._$$DataNotify = _k._$klass();
  _pro = _p._$$DataNotify._$extend(_t._$$EventTarget);

  _t1._$$CustomEvent._$allocate({
    element: _p._$$DataNotify,
    event: 'barchange'
  });

  /**
   * 推送日期
   * @param _data
   * @public
   */
  _p._$pushDateTime = function(_data){
    _v._$dispatchEvent(_p._$$DataNotify,'barchange', _data);
  };

  return _p;
});
