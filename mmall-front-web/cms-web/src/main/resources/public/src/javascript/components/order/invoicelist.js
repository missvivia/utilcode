/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./invoicelist.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0"
  ], function(tpl, ListComponent){
  var PdList = ListComponent.extend({
    url: "/order/query/orderdetail/getInvoiceList",
    name: "m-invoicelist",
    template: tpl,
    data: {
      statusMap:{
        1: "待发货",
        2: "配送中",
        3: "已签收",
        4: "拒签"
      }
    },
    // @子类修改
    getExtraParam: function(data){
      return {
        orderId: window.__basicInfo__.orderId,
        userId:  window.__basicInfo__.userId
      }
    }
  });
  return PdList;

})