/*
 * ------------------------------------------
 * 选项模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'util/tab/tab',
    'util/dispatcher/module',
    'pro/page/builder/module'
],function(_k,_e,_t,_x,_m,_p,_o,_f,_r,_pro){
    /**
     * 选项模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__taber = _t._$$Tab._$allocate({
            selected:'active',
            list:_e._$getByClassName(
                this.__body,'j-flag'
            ),
            onchange:this.__onTabChange._$bind(this)
        });
    };
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__super({
            tid:'builder-module-side'
        });
        this.__export.parent = this.__body;
    };
    /**
     * 切换类型
     * @return {Void} 
     */
    _pro.__onTabChange = (function(){
        var _umis = [
            '/?/image/',
            '/?/product/',
            '/?/widget/'
        ];
        return function(_event){
            this.__dispatcher._$redirect(
                _umis[_event.index],{
                    input:{
                        parent:this.__export.parent
                    }
                }
            );
        };
    })();
    // regist module
    _x._$regist('side',_p._$$Module);
});
