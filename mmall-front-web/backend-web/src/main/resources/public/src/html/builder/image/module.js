/*
 * ------------------------------------------
 * 图片列表模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/config',
    'base/util',
    'util/dispatcher/module',
    'pro/page/builder/module',
    'pro/page/builder/cache/image'
],function(_k,_c,_u,_x,_m,_d,_p,_o,_f,_r,_pro){
    /**
     * 图片列表模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__mopt = {
            limit:15,
            item:{
                root:_c._$get('root'),
                klass:'builder-module-image-list'
            },
            cache:{klass:_d._$$CacheImage,clear:!0}
        };
        this.__super({
            tid:'builder-module-image'
        });
        // init category list
        this.__doInitCategory();
    };
    /**
     * 初始化分类信息
     * @return {Void}
     */
    _pro.__doInitCategory = function(){
        var _select = this.__form._$get('categoryId');
        var _list = _d._$do(function(_cache){
            return _cache._$getCategory();
        });
        _u._$forEach(
            _list,function(_item){
                _select.add(new Option(_item.name,_item.id));
            }
        );
    };
    // regist module
    _x._$regist('image',_p._$$Module);
});
