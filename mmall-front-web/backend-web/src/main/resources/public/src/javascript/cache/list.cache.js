/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
var f = function() {
    var _ = NEJ.P,
        _o = NEJ.O,
        _e = _('nej.e'),
        _v = _('nej.v'),
        _u = _('nej.u'),
        _j = _('nej.j'),
        _ui = _('nej.ui'),
        _i = _('nej.i'),
        _t = _('nej.ut'),
        _pi = _('xx.i'),
        _pu = _('xx.ui'),
        _pd = _('xx.d'),
        _px = _('xx.x'),
        _pl = _('xx.l'),
        _pro, _sup;

    /**
     * 缓存类实现
     * @class   {nm.l._$$CacheListCustom}
     * @extends {nej.ui._$$ListCache}
     *
     *
     */
    _pd._$$CacheListCustom = NEJ.C();
    _pro = _pd._$$CacheListCustom._$extend(_t._$$ListCache);
    _sup = _pd._$$CacheListCustom._$supro;
    /**
     * 初使化注册事件
     * @protected
     * @method {__init}
     * @param  {Object} _options 可选配置参数
     *   // 第二步：实例化一个上面的对象
     *   var _cC = _pd._$$CacheListCustom._$allocate({
     *       // id作为cache的标识
     *       id:'a',
     *       // 根据key，也就是上面的id，到缓存中取数据，然后处理数据
     *       onlistload:function(_ropt){
     *           _cc._$getListInCache(_ropt.key);
     *       },
     *        // 根据key，也就是上面的id，到缓存中取数据，然后处理数据
     *       onitemload:function(_ropt){
     *           _cc._$getItemInCache(_ropt.key);
     *       }
     *   });
     * [/code]
     *
     * [code]
     *   // 第三步：发送请求
     *   // 第一个列表的请求
     *   _cc._$getList({key:'abc',data:{},offset:0,limit:10})
     *   // 不会发请求，直接走缓存
     *   _cc._$getList({key:'abc',data:{},offset:0,limit:10})
     *   // 第一个项请求
     *   _cc._$getItem({id:'abc',key:'123',data:{})
     *   // 不会发请求，直接走缓存
     *   _cc._$getItem({id:'abc',key:'123',data:{})
     */
    _pro.__init = function(_options) {
        this.__supInit(_options);
        this._$batEvent({
            doloadlist: this.__doLoadList._$bind(this),
            doloaditem: this.__doLoadItem._$bind(this),
            doadditem: this.__doAddItem._$bind(this),
            dodeleteitem: this.__doDeleteItem._$bind(this),
            doupdateitem: this.__doUpdateItem._$bind(this),
            dopullrefresh: this.__doPullRefresh._$bind(this)
        });
    };
    /**
     * 实现取列表的方法
     * 数据返回的回调是onload
     */
    _pro.__doLoadList = function(_options) {
        var _key = _options.key;
        var _data = _options.data;
        var _offset = _options.offset;
        var _limit = _options.limit;
        var _rkey = _options.rkey;
        var _onload = _options.onload;
        _j._$request('http://123.163.com:3000/xhr/getLog', {
            type: 'json',
            method: 'POST',
            data: {
                offset: _offset,
                limit: _limit
            },
            timeout: 1000,
            onload: _onload._$bind(this),
            onerror: function(_error) {}
        });
    };
    /**
     * 实现取项的方法
     * 数据返回的回调是onload
     */
    _proCacheListCustom.__doLoadItem = function(_options) {
        var _id = _options.id;
        var _key = _options.key;
        var _rkey = _options.rkey;
        var _onload = _options.onload;
        _j._$request('http://123.163.com:3000/xhr/getLog', {
            type: 'json',
            method: 'POST',
            data: {
                id: _id,
                key: _key
            },
            timeout: 1000,
            onload: _onload._$bind(this),
            onerror: function(_error) {}
        });
    };
    /**
     * 销毁控件
     */
    _pro.__destroy = function() {
        this.__supDestroy();
    };
    /**
     * 初使化节点
     */
    _pro.__initNode = function() {
        this.__supInitNode();
    };
    /**
     * 刷新数据
     */
    _pro.__doRefresh = function(_data) {
        //set data to dom
    };
}
define(['{lib}util/cache/cache.list.js'], f);