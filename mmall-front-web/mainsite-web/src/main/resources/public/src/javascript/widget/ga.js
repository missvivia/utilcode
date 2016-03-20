/*
 * ------------------------------------------
 * 顶栏功能实现文件
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/event',
    'base/element',
    'pro/extend/util',
    'base/event'
],function(_k,_u,_t,_e,_,_v,_p,_o,_f,_r,_pro){
    /**
     * 顶栏功能控件封装
     *
     * @class   _$$GA
     * @extends _$$EventTarget
     *  <a data-ganame="xinchonghui" data-gapoint="libao" data-gapage="activitymain" data-login="1"></a>
     */
    _p._$$GA = _k._$klass();
    _pro = _p._$$GA._$extend(_t._$$EventTarget);

    /**
     * 控件初始化
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        _v._$addEvent(document, 'click', this.__onGACheck._$bind(this));
    };
    /**
     * 登录
     * @return {Void}
     */
    _pro.__onGACheck = function(_event){
      var _elm = _v._$getElement(_event,'d:ganame');
      var _gaName = _e._$dataset(_elm,'ganame');
      var _gaPoint = _e._$dataset(_elm,'gapoint');
      var _gaPage = _e._$dataset(_elm,'gapage');
      var _gaLogin = _e._$dataset(_elm,'galogin');
      if(_gaName){
    	  if(parseInt(_gaLogin)){
    		  var _id = _.getFullUserName();
    		  _gaPage += '###'+_id;
    	  }
    	  _gaq.push(['_trackEvent', _gaName, _gaPoint, _gaPage]);
      }
    };

    return _p;
});
