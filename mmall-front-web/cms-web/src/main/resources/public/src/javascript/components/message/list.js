/**
 * 消息列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./list.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0"
  ], function(tpl, ListComponent){
  var MsgList = ListComponent.extend({
    url: "/src/pages/data.json",
    name: "m-msglist",
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return {status: data.status}
    },
    mark: function(msg){
      msg.important = !msg.important;
    }
  });
  return MsgList;

})