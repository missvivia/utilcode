/**
 * 档期详情列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./intable1.html",
  "{pro}components/ListComponent.js",
  "{pro}widget/layer/sell.invoice.js"
  ], function(tpl, ListComponent, ExpressWin){
  var ActList = ListComponent.extend({
    url: "/src/javascript/components/sell/intable1.json",
    name: "m-actlist",
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return {status: data.status}
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    showWin: function(num){
      if(!!this.addexpressWin){
          this.addexpressWin._$recycle();
      }
      this.addexpressWin = ExpressWin._$allocate({parent:document.body,onok:this.onAddOK._$bind(this)})._$show();
    },
    onAddOK: function(){
      console.log("add");
    }
  });
  return ActList;

})