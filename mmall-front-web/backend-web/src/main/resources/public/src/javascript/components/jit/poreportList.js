/**
 * po单报表的列表组件
 * author hzzhengff(hzzhengff@corp.netease.com)
 */

define([
  "{pro}extend/util.js",
  "text!./poreportList.html",
  "{pro}widget/BaseComponent.js",
  "{pro}components/pager/pager.js"
  ], function(_, tpl, BaseComponent, Pager){
	
	var SizeList = BaseComponent.extend({
		url: "/jit/poreport/getReportList",
	    //name: "m-sizelist",
	    template: tpl,
	    watchedAttr: ['current'],
	    config: function(data){
	      _.extend(data, {
	        total: 1,
	        current: 1,
	        limit: 10,
	        list: [],
	        currentlist:[]
	      });
	      this.$watch(this.watchedAttr, function(){
	        if(this.shouldUpdateList()) this.data.currentlist = this.getCurrents();
	      });
	    },
	    init: function(){
	      if(!this.url) throw "ListModule未指定url";

	      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
	      this.__getList();
	      this.$update();

	    },
	    shouldUpdateList: function(){
		  return (this.data.list.length<1?false:true);
	    },
	    getExtraParam:function(){
	      return this.data.condition;
	    },
	    refresh:function(_data){
	      this.data.condition = _data||this.data.condition;
	      this.__getList();
	      this.data.current = 1;
	    },
	    getCurrents: function(){
	      var lists = this.data.list;
	      return lists.slice((this.data.current-1) * this.data.limit, this.data.current * this.data.limit); 
	    },
	    // update loading
	    __getList: function(){
	      this.$request(this.url, {
	    	method:'post',
	        data: this.getExtraParam(),
	        onload: function(json){
	          var result = json.result,
	            list = result.list||result;

	          this.data.total = result.total;
	          this.data.list = list;
	          this.data.currentlist = this.getCurrents();

	        }._$bind(this),
	        // test
	        onerror: function(json){
	          // @TODO: remove
	        }
	      });
	    }
  });
	
  return SizeList;

});