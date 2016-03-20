/**
 * 基于NEJ和bootstrap的日期选择器 author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define(
[ "{pro}extend/util.js", 
  "{lib}base/util.js",
  "{pro}widget/BaseComponent.js",
  "{pro}components/pager/mmallpager.js",
  ],  function(_, _ut,BaseComponent) 
  {
	var MmallListComponent = BaseComponent.extend(
	{
		// 默认对列表数据做合并，减少节点回收
		notMerge : true,
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
				startRownum:1,
				status:0,
				offset:0,
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
		
		shouldUpdateList : function(data) 
		{
			return true;
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
				limit:data.pageSize,
				offset:data.pageSize * (data.currentPage-1),
				status:data.status
			}, 
			data.condition,true);
		},
		
		// update loading
		__getList : function() 
		{
			var data = this.data;
			
			var option = 
			{
				progress : true,
				method:'GET',
				data : _ut._$object2query(this.getListParam()),
				onload : function(json) 
				{
					if (json.code == 200) 
					{
						var result = json.result,
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
					}
				},
				
				onerror : function(json) 
				{
					console.log('ProductList Get Error');
				}
			};
			
			
			// 继承类提供xdrOption方法，用来表明请求类型
			/**
			 *  * function(){ return {method:'POST',norest:true} & }
			 *  */
			 /**
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
			*/	
			this.$request(this.url, option)
		}
	})
	
	return MmallListComponent;
})