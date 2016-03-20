/**
 * 订单列表
 * author liuqing
 */

define([
  "{pro}extend/util.js",
  'util/template/jst',
  '{pro}components/ListComponent.js',
  '{pro}components/notify/notify.js',
  'text!./orderList.html?v=1.0.0.0'
  ], function(_,_t, ListComponent,notify,_html){

var URL = {
	SEARCH_ORDER : '/item/limit/order',
};
var OrderList = ListComponent.extend({
    url: URL.SEARCH_ORDER,
    template: _html,
    data: {
    	total: 1,
        current: 1,
        limit: 10,
        list: []
    },
    config: function(data){
      this.supr(data)
    },
    getListParam: function(){
        var data = this.data;
        
        return _.extend({
            limit: data.limit,
            offset: data.limit * (data.current-1),
          }, this.getExtraParam(data));
    },
    getExtraParam:function(){
          var obj = {};
          var condition = this.data.condition;
         
	  	  for(var i in condition){
	      	  var item = condition[i];
	      	  if(item){
	      		 obj[i] = item;
	      	  }
	        }
	  	  
	  	  return obj;
	    }
  });
  return OrderList;

});
