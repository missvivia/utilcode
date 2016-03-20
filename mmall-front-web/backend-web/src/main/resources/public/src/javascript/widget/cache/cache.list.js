/*
 * --------------------------------------------
 * 缓存实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        , '{lib}base/element.js'
        , '{lib}util/cache/list.js'
        , '{lib}util/ajax/xdr.js'], 
        function(u, v, e, ut,j, p, o, f, r) {
	var pro, sup;
	var itemList = [{
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}, {
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}, {
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}, {
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}, {
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}, {
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}, {
		width: 253,
		height: 539,
		url:'/res/images/cut.jpg',
		name:u._$randString(10),
		id:u._$randString(10)
	}];
	/**
	 * 缓存类实现
	 * @class   {nm.l._$$CacheListCustom}
	 * @extends {nej.ui._$$ListCache}
	 *
	 *
	 */
	p._$$CacheListCustom = NEJ.C();
	pro = p._$$CacheListCustom._$extend(ut._$$CacheList);
	sup = p._$$CacheListCustom._$supro;
	/**
	 * 初使化注册事件
	 * @protected
	 * @method {__init}
	 * @param  {Object} options 可选配置参数
	 *   // 第二步：实例化一个上面的对象
	 *   var cc = p._$$CacheListCustom._$allocate({
	 *       // id作为cache的标识
	 *       id:'a',
	 *       // 根据key，也就是上面的id，到缓存中取数据，然后处理数据
	 *       onlistload:function(_ropt){
	 *           cc._$getListInCache(_ropt.key);
	 *       },
	 *        // 根据key，也就是上面的id，到缓存中取数据，然后处理数据
	 *       onitemload:function(_ropt){
	 *           cc._$getItemInCache(_ropt.key);
	 *       }
	 *   });
	 * [/code]
	 *
	 * [code]
	 *   // 第三步：发送请求
	 *   // 第一个列表的请求
	 *   cc._$getList({key:'abc',data:{},offset:0,limit:10})
	 *   // 不会发请求，直接走缓存
	 *   cc._$getList({key:'abc',data:{},offset:0,limit:10})
	 *   // 第一个项请求
	 *   cc._$getItem({id:'abc',key:'123',data:{})
	 *   // 不会发请求，直接走缓存
	 *   cc._$getItem({id:'abc',key:'123',data:{})
	 */
	pro.__init = function(options) {
		this.__supInit(options);
		this._$batEvent({
			doloadlist: this.__doLoadList._$bind(this),
			dodeleteitem: this.__doDeleteItem._$bind(this),
			doupdateitem: this.__doUpdateItem._$bind(this),
			doadditem: this.__doAddItem._$bind(this),
			dopullrefresh: this.__doPullRefresh._$bind(this)
			
		});
	};
	/**
	 * 实现取列表的方法
	 * 数据返回的回调是onload
	 */
	pro.__doLoadList = function(options) {
		var key = options.key;
		var _data = options.data;
		var _offset = options.offset;
		var _limit = options.limit;
		var rkey = options.rkey;
		var onload = options.onload;
		this.__cbLoadList(onload, {
			data: itemList,
			total: 2
		})
		//  j._$request('http://123.163.com:3000/xhr/getLog',{
			//     type:'json',
		//     method:'POST',
		//     data:{offset:_offset,limit:_limit},
		//     timeout:1000,
		//     onload:this.__cbBlaockListload._$bind(this,onload),
		//     onerror:function(error){}
		//   }
		// );
	};
	
	pro.__cbLoadList = function(onload, result) {
		onload({
			total: result.data.length,
			list: result.data
		});
	};
	/**
	 * 实现取项的方法
	 * 数据返回的回调是onload
	 */
	pro.__doLoadItem = function(options) {
		var id = options.id;
		var key = options.key;
		var rkey = options.rkey;
		var onload = options.onload;
		//测试代码直接回调
		this.__cbLoadItem(onload);
		//真实开发时要调用ajax获取数据
		j._$request('http://123.163.com:3000/xhr/getLog', {
			type: 'json',
			method: 'POST',
			data: {
				id: id,
				key: key
			},
			timeout: 1000,
			onload: onload._$bind(this),
			onerror: function(error) {}
		});
	};
	
	this.__cbLoadItem = function(onload, result) {
		onload();
	}
	
	/**
	 * 删除
	 */
	pro.__doDeleteItem = function(options) {
		var id = options.id;
		var key = options.key;
		var rkey = options.rkey;
		var onload = options.onload;
		var _data = options.data;
		this.__cbDeleteItem(onload, {
			data: _data
		});
		// j._$request('http://123.163.com:3000/xhr/getLog', {
		//     type: 'json',
		//     method: 'POST',
		//     data: {
		//         id: id,
		//         key: key
		//     },
		//     timeout: 1000,
		//     onload: this.__cbDeleteItem._$bind(this, onload),
		//     onerror: function(error) {}
		// });
	};
	
	/**
	 * 函数描述
	 * @param {Type} parameter  [description]
	 * @return {Type}
	 */
	pro.__cbDeleteItem = function(onload, result) {
		onload(result.data);
	};
	/**
	 * 编辑
	 */
	pro.__doUpdateItem = function(options) {
		var id = options.id;
		var key = options.key;
		var rkey = options.rkey;
		var onload = options.onload;
		this.__cbUpdateItem(onload, {})
		// j._$request('http://123.163.com:3000/xhr/getLog', {
		//     type: 'json',
		//     method: 'POST',
		//     data: {
		//         id: id,
		//         key: key
		//     },
		//     timeout: 1000,
		//     onload: this.__cbUpdateItem._$bind(this, onload),
		//     onerror: function(error) {}
		// });
	};
	/**
	 * 刷新数据
	 */
	pro.__cbUpdateItem = function(onload, result) {
		//set data to dom
		onload({});
	};
	/**
	 * 下拉获取数据
	 * @param {Object}  可选项数据
	 */
	pro.__doPullRefresh = function(options) {
		var id = options.id;
		var key = options.key;
		var rkey = options.rkey;
		var onload = options.onload;
		this.__cbPullRefresh(onload, {})
		// j._$request('http://123.163.com:3000/xhr/getLog', {
		//     type: 'json',
		//     method: 'POST',
		//     data: {
		//         id: id,
		//         key: key
		//     },
		//     timeout: 1000,
		//     onload: this.__cbUpdateItem._$bind(this, onload),
		//     onerror: function(error) {}
		// });
	};
	pro.__cbPullRefresh = function(options) {
		onload({});
	};
	pro.__doAddItem = function(options) {
		var id = options.id;
		var key = options.key;
		var rkey = options.rkey;
		var onload = options.onload;
		onload({});
	};
	pro.__cbAddItem = function(onload, result) {
		onload({});
	};
	return p._$$CacheListCustom;
})
