/*
 * ------------------------------------------
 * 布局模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/scroll/smart',
    'util/cache/share',
    'util/toggle/toggle',
    'util/dispatcher/module',
    'pro/page/builder/module'
],function(_k,_v,_e,_t,_d,_y,_x,_m,_p,_o,_f,_r,_pro){
    /**
     * 布局模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 取APP容器节点
     * @return {Void}
     */
    _pro.__doParseParent = function(){
        return document.mbody;
    };
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__super({
            tid:'builder-module-layout'
        });
        // 0 - side box
        // 1 - action box
        // 2 - main box
        // 3 - fold button
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__nact = _list[1];
        this.__export = {
            side:_list[0],
            main:_list[2]
        };
        // toggle fold
        _y._$toggle(_list[3],{
            clazz:'j-fold-toggle',
            ontoggle:this.__onFoldToggle._$bind(this)
        });
        // sync height when resize
        _v._$addEvent(
            window,'resize',
            this.__doSyncHeight._$bind(this)
        );
    };
    /**
     * 显示模块
     * @param {Object} _options
     */
    _pro.__onShow = function(_options){
        this.__super(_options);
        this.__doSyncHeight();
        // init smart scroll
        if (!this.__scroll){
            this.__scroll = _t._$$SmartScroll._$allocate({
                limit:[0,1000],
                viewport:this.__export.main,
                onscrollcheck:this.__onScrollCheck._$bind(this)
            });
        }
    };
    /**
     * 同步滚动容器高度
     * @return {Void}
     */
    _pro.__doSyncHeight = function(_height){
        var _height = _e._$getPageBox().clientHeight;
        // main body
        _e._$setStyle(
            this.__export.main,'height',
            _height-this.__nact.offsetHeight+'px'
        );
        // side scroll body
        var _body = _e._$getByClassName(
            this.__export.side,'j-sbody'
        )[0];
        _e._$setStyle(
            _body,'maxHeight',
            _height-200+'px'
        );
    };
    /**
     * 检查自动滚动
     * @return {Void}
     */
    _pro.__onScrollCheck = function(_event){
        _event.stopped = !_d.localCache._$get('resource');
    };
    /**
     * 侧边栏折叠功能
     * @param {Object} _event
     */
    _pro.__onFoldToggle = function(_event){
        _event.source.title = !_event.toggled
                            ? '隐藏侧边':'显示侧边';
    };
    
    // regist module
    _x._$regist('app',_p._$$Module);
});
