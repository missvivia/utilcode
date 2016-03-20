define([
  "text!./mmallpager.html",
  "{pro}widget/BaseComponent.js"
  ],
  function(tpl, BaseComponent)
  {
	// <pager total=3 current=1></pager>
	var Pager = BaseComponent.extend(
	{
		name: "mmallpager",
		template: tpl,
		// is called before compile. 一般用来处理数据
		config: function(data)
		{
			var count =  5;
			var show = data.show = Math.floor( count/2 );
			data.pageSize = parseInt(data.pageSize || 1);
			data.currentPage = parseInt(data.currentPage || 1);
			data.totalResults = parseInt(data.totalResults || 1);
			data.totalPages = parseInt(data.totalPages || 1);
			data.isPage = parseInt(data.isPage || 1);
			
			this.$watch(['currentPage', 'totalPages'], function( currentPage, totalPages )
			{
				data.begin = currentPage - show; 
				data.end = currentPage + show;
				if(data.begin < 2) data.begin = 2;
				if(data.end > data.totalPages-1) data.end = data.totalPages-1;
				if(currentPage-data.begin <= 1) data.end = data.end + show + data.begin- currentPage;
				if(data.end - currentPage <= 1) data.begin = data.begin-show-currentPage+ data.end;
			});
		},
    
		nav: function(page)
		{
			var data = this.data;
			if(page < 1) return;
			if(page > data.totalPages) return;
			if(page === data.current) return;
			data.currentPage = page;
			this.$emit('nav', page);
		}
	});
	return Pager;
})