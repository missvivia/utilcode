/*
 * ------------------------------------------
 * 图片数据管理器实现文件
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
     * 图片数据层管理
     * 
     * @class   _$$CacheImage
     * @extends _$$CacheListAbstract
     */
    _p._$$CacheImage = _k._$klass();
    _pro = _p._$$CacheImage._$extend(_t._$$CacheListAbstract);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__key = 'imgId';
    };
    /**
     * 从服务器端载入列表，子类实现具体逻辑
     * 
     * @param    {Object}   arg0   - 请求信息
     * @property {String}   key    - 列表标识
     * @property {Number}   offset - 偏移量
     * @property {Number}   limit  - 数量
     * @property {String}   data   - 请求相关数据
     * @property {Function} onload - 列表项载入回调
     * @return   {Void}
     */
    _pro.__doLoadList = function(_options){
        _req('/image/manage/list',{
            type:'json',
            method:'POST',
            data:_options.data,
            onload:function(_json){
                _options.onload(_json.result);
            },
            onerror:function(_error){
                _options.onload(null);
            }
        });
    };
    /**
     * 导出图片地址信息
     * @return {Object} ID与地址的映射表
     */
    _pro._$dump = function(_list){
        var _ret = {};
        _u._$forEach(
            _list,function(_id){
                var _image = this._$getItemInCache(_id);
                if (!!_image){
                    _ret[_id] = _image.imgUrl;
                }
            },this
        );
        return _ret;
    };
    /**
     * 导入图片地址信息
     * @return {Void}
     */
    _pro._$push = function(_umap){
        _u._$forIn(
            _umap,function(_url,_id){
                this.__doSaveItemToCache({
                    imgId:_id,
                    imgUrl:_url
                });
            },this
        );
    };
    /**
     * 合并图片数据信息
     * @return {Void}
     */
    _pro._$merge = function(_list){
        _u._$forEach(
            _list,function(_data){
                if (!_data.imgId){
                    _data.imgId = _data.id;
                }
                this.__doSaveItemToCache(_data);
            },this
        );
    };
    /**
     * 设置布局标识
     * @return {Void}
     */
    _pro._$setLayoutId = function(_conf){
        this.__setDataInCache('layout',_conf);
    };
    /**
     * 设置图片分类列表
     * @return {Void}
     */
    _pro._$setCategory = function(_category){
        this.__setDataInCache('category',_category);
    };
    /**
     * 获取图片分类列表
     * @return {Void}
     */
    _pro._$getCategory = function(){
        return this.__getDataInCache('category');
    };
    /**
     * 使用临时缓存执行某个操作后回收
     * @return {Void}
     */
    _p._$do = function(_func){
        if (_u._$isFunction(_func)){
            var _cache = _p._$$CacheImage._$allocate(),
                _ret = _func.call(null,_cache);
            _cache._$recycle();
            return _ret;
        }
    };
    
    return _p;
});
