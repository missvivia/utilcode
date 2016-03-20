/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./pdlist.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{pro}extend/config.js?v=1.0.0.0"
  ], function(tpl, ListComponent,config){
  var PdList = ListComponent.extend({
    url: "/order/query/orderDetailList",
    name: "m-productlist",
    template: tpl,
    data: {
      mainsite:config.MAINSITE
    },
    // @子类修改
    getExtraParam: function(data){
      return {
        type: window.__type__,
        key:window.__key__
      }
    },
    getPackLength: function(id){
      var length = 0,
          list = this.data.list.list;
      for(var i=0,len=list.length;i<len;i++){
        if(list[i].packageId == id){
          length++;
        }
      }
      return length;
    }
  });
  return PdList;

})