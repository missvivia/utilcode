/**
 * 档期详情列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./intable2.html",
  "{pro}components/ListComponent.js",
  "{pro}widget/layer/sell.invoice.js"
  ], function(tpl, ListComponent,ExpressWin){
  var ActList = ListComponent.extend({
    url: "/src/javascript/components/sell/intable2.json",
    name: "m-tablelist",
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return {status: data.status}
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    showWin: function(num,btntitlt){
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