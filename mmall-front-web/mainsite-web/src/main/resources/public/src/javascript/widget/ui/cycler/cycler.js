/*
 * ------------------------------------------
 * 循环播放封装实现文件
 * @author   hzwuyuedong@corp.netease.com
 * ------------------------------------------
 */

define([
  'base/klass',
  'base/element',
  'base/event',
  'ui/base',
  'util/page/simple',
  'util/template/jst',
  'base/config',
  'text!./cycler.css',
  'text!./cycler.html'
], function (_k, _e, _v, _i, _t, _t0, _c, _css, _html, _p, _o, _f, _r) {
  var _pro,
    _seed_css = _e._$pushCSSText(_css, {root: _c._$get('root')}),
    _seed_page = _t0._$add(_html);

  _p._$$Cycler = _k._$klass();
  _pro = _p._$$Cycler._$extend(_i._$$Abstract);

  /**
   * 控件初始化
   *
   * @protected
   * @return {Void}
   */
  _pro.__init = function () {
    this.__popt = {
      index: 1,
      onchange: this.__onPageChange._$bind(this)
    };
    this.__super();
  };
  /**
   * 控件重置
   *
   * @protected
   * @param  {Object} arg0 - 可选配置参数
   * @return {Void}
   */
  _pro.__reset = function (_options) {
    this.__super(_options);
    this.__list = _options.list || [];
    this.__interval = (_options.interval || 5) * 1000;
    this.__popt.event = _options.event;
    this.__popt.selected = _options.selected || 'selected';
    this.__popt.total = this.__list.length;
    this.__doGenListXhtml(_options);
    this.__doInitDomEvent([
      [this.__pbtn, 'click', this.__onNextPage._$bind(this, !1)],
      [this.__nbtn, 'click', this.__onNextPage._$bind(this, !0)]
    ]);
    this.__pager = _t._$$PageSimple._$allocate(this.__popt);
  };
  /**
   * 控件销毁
   *
   * @protected
   * @return {Void}
   */
  _pro.__destroy = function () {
    this.__super();
    delete this.__popt;
    this.__pager._$recycle();
    this.__timer = window.clearTimeout(this.__timer);
  };


  /**
   * 初始化外观信息
   *
   * @protected
   * @return {Void}
   */
  _pro.__initXGui = function () {
    this.__seed_css = _seed_css;
    this.__seed_html= _seed_page;
  };

  /**
   * 动态构建控件节点模板
   *
   * @protected
   * @return {Void}
   */
  _pro.__doGenListXhtml = function () {
      var that = this;
    _t0._$render(
      this.__body, that.__seed_html, {data: this.__list}
    );
    var _seed = _t0._$seed();
    this.__popt.list = _e._$getByClassName(
      this.__body,
        'js-i-' + _seed
    );

    this.__cyclerList = _e._$getByClassName(
      this.__body,
        'js-c-' + _seed
    );

    this.__pbtn = (
      _e._$getByClassName(
        this.__body,
          'js-p-' + _seed
      ) || _r
      )[0];
    this.__nbtn = (
      _e._$getByClassName(
        this.__body,
          'js-n-' + _seed
      ) || _r
      )[0];
  };

  /**
   * 页面变化回调
   *
   * @protected
   * @param    {Object} arg0  - 页码信息
   * @property {Number} event - 页码信息
   * @return   {Void}
   */
  _pro.__onPageChange = function (_event) {
    this.__timer = window.clearTimeout(this.__timer);
    this.__timer = window.setTimeout(
      this.__onNextPage._$bind(this, !0), this.__interval);
    this.__doChangeImage(_event);
    //具体切換方式由调用者实现
    this._$dispatchEvent('onchange', _event);
  };

  /**
   * 具体切换逻辑
   *
   * @protected
   * @param    {Object} arg0  - 页码信息
   * @return   {Void}
   */
  _pro.__doChangeImage = function (_event) {
    var _list = this.__cyclerList,
      _last = _list[_event.last - 1],
      _index = _list[_event.index - 1];
    _e._$delClassName(_last, 'item-exposure');
    _e._$addClassName(_index, 'item-exposure');
  };

  /**
   * 去到临近页
   *
   * @protected
   * @param    {Boolean} arg0  - 是否下一页
   * @return {Void}
   */
  _pro.__onNextPage = function (isNext) {
    var _index = this.__pager._$getIndex() - 1,
      _total = this.__pager._$getTotal();
    _index = !!isNext
      ? _index + 1
      : _index - 1;
    this.__pager._$setIndex((_index + _total) % (_total) + 1);
  };

  /**
   * 去到上一页
   *
   * @public
   * @return {Void}
   */
  _pro._$onPrev = function () {
    this.__onNextPage(!1);
  };

  /**
   * 去到下一页
   *
   * @public
   * @return {Void}
   */
  _pro._$onNext = function () {
    this.__onNextPage(!0);
  };

  return _p;
});