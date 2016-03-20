/*
 * ------------------------------------------
 * 自定义组件基类
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'ui/base',
    'util/template/tpl',
    '../../cache/widget.js',
    '../../ui/toolbar/toolbar.js',
    'text!./widget.html'
],function(_k,_e,_v,_u,_i,_l,_d,_b,_html,_p,_o,_f,_r,_pro){
    /**
     * 自定义组件基类
     * @class   _$$Widget
     * @extends _$$Abstract
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Abstract);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init id and type
        this.__type = _options.type;
        this.__id = _options.id||_u._$uniqueID();
        _e._$dataset(this.__body,'id',this.__id);
        _e._$dataset(this.__body,'type',this.__type);
        // init event
        this.__doInitDomEvent([[
            this.__line,'dblclick',
            this.__onResetSpace._$bind(this)
        ],[
            this.__wrap,'click',
            this.__onAction._$bind(this)
        ]]);
        // init space top
        if (_options.spaceTop!=null){
            _e._$setStyle(
                this.__body,'paddingTop',
                _options.spaceTop+'px'
            );
        }
        if (!!this.__tbop){
            this.__toolbar = _b._$$ToolBar._$allocate(this.__tbop);
        }
    };
    /**
     * 控件回收
     * @return {Void}
     */
    _pro.__destroy = function(){
        _e._$setStyle(this.__body,'paddingTop','');
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = function(){
        this.__dataset = {boxInsert:'both'};
    };
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = (function(){
        var _seed_html = _l._$addNodeTemplate(_html);
        return function(){
            this.__body = _l._$getNodeTemplate(_seed_html);
            // 0 - content wrapper
            // 1 - margin adjust line
            var _list = _e._$getByClassName(this.__body,'j-flxg');
            this.__wrap = _list[0];
            this.__line = _list[1];
            // init toolbar config
            if (!!this.__tbop){
                this.__tbop.parent = this.__wrap;
            }
            // init widget body
            var _html = _l._$getTextTemplate(this.__seed_html)||'';
            if (!!_html){
                this.__wrap.insertAdjacentHTML('beforeEnd',_html);
            }
            _e._$addClassName(this.__body,this.__seed_css);
            _u._$forIn(
                this.__dataset,function(_value,_name){
                    _e._$dataset(this.__body,_name,_value);
                },this
            );
        };
    })();
    /**
     * 重置模块间距
     * @return {Void}
     */
    _pro.__onResetSpace = function(){
        _e._$setStyle(this.__body,'paddingTop',0);
    };
    /**
     * 资源错误事件
     * @return {Void}
     */
    _pro.__onError = function(_error){
        this._$dispatchEvent('onerror',_error);
    };
    /**
     * 行为处理
     * @return {Void}
     */
    _pro.__onAction = function(_event){
        var _node = _v._$getElement(_event,'d:action');
        if (!_node) return;
        switch(_e._$dataset(_node,'action')){
            case 'delete':
                if (window.confirm('您确定要移除此组件？')){
                    this._$recycle();
                    this._$dispatchEvent('onremove',{
                        id:this._$getId()
                    });
                }
            break;
        }
    };
    /**
     * 取控件标识
     * @return {String} 控件标识
     */
    _pro._$getId = function(){
        return this.__id;
    };
    /**
     * 取控件类型
     * @return {String} 控件类型
     */
    _pro._$getType = function(){
        return this.__type;
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = _f;
    
    return _p;
});
