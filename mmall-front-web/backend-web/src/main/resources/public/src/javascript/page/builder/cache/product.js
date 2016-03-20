/*
 * ------------------------------------------
 * 产品数据管理器实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/event',
    'util/event/event',
    'util/cache/abstract',
    'pro/extend/request',
    '../util.js'
],function(_k,_u,_v,_y,_t,_req,_x,_p,_o,_f,_r,_pro){
    /**
     * 产品数据层管理
     * 
     * @class   _$$CacheProduct
     * @extends _$$CacheListAbstract
     */
    _p._$$CacheProduct = _k._$klass();
    _pro = _p._$$CacheProduct._$extend(_t._$$CacheListAbstract);
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
        var _data = _options.data||{};
        _data.scheduleId = this.
            __getDataInCache('layout').scheduleId;
        _req('/rest/schedule/product',{
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
     * 合并图片数据信息
     * @return {Void}
     */
    _pro._$merge = function(_list){
        _u._$forEach(
            _list,function(_data){
                if (!_data.id){
                    _data.id = _data.productId;
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
     * 获取商品分类列表
     * @return {Void}
     */
    _pro._$getCategory = function(){
        var _list = this.__getDataInCache('category');
        if (!!_list){
            this._$dispatchEvent(
                'oncategoryload',{
                    list:_list
                }
            );
            return;
        }
        var _layout = this.__getDataInCache('layout'),
            _callback = this._$dispatchEvent._$bind(this);
        if (!this.__doQueueRequest('category',_callback)){
            _req('/rest/schedule/category',{
                norest:!0,
                type:'json',
                method:'GET',
                query:{scheduleId:_layout.scheduleId},
                onload:this.__getCategory._$bind(this),
                onerror:this.__getCategory._$bind(this)
            });
        }
    };
    /**
     * 商品分类列表载入回调
     * @return {Void}
     */
    _pro.__getCategory = function(_json){
        _json = _json||_o;
        // check result
        var _list = [];
        if (_json.code==200){
            _list = (_json.result||_o).list||[];
        }
        // save data
        this.__setDataInCache('category',_list);
        // sort category
        var _arr = _list.slice(0,_list.length);
        if (!!this.__seq){
            _arr = _x._$sortListBySeq(
                _arr,this.__seq
            );
            delete this.__seq;
        }
        this.__setDataInCache(
            'category-sort',_arr
        );
        // call locked callback
        this.__doCallbackRequest(
            'category','oncategoryload',{
                list:_list
            }
        );
    };
    /**
     * 获取排序过的商品分类列表
     * @return {Void}
     */
    _pro._$getSortCategory = function(){
        return this.__getDataInCache('category-sort');
    };
    /**
     * 取排序方式
     * @return {Number}
     */
    _pro._$getSortType = function(){
        return this.__getDataInCache('sort-type')||0;
    };
    /**
     * 取排序列表
     * @return {String}
     */
    _pro._$getSortList = function(){
        if (this._$getSortType()!=1){
            return '';
        } 
        var _ret = [];
        _u._$forEach(
            this._$getSortCategory(),function(_item){
                _ret.push(_item.id);
            }
        );
        return _ret.join(',');
    };
    /**
     * 更新排序方式
     * @return {Void}
     */
    _pro._$updateSortType = function(_type,_seq){
        this.__setDataInCache('sort-type',_type);
        if (_type==1){
            // sort category
            var _list = this._$getSortCategory();
            if (!_list){
                this.__seq = _seq;
                return;
            }
            this.__setDataInCache(
                'category-sort',
                _x._$sortListBySeq(_list,_seq)
            );
        }
    };
    /**
     * 更新排序方式
     * @return {Void}
     */
    _pro._$updateSort = function(_data){
        _data.scheduleId = this.
            __getDataInCache('layout').scheduleId;
        _req('/rest/schedule/updateSort',{
            type:'json',
            method:'POST',
            data:_data,
            onload:this.__cbUpdateSort._$bind(this,_data),
            onerror:this.__cbUpdateSort._$bind(this)
        });
    };
    /**
     * 更新排序方式回调
     * @return {Void}
     */
    _pro.__cbUpdateSort = function(_data,_json){
        _json = _json||_o;
        var _isok = !1;
        if (_json.code==200){
            _isok = !0;
            this._$updateSortType(
                _data.type,
                (_data.seq||_r).join(',')
            );
        }
        this._$dispatchEvent(
            'onsortupdate',{
                isok:_isok,
                type:this._$getSortType()
            }
        );
        if (_isok){
            _v._$dispatchEvent(
                _p._$$CacheProduct,'sortupdate'
            );
        }
    };
    /**
     * 使用临时缓存执行某个操作后回收
     * @return {Void}
     */
    _p._$do = function(_func){
        if (_u._$isFunction(_func)){
            var _cache = _p._$$CacheProduct._$allocate(),
                _ret = _func.call(null,_cache);
            _cache._$recycle();
            return _ret;
        }
    };
    // custom event
    _y._$$CustomEvent._$allocate({
        element:_p._$$CacheProduct,
        event:'sortupdate'
    });
    
    return _p;
});
