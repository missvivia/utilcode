/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./sizeTable.html",
  "{pro}widget/BaseComponent.js"
  ], function(tpl, BaseComponent){
  var SizeTable = BaseComponent.extend({
    url: "/src/javascript/components/product/size.json",
    template: tpl
    // @子类修改
  });
  return SizeTable;
})