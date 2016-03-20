/**
 * Created by hzzhangzhoujie on 2014/10/08.
 */

define([
  '{lib}base/klass.js',
  '{pro}widget/layer/window.js',
  'text!./usedDetail.html',
  'text!./usedDetail.css',
  'util/template/tpl',
  'pro/widget/ui/login/login',
  'base/element',
  'base/event',
  'util/template/tpl',
  'util/template/jst'
],function(_k, _BaseWindow, _html, _css, _t, _l, _e, _v, _t1, _t2, _p){
  // variable
  var _pro,
    _seed_css = _e._$pushCSSText(_css),
  	_seed_html= _t2._$add(_html);
  /**
   * 登录弹窗
   * 
   * 脚本举例
   * ```javascript
   * NEJ.define([
   *     'pro/widget/layer/redpacket/usedDetail'
   * ],function(_l,_p,_o,_f,_r){
   *     var _window = _l._$$UsedDetailWindow._$allocate({
   *         parent:document.body,
   *     });
   *     // 显示窗口，默认实例化后会显示，如果_$hide()后需要手动调用
   *     _window._$show();
   * });
   *
   * @class   {nm.l._$$UsedDetailWindow}
   * @extends {nm.l._$$LWindow}
   *
   * @param     {Object} arg0             -  可选配置参数
   */
  _p._$$UsedDetailWindow = _k._$klass();
  _pro = _p._$$UsedDetailWindow._$extend(_BaseWindow);
  
  /**
   * 控件重置
   * @param {Object} _options
   */
  _pro.__reset = function(_options){
	_options.title = "使用详情";
    _options.clazz = "m-window";
    this.__super(_options);
    this.__resetBody(_options.data);
  };
  /**
   * 重置body
   */
  _pro.__resetBody = function(_data) {
    var _list  = [],
        _listD = _data.dtos||[];
    for(var i=0,len=_listD.length;i<len;i++){
      if(_listD[i].cash!=0){
        _list.push(_listD[i]);
      }
    }
    _data.dtos = _list;
	  _t2._$render(this.__body,_seed_html,_data);
	  var list = _e._$getByClassName(this.__body, 'j-flag');
	  _v._$addEvent(list[0], 'click', this.__onOKClick._$bind(this));
  };
  /**
   * 初使化UI
   */
  _pro.__initXGui = function() {
	  //this.__seed_html = _seed_html;
	  this.__seed_css = _seed_css;
  };
  
  _pro.__onOKClick = function(event) {
		_v._$stop(event);
		this._$dispatchEvent('onok');
		this._$hide();
	};

  return _p;
});
