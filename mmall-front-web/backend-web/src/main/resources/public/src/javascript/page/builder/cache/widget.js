/*
 * ------------------------------------------
 * 组件数据管理器实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/cache/abstract',
    'pro/extend/request',
    'json!./widget.json'
],function(_k,_u,_t,_req,_list,_p,_o,_f,_r,_pro){
    /**
     * 组件数据层管理
     * 
     * @class   _$$CacheWidget
     * @extends _$$CacheListAbstract
     */
    _p._$$CacheWidget = _k._$klass();
    _pro = _p._$$CacheWidget._$extend(_t._$$CacheListAbstract);
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
        _options.onload({
            result:_list,
            total:_list.length
        });
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        return this.__getDataInCache('layout');
    };
    /**
     * 更新组件布局信息
     * @param  {Object} 布局信息
     * @return {Void}
     */
    _pro._$setLayout = function(_layout){
        this.__setDataInCache('layout',_layout);
        // flush data to cache
        _u._$forEach(
            _list,function(_data){
                this.__doSaveItemToCache(_data);
            },this
        );
    };
    /**
     * 合并布局数据
     * @return {Void}
     */
    _pro.__doMergeLayout = function(_layout){
        var _data = this._$getLayout();
        if (_data.id!=null){
            _layout.id = _data.id;
        }
        if (_data.scheduleId!=null){
            _layout.scheduleId = _data.scheduleId;
        }
    };
    /**
     * 保存布局信息至服务器
     * @return {Void}
     */
    _pro._$save = function(_layout){
        this.__doMergeLayout(_layout);
        _req('/schedule/decorate/save',{
            data:_layout,
            method:'POST',
            timeout:0,
            onload:this.__cbSubmit._$bind(
                this,'onsave',_layout
            ),
            onerror:this.__cbSubmit._$bind(
                this,'onsave',_layout
            )
        });
    };
    /**
     * 发布布局信息至服务器
     * @return {Void}
     */
    _pro._$publish = function(_layout){
        this.__doMergeLayout(_layout);
        _req('/schedule/decorate/publish',{
            data:_layout,
            method:'POST',
            timeout:0,
            onload:this.__cbSubmit._$bind(
                this,'onpublish',_layout
            ),
            onerror:this.__cbSubmit._$bind(
                this,'onpublish',_layout
            )
        });
    };
    /**
     * 保存/发布回调
     * @return {Void}
     */
    _pro.__cbSubmit = function(_type,_layout,_json){
        _json = _json||_o;
        if (_json.code!=200){
            this._$dispatchEvent(
                'onerror',{
                    type:_type
                }
            );
            return;
        }
        // callback
        var _data = this._$getLayout();
        _data.layout = _layout;
        this._$dispatchEvent(_type,_data);
    };
    /**
     * 使用临时缓存执行某个操作后回收
     * @return {Void}
     */
    _p._$do = function(_func){
        if (_u._$isFunction(_func)){
            var _cache = _p._$$CacheWidget._$allocate(),
                _ret = _func.call(null,_cache);
            _cache._$recycle();
            return _ret;
        }
    };
    
    return _p;
});
