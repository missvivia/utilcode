/**
 * 订单页的翻页样式
 * author cheng-lin(cheng-lin@corp.netease.com)
 */
NEJ.define([
  "text!./orderpager.html",
  "pro/components/pager/pager.js?20150828"
  ], function(tpl, BasePager){

  // <pager total=3 current=1></pager>
  var OrderPager = BasePager.extend({
    name: "orderpager",
    template: tpl
  });

  return OrderPager;

})