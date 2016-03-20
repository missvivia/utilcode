/**
 * 基础模块
 * author zff(hzzhengff@corp.netease.com) based on pklist module
 */
define([  '{lib}base/klass.js'
         ,'{lib}util/event.js'
         ,'{lib}util/list/module.pager.js'
         ,'{pro}widget/item/polist/polist.js'
         ,'{pro}widget/pager/pager.js'
         ,'{pro}widget/cache/po.cache.list.js'], 
    function(k, t, t1, item, pg, cache, p, o, f, r) {
		var pro;
		p._$$POList = k._$klass();
		pro = p._$$POList._$extend(t._$$EventTarget);

		pro.__init = function(options) {
			this.__supInit(options);
		};
		
		pro.__reset = function(options) {
			this.__supReset(options);
			this.container = options.node;
			this.pager = options.pager;
			this.dataForm = options.dataForm||{};
			
			//请添加相关的cache脚本文件和item文件,
			//添加{lib}util/list/module.pager.js
			//添加{pro}widget/item/item.js
			//添加{pro}widget/cache/cache.list.js
			this.mdl = t1._$$ListModulePG._$allocate({
				limit: 10,
				parent: this.container, //列表容器节点
				item: {
					klass: item._$$POItem,  // data1不变
					prefix: 'g-item'
				}, // klass 可以是item对象，也可以是jst，如果是jst传入模板id就可，如果是item对象需要实现item对象
				cache: {
					key: 'id', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
					lkey: 'user-list', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
					data: this.dataForm, //  列表加载时携带数据信息，此数据也可在cache层补全
					klass: cache._$$CacheListPO   // data3变化
				},
				ondelete: function(data) {
					alert('删除成功');
					this.mdl._$delete(data);
				}._$bind(this),
				onupdate: function(data) {
					alert('更新成功');
					this.mdl._$update(data);
				}._$bind(this),
				onafterupdaterender: function(data) {
					alert('更新成功');
					this.mdl._$refresh();
				}._$bind(this),
				pager: {
					parent: this.pager,
					klass: pg._$$Pager    // data2不变
				}
			});
		};
		
		/**
		 * 销毁控件
		 */
		pro.__destroy = function() {
			this.__supDestroy();
			if (!!this.mdl){
		        this.mdl._$recycle();
		        delete this.mdl;
		    }

		    delete this.container;
		    delete this.pager;
		};

		return p;
});