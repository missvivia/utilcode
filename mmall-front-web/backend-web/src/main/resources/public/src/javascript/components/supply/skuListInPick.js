/**
 * 拣货单详情中的SKU列表组件
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define([
  "text!./skuListInPick.html",
  "{pro}widget/BaseComponent.js",
  '{pro}components/pager/pager.js'
  ], function(tpl, BaseComponent, Pager){

  var PAGE_NUM = 10;


  var SkuListInPick = BaseComponent.extend({
    name: "m-skuListInPick",
    template: tpl,
    // 
    config: function(data){
      // @TODO: remove. 使用假数据
      data.lists = data.lists;
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

  return SkuListInPick;

})