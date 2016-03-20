/*
 * ------------------------------------------
 * 提示确认控件
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
    'text!./confirm.html'
],function(_k,_v,_e,_u,_i,_t,_html,_p,_o,_f,_r,_pro){
    /**
     * 组件工具栏控件
     */
    _p._$$Confirm = _k._$klass();
    _pro = _p._$$Confirm._$extend(_i._$$Abstract);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init event
        this.__doInitDomEvent([[
            this.__body['ok'],'click',
            this.__onOK._$bind(this)
        ],[
            this.__body['cc'],'click',
            this.__onCC._$bind(this)
        ]]);
        // init show
        this.__extdata = _options.data;
        this.__nmsg.innerHTML = _options.message||'提示信息';
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_html = _t._$addNodeTemplate(_html);
        return function(){
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - message show
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__nmsg = _list[0];
    };
    /**
     * 取消关闭
     * @return {Void}
     */
    _pro.__onCC = function(){
        this._$dispatchEvent('oncc',{
            data:this.__extdata
        });
        this._$recycle();
    };
    /**
     * 确认关闭
     * @return {Void}
     */
    _pro.__onOK = function(){
        this._$dispatchEvent('onok',{
            data:this.__extdata
        });
        this._$recycle();
    };
    
    return _p;
});
