/**
 * 商家平台-jit管理——po退供物流信息
 * author：hzzhengff(hzzhengff@corp.netease.com)
 */

define([
  "text!./poreturnlist.html",
  "{pro}widget/BaseComponent.js",
  '{pro}components/pager/pager.js'
  ], function(tpl, BaseComponent, Pager){

  var PAGE_NUM = 1;

  //@TODO: remove
//  var lists = [];
//  for(var i = 0; i< 105 ;i++){
//    lists.push({
//    	actId:i+1,
//    	actType:'新增导单提醒手机号',
//    	actPerson:'供应商管理员',
//    	actTime:1410502727855
//    })
//  }

  var SizeList = BaseComponent.extend({
    //name: "m-sizelist",
    template: tpl,  // html模板 各个字段
    // 
    config: function(data){
      // @TODO: remove. 使用假数据
      //data.lists = data.lists || lists;
      data.lists = data.lists;
      data.current = 1;
    },
    //
    init: function(){
    },
    getCurrents: function(current){
      var lists = this.data.lists;
      return lists.slice((current-1) * PAGE_NUM, current * PAGE_NUM); 
    }
  });

  return SizeList;

});