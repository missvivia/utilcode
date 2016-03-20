/*
 * ------------------------------------------
 * 页面模块基类
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'util/template/tpl',
    'util/dispatcher/module',
    'util/scroll/simple',
    'util/list/page',
    './ui/pager/pager.js',
    './form.js'
],function(_k,_e,_v,_u,_l,_m,_x,_lm,_i,_t,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$ModuleAbstract
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$ModuleAbstract);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(_options){
        this.__super();
        // build body
        this.__body = _e._$html2node(
            _l._$getTextTemplate(_options.tid)
        );
        if (!this.__body){
            this.__body = _e._$create('div');
        }
        if (!this.__mopt) return;
        // 0 - list box
        // 1 - pager box
        var _list = _e._$getByClassName(
            this.__body,'j-mbox'
        );
        this.__mopt.parent = _list[0];
        this.__mopt.pager = {
            parent:_list[1],
            klass:_i._$$Pager
        };
        // check scrollbar
        var _node = _e._$getByClassName(
            this.__body,'j-sbar'
        )[0];
        if (!!_node){
            this.__scrollbar = _x._$$SimpleScroll._$allocate({
                ybar:_node,
                trigger:_node.parentNode,
                parent:this.__mopt.parent
            });
            _e._$setStyle(
                this.__mopt.parent,'maxHeight',
                _e._$getPageBox().clientHeight-200+'px'
            );
        }
        // check select
        var _select = this.__body.getElementsByTagName('select');
        if (!!_select&&!!_select[0]){
            _v._$addEvent(
                _select[0],'change',
                this.__onCatChange._$bind(this)
            );
        }
        // check search form
        var _form = this.__body.getElementsByTagName('form');
        if (!!_form&&!!_form[0]){
            this.__form = _t._$$WebForm._$allocate({
                form:_form[0],
                onsubmit:this.__onSubmit._$bind(this)
            });
        }else{
            this.__onSubmit({widget:'list'});
        }
    };
    /**
     * 提交查询条件
     * @param {Object} _data
     */
    _pro.__onSubmit = function(_data){
        if (!!this.__module){
            this.__module._$recycle();
        }
        this.__mopt.cache.data = _data;
        this.__mopt.cache.lkey = 
            _u._$object2string(_data,'-');
        this.__module = _lm.
            _$$ListModulePG._$allocate(this.__mopt);
    };
    /**
     * 类目变化事件
     * @return {Void}
     */
    _pro.__onCatChange = function(){
        this.__onSubmit(
            this.__form._$data()
        );
    };
    
    return _p;
});
