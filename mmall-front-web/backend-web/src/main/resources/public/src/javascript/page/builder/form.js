/*
 * ------------------------------------------
 * 项目通用表单控件实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/event',
    'util/form/form'
],function(_k,_v,_e,_t,_t0,_p,_o,_f,_r,_pro){
    /**
     * 项目通用表单控件
     * 
     * @param    {Object}      config   - 配置参数
     * @property {Node|String} form     - 表单节点
     */
    _p._$$WebForm = _k._$klass();
    _pro = _p._$$WebForm._$extend(_t._$$EventTarget);
    /**
     * 重置控件
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        var _form = _e._$get(_options.form);
        _v._$addEvent(
            _form['btn-submit'],'click',
            this.__onSubmit._$bind(this)
        );
        this.__form = _t0._$$WebForm._$allocate({
            form:_form,
            onenter:this.__onSubmit._$bind(this)
        });
        this.__onSubmit();
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__form._$recycle();
        delete this.__form;
        this.__super();
    };
    /**
     * 提交查询
     * @param {Object} _event
     */
    _pro.__onSubmit = function(_event){
        _v._$stop(_event);
        if (this.__form._$checkValidity()){
            this._$dispatchEvent(
                'onsubmit',this.__form._$data()
            );
        }
    };
    /**
     * 重置表单
     * @return {Void}
     */
    _pro._$reset = function(){
        this.__form._$reset();
    };
    /**
     * 取表单数据
     * @return {Void}
     */
    _pro._$data = function(){
        return this.__form._$data();
    };
    /**
     * 取指定名称的表单控件对象
     * 
     * @param  {String} arg0 - 控件名称
     * @return {Node}          表单控件对象
     */
    _pro._$get = function(_name){
        return this.__form._$get(_name);
    };
    
    return _p;
});
