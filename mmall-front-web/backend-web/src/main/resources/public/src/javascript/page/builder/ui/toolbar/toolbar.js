/*
 * ------------------------------------------
 * 组件工具栏控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'ui/base',
    'util/template/tpl',
    'util/template/jst',
    'util/toggle/toggle',
    'text!./toolbar.html'
],function(_k,_v,_e,_u,_i,_t,_x,_y,_html,_p,_o,_f,_r,_pro){
    /**
     * 组件工具栏控件
     */
    _p._$$ToolBar = _k._$klass();
    _pro = _p._$$ToolBar._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__gopt = {
            clazz:'j-bd-toggle',
            ontoggle:this.__doToggleVisible._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = (function(){
        var _imap = {
            edit:{c:'pencil',t:'设置'},
            sort:{c:'move j-move',t:'拖拽调整顺序'},
            view:{c:'eye-open',t:'显示'},
            remove:{c:'remove',a:'delete',t:'删除'}
        };
        return function(_options){
            this.__super(_options);
            // init event
            this.__doInitDomEvent([[
                this.__nact,'click',
                this.__onAction._$bind(this)
            ]]);
            
            this.__nttl.innerText = _options.title||'组件标题';
            // init actions
            _x._$render(
                this.__nact,
                this.__seed_acts,{
                    xlist:_options.acts||_r,
                    xmap:_imap
                }
            );
            this.__doInitVisible();
            this._$dispatchEvent(
                'onactbuild',{
                    parent:this.__nact
                }
            );
        };
    })();
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this._$dispatchEvent(
            'onactdestroy',{
                parent:this.__nact
            }
        );
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed = _t._$parseUITemplate(_html);
        return function(){
            this.__seed_html = _seed.mdl;
            this.__seed_acts = _seed.act;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - title show
        // 1 - actions list
        var _list = _e._$getChildren(this.__body);
        this.__nttl = _list[0];
        this.__nact = _list[1];
    };
    /**
     * 菜单操作
     * @param {Object} _event
     */
    _pro.__onAction = function(_event){
        var _action = _e._$dataset(
            _v._$getElement(
                _event,'d:action'
            ),'action'
        );
        if (!!_action){
            this._$dispatchEvent(
                'on'+_action
            );
        }
    };
    /**
     * 初始化可见性切换
     */
    _pro.__doInitVisible = function(){
        var _list = _e._$getByClassName(
            this.__nact,'i-view'
        );
        if (!_list||!_list.length) return;
        this.__nshow = _list[0];
        _y._$toggle(this.__nshow,this.__gopt);
    };
    /**
     * 可见性切换
     */
    _pro.__doToggleVisible = function(_event){
        this._$setVisible(!_event.toggled);
    };
    /**
     * 设置可见属性
     * @param {Object} _visible
     */
    _pro._$setVisible = function(_visible){
        if (!this.__nshow) return;
        var _parent = this.__nshow.parentNode;
        if (!_visible){
            // hidden
            _e._$addClassName(
                _parent,this.__gopt.clazz
            );
            _e._$replaceClassName(
                this.__nshow,
                'glyphicon-eye-close',
                'glyphicon-eye-open'
            );
            this.__nshow.title = '显示';
        }else{
            // visible
            _e._$delClassName(
                _parent,this.__gopt.clazz
            );
            _e._$replaceClassName(
                this.__nshow,
                'glyphicon-eye-open',
                'glyphicon-eye-close'
            );
            this.__nshow.title = '隐藏';
        }
    };
    /**
     * 获取可见属性
     * @return {Boolean}
     */
    _pro._$getVisible = function(){
        return !_e._$hasClassName(
            this.__nshow.parentNode,
            this.__gopt.clazz
        );
    };
    
    return _p;
});
