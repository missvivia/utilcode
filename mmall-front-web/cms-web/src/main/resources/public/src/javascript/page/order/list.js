/**
 * 订单列表筛选
 * author shuyuqiong
 */

define([
  "{pro}extend/util.js",
  'util/template/jst',
  '{pro}components/ListComponent.js',
  '{pro}components/notify/notify.js',
  'util/form/form'
  ], function(_,_t, ListComponent,notify,_f){

var URL = {
	SEARCH_ORDER : '/order/query/getOrderListByQueryType',
	DELIVER : '/order/deliver'
};
var OrderList = ListComponent.extend({
    url: URL.SEARCH_ORDER,
    template: "#orderListTpl",
    data: {
    	total: 1,
        current: 1,
        limit: 10,
        list: [],
        statusMap:{
            '0':'等待付款',
            '1':'待发货',
            '2':'待发货',
            '5':'待发货',
            '6':'待发货',
            '9':'已发货',
            '10':'已发货',
            '11':'交易完成',
            '20':'取消中',
            '21':'已取消',
            '25':'审核未通过(货到付款)'
          },
          payMethod : {
        	  '0' : '在线支付',		//'网易宝支付',
        	  '1' : '货到付款',		//'货到现金付款',
        	  '2' : '在线支付',		//'支付宝支付', 
        	  '3' : '货到付款'		//'货到POS机付款'
          },
          payStatus : {
        	  '20' :  '在线支付-未付款',
        	  '21' :  '货到付款-未支付',
        	  '30' :  '在线支付-已支付',
        	  '31' :  '货到付款-已支付'
          }
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
	      		  if(i == "searchKey"){
	      			  obj[condition.searchType] = item;
	      		  }else if(i == "orderColumn"){
	      			 var arr = item.split("|");
	      			 obj["orderColumn"] = arr[0];
	      			 obj["asc"] = arr[1];
	      		  }else if(i != "searchType"){
	      			  obj[i] = item;
	      		  }
	      	  }
	        }
	  	  
	  	  return obj;
	    }
  });
  return OrderList;

})
