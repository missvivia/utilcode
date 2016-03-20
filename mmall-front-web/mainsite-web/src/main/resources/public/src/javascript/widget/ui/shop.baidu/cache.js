/*
 * ------------------------------------------
 * 商店店铺列表缓存控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/cache/abstract',
    'pro/extend/request'
],function(_k,_u,_t,_req,_p,_o,_f,_r,_pro){
    /**
     * 商店店铺列表缓存控件
     * 
     */
    _p._$$ShopCache = _k._$klass();
    _pro = _p._$$ShopCache._$extend(_t._$$CacheListAbstract);
    /**
     * 从服务器端载入列表，子类实现具体逻辑
     * 
     * @abstract
     * @method   module:util/cache/abstract._$$CacheListAbstract#__doLoadList
     * @param    {Object}   arg0   - 请求信息
     * @property {String}   key    - 列表标识
     * @property {Number}   offset - 偏移量
     * @property {Number}   limit  - 数量
     * @property {String}   data   - 请求相关数据
     * @property {Function} onload - 列表项载入回调
     * @return   {Void}
     */
    _pro.__doLoadList = function(_options){
        var _callback = _options.onload;
        _req('/brand/shops',{
            method:'GET',
            data:_options.data,
            onload:function(_json){
                _callback(_json.result);
            },
            onerror:function(_error){
                _callback(null);
            }
        });
    };
    /**
     * 格式化数据
     * @return {Void}
     */
    _pro.__doFormatItem = function(_item){
        if (_u._$isObject(_item.city)){
            _item.city = _item.city.name;
        }
        if (_u._$isObject(_item.district)){
            _item.district = _item.district.name;
        }
        return _item;
    };
    /**
     * 设置店铺列表
     * @param {Object} _list
     */
    _pro._$setShops = function(_list){
        this._$setListInCache('all',_list||_r);
        // category by city
        var _ret = {};
        _u._$forEach(
            _list,function(_shop){
                _shop = this.__doFormatItem(_shop);
                var _arr = _ret[_shop.city]||[];
                _ret[_shop.city] = _arr;
                _arr.push(_shop);
            },this
        );
        // save list to cache
        var _arr = [];
        _u._$forIn(
            _ret,function(_list,_city){
                _arr.push(_city);
                this._$setListInCache(_city,_list);
            },this
        );
        return _arr;
    };
    /**
     * 单次执行动作
     * @param {Object} _func
     */
    _p._$do = function(_func){
        var _ret;
        var _inst = _p._$$ShopCache._$allocate();
        try{
            _ret = _func.call(null,_inst);
        }catch(ex){
            // ignore
        }
        _inst._$recycle();
        return _ret;
    };
    
    return _p;
});