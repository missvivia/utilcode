/**
 * 发货单详情中的SKU列表组件
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define([
  "text!./skuListInInvoice.html",
  "{pro}widget/BaseComponent.js",
  '{pro}components/pager/pager.js'
  ], function(tpl, BaseComponent, Pager){

  var PAGE_NUM = 10;

  

  var SkuListInInvoice = BaseComponent.extend({
    name: "m-skuListInInvoice",
    template: tpl,
    // 
    config: function(data){
      data.lists = data.lists
      data.current = 1;
    },
    //
    init: function(){
    },
    getCurrents: function(current){
      var lists = this.data.lists;
      return lists.slice((current-1) * PAGE_NUM, current * PAGE_NUM); 
    },
  });

  return SkuListInInvoice;

})