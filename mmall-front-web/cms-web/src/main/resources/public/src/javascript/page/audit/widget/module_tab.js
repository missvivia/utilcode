/*
 * ------------------------------------------
 * 带TAB切换审核页面
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    './tab/tab.js',
    'pro/widget/form',
    'pro/widget/module'
],function(_k,_u,Tab,_t,Module,_p,_o,_f,_r,_pro){
    /**
     * 档期BANNER审核页面
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(Module);
    /**
     * 模块初始化
     * @param {Object} 
     */
    _pro.__init = function(_options){
        _options.onchange = 
            this.__onChange._$bind(this);
        this.__super(_options);
        // init component
        this.__list = new _options.List();
        this.__list.$inject(_options.parent||'#module-box');
        // init search form
        this.__forms = [];
        _u._$forEach(
            _options.forms,function(_id){
                var _form = _t._$$WebForm._$allocate({
                    form:_id
                });
                _form._$setEvent(
                    'onsubmit',this.__onSearch._$bind(this)
                );
                this.__forms.push(_form);
            },this
        );
        // init list
        this.__onChange(0);
    };
    /**
     * 类型变化事件
     * @return {Void}
     */
    _pro.__onChange = function(_index){
        this.__type = _index;
        _u._$forEach(
            this.__forms,function(_form){
                _form._$reset();
            }
        );
        this.__list.refresh(_u._$merge(
            this.__forms[0]._$data(),{status:_index}
        ));
    };
    /**
     * 确定查询
     * @param {Object} _data
     */
    _pro.__onSearch = function(_data){
        this.__list.refresh(_u._$merge(
            _data,{status:this.__type}
        ));
    };
    
    return _p;
});
