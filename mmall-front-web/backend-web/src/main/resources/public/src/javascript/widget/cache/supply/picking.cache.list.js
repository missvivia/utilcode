/*
 * --------------------------------------------
 * 拣货单列表缓存实现
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * --------------------------------------------
 */
define([ 'base/klass'
        ,'base/event'
        ,'base/element'
        ,'util/cache/abstract'
        ,'util/encode/json'
        ,"{pro}components/notify/notify.js"
        ,'util/ajax/xdr'], 
        function(_k, _v, _e, _cache, JSON, notify, _j, _p) {
	var _pro;
	
	/* 
     * @class   module:width/cache/picking.cache.list._$$CacheListPicking
     * @extends module:util/cache/abstract._$$CacheListAbstract
     * 
     */
	_p._$$CacheListPicking = _k._$klass();
    _pro = _p._$$CacheListPicking._$extend(_cache._$$CacheListAbstract);
	
	_pro.__init = function(options) {
		this.__super(options);
	};

	/**
	 * 实现取列表的方法
	 * 数据返回的回调是onload
	 */
	_pro.__doLoadList = function(options) {
		var _key = options.key;
		var _data = options.data;
		var onload = options.onload;
		delete _data.limit;
		delete _data.total;
		delete _data.offset;
		
		 _j._$request('/supply/getPKList.json',{
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
		onload({
			total: (result.data)?result.data.length:0,
			list: (result.data)?result.data:[]
		});
	};
	
	/**
	 * 编辑
	 */
	_pro.__doUpdateItem = function(options) {
		var _key = options.key;
		var _data = options.data;
		var onload = options.onload;
		this.__cbUpdateItem(onload, _data);
		_j._$request('/supply/addexportone.json', {
		    type: 'json',
		    method: 'get',
		    data:{t:+new Date},
		    query: {
		        id: _data.pickOrderId
		    },
		    onload: this.__cbUpdateItem._$bind(this, onload),
		    onerror: function(error) {}
		});
	};
	/**
	 * 刷新数据
	 */
	_pro.__cbUpdateItem = function(onload, result) {
		//set data to dom
		if(result.result){
			onload(result.data);
		}else{
			notify.showError('更新失败，请稍后重试！');
		}
		
	};
	

	return _p;
})
