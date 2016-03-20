/**
 * 基于NEJ和bootstrap的日期选择器 author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define(
[ "{pro}extend/util.js", 
  "{lib}base/util.js",
  "{pro}widget/BaseComponent.js",
  "{pro}components/pager/mmallpager.js"
  ],  function(_, _ut,BaseComponent) 
  {
	// ###data
	// - pager
	// * total: 列表总数
	// * list : 列表数组
	
	var MmallListComponent = BaseComponent.extend(
	{
		// 配置链接
		// @子类必须提供
		// url: 'xx.json',
		// 任意一个监听列表发生改变时，判断更新列表
		// @子类修改

		// 默认对列表数据做合并，减少节点回收
		notMerge : false,
		watchedAttr : [ 'currentPage', 'status' ],
		config : function(data) 
		{
			_.extend(data, 
			{
				pageSize:20,
				currentPage:1,
				totalResults:1,
				totalPages:1,
				list : [],
				isPage:1,
				startRownum:1
			});
			
			this.$watch(this.watchedAttr, function() 
			{
				if (this.shouldUpdateList())
				{
					this.__getList();
				}
			})
		},
		
		init : function() 
		{
			// if(!this.url) throw "ListModule未指定url";
			// 需要自定义复杂的更新策略, $emit('updatelist')事件即可
			this.$on("updatelist", this.__getList.bind(this));
		},
		
		// @子类修改
		shouldUpdateList : function(data) 
		{
			return true;
		},
		
		getExtraParam : function() 
		{
			return this.data.condition;
		},
		
		refresh : function(_data) 
		{
			this.data.currentPage = 1;
			
			this.data.condition = _data;
			this.$emit('updatelist');
		},
		
		getListParam : function() 
		{
			var data = this.data;
			return _.extend(
			{
				pageSize:data.pageSize,
				currentPage:data.currentPage,
				isPage:data.isPage,
			}, 
			
			this.getExtraParam(data));
		},
		
		// update loading
		__getList : function(_option) 
		{
			_option = _option || {};
			var data = this.data;
			var self = this;
			var onload = _option.onload || function() 
			{
				return !0
			};
			
			var option = 
			{
					progress : true,
					data : this.getListParam(),
					onload : function(json) 
					{
						if (json.code == 200) 
						{
							var result = json.result;
							list = result.list || [];
							if (!self.notMerge) 
							{
								_.mergeList(list, data.list, data.key || 'id')
							}
							
							data.list = list || [];
							data.pageSize = result.pageSize;
							data.currentPage = result.currentPage;
							data.totalResults = result.total;
							data.totalPages = result.totalPages;
							data.isPage = result.isPage;
							data.startRownum = result.startRownum;
							onload(result);
						}
					},
					
					// test
					onerror : function(json) 
					{
						// @TODO: remove
					}
			};
			
			// 继承类提供xdrOption方法，用来表明请求类型
			/**
			 *  * function(){ return {method:'POST',norest:true} & }
			 *  */
			if (this.xdrOption) 
			{
				var xdrOpt = this.xdrOption();
				if (xdrOpt.norest) 
				{
					option.data = _ut._$object2query(this.getListParam());
					option.norest = true;
				}
				
				option.method = xdrOpt.method || 'GET';
			}
			
			this.$request(this.url, option)
		}
	})
	
	return MmallListComponent;
})