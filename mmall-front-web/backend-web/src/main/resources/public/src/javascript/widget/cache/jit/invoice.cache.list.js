/*
 * --------------------------------------------
 * 发货单列表缓存实现
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * --------------------------------------------
 */
define([ 'base/klass'
        ,'base/event'
        ,'base/element'
        ,'util/encode/json'
        ,"{pro}components/notify/notify.js"
        ,'util/cache/abstract'
        ,'util/ajax/xdr'], 
        function(_k, _v, _e, JSON, notify,_cache, _j, _p) {
	var _pro;
	
	/* 
     * @class   module:width/cache/picking.cache.list._$$CacheListPicking
     * @extends module:util/cache/abstract._$$CacheListAbstract
     * 
     */
	_p._$$CacheListInvoice = _k._$klass();
    _pro = _p._$$CacheListInvoice._$extend(_cache._$$CacheListAbstract);
	

	/**
	 * 实现取列表的方法
	 * 数据返回的回调是onload
	 */
	_pro.__doLoadList = function(options) {
		var key = options.key;
		var _data = options.data;
		delete _data.total;
		var onload = options.onload;
		_j._$request('/jit/getInvoiceList.json',{
		 	headers:{"Content-Type":"application/json;charset=UTF-8"},
			type:'json',
		    method:'POST',
		    data:JSON.stringify(_data),
		    onload:this.__cbLoadList._$bind(this,onload),
		    onerror:function(error){notify.showError('获取数据失败，请稍后再试！');}
		  }
		);
	};
	
	_pro.__cbLoadList = function(onload, result) {
		onload(result);
	};
	
	/**
	 * 编辑
	 */
	_pro.__doUpdateItem = function(options) {
		var _key = options.key;
		var _data = options.data;
		var onload = options.onload;
		this.__cbUpdateItem(onload, _data);
		
	};
	/**
	 * 刷新数据
	 */
	_pro.__cbUpdateItem = function(onload, result) {
		//set data to dom
		onload(result);
	};
	

	return _p;
})
