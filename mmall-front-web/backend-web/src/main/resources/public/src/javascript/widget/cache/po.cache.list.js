/*
 * --------------------------------------------
 * PO列表缓存实现
 * @version  1.0
 * @author   zff(hzzhengff@corp.netease.com)
 * --------------------------------------------
 */
define([ '{lib}base/klass.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{lib}util/cache/abstract.js'
        ,'util/encode/json'
        ,"{pro}components/notify/notify.js"
        ,'{lib}util/ajax/xdr.js'], 
        function(_k, _v, _e, _cache, JSON, notify, _j, _p) {
	var _pro;
	
	/**
	 * PO列表缓存类实现
     * @class   module:widget/cache/po.cache.list._$$CacheListPO
     * @extends module:util/cache/abstract._$$CacheListAbstract
     * 
     */
	_p._$$CacheListPO = _k._$klass();
    _pro = _p._$$CacheListPO._$extend(_cache._$$CacheListAbstract);
	
	_pro.__init = function(options) {
		this.__super(options);
//		this.__itemList = [];
//		for(var i = 0; i< 105 ;i++){
//		    this.__itemList.push({
//		    	poOrderId:"2000259340" + i,
//		    	commodityStartTime:"2014-08-29 10:00:00",
//		    	commodityEndTime:"2014-08-31 10:00:00",
//		    	virtualTotalinventory:567,
//		    	soldAmount:20,
//		    	unPickQuantity:237
//		    })
//		}
	};

	/**
	 * 实现取列表的方法
	 * 数据返回的回调是onload
	 */
	_pro.__doLoadList = function(options) {
		var key = options.key;
		var _data = options.data;
		var _offset = options.offset;
		var _limit = options.limit;
		var rkey = options.rkey;
		var onload = options.onload;
//		分页数据恢复
//		delete _data.limit;
//		delete _data.offset;
		delete _data.total;
		//this.__cbLoadList(onload, {
		//	data: this.__itemList,
		//	total: 105
		//});
		_j._$request('/jit/polist/getList',{   // /jit/polist/getList /src/javascript/cache/po.fake.json
			headers:{"Content-Type":"application/json;charset=UTF-8"},
			type:'json',
		    method:'POST',
		    data:JSON.stringify(_data),
		    timeout:1000,
		    onload:this.__cbLoadList._$bind(this,onload),
		    onerror:function(error){notify.showError('获取数据失败，请稍后再试！');}
		  }
		);
	};
	
	_pro.__cbLoadList = function(onload, result) {
		onload({
			total: (result.result.total)?result.result.total:result.result.list.length,
			list: (result.result.list)?result.result.list:[]
		});
	};
	
	return _p;
});
