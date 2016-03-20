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
    'util/template/tpl', 
    './util/lazy.js', 
    './frame/topbar.js', 
    './frame/banner.js', 
    './BaseComponent.js'
], function(_, _k, _e, _t, _t1, _t2, _f1,_f2, BaseComponent, _p, _o, _f, _r, _pro) {
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
        // init banner slider show
        var _parent = _e._$get('banner-box');
        if (!!_parent) {
            this.__banner = _f2._$$FrmBanner._$allocate({
                parent : _parent
            });
        }
        this.__super();
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
