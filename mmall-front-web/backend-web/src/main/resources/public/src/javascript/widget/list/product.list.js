/**
 * 基础模块
 * author author(author@corp.netease.com)
 */
define([ '{lib}base/util.js'
         ,'{lib}base/event.js'
         ,'{lib}base/element.js'
         ,'{lib}util/event.js'
         ,'{lib}util/list/module.pager.js'
         ,'{pro}widget/item/pitem.js'
         ,'{pro}widget/cache/cache.list.js'], 
       function(u, v, e, t,t1, PItem,Cache,p, o, f, r) {
	var pro;

	var _$$Module = NEJ.C();
	pro = _$$Module._$extend(t._$$EventTarget);

	pro.__init = function(options) {
		this.__supInit(options);
		this.container = options.node;
		this.pager = options.pager;
		
	};
	pro.__reset = function(options) {
		this.__supReset(options);
		//请添加相关的cache脚本文件和item文件,
		//添加{lib}util/list/module.pager.js
		//添加{pro}widget/item/item.js
		//添加{pro}widget/cache/cache.list.js
		this.mdl = t1._$$ListModulePG._$allocate({
			limit : 9,
			parent : this.container, //列表容器节点
			item : {
				klass : PItem,
				prefix : 'g-item'
			}, // klass 可以是item对象，也可以是jst，如果是jst传入模板id就可，如果是item对象需要实现item对象
			cache : {
				key : 'id', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
				lkey : 'user-list', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
				data : {
					uid : 'ww'
				}, //  列表加载时携带数据信息，此数据也可在cache层补全
				klass :Cache
			},
			ondelete : function(data) {
				alert('删除成功');
				this.mdl._$delete(data);
			}._$bind(this),
			onupdate : function(data) {
				alert('更新成功');
				this.mdl._$update(data);
			}._$bind(this),
			pager : {
				parent : this.pager
			}
		});
	};
	/**
	 * 销毁控件
	 */
	pro.__destory = function() {
		this.__supDestroy();
		
	};

	return _$$Module;
});