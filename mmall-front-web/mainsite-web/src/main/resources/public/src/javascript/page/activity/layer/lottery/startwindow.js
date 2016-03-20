/**
 * Created by hzzhangweidong
 */

define([
  '{lib}base/klass.js',
  'base/element',
  '{pro}widget/layer/window.js',
  'pro/page/activity/layer/lottery/resultwindow',
  'text!./startwindow.html',
  'text!./startwindow.css',
  'pro/extend/request',
  'util/template/tpl',
  'pro/widget/ui/login/login',
  'base/event'
],function(_k,_e,_BaseWindow,ResultWindow, _html,_css,_request, _t, _l, _v, _p){
  // variable
  var _pro,
    _seed_css = _e._$pushCSSText(_css),
    __html = _t._$addNodeTemplate(_html);
  
  _p._$$StartWindow = _k._$klass();
  _pro = _p._$$StartWindow._$extend(_BaseWindow);
  
  /**
   * 控件重置
   * @param {Object} _options
   */
  _pro.__reset = function(_options){
	_options.title = "抽奖";
    _options.clazz = "m-window m-lottery-start-window";
    _options.mask=true;
    this.__options=_options;
    this.__options.boxtype=_options.boxtype||"startbox";
    this.__super(_options);
    _e._$removeByEC(this.__layer.__dopt.mbar);
    var _closeBtn=_e._$getByClassName(this.__layer.__dopt.body,'zcls')[0];
    _e._$removeByEC(_closeBtn);
//    this.__doInitDomEvent([
//       [this.__lottery, 'click', this.__onLotteryClick._$bind(this)]
//    ]);
  };
  
  _pro.__initNode = function () {
	    this.__super();
	    this.__startNode=_e._$getByClassName(this.__body,"m-start")[0];
	    _v._$addEvent(this.__startNode, 'click', this.__onStartClick._$bind(this));
  };
  /**
   * 初使化UI
   */
  _pro.__initXGui = (function(){
     this._seed_css = _seed_css;
     this.__seed_html = __html;
  });
  
  _pro.__onStartClick=function(_event){
	  var that=this;
	  _v._$stop(_event);
	  _e._$addClassName(this.__startNode,'lb-shake');
	  setTimeout(function(){
		  _request('/activity/lottery',{
	          method:'POST',
	          onload:function(_data){
	          	_e._$delClassName(that.__startNode,'lb-shake');
	          	that._$hide();
	          	 that.__onShowResultWindow(_data.result);
	          	 that._$dispatchEvent("onLotteryCallBack",_data.result);
	          },
	          onerror:function(_result){
	        	  _e._$delClassName(that.__startNode,'lb-shake');
	        	  that._$hide();
	          }
	        })
	  },500);
	
	  
  };
  
	_pro.__onShowResultWindow=function(_result){
		this.__resultWindow=ResultWindow._$allocate({result:_result})._$show();
	};  

  return _p._$$StartWindow;
});
