/**
 * Created by wuyuedong on 2014/10/9.
 */


define([
  '{lib}base/klass.js',
  '{lib}util/template/tpl.js',
  '{lib}base/element.js',
  '{lib}base/event.js',
  '{pro}widget/layer/window.js',
  'text!./alert.html'
],function(_k, _t, _e, _v, _BaseWindow, html, _p){
  var _pro,
    _seed_html= _t._$addNodeTemplate(html);

  /**
   * 项目窗体基类
   *  onok            回调
   *  text         内容（可以是html）
   */
  _p._$$AlertWindow = _k._$klass();
  _pro = _p._$$AlertWindow._$extend(_BaseWindow);

  /**
   * 控件重置
   * @param {Object} _options
   */
  _pro.__reset = function(_options){
    _options.title = _options.title || '&nbsp;';
    _options.clazz +=' m-window-alert';
    this.__super(_options);
    this.__setContent(_options);
  };

  /**
   * 初使化UI
   */
  _pro.__initXGui = function() {
    this.__seed_html = _seed_html;
  };

  /**
   * 初使化节点
   */
  _pro.__initNode = function() {
    this.__super();
    var _list = _e._$getByClassName(this.__body, 'j-flag');
    this.__contentBox = _list[0];
    this.__okBtn = _list[1];
    _v._$addEvent(this.__okBtn, 'click', this.__onOK._$bind(this));
  };

  /**
   * 设置内容
   */
  _pro.__setContent = function(_options){
    this.__contentBox.innerHTML = _options.text;
  };

  /**
   * ok回调
   */
  _pro.__onOK = function(){
    this._$dispatchEvent('onok');
    this._$recycle();
  };

  /**
   * destory
   */
  _pro.__destory = function() {
    this.__super();
    this.__contentBox.innerHTML = '';
    //_e._$remove(this.__content, false);
  };

  return _p;
});
