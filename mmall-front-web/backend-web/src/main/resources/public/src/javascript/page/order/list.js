/**
 * 订单列表筛选
 * author shuyuqiong
 */

define([
  "{pro}extend/util.js?v=1.0.0.0",
  '{lib}util/template/jst.js?v=1.0.0.0',
  '{pro}components/ListComponent.js?v=1.0.0.0',
  '{pro}components/modal/modal.js?v=1.0.0.0',
  '{pro}components/notify/notify.js?v=1.0.0.0',
  '{lib}util/form/form.js?v=1.0.0.0'
  ], function(_,_t, ListComponent,Modal,notify,_f){

var URL = {
	SEARCH_ORDER : '/order/searchOrder',
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
      orderStatus:{
        '0':'等待付款',
        '6':'待发货',
        '10':'已发货',
        '11' : '交易完成',
        '21':'已取消'
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
    	  if(condition.searchType == "orderId"){
    		  obj["queryType"] = "5";
    	  }else if(condition.searchType == "userName"){
    		  obj["queryType"] = "6";
    	  }
    	  return obj;
      },
      send : function(item){
    	  _t._$add('sendFormTemplate');
    	  var payMethodFormat = this.data.payMethod[item.orderFormPayMethod];
    	  item.payMethodFormat = payMethodFormat;
    	  var expressStr = document.getElementById("expressBox").innerHTML;
    	  var expressList = expressStr.substring(0,expressStr.length-1).split("|");
    	  item.expressData = [];
    	  for(var i=0; i < expressList.length;i++){
    		  var obj = {};
    		  var arr = expressList[i].split("-");
    		  obj["code"] = arr[0];
    		  obj["name"] = arr[1];
    		  item.expressData.push(obj);
    	  }
//    	  var _html = _t._$get('sendFormTemplate',{
//    	      item:item
//    	  });
//    	  
//  		  var modal = new Modal({
//          	data:{
//                'title':'发货',
//            	content : _html
//            }
//    	  });
//      	  var _form = _f._$$WebForm._$allocate({
//      		  form:'sendForm'
//		  });
//    	  modal.$on('confirm',function(){
////    		  console.log(_form._$checkValidity());
//    		  if (_form._$checkValidity()){
	    		  this.$request(URL.DELIVER,{
	    	          	method:'post',
	    	          	data : {
	    	          		"userId" : item.userId,
	    	          		"orderId" : item.orderId,
//	    	          		"mailNO" : document.getElementById("mailNO").value,
//	    	          		"expressCompany" : document.getElementById("express").value
	    	          	},
	    	          	onload:function(_json){
	    	              if(_json.code==200){
	    	                notify.show("发货成功！");
	    	                this.$emit('updatelist');
	    	              }
	    	            },
	    	            onerror:function(_json){
	    	              notify.show(_json.message);
	    	            }
		        	});
//		        	modal.destroy();
//    		  }
//	      	}.bind(this));
//	      modal.$on('close',function(){modal.destroy();}.bind(this));
      }
  });
  return OrderList;

})
