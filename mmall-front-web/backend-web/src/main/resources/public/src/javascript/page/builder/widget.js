NEJ.define(function(_p){
    // 尺寸不匹配
    _p.UNMATCH_SIZE = -1;
    // 类型不匹配
    _p.UNMATCH_TYPE = -2;
    // 资源载入失败
    _p.FAILD_LOAD_RES = -3;
    
    // cache widget impl
    var _cache = {};
    /**
     * 缓存组件实现
     * @param {Object} _id
     * @param {Object} _impl
     */
    _p._$regist = function(_id,_klass){
        _cache[_id] = _klass;
    };
    /**
     * 取组件构造
     * @param {Object} _id
     */
    _p._$get = function(_id){
        return _cache[_id];
    };
    
    return _p;
});
