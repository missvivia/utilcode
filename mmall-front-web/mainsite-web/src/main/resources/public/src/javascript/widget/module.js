/*
 * ------------------------------------------
 * 项目模块基类实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/extend/util', 
    'base/klass', 
    'base/element', 
    'util/event',
    'util/chain/chainable',
    'util/template/tpl', 
    'util/lazy/image', 
    'pro/widget/frame/topbar', 
    'pro/widget/frame/sidebar/sidebar', 
    'pro/widget/frame/banner', 
    'pro/page/activity/lottery',
    'pro/widget/BaseComponent',
    'pro/extend/request',
    'pro/widget/layer/login/login',
    'pro/widget/ga'
], function(_, _k, _e, _t, $, _t1, _t2, _f1, _f2, _f3,_l,BaseComponent, _request,_Login,_ga, _p, _o, _f, _r) {
    var _pro;
    /**
     * 项目模块基类
     *
     * @class   _$$Module
     * @extends _$$EventTarget
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_t._$$EventTarget);

    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function() {
        // parse template
        _t1._$parseTemplate('template-box');
        // init page topbar
        var _parent = _e._$get('topbar-box');
        if (!!_parent) {
            this.__topbar = _f1._$$FrmTopBar._$allocate({
                parent : _parent
            });
        }
        // init page sidebar
        var _parent = _e._$get('sidebar-box');
        if (!!_parent) {
            this.__sidebar = _f2._$$FrmSideBar._$allocate({
                parent : _parent
            });
            this.__sidebar._$show();
        }
        //init activity module
        var _parentBar = _e._$get('activity-countdown');
        if(!!_parentBar){
        	_request('/preheat/activityend',{
                query:"t="+new Date(),
                onload:function(_data){
                	if(_data&&_data.result == false){
                		this.__activityBar=_l._$$LotteryModule._$allocate({
                    		parent : _parentBar
                    	});
                	}
                	
                }._$bind(this),
                onerror:function(_result){
//                	console.log(_result);
                }
              });
        	
        }
        // init banner slider show
        var _parent = _e._$get('banner-box');
        if (!!_parent) {
            this.__banner = _f3._$$FrmBanner._$allocate({
                parent : _parent
            });
        }

        var _service = $('.j-service-btn');
        if(!!_service && _service.length >0 ){
          _service._$on('click', function(event){
            if(!_.isLogin()){
              _Login._$$LoginWindow._$allocate({
                parent: document.body,
                redirectURL: window.location.href
              })._$show();
              return;
            }
            _._$openKefuWin();
          })
        }
        this.__super();
        _ga._$$GA._$allocate();
    };
    _pro.__refreshTopbar = function(){
    	if(this.__topbar){
    		this.__topbar._$refresh();
    	}
    };
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options) {
        this.__super(_options);
        if (!_options.noboot) {
            this.__hub = BaseComponent.boot(_options);
        }
        if (!_options.nolazy) {
            // init image lazy loading
            this.__lazy = _t2._$$LazyImage._$allocate({
                attr : 'src',
                oncheck : function(_event) {
                    if (!_e._$hasClassName(_event.target, 'u-loading-1')) {
                        _event.value = 0;
                    }
                },
                onappend : function(_event) {
                    _e._$setStyle(_event.target,'backgroundImage','none');
                    // TODO fadein
                },
                onremove : function(_event) {
                    _e._$setStyle(_event.target,'backgroundImage','');
                    // TODO fadeout
                }
            });
        }
    };
    /**
     * 刷新延时载入
     * @return {Void}
     */
    _pro.__doLazyRefresh = function() {
        if (!!this.__lazy) {
            this.__lazy._$refresh();
        }
    };

    return _p;
});