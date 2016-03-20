/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./returndetail.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0"
  ], function(tpl, ListComponent){
  var PdList = ListComponent.extend({
    url: "/order/return/getProductList",
    name: "m-productlist",
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return {
        userId: window.__userId__,
        returnId: window.__returnId__
      }
    }
  });
  return PdList;

})