/*
 * ------------------------------------------
 * 背景效果设置控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'util/dragger/dragger',
    'ui/base'
],function(_k,_v,_e,_u,_t,_i,_p,_o,_f,_r,_pro){
    /**
     * 背景效果设置控件
     */
    _p._$$Setting = _k._$klass();
    _pro = _p._$$Setting._$extend(_i._$$Abstract);
    /**
     * 初始化控件
     * @return {Void}
     */
    _pro.__init = function(){
        this.__dgopt = {};
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init event
        this.__doInitDomEvent([[
            this.__body,'click',
            this.__onAction._$bind(this)
        ]]);
        // align middle
        var _pbox = _e._$getPageBox(),
            _left = (_pbox.clientWidth-this.__wrap.offsetWidth)/2,
            _top  = (_pbox.clientHeight-this.__wrap.offsetHeight)/2;
        _e._$setStyle(
            this.__body,'height',
            _pbox.scrollHeight+'px'
        );
        _e._$style(
            this.__wrap,{
                top:_pbox.scrollTop+Math.max(0,_top)+'px',
                left:_pbox.scrollLeft+Math.max(0,_left)+'px'
            }
        );
        // draggable
        this.__dragger = _t.
            _$$Draggable._$allocate(this.__dgopt);
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        this.__wrap = _e._$getChildren(this.__body)[0];
        _e._$setStyle(this.__body,'display','block');
        this.__dgopt.body = this.__wrap;
        this.__dgopt.mbar = _e._$getByClassName(
            this.__wrap,'j-dragger'
        )[0];
    };
    /**
     * 生成表单数据
     * @return {Void}
     */
    _pro.__doGenSubmitData = _f;
    /**
     * 操作事件 
     * @param {Object} _event
     */
    _pro.__onAction = function(_event){
        var _node = _v._$getElement(_event,'d:action');
        if (!_node) return;
        switch(_e._$dataset(_node,'action')){
            case 'close':
                this._$recycle();
            break;
            case 'submit':
                this._$dispatchEvent(
                    'onsubmit',
                    this.__doGenSubmitData()
                );
                this._$recycle();
            break;
        }
    };
    
    return _p;
});
