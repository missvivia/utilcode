/**
 * 发货单列表模块
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */
define([  'base/klass'
         ,'util/event'
         ,'util/list/module.pager'
         ,'{pro}widget/item/supply/invoice/invoice.js'
         ,'{pro}widget/pager/pager.js'
         ,'{pro}widget/cache/supply/invoice.cache.list.js'], 
    function(k, t, t1, item, pg, cache, p, o, f, r) {
		var pro;

		p._$$InvoiceList = k._$klass();
		pro = p._$$InvoiceList._$extend(t._$$EventTarget);

		pro.__reset = function(options) {
			this.__supReset(options);
			this.__container = options.node;
			this.__pager = options.pager;
			this.__dataForm = options.dataForm||{};

			this.__mdl = t1._$$ListModulePG._$allocate({
				limit : 10,
				parent : this.__container, //列表容器节点
				item : {
					klass : item._$$InvoiceItem
				}, 
				cache : {
					key : 'shipOrderId', 
					lkey : 'invoice-list', 
					data : this.__dataForm, //  列表加载时携带数据信息，此数据也可在cache层补全
					klass :cache._$$CacheListInvoice
				},
				onupdate : function(data) {
					this.__mdl._$update(data);
				}._$bind(this),
				onafterupdaterender:function(data) {
					this.__mdl._$refresh();
				}._$bind(this),
				pager : {
					parent : this.__pager,
					klass: pg._$$Pager
				}
			});
		};
		/**
		 * 销毁控件
		 */
		pro.__destroy = function() {
			this.__supDestroy();
			if (!!this.__mdl){
		        this.__mdl._$recycle();
		        delete this.__mdl;
		    }

		    delete this.__container;
		    delete this.__pager;
			
		};

		return p;
});