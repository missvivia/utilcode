/**
 * Created by hzzhangzhoujie on 2014/10/08.
 */

define([
  '{lib}base/klass.js',
  '{pro}widget/layer/window.js',
  'text!./login.html',
  'util/template/tpl',
  'pro/widget/ui/login/login',
  'base/element'
],function(_k, _BaseWindow, html, _t, _l, _e, _p){
  // variable
  var _pro,
  	_seed_html= _t._$addNodeTemplate(html);
  /**
   * 登录弹窗
   * 
   * 脚本举例
   * ```javascript
   * NEJ.define([
   *     'pro/widget/layer/login/login'
   * ],function(_l,_p,_o,_f,_r){
   *     var _window = _l._$$LoginWindow._$allocate({
   *         parent:document.body,
   *         redirectURL:window.location.href
   *     });
   *     // 显示窗口，默认实例化后会显示，如果_$hide()后需要手动调用
   *     _window._$show();
   * });
   *
   * @class   {nm.l._$$LoginWindow}
   * @extends {nm.l._$$LWindow}
   *
   * @param     {Object} arg0             -  可选配置参数
   * @property  {String} redirectURL      -  登录回跳地址
   */
  _p._$$LoginWindow = _k._$klass();
  _pro = _p._$$LoginWindow._$extend(_BaseWindow);
  
  /**
   * 控件重置
   * @param {Object} _options
   */
  _pro.__reset = function(_options){
	_options.title = "登录";
    _options.clazz = "m-window-login";
    this.__super(_options);
    this.__redirectURL = _options.redirectURL||window.location.href;
    if(!this.__loginM){
    	this.__loginM = _l._$$LoginModule._$allocate({
        	parent:this.__loginCon,
        	redirectURL:this.__redirectURL
        });
    }
    this.__loginM._$show();
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
	this.__loginCon = _e._$getByClassName(this.__body, 'j-flag')[0];
      
  };  
  /**
   * 显示窗体
   */
  _pro._$show = function(){
    this.__super();
  };

  return _p;
});
