/**
 * 档期管理列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./returndetail.html",
  "{pro}components/ListComponent.js",
  "{pro}widget/layer/return.detail.js"
  ], function(tpl, ListComponent,returndetailWin){
  var ActList = ListComponent.extend({
    url: "/src/javascript/components/schedule/returndetail.json",
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
    showWin: function(a){
      if(!!this.returnWin){
          this.returnWin._$recycle();
      }
      this.returnWin = returndetailWin._$allocate({title:a,parent:document.body,onok:this.onAddOK._$bind(this)})._$show();
    },
    onAddOK: function(){
      console.log("add");
    }
  });
  return ActList;

})