/**
 * 档期管理列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./detaillist.html",
  "{pro}components/ListComponent.js"
  ], function(tpl, ListComponent){
  var ActList = ListComponent.extend({
    url: "/src/javascript/components/sell/detaillist.json",
    name: "m-actlist",
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return {status: data.status}
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    }
  });
  return ActList;

})